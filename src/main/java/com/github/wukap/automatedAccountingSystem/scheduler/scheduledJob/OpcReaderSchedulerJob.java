package com.github.wukap.automatedAccountingSystem.scheduler.scheduledJob;

import com.github.wukap.automatedAccountingSystem.driver.opcDriver.OpcUaDriver;
import com.github.wukap.automatedAccountingSystem.h2Database.LastValuesHashCodeRepository;
import com.github.wukap.automatedAccountingSystem.h2Database.SpMsnStatusSetValueRepository;
import com.github.wukap.automatedAccountingSystem.h2Database.SpMsrValueRepository;
import com.github.wukap.automatedAccountingSystem.h2Database.SpTransactionValueRepository;
import com.github.wukap.automatedAccountingSystem.model.OpcValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.BdrvValue;
import com.github.wukap.automatedAccountingSystem.model.config.InputConfig;
import com.github.wukap.automatedAccountingSystem.utils.MathUtils;
import com.github.wukap.automatedAccountingSystem.utils.OpcValueToBdrvValueConverter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.opcfoundation.ua.common.ServiceResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OpcReaderSchedulerJob implements ScheduledJob {
    private final OpcUaDriver opcUaDriver;
    private final InputConfig config;
    private final Executor executor;
    private final SpMsrValueRepository spMsrValueRepository;
    private final SpMsnStatusSetValueRepository spMsnStatusSetValueRepository;
    private final SpTransactionValueRepository spTransactionValueRepository;
    @Getter
    private final int delay;
    private final int sp_msr_value_delay;
    private final int sp_msn_status_value_delay;
    private final int sp_transaction_value_delay;
    private AtomicInteger currentTimeCounter = new AtomicInteger(0);
    private final LastValuesHashCodeRepository lastValuesHashCodeRepository;
    Map<String, Integer> lastValues;

    @Autowired
    public OpcReaderSchedulerJob(OpcUaDriver opcUaDriver, InputConfig config, SpMsrValueRepository spMsrValueRepository, SpMsnStatusSetValueRepository spMsnStatusSetValueRepository, SpTransactionValueRepository spTransactionValueRepository, @Value("${sp_msr_value_reading_in_seconds}") int spMsrValueInSeconds, @Value("${sp_msn_status_value_reading_in_seconds}") int spMsnStatusValueInSeconds, @Value("${sp_transaction_value_reading_in_seconds}") int spTransactionValueInSeconds, LastValuesHashCodeRepository lastValuesHashCodeRepository) {
        this.opcUaDriver = opcUaDriver;
        this.config = config;
        this.spMsrValueRepository = spMsrValueRepository;
        this.spMsnStatusSetValueRepository = spMsnStatusSetValueRepository;
        this.spTransactionValueRepository = spTransactionValueRepository;
        this.sp_msr_value_delay = spMsrValueInSeconds;
        sp_msn_status_value_delay = spMsnStatusValueInSeconds;
        sp_transaction_value_delay = spTransactionValueInSeconds;
        this.lastValuesHashCodeRepository = lastValuesHashCodeRepository;
        this.executor = Executors.newFixedThreadPool(3);
        this.delay = MathUtils.findGCD(sp_msr_value_delay, sp_msn_status_value_delay, sp_transaction_value_delay);
    }

    @Override
    public void run() {
        int timeInThisIteration = currentTimeCounter.getAndAdd(delay);
        try {
            lastValues = lastValuesHashCodeRepository.findAll().stream().collect(Collectors.toMap(LastValuesHashCodeRepository.LastValuesHashCode::getTag, LastValuesHashCodeRepository.LastValuesHashCode::getHash));
            if (timeInThisIteration % sp_msr_value_delay == 0) for (InputConfig.Sensor sensor : config.getSensors()) {
                executor.execute(() -> this.readMsrValue(sensor));
            }
            if (timeInThisIteration % sp_msn_status_value_delay == 0)
                for (InputConfig.EventStatus status : config.getEventStatuses()) {
                    executor.execute(() -> this.readStatusSet(status));
                }
//            if (timeInThisIteration % sp_transaction_value_delay == 0) {
//                for (InputConfig.EventTransaction transaction : config.getEventTransactions()) {
//                    executor.execute(() -> this.readTransaction(transaction));
//                }
//            }
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
        if (value == null) {
            log.warn("Value from OPC UA with item_id: " + sensor.getTag() + " is null");
            return;
        }

        var bdrvValue = OpcValueToBdrvValueConverter.opcValueToSpMsrValueConverter(sensor.getTag(), value, sensor.getId());
        if (bdrvValue == null) {
            log.warn("Converted value from OPC UA with item_id: " + sensor.getTag() + " is null");
            return;
        }
        if (lastValues.containsKey(sensor.getTag()) && Objects.equals(lastValues.get(sensor.getTag()), bdrvValue.hashCode())) {
            log.info("Value from OPC UA with item_id: " + sensor.getTag() + " was skipped");
            return;
        }
        lastValuesHashCodeRepository.save(new LastValuesHashCodeRepository.LastValuesHashCode(sensor.getTag(), bdrvValue.hashCode()));
        spMsrValueRepository.save(bdrvValue);
        log.info("Value from OPC UA with item_id: " + sensor.getTag() + " was saved");

    }

    private void readStatusSet(InputConfig.EventStatus status) {
        OpcValue value = readValueByTag(status.getTag());

        var bdrvValue = OpcValueToBdrvValueConverter.opcValueToSpMsnStatusSetValueConverter(status.getTag(), value, status.getUuId());
        if (bdrvValue == null) {
            log.warn("Converted value from OPC UA with item_id: " + status.getTag() + " is null");
            return;
        }
        if (lastValues.containsKey(status.getTag()) && Objects.equals(lastValues.get(status.getTag()), bdrvValue.hashCode())) {
            log.info("Value from OPC UA with item_id: " + status.getTag() + " was skipped");
            return;
        }
        lastValuesHashCodeRepository.save(new LastValuesHashCodeRepository.LastValuesHashCode(status.getTag(), bdrvValue.hashCode()));
        spMsnStatusSetValueRepository.save(bdrvValue);
        log.info("Value from OPC UA with item_id: " + status.getTag() + " was saved");
    }

    private void readTransaction(InputConfig.EventTransaction transaction) {
        OpcValue valueStart = readValueByTag(transaction.getTagStart());
        Double TRANSACTION_START_VALUE = 1.0;
        if (!Objects.equals(valueStart != null ? valueStart.getValue() : null, TRANSACTION_START_VALUE)) return;
        OpcValue valueTag1 = readValueByTag(transaction.getTagStart());
        if ((valueTag1 != null ? valueTag1.getValue() : null) == 1.0) {
            OpcValue valueTag2 = readValueByTag(transaction.getTag2());
            OpcValue valueTag3 = readValueByTag(transaction.getTag3());
            OpcValue valueTag4 = readValueByTag(transaction.getTag4());
            var bdrvValue = OpcValueToBdrvValueConverter.opcValueToSpTransactionValueConverter(transaction.getTagStart(), valueStart, valueTag1, valueTag2, valueTag3, valueTag4);
            if (bdrvValue == null) {
                log.warn("Converted value from OPC UA with item_id: " + transaction.getTagStart() + " is null");
                return;
            }
            if (lastValues.containsKey(transaction.getTagStart()) && Objects.equals(lastValues.get(transaction.getTagStart()), bdrvValue.hashCode())) {
                log.info("Value from OPC UA with item_id: " + transaction.getTagStart() + " was skipped");
                return;
            }
            lastValuesHashCodeRepository.save(new LastValuesHashCodeRepository.LastValuesHashCode(transaction.getTagStart(), bdrvValue.hashCode()));
            spTransactionValueRepository.save(bdrvValue);
            log.info("Value from OPC UA with item_id: " + transaction.getTagStart() + " was saved");
        }

    }

    private OpcValue readValueByTag(String tag) {
        OpcValue value = null;
        try {
            log.info("Try read value from OPC UA with item_id: " + tag);
            value = opcUaDriver.read(tag);
        } catch (ServiceResultException e) {
            log.error(String.valueOf(e));
            return null;
        }
        log.info("Value from OPC UA with item_id: " + tag + " was read ");
        if (value == null) {
            log.warn("Value from OPC UA with item_id: " + tag + " is null");
            return null;
        }
        return value;
    }

}
