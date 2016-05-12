package com.bbd.saas.api.impl.mongo;

import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.dao.mongo.SiteDao;
import com.bbd.saas.dao.mysql.BbtAddressDao;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.models.BbtAddress;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.SiteVO;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.springframework.stereotype.Service;

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
    public PageModel<Site> getSitePage(PageModel<Site> pageModel, String companyId, Integer status, String keyword) {
        return siteDao.findSites(pageModel, companyId, status, keyword);
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
        String areaCode = dealOrderWithGetAreaCode(site.getProvince() + site.getCity() + site.getArea());
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
    public List<Site> findSiteListByCompanyId(String companyId) {
        return siteDao.selectByCompanyId(companyId);
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
        String str[] = new String[3];
        int tempNum = 0;
        while (true) {
            str = dealOrderWithProvince(str, address);
            //如果区的数值不存在，则继续搜索
            if (StringUtils.isBlank(str[2])) {
                //替换省市的信息继续搜索
                if (!StringUtils.isBlank(str[1])) {
                    address = address.replace(str[1].split("-")[1], "").trim();
                } else if (!StringUtils.isBlank(str[0])) {
                    address = address.replace(str[0].split("-")[1], "").trim();
                }
            } else {
                break;
            }
            tempNum++;
            if (tempNum >= 3) {
                break;
            }
        }
        String areaCode = "";
        if (str != null && str.length > 1) {
            //处理区，生成区域码
            String area = str[str.length - 1];
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

    private String[] dealOrderWithProvince(String[] strs, String province) {
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
		List<Site> siteList = this.siteDao.selectByCompanyCode(selfSite.getCompanycode());
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
	public List<SiteVO> findAllSiteVOByCompanyId(String companyId) {
		List<Site> siteList = this.siteDao.selectByCompanyId(companyId);
		List<SiteVO> siteVoList = null;
		if(siteList != null && siteList.size() > 0){
			siteVoList = new ArrayList<SiteVO>();
			SiteVO siteVo = null;
			for(Site site : siteList){
				siteVoList.add(siteToSiteVO(site));
			}
		}
		return siteVoList;
	}

	@Override
	public List<Site> findAllSiteList() {
		return siteDao.find().asList();
	}

	private SiteVO siteToSiteVO(Site site){
		SiteVO siteVo = new SiteVO();
		siteVo.setId(site.getId().toString());
		siteVo.setAreaCode(site.getAreaCode());
		siteVo.setName(site.getName());
        siteVo.setLng(site.getLng());
        siteVo.setLat(site.getLat());
		return siteVo;
	}

}
