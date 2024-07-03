package com.github.wukap.automatedAccountingSystem.driver;

import com.github.wukap.automatedAccountingSystem.common.CustomThreadFactory;
import com.github.wukap.automatedAccountingSystem.model.ESValue;
import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public abstract class Driver<T> {

    public static final String DRIVER_TAG = "driver";

    private ExecutorService driverExecutorService;
    @Getter
    private final String name = getClass().getSimpleName();


    @Getter
    private boolean isStarted;

    protected void setIsStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    protected abstract ESValue read_(String tagname);

    protected abstract void write_(String tagname, T value);

    public ESValue read(String tagname) {

        ESValue value = read_(tagname);

        return value;
    }

    public void write(String tagname, T value) {

        try {
            write_(tagname, value);

        } catch (Exception e) {
            log.error("Exception while writing to driver " + getName(), e);


        }
    }

    @Synchronized
    public ExecutorService getDriverExecutor(int threadCount) {
        if (driverExecutorService == null) {
            driverExecutorService = Executors.newFixedThreadPool(threadCount, new CustomThreadFactory("common-driver-executor"));
        }
        return driverExecutorService;
    }
}
