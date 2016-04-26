<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.OrderStatus" %>
<%@ page import="java.util.List" %>
<%@ page import="com.bbd.saas.utils.Dates" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<jsp:include page="../main.jsp" flush="true" />
	<!-- 显示物流信息 -->
	<jsp:include page="../../common/mailMap.jsp" flush="true" />
</head>
<%
	String proPath = request.getContextPath();
	String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+proPath;
	
%>
<style type="text/css">
.font-bg-color { background-color: #FFED97; }
</style>
<body >

<!-- S content -->
<div class="clearfix b-branch">
	<div class="container-fluid">
		<div class="row">
			<!-- S sidebar -->
			<div class="col-xs-12 col-sm-12 bbd-md-3" style="opacity:0;">
				<ul class="b-sidebar">
					<li class="lv1"><a href="package-arrives.html"><i class="b-icon p-package"></i>包裹到站</a></li>
					<li class="lv1"><a href="tracking-assign.html"><i class="b-icon p-aign"></i>运单分派</a></li>
					<li class="lv1"><a href="exception-processing.html"><i class="b-icon p-error"></i>异常件处理</a></li>
					<li class="lv1 side-cur"><a href="data-query.html"><i class="b-icon p-query"></i>数据查询</a></li>
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
	  					<div class="row pb20">
	  						<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
	  							<label>状态：</label>
	  							<select id="status" name="status" class="form-control form-con-new">
	  								<%=OrderStatus.Srcs2HTML(-1)%>
	  							</select>
	  						</div>
	  						<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4 pad0">
	  							<label>到站时间：</label>
	  							<input id="arriveBetween" name="arriveBetween" value="${arriveBetween}" type="text" placeholder="请选择到站时间" class="form-control"  />
	  						</div>
	  						<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4 pad0">
	  							<label>运单号：</label>
	  							<input id="mailNum" name="mailNum" type="text" placeholder="请输入运单号" class="form-control"  />
	  						</div>
	  					</div>
	  					<div class="row pb20">
	  						<div class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
	  							<a href="javascript:void(0)" class="ser-btn l" onclick="gotoPage(0);"><i class="b-icon p-query p-ser"></i>查询</a>
	  							<a href="javascript:void(0)" class="ser-btn d ml16" onclick="exportData();"><i class="glyphicon glyphicon-off f16 mr10"></i>导出</a>
	  							
	  						</div>
	  					</div>
	  				</div>
				</form>
				<!-- 用于导出 -->
				<form action="<%=request.getContextPath()%>/dataQuery/exportToExcel" method="get" id="exptForm">
					<input id="status_expt" name="status" type="hidden" />
					<input id="arriveBetween_expt" name="arriveBetween_expt" type="hidden" />
					<input id="mailNum_expt" name="mailNum" type="hidden" />
				</form>				
				<!-- E 搜索区域 -->
				<div class="tab-bod mt20">
					<!-- S table -->
					<div class="table-responsive">
  					<table class="table">
  						<thead>
  							<tr>
  								<th>包裹号</th>
								<th>运单号</th>
								<th>订单号</th>
								<th>来源</th>
								<th>收货人</th>
								<th>收货人电话</th>
								<th width="15%">收货人地址</th>
								<th>司机取货时间</th>
								<th>预计到站时间</th>
								<th>到站时间</th>
								<th>派送员</th>
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
									<td><%=Dates.formatDateTime_New(order.getDateDriverGeted())%></td>
									<td><%=Dates.formatDate2(order.getDateMayArrive())%></td>
									<td><%=Dates.formatDateTime_New(order.getDateArrived())%></td>
									<%
										if(order.getUserId() == null || "".equals(order.getUserId())){//未分派
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
									<td><a href="javascript:void(0);" class="orange" onclick="LoadMailAndMapData('<%=order.getMailNum()%>','<%=Order.getExpressList(order.getExpresses())%>')">查看物流信息 </a></td>
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


<!--S 显示物流地图-->
<!-- <div id="mailMap" class="j-site-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog b-modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header b-modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title tc">转其他站点</h4>
			</div>
			<div class="modal-body b-modal-body">
				站点:<select id="site_select" class="form-control form-bod">
					</select>
				<div class="row mt20">
					<span class="col-md-6"><a href="javascript:void(0)" onclick="hideOtherSiteDiv()" class="sbtn sbtn2 g">取消</a></span>
					<span class="col-md-6"><a href="javascript:void(0)" onclick="chooseOtherSite()" class="sbtn sbtn2 l">确定</a></span>
					
				</div>
			</div>
		</div>
	</div>
</div> -->
<!--E 转其他站点-->

<!-- S footer -->
<footer class="pos-footer tc">
    <em class="b-copy">京ICP备 465789765 号 版权所有 &copy; 2016-2020 棒棒达       北京棒棒达科技有限公司</em>
</footer>
<!-- E footer -->
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
           //	alert("加载分页数据异常！");  
           window.location.href = "<%=path%>/login";
      	}    
    });	
}	


//封装一行的数据
function getRowHtml(data){
	var mailNum = $("#mailNum").val();
	var row = "<tr>";
	row +=  "<td>" + data.parcelCode + "</td>";
	if(mailNum == null || mailNum == ""){//没有按照yun查，不需要着色
		row += "<td>" + data.mailNum + "</td>";
	}else{
		row += "<td>" + data.mailNum.replace(mailNum, "<span class='font-bg-color'>" + mailNum + "</span>") + "</td>";
	}
	row += "<td>" + data.orderNo + "</td>";
	row += "<td>" + getSrcName(data.src) + "</td>";
	row += "<td>" + data.reciever.name + "</td>";
	row += "<td>" + data.reciever.phone + "</td>";
	row += "<td>" + data.reciever.province + data.reciever.city + data.reciever.area + data.reciever.address + "</td>";
	row += "<td>" + getDate1(data.dateDriverGeted) + "</td>";
	row += "<td>" + getDate2(data.dateMayArrive) + "</td>";
	row += "<td>" + getDate1(data.dateArrived) + "</td>";
	//派件员==未分派，不需要显示派件员姓名和电话
	if(data.userId == null || data.userId == ""){
		row += "<td></td><td></td>";
	}else{
		row += "<td>" + data.userVO.realName + "</td>";
		row += "<td>" + data.userVO.loginName + "</td>";
	}
	//状态
	row += "<td>" + getStatus(data.orderStatus) + "</td>";
	row += "<td><a href='javascript:void(0);' onclick='LoadMailAndMapData(\"" + data.mailNum + "\", \"" + data.expresses + "\")'  class='orange'>查看物流信息 </a>";
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
	//console.log("form ===" + $("#exptForm").action + " arrive==" + $("#arriveBetween").val());
}	
	
	
	
</script>
</body>
</html>