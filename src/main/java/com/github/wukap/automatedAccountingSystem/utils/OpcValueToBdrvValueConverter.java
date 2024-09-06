package com.github.wukap.automatedAccountingSystem.utils;

import com.github.wukap.automatedAccountingSystem.model.OpcValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsnStatusSetValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsrValue;
import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpTransactionValue;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class OpcValueToBdrvValueConverter {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    private OpcValueToBdrvValueConverter() {
    }

    public static String instantToFormattedStringConverter(Instant sourceTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(sourceTime, ZoneId.of("UTC"));
        return formatter.format(zonedDateTime);
    }

    public static ZonedDateTime formattedStringToZonedDateTimeConverter(String formattedTime) {
        LocalDateTime localDateTime = LocalDateTime.parse(formattedTime, formatter);
        return localDateTime.atZone(ZoneId.of("UTC"));
    }

    public static SpMsrValue opcValueToSpMsrValueConverter(String tag, OpcValue opcValue, String pMsnId) {
        if (opcValue == null) return null;
        String formattedSourceTime = instantToFormattedStringConverter(opcValue.getSourceTime());
        return new SpMsrValue(opcValue.getValue().toString(), pMsnId, formattedSourceTime, tag);
    }

    public static SpMsnStatusSetValue opcValueToSpMsnStatusSetValueConverter(String tag, OpcValue opcValue, String uuId) {
        if (opcValue == null) return null;
        String formattedSourceTime = instantToFormattedStringConverter(opcValue.getSourceTime());
        return new SpMsnStatusSetValue(uuId, String.valueOf(opcValue.getValue()), formattedSourceTime, tag);
    }

    public static SpTransactionValue opcValueToSpTransactionValueConverter(String tag, OpcValue valueStart, OpcValue valueTag1, OpcValue valueTag2, OpcValue valueTag3, OpcValue valueTag4) {
        if (valueStart == null || valueTag1 == null || valueTag2 == null || valueTag3 == null || valueTag4 == null)
            return null;
        String formattedSourceTime = instantToFormattedStringConverter(valueStart.getSourceTime());
        String pTrnInfo = valueTag2.getValue().toString() + valueTag3.getValue().toString() + valueTag4.getValue().toString();
        return new SpTransactionValue(valueTag1.toString(), pTrnInfo, formattedSourceTime, tag);
    }
}
