package com.github.wukap.automatedAccountingSystem;

import lombok.Getter;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
@Slf4j
public abstract class ESDriver<T>
{

    private ExecutorService driverExecutorService;
    @Getter
    private final String name = getClass().getSimpleName();
    public abstract boolean isInput();

    public abstract boolean isOutput();
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
            //TODO
            //driverExecutorService = Executors.newFixedThreadPool(threadCount, new CustomThreadFactory("common-driver-executor"));
        }
        return driverExecutorService;
    }
}
