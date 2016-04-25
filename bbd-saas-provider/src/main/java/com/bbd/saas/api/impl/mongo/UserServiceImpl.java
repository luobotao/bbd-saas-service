package com.bbd.saas.api.impl.mongo;


import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.springframework.stereotype.Service;

import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.dao.mongo.SiteDao;
import com.bbd.saas.dao.mongo.UserDao;
import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.UserQueryVO;
import com.bbd.saas.vo.UserVO;

/**
 * Created by luobotao on 2016/4/1.
 * 管理员接口
 */
@Service("userService")
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
     * @param loginName
     * @return
     */
    public User findUserByLoginName(String loginName) {
        return userDao.findOne("loginName",loginName);
    }
    /**
     * 根据用户名查找是否存在此用户
     * @param realName
     * @return
     */
    public User findUserByRealName(String realName) {
        return userDao.findOne("realName",realName);
    }
    /**
     * 根据用户id查找是否存在此用户
     * @param id
     * @return
     */
    public User findUserById(String id) {
        return userDao.findOne("_id", new ObjectId(id));
    }
    /**
     * 保存用户对象信息
     * @param user
     * @return Key<User>
     */
    public Key<User> save(User user){
    	return userDao.save(user);
    }
    
    /**
     * 保存用户对象信息
     * @param user
     * @return WriteResult
     */
    public void delUser(User user){
    	userDao.delete(user);
    }


    /**
     * 获取用户列表信息
     * @param pageModel
     * @return
     */
    public PageModel<User> findUserList(PageModel<User> pageModel,UserQueryVO userQueryVO,Site site){
    	return userDao.findUserList(pageModel,userQueryVO,site);
    }
    
    /**
     * Description: 获取指定站点下的所有派件员
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
		List<User> userList = userDao.findUserListBySite(site, UserRole.SENDMEM);
		List<UserVO> userVoList = new ArrayList<UserVO>();
		if(userList != null && userList.size() > 0){
			for(User user : userList){
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
	private String init(String value, String defalut){
		if(value == null){
			return defalut;
		}
		return value;
	}
	
	/**
     * Description: 根据用户id查询
     * @param userId 用户id
     * @return
     * @author: liyanlei
     * 2016年4月12日下午6:10:40
     */
	@Override
	public User findOne(String userId) {
		if(userId == null || "".equals(userId)){
			return null;
		}
		return userDao.findOne("_id",new ObjectId(userId));
		
	}
	
	/**
     * 根据site和staffid查找是该staffid是否在该站点已存在
     * @param site、staffid
     * @return User
     */
    public User findOneBySiteByStaffid(Site site, String staffid) {
    	return userDao.findOneBySiteByStaffid(site, staffid);
    }
	
	
    /**
     * 更新用户状态
     * @param loginName 、UserStatus
     * return UpdateResults
     */
    public void updateUserStatu(String loginName, UserStatus userStatus) {
    	userDao.updateUserStatu(loginName, userStatus);
    }
}
