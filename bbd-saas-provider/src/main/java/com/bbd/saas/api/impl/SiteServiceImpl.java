package com.bbd.saas.api.impl;


import com.bbd.saas.api.OrderService;
import com.bbd.saas.api.SiteService;
import com.bbd.saas.dao.OrderDao;
import com.bbd.saas.dao.SiteDao;
import com.bbd.saas.mongoModels.Order;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.utils.PageModel;
import org.springframework.stereotype.Service;

/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
@Service("orderService")
public class SiteServiceImpl implements SiteService {
    private SiteDao siteDao;

    public void save(Site site){
       siteDao.save(site);
    }

    public SiteDao getSiteDao() {
        return siteDao;
    }

    public void setSiteDao(SiteDao siteDao) {
        this.siteDao = siteDao;
    }
}
