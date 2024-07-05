package com.github.wukap.automatedAccountingSystem.scheduler.scheduledJob;

import java.util.concurrent.TimeUnit;

public interface ScheduledJob extends Runnable {


    void run();

    Type getType();

    int getDelay();

    TimeUnit getDelayTimeUnit();

    enum Type {
        RATE, DELAY
    }
}
