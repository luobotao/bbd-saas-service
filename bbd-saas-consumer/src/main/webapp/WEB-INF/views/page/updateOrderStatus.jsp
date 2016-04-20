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
<body class="fbg">
<!-- S content -->
<div class="clearfix b-branch">
	<div class="container-fluid">
		<div class="row">
			<!-- S sidebar -->
			<div class="col-xs-12 col-sm-12 bbd-md-3" style="opacity:0;">
				<ul class="b-sidebar">
					<li class="lv1"><a href="package-arrives.html"><i class="b-icon p-package"></i>包裹到站</a></li>
					<li class="lv1 side-cur"><a href="tracking-assign.html"><i class="b-icon p-aign"></i>运单分派</a></li>
					<li class="lv1"><a href="exception-processing.html"><i class="b-icon p-error"></i>异常件处理</a></li>
					<li class="lv1"><a href="data-query.html"><i class="b-icon p-query"></i>数据查询</a></li>
					<li class="lv1"><a href="system-distribution.html"><i class="b-icon p-set"></i>系统设置</a></li>
					<ul class="menu dn">
		                <li><a href="system-distribution.html">配送区域</a></li>
		                <li><a href="system-usermanage.html">用户管理</a></li>
		                <li><a href="system-role.html">角色管理</a></li>
		            </ul>
				</ul>
			</div>
			<!-- E sidebar -->
			<!-- S detail -->
			<div class="b-detail col-xs-12 col-sm-12 col-md-9 col-lg-9">
				修改订单状态：
				<!-- S 搜索区域 -->
				<div class="search-area">
  					<div class="row">
  						<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
  							<label>状态：</label>
  							<select id="status" name="status" class="form-control form-con-new">
  								<%=OrderStatus.Srcs2HTML(-1)%>
  							</select>
  						</div>
  						<div class="form-group col-xs-12 col-sm-6 col-md-5 col-lg-5">
  							<label>到站时间：</label>
  							<input id="arriveBetween" name="arriveBetween" value="${arriveBetween}" type="text" placeholder="请选择到站时间" class="form-control"  />
  						</div>
  						<div class="form-group col-xs-12 col-sm-6 bbd-md-3">
  							<a href="javascript:void(0)" onclick="gotoPage(0);" class="ser-btn l"><i class="b-icon p-query p-ser"></i>查询</a>
  						</div>
  					</div>
  					<div class="b-line"></div>
  					<div class="row pb20">
  						<div class="form-group col-xs-12 col-sm-6 col-md-3 col-lg-3">
  							<a href="javascript:void(0)" onclick="showCourierDiv()" class="ser-btn l">选择派件员</a>
  							<span class="ft12 pt20">已选择：<span id="courierName"></span></span>
  							<input id="courierId" type="hidden" value="" /> 
  						</div>
  						
  						<div class="form-group col-xs-12 col-sm-6 col-md-5 col-lg-5">
  							<label>扫描运单号：</label>
  							<input id="mailNum" name="mailNum" type="text" placeholder="请扫描运单号" class="form-control" onkeypress="enterPress(event)" />
  							<span class="pl20 ft12" id="mailNum_check"> </span>	
  						</div>
  					</div>
  				</div>
				<!-- E 搜索区域 -->
  			<div class="tab-bod mt20">
  				<!-- S table -->
  				<div class="table-responsive">
  					<table class="table">
  						<thead>
  							<tr>
								<th>运单号</th>
								<th>收货人</th>
								<th width="20%">收货人地址</th>
								<th>到站时间</th>
								<th>派送员姓名</th>
								<th>派送员手机</th>
								<th>状态</th>
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
										<td><%=order.getUser().getLoginName()%></td>
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
  				</div>
  				<!-- E table -->
  				<!-- S tableBot -->
	  			<div class="clearfix pad20" id="pagin"></div>
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
		var status = $("#status").val();
		if(status == null || status == "-1"){
	  		$("#mailNum_check").text("请选择状态！");
	  		return ;
	  	}
	  	if(status == "2"){//已分派--需要选择派件员
	  		if($("#courierId").val() == null || $("#courierId").val() == ""){
	  			$("#mailNum_check").text("请选择派件员！");
	  			return ;
	  		}
	  	}
	 }).blur(function(){//扫描运单号--把快递分派给派件员
		var status = $("#status").val();
		if(status == null || status == "-1"){
	  		$("#mailNum_check").text("请选择状态！");
	  		return ;
	  	}
	  	if(status == "2"){//已分派--需要选择派件员
	  		if($("#courierId").val() == null || $("#courierId").val() == ""){
	  			$("#mailNum_check").text("请选择派件员！");
	  			return ;
	  		}
	  	}
	  	//修改运单状态	 
	  	updateStatus();
	});
	//扫描运单号--把快递分派给派件员
	$("#mailNum").on('input',function(e){ 
		
	});
	 

});
// 运单分派  
function updateStatus() {  
	$.ajax({
        type : "GET",  //提交方式  
		url : "<%=request.getContextPath()%>/updOrderStatus/updateOrderStatus",//路径  
		data : {  
		    "mailNum" : $("#mailNum").val(),
		    "courierId" : $("#courier_select").val(),
		    "status" : $("#status").val()  
		},//数据，这里使用的是Json格式进行传输  
		success : function(data) {//返回数据根据结果进行相应的处理  
		    if (data.operFlag == 1) { 
		    	$("#mailNum_check").text($("#mailNum").val() + "更新成功！");
		    	//刷新列表
		    	refreshTable(data.orderPage);
		    }else {
		    	$("#mailNum_check").text($("#mailNum").val() + "更新失败！");
		    }
		},
		error : function() {  
			alert("服务器繁忙，请稍后再试！");  
		}     
    });
}  

//回车事件--运单分派
function enterPress(e){
	if(!e) e = window.event;//火狐中是 window.event
	if((e.keyCode || e.which) == 13){
		//未选择派件员 
		if($("#courierId").val() == null || $("#courierId").val() == ""){
		  	$("#mailNum_check").text("请选择派件员！");
		  	return ;
		}	
		//已选择派件员，把快递分派给派件员	 
		dispatch();
	}
}

//加载带有查询条件的指定页的数据
function gotoPage(pageIndex) {
	//查询所有派件员
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/updOrderStatus/getList",//路径  
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
		row += "<td>" + data.user.loginName + "</td>";
	}
	//状态
	row += "<td>" + getStatus(data.orderStatus) + "</td>";
	row += "</tr>";
	return row;
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
//初始化派件员下拉框（快递员）
function initCourier() {
	//查询所有派件员
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/updOrderStatus/getAllUserList",//路径  
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