package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.ExpressCompany;

import java.util.List;
import java.util.Map;

/**
 * Created by huozhijie on 2016/5/30.
 */
public interface ExpressCompanyDao {

    /**  查询所有的快递公司
     *
     * @return
     */
    List<ExpressCompany> getExpressCompany();

    /**
     * 根据id查询
     * @param ecompanyId
     * @return
     */
    ExpressCompany getExpressCompanyById(Integer ecompanyId);

    /**
     * app端查询所有的快递公司
     * @return {{companyname,companycode},{companyname,companycode}---}
     */
    List<Map<String,String>> selectCompanyList();
}

