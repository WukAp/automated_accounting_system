package com.github.wukap.automatedAccountingSystem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ESStatistics
{
    private final ScheduledExecutorService es;
    private final HashMap<String, Integer> measurements = new HashMap<>();
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void updateMeasurement(String name)
    {
        synchronized (measurements)
        {
            measurements.compute(name, (n, v) -> Optional.ofNullable(v).orElse(0) + 1);
        }
    }

    public void close()
    {
        es.shutdown();
    }

    public ESStatistics(String name, long logIntervalMs)
    {
        es = Executors.newScheduledThreadPool(1);
        es.scheduleAtFixedRate(() -> {
            synchronized (measurements)
            {
                log.info(name + " new measurements since last log: " + GSON.toJson(measurements));
                Set<String> keys = new HashSet<>(measurements.keySet());
                keys.forEach(k -> measurements.put(k, 0));
            }
        }, logIntervalMs, logIntervalMs, TimeUnit.MILLISECONDS);
    }
}
