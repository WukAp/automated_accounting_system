package com.github.wukap.automatedAccountingSystem.driver.bdrvDriver;

import com.github.wukap.automatedAccountingSystem.driver.opcDriver.ConnectionFactory;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
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
        if(!isInit) {
            log.warn("Can't connect to database");
            throw new IllegalStateException("Can't init HikariDataSource");
        }
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
        if (dataSource == null) return false;
        try (Connection connection = dataSource.getConnection()) {
            if (connection.isValid(bdrvConnectionInfo.getDbConnectionTimeout())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private void initHikariDataSourceIfNotExist() {

        if (!isInit) {
            try {
                initHikariDataSource();
                isInit = true;
            } catch (Exception e) {
                log.error("Can't init HikariDataSource", e);
            }
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

