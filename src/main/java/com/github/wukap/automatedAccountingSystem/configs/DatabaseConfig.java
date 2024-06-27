package com.github.wukap.automatedAccountingSystem.configs;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig
{

    @Value("${db.ip}")
    private String dbAddress;
    @Value("${db.name}")
    private String dbName;

    @Value("${db.username}")
    private String dbUsername;

    @Value("${db.password}")
    private String dbPassword;
    @Value("${db.connectionTimeout}")
    private int dbConnectionTimeout;

    @Bean
    public HikariDataSource hikariSqlDataSource()
    {
        return new HikariDataSource(
                new HikariConfig()
                {{
                    setJdbcUrl("jdbc:sqlserver://" + dbAddress + ";databaseName=" + dbName);
                    setUsername(dbUsername);
                    setPassword(dbPassword);
                    setMaximumPoolSize(Runtime.getRuntime().availableProcessors());
                    //setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
                    setConnectionTimeout(dbConnectionTimeout);
                    setValidationTimeout(10000);
                    setIdleTimeout(10000);
                    setConnectionTestQuery("select 1");
                }}
        );
    }
}