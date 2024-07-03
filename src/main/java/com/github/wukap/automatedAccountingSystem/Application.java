package com.github.wukap.automatedAccountingSystem;

import com.github.wukap.automatedAccountingSystem.driver.asutpDriver.OpcUaDriver;
import com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.BdrvDriver;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsnStatusSetValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsrValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpTransactionValue;
import com.github.wukap.automatedAccountingSystem.model.config.InputConfig;
import com.github.wukap.automatedAccountingSystem.ringBufferDatabase.SpMsnStatusSetValueRepository;
import com.github.wukap.automatedAccountingSystem.ringBufferDatabase.SpMsrValueRepository;
import com.github.wukap.automatedAccountingSystem.ringBufferDatabase.SpTransactionValueRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class for Java Repository Template.
 */
@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) throws InterruptedException {

        var app = SpringApplication.run(Application.class, args);
        var sqlOutputDriver = app.getBean(BdrvDriver.class);
        var config = app.getBean(InputConfig.class);
        System.out.println(app.getBean(InputConfig.class));
//        Thread.sleep(3000);
        sqlOutputDriver.write("test", new SpMsrValue(23, 105, 9520, "08.10.2015 20:00:00"));
        //sqlOutputDriver.write("test", new SpTransactionValue(19, 0, 10701, "07.10.2015 18:16:25"));
        //sqlOutputDriver.write("test", new SpMsnStatusValue(19, 1, 1, "07.10.2015 18:16:25"));
        var val = app.getBean(SpMsrValueRepository.class);
        val.save(new SpMsrValue(10, 20, 30, "Initial Data"));
        val.save(new SpMsrValue(10, 20, 30, "Initial Data"));
        val.save(new SpMsrValue(10, 20, 30, "Initial Data"));
        val.save(new SpMsrValue(10, 20, 30, "Initial Data"));
        val.findAll().forEach(System.out::println);

        val.findAll().forEach(System.out::println);
        System.out.println("----");
        val.save(new SpMsrValue(10, 20, 30, "Initial Data"));
        var vaf = app.getBean(SpTransactionValueRepository.class);
        vaf.save(new SpTransactionValue(10, 20, 30, "Initial Data"));
        var val2 = app.getBean(SpMsnStatusSetValueRepository.class);
        val2.save(new SpMsnStatusSetValue(10, 20, 30, "Initial Data"));

        val.findAll().forEach(System.out::println);
        var statistic = app.getBean(StatisticService.class);
        System.out.println(statistic.getBufferedValueAmount());
        var opc = app.getBean(OpcUaDriver.class);
        opc.read_("//ObjectRoot/ASUTP/Cycle_Data/Value2");
        //opc.write("test", new SpTransactionValue(19, 0, 10701, "07.10.2015 18:16:25"));
        //opc.write("test", new SpMsnStatusValue(19, 1, 1, "07.10.2015 18:16:25"));
    }
}
