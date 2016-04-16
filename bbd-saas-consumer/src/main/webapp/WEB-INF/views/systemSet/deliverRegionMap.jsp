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
			<div class="col-xs-2" >
				<label style="float:left;"><p class="text-left">站点名称：</p></label>
				<div >
					<strong style="float:left;">${site.name}</strong>
				</div>
			</div>
			<div class="col-xs-10" >
				<label style="float:left;"><p class="text-left">站点地址：</p></label>
				<div>
					<strong style="float:left;">${site.province}-${site.city}-${site.area} ${site.address}</strong>
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
					<form action="/site/updateSiteWithRadius" method="POST" id="siteRadiusForm">
						<br/>
						<div class="row">
							<div class="col-xs-12" >
								<label class="control-label">设置配送范围后，将优先匹配站点附近的订单</label>
							</div>
						</div>
						<div class="row">
							<div class="col-xs-12" >
								<label for="radius" class="control-label">站点周围：</label>
								<label >
									<c:set var="count" value="20"/>
									<select id="radius" name="radius" style="float:left">
										<option value="">请选择</option>
										<c:forEach var = "temp" begin="1" step="1" end="${count}">
											<option value ="${temp}" <c:if test="${temp eq site.getDeliveryArea()}">selected</c:if>>${temp}</option>
										</c:forEach>
									</select>公里
									<input type="hidden" id="siteId" name="siteId" value="${site.getId()}"/>
								</label>
							</div>
						</div>
						<c:if test="${site.getDeliveryArea()!=null&&site.getDeliveryArea()!='0'}">
							<div class="row" >
								<div id="allmap" style="width: 800px;height: 400px;margin-left:15px;margin-top:10px; "></div>
							</div>
							<br/>
						</c:if>
						<div class="row">
							<button type="button" class="btn btn-warning" id="saveSiteBtn" style="margin-left: 10px;">保存</button>
						</div>
					</form>
				</div>
			</div>
			<div class="tab-pane <c:if test="${activeNum eq '3'}">active</c:if>" id="panel-3">
				<jsp:include page="deliverRegionKey.jsp"/>
			</div>
		</div>
	</section>
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=5LVr5CieSP2a11pR4sHAtWGU"></script>
	<script type="text/javascript">
		//保存站点配送范围信息
		$("#saveSiteBtn").click(function(){
			$.ajax({
				url: '/site/updateSiteWithRadius/'+$("#radius option:selected").val()+'/'+$("#siteId").val(),
				type: 'get',
				cache: false,
				dataType: "text",
				data: {},
				success: function(response){
					console.log(response);
					alert("保存成功");
					window.location.href="/deliverRegion/map/1";
				},
				error: function(){
					alert('服务器繁忙，请稍后再试！');
				}
			});
		})

		showMap();
		//展示配送范围
		function showMap(){
			// 百度地图API功能
			var map = new BMap.Map("allmap");
			var point = new BMap.Point(${site.lng}, ${site.lat});
			map.centerAndZoom(point, 15);
			var marker = new BMap.Marker(point);  // 创建标注
			map.addOverlay(marker);               // 将标注添加到地图中
			marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
			var circle = new BMap.Circle(point,${site.deliveryArea},{strokeColor:"blue", strokeWeight:2, strokeOpacity:0.5}); //创建圆
			map.addOverlay(circle);            //增加圆
			map.enableScrollWheelZoom(true);
		}

		//-----------panel 3-----------------------
		$("#importSiteButton").click(function(){
			console.log("sss");
			$("#importSiteForm").submit();
		})
	</script>
</div>
</body>
</html>