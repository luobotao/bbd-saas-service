<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.AbnormalStatus" %>
<%@ page import="com.bbd.saas.enums.OrderStatus" %>
<%@ page import="com.bbd.saas.vo.UserVO" %>
<%-- <%@ page import="com.bbd.saas.mongoModels.Site" %> --%>
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
						<%=AbnormalStatus.Srcs2HTML(-1)%>
					</select>
				</div>
				<div class="col-xs-3">
					<label>到站时间：</label>
					<input id="arriveBetween" name="arriveBetween" type="text" class="form-control" placeholder="请选择到站时间" value="${arriveBetween}"/>
				</div>
			</div>
			<div class="row">
				<button class="btn btn-primary" style="margin-top:10px ; margin-left: 15px ;" onclick="gotoPage(0);">查询</button>
			</div>
		</div>
	</div>
	<div class="col-xs-12">
		
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
						<td>操作</td>
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
							if(order.getOrderStatus() == OrderStatus.RETENTION){
						%>
							<td><%=AbnormalStatus.RETENTION.getMessage()%></td>
						<%
							}else{
						%>
							<td><%=AbnormalStatus.REJECTION.getMessage()%></td>
						<%
							}
						%>
						<td>
							<a href="javascript:void(0);" onclick="showCourierDiv('<%=order.getMailNum()%>','<%=order.getUser().getStaffid()%>')">重新分派</a>
							<a href="javascript:void(0);" onclick="showOtherExpressDiv()">转其他快递</a>
							<a href="javascript:void(0);" onclick="showOtherSiteDiv('<%=order.getMailNum()%>')">转其他站点</a>
							<a href="javascript:void(0);" onclick="showApplyReturnDiv()">申请退货</a>
						</td>
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


<!-- 重新分派面板-开始 -->
<div  id="chooseCourier_div" class="popDiv" >
	<div class="title_div">重新分派</div>
	<div class="m20">
		<span>派件员:
			<select id="courier_select" >  
				 
			</select>				  
		</span> 
	</div>
	<div class="m20">
		<button onclick="hideCourierDiv()">取消</button>
		<button onclick="chooseCourier()">确定</button>
	</div>
</div>
<!-- 重新分派面板-结束 -->

<!-- 转其他快递面板-开始 -->
<div  id="chooseOtherExpress_div" class="popDiv" >
	<div class="title_div">转其他快递</div>
	<div class="m20">
		<span>快递公司:
			<select id="express_select">  
				<option value ="中通">中通</option>  
				<option value ="申通">申通</option>  
				<option value="顺风">顺风</option>  
			</select>				  
		</span> <br><br>
		<span>运单号：<input id="mailNum" name="mailNum" type="text" value="" placeholder="请输入运单号"/></span>
	</div>
	<div class="m20">
		<button onclick="hideOtherExpressDiv()">取消</button>
		<button onclick="chooseOtherExpress()">确定</button>
	</div>
</div>
<!-- 转其他快递面板-结束 -->


<!-- 转其他站点面板-开始 -->
<div  id="chooseOtherSite_div" class="popDiv" >
	<div class="title_div">转其他站点</div>
	<div class="m20">
		<span>站点:
			<select id="site_select">  
				<option value ="站点A">站点A</option>  
				<option value ="站点B">站点B</option>  
				<option value="站点C">站点C</option>  
			</select>				  
		</span> <br>
	</div>
	<div class="m20">
		<button onclick="hideOtherSiteDiv()">取消</button>
		<button onclick="chooseOtherSite('mailNum')">确定</button>
	</div>
</div>
<!-- 转其他站点面板-结束 -->

<!-- 申请退货-开始 -->
<div  id="apply_return_div" class="popDiv" >
	<div class="title_div">申请退货</div>
	<div class="m20">
		<span>选择退货原因:
			<select id="returnReasonType" name="returnReasonType">  
				<option value ="货物破损">货物破损</option>  
				<option value ="超时配送">超时配送</option>  
				<option value="客户端要求退换">客户端要求退换</option>  
				<option value="其他">其他</option>  
			</select>				  
		</span> <br><br>
		<span>
			<textarea style="display: none;" rows="5" cols="50" id="returnReasonInfo" name="returnReasonInfo" placeholder="请输入退货原因">
				
			</textarea>
		</span>
	</div>
	<div class="m20">
		<button onclick="hideApplyReturnDiv()">取消</button>
		<button onclick="applyReturn('mailNum')">确定</button>
	</div>
</div>
<!-- 转其他站点面板-结束 -->

<!-- 分页js -->
<script src="<c:url value="/resources/javascripts/page/pageBar.js" />"> </script>
<script src="<c:url value="/resources/javascripts/timeUtil.js" />"> </script>

<script type="text/javascript">
//缓存快递员列表和站点列表数据
var courierList = null, siteList = null;
//var staffId = null;
var siteId = null, mailNum = null;

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
	
	//退货原因，选择其他的原因弹出详情输入框
	$("#returnReasonType").change(function(){
		if(this.value == "其他"){
			$("#returnReasonInfo").show();
		} else {
			$("#returnReasonInfo").hide();
		}
	});
	
	//初始化快递员列表
	initCourierList();
	//初始化站点列表
	initSiteList();
});
/************************分页条***************开始***************************************/
//加载带有查询条件的指定页的数据
function gotoPage(pageIndex) {
	//查询所有派件员
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/handleAbnormal/getList",//路径  
        data : {  
            "pageIndex" : pageIndex,
            "status" : $("#status").val(), 
            "arriveBetween" : $("#arriveBetween").val() 
        },//数据，这里使用的是Json格式进行传输  
        success : function(dataObject) {//返回数据根据结果进行相应的处理 
            refreshTable(dataObject);  
		},
        error : function() {  
           	alert("加载分页数据异常，请重试！");  
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
	/* //派件员姓名和电话
	row += "<td>" + data.user.realName + "</td>";
	row += "<td>" + data.user.phone + "</td>";
	 */
	//派件员==未分派，不需要显示派件员姓名和电话
	if(data.user == null){
		row += "<td></td><td></td>";
	}else{
		row += "<td>" + data.user.realName + data.user.staffid + "</td>";
		row += "<td>" + data.user.phone + "</td>";
	}
	//状态
	if(data.orderStatus == "<%=OrderStatus.RETENTION %>" || data.orderStatus==null){
		row += "<td>" + "<%=AbnormalStatus.RETENTION.getMessage()%>" + "</td>";
	}else{
		row += "<td>" + "<%=AbnormalStatus.REJECTION.getMessage()%>" + "</td>";
	}
	
	row += "<td><a href='javascript:void(0);' onclick='showCourierDiv(\"" + data.mailNum + "\", " + "\"" + data.user.staffid + "\")'>重新分派</a>";
	row += "<a href='javascript:void(0);' onclick='showOtherExpressDiv(\"" + data.mailNum + "\")'>转其他快递</a>";
	row += "<a href='javascript:void(0);' onclick='showOtherSiteDiv(\"" + data.mailNum + "\")'>转其他站点</a>";
	row += "<a href='javascript:void(0);' onclick='showApplyReturnDiv(\"" + data.mailNum + "\")'>申请退货</a></td>";
	row += "</tr>";
	return row;
}


/************************分页条***************结束***************************************/	

/**************************重新分派***************开始***********************************/
//初始化派件员下拉框（快递员）
function initCourierList() {
	//查询所有派件员
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/handleAbnormal/getAllUserList",//路径  
        data : {},//数据，这里使用的是Json格式进行传输  
        success : function(dataList) {//返回数据根据结果进行相应的处理  
        	courierList = dataList;
        },
        error : function() {  
       		//alert("派件员列表加载异常，请重试！");
       		courierList = null;  
       	}    
    });
}
//显示选择派件员div
function showCourierDiv(mailNumStr, staffId) {
	mailNum = mailNumStr;
	//staffId = staffIdStr;
	console.log("v==mailNum=="+mailNum);
	//console.log("courierList===="+courierList);
	if(courierList != null){
		loadCouriers(courierList, staffId);
	}else{//重新查询所有派件员
		$.ajax({
			type : "GET",  //提交方式  
	        url : "<%=path%>/handleAbnormal/getAllUserList",//路径  
	        data : {},//数据，这里使用的是Json格式进行传输  
	        success : function(dataList) {//返回数据根据结果进行相应的处理  
	        	loadCouriers(dataList, staffId);
	        },
	        error : function() {  
	       		alert("服务器繁忙，请稍后再试！");
	  		}    
	    });
	}
	$("#chooseCourier_div").show();
}

//把派件员添加到下拉框中
function loadCouriers(courierList, staffId) {
	if(staffId == null || staffId == "undefined"){
		staffId = "";
	}
	var courier_select = $("#courier_select");
	// 清空select  
	courier_select.empty(); 
	if(courierList != null){
	    for(var i = 0; i < courierList.length; i++){
			data = courierList[i];
			if(data.staffId != staffId){
				courier_select.append("<option value='"+data.staffId+"'>"+data.realName+"</option>");
			}
		}
	}
}

//隐藏选择派件员div
function hideCourierDiv() {
	mailNum = null;
	//staffId = null;
	$("#chooseCourier_div").hide();
}

//重新分派
function chooseCourier() {
	console.log("v==mailNum=="+mailNum);
	//获取当前页
    var pageIndex = parseInt($(".pagination .active a").html())-1;
	//保存分派信息
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/handleAbnormal/reDispatch",//路径  
        data : {  
            "mailNum" : mailNum, //全局变量
            "staffId" : $("#courier_select").val(), //全局变量
            "pageIndex" : pageIndex,//更新列表的参数
            "status" : $("#status").val(), 
            "arriveBetween" : $("#arriveBetween").val() 
        },//数据，这里使用的是Json格式进行传输  
        success : function(data) {//返回数据根据结果进行相应的处理  
        	if(data.operFlag == 1){
        		//分派成功，刷新列表！
        		refreshTable(data.orderPage);
        	}else{
        		alert("重新分派失败，请稍后再试！");  
        	}
        },
        error : function() {  
       		alert("服务器繁忙，请稍后再试！");  
  		}    
    });
    //隐藏面板
	$("#chooseCourier_div").hide();
}
/**************************重新分派***************结束***********************************/

/**************************转其他站点***************开始***********************************/
//初始化站点列表
function initSiteList() {
	//查询所有站点
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/handleAbnormal/getAllOtherSiteList",//路径  
        data : {},//数据，这里使用的是Json格式进行传输  
        success : function(dataList) {//返回数据根据结果进行相应的处理  
        	siteList = dataList;
        },
        error : function() { 
        	siteList = null; 
  		}    
    });
}
//显示转其他站点div
function showOtherSiteDiv(mailNumStr) {
	mailNum = mailNumStr;
	//console.log("siteList==="+siteList+" mailNumStr==="+mailNumStr);
	if(siteList != null){
		//console.log("siteList != null== load div=");
		loadSites(siteList);
	}else{//重新查询所有派件员
		$.ajax({
			type : "GET",  //提交方式  
	        url : "<%=path%>/handleAbnormal/getAllOtherSiteList",//路径  
	        data : {},//数据，这里使用的是Json格式进行传输  
	        success : function(dataList) {//返回数据根据结果进行相应的处理  
	        	loadSites(dataList);
	        },
	        error : function() {  
	       		alert("服务器繁忙，请稍后再试！");
	  		}    
	    });
	}
	$("#chooseOtherSite_div").show();
}
//把站点添加到下拉框中
function loadSites(siteList) {
	console.log("siteList=33333=="+siteList);
	var site_select = $("#site_select");
	// 清空select  
	site_select.empty(); 
	console.log("siteList==="+siteList);
	if(siteList != null){
		console.log("siteList.length==="+siteList.length);
		for(var i = 0; i < siteList.length; i++){
			data = siteList[i];
			site_select.append("<option value='"+data.id+"'>"+data.name+"</option>");
		}
	}
}
//隐藏转其他站点div
function hideOtherSiteDiv() {
	mailNum = null;
	$("#chooseOtherSite_div").hide();
}
//转其他站点
function chooseOtherSite() {
	//获取当前页
    var pageIndex = parseInt($(".pagination .active a").html())-1;
    $.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/handleAbnormal/toOtherSite",//路径  
        data : {  
            "mailNum" : mailNum, //
            "areaCode" : $("#site_select").val(),//站点编号
            "pageIndex" : pageIndex,//更新列表
            "status" : $("#status").val(), 
            "arriveBetween" : $("#arriveBetween").val() 
        },//数据，这里使用的是Json格式进行传输  
        success : function(data) {//返回数据根据结果进行相应的处理  
        	if(data.operFlag == 1){
        		//分派成功，刷新列表！
        		refreshTable(data.orderPage);
        	}else{
        		alert("转其他站点失败，请稍后再试！");  
        	}
        },
        error : function() {  
       		alert("服务器繁忙，请稍后再试！");  
  		}    
    });
    //隐藏面板
	$("#chooseOtherSite_div").hide();
}
/**************************转其他站点***************结束***********************************/

/*************************************下面的暂时不做*****************************************************/

/************************转其他快递公司***************开始***************************************/	
//初始化快递公司
function initExpressCompany() {
	//查询所有派件员
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/handleAbnormal/getAllExpressCompanyList",//路径  
        data : {  
            "areaCode" : "areaCode" //$("#mailNum").val()
        },//数据，这里使用的是Json格式进行传输  
        success : function(dataList) {//返回数据根据结果进行相应的处理  
        	
		var express_select = $("#express_select");
		// 清空select  
		courier_select.empty(); 
		if(dataList != null){
			for(var i = 0; i < dataList.length; i++){
				data = dataList[i];
				express_select.append("<option value='"+data.id+"'>"+data.realName+"</option>");
			}
		} 
        },
        error : function() {  
       		alert("快递公司列表加载异常，请重试！");  
  		}    
    });
}	

//显示转其他快递公司div
function showOtherExpressDiv(mailNum) {
	$("#chooseOtherExpress_div").show();
	
}
//隐藏转其他快递公司div
function hideOtherExpressDiv() {
	$("#chooseOtherExpress_div").hide();
}
//选择其他快递
function chooseOtherExpress(mailNum) {
	//转其他快递公司
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/handleAbnormal/toOtherExpress",//路径  
        data : {  
            "mailNum" : mailNum, //
            "expressId" : $("#express_select").val() //$("#mailNum").val()
        },//数据，这里使用的是Json格式进行传输  
        success : function(data) {//返回数据根据结果进行相应的处理  
        	if(data.success){
        		alert("已转到其他快递！");  
        		//已转到其他快递，刷新列表！
        		//获取当前页
    			var pageIndex = parseInt($(".pagination .active a").html())-1;
    			//console.log("pageIndex==="+pageIndex);
        		gotoPage(pageIndex);
        	}else{
        		alert("转到其他快递失败，请重新选择快递公司！");  
        	}
        },
        error : function() {  
       		alert("转到其他快递发生异常，请重试！");  
  		}    
    });
    //隐藏面板
	$("#chooseOtherExpress_div").hide();
}

/************************转其他快递公司***************结束***************************************/

/************************申请退货***************开始***************************************/
//显示申请退货div
function showApplyReturnDiv(mailNum) {
	$("#apply_return_div").show();
}
//隐藏申请退货div
function hideApplyReturnDiv() {
	$("#apply_return_div").hide();
}
//确定退货
function applyReturn(mailNum) {
	//保存退货信息
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/handleAbnormal/saveReturn",//路径  
        data : {  
            "mailNum" : mailNum, //
            "returnReasonType" : $("#returnReasonType").val(), 
            "returnReasonInfo" : $("#returnReasonInfo").val() 
        },//数据，这里使用的是Json格式进行传输  
        success : function(data) {//返回数据根据结果进行相应的处理  
        	if(data.success){
        		alert("退货成功！");  
        		//退货成功，刷新列表！
        		//获取当前页
    			var pageIndex = parseInt($(".pagination .active a").html())-1;
    			//console.log("pageIndex==="+pageIndex);
        		gotoPage(pageIndex);
        	}else{
        		alert("退货失败，请重试！");  
        	}
        },
        error : function() {  
       		alert("退货发生异常，请重试！");  
  		}    
    });
    //隐藏面板
	$("#apply_return_div").hide();
}
/**********************申请退货**************************结束************************************/
</script>
</body>
</html>