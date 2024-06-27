package com.github.wukap.automatedAccountingSystem;

import com.github.wukap.automatedAccountingSystem.sqlDriver.SqlData;
import com.github.wukap.automatedAccountingSystem.sqlDriver.SqlOutputDriver;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

/**
 * Main class for Java Repository Template.
 */
@SpringBootApplication
@Slf4j
public class Application
{
    @Autowired
    public static final String JAVA_REPOSITORY_TEMPLATE = "maven-template-repository";

    public static void main(String[] args) throws InterruptedException
    {

        var app = SpringApplication.run(Application.class, args);
        var sqlOutputDriver = app.getBean(SqlOutputDriver.class);
        Thread.sleep(3000);
        sqlOutputDriver.write("test", new SqlData(23, 105, 9520, "08.10.2015 20:00:00"));

    }
}
