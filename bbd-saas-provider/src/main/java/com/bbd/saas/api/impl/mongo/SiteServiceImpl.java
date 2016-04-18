package com.bbd.saas.api.impl.mongo;


import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.dao.mongo.SiteDao;
import com.bbd.saas.mongoModels.Site;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.springframework.stereotype.Service;

/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
@Service("siteService")
public class SiteServiceImpl implements SiteService {
    private SiteDao siteDao;

    public SiteDao getSiteDao() {
        return siteDao;
    }

    public void setSiteDao(SiteDao siteDao) {
        this.siteDao = siteDao;
    }


    @Override
    public Key<Site> save(Site site){
        return siteDao.save(site);
    }

    @Override
    public Site findSite(String id) {
        return siteDao.findOne("_id",new ObjectId(id));
    }
}
