<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.AbnormalStatus" %>
<%@ page import="com.bbd.saas.enums.OrderStatus" %>
<%@ page import="com.bbd.saas.utils.Dates" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
 
<html>
<head>
	<jsp:include page="../main.jsp" flush="true" />
</head>
<%
	String proPath = request.getContextPath();
	String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+proPath;
	//System.out.println("========状态==========="+OrderStatus.Srcs2HTML(-1));
%>
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
				<!-- S 搜索区域 -->
				<form class="form-inline form-inline-n">
					<div class="search-area">
	  					<div class="row pb20">
	  						<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
	  							<label>状态：</label>
	  							<select id="status" name="status" class="form-control form-con-new">
	  								<%=AbnormalStatus.Srcs2HTML(-1)%>
	  							</select>
	  						</div>
	  						<div class="form-group col-xs-12 col-sm-6 col-md-5 col-lg-5">
	  							<label>到站时间：</label>
	  							<input id="arriveBetween" name="arriveBetween" value="${arriveBetween}" type="text" placeholder="请选择到站时间" class="form-control c-disable"  />
	  						</div>
	  						<div class="form-group col-xs-12 col-sm-6 col-md-3 col-lg-3">
	  							<a href="javascript:void(0)" onclick="gotoPage(0);" class="ser-btn l"><i class="b-icon p-query p-ser"></i>查询</a>
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
								<th width="180px">操作</th>
  							</tr>
  						</thead>
  						<tbody id="dataList">
						<%
							PageModel<Order> orderPage = (PageModel<Order>)request.getAttribute("orderPage");
							if(orderPage != null){
							
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
								<td class="tl"><%=order.getReciever().getProvince()%> <%=order.getReciever().getCity()%> <%=order.getReciever().getArea()%> <%=order.getReciever().getAddress()%></td>
								<td><%=Dates.formatDateTime_New(order.getDateArrived())%></td>
								<%
									if(order.getUserVO()==null ||order.getUserId() == null && !"".equals(order.getUserId())){//未分派
								%>
										<td></td>
										<td></td>
								<%
									}else{
								%>
										<td><%=order.getUserVO().getRealName()%></td>
										<td><%=order.getUserVO().getLoginName()%></td>
								<%
									}
									if(order.getOrderStatus() == OrderStatus.RETENTION){
								%>
									<td><%=AbnormalStatus.RETENTION.getMessage()%></td>
									<td class="tl">
										<a href="javascript:void(0);" onclick="showCourierDiv('<%=order.getMailNum()%>')" class="orange">重新分派</a>
										<a href="javascript:void(0);" onclick="showOtherSiteDiv('<%=order.getMailNum()%>')" class="orange">转其他站点</a>
										<br>
										<a href="javascript:void(0);" onclick="showExpressCompanyDiv('<%=order.getMailNum()%>')" class="orange">转其他快递</a>
										<a href="javascript:void(0);" onclick="showApplyReturnDiv('<%=order.getMailNum()%>')" class="orange">申请退货</a>
									</td>
								<%
									}else{
								%>
									<td><%=AbnormalStatus.REJECTION.getMessage()%></td>
									<td class="tl">
										<a href="javascript:void(0);" onclick="showOtherSiteDiv('<%=order.getMailNum()%>')" class="orange">转其他站点</a>
										<a href="javascript:void(0);" onclick="showExpressCompanyDiv('<%=order.getMailNum()%>')" class="orange">转其他快递</a>
										<br>
										<a href="javascript:void(0);" onclick="showApplyReturnDiv('<%=order.getMailNum()%>')" class="orange">申请退货</a>
									</td>
								<%
									}
								%>
								<%-- <td>
									<a href="javascript:void(0);" onclick="showCourierDiv('<%=order.getMailNum()%>')" class="orange">重新分派</a>
									<a href="javascript:void(0);" onclick="showOtherSiteDiv('<%=order.getMailNum()%>')" class="orange">转其他站点</a>
									<!-- <a href="javascript:void(0);" onclick="showOtherExpressDiv()" class="orange">转其他快递</a> -->
									<!-- <a href="javascript:void(0);" onclick="showApplyReturnDiv()" class="orange">申请退货</a> -->
								</td> --%>
							</tr>
						<%
							}//for
						}//else
						}%>
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
<!-- S pop -->
<!-- 重新分派面板-开始 -->
<div id="chooseCourier_div" class="j-sel-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" style="display:none;">
	<div class="modal-dialog b-modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header b-modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title tc">重新分派</h4>
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
					<!-- <li class="col-md-12">
						<div class="c-red"><i class="glyphicon glyphicon-exclamation-sign pl15"></i> 请选择派件员</div>
					</li> -->
				</ul>
				<div class="row mt20">
					<span class="col-md-6"><a href="javascript:void(0)" onclick="hideCourierDiv()" class="sbtn sbtn2 g">取消</a></span>
					<span class="col-md-6"><a href="javascript:void(0)" onclick="chooseCourier()" class="sbtn sbtn2 l">确定</a></span>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- 重新分派面板-结束 -->



<!--S 申请退货-->
<div id="apply_return_div" class="j-th-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog b-modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header b-modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title tc">申请退货</h4>
			</div>
			<div class="modal-body b-modal-body">
				选择退货原因:
				<select id="rtnReason" name="rtnReason" class="form-control form-bod">
				</select>
				<textarea id="rtnRemark" name="rtnRemark" class="form-control form-bod mt20" col="3" placeholder="备注"></textarea>
				<div class="row mt20">
					<span class="col-md-6"><a href="javascript:void(0)" onclick="hideApplyReturnDiv()" class="sbtn sbtn2 g">取消</a></span>
					<span class="col-md-6"><a href="javascript:void(0)" onclick="applyReturn()" class="sbtn sbtn2 l">确定</a></span>
				</div>
			</div>
		</div>
	</div>
</div>
<!--E 申请退货-->


<!--S 转其他快递-->
<div id="chooseOtherExpress_div" class="j-turn-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog b-modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header b-modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title tc">转其他快递</h4>
			</div>
			<div class="modal-body b-modal-body">
				快递公司:
				<select id="express_select" name="companyname" class="form-control form-bod">
					<option>请选择快递公司</option>
				</select>
				运单号：<textarea id="mailNum" name="mailNum"  class="form-control form-bod mt20" col="3" placeholder="请输入运单号"></textarea>
				<div class="row mt20">
					<span class="col-md-6"><a href="javascript:void(0)" onclick="hideExpressCompanyDiv()" class="sbtn sbtn2 g">取消</a></span>
					<span class="col-md-6"><a href="javascript:void(0)" onclick="toOtherExpressCompanys()" class="sbtn sbtn2 l">确定</a></span>

				</div>
			</div>
		</div>
	</div>
</div>
<!--E 转其他快递-->


<!--S 转其他站点-->
<div id="chooseOtherSite_div" class="j-site-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog b-modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header b-modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title tc">转其他站点</h4>
			</div>
			<div class="modal-body b-modal-body form-inline-n">
				站点:<select id="site_select" class="form-control form-bod mt10">
					</select>
				<div class="row mt20">
					<span class="col-md-6"><a href="javascript:void(0)" onclick="hideOtherSiteDiv()" class="sbtn sbtn2 g">取消</a></span>
					<span class="col-md-6"><a href="javascript:void(0)" onclick="chooseOtherSite()" class="sbtn sbtn2 l">确定</a></span>
					
				</div>
			</div>
		</div>
	</div>
</div>
<!--E 转其他站点-->
<!-- E pop -->

<!-- 分页js -->
<script src="<c:url value="/resources/javascripts/page/pageBar.js" />"> </script>
<script src="<c:url value="/resources/javascripts/timeUtil.js" />"> </script>

<script type="text/javascript">
//缓存快递员列表和站点列表数据
var courierList = null, siteList = null;
//var staffId = null;
var siteId = null, mailNum = null;

 var expressCompanysList=null;

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

	//初始化快递员列表
	initCourierList();
	//初始化站点列表
	initSiteList();

	//初始化快递公司
	initExpressCompanys();
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
           //	alert("加载分页数据异常，请重试！"); 
           gotoLoginPage(); 
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
		}
	} else{
		tbody.append("<tr><td colspan='7'>没有符合查询条件的数据</td></tr>");
	}
	//更新分页条
	var pageStr = paginNav(dataObject.pageNo, dataObject.totalPages, dataObject.totalCount);
	$("#pagin").html(pageStr);
}
//封装一行的数据
function getRowHtml(data){
	var row = "<tr>";
	row +=  "<td>" + data.mailNum + "</td>";
	row += "<td>" + data.reciever.name + "</td>";
	row += "<td class='tl'>" + data.reciever.province +" "+ data.reciever.city +" "+ data.reciever.area +" "+ data.reciever.address + "</td>";
	row += "<td>" + getDate1(data.dateArrived) + "</td>";
	/* //派件员姓名和电话
	row += "<td>" + data.user.realName + "</td>";
	row += "<td>" + data.user.loginName + "</td>";
	 */
	//派件员==未分派，不需要显示派件员姓名和电话
	 if( data.userId == null || data.userId == ""){
	 row += "<td></td><td></td>";
	 }else{
	 row += "<td>" + data.userVO.realName + "</td>";
	 row += "<td>" + data.userVO.loginName + "</td>";
	 }
	 //状态
	 if(data.orderStatus == "<%=OrderStatus.RETENTION %>" || data.orderStatus==null){
		 row += "<td><%=AbnormalStatus.RETENTION.getMessage()%></td>";
		 row += "<td class='tl' width='190px'><a href='javascript:void(0);' onclick='showCourierDiv(\"" + data.mailNum + "\")' class='orange'>重新分派</a>";
		 row += "<a href='javascript:void(0);' onclick='showOtherSiteDiv(\"" + data.mailNum + "\")' class='orange ml16'>转其他站点</a>";
		row += "<a href='javascript:void(0);' onclick='showExpressCompanyDiv(\"" + data.mailNum + "\")' class='orange ml16'>转其他快递</a>";
		row += "<a href='javascript:void(0);' onclick='showApplyReturnDiv(\"" + data.mailNum + "\")' class='orange ml16'>申请退货</a></td>";
	}else{
		row += "<td><%=AbnormalStatus.REJECTION.getMessage()%></td>";
		row += "<td class='tl' width='190px'><a href='javascript:void(0);' onclick='showOtherSiteDiv(\"" + data.mailNum + "\")' class='orange'>转其他站点1</a>";
		row += "<a href='javascript:void(0);' onclick='showExpressCompanyDiv(\"" + data.mailNum + "\")' class='orange ml16'>转其他快递</a>";
		row += "<br><a href='javascript:void(0);' onclick='showApplyReturnDiv(\"" + data.mailNum + "\")' class='orange'>申请退货</a></td>";
	}
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
       		gotoLoginPage();
       	}    
    });
}
//出错跳转到登录页
function gotoLoginPage() {
	if(window.top==window.self){//不存在父页面
		window.location.href="<c:url value="/login" />"
	}else{
		window.top.location.href="<c:url value="/login" />"
	}
} 

//显示选择派件员div
function showCourierDiv(mailNumStr) {
	mailNum = mailNumStr;
	if(courierList != null){
		loadCouriers(courierList);
	}else{//重新查询所有派件员
		$.ajax({
			type : "GET",  //提交方式  
	        url : "<%=path%>/handleAbnormal/getAllUserList",//路径  
	        data : {},//数据，这里使用的是Json格式进行传输  
	        success : function(dataList) {//返回数据根据结果进行相应的处理  
	        	loadCouriers(dataList);
	        },
	        error : function() {  
	       		//alert("服务器繁忙，请稍后再试！");
	       		gotoLoginPage();
	  		}    
	    });
	}
	$(".j-sel-pop").modal("show");
}
 
//把派件员添加到下拉框中
function loadCouriers(courierList, staffId) {
	var courier_select = $("#courier_select");
	// 清空select  
	courier_select.empty(); 
	if(courierList != null){
	    for(var i = 0; i < courierList.length; i++){
			data = courierList[i];
			courier_select.append("<option value='"+data.id+"'>"+data.realName+"</option>");
		}
	}
}

//隐藏选择派件员div
function hideCourierDiv() {
	mailNum = null;
	$(".j-sel-pop").modal("hide");
}

//重新分派
function chooseCourier() {
	//获取当前页
    var pageIndex = getCurrPage();		
	//保存分派信息
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/handleAbnormal/reDispatch",//路径
		dataType: "json",
        data : {  
            "mailNum" : mailNum, //全局变量
            "userId" : $("#courier_select").val(), //全局变量
            "pageIndex" : pageIndex,//更新列表的参数
            "status" : $("#status").val(), 
            "arriveBetween" : $("#arriveBetween").val() 
        },//数据，这里使用的是Json格式进行传输

        success : function(data) {//返回数据根据结果进行相应的处理  
        	if(data.operFlag == 1){
        		//分派成功，刷新列表！
        		refreshTable(data.orderPage);
        	}else{
				ioutDiv("重新分派失败，请稍后再试！");
        	}
        },
        error : function() {  
       		ioutDiv("服务器繁忙，请稍后再试！");
  		}
    });
    //隐藏面板
    $(".j-sel-pop").modal("hide");
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
        	gotoLoginPage();
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
	       		//alert("服务器繁忙，请稍后再试！");
	       		gotoLoginPage();
	  		}    
	    });
	}
	$("#chooseOtherSite_div").modal("show");
}
//把站点添加到下拉框中
function loadSites(siteList) {
	var site_select = $("#site_select");
	// 清空select  
	site_select.empty(); 
	if(siteList != null){
		for(var i = 0; i < siteList.length; i++){
			data = siteList[i];
			site_select.append("<option value='"+data.id+"'>"+data.name+"</option>");
		}
	}
}
//隐藏转其他站点div
function hideOtherSiteDiv() {
	mailNum = null;
	//$(".j-site-pop").modal("hide");
	$("#chooseOtherSite_div").modal("hide");
}
//转其他站点
function chooseOtherSite() {
	//获取当前页
    var pageIndex = getCurrPage(); 
    $.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/handleAbnormal/toOtherSite",//路径  
        data : {  
            "mailNum" : mailNum, //
            "siteId" : $("#site_select").val(),//站点编号
            "pageIndex" : pageIndex,//更新列表
            "status" : $("#status").val(), 
            "arriveBetween" : $("#arriveBetween").val() 
        },//数据，这里使用的是Json格式进行传输  
        success : function(data) {//返回数据根据结果进行相应的处理  
        	if(data.operFlag == 1){
				//分派成功，刷新列表！
				refreshTable(data.orderPage);
        	}else{
        		ioutDiv("转其他站点失败，请稍后再试！");
        	}
        },
        error : function() {  
       		ioutDiv("服务器繁忙，请稍后再试！");
       		//gotoLoginPage();
  		}    
    });
	//隐藏面板
	$(".j-site-pop").modal("hide");
}
/**************************转其他站点***************结束***********************************/

/*************************************下面的暂时不做*****************************************************/

/************************申请退货***************开始***************************************/
//显示申请退货div
function showApplyReturnDiv(mailNumStr) {
	mailNum = mailNumStr;
	//加载退货原因
	$.ajax({
		type : "GET",  //提交方式
		url : "<%=path%>/handleAbnormal/getRtnReasonList",//路径
		data : {},//数据，这里使用的是Json格式进行传输
		success : function(dataList) {//返回数据根据结果进行相应的处理
			var rtnReasonObj = $("#rtnReason");
			// 清空select
			rtnReasonObj.empty();
			if(dataList != null){
				rtnReasonObj.append("<option value=''>请选择</option>");
				for(var i = 0; i < dataList.length; i++){
					data = dataList[i];
					rtnReasonObj.append("<option value='"+data.status+"'>"+data.message+"</option>");
				}
			}
		},
		error : function() {
			//alert("服务器繁忙，请稍后再试！");
			gotoLoginPage();
		}
	});
	$("#apply_return_div").modal("show");
}

//隐藏申请退货div
function hideApplyReturnDiv() {
	mailNum = null;
	$("#apply_return_div").modal("hide");
}
//确定退货
function applyReturn() {
	//表单校验
	var rtnReason = $("#rtnReason").val();
	var rtnRemark = $("#rtnRemark").val();
	if(rtnReason == "" || rtnReason == null){
		ioutDiv("请选择退货原因");
		return false;
	}else{
		if(rtnReason == "4"){//其他
			if(rtnRemark == "" || rtnRemark == null){
				ioutDiv("请输入备注");
				$("#rtnRemark").focus();
				return false;
			}
		}
	}
	//获取当前页
	var pageIndex = getCurrPage();
	//保存退货信息
	$.ajax({
		type : "POST",  //提交方式
		url : "<%=path%>/handleAbnormal/doReturn?${_csrf.parameterName}=${_csrf.token}",//路径
		data : {
			"mailNum" : mailNum, //
			"rtnReason" : rtnReason,
			"rtnRemark" : rtnRemark,
			"pageIndex" : pageIndex,//更新列表
			"status" : $("#status").val(),
			"arriveBetween" : $("#arriveBetween").val()
		},//数据，这里使用的是Json格式进行传输
		success : function(data) {//返回数据根据结果进行相应的处理
			ioutDiv(data.msg);
			if(data.success){//分派成功，刷新列表！
				refreshTable(data.orderPage);
				//outDiv有延迟，所以页面刷新需要同步延迟
				/*setTimeout(function(){
					refreshTable(data.orderPage);
				},2000);*/
			}
		},
		error : function() {
			ioutDiv("服务器繁忙，请稍后再试！");
		}
	});
	//隐藏面板
	$("#apply_return_div").modal("hide");
}
//获取当前页
function getCurrPage(){
	//获取当前页
	var pageIndex = $(".pagination .active a").html();
	var currPage = 0;
	if(pageIndex != null && pageIndex != ""){
		currPage = parseInt(pageIndex)-1;
	} 
	return currPage;
}

/**********************申请退货**************************结束************************************/


/**********************转为其他快递公司2**************************开始************************************/

var from=null;
var to=null;


//初始化快递公司信息
function initExpressCompanys() {
	//查询所有其他快递公司
	$.ajax({
		type : "GET",  //提交方式
		url : "<%=path%>/handleAbnormal/getExpressCompanys",//路径
		data : {},//数据，这里使用的是Json格式进行传输
		success : function(dataList) {//返回数据根据结果进行相应的处理
			expressCompanysList = dataList;  //查询所有的快递公司数据
		},
		error : function() {
			expressCompanysList = null;
			gotoLoginPage();
		}
	});
}
//显示其他快递公司div
function showExpressCompanyDiv(mailNumStr) {
	mailNum = mailNumStr;
	if(expressCompanysList != null){
		loadExpressCompanys(expressCompanysList);
	}else{//重新查询所有快递公司
		$.ajax({
			type : "GET",  //提交方式
			url : "<%=path%>/handleAbnormal/getExpressCompanys",//路径
			data : {},//数据，这里使用的是Json格式进行传输
			success : function(dataList) {//返回数据根据结果进行相应的处理
				loadExpressCompanys(dataList);
			},
			error : function() {
				ioutDiv("服务器繁忙，请稍后再试！");
			}
		});
	}
	$("#chooseOtherExpress_div").modal("show");
}
//把其他快递公司信息添加到下拉框中
function loadExpressCompanys(expressCompanysList) {
	var express_select = $("#express_select");
	// 清空select
	express_select.empty();
	if(expressCompanysList != null){
		for(var i = 0; i < expressCompanysList.length; i++){
			data = expressCompanysList[i];
			express_select.append("<option value='"+data.id+"'>"+data.companyname+"</option>");
		}
	}
}
//隐藏转其他快递公司div
function hideExpressCompanyDiv() {
	mailNum = null;
	//$(".j-site-pop").modal("hide");
	$("#chooseOtherExpress_div").modal("hide");
}


//转其他快递公司
function toOtherExpressCompanys() {
	//表单校验id="mailNum"
	var companyId = $("#express_select").find("option:selected").val();
	var mailNumNew = $("#mailNum").val();
	if (companyId == "" || companyId == null) {
		ioutDiv("请选择快递公司");
		return false;
	}
	if (mailNumNew == "" || mailNumNew == null) {
		ioutDiv("请填写运单号");
		$("#mailNum").focus();
		return false;
	}
	//获取当前页
	var pageIndex = getCurrPage();

	//转其他快递公司
	$.ajax({
		type: "POST",  //提交方式
		url: "<%=path%>/handleAbnormal/toOtherExpressCompanys",//路径
		data: {
			"mailNum": mailNum, //运单号
			"companyId": companyId,
			"mailNumNew": mailNumNew,//输入的运单号
			"pageIndex": pageIndex,//更新列表
			"status": $("#status").val(),
			"arriveBetween": $("#arriveBetween").val()
		},//数据，这里使用的是Json格式进行传输
		success: function (data) {//返回数据根据结果进行相应的处理
			ioutDiv(data.msg);
			if (data.success) {//分派成功，刷新列表！
				//outDiv有延迟，所以页面刷新需要同步延迟
				refreshTable(data.orderPage);
				/*setTimeout(function () {
				 refreshTable(data.orderPage);
				 }, 2000);*/
			}
		},
		error: function () {
			ioutDiv("服务器繁忙，请稍后再试！");
		}
	});
	//隐藏面板
	$("#chooseOtherExpress_div").modal("hide");
}

/**********************转为其他快递公司2**************************结束************************************/
</script>
</body>
</html>