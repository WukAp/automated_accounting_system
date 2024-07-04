package com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.query;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsrValue;
import lombok.Getter;

@Getter
public class SpMsrValueQuery extends BdrvQuery {
    private SpMsrValue data;

    public SpMsrValueQuery(SpMsrValue data, int ffcId) {
        super(ffcId);
        this.data = data;
    }
    @Override
    public String getQuery() {
        return"exec sp_msr_value_send @p_ffc_id=" + this.ffc_id + ", @p_msd_id=" + data.getPMsdId() + ", @p_msr_value=" + data.getPMsrValue() + ", @p_msr_time='" + data.getPMsrTime() + "'";
    }
}
