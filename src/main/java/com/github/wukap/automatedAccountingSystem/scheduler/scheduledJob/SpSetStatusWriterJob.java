package com.github.wukap.automatedAccountingSystem.scheduler.scheduledJob;

import com.github.wukap.automatedAccountingSystem.statistic.StatisticService;
import com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.BdrvDriver;
import com.github.wukap.automatedAccountingSystem.h2Database.SpMsnStatusSetValueRepository;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsnStatusSetValue;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@Getter
public class SpSetStatusWriterJob extends BdrvWriterSchedulerJob<SpMsnStatusSetValueRepository, SpMsnStatusSetValue> {
    private final int delay;

    public SpSetStatusWriterJob(BdrvDriver bdrvDriver, SpMsnStatusSetValueRepository valueRepository, StatisticService statisticService, @Value("${sp_msn_status_value_writing_in_seconds}") int delay) {
        super(bdrvDriver, valueRepository, statisticService);
        this.delay = delay;
    }

    @Override
    public TimeUnit getDelayTimeUnit() {
        return TimeUnit.SECONDS;
    }
}