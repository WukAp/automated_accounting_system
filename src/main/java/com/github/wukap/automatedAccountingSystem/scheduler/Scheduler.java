package com.github.wukap.automatedAccountingSystem.scheduler;

import com.github.wukap.automatedAccountingSystem.scheduler.scheduledJob.ScheduledJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class Scheduler {
    private final List<ScheduledJob> scheduledJobs;
    private final ScheduledExecutorService executor;

    @Autowired
    public Scheduler(List<ScheduledJob> scheduledJobs) {
        this.scheduledJobs = scheduledJobs;
        executor = Executors.newScheduledThreadPool(scheduledJobs.size());
    }

    public void start() {
        for (ScheduledJob job : scheduledJobs) {
            switch (job.getType()) {
                case DELAY:
                    executor.scheduleWithFixedDelay(job, 0, job.getDelay(), job.getDelayTimeUnit());
                    ;
                    break;
                case RATE:
                    executor.scheduleAtFixedRate(job, 0, job.getDelay(), job.getDelayTimeUnit());
                    break;
            }

        }
    }
}
