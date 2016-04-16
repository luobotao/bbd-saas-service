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
	<div class="container">
		<div class="row">
			<!-- S sidebar -->
			<div class="col-xs-12 col-sm-12 col-md-3 col-lg-3" style="opacity:0;">
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
			<div class="b-detail col-xs-12 col-sm-12 col-md-9 col-lg-9">
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
									<span class="b-map"><div id="allmap" style="width: 733px;height: 392px;margin-left:15px;margin-top:10px; "></div></span>
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
								<span class="b-map"><img src="static/images/map.png" /></span>
							</div>
							<div class="col-md-12 mt20">
								<label>
									边框宽度：
									<select class="form-control">
										<option>4</option>
									</select>
								</label>
								<label>
									边框颜色：
									<select class="form-control">
										<option>4</option>
									</select>
								</label>
								<label>
									填充颜色：
									<select class="form-control">
										<option>4</option>
									</select>
								</label>
							</div>

							<div class="col-md-12">
								<a href="javascript:void(0);" class="ser-btn c">绘制/重绘</a>
								<a href="javascript:void(0);" class="ser-btn c ml6">编 辑</a>
								<a href="javascript:void(0);" class="ser-btn l ml6">保 存</a>
								<a href="javascript:void(0);" class="ser-btn d ml6">提 交</a>
							</div>
						</div>
						<!-- E 绘制电子围栏 -->
						<!-- S 导入地址关键词 -->
						<div class="clearfix tab-pane fade" id="import-key">
							<div class="row pb20">
								<c:url var="importSiteKeywordFileUrl" value="/site/importSiteKeywordFile?${_csrf.parameterName}=${_csrf.token}"/>
								<form action="/deliverRegion/map/3" method="get" id="siteKeywordForm" name="siteKeywordForm">
									<div class="form-group col-xs-12 col-sm-6 col-md-5 col-lg-5">
										<label>导入时间：</label>
										<input id="between" name="between" type="text" class="form-control" placeholder="请选择导入时间范围" value="${between}"/>
									</div>
									<div class="form-group col-xs-12 col-sm-6 col-md-5 col-lg-5">
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
									<a href="javascript:void(0)" class="ser-btn b">导入地址关键词</a>
									<a href="/site/downloadSiteKeywordTemplate" class="ser-btn b ml6">下载导入模板</a>
									<a href="/site/exportSiteKeywordFile" class="ser-btn b ml16">导出地址关键词</a>
								</div>
							</div>
							<form action="${importSiteKeywordFileUrl}" method="post" enctype="multipart/form-data">
								选择文件:<input type="file" name="file">
								<input type="submit" value="提交">
							</form>
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
													<td>${siteKeyword.createAt}</td>
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
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=5LVr5CieSP2a11pR4sHAtWGU"></script>
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
		map.centerAndZoom(point, 12);
		var myIcon = new BMap.Icon("/resources/images/b_marker.png", new BMap.Size(20,25));
		var marker = new BMap.Marker(point,{icon:myIcon});  // 创建标注
		map.addOverlay(marker);               // 将标注添加到地图中
		marker.setAnimation(BMAP_ANIMATION_BOUNCE); //跳动的动画
		var circle = new BMap.Circle(point,${site.deliveryArea*1000},{strokeColor:"red", strokeWeight:2, strokeOpacity:0.5}); //创建圆
		map.addOverlay(circle);            //增加圆
		map.enableScrollWheelZoom(true);
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
</script>
</body>
</html>