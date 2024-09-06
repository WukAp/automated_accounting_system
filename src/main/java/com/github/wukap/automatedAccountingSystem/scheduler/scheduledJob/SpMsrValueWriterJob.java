package com.github.wukap.automatedAccountingSystem.scheduler.scheduledJob;

import com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.BdrvDriver;
import com.github.wukap.automatedAccountingSystem.h2Database.SpMsrValueRepository;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsrValue;
import com.github.wukap.automatedAccountingSystem.statistic.StatisticService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Getter
@Slf4j
@Component
public class SpMsrValueWriterJob extends BdrvWriterSchedulerJob<SpMsrValueRepository, SpMsrValue> {
    private final int delay;

    public SpMsrValueWriterJob(BdrvDriver bdrvDriver, SpMsrValueRepository valueRepository, StatisticService statisticService, @Value("${sp_msr_value_writing_in_seconds}") int delay) {
        super(bdrvDriver, valueRepository, statisticService);
        this.delay = delay;
    }

    @Override
    public void run() {
        try {
            if (isRepositoryEmpty()) {
                return;
            }
            List<SpMsrValue> values = valueRepository.findMinTimeForEachMsdId();
            for (SpMsrValue value : values) {
                log.debug(value.toString() + " is going to be written");
                boolean result = false;
                try {
                    result = bdrvDriver.writeValue((SpMsrValue) value);
                } catch (SQLException e) {
                    log.error("Can't write " + value + " to BDRV because of SQL exception");
                    valueRepository.delete(value);
                    statisticService.addThrownLog((SpMsrValue) value);
                }
                if (result) {
                    log.info(value + " was successfully written");
                    valueRepository.delete(value);
                    statisticService.addWrittenLog((SpMsrValue) value);
                } else {
                    log.warn(value + " was not written, something went wrong");
                }
            }

        } catch (Exception e) {
            log.error(e.getMessage());
        }


    }

    @Override
    public TimeUnit getDelayTimeUnit() {
        return TimeUnit.SECONDS;
    }
}
