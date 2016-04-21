<%@ page import="com.bbd.saas.utils.Dates" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta charset="UTF-8">
        <title>棒棒达快递</title>
        <jsp:include page="../main.jsp" flush="true" />
    </head>
    <body>
       <!-- S pop -->
       <!--S 引导页-->
		<div class="j-guide-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
			<div class="modal-dialog b-guide-dialog" role="document">
				<div class="modal-content">
					<div class="modal-header b-modal-header">
						<h4 class="modal-title tc">设置站点配送区域</h4>
					</div>
					<div class="modal-body tab-area">
						<ul class="b-tit b-guide-tab">
							<li class="guide-cur">01<span class="c-line">|</span>配送范围</li>
							<li>02<span class="c-line">|</span>绘制电子围栏</li>
							<li>03<span class="c-line">|</span>导入地址关键词</li>
						</ul>
						<div class="b-tab-all">
							<div class="b-tab-con b-guide-con form-inline form-inline-n">
								<!-- S 配送区域 -->
								<div class="row step step1">
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
									<div class="col-md-12 pb20">
										<span class="b-map"><img src="static/images/pos_map.png" /></span>
									</div>
								</div>
								<!-- E 配送区域 -->
								
								<!-- S 绘制电子围栏 -->
								<div class="row step step2">
									<div class="col-md-12">
										<div class="b-map">
											<!--主体部分-->
											<div id="allmap" style="overflow:hidden;zoom:1;position:relative;;">
												<div id="map" style="height:473px;-webkit-transition: all 0.5s ease-in-out;transition: all 0.5s ease-in-out;"></div>
											</div>

											<!--主体部分 end-->
										</div>
									</div>
									<div class="col-md-12 mt20">
										<input type="hidden" id="sitePoints" name="sitePoints" value="${sitePoints}"/>
										<a href="javascript:void(0);" class="ser-btn c" onclick="openDraw()">绘制</a>
										<a href="javascript:void(0);" class="ser-btn c" onclick="bmap.clearAll()">清除</a>
										<a href="javascript:void(0);" class="ser-btn d ml6" id="formBtn">提 交</a>
									</div>
								</div>
								<!-- E 绘制电子围栏 -->
								<!-- S 导入地址关键词 -->
								<div class="clearfix step step3">
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
										        <a href="javascript:void(0);"  id="piliangDel" class="ser-btn l">批量删除</a>
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
							
						 <div class="modal-footer mb15 bod0 pad0">
						 	<div class="fl pre-step">
						 		<a href="javascript:void(0);" class="ser-btn l">上一步</a>
						 	</div>
					        <a href="javascript:void(0);" class="ser-btn g" data-dismiss="modal">跳过</a>
					        <a href="javascript:void(0);" class="ser-btn l next-step">保存</a>
					     </div>
					</div>
				</div>
			</div>
		</div>
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
    </body>
</html>