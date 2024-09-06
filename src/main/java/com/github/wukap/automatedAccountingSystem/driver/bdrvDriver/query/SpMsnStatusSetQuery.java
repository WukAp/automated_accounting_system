package com.github.wukap.automatedAccountingSystem.driver.bdrvDriver.query;

import com.github.wukap.automatedAccountingSystem.model.bdrvValue.SpMsnStatusSetValue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.Query;

@Getter
@Slf4j
public class SpMsnStatusSetQuery extends BdrvQuery {
    private SpMsnStatusSetValue data;

    public SpMsnStatusSetQuery(SpMsnStatusSetValue data, int ffcId) {
        super(ffcId);
        this.data = data;
    }

    @Override
    public String getQuery() {
        String query = "exec sp_msn_status_set @p_ffc_id=" + this.ffcId + ", @p_msn_id=" + data.getPMsnId() + ", @p_mns_id=" + data.getPMnsId() + ", @p_set_time='" + data.getPSetTime() + "'";
log.info(query);
        return query; }

}
