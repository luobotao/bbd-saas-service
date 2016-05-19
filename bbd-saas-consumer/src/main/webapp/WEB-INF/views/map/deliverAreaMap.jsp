<%@ page import="com.bbd.saas.vo.SiteVO" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/main.jsp"%>
<html>
<head>
	<title>配送区域 - 系统设置 - 棒棒达快递</title>
	<%
		List<SiteVO> siteList = (List<SiteVO>)request.getAttribute("siteList");
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
		.prompt-txt {
			display: block;
			min-width: 300px;
			padding: 0 20px;
			height: 50px;
			line-height: 48px;
			font-size: 20px;
			color: #3d3838;
			text-align: center;
			background: #ffc800;
			border: 1px solid #fc9e28;
			left: 60%;
			margin-left: 80px;
			z-index: 1060;
		}
		.ckbox {
			background: rgb(255, 255, 255);
			cursor: pointer;
			width: 17px;
			height: 17px;
		}
		.ckbox_ajax {
			-webkit-appearance: none;
			background: #fff ;/*background: #fff url(i/blue.png);*/
			display: block;
			width: 17px;
			height: 17px;
			padding: 0 20px;
			line-height: 48px;
			font-size: 20px;
			color: #3d3838;
			text-align: center;
			/*background: #ffc800;*/
			border: 1px solid #fc9e28;
			margin-left: 80px;
			z-index: 1060;
		}
	</style>
</head>
<body class="fbg">
<!-- S content -->
<div class="clearfix b-branch">
	<div class="container-fluid">
		<div class="row">
			<!-- S sidebar -->
			<div class="col-xs-12 col-sm-12 bbd-md-3" style="visibility: hidden;">
				<ul class="sub-sidebar">
				</ul>
			</div>
			<!-- E sidebar -->
			<!-- S detail -->
			<div class="b-detail col-xs-12 col-sm-12 bbd-md-9 j-full-div">
				<!-- S 搜索区域 -->
				<div class="search-area d-search-area j-detail-area">
					<ul class="row pb20">
						<li class="txt-info txt-info-l">
							<em>公司名称：</em>
							<i>${company.companyname}</i>
						</li>
						<li class="txt-info">
							<em>公司编码：</em>
							<i>${company.companycode}</i>
						</li>
						<li class="txt-info">
							<em>公司地址：</em>
							<i>${company.province}-${company.city}-${company.area} ${company.address}</i>
						</li>
					</ul>
				</div>
				<!-- E 搜索区域 -->

				<!-- S tab -->
				<div class="tab-area mt20">
					<ul class="clearfix b-tab">
						<li <c:if test="${activeNum eq '1'}"> class="tab-cur"</c:if>><a href="#send-range">配送区域</a></li>
						<li <c:if test="${activeNum eq '2'}"> class="tab-cur"</c:if>><a href="#draw-map" >绘制电子围栏</a></li>
						<li <c:if test="${activeNum eq '3'}"> class="tab-cur"</c:if>><a href="#import-key">导入地址关键词</a></li>
					</ul>
					<div class="b-tab-con form-inline form-inline-n tab-content capacity-map">
						<!-- S 配送区域 -->
						<div class="row tab-pane fade" id="send-range">
							<div class="col-md-12 pb20 f16">
								设置配送范围后，将优先匹配站点附近的订单。
							</div>
							<form  method="POST" id="siteRadiusForm">
								<div class="col-md-3 pb20">
									<label>站点：　</label>
									<select id="siteId" class="form-control form-con-new">
										<option value="">请选择</option>
										<c:if test="${not empty siteList}">
											<c:forEach var="site" items="${siteList}">
												<option value="${site.id}">${site.name}</option>
											</c:forEach>
										</c:if>
									</select>
								</div>
								<div class="col-md-4 pb20">
									<label class="f16">
										站点周围：<c:set var="count" value="20"/>
										<select id="radius" name="radius"  class="form-control form-con-new f16">
											<option value="0">请选择</option>
											<c:forEach var = "temp" begin="1" step="1" end="${count}">
												<option value ="${temp}" <c:if test="${temp eq site.getDeliveryArea()}">selected</c:if>>${temp}</option>
											</c:forEach>
										</select>  公里
									</label>
								</div>
								<div class="col-md-12">
									<div class="b-map">
										<div id="areaMap" style="height: 533px;"></div>
										<div class="draw-btn" id="areaTool">
											<div class="bg-alpha" id="areaTool_bg"></div>
											<a href="javascript:void(0);" class="ser-btn l ml12" id="saveSiteBtn">　保存　</a>
										</div>
									</div>
								</div>
							</form>
						</div>
						<!-- E 配送区域 -->

						<!-- S 绘制电子围栏 -->
						<div class="row tab-pane fade" id="draw-map">
							<div class="col-md-3 pb20">
								<label>站点：　</label>
								<select id="fenceSiteId" class="form-control form-con-new">
									<option value="">请选择</option>
									<c:if test="${not empty siteList}">
										<c:forEach var="site" items="${siteList}">
											<option value="${site.id}">${site.name}</option>
										</c:forEach>
									</c:if>
								</select>
							</div>
							<div class="col-md-12">
								<div class="b-map">
									<div id="fenceMap" class="bod-rad capacity-map" style="height: 533px;"></div>
									<a href="javascript:void(0)" onclick="fenceObj.theLocation()" class="pos-adr"></a>
									<div class="b-f-screen b-forward-full j-full-btn"></div>
									<div class="draw-btn" id="eFenceTool">
										<div class="bg-alpha" id="eFenceTool_bg"></div>
										<a href="javascript:void(0);" class="ser-btn c ml12" onclick="openDraw()">绘制</a>
										<a href="javascript:void(0);" class="ser-btn d ml6" id="formBtn">提 交</a>
									</div>
								</div>
							</div>
						</div>
						<!-- E 绘制电子围栏 -->
						<!-- S 导入地址关键词 -->
						<div class="clearfix tab-pane fade" id="import-key">
							<div class="row pb20">
								<c:url var="importKeywordUrl" value="/siteKeyWord/importKeyword?${_csrf.parameterName}=${_csrf.token}"/>
								<form action="${ctx}/deliverArea/queryKeyWord" method="get" id="siteKeywordForm" name="siteKeywordForm" class="form-inline form-inline-n">
									<div class="form-group col-lg-3">
										<label>站点：　</label>
										<select id="keywordSiteId" name="siteId" class="form-control form-con-new">
											<c:if test="${not empty siteList}">
												<c:forEach var="site" items="${siteList}">
													<option value="${site.id}">${site.name}</option>
												</c:forEach>
											</c:if>
										</select>
									</div>
									<div class="form-group col-lg-3">
										<label>导入时间：</label>
										<input id="between" name="between" type="text" class="form-control" placeholder="请选择导入时间范围" value="${between}"/>
									</div>
									<div class="form-group col-lg-3">
										<label>关键词：</label>
										<input id="keyword" name="keyword" type="text" class="form-control" placeholder="请输入关键词" value="${keyword}"/>
									</div>
									<div class="form-group col-lg-2">
										<a href="javascript:void(0)" class="ser-btn l" id="querySiteBtn"><i class="b-icon p-query p-ser"></i>查询</a>
									</div>
									<input type="hidden" id="pageIndex" value="${pageIndex}" name="pageIndex">
								</form>
							</div>
							<div class="row pb20">
								<div class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
									<form action="${importKeywordUrl}" method="post" enctype="multipart/form-data" id="importFileForm">
										<input type="hidden" name="siteId" id="imptSiteId" />
										<label class="ser-btn b fileup_ui fl">
											<span>导入地址关键词</span>
											<input type="file" name="file" class="import-file" />
										</label>

										<a href="${ctx}/site/downloadSiteKeywordTemplate" class="ser-btn b ml6">下载导入模板</a>
										<a href="javascript:void(0)" onclick="exportKeyword();" class="ser-btn b ml10">导出地址关键词</a>
									</form>
									<form action="${ctx}/siteKeyWord/exportKeyWord" method="get" id="exptForm">
										<input id="siteId_expt" name="siteId" type="hidden" />
										<input id="between_expt" name="between" type="hidden" />
										<input id="keyword_expt" name="keyword" type="hidden" />
									</form>
								</div>
							</div>

							<!-- S table -->
							<div class="tab-bod mt20">
								<div class="table-responsive">
									<table class="table" id="dis-table">
										<thead>
										<tr>
											<th><input type="checkbox" name="inputA" class="j-sel-all c-cbox" id="selectAll" /></th>
											<th>站点名称</th>
											<th>导入日期</th>
											<th>省</th>
											<th>市</th>
											<th>区</th>
											<th>地址关键词</th>
											<th>操作</th>
										</tr>
										</thead>
										<tbody id="dataList">

										<c:forEach items="${siteKeywordPageList}" var="siteKeyword">
											<tr>
												<td><input type="checkbox" value="${siteKeyword.id}" name="inputC" class="c-cbox"/></td>
												<td>${siteKeyword.siteId}</td>
												<td>${Dates.formatDateTime_New(siteKeyword.createAt)}</td>
												<td>${siteKeyword.province}</td>
												<td>${siteKeyword.city}</td>
												<td>${siteKeyword.distict}</td>
												<td>${siteKeyword.keyword}</td>
												<td><a href="javascript:void(0)" onclick="deleteKeyword('${siteKeyword.id}')"  class="orange">删除</a></td>
											</tr>
										</c:forEach>
										</tbody>
									</table>
								</div>
								<!-- E table -->
								<!-- S tableBot -->
								<div class="clearfix">
									<!-- S button -->
									<div class="clearfix fl pad20">
										<a href="javascript:void(0);" id="piliangDel" class="ser-btn l">批量删除</a>
									</div>
									<!-- E button -->
									<!-- S page -->
									<div id="pagin"></div>
									<!-- E page -->
								</div>
								<!-- E tableBot -->
							</div>
						</div>

						<!-- E 导入地址关键词 -->
					</div>
				</div>
				<!-- S tab -->

			</div>
			<!-- E detail -->
		</div>
	</div>
</div>
<!-- E content -->
<!-- S footer -->
<footer class="pos-footer tc">
	<em class="b-copy">京ICP备 465789765 号 版权所有 &copy; 2016-2020 棒棒达       北京棒棒达科技有限公司</em>
</footer>
<!-- E footer -->
<div class="b-loading">
	<div class="spinner" style="display:none">
		<div class="spinner-container container1">
			<div class="circle1"></div>
			<div class="circle2"></div>
			<div class="circle3"></div>
			<div class="circle4"></div>
		</div>
		<div class="spinner-container container2">
			<div class="circle1"></div>
			<div class="circle2"></div>
			<div class="circle3"></div>
			<div class="circle4"></div>
		</div>
		<div class="spinner-container container3">
			<div class="circle1"></div>
			<div class="circle2"></div>
			<div class="circle3"></div>
			<div class="circle4"></div>
		</div>
		<div class="">
			<i class="prompt-txt" >正在导入，请稍候...</i>
		</div>
	</div>
</div>

<!-- S pop -->

<!--S 提示-->
<div class="j-import-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog b-modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title tc">导入</h4>
			</div>
			<div class="modal-body">
				<div class="col-md-12">
					<span>确定要导入吗？</span>
				</div>
			</div>
			<div class="modal-footer mt20 bod0">
				<a href="javascript:void(0);" class="ser-btn g" data-dismiss="modal" id="cancelImpt">取消</a>
				<a href="javascript:void(0);" class="ser-btn l" id="importBtn">确定</a>
			</div>
		</div>
	</div>
</div>

<!--E 提示-->

<!-- E pop -->
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=5LVr5CieSP2a11pR4sHAtWGU"></script>
<!--加载鼠标绘制工具-->
<script type="text/javascript" src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
<link rel="stylesheet" href="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
<!--加载检索信息窗口-->
<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.js"></script>
<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.css" />
<script type="application/javascript">
	var defaultCenter = new BMap.Point(${centerPoint.lng}, ${centerPoint.lat});
	var defaultZoom = 11;
	/************************ 配送区域 ************* start **************************/
	// 百度地图API功能
	var areaMap = new BMap.Map("areaMap", {enableMapClick:false,minZoom:5});

	/*================初始化加载配送区域=======================start===============*/
	function initDeliveryMap(){
		areaMap.enableScrollWheelZoom(true);
		//设置地图中心点和放大级别
		areaMap.centerAndZoom(defaultCenter, 11);
		//显示站点和配送区域
		<%
			if (siteList != null) {
				for (SiteVO site : siteList) {
		%>
		showOneSiteArea2("<%=site.getName()%>", "<%=site.getLng()%>", "<%=site.getLat()%>", "<%=site.getDeliveryArea()%>");
		<%
				}
			}
		%>

	}

	function getPointBySite2(lng, lat){
		var point = null;
		if(lng != null && lng != "0.000000" && lng != "null" && lat != null && lat != "null" && lat != "0.000000" ){
			point = new BMap.Point(lng, lat);
		}else{
			point = defaultCenter;//设置默认中心点
		}
		return point;
	}
	//显示一个站点及其配送范围
	function showOneSiteArea2(name, lng, lat, radius){
		var point = getPointBySite2(lng, lat);
		if(radius == null || radius == "null" ||  radius == ""){
			radius = 0;
		}
		var myIcon = new BMap.Icon("${ctx}/resources/images/b_marker.png", new BMap.Size(20,25));
		var marker = new BMap.Marker(point,{icon:myIcon});  // 创建标注
		areaMap.addOverlay(marker);               // 将标注添加到地图中
		var label = newLabel(point, name);
		areaMap.addOverlay(label);               // 将label添加到地图中
		var circle = new BMap.Circle(point, radius*1000,{fillColor:"#ff2400", strokeColor:"#ff2400", strokeWeight: 1 ,fillOpacity: 0.1, strokeOpacity: 1});
		areaMap.addOverlay(circle);            //增加圆
	}
	initDeliveryMap();
	/*================初始化加载配送区域=======================end===============*/


	//配送区域--更改站点
	$("#siteId").change(function(){
		var siteId = $("#siteId").val();
		showSiteChangeMap(siteId);
	});
	//更改配送范围
	$("#radius").change(function(){
		var siteId = $("#siteId").val();
		var radius = $("#radius").val();
		if (siteId == "") {//全部
			outDiv("请选择一个站点");
			return;
		}
		showRadiusChangeMap(siteId, radius);
	})
	//展示配送范围--更改站点
	function showSiteChangeMap(siteId){
		$.ajax({
			type : "GET",  //提交方式
			url : "${ctx}/deliverArea/getSiteById",//路径
			data : {
				"siteId" : siteId
			},//数据，这里使用的是Json格式进行传输
			success : function(dataObject) {//返回数据
				//清除所有覆盖物
				areaMap.clearOverlays();
				if (siteId == ""){//全部
					/*$("#areaTool").hide();
					 $("#areaTool_bg").hide();*/
					//更新配送区域
					$("#radius").val(0);
					var centerSite = dataObject.centerSite;
					var siteList = dataObject.siteList;

					//显示站点和配送区域
					if (siteList != null) {
						var center = getPointBySite(centerSite);
						//设置地图中心点和放大级别
						areaMap.centerAndZoom(center, defaultZoom);
						var circle = new BMap.Circle(center, radius * 1000);
						areaMap.addOverlay(circle);
						//显示站点和配送区域
						for (var i = 0;i < siteList.length ; i++) {
							showOneSiteArea(siteList[i]);
						}
					}
				} else {
					/*$("#areaTool").show();
					 $("#areaTool_bg").show();*/
					var site = dataObject.site;
					var center = getPointBySite(site);
					var zoom = getMapZoom(site.deliveryArea);
					//设置地图中心点和放大级别
					areaMap.centerAndZoom(center, zoom);
					//显示站点和配送区域
					showOneSiteArea(site);
					//更新配送区域
					if(site.deliveryArea == null || site.deliveryArea == ""){
						$("#radius").val(0);
					}else {
						$("#radius").val(site.deliveryArea);
					}
				}
			},
			error : function() {
				outDiv("服务器繁忙，请稍后再试");
			}
		});
	}
	//根据半径获得地图放大级别
	function getMapZoom(radius){
		//地图放大级别
		var radiusVal = 11;
		if(radius == null){
			return radiusVal;
		}
		radius = radius * 1000;
		if(radius<2000){
			radiusVal = 15;
		}else if(radius<3000){
			radiusVal = 14;
		}else if(radius<5000){
			radiusVal = 13;
		}else if(radius<10000){
			radiusVal = 12;
		}else{
			radiusVal = 11;
		}
		return radiusVal;
	}

	function getPointBySite(site){
		var point = null;
		if(site != null && site.lng != null && site.lng != "0.000000" && site.lng != "null" && site.lat != null && site.lat != "null" && site.lat != "0.000000" ){
			point = new BMap.Point(site.lng, site.lat);
		}else{
			point = defaultCenter;//设置默认中心点
		}
		return point;
	}
	//显示一个站点及其配送范围
	function showOneSiteArea(site, radius){
		var point = getPointBySite(site);
		//radius优先级高于site.deliveryArea
		if(radius == null || radius == ""){
			if(site.deliveryArea == null || site.deliveryArea == ""){
				radius = 0;
			}else{
				radius = site.deliveryArea;
			}
		}
		//radius = 2;
		var myIcon = new BMap.Icon("${ctx}/resources/images/b_marker.png", new BMap.Size(20,25));
		var marker = new BMap.Marker(point,{icon:myIcon});  // 创建标注
		areaMap.addOverlay(marker);               // 将标注添加到地图中
		var label = newLabel(point, site.name);
		areaMap.addOverlay(label);               // 将label添加到地图中
		var circle = new BMap.Circle(point, radius * 1000,{fillColor:"#ff2400", strokeColor:"#ff2400", strokeWeight: 1 ,fillOpacity: 0.1, strokeOpacity: 1});
		areaMap.addOverlay(circle);          //增加圆
	}
	function newLabel(point, name){
		var opts = {
			position : point,    // 指定文本标注所在的地理位置
			offset   : new BMap.Size(-40, -50)    //设置文本偏移量
		}
		var label = new BMap.Label(name, opts);  // 创建文本标注对象
		label.setStyle({
			color : "#fff",//"#fff"
			border : "0",
			fontSize : "16px",
			fontFamily:"simhei",
			backgroundColor:"rgba(4, 4, 4,0.7)",
		});
		return label;
	}
	function newEFenceLabel(point, name){
		var index = 25;
		/*if(){

		 }*/

		var opts = {
			position : point,    // 指定文本标注所在的地理位置
			offset   : new BMap.Size(-45, -20)    //设置文本偏移量
		}
		var label = new BMap.Label(name, opts);  // 创建文本标注对象
		label.setStyle({
			color : "#fff",//"#fff"
			border : "0",
			fontSize : "16px",
			fontFamily:"simhei",
			backgroundColor:"rgba(4, 4, 4,0.7)",
		});
		return label;
	}
	//展示配送范围--更改半径
	function showRadiusChangeMap(siteId, radius){
		$.ajax({
			type : "GET",  //提交方式
			url : "${ctx}/deliverArea/getSiteById",//路径
			data : {
				"siteId" : siteId
			},//数据，这里使用的是Json格式进行传输
			success : function(dataObject) {//返回数据
				//清除所有覆盖物
				areaMap.clearOverlays();
				var site = dataObject.site;
				var center = getPointBySite(site);
				var zoom = getMapZoom(radius);
				//设置地图中心点和放大级别
				areaMap.centerAndZoom(center, zoom);
				//显示站点和配送区域
				showOneSiteArea(site, radius);
			},
			error : function() {
				outDiv("服务器繁忙，请稍后再试");
			}
		});
	}

	//保存站点配送范围信息
	$("#saveSiteBtn").click(function(){
		var siteId = $("#siteId").val();
		if(siteId == "" || siteId == null){
			outDiv("请先选择站点");
			return false;
		}
		var radiusVal = $("#radius").val();
		if(radiusVal==0){
			outDiv("请选择站点配送范围");
			return false;
		}
		$.ajax({
			url: '${ctx}/site/updateSiteWithRadius/'+radiusVal+'/'+$("#siteId").val(),
			type: 'get',
			cache: false,
			dataType: "text",
			data: {},
			success: function(response){
				outDiv("保存成功");
				//window.location.href="${ctx}/deliverArea/map/1";
			},
			error: function(){
				outDiv('服务器繁忙，请稍后再试');
			}
		});
	});

	/************************ 配送区域 ************* end **************************/

	/************************ 绘制电子围栏 ************* start **************************/
	var fenceArray = [];
	//初始化电子围栏一个站点的数据
	function addOneEFenceData(efence){
		var siteArr = efence.split(";");
		for (var i = 0; i < siteArr.length; i++) {
			var arr = siteArr[i].split(",");
			var barr = [];
			for (var j = 0; j < arr.length; j++) {
				var tmp = arr[j].split("_");
				barr.push(new BMap.Point(tmp[0], tmp[1]));
			}
			fenceArray.push(barr);
		}
	}
	function addAllEFenceData(){

	}
	var fenceObj = {
		status: false,
		map: '',
		point: '',
		efenceObj: '',//{name, overlay[][]}
		overlays: [],
		overlaysCache: [],
		myPolygon: '',
		myOverlay: [],
		drawingManager: '',
		styleOptions: {
			strokeColor:"red",      //边线颜色。
			fillColor:"red",        //填充颜色。当参数为空时，圆形将没有填充效果。
			strokeWeight: 3,        //边线的宽度，以像素为单位。
			strokeOpacity: 0.8,     //边线透明度，取值范围0 - 1。
			fillOpacity: 0.3,       //填充的透明度，取值范围0 - 1。
			strokeStyle: 'solid'    //边线的样式，solid或dashed。
		},
		/**
		 * 实例化
		 */
		init: function(centerPoint){
			if(this.status){
				return;
			}
			if(centerPoint == null){
				centerPoint = defaultCenter;
			}
			this.status = true;
			this.point = centerPoint;
			this.map = new BMap.Map('fenceMap',{enableMapClick:false,minZoom:5,noAnimation:true});
			this.map.centerAndZoom(this.point,12);
			this.map.enableScrollWheelZoom();
			this.map.disableInertialDragging();
			var styleOptions = this.styleOptions;
			//实例化鼠标绘制工具
			this.drawingManager = new BMapLib.DrawingManager(this.map, {
				isOpen: false, //是否开启绘制模式
				enableDrawingTool: false, //是否显示工具栏
				drawingToolOptions: {
					anchor: BMAP_ANCHOR_TOP_RIGHT, //位置
					offset: new BMap.Size(5, 5), //偏离值
					scale: 0.8 //工具栏缩放比例
				},
				polygonOptions: styleOptions, //多边形的样式
			});
			//添加鼠标绘制工具监听事件，用于获取绘制结果
			this.drawingManager.addEventListener('overlaycomplete', fenceObj.overlaycomplete);
			// S 增加的控件
			var bottom_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT});// 左上角，添加比例尺
			var bottom_right_navigation = new BMap.NavigationControl({
				anchor: BMAP_ANCHOR_BOTTOM_RIGHT, type: BMAP_NAVIGATION_CONTROL_ZOOM
			}); //右上角，仅包含平移和缩放按钮
			this.map.addControl(bottom_left_control);
			this.map.addControl(bottom_right_navigation);
			// E 增加的控件

			/*加载所有站点和其多边形围栏*/
			this.loadAllSiteAndEFence();
			/*if (this.myOverlay) {
			 this.loadMyOverlay();
			 };*/

		},
		loadOneSite: function(name, lng, lat){//加载站点标注
			var myIcon = new BMap.Icon("${ctx}/resources/images/b_marker.png", new BMap.Size(20,25));
			var marker = new BMap.Marker(new BMap.Point(lng, lat),{icon:myIcon});  // 创建标注
			//marker.disableMassClear();//右键删除电子围栏的时候，不能被删除
			//marker.enableMassClear;
			this.map.addOverlay(marker);               // 将标注添加到地图中
			/*var label = newLabel(new BMap.Point(lng, lat), name);
			this.map.addOverlay(label);               // 将label添加到地图中*/
		},
		loadAllSiteAndEFence: function(){//加载站点标注
			//加载所有站点
			<%
				if (siteList != null) {
					for (SiteVO site : siteList) {
			%>
			//加载站点
			this.loadOneSite("<%=site.getName()%>", "<%=site.getLng()%>", "<%=site.getLat()%>");
			//加载电子围栏
			var efenceObj = new EFenceObj("<%=site.getName()%>", "<%=site.geteFence()%>", "<%=site.getLng()%>", "<%=site.getLat()%>");
			efenceObj.loadDataAndShow(false);
			<%
                    }
                }
            %>
		},
		loadMyOverlay: function(){
			var map = this.map;
			this.myOverlay.forEach(function(e){
				myPolygon = new BMap.Polygon(e, this.styleOptions);
				this.myPolygon = myPolygon;
				try{
					myPolygon.enableEditing();
					myPolygon.enableMassClear();
				}catch(e){}
				myPolygon.addEventListener("lineupdate",function(e){
					fenceObj.showLatLon(e.currentTarget.ro);
				});
				myPolygon.addEventListener("rightclick",function(e){
					if(confirm("确认删除该电子围栏？")){
						fenceObj.delPolygon(e);
					}
				});
				fenceObj.overlays.push(myPolygon);
				map.addOverlay(myPolygon);
			})
		},
		showLatLon: function(a){
			var len = a.length;
			var s = '';
			var arr = [];
			for(var i =0 ; i < len-1; i++){
				arr.push([a[i].lng, a[i].lat]);
				s += '<li>'+ a[i].lng +','+ a[i].lat +'<span class="red" title="删除" onclick="fenceObj.delPoint('+i+')">X</span></li>';
			}
			this.overlaysCache = arr;
			$("panelWrap").innerHTML = '<ul>'+ s +'</ul>';
		},
		delPoint: function(i){
			if(this.overlaysCache.length <=3 ){
				outDiv('不能再删除, 请保留3个以上的点.');
				return;
			}
			this.overlaysCache.splice(i,1);
			var a = this.overlaysCache;
			var newOverlay = [];
			for(var i in a ){
				newOverlay.push( new BMap.Point(a[i][0],a[i][1]) );
			}
			this.myOverlay = newOverlay;
			this.loadMyOverlay();
		},
		/**
		 *回调获得覆盖物信息
		 */
		overlaycomplete: function(e){
			fenceObj.overlays.push(e.overlay);
			e.overlay.enableEditing();
			e.overlay.addEventListener("lineupdate",function(e){
				fenceObj.showLatLon(e.currentTarget.ro);
			});
			e.overlay.addEventListener("rightclick",function(e){
				if(confirm("确认删除该电子围栏？")){
					fenceObj.delPolygon(e);
				}
			});
		},
		//监听左键click事件
		delPolygon:function(e){
			var map = this.map;
			map.removeOverlay(e.target);
			var overlays = this.overlays;
			var overlaysTmp = [];
			for(var i = 0; i < overlays.length; i++){
				if(overlays[i] != e.target){
					overlaysTmp.push(overlays[i]);
				}
			}
			this.overlays=overlaysTmp;
		},
		/**
		 * 清除覆盖物
		 */
		clearAll: function() {
			var map = this.map;
			var overlays = this.overlays;
			for(var i = 0; i < overlays.length; i++){
				map.removeOverlay(overlays[i]);
			}
			this.overlays.length = 0
			map.clearOverlays();
			this.myPolygon = '';
		},
		/**
		 *取覆盖物的经纬度
		 */
		getOverLay: function(){
			var box = this.myPolygon ? this.myPolygon : this.overlays[this.overlays.length - 1];
		},
		getCount: function(){
			var n = 0;
			if (this.myPolygon) {
				n++;
			};
			if (this.overlays) {
				n = n + this.overlays.length;
			};
		},
		// 用经纬度设置地图中心点
		theLocation:function(){
			this.map.panTo(this.point);
		}
	};

	//一个站点的电子围栏，pointstr(lng_lat,lng_lat,***;lng_lat,lng_lat,***)
	function EFenceObj(name, pointStrs, siteLng, siteLat){
		this.name = name;
		this.pointStrs = pointStrs;
		this.siteLng = siteLng;
		this.siteLat = siteLat;
		this.pointArray = [];
		this.loadData = function(){
			if(this.pointStrs == null || this.pointStrs == ""){
				return;
			}
			var pointArray = this.pointStrs.split(";");
			for (var i = 0; i < pointArray.length; i++) {
				var arr = pointArray[i].split(",");
				var barr = [];
				for (var j = 0; j < arr.length; j++) {
					var tmp = arr[j].split("_");
					barr.push(new BMap.Point(tmp[0], tmp[1]));
				}
				this.pointArray.push(barr);
			}
		}
		this.show = function(isEdit){
			if(this.pointArray == null || this.pointArray == ""){
				return;
			}
			var name = this.name;
			var lng = this.siteLng;
			var lat = this.siteLat;
			this.pointArray.forEach(function(e){
				var myPolygon = new BMap.Polygon(e);
				//this.myPolygon = myPolygon;
				if(isEdit){//单个站点可编辑
					try{
						myPolygon.enableEditing();
						myPolygon.enableMassClear();
					}catch(e){}
					myPolygon.addEventListener("lineupdate",function(e){
						fenceObj.showLatLon(e.currentTarget.ro);
					});
					myPolygon.addEventListener("rightclick",function(e){
						if(confirm("确认删除该电子围栏？")){
							fenceObj.delPolygon(e);
						}
					});
				}
				fenceObj.overlays.push(myPolygon);//可删除
				fenceObj.map.addOverlay(myPolygon);
				//在多边形中心点显示站点名称
				var bounds = myPolygon.getBounds();
				var poi = bounds.getCenter();
				var efencelabel = newEFenceLabel(poi, name);
				fenceObj.map.addOverlay(efencelabel);

				//站点不在多边形中，在多边形中心点显示站点名称
				/*var iscontain = bounds.containsPoint(new BMap.Point(lng, lat));//不起作用
				 if(!iscontain){
				 var poi = bounds.getCenter();
				 var efencelabel = newEFenceLabel(poi, name);
				 fenceObj.map.addOverlay(efencelabel);
				 }*/
			});
		}
		this.loadDataAndShow = function(isEdit){
			this.loadData();
			this.show(isEdit);
		}

	}

	//配送范围-- 绘制电子地图-- 隐藏绘制-保存按钮 -- 默认全部，需要隐藏
	/*$(".draw-btn").hide();
	 $(".bg-alpha").hide();*/

	//绘制电子围栏 -- 更改站点
	$("#fenceSiteId").change(function(){
		var siteId = $("#fenceSiteId").val();
		eFenceMapChangeSite(siteId);
	});
	function eFenceMapChangeSite(siteId){
		$.ajax({
			type : "GET",  //提交方式
			url : "${ctx}/deliverArea/getFence",//路径
			data : {
				"siteId" : siteId
			},//数据，这里使用的是Json格式进行传输
			success : function(dataObject) {//返回数据
				//清除所有覆盖物
				fenceObj.clearAll();
				if (siteId == ""){//全部
					var site = dataObject.centerSite;
					var siteList = dataObject.siteList;
					var center = getPointBySite(site);
					var zoom = getMapZoom(site.deliveryArea);
					//设置地图中心点和放大级别
					fenceObj.map.centerAndZoom(center, zoom);
					//显示站点和围栏
					if (siteList != null){
						//fenceArray = [];
						siteList.forEach(function(site){
							//加载
							fenceObj.loadOneSite(site.name, site.lng, site.lat);
							//加载电子围栏
							var efenceObj = new EFenceObj(site.name, site.eFence, site.lng, site.lat);
							efenceObj.loadDataAndShow(false);
							/*addOneEFenceData(site.eFence);*/
						});
					}
					/*fenceObj.myOverlay = fenceArray;
					 fenceObj.loadMyOverlay();*/
				} else {
					//显示绘制-保存按钮
					/*$("#eFenceTool").show();
					 $("#eFenceTool_bg").show();*/
					var site = dataObject.site;
					var center = getPointBySite(site);
					var zoom = getMapZoom(site.deliveryArea);
					//设置地图中心点和放大级别
					fenceObj.map.centerAndZoom(center, zoom);
					//显示站点和围栏
					fenceObj.loadOneSite(site.name, site.lng, site.lat);

					//加载电子围栏f
					var efenceObj = new EFenceObj(site.name, site.eFence, site.lng, site.lat);
					efenceObj.loadDataAndShow(true);

				}
			},
			error : function() {
				outDiv("服务器繁忙，请稍后再试");
			}
		});
	}
	//绘制电子围栏 -- 提交 保存
	$("#formBtn").click(function () {
		var siteId = $("#fenceSiteId").val();
		if(siteId == null || siteId == ""){
			outDiv("请先选择站点");
			return;
		}
		var jsonStr = "";
		fenceObj.overlays.forEach(function (e) {
			var arrs = e.ro;
			if(arrs.length>2){
				for (var i = 0; i < arrs.length; i++) {
					jsonStr = jsonStr + arrs[i].lng + "_" + arrs[i].lat;
					if (i < arrs.length - 1) {
						jsonStr = jsonStr + ",";
					}
				}
				jsonStr.substring(0, jsonStr.length - 1);
				jsonStr = jsonStr + ";";
			}
		})
		if ("" != jsonStr) {
			var url = "<c:url value='/site/putAllOverLay?${_csrf.parameterName}=${_csrf.token}'/>";
			$.ajax({
				url: url,
				type: 'POST',
				cache: false,
				data: {
					"siteId" : siteId,
					"jsonStr" : jsonStr
				},
				success: function(data){
					if(data == "success"){
						outDiv("提交成功");
						//重新加载地图
						//eFenceMapChangeSite(siteId);
					}else{
						outDiv("error:"+data);
					}
				},
				error: function(){
					outDiv('服务器繁忙，请稍后再试');
				}
			});

		} else {
			outDiv("请先绘制电子围栏");
		}
	});


	//显示结果面板动作
	var isPanelShow = false;
	$("showPanelBtn").onclick = showPanel;
	function showPanel(){
		if (isPanelShow == false) {
			isPanelShow = true;
			$("showPanelBtn").style.right = "230px";
			$("panelWrap").style.width = "230px";
			$("map").style.marginRight = "230px";
			$("showPanelBtn").innerHTML = "编辑多边形<br/>>";
		} else {
			isPanelShow = false;
			$("showPanelBtn").style.right = "0px";
			$("panelWrap").style.width = "0px";
			$("map").style.marginRight = "0px";
			$("showPanelBtn").innerHTML = "编辑多边形<br/>>";
		}
	}
	//绘制按钮事件
	function openDraw(){
		var siteId = $("#fenceSiteId").val();
		if(siteId == null || siteId == ""){
			outDiv("请先选择站点");
			return;
		}
		fenceObj.drawingManager.open();
		fenceObj.drawingManager.setDrawingMode(BMAP_DRAWING_POLYGON);
	}

	$('.b-tab li:eq(${activeNum-1}) a').tab('show');
	fenceObj.myOverlay = fenceArray;
	fenceObj.init();
	$('.b-tab a').click(function (e) {
		e.preventDefault();
		$(this).tab('show');
		$(this).parents("li").addClass("tab-cur").siblings().removeClass("tab-cur");
		var index=$(this).parent().index();

		if(index == 1){
			window.setTimeout(function(){
				//fenceObj.map.panTo(new BMap.Point(${centerPoint.lng},${centerPoint.lat}));
				fenceObj.map.reset();
			}, 300);

		}
	});
	window.setTimeout(function(){
		areaMap.reset();
	}, 300);

	var winhei2=window.screen.availHeight;
	var inithei=$("#fenceMap").height();


	/************************ 绘制电子围栏 ************* end **************************/

	/************************ 导入地址关键词 ************* start **************************/

		// 导入文件
	$(".import-file").on("change",function(){
		var siteId = $("#keywordSiteId").val();
		if(siteId == ""){//全部
			outDiv("请先选择站点");
			return;
		}
		$(".j-import-pop").modal();


	});
	$("#importBtn").click(function(){
		$(this).parents(".j-import-pop").modal('hide');
		$(".spinner").modal('show');
		$("#imptSiteId").val($("#keywordSiteId").val());
		$("#importFileForm").ajaxSubmit({
			type: 'post',
			url: "${ctx}/siteKeyWord/ajaxImportKeyword?${_csrf.parameterName}=${_csrf.token}",
			//data : $( '#importFileForm').serialize(),
			enctype: 'multipart/form-data',
			timeout: 0,
			success: function(map){
				loadTableHtml(map.pageList);
				$(".spinner").modal('hide');
				//清空file
				$(".import-file").val("");
			},
			error: function(JsonHttpRequest, textStatus, errorThrown){
				outDiv( "超时，服务器异常!");
				$(".spinner").modal('hide');
				//清空file
				$(".import-file").val("");
			}
		});
	});
	$("#cancelImpt").click(function(){
		//清空file
		$(".import-file").val("");
	});


	$("input[type='checkbox']").iCheck({
		checkboxClass : 'icheckbox_square-blue'
	});
	$("#selectAll").on('ifUnchecked', function() {
		$("input[type='checkbox']", "#dis-table").iCheck("uncheck");
	}).on('ifChecked', function() {
		$("input[type='checkbox']", "#dis-table").iCheck("check");
	});

	//$(".spinner").modal('show');

	//--------------------panel 3------------------------------------
	$("#between").daterangepicker({
		locale: {
			applyLabel: '确定',
			cancelLabel: '取消',
			fromLabel: '开始',
			toLabel: '结束',
			weekLabel: 'W',
			customRangeLabel: 'Custom Range',
			showDropdowns: true
		},
		format: 'YYYY/MM/DD'
	});
	$("#querySiteBtn").click(function(){
		/*$("#siteKeywordForm").submit();*/
		gotoPage(0);
	})
	//批量删除
	$("#piliangDel").click(function(){
		var id_array=new Array();
		$('input[name="inputC"]:checked').each(function(){
			id_array.push($(this).val());//向数组中添加元素
		});
		var delIds = id_array.join(',');
		if(delIds==""){
			outDiv("请选择要删除的站点关键词");
			return false;
		}
		if(confirm("确认批量删除所选站点关键词？")){
			var url = "${ctx}/siteKeyWord/batchDeleteKeyword/"+delIds;
			$(".spinner").modal('show');
			doDelete(url);
			$("#selectAll").attr("checked",false);
		}
	})

	//显示分页条
	var pageStr = paginNav(${pageIndex}, ${pageNum}, ${pageCount});
	$("#pagin").html(pageStr);

	//加载带有查询条件的指定页的数据
	function gotoPage(pageIndex) {
		//查询所有派件员
		$.ajax({
			type : "GET",  //提交方式
			url : "${ctx}/deliverArea/queryKeyWord",//路径
			data : {
				"pageIndex" : pageIndex,
				"siteId" : $("#keywordSiteId").val(),
				"between" : $("#between").val(),
				"keyword" : $("#keyword").val()
			},//数据，这里使用的是Json格式进行传输
			success : function(pageTable) {//刷新列表
				loadTableHtml(pageTable);
			},
			error : function() {
				//outDiv("加载分页数据异常");
				if(window.top==window.self){//不存在父页面
					window.location.href="<c:url value="/login" />"
				}else{
					window.top.location.href="<c:url value="/login" />"
				}
			}
		});
		//$("#siteKeywordForm").submit();
	}
	//刷新列表
	function loadTableHtml(pageTable){
		var tbody = $("#dataList");
		var dataList = pageTable.list;
		if(dataList != null){
			var datastr = "";
			for(var i = 0; i < dataList.length; i++){
				datastr += getRowHtml(dataList[i]);
			}
			tbody.html(datastr);
			//更新分页条
			var pageStr = paginNav(pageTable.page, pageTable.pageNum, pageTable.count);
			$("#pagin").html(pageStr);
			$("input[type='checkbox']").iCheck({
				checkboxClass : 'icheckbox_square-blue'
			});
			$("#selectAll").on('ifUnchecked', function() {
				$("input[type='checkbox']", "#dis-table").iCheck("uncheck");
			}).on('ifChecked', function() {
				$("input[type='checkbox']", "#dis-table").iCheck("check");
			});
		}

	}
	//封装一行的数据
	function getRowHtml(data){
		var row = "<tr>";
		row +=  "<td><input type='checkbox' name='inputC' value='" + data.id + "'  class='ckbox'/></td>";
		row += "<td>" + data.siteId + "</td>";
		row += "<td>" + getDate1(data.createAt) + "</td>";
		row += "<td>" + data.province + "</td>";
		row += "<td>" + data.city + "</td>";
		row += "<td>" + data.distict + "</td>";
		var keyword = $("#keyword").val();
		if(keyword == null || keyword == ""){//没有按照关键词查，不需要着色
			row += "<td>" + data.keyword + "</td>";
		}else{
			row += "<td>" + data.keyword.replace(keyword, "<span class='font-bg-color'>" + keyword + "</span>") + "</td>";
		}
		row += "<td><a href='javascript:void(0)' onclick='deleteKeyword(\"" + data.id + "\")' class='orange'>删除</a></td>";
		row += "</tr>";
		return row;
	}
	function deleteKeyword(id){
		if(id == null || id == ""){
			outDiv("关键词无编号，无法删除")
			return ;
		}
		if(confirm('确认删除？')){
			var url = "${ctx}/siteKeyWord/deleteKeyword/"+id;
			$(".spinner").modal('show');
			doDelete(url);
		}
	}
	function doDelete(url){
		var pageIndex = (parseInt($(".pagination .active a").html()) - 1);
		//查询所有派件员
		$.ajax({
			type : "GET",  //提交方式
			url : url,//路径
			data : {
				"pageIndex" : pageIndex,
				"siteId" : $("#keywordSiteId").val(),
				"between" : $("#between").val(),
				"keyword" : $("#keyword").val()
			},//数据，这里使用的是Json格式进行传输
			success : function(dataObject) {//返回数据根据结果进行相应的处理
				var result = dataObject.result;
				if(result){//删除成功，刷新列表
					loadTableHtml(dataObject.pageList);
				}else{
					outDiv("删除失败");
				}
				$(".spinner").modal('hide');
			},
			error : function() {
				$(".spinner").modal('hide');
				outDiv("服务器繁忙，请稍候再试");
			}
		});
	}
	//--------------------panel 2------------------------------------
	// 地图全屏显示
	var winwid=window.screen.availWidth;
	var initwid=$(".b-map").width();
	$(".j-full-btn").on("click",function(){

		var parentD=$('#psrE',window.parent.document);
		if($(this).hasClass("b-forward-full")){
			$(".j-detail-area").hide();
			parentD.find(".i-hei").css({zIndex:5,top:0,height:winhei2+2});
			$(".pos-footer").hide();
			$("#fenceMap,.b-map").css({width:winwid,height:winhei2-60-84,marginLeft:"-10px"});
			$(".j-full-div").css({left:"-16%"});
			$(".b-f-screen,.pos-adr").css({right:"25px"});
			$(".draw-btn").css({marginLeft:"-10px"})
			$("#draw-map").addClass("full-map");
			$(this).addClass("b-back-full").removeClass("b-forward-full");
		}else{
			$(".j-detail-area").show();
			$(".pos-footer").show();
			parentD.find(".i-hei").css({zIndex:3,top:"60px",height:winhei2+140});
			$("#fenceMap,.b-map").css({width:initwid,height:inithei,margin:0});
			$(".j-full-div").css({left:"0"});
			$(".b-f-screen,.pos-adr").css({right:"15px"});
			$(".draw-btn").css({marginLeft:"0"})
			$("#draw-map").removeClass("full-map");
			$(this).removeClass("b-back-full").addClass("b-forward-full");
		}
	})

	//导出关键词
	function exportKeyword() {
		$("#siteId_expt").val($("#keywordSiteId").val());
		$("#between_expt").val($("#between").val());
		$("#keyword_expt").val($("#keyword").val());
		$("#exptForm").submit();
	}



	/************************ 导入地址关键词 ************* end **************************/

</script>
</body>
</html>