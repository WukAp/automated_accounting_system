package com.github.wukap.automatedAccountingSystem.configs;

import com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.BdrvDriver;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class BdrvDatabaseConfig {
    @Autowired
    private EntityManagerFactoryBuilder entityManagerFactoryBuilder;
    @Value("${bdrv.db.ip}")
    private String dbAddress;
    @Value("${bdrv.db.name}")
    private String dbName;

    @Value("${bdrv.db.username}")
    private String dbUsername;

    @Value("${bdrv.db.password}")
    private String dbPassword;
    @Value("${bdrv.db.connectionTimeout}")
    private int dbConnectionTimeout;

    @Value("${bdrv.db.maxLifetime}")
    private int dbMaxLifetime;
    @Value("${bdrv.db.maximumPoolSize}")
    private int dbMaximumPoolSize;
    @Value("${bdrv.db.minimumIdle}")
    private int dbMinimumIdle;


    private HikariDataSource getDataSource() {
        // Implement logic to dynamically create and return DataSource
        return null; // Placeholder, implement as needed
    }

    @Bean
    public BdrvDriver.BdrvConnectionInfo bdrvConnectionInfo() {
        return new BdrvDriver.BdrvConnectionInfo("jdbc:sqlserver://" + dbAddress + ";databaseName=" + dbName, dbUsername, dbPassword, dbConnectionTimeout, dbMaxLifetime, dbMaximumPoolSize, dbMinimumIdle);
    }

}