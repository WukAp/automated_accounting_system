package com.github.wukap.automatedAccountingSystem.statistic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatisticController {

    @Autowired
    private StatisticPrettyPrinterService statisticPrettyPrinterService;

    @GetMapping("/statistic")
    public String prettyPrintStatistic() {
        return statisticPrettyPrinterService.prettyPrintStatistic();
    }

    @GetMapping("/statistic/bufferedValueAmount")
    public String prettyPrintStatisticBufferedValueAmount() {
        return statisticPrettyPrinterService.prettyPrintStatisticBufferedValueAmount();
    }

    @GetMapping("/statistic/bufferedValueProcent")
    public String prettyPrintStatisticBufferedValueProcentOfFilled() {
        return statisticPrettyPrinterService.prettyPrintStatisticBufferedValueProcentOfFilled();
    }

    @GetMapping("/statistic/isNetworkConnected")
    public String isNetworkConnected() {
        return statisticPrettyPrinterService.prettyPrintStatisticIsNetworkConnected();
    }

    @GetMapping("/statistic/written")
    public String getBufferedValueAmount() {
        return statisticPrettyPrinterService.prettyPrintStatisticWrittenLogs();
    }

    @GetMapping("/statistic/thrown")
    public String getThrownLogs() {
        return statisticPrettyPrinterService.prettyPrintStatisticThrownLogs();
    }

}