package com.github.wukap.automatedAccountingSystem.configs;


import com.github.wukap.automatedAccountingSystem.model.config.ESConfig;
import com.github.wukap.automatedAccountingSystem.utils.XmlConfigParserUtils;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import java.io.IOException;
import java.util.HashMap;

@Configuration
@ComponentScan("com.github.wukap.automatedAccountingSystem")
public class AppConfig
{
    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
    }
    @Bean
    public ESConfig esConfig() throws IOException
    {
        //TODO: write file location from args
        String fileLocation = "inputConfig.xml";
        return XmlConfigParserUtils.parse(fileLocation);
    }
}
