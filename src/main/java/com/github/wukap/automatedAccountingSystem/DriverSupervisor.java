package com.github.wukap.automatedAccountingSystem;

public interface DriverSupervisor {
    boolean canProcess();

    void reportFailure(ESDriver failedDriver);
}