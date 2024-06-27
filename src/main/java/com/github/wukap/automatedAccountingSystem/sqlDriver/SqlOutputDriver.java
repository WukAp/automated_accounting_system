package com.github.wukap.automatedAccountingSystem.sqlDriver;

import com.github.wukap.automatedAccountingSystem.ESDriver;
import com.github.wukap.automatedAccountingSystem.ESValue;
import com.zaxxer.hikari.HikariDataSource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
@Service
public class SqlOutputDriver
        extends ESDriver<SqlData>
{
    @Autowired
    @Qualifier("hikariSqlDataSource")
    private HikariDataSource dataSource;

    public SqlOutputDriver()
    {

    }

    @Override
    protected ESValue read_(String tagname)
    {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void write_(String tagname, SqlData data)
    {
        //var writeonlyQuery = writeOnlyQueries.get(0).stream().findAny().orElse(null);
        var query = "exec sp_msr_value_send @p_ffc_id=?, @p_msd_id=?, @p_msr_value=?, @p_msr_time=?";
        writeWriteOnly(query, data);
    }

    @SneakyThrows
    private void writeWriteOnly(String query, SqlData data)
    {
        log.info("Try execute SQL: " + data.toString());

            try
            {
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(query);

                // Установка параметров
                statement.setInt(1, data.pFfcId());
                statement.setInt(2, data.pMsdId());
                statement.setInt(3, data.pMsrValue());
                statement.setString(4, data.pMsrTime());
                //statement.setQueryTimeout(3); // Set query timeout to 10 seconds
                // Create a separate thread to handle query timeout
                var result = statement.executeQuery();

                System.out.println("SQL query executed successfully. Result: " + result);

                // Execute the query

            } catch (SQLException e)
            {
                System.out.println("SQL query execution failed: " + e.getMessage());
                throw e;
            }

    }

    @Override
    public boolean isInput()
    {
        return false;
    }

    @Override
    public boolean isOutput()
    {
        return true;
    }




}
