package com.bbd.saas.api.mongo;

import com.bbd.saas.enums.UserRole;
import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.PageModel;
import com.bbd.saas.vo.UserQueryVO;
import com.bbd.saas.vo.UserVO;
import org.mongodb.morphia.Key;

import java.util.List;
import java.util.Map;

/**
 * Created by luobotao on 2016/4/11.
 * 站点用户接口
 */
public interface UserService {


    /**
     * 根据用户id查询
     * @param userId 用户id
     * @return 用户
     * @author: liyanlei
     * 2016年4月12日下午6:10:40
     */
    User findOne(String userId);

    /**
     * 根据用户名检索站点用户
     * @param loginName 登录名即为手机号
     * @return 用户
     */
    User findUserByLoginName(String loginName);

    /**
     * 查询用户名为loginName, _id不为userId的用户
     * @param loginName 用户名即为手机号
     * @param userId _id
     * @return 用户对象
     */
    User findByLoginNameAndId(String loginName, String userId);

    /**
     * 根据user对象删除站点用户
     * @param user 用户
     */
    public void delUser(User user);

    /**
     * 根据用户姓名检索站点用户
     * @param realName 姓名
     * @return 用户
     */
    User findUserByRealName(String realName);

    /**
     * 保存用户对象信息
     * @param user 用户
     * @return Key<User>保存结果
     */
    Key<User> save(User user);

    /**
     * 根据条件分页查询指定站点下的用户列表信息
     * @param pageModel 分页对象
     * @param userQueryVO 查询条件
     * @param site 站点对象
     * @return 分页对象（分页信息和当前页的数据）
     */
    public PageModel<User> findUserList(PageModel<User> pageModel,UserQueryVO userQueryVO,Site site);

    /**
     * 根据条件分页查询多个站点下的用户列表信息
     * @param pageModel 分页对象
     * @param userQueryVO 查询条件
     * @param siteList 站点对象集合
     * @return 分页对象（分页信息和当前页的数据）
     */
    public PageModel<User> findPageUser(PageModel<User> pageModel, UserQueryVO userQueryVO, List<Site> siteList);

    /**
     * 获取指定站点编号下的所有派件员 -- 暂时没有用到
     * @param areaCode 站点编号
     * @return 派件员VO集合
     * @author: liyanlei
     * 2016年4月16日下午5:22:17
     */
    public List<UserVO> findUserListBySite(String areaCode);
    /**
     * Description: 获取指定站点下的所有派件员
     * @param site 站点
     * @return 派件员VO集合
     * @author: liyanlei
     * 2016年4月16日下午5:22:17
     */
    public List<UserVO> findUserListBySite(Site site);

    /**
     * 根据site和staffid查找是该staffid是否在该站点已存在
     * @param site 站点
     * @param staffid 员工Id
     * @return 用户（派件员）
     */
    public User findOneBySiteByStaffid(Site site, String staffid);

    /**
     * 更新用户状态
     * @param loginName 登录名称
     * @param userStatus 用户状态
     */
    public void updateUserStatu(String loginName, UserStatus userStatus);

    /**
     * 更新派件员--到站的权限，开通时，密码为空，设置初始化密码
     * @param loginName 登录名称
     * @param dispatchPermsn 权限 0：关闭到站的权限；1：开通到站的权限
     */
    public int updateDispatchPermsn(String loginName, Integer dispatchPermsn, String pwd);

    /**
     * 根据用户名（手机号）去更新此用户的公司ID
     * @param companyId 公司id
     * @param phone  用户名（手机号）
     */
    void updateCompanyIdByLoginName(int companyId, String phone);

    /**
     * 删除此站点下的所有用户
     * @param siteId 站点id
     */
    void delUsersBySiteId(String siteId);

    /**
     * 根据公司Id查询所有派件员 -- 咱没有用到
     * @param companyId 公司Id
     * @return 派件员集合
     */
    public List<User> findUserListByCompanyId(String companyId);
    /**
     * 根据站点、用户角色、用户状态查询所有派件员
     * @param site 站点
     * @param userRole 用户角色
     * @param userStatus 用户状态
     * @return 用户集合
     */
    public List<User> findUsersBySite(Site site, UserRole userRole,UserStatus userStatus);
    /**
     * 根据站点、用户角色、用户状态查询所有派件员
     * @param siteList 站点集合
     * @param userRole 用户角色
     * @param userStatus 用户状态
     * @return 用户集合
     */
    public List<User> findUsersBySite(List<Site> siteList, UserRole userRole,UserStatus userStatus);

    /**
     * 根据staffidLis获得同一公司下的派件员user与站点名称的对应关系
     * @param staffidList 员工Id
     * @param companyId 公司Id
     * @return map<postmanUserId, siteName>
     */
    public Map<Long, String> findUserSiteMap(List<Long> staffidList, String companyId);

    /**
     * 根据站点和到站权限，查询符合条件的数目
     * @param site 站点
     * @param dispatchPermsn 到站权限
     * @return 符合查询条件的数目
     */
    public long findCountBySiteAndDisptcherPermsn(Site site, int dispatchPermsn);

    /*
   * 查找指定的用户
   */
    public User findByAppkeyAndSessionkey(String appKey, String sessionKey);

    /**
     * app端-根据站点查询小件员列表
     * @param site 站点
     * @param role 角色
     * @param lastindex 分页跳过的条数
     * @param pagesize 要查询的条数
     * @return 小件员列表
     */
    public PageModel<User> findPageUser(Site site, UserRole role, UserStatus userStatus, int lastindex, int pagesize);

}
