<!DOCTYPE html>
<%@ page import="com.bbd.saas.utils.Dates" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../main.jsp"%>
<html>
<head>
	<title>快递员配送区域展示</title>
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
<body class="fbg" >
<!-- S content -->
<div class="clearfix b-branch">
	<div class="container-fluid">
		<div class="row">
			<!-- S detail -->
			<div class="j-full-div">
				<!-- S 搜索区域 -->
				<!-- S 搜索区域 -->
				<form class="form-inline form-inline-n" action="/bbd/postmanEfence" method="get">
					<div class="search-area">
						<div class="row pb20">
							<div class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<label>快递员手机号：</label>
								<input type="text" id="phone" name="phone" placeholder="请输入快递员手机号" class="form-control" value="${phone}" />
								<input type="submit" id="queryBtn" class="form-control ser-btn l"  value="查询" />
							</div>
						</div>
					</div>
				</form>
				<!-- E 搜索区域 -->

				<!-- S tab -->
				<div class="col-xs-12">
					<div class="box">
						<div class="box-header">
						</div>
						<div class="box-body table-responsive">
							<!-- S 绘制电子围栏 -->
							<div id="allmap" class="bod-rad j-s-hei" style="height: 533px;"></div>
							<input type="hidden" id="sitePoints" name="sitePoints" value="${sitePoints}"/>
							<!-- E 绘制电子围栏 -->
						</div>
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
<!-- E pop -->
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=5LVr5CieSP2a11pR4sHAtWGU"></script>
<!--加载鼠标绘制工具-->
<script type="text/javascript" src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
<link rel="stylesheet" href="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
<!--加载检索信息窗口-->
<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.js"></script>
<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.css" />
<script type="application/javascript">
	var jhei=$(window).height();
	$(".j-s-hei").css({height:jhei-82});

	var defaultPoint = new BMap.Point(116.404, 39.915);
	var bmapArr = [];
	var arrayPoint = $("#sitePoints").val();
	console.log(arrayPoint);
	var siteArr = arrayPoint.split(";");
	for (var i = 0; i < siteArr.length; i++) {
		var arr = siteArr[i].split(",");
		var barr = [];
		for (var j = 0; j < arr.length; j++) {
			var tmp = arr[j].split("_");
			barr.push(new BMap.Point(tmp[0], tmp[1]));
		}
		if(i==0){
			defaultPoint = barr[0];
		}
		bmapArr.push(barr);
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
			fontSize : "18px",
			fontFamily:"simhei",
			backgroundColor:"rgba(4, 4, 4,0.7)",
		});
		return label;
	}
	var bmap = {
		status: false,
		map: '',
		point: '',
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
		init: function(){
			if(this.status){
				return;
			}
			this.status = true;
			this.map = new BMap.Map('allmap',{enableMapClick:false,minZoom:11,noAnimation:true});
			this.point = defaultPoint;
			this.map.centerAndZoom(this.point,14);
			this.map.enableScrollWheelZoom();
			this.map.disableInertialDragging();

			var map = this.map;
			var styleOptions = this.styleOptions;
			//实例化鼠标绘制工具
			this.drawingManager = new BMapLib.DrawingManager(map, {
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
			this.drawingManager.addEventListener('overlaycomplete', bmap.overlaycomplete);
			/*加载一个已有的多边形*/
			if (this.myOverlay) {
				this.loadMyOverlay();
			};
			//添加label
			var label = newLabel(this.point, "${site.name}");
			this.map.addOverlay(label);
			// S 增加的控件
			var bottom_left_control = new BMap.ScaleControl({anchor: BMAP_ANCHOR_BOTTOM_LEFT});// 左上角，添加比例尺
			var bottom_right_navigation = new BMap.NavigationControl({
				anchor: BMAP_ANCHOR_BOTTOM_RIGHT, type: BMAP_NAVIGATION_CONTROL_ZOOM
			}); //右上角，仅包含平移和缩放按钮
			map.addControl(bottom_left_control);
			map.addControl(bottom_right_navigation);
			// E 增加的控件

		},
		loadMyOverlay: function(){
			var map = this.map;
			this.clearAll();
			this.myOverlay.forEach(function(e){
				myPolygon = new BMap.Polygon(e, this.styleOptions);
				this.myPolygon = myPolygon;
				try{myPolygon.enableEditing();
					myPolygon.enableMassClear();}catch(e){}
				myPolygon.addEventListener("lineupdate",function(e){
					bmap.showLatLon(e.currentTarget.ro);
				});
				myPolygon.addEventListener("rightclick",function(e){
					if(confirm("确认删除该电子围栏？")){
						bmap.delPolygon(e);
					}
				});
				bmap.overlays.push(myPolygon);
				map.addOverlay(myPolygon);
			})
		},
		showLatLon: function(a){
			var len = a.length;
			var s = '';
			var arr = [];
			for(var i =0 ; i < len-1; i++){
				arr.push([a[i].lng, a[i].lat]);
				s += '<li>'+ a[i].lng +','+ a[i].lat +'<span class="red" title="删除" onclick="bmap.delPoint('+i+')">X</span></li>';
			}
			this.overlaysCache = arr;
			$("panelWrap").innerHTML = '<ul>'+ s +'</ul>';
		},
		delPoint: function(i){
			if(this.overlaysCache.length <=3 ){
				ioutDiv('不能再删除, 请保留3个以上的点.');
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
			bmap.overlays.push(e.overlay);
			e.overlay.enableEditing();
			e.overlay.addEventListener("lineupdate",function(e){
				bmap.showLatLon(e.currentTarget.ro);
			});
			e.overlay.addEventListener("rightclick",function(e){
				if(confirm("确认删除该电子围栏？")){
					bmap.delPolygon(e);
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

	function openDraw(){
		bmap.drawingManager.open();
		bmap.drawingManager.setDrawingMode(BMAP_DRAWING_POLYGON);
	}

	$('.b-tab li:eq(${activeNum-1}) a').tab('show');
	bmap.myOverlay=bmapArr;
	bmap.init();
	$('.b-tab a').click(function (e) {
		e.preventDefault();
		$(this).tab('show');
		$(this).parents("li").addClass("tab-cur").siblings().removeClass("tab-cur");
		var index=$(this).parent().index();
		if(index == 1){
			window.setTimeout(function(){
				bmap.map.reset();
			}, 500);
		}
	});
	parentE=$('#psrE',window.parent.document);
	var winhei2=parentE.height();
	var winwid=window.screen.availWidth;
	var initwid=$(".b-map").width();
	var inithei=$("#allmap").height();

	$(".j-full-btn").on("click",function(){

		var parentD=$('#psrE',window.parent.document);
		if($(this).hasClass("b-forward-full")){
			parentD.find(".i-hei").attr("scrolling","no");
			parentD.find(".i-hei").css({zIndex:5,top:0,height:winhei2});
			parentD.find(".pos-footer").hide();
			$("#allmap,.b-map").css({width:winwid,height:winhei2,marginLeft:"-10px"});
			$(".j-full-div").css({left:"-16%"});
			$(".b-f-screen,.pos-adr").css({right:"25px"});
			$(".draw-btn").css({marginLeft:"-7px"})
			$(".full-screen").addClass("full-map");
			$(this).addClass("b-back-full").removeClass("b-forward-full");
		}else{
			parentD.find(".i-hei").attr("scrolling","auto");
			parentD.find(".pos-footer").show();
			parentD.find(".i-hei").css({zIndex:3,top:"60px",height:winhei2-146});
			$("#allmap,.b-map").css({width:initwid,height:inithei,margin:0});
			$(".j-full-div").css({left:"0"});
			$(".b-f-screen,.pos-adr").css({right:"15px"});
			$(".draw-btn").css({marginLeft:"0"})
			$(".full-screen").removeClass("full-map");
			$(this).removeClass("b-back-full").addClass("b-forward-full");
		}
	})


</script>
</body>
</html>