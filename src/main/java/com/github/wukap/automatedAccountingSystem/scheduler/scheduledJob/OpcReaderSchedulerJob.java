package com.github.wukap.automatedAccountingSystem.scheduler.scheduledJob;

import com.github.wukap.automatedAccountingSystem.driver.opcDriver.OpcUaDriver;
import com.github.wukap.automatedAccountingSystem.h2Database.SpMsnStatusSetValueRepository;
import com.github.wukap.automatedAccountingSystem.h2Database.SpMsrValueRepository;
import com.github.wukap.automatedAccountingSystem.model.OpcValue;
import com.github.wukap.automatedAccountingSystem.model.config.InputConfig;
import com.github.wukap.automatedAccountingSystem.utils.MathUtils;
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
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class OpcReaderSchedulerJob implements ScheduledJob {
    private final OpcUaDriver opcUaDriver;
    private final InputConfig config;
    private final Executor virtualExecutor;
    private final SpMsrValueRepository spMsrValueRepository;
    private final SpMsnStatusSetValueRepository spMsnStatusSetValueRepository;
    @Getter
    private final int delay;
    private final int sp_msr_value_delay;
    private final int sp_msn_status_value_delay;
    private final int sp_transaction_value_delay;
    private AtomicInteger currentDelay = new AtomicInteger(0);


    @Autowired
    public OpcReaderSchedulerJob(OpcUaDriver opcUaDriver, InputConfig config, SpMsrValueRepository spMsrValueRepository, SpMsnStatusSetValueRepository spMsnStatusSetValueRepository, @Value("${sp_msr_value_reading_in_seconds}") int spMsrValueInSeconds, @Value("${sp_msn_status_value_reading_in_seconds}") int spMsnStatusValueInSeconds, @Value("${sp_transaction_value_reading_in_seconds}") int spTransactionValueInSeconds) {
        this.opcUaDriver = opcUaDriver;
        this.config = config;
        this.spMsrValueRepository = spMsrValueRepository;
        this.spMsnStatusSetValueRepository = spMsnStatusSetValueRepository;
        this.sp_msr_value_delay = spMsrValueInSeconds;
        sp_msn_status_value_delay = spMsnStatusValueInSeconds;
        sp_transaction_value_delay = spTransactionValueInSeconds;
        this.virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();
        this.delay = MathUtils.findGCD(sp_msr_value_delay, sp_msn_status_value_delay, sp_transaction_value_delay);
    }

    @Override
    public void run() {
        try {
            if (currentDelay.get() % sp_msr_value_delay == 0) for (InputConfig.Sensor sensor : config.getSensors()) {
                virtualExecutor.execute(() -> this.readMsrValue(sensor));
            }
            if (currentDelay.get() % sp_msn_status_value_delay == 0)
                for (InputConfig.EventStatus status : config.getEventStatuses()) {
                    virtualExecutor.execute(() -> this.readStatusSet(status));
                }
            if (currentDelay.get() % sp_transaction_value_delay == 0) {

            }
            currentDelay.incrementAndGet();
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

    private void readMsrValue(InputConfig.Sensor sensor) {
        OpcValue value = readValueByTag(sensor.getTag());

        var bdrvValue = OpcValueToBdrvValueConverter.opcValueToSpMsrValueConverter(value, sensor.getId());
        if (bdrvValue == null) {
            log.warn("Converted value from OPC UA with item_id: " + sensor.getTag() + " is null");
            return;
        }
        spMsrValueRepository.save(bdrvValue);
        log.info("Value from OPC UA with item_id: " + sensor.getTag() + " was saved");
    }

    private void readStatusSet(InputConfig.EventStatus status) {
        OpcValue value = readValueByTag(status.getTag());

        var bdrvValue = OpcValueToBdrvValueConverter.opcValueToSpMsnStatusSetValueConverter(value, status.getUuId());
        if (bdrvValue == null) {
            log.warn("Converted value from OPC UA with item_id: " + status.getTag() + " is null");
            return;
        }
        spMsnStatusSetValueRepository.save(bdrvValue);
        log.info("Value from OPC UA with item_id: " + status.getTag() + " was saved");
    }

    private void readTransactionValue(InputConfig.EventTransaction transaction) {

    }

    private OpcValue readValueByTag(String tag) {
        OpcValue value = null;
        try {
            value = opcUaDriver.read(tag);
        } catch (ServiceResultException e) {
            log.error(String.valueOf(e));
            return null;
        }
        log.info("Value from OPC UA with item_id: " + tag + " was read");
        if (value == null) {
            log.warn("Value from OPC UA with item_id: " + tag + " is null");
            return null;
        }
        return value;
    }

}
