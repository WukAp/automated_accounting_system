package com.github.wukap.automatedAccountingSystem.ringBufferDatabase;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
public class BufferedValue {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NonNull
    private Integer parameter1;
    @NonNull
    private Integer parameter2;
    @NonNull
    private Integer parameter3;
    @NonNull
    private String timestamp;
    @NonNull
    private Type type;

    public enum Type {
        SpMsrValue, SpTransactionValue, SpMsnStatusSetValue

    }
}