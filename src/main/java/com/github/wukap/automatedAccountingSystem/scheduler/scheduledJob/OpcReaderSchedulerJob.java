package com.github.wukap.automatedAccountingSystem.scheduler.scheduledJob;

import com.github.wukap.automatedAccountingSystem.driver.opcDriver.OpcUaDriver;
import com.github.wukap.automatedAccountingSystem.model.OpcValue;
import com.github.wukap.automatedAccountingSystem.model.config.InputConfig;
import com.github.wukap.automatedAccountingSystem.ringBufferDatabase.SpMsrValueRepository;
import com.github.wukap.automatedAccountingSystem.utils.OpcValueToBdrvValueConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
@Slf4j
@Component
public class OpcReaderSchedulerJob implements ScheduledJob {
    private final OpcUaDriver opcUaDriver;
    private final InputConfig config;
    private final Executor virtualExecutor;
    private final SpMsrValueRepository spMsrValueRepository;


    @Autowired
    public OpcReaderSchedulerJob(OpcUaDriver opcUaDriver, InputConfig config, SpMsrValueRepository spMsrValueRepository) {
        this.opcUaDriver = opcUaDriver;
        this.config = config;
        this.spMsrValueRepository = spMsrValueRepository;
        this.virtualExecutor = Executors.newVirtualThreadPerTaskExecutor();
    }

    @Override
    public void run() {

        try {
            for (InputConfig.Sensor sensor : config.getSensors()) {
                virtualExecutor.execute(() -> {
                    OpcValue value = opcUaDriver.read_(sensor.getItem_id());
                    if (value == null) {
                        log.warn("Value from OPC UA with item_id: " + sensor.getItem_id() + " is null");
                        return;
                    };
                        var bdrvValue = OpcValueToBdrvValueConverter.opcValueToSpMsnStatusSetValue(value, sensor.getId());
                        if (bdrvValue == null) return;
                        log.trace(bdrvValue.toString());
                        spMsrValueRepository.save(bdrvValue);
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
