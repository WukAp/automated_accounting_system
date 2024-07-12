package com.github.wukap.automatedAccountingSystem.h2Database;

public interface OldNoteDeletable {

    void deleteOldNotesByLifeTime(int lifeTimeInDays);
}
