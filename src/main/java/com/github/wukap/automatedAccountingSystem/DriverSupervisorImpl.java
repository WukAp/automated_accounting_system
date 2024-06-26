package com.github.wukap.automatedAccountingSystem;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DriverSupervisorImpl implements DriverSupervisor
{
    private final ConcurrentMap<String, ESDriver> failedDrivers = new ConcurrentHashMap<>();
    private final AtomicBoolean isRetrying = new AtomicBoolean(false);

    @Override
    public boolean canProcess()
    {
        if (failedDrivers.isEmpty())
        {
            return true;
        } else
        {
            if (isRetrying.get())
            {
                return false;
            }
            safelyRetry();
            return failedDrivers.isEmpty();
        }
    }

    private void safelyRetry()
    {
        if (isRetrying.compareAndSet(false, true))
        {
            try
            {
                List<String> successfullyRetriedDrivers = failedDrivers.entrySet()
                        .stream()
                        .map(e -> {
                            if (e.getValue().retryStore())
                            {
                                return e.getKey();
                            } else
                            {
                                return null;
                            }
                        })
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                successfullyRetriedDrivers.forEach(failedDrivers::remove);
                if (!failedDrivers.isEmpty())
                {
                    log.debug("The list of failed drivers: " + failedDrivers);
                }
            } finally
            {
                isRetrying.set(false);
            }
        }
    }

    @Override
    public void reportFailure(ESDriver failedDriver)
    {
        failedDrivers.putIfAbsent(failedDriver.getName(), failedDriver);
        //TODO NOW add proper logging here
    }
}
