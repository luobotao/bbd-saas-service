package com.bbd.saas.api.mongo;

import java.util.List;

import org.mongodb.morphia.Key;

import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.vo.SiteVO;

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
    
    /**
     * Description: 查询除站点号为areaCode的其他所有站点的VO对象
     * @param otherAreaCode 站点编号
     * @return
     * @author: liyanlei
     * 2016年4月18日上午10:20:30
     */
    List<SiteVO> findAllOtherSiteVOList(String areaCode);
}
