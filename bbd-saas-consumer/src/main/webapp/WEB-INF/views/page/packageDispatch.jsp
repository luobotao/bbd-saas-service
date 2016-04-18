<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.OrderStatus" %>
<%@ page import="com.bbd.saas.enums.DispatchStatus" %>
<%@ page import="com.bbd.saas.vo.UserVO" %>
<%@ page import="com.bbd.saas.vo.Reciever" %>
<%@ page import="com.bbd.saas.mongoModels.User" %>
<%@ page import="com.bbd.saas.mongoModels.Site" %>
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
						<%=DispatchStatus.Srcs2HTML(-1)%>
					</select>
				</div>
				<div class="col-xs-3">
					<label>到站时间：</label>
					<input id="arriveBetween" name="arriveBetween" type="text" class="form-control" placeholder="请选择到站时间" value="${arriveBetween}"/>
				</div>
				<div >
					<button class="btn btn-primary" style="margin-top:10px ; margin-left: 15px ;" onclick="gotoPage(0);">查询</button>
				</div>
			</div>
		</div>
	</div>
	<div class="col-xs-12">
		<div class="row">
			<div class="col-xs-3">
				<button onclick="showCourierDiv()">选择派件员</button>	
				<span class="ft12 pt20">已选择：<span id="courierName"></span></span>
				<input id="courierId1" type="hidden" value="">
				<input id="courierId" type="text" value="" style="width:200px"/> 	
			</div>
			
			<div class="col-xs-4">
				扫描运单号：<input id="mailNum" name="mailNum" type="text" placeholder="请扫描运单号"/></span>
			    <span class="pl20 ft12" id="mailNum_check"> </span>		
			</div>
		</div>
		
		<div class="box-body table-responsive">
			<table id="orderTable" class="table table-bordered table-hover">
				<thead>
					<tr>
						<td>运单号</td>
						<td>收货人</td>
						<td>收货人地址</td>
						<td>到站时间</td>
						<td>派送员姓名</td>
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
						<td><%=order.getMailNum()%></td>
						<td><%=order.getReciever().getName()%></td>
						<td><%=order.getReciever().getProvince()%> <%=order.getReciever().getCity()%> <%=order.getReciever().getArea()%> <%=order.getReciever().getAddress()%></td>
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
							if(order.getOrderStatus() == OrderStatus.NOTDISPATCH){
						%>
							<td><%=DispatchStatus.NOTDISPATCH.getMessage()%></td>
						<%
							}else{
						%>
							<td><%=DispatchStatus.DISPATCHED.getMessage()%></td>
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

<!-- 选择派件员弹出窗-开始 -->
<div  id="chooseCourier_div" class="popDiv" >
	<div class="title_div">选择派件员</div>
	<div class="m20">
		<span>派件员:
			<select id="courier_select">  
				  
			</select>				  
		</span> 
	</div>
	<div>
		<button onclick="hideCourierDiv()">取消</button>
		<button onclick="chooseCourier()">确定</button>
	</div>
<div>
<!-- 选择派件员弹出窗-结束 -->

<script src="<c:url value="/resources/javascripts/timeUtil.js" />"> </script>

<script type="text/javascript">
var courierIsLoadSuccess = 0;
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
	//初始化派件员下拉框（快递员）
	initCourier();  
	//扫描运单号  focus事件
	$("#mailNum").focus(function(){
		if($("#courierId").val() == null || $("#courierId").val() == ""){
	  		$("#mailNum_check").text("请选择派件员！");
	  	}
	}).blur(function(){//扫描运单号--把快递分派给派件员
		//console.log("开始分派");
		//未选择派件员 
		if($("#courierId").val() == null || $("#courierId").val() == ""){
	  		$("#mailNum_check").text("请选择派件员！");
	  		return ;
	  	}	
	  	//已选择派件员，把快递分派给派件员	 
	  	dispatch();
	  	//console.log("分派完毕");
	});
	//扫描运单号--把快递分派给派件员
	$("#mailNum").on('input',function(e){ 
		
	});
	 

});
// 运单分派  
function dispatch() {  
	$.ajax({
        type : "GET",  //提交方式  
		url : "<%=request.getContextPath()%>/packageDispatch/dispatch",//路径  
		data : {  
		    "mailNum" : $("#mailNum").val(),
		    "courierId" : $("#courier_select").val()  
		},//数据，这里使用的是Json格式进行传输  
		success : function(data) {//返回数据根据结果进行相应的处理  
		    if (data.operFlag == 1) { 
		    	$("#mailNum_check").text("分派成功！");
		    	//刷新列表
		    	refreshTable(data.orderPage);
		    }else if(data.operFlag == 0){
		    	$("#mailNum_check").text("【异常扫描】不存在此订单！");
		    }else if(data.operFlag == 2){
		    	$("#mailNum_check").text("重复扫描，此运单已经分派过啦！");
		    }else if(data.operFlag == 3){
		    	$("#mailNum_check").text("分派失败，请重试！");
		    }else{
		    	$("#mailNum_check").text("运单分派过程中发生不可预料的错误，请稍后再试！");
		    }
		},
		error : function() {  
			alert("服务器繁忙，请稍后再试！");  
		}     
    });
}  

//加载带有查询条件的指定页的数据
function gotoPage(pageIndex) {
	//查询所有派件员
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/packageDispatch/getList",//路径  
        data : {  
            "pageIndex" : pageIndex,
            "status" : $("#status").val(), 
            "arriveBetween" : $("#arriveBetween").val() 
            //"courierId" : $("#courierId").val()
        },//数据，这里使用的是Json格式进行传输  
        success : function(dataObject) {//返回数据根据结果进行相应的处理 
            refreshTable(dataObject);
		},
        error : function() {  
           	alert("加载分页数据异常！");  
      	}    
    });	
}	
//刷新列表--列表数据和分页条
function refreshTable(dataObject){
	//更新列表数据
	var tbody = $("#dataList");
	tbody.html("");
    //var datastr = "";
    var dataList = dataObject.datas;
	if(dataList != null){
		for(var i = 0; i < dataList.length; i++){
			tbody.append(getRowHtml(dataList[i])); 
			//datastr += getRowHtml(dataList[i]);
		}
	} else{
		tbody.append("<tr><td colspan='7'>没有符合查询条件的数据</td></tr>");
		//datastr += "<tr><td colspan='7'>没有符合查询条件的数据</td></tr>";
	}
	//tbody.html(datastr);
	//更新分页条
	var pageStr = paginNav(dataObject.pageNo, dataObject.totalPages, dataObject.totalCount);
	$("#pagin").html(pageStr);
}
//封装一行的数据
function getRowHtml(data){
	var row = "<tr>";
	row +=  "<td>" + data.mailNum + "</td>";
	row += "<td>" + data.reciever.name + "</td>";
	row += "<td>" + data.reciever.province + data.reciever.city + data.reciever.area + data.reciever.address + "</td>";
	row += "<td>" + getDate1(data.dateArrived) + "</td>";
	//派件员==未分派，不需要显示派件员姓名和电话
	if(data.user == null){
		row += "<td></td><td></td>";
	}else{
		row += "<td>" + data.user.realName + "</td>";
		row += "<td>" + data.user.phone + "</td>";
	}
	//状态
	if(data.orderStatus == "<%=OrderStatus.NOTDISPATCH %>" || data.orderStatus==null){
		row += "<td>" + "<%=DispatchStatus.NOTDISPATCH.getMessage()%>" + "</td>";
	}else{
		row += "<td>" + "<%=DispatchStatus.DISPATCHED.getMessage()%>" + "</td>";
	}
	row += "</tr>";
	return row;
}

//初始化派件员下拉框（快递员）
function initCourier() {
	//查询所有派件员
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/packageDispatch/getAllUserList",//路径  
        data : {},//数据，这里使用的是Json格式进行传输  
        success : function(dataList) {//返回数据根据结果进行相应的处理  
        	var courier_select = $("#courier_select");
			// 清空select  
			courier_select.empty(); 
			if(dataList != null){
				for(var i = 0; i < dataList.length; i++){
					data = dataList[i];
					courier_select.append("<option value='"+data.id+"'>"+data.realName+"</option>");
				}
			} 
			courierIsLoadSuccess = 1;
        },
        error : function() {  
        	courierIsLoadSuccess = 0;
       		//alert("派件员列表加载异常！");  
  		}    
    });
}	

// 添加  
function col_add() {  
    var selObj = $("#mySelect");  
    var value="value";  
    var text="text";  
    selObj.append("<option value='"+value+"'>"+text+"</option>");  
}  
	
//显示选择派件员div
function showCourierDiv(mailNum) {
	if(courierIsLoadSuccess == 0){//派件员加载失败的话，重新加载
		initCourier();
	}
	$("#chooseCourier_div").show();
}
//隐藏选择派件员div
function hideCourierDiv() {
	$("#chooseCourier_div").hide();
}
//选择派件员
function chooseCourier() {
$("#ddlregtype").find("option:selected").text(); 
	$("#courierName").text($("#courier_select").find("option:selected").text());
	$("#courierId").val($("#courier_select").val());
	$("#chooseCourier_div").hide();
}
	
</script>
</body>
</html>