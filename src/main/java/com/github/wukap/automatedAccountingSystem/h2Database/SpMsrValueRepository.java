package com.github.wukap.automatedAccountingSystem.h2Database;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsrValue;
import com.github.wukap.automatedAccountingSystem.utils.OpcValueToBdrvValueConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Repository
public interface SpMsrValueRepository extends JpaRepository<SpMsrValue, String>, OldNoteDeletable {

    @Query("SELECT v FROM  SpMsrValue v ORDER BY pMsrTime LIMIT 1")
    public SpMsrValue findFirst();

    @Transactional
    @Modifying
    @Query("DELETE FROM SpMsrValue v WHERE v.pMsrTime < ?1")
    public void deleteOldNotesByLastPossibleTimestamp(String lastPossibleTimeStamp);

    default void deleteOldNotesByLifeTime(int lifeTimeInDays) {
        var lastPossibleTimeStamp = OpcValueToBdrvValueConverter.instantToFormattedStringConverter(LocalDateTime.now().minusDays(lifeTimeInDays).atZone(ZoneId.of("UTC")).toInstant());
        deleteOldNotesByLastPossibleTimestamp(lastPossibleTimeStamp);
    }

    ;
}