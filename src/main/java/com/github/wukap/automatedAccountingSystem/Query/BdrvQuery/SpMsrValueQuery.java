package com.github.wukap.automatedAccountingSystem.Query.BdrvQuery;

import com.github.wukap.automatedAccountingSystem.model.BdrvValue.SpMsrValue;
import lombok.Getter;

public class SpMsrValueQuery extends BdrvQuery {
    @Getter
    private final String Query;
    @Getter
    private SpMsrValue data;

    public SpMsrValueQuery(SpMsrValue data) {
        super(data);
        this.Query = "exec sp_msr_value_send @p_ffc_id=" + data.getPFfcId() + ", @p_msd_id=" + data.getPMsdId() + ", @p_msr_value=" + data.getPMsrValue() + ", @p_msr_time='" + data.getPMsrTime() + "'";
        this.data = data;
    }
}
