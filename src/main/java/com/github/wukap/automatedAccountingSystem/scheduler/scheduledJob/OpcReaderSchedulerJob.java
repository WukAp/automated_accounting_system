package com.github.wukap.automatedAccountingSystem.scheduler.scheduledJob;

import com.github.wukap.automatedAccountingSystem.driver.opcDriver.OpcUaDriver;
import com.github.wukap.automatedAccountingSystem.model.OpcValue;
import com.github.wukap.automatedAccountingSystem.model.config.InputConfig;
import com.github.wukap.automatedAccountingSystem.ringBufferDatabase.SpMsrValueRepository;
import com.github.wukap.automatedAccountingSystem.utils.OpcValueToBdrvValueConverter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.opcfoundation.ua.common.ServiceResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class OpcReaderSchedulerJob implements ScheduledJob {
    private final OpcUaDriver opcUaDriver;
    private final InputConfig config;
    private final Executor virtualExecutor;
    private final SpMsrValueRepository spMsrValueRepository;
    @Getter
    private final int delay;
    private final int sp_msr_value_delay;
    private final int sp_msn_status_value_delay;
    private final int sp_transaction_value_delay;
    private int currentDelay = 0;


    @Autowired
    public OpcReaderSchedulerJob(OpcUaDriver opcUaDriver, InputConfig config, SpMsrValueRepository spMsrValueRepository, @Value("${sp_msr_value_reading_in_seconds}") int spMsrValueInSeconds, @Value("${sp_msn_status_value_in_seconds}") int spMsnStatusValueInSeconds, @Value("${sp_transaction_value_in_seconds}") int spTransactionValueInSeconds) {
        this.opcUaDriver = opcUaDriver;
        this.config = config;
        this.spMsrValueRepository = spMsrValueRepository;
        this.sp_msr_value_delay = spMsrValueInSeconds;
        sp_msn_status_value_delay = spMsnStatusValueInSeconds;
        sp_transaction_value_delay = spTransactionValueInSeconds;
        this.virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();
        this.delay = sp_msr_value_delay * sp_msn_status_value_delay * sp_transaction_value_delay;
    }

    @Override
    public void run() {
        try {
            if (currentDelay % sp_msr_value_delay == 0) for (InputConfig.Sensor sensor : config.getSensors()) {
                virtualExecutor.execute(() -> {
                    OpcValue value = null;
                    try {
                        value = opcUaDriver.read(sensor.getTag());
                    } catch (ServiceResultException e) {
                        log.error(String.valueOf(e));
                        return;
                    }
                    log.info("Value from OPC UA with item_id: " + sensor.getTag() + " was read");
                    if (value == null) {
                        log.warn("Value from OPC UA with item_id: " + sensor.getTag() + " is null");
                        return;
                    }
                    ;
                    var bdrvValue = OpcValueToBdrvValueConverter.opcValueToSpMsnStatusSetValue(value, sensor.getId());
                    if (bdrvValue == null) {
                        log.warn("Converted value from OPC UA with item_id: " + sensor.getTag() + " is null");
                        return;
                    }
                    spMsrValueRepository.save(bdrvValue);
                    log.info("Value from OPC UA with item_id: " + sensor.getTag() + " was saved");
                });
            }
            if (currentDelay % sp_msn_status_value_delay == 0) {

            }
            if (currentDelay % sp_transaction_value_delay == 0) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Type getType() {
        return Type.DELAY;
    }

    @Override
    public TimeUnit getDelayTimeUnit() {
        return TimeUnit.SECONDS;
    }


}
