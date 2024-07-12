package com.github.wukap.automatedAccountingSystem.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class StatisticController {

    @Autowired
    private StatisticPrettyPrinterService statisticPrettyPrinterService;

    @Async
    @GetMapping("/statistic")
    public CompletableFuture<String> prettyPrintStatistic() {
        return CompletableFuture.completedFuture(statisticPrettyPrinterService.prettyPrintStatistic());
    }

    @Async
    @GetMapping("/statistic/bufferedValueAmount")
    public CompletableFuture<String> prettyPrintStatisticBufferedValueAmount() {
        return CompletableFuture.completedFuture(statisticPrettyPrinterService.prettyPrintStatisticBufferedValueAmount());
    }

    @Async
    @GetMapping("/statistic/bufferedValueProcent")
    public CompletableFuture<String> prettyPrintStatisticBufferedValueProcentOfFilled() {
        return CompletableFuture.completedFuture(statisticPrettyPrinterService.prettyPrintStatisticBufferedValueProcentOfFilled());
    }

    @Async
    @GetMapping("/statistic/isNetworkConnected")
    public CompletableFuture<String> isNetworkConnected() {
        return CompletableFuture.completedFuture(statisticPrettyPrinterService.prettyPrintStatisticIsNetworkConnected());
    }

    @Async
    @GetMapping("/statistic/written")
    public CompletableFuture<String> getBufferedValueAmount() {
        return CompletableFuture.completedFuture(statisticPrettyPrinterService.prettyPrintStatisticWrittenLogs());
    }

    @Async
    @GetMapping("/statistic/thrown")
    public CompletableFuture<String> getThrownLogs() {
        return CompletableFuture.completedFuture(statisticPrettyPrinterService.prettyPrintStatisticThrownLogs());
    }
}