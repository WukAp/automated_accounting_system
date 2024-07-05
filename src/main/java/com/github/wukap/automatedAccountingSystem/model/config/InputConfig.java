package com.github.wukap.automatedAccountingSystem.model.config;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InputConfig {
    private List<Sensor> sensors;
    private List<EventTransaction> eventTransactions;
    private List<EventStatus> eventStatuses;
    private Settings settings;

    // Getters and setters


    @Data
    @AllArgsConstructor
    public static class Sensor {
        private String id;
        private String tag;
    }

    @Data
    @AllArgsConstructor
    public static class EventTransaction {
        private String type;
        private Map<String, String> tags;
    }
    @Data
    @AllArgsConstructor
    public static class EventStatus {
        private String type;
        private String tag;
        private String uuId;
    }

    @Data
    @AllArgsConstructor
    @Getter
    public static class Settings {
        private int measureEventsPeriod;
        private int historyDays;
        private int ffcId;
    }


}


