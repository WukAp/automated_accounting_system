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
public class SpMsrValue implements BdrvValue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String databaseId;

    @NonNull
    private String pMsrValue;

    @NonNull
    private String pMsdId;

    @NonNull
    private String pMsrTime;

    @NonNull
    String tag;

    @NonNull
    Date timeWhenWritten;

    public SpMsrValue(
                      @NonNull String pMsrValue,
                      @NonNull String pMsdId, @NonNull String pMsrTime, @NonNull String tag) {
        this.pMsrValue = pMsrValue;
        this.pMsdId = pMsdId;
        this.pMsrTime = pMsrTime;
        this.tag = tag;
        this.timeWhenWritten = new Date(System.currentTimeMillis());
    }

    @Override
    public String getTime() {
        return pMsrTime;
    }

    @Override
    public String getValue() {
        return pMsrValue;
    }

    @Override
    public String getId() {
        return pMsdId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpMsrValue that = (SpMsrValue) o;
        return Objects.equals(pMsrValue, that.pMsrValue) && Objects.equals(pMsdId, that.pMsdId) && Objects.equals(pMsrTime, that.pMsrTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pMsrValue, pMsdId, pMsrTime);
    }
}