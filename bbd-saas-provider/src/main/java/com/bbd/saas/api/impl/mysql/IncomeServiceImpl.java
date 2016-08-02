package com.bbd.saas.api.impl.mysql;

import com.bbd.saas.api.mysql.IncomeService;
import com.bbd.saas.api.mysql.PostcompanyService;
import com.bbd.saas.dao.mysql.IncomeDao;
import com.bbd.saas.dao.mysql.PostcompanyDao;
import com.bbd.saas.models.Postcompany;
import com.bbd.saas.models.SmsInfo;
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

    @Override
    public void expresstomysql(String mailNum, String printDate, String packDate, String driverDate, String stationDate,
                               String deliveryDate, String successDate, String dailyDate, String refuseDate, String changeStationDate, String changeExpressDate,
                               String areaCode,String sitename, String city, String province, String companycode, String companyId,
                               String companyName,String src, String expressSource, String driver_Id) {
        Map<String, Object> parms = new HashMap<String, Object>();
        parms.put("mailNum", mailNum);
        parms.put("printDate", printDate);
        parms.put("packDate", packDate);
        parms.put("driverDate", driverDate);
        parms.put("stationDate", stationDate);
        parms.put("deliveryDate", deliveryDate);
        parms.put("successDate", successDate);
        parms.put("dailyDate", dailyDate);
        parms.put("refuseDate", refuseDate);
        parms.put("changeStationDate", changeStationDate);
        parms.put("changeExpressDate", changeExpressDate);
        parms.put("areaCode", areaCode);
        parms.put("sitename", sitename);
        parms.put("city", city);
        parms.put("province", province);
        parms.put("companycode", companycode);
        parms.put("companyId", companyId);
        parms.put("companyName", companyName);
        parms.put("src", src);
        parms.put("expressSource", expressSource);
        parms.put("driver_Id", driver_Id);
        incomeDao.expresstomysql(parms);
    }
}
