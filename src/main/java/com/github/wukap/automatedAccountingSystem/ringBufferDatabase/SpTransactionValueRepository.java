package com.github.wukap.automatedAccountingSystem.ringBufferDatabase;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpTransactionValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpTransactionValueRepository extends JpaRepository<SpTransactionValue, String> {

    @Query("SELECT count(*) FROM SpTransactionValue v")
    public long countValues();
}