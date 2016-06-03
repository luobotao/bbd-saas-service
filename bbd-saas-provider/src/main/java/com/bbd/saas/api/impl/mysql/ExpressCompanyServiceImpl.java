package com.bbd.saas.api.impl.mysql;

import com.bbd.saas.api.mysql.ExpressCompanyService;
import com.bbd.saas.dao.mysql.ExpressCompanyDao;
import com.bbd.saas.models.ExpressCompany;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by huozhijie on 2016/5/30.
 */
 /*查询所有快递公司实现*/
@Service("expressCompanyService")
@Transactional
public class ExpressCompanyServiceImpl implements ExpressCompanyService {
    @Resource
    private ExpressCompanyDao expressCompanyDao;

    public ExpressCompanyDao getExpressCompanyDao() {
        return expressCompanyDao;
    }

    public void setExpressCompanyDao(ExpressCompanyDao expressCompanyDao) {
        this.expressCompanyDao = expressCompanyDao;
    }

    /**
     * 查询所有的快递公司
     * @return
     */
    @Override
    public List<ExpressCompany> getExpressCompany() {
        List<ExpressCompany> expressCompanys = expressCompanyDao.getExpressCompany();
        return expressCompanys;
    }

    @Override
    public ExpressCompany getExpressCompanyById(Integer ecompanyId) {
        ExpressCompany expressCompany=  expressCompanyDao.getExpressCompanyById(ecompanyId);
        return expressCompany;
    }
}
