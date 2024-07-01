package com.github.wukap.automatedAccountingSystem.ringBufferDatabase;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsrValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BufferedValueRepository extends JpaRepository<BufferedValue, Long> {
    @Query("SELECT v FROM BufferedValue v WHERE v.type = ?1 ORDER BY v.timestamp")
    public List<BufferedValue> findAllByType(BufferedValue.Type type);
}