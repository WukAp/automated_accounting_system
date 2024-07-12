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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@Service
public class BdrvDriver extends Driver<Object, BdrvQuery> {
    private final BdrvConnectionFactory connectionFactory;
    private final InputConfig inputConfig;

    @Autowired
    public BdrvDriver(InputConfig inputConfig, BdrvConnectionInfo bdrvConnectionInfo) {
        this.inputConfig = inputConfig;
        connectionFactory = new BdrvConnectionFactory(bdrvConnectionInfo);
    }


    @Override
    protected Object read(String tagname) {
        throw new UnsupportedOperationException();
    }

    public boolean isNetworkConnected() {
        return connectionFactory.isNetworkConnected();
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

        try (Connection connection = connectionFactory.getActiveConnection()) {
            Statement statement = connection.createStatement();
            var result = statement.executeUpdate(query.getQuery());

            return true;
        } catch (SQLException e) {
            log.warn("SQL query execution failed: " + e.getMessage());
            throw e;
        } catch (CannotGetJdbcConnectionException e) {
            log.warn("Cannot get JDBC connection: " + e.getMessage());
        } catch (Exception e) {
            log.error("Exception while writing: " + e.getMessage());
        }
        return false;
    }

    @AllArgsConstructor
    @Getter
    public static class BdrvConnectionInfo {
        @NonNull
        private String dbUrl;
        @NonNull
        private String dbUsername;
        @NonNull
        private String dbPassword;
        @NonNull
        private int dbConnectionTimeout;
        @NonNull
        private int dbMaxLifetime;
        @NonNull
        private int dbMaximumPoolSize;
        @NonNull
        private int dbMinimumIdle;
    }
}
