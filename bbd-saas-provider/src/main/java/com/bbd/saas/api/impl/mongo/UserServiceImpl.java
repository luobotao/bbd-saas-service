package com.bbd.saas.api.impl.mongo;


import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.dao.mongo.SiteDao;
import com.bbd.saas.dao.mongo.UserDao;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.UserQueryVO;
import com.bbd.saas.vo.UserQueryVO2;
import com.bbd.saas.vo.UserVO;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.UpdateResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
public class UserServiceImpl implements UserService {

    private UserDao userDao;

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    SiteDao siteDao;

    public SiteDao getSiteDao() {
        return siteDao;
    }

    public void setSiteDao(SiteDao siteDao) {
        this.siteDao = siteDao;
    }

    /**
     * 根据用户名查找是否存在此管理员
     *
     * @param loginName
     * @return
     */
    public User findUserByLoginName(String loginName) {
        return userDao.findOne("loginName", loginName);
    }

    @Override
    public User findByLoginNameAndId(String loginName, String userId) {
        return userDao.selectByLoginNameAndId(loginName, userId);
    }

    /**
     * 根据用户名查找是否存在此用户
     *
     * @param realName
     * @return
     */
    public User findUserByRealName(String realName) {
        return userDao.findOne("realName", realName);
    }


    /**
     * 保存用户对象信息
     *
     * @param user
     * @return Key<User>
     */
    public Key<User> save(User user) {
        return userDao.save(user);
    }

    /**
     * 保存用户对象信息
     *
     * @param user
     * @return WriteResult
     */
    public void delUser(User user) {
        userDao.delete(user);
    }


    /**
     * 获取用户列表信息
     *
     * @param pageModel
     * @return
     */
    public PageModel<User> findUserList(PageModel<User> pageModel, UserQueryVO userQueryVO, Site site) {
        return userDao.findUserList(pageModel, userQueryVO, site);
    }

    @Override
    public PageModel<User> findPageUser(PageModel<User> pageModel, UserQueryVO userQueryVO, List<Site> siteList) {
        return this.userDao.findPageUser(pageModel, userQueryVO, siteList);
    }

    /**
     * Description: 获取指定站点下的所有派件员
     *
     * @param areaCode
     * @return
     * @author: liyanlei
     * 2016年4月12日上午11:27:25
     */
    @Override
    public List<UserVO> findUserListBySite(String areaCode) {
        Site site = siteDao.findOne("areaCode", areaCode);
        return this.findUserListBySite(site);
    }

    @Override
    public List<UserVO> findUserListBySite(Site site) {
        List<User> userList = userDao.findUserListBySite(site, null,UserStatus.VALID);
        List<UserVO> userVoList = new ArrayList<UserVO>();
        if (userList != null && userList.size() > 0) {
            for (User user : userList) {
                UserVO userVo = new UserVO();
                userVo.setId(user.getId().toString());
                userVo.setRealName(init(user.getRealName(), "张三"));
                userVo.setStaffId(user.getStaffid());
                //userVo.setLoginName(user.getLoginName());
                //userVo.setPhone(user.getPhone());
                userVoList.add(userVo);
            }
        }
        return userVoList;
    }

    private String init(String value, String defalut) {
        if (value == null) {
            return defalut;
        }
        return value;
    }

    /**
     * Description: 根据用户id查询
     *
     * @param userId 用户id
     * @return
     * @author: liyanlei
     * 2016年4月12日下午6:10:40
     */
    @Override
    public User findOne(String userId) {
        if (userId == null || "".equals(userId)) {
            return null;
        }
        return userDao.findOne("_id", new ObjectId(userId));

    }

    /**
     * 根据site和staffid查找是该staffid是否在该站点已存在
     *
     * @param site、staffid
     * @return User
     */
    public User findOneBySiteByStaffid(Site site, String staffid) {
        return userDao.findOneBySiteByStaffid(site, staffid);
    }


    /**
     * 更新用户状态
     *
     * @param loginName 、UserStatus
     *                  return UpdateResults
     */
    public void updateUserStatu(String loginName, UserStatus userStatus) {
        userDao.updateUserStatu(loginName, userStatus);
    }

    @Override
    public int updateDispatchPermsn(String loginName, Integer dispatchPermsn, String pwd) {
        UpdateResults results = userDao.updateDispatchPermsn(loginName, dispatchPermsn, pwd);
        int i = 0;
        if(results != null && results.getWriteResult() != null){
            i = results.getWriteResult().getN();
        }
        return i;
    }

    /**
     * 根据用户名（手机号）去更新此用户的公司ID
     *
     * @param companyId
     * @param phone
     */
    @Override
    public void updateCompanyIdByLoginName(int companyId, String phone) {
        userDao.updateCompanyIdByLoginName(companyId, phone);
    }

    /**
     * 删除此站点下的所有用户
     * @param siteId
     */
    @Override
    public void delUsersBySiteId(String siteId) {
        userDao.delUsersBySiteId(siteId);
    }

    @Override
    public List<User> findUserListByCompanyId(String companyId) {
        return userDao.selectUserListByCompanyId(companyId);
    }

    @Override
    public List<User> findUsersBySite(Site site,UserRole userRole,UserStatus userStatus) {
        return userDao.findUserListBySite(site, userRole,userStatus);
    }

    @Override
    public List<User> findUsersBySite(List<Site> siteList, UserRole userRole, UserStatus userStatus) {
        return this.userDao.selectUserListBySite(siteList, userRole, userStatus);
    }

    @Override
    public Map<Long, String> findUserSiteMap(List<Integer> postManIdList, String companyId) {
        UserQueryVO2 query = new UserQueryVO2();
        query.postManIdList = postManIdList;
        query.companyId = companyId;
        List<User> userList = userDao.selectUserListByQuerys(query);
        Map<Long, String> map = new HashMap<Long, String>();
        String siteName = null;
        if(userList != null && userList.size() > 0){
            for (User user : userList){
                if(user.getSite() != null){
                    siteName = user.getSite().getName();
                }else{
                    siteName = null;
                }
                map.put((long) user.getPostmanuserId(), defaultString(siteName, ""));
            }
        }
        return map;
    }
    private String defaultString(String  str, String defaultStr){
        if(str == null){
            return defaultStr;
        }else {
            return str;
        }
    }

    @Override
    public long findCountBySiteAndDisptcherPermsn(Site site, int dispatchPermsn) {
        return userDao.selectCountBySiteAndDisptcherPermsn(site, dispatchPermsn);
    }

    @Override
    public User findByAppkeyAndSessionkey(String appKey, String sessionKey) {
        return userDao.findByAppkeyAndSessionkey(appKey, sessionKey);
    }

    @Override
    public PageModel<User> findPageUser(Site site, UserRole role, UserStatus userStatus, int lastindex, int pagesize) {
        return this.userDao.selectPageUser(site, role, userStatus, lastindex, pagesize);
    }
}
