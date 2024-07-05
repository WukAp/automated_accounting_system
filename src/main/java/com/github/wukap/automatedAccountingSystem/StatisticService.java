package com.github.wukap.automatedAccountingSystem;

import com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.BdrvDriver;
import com.github.wukap.automatedAccountingSystem.h2Database.SpMsnStatusSetValueRepository;
import com.github.wukap.automatedAccountingSystem.h2Database.SpMsrValueRepository;
import com.github.wukap.automatedAccountingSystem.h2Database.SpTransactionValueRepository;
import com.github.wukap.automatedAccountingSystem.model.HistoryLog;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.BdrvValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsnStatusSetValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsrValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpTransactionValue;
import com.github.wukap.automatedAccountingSystem.model.config.InputConfig;
import com.github.wukap.automatedAccountingSystem.utils.OpcValueToBdrvValueConverter;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;

import static java.lang.Math.max;

@Getter
@Service
public class StatisticService {
    private final ArrayBlockingQueue<HistoryLog> historyLogsQueue;
    private final List<JpaRepository<? extends BdrvValue, String>> bufferedBdrvValuesRepositories;
    private final InputConfig config;
    private final BdrvDriver bdrvDriver;

    @Autowired
    public StatisticService(ArrayBlockingQueue<HistoryLog> historyLogsQueue, SpMsrValueRepository spMsrValueRepository, SpTransactionValueRepository spTransactionValueRepository, SpMsnStatusSetValueRepository spMsnStatusSetValueRepository, InputConfig config, BdrvDriver bdrvDriver) {
        this.config = config;
        this.bdrvDriver = bdrvDriver;
        this.bufferedBdrvValuesRepositories = List.of(spMsrValueRepository, spTransactionValueRepository, spMsnStatusSetValueRepository);
        this.historyLogsQueue = historyLogsQueue;
    }

    public long getBufferedValueAmount() {
        return bufferedBdrvValuesRepositories.stream().mapToLong(JpaRepository::count).sum();
    }

    public String getProsentageOfFilling() {
        var lastValue = bufferedBdrvValuesRepositories.stream().flatMap(repo -> repo.findAll().stream()).min(Comparator.comparing(BdrvValue::getTime));
        if (lastValue.isEmpty()) {
            return "0 days of " + config.getSettings().getHistoryDays() + " days";
        }
        ZonedDateTime lastTime = OpcValueToBdrvValueConverter.formattedStringToZonedDateTimeConverter(lastValue.get().getTime());
        long daysPassed = ChronoUnit.DAYS.between(lastTime.toLocalDate(), LocalDate.now());

        return max((daysPassed * 100.0) / config.getSettings().getHistoryDays(), 0) + "%" + " of " + config.getSettings().getHistoryDays() + " days";
    }

    public boolean isNetworkConnected() {
        return bdrvDriver.isNetworkConnected();
    }

    public void addHistoryLog(HistoryLog transactionLog) {
        if (historyLogsQueue.remainingCapacity() == 0) {
            historyLogsQueue.poll();
        }
        historyLogsQueue.add(transactionLog);
    }

    public void addHistoryLog(SpMsrValue value) {
        Optional<InputConfig.Sensor> currentSensor = config.getSensors().stream().filter(sensor -> sensor.getId().equals(value.getId())).findFirst();
        if (currentSensor.isEmpty()) {
            return;
        }
        addHistoryLog(new HistoryLog(value.getId(), currentSensor.get().getTag(), value.getValue(), value.getTime()));
    }

    public void addHistoryLog(SpTransactionValue value) {
        for (InputConfig.EventTransaction eventTransaction : config.getEventTransactions()) {
            for (Map.Entry<String, String> tag : eventTransaction.getTags().entrySet()) {
                if (tag.getValue().equals(value.getId())) {
                    addHistoryLog(new HistoryLog(value.getId(), tag.getKey(), value.getValue(), value.getTime()));
                    return;
                }
            }
        }
    }

    public void addHistoryLog(SpMsnStatusSetValue value) {
        Optional<InputConfig.EventStatus> currentEvent = config.getEventStatuses().stream().filter(status -> status.getUuId().equals(value.getId())).findFirst();
        if (currentEvent.isEmpty()) {
            return;
        }
        addHistoryLog(new HistoryLog(value.getId(), currentEvent.get().getTag(), value.getValue(), value.getTime()));

    }

    public void addHistoryLog(BdrvValue value) {
        addHistoryLog(new HistoryLog(value.getId(), "unknown", value.getValue(), value.getTime()));
    }

}
