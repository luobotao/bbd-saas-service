<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.vo.Express" %>
<%@ page import="com.bbd.saas.enums.OrderStatus" %>
<%@ page import="com.bbd.saas.utils.Dates" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="<%=request.getContextPath()%>"/>
<html>
<head>
	<jsp:include page="../main.jsp" flush="true" />
	<style>
    	.order-info {
	     	width:1140px ;
			margin:40px auto 95px ;
			height:760px ;
			background:#FFF ;
			overflow:hidden ;
			border-radius:6px ;
			border:1px solid #d7d6eb ;
			position:relative ;
		}
		.order-number {
			position:absolute ;
			top:0 ;
			left:0 ;
			width:400px ;
			border-right:1px solid #d7d6eb ;
			height:720px ;
		}
		.order-number h2 {
			display:block ;
			font-size:24px ;
			line-height:99px ;
			height:99px ;
			color:#33333d ;
			background:#f9f9fd ;
			text-align:center ;
			border-bottom:1px solid #d7d6eb ;
			overflow: hidden ;
			word-break: keep-all ;
			white-space: nowrap ;
			text-overflow: ellipsis ;
		}
		.order-show {
			height:620px ;
			overflow:auto ;
		}
		.order-show-padding {
			padding:25px 36px ;
		}
		.order-time {
			font-size:14px ;
			line-height:20px ;
			padding-bottom:2px ;
			color:#b3b6c7 ;
		}
		.order-time i {
			display:inline-block ;
			width:12px ;
			height:12px ;
			border:1px solid #07adf0 ;
			background:#28caff ;
			vertical-align:middle ;
			border-radius:50% ;
			margin-right:6px ;
		}
		.order-cont {
			border-left:4px solid #bcc8ce ;
			margin-left:5px ;
			padding:0 0 40px 16px ;
			font-size:16px ;
			line-height:24px ;
			color:#33333d ;
		}
		.order-success {
			border-left:0 ;
			padding:0 0 30px 20px ;
		}
		.order-cont em {
			color:#38a7f8 ;
		}
		.order-map {
			width:740px ;
			height:760px ;
			overflow:hidden ;
			float:right;
		}
		#controller{width:100%; border-bottom:3px outset; height:30px; filter:alpha(Opacity=100); -moz-opacity:1; opacity:1; z-index:10000; background-color:lightblue;}
		</style>               
</head>
<%
	String proPath = request.getContextPath();
	String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+proPath;
%>
<!-- <body class="fbg"> -->
<body style="background-color:#f0f0f7 " onload="init();">
    <div class="container">
       <nav class="navbar navbar-default b-navbar">
			<div class="container-fluid">
				<!-- Brand and toggle get grouped for better mobile display -->
				<div class="navbar-header">
					<button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
						<span class="sr-only">Toggle navigation</span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
						<span class="icon-bar"></span>
					</button>
					<a class="navbar-brand" href="<c:url value="/" />"><img src="<c:url value="/resources/images/logo.png" />" alt="logo" /></a>
				</div>
		
				<!-- Collect the nav links, forms, and other content for toggling -->
				<div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
					<ul class="nav navbar-nav navbar-right f16">
						<li><a href="<c:url value="/home" />">首页</a></li>
						<li><a href="javascript:void(0);"><i class="p-icon p-user"></i>　<em class="orange">${user.realName}</em></a></li>
						<li><a href="<c:url value="/logout" />">退出登录</a></li>
					</ul>
				</div><!-- /.navbar-collapse -->
			</div><!-- /.container-fluid -->
		</nav>
	
		<!-- /.navbar-collapse -->
		
        <div class="order-info">
        <%
			Order order = (Order)request.getAttribute("order");
			if(order != null){
		%>
				<div class="order-number">
	                <h2>运单号：<%=order.getMailNum()%></h2>
	                <div class="order-show">
	                    <div class="order-show-padding" id="mailsDiv">
	                    <%
	                    if(order.getExpresses() != null){ 
	                    	for(Express express : order.getExpresses()){
	                    %>
	                    	<p class="order-time">
	                            <i></i>
	                            <%=Dates.formatDateTime_New(express.getDateAdd())%>
	                        </p>
	                        <p class="order-cont <% if(express.getRemark().indexOf("已签收") > -1){%>order-success<%} %>" >
	                        	<%=express.getRemark()%>  <br />      
	                        </p>
	                    <%
	                    	}
	                    } %>
	                    </div>
	                </div>
	            </div>
				<div class="log-status">
					<div id="container" class="order-map"></div>
					<div class="log-draw-btn">
						<div class="bg-alpha"></div>
						<input id="follow" type="checkbox"  class="ml12"><span class="follow-pic">画面跟随</span></input>
						<input id="play" type="button" class="b-play-icon b-play" onclick="play();" disabled />
						<input id="pause" type="button"  class="b-play-icon b-pause ml12" onclick="pause();" disabled />
						<input id="reset" type="button"  class="b-play-icon b-reset ml12" onclick="reset()" disabled />
					</div>
				</div>
		<%
			}
		%>
		</div>
	</div>
	
</body>       
<!-- S footer -->
<footer class="pos-footer tc">
    <em class="b-copy">京ICP备 465789765 号 版权所有 &copy; 2016-2020 棒棒达       北京棒棒达科技有限公司</em>
</footer>
<!-- E footer -->

<script src="<c:url value="/resources/javascripts/timeUtil.js"/>" >   </script>

<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=5LVr5CieSP2a11pR4sHAtWGU"></script>
<script type="text/javascript">
	$("input[type='checkbox']").iCheck({
		checkboxClass : 'icheckbox_square-blue'
	});
	String.prototype.startWith=function(s){
		if(s==null||s==""||this.length==0||s.length>this.length)
			return false;
		if(this.substr(0,s.length)==s)
			return true;
		else
			return false;
		return true;
	}
	//获取所有点的坐标
	var points = new Array();
	var myIconArray = new Array();
	var hasKdy = false;
	<%
		if(order != null && order.getExpresses() != null){
			for(Express express : order.getExpresses()){
		%>
			var lon = "<%=express.getLon()%>";
			var lat = "<%=express.getLat()%>";
			if(lat != "" && lat != "0.0" && lat.indexOf("E") == -1 && lat.indexOf("e") == -1
				&&lon != "" && lon != "0.0" && lon.indexOf("E") == -1 && lon.indexOf("e") == -1){
				var remark = "<%=express.getRemark()%>";
				var flag = false;
				if(remark.startWith("订单分拣中")){
					flag = true;
					myIconArray.push(new BMap.Icon("${ctx}/resources/images/admin/start.png", new BMap.Size(64,64)));
				}else if(remark.startWith("订单已送达")){
					flag = true;
					myIconArray.push(new BMap.Icon("${ctx}/resources/images/admin/b_pos.png", new BMap.Size(64,64)));
				}else if(remark.startWith("订单已由")){
					flag = true;
					myIconArray.push(new BMap.Icon("${ctx}/resources/images/admin/b_pos.png", new BMap.Size(64,64)));
				}else if(remark.startWith("您的订单已送达")){
					flag = true;
					hasKdy=true;
					myIconArray.push(new BMap.Icon("${ctx}/resources/images/admin/end.png", new BMap.Size(64,64)));
				}
				if(flag){
					points.push(new BMap.Point(lon, lat));
				}
			}
			<%
            }//for
        }//if order != null
    %>
	var pointsArray=new Array;
	var pointsTotal=[];
	var map; //百度地图对象
	var car; //汽车图标
	var courier; //快递员图标
	var label; //信息标签
	var centerPoint;
	var timer; //定时器
	var index = 0; //记录播放到第几个point
	var followChk, playBtn, pauseBtn, resetBtn; //几个控制按钮
	var cnt =0;
	var carlength=0;
	var courierlength=0;
	function init() {
		if(points==null||points.length==0){
			map = new BMap.Map("container");
			var center = new BMap.Point("${defaultLng}", "${defaultLat}");
			map.centerAndZoom(center, 15);
			map.enableScrollWheelZoom();
			map.addControl(new BMap.NavigationControl());
			map.addControl(new BMap.ScaleControl({anchor:BMAP_ANCHOR_BOTTOM_LEFT}));
			map.addControl(new BMap.OverviewMapControl({isOpen: true}));
			return false;
		}else{
			followChk = document.getElementById("follow");
			playBtn = document.getElementById("play");
			pauseBtn = document.getElementById("pause");
			resetBtn = document.getElementById("reset");
			//初始化地图,选取第一个点为起始点
			map = new BMap.Map("container");
			map.centerAndZoom(points[0],15);
			map.enableScrollWheelZoom();
			map.addControl(new BMap.NavigationControl());
			map.addControl(new BMap.ScaleControl({anchor:BMAP_ANCHOR_BOTTOM_LEFT}));
			map.addControl(new BMap.OverviewMapControl({isOpen: true}));
			//map.pointToOverlayPixel(Point);

			//通过DrivingRoute获取一条路线的point
			var driving = new BMap.DrivingRoute(map);

			for (var i = 0;i < points.length ; i++) {
				var myIcon = myIconArray[i];
				var marker2 = new BMap.Marker(points[i],{icon: myIcon});  // 创建标注
				//var lab = new  BMap.Label("途径点"+i,{position:points[i]});
				map.addOverlay(marker2);              // 将标注添
				//map.addOverlay(lab);
				if(i < points.length-1){
					driving.search(points[i],points[i+1]);
				}
			}
			driving.setSearchCompleteCallback(function() {
				var tmp = driving.getResults().getPlan(0).getRoute(0).getPath();
				var first = tmp[0];
				cnt++;
				for (var i = 0;i < points.length-1 ; i++) {
					if(Math.abs(first.lng - points[i].lng) < 0.01&& Math.abs(first.lat - points[i].lat)<0.01){
						pointsArray[i]  = tmp;
						if(i < points.length-2){
							carlength = carlength + pointsArray[i].length;
						}
						if(hasKdy){
							courierlength = courierlength + pointsArray[i].length+carlength;
						}else{
							carlength = carlength + pointsArray[i].length;
						}
					}
				}
				if(cnt ==points.length-1){
					for (var i = 0;i < pointsArray.length ; i++) {
						pointsTotal = pointsTotal.concat(pointsArray[i]);
					}
					var polyline = new BMap.Polyline(pointsTotal, {strokeColor: "#36cbff", strokeWeight: 6, strokeOpacity: 1});
					map.addOverlay(polyline);
					map.setViewport(pointsTotal);
				}
			});
			label = new BMap.Label("", {offset: new BMap.Size(-20, -20)});
			//显示小车子
			var myIconCar = new BMap.Icon("${ctx}/resources/images/admin/car1.png", new BMap.Size(128,128), { imageOffset: new BMap.Size(30,15),imageSize:new BMap.Size(64,64)});
			car = new BMap.Marker(points[0],{icon:myIconCar});
			map.addOverlay(car);
			if(hasKdy){
				//显示快递员
				var myIconCourier = new BMap.Icon("${ctx}/resources/images/admin/courier.png", new BMap.Size(128,128), { imageOffset: new BMap.Size(30,15),imageSize:new BMap.Size(64,64)});
				courier = new BMap.Marker(points[points.length-2],{icon:myIconCourier});
				map.addOverlay(courier);
			}
			//点亮操作按钮
			playBtn.disabled = false;
			resetBtn.disabled = false;
		}
	}
	function play() {
		playBtn.style.backgroundPosition = "-92px 0";
		playBtn.disabled = true;
		pauseBtn.style.backgroundPosition = "0 -42px";
		pauseBtn.disabled = false;
		var point = pointsTotal[index];
		if(index <  carlength) {
			car.setPosition(point);
		}else{
			if(hasKdy){
				if(index <  courierlength) {
					courier.setPosition(point);
				}
			}
		}
		if(index > 0) {
			map.addOverlay(new BMap.Polyline([pointsTotal[index - 1], point], {strokeColor: "red", strokeWeight: 1, strokeOpacity: 1}));
		}
		index++;
		if(followChk.checked) {
			map.panTo(point);
		}
		if(index < pointsTotal.length) {
			timer = window.setTimeout("play(" + index + ")", 50);
		} else {
			playBtn.disabled = true;
			pauseBtn.disabled = true;
			map.panTo(point);
		}

	}
	function pause() {
		playBtn.style.backgroundPosition = "0 0";
		playBtn.disabled = false;
		pauseBtn.style.backgroundPosition = "-92px -42px";
		pauseBtn.disabled = true;
		if(timer) {
			window.clearTimeout(timer);
		}
	}
	function reset() {
		followChk.checked = false;
		playBtn.style.backgroundPosition = "0 0";
		playBtn.disabled = false;
		pauseBtn.style.backgroundPosition = "-92px -42px";
		pauseBtn.disabled = true;
		if(timer) {
			window.clearTimeout(timer);
		}
		index = 0;
		car.setPosition(points[0]);
		map.panTo(centerPoint);
	}
</script>

</html>