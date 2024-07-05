package com.github.wukap.automatedAccountingSystem;

import com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.BdrvDriver;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsrValue;
import com.github.wukap.automatedAccountingSystem.ringBufferDatabase.SpMsrValueRepository;
import com.github.wukap.automatedAccountingSystem.scheduler.Scheduler;
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
        var repo = app.getBean(SpMsrValueRepository.class);
        repo.save(new SpMsrValue("23", "9520", "105", "03.06.2024 20:01:00"));
        repo.save(new SpMsrValue("243", "9520", "105", "03.06.2024 20:01:00"));
        repo.save(new SpMsrValue("263", "9520", "105", "03.06.2024 20:01:00"));
        repo.save(new SpMsrValue("223", "9520", "105", "05.06.2024 20:01:00"));
        repo.save(new SpMsrValue("263", "9520", "105", "05.06.2024 20:01:00"));
        repo.save(new SpMsrValue("234", "9520", "1055", "02.06.2024 20:02:00"));
        var scheduler = app.getBean(Scheduler.class);
        scheduler.start();
        repo.findAll().forEach(System.out::println);
        var statisticService = app.getBean(StatisticService.class);
        var bdrvDriver = app.getBean(BdrvDriver.class);
while (true) {
    Thread.sleep(1000);
    repo.findAll().forEach(System.out::println);
    System.out.println("------------");
}
    }
}
