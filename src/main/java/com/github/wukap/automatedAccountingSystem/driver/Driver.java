package com.github.wukap.automatedAccountingSystem.driver;

import com.github.wukap.automatedAccountingSystem.common.CustomThreadFactory;
import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public abstract class Driver<R, V> {

    public static final String DRIVER_TAG = "driver";

    private ExecutorService driverExecutorService;
    @Getter
    private final String name = getClass().getSimpleName();


    @Getter
    private boolean isStarted;

    protected void setIsStarted(boolean isStarted) {
        this.isStarted = isStarted;
    }

    protected abstract R read_(String tagname);

    protected abstract void write_(V value);


    public void write( V value) {

        try {
            write_(value);

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
