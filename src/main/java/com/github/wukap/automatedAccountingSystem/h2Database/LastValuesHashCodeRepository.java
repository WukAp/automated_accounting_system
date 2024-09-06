package com.github.wukap.automatedAccountingSystem.h2Database;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LastValuesHashCodeRepository extends JpaRepository<LastValuesHashCodeRepository.LastValuesHashCode, String> {


    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class LastValuesHashCode {
        @Id
        private String tag;
        private int hash;
    }
}