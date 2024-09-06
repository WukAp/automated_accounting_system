package com.github.wukap.automatedAccountingSystem.model.bdrvValue;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
public class SpMsnStatusSetValue implements BdrvValue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String databaseId;

    @NonNull
    private String pMsnId;

    @NonNull
    private String pMnsId;

    @NonNull
    private String pSetTime;
    @NonNull
    String tag;

    @NonNull
    Date timeWhenWritten;

    public SpMsnStatusSetValue(
                               @NonNull String pMsnId,
                               @NonNull String pMnsId, @NonNull String pSetTime, @NonNull String tag) {
        this.pMsnId = pMsnId;
        this.pMnsId = pMnsId;
        this.pSetTime = pSetTime;
        this.tag = tag;
        this.timeWhenWritten = new Date(System.currentTimeMillis());
    }


    @Override
    public String getTime() {
        return pSetTime;
    }

    @Override
    public String getId() {
        return pMsnId + ":" + pMnsId;
    }


    @Override
    public String getValue() {
        return "-";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SpMsnStatusSetValue that = (SpMsnStatusSetValue) o;
        return Objects.equals(pMsnId, that.pMsnId) && Objects.equals(pMnsId, that.pMnsId) && Objects.equals(pSetTime, that.pSetTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pMsnId, pMnsId, pSetTime);
    }
}
