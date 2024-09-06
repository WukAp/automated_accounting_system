package com.github.wukap.automatedAccountingSystem.scheduler.scheduledJob;

import com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.BdrvDriver;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.BdrvValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsrValue;
import com.github.wukap.automatedAccountingSystem.statistic.StatisticService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.SQLException;

@Slf4j

public abstract class BdrvWriterSchedulerJob<R extends JpaRepository<T, String>, T extends BdrvValue> implements ScheduledJob {
    protected final BdrvDriver bdrvDriver;
    protected final R valueRepository;
    protected final StatisticService statisticService;

    @Autowired
    public BdrvWriterSchedulerJob(BdrvDriver bdrvDriver, R valueRepository, StatisticService statisticService) {
        this.bdrvDriver = bdrvDriver;
        this.valueRepository = valueRepository;
        this.statisticService = statisticService;
    }

    @Override
    public void run() {
        try {

            if (isRepositoryEmpty()) {
                return;
            }
            for (T value : valueRepository.findAll()) {
                log.debug(value.toString() + " is going to be written");
                boolean result = false;
                try {
                    result = bdrvDriver.writeValue( value);
                } catch (SQLException e) {
                    log.error("Can't write " + value + " to BDRV because of SQL exception");
                    valueRepository.delete(value);
                    statisticService.addThrownLog(value);
                }

                if (result) {
                    log.info(value + " was successfully written");
                    valueRepository.delete(value);
                    statisticService.addWrittenLog(value);
                } else {
                    log.warn(value + " was not written, something went wrong");
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    protected boolean isRepositoryEmpty() {
        if (valueRepository.count() == 0) {
            log.debug("There is no values to write from " + this.getClass().getSimpleName());
            return true;
        }
        return false;
    }

    @Override
    public Type getType() {
        return Type.DELAY;
    }
}
