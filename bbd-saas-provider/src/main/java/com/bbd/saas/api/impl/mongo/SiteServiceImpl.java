package com.bbd.saas.api.impl.mongo;

import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.dao.mongo.SiteDao;
import com.bbd.saas.dao.mysql.BbtAddressDao;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.models.BbtAddress;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.Option;
import com.bbd.saas.vo.SiteVO;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
public class SiteServiceImpl implements SiteService {
    private SiteDao siteDao;
    private BbtAddressDao bbtAddressDao;

    public SiteDao getSiteDao() {
        return siteDao;
    }

    public void setSiteDao(SiteDao siteDao) {
        this.siteDao = siteDao;
    }

    public BbtAddressDao getBbtAddressDao() {
        return bbtAddressDao;
    }

    public void setBbtAddressDao(BbtAddressDao bbtAddressDao) {
        this.bbtAddressDao = bbtAddressDao;
    }

    public Key<Site> save(Site site) {
        return siteDao.save(site);
    }

    @Override
    public Site findSite(String id) {
        return siteDao.findOne("_id", new ObjectId(id));
    }

    @Override
    public Site findSiteByAreaCode(String areaCode) {
        return siteDao.findOne("areaCode", areaCode);
    }

    /**
     * 根据手机号查询站点
     * @param phone
     * @return
     */
    @Override
    public Site findSiteByUserName(String phone) {
        return siteDao.findOne("username", phone);
    }

    /**
     * 根据站点状态与关键词进行站点分页查询
     *
     * @param pageModel
     * @param status
     * @param keyword   站点名称/站⻓姓名/⼿机号
     * @return
     */
    @Override
    public PageModel<Site> getSitePage(PageModel<Site> pageModel, String companyId, List<ObjectId> siteIdList, Integer status, Integer areaFlag, String keyword) {
        return siteDao.findSites(pageModel, companyId, siteIdList, status, areaFlag, keyword);
    }
    /**
     * 根据站点状态与关键词进行站点分页查询
     * @param pageModel
     * @param statusList 状态集合
     * @return
     */
    @Override
    public PageModel<Option> getSitePage(PageModel<Option> pageModel, String companyId, List<String> areaCodeList, List<SiteStatus> statusList, Integer areaFlag) {
        return siteDao.findSites(pageModel, companyId, areaCodeList, statusList, areaFlag);
    }

    @Override
    public PageModel<Site> findOtherSitesPage(String companyId, String selfAreaCode, int lastindex, int pagesize) {
        return this.siteDao.selectOtherSitesPage(companyId, selfAreaCode, lastindex, pagesize);
    }

    /**
     * 删除站点
     *
     * @param siteId
     */
    @Override
    public void delSiteBySiteId(String siteId) {
        siteDao.deleteById(new ObjectId(siteId));
    }

    /**
     * 审核通过站点
     *
     * @param siteId
     */
    @Override
    public void validSite(String siteId) {
        Site site = findSite(siteId);
        site.setMemo("您提交的信息已审核通过，您可访问http://www.bangbangda.cn登录。");
        //生成区域码（棒棒达）----------------------------------------------------------
        String areaCode = dealOrderWithGetAreaCode(site.getProvince() + site.getCity());//只通过省市
        site.setAreaCode(areaCode);
        site.setStatus(SiteStatus.APPROVE);
        site.setDateUpd(new Date());
        save(site);
    }
    /**
     * 根据公司ID获取该公司下的所有站点
     * @param companyId
     * @return
     */
    @Override
    public List<Site> findSiteListByCompanyId(String companyId, SiteStatus status) {
        return siteDao.selectByCompanyId(companyId, status);
    }

    /**
     * 生成区域码
     *
     * @param address
     * @return
     */
    public String dealOrderWithGetAreaCode(String address) {
        address = address.replaceAll("省", "");
        address = address.replaceAll("市", "");
        String str[] = new String[2];
        int tempNum = 0;
        while (true) {
            str = dealOrderWithProvince(str, address);//河南-濮阳
            //替换省市的信息继续搜索
            if (!StringUtils.isBlank(str[1])) {
                address = address.replace(str[1].split("-")[1], "").trim();
            } else if (!StringUtils.isBlank(str[0])) {
                address = address.replace(str[0].split("-")[1], "").trim();
            }

            tempNum++;
            if (tempNum >= 2) {
                break;
            }
        }
        String areaCode = "";
        if (str != null && str.length > 1) {
            //处理区，生成区域码
            String area = str[str.length - 1];//
            String[] areas = area.split("-");
            BbtAddress bbtAddress = bbtAddressDao.getBbtAddressWithId(areas[0]);
            if (bbtAddress != null) {
                //去掉前两位
                String code = bbtAddress.getCode().substring(2);
                String maxstationcode = bbtAddress.getMaxstationcode();
                BigDecimal big = new BigDecimal(maxstationcode);
                int maxNumber = big.intValue() + 1;
                //更新数据库信息
                String temp = String.format("%03d", maxNumber);
                updateMaxstationcodeByCode(temp, bbtAddress.getCode());
                // 0 代表前面补充0
                // 4 代表长度为4
                // d 代表参数为正数型
                areaCode = code + "-" + temp;
            }
        }
        return areaCode;
    }

    public void updateMaxstationcodeByCode(String maxstationcode, String code) {
        bbtAddressDao.updateMaxstationcodeByCode(maxstationcode, code);
    }

    /**
     * 根据地址处理数据，省市区
     *
     * @param str
     * @param province
     * @return
     */
    public String[] dealStrWithAddress(String[] str, String province) {
        province = province.replaceAll("省", "");
        province = province.replaceAll("市", "");
        //province = dealProvince(province);
        int tempNum = 0;
        while (true) {
            str = dealOrderWithProvince(str, province);
            //如果区的数值不存在，则继续搜索
            if (StringUtils.isBlank(str[2])) {
                //替换省市的信息继续搜索
                if (!StringUtils.isBlank(str[1])) {
                    province = province.replace(str[1].split("-")[1], "").trim();
                } else if (!StringUtils.isBlank(str[0])) {
                    province = province.replace(str[0].split("-")[1], "").trim();
                }
            } else {
                break;
            }
            tempNum++;
            if (tempNum >= 3) {
                break;
            }
        }
        //针对异常数据进行置空处理
        for (int i = 0; i < str.length; i++) {
            if (StringUtils.isBlank(str[i])) {
                str = null;
                break;
            } else {
                str[i] = str[i].split("-")[1];
            }
        }
        return str;
    }

    public String[] dealOrderWithProvince(String[] strs, String province) {
        List<BbtAddress> addresses = bbtAddressDao.getBbtAddressWithProvince(province);
        if (addresses != null && addresses.size() > 0) {
            for (BbtAddress erpAddress : addresses) {
                if (!StringUtils.isBlank(erpAddress.getName())) {
                    int tier = erpAddress.getTier();
                    //用于判断parentid是否等于上一层的id
                    if (tier > 1) {
                        if (!StringUtils.isBlank(strs[tier - 2])) {
                            String[] tempStr = strs[tier - 2].split("-");
                            if (tempStr[0].equals(String.valueOf(erpAddress.getParentid()))) {
                                strs[tier - 1] = erpAddress.getId() + "-" + erpAddress.getName();
                            }
                        }
                    } else {
                        strs[tier - 1] = erpAddress.getId() + "-" + erpAddress.getName();
                    }
                }
            }
        }
        return strs;
    }
	@Override
	public List<SiteVO> findAllOtherSiteVOList(Site selfSite) {
        List<Site> siteList = this.siteDao.selectByCompanyId(selfSite.getCompanyId(), SiteStatus.APPROVE);
		String areaCode = selfSite.getAreaCode();
		if(areaCode == null){
			areaCode = "";
		}
		List<SiteVO> siteVoList = null;
		if(siteList != null && siteList.size() > 0){
			siteVoList = new ArrayList<SiteVO>();
			SiteVO siteVo = null;
			for(Site site : siteList){
				//排除站点编号为areaCode的站点
				if(!areaCode.equals(site.getAreaCode())){ //if(site != selfSite){
					siteVoList.add(siteToSiteVO(site));
				}
			}
		}
		return siteVoList;
	}

	@Override
	public List<SiteVO> findAllSiteVOByCompanyIdAndStatusList(String companyId, List<SiteStatus> statusList) {
        List<Site> siteList = this.siteDao.selectByCompanyId(companyId, statusList);
        return siteListToSiteVO(siteList);
	}
    @Override
    public List<SiteVO> findAllSiteVOByCompanyId(String companyId, SiteStatus status) {
        List<Site> siteList = this.siteDao.selectByCompanyId(companyId, status);
        return siteListToSiteVO(siteList);
    }

    @Override
    public List<Site> findSiteVOByCompanyIdAndAreaCode(String companyId, List<String> areaCodeList, SiteStatus status) {
        return this.siteDao.selectByCompanyIdAndAreaCode(companyId, areaCodeList, status);
    }

    @Override
	public List<Site> findAllSiteList() {
		return siteDao.find().asList();
	}

	private SiteVO siteToSiteVO(Site site){
		SiteVO siteVo = new SiteVO();
		/*siteVo.setAreaCode(site.getAreaCode());
		siteVo.setName(site.getName());
        siteVo.setLng(site.getLng());
        siteVo.setLat(site.getLat());
        siteVo.setDeliveryArea(site.getDeliveryArea());*/
        BeanUtils.copyProperties(site, siteVo);
        siteVo.setId(site.getId().toString());
		return siteVo;
	}
    private List<SiteVO> siteListToSiteVO(List<Site> siteList){
        List<SiteVO> siteVoList = null;
        if(siteList != null && siteList.size() > 0){
            siteVoList = new ArrayList<SiteVO>();
            for(Site site : siteList){
                siteVoList.add(siteToSiteVO(site));
            }
        }
        return siteVoList;
    }

    @Override
    public List<SiteVO> findSiteVOByCompanyIdAndAddress(String companyId, String prov, String city, String area, SiteStatus status, int areaFlag) {
        List<Site> siteList = this.siteDao.selectByCompanyIdAndAddress(companyId, prov, city, area, status, areaFlag);
        return siteListToSiteVO(siteList);
    }

    @Override
    public List<Option> findOptByCompanyIdAndAddress(String companyId, String prov, String city, String area, String siteName, List<SiteStatus> statusList) {
        return this.siteDao.selectByCompanyIdAndAddress(companyId, prov, city, area, siteName, statusList, null);
    }

    @Override
    public List<Option> findOptByCompanyIdAndAddress(String companyId, String prov, String city, String area, String siteName, List<SiteStatus> statusList, Integer areaFlag) {
        return this.siteDao.selectByCompanyIdAndAddress(companyId, prov, city, area, siteName, statusList, areaFlag);
    }

    @Override
    public List<Option> findByAreaCodes(String[] areaCodes) {
        return this.siteDao.selectByAreaCodes(areaCodes);
    }

    @Override
    public List<Site> findSiteListByAreaCodes(List<String> areaCodeList) {
        return this.siteDao.selectByCompanyIdAndAreaCode(null, areaCodeList, null);
    }

    @Override
    public List<Site> findByCompanyIdAndAddress(String companyId, String prov, String city, String area, List<ObjectId> siteIdList, List<SiteStatus> statusList) {
        return this.siteDao.selectByCompanyIdAndAddress(companyId, prov, city, area, siteIdList, statusList);
    }

    @Override
    public PageModel<Site> findPageByCompanyIdAndAddress(PageModel<Site> pageModel, String companyId, String prov, String city, String area, List<ObjectId> siteIdList, List<SiteStatus> statusList) {
        return this.siteDao.selectPageByCompanyIdAndAddress(pageModel, companyId, prov, city, area, siteIdList, statusList);
    }

    @Override
    public Site findSiteByName(String name) {
        return siteDao.findOne("name", name);
    }

    /**
     * 获取热门公司列表
     * @return
     */
    public List<Site> findSiteList(String companyId){
        return siteDao.findSiteList(companyId);
    }

    @Override
    public long findOtherSiteCount(String companyId, String areaCode) {
        return this.siteDao.selectOtherSiteCount(companyId, areaCode);
    }
}
