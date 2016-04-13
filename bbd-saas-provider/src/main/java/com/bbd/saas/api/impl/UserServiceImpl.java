package com.bbd.saas.api.impl;


import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Key;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bbd.saas.api.UserService;
import com.bbd.saas.dao.UserDao;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;
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
	/**
     * 根据用户名查找是否存在此管理员
     * @param userName
     * @return
     */
    public User findUserByLoginName(String loginName) {
        return userDao.findOne("loginName",loginName);
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
     * 获取用户列表信息
     * @param PageModel<User> 
     * @return PageModel<User> 
     */
    public PageModel<User> findUserList(PageModel<User> pageModel){
    	return userDao.findUserList(pageModel);
    }
    
    /**
     * Description: 获取指定站点下的所有用户
     * @param siteId
     * @return
     * @author: liyanlei
     * 2016年4月12日上午11:27:25
     */
	@Override
	public List<UserVO> findUserListBySite(String siteId) {
		List<User> userList = userDao.findUserListBySite(siteId);
		List<UserVO> userVoList = new ArrayList<UserVO>();
		if(userList != null && userList.size() > 0){
			for(User user : userList){
				UserVO userVo = new UserVO();
				userVo.setId(user.getId().toString());
				userVo.setRealName(user.getRealName());
				//userVo.setLoginName(user.getLoginName());
				//userVo.setPhone(user.getPhone());
				userVoList.add(userVo);
			}
		}
		return userVoList;
	}
}
