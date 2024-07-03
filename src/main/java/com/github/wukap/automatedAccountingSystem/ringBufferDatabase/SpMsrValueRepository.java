package com.github.wukap.automatedAccountingSystem.ringBufferDatabase;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsrValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpMsrValueRepository extends JpaRepository<SpMsrValue, String> {

    @Query("SELECT count(*) FROM SpMsrValue v")
    public long countValues();
}