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
}
