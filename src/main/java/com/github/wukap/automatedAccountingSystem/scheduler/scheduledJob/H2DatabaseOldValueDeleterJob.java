package com.github.wukap.automatedAccountingSystem.scheduler.scheduledJob;

import com.github.wukap.automatedAccountingSystem.h2Database.OldNoteDeletable;
import com.github.wukap.automatedAccountingSystem.model.config.InputConfig;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class H2DatabaseOldValueDeleterJob implements ScheduledJob {

    private final List<OldNoteDeletable> oldNoteDeletables;
    private final int historyDays;

    public H2DatabaseOldValueDeleterJob(List<OldNoteDeletable> oldNoteDeletables, InputConfig config) {
        this.oldNoteDeletables = oldNoteDeletables;
        this.historyDays = config.getSettings().getHistoryDays();
    }

    @Override
    public void run() {
        oldNoteDeletables.forEach(oldNoteDeletable -> oldNoteDeletable.deleteOldNotesByLifeTime(historyDays));
    }

    @Override
    public Type getType() {
        return Type.DELAY;
    }

    @Override
    public int getDelay() {
        return historyDays; // Convert days to seconds
    }

    @Override
    public TimeUnit getDelayTimeUnit() {
        return TimeUnit.DAYS;
    }
}
