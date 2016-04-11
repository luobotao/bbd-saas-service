<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.ArriveStatus" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<jsp:include page="../main.jsp" flush="true" />
</head>
<body >
<div>
</div>
<section class="content">
	<div class="col-xs-12">
		<!-- 订单数显示 开始 -->
		<div class="row">
			<div class="col-xs-3"><span>${non_arrival_num}</span><br><span>今日未到站订单数</span></div>
			<div class="col-xs-3"><span>${history_non_arrival_num}</span><br><span>历史未到站订单数</span></div>
			<div class="col-xs-3"><span>${arrived_num}</span><br><span>今日已到站订单数</span></div>
		</div>
	</div>
	<div class="col-xs-12">
		<!-- 订单数显示 结束   -->
		<div class="box-body">
			<form action="?" method="get" id="searchOrderForm" name="searchOrderForm">
				<div class="row">
					<div class="col-xs-3">
						<label>状态：</label>
						<select id="src" name="src" class="form-control">
							<%=ArriveStatus.Srcs2HTML(-1)%>
						</select>
					</div>
					<div class="col-xs-3">
						<label>预计到站时间：</label>
						<input id="between" name="between" type="text" class="form-control" placeholder="请选择预计到站时间" value=""/>
					</div>
					<div >
						<button class="btn btn-primary" style="margin-top:10px ; margin-left: 15px ;" type="submit">查询</button>

					</div>
				</div>
			</form>
		</div>
	</div>
	<div class="col-xs-12">
		<div class="m20">
			<span>扫描包裹号：<input id="packageId" name="packageId" type="text" /></span>
			<span class="pl20">扫描运单号：<input id="waybillId" name="waybillId" type="text" /></span><br><br>
			<span class=""><input id="batchToSite" name="batchToSite" type="button" value="批量到站"/></span>
		</div>
		<div class="box-body table-responsive">
			<table id="orderTable" class="table table-bordered table-hover">
				<thead>
					<tr>
						<td>选择</td>
						<td>包裹号</td>
						<td>运单号</td>
						<td>订单号</td>
						<td>来源</td>
						<td>收货人</td>
						<td>收货人电话</td>
						<td>地址</td>
						<td>库房打单时间</td>
						<td>预计到站时间</td>
						<td>状态</td>
					</tr>
				</thead>
				<tbody>
				<%
					PageModel<Order> orderPage = (PageModel<Order>)request.getAttribute("orderPage");
					for(Order order : orderPage.getDatas()){
				%>
				<tr>
					<td><input type="checkbox" value="<%=order.getId()%>" name="id"></td>
					<td><%=order.getAreaName()%></td>
					<td><%=order.getMailNum()%></td>
					<td><%=order.getOrderNo()%></td>
					<td><%=order.getSrc()%></td>
					<td><%=order.getReciever().getName()%></td>
					<td><%=order.getReciever().getPhone()%></td>
					<td><%=order.getReciever().getProvince()%> <%=order.getReciever().getCity()%> <%=order.getReciever().getArea()%> <%=order.getReciever().getAddress()%></td>
					<td><%=order.getDatePrint()%></td>
					<td>2016-04-06</td>
					<td>未到站</td>
				</tr>
				<%
					}
				%>
				</tbody>
				<tfoot>
					<th colspan="13">
						<input id="selectAll" name="selectAll" type="checkbox"> <label for="selectAll">
						全选</label> &nbsp;
					</th>
				</tfoot>
			</table>
			<%
				if(orderPage.getDatas().size()>0){
			%>
			<div>
				<div class="col-xs-6">
					<div class="dataTables_info" id="userTable_info">页码：
						<%=(orderPage.getPageNo()+1)%>/<%=orderPage.getTotalPages()%> 共计<%=orderPage.getTotalCount()%>条记录</div>
				</div>
				<div >
					<div class="dataTables_paginate paging_bootstrap">
						<ul class="pagination">
							<%
								if(orderPage.getPageNo()<1){
							%>
							<li class="prev disabled"><a href="javascript:">首页</a></li>
							<%
							}else{
							%>
							<li class="prev"><a href="@searchParam()page=0">首页</a></li>
							<%
								}
								for(int i=0;i<orderPage.getTotalPages()-1;i++){
									if(orderPage.getTotalPages()<8){
							%>
							<li class="@if(index == formPage.page) {active}"><a href="@searchParam()page=@index">@(index + 1)</a></li>
							<%
							}else {
								if(orderPage.getPageNo()<7){

									if(i<8){
							%>
							<li class="@if(index == formPage.page) {active}"><a href="@searchParam()page=@index">@(index + 1)</a></li>
							<%
							}else{
								if(i==(orderPage.getTotalPages()-1)){
							%>
							<li class=""><a href="javascript:">...</a></li>
							<%
								}
								if(i==(orderPage.getTotalPages())){
							%>
							<li class="@if(index == formPage.page) {active}"><a href="@searchParam()page=@index">@(index + 1)</a></li>
							<%
												}
											}

										}else{


										}

									}

								}
							%>

							@for( index <- 0 to (pages - 1)) {
							@if(pages < 8) {
							<li class="@if(index == formPage.page) {active}"><a href="@searchParam()page=@index">@(index + 1)</a></li>
							} else {
							@if(formPage.page < 7) {
							@if(index < 8) {
							<li class="@if(index == formPage.page) {active}"><a href="@searchParam()page=@index">@(index + 1)</a></li>
							} else {
							@if(index == (pages - 1)) {
							<li class=""><a href="javascript:">...</a></li>
							}
							@if(index == (pages - 1)) {
							<li class="@if(index == formPage.page) {active}"><a href="@searchParam()page=@index">@(index + 1)</a></li>
							}
							}
							} else {
							@if(formPage.page < (pages - 4)) {
							@if(index == 0 || index > (formPage.page - 4) && index < (formPage.page + 5)) {
							<li class="@if(index == formPage.page) {active}"><a href="@searchParam()page=@index">@(index + 1)</a></li>
							} else {
							@if(index == 2) {
							<li class=""><a href="javascript:">...</a></li>
							}
							@if(index == (pages - 1)) {
							<li class=""><a href="javascript:">...</a></li>
							}
							@if(index == pages) {
							<li class="@if(index == formPage.page) {active}"><a href="@searchParam()page=@index">@(index + 1)</a></li>
							}
							}
							} else {
							@if(index == 0 || index > (pages - 8)) {
							<li class="@if(index == formPage.page) {active}"><a href="@searchParam()page=@index">@(index + 1)</a></li>
							} else {
							@if(index == 2) {
							<li class=""><a href="javascript:">...</a></li>
							}
							}
							}
							}
							}
							}
							<%
								if(orderPage.getTotalPages()==orderPage.getPageNo()){
							%>
							<li class="next disabled"><a href="javascript:">尾页</a></li>
							<%
								}else{
							%>
							<li class="next"><a href="@searchParam()page=@{ pages - 1}">尾页</a></li>
							<%
								}
							%>

						</ul>
					</div>
				</div>
			</div>
			<%
				}
			%>

		</div>
	</div>
</section>



<script type="text/javascript">
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
</script>
</body>
</html>