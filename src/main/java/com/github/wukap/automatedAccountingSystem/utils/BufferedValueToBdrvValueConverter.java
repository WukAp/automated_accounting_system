package com.github.wukap.automatedAccountingSystem.utils;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.BdrvValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsnStatusSetValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsrValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpTransactionValue;
import com.github.wukap.automatedAccountingSystem.ringBufferDatabase.BufferedValue;

public class BufferedValueToBdrvValueConverter {
    private BufferedValueToBdrvValueConverter() {
    }

    public static BdrvValue convert(BufferedValue bufferedValue) {
        return switch (bufferedValue.getType()) {
            case BufferedValue.Type.SpMsrValue ->
                    new SpMsrValue(bufferedValue.getParameter1(), bufferedValue.getParameter2(), bufferedValue.getParameter3(), bufferedValue.getTimestamp());
            case BufferedValue.Type.SpTransactionValue ->
                    new SpTransactionValue(bufferedValue.getParameter1(), bufferedValue.getParameter2(), bufferedValue.getParameter3(), bufferedValue.getTimestamp());
            case BufferedValue.Type.SpMsnStatusSetValue ->
                    new SpMsnStatusSetValue(bufferedValue.getParameter1(), bufferedValue.getParameter2(), bufferedValue.getParameter3(), bufferedValue.getTimestamp());
        };
    }

    public static SpMsrValue convertToSpMsrValue(BufferedValue bufferedValue) {
        if (bufferedValue.getType() != BufferedValue.Type.SpMsrValue) {
            throw new IllegalArgumentException();
        }
        return new SpMsrValue(bufferedValue.getParameter1(), bufferedValue.getParameter2(), bufferedValue.getParameter3(), bufferedValue.getTimestamp());
    }

    public static SpTransactionValue convertToSpTransactionValue(BufferedValue bufferedValue) {
        if (bufferedValue.getType() != BufferedValue.Type.SpTransactionValue) {
            throw new IllegalArgumentException();
        }
        return new SpTransactionValue(bufferedValue.getParameter1(), bufferedValue.getParameter2(), bufferedValue.getParameter3(), bufferedValue.getTimestamp());
    }

    public static SpMsnStatusSetValue convertToSpMsnStatusSetValue(BufferedValue bufferedValue) {
        if (bufferedValue.getType() != BufferedValue.Type.SpMsnStatusSetValue) {
            throw new IllegalArgumentException();
        }
        return new SpMsnStatusSetValue(bufferedValue.getParameter1(), bufferedValue.getParameter2(), bufferedValue.getParameter3(), bufferedValue.getTimestamp());
    }
}
