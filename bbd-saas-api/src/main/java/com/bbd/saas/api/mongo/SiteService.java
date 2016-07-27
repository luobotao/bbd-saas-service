package com.bbd.saas.api.mongo;

import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.Option;
import com.bbd.saas.vo.SiteVO;
import org.mongodb.morphia.Key;

import java.util.List;

/**
 * Created by luobotao on 2016/4/11.
 * 站点接口
 */
public interface SiteService {
    /**
     * 根据详细地址信息生成区域码
     * @param address 详细地址信息
     * @return areaCode
     */
    String dealOrderWithGetAreaCode(String address);
    /**
     * 根据详细地址信息生成省市区数组
     * @param strs 详细地址信息
     * @return areaCode
     */
    String[] dealStrWithAddress(String[] strs, String province);
    /**
     * 保存站点
     * @param site 站点
     * @return 保存结果
     */
    Key<Site> save(Site site);

    /**
     * 根据ID获取Site
     * @param id 站点_id
     * @return 站点
     */
    Site findSite(String id);

    /**
     * 根据站点编号查询站点
     * @param areaCode 站点编号
     * @return 站点
     * @author: liyanlei
     * 2016年4月19日上午10:29:29
     */
    Site findSiteByAreaCode(String areaCode);
    /**
     * 根据手机号查询站点
     * @param phone 手机号码（对应username字段）
     * @return 站点
     */
    Site findSiteByUserName(String phone);
    /**
     * 查询同一公司下除当前站点selfSite的其他所有站点的VO对象
     * @param selfSite 当前站点
     * @return 站点的VO集合
     * @author: liyanlei
     * 2016年4月18日上午10:20:30
     */
    List<SiteVO> findAllOtherSiteVOList(Site selfSite);

    /**
     * 根据站点状态与关键词进行站点分页查询
     * @param pageModel 分页对象
     * @param companyId 公司ID
     * @param status 站点状态SiteStatus对应的值
     * @param keyword 站点名称/站⻓姓名/⼿机号
     * @return 分页对象（分页信息和当前页的数据）
     */
    PageModel<Site> getSitePage(PageModel<Site> pageModel, String companyId,Integer status, Integer areaFlag, String keyword);

    /**
     * 根据站点状态进行站点分页查询
     * @param pageModel 分页对象
     * @param companyId 公司ID
     * @param statusList 站点状态集合
     * @return 分页对象（分页信息和当前页的数据）
     */
    PageModel<Site> getSitePage(PageModel<Site> pageModel, String companyId, List<SiteStatus> statusList);

    /**
     * 删除站点
     * @param siteId 站点
     */
    void delSiteBySiteId(String siteId);

    /**
     * 将此站点审核通过
     * @param areaCode 站点编号
     */
    void validSite(String areaCode);

    /**
     * 根据公司ID获取该公司下的指定站点状态的所有站点
     * @param companyId 公司Id
     * @return 站点集合
     */
    List<Site> findSiteListByCompanyId(String companyId, SiteStatus status);
    /**
     * 查询指定公司下的指定状态集合的所有站点
     * @param companyId 公司id
     * @param statusList 站点状态集合
     * @return 站点VO集合
     */
    List<SiteVO> findAllSiteVOByCompanyIdAndStatusList(String companyId, List<SiteStatus> statusList);
    /**
     * 查询指定公司下的特定站点状态的所有站点
     * @param companyId 公司名称
     * @param status 特定站点状态
     * @return 站点VO集合
     */
    List<SiteVO> findAllSiteVOByCompanyId(String companyId, SiteStatus status);

    /**
     * 查询所有站点
     * @return 站点集合
     */
    List<Site> findAllSiteList();
    /**
     * 查询指定公司的不同地区的特定站点状态的站点集合
     * @param companyId 公司Id
     * @param prov 省
     * @param city 市
     * @param area 区
     * @param status 站点状态
     * @return 站点集合
     */
    List<SiteVO> findSiteVOByCompanyIdAndAddress(String companyId, String prov, String city, String area, SiteStatus status);

    /**
     * 查询指定公司的不同地区的特定站点状态的站点集合
     * @param companyId 公司Id
     * @param prov 省
     * @param city 市
     * @param area 区
     * @param statusList 站点状态集合
     * @return 站点集合
     */
    List<Option> findByCompanyIdAndAddress(String companyId, String prov, String city, String area, String siteName, List<SiteStatus> statusList);

    /**
     * 根据站点编号数组查询
     * @param areaCodes 站点编号数组
     * @return List<areaCode,name>集合
     */
    List<Option> findByAreaCodes(String[] areaCodes);


}
