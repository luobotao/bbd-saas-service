package com.bbd.saas.api.impl.mongo;

import com.bbd.saas.api.mongo.ExpressExchangeService;
import com.bbd.saas.api.mongo.SiteExchangeService;
import com.bbd.saas.dao.mongo.ExpressExchangeDao;
import com.bbd.saas.dao.mongo.SiteExchangeDao;
import com.bbd.saas.mongoModels.ExpressExchange;
import com.bbd.saas.mongoModels.SiteExchange;
import org.mongodb.morphia.Key;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by huozhijie on 2016/6/15.
 */
public class SiteExchangeServiceImpl implements SiteExchangeService {
    public static final Logger logger = LoggerFactory.getLogger(SiteExchangeServiceImpl.class);
    @Autowired
    private SiteExchangeDao siteExchangeDao;


    public SiteExchangeDao getSiteExchangeDao() {
        return siteExchangeDao;
    }

    public void setSiteExchangeDao(SiteExchangeDao siteExchangeDao) {
        this.siteExchangeDao = siteExchangeDao;
    }

    @Override
    public SiteExchange findSiteExchangeByCodeAndTyp(String code, String typ) {
        return siteExchangeDao.findSiteExchangeByCodeAndTyp(code,typ);
    }
}


