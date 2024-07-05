package com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.query;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.BdrvValue;

public abstract class BdrvQuery {

    public abstract String getQuery();

    public abstract BdrvValue getData();

    protected int ffcId;

    public BdrvQuery(int ffcId) {
        this.ffcId = ffcId;
    }

}
