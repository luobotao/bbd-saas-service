package com.bbd.saas.api.impl.mongo;

import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.dao.mongo.SiteDao;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.vo.SiteVO;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
@Service("siteService")
public class SiteServiceImpl implements SiteService {
    private SiteDao siteDao;

    public SiteDao getSiteDao() {
        return siteDao;
    }

    public void setSiteDao(SiteDao siteDao) {
        this.siteDao = siteDao;
    }
    public Key<Site> save(Site site){
        return siteDao.save(site);
    }
    @Override
    public Site findSite(String id) {
        return siteDao.findOne("_id",new ObjectId(id));
    }
    @Override
	public Site findSiteByAreaCode(String areaCode) {
    	return siteDao.findOne("areaCode", areaCode);
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
	private SiteVO siteToSiteVO(Site site){
		SiteVO siteVo = new SiteVO();
		siteVo.setId(site.getId().toString());
		siteVo.setAreaCode(site.getAreaCode());
		siteVo.setName(site.getName());
		return siteVo;
	}

}
