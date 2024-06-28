package com.github.wukap.automatedAccountingSystem;

import com.github.wukap.automatedAccountingSystem.model.BdrvValue.SpMsnStatusValue;
import com.github.wukap.automatedAccountingSystem.model.config.ESConfig;
import com.github.wukap.automatedAccountingSystem.Driver.BdrvDriver.BdrvDriver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for Java Repository Template.
 */
@SpringBootApplication
@Slf4j
public class Application
{

    public static void main(String[] args) throws InterruptedException
    {

        var app = SpringApplication.run(Application.class, args);
        var sqlOutputDriver = app.getBean(BdrvDriver.class);
        var config = app.getBean(ESConfig.class);
        System.out.println(app.getBean(ESConfig.class));
//        Thread.sleep(3000);
        //sqlOutputDriver.write("test", new SpMsrValue(23, 105, 9520, "08.10.2015 20:00:00"));
        //sqlOutputDriver.write("test", new SpTransactionValue(19, 0, 10701, "07.10.2015 18:16:25"));
        sqlOutputDriver.write("test", new SpMsnStatusValue(19, 1, 1, "07.10.2015 18:16:25"));


    }
}
