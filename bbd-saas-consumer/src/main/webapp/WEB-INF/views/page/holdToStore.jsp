<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.ArriveStatus" %>
<%@ page import="com.bbd.saas.enums.OrderStatus" %>
<%@ page import="com.bbd.saas.utils.Dates" %>
<%@ page import="com.bbd.saas.enums.ExpressStatus" %>
<%@ page import="com.bbd.saas.enums.OrderSetStatus" %>
<%@ page import="com.bbd.saas.mongoModels.User" %>
<%@ page import="com.bbd.saas.constants.UserSession" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<jsp:include page="../main.jsp" flush="true" />

</head>
<body class="fbg">
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
				<!-- S 状态 -->
				<ul class="row">
					<li class="b-board-card col-xs-12 col-sm-6 col-md-4 col-lg-4" >
						<dl class="arrive-status  c1" onclick="todaySuccessOrder();" style="cursor: pointer">
							<dt class="b-order" id="successOrderNum">${successOrderNum}</dt>
							<dd>今日成功 </dd>
							<dd>接单数</dd>
						</dl>
					</li>
					<li class="b-board-card col-xs-12 col-sm-6 col-md-4 col-lg-4" >
						<dl class="arrive-status c2" onclick="historyNotArri();" style="cursor: pointer">
							<dt class="b-order" id="todayNoToStoreNum">${todayNoToStoreNum}</dt>
							<dd>今日未入库</dd>
							<dd>订单数</dd>
						</dl>
					</li>
					<li class="b-board-card col-xs-12 col-sm-6 col-md-4 col-lg-4" >
						<dl class="arrive-status c3" onclick="todayArri();" style="cursor: pointer">
							<dt class="b-order"id="historyToStoreNum">${historyToStoreNum}</dt>
							<dd>历史未入库 </dd>
							<dd>订单数</dd>
						</dl>
					</li>
					<li class="b-board-card col-xs-12 col-sm-6 col-md-4 col-lg-4" >
						<dl class="arrive-status c3" onclick="todayToStore();" style="cursor: pointer">
							<dt class="b-order"id="todayToStoreNum">${todayToStoreNum}</dt>
							<dd>今日已入库 </dd>
							<dd>订单数</dd>
						</dl>
					</li>
				</ul>
				<!-- E 状态 -->
				<!-- S 搜索区域 -->
				<form class="form-inline form-inline-n">
					<div class="search-area">
						<div class="row pb20">
							 <div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
                                <label>状态：</label>
                                <select id="orderSetStatus" name="orderSetStatus" class="form-control form-con-new">
                                    <%=OrderSetStatus.Srcs2HTML(-1)%>
                                </select>
                            </div>
                            <div class="form-group col-xs-12 col-sm-6 col-md-5 col-lg-5">
                                <label>揽件员：</label>
                                <select id="embrace" name="embrace" class="form-control form-con-new">
									<option value="0">请选择</option>
									<c:if test="${userList!=null}">
									<c:forEach items="${userList}" var="user">
										<option value="${user.id}">${user.realName}</option>
								</c:forEach>
									</c:if>
                                </select>
                            </div>
							<div class="form-group col-xs-12 col-sm-6 col-md-3 col-lg-3">
								<a href="javascript:void(0)" class="ser-btn l" onclick="searchOrder()"><i class="b-icon p-query p-ser"></i>查询</a>
							</div>
						</div>
						<div class="row pb20">
							<%--<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
								<label>扫描包裹号：</label>
								<input id="parcelCode" name="parcelCode" class="form-control" type="text" onkeypress="enterPress(event)" />
								<p class="help-block" id="parcelCodeP" style="display:none;"></p>
							</div>--%>
							<div class="form-group col-xs-12 col-sm-6 col-md-5 col-lg-5">
								<label>扫描运单号：　</label>
								<input id="mailNum" name="mailNum" class="form-control" type="text" onkeypress="enterPress(event)"/>
								<p class="help-block" id="mailNumP" style="display:none;"></p>
							</div>
						</div>
					</div>
				</form>
				<!-- E 搜索区域 -->
				<div class="tab-bod mt20">
					<!-- S table -->
					<div class="table-responsive">
						<table id="orderTable" class="table"> 
                            <thead>
                            <tr>
                                <th>揽件员</th>
                                <th>手机</th>
                                <th>运单号</th>
                                <th>收货人</th>
                                <th>收货人电话</th>
                                <th width="20%">地址</th>
                                <th>状态</th>
                            </tr>
                            </thead>
                            <tbody id="dataList">

                            <c:if test="${orderHoldPageModel.datas==null}">
                                <tr>
                                    <td colspan="7">没有符合查询条件的数据</td>
                                </tr>
                            </c:if>

                            <c:if test="${orderHoldPageModel.datas !=null}">
                                <c:forEach items="${orderHoldPageModel.datas}" var="orderHoldToStore">
                                    <tr>

                                        <td>${orderHoldToStore.userName} </td>
                                        <td>${orderHoldToStore.phone} </td>
                                        <td>${orderHoldToStore.mailNum} </td>
                                        <td>${orderHoldToStore.recieverName} </td>
                                        <td>${orderHoldToStore.recieverPhone} </td>
                                        <td class="tl">${orderHoldToStore.recieverAddress} </td>
                                        <td>
                                            <c:if test="${orderHoldToStore.orderSetStatus=='NOEMBRACE'}">
                                                待揽件
                                            </c:if>
                                            <c:if test="${orderHoldToStore.orderSetStatus=='WAITTOIN'}">
                                                待入库
                                            </c:if>
                                            <c:if test="${orderHoldToStore.orderSetStatus=='WAITSET'}">
                                                待揽件集包
                                            </c:if>
                                            <c:if test="${orderHoldToStore.orderSetStatus=='WAITDRIVERGETED'}">
                                                待司机取货送往分拨中心
                                            </c:if>
                                            <c:if test="${orderHoldToStore.orderSetStatus=='DRIVERGETED'}">
                                                正在运输到分拨中心
                                            </c:if>
                                            <c:if test="${orderHoldToStore.orderSetStatus=='ARRIVEDISPATCH'}">
                                                已到达分拨中心
                                            </c:if>
                                            <c:if test="${orderHoldToStore.orderSetStatus=='WAITDISPATCHSET'}">
                                                待分拣集包
                                            </c:if>
                                            <c:if test="${orderHoldToStore.orderSetStatus=='WAITDRIVERTOSEND'}">
                                                待司机取货送往配送点
                                            </c:if>
                                            <c:if test="${orderHoldToStore.orderSetStatus=='DRIVERSENDING'}">
                                                正在运输到配送点
                                            </c:if>

                                            <c:if test="${orderHoldToStore.orderSetStatus=='ARRIVED'}">
                                                已到达配送点
                                            </c:if>

                                        </td>
                                    </tr>

                                </c:forEach>
                            </c:if> 
							</tbody>
							<tfoot>
								<div class="j-pl-pop modal fade" id="toSitePrompt" tabindex="-1" role="dialog" aria-hidden="true">
									<div class="modal_wrapper">
										<div class="modal-dialog b-modal-dialog">
											<div class="modal-content">
												<div class="modal-header b-modal-header">
													<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
													<h4 class="modal-title  tc" id="activeUserLabel">确认</h4>
												</div>
												<div class="modal-body b-modal-body" id="popContent">
													确认批量到站？<br>
													该操作会把选中的订单设置为已到站。
												</div>
												<div class="modal-footer">
													<div class="row mt20">
														<span class="col-md-6">
															<button type="button" class="ser-btn g wp100" data-dismiss="modal" onclick="cancelToSite()">取消</button>
														</span>
														<span class="col-md-6">
															<button  type="button" class="ser-btn wp100 l" onclick="doToSite()">确认</button>
														</span>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</tfoot>
						</table>
					</div>
					<!-- E table -->
					<!-- S tableBot -->
					<div class="clearfix pad20">
						<%--<!-- S button -->
						<div class="clearfix fl">
							<a href="#" onclick="batchBtn()" data-toggle="modal" class="ser-btn l">批量到站</a>
						</div>--%>
						<!-- E button -->
						<!-- S page -->
						<div id="pagin"></div>
						<!-- E page -->
					</div>
					<!-- E tableBot -->
				</div>

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


<script type="text/javascript">


	//var flag = true;
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
	$("input[type='checkbox']").iCheck({
		checkboxClass : 'icheckbox_square-blue'
	});

	$("#selectAll").on('ifUnchecked', function() {
		$("input[type='checkbox']", "#orderTable").iCheck("uncheck");
	}).on('ifChecked', function() {
		$("input[type='checkbox']", "#orderTable").iCheck("check");
	});

	//显示分页条
	var pageStr = paginNav('${orderHoldPageModel.pageNo}', '${orderHoldPageModel.totalPages}', '${orderHoldPageModel.totalCount}');
	$("#pagin").html(pageStr);


	/*var option = "";
	  var userList='${userList}';

	$.each(userList, function (n, user) {
		alert(user);
		option += "<option id='"+user.id+"'>" + user.realName + "</option>";

	});*/
	/*$("#embraceId").html(option);*/
	//加载带有查询条件的指定页的数据
	function gotoPage(pageIndex) {
		var embraceId = $("#embrace option:selected").val();
		var orderSetStatus = $('#orderSetStatus option:selected').val();
		$.ajax({
			type : "GET",  //提交方式
			url : "<c:url value="/holdToStoreController/getList" />",//路径
			data : {
				"pageIndex" : pageIndex,
				"orderSetStatus" : orderSetStatus,
				"embraceId" : embraceId
			},//数据，这里使用的是Json格式进行传输
			success : function(dataObject) {//返回数据根据结果进行相应的处理
				var tbody = $("#dataList");
				// 清空表格数据
				//tbody.html("");
				var dataList = dataObject.datas;

				if(dataList != null){
					var datastr = "";
					for(var i = 0; i < dataList.length; i++){
						datastr += getRowHtml(dataList[i]);
					}

					tbody.html(datastr);
				}
				//更新分页条
				var pageStr = paginNav(pageIndex,  dataObject.totalPages, dataObject.totalCount);
				$("#pagin").html(pageStr);
			},
			error : function() {
				ioutDiv("加载分页数据异常！");
			}
		});
	}


	//封装一行的数据
	function getRowHtml(data){
		var row = "<tr>";
		if(data!=null){
		row +=  "<td>"+data.userName+"</td>";
		row +=  "<td>" + data.phone + "</td>";
		row += "<td>" + data.mailNum + "</td>";
		row += "<td>" + data.recieverName + "</td>";
		row += "<td>" + data.recieverPhone + "</td>";
		row += "<td class='tl'>" + data.recieverAddress +"</td>";

			if(data.orderSetStatus=="<%=OrderSetStatus.NOEMBRACE%>"){
				row += "<td>" + "<%=OrderSetStatus.NOEMBRACE.getMessage()%>" + "</td>";
			}

			else if(data.orderSetStatus=="<%=OrderSetStatus.WAITTOIN%>"){
				row += "<td>" +"<%=OrderSetStatus.WAITTOIN.getMessage()%>" + "</td>";
			}
			else if(data.orderSetStatus=="<%=OrderSetStatus.WAITSET%>"){
				row += "<td>" + "<%=OrderSetStatus.WAITSET.getMessage()%>" + "</td>";
			}
			else if(data.orderSetStatus=="<%=OrderSetStatus.WAITDRIVERGETED%>"){
				row += "<td>" + "<%=OrderSetStatus.WAITDRIVERGETED.getMessage()%>" + "</td>";
			}
			else if(data.orderSetStatus=="<%=OrderSetStatus.DRIVERGETED%>"){
				row += "<td>" + "<%=OrderSetStatus.DRIVERGETED.getMessage()%>" + "</td>";
			}
			else if(data.orderSetStatus=="<%=OrderSetStatus.ARRIVEDISPATCH%>"){
				row += "<td>" + "<%=OrderSetStatus.ARRIVEDISPATCH.getMessage()%>" + "</td>";
			}
			else if(data.orderSetStatus=="<%=OrderSetStatus.WAITDISPATCHSET%>"){
				row += "<td>" + "<%=OrderSetStatus.WAITDISPATCHSET.getMessage()%>" + "</td>";
			}
			else if(data.orderSetStatus=="<%=OrderSetStatus.WAITDRIVERTOSEND%>"){
				row += "<td>" + "<%=OrderSetStatus.WAITDRIVERTOSEND.getMessage()%>" + "</td>";
			}
			else if(data.orderSetStatus=="<%=OrderSetStatus.DRIVERSENDING%>"){
				row += "<td>" + "<%=OrderSetStatus.DRIVERSENDING.getMessage()%>" + "</td>";
			}
			else if(data.orderSetStatus=="<%=OrderSetStatus.ARRIVED%>"){
				row += "<td>" + "<%=OrderSetStatus.ARRIVED.getMessage()%>" + "</td>";
			}

		}else {
			 row+="<td colspan='7'>"+没有符合查询条件的数据+"</td>"
		}
		row += "</tr>";
		return row;
	}


	//查询按钮事件
	function searchOrder(){
		var statusId=$("#status").val();
		var  embraceId =$("#embraceId").val();
		gotoPage(0);
	}
	//历史未到站事件
	function historyNotArri(){
		$("#parcelCode").val("");
		$("#mailNum").val("");
		$("#between").val("");
		$('#arriveStatus').val(<%=ArriveStatus.NOTARR.getStatus()%>);
		gotoPage(0);
	}
	//今日成功接单数
	function todaySuccessOrder(){
		$('#orderSetStatus').val(<%=OrderSetStatus.NOEMBRACE.getStatus()%>);
		gotoPage(0);
	}
	//今日已入库
	function todayToStore(){
		$('#orderSetStatus').val(<%=OrderSetStatus.WAITSET.getStatus()%>);
		gotoPage(0);
	}
	//回车事件
	function enterPress(e){
		if(!e) e = window.event;//火狐中是 window.event
		if((e.keyCode || e.which) == 13){
			if($("#mailNum").val()!=null && $("#mailNum").val()!=""){
				checkOrderByMailNum1($("#mailNum").val());
			}
		}
	}

	var isBatchToSite = false; //单个运单到站
	//检查运单号
	function checkOrderByMailNum1(mailNum){
		if(mailNum!=""&& mailNum!=undefined&&mailNum!=null){
			console.log("mailNum======" + mailNum);
			$.ajax({
				url: "<%=request.getContextPath()%>/holdToStoreController/checkHoldToStoreByMailNum?mailNum="+mailNum ,
				type: 'GET',
				cache: false,
				success: function(response){
					alert(response);
                      $("#mailNum").val(response.mailNum);
					alert(response.order.orderSetStatus);
					if(response.order!=null &&  response.order!=""){
						console.log("aa");
						if((response.order.orderSetStatus!='NOEMBRACE'|| response.order.orderSetStatus!='WAITTOIN' )&& response.order.orderSetStatus!=null){
							console.log("ee");
							$("#mailNumP").html("重复扫描，此运单已经扫描过啦");
							$("#mailNumP").attr("style","color:red");
						}else{
							if(response.areaCode==response.order.areaCode){
								console.log("bb");
								$("#popContent").html("扫描成功，完成⼊库。此订单属于您的站点，可直接进⾏【运单分派】操作");
								$("#toSitePrompt").modal("show");
								isBatchToSite = false;//单个运单执行到站
								doToSite();
							}else if(response.areaCode!=response.order.areaCode){
								if(response.site.type=="1"){//为分拨站点
									console.log("cc");
									$("#popContent").html("扫描成功，完成⼊库。请到App中进⾏【分拣】操作");
									$("#toSitePrompt").modal("show");
								}else{
									console.log("dd");
									//不为分拨站点
									$("#popContent").html("扫描成功，完成⼊库。请到App中进⾏【揽件集包】操作");
									$("#toSitePrompt").modal("show");
								}
							}
						}
					}else{

						$("#mailNumP").html("【异常扫描】不存在此运单号") ;
						$("#mailNumP").attr("style","color:red");
					}
				},
				error: function(){
					console.log("error======");
					$("#mailNumP").html("【异常扫描】不存----在此运单号") ;
					$("#mailNumP").attr("style","color:red");
				}
			});
		}
	}
	//取消到站操作
	function cancelToSite(){
		$("#toSitePrompt").modal("hide");
	}
	//做到站操作
	function doToSite(){
		if(isBatchToSite){//批量到站
			doBatchToSite()();
		}else{//单个运单到站
			$("#mailNumP").html("");
			$("#mailNumP").attr("style","display:none");
			gotoPage1(0,$("#parcelCode").val(),$("#mailNum").val());
			$("#toSitePrompt").modal("hide");
		}

	}

	//加载带有查询条件的指定页的数据
	function gotoPage1(pageIndex,parcelCode,mailNum) {
		var between = $("#between").val();
		var arriveStatus = $('#arriveStatus option:selected').val();
		$("input[type='checkbox']", "#orderTable").iCheck("uncheck");
		$.ajax({
			type : "GET",  //提交方式
			url : "<%=request.getContextPath()%>/packageToSite/getOrderPage",//路径
			data : {
				"pageIndex" : pageIndex,
				"arriveStatus" : arriveStatus,
				"between" : between,
				"parcelCode" : parcelCode,
				"mailNum" : mailNum
			},//数据，这里使用的是Json格式进行传输
			success : function(dataObject) {//返回数据根据结果进行相应的处理
				var tbody = $("#dataList");
				// 清空表格数据
				tbody.html("");

				var dataList = dataObject.datas;
				if(dataList != null){
					var datastr = "";
					for(var i = 0; i < dataList.length; i++){
						datastr += getRowHtml(dataList[i]);
					}
					tbody.html(datastr);
				}
				//更新分页条
				var pageStr = paginNav(pageIndex,  dataObject.totalPages, dataObject.totalCount);
				$("#pagin").html(pageStr);
				$("input[type='checkbox']").iCheck({
					checkboxClass : 'icheckbox_square-blue'
				});
				$("#selectAll").on('ifUnchecked', function() {
					$("input[type='checkbox']", "#orderTable").iCheck("uncheck");
				}).on('ifChecked', function() {
					$("input[type='checkbox']", "#orderTable").iCheck("check");
				});
				updateOrderNumVO();
			},
			error : function() {
				ioutDiv("加载分页数据异常！");
			}
		});
	}

	//更新数据统计的数据
	function updateOrderNumVO(){
		$.ajax({
			url: '<%=request.getContextPath()%>/packageToSite/updateOrderNumVO',
			type: 'GET',
			cache: false,
			dataType: "json",
			data: {},
			success: function(response){
				if(response!=null &&  response!="") {
					$("#non_arrival_num").html(response.noArrive);
					$("#history_non_arrival_num").html(response.noArriveHis);
					$("#arrived_num").html(response.arrived);
				}
			},
			error: function(){
			}
		});
	}
	//检查包裹号
	function checkOrderParcelByParcelCode(parcelCode){
		if(parcelCode!=""){
			$.ajax({
				url: '<%=request.getContextPath()%>/packageToSite/checkOrderParcelByParcelCode?parcelCode='+parcelCode,
				type: 'GET',
				cache: false,
				dataType: "text",
				data: {},
				success: function(response){
					if(response=="false"){
						$("#parcelCodeP").html("【异常扫描】包裹号不存在") ;
						$("#parcelCodeP").attr("style","color:red");
					}else{
						$("#parcelCodeP").html("扫描成功，请扫描运单号");
						$("#parcelCodeP").attr("style","color:red");
						gotoPage(0,$("#parcelCode").val(),$("#mailNum").val());
					}
				},
				error: function(){
					$("#parcelCodeP").html("【异常扫描】不存在此包裹号");
					$("#parcelCodeP").attr("style","color:red");
				}
			});
		}
	}

	var ids = [];
	//批量到站button
	function batchBtn(){
		var checkids = $('input[name="id"]:checked');
		ids = [];
		if(checkids.length) {
			$.each(checkids, function(i, n){
				if(!$(n).closest('tr').find('.status .label').hasClass('label-success')) {
					ids.push($(n).val());
				}
			});
		}
		if(ids.length>0){
			var url = "<c:url value="/packageToSite/isAllDriverGeted?${_csrf.parameterName}=${_csrf.token}" />";
			$.ajax({
				url: url,
				type: 'POST',
				cache: false,
				dataType: "json",
				data: {
					"ids" : JSON.stringify(ids)
				},
				success: function(data){
					if(data){//没有出现物流状态为异常的订单
						$("#popContent").html("确认批量到站？<br>该操作会把选中的订单设置为已到站。");
						$("#toSitePrompt").modal("show");
						isBatchToSite = true;//批量到站
					}else{
						$("#popContent").html("确认批量到站？<br>运单未进行分拣、司机取货等操作，该操作会把选中的订单设置为已到站。");
						$("#toSitePrompt").modal("show");
						isBatchToSite = true;//批量到站
					}
				},
				error: function(){
					ioutDiv('服务器繁忙，请稍后再试！');
				}
			});
		}else{
			ioutDiv('请选择运单！');
		}
	}
	function doBatchToSite(){
		if(ids.length>0){
			var url = "<c:url value="/packageToSite/arriveBatch?${_csrf.parameterName}=${_csrf.token}" />";
			$.ajax({
				url: url,
				type: 'POST',
				cache: false,
				dataType: "json",
				data: {
					"ids" : JSON.stringify(ids)
				},
				success: function(json){
					location.reload();
				},
				error: function(){
					ioutDiv('服务器繁忙，请稍后再试！');
				}
			});
		}else{
			ioutDiv('请选择运单！');
		}
	}
</script>
</body>
</html>
