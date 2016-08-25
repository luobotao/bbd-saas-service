package com.bbd.saas.api.impl.mysql;

import com.bbd.saas.api.mysql.SiteMySqlService;
import com.bbd.saas.dao.mysql.SiteDao;
import com.bbd.saas.models.SiteMySql;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * 公司Service实现
 * Created by zuowenhai on 2016/4/18.
 */
@Service("siteMysqlService")
@Transactional
public class SiteMySqlServiceImpl implements SiteMySqlService {
	public static final Logger logger = LoggerFactory.getLogger(SiteMySqlServiceImpl.class);
	@Resource
	private SiteDao siteDaoMySql;

	public SiteDao getSiteDaoMySql() {
		return siteDaoMySql;
	}

	public void setSiteDaoMySql(SiteDao siteDaoMySql) {
		this.siteDaoMySql = siteDaoMySql;
	}

	/**
     *根据siteid查询站点记录
     * @param 
     * @return
     */
	@Override
	@Transactional(readOnly = true)
	public SiteMySql selectIdBySiteId(String siteid) {
		return siteDaoMySql.selectIdBySiteId(siteid);
	}

	/**
	 * 更新site表中daycnt
	 * @return
	 */
	public int updateSiteDayCntBySiteId(String siteid){
		return siteDaoMySql.updateSiteDayCntBySiteId(siteid);
	}
	@Transactional
	@Override
	public int save(SiteMySql siteMySql) {
		int count = this.siteDaoMySql.selectCountBySiteId(siteMySql.getSiteid());
		if(count == 0){//不存在，插入
			return this.siteDaoMySql.insert(siteMySql);
		}else{
			return this.siteDaoMySql.update(siteMySql);
		}
	}
}
