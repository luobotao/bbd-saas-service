package com.bbd.saas.api;

import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.utils.PageModel;

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
    void save(Site site);
}
