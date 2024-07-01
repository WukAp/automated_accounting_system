package com.github.wukap.automatedAccountingSystem.common;


import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
public class CustomThreadFactory implements ThreadFactory {
    private final String threadNamePrefix;
    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        Thread newThread = new Thread(runnable);
        newThread.setName(threadNamePrefix + "-thread-" + counter.incrementAndGet());
        newThread.setDaemon(true);
        newThread.setUncaughtExceptionHandler((thread, throwable) -> log.error("FATAL: uncaught exception in " + thread.getName(), throwable));
        return newThread;
    }
}