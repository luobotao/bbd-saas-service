package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.PostmanUser;
import com.bbd.saas.models.SiteMySql;
import com.bbd.saas.models.SiteMySql;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


public interface SiteDao {

    /**
     * 根据siteid查询站点记录
     */
    public SiteMySql selectIdBySiteId(@Param("siteid") String siteid);

    public int updateSiteDayCntBySiteId(@Param("siteid")String siteid);

}
