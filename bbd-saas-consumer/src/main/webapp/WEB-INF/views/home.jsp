<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>配送系统首页  - 棒棒达快递</title>
	<link href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />" rel="stylesheet"  type="text/css" />
	<link href="<c:url value="/resources/stylesheets/main.css" />" rel="stylesheet"  type="text/css" /><!--自定义css-->
	<script src="<c:url value="/resources/adminLTE/plugins/jQuery/jQuery-2.1.3.min.js" />"> </script>
	<script src="<c:url value="/resources/bootstrap/js/bootstrap.min.js" />" type="text/javascript"></script>
</head>
<body>
<!-- S nav -->
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
				<li><a href="<c:url value="/" />">首页</a></li>
				<li><a href="javascript:void(0);"><i class="glyphicon glyphicon-user orange">${user.realName}</i></a></li>
				<li><a href="<c:url value="/logout" />">退出登录</a></li>
			</ul>
		</div><!-- /.navbar-collapse -->
	</div><!-- /.container-fluid -->
</nav>
<!-- E nav -->
<!-- S content -->
<div class="clearfix">
	<div class="b-con">
		<span class="cloud"><img src="<c:url value="/resources/images/cloud.png" />" /></span>
		<div class="container">
			<ul class="row">
				<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
					<a href="<c:url value="/?typ=arrive" />">
						<div class="b-status-card">
							<span><img src="<c:url value="/resources/images/arrive.png" />" alt="包裹到站" /></span>
							<h3>包裹到站</h3>
						</div>
					</a>

				</li>
				<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
					<a href="<c:url value="/?typ=asign" />">
						<div class="b-status-card">
							<span><img src="<c:url value="/resources/images/asign.png" />" alt="运单分派" /></span>
							<h3>运单分派</h3>
						</div>
					</a>
				</li>
				<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
					<a href="<c:url value="/?typ=error" />">
						<div class="b-status-card">
							<span><img src="<c:url value="/resources/images/error.png" />" alt="异常件处理" /></span>
							<h3>异常件处理</h3>
						</div>
					</a>
				</li>
				<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
					<a href="<c:url value="/dataQuery" />">
						<div class="b-status-card">
							<span><img src="<c:url value="/resources/images/query.png"/>" alt="数据查询" /></span>
							<h3>数据查询</h3>
						</div>
					</a>
				</li>
				<li class="b-status col-xs-12 col-sm-6 col-md-4 col-lg-4">
					<a href="<c:url value="/?typ=set" />">
						<div class="b-status-card">
							<span><img src="<c:url value="/resources/images/set.png" />" alt="系统设置" /></span>
							<h3>系统设置</h3>
						</div>
					</a>
				</li>
			</ul>
		</div>

	</div>
	<div class="b-con-bot"></div>
</div>
<!-- E content -->
<!-- S footer -->
<!-- S pop -->
<!--S 引导页-->

<!--E 引导页-->
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
				<a href="javascript:void(0);" class="ser-btn g" data-dismiss="modal">取消</a>
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
	$('.b-tab li:eq(${activeNum-1}) a').tab('show');
	$('.b-tab a').click(function (e) {
		e.preventDefault()
		$(this).tab('show')
		$(this).parents("li").addClass("tab-cur").siblings().removeClass("tab-cur");
	})
	//保存站点配送范围信息
	$("#saveSiteBtn").click(function(){
		$.ajax({
			url: '/site/updateSiteWithRadius/'+$("#radius option:selected").val()+'/'+$("#siteId").val(),
			type: 'get',
			cache: false,
			dataType: "text",
			data: {},
			success: function(response){
				alert("保存成功");
				window.location.href="/deliverRegion/map/1";
			},
			error: function(){
				alert('服务器繁忙，请稍后再试！');
			}
		});
	})
	$("#importBtn").click(function(){
		$("#importFileForm").submit();
	})

	showMap();
	//展示配送范围
	function showMap(){
		// 百度地图API功能
		var map = new BMap.Map("allmapPs");
		if(${site.lng != ""&&site.lat != ""}){
			var point = new BMap.Point(${site.lng}, ${site.lat});
			map.centerAndZoom(point, 12);
			var myIcon = new BMap.Icon("/resources/images/b_marker.png", new BMap.Size(20,25));
			var marker = new BMap.Marker(point,{icon:myIcon});  // 创建标注
			map.addOverlay(marker);               // 将标注添加到地图中
			marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
			var circle = new BMap.Circle(point,${site.deliveryArea*1000},{strokeColor:"red", strokeWeight:2, strokeOpacity:0.5}); //创建圆
			map.addOverlay(circle);            //增加圆
			map.enableScrollWheelZoom(true);
		}
	}

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
		$("#siteKeywordForm").submit();
	})
	//批量删除
	$("#piliangDel").click(function(){
		var id_array=new Array();
		$('input[name="inputC"]:checked').each(function(){
			id_array.push($(this).val());//向数组中添加元素
		});
		var delIds = id_array.join(',');
		if(delIds==""){
			alert("请选择要删除的站点关键词");
			return false;
		}
		if(confirm("确认批量删除所选站点关键词？")){
			window.location.href="/site/piliangDeleteSitePoiKeyword/"+delIds
		}
	})

	//显示分页条
	var pageStr = paginNav(${page}, ${pageNum}, ${pageCount});
	$("#pagin").html(pageStr);

	//加载带有查询条件的指定页的数据
	function gotoPage(pageIndex,keyword,between) {
		$("#page").val(pageIndex);
		$("#siteKeywordForm").submit();
	}


	//--------------------panel 2------------------------------------

	var bmapArr = [];
	var arrayPoint = $("#sitePoints").val();
	var siteArr = arrayPoint.split(";");
	for (var i = 0; i < siteArr.length; i++) {
		var arr = siteArr[i].split(",");
		var barr = [];
		for (var j = 0; j < arr.length; j++) {
			var tmp = arr[j].split("_");
			barr.push(new BMap.Point(tmp[0], tmp[1]));
		}
		bmapArr.push(barr);
	}
	//console.log(bmapArr);

	//提交保存
	$("#formBtn").click(function () {
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
			console.log(jsonStr);
		})
		if ("" != jsonStr) {
			var url = "<c:url value='/site/putAllOverLay?${_csrf.parameterName}=${_csrf.token}'/>";
			$.ajax({
				url: url,
				type: 'POST',
				cache: false,
				data: {
					"jsonStr" : jsonStr
				},
				success: function(data){
					console.log(data);
					if(data == "success"){
						alert("提交成功");
					}else{
						console.log("error:"+data);
					}
				},
				error: function(){
					alert('服务器繁忙，请稍后再试！');
				}
			});
			/*			$("#jsonStr").val(jsonStr);
			 $('#allLaysForm').submit();*/
		} else {
			alert("请先绘制电子围栏");
		}
	})

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
			this.point = new BMap.Point(${site.lng}, ${site.lat});
			var map = this.map;
			var styleOptions = this.styleOptions;
			map.centerAndZoom(this.point, 12);
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
				try{myPolygon.enableEditing();
					myPolygon.enableMassClear();}catch(e){}
				myPolygon.addEventListener("lineupdate",function(e){
					bmap.showLatLon(e.currentTarget.ro);
				});
				myPolygon.addEventListener("click",function(e){
					if(confirm("删除？")){
						bmap.delPolygon(e);
					}
				});
				console.log(myPolygon);
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
			//console.log("[arr]:"+arr);
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
			console.log(overlays);
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
			//console.log(box.ro);
		},
		getCount: function(){
			var n = 0;
			if (this.myPolygon) {
				n++;
			};
			if (this.overlays) {
				n = n + this.overlays.length;
			};
			//console.log(n);
		}
	};
	//显示结果面板动作
	var isPanelShow = false;
	$("showPanelBtn").onclick = showPanel;
	function showPanel(){
		console.log(isPanelShow);
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
	//console.log(bmap.myOverlay);
	bmap.init();

	function openDraw(){
		bmap.drawingManager.open();
		bmap.drawingManager.setDrawingMode(BMAP_DRAWING_POLYGON);
	}
</script>
<!-- E pop -->
<footer class="container tc">
	<em class="b-copy">京ICP备 465789765 号 版权所有 &copy; 2016-2020 棒棒达       北京棒棒达科技有限公司</em>
</footer>
<!-- E footer -->
<script type="application/javascript">
	if(${user.loginCount==0}){
		console.log("aabb");
		$(".j-guide-pop").modal();
	}
</script>
</body>
</html>
