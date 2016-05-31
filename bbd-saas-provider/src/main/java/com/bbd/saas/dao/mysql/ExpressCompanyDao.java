package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.ExpressCompany;

import java.util.List;

/**
 * Created by huozhijie on 2016/5/30.
 */
public interface ExpressCompanyDao {

    /**  查询所有的快递公司
     *
     * @return
     */
    List<ExpressCompany> getExpressCompany();
}
