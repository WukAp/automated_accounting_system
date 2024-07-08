package com.github.wukap.automatedAccountingSystem.model.bdrvValue;

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
public class SpTransactionValue implements BdrvValue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String databaseId;

    @NonNull
    private String pInfoType;

    @NonNull
    private String pTrnInfo;

    @NonNull
    private String pMsrTime;
    @NonNull
    String tag;

    @Override
    public String getTime() {
        return pMsrTime;
    }

    @Override
    public String getValue() {
        return pTrnInfo;
    }

    @Override
    public String getId() {
        return pInfoType;
    }
}