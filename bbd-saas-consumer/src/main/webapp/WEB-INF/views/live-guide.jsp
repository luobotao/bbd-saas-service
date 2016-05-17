<%@ page import="com.bbd.saas.utils.Dates" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/main.jsp"%>
<html>
	<head></head>
</html>
<bobdy>
	<div class="modal-header b-modal-header">
		<button type="button" class="close j-close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
		<h4 class="modal-title tc">设置站点配送区域</h4>
	</div>
	<div class="modal-body tab-area">
		<ul class="b-tit b-guide-tab">
			<li class="guide-cur">01<span class="c-line">|</span>配送范围</li>
			<li>02<span class="c-line">|</span>绘制电子围栏</li>
			<li >03<span class="c-line">|</span>导入地址关键词</li>
		</ul>
		<div class="b-tab-all">
			<div class="b-tab-con b-guide-con form-inline form-inline-n">
				<!-- S 配送区域 -->
				<div class="row step step1">
					<div class="col-md-12 pb20 f16">
						设置配送范围后，将优先匹配站点附近的订单。
					</div>
					<form  method="POST" id="siteRadiusForm">
						<div class="col-md-12 pb20">
							<label>
								站点周围：<c:set var="count" value="20"/>
								<select id="radius" name="radius"  class="form-control form-con-new">
									<option value="0">请选择</option>
									<c:forEach var = "temp" begin="1" step="1" end="${count}">
										<option value ="${temp}" <c:if test="${temp eq site.getDeliveryArea()}">selected</c:if>>${temp}</option>
									</c:forEach>
								</select>  公里
								<input type="hidden" id="siteId" name="siteId" value="${site.getId()}"/>
							</label>

						</div>
						<div class="col-md-12 pb20">
							<div class="b-map"><div id="allmapPs" style="height: 533px;"></div></div>
						</div>
					</form>
				</div>
				<!-- E 配送区域 -->


				<!-- S 绘制电子围栏 -->
				<div class="row step step2">
					<div class="col-md-12 pb20">
						<div class="b-map">
							<div id="allmap" class="bod-rad" style="height: 533px;"></div>
							<a href="javascript:void(0)" onclick="bmap.theLocation()" class="pos-adr"></a>

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
				<div class="clearfix step step3">
					<div class="row pb20">
						<form action="" method="post" id="siteKeywordForm" >
							<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
								<label>导入时间：</label>
								<input id="between" name="between" type="text" class="form-control w150" placeholder="请选择导入时间"/>
							</div>
							<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
								<label>关键词：</label>
								<input id="keyword" name="keyword" type="text" class="form-control w150" placeholder="请输入关键词" />
							</div>
							<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
								<a href="javascript:void(0)" class="ser-btn l" id="querySiteBtn"><i class="b-icon p-query p-ser"></i>查询</a>
							</div>
							<input type="hidden" id="page" name="page">
						</form>
					</div>
					<div class="row">
						<div class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
							<form action="" method="post" enctype="multipart/form-data" id="importFileForm">
								<label class="ser-btn b fileup_ui fl">
									<span>导入地址关键词</span>
									<input type="file" name="file" class="import-guid-file" />
								</label>

								<a href="${ctx}/site/downloadSiteKeywordTemplate" class="ser-btn b ml6">下载导入模板</a>
							</form>
						</div>
					</div>

					<!-- S table -->
					<div class="tab-bod mt20">
						<div class="table-responsive y-scroll">
							<table class="table mb0" id="guide-table">
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

								</tbody>
							</table>
						</div>
						<!-- E table -->
						<!-- S tableBot -->
						<div class="clearfix mt20">
							<!-- S button -->
							<div class="clearfix fl ml12">
								<a href="javascript:void(0);" id="piliangDel" class="ser-btn l">批量删除</a>
							</div>
							<!-- E button -->

						</div>
						<!-- S page -->
						<div id="pagin" class="clearfix pb20"></div>
						<!-- E page -->
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
			<a href="javascript:void(0);" class="ser-btn g next-step">跳过</a>
			<a href="javascript:void(0);" class="ser-btn l next-step" id="saveBtn">保存</a>
		</div>
	</div>
	<!-- S pop -->
	<!--S 提示-->
	<div class="j-import-guid-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
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
					<a href="javascript:void(0);" class="ser-btn g" id="cancelImportBtn">取消</a>
					<a href="javascript:void(0);" class="ser-btn l" id="importBtn">确定</a>
				</div>
			</div>
		</div>
	</div>
	<!--E 提示-->
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
	<!-- E pop -->
	<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=5LVr5CieSP2a11pR4sHAtWGU"></script>
	<!--加载鼠标绘制工具-->
	<script type="text/javascript" src="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.js"></script>
	<link rel="stylesheet" href="http://api.map.baidu.com/library/DrawingManager/1.4/src/DrawingManager_min.css" />
	<!--加载检索信息窗口-->
	<script type="text/javascript" src="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.js"></script>
	<link rel="stylesheet" href="http://api.map.baidu.com/library/SearchInfoWindow/1.4/src/SearchInfoWindow_min.css" />
	<script type="application/javascript">
		$("input[type='checkbox']").iCheck({
			checkboxClass : 'icheckbox_square-blue'
		});

		// 导入文件
		$(".import-guid-file").on("change",function(){
			$(".j-import-guid-pop").addClass("in").show();
		})
		//引导页
		$(".b-guide-con .step:eq(2)").show();
		//点击保存
		mcount = 0;
		$('.next-step').click(function () {
			//alert(lilen)
			$('.pre-step').show();
			mcount += 1
			// 3的倍数
			if (mcount >= 3) {
				$(".j-guide-pop").hide().removeClass("in");
				$("#mask").hide();
			} else if (mcount < 3) {

				if(mcount==1) {
					//保存最新站点值
					saveSiteFunc();
					window.setTimeout(function(){
						bmap.map.panTo(new BMap.Point(${site.lng},${site.lat}));
					}, 500);
				}else if (mcount == 2)  {
					$("#saveBtn").html("完成")
				}
				$(".b-guide-tab li").eq(mcount).addClass("guide-cur");
			}
			showOther(mcount)
		});
		//点击上一步
		$('.pre-step').click(function () {
			$("#saveBtn").html("保存");
			mcount -= 1
			if (mcount <= 0) {
				mcount = 0;
				$(".b-guide-tab li").eq(mcount + 1).removeClass("guide-cur");
				$('.pre-step').hide();

			} else if (mcount >= 0) {
				//alert(mcount)
				$(".b-guide-tab li").eq(mcount + 1).removeClass("guide-cur");
			}
			showOther(mcount);
		});
		//点击左右按钮执行的函数
		function showOther(mcount) {
			var viewwid = $('.b-tab-all').width();
			$(".b-guide-con").css({"margin-left": -viewwid * mcount + 'px'});
		};
		//保存站点配送范围信息
		function saveSiteFunc(){
			var radiusVal = $("#radius option:selected").val();
			if(radiusVal==0){
				console.log("请选择站点配送范围");
				return false;
			}else{
				$.ajax({
					url: '${ctx}/site/updateSiteWithRadius/'+radiusVal+'/'+$("#siteId").val(),
					type: 'get',
					cache: false,
					dataType: "text",
					data: {},
					success: function(response){
						console.log("保存最新站点配送范围成功");
					},
					error: function(){
						console.log('服务器繁忙，请稍后再试！');
						window.location.href="${ctx}/login";
					}
				});
			}
		}

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
			var circle = new BMap.Circle(pointPs,radius,{fillColor:"#ff2400",strokeColor:"#ff2400", strokeWeight: 1 ,fillOpacity: 0.1, strokeOpacity: 1});
			allmapPs.addOverlay(circle);            //增加圆
			allmapPs.enableScrollWheelZoom(true);
			window.setTimeout(function(){
				allmapPs.panTo(new BMap.Point(${site.lng},${site.lat}));
			}, 500);
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
				if(arrs.length>2){
					console.log(arrs);
					for (var i = 0; i < arrs.length; i++) {
						jsonStr = jsonStr + arrs[i].lng + "_" + arrs[i].lat;
						if (i < arrs.length - 1) {
							jsonStr = jsonStr + ",";
						}
						console.log(jsonStr);
					}
					jsonStr.substring(0, jsonStr.length - 1);
					jsonStr = jsonStr + ";";
					console.log(jsonStr);
				}
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
						console.log('服务器繁忙，请稍后再试！');
						window.location.href="${ctx}/login";
					}
				});
				/*          $("#jsonStr").val(jsonStr);
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
				console.log("ctt")
				console.log(this.myOverlay);
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
				console.log(overlays);
				for(var i = 0; i < overlays.length; i++){
					map.removeOverlay(overlays[i]);
				}
				this.overlays.length = 0
				map.clearOverlays();
				this.myPolygon = '';
			},
			// 用经纬度设置地图中心点
			theLocation:function(){
				console.log("xxxx");
				console.log(this.map);
				this.map.panTo(this.point);
			}
		};

		function openDraw(){
			bmap.drawingManager.open();
			bmap.drawingManager.setDrawingMode(BMAP_DRAWING_POLYGON);
		}

		showMap(${site.deliveryArea*1000});
		bmap.myOverlay=bmapArr;
		console.log(bmap.myOverlay);
		bmap.init();

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


		//--------------------panel 3------------------------------------
		$("#importBtn").click(function(){
			console.log("import start");
			$(".j-import-guid-pop").hide();
			$(".spinner").show();
			$("#importFileForm").ajaxSubmit({
				type: 'post',
				url: "${ctx}/site/importSiteKeywordFileWithAjax?${_csrf.parameterName}=${_csrf.token}",
				data : $( '#importFileForm').serialize(),
				dataType : 'json',
				timeout: 0,
				success: function(data){
					console.log("import file success");
					console.log(data);
					updateDataList(data);
					$(".import-guid-file").val("");
					$(".spinner").hide();
				},
				error: function(JsonHttpRequest, textStatus, errorThrown){
					console.log( "超时，服务器异常!");
					$(".spinner").hide();
					$(".table-responsive").addClass("guide-tab");
					querySiteKey();
				}
			});
		})
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
			console.log("query site start");
			$(".table-responsive").addClass("guide-tab");
			querySiteKey();

		})
		function updateDataList(data){
			$( '#dataList').html("");
			$.each(data.siteKeywordPageList, function(i, item){
				var time = getDate1(item.createAt);
				$( '#dataList').append("<tr><td><input type='checkbox' value='"+item.id+"' name='inputC' class='c-cbox'/></td>" +
						"<td>"+time+"</td><td>"+item.province+"</td><td>"+item.city+"</td><td>"+item.distict+"</td><td>"+item.keyword+"</td>" +
						"<td><a href='javascript:void(0);' class='orange' onclick='delSiteKeywordWithTr(\""+item.id+"\")'>删除</a></td></tr>");
			});
			$("input[type='checkbox']").iCheck({
				checkboxClass : 'icheckbox_square-blue'
			})
			$("#selectAll").on('ifUnchecked', function() {
				$("input[type='checkbox']", "#guide-table").iCheck("uncheck");
			}).on('ifChecked', function() {
				$("input[type='checkbox']", "#guide-table").iCheck("check");
			});;
			//显示分页条
			var pageStr = paginNavMin(data.page, data.pageNum, data.pageCount);
			$("#pagin").html(pageStr);
		}
		//加载带有查询条件的指定页的数据
		function gotoPage(pageIndex) {
			$("#page").val(pageIndex);
			console.log("page change");
			querySiteKey();
		}
		function querySiteKey(){
			$("#siteKeywordForm").ajaxSubmit({
				type: 'post',
				url: "${ctx}/site/deliverRegionWithAjax?${_csrf.parameterName}=${_csrf.token}",
				data : {},
				dataType : 'json',
				success: function(data){
					console.log("query site success");
					console.log(data);
					updateDataList(data);
				},
				error: function(JsonHttpRequest, textStatus, errorThrown){
					console.log( "服务器异常!");
					window.location.href="${ctx}/login";
				}
			});
		}
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
				$.ajax({
					url: '${ctx}/site/piliangDeleteSitePoiKeywordWithAjax/'+delIds,
					type: 'get',
					cache: false,
					dataType: "json",
					data: {},
					success: function(data){
						console.log("批量删除站点关键字成功");
						updateDataList(data);
						$("input[name=inputA]").prop("checked", false);
					},
					error: function(){
						console.log('批量删除站点关键字失败');
						window.location.href="${ctx}/login";
					}
				});
			}
		})


		function delSiteKeywordWithTr(siteKeyId){
			console.log("aabbcc");
			if(confirm('确认删除？')){
				$.ajax({
					url: '${ctx}/site/deleteSitePoiKeywordWithAjax/'+siteKeyId,
					type: 'get',
					cache: false,
					dataType: "json",
					data: {},
					success: function(data){
						console.log("删除站点关键字成功");
						updateDataList(data);
					},
					error: function(){
						console.log('删除站点关键字失败');
						window.location.href="${ctx}/login";
					}
				});
			}
		}
		$("#cancelImportBtn").click(function(){
			$(".j-import-guid-pop").hide();
			$(".import-guid-file").val("");
		})

		function getLocalTime(nS) {
			return new Date(parseInt(nS) * 1000).toLocaleString().replace(/年|月/g, "-").replace(/日/g, " ");
		}

	</script>
</bobdy>
</html>