package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.SiteQueryVO;
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
        query.filter("status", SiteStatus.APPROVE);
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
        SiteQueryVO queryVO = new SiteQueryVO();
        queryVO.companyId = companyId;
        queryVO.status = SiteStatus.status2Obj(status);
        queryVO.keyword = keyword;
        Query<Site> query = getQuerys(queryVO);
        query.order("-dateAdd");
        return queryPageData(pageModel, query);
    }

    /**
     * 带查询条件去检索订单
     * @param pageModel
     * @param status
     * @return
     */
    public PageModel<Site> findSites(PageModel<Site> pageModel,String companyId, SiteStatus status) {
        SiteQueryVO queryVO = new SiteQueryVO();
        queryVO.companyId = companyId;
        queryVO.status = status;
        Query<Site> query = getQuerys(queryVO);
        query.order("areaCode");
        return queryPageData(pageModel, query);
    }

    private Query<Site> getQuerys(SiteQueryVO siteQueryVO){
        Query<Site> query = createQuery();
        if(StringUtils.isNotBlank(siteQueryVO.id)){
            query.filter("_id", siteQueryVO.id);
        }
        if(StringUtils.isNotBlank(siteQueryVO.areaCode)){
            query.filter("areaCode", siteQueryVO.areaCode);
        }
        if(StringUtils.isNotBlank(siteQueryVO.name)){
            query.filter("name", siteQueryVO.name);
        }
        if(StringUtils.isNotBlank(siteQueryVO.companyId)){
            query.filter("companyId", siteQueryVO.companyId);
        }
        if(StringUtils.isNotBlank(siteQueryVO.companyCode)){
            query.filter("companyCode", siteQueryVO.companyCode);
        }
        if(StringUtils.isNotBlank(siteQueryVO.responser)){
            query.filter("responser", siteQueryVO.responser);
        }
        if(siteQueryVO.status != null){
            query.filter("status", siteQueryVO.status);
        }
        if(StringUtils.isNotBlank(siteQueryVO.keyword)){
            siteQueryVO.keyword = siteQueryVO.keyword.trim();
            query.or(query.criteria("responser").containsIgnoreCase(siteQueryVO.keyword),query.criteria("name").containsIgnoreCase(siteQueryVO.keyword),query.criteria("username").containsIgnoreCase(siteQueryVO.keyword));
        }
        return query;
    }
    private PageModel<Site> queryPageData(PageModel<Site> pageModel, Query<Site> query){
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
        Query<Site> query = createQuery().order("areaCode");
        query.filter("companyId", companyId);
        query.filter("status", SiteStatus.APPROVE);
        return  find(query).asList();
    }
}
