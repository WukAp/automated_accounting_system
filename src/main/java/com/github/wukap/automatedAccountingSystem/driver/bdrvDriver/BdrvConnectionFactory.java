package com.github.wukap.automatedAccountingSystem.driver.bdrvDriver;

import com.github.wukap.automatedAccountingSystem.driver.opcDriver.ConnectionFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class BdrvConnectionFactory implements ConnectionFactory<BdrvDriver.BdrvConnectionInfo, Connection> {

    private final BdrvDriver.BdrvConnectionInfo bdrvConnectionInfo;
    private boolean isInit;
    private HikariDataSource dataSource;

    public BdrvConnectionFactory(BdrvDriver.BdrvConnectionInfo bdrvConnectionInfo) {
        isInit = false;
        this.bdrvConnectionInfo = bdrvConnectionInfo;
    }

    @Override
    public Connection getActiveConnection() throws SQLException {
        initHikariDataSourceIfNotExist();
        // Implement logic to get active connection using HikariDataSource
        return dataSource.getConnection(); // Placeholder, implement as needed
    }

    @Override
    public void closeConnection() {
        throw new UnsupportedOperationException();
    }

    @Override
    public BdrvDriver.BdrvConnectionInfo getActiveConnectionInfo() {
        return this.bdrvConnectionInfo;
    }

    public boolean isNetworkConnected() {
        initHikariDataSourceIfNotExist();
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(bdrvConnectionInfo.getDbConnectionTimeout())) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    private void initHikariDataSourceIfNotExist() {
        if (!isInit) {
            initHikariDataSource();
            isInit = true;
        }
    }

    private void initHikariDataSource() {
        HikariDataSource dataSource = new HikariDataSource(new HikariConfig() {{

            setJdbcUrl(bdrvConnectionInfo.getDbUrl());
            setUsername(bdrvConnectionInfo.getDbUsername());
            setPassword(bdrvConnectionInfo.getDbPassword());
            setMinimumIdle(bdrvConnectionInfo.getDbMinimumIdle());
            setMaximumPoolSize(bdrvConnectionInfo.getDbMaximumPoolSize());
            setMaxLifetime(bdrvConnectionInfo.getDbMaxLifetime());
            //setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
            setConnectionTimeout(bdrvConnectionInfo.getDbConnectionTimeout());
            setConnectionTestQuery("SELECT 1");
        }});

        this.dataSource = dataSource;
    }
}

