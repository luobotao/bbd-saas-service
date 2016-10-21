package com.bbd.saas.controllers.map;

import com.bbd.poi.api.Geo;
import com.bbd.poi.api.vo.MapPoint;
import com.bbd.saas.Services.AdminService;
import com.bbd.saas.api.mongo.SiteService;
import com.bbd.saas.api.mongo.UserService;
import com.bbd.saas.api.mysql.PostcompanyService;
import com.bbd.saas.api.mysql.PostmanUserService;
import com.bbd.saas.constants.Constants;
import com.bbd.saas.constants.UserSession;
import com.bbd.saas.enums.SiteStatus;
import com.bbd.saas.enums.UserStatus;
import com.bbd.saas.models.Postcompany;
import com.bbd.saas.mongoModels.Site;
import com.bbd.saas.mongoModels.User;
import com.bbd.saas.utils.MathEval;
import com.bbd.saas.utils.Numbers;
import com.bbd.saas.utils.StringUtil;
import com.bbd.saas.vo.SiteVO;
import com.bbd.saas.vo.UserVO;
import com.google.common.collect.Lists;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 运力分布
 */
@Controller
@RequestMapping("/capacityDistribution")
public class CapacityDistributionController {
	public static final Logger logger = LoggerFactory.getLogger(CapacityDistributionController.class);
	@Autowired
	AdminService adminService;
	@Autowired
	SiteService siteService;
	@Autowired
	PostmanUserService postmanUserService;
	@Autowired
	UserService userService;
	@Autowired
	Geo geo;
	@Autowired
	PostcompanyService postCompanyService;
	/**
	 * 跳转到运力分布页面
	 * @param request
	 * @param model
     * @return
     */
	@RequestMapping(value="", method=RequestMethod.GET)
	public String toPage(final HttpServletRequest request, Model model) {
		try {
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			if(!Constants.BBD_COMPANYID.equals(currUser.getCompanyId())){//棒棒达公司的默认不显示所有站点的所有数据，只有点击的时候才会显示
				Map<String, Object> map = this.getAllSiteAndCourier(null, null, null,null, null, request);
				if(map != null && map.size() > 0){
					model.addAttribute("siteList", map.get("siteList"));
					model.addAttribute("userList", map.get("userList"));
				}
			}
			//logger.info("=====运力分布站点===" + map);
			//设置地图默认的中心点
			SiteVO centerSite = getDefaultPoint(this.getCmpAddressByCmpId(currUser.getCompanyId()));
			model.addAttribute("centerSite", centerSite);
			return "map/capacityDistribution";
		} catch (Exception e) {
			logger.error("===跳转到运力分布页面==出错 :" + e.getMessage());
		}
		return "map/capacityDistribution";
	}
	private String getCmpAddressByCmpId(String companyId){
		if (companyId != null ){
			Postcompany company = postCompanyService.selectPostmancompanyById(Integer.parseInt(companyId));
			if(company != null){
				StringBuffer  addressBS = new StringBuffer();
				addressBS.append(StringUtil.initStr(company.getProvince(),""));
				addressBS.append(StringUtil.initStr(company.getCity(),""));
				addressBS.append(StringUtil.initStr(company.getArea(),""));
				addressBS.append(StringUtil.initStr(company.getAddress(),""));
				return  addressBS.toString();
			}
		}
		return null;
	}

	//地图默认中心位置--公司公司经纬度
	private SiteVO getDefaultPoint(String address){
		SiteVO centerSite = new SiteVO();
		if(address == null || "".equals(address)){
			centerSite.setName("");
			centerSite.setLat("39.915");
			centerSite.setLng("116.404");
			centerSite.setDeliveryArea("50000");
			return centerSite;
		}
		try {
			MapPoint mapPoint = geo.getGeoInfo(address);
			if (mapPoint != null) {
				logger.info("[address]:" + address + " [search geo result] 经度:" + mapPoint.getLng() + ",纬度："+ mapPoint.getLat());
				centerSite.setName("");
				centerSite.setLat(mapPoint.getLat()+"");
				centerSite.setLng(mapPoint.getLng()+"");
				centerSite.setDeliveryArea("50000");
			}else{//默认中心位置为北京
				logger.info("[address]:" + address + " [search geo result] null,无法获取经纬度");
				centerSite.setName("");
				centerSite.setLat("39.915");
				centerSite.setLng("116.404");
				centerSite.setDeliveryArea("50000");
			}
		}catch(Exception e){
			e.printStackTrace();
			logger.info("[address]:" + address + " [search geo result] exception,异常");
		}
		return centerSite;
		//map.put("centerSite", centerSite);
	}

	/**
	 *  根据站点Ajax更新地图
	 * @param siteIdStr 站点编号集合areaCode1,areaCode2---
	 * @param request 请求
	 * @return 分页列表数据
	 */
	@ResponseBody
	@RequestMapping(value="/getSiteAndCourierList", method=RequestMethod.GET)
	public Map<String, Object> getAllSiteAndCourier(String prov, String city, String area, String siteIdStr, Integer start, final HttpServletRequest request) {
		//查询数据
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			start = Numbers.defaultIfNull(start, -1);
			/*if(){

			}*/
			//当前登录的用户信息
			User currUser = adminService.get(UserSession.get(request));
			List<ObjectId> siteIdList = null;
			if(!StringUtils.isEmpty(siteIdStr)){//部分站点
				String [] ids = siteIdStr.split(",");
				if(ids.length > 0){
					siteIdList = new ArrayList<ObjectId>();
					for(String id : ids){
						siteIdList.add(new ObjectId(id));
					}
				}
			}
			List<SiteStatus> statusList = new ArrayList<SiteStatus>();
			statusList.add(SiteStatus.APPROVE);
			//查询登录用户的公司下的所有站点
			List<Site> siteList = siteService.findByCompanyIdAndAddress(currUser.getCompanyId(), prov, city, area, siteIdList, statusList, start);
			if(siteList != null && !siteList.isEmpty()){
				List<UserVO> userVOList = Lists.newArrayList();
				int siteSize = siteList.size();
				logger.info("getSiteAndCourierList : siteSize =" + siteSize);
				int index = 0, step = 100, max = 0;
				while (index < siteSize){//一次查询step条站点的派件员
					max = index + step;
					if(max > siteSize){
						max = siteSize;
					}
					logger.info("getSiteAndCourierList : index =" + index +", max="+max);
					List<Site> subSiteList = siteList.subList(index, max);//一次处理step个站点
					List<User> userList = userService.findUsersBySite(subSiteList, null, UserStatus.VALID);//所有小件员
					if (userList != null && userList.size() >0){
						List<Integer> postmanIdList = new ArrayList<Integer>();
						for (User user : userList){
							postmanIdList.add(user.getPostmanuserId());
						}
						//获取经纬度，Map<id, UserVo{id, lat, lon}>
						Map<Integer, UserVO> latAndLngMap = postmanUserService.findLatAndLngByIds(postmanIdList);
						//转化为UserVO
						for (User user : userList){
							//设置经纬度
							UserVO userVO2 = latAndLngMap.get(user.getPostmanuserId());
							UserVO userVO = userToUserVO(user, userVO2);
							userVOList.add(userVO);
						}
						logger.error("=getSiteAndCourierList=one===userVOList:" + userVOList.size());
					}
					index += step;
				}
				map.put("userList", userVOList);
				map.put("siteList", siteListToSiteVO(siteList));
			}
		} catch (Exception e) {
			logger.error("===ajax查询所有站点和派件员经纬度===出错:" + e.getMessage());
		}
		return map;
	}

	/**
	 * 转化为UserVO
	 * @param user 设置基本信息
	 * @param userVO2 携带经纬度信息
     * @return UserVO
     */
	private UserVO userToUserVO(User user, UserVO userVO2){
		UserVO userVO = new UserVO();
		userVO.setPostManId(user.getPostmanuserId());
		userVO.setLoginName(user.getLoginName());
		userVO.setRealName(user.getRealName());
		userVO.setSiteName(user.getSite().getName());
		BigDecimal temp = new BigDecimal(0.000000);
		//设置经纬度
		if(userVO2 == null || userVO2.getLat() == null || userVO2.getLng() == null || userVO2.getLat() == temp || userVO2.getLng() == temp){//默认为站点的经纬度
			userVO.setLat(MathEval.stringToBigDecimal(user.getSite().getLat()));
			userVO.setLng(MathEval.stringToBigDecimal(user.getSite().getLng()));
		}else{
			userVO.setLat(userVO2.getLat());
			userVO.setLng(userVO2.getLng());
		}
		return userVO;
	}
	private List<SiteVO> siteListToSiteVO(List<Site> siteList){
		List<SiteVO> siteVoList = null;
		if(siteList != null && siteList.size() > 0){
			siteVoList = new ArrayList<SiteVO>();
			for(Site site : siteList){
				SiteVO siteVo = new SiteVO();
				BeanUtils.copyProperties(site, siteVo);
				siteVo.setId(site.getId().toString());
				siteVoList.add(siteVo);
			}
		}
		return siteVoList;
	}


}
