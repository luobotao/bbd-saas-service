<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.AbnormalStatus" %>
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
			<form action="?" method="get" id="searchOrderForm" name="searchOrderForm">
				<div class="row">
					<div class="col-xs-3">
						<label>状态：</label>
						<select id="status" name="status" class="form-control">
							<%=AbnormalStatus.Srcs2HTML(-1)%>
						</select>
					</div>
					<div class="col-xs-3">
						<label>到站时间：</label>
						<input id="between" name="between" type="text" class="form-control" placeholder="请选择到站时间" value="${between}"/>
					</div>
				</div>
				<div class="row">
					<button class="btn btn-primary" style="margin-top:10px ; margin-left: 15px ;" onclick="gotoPage(0);">查询</button>
				</div>
			</form>
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
					
					for(Order order : orderPage.getDatas()){
				%>
				<tr>
					<td><%=order.getMailNum()%></td>
					<td><%=order.getReciever().getName()%></td>
					<td><%=order.getReciever().getProvince()%> <%=order.getReciever().getCity()%> <%=order.getReciever().getArea()%> <%=order.getReciever().getAddress()%></td>
					<td>到站时间2016-04-06 15:22:10<%=order.getDatePrint()%></td>
					<td><%=order.getUser().getRealName()%></td>
					<td><%=order.getUser().getPhone()%></td>
					<td><%=order.getExpressStatus()%></td>
					<td>
						<a href="javascript:void(0);" onclick="showCourierDiv()">重新分派</a>
						<a href="javascript:void(0);" onclick="showOtherExpressDiv()">转其他快递</a>
						<a href="javascript:void(0);" onclick="showOtherSiteDiv()">转其他站点</a>
						<a href="javascript:void(0);" onclick="showApplyReturnDiv()">申请退货</a>
					</td>
					
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
		<span>运单号：<input id="waybillId" name="waybillId" type="text" value="" placeholder="请输入运单号"/></span>
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
		<button onclick="chooseOtherSite('waybillId')">确定</button>
	</div>
</div>
<!-- 转其他站点面板-结束 -->

<!-- 申请退货-开始 -->
<div  id="apply_return_div" class="popDiv" >
	<div class="title_div">申请退货</div>
	<div class="m20">
		<span>选择退货原因:
			<select id="reasonType" name="reasonType">  
				<option value ="货物破损">货物破损</option>  
				<option value ="超时配送">超时配送</option>  
				<option value="客户端要求退换">客户端要求退换</option>  
				<option value="其他">其他</option>  
			</select>				  
		</span> <br><br>
		<span>
			<textarea style="display: none;" rows="5" cols="50" id="reasonInfo" name="reasonInfo" placeholder="请输入退货原因">
				
			</textarea>
		</span>
	</div>
	<div class="m20">
		<button onclick="hideApplyReturnDiv()">取消</button>
		<button onclick="applyReturn('waybillId')">确定</button>
	</div>
</div>
<!-- 转其他站点面板-结束 -->

<!-- 分页js -->
<script src="<c:url value="/resources/javascripts/page/pageBar.js" />"> </script>

<script type="text/javascript">


$(document).ready(function() {
	//显示分页条
	var pageStr = paginNav(<%=currentPage%>, <%=totalPage%>, <%=count%>);
	$("#pagin").html(pageStr);
	//到站时间，选择时间
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
	//退货原因，选择其他的原因弹出详情输入框
	$("#reasonType").change(function(){
		if(this.value == "其他"){
			$("#reasonInfo").show();
		} else {
			$("#reasonInfo").hide();
		}
	});

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
            "status" : -1, 
            "courierId" : ""
            /* "status" : $("#status").val(), 
            "between" : $("#between").val(), 
            "courierId" : $("#courierId").val(), */ 
        },//数据，这里使用的是Json格式进行传输  
        success : function(dataObject) {//返回数据根据结果进行相应的处理 
            var dataList = dataObject.datas;
            //刷新列表和分页条
            refreshTable(dataList, pageIndex);
		},
        error : function() {  
           	alert("加载分页数据异常，请重试！");  
      	}    
    });	
}	
//封装一行的数据
function getRowHtml(data){
	var row = "<tr>";
	row +=  "<td>" + data.mailNum + "</td>";
	row += "<td>" + data.reciever.name + "</td>";
	row += "<td>" + data.reciever.province + data.reciever.city + data.reciever.area + data.reciever.address + "</td>";
	row += "<td>到站时间2016-04-06 15:22:10" + data.reciever.name + "</td>";
	row += "<td>" + data.user.realName + "</td>";
	row += "<td>" + data.user.phone + "</td>";
	row += "<td>" + data.user.expressStatus + "</td>";
	row += "<td><a href='javascript:void(0);' onclick='showCourierDiv(" + data.user.expressStatus + ")'>重新分派</a>";
	row += "<a href='javascript:void(0);' onclick='showOtherExpressDiv(" + data.user.expressStatus + ")'>转其他快递</a>";
	row += "<a href='javascript:void(0);' onclick='showOtherSiteDiv(" + data.user.expressStatus + ")'>转其他站点</a>";
	row += "<a href='javascript:void(0);' onclick='showApplyReturnDiv(" + data.user.expressStatus + ")'>申请退货</a></td>";
	row += "</tr>";
	return row;
}
//刷新列表和分页条
function refreshTable(dataList, pageIndex){
	//更新列表
	var tbody = $("#dataList");
    if(dataList != null){
		var datastr = "";
		for(var i = 0; i < dataList.length; i++){
			datastr += getRowHtml(dataList[i]);
		}
		tbody.html(datastr);
	} 
	//更新分页条
	var pageStr = paginNav(pageIndex, <%=totalPage%>, <%=count%>);
	$("#pagin").html(pageStr);        
}

/************************分页条***************结束***************************************/	

/************************初始化各个操作面板***************开始***************************************/


//初始化派件员下拉框（快递员）
function initCourier() {
	//查询所有派件员
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/handleAbnormal/getAllUserList",//路径  
        data : {  
            "siteId" : "siteId" //$("#waybillId").val()
        },//数据，这里使用的是Json格式进行传输  
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
        },
        error : function() {  
       		alert("派件员列表加载异常，请重试！");  
  		}    
    });
}	
//初始化快递公司
function initExpressCompany() {
	//查询所有派件员
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/handleAbnormal/getAllExpressCompanyList",//路径  
        data : {  
            "siteId" : "siteId" //$("#waybillId").val()
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
//初始化站点
function initSite() {
	//查询所有站点
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/handleAbnormal/getAllSiteList",//路径  
        data : {  
            "siteId" : "siteId" //$("#waybillId").val()
        },//数据，这里使用的是Json格式进行传输  
        success : function(dataList) {//返回数据根据结果进行相应的处理  
        	
		var site_select = $("#site_select");
		// 清空select  
		courier_select.empty(); 
		if(dataList != null){
			for(var i = 0; i < dataList.length; i++){
				data = dataList[i];
				site_select.append("<option value='"+data.id+"'>"+data.realName+"</option>");
			}
		} 
        },
        error : function() {  
       		alert("站点列表加载异常，请重试！");  
  		}    
    });
}	

/************************初始化各个操作面板***************结束***************************************/
//显示选择派件员div
function showCourierDiv(waybillId) {
	$("#chooseCourier_div").show();
}
//隐藏选择派件员div
function hideCourierDiv() {
	$("#chooseCourier_div").hide();
}
//重新分派
function chooseCourier(mailNum) {
	//保存分派信息
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/handleAbnormal/reDispatch",//路径  
        data : {  
            "mailNum" : mailNum, //
            "courierId" : $("#sender_select").val() //$("#waybillId").val()
        },//数据，这里使用的是Json格式进行传输  
        success : function(data) {//返回数据根据结果进行相应的处理  
        	if(data.success){
        		alert("分派成功，刷新列表！");  
        		//分派成功，刷新列表！
        		//获取当前页
    			var pageIndex = parseInt($(".pagination .active a").html())-1;
    			//console.log("pageIndex==="+pageIndex);
        		gotoPage(pageIndex);
        	}else{
        		alert("重新分派失败，请重新分派！");  
        	}
        },
        error : function() {  
       		alert("重新分派发生异常，请重试！");  
  		}    
    });
    //隐藏面板
	$("#chooseCourier_div").hide();
}

//显示转其他快递公司div
function showOtherExpressDiv(waybillId) {
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
            "expressId" : $("#express_select").val() //$("#waybillId").val()
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

//显示转其他站点div
function showOtherSiteDiv(waybillId) {
	$("#chooseOtherSite_div").show();
}
//隐藏转其他站点div
function hideOtherSiteDiv() {
	$("#chooseOtherSite_div").hide();
}
//转其他站点
function chooseOtherSite(mailNum) {
	//转其他站点
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/handleAbnormal/toOtherSite",//路径  
        data : {  
            "mailNum" : mailNum, //
            "siteId" : $("#site_select").val() //$("#waybillId").val()
        },//数据，这里使用的是Json格式进行传输  
        success : function(data) {//返回数据根据结果进行相应的处理  
        	if(data.success){
        		alert("已转到其他站点！");  
        		//已转到其他快递，刷新列表！
        		//获取当前页
    			var pageIndex = parseInt($(".pagination .active a").html())-1;
    			//console.log("pageIndex==="+pageIndex);
        		gotoPage(pageIndex);
        	}else{
        		alert("转到其他站点失败，请重新选择站点公司！");  
        	}
        },
        error : function() {  
       		alert("转到其他站点发生异常，请重试！");  
  		}    
    });
    //隐藏面板
	$("#chooseOtherSite_div").hide();
}

//显示申请退货div
function showApplyReturnDiv(waybillId) {
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
            "reasonType" : $("#reasonType").val(), 
            "reasonInfo" : $("#reasonInfo").val() 
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

</script>
</body>
</html>