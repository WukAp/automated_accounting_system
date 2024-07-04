package com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.query;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpTransactionValue;
import lombok.Getter;

@Getter
public class SpTransactionQuery extends BdrvQuery {
    private SpTransactionValue data;

    public SpTransactionQuery(SpTransactionValue data, int ffcId) {
        super(ffcId);
        this.data = data;
    }

    @Override
    public String getQuery() {
        return "exec sp_transaction @p_ffc_id=" + this.ffc_id  + ", @p_info_type=" + data.getPInfoType() + ", @p_trn_indo=" + data.getPTrnInfo() + ", @p_msg_time='" + data.getPMsrTime() + "'";
    }
}
