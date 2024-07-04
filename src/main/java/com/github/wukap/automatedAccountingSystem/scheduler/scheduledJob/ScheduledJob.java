package com.github.wukap.automatedAccountingSystem.scheduler.scheduledJob;

public interface ScheduledJob extends Runnable {
    @Override
    public void run();
}
