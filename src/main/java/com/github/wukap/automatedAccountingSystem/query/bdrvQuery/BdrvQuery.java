package com.github.wukap.automatedAccountingSystem.query.bdrvQuery;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.BdrvValue;

public abstract class BdrvQuery {

    public abstract String getQuery();
    public abstract BdrvValue getData();

    public BdrvQuery(BdrvValue data) {

    }
}
