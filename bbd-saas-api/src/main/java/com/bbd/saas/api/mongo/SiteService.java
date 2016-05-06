package com.bbd.saas.api.mongo;

import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.vo.SiteVO;
import org.mongodb.morphia.Key;

import java.util.List;

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

    /**
     * 根据ID获取Site
     * @param id
     * @return
     */
    Site findSite(String id);

    /**
     * Description: 根据站点编号查询站点
     * @param areaCode 站点编号
     * @return
     * @author: liyanlei
     * 2016年4月19日上午10:29:29
     */
    Site findSiteByAreaCode(String areaCode);

    /**
     * Description: 查询同一公司下除站点site的其他所有站点的VO对象
     * @param selfSite 站点
     * @return
     * @author: liyanlei
     * 2016年4月18日上午10:20:30
     */
    List<SiteVO> findAllOtherSiteVOList(Site selfSite);

    /**
     * 查询指定公司下的所有站点
     * @param companyId 公司名称
     * @return
     * @date 2016/5/5 17:59
     * @auth liyanlei
     */
    List<SiteVO> findAllSiteVOByCompanyId(String companyId);
}
