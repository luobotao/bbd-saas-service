package com.bbd.saas.dao;

import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.UserQueryVO;


/**
 * Created by luobotao on 2016/4/11.
 * 站点用户DAO
 */
@Repository
public class UserDao extends BaseDAO<User, ObjectId> {
    public static final Logger logger = LoggerFactory.getLogger(UserDao.class);
    
    SiteDao siteDao;
    public SiteDao getSiteDao() {
        return siteDao;
    }

    public void setSiteDao(SiteDao siteDao) {
        this.siteDao = siteDao;
    }
    
    UserDao(LinkedHashMap<String, Datastore> datastores) {
        super(datastores);
    }

    /**
     * 根据用户名查找是否存在此管理员
     * @param userName
     * @return
     */
    public User findOne(String userName){
    	User user = findOne(userName,"56d013f156f6c3ba9fe959ce");
    	return user;
    }
    /**
     * 获取用户列表信息
     * @param PageModel<User>
     * @return PageModel<User>
     */
    public PageModel<User> findUserList(PageModel<User> pageModel,UserQueryVO userQueryVO) {
    	
    	Query<User> query = createQuery();
        
    	if(userQueryVO!=null){
    		if(userQueryVO.roleId!=null && userQueryVO.roleId!=-1){
    			query.filter("role", UserRole.status2Obj(userQueryVO.roleId)).filter("role <>", null);
    		}
    		if(userQueryVO.status!=null && userQueryVO.status!=-1){
    			query.filter("userStatus", UserStatus.status2Obj(userQueryVO.status)).filter("userStatus <>", null);
    		}
    		if(userQueryVO.keyword!=null && !userQueryVO.keyword.equals("")){
    			query.or(query.criteria("realName").equal(userQueryVO.keyword),query.criteria("realName").equal(null));
    			query.or(query.criteria("phone").equal(userQueryVO.keyword),query.criteria("phone").equal(null));
    		}
        }
    	List<User> userList = find(query.offset(pageModel.getPageNo() * pageModel.getPageSize()).limit(pageModel.getPageSize())).asList();

        pageModel.setDatas(userList);
        pageModel.setTotalCount(count(query));
    	
        return pageModel;
    }
    
    /**
     * Description: 获取指定站点下的所有派件员
     * @param areaCode 站点编号
     * @return
     * @author: liyanlei
     * 2016年4月14日下午8:04:44
     */
    public List<User> findUserListBySite(String areaCode) {
    	Query<User> query = createQuery();
    	Site site = siteDao.findOne("areaCode", areaCode);
    	if(StringUtils.isNotBlank(areaCode)){
    		query.filter("site", site);
    		query.filter("role", UserRole.SENDMEM);
    	}
        return  find(query).asList();
    }
}
