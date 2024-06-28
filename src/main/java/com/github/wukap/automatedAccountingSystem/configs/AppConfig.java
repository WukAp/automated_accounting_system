package com.github.wukap.automatedAccountingSystem.configs;


import com.github.wukap.automatedAccountingSystem.model.config.ESConfig;
import com.github.wukap.automatedAccountingSystem.parserUtils.XmlConfigParserUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@ComponentScan("com.github.wukap.automatedAccountingSystem")
public class AppConfig
{
    @Bean
    public ESConfig esConfig() throws IOException
    {
        //TODO: write file location from args
        String fileLocation = "inputConfig.xml";
        return XmlConfigParserUtils.parse(fileLocation);
    }
}
