package com.github.wukap.automatedAccountingSystem.driver.bdrvDriver;

import com.github.wukap.automatedAccountingSystem.driver.Driver;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.BdrvValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsrValue;
import com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.query.BdrvQuery;
import com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.query.SpMsrValueQuery;
import com.github.wukap.automatedAccountingSystem.model.config.InputConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ScheduledExecutorService;

@Slf4j
@Service
public class BdrvDriver extends Driver<Object, BdrvValue> {
    @Autowired
    @Qualifier("bdrvDataSource")
    private HikariDataSource dataSource;
    private ScheduledExecutorService executorService;
    @Autowired
    private InputConfig inputConfig;

    protected void start_() {
        if (isStarted()) return;
        setIsStarted(true);
        if (executorService != null) executorService.shutdown();

    }

    public void stop() {
        setIsStarted(false);
        executorService.shutdown();
        dataSource.close();
    }

    @Override
    protected Object read_(String tagname) {
        throw new UnsupportedOperationException();
    }


    @Override
    protected void write_(BdrvValue data) {
        //var writeonlyQuery = writeOnlyQueries.get(0).stream().findAny().orElse(null);
        //writeWriteOnly(new SpMsrValueQuery(data));
        //writeWriteOnly(new SpTransactionQuery((SpTransactionValue) data));
        writeWriteOnly(new SpMsrValueQuery((SpMsrValue) data, inputConfig.getSettings().getFfc_id()));
    }

    @SneakyThrows
    private void writeWriteOnly(BdrvQuery query) {
        log.info("Try execute SQL: " + query.getQuery());

        try (Connection connection = dataSource.getConnection()){
            Statement statement = connection.createStatement();
            var result = statement.executeQuery(query.getQuery());
            log.trace("SQL query executed successfully. Result: " + result);
            // Execute the query

        } catch (SQLException e) {
            log.trace("SQL query execution failed: " + e.getMessage());
            throw e;
        }

    }

}
