package com.github.wukap.automatedAccountingSystem.driver.opcDriver;

public interface ConnectionFactory<I, C> {
    C getActiveConnection();

    void closeConnection();

    I getActiveConnectionInfo();
}
