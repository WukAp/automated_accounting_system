package com.github.wukap.automatedAccountingSystem.driver.opcDriver;

import java.sql.SQLException;

public interface ConnectionFactory<I, C> {
    C getActiveConnection() throws SQLException;

    void closeConnection();

    I getActiveConnectionInfo();
}
