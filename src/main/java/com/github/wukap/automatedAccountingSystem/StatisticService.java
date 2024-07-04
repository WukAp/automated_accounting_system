package com.github.wukap.automatedAccountingSystem;

import com.github.wukap.automatedAccountingSystem.model.HistoryLog;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.BdrvValue;
import com.github.wukap.automatedAccountingSystem.ringBufferDatabase.SpMsnStatusSetValueRepository;
import com.github.wukap.automatedAccountingSystem.ringBufferDatabase.SpMsrValueRepository;
import com.github.wukap.automatedAccountingSystem.ringBufferDatabase.SpTransactionValueRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

@Getter
@Service
public class StatisticService {
    private final ArrayBlockingQueue<HistoryLog> historyLogsQueue;
    private final List<JpaRepository<? extends BdrvValue , String>> bufferedBdrvValuesRepositories;

    @Autowired
    public StatisticService(ArrayBlockingQueue<HistoryLog> historyLogsQueue, SpMsrValueRepository spMsrValueRepository, SpTransactionValueRepository spTransactionValueRepository, SpMsnStatusSetValueRepository spMsnStatusSetValueRepository) {
        this.bufferedBdrvValuesRepositories = List.of(spMsrValueRepository, spTransactionValueRepository, spMsnStatusSetValueRepository);
        this.historyLogsQueue = historyLogsQueue;
    }

    public void addTransactionLog(HistoryLog transactionLog) {
        if (historyLogsQueue.remainingCapacity() == 0) {
            historyLogsQueue.poll();
        }
        historyLogsQueue.add(transactionLog);
    }

    public long getBufferedValueAmount() {
        return bufferedBdrvValuesRepositories.stream().mapToLong(JpaRepository::count).sum();
    }
}
