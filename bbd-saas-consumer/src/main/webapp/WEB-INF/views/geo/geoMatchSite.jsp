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
							<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-3">
								<label>关键字：</label>
								<input type="text" id="keyword" name="keyword" placeholder="订单号/运单号" class="form-control" value="${keyword}" />
							</div>
							<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-5">
								<label>地址：</label>
								<input type="text" id="address" name="address" placeholder="查询地址" class="form-control wp80" value="${address}" />
							</div>
							<div class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-4">
								<input type="submit" id="queryBtn" class="form-control ser-btn l w150" value="查询" />
							</div>
						</div>
					</div>
				</form>
				<!-- E 搜索区域 -->
				<!-- S 搜索结果 -->
				<div class="content" id="map_div">
					<!-- S 文本信息 -->
					<div class="search-area content_left">
						<div class="row pb20">
							<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-12">
								<label>地址：</label>
								${data.province}-${data.city}-${data.district}-${data.street}-${data.formatedAddress}
							</div>
							<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-12">
								<label>来源：</label>
								<%=LocSource.fromString(source).getMsg()%>
								<%--<c:choose>
									<c:when test="${data.source == 'AMAP'}"><c:set var="source" scope="session" value="高德地图"/>高德地图</c:when>
									<c:when test="${data.source == 'BAIDU'}">百度地图</c:when>
									<c:when test="${data.source == 'TENCENT'}">腾讯地图</c:when>
									<c:when test="${data.source == 'BBD'}">棒棒达地图</c:when>
									<c:otherwise>高德地图</c:otherwise>
								</c:choose>--%>

							</div>
							<div class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<label>是否匹配到站点：</label>
								<c:choose>
									<c:when test="${efenceMap != null}">是</c:when>
									<c:otherwise>否</c:otherwise>
								</c:choose>
							</div>

							<div class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<label>修正为：</label><input type="button"  class="sbtn80 ml16" onclick="fixAddress()" value="确定" />
								<c:if test="${data.source != 'AMAP'}">
									<br><input class="ml16" type="radio" name="source" value="AMAP" onclick="chooseAddress('AMAP','${data.AMAP.lng}','${data.AMAP.lat}')"/>
									&nbsp;高德&nbsp;&nbsp;
								</c:if>
								<c:if test="${data.source != 'BAIDU'}">
									<br><input class="ml16"  type="radio" name="source" value="BAIDU"  onclick="chooseAddress('BAIDU','${data.BAIDU.lng}','${data.BAIDU.lat}')"/>
									&nbsp;百度&nbsp;&nbsp;
								</c:if>
								<c:if test="${data.source != 'TENCENT'}">
									<br><input class="ml16"  type="radio" name="source" value="TENCENT"  onclick="chooseAddress('TENCENT','${data.TENECNT.lng}','${data.TENECNT.lat}')"/>
									&nbsp;腾讯&nbsp;&nbsp;
								</c:if>
								<br><input class="ml16"  type="radio" name="source" value="BBD"  onclick="chooseAddress('BBD','${data.loc.lng}','${data.loc.lat}')"/>手动修改(拖动图标到目标位置)
								<input type="hidden" id="fixSource" value="" />
								<input type="hidden" id="lng" value="" />
								<input type="hidden" id="lat" value="" />

							</div>

						</div>
					</div>
					<!-- E 文本信息 -->
					<!-- S 地图 -->
					<div class="box-body table-responsive capacity-map content_main">
						<div id="addrMap" class="bod-rad j-s-hei " style="height: 600px;"></div>
					</div>
					<!-- S 地图 -->
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
	var center = null;
	var zoom = 16;
	// 百度地图API功能
	var addrMap = new BMap.Map("addrMap", {enableMapClick:false,minZoom:13});
	var goalMarker = null;//目标marker,用于拖动到目标位置的图标
	function initAddrMap(){
		addrMap.enableScrollWheelZoom(true);
		//设置地图中心点和放大级别
		//显示目标位置
		<c:choose>
		<c:when test="${data.loc != null}">
		center = new BMap.Point("${data.loc.lng}", "${data.loc.lat}");
		addrMap.centerAndZoom(center, zoom);
		goalMarker = showPoint(center, "<%=LocSource.fromString(source).getMsg()%>返回结果", true);
		</c:when>
		<c:otherwise>
		addrMap.centerAndZoom(defaultPoint, zoom);
		</c:otherwise>
		</c:choose>
		//加载电子围栏
		<%
            if(efenceMap != null){
                for (Map.Entry<String ,String> entry : efenceMap.entrySet()) {
        %>
		var efenceObj = new EFenceObj("<%=entry.getKey()%>", "<%=entry.getValue()%>");
		efenceObj.loadDataAndShow(false);
		<%
                }
            }
        %>
		//显示高德地图
		<c:if test="${data.source != 'AMAP' && data.AMAP != null}">
		var point = new BMap.Point("${data.AMAP.lng}", "${data.AMAP.lat}");
		showPoint(point, "高德地图返回结果");
		</c:if>
		//显示百度地图
		<c:if test="${data.source != 'BAIDU' && data.BAIDU != null}">
		var point = new BMap.Point("${data.BAIDU.lng}", "${data.BAIDU.lat}");
		showPoint(point, "百度地图返回结果");
		</c:if>
		//显示腾讯地图
		<c:if test="${data.source != 'TENCENT' && data.TENECNT != null}">
		var point = new BMap.Point("${data.TENECNT.lng}", "${data.TENECNT.lat}");
		showPoint(point, "腾讯地图返回结果");
		</c:if>
	}
	initAddrMap();
	//在地图上显示点和label
	function showPoint(point, name, isSource){
//		console.log("name==="+name+"   isSource==="+isSource);
		var marker = null;
		if(isSource){
			<%--console.log("<%=request.getContextPath()%>");--%>
			<%--console.log("${ctx}/resources/images/admin/end.png");--%>
			var myIcon = new BMap.Icon("${ctx}/resources/images/admin/end.png", new BMap.Size(55,55),{anchor: new BMap.Size(27, 55)});
			marker = new BMap.Marker(point,{icon:myIcon});  // 创建标注
		}else{
			marker = new BMap.Marker(point);// 创建标注
		}
		//marker = new BMap.Marker(point);// 创建标注
		addrMap.addOverlay(marker);             // 将标注添加到地图中
		var label = newLabel(point, name, isSource);
		addrMap.addOverlay(label);
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
	function newLabel(point, name, isSource){
		var index = 25;
		var opts = null;
		if(isSource){//目标图标（大图）
			opts = {
				position : point,    // 指定文本标注所在的地理位置
				offset   : new BMap.Size(-30, -81)    //设置文本偏移量
			}
		}else{
			opts = {
				position : point,    // 指定文本标注所在的地理位置
				offset   : new BMap.Size(-37, -55)    //设置文本偏移量
			}
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
	function chooseAddress(source, lng, lat){
		$("#fixSource").val(source);//来源
		var point = new BMap.Point(lng, lat);
		addrMap.panTo(point);//地图中心移动到选中目标位置
		addrMap.setZoom(18);
		if(source == "BBD"){ //获取地图上的经纬度
			goalMarker.enableDragging();
			goalMarker.addEventListener("dragend", function(e){
				$("#lng").val(e.point.lng);
				$("#lat").val(e.point.lat);
				//console.log("当前位置：" + e.point.lng + ", " + e.point.lat);
			});
		}else{
			$("#lng").val(lng);
			$("#lat").val(lat);
		}

	}
	function fixAddress(){
		var url = "<c:url value='/bbd/fixAddress?${_csrf.parameterName}=${_csrf.token}'/>";
		$.ajax({
			url: url,
			type: 'POST',
			cache: false,
			data: {
				"address" : $("#address").val(),
				"source" : $("#fixSource").val(),
				"lng" : $("#lng").val(),
				"lat" : $("#lat").val()
			},
			success: function(data){
				outDiv(data.msg);
				if(data.code == 0){//修改成功
					goalMarker.disableDragging();
					location.reload();
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