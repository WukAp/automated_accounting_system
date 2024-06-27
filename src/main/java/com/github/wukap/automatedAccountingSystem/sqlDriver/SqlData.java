package com.github.wukap.automatedAccountingSystem.sqlDriver;

import jakarta.validation.constraints.NotNull;

public record SqlData(@NotNull int pFfcId, @NotNull int pMsrValue, @NotNull int pMsdId, @NotNull String pMsrTime)
    {
    }