<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<jsp:include page="../main.jsp" flush="true" />
</head>
<body>
<div class="content">
	<section class="content-header" >
		<div class="row">
			<div class="col-xs-12" >
				<label for="siteNameLabel" class="col-md-2 control-label">站点名称：</label>
				<div class="col-md-10">
					<span id="siteNameLabel"  style="float:left">立水桥  </span>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-xs-12" >
				<label for="siteAddressLabel" class="col-md-2 control-label">站点地址：</label>
				<div class="col-md-10">
					<span id="siteAddressLabel" style="float:left">北京市-朝阳区-立水桥-xxx</span>
				</div>
			</div>
		</div>

	</section>
	<section class="content">
		<ul class="nav nav-tabs">
			<li <c:if test="${activeNum eq '1'}">class="active"</c:if>>
				<a href="#panel-1" data-toggle="tab">配送范围</a>
			</li>
			<li <c:if test="${activeNum eq '2'}">class="active"</c:if>>
				<a href="#panel-2" data-toggle="tab">绘制电子围栏</a>
			</li>
			<li <c:if test="${activeNum eq '3'}">class="active"</c:if>>
				<a href="#panel-3" data-toggle="tab">导入地址关键词</a>
			</li>
		</ul>
		<div class="tab-content" style="height:800px;">
			<div class="tab-pane <c:if test="${activeNum eq '1'}">active</c:if>" id="panel-1">
				<div class="box box-primary">
					<div class="row">
						<p2>设置配送范围后，将优先匹配站点附近的订单</p2>
					</div>
					<div class="row">
						<div class="col-xs-12" >
							<label for="siteRangeLabel" class="control-label">站点周围：</label>
							<label >
								<c:set var="count" value="20"/>
								<select id="siteRangeLabel"  style="float:left">
									<option value="">请选择</option>
									<c:forEach var = "temp" begin="1" step="1" end="${count}">
										<option value ="${temp}" <c:if test="${temp eq 2}">selected</c:if>>${temp}</option>
									</c:forEach>
								</select>公里
							</label>
						</div>
					</div>
					<div id="allmap" style="width: 400px;height: 400px;margin-left:15px;margin-top:10px; display:none;"></div>
				</div>
			</div>
			<div class="tab-pane <c:if test="${activeNum eq '2'}">active</c:if>" id="panel-2">
				<div class="box box-primary">
					<div class="m20" style="height:500px;width:600px; background-color:#dda;">
						电子围栏
					</div>
				</div>
			</div>
			<div class="tab-pane <c:if test="${activeNum eq '3'}">active</c:if>" id="panel-3">
				<div class="box box-primary">
					地址关键词
				</div>
			</div>
		</div>
	</section>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=5LVr5CieSP2a11pR4sHAtWGU"></script>
	<script type="text/javascript">
		showMap();
		function showMap(){
			$("#allmap").css("display","block");
			// 百度地图API功能
			var map = new BMap.Map("allmap");
			var point = new BMap.Point(116.331398,39.897445);
			map.centerAndZoom(point,12);
			// 创建地址解析器实例
			var myGeo = new BMap.Geocoder();
			// 将地址解析结果显示在地图上,并调整地图视野
			var prov="北京";
			var addressStr = $("#address").val();
			if(id==0){
			var prov = $(".prov").val();
			var city = $(".city").val();
			var dist = $(".dist").val();
			var str = prov+city+addressStr;
			if(dist!=null){
			str = prov+city+dist+addressStr;
			}
			}else{
			str = addressStr;
			}
			console.log(str);
			myGeo.getPoint(str, function(point){
			if (point) {
			map.centerAndZoom(point, 16);
			map.addOverlay(new BMap.Marker(point));
			$("#lng").val(point.lng);
			$("#lat").val(point.lat);
			}else{
			alertify.error("您选择地址没有解析到结果!");
			}
			}, prov);
			function showInfo(e){
			map.clearOverlays();
			console.log(e.point.lng + ", " + e.point.lat);
			$("#lng").val(e.point.lng);
			$("#lat").val(e.point.lat);
			var point = new BMap.Point(e.point.lng, e.point.lat);
			var marker = new BMap.Marker(point);  // 创建标注
			map.addOverlay(marker);               // 将标注添加到地图中
			marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
			}
			map.addEventListener("click", showInfo);
		}
	</script>
</div>
</body>
</html>