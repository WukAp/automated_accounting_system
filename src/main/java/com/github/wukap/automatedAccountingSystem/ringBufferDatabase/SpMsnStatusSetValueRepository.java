package com.github.wukap.automatedAccountingSystem.ringBufferDatabase;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsnStatusSetValue;
import com.github.wukap.automatedAccountingSystem.utils.OpcValueToBdrvValueConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Repository
public interface SpMsnStatusSetValueRepository extends JpaRepository<SpMsnStatusSetValue, String>, OldNoteDeletable {

    @Transactional
    @Modifying
    @Query("DELETE FROM SpMsnStatusSetValue v WHERE v.pSetTime < ?1")
    public void deleteOldNotesByLastPossibleTimestamp(String lastPossibleTimeStamp);

    default void deleteOldNotesByLifeTime(int lifeTimeInDays) {
        var lastPossibleTimeStamp = OpcValueToBdrvValueConverter.instantToFormattedStringConverter(LocalDateTime.now().minusDays(lifeTimeInDays).atZone(ZoneId.of("UTC")).toInstant());
        deleteOldNotesByLastPossibleTimestamp(lastPossibleTimeStamp);
    }
}