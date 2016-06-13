<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.vo.Express" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<style>
    	.order-info {
    		/* min-width: 800px; */
	     	width:990px ;
			margin:20px auto 35px ;
			height:600px ;
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
			/* min-width: 260px; */
			width:350px ;
			border-right:1px solid #d7d6eb ;
			height:600px ;
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
			height:465px ;
			overflow:auto ;
		}
		.order-show-padding {
			padding:2px 10px ;
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
			width:620px ;
			height:580px ;
			overflow:hidden ;
		}
		</style>               
</head>

<body class="fbg">
<!--S 显示物流地图-->
<div id="mailMap" class="j-site-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog b-modal-dialog" role="document" style="width:1030px;">
		<div class="modal-content clearfix">
			<div class="modal-header b-modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title tc">转其他站点</h4>
			</div>
			<div class="modal-body b-modal-body order-info">
				<!-- map start -->
				<div class="order-number fl" style=" display:inline">
				     <h2>运单号：<span id="map_mailNum"></span></h2>
				     <div class="order-show">
				         <div class="order-show-padding" id="mailsDiv">
				         </div>
				     </div>
				</div>
				<!-- <div  class="fl" style=" display:inline">
				         
				       
				</div> -->
				<div id="allmap" class="fr order-map" style="margin:-30px"></div> 
				
				<!-- map end -->
			</div> <!-- modal body end -->
		</div>
	</div>
</div>
<!--E 显示物流地图-->

    
	
</body>       
<!-- 

<script src="<c:url value="/resources/javascripts/timeUtil.js"/>" >   </script>

 -->

<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=5LVr5CieSP2a11pR4sHAtWGU"></script>
<script type="text/javascript">
	var dataDetail = new Array();
    // 百度地图API功能
    var map = new BMap.Map("allmap");
    var bounds = null;
    var linesPoints = null;  
        
	//load map data
	function LoadMailAndMapData(mailNum, expressListJson) {
		expressListJson = expressListJson.replace(/`/g, "\'");
		//var expressList = eval('(' + expressListJson + ')');
		var expressList = eval(expressListJson);
        $("#map_mailNum").html(mailNum);//运单号
        //	物流信息
        mailDatas = "";
        var lon = "";
        var lat = "";
        if(expressList != null){
        	for(var index in expressList){
        		var express = expressList[index];
        		mailDatas += "<p class='order-time'><i></i>";
        		mailDatas += getDate1(express.dateAdd);
        		mailDatas += "<p class='order-cont";
        		if(express.remark != null && express.remark.indexOf("已签收") > -1){
        			mailDatas += " order-success";
        		}
        		mailDatas += "'>" + express.remark + "  <br/> </p>";
        		lon = express.lon;
         		lat = express.lat;
		        if(lat != null && lat != "0.0" && lat != "" && lon != null && lon != "0.0" && lon != ""){
		            dataDetail.push(new Array(lon,lat));
		        }
        	}
        } 
        showmap();
        $("#mailsDiv").html(mailDatas);//运单物流信息
        $("#mailMap").modal("show");
        
        
    }
	showmap();
     

     function initMap() {
        map.enableScrollWheelZoom();//启用滚轮放大缩小
        map.addControl(new BMap.NavigationControl()); // 添加平移缩放控件
        map.addControl(new BMap.ScaleControl()); // 添加比例尺控件
        map.addControl(new BMap.OverviewMapControl()); //添加缩略地图控件
     }
     function addClickHandler(content,marker){
        marker.addEventListener("click",function(e){
            openInfo(content,e)}
        );
     }
     function openInfo(content,e){
        var p = e.target;
        var point = new BMap.Point(p.getPosition().lng, p.getPosition().lat);
        var infoWindow = new BMap.InfoWindow(content,opts);  // 创建信息窗口对象
        map.openInfoWindow(infoWindow,point); //开启信息窗口
     }

	 function loopData(dataDetail){
	     var detailTemp = dataDetail[0];
	     var detailTemp1 = dataDetail[1];
	     if(detailTemp[0]!=detailTemp1[0] ||  detailTemp[1]!=detailTemp1[1]){
	         var spoi = new BMap.Point(detailTemp[0],detailTemp[1]);    // 起点1
	         var epoi  = new BMap.Point(detailTemp1[0],detailTemp1[1]);    // 终点
	         bounds = new Array();
	         linesPoints = new Array();
	         var driving3 = new BMap.DrivingRoute(map,{onSearchComplete:drawLine});  // 驾车实例,并设置回调
	         driving3.search(spoi, epoi);                                       // 搜索一条线路
	     }
	     dataDetail.splice(0,1);
	     if(dataDetail.length>1){
	         loopData(dataDetail)
	     }
	 }
	 function drawLine(results){
	     var opacity = 0.45;
	     var planObj = results.getPlan(0);
	     var b = new Array();
	     var addMarkerFun = function(point,imgType,index,title){
	         var url;
	         var width;
	         var height
	         var myIcon;
	         // imgType:1的场合，为起点和终点的图；2的场合为车的图形
	         if(imgType == 1){
	             url = "http://developer.baidu.com/map/jsdemo/img/dest_markers.png";
	             width = 42;
	             height = 34;
	             myIcon = new BMap.Icon(url,new BMap.Size(width, height),{offset: new BMap.Size(14, 32),imageOffset: new BMap.Size(0, 0 - index * height)});
	         }else{
	             url = "http://developer.baidu.com/map/jsdemo/img/trans_icons.png";
	             width = 22;
	             height = 25;
	             var d = 25;
	             var cha = 0;
	             var jia = 0
	             if(index == 2){
	                 d = 21;
	                 cha = 5;
	                 jia = 1;
	             }
	             myIcon = new BMap.Icon(url,new BMap.Size(width, d),{offset: new BMap.Size(10, (11 + jia)),imageOffset: new BMap.Size(0, 0 - index * height - cha)});
	         }
	
	         var marker = new BMap.Marker(point, {icon: myIcon});
	         if(title != null && title != ""){
	             marker.setTitle(title);
	         }
	         // 起点和终点放在最上面
	         if(imgType == 1){
	             marker.setTop(true);
	         }
	         map.addOverlay(marker);
	     }
	     var addPoints = function(points){
	         for(var i = 0; i < points.length; i++){
	             bounds.push(points[i]);
	             b.push(points[i]);
	         }
	     }
	     // 绘制驾车步行线路
	     if(planObj != null && planObj.getNumRoutes() != null){
		     for (var i = 0; i < planObj.getNumRoutes(); i ++){
		         var route = planObj.getRoute(i);
		         if (route.getDistance(false) <= 0){continue;}
		         addPoints(route.getPath());
		         // 驾车线路
		         if(route.getRouteType() == BMAP_ROUTE_TYPE_DRIVING){
		             map.addOverlay(new BMap.Polyline(route.getPath(), {strokeColor: "#0030ff",strokeOpacity:opacity,strokeWeight:6,enableMassClear:true}));
		         }else{
		             // 步行线路有可能为0
		             map.addOverlay(new BMap.Polyline(route.getPath(), {strokeColor: "#30a208",strokeOpacity:0.75,strokeWeight:4,enableMassClear:true}));
		         }
		     }
	     }
	     
	     
	     map.setViewport(bounds);
	     // 终点
	     addMarkerFun(results.getEnd().point,1,1);
	     // 开始点
	     addMarkerFun(results.getStart().point,1,0);
	     linesPoints[linesPoints.length] = b;
	 }
	 function initLine(){
	     bounds = new Array();
	     linesPoints = new Array();
	     map.clearOverlays();                                                    // 清空覆盖物
	     var driving3 = new BMap.DrivingRoute(map,{onSearchComplete:drawLine});  // 驾车实例,并设置回调
	     driving3.search(spoi1, epoi);                                       // 搜索一条线路
	     var driving4 = new BMap.DrivingRoute(map,{onSearchComplete:drawLine});  // 驾车实例,并设置回调
	     driving4.search(spoi2, epoi);                                       // 搜索一条线路
	 }
     function showmap(){
         
         initMap();
         if(dataDetail.length>0){
             map.centerAndZoom(new BMap.Point(dataDetail[0][0],dataDetail[0][1]), 18);// 初始化地图,设置中心点坐标和地图级别。
             loopData(dataDetail);
             for(var i=0;i<dataDetail.length;i++){
                 var marker = new BMap.Marker(new BMap.Point(dataDetail[i][0],dataDetail[i][1]));  // 创建标注
                 var content = dataDetail[i][2];
                 map.addOverlay(marker);               // 将标注添加到地图中
             //      addClickHandler(content,marker);
             }
             var myIcon = new BMap.Icon("http://developer.baidu.com/map/jsdemo/img/Mario.png", new BMap.Size(32, 70), {imageOffset: new BMap.Size(0, 0)});

             function run(){
                 var timeWait = 0;
                 runReset(0,linesPoints.length);
                 function runReset(m,pointLength){
                     if(m<pointLength){
                         var pts = linesPoints[m];
                         var len = pts.length;
                         setTimeout(function(){
                             var carMk = new BMap.Marker(pts[0],{icon:myIcon});
                             map.addOverlay(carMk);
                             resetMkPoint(1,len,pts,carMk,m);
                             m++;
                             timeWait = (timeWait+ len);
                             runReset(m,pointLength);
                         },100*timeWait);
                     }
                 }

                 function resetMkPoint(i,len,pts,carMk,j){
                     carMk.setPosition(pts[i]);
                     if(i < len){
                         setTimeout(function(){
                             i++;
                             resetMkPoint(i,len,pts,carMk,j);
                         },100);
                     }
                 }
             }
         }else{
             map.centerAndZoom(new BMap.Point(116.404, 39.915), 11);  // 初始化地图,设置中心点坐标和地图级别
             map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
             map.setCurrentCity("北京");          // 设置地图显示的城市 此项是必须设置的
             map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
         }

     }
</script>

</html>