<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.OrderStatus" %>
<%@ page import="com.bbd.saas.enums.ArriveStatus" %>
<%@ page import="com.bbd.saas.vo.UserVO" %>
<%@ page import="com.bbd.saas.vo.Reciever" %>
<%@ page import="com.bbd.saas.mongoModels.User" %>
<%@ page import="com.bbd.saas.mongoModels.Site" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<link href="<c:url value="/resources/frame.css" />" rel="stylesheet"  type="text/css" />		
	<jsp:include page="../main.jsp" flush="true" />
</head>
<%

	String proPath = request.getContextPath();
	String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+proPath;
	
	PageModel<Order> orderPage = (PageModel<Order>)request.getAttribute("orderPage");
	long count = orderPage.getTotalCount();
	int totalPage = orderPage.getTotalPages();
	int pagesize = orderPage.getPageSize();
	int currentPage = orderPage.getPageNo();
	
%>
<body >
<div>
</div>
<section class="content">
	<div class="col-xs-12">
		<!-- 订单数显示 结束   -->
		<div class="box-body">
			<form action="<%=path%>/dataQuery/exportData" method="post" id="searchOrderForm" name="searchOrderForm">
				<div class="row">
					<div class="col-xs-3">
						<label>状态：</label>
						<select id="status" name="status" class="form-control">
							<%=OrderStatus.Srcs2HTML(-1)%>
						</select>
					</div>
					<div class="col-xs-3">
						<label>到站时间：</label>
						<input id="between" name="between" type="text" class="form-control" placeholder="请选择到站时间" value="${between}"/>
					</div>
					<div class="col-xs-3">
						<label>运单号：</label>
						<input id="mailNum" name="mailNum" type="text"  class="form-control" placeholder="请输入运单号"/>
					</div>
				</div>
				<div class="row">
					<button class="btn btn-primary" style="margin-top:10px ; margin-left: 15px ;" onclick="gotoPage(0);">查询</button>
					<button class="btn btn-primary" style="margin-top:10px ; margin-left: 15px ;" onclick="exportData();">导出</button>
				</div>
			</form>
		</div>
	</div>
	<div class="col-xs-12">
		<div class="box-body table-responsive">
			<table id="orderTable" class="table table-bordered table-hover">
				<thead>
					<tr>
						<td>包裹号</td>
						<td>运单号</td>
						<td>订单号</td>
						<td>来源</td>
						<td>收货人</td>
						<td>收货人电话</td>
						<td>地址</td>
						<td>司机取货时间</td>
						<td>预计到站时间</td>
						<td>到站时间</td>
						<td>派送员</td>
						<td>派送员手机</td>
						<td>状态</td>
					</tr>
				</thead>
				<tbody id="dataList">
				<%
					
					for(Order order : orderPage.getDatas()){
				%>
				<tr>
					<td><%=order.getParcelCode()%></td>
					<td><%=order.getMailNum()%></td>
					<td><%=order.getOrderNo()%></td>
					<td><%=order.getSrc()%></td>
					<td><%=order.getReciever().getName()%></td>
					<td><%=order.getReciever().getPhone()%></td>
					<td><%=order.getReciever().getProvince()%> <%=order.getReciever().getCity()%> <%=order.getReciever().getArea()%> <%=order.getReciever().getAddress()%></td>
					<td>司机取货时间2016-04-06</td>
					<td>预计到站时间2016-04-06</td>
					<td>到站时间2016-04-06 15:22:10<%=order.getDatePrint()%></td>
					<td><%=order.getUser().getRealName()%></td>
					<td><%=order.getUser().getPhone()%></td>
					<%
						if(order.getOrderStatus()==null || order.getOrderStatus()==OrderStatus.NOTARR){
					%>
					<td><%=ArriveStatus.NOTARR.getMessage()%></td>
					<%
						}else{
					%>
					<td><%=ArriveStatus.ARRIVED.getMessage()%></td>
					<%
						}
					%>
					
				</tr>
				<%
					}
				%>
				</tbody>
			</table>
			
			<!--页码 start-->
			<div id="pagin"></div>	
			<!--页码 end-->
			
		</div>
	</div>
</section>

<!-- 分页js -->
<script src="<c:url value="/resources/javascripts/page/pageBar3.js" />"> </script>

<script type="text/javascript">

$(document).ready(function() {
	//显示分页条
	var pageStr = paginNav(<%=currentPage%>, <%=totalPage%>, <%=count%>);
	$("#pagin").html(pageStr);
	//到站时间
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

});

//加载带有查询条件的指定页的数据
function gotoPage(pageIndex) {
	//查询所有派件员
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/dataQuery/getList",//路径  
        data : {  
            "pageIndex" : pageIndex,
            "status" : -1, 
            "courierId" : ""
            /* "status" : $("#status").val(), 
            "between" : $("#between").val(), 
            "courierId" : $("#courierId").val(), */ 
        },//数据，这里使用的是Json格式进行传输  
        success : function(dataObject) {//返回数据根据结果进行相应的处理 
            console.log("dataObject==="+dataObject);
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
			var pageStr = paginNav(pageIndex, <%=totalPage%>, <%=count%>);
			console.log("pageIndex===" + pageIndex + "  totalPage===" + <%=totalPage%> + "  count===" + <%=count%>);
			$("#pagin").html(pageStr);
		},
        error : function() {  
           	alert("加载分页数据异常！");  
      	}    
    });	
}	


//封装一行的数据
function getRowHtml(data){
	var row = "<tr>";
	row +=  "<td>" + data.parcelCode + "</td>";
	row += "<td>" + data.mailNum + "</td>";
	row += "<td>" + data.orderNo + "</td>";
	row += "<td>" + data.src + "</td>";
	row += "<td>" + data.reciever.name + "</td>";
	row += "<td>" + data.reciever.phone + "</td>";
	row += "<td>" + data.reciever.province + data.reciever.city + data.reciever.area + data.reciever.address + "</td>";
	row += "<td>司机取货时间2016-04-03 15:2:23 " + data.datePrint + "</td>";
	row += "<td>预计到站时间2016-04-06 15:2:23 " + data.src + "</td>";
	row += "<td>到站时间2016-04-06 15:2:23 " + data.src + "</td>";
	row += "<td>" + data.user.realName + "</td>";
	row += "<td>" + data.user.phone + "</td>";
	if(data.orderStatus=="<%=OrderStatus.NOTARR%>" || data.orderStatus==null){
		row += "<td>" + "<%=ArriveStatus.NOTARR.getMessage()%>" + "</td>";
	}else{
		row += "<td>" + "<%=ArriveStatus.ARRIVED.getMessage()%>" + "</td>";
	}
	row += "</tr>";	
	return row;
}

//导出数据
function exportData() {
	
}	
	
	
	
</script>
</body>
</html>