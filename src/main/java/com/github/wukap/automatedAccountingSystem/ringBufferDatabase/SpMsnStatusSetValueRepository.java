package com.github.wukap.automatedAccountingSystem.ringBufferDatabase;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsnStatusSetValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SpMsnStatusSetValueRepository extends JpaRepository<SpMsnStatusSetValue, String> {

    @Query("SELECT count(*) FROM SpMsnStatusSetValue v")
    public long countValues();
}