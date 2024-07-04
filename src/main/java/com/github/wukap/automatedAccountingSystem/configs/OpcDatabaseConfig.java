package com.github.wukap.automatedAccountingSystem.configs;

import com.github.wukap.automatedAccountingSystem.driver.opcDriver.OpcUaDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpcDatabaseConfig {
    @Value("${opc.db.url}")
    private String dbUrl;
    @Value("${opc.db.namespace}")
    private int dbNamespace;

    @Value("${opc.db.prefix}")
    private String dbPrefix;

    @Bean
    public OpcUaDriver.OpcUaServerConnectionInfo connectionsInfo() {
        return new OpcUaDriver.OpcUaServerConnectionInfo(dbUrl, dbNamespace, dbPrefix);
    }


}
