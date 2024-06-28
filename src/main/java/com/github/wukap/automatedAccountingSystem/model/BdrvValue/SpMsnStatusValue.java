package com.github.wukap.automatedAccountingSystem.model.BdrvValue;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class SpMsnStatusValue implements BdrvValue {
    @NotNull
    int pFfcId;
    @NotNull
    int pMsnId;
    @NotNull
    int pMnsId;
    @NotNull
    String pSetTime;
}
