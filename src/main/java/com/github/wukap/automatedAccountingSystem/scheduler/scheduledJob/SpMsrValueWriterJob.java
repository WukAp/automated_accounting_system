package com.github.wukap.automatedAccountingSystem.scheduler.scheduledJob;

import com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.BdrvDriver;
import com.github.wukap.automatedAccountingSystem.ringBufferDatabase.SpMsrValueRepository;
import org.springframework.stereotype.Component;

@Component
public class SpMsrValueWriterJob extends BdrvWriterSchedulerJob<SpMsrValueRepository> {
    public SpMsrValueWriterJob(BdrvDriver bdrvDriver, SpMsrValueRepository valueRepository) {
        super(bdrvDriver, valueRepository);
    }
}
