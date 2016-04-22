package com.bbd.saas.dao.mongo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Criteria;
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
    public PageModel<User> findUserList(PageModel<User> pageModel,UserQueryVO userQueryVO,Site site) {
    	
    	Query<User> query = createQuery();
    	if(userQueryVO!=null){
    		
    		query.filter("role", UserRole.status2Obj(1));
    		/*if(userQueryVO.roleId!=null && userQueryVO.roleId!=-1){
    			query.filter("role", UserRole.status2Obj(userQueryVO.roleId));
    		}*/
    		query.filter("site", site);
    		if(userQueryVO.status!=null && userQueryVO.status!=-1){
    			query.filter("userStatus", UserStatus.status2Obj(userQueryVO.status));
    		}
    		if(userQueryVO.keyword!=null && !userQueryVO.keyword.equals("")){
    			
    			query.or(query.criteria("realName").containsIgnoreCase(userQueryVO.keyword),query.criteria("loginName").containsIgnoreCase(userQueryVO.keyword));
    			
    		}
    		
        }
    	List<User> userList = find(query.offset(pageModel.getPageNo() * pageModel.getPageSize()).limit(pageModel.getPageSize())).asList();

        pageModel.setDatas(userList);
        pageModel.setTotalCount(count(query));
    	
        return pageModel;
    }
    
    /**
     * Description: 获取指定站点下的所有状态为有效的用户
     * @param site 站点
     * @return
     * @author: liyanlei
     * 2016年4月14日下午8:04:44
     */
    public List<User> findUserListBySite(Site site, UserRole userRole) {
    	Query<User> query = createQuery();
    	if(site != null){
    		query.filter("site", site);
    	}
    	if(userRole != null){
    		query.filter("role", userRole);
    	}
    	//有效用户
    	query.filter("userStatus", UserStatus.status2Obj(1));
        return  find(query).asList();
    }
    
    
    /**
     * 根据site和staffid查找是该staffid是否在该站点已存在
     * @param site、staffid
     * @return User
     */
    public User findOneBySiteByStaffid(Site site, String staffid) {
        Query<User> query = createQuery();
        if(StringUtils.isNotBlank(staffid))
            query.filter("staffid",staffid);
        query.filter("site",site);
        return findOne(query);
    }
    
}
