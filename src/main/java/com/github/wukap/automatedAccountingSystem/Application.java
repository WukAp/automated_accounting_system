package com.github.wukap.automatedAccountingSystem;

import com.github.wukap.automatedAccountingSystem.h2Database.SpMsnStatusSetValueRepository;
import com.github.wukap.automatedAccountingSystem.h2Database.SpMsrValueRepository;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsnStatusSetValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsrValue;
import com.github.wukap.automatedAccountingSystem.scheduler.Scheduler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static com.github.wukap.automatedAccountingSystem.utils.OpcValueToBdrvValueConverter.instantToFormattedStringConverter;
import static java.lang.System.exit;

/**
 * Main class for Java Repository Template.
 */
@SpringBootApplication
@Slf4j
public class Application {

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            System.out.println("Usage: java -jar your-jar-file.jar  --config.path=<path-to-inputConfig.xml> --spring.config.location=file:/path/to/application.properties");
            exit(1);
        }
        var app = SpringApplication.run(Application.class, args);
//        var repo = app.getBean(SpMsrValueRepository.class);
//        var repo1 = app.getBean(SpMsnStatusSetValueRepository
//
//                .class);
//        var nowTime = Instant.now();
//        for (int i = 0; i < 1000; i++)
//        {
//            String varTime = instantToFormattedStringConverter(nowTime.plusSeconds(i));
//            repo.save(new SpMsrValue(String.valueOf(i), "105", varTime, "//ObjectRoot/ASUTP/Cycle_Data/Value1"));
//            repo.save(new SpMsrValue(String.valueOf(i), "104", varTime, "//ObjectRoot/ASUTP/Cycle_Data/Value2"));
//            repo.save(new SpMsrValue(String.valueOf(i), "103", varTime, "//ObjectRoot/ASUTP/Cycle_Data/Value3"));
//        }
//        String varTime = instantToFormattedStringConverter(Instant.now());
//        repo1.save(new SpMsnStatusSetValue("01", "2", varTime, "1055"));

       // repo.save(new SpMsrValue("234", "9520", "02.07.2024 20:02:00", "1055"));
        var scheduler = app.getBean(Scheduler.class);
        scheduler.start();
    }
}
