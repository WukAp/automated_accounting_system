package com.github.wukap.automatedAccountingSystem.model;

import lombok.NonNull;

public record HistoryLog(@NonNull String id, @NonNull String tag, @NonNull String value, @NonNull String timestamp) {

    @Override
    public String toString() {
        return "[" +
                "id='" + id + '\'' +
                ", tag='" + tag + '\'' +
                ", value='" + value + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ']';
    }}
