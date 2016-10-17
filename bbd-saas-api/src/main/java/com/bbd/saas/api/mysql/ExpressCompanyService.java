package com.bbd.saas.api.mysql;

import com.bbd.saas.models.ExpressCompany;

import java.util.List;
import java.util.Map;

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
     * 根据快递公司Id获取公司对象
     * @param ecompanyId 快递公司Id
     * @return 快递公司对象
     */
    ExpressCompany  getExpressCompanyById( Integer ecompanyId);

    /**
     * app端查询所有的快递公司
     * @return {{companyname,companycode},{companyname,companycode}---}
     */
    List<Map<String,String>> findCompanyList();
}
