<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.ArriveStatus" %>
<%@ page import="com.bbd.saas.enums.OrderStatus" %>
<%@ page import="com.bbd.saas.utils.Dates" %>
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
					<li class="b-board-card col-xs-12 col-sm-6 col-md-4 col-lg-4">
						<dl class="arrive-status  c1">
							<dt class="b-order" id="non_arrival_num">${non_arrival_num}</dt>
							<dd>今日未到站 </dd>
							<dd>订单数</dd>
						</dl>
					</li>
					<li class="b-board-card col-xs-12 col-sm-6 col-md-4 col-lg-4">
						<dl class="arrive-status c2">
							<dt class="b-order" id="history_non_arrival_num">${history_non_arrival_num}</dt>
							<dd>历史未到站 </dd>
							<dd>订单数</dd>
						</dl>
					</li>
					<li class="b-board-card col-xs-12 col-sm-6 col-md-4 col-lg-4">
						<dl class="arrive-status c3">
							<dt class="b-order"id="arrived_num">${arrived_num}</dt>
							<dd>今日已到站 </dd>
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
								<label>状态：　　　</label>
								<select id="arriveStatus" name="arriveStatus" class="form-control form-con-new">
									<%=ArriveStatus.Srcs2HTML(-1)%>
								</select>
							</div>
							<div class="form-group col-xs-12 col-sm-6 col-md-5 col-lg-5">
								<label>预计到站时间：</label>
								<input id="between" name="between" type="text" class="form-control c-disable" placeholder="请选择预计到站时间" value="${between}"/>
							</div>
							<div class="form-group col-xs-12 col-sm-6 col-md-3 col-lg-3">
								<a href="javascript:void(0)" class="ser-btn l" onclick="searchOrder()"><i class="b-icon p-query p-ser"></i>查询</a>
							</div>
						</div>
						<div class="row pb20">
							<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
								<label>扫描包裹号：</label>
								<input id="parcelCode" name="parcelCode" class="form-control" type="text" onkeypress="enterPress(event)" />
								<p class="help-block" id="parcelCodeP" style="display:none;"></p>
							</div>
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
								<th width="4%"><input type="checkbox"  id="selectAll" name="selectAll" class="j-sel-all" /></th>
								<th>包裹号</th>
								<th>运单号</th>
								<th>订单号</th>
								<th>来源</th>
								<th>收货人</th>
								<th>收货人电话</th>
								<th width="10%">地址</th>
								<th>打单时间</th>
								<th>预计到站时间</th>
								<th>状态</th>
							</tr>
							</thead>
							<tbody id="dataList">
							<%
								PageModel<Order> orderPage = (PageModel<Order>)request.getAttribute("orderPage");
								for(Order order : orderPage.getDatas()){
							%>
							<tr>
								<td><input type="checkbox" value="<%=order.getMailNum()%>" name="id"></td>
								<td><%=order.getParcelCode()%></td>
								<td><%=order.getMailNum()%></td>
								<td><%=order.getOrderNo()%></td>
								<td><%=order.getSrc().getMessage()%></td>
								<td><%=order.getReciever().getName()%></td>
								<td><%=order.getReciever().getPhone()%></td>
								<td class="tl"><%=order.getReciever().getProvince()%> <%=order.getReciever().getCity()%> <%=order.getReciever().getArea()%> <%=order.getReciever().getAddress()%></td>
								<td><%=Dates.formatDateTime_New(order.getDatePrint())%></td>
								<td><%=Dates.formatDate2(order.getDateMayArrive())%></td>
								<%
									if(order.getOrderStatus()==OrderStatus.NOTARR || order.getOrderStatus()==null){
								%>
								<td class="orange"><%=ArriveStatus.NOTARR.getMessage()%></td>
								<%
								}else{
								%>
								<td class="c-green"><%=ArriveStatus.ARRIVED.getMessage()%></td>
								<%
									}
								%>
							</tr>
							<%
								}
							%>
							</tbody>
							<tfoot>
								<div class="j-pl-pop modal fade" id="batchToSite" tabindex="-1" role="dialog" aria-hidden="true">
									<div class="modal_wrapper">
										<div class="modal-dialog b-modal-dialog">
											<div class="modal-content">
												<div class="modal-header b-modal-header">
													<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
													<h4 class="modal-title  tc" id="activeUserLabel">确认信息</h4>
												</div>
												<div class="modal-body b-modal-body">
													确认批量到站？<br>
													该操作会把选中的订单设置为已到站。
												</div>
												<div class="modal-footer">
													<button type="button" class="ser-btn g" data-dismiss="modal">取消</button>
													<button id="enableSelect" type="button" class="ser-btn l">确认</button>
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
						<!-- S button -->
						<div class="clearfix fl">
							<a href="#" data-toggle="modal" data-target="#batchToSite" class="ser-btn l">批量到站</a>
						</div>
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
	var flag = true;
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
	var pageStr = paginNav(<%=orderPage.getPageNo()%>, <%=orderPage.getTotalPages()%>, <%=orderPage.getTotalCount()%>);
	$("#pagin").html(pageStr);



	//加载带有查询条件的指定页的数据
	function gotoPage(pageIndex,parcelCode,mailNum) {
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
				alert("加载分页数据异常！");
			}
		});
	}


	//封装一行的数据
	function getRowHtml(data){
		var row = "<tr>";
		row +=  "<td><input type='checkbox' value='" + data.mailNum + "' name='id'></td>";
		row +=  "<td>" + data.parcelCode + "</td>";
		row += "<td>" + data.mailNum + "</td>";
		row += "<td>" + data.orderNo + "</td>";
		row += "<td>" + data.srcMessage + "</td>";
		row += "<td>" + data.reciever.name + "</td>";
		row += "<td>" + data.reciever.phone + "</td>";
		row += "<td class='tl'>" + data.reciever.province + data.reciever.city + data.reciever.area + data.reciever.address + "</td>";
		row += "<td>" + getDate1(data.datePrint) + "</td>";
		row += "<td>" + getDate2(data.dateMayArrive) + "</td>";
		if(data.orderStatus=="<%=OrderStatus.NOTARR%>" || data.orderStatus==null){
			row += "<td class='orange'>" + "<%=ArriveStatus.NOTARR.getMessage()%>" + "</td>";
		}else{
			row += "<td class='c-green'>" + "<%=ArriveStatus.ARRIVED.getMessage()%>" + "</td>";
		}
		row += "</tr>";
		return row;
	}


	//查询按钮事件
	function searchOrder(){
		$("#parcelCode").val("");
		$("#mailNum").val("");
		gotoPage(0);
	}
	//回车事件
	function enterPress(e){
		if(!e) e = window.event;//火狐中是 window.event
		if((e.keyCode || e.which) == 13){
			if($("#mailNum").val()!=null && $("#mailNum").val()!=""){
				$("#parcelCode").val("");
				checkOrderByMailNum($("#mailNum").val());
			}
			if($("#parcelCode").val()!=null && $("#parcelCode").val()!=""){
				checkOrderParcelByParcelCode($("#parcelCode").val());
			}
		}
	}

	//检查运单号
	function checkOrderByMailNum(mailNum){
		if(mailNum!=""){
			$.ajax({
				url: '<%=request.getContextPath()%>/packageToSite/checkOrderByMailNum?mailNum='+mailNum,
				type: 'GET',
				cache: false,
				dataType: "json",
				data: {},
				success: function(response){
					if(response!=null &&  response!=""){
						if(response.orderStatus!="NOTARR" && response.orderStatus!=null){
							$("#mailNumP").html("重复扫描，此运单已经扫描过啦");
							$("#mailNumP").attr("style","color:red");
						}else{
							$("#mailNumP").html("");
							$("#mailNumP").attr("style","display:none");
							gotoPage(0,$("#parcelCode").val(),$("#mailNum").val());
						}
					}else{
						$("#mailNumP").html("【异常扫描】不存在此运单号") ;
						$("#mailNumP").attr("style","color:red");
					}
				},
				error: function(){
					$("#mailNumP").html("【异常扫描】不存在此运单号") ;
					$("#mailNumP").attr("style","color:red");
				}
			});
		}
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


	//批量到货确认button
	$('#enableSelect').on('click', confirmFunction);

	function confirmFunction(){
		var checkids = $('input[name="id"]:checked');
		var ids = [];
		var flag=true;
		if(checkids.length) {
			$.each(checkids, function(i, n){
				if(!$(n).closest('tr').find('.status .label').hasClass('label-success')) {
					ids.push($(n).val());
				}
			});
		}
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
					alert('服务器繁忙，请稍后再试！');
				}
			});
		}else{
			alert('请选择运单！');
		}


	}
</script>
</body>
</html>
