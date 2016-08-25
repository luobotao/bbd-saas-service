package com.bbd.saas.dao.mysql;

import com.bbd.saas.models.SiteMySql;
import org.apache.ibatis.annotations.Param;


public interface SiteDao {

    /**
     * 根据siteid查询站点记录
     */
    public SiteMySql selectIdBySiteId(@Param("siteid") String siteid);

    public int updateSiteDayCntBySiteId(@Param("siteid")String siteid);

    /**
     * 插入一条记录
     * @param siteMySql 实体类对象
     * @return
     */
    int insert(SiteMySql siteMySql);

    /**
     * 更新一条记录
     * @param siteMySql 实体类对象
     * @return
     */
    int update(SiteMySql siteMySql);

    /**
     * 根据siteid查询站点记录条数
     * @param siteid 站点id
     * @return 记录条数
     */
    public int selectCountBySiteId(@Param("siteid") String siteid);

}
