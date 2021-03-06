<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.OrderStatus" %>
<%@ page import="com.bbd.saas.utils.Dates" %>
<%@ page import="com.bbd.saas.enums.ExpressStatus" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<jsp:include page="../main.jsp" flush="true" />
</head>
<%
	String proPath = request.getContextPath();
	String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+proPath;
%>
<style type="text/css">
.font-bg-color { background-color: #FFED97; }
</style>
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
						<div class="row" >
							<jsp:include page="../control/siteStatusControl.jsp" flush="true" />
						</div>
	  					<div class="row">
	  						<div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
								<div class="form-group pb20">
									<label>运单状态：</label>
									<div class="crt-s w400">
										<div class="c-sel j-sel-input">
											<span class="show-ele j-empty">请选择</span>
											<div class='showA'><ul class='c-show cityshow' id="statusOpts"></ul></div>
										</div>
										<div class="all-area all-area1 pm-dn">
											<!-- S 1 -->
											<div class="pv-bg clearfix">
												<%--<input id="statusName" type="text" class="sel-input" placeholder="请输入运单状态" />--%>
												<div class="l-sel-p">
													<ul class="pv-part" id="optionList2">
														<%=OrderStatus.Srcs2MultiHTML(new Integer[] {-1})%>
													</ul>
												</div>
											</div>
											<!-- E 1 -->
										</div>
									</div>
									<%--<select id="status" name="status" class="form-control form-con-new">
                                        <%=OrderStatus.Srcs2HTML(-1)%>
                                    </select>--%>
								</div>
								<div class="form-group plr">
									<label>站点入库时间：</label>
									<input id="arriveBetween" name="arriveBetween" value="${arriveBetween}" type="text" placeholder="请选择站点入库时间" class="form-control c-disable"  />
								</div>
								<div class="form-group pb20">
									<label>　运单号：</label>
									<input id="mailNum" name="mailNum" type="text" placeholder="请输入运单号" class="form-control"  />
								</div>
								<div class="form-group ml6 pb20">
									<a href="javascript:void(0)" class="ser-btn l" onclick="gotoPage(0);"><i class="b-icon p-query p-ser"></i>查询</a>
									<a href="javascript:void(0)" class="ser-btn d ml16" onclick="exportData();"><i class="glyphicon glyphicon-off f16 mr10"></i>导出</a>
								</div>

	  						</div>

	  					</div>
	  				</div>
				</form>
				<!-- 用于导出 -->
				<form action="<%=request.getContextPath()%>/mailQuery/exportToExcel" method="get" id="exptForm">
					<input id="prov_expt" name="prov" type="hidden" />
					<input id="city_expt" name="city" type="hidden" />
					<input id="area_expt" name="area" type="hidden" />
					<input id="siteStatus_expt" name="siteStatus" type="hidden" />
					<input id="areaFlag_expt" name="areaFlag" type="hidden" />
					<input id="areaCode_expt" name="areaCodeStr" type="hidden" />
					<input id="status_expt" name="statusStr" type="hidden" />
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
								<th>站点名称</th>
								<th>运单号</th>
								<th width="7%">收货人</th>
								<th>收货人电话</th>
								<th width="15%">收货人地址</th>
								<th>司机取货时间</th>
								<th>预计站点入库时间</th>
								<th>站点入库时间</th>
								<th>签收时间</th>
								<th>派送员</th>
								<th>派送员手机</th>
								<th>运单状态</th>
								<th>操作</th>
  							</tr>
  						</thead>
  						<tbody id="dataList">
  							<%
								PageModel<Order> orderPage = (PageModel<Order>)request.getAttribute("orderPage");
								if(orderPage==null || orderPage.getDatas() == null){
							%>
								<tr>
									<td colspan="16">没有符合查询条件的数据</td>
								</tr>
							<%
								}else{
									for(Order order : orderPage.getDatas()){
							%>
								<tr>
									<td><%=order.getAreaName()%></td>
									<td><%=order.getMailNum()%></td>
									<td><%=order.getReciever().getName()%></td>
									<td><%=order.getReciever().getPhone()%></td>
									<td class="tl"><%=order.getReciever().getProvince()%> <%=order.getReciever().getCity()%> <%=order.getReciever().getArea()%> <%=order.getReciever().getAddress()%></td>
									<td><%=Dates.formatDateTime_New(order.getDateDriverGeted())%></td>
									<td><%=Dates.formatDate2(order.getDateMayArrive())%></td>
									<td><%=Dates.formatDateTime_New(order.getDateArrived())%></td>
									<%-- 签收时间  start --%>
									<%
										if(order.getOrderStatus() != null && order.getOrderStatus() == OrderStatus.SIGNED){
									%>
										<td><%=Dates.formatDateTime_New(order.getDateUpd())%></td>
									<%
										}else{
									%>
										<td></td>
									<%
										}
									%>
									<%-- 签收时间  end --%>
									<%
										if(order.getUserId() == null || "".equals(order.getUserId())){//未分派
									%>
										<td></td>
										<td></td>
									<%
										}else if(order.getPostmanUser() != null && !"".equals(order.getPostmanUser())){//分派
									%>
										<td><%=order.getPostmanUser()%></td>
										<td><%=order.getPostmanPhone()%></td>
									<%
										}else if(order.getUserVO() != null){//分派
									%>
										<td><%=order.getUserVO().getRealName()%></td>
										<td><%=order.getUserVO().getLoginName()%></td>
									<%
										}else{
									%>
										<td></td>
										<td></td>
									<%
										}
										//样式
										if(order.getOrderStatus() == null || order.getOrderStatus() == OrderStatus.NOTARR){//未到站--
									%>
											<td><em class="l-blue">未到站</em></td>
									<%
										}else if(order.getOrderStatus() == OrderStatus.NOTDISPATCH){
									%>
											<td><em class="orange">未分派</em></td>
									<%
										}else if(order.getOrderStatus() == OrderStatus.DISPATCHED){
									%>
											<td><em class="c-green">已分派</em></td>
									<%
										}else if(order.getOrderStatus() == OrderStatus.RETENTION){
									%>
											<td><em class="purple">滞留</em></td>
									<%
										}else if(order.getOrderStatus() == OrderStatus.REJECTION){
									%>
											<td><em class="d-red">拒收</em></td>
									<%
										}else if(order.getOrderStatus() == OrderStatus.SIGNED){
									%>
											<td><em class="black">已签收</em></td>
									<%
										}else if(order.getOrderStatus() == OrderStatus.TO_OTHER_EXPRESS){
									%>
											<td><em class="d-blue">已转其他快递</em></td>
									<%
										}else if(order.getOrderStatus() == OrderStatus.APPLY_RETURN){
									%>
											<td><em class="brown">申请退货</em></td>
									<%
										}else if(order.getOrderStatus() == OrderStatus.RETURNED){
									%>
											<td><em class="l-green">退货完成</em></td>
									<%
										}
									%>
									<td><a href="<%=path%>/mailQuery/getOrderMail?areaCode=<%=order.getAreaCode()%>&mailNum=<%=order.getMailNum()%>" target="_blank" class="orange">查看物流信息 </a></td>
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
<div id="mailMap" class="j-site-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
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
</div>
<!--E 转其他站点-->

<!-- S footer -->
<footer class="pos-footer tc">
    <em class="b-copy">京ICP备 465789765 号 版权所有 &copy; 2016-2020 棒棒达       北京棒棒达科技有限公司</em>
</footer>
<!-- E footer -->
<!-- S 省市区站点选择控件 -->
<script type="text/javascript">
	var siteUrl = "<c:url value="/site/getQuerySiteList"/>";
	var inputName = null;
	var isSiteId = false;
</script>
<script src="<c:url value="/resources/javascripts/siteStatusControl.js" />"> </script>
<%--<script src="<c:url value="/resources/javascripts/statusControl.js" />"> </script>--%>
<!-- E 省市区站点选择控件  -->
<script type="text/javascript">
$(document).ready(function() {
	//显示分页条
	var pageStr = paginNav(<%=orderPage.getPageNo()%>, <%=orderPage.getTotalPages()%>, <%=orderPage.getTotalCount()%>);
	$("#pagin").html(pageStr);
	//初始化站点入库时间框
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
	/*var areaCodeStr = getAreaCodeStr();
	console.log(areaCodeStr);*/
	/*console.log("siteStatus==="+$("#siteStatus").val());
	console.log("areaFlag==="+$("#areaFlag").val());*/
	//查询所有派件员
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/mailQuery/getList",//路径
        data : {
			"prov" : $("#addr_control .prov").val(),
			"city" :  $("#addr_control .city").val(),
			"area" :  $("#addr_control .dist").val(),
            "pageIndex" : pageIndex,
			"siteStatus" : $("#siteStatus").val(),//站点状态
			"areaFlag" : $("#areaFlag").val(),//配送区域
			"areaCodeStr" : getAreaCodeStr(),//站点编号集合
            "statusStr" : getAreaCodeStr("statusOpts"),
            "arriveBetween" : $("#arriveBetween").val(),
            "mailNum" : $("#mailNum").val() 
        },//数据，这里使用的是Json格式进行传输
        success : function(dataObject) {//返回数据根据结果进行相应的处理 
            var tbody = $("#dataList");
            var dataList = dataObject.datas;
			var datastr = "";
			if(dataList != null){
				for(var i = 0; i < dataList.length; i++){
					datastr += getRowHtml(dataList[i]);
				}
			}
			tbody.html(datastr);
			//更新分页条
			var pageStr = paginNav(pageIndex, dataObject.totalPages, dataObject.totalCount);
			$("#pagin").html(pageStr);
		},
        error : function() {
		}
    });	
}	


//封装一行的数据
function getRowHtml(data){
	var mailNum = $("#mailNum").val();
	var row = "<tr>";
	row += "<td>" + data.areaName + "</td>";
	if(mailNum == null || mailNum == ""){//没有按照yun查，不需要着色
		row += "<td>" + data.mailNum + "</td>";
	}else{
		row += "<td>" + data.mailNum.replace(mailNum, "<span class='font-bg-color'>" + mailNum + "</span>") + "</td>";
	}
	row += "<td>" + data.reciever.name + "</td>";
	row += "<td>" + data.reciever.phone + "</td>";
	row += "<td class='tl'>" + data.reciever.province + data.reciever.city + data.reciever.area + data.reciever.address + "</td>";
	row += "<td>" + getDate1(data.dateDriverGeted) + "</td>";
	row += "<td>" + getDate2(data.dateMayArrive) + "</td>";
	row += "<td>" + getDate1(data.dateArrived) + "</td>";
	<%-- 签收时间  start --%>
	if(data.orderStatus != null && data.orderStatus == "<%=OrderStatus.SIGNED %>"){
		row += "<td>" + getDate1(data.dateUpd) + "</td>";
	}else{
		row += "<td></td>";
	}
	<%-- 签收时间  end --%>
	//派件员==未分派，不需要显示派件员姓名和电话
	if(data.userId == null){//未分派||派件员没有查询到
		row += "<td></td><td></td>";
	}else if(data.postmanUser != null && data.postmanUser != ""){
		row += "<td>" + data.postmanUser + "</td>";
		row += "<td>" + data.postmanPhone + "</td>";
	}else if(data.userVO != null && data.userVO != ""){
		row += "<td>" + data.userVO.realName + "</td>";
		row += "<td>" + data.userVO.loginName + "</td>";
	}else{
		row += "<td></td><td></td>";
	}

	//状态
	row += "<td><em class='" + getStatusCss(data.orderStatus) + "'>" + data.orderStatusMsg + "</em></td>";
	row += "<td><a href='<%=path%>/mailQuery/getOrderMail?areaCode=" + data.areaCode + "&mailNum=" + data.mailNum + "' target='_blank' class='orange'>查看物流信息 </a></td>";
	row += "</tr>";	
	return row;
}
//S 运单状态样式
function getStatusCss(status){
	if(status != null){
		if(status == "<%=OrderStatus.NOTARR %>"){
			return "l-blue";
		}else if(status == "<%=OrderStatus.NOTDISPATCH %>"){
			return "orange";
		}else if(status == "<%=OrderStatus.DISPATCHED %>"){
			return "c-green";
		}else if(status == "<%=OrderStatus.RETENTION %>"){
			return "purple";
		}else if(status == "<%=OrderStatus.REJECTION %>"){
			return "d-red";
		}else if(status == "<%=OrderStatus.SIGNED %>"){
			return "black";
		}else if(status == "<%=OrderStatus.TO_OTHER_EXPRESS %>"){
			return "d-blue";
		}else if(status == "<%=OrderStatus.APPLY_RETURN %>"){
			return "brown";
		}else if(status == "<%=OrderStatus.RETURNED %>"){
			return "l-green";
		}
	}
	return "";
}
//S 运单状态样式


//导出数据
function exportData() {
	$("#prov_expt").val($("#addr_control .prov").val());
	$("#city_expt").val($("#addr_control .city").val());
	$("#area_expt").val($("#addr_control .dist").val());
	$("#siteStatus_expt").val($("#siteStatus").val());
	$("#areaFlag_expt").val($("#areaFlag").val());
	$("#areaCode_expt").val(getAreaCodeStr());//站点编号集合
	$("#status_expt").val(getAreaCodeStr("statusOpts"));
	$("#arriveBetween_expt").val($("#arriveBetween").val());
	$("#mailNum_expt").val($("#mailNum").val());
	$("#exptForm").submit();
}
</script>


</body>
</html>