package com.github.wukap.automatedAccountingSystem;

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
        if (args.length < 2) {
            System.out.println("Usage: java -jar your-jar-file.jar  --config.path=<path-to-inputConfig.xml> --spring.config.location=file:/path/to/application.properties");
            System.exit(1);
        }
        var app = SpringApplication.run(Application.class, args);

        var scheduler = app.getBean(Scheduler.class);
        scheduler.start();

    }
}
