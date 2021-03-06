<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="com.bbd.saas.enums.OrderStatus" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="../main.jsp"%>
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
				<!-- S 搜索区域 -->
				<form class="form-inline form-inline-n">
					<div class="search-area">
	  					<div class="row">
	  						<div class="form-group plr">
	  							<label>运单号：</label>
								<textarea id="mailNumStr" name="mailNumStr" rows="15" cols="60" style="border: 1px solid #d9d8eb;"></textarea>
								<em class='d-red'>输入格式要求：一个运单号占一行</em>
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
				<form action="<%=request.getContextPath()%>/superAreaHandle/exportToExcel" method="get" id="exptForm">
					<input id="mailNumStr_expt" name="mailNumStr_expt" type="hidden" />
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
								<th>收货人电话</th>
								<th width="15%">收货人地址</th>
								<th>司机取货时间</th>
								<th>预计站点入库时间</th>
								<th>站点入库时间</th>
								<th>签收时间</th>
								<th>派送员</th>
								<th>派送员手机</th>
								<th>状态</th>
								<th>操作</th>
  							</tr>
  						</thead>
  						<tbody id="dataList">

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
	var mailNumStr = $("#mailNumStr").val();
	if(mailNumStr == ""){
		$("#mailNumStr").val("yundan1\nyundan2");
	}
	$("#mailNumStr").click(function(){
		var content = $(this).val();
		if(content.indexOf("yundan1\nyundan2") > -1){
			$(this).val("");
		}
	});
	//显示分页条
	var pageStr = paginNav(0, 0, 0);
	$("#pagin").html(pageStr);
});

//加载带有查询条件的指定页的数据
function gotoPage(pageIndex) {
	//查询所有派件员
	$.ajax({
		type : "GET",  //提交方式
        url : "<%=path%>/superAreaHandle/getList",//路径
        data : {  
            "pageIndex" : pageIndex,
            "mailNumStr" : $("#mailNumStr").val()
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
	var row = "<tr>";
	row += "<td>" + data.mailNum + "</td>";
	if(data.reciever == null){
		row += "<td></td>";
		row += "<td></td>";
		row += "<td></td>";
	}else{
		row += "<td>" + defalutVaule(data.reciever.name) + "</td>";
		row += "<td>" + defalutVaule(data.reciever.phone) + "</td>";
		row += "<td class='tl'>" + defalutVaule(data.reciever.province) + defalutVaule(data.reciever.city) + defalutVaule(data.reciever.area) + defalutVaule(data.reciever.address) + "</td>";
	}
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
	if(data.userId == null || data.userId == ""){//未分派||派件员没有查询到
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
	row += "<td><a href='<%=path%>/dataQuery/getOrderMail?mailNum=" + data.mailNum + "' target='_blank' class='orange'>查看物流信息 </a></td>";
	row += "</tr>";	
	return row;
}
function defalutVaule(data){
	if(data == null){
		return "";
	} else {
		return data;
	}
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
	$("#mailNumStr_expt").val($("#mailNumStr").val());
	$("#exptForm").submit();
}
</script>
</body>
</html>