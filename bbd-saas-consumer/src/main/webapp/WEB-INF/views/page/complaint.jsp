<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.bbd.saas.enums.ComplaintStatus" %>
<%@ page import="com.bbd.saas.enums.AppealStatus" %>
<%@ page import="com.bbd.saas.enums.PunishReason" %>
<%@ page import="com.bbd.saas.vo.entity.ComplaintVO" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.utils.Dates" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../main.jsp"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
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
				<!-- S 信息展示 -->
				<div class="b-total">
					本月分数
					<c:choose>
						<c:when test="${grade >= 80}">
							<span class="b-grade c-greens">
						</c:when>
						<c:otherwise>
							<span class="b-grade c-red">
						</c:otherwise>
					</c:choose>
					<fmt:formatNumber type="number" value="${grade}" pattern="0.0" maxFractionDigits="1"/></span>
					分
					<span class="b-alls">总分100分</span>
				</div>
				<!-- E 信息展示 -->

				<!-- S 搜索区域 -->
				<form class="form-inline form-inline-n mt20">
					<div class="search-area">
	  					<div class="row pb20">
							<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
								<label>投诉状态：</label>
								<select id="complaintStatus" name="complaintStatus" class="form-control form-con-new">
									<%=ComplaintStatus.Srcs2HTML(-1)%>
								</select>
							</div>
	  						<div class="form-group col-xs-12 col-sm-6 col-md-5 col-lg-4">
								<label>申诉状态：</label>
								<select id="appealStatus" name="appealStatus" class="form-control form-con-new">
									<%=AppealStatus.Srcs2HTML(-1)%>
								</select>
	  						</div>
							<div class="form-group col-xs-12 col-sm-6 col-md-5 col-lg-4">
								<label>投诉理由：</label>
								<select id="reason" name="reason" class="form-control form-con-new">
									<%=PunishReason.Srcs2HTML(-1)%>
								</select>
							</div>
						</div>
						<div class="row pb20">
							<div class="form-group col-xs-12 col-sm-6 col-md-5 col-lg-4">
								<label>投诉时间：</label>
								<input id="between" name="between" value="${between}" type="text" placeholder="请选择投诉时间" class="form-control c-disable"  />
							</div>
	  						<div class="form-group col-xs-12 col-sm-6 col-md-5 col-lg-4">
	  							<label>运单号：</label>
	  							<input id="mailNum" name="mailNum" type="text" placeholder="请输入运单号" class="form-control"  />
	  						</div>
							<div class="form-group col-xs-12 col-sm-6 col-md-5 col-lg-4">
								<a href="javascript:void(0)" class="ser-btn l" onclick="gotoPage(0);"><i class="b-icon p-query p-ser"></i>查询</a>
							</div>
	  					</div>
	  				</div>
				</form>
				<!-- E 搜索区域 -->
				<p class="b-tips">请及时查看未申诉订单，并尽快通知快递员至APP提交申诉</p>
				<div class="tab-bod mt10">
					<!-- S table -->
					<div class="table-responsive">
  					<table class="table">
  						<thead>
  							<tr>
								<th>运单号</th>
								<th width="15%">投诉理由</th>
								<th>投诉时间</th>
								<th>被投诉人</th>
								<th>申诉状态</th>
								<th>投诉状态</th>
								<th>处罚结果</th>
  							</tr>
  						</thead>
  						<tbody id="dataList">
  							<%
								PageModel<ComplaintVO> dataPage = (PageModel<ComplaintVO>)request.getAttribute("dataPage");
								if(dataPage==null || dataPage.getDatas() == null){
							%>
								<tr>
									<td colspan="7">没有符合查询条件的数据</td>
								</tr>
							<%
								}else{
									for(ComplaintVO complaint : dataPage.getDatas()){
							%>
								<tr>
									<td><%=complaint.getMailNum()%></td>
									<td><%=complaint.getReason()%></td>
									<td><%=Dates.formatDateTime_New(complaint.getDateAdd())%></td>
									<td><%=complaint.getRespondent()%></td>
									<%
										if(complaint.getAppealStatus() != null){//申诉状态
									%>
										<td><%=complaint.getAppealStatus().getMessage()%></td>
									<%
										}else {
									%>
										<td></td>
									<%
										}
										if(complaint.getComplaintStatus() != null){//投诉状态
									%>
										<%
											if(complaint.getComplaintStatus() == ComplaintStatus.COMPLAINT_WAIT){//等待客服处理
										%>
											<td>客服处理中</td>
											<td>暂无处罚</td>
										<%
											}else if(complaint.getComplaintStatus() == ComplaintStatus.COMPLAINT_CLOSE){//投诉关闭
										%>
											<td><%=complaint.getComplaintStatusMsg()%></td>
											<td>暂无处罚</td>
										<%
											}else{//投诉成立+撤销
										%>
											<td><%=complaint.getComplaintStatusMsg()%></td>
											<td><%=complaint.getDealResult()%></td>
										<%
											}
										%>
									<%
										}else {
									%>
										<td></td>
										<td></td>
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

<script type="text/javascript">
$(document).ready(function() {
	//显示分页条
	var pageStr = paginNav(<%=dataPage.getPageNo()%>, <%=dataPage.getTotalPages()%>, <%=dataPage.getTotalCount()%>);
	$("#pagin").html(pageStr);
	
	//初始化到站时间框
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
});

//加载带有查询条件的指定页的数据
function gotoPage(pageIndex) {
	//查询所有派件员
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/complaint/getList",//路径
        data : {  
            "pageIndex" : pageIndex,
            "complaintStatus" : $("#complaintStatus").val(),
            "appealStatus" : $("#appealStatus").val(),
            "reason" : $("#reason").val(),
            "between" : $("#between").val(),
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
			}else{
				datastr = "<tr><td colspan='7'>没有符合查询条件的数据</td></tr>";
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
	if(mailNum == null || mailNum == ""){//没有按照运单查，不需要着色
		row += "<td>" + data.mailNum + "</td>";
	}else{
		row += "<td>" + data.mailNum.replace(mailNum, "<span class='font-bg-color'>" + mailNum + "</span>") + "</td>";
	}
	row += "<td>" + data.reason + "</td>";
	row += "<td>" + getDate1(data.dateAdd) + "</td>";
	row += "<td>" + data.respondent + "</td>";//被投诉人
	row += "<td>" + data.appealStatusMsg + "</td>";//申诉状态
	if(data.complaintStatus != null){
		if(data.complaintStatus == "<%=ComplaintStatus.COMPLAINT_WAIT %>"){
			row += "<td>客服处理中</td>";
			row += "<td>暂无处罚</td>";
		}else if(data.complaintStatus == "<%=ComplaintStatus.COMPLAINT_CLOSE %>"){
			row += "<td>" + data.complaintStatusMsg + "</td>";//投诉状态
			row += "<td>暂无处罚</td>";
		}else {
			row += "<td>" + data.complaintStatusMsg + "</td>";//投诉状态
			row += "<td>" + data.dealResult + "</td>";//处罚结果
		}
	}else{
		row += "<td></td>";
		row += "<td></td>";
	}



	row += "</tr>";
	return row;
}
</script>
</body>
</html>