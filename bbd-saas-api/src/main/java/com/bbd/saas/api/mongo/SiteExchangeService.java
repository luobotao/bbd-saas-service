package com.bbd.saas.api.mongo;

import com.bbd.saas.mongoModels.SiteExchange;

/**
 * Created by huozhijie on 2016/6/15.
 */

public interface SiteExchangeService {
    /**
     * 查找一个expressExchange对象
     * @return
     */
    SiteExchange findSiteExchangeByCodeAndTyp(String code, String typ);

    /**
     * 根据areaCode查找一个expressExchange对象
     * @param areaCode 站点编号
     * @return expressExchange对象
     */
    SiteExchange findOneByAreaCode(String areaCode);
}
