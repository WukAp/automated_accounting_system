package com.github.wukap.automatedAccountingSystem.driver.bdrvDriver;

import com.github.wukap.automatedAccountingSystem.driver.Driver;
import com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.query.BdrvQuery;
import com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.query.SpMsnStatusSetQuery;
import com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.query.SpMsrValueQuery;
import com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.query.SpTransactionQuery;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.BdrvValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsnStatusSetValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsrValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpTransactionValue;
import com.github.wukap.automatedAccountingSystem.model.config.InputConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
@Service
public class BdrvDriver extends Driver<Object, BdrvQuery> {
    @Autowired
    @Qualifier("bdrvDataSource")
    private HikariDataSource dataSource;
    private ScheduledExecutorService executorService;
    private final InputConfig inputConfig;
    private final int dbConnectionTimeout;

    @Autowired
    public BdrvDriver(InputConfig inputConfig, @Value("${bdrv.db.connectionTimeout}") int dbConnectionTimeout) {
        this.inputConfig = inputConfig;
        this.dbConnectionTimeout = dbConnectionTimeout;
    }


    @Override
    protected Object read(String tagname) {
        throw new UnsupportedOperationException();
    }

    public boolean isNetworkConnected() {
        try {
            return dataSource.getConnection().isValid(dbConnectionTimeout);
        } catch (SQLException e) {
            return false;
        }
    }

    public boolean writeValue(SpMsrValue data) throws SQLException {
        return write(new SpMsrValueQuery(data, inputConfig.getSettings().getFfcId()));
    }

    public boolean writeValue(SpTransactionValue data) throws SQLException {
        return write(new SpTransactionQuery(data, inputConfig.getSettings().getFfcId()));
    }

    public boolean writeValue(SpMsnStatusSetValue data) throws SQLException {
        return write(new SpMsnStatusSetQuery(data, inputConfig.getSettings().getFfcId()));
    }

    public boolean writeValue(BdrvValue data) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected boolean write_(BdrvQuery data) throws SQLException {
        return writeWriteOnly(data);
    }

    @SneakyThrows
    public boolean writeWriteOnly(BdrvQuery query) throws SQLException {
        log.info("Try execute SQL: " + query.getQuery());

        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            var result = statement.executeUpdate(query.getQuery());

            return true;
        } catch (SQLException e) {
            log.info("SQL query execution failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            log.info("SQL query execution failed: " + e.getMessage());
        }
        return false;
    }

}
