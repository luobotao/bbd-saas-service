package com.bbd.saas.api.impl.mysql;

import com.bbd.saas.api.mysql.PostmanUserService;
import com.bbd.saas.dao.mysql.PostmanUserDao;
import com.bbd.saas.models.PostmanUser;
import com.bbd.saas.utils.StringUtil;
import com.bbd.saas.vo.UserVO;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 公司Service实现
 * Created by zuowenhai on 2016/4/18.
 */
@Service("userMysqlService")
@Transactional
public class PostmanUserServiceImpl implements PostmanUserService {
	public static final Logger logger = LoggerFactory.getLogger(PostmanUserServiceImpl.class);
	@Resource
	private PostmanUserDao postmanUserDao;

	public PostmanUserDao getPostmanUserDao() {
		return postmanUserDao;
	}

	public void setPostmanUserDao(PostmanUserDao postmanUserDao) {
		this.postmanUserDao = postmanUserDao;
	}


	/**
     * 获取所有用户列表
     * @param 
     * @return
     */
	@Override
	@Transactional(readOnly = true)
	public List<PostmanUser> selectAll() {
		return postmanUserDao.selectAll();
	}

	/**
	 * 根据用户ID获取此用户
	 * @param uid
	 * @return
	 */
	@Override
	public PostmanUser findById(Integer uid){
		return postmanUserDao.findById(uid);
	}
	/**
	 * 根据用户token获取此用户
	 * @param token
	 * @return
	 */
	@Override
	public PostmanUser findByToken(String token) {
		return postmanUserDao.findByToken(token);
	}

	/**
     * 根据phone获取对应的postmanUser
     * @param phone
     * @return PostmanUser
     */
	public PostmanUser selectPostmanUserByPhone(String phone, Integer id){
		return postmanUserDao.selectPostmanUserByPhone(phone, id);
	}
	/**
     * 根据phone获取对应的postmanUser的id
     * @param phone
     * @return id
     */
    public int selectIdByPhone(String phone){
    	return postmanUserDao.selectIdByPhone(phone);
    }
	/**
     * 保存postmanUser
     * @param postmanUser
     * @return ret 成功或失败
     */
	public PostmanUser insertUser(PostmanUser postmanUser){
		postmanUserDao.insertUser(postmanUser);
		return postmanUser;
	}
	
	/**
     * 更新postmanUser
     * @param postmanUser
     * @return 
     */
	public int updateByPhone(PostmanUser postmanUser, String oldPhone){
		if(StringUtil.isEmpty(oldPhone)){//旧手机号和新手机好相同 == 手机号没有更改
			oldPhone = postmanUser.getPhone();
		}
		return postmanUserDao.updateByPhone(postmanUser, oldPhone);
	}
	
	/**
     * 删除postmanUser
     * @param id
     * @return 
     */
    public void deleteById(Integer id){
    	postmanUserDao.deleteById(id);
    }
    
    /**
     * 更新postmanUser
     * @param sta、id
     * @return 
     */
    public int updateById(@Param("sta") Integer sta,@Param("id") Integer id){
    	return postmanUserDao.updateById(sta, id);
    }
    
    /**
     * 更新postmanUser
     * @param nickname、id
     * @return 
     */
    public int updatePostmanUserById(String nickname,Integer id){
    	return postmanUserDao.updatePostmanUserById(nickname, id);
    }
	/**
	 * 根据公司Id查询所有派件员的经纬度
	 * @param companyId 公司Id
	 * @return
	 */
	public List<UserVO> findLatAndLngByCompanyId(String companyId){
		List<Map<String, Object>> postmanList = postmanUserDao.selectLatAndLngByCompanyId(companyId);
		return toUserVOList(postmanList);
	}
	/**
	 * 根据派件员id集合查询所有派件员的经纬度
	 * @param ids 派件员id(id1,id2,id3---)
	 * @return Map<id, UserVo{id, lat, lon}>
	 */
	public Map<Integer, UserVO>  findLatAndLngByIds(List<Integer> ids){
		Map<Integer, UserVO> userVOMap = new HashMap<Integer, UserVO>();
		List<Map<String, Object>> postmanList = postmanUserDao.selectLatAndLngByIds(ids);
		if (postmanList != null){
			for (Map<String, Object> postmanMap : postmanList){
				UserVO userVO = new UserVO();
				userVO.setPostManId(((Long) postmanMap.get("id")).intValue());
				userVO.setLat((BigDecimal) postmanMap.get("lat"));
				userVO.setLng((BigDecimal) postmanMap.get("lon"));
				userVOMap.put(userVO.getPostManId(), userVO);
			}
		}
		return userVOMap;
	}
	private List<UserVO> toUserVOList(List<Map<String, Object>> postmanList){
		if (postmanList == null){
			return null;
		}
		List<UserVO> userVOList = new ArrayList<UserVO>();
		for (Map<String, Object> map : postmanList){
			UserVO userVO = new UserVO();
			userVO.setPostManId(((Long) map.get("id")).intValue());
			userVO.setLoginName((String) map.get("phone"));
			userVO.setRealName((String) map.get("nickname"));
			userVO.setLat((BigDecimal) map.get("lat"));
			userVO.setLng((BigDecimal) map.get("lon"));
			userVOList.add(userVO);
		}
		return userVOList;
	}

	/**
	 * 获取积分
	 * @param areaCode
	 * @param phone
     * @return
     */
	@Transactional(propagation= Propagation.NEVER)
	public Map<String, Object> getIntegral(String areaCode,String phone){
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("areaCode", areaCode);
		map.put("phone", phone);
		//取得返回的结果集
		logger.info("[站点积分] 站点区域码："+areaCode+"，站长手机号："+phone);
		List<Map<String, Object>> results  = postmanUserDao.getIntegral(map);
		Map<String, Object> result = new HashMap<>();
		if(results!=null&&results.size()>0) {
			//第一条结果集 总数量
			try {
				result = results.get(0);
				logger.info("[站点积分]站点区域码：" + areaCode + "，站长手机号：" + phone + "，积分信息："+result.toString());
				//第二条订单列表
			}catch (Exception e){
				logger.info("[站点积分]站点区域码：" + areaCode + "，站长手机号：" + phone + ", 获取积分值失败，存储过程无返回，"+e.getMessage());
				logger.info(e.getMessage());
			}
		}
		return result;
	}

	@Override
	public int updateRoleByPhone(String phone, Integer postrole) {
		return postmanUserDao.updateRoleByPhone(phone, postrole);
	}

	/**
	 * 删除电话为phone,id不为id的postmanUser
	 * @param phone
	 * @param id
     */
	public int deleteByPhoneAndId(String phone, int id){
		return postmanUserDao.deleteByPhoneAndId(phone, id);
	}

	@Override
	public int updateSitenameBySiteId(String siteid, String siteName) {
		return postmanUserDao.updateSubstationBySiteId(siteid, siteName);
	}

	@Override
	public List<PostmanUser> findAllByAreaCode(String areaCode) {
		return postmanUserDao.findAllByAreaCode(areaCode);
	}
}
