<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.OrderStatus" %>
<%@ page import="java.util.List" %>
<%@ page import="com.bbd.saas.utils.Dates" %>
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
%>
<body >
<div>
</div>
<section class="content">
	<div class="col-xs-12">
		<!-- 订单数显示 结束   -->
		<div class="box-body">
					
			<div class="row">
				<div class="col-xs-3">
					<label>状态：</label>
					<select id="status" name="status" class="form-control">
						<%=OrderStatus.Srcs2HTML(-1)%>
					</select>
				</div>
				<div class="col-xs-3">
					<label>到站时间：</label>
					<input id="arriveBetween" name="arriveBetween" type="text" class="form-control" placeholder="请选择到站时间" value="${arriveBetween}"/>
				</div>
				<div class="col-xs-3">
					<label>运单号：</label>
					<input id="mailNum" name="mailNum" type="text"  class="form-control" placeholder="请输入运单号"/>
				</div>
			</div>
			<div class="row">
				<button class="btn btn-primary" style="margin-top:10px ; margin-left: 15px ;" onclick="gotoPage(0);">查询</button>
				<button class="btn btn-primary" style="margin-top:10px ; margin-left: 15px ;" onclick="exportData();">导出</button>
				<!-- 用于导出 -->
				<form action="<%=path%>/dataQuery/exportToExcel" method="post" id="exptForm">
					<input id="status_expt" name="status" type="hidden" />
					<input id="arriveBetween_expt" name="arriveBetween_expt" type="hidden" />
					<input id="mailNum_expt" name="mailNum" type="hidden" />
				</form>
			</div>
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
					PageModel<Order> orderPage = (PageModel<Order>)request.getAttribute("orderPage");
					if(orderPage.getDatas() == null){
				%>
					<tr>
						<td colspan="7">没有符合查询条件的数据</td>
					</tr>
				<%
					}else{
						for(Order order : orderPage.getDatas()){
				%>
					<tr>
						<td><%=order.getParcelCode()%></td>
						<td><%=order.getMailNum()%></td>
						<td><%=order.getOrderNo()%></td>
						<%
							if(order.getSrc() == null){//来源
						%>
								<td></td>
						<%
							}else{
						%>
								<td><%=order.getSrc().getMessage()%></td>
						<%
							}
						%>
						<td><%=order.getReciever().getName()%></td>
						<td><%=order.getReciever().getPhone()%></td>
						<td><%=order.getReciever().getProvince()%> <%=order.getReciever().getCity()%> <%=order.getReciever().getArea()%> <%=order.getReciever().getAddress()%></td>
						<td>待增加字段<%=Dates.formatDateTime_New(order.getDatePrint())%></td>
						<td><%=Dates.formatDate2(order.getDateMayArrive())%></td>
						<td><%=Dates.formatDateTime_New(order.getDateArrived())%></td>
						<%
							if(order.getUser() == null){//未分派
						%>
								<td></td>
								<td></td>
						<%
							}else{
						%>
								<td><%=order.getUser().getRealName()%></td>
								<td><%=order.getUser().getPhone()%></td>
						<%
							}
							if(order.getOrderStatus() == null){//未到站
						%>
								<td>未到站</td>
						<%
							}else{
						%>
								<td><%=order.getOrderStatus().getMessage()%></td>
						<%
							}
						%>
					</tr>
				<%
					}//for
				}//else
				%>
				</tbody>
			</table>
			
			<!--页码 start-->
			<div id="pagin"></div>	
			<!--页码 end-->
			
		</div>
	</div>
</section>

<script src="<c:url value="/resources/javascripts/timeUtil.js" />"> </script>

<script type="text/javascript">

$(document).ready(function() {
	//显示分页条
	var pageStr = paginNav(<%=orderPage.getPageNo()%>, <%=orderPage.getTotalPages()%>, <%=orderPage.getTotalCount()%>);
	$("#pagin").html(pageStr);
	
	//初始化到站时间框
	$("#arriveBetween").daterangepicker({
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
            "status" : $("#status").val(), 
            "arriveBetween" : $("#arriveBetween").val(), 
            "mailNum" : $("#mailNum").val() 
        },//数据，这里使用的是Json格式进行传输  
        success : function(dataObject) {//返回数据根据结果进行相应的处理 
            //console.log("dataObject==="+dataObject);
            var tbody = $("#dataList");
            
            var dataList = dataObject.datas;
			if(dataList != null){
				var datastr = "";
				for(var i = 0; i < dataList.length; i++){
					datastr += getRowHtml(dataList[i]);
				}
				tbody.html(datastr);
			} 
			//更新分页条
			var pageStr = paginNav(pageIndex, dataObject.totalPages, dataObject.totalCount);
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
	row += "<td>" + getSrcName(data.src) + "</td>";
	row += "<td>" + data.reciever.name + "</td>";
	row += "<td>" + data.reciever.phone + "</td>";
	row += "<td>" + data.reciever.province + data.reciever.city + data.reciever.area + data.reciever.address + "</td>";
	row += "<td>待增加" + getDate1(data.dateArrived) + "</td>";
	row += "<td>" + getDate2(data.dateMayArrive) + "</td>";
	row += "<td>" + getDate1(data.dateArrived) + "</td>";
	//派件员==未分派，不需要显示派件员姓名和电话
	if(data.user == null){
		row += "<td></td><td></td>";
	}else{
		row += "<td>" + data.user.realName + "</td>";
		row += "<td>" + data.user.phone + "</td>";
	}
	//状态
	row += "<td>" + getStatus(data.orderStatus) + "</td>";
	row += "</tr>";	
	return row;
}

//转义状态
function getSrcName(src) {
	if(src == null){
		return "";
	}
    x = "";
	switch (src)
	{
	case "BBT":
	  	x = "棒棒糖";
	 	break;
	case "棒棒糖":
	  	x =  "京东";
	  	break;
	case "TAOBAO":
	  	x =  "淘宝";
	  	break;
	case "TIANMAO":
	  x =  "天猫";
	  break; 
	case "BAIDUWAIMAI":
	  	x =  "百度外卖";
	  	break;
	case "PINHAOHUO":
	  	x =  "拼好货";
	  	break;
	case "OTHERS":
	  	x =  "其他";
	  	break;
	default : 
		//x = "棒棒糖";
		x = src;
	}
	return x;
}

//转义状态
function getStatus(status) {
	if(status == null){
		return "未到站";
	}
    x = "未到站";
	switch (status)
	{
	case "NOTARR":
	  	x = "未到站";
	 	break;
	case "NOTDISPATCH":
	  	x =  "未分派";
	  	break;
	case "DISPATCHED":
	  	x =  "已分派";
	  	break;
	case "RETENTION":
	  x =  "滞留";
	  break; 
	case "REJECTION":
	  	x =  "拒收";
	  	break;
	case "SIGNED":
	  	x =  "已签收";
	  	break;
	case "TO_OTHER_EXPRESS":
	  	x =  "已转其他快递";
	  	break;
	case "APPLY_RETURN":
	  	x =  "申请退货";
	  	break;
	case "RETURNED":
	  	x =  "退货完成";
	  	break; 	
	default : 
		//x = "未到站";
		x = status;
	}
	return x;
}	

//导出数据
function exportData() {
	$("#status_expt").val($("#status").val());
	$("#arriveBetween_expt").val($("#arriveBetween").val());
	$("#mailNum_expt").val($("#mailNum").val());
	$("#exptForm").submit();
}	
	
	
	
</script>
</body>
</html>