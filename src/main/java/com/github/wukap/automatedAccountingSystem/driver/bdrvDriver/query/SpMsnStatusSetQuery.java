package com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.query;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsnStatusSetValue;
import lombok.Getter;

@Getter
public class SpMsnStatusSetQuery extends BdrvQuery {
    private SpMsnStatusSetValue data;

    public SpMsnStatusSetQuery(SpMsnStatusSetValue data, int ffcId) {
        super(ffcId);
        this.data = data;
    }

    @Override
    public String getQuery() {
        return "exec sp_msn_status_set @p_ffc_id=" + this.ffc_id + ", @p_msn_id=" + data.getPMsnId() + ", @p_mns_id=" + data.getPMnsId() + ", @p_set_time='" + data.getPSetTime() + "'";
    }

}
