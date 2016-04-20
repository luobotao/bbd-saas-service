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
		            </ul>
				</ul>
			</div>
			<!-- E sidebar -->
			<!-- S detail -->
			<div class="b-detail col-xs-12 col-sm-12 bbd-md-9">
				<!-- S 搜索区域 -->
				<form class="form-inline form-inline-n">
					<div class="search-area">
	  					<div class="row">
	  						<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
	  							<label>状态：</label>
	  							<select id="status" name="status" class="form-control form-con-new">
	  								<%=DispatchStatus.Srcs2HTML(-1)%>
	  							</select>
	  						</div>
	  						<div class="form-group col-xs-12 col-sm-6 col-md-5 col-lg-5">
	  							<label>到站时间：</label>
	  							<input id="arriveBetween" name="arriveBetween" value="${arriveBetween}" type="text" placeholder="请选择到站时间" class="form-control"  />
	  						</div>
	  						<div class="form-group col-xs-12 col-sm-6 col-md-3 col-lg-3">
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
				</form>
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

<!-- 运单分派面板-开始 -->
<div id="chooseCourier_div" class="j-sel-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="display:none;">
	<div class="modal-dialog b-modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header b-modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title tc">选择派件员</h4>
			</div>
			<div class="modal-body b-modal-body">
				<ul class="row">
					<li class="col-md-12">
					<!-- 派件员: -->
						<div class="has-sel-icon mb12">
							<i class="glyphicon glyphicon-user c-gray pl15"></i>
							<select id="courier_select"> </select>
						</div>
					</li>
					<li class="col-md-12">
						<div class="c-red"><i class="glyphicon glyphicon-exclamation-sign pl15"></i> 请选择派件员</div>
					</li>
				</ul>
				<div class="row mt20">
					<span class="col-md-6"><a href="javascript:void(0)" onclick="hideCourierDiv()" class="sbtn sbtn2 g">取消</a></span>
					<span class="col-md-6"><a href="javascript:void(0)" onclick="chooseCourier()" class="sbtn sbtn2 l">确定</a></span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 运单分派面板-结束 -->

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
		//未选择派件员 
		if($("#courierId").val() == null || $("#courierId").val() == ""){
	  		$("#mailNum_check").text("请选择派件员！");
	  		return ;
	  	}	
	  	//已选择派件员，把快递分派给派件员	 
	  	dispatch();
	});
	//扫描运单号--把快递分派给派件员--边输入边改变
	$("#mailNum").on('input',function(e){ 
		
	});
	 

});

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
		    	$("#mailNum_check").text($("#mailNum").val() + "运单分派成功！");
		    	//刷新列表
		    	refreshTable(data.orderPage);
		    }else if(data.operFlag == 0){
		    	$("#mailNum_check").text("【异常扫描】不存在此订单！");
		    }else if(data.operFlag == 2){
		    	$("#mailNum_check").text("重复扫描，此运单已经分派过啦！");
		    }else if(data.operFlag == 3){
		    	$("#mailNum_check").text($("#mailNum").val() + "运单分派失败，请重试！");
		    }else{
		    	$("#mailNum_check").text("只有状态为未分派、滞留、拒收的运单才能分派！");
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
		row += "<td>" + data.user.loginName + "</td>";
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
	$("#chooseCourier_div").modal("show");
}
//隐藏选择派件员div
function hideCourierDiv() {
    $("#chooseCourier_div").modal("hide");
}
//选择派件员
function chooseCourier() {
$("#ddlregtype").find("option:selected").text(); 
	$("#courierName").text($("#courier_select").find("option:selected").text());
	$("#courierId").val($("#courier_select").val());
	$("#chooseCourier_div").modal("hide");
}
	
</script>
</body>
</html>