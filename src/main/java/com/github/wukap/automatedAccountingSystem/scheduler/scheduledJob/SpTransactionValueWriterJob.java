package com.github.wukap.automatedAccountingSystem.scheduler.scheduledJob;

import com.github.wukap.automatedAccountingSystem.StatisticService;
import com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.BdrvDriver;
import com.github.wukap.automatedAccountingSystem.h2Database.SpTransactionValueRepository;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpTransactionValue;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

@Getter
public class SpTransactionValueWriterJob extends BdrvWriterSchedulerJob<SpTransactionValueRepository, SpTransactionValue> {
    private final int delay;

    public SpTransactionValueWriterJob(BdrvDriver bdrvDriver, SpTransactionValueRepository valueRepository, StatisticService statisticService, @Value("${sp_transaction_value_writing_in_seconds}") int delay) {
        super(bdrvDriver, valueRepository, statisticService);
        this.delay = delay;
    }

    @Override
    public TimeUnit getDelayTimeUnit() {
        return TimeUnit.SECONDS;
    }
}