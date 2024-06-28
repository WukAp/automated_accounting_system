package com.github.wukap.automatedAccountingSystem.Query.BdrvQuery;

import com.github.wukap.automatedAccountingSystem.model.BdrvValue.SpMsnStatusValue;
import com.github.wukap.automatedAccountingSystem.model.BdrvValue.SpMsrValue;
import lombok.Getter;

@Getter
public class SpMsnStatusSet extends BdrvQuery {
    private final String Query;
    private SpMsnStatusValue data;

    public SpMsnStatusSet(SpMsnStatusValue data) {
        super(data);
        this.Query = "exec sp_msn_status_set @p_ffc_id=" + data.getPFfcId() + ", @p_msn_id=" + data.getPMsnId() + ", @p_mns_id=" + data.getPFfcId() + ", @p_set_time='" + data.getPSetTime() + "'";
        this.data = data;
    }
}
