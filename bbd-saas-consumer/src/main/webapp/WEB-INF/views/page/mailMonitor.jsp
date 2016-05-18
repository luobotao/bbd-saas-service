<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.bbd.saas.vo.OrderMonitorVO" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
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
<body >

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
								<label>站点：</label>
								<select id="areaCode" name="areaCode" class="form-control form-con-new">
									<option value="">全部</option>
									<c:if test="${not empty siteList}">
										<c:forEach var="site" items="${siteList}">
											<option value="${site.areaCode}">${site.name}</option>
										</c:forEach>
									</c:if>
								</select>
							</div>
	  						<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4 pad0">
	  							<label>时间：</label>
	  							<input id="timeBetween" name="timeBetween" value="${timeBetween}" type="text" placeholder="请选择到站时间" class="form-control c-disable"  />
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
				<form action="<%=request.getContextPath()%>/mailMonitor/exportToExcel" method="get" id="exptForm">
					<input id="areaCode_expt" name="areaCode" type="hidden" />
					<input id="timeBetween_expt" name="timeBetween" type="hidden" />
				</form>
				<!-- E 搜索区域 -->
				<div class="tab-bod mt20">
					<!-- S table -->
					<div class="table-responsive">
  					<table class="table">
  						<thead>
  							<tr>
  								<th>站点</th>
								<th>未到站订单数</th>
								<th>已到站订单数</th>
								<th>未分派数</th>
								<th>已分派数</th>
								<th>签收数</th>
								<th>滞留数</th>
								<th>拒收数</th>
								<th>转站数</th>
  							</tr>
  						</thead>
  						<tbody id="dataList">
  							<%
								PageModel<OrderMonitorVO> orderMonitorVOPage = (PageModel<OrderMonitorVO>)request.getAttribute("orderMonitorVOPage");
								if(orderMonitorVOPage==null || orderMonitorVOPage.getDatas() == null){
							%>
								<tr>
									<td colspan="9">没有站点，请建立站点。</td>
								</tr>
							<%
								}else{
									for(OrderMonitorVO orderMonitorVO : orderMonitorVOPage.getDatas()){
							%>
								<tr>
									<td><%=orderMonitorVO.getSiteName()%></td>
									<td><%=orderMonitorVO.getNoArrive()%></td>
									<td><%=orderMonitorVO.getArrived()%></td>
									<td><%=orderMonitorVO.getNoDispatch()%></td>
									<td><%=orderMonitorVO.getDispatched()%></td>
									<td><%=orderMonitorVO.getSigned()%></td>
									<td><%=orderMonitorVO.getRetention()%></td>
									<td><%=orderMonitorVO.getRejection()%></td>
									<td><%=orderMonitorVO.getToOtherSite()%></td>
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
<script type="text/javascript">

$(document).ready(function() {
	//显示分页条
	var pageStr = paginNav(<%=orderMonitorVOPage.getPageNo()%>, <%=orderMonitorVOPage.getTotalPages()%>, <%=orderMonitorVOPage.getTotalCount()%>);
	$("#pagin").html(pageStr);
	
	//初始化到站时间框
	$("#timeBetween").daterangepicker({
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
	console.log("areaCode===="+$("#areaCode").val());
	//查询所有派件员
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/mailMonitor/getList",//路径
        data : {  
            "pageIndex" : pageIndex,
			"areaCode" : $("#areaCode").val(),
            "timeBetween" : $("#timeBetween").val(),
        },//数据，这里使用的是Json格式进行传输
        success : function(dataObject) {//返回数据根据结果进行相应的处理 
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
           	if(window.top==window.self){//不存在父页面
				window.location.href="<c:url value="/login" />"
			}else{
				window.top.location.href="<c:url value="/login" />"
			}
      	}    
    });	
}	


//封装一行的数据
function getRowHtml(data){
	var row = "<tr>";
	row += "<td>" + data.siteName + "</td>";
	row += "<td>" + data.noArrive + "</td>";
	row += "<td>" + data.arrived + "</td>";
	row += "<td>" + data.noDispatch + "</td>";
	row += "<td>" + data.dispatched + "</td>";
	row += "<td>" + data.signed + "</td>";
	row += "<td>" + data.retention + "</td>";
	row += "<td>" + data.rejection + "</td>";
	row += "<td>" + data.toOtherSite + "</td>";
	row += "</tr>";
	return row;
}


//导出数据
function exportData() {
	$("#areaCode_expt").val($("#areaCode").val());
	$("#timeBetween_expt").val($("#timeBetween").val());
	$("#exptForm").submit();
}

</script>
</body>
</html>