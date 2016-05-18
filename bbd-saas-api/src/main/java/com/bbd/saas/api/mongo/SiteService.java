package com.bbd.saas.api.mongo;

import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.SiteVO;
import org.mongodb.morphia.Key;

import java.util.List;

/**
 * Created by luobotao on 2016/4/11.
 * 站点接口
 */
public interface SiteService {
    /**
     *
     * @param address
     * @return
     */
    String dealOrderWithGetAreaCode(String address);
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
     * 根据手机号查询站点
     * @param phone
     * @return
     */
    Site findSiteByUserName(String phone);
    /**
     * Description: 查询同一公司下除站点site的其他所有站点的VO对象
     * @param selfSite 站点
     * @return
     * @author: liyanlei
     * 2016年4月18日上午10:20:30
     */
    List<SiteVO> findAllOtherSiteVOList(Site selfSite);

    /**
     * 根据站点状态与关键词进行站点分页查询
     * @param pageModel
     * @param companyId 公司ID
     * @param status
     * @param keyword 站点名称/站⻓姓名/⼿机号
     * @return
     */
    PageModel<Site> getSitePage(PageModel<Site> pageModel, String companyId,Integer status, String keyword);

    /**
     * 根据站点状态进行站点分页查询
     * @param pageModel
     * @param companyId 公司ID
     * @param status
     * @return
     */
    PageModel<Site> getSitePage(PageModel<Site> pageModel, String companyId,SiteStatus status);

    /**
     * 删除站点
     * @param siteId
     */
    void delSiteBySiteId(String siteId);

    /**
     * 将此站点审核通过
     * @param areaCode
     */
    void validSite(String areaCode);

    /**
     * 根据公司ID获取该公司下的所有站点
     * @param companyId
     * @return
     */
    List<Site> findSiteListByCompanyId(String companyId, SiteStatus status);
    /**
     * 查询指定公司下的所有站点
     * @param companyId 公司名称
     * @return
     * @date 2016/5/5 17:59
     * @auth liyanlei
     */
    List<SiteVO> findAllSiteVOByCompanyId(String companyId, SiteStatus status);

    /**
     * 查询所有站点
     * @return
     */
    List<Site> findAllSiteList();

}
