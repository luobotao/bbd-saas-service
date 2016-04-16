package com.bbd.saas.api.mongo;

import java.util.List;

import org.mongodb.morphia.Key;

import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.UserQueryVO;
import com.bbd.saas.vo.UserVO;
import com.mongodb.WriteResult;

/**
 * Created by luobotao on 2016/4/11.
 * 站点用户接口
 */
public interface UserService {

	
    /**
     * Description: 根据用户id查询
     * @param userId 用户id
     * @return
     * @author: liyanlei
     * 2016年4月12日下午6:10:40
     */
    User findOne(String userId);

    /**
     * 根据用户名检索站点用户
     * @param loginName
     * @return
     */
    
    User findUserByLoginName(String loginName);
    
    /**
     * 根据user对象删除站点用户
     * @param user
     * @return WriteResult
     */
    public void delUser(User user);
    
    
    /**
     * 根据用户名检索站点用户
     * @param loginName
     * @return
     */
    User findUserByRealName(String realName);
    
    /**
     * 保存用户对象信息
     * @param user
     * @return Key<User>
     */
    Key<User> save(User user);
    
    /**
     * 获取用户列表信息
     * @param PageModel<User>
     * @return PageModel<User>
     */
    public PageModel<User> findUserList(PageModel<User> pageModel,UserQueryVO userQueryVO);
    
    /**
     * Description: 获取指定站点下的所有派件员
     * @param areaCode 站点编号
     * @return
     * @author: liyanlei
     * 2016年4月16日下午5:22:17
     */
    public List<UserVO> findUserListBySite(String areaCode);
    
}
