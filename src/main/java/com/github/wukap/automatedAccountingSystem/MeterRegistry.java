package com.github.wukap.automatedAccountingSystem;

public class MeterRegistry
{

    public Counter counter(String name, String... tags) {
        return new Counter();
    }
}
