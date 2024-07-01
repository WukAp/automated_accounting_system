package com.github.wukap.automatedAccountingSystem.query.bdrvQuery;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsnStatusSetValue;
import lombok.Getter;

@Getter
public class SpMsnStatusSetQuery extends BdrvQuery {
    private final String query;
    private SpMsnStatusSetValue data;

    public SpMsnStatusSetQuery(SpMsnStatusSetValue data) {
        super(data);
        this.query = "exec sp_msn_status_set @p_ffc_id=" + data.getPFfcId() + ", @p_msn_id=" + data.getPMsnId() + ", @p_mns_id=" + data.getPFfcId() + ", @p_set_time='" + data.getPSetTime() + "'";
        this.data = data;
    }
}
