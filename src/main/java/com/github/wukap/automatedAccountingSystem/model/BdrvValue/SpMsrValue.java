package com.github.wukap.automatedAccountingSystem.model.BdrvValue;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class SpMsrValue implements BdrvValue {
    @NotNull
    int pFfcId;
    @NotNull
    int pMsrValue;
    @NotNull
    int pMsdId;
    @NotNull
    String pMsrTime;
}