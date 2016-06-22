package com.bbd.saas.api.impl.mysql;

import com.bbd.saas.api.mysql.BalanceService;
import com.bbd.saas.dao.mysql.BalanceDao;
import com.bbd.saas.models.Balance;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by huozhijie on 2016/6/2.
 */
@Service("balanceService")
@Transactional
public class BalanceServiceImpl implements BalanceService {
    @Resource
    private BalanceDao balanceDao;


    public BalanceDao getBalanceDao() {
        return balanceDao;
    }

    public void setBalanceDao(BalanceDao balanceDao) {
        this.balanceDao = balanceDao;
    }

    @Override
    public void insertBalance(Balance balance) {
        balanceDao.insert(balance);
    }

    /**
     * 根据用户ID获取该用户的资金信息
     *
     * @param uid
     * @return
     */
    @Override
    public Balance findBalanceByUid(int uid) {
        return balanceDao.findBalanceByUid(uid);
    }
}
