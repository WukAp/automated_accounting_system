package com.github.wukap.automatedAccountingSystem.configs;


import com.github.wukap.automatedAccountingSystem.model.config.InputConfig;
import com.github.wukap.automatedAccountingSystem.utils.XmlConfigParserUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.util.HashMap;

@Configuration
@ComponentScan("com.github.wukap.automatedAccountingSystem")

public class AppConfig {
    @Value("${config.path}")
    private String fileLocation;

    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }

    @Bean
    public InputConfig esConfig() {
        //TODO: write file location from args
        return XmlConfigParserUtils.parse(fileLocation);
    }
}
