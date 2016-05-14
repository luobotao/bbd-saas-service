package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.utils.PageModel;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by luobotao on 2016/4/1.
 * 站点DAO
 */
@Repository
public class SiteDao extends BaseDAO<Site, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(SiteDao.class);

    SiteDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }
    public List<Site> selectByCompanyCode(String companycode) {
    	Query<Site> query = createQuery();
    	query.filter("companycode", companycode);
    	return  find(query).asList();
    }

    /**
     * 带查询条件去检索订单
     * @param pageModel
     * @param status
     * @param keyword
     * @return
     */
    public PageModel<Site> findSites(PageModel<Site> pageModel,String companyId, Integer status, String keyword) {
        Query<Site> query = createQuery().order("-dateAdd");
        if(StringUtils.isNotBlank(companyId)){
            query.filter("companyId", companyId);
        }
        if(status!=null && status.intValue()!=-1){
            query.filter("status", SiteStatus.status2Obj(status));
        }
        if(StringUtils.isNotBlank(keyword)){
            keyword = keyword.trim();
            query.or(query.criteria("responser").containsIgnoreCase(keyword),query.criteria("name").containsIgnoreCase(keyword),query.criteria("username").containsIgnoreCase(keyword));
        }

        List<Site> siteList = find(query.offset(pageModel.getPageNo() * pageModel.getPageSize()).limit(pageModel.getPageSize())).asList();
        pageModel.setDatas(siteList);
        pageModel.setTotalCount(count(query));
        return pageModel;
    }

    /**
     * 根据公司ID获取该公司下的所有站点
     * @param companyId
     * @return
     */
    public List<Site> selectByCompanyId(String companyId) {
        Query<Site> query = createQuery();
        query.filter("companyId", companyId);
        return  find(query).asList();
    }
}
