<%@ page import="com.bbd.saas.utils.Dates" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<title>配送区域 - 系统设置 - 棒棒达快递</title>

	<jsp:include page="../main.jsp" flush="true" />

	</head>
<body>
<body class="fbg">
<!-- S content -->
<div class="clearfix b-branch">
	<div class="container-fluid">
		<div class="row">
			<!-- S sidebar -->
			<div class="col-xs-12 col-sm-12 bbd-md-3" style="opacity:0;">
				<ul class="b-sidebar">
					<li class="lv1"><a href="package-arrives.html"><i class="b-icon p-package"></i>包裹到站</a></li>
					<li class="lv1"><a href="tracking-assign.html"><i class="b-icon p-aign"></i>运单分派</a></li>
					<li class="lv1"><a href="exception-processing.html"><i class="b-icon p-error"></i>异常件处理</a></li>
					<li class="lv1"><a href="data-query.html"><i class="b-icon p-query"></i>数据查询</a></li>
					<li class="lv1 side-cur"><a href="system-distribution.html"><i class="b-icon p-set"></i>系统设置</a></li>
					<ul class="menu">
						<li class="curr"><a href="system-distribution.html">配送区域</a></li>
						<li><a href="system-usermanage.html">用户管理</a></li>
						<li><a href="system-role.html">角色管理</a></li>
					</ul>
				</ul>
			</div>
			<!-- E sidebar -->
			<!-- S detail -->
			<div class="b-detail col-xs-12 col-sm-12 bbd-md-9">
				<!-- S 搜索区域 -->
				<div class="search-area">
					<ul class="row pb20">
						<li class="txt-info">
							<em>站点名称：</em>
							<i>${site.name}</i>
						</li>
						<li class="txt-info">
							<em>站点地址：</em>
							<i>${site.province}-${site.city}-${site.area} ${site.address}</i>
						</li>
					</ul>
				</div>
				<!-- E 搜索区域 -->

				<!-- S tab -->
				<div class="tab-area mt20">
					<ul class="clearfix b-tab">
						<li <c:if test="${activeNum eq '1'}"> class="tab-cur"</c:if>><a href="#send-range">配送区域</a></li>
						<li <c:if test="${activeNum eq '2'}"> class="tab-cur"</c:if>><a href="#draw-map">绘制电子围栏</a></li>
						<li <c:if test="${activeNum eq '3'}"> class="tab-cur"</c:if>><a href="#import-key">导入地址关键词</a></li>
					</ul>
					<div class="b-tab-con form-inline form-inline-n tab-content">
						<!-- S 配送区域 -->
						<div class="row tab-pane fade" id="send-range">
							<div class="col-md-12 pb20">
								设置配送范围后，将优先匹配站点附近的订单。
							</div>
							<form action="/site/updateSiteWithRadius" method="POST" id="siteRadiusForm">
								<div class="col-md-12 pb20">
									<label>
										站点周围：<c:set var="count" value="20"/>
										<select id="radius" name="radius"  class="form-control form-con-new">
											<option value="">请选择</option>
											<c:forEach var = "temp" begin="1" step="1" end="${count}">
												<option value ="${temp}" <c:if test="${temp eq site.getDeliveryArea()}">selected</c:if>>${temp}</option>
											</c:forEach>
										</select>  公里
										<input type="hidden" id="siteId" name="siteId" value="${site.getId()}"/>
									</label>

								</div>
								<div class="col-md-12 pb20">
									<div class="b-map"><div id="allmapPs" style="height: 473px;"></div></div>
								</div>
								<div class="col-md-12 form-inline form-inline-n">
									<a href="javascript:void(0);" class="ser-btn l" id="saveSiteBtn">　保存　</a>
								</div>
							</form>
						</div>
						<!-- E 配送区域 -->

						<!-- S 绘制电子围栏 -->
						<div class="row tab-pane fade" id="draw-map">
							<div class="col-md-12">
								<div class="b-map">
									<!--主体部分-->
									<div id="allmap" style="overflow:hidden;zoom:1;position:relative;;">
										<div id="map" style="height:473px;-webkit-transition: all 0.5s ease-in-out;transition: all 0.5s ease-in-out;"></div>
										<div id="showPanelBtn" style="position:absolute;font-size:14px;top:50%;margin-top:-95px;right:0px;width:20px;padding:10px 10px;color:#999;cursor:pointer;text-align:center;height:170px;rgba(255,255,255,0.9);-webkit-transition:  all 0.5s ease-in-out;transition: all 0.5s ease-in-out;font-family:'微软雅黑';font-weight:bold;">编辑多边形<br/>&lt;</div>
										<div id="panelWrap" style="width:0px;position:absolute;top:0px;right:0px;height:100%;overflow:auto;-webkit-transition: all 0.5s ease-in-out;transition: all 0.5s ease-in-out;">
											<div style="width:20px;height:200px;margin:-100px 0 0 -10px;color:#999;position:absolute;opacity:0.5;top:50%;left:50%;" id="showOverlayInfo">此处用于展示覆盖物信息</div>
											<div id="panel" style="position:absolute;"></div>
										</div>
									</div>

									<!--主体部分 end-->
								</div>
							</div>
							<div class="col-md-12 mt20">

								<form action="/site/putAllOverLay?${_csrf.parameterName}=${_csrf.token}" method="post" id="allLaysForm">
									<input type="hidden" name="jsonStr" id="jsonStr"/>
									<input type="hidden" id="sitePoints" name="sitePoints" value="${sitePoints}"/>
									<a href="javascript:void(0);" class="ser-btn c" onclick="openDraw()">绘制</a>
									<a href="javascript:void(0);" class="ser-btn c" onclick="bmap.clearAll()">清除</a>
									<a href="javascript:void(0);" class="ser-btn d ml6" id="formBtn">提 交</a>
								</form>

							</div>
						</div>
						<!-- E 绘制电子围栏 -->
						<!-- S 导入地址关键词 -->
						<div class="clearfix tab-pane fade" id="import-key">
							<div class="row pb20">
								<c:url var="importSiteKeywordFileUrl" value="/site/importSiteKeywordFile?${_csrf.parameterName}=${_csrf.token}"/>
								<form action="/deliverRegion/map/3" method="get" id="siteKeywordForm" name="siteKeywordForm">
									<div class="form-group col-xs-12 col-sm-6 col-md-3 col-lg-3">
										<label>导入时间：</label>
										<input id="between" name="between" type="text" class="form-control" placeholder="请选择导入时间范围" value="${between}"/>
									</div>
									<div class="form-group col-xs-12 col-sm-6 col-md-3 col-lg-3">
										<label>关键词：</label>
										<input id="keyword" name="keyword" type="text" class="form-control" placeholder="请输入关键词" value="${keyword}"/>
									</div>
									<div class="form-group col-xs-12 col-sm-6 col-md-2 col-lg-2">
										<a href="javascript:void(0)" class="ser-btn l" id="querySiteBtn"><i class="b-icon p-query p-ser"></i>查询</a>
									</div>
									<input type="hidden" id="page" value="${page}" name="page">
								</form>
							</div>
							<div class="row pb20">
								<div class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
									<form action="${importSiteKeywordFileUrl}" method="post" enctype="multipart/form-data" id="importFileForm">
										<label class="ser-btn b fileup_ui fl">
											<span>导入地址关键词</span>
											<input type="file" name="file" class="import-file" />
										</label>

										<a href="/site/downloadSiteKeywordTemplate" class="ser-btn b ml6">下载导入模板</a>
										<a href="/site/exportSiteKeywordFile" class="ser-btn b ml10">导出地址关键词</a>
									</form>
								</div>
							</div>

							<!-- S table -->
							<div class="tab-bod mt20">
								<div class="table-responsive">
									<table class="table">
										<thead>
										<tr>
											<th><input type="checkbox" name="inputA" class="j-sel-all"  /></th>
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
													<td><input type="checkbox" value="${siteKeyword.id}" name="inputC"/></td>
													<td>${Dates.formatDateTime_New(siteKeyword.createAt)}</td>
													<td>${siteKeyword.province}</td>
													<td>${siteKeyword.city}</td>
													<td>${siteKeyword.distict}</td>
													<td>${siteKeyword.keyword}</td>
													<td><a href="/site/deleteSitePoiKeyword/${siteKeyword.id}" class="orange j-del">删除</a></td>
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
	console.log(${activeNum});
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
				console.log(response);
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
		console.log(delIds);
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
	console.log(bmapArr);
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
			this.myOverlay.forEach(function(e){
				myPolygon = new BMap.Polygon(e, this.styleOptions);
				this.myPolygon = myPolygon;
				map.removeOverlay(this.myPolygon);
			})

			this.myPolygon = '';
		},
		/**
		 *取覆盖物的经纬度
		 */
		getOverLay: function(){
			var box = this.myPolygon ? this.myPolygon : this.overlays[this.overlays.length - 1];
			console.log(box.ro);
		},
		getCount: function(){
			var n = 0;
			if (this.myPolygon) {
				n++;
			};
			if (this.overlays) {
				n = n + this.overlays.length;
			};
			console.log(n);
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
	console.log(bmap.myOverlay);
	bmap.init();

	function openDraw(){
		bmap.drawingManager.open();
		bmap.drawingManager.setDrawingMode(BMAP_DRAWING_POLYGON);
	}
</script>
</body>
</html>