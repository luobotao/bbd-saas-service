package com.bbd.saas.api.impl.mysql;

import com.bbd.saas.api.mysql.PostmanUserService;
import com.bbd.saas.dao.mysql.PostmanUserDao;
import com.bbd.saas.models.PostmanUser;
import com.bbd.saas.vo.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 公司Service实现
 * Created by zuowenhai on 2016/4/18.
 */
@Service("userMysqlService")
@Transactional
public class PostmanUserServiceImpl implements PostmanUserService {
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
	public int updateByPhone(PostmanUser postmanUser){
		return postmanUserDao.updateByPhone(postmanUser);
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
	 * @return
	 */
	public List<UserVO> findLatAndLngByIds(List<Integer> ids){
		List<Map<String, Object>> postmanList = postmanUserDao.selectLatAndLngByIds(ids);
		return toUserVOList(postmanList);
	}
	private List<UserVO> toUserVOList(List<Map<String, Object>> postmanList){
		if (postmanList == null){
			return null;
		}
		List<UserVO> userVOList = new ArrayList<UserVO>();
		for (Map<String, Object> map : postmanList){
			UserVO userVO = new UserVO();
			userVO.setPostManId((Long) map.get("id"));
			userVO.setLoginName((String) map.get("phone"));
			userVO.setRealName((String) map.get("nickname"));
			userVO.setLat((BigDecimal) map.get("lat"));
			userVO.setLng((BigDecimal) map.get("lon"));
			userVOList.add(userVO);
		}
		return userVOList;
	}
}
