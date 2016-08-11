package com.bbd.saas.api.mysql;

import com.bbd.saas.models.PostmanUser;
import com.bbd.saas.models.SiteMySql;
import com.bbd.saas.models.SiteMySql;
import com.bbd.saas.vo.UserVO;

import java.util.List;
import java.util.Map;

/**
 * 公司Service
 * Created by zuowenhai on 2016/4/16.
 */
public interface SiteMySqlService {

    /**
     * 根据siteid查询站点记录
     */
    public SiteMySql selectIdBySiteId(String siteid);

    public int updateSiteDayCntBySiteId(String siteid);
}
