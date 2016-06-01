package com.bbd.saas.api.mysql;

import com.bbd.saas.models.ExpressCompany;

import java.util.List;

/**
 * Created by huozhijie on 2016/5/30.
 */
public interface  ExpressCompanyService {

    /**
     *    查询出所有的list对象
     * @return List<ExpressCompanyVO>
     */
    List<ExpressCompany> getExpressCompany();

    /**
     *
     * @return
     */
    ExpressCompany  getExpressCompanyById( Integer ecompanyId);
}
