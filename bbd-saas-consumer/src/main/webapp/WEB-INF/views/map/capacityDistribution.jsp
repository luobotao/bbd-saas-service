<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page import="com.bbd.saas.vo.SiteVO" %>
<%@ page import="com.bbd.saas.vo.UserVO" %>
<%@ page import="java.util.List" %>
<%@ include file="/WEB-INF/views/main.jsp"%>
<html>
<head>
	<title>运力分布 - 数据查询 - 棒棒达快递</title>
	<%
		String proPath = request.getContextPath();
		String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+proPath;
		List<SiteVO> siteList = (List<SiteVO>)request.getAttribute("siteList");
		List<UserVO> userList = (List<UserVO>)request.getAttribute("userList");
	%>
	<style>
		.capacity-map .BMapLabel{
			max-width:inherit;
			margin-bottom:0;
			border-radius:4px;
			padding:6px !important;
		}
		.capacity-map .BMapLabel:after {
			content: '';
			display: block;/*这个也很关键的*/
			position: absolute;
			width: 0px;
			height: 0px;
			bottom: -6px;
			left:32px;
			/*left: 50%;
            margin-left:-6px;*/
			border: 6px solid transparent;
			border-top: 6px solid rgba(4, 4, 4,0.7);
			border-bottom: none;
			z-index: 1;
		}
	</style>
</head>
<body>
<!-- S content -->
<div class="clearfix b-branch">
	<div class="container-fluid">
		<div class="row">
			<!-- S sidebar -->
			<div class="col-xs-12 col-sm-12 bbd-md-3" style="opacity:0;">
				<ul class="sub-sidebar">
				</ul>
			</div>
			<!-- E sidebar -->
			<!-- S detail -->
			<div class="b-detail col-xs-12 col-sm-12 bbd-md-9">
				<!-- S 搜索区域 -->
				<form class="form-inline form-inline-n">
					<div class="search-area">
						<div class="row pb20">
							<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
								<label>站点：　</label>
								<select id="siteId" class="form-control form-con-new">
									<option value="">全部</option>
									<c:if test="${not empty siteList}">
										<c:forEach var="site" items="${siteList}">
											<option value="${site.id}">${site.name}</option>
										</c:forEach>
									</c:if>
								</select>
							</div>
						</div>
					</div>
				</form>
				<!-- E 搜索区域 -->
				<!-- S map -->
				<div class="capacity-map mt20">
					<div id="capamap" class="capa-br" style="height:533px;"></div>
				</div>

				<!-- E map -->

			</div>
			<!-- E detail -->
		</div>
	</div>
</div>
<!-- E content -->

<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=5LVr5CieSP2a11pR4sHAtWGU"></script>
<!--加载鼠标绘制工具-->
<script type="text/javascript" src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
<link rel="stylesheet" href="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
<!--加载检索信息窗口-->
<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.js"></script>
<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.css" />
<script type="application/javascript">
	var defaultLng = "${centerSite.lng}";
	var defaultLat = "${centerSite.lat}";
	// 百度地图API功能
	var capamap = new BMap.Map("capamap", {enableMapClick:false,minZoom:8});
	//显示全部站点 -- 地图中心为公司经纬度
	var defaultPoint = new BMap.Point("${centerSite.lng}", "${centerSite.lat}");
	$(document).ready(function() {
		//显示站点和派件员信息
		initMap();
		//更改站点
		$("#siteId").change(function(){
			var siteId = $("#siteId option:selected").val();
			loadDataAndShow(siteId);
		});
	});
	//默认展示全部站点和所有派件员
	function initMap(){
		var center = new BMap.Point("${centerSite.lng}", "${centerSite.lat}");
		var radiusVal = 11;//显示大小级别--单个站点
		if (siteId == ""){//显示大小级别-全部
			radiusVal = 6;
		}
		capamap.centerAndZoom(center, radiusVal);
		capamap.enableScrollWheelZoom();
		//显示站点
		<%
			if (siteList != null) {
				for (SiteVO site : siteList) {
		%>
					showOnePoint("<%=site.getName()%>", "<%=site.getLng()%>", "<%=site.getLat()%>", 0);
		<%
				}
			}
		%>

		//显示派件员
		<%
			if (userList != null) {
				for (UserVO user : userList) {
		%>
					showOnePoint("<%=user.getRealName()%>", "<%=user.getLng()%>", "<%=user.getLat()%>", 1, "<%=user.getSiteName()%>");
		<%
				}
			}
		%>
	}

	//加载站点和派件员名称和经纬度信息
	function loadDataAndShow(siteId){
		$.ajax({
			type : "GET",  //提交方式
			url : "<%=path%>/capacityDistribution/getSiteAndCourierList",//路径
			data : {
				"siteId" : siteId
			},//数据，这里使用的是Json格式进行传输
			success : function(dataObject) {//返回数据
				capamap.clearOverlays();
				if (siteId == ""){//全部
					var center = dataObject.centerSite
					defaultLng = center.lng;
					defaultLat = center.lat;
					showJsonMap(center, dataObject.siteList, dataObject.userList);
				}else {
					//设置中心位置和显示派件员
					var site = dataObject.site;
					defaultLng = site.lng;
					defaultLat = site.lat;
					showJsonMap(site, null, dataObject.userList);
					//站点
					var site = dataObject.site;
					showOnePoint(site.name, site.lng, site.lat, 0);
				}
			},
			error : function() {
				alert("服务器繁忙，请稍后再试！");
			}
		});
	}
	//展示站点和派件员--ajax获取json对象
	function showJsonMap(centerSite, siteList, userList){
		console.log("centerSite.lng=" + centerSite.lng +"  centerSite.lat="+centerSite.lat);
		console.log("siteList=" + siteList +"  userList="+userList);
		var center = new BMap.Point(centerSite.lng, centerSite.lat);
		var radiusVal = 15;//显示大小级别--单个站点
		if (siteId == ""){//显示大小级别-全部
			radiusVal = 11;
		}
		capamap.centerAndZoom(center, radiusVal);
		capamap.enableScrollWheelZoom();
		var isShowAll = false;
		//显示站点
		if (siteList != null) {
			isShowAll = true;//查看全部
			var site = null;
			for (var i = 0;i < siteList.length ; i++) {
				site = siteList[i];
				showOnePoint(site.name, site.lng, site.lat, 0);
			}
		}

		//显示派件员
		if (userList != null) {
			var user = null;
			for (var i = 0;i < userList.length ; i++) {
				user = userList[i];
				if(isShowAll){//派件员需要显示站点名称
					showOnePoint(user.realName, user.lng, user.lat, 1, user.siteName);
				}else {//单个站点
					showOnePoint(user.realName, user.lng, user.lat, 1);
				}
			}
		}
	}
	function getRandom(numStr){
		var flag = Math.round(Math.random() * 2);
		var num = 0;
		var random = Math.random() * 0.005;
		if(flag > 1){
			num = parseFloat(numStr) + random;
		}else{
			num = parseFloat(numStr) - random;
		}
		//console.log("  numStr == "+ numStr + "   random===="+ random + " num ==="+num);
		return num;
	}
	//展示配送范围
	//flag-0:站点，flag-1:派件员
	//siteName:查看全部的时候需要显示派件员站点名称
	function showOnePoint(name, lng, lat, flag, siteName){
		var point = null;
		//经纬度为空，显示在公司周围或者站点周围
		if(lng == null || lng == "0.000000" || lng == "null" || lat == null || lat == "null" || lat == "0.000000" ){
			point = new BMap.Point(getRandom(defaultLng), getRandom(defaultLat));
		}else{
			point = new BMap.Point(lng, lat);
		}
		var iconPic = "courier.png";//派件员
		if (flag == 0){//站点
			iconPic = "b_pos.png";
		}
		var icon = new BMap.Icon("<%=path%>/resources/images/" + iconPic, new BMap.Size(64,64));
		var marker = new BMap.Marker(point,{icon:icon});
		var opts = {
			position : point,    // 指定文本标注所在的地理位置
			offset   : new BMap.Size(-36, -70)    //设置文本偏移量
		}
		//查看全部的时候需要显示派件员的站点名称
		if(siteName != null){
			name = name + "(" + siteName + ")";
		}
		var label = new BMap.Label(name, opts);  // 创建文本标注对象
		label.setStyle({
			color : "#fff",
			border : "0",
			fontSize : "18px",
			fontFamily:"simhei",
			backgroundColor:"rgba(4, 4, 4,0.7)",
		});
		//添加覆盖物
		capamap.addOverlay(marker);              // 将标注添
		capamap.addOverlay(label);
	}
</script>
</body>
</html>