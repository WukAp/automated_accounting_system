package com.github.wukap.automatedAccountingSystem;

import com.github.wukap.automatedAccountingSystem.DriverSupervisor;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

@Slf4j
public abstract class ESDriver {
    public static final String DRIVER_TAG = "driver";
    private static AtomicInteger driverCounter = new AtomicInteger();
    @Getter
    private final String name = getClass().getSimpleName() + "-" + driverCounter.incrementAndGet();
    public TagValue failedTagValue;
    protected DriverSupervisor driverSupervisor;
    protected MeterRegistry meterRegistry;
    @Getter
    protected boolean started;
    private Counter readsCounter;
    private Counter writesCounter;
    public Counter writeErrorsCounter;
    private Counter retriesCounter;
    private Counter retryErrorsCounter;
    private ExecutorService driverExecutorService;

    public ESDriver(DriverSupervisor driverSupervisor, @Nullable MeterRegistry meterRegistry) {
        this.driverSupervisor = driverSupervisor;
        this.meterRegistry = meterRegistry;
        if (meterRegistry != null) {
            this.readsCounter = meterRegistry.counter("es.driver.reads.count", DRIVER_TAG, name);
            this.writesCounter = meterRegistry.counter("es.driver.writes.count", DRIVER_TAG, name);
            this.writeErrorsCounter = meterRegistry.counter("es.driver.write.errors.count", DRIVER_TAG, name);
            this.retriesCounter = meterRegistry.counter("es.driver.retries.count", DRIVER_TAG, name);
            this.retryErrorsCounter = meterRegistry.counter("es.driver.retry.errors.count", DRIVER_TAG, name);
        }
    }

    public abstract boolean isInput();

    protected abstract void start_();

    public abstract void stop();

    protected abstract void listen_(String tagname, Function<ESValue, Boolean> callback);

    protected abstract ESValue read_(String tagname);

    protected abstract void write_(String tagname, ESValue value);

    public boolean isOutput() {
        return !isInput();
    }

    public void start() {
        checkSupervisor();
        if (isStarted()) {
            return;
        }
        try {
            start_();
            log.info("Driver {} was started", getName());
        } catch (Exception e) {
            log.error("Exception while starting driver " + getName(), e);
            driverSupervisor.reportFailure(this);
        }
    }

    /**
     * @param tagname
     * @param callback should return false for stop listening
     */
    public void listen(String tagname, Function<ESValue, Boolean> callback) {
        checkSupervisor();
        listen_(tagname, v -> {
            Boolean result = callback.apply(v);
            if (readsCounter != null) {
                readsCounter.increment();
            }
            return result;
        });
    }

    public ESValue read(String tagname) {
        checkSupervisor();
        ESValue value = read_(tagname);
        if (readsCounter != null) {
            readsCounter.increment();
        }
        return value;
    }

    public void write(String tagname, ESValue value) {
        checkSupervisor();
        try {
            write_(tagname, value);
            if (writesCounter != null) {
                writesCounter.increment();
            }
        } catch (Exception e) {
            log.error("Exception while writing to driver " + getName(), e);
            if (writeErrorsCounter != null) {
                writeErrorsCounter.increment();
            }
            failedTagValue = new TagValue(tagname, value);
            driverSupervisor.reportFailure(this);
        }
    }

    @Synchronized
    public boolean retryStore() {
        if (failedTagValue != null) {
            try {
                write_(failedTagValue.getTagname(), failedTagValue.getValue());
                log.debug("Retry was completed in driver " + getName());
                if (retriesCounter != null) {
                    retriesCounter.increment();
                }
                failedTagValue = null;
                return true;
            } catch (Exception e) {
                log.error("Retry failed in driver " + getName(), e);
                if (retryErrorsCounter != null) {
                    retryErrorsCounter.increment();
                }
                return false;
            }
        }
        return true;
    }

    private void checkSupervisor() {
        if (!driverSupervisor.canProcess()) {
            throw new IllegalStateException("Driver " + getName() + " can't process values. There is some problem in output drivers.");
        }
    }

    @Synchronized
    public ExecutorService getDriverExecutor(int threadCount) {
        if (driverExecutorService == null) {
            driverExecutorService = Executors.newFixedThreadPool(threadCount, new CustomThreadFactory("common-driver-executor"));
        }
        return driverExecutorService;
    }

    @Getter
    @AllArgsConstructor
    public static class TagValue {
        private String tagname;
        private ESValue value;
    }
}
