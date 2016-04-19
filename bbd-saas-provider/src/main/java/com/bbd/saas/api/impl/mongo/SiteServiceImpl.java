package com.bbd.saas.api.impl.mongo;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.springframework.stereotype.Service;

import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.dao.mongo.SiteDao;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.vo.SiteVO;

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
		List<Site> siteList = this.siteDao.find().asList();
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
					siteVo = new SiteVO();
					siteVo.setId(site.getId().toString());
					siteVo.setAreaCode(site.getAreaCode());
					siteVo.setName(site.getName());
					siteVoList.add(siteVo);
				}
			}
		}
		return siteVoList;
	}

}
