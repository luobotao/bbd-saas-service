<%@ page import="com.bbd.saas.utils.Dates" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../main.jsp"%>
<html>
<head>
	<title>配送区域 - 系统设置 - 棒棒达快递</title>
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
			<!-- S sidebar -->
			<div class="col-xs-12 col-sm-12 bbd-md-3" style="visibility: hidden;">
				<ul class="sub-sidebar">
				</ul>
			</div>
			<!-- E sidebar -->
			<!-- S detail -->
			<div class="b-detail col-xs-12 col-sm-12 bbd-md-9">
				<!-- S 搜索区域 -->
				<div class="search-area d-search-area">
					<ul class="row pb20">
						<li class="txt-info txt-info-l">
							<em>站点名称：</em>
							<i>${site.name}</i>
						</li>
						<li class="txt-info">
							<em>区域码：</em>
							<i>${site.areaCode}</i>
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
						<li <c:if test="${activeNum eq '2'}"> class="tab-cur"</c:if>><a href="#draw-map" >绘制电子围栏</a></li>
						<li <c:if test="${activeNum eq '3'}"> class="tab-cur"</c:if>><a href="#import-key">导入地址关键词</a></li>
					</ul>
					<div class="b-tab-con form-inline form-inline-n tab-content capacity-map">
						<!-- S 配送区域 -->
						<div class="row tab-pane fade" id="send-range">
							<div class="col-md-12 pb20 f16">
								设置配送范围后，将优先匹配站点附近的订单。
							</div>
							<form  method="POST" id="siteRadiusForm">
								<div class="col-md-12 pb20">
									<label class="f16">
										站点周围：<c:set var="count" value="20"/>
										<select id="radius" name="radius"  class="form-control form-con-new f16">
											<option value="0">请选择</option>
											<c:forEach var = "temp" begin="1" step="1" end="${count}">
												<option value ="${temp}" <c:if test="${temp eq site.getDeliveryArea()}">selected</c:if>>${temp}</option>
											</c:forEach>
										</select>  公里
										<input type="hidden" id="siteId" name="siteId" value="${site.getId()}"/>
									</label>

								</div>
								<div class="col-md-12">
									<div class="b-map">
										<div id="allmapPs" style="height: 533px;"></div>
										<div class="draw-btn">
											<div class="bg-alpha"></div>
											<a href="javascript:void(0);" class="ser-btn l ml12" id="saveSiteBtn">　保存　</a>
										</div>
									</div>
								</div>
							</form>
						</div>
						<!-- E 配送区域 -->

						<!-- S 绘制电子围栏 -->
						<div class="row tab-pane fade" id="draw-map">
							<div class="col-md-12">
								<div class="b-map">
									<div id="allmap" class="bod-rad" style="height: 533px;"></div>
									<a href="javascript:void(0)" onclick="bmap.theLocation()" class="pos-adr"></a>
									<div class="b-f-screen b-forward-full j-full-btn"></div>
									<div class="draw-btn">
										<div class="bg-alpha"></div>
										<input type="hidden" id="sitePoints" name="sitePoints" value="${sitePoints}"/>
										<a href="javascript:void(0);" class="ser-btn c ml12" onclick="openDraw()">绘制</a>
										<a href="javascript:void(0);" class="ser-btn d ml6" id="formBtn">提 交</a>
									</div>
								</div>
							</div>
						</div>
						<!-- E 绘制电子围栏 -->
						<!-- S 导入地址关键词 -->
						<div class="clearfix tab-pane fade" id="import-key">
							<div class="row pb20">
								<c:url var="importSiteKeywordFileUrl" value="/site/importSiteKeywordFile?${_csrf.parameterName}=${_csrf.token}"/>
								<form action="${ctx}/deliverRegion/map/3" method="get" id="siteKeywordForm" name="siteKeywordForm" class="form-inline form-inline-n">
									<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
										<label>导入时间：</label>
										<input id="between" name="between" type="text" class="form-control" placeholder="请选择导入时间范围" value="${between}"/>
									</div>
									<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
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

										<a href="${ctx}/site/downloadSiteKeywordTemplate" class="ser-btn b ml6">下载导入模板</a>
										<a href="${ctx}/site/exportSiteKeywordFile" class="ser-btn b ml10">导出地址关键词</a>
									</form>
								</div>
							</div>

							<!-- S table -->
							<div class="tab-bod mt20">
								<div class="table-responsive">
									<table class="table" id="dis-table">
										<thead>
										<tr>
											<th><input type="checkbox" name="inputA" class="j-sel-all c-cbox" id="selectAll" /></th>
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
												<td><input type="checkbox" value="${siteKeyword.id}" name="inputC" class="c-cbox"/></td>
												<td>${Dates.formatDateTime_New(siteKeyword.createAt)}</td>
												<td>${siteKeyword.province}</td>
												<td>${siteKeyword.city}</td>
												<td>${siteKeyword.distict}</td>
												<td>${siteKeyword.keyword}</td>
												<td><a href="javascript:if(confirm('确认删除？'))location='${ctx}/site/deleteSitePoiKeyword/${siteKeyword.id}'" class="orange">删除</a></td>
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
<div class="b-loading">
<div class="spinner" style="display:none">
	<div class="spinner-container container1">
		<div class="circle1"></div>
		<div class="circle2"></div>
		<div class="circle3"></div>
		<div class="circle4"></div>
	</div>
	<div class="spinner-container container2">
		<div class="circle1"></div>
		<div class="circle2"></div>
		<div class="circle3"></div>
		<div class="circle4"></div>
	</div>
	<div class="spinner-container container3">
		<div class="circle1"></div>
		<div class="circle2"></div>
		<div class="circle3"></div>
		<div class="circle4"></div>
	</div>
</div>
</div>

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
				<a href="javascript:void(0);" class="ser-btn g" id="cancelBtn">取消</a>
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
	// 导入文件
	$(".import-file").on("change",function(){
		$(".j-import-pop").modal();
	})
	$("input[type='checkbox']").iCheck({
		checkboxClass : 'icheckbox_square-blue'
	});
	$("#selectAll").on('ifUnchecked', function() {
		$("input[type='checkbox']", "#dis-table").iCheck("uncheck");
	}).on('ifChecked', function() {
		$("input[type='checkbox']", "#dis-table").iCheck("check");
	});;
	//保存站点配送范围信息
	$("#saveSiteBtn").click(function(){
		var radiusVal = $("#radius option:selected").val();
		if(radiusVal==0){
			alert("请选择站点配送范围");
			return false;
		}else{
			$.ajax({
				url: '${ctx}/site/updateSiteWithRadius/'+radiusVal+'/'+$("#siteId").val(),
				type: 'get',
				cache: false,
				dataType: "text",
				data: {},
				success: function(response){
					alert("保存成功");
					window.location.href="${ctx}/deliverRegion/map/1";
				},
				error: function(){
					alert("抱歉，由于您长时间未操作，当前登录信息已失效。请重新登录");
					if(window.top==window.self){//不存在父页面
						window.location.href="<c:url value="/login" />"
					}else{
						window.top.location.href="<c:url value="/login" />"
					}
				}
			});
		}
	})
	$("#importBtn").click(function(){
		$(this).parents(".j-import-pop").hide();
		$(".spinner").show();
		$("#importFileForm").submit();
		$("[name='file']").val("");
	})
	//更改配送范围
	$("#radius").change(function(){
		var radius = $("#radius option:selected").val();
		showMap(radius*1000);
	})


	//展示配送范围
	function showMap(radius){
		// 百度地图API功能
		var allmapPs = new BMap.Map("allmapPs", {enableMapClick:false,minZoom:11});
		var pointPs = new BMap.Point(${site.lng}, ${site.lat});
		var radiusVal = 15;
		if(radius<2000){
			radiusVal = 15;
		}else if(radius<3000){
			radiusVal = 14;
		}else if(radius<5000){
			radiusVal = 13;
		}else if(radius<10000){
			radiusVal = 12;
		}else{
			radiusVal = 11;
		}
		allmapPs.centerAndZoom(pointPs, radiusVal);
		var myIcon = new BMap.Icon("${ctx}/resources/images/b_marker.png", new BMap.Size(20,25));
		var marker = new BMap.Marker(pointPs,{icon:myIcon});  // 创建标注
		allmapPs.addOverlay(marker);               // 将标注添加到地图中
		marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
		//var circle = new BMap.Circle(point,radius,{strokeColor:"red", strokeWeight:2, strokeOpacity:0.5}); //创建圆
		var circle = new BMap.Circle(pointPs,radius,{fillColor:"#ff2400", strokeColor:"#ff2400", strokeWeight: 1 ,fillOpacity: 0.1, strokeOpacity: 1});
		allmapPs.addOverlay(circle);            //增加圆
		allmapPs.enableScrollWheelZoom(true);
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
		$("#page").val("0");
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
			window.location.href="${ctx}/site/piliangDeleteSitePoiKeyword/"+delIds
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
	$("#cancelBtn").click(function(){
		$(".j-import-pop").modal('hide');
		$("[name='file']").val("");
	})

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
			if(arrs.length>2){
				//console.log(arrs);
				for (var i = 0; i < arrs.length; i++) {
					jsonStr = jsonStr + arrs[i].lng + "_" + arrs[i].lat;
					if (i < arrs.length - 1) {
						jsonStr = jsonStr + ",";
					}
					//console.log(jsonStr);
				}
				jsonStr.substring(0, jsonStr.length - 1);
				jsonStr = jsonStr + ";";
				//console.log(jsonStr);
			}
		})
		if ("" != jsonStr) {
			var siteId =  $("#siteId").val();
			var url = "<c:url value='/site/putAllOverLay?${_csrf.parameterName}=${_csrf.token}'/>";
			$.ajax({
				url: url,
				type: 'POST',
				cache: false,
				data: {
					"jsonStr" : jsonStr,
					"siteId":siteId
				},
				success: function(data){
					//console.log(data);
					if(data == "success"){
						alert("提交成功");
					}else{
						console.log("error:"+data);
					}
				},
				error: function(){
					alert("抱歉，由于您长时间未操作，当前登录信息已失效。请重新登录");
					if(window.top==window.self){//不存在父页面
						window.location.href="<c:url value="/login" />"
					}else{
						window.top.location.href="<c:url value="/login" />"
					}
				}
			});
			/*          $("#jsonStr").val(jsonStr);
			 $('#allLaysForm').submit();*/
		} else {
			alert("请先绘制电子围栏");
		}
	})

	function newLabel(point, name){
		var opts = {
			position : point,    // 指定文本标注所在的地理位置
			offset   : new BMap.Size(-40, -50)    //设置文本偏移量
		}
		//console.log("name===" + name);
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
			this.point = new BMap.Point(${site.lng},${site.lat});
			this.map.centerAndZoom(this.point,12);
			this.map.enableScrollWheelZoom();
			this.map.disableInertialDragging();

			var map = this.map;
			var styleOptions = this.styleOptions;
			var myIcon = new BMap.Icon("${ctx}/resources/images/b_marker.png", new BMap.Size(20,25));
			var marker = new BMap.Marker(this.point,{icon:myIcon});  // 创建标注
			marker.disableMassClear();
			this.map.addOverlay(marker);               // 将标注添加到地图中
			/*var label = new BMap.Label("${site.name}",{offset:new BMap.Size(20,-10)});
			marker.setLabel(label);*/

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
			//console.log("siteName===${site.name}");
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
			//console.log("ctt")
			//console.log(this.myOverlay);
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
				//console.log(myPolygon);
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
		},
		// 用经纬度设置地图中心点
		theLocation:function(){
			//console.log("xxxx");
			//console.log(this.map);
			this.map.panTo(this.point);
		}
	};

	//显示结果面板动作
	var isPanelShow = false;
	$("showPanelBtn").onclick = showPanel;
	function showPanel(){
		//console.log(isPanelShow);
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
	function openDraw(){
		bmap.drawingManager.open();
		bmap.drawingManager.setDrawingMode(BMAP_DRAWING_POLYGON);
	}

	$('.b-tab li:eq(${activeNum-1}) a').tab('show');
	showMap(${site.deliveryArea*1000});
	bmap.myOverlay=bmapArr;
	//console.log(bmap.myOverlay);
	bmap.init();
	$('.b-tab a').click(function (e) {
		//console.log($(this));
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


	var winhei2=window.screen.availHeight;
	var inithei=$("#allmap").height();

	$(".j-full-btn").on("click",function(){
		if($(this).hasClass("b-forward-full")){
			$(".pos-footer").hide();
			$("#allmap,.b-map").css({height:winhei2-60-84,marginLeft:"-10px"});
//			$(".b-f-screen,.pos-adr").css({right:"25px"});
			$(".draw-btn").css({marginLeft:"-10px"})
			$("#draw-map").addClass("full-map");
			$(this).addClass("b-back-full").removeClass("b-forward-full");
		}else{
			$(".pos-footer").show();
			$("#allmap,.b-map").css({height:inithei,margin:0});
//			$(".b-f-screen,.pos-adr").css({right:"15px"});
			$(".draw-btn").css({marginLeft:"0"})
			$("#draw-map").removeClass("full-map");
			$(this).removeClass("b-back-full").addClass("b-forward-full");
		}
	})


</script>
</body>
</html>