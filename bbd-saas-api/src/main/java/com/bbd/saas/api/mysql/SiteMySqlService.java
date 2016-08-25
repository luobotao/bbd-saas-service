package com.bbd.saas.api.mysql;

import com.bbd.saas.models.SiteMySql;

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

    /**
     * 保存或者更新一条记录(saveOrUpdate)
     * @param siteMySql 实体对象
     * @return
     */
    public int save(SiteMySql siteMySql);

}
