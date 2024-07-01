package com.github.wukap.automatedAccountingSystem.query.bdrvQuery;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsrValue;
import lombok.Getter;

@Getter
public class SpMsrValueQuery extends BdrvQuery {
    private final String Query;
    private SpMsrValue data;

    public SpMsrValueQuery(SpMsrValue data) {
        super(data);
        this.Query = "exec sp_msr_value_send @p_ffc_id=" + data.getPFfcId() + ", @p_msd_id=" + data.getPMsdId() + ", @p_msr_value=" + data.getPMsrValue() + ", @p_msr_time='" + data.getPMsrTime() + "'";
        this.data = data;
    }
}
