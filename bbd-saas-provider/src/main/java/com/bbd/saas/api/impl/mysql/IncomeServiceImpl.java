package com.bbd.saas.api.impl.mysql;

import com.bbd.saas.api.mysql.IncomeService;
import com.bbd.saas.api.mysql.PostcompanyService;
import com.bbd.saas.dao.mysql.IncomeDao;
import com.bbd.saas.dao.mysql.PostcompanyDao;
import com.bbd.saas.models.Postcompany;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 返现Service实现
 * Created by luobotao on 2016/5/25.
 */
@Service("incomeService")
@Transactional
public class IncomeServiceImpl implements IncomeService {
    @Resource
    private IncomeDao incomeDao;

    public IncomeDao getIncomeDao() {
        return incomeDao;
    }

    public void setIncomeDao(IncomeDao incomeDao) {
        this.incomeDao = incomeDao;
    }


    /**
     * 向司机返现
     *
     * @param driverId
     * @param price
     * @param trackNo
     */
    @Override
    public void driverIncome(int driverId, double price, String trackNo) {
        Map<String, Object> parms = new HashMap<String, Object>();
        parms.put("uid", driverId);
        parms.put("amount", price);
        parms.put("trackNo", trackNo);
        parms.put("src", "driver");
        incomeDao.driverIncome(parms);
    }


}
