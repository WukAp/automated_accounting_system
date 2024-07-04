package com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.query;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.BdrvValue;
import com.github.wukap.automatedAccountingSystem.model.config.InputConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

public abstract class BdrvQuery {

    public abstract String getQuery();
    public abstract BdrvValue getData();
    protected int ffc_id;

   public BdrvQuery(int ffc_id) {
       this.ffc_id = ffc_id;
   }

}
