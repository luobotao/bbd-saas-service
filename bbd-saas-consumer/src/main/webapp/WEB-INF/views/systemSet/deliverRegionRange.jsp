<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
	<style type="text/css">
		body, html{width: 100%;height: 100%;overflow: hidden;margin:0;}
		#allmap {margin-right: 300px;height: 100%;overflow: hidden;}
		#result {border-left:1px dotted #999;height:100%;width:295px;position:absolute;top:0px;right:0px;font-size:12px;}
		dl,dt,dd,ul,li{
			margin:0;
			padding:0;
			list-style:none;
		}
		p{font-size:12px;}
		dt{
			font-size:14px;
			font-family:"微软雅黑";
			font-weight:bold;
			border-bottom:1px dotted #000;
			padding:5px 0 5px 5px;
			margin:5px 0;
		}
		dd{
			padding:5px 0 0 5px;
		}
		li{
			line-height:28px;
		}
		.red{color: red;}
	</style>


	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=5LVr5CieSP2a11pR4sHAtWGU"></script>
	<title>百度地图绘制多边型带编辑功能</title>
	<!-- jQuery 2.1.3 -->
	<script src="/resources/adminLTE/plugins/jQuery/jQuery-2.1.3.min.js" > </script>
	<!-- jQuery UI 1.11.2 -->
	<script src="/resources/adminLTE/plugins/src/jquery-ui.min.js" > </script>

</head>
<body>
<div id="allmap" style="overflow:hidden;zoom:1;position:relative;">
	<div id="map" style="height:60%;-webkit-transition: all 0.5s ease-in-out;transition: all 0.5s ease-in-out;"></div>
	<div id="showPanelBtn" style="position:absolute;font-size:14px;top:50%;margin-top:-95px;right:0px;width:20px;padding:10px 10px;color:#999;cursor:pointer;text-align:center;height:170px;rgba(255,255,255,0.9);-webkit-transition:  all 0.5s ease-in-out;transition: all 0.5s ease-in-out;font-family:'微软雅黑';font-weight:bold;">编辑多边形<br/>&lt;</div>
	<div id="panelWrap" style="width:0px;position:absolute;top:0px;right:0px;height:100%;overflow:auto;-webkit-transition: all 0.5s ease-in-out;transition: all 0.5s ease-in-out;">
		<div style="width:20px;height:200px;margin:-100px 0 0 -10px;color:#999;position:absolute;opacity:0.5;top:50%;left:50%;" id="showOverlayInfo">此处用于展示覆盖物信息</div>
		<div id="panel" style="position:absolute;"></div>
	</div>
</div>
<div id="result">
	<dl>
		<dt>绘制工具功能</dt>
		<ul>
			<li><label><input type="radio" name="openClose1"  onclick="openDraw()" />打开</label>  <label><input type="radio" name="openClose1" onclick="bmap.drawingManager.close()" checked="checked"/>关闭</label></li>
		</ul>
		</dd>
		<dt>绘制功能</dt>
		<dd>
			<ul>
				<li>
					<label><input type="radio" name="drawmode" onclick="bmap.drawingManager.setDrawingMode(BMAP_DRAWING_POLYGON)" checked="checked"/>画多边形</label>
				</li>
			</ul>
		</dd>
		<dt>覆盖物操作</dt>
		<dd>
			<ul>
				<li>
					${sitePoints}
					<input type="text" id="sitePoints" name="sitePoints" value="${sitePoints}"/>
					<input type="button" value="清除所有覆盖物" onclick="bmap.clearAll()"/>
					<input type="button" value="获取最后一个覆盖物信息" onclick="bmap.getOverLay()"/>
					<c:url var="putAllOverLayUrl" value="/site/putAllOverLay?${_csrf.parameterName}=${_csrf.token}"/>
					<form action="/site/putAllOverLay?${_csrf.parameterName}=${_csrf.token}" method="post" id="allLaysForm">
						<input type="hidden" name="jsonStr" id="jsonStr"/>
						<input type="button" value="提交" id="formBtn">
					</form>

				</li>
			</ul>
		</dd>
	</dl>
</div>
<!--加载鼠标绘制工具-->
<script type="text/javascript" src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
<link rel="stylesheet" href="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
<!--加载检索信息窗口-->
<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.js"></script>
<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.css" />
<script type="text/javascript">
	var bmapArr = [];
	var arrayPoint = $("sitePoints").value;
	var siteArr = arrayPoint.split(";");
	for(var i = 0; i < siteArr.length; i++){
		var arr = siteArr[i].split(",");
		var barr = [];
		for(var j = 0; j < arr.length; j++){
			var tmp = arr[j].split("_");
			barr.push(new BMap.Point(tmp[0],tmp[1]));
		}
		bmapArr.push(barr);
	}

	var $j = jQuery.noConflict();
	$j(document).ready(function() {
		//提交保存
		$j("#formBtn").click(function () {
			var jsonStr = "";
			bmap.overlays.forEach(function (e) {
				var arrs = e.ro;
				for (var i = 0; i < arrs.length; i++) {
					jsonStr = jsonStr + arrs[i].lng + "_" + arrs[i].lat;
					if (i < arrs.length - 1) {
						jsonStr = jsonStr + ",";
					}
				}
				jsonStr.substring(0, jsonStr.length - 1);
				jsonStr = jsonStr + ";";
			})
			if ("" != jsonStr) {
				$j("#jsonStr").val(jsonStr);
				$j('#allLaysForm').submit();
			} else {
				alert("请先绘制电子围栏");
			}
		})
	});
	/**
	 * Author: mobai
	 * http://mobai.blog.51cto.com/
	 * 转载请注明出处
	 */
	function $(id){
		return document.getElementById(id);
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
			this.map = new BMap.Map('map');
			this.point = new BMap.Point(116.307852,40.057031);
			var map = this.map;
			var styleOptions = this.styleOptions;
			map.centerAndZoom(this.point, 16);
			map.enableScrollWheelZoom();
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
			map.addEventListener("rightclick",function(e){
				alert(e.point.lng + "," + e.point.lat);
			});
		},
		loadMyOverlay: function(){
			var map = this.map;
			this.clearAll();
			map.centerAndZoom(this.point, 11);
			this.myOverlay.forEach(function(e){
				myPolygon = new BMap.Polygon(e, this.styleOptions);
				this.myPolygon = myPolygon;
				try{myPolygon.enableEditing();}catch(e){};
				myPolygon.addEventListener("lineupdate",function(e){
					bmap.showLatLon(e.currentTarget.ro);
				});
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
				alert('不能再删除, 请保留3个以上的点.');
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
			map.removeOverlay(this.myPolygon);
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
	//加载一个已有的多边形

	/*bmap.myOverlay = [[
		new BMap.Point(116.256515,39.995242),
		new BMap.Point(116.502579,39.951893),
		new BMap.Point(116.256515,39.866882)
	],[new BMap.Point(116.403863,40.910426),
		new BMap.Point(116.636129,40.902574),
		new BMap.Point(116.582087,40.731351)
	]];*/
	bmap.myOverlay=bmapArr;
	bmap.init();

	function openDraw(){
		bmap.drawingManager.open();
		bmap.drawingManager.setDrawingMode(BMAP_DRAWING_POLYGON);
	}



</script>


</body>
</html>