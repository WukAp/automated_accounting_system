package com.github.wukap.automatedAccountingSystem.statistic;

import com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.BdrvDriver;
import com.github.wukap.automatedAccountingSystem.h2Database.SpMsnStatusSetValueRepository;
import com.github.wukap.automatedAccountingSystem.h2Database.SpMsrValueRepository;
import com.github.wukap.automatedAccountingSystem.h2Database.SpTransactionValueRepository;
import com.github.wukap.automatedAccountingSystem.model.HistoryLog;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.BdrvValue;
import com.github.wukap.automatedAccountingSystem.model.config.InputConfig;
import com.github.wukap.automatedAccountingSystem.utils.OpcValueToBdrvValueConverter;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

import static java.lang.Math.max;

@Getter
@Service
public class StatisticService {
    private final ArrayBlockingQueue<HistoryLog> writtenLogsQueue;
    private final ArrayBlockingQueue<HistoryLog> thrownLogsQueue;
    private final List<JpaRepository<? extends BdrvValue, String>> h2Repositories;
    private final InputConfig config;
    private final BdrvDriver bdrvDriver;

    @Autowired
    public StatisticService(@Qualifier("writtenLogsQueue") ArrayBlockingQueue<HistoryLog> writtenLogsQueue, @Qualifier("thrownLogsQueue") ArrayBlockingQueue<HistoryLog> thrownLogsQueue, SpMsrValueRepository spMsrValueRepository, SpTransactionValueRepository spTransactionValueRepository, SpMsnStatusSetValueRepository spMsnStatusSetValueRepository, InputConfig config, BdrvDriver bdrvDriver) {
        this.thrownLogsQueue = thrownLogsQueue;
        this.config = config;
        this.bdrvDriver = bdrvDriver;
        this.h2Repositories = List.of(spMsrValueRepository, spTransactionValueRepository, spMsnStatusSetValueRepository);
        this.writtenLogsQueue = writtenLogsQueue;
    }

    public long getBufferedValueAmount() {
        return h2Repositories.stream().mapToLong(JpaRepository::count).sum();
    }

    public String getProsentageOfFilling() {
        var lastValue = h2Repositories.stream().flatMap(repo -> repo.findAll().stream()).min(Comparator.comparing(BdrvValue::getTime));
        if (lastValue.isEmpty()) {
            return "0 days of " + config.getSettings().getHistoryDays() + " days";
        }
        try {
            ZonedDateTime lastTime = OpcValueToBdrvValueConverter.formattedStringToZonedDateTimeConverter(lastValue.get().getTime());
            long daysPassed = ChronoUnit.DAYS.between(lastTime.toLocalDate(), LocalDate.now());
            int eps = 100;
            return max((daysPassed * 100 * eps) / config.getSettings().getHistoryDays() / (double) eps, 0) + "% (" + daysPassed + " days of " + config.getSettings().getHistoryDays() + " days)";
        } catch (Exception e) {
            return "Error: cannot parse the time. The oldest value is from " + lastValue.get().getTime() + "(? days of " + config.getSettings().getHistoryDays() + " days)";
        }

    }

    public boolean isNetworkConnected() {
        return bdrvDriver.isNetworkConnected();
    }

    public void addWrittenLog(HistoryLog transactionLog) {
        if (writtenLogsQueue.remainingCapacity() == 0) {
            writtenLogsQueue.poll();
        }
        writtenLogsQueue.add(transactionLog);
    }

    public void addWrittenLog(BdrvValue value) {

        addWrittenLog(new HistoryLog(value.getId(), value.getTag(), value.getValue(), value.getTime()));
    }


    public void addThrownLog(HistoryLog transactionLog) {
        if (thrownLogsQueue.remainingCapacity() == 0) {
            thrownLogsQueue.poll();
        }
        thrownLogsQueue.add(transactionLog);
    }


    public void addThrownLog(BdrvValue value) {
        addThrownLog(new HistoryLog(value.getId(), value.getTag(), value.getValue(), value.getTime()));
    }


}
