package com.github.wukap.automatedAccountingSystem.query.bdrvQuery;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsnStatusValue;
import lombok.Getter;

@Getter
public class SpMsnStatusSet extends BdrvQuery {
    private final String query;
    private SpMsnStatusValue data;

    public SpMsnStatusSet(SpMsnStatusValue data) {
        super(data);
        this.query = "exec sp_msn_status_set @p_ffc_id=" + data.getPFfcId() + ", @p_msn_id=" + data.getPMsnId() + ", @p_mns_id=" + data.getPFfcId() + ", @p_set_time='" + data.getPSetTime() + "'";
        this.data = data;
    }
}
