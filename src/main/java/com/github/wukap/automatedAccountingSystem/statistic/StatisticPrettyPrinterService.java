package com.github.wukap.automatedAccountingSystem.statistic;

import com.github.wukap.automatedAccountingSystem.model.HistoryLog;
import org.springframework.stereotype.Service;

@Service
public class StatisticPrettyPrinterService {
    private final StatisticService statisticService;

    public StatisticPrettyPrinterService(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    public String prettyPrintStatistic() {

        return prettyPrintStatisticThrownLogs() + "------------------------------------------------\n\n" + prettyPrintStatisticWrittenLogs() + "------------------------------------------------\n\n" + prettyPrintStatisticBufferedValueAmount() + "------------------------------------------------\n\n" + prettyPrintStatisticBufferedValueProcentOfFilled() + "------------------------------------------------\n\n" + prettyPrintStatisticIsNetworkConnected();
    }

    public String prettyPrintStatisticWrittenLogs() {
        if (statisticService.getWrittenLogsQueue().isEmpty()) {
            return "No written values" + "\n";
        }
        var historyLogs = statisticService.getWrittenLogsQueue().stream().map(HistoryLog::toString).reduce((a, b) -> a + "\n" + b).orElse("");
        return "Last sent values: \n" + historyLogs + "\n";
    }

    public String prettyPrintStatisticThrownLogs() {
        if (statisticService.getThrownLogsQueue().isEmpty()) {
            return "No thrown values" + "\n";
        }
        var historyLogs = statisticService.getThrownLogsQueue().stream().map(HistoryLog::toString).reduce((a, b) -> a + "\n" + b).orElse("");
        return "Last thrown values: \n" + historyLogs + "\n";
    }

    public String prettyPrintStatisticBufferedValueAmount() {
        return "Buffered value amount: " + statisticService.getBufferedValueAmount() + "\n";
    }

    public String prettyPrintStatisticBufferedValueProcentOfFilled() {
        return "Buffered value procent of filled: " + statisticService.getProsentageOfFilling() + "\n";
    }

    public String prettyPrintStatisticIsNetworkConnected() {
        return (statisticService.isNetworkConnected() ? "Network connected" : "Network not connected") + "\n";
    }
}
