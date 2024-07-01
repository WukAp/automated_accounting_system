package com.github.wukap.automatedAccountingSystem.model.bdrvValue;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class SpMsnStatusSetValue implements BdrvValue {
    @NotNull
    int pFfcId;
    @NotNull
    int pMsnId;
    @NotNull
    int pMnsId;
    @NotNull
    String pSetTime;
}
