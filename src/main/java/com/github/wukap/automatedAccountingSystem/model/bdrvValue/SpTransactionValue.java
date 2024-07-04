package com.github.wukap.automatedAccountingSystem.model.bdrvValue;

import com.github.wukap.automatedAccountingSystem.model.config.InputConfig;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class SpTransactionValue extends BdrvValue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @NonNull
    private String pInfoType;

    @NonNull
    private String pTrnInfo;

    @NonNull
    private String pMsrTime;

}