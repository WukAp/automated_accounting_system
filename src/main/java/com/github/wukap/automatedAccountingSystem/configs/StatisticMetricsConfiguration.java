package com.github.wukap.automatedAccountingSystem.configs;

import com.github.wukap.automatedAccountingSystem.model.HistoryLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;

@Configuration

public class StatisticMetricsConfiguration {
    @Value("${history_logs_buffer_capacity}")
    private int historyLogsBufferCapacity;
    @Bean
    public ArrayBlockingQueue<HistoryLog> transactionLogsQueue() {
        return new ArrayBlockingQueue<HistoryLog>(historyLogsBufferCapacity);

    }
}