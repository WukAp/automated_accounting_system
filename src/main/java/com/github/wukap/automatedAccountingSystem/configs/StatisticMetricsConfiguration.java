package com.github.wukap.automatedAccountingSystem.configs;

import com.github.wukap.automatedAccountingSystem.model.HistoryLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;

@Configuration

public class StatisticMetricsConfiguration {
    @Value("${written_logs_buffer_capacity}")
    private int writtenLogsBufferCapacity;
    @Value("${thrown_logs_buffer_capacity}")
    private int thrownLogsBufferCapacity;

    @Bean
    public ArrayBlockingQueue<HistoryLog> writtenLogsQueue() {
        return new ArrayBlockingQueue<HistoryLog>(writtenLogsBufferCapacity);

    }

    @Bean
    public ArrayBlockingQueue<HistoryLog> thrownLogsQueue() {
        return new ArrayBlockingQueue<HistoryLog>(thrownLogsBufferCapacity);

    }
}