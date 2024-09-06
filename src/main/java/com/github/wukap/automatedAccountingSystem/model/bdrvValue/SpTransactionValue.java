package com.github.wukap.automatedAccountingSystem.model.bdrvValue;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Date;
import java.util.Objects;

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
    private String pMsgTime;
    @NonNull
    String tag;

    @NonNull
    Date timeWhenWritten;

    public SpTransactionValue(
                              @NonNull String pInfoType,
                              @NonNull String pTrnInfo, @NonNull String pMsgTime, @NonNull String tag) {
        this.pInfoType = pInfoType;
        this.pTrnInfo = pTrnInfo;
        this.pMsgTime = pMsgTime;
        this.tag = tag;
        this.timeWhenWritten = new Date(System.currentTimeMillis());
    }

    @Override
    public String getTime() {
        return pMsgTime;
    }

    @Override
    public String getValue() {
        return pTrnInfo;
    }

    @Override
    public String getId() {
        return pInfoType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpTransactionValue that = (SpTransactionValue) o;
        return Objects.equals(pInfoType, that.pInfoType) && Objects.equals(pTrnInfo, that.pTrnInfo) && Objects.equals(pMsgTime, that.pMsgTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pInfoType, pTrnInfo, pMsgTime);
    }
}