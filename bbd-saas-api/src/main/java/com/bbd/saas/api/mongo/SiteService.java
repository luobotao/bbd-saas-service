package com.bbd.saas.api.mongo;

import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.models.BbtAddress;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.Option;
import com.bbd.saas.vo.SiteVO;
import org.bson.types.ObjectId;
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
     * 把站点信息同步到SPOI表中，并更新站点表的经纬度信息
     * @param siteId 站点id
     */
    void addSPOIAndSetLatAndLng(String siteId);

    /**
     * 如果SPOI表中没有站点信息，则调用addSPOIAndSetLatAndLng进行同步
     */
    void checkAndUpdateSiteToPoi();

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
     * 根据name查找站点
     * @param name
     * @return 站点
     */
    Site findSiteByName(String name);

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
     * @param siteIdList 站点id集合，为空即为查询公司下的全部站点
     * @param status 站点状态SiteStatus对应的值
     * @param keyword 站点名称/站⻓姓名/⼿机号
     * @return 分页对象（分页信息和当前页的数据）
     */
    PageModel<Site> getSitePage(PageModel<Site> pageModel, String companyId, List<ObjectId> siteIdList, Integer status, Integer areaFlag, String keyword);

    /**
     * 根据站点状态进行站点分页查询
     * @param pageModel 分页对象
     * @param companyId 公司ID
     * @param areaCodeList 站点编号集合
     * @param statusList 站点状态集合
     * @return 分页对象（分页信息和当前页的数据）
     */
    PageModel<Option> getSitePage(PageModel<Option> pageModel, String companyId, List<String> areaCodeList, List<SiteStatus> statusList, Integer areaFlag);

    /**
     * 分页查询公司下的除本站点外的其他站点 （转其他站点列表）
     * @param companyId 公司ID
     * @param selfAreaCode 本站地编号
     * @param lastindex 跳过的条数
     * @param pagesize 查询的条数
     * @return 分页对象（分页信息和当前页的数据）
     */
    PageModel<Site> findOtherSitesPage(String companyId, String selfAreaCode, int lastindex,int pagesize);

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
     * 查询指定公司下的特定站点状态的所有站点
     * @param companyId 公司名称
     * @param areaCodeList 站点编码集合
     * @param status 特定站点状态
     * @return 站点VO集合
     */
    List<Site> findSiteVOByCompanyIdAndAreaCode(String companyId, List<String> areaCodeList, SiteStatus status);
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
     * @param areaFlag 配送区域状态
     * @return 站点集合
     */
    List<SiteVO> findSiteVOByCompanyIdAndAddress(String companyId, String prov, String city, String area, SiteStatus status, int areaFlag);

    /**
     * 查询指定公司的不同地区的特定站点状态的站点集合
     * @param companyId 公司Id
     * @param prov 省
     * @param city 市
     * @param area 区
     * @param statusList 站点状态集合
     * @return 站点集合
     */
    List<Option> findOptByCompanyIdAndAddress(String companyId, String prov, String city, String area, String siteName, List<SiteStatus> statusList);

    /**
     * 查询指定公司的不同地区的特定站点状态的站点集合
     * @param companyId 公司Id
     * @param prov 省
     * @param city 市
     * @param area 区
     * @param  statusList 站点状态
     * @param  areaFlag 配送区域状态
     * @return 站点集合
     */
    List<Option> findOptByCompanyIdAndAddress(String companyId, String prov, String city, String area, String siteName, List<SiteStatus> statusList, Integer areaFlag);

    /**
     * 根据站点编号数组查询
     * @param areaCodes 站点编号数组
     * @return List<areaCode,name>集合
     */
    List<Option> findByAreaCodes(String[] areaCodes);

    /**
     * 根据站点编号集合查询站点列表
     * @param areaCodes 站点编号集合
     * @return List<site>站点集合
     */
    List<Site> findSiteListByAreaCodes(List<String> areaCodes);

    /**
     * 查询指定公司的不同地区的特定站点状态的站点集合
     * @param companyId 公司Id
     * @param prov 省
     * @param city 市
     * @param area 区
     * @param siteIdList 站点集合
     * @param statusList 站点状态集合
     * @return 站点集合
     */
    List<Site> findByCompanyIdAndAddress(String companyId, String prov, String city, String area, List<ObjectId> siteIdList, List<SiteStatus> statusList);

    /**
     * 分页查询指定公司的不同地区的特定站点状态的站点集合
     * @param pageModel 分页信息
     * @param companyId 公司Id
     * @param prov 省
     * @param city 市
     * @param area 区
     * @param siteIdList 站点集合
     * @param statusList 站点状态集合
     * @return 站点集合
     */
    PageModel<Site> findPageByCompanyIdAndAddress(PageModel<Site> pageModel, String companyId, String prov, String city, String area, List<ObjectId> siteIdList, List<SiteStatus> statusList);

    /**
     * 获取热门公司列表
     * @return
     */
    List<Site> findSiteList(String companyId);

    /**
     * 查询公司id为companyId,但是站点编号不为areaCode的站点个数
     * @return 符合条件的站点个数
     */
    long findOtherSiteCount(String companyId, String areaCode);

    BbtAddress findOneWithNameAndTier(String name, String tier);
}
