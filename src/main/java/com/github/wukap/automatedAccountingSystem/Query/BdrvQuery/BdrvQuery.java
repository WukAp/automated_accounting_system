package com.github.wukap.automatedAccountingSystem.Query.BdrvQuery;

import com.github.wukap.automatedAccountingSystem.model.BdrvValue.BdrvValue;

public abstract class BdrvQuery {

    public abstract String getQuery();
    public abstract BdrvValue getData();

    public BdrvQuery(BdrvValue data) {

    }
}
