package com.github.wukap.automatedAccountingSystem.model.config;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InputConfig {
    @NonNull
    private List<Sensor> sensors;
    @NonNull
    private List<EventTransaction> eventTransactions;
    @NonNull
    private List<EventStatus> eventStatuses;
    @NonNull
    private Settings settings;

    // Getters and setters


    @Data
    @AllArgsConstructor
    public static class Sensor {
        @NonNull
        private String id;
        @NonNull
        private String tag;
    }

    @Data
    @AllArgsConstructor
    public static class EventTransaction {
        private String type;
        @NonNull
        private String tagStart;
        @NonNull
        private String tag1;
        @NonNull
        private String tag2;
        @NonNull
        private String tag3;
        @NonNull
        private String tag4;
    }

    @Data
    @AllArgsConstructor
    public static class EventStatus {
        @NonNull
        private String type;
        @NonNull
        private String tag;
        @NonNull
        private String uuId;
    }

    @Data
    @AllArgsConstructor
    @Getter
    public static class Settings {
        @NonNull
        private int measureEventsPeriod;
        @NonNull
        private int historyDays;
        @NonNull
        private int ffcId;
    }


}


