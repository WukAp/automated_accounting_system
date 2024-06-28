package com.github.wukap.automatedAccountingSystem.model.BdrvValue;

import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class SpTransactionValue implements BdrvData {
    @NotNull
    int pFfcId;
    @NotNull
    int pInfoType;
    @NotNull
    long pTrnInfo;
    @NotNull
    String pMsrTime;
}