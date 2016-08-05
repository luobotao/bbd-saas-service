<!DOCTYPE html>
<%@ page import="com.bbd.saas.enums.LocSource" %>
<%@ page import="java.util.Map" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../main.jsp"%>
<html>
<head>
	<title>地址匹配站点展示</title>
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
	<%
		Map<String, String> efenceMap = (Map<String, String>)request.getAttribute("efenceMap");
		Map<String, Object> data = (Map<String, Object>)request.getAttribute("data");
		String source = null;
		if(data != null){
			source = (String) data.get("source");
		}
	%>
</head>
<body class="fbg" >
<!-- S content -->
<div class="clearfix b-branch">
	<div class="container-fluid">
		<div class="row">
			<!-- S detail -->
			<div class="col-xs-12 col-sm-12 j-full-div">
				<!-- S 搜索区域 -->
				<!-- S 搜索区域 -->
				<form class="form-inline form-inline-n" action="/bbd/geoMatchSite" method="get">
					<div class="search-area">
						<div class="row pb20">
							<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-6" id="addr_control">
								<label>　省：</label>
								<select name="prov" class="prov form-control form-con-new">
								</select>
								<label id="cityLable">　市：</label>
								<select  class="city form-control form-con-new" disabled="disabled">
								</select>
								<label id="distLable">　区：</label>
								<select name="dist" class="dist form-control form-con-new"  disabled="disabled">
								</select>
							</div>
							<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-3">
								<label>时间：</label>
								<input id="dateBetween" name="dateBetween" value="${dateBetween}" type="text" placeholder="请选择时间" class="form-control c-disable"  />
							</div>
							<div class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-2">
								<span id="queryBtn" onclick="getData()" class="form-control ser-btn l w150" >查询</span>
							</div>
						</div>
					</div>
				</form>
				<!-- E 搜索区域 -->
				<!-- S 搜索结果 -->
				<div class="content capacity-map" id="map_div">
					<div id="addrMap" class="bod-rad j-s-hei " style="height: 600px;"></div>
				</div>
				<!-- E  搜索结果 -->



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

<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=5LVr5CieSP2a11pR4sHAtWGU"></script>
<!--加载鼠标绘制工具-->
<%--
<script type="text/javascript" src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
<link rel="stylesheet" href="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
--%>

<script type="application/javascript">
	var defaultPoint = new BMap.Point(116.404, 39.915);

	var center = "${prov}${city}${area}";
	var zoom = 16;
	// 百度地图API功能
	var addrMap = new BMap.Map("addrMap", {enableMapClick:false,minZoom:13});
	var goalMarker = null;//目标marker,用于拖动到目标位置的图标
	$(document).ready(function() {
		//console.log("center==="+center);
		addrMap.centerAndZoom(center);
		//初始化到站时间框
		$("#arriveBetween").daterangepicker({
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
		// 初始化省市区下拉框
		$("#addr_control").citySelect({
			prov: "${prov}",
			city: "${city}",
			dist: "${area}",
			nodata: "none"
		});
		// 省改变
		$('#addr_control .prov').change(function(){
			if(this.value == null || this.value == ""){
				$('#cityLable').hide();
			}else{
				$('#cityLable').show();
			}
			$('#distLable').hide();
			$("#addr_control .city").val("");//清空
			$("#addr_control .dist").val("");
		});
		// 市改变
		$('#addr_control .city').change(function(){
			if(this.value == null || this.value == ""){
				$('#distLable').hide();
			}else{
				$('#distLable').show();
			}
			$("#addr_control .dist").val("");//清空
		}) ;
		initAddrMap();

	});

	function initAddrMap(){
		addrMap.enableScrollWheelZoom(true);
		//显示订单目的地
		<c:if test="${orderPointList != null}">
			<c:forEach items="${orderPointList}" var="orderPoint">
				showPoint("${orderPoint.lng}", "${orderPoint.lat}", "${orderPoint.orderNo}","${orderPoint.address}");
			</c:forEach>
		</c:if>

	}

	//在地图上显示点和label
	function showPoint(lng, lat, orderNo, address){
		var point = new BMap.Point(lng, lat);
//		console.log("name==="+name+"   isSource==="+isSource);
		var marker = new BMap.Marker(point);// 创建标注
		addrMap.addOverlay(marker);             // 将标注添加到地图中
		//console.log(address);
		/*if(address.indexOf(",") > -1){
			address = address.substr(address.indexOf(",")+1);
		}
		// orderNo+","+address
		//console.log(address);
		var label = newLabel(point,address);
		addrMap.addOverlay(label);*/
		return marker;
	}

	/*****************************************************************************/
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
		this.show = function(){
			if(this.pointArray == null || this.pointArray == ""){
				return;
			}
			var name = this.name;
			this.pointArray.forEach(function(e){
				var myPolygon = new BMap.Polygon(e);
				addrMap.addOverlay(myPolygon);
				//在多边形中心点显示站点名称
				var bounds = myPolygon.getBounds();
				var poi = bounds.getCenter();
				var efencelabel = newLabel(poi, name);
				addrMap.addOverlay(efencelabel);
				/*var myIcon = new BMap.Icon("${ctx}/resources/images/b_marker.png", new BMap.Size(39,50));
				 var marker = new BMap.Marker(poi,{icon:myIcon});  // 创建标注
				 addrMap.addOverlay(marker);               // 将标注添加到地图中*/
			});

		}
		this.loadDataAndShow = function(isEdit){
			this.loadData();
			this.show(isEdit);
		}

	}
	function newLabel(point, name){
		var opts = null;
		opts = {
			position : point,    // 指定文本标注所在的地理位置
			offset   : new BMap.Size(-37, -55)    //设置文本偏移量
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
	/*****************************************************************************/
	/************************************ S 修正位置 *****************************************/
	function getData(){
		var url = "<c:url value='/geoMailManage/getDataList?${_csrf.parameterName}=${_csrf.token}'/>";
		$.ajax({
			url: url,
			type: 'POST',
			cache: false,
			data: {
				"prov" : $("#addr_control .prov").val(),
				"city" :  $("#addr_control .city").val(),
				"area" :  $("#addr_control .dist").val(),
				"dateBetween" : $("#dateBetween").val()
			},
			success: function(dataList){
				//清除所有覆盖物
				addrMap.clearOverlays();
				//地图中心点
				var addr = $("#addr_control .prov").val()+$("#addr_control .city").val()+$("#addr_control .dist").val();
				if(addr == "" || addr == null){
					addr = center;
				}
				addrMap.centerAndZoom(addr);
				//显示点
				if(dataList != null && dataList.length > 0){
					for(var i = 0; i < dataList.length; i++){
						showPoint(dataList[i].lng, dataList[i].lat, dataList[i].orderNo,dataList[i].address);
					}
				}
			},
			error: function(){
				outDiv('修正失败');
			}
		});
	}
	/*********************************** E 修正位置 ******************************************/

</script>
</body>
</html>