package com.bbd.saas.api;

import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.utils.PageModel;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;

/**
 * Created by luobotao on 2016/4/11.
 * 站点接口
 */
public interface SiteService {

    /**
     * 保存站点
     * @param site
     * @return
     */
    Key<Site> save(Site site);

    Site findSite(String id);
}
