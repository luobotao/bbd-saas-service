package com.bbd.saas.api.impl.mysql;

import com.bbd.saas.api.mysql.PushService;
import com.bbd.saas.dao.mysql.PushDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 返现Service实现
 * Created by luobotao on 2016/5/25.
 */
@Service("pushService")
@Transactional
public class PushServiceImpl implements PushService {
    @Resource
    private PushDao pushDao;

    public PushDao getPushDao() {
        return pushDao;
    }

    public void setPushDao(PushDao pushDao) {
        this.pushDao = pushDao;
    }

    /**
     * 订单已完成push消息
     * @param uid 小件员Id
     * @param typ 类型 2
     * @param tradeNo 订单号
     */
    public void tradePush(int uid, String typ, String tradeNo) {
        Map<String, Object> parms = new HashMap<String, Object>();
        parms.put("uid", uid);
        parms.put("typ", typ);
        parms.put("tradeNo", tradeNo);
        pushDao.tradePush(parms);
    }


}
