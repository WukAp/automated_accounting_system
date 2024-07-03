package com.github.wukap.automatedAccountingSystem.driver.asutpDriver;

public interface ConnectionFactory<I, C> {
    C getActiveConnection();

    void closeConnection();

    I getActiveConnectionInfo();
}
