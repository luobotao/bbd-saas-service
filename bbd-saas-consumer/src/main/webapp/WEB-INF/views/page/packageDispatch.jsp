<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.bbd.saas.enums.DispatchStatus" %>
<%@ page import="com.bbd.saas.enums.OrderStatus" %>
<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.utils.Constants" %>
<%@ page import="com.bbd.saas.utils.Dates" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="java.util.Date" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
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
	  					<div class="row">
	  						<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
	  							<label>状态：</label>
	  							<select id="status" name="status" class="form-control form-con-new">
	  								<%=DispatchStatus.Srcs2HTML(-1)%>
	  							</select>
	  						</div>
	  						<div class="form-group col-xs-12 col-sm-6 col-md-5 col-lg-5">
	  							<label>到站时间：　</label>
	  							<input id="arriveBetween" name="arriveBetween" value="${arriveBetween}" type="text" placeholder="请选择到站时间" class="form-control c-disable"  />
	  						</div>
	  						<div class="form-group col-xs-12 col-sm-6 col-md-3 col-lg-3">
	  							<a href="javascript:void(0)" onclick="queryData(0);" class="ser-btn l"><i class="b-icon p-query p-ser"></i>查询</a>
	  						</div>
	  					</div>
	  					<div class="b-line"></div>
	  					<div class="row pb20">
	  						<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
								<span onclick="showCourierDiv()" class="ser-btn l" >选择派件员</span>
	  							<%--<a href="javascript:void(0)" onclick="showCourierDiv()" class="ser-btn d">选择派件员</a>
	  							--%>
								<span class="ft16 pt20 tip-info" id="courierName"></span>
	  							<input id="courierId" type="hidden" value="" /> 
	  						</div>
	  						
	  						<div class="form-group col-xs-12 col-sm-6 col-md-6 col-lg-6">
	  							<label>扫描运单号：</label>
	  							<input id="mailNum" name="mailNum" type="text" placeholder="扫描运单号,按回车键分派" class="form-control" onkeypress="enterPress(event)" />
								<span onclick="dispatch()" class="ser-btn l ml16">确定分派</span>
	  						</div>
	  					</div>
	  				</div>
				</form>
				<!-- E 搜索区域 -->
				<!-- S button -->
				<div class="clearfix mt20">
					<span onclick="batchDispatchBtn()" class="ser-btn l" >批量分派</span>
				</div>
				<!-- E button -->
  			<div class="tab-bod mt20">
  				<!-- S table -->
  				<div class="table-responsive">
  					<table id="orderTable" class="table">
  						<thead>
  							<tr>
								<c:choose>
									<c:when test="${areaCode != null && areaCode == Constants.NO_SITE_AREACODE}">
										<th width="4%"><input type="checkbox"  id="selectAll" name="selectAll" class="j-sel-all" /></th>
									</c:when>
									<c:otherwise><th width="4%"></th></c:otherwise>
								</c:choose>
								<th>运单号</th>
								<th width="6%">收货人</th>
								<th width="20%">收货人地址</th>
								<th>到站时间</th>
								<th>派送员姓名</th>
								<th>派送员手机</th>
								<th>状态</th>
								<th>操作</th>
							</tr>
  						</thead>
  						<tbody id="dataList">
						<%
							PageModel<Order> orderPage = (PageModel<Order>)request.getAttribute("orderPage");
							if(orderPage.getDatas() == null){
						%>
							<tr>
								<td colspan="9">没有符合查询条件的数据</td>
							</tr>
						<%
							}else{
								for(Order order : orderPage.getDatas()){
						%>
						<%
							if(order.getOrderStatus() == OrderStatus.NOTDISPATCH){//未分派
						%>
							<%
								long arriveDays = Dates.daysBetween(order.getDateArrived(), new Date());
								if(arriveDays <= 0){//不足一天
							%>
								<tr>
							<%
								}else if(arriveDays == 1){//超过一天
							%>
								<tr class="bg-yellow">
							<%
								}else{//超过2天
							%>
								<tr class="bg-pink">
							<%
								}
							%>
							<td><input type="checkbox" value="<%=order.getMailNum()%>" name="id"></td>
						<%
							}else{//已分派
						%>
						<tr><td></td>
						<%
							}
						%>
								<td><%=order.getMailNum()%></td>
								<td><%=order.getReciever().getName()%></td>
								<td class="tl"><%=order.getReciever().getProvince()%> <%=order.getReciever().getCity()%> <%=order.getReciever().getArea()%> <%=order.getReciever().getAddress()%></td>
								<td><%=Dates.formatDateTime_New(order.getDateArrived())%></td>
								<%
									if(order.getUserVO()==null){//未分派||派件员没有查询到
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
									if(order.getOrderStatus() == OrderStatus.NOTDISPATCH){
								%>
									<td><em class="orange"><%=DispatchStatus.NOTDISPATCH.getMessage()%></em></td>
									<td><a href="javascript:void(0);" onclick="showSuperAreaDiv('<%=order.getMailNum()%>')" class="orange" data-toggle="modal" data-target="#superAreaDiv">设为超区件</a></td>
								<%
									}else{
								%>
									<td><em class="c-green"><%=DispatchStatus.DISPATCHED.getMessage()%></em></td>
									<td>
										<a href="javascript:void(0);" onclick="showSuperAreaDiv('<%=order.getMailNum()%>')" class="orange" data-toggle="modal" data-target="#superAreaDiv">设为超区件</a>
										<a href="javascript:void(0);" onclick="checkOrderStatus('<%=order.getMailNum()%>')" class="orange">取消分派</a>
									</td>
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
					<div class="clearfix pad20">
						<!-- S button -->
						<div class="clearfix fl">
							<c:if test="${areaCode != null && areaCode == Constants.NO_SITE_AREACODE}">
								<a href="javascript:void(0)" onclick="batchSuperAreaBtnWithNoCheck()" data-toggle="modal" class="ser-btn l">批量设为超区件</a>
							</c:if>
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
							<i class="p-icon p-patch"></i>
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
<!-- 运单分派面板-结束 -->

<!--S 取消分派-->
<div class="j-pl-pop modal fade" id="confirmDiv" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal_wrapper">
		<div class="modal-dialog b-modal-dialog">
			<div class="modal-content">
				<div class="modal-header b-modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					<h4 class="modal-title  tc" id="confirmTitle">确 认</h4>
				</div>
				<div class="modal-body b-modal-bodyS tc">
					<em class="f16" id="confirmBody">确认将该订单设置为超区件？</em>
					<div class="row mt20">
						<span class="col-md-6">
							<button type="button" class="sbtn sbtn2 g" data-dismiss="modal" class="close">取消</button>
						</span>
						<span class="col-md-6">
							<button  type="button" class="sbtn sbtn2 l" onclick="doOperation()">确认</button>
						</span>
					</div>
				</div>



			</div>
		</div>
	</div>
</div>
<!--E 取消分派-->

<!--S 订单已被处理-->
<div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="orderStatusLabel" id="orderStatusModal"
	 aria-hidden="true">
	<div class="modal-dialog b-modal-dialog middleS" role="document">
		<div class="modal-content">
			<div class="modal-header b-modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title tc">提 示</h4>
			</div>
			<div class="modal-body b-modal-bodyS">
				<div class="pop-tip2">
					<i class="mr10">
						<img src="<c:url value="/resources/images/per_erroricon.png" />" />
					</i>
					<em class="f16" id="modal_body"></em>
				</div>

				<div class="clearfix mt20 tc">
					<button  type="button" class="ser-btn l w160" onclick="closeModal('orderStatusModal')">确 定</button>
				</div>
			</div>
		</div>
	</div>
</div>
<!--E 订单已被处理-->

<jsp:include page="superArea.jsp" flush="true" />

<script src="<c:url value="/resources/javascripts/timeUtil.js" />"> </script>

<script type="text/javascript">
var courierIsLoadSuccess = 0;
var status = ""; 
var arriveBetween = "";
var operFlag = ""; //操作类型（cancelDispatch：取消分派；）
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
	//选择框样式
	$("input[type='checkbox']").iCheck({
		checkboxClass : 'icheckbox_square-blue'
	});
	$("#selectAll").on('ifUnchecked', function() {
		$("input[type='checkbox']", "#orderTable").iCheck("uncheck");
	}).on('ifChecked', function() {
		$("input[type='checkbox']", "#orderTable").iCheck("check");
	});

	//初始化派件员下拉框（快递员）
	initCourier();  
	//扫描运单号  focus事件
	$("#mailNum").focus(function(){
		if($("#courierId").val() == null || $("#courierId").val() == ""){
			ioutDiv("请选择派件员！");
	  	}
	});
	//扫描运单号--把快递分派给派件员--边输入边改变
	$("#mailNum").on('input',function(e){

	});
});

//回车事件--运单分派
function enterPress(e){
	if(!e) e = window.event;//火狐中是 window.event
	if((e.keyCode || e.which) == 13){
		//分派
		dispatch();
	}
}
	
// 运单分派  
function dispatch() {
	//未选择派件员
	if($("#courierId").val() == null || $("#courierId").val() == ""){
		ioutDiv("请选择派件员！");
		return false;
	}
	//未选择派件员
	var mailNum = $("#mailNum").val();
	if(mailNum == null || mailNum == ""){
		ioutDiv("请扫描或输入运单号");
		return false;
	}
	//已选择派件员，把快递分派给派件员
	$.ajax({
        type : "GET",  //提交方式  
		url : "<%=request.getContextPath()%>/packageDispatch/dispatch",//路径  
		data : {
			"pageIndex" : parseInt($(".pagination .active").text())-1,
			"status" : $("#status").val(),
			"arriveBetween" : $("#arriveBetween").val(),
			"mailNum" : $("#mailNum").val(),
		    "courierId" : $("#courier_select").val()
		},//数据，这里使用的是Json格式进行传输  
		dataType: "json",
		success : function(data) {//返回数据根据结果进行相应的处理  
		   	if (data.operFlag == 1) {
				ioutDiv($("#mailNum").val() + "运单分派成功！");
		    	//刷新列表
		    	refreshTable(data.orderPage);
		    }else if(data.operFlag == 0){
				ioutDiv("【异常扫描】不存在此订单！");
		    }else if(data.operFlag == 2){
				ioutDiv("重复扫描，此运单已经分派过啦！");
		    }else if(data.operFlag == 3){
		    	ioutDiv($("#mailNum").val() + "运单分派失败，请重试！");
		    }else if(data.operFlag == 4){
		    	ioutDiv("只有状态为未分派、滞留的运单才能分派！");
		    }else{
				ioutDiv("分派失败，请检查运单状态是否正确");
			}
		},
		error : function() {  
		}
    });
	selectInputTxt("mailNum");
}  
//加载带有查询条件的指定页的数据
function queryData(pageIndex) {
	//清除派件员的查询条件
	$("#courierId").val("");
	$("#courierName").text("");
	status = $("#status").val();
  	arriveBetween = $("#arriveBetween").val();
	//查询所有派件员
	gotoPage(pageIndex);
}	
 
//加载带有查询条件的指定页的数据
function gotoPage(pageIndex) {
	if(pageIndex == null){
		pageIndex = parseInt($(".pagination .active").text())-1;
	}
	//查询所有派件员
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/packageDispatch/getList",//路径  
        data : {  
            "pageIndex" : pageIndex,
            "status" : $("#status").val(),
            "arriveBetween" : $("#arriveBetween").val(),
        },//数据，这里使用的是Json格式进行传输
        success : function(dataObject) {//返回数据根据结果进行相应的处理 
            refreshTable(dataObject);
		},
        error : function() {  
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
		tbody.append("<tr><td colspan='9'>没有符合查询条件的数据</td></tr>");
		//datastr += "<tr><td colspan='7'>没有符合查询条件的数据</td></tr>";
	}
	//tbody.html(datastr);
	//更新分页条
	var pageStr = paginNav(dataObject.pageNo, dataObject.totalPages, dataObject.totalCount);
	$("#pagin").html(pageStr);
	//样式
	$("input[type='checkbox']").iCheck({
		checkboxClass : 'icheckbox_square-blue'
	});
	$("#selectAll").on('ifUnchecked', function() {
		$("input[type='checkbox']", "#orderTable").iCheck("uncheck");
	}).on('ifChecked', function() {
		$("input[type='checkbox']", "#orderTable").iCheck("check");
	});
}
//封装一行的数据
function getRowHtml(data){
	var row = "";
	if(data.orderStatus == "<%=OrderStatus.NOTDISPATCH %>" || data.orderStatus==null){
		var arriveDays = getDayDiff(getDate1(data.dateArrived));
		if(arriveDays <= 0){//不足一天
			row += "<tr>";
		}else if(arriveDays == 1){//超过一天
			row += "<tr class='bg-yellow'>";
		}else{//超过2天
			row += "<tr class='bg-pink'>";
		}
		row +=  "<td><input type='checkbox' value='" + data.mailNum + "' name='id'></td>";
	}else{
		row +=  "<tr><td></td>";
	}
	row +=  "<td>" + data.mailNum + "</td>";
	row += "<td>" + data.reciever.name + "</td>";
	row += "<td class='tl'>" + data.reciever.province + data.reciever.city + data.reciever.area + data.reciever.address + "</td>";
	row += "<td>" + getDate1(data.dateArrived) + "</td>";
	//派件员==未分派，不需要显示派件员姓名和电话
	if(data.userVO == null){//未分派||派件员没有查询到
		row += "<td></td><td></td>";
	}else{
		row += "<td>" + data.userVO.realName + "</td>";
		row += "<td>" + data.userVO.loginName + "</td>";
	}
	//状态
	if(data.orderStatus == "<%=OrderStatus.NOTDISPATCH %>" || data.orderStatus==null){
		row += "<td><em class='orange'><%=DispatchStatus.NOTDISPATCH.getMessage()%></em></td>";
		row += "<td><a href='javascript:void(0);' onclick='showSuperAreaDiv(\"" + data.mailNum + "\")' class='orange' data-toggle='modal' data-target='#superAreaDiv'>设为超区件</a></td>";
	}else{
		row += "<td><em class='c-green'><%=DispatchStatus.DISPATCHED.getMessage()%></em></td>";
		row += "<td><a href='javascript:void(0);' onclick='showSuperAreaDiv(\"" + data.mailNum + "\")' class='orange' data-toggle='modal' data-target='#superAreaDiv'>设为超区件</a>" +
				"<a href='javascript:void(0);' onclick='checkOrderStatus(\"" + data.mailNum + "\")' class='orange ml16'>取消分派</a></td>";
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
	$("#courierName").text("已选择:" + $("#courier_select").find("option:selected").text());
	$("#courierId").val($("#courier_select").val());
	//设置分页查询条件
	status = "";
  	arriveBetween = ""; 
	$("#chooseCourier_div").modal("hide");
}

/******************************************************** 取消操作 *****************************************************************/
	//显示操作提示框
	function showConfirmDiv(mailNumStr, operType, info) {
		mailNum = mailNumStr;
		operFlag = operType;
		$("#confirmBody").html(info);
	}
	//操作确认按钮
	function doOperation() {
		if(operFlag == "cancelDispatch"){//取消分派
			doCancel();
		}else if(operFlag == "batchDispatch"){//批量分派
			doBatchDispatch();
		}
	}
	//检查订单状态，只有订单处于已分派时，才能取消分派
	function checkOrderStatus(mailNumStr) {
		$.ajax({
			type : "POST",  //提交方式
			url : "<c:url value="/packageDispatch/checkOrderStatus?${_csrf.parameterName}=${_csrf.token}" />",//路径
			data : {
				"mailNum" : mailNumStr
			},//数据，这里使用的是Json格式进行传输
			success : function(data) {//返回数据根据结果进行相应的处理
				if(data != null){
					if(data.code == 1){//订单状态为已分派
						//执行取消分派操作
						showConfirmDiv(mailNumStr, 'cancelDispatch', '确定取消分派？');
						$("#confirmDiv").modal("show");
					}else if(data.code == 0){//订单状态不为已分派
						$("#orderStatusModal").modal("show");
						$("#modal_body").html("取消分派失败！该订单已被" + data.msg + "!");
					}else{//datat.code == -1,订单不存在
						ioutDiv(data.msg);
					}
				}
			},
			error : function() {
				ioutDiv("服务器繁忙，请稍后再试！");
			}
		});
		$("#confirmDiv").modal("hide");
	}

	//取消分派
	function doCancel() {
		$.ajax({
			type : "POST",  //提交方式
			url : "<c:url value="/packageDispatch/cancelDispatch?${_csrf.parameterName}=${_csrf.token}" />",//路径
			data : {
				mailNum : mailNum
			},//数据，这里使用的是Json格式进行传输
			success : function(data) {//返回数据根据结果进行相应的处理
				if(data != null){
					ioutDiv(data.msg);
					if(data.success){//取消分派成功
						gotoPage();
					}
				}
			},
			error : function() {
				ioutDiv("服务器繁忙，请稍后再试！");
			}
		});
		$("#confirmDiv").modal("hide");
	}
	//操作确认按钮
	function closeModal(modalId) {
		$("#"+modalId).modal("hide");
	}
/********************************************************** 取消操作 **************************************************************/
/********************************************************** 批量分派操作 **************************************************************/
var ids = [];
//检查订单状态，只有订单处于已分派时，才能取消分派
function batchDispatchBtn() {
	//未选择派件员
	if($("#courierId").val() == null || $("#courierId").val() == ""){
		ioutDiv("请选择派件员！");
		return false;
	}
	var checkids = $('input[name="id"]:checked');
	ids = [];
	if(checkids.length) {
		$.each(checkids, function(i, n){
			if(!$(n).closest('tr').find('.status .label').hasClass('label-success')) {
				ids.push($(n).val());
			}
		});
	}
	if(ids.length == 0){
		ioutDiv('请选择运单！');
		return false;
	}
	showConfirmDiv(JSON.stringify(ids), 'batchDispatch', '确认批量分派【' + ids.length + '单】给【' + $("#courierName").text().replace("已选择:", "") + "】吗？");
	$("#confirmDiv").modal("show");
}
//批量分派操作
function doBatchDispatch() {
	var url = "<c:url value="/packageDispatch/batchDispatch?${_csrf.parameterName}=${_csrf.token}" />";
	$.ajax({
		url: url,
		type: 'POST',
		cache: false,
		dataType: "json",
		data: {
			"pageIndex" : parseInt($(".pagination .active").text())-1,//查询条件
			"status" : $("#status").val(),
			"arriveBetween" : $("#arriveBetween").val(),
			"mailNums" : mailNum,//批量分派Id
			"courierId" : $("#courier_select").val()//派件员
		},
		success: function(data){
			if(data != null){
				var msg = data.msg;
				if (msg == "-1") {
					ioutDiv("派件员不存在！-1");
				}else if(msg == "0"){
					ioutDiv("所有运单均不存在！0");
				}else{
					if(msg == ""){
						ioutDiv("批量分派成功！");//所有运单均分派成功！
					}else{
						msg = msg.replace(new RegExp(/(#0#)/g),'运单不存在');
						msg = msg.replace(new RegExp(/(#2#)/g),'重复分派');
						msg = msg.replace(new RegExp(/(#3#)/g),'分派失败');
						msg = msg.replace(new RegExp(/(#4#)/g),'只有状态为未分派、滞留的运单才能分派');
						ioutDiv(msg, 12000);
					}
					if(data.orderPage != null){
						//刷新列表
						refreshTable(data.orderPage);
					}
				}
			}
		},
		error: function(){
			ioutDiv('服务器繁忙，请稍后再试！');
		}
	});
	$("#confirmDiv").modal("hide");
}
/********************************************************** 批量分派取消操作 **************************************************************/

</script>
</body>
</html>