package com.bbd.saas.dao.mongo;

import com.bbd.db.morphia.BaseDAO;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.UserQueryVO;
import com.bbd.saas.vo.UserQueryVO2;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;


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
     * @param pageModel
     * @return PageModel<User>
     */
    public PageModel<User> findUserList(PageModel<User> pageModel,UserQueryVO userQueryVO,Site site) {
        Query<User> query = this.getPageQuery(userQueryVO);
        if(site!=null){
            query.filter("site", site);
        }
        List<User> userList = find(query.offset(pageModel.getPageNo() * pageModel.getPageSize()).limit(pageModel.getPageSize())).asList();
        pageModel.setDatas(userList);
        pageModel.setTotalCount(count(query));

        return pageModel;
    }
    /**
     * 获取用户列表信息
     * @param pageModel
     * @return PageModel<User>
     */
    public PageModel<User> findPageUser(PageModel<User> pageModel,UserQueryVO userQueryVO,List<Site> siteList) {
        Query<User> query = this.getPageQuery(userQueryVO);
        if(siteList != null){//站点集合(siteList.isEmpty():省市区下没有站点，但是选择了全部)
            query.filter("site in", siteList);
        }
        //设置排序
        query.order("site,-dateUpdate");
        List<User> userList = find(query.offset(pageModel.getPageNo() * pageModel.getPageSize()).limit(pageModel.getPageSize())).asList();
        pageModel.setDatas(userList);
        pageModel.setTotalCount(count(query));
        return pageModel;
    }
    private Query<User> getPageQuery(UserQueryVO userQueryVO){
        Query<User> query = createQuery();
        //设置排序
        query.order("-dateUpdate");
        if(userQueryVO!=null){
            if(StringUtils.isNotBlank(userQueryVO.companyId)){//公司用户
                query.filter("companyId", userQueryVO.companyId);
            }
            query.filter("role <>", UserRole.COMPANY);
            if(StringUtils.isNotBlank(userQueryVO.roleId) && !"-1".equals(userQueryVO.roleId)){
                query.filter("role", userQueryVO.roleId);
            }
            if(userQueryVO.status!=null && userQueryVO.status!=-1){
                query.filter("userStatus", UserStatus.status2Obj(userQueryVO.status));
            }
            if(userQueryVO.keyword!=null && !userQueryVO.keyword.equals("")){
                query.or(query.criteria("realName").containsIgnoreCase(userQueryVO.keyword),query.criteria("loginName").containsIgnoreCase(userQueryVO.keyword));
            }
        }
        return query;
    }

    /**
     * Description: 获取指定站点下的所有状态为有效的用户
     * @param site 站点
     * @return
     * @author: liyanlei
     * 2016年4月14日下午8:04:44
     */
    public List<User> findUserListBySite(Site site, UserRole userRole,UserStatus userStatus) {
        Query<User> query = createQuery();
        if(site != null){
            query.filter("site", site);
        }
        if(userRole != null){
            query.filter("role", userRole);
        }
        if(userStatus!=null){
            query.filter("userStatus", userStatus);
        }
        return  find(query).asList();
    }
    /**
     * 根据站点、角色、状态查询用户
     * @param siteList 站点集合
     * @param userRole 角色
     * @param userStatus 状态
     * @return
     */
    public List<User> selectUserListBySite(List<Site> siteList, UserRole userRole,UserStatus userStatus) {
        Query<User> query = createQuery();
        if(siteList != null && !siteList.isEmpty()){
            query.filter("site in", siteList);
        }
        if(userRole != null){
            query.filter("role", userRole);
        }
        if(userStatus!=null){
            query.filter("userStatus", userStatus);
        }
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
    /**
     * 更新用户状态
     * @param loginName 、UserStatus
     * return UpdateResults
     */
    public UpdateResults updateUserStatu(String loginName, UserStatus userStatus) {
        Query<User> query = createQuery();
        query.filter("loginName",loginName);
        UpdateOperations<User> ops = createUpdateOperations().set("userStatus",userStatus).set("dateUpdate",new Date());
        return update(query,ops);
    }
    /**
     * 更新派件员--到站的权限;开通时，密码为空，设置初始化密码
     * @param loginName 登录名称
     * @param dispatchPermsn 权限 0：关闭到站的权限；1：开通到站的权限
     */
    public UpdateResults updateDispatchPermsn(String loginName, Integer dispatchPermsn, String pwd) {
        Query<User> query = createQuery();
        query.filter("loginName",loginName);
        UpdateOperations<User> ops = createUpdateOperations().set("dispatchPermsn",dispatchPermsn).set("dateUpdate",new Date());
        if(StringUtils.isNotBlank(pwd)){
            ops = ops.set("passWord", pwd);
        }
        UpdateResults result = update(query, ops);
        return result;
    }
    /**
     * 根据用户名（手机号）去更新此用户的公司ID
     * @param companyId
     * @param loginName
     */
    public UpdateResults updateCompanyIdByLoginName(int companyId, String loginName) {
        Query<User> query = createQuery();
        query.filter("loginName",loginName);
        UpdateOperations<User> ops = createUpdateOperations().set("companyId",companyId).set("dateUpdate",new Date());
        return update(query,ops);
    }

    /**
     * 删除此站点下的所有用户
     * @param siteId
     */
    public void delUsersBySiteId(String siteId) {
        Site site = getDatastore().get(Site.class,new ObjectId(siteId));
        Query<User> query = createQuery();
        query.filter("site",site);
        deleteByQuery(query);
    }
    public List<User> selectUserListByCompanyId(String companyId){
        if(StringUtils.isBlank(companyId)){
            return null;
        }
        Query<User> query = createQuery();
        query.filter("companyId", companyId);
        return find(query).asList();
    }

    /**
     * 根据条件查询用户
     * @param userQueryVO 查询条件
     * @return
     */
    public List<User> selectUserListByQuerys(UserQueryVO2 userQueryVO){
        if(userQueryVO == null){
            return null;
        }
        Query<User> query = getQuery(userQueryVO);
        return find(query).asList();
    }
    private Query<User> getQuery(UserQueryVO2 userQueryVO){
        Query<User> query = createQuery();
        //状态
        if(userQueryVO.userStatus != null){
            query.filter("userStatus", userQueryVO.userStatus);
        }
        //角色
        if(userQueryVO.role != null){
            query.filter("role", userQueryVO.role);
        }
        //员工Id集合
        if(userQueryVO.postManIdList != null){
            query.filter("postmanuserId in", userQueryVO.postManIdList);
        }
        //公司
        if(StringUtils.isNotBlank(userQueryVO.companyId)){
            query.filter("companyId", userQueryVO.companyId);
        }
        return  query;
    }

    /**
     * 查询用户名为loginName, _id不为userId的用户
     * @param loginName 用户名即为手机号
     * @param userId _id
     * @return 用户对象
     */
    public User selectByLoginNameAndId(String loginName, String userId) {
        Query<User> query = createQuery();
        //_id
        if(StringUtils.isNotBlank(userId)){
            query.filter("_id <>", new ObjectId(userId));
        }
        //用户名
        if(StringUtils.isNotBlank(loginName)){
            query.filter("loginName", loginName);
        }
        return findOne(query);
    }
    /**
     * 根据站点和到站权限，查询符合条件的数目
     * @param site 站点
     * @param dispatchPermsn 到站权限
     * @return 符合查询条件的数目
     */
    public long selectCountBySiteAndDisptcherPermsn(Site site, int dispatchPermsn){
        Query<User> query = createQuery();
        query.filter("site", site);
        query.filter("dispatchPermsn", dispatchPermsn);
        return count(query);
    }

    public User findByAppkeyAndSessionkey(String appKey, String sessionKey) {
        Query<User> query = createQuery();
        query.filter("appKey", appKey);
        query.filter("sessionKey", sessionKey);
        return findOne(query);
    }
    /**
     * app端-根据站点查询小件员列表
     * @param site 站点
     * @param role 角色
     * @param lastindex 分页跳过的条数
     * @param pagesize 要查询的条数
     * @return 小件员列表
     */
    public PageModel<User> selectPageUser(Site site, UserRole role, UserStatus userStatus, int lastindex, int pagesize){
        Query<User> query = createQuery().order("loginName");
        //站点
        if(site != null){
            query.filter("site", site);
        }
        //角色
        if(role != null){
            query.filter("role", role);
        }
        //用户状态
        if(userStatus != null){
            query.filter("userStatus", userStatus);
        }
        PageModel<User> userPageModel = new PageModel<User>();
        userPageModel.setTotalCount(count(query));
        //分页
        if(lastindex > -1 && pagesize > 0){
            query.offset(lastindex).limit(pagesize);
        }
        userPageModel.setDatas(find(query).asList());
        return userPageModel;
    }
}
