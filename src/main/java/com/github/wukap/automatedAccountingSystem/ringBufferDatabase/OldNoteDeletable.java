package com.github.wukap.automatedAccountingSystem.ringBufferDatabase;

public interface OldNoteDeletable {
    void deleteOldNotesByLifeTime(int lifeTimeInDays);
}
