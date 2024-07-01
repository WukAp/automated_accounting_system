package com.github.wukap.automatedAccountingSystem.query.bdrvQuery;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpTransactionValue;
import lombok.Getter;
@Getter
public class SpTransactionQuery extends BdrvQuery {

    private final String Query;
    private SpTransactionValue data;

    public SpTransactionQuery(SpTransactionValue data) {
        super(data);
        this.Query = "exec sp_transaction @p_ffc_id=" + data.getPFfcId() + ", @p_info_type=" + data.getPInfoType() + ", @p_trn_indo=" + data.getPTrnInfo() + ", @p_msg_time='" + data.getPMsrTime() + "'";
        this.data = data;
    }
}
