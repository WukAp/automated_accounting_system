package com.github.wukap.automatedAccountingSystem.Query.BdrvQuery;

import com.github.wukap.automatedAccountingSystem.model.BdrvValue.BdrvData;

public abstract class BdrvQuery {

    public abstract String getQuery();
    public abstract BdrvData getData();

    public BdrvQuery(BdrvData data) {

    }
}
