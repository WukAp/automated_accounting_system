package com.github.wukap.automatedAccountingSystem.Driver.BdrvDriver;

import com.github.wukap.automatedAccountingSystem.Driver.ESDriver;
import com.github.wukap.automatedAccountingSystem.Query.BdrvQuery.BdrvQuery;
import com.github.wukap.automatedAccountingSystem.Query.BdrvQuery.SpMsnStatusSet;
import com.github.wukap.automatedAccountingSystem.model.BdrvValue.BdrvValue;
import com.github.wukap.automatedAccountingSystem.model.BdrvValue.SpMsnStatusValue;
import com.github.wukap.automatedAccountingSystem.model.ESValue;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
@Service
public class BdrvDriver extends ESDriver<BdrvValue> {
    @Autowired
    @Qualifier("hikariSqlDataSource")
    private HikariDataSource dataSource;

    public BdrvDriver() {
    }

    @Override
    protected ESValue read_(String tagname) {
        throw new UnsupportedOperationException();
    }


    @Override
    protected void write_(String tagname, BdrvValue data) {
        //var writeonlyQuery = writeOnlyQueries.get(0).stream().findAny().orElse(null);
        //writeWriteOnly(new SpMsrValueQuery(data));
        //writeWriteOnly(new SpTransactionQuery((SpTransactionValue) data));
        writeWriteOnly(new SpMsnStatusSet((SpMsnStatusValue) data));
    }

    @SneakyThrows
    private void writeWriteOnly(BdrvQuery query) {
        log.info("Try execute SQL: " + query.getQuery());

        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            var result = statement.executeQuery(query.getQuery());
            System.out.println("SQL query executed successfully. Result: " + result);
            // Execute the query

        } catch (SQLException e) {
            System.out.println("SQL query execution failed: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean isInput() {
        return false;
    }

    @Override
    public boolean isOutput() {
        return true;
    }
}
