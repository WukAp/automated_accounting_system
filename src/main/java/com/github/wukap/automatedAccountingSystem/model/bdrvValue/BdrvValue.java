package com.github.wukap.automatedAccountingSystem.model.bdrvValue;

import java.sql.Date;

public interface BdrvValue {
    String getTime();

    String getValue();

    String getId();

    String getTag();

    Date getTimeWhenWritten();
}
