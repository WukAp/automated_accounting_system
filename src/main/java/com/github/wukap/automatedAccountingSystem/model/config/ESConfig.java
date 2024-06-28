package com.github.wukap.automatedAccountingSystem.model.config;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
public class ESConfig
{
    private List<Ffc> ffcList;


    // Getters and setters
    @Data
    @AllArgsConstructor
    public static class Ffc
    {
        private List<Sensor> sensors;
        private List<EventTransaction> eventTransactions;
        private List<EventStatus> eventStatuses;
        private Settings settings;
    }

    @Data
    @AllArgsConstructor
    public static class Sensor
    {
        private String id;
        private String item_id;
    }

    @Data
    @AllArgsConstructor
    public static class EventTransaction
    {
        private String type;
        private Map<String, String> tags;
    }

    @AllArgsConstructor
    public static class EventStatus
    {
        private String type;
        private String tag;
        private String UU_id;
    }

    @Data
    @AllArgsConstructor
    public static class Settings
    {
        private int ffc_id;
        private int measure_events_period;
        private int history_days;
    }
}


