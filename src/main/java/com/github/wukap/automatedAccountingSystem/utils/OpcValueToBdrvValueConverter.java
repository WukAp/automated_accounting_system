package com.github.wukap.automatedAccountingSystem.utils;

import com.github.wukap.automatedAccountingSystem.model.OpcValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsnStatusSetValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsrValue;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class OpcValueToBdrvValueConverter {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private OpcValueToBdrvValueConverter() {
    }

    public static String instantToFormattedStringConverter(Instant sourceTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(sourceTime, ZoneId.of("Europe/Moscow"));
        return formatter.format(zonedDateTime);
    }

    public static SpMsrValue opcValueToSpMsnStatusSetValue(OpcValue opcValue, String pMsnId) {
        if (opcValue == null) return null;
        String formattedSourceTime = instantToFormattedStringConverter(opcValue.getSourceTime());
        return new SpMsrValue(opcValue.getValue().toString(), pMsnId, formattedSourceTime);
    }
}
