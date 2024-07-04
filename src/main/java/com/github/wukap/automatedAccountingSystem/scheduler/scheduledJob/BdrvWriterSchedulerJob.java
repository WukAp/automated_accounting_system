package com.github.wukap.automatedAccountingSystem.scheduler.scheduledJob;

import com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.BdrvDriver;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.BdrvValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
public abstract class BdrvWriterSchedulerJob<R extends JpaRepository<? extends BdrvValue, String>> implements ScheduledJob {
    private final BdrvDriver bdrvDriver;
    private final R valueRepository;
    private final Executor virtualExecutor;

    @Autowired
    public BdrvWriterSchedulerJob(BdrvDriver bdrvDriver, R valueRepository) {
        this.bdrvDriver = bdrvDriver;
        this.valueRepository = valueRepository;
        this.virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();
    }

    @Override
    public void run() {
        for (var value : valueRepository.findAll()) {
            virtualExecutor.execute(() -> {
                log.trace(value.toString());
                bdrvDriver.write(value);
            });
        }
        log.trace("Buffered value amount: " + valueRepository.count());
    }
}
