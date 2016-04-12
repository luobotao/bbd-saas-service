<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.DispatchStatus" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../../common/pages.jsp"%>
<html>
<head>
	<link href="<c:url value="/resources/frame.css" />" rel="stylesheet"  type="text/css" />		
	<jsp:include page="../main.jsp" flush="true" />
</head>
<%
	String type= (String)request.getAttribute("type");
/* 
	int count = 0,totalPage = 0,pagesize = 0;
	if (p != null){
		count = p.getCount();
		totalPage = p.getTotalPage();
		pagesize = p.getPageSize();
	}
	
	String pageIndex = StrTool.initStr(request.getParameter("pageIndex"),"1");
	int currentPage = Integer.parseInt(pageIndex);
	Map<String,String> map = new HashMap<String,String>();
	map.put("pageIndex",currentPage + "");
	
	if(StringUtils.isNotEmpty(search.getsTime())){
		map.put("sTime",search.getsTime());
	}
	if(StringUtils.isNotEmpty(search.geteTime())){
		map.put("eTime",search.geteTime());
	} */
	int count = 120, totalPage = 1, pagesize = 0, currentPage=1;
	Map<String,String> map = new HashMap<String,String>();
	String pageInfo = pageNav("/packageDispatch", totalPage, currentPage,count, "POST", map);
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
						<select id="src" name="src" class="form-control">
							<%=DispatchStatus.Srcs2HTML(-1)%>
						</select>
					</div>
					<div class="col-xs-3">
						<label>到站时间：</label>
						<input id="between" name="between" type="text" class="form-control" placeholder="请选择到站时间" value=""/>
					</div>
					<div >
						<button class="btn btn-primary" style="margin-top:10px ; margin-left: 15px ;" type="submit">查询</button>

					</div>
				</div>
			</form>
		</div>
	</div>
	<div class="col-xs-12">
		<div class="row">
			<div class="col-xs-3">
				<button onclick="showSenderDiv()">选择派件员</button>	
				<span class="ft12 pt20">已选择：<span id="senderName"></span></span>
				<input id="senderId1" type="hidden" value="">
				<input id="senderId" type="text" value="" /> 	
			</div>
			
			<div class="col-xs-4">
				扫描运单号：<input id="waybillId" name="waybillId" type="text" /></span>
			    <span class="pl20 ft12" id="waybillId_check"> </span>		
			</div>
		</div>
		
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
					</tr>
				</thead>
				<tbody>
				<%
					PageModel<Order> orderPage = (PageModel<Order>)request.getAttribute("orderPage");
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
				</tr>
				<%
					}
				%>
				</tbody>
			</table>
			
			<!--页码 start-->
				<%=pageInfo%>
			<!--页码 end-->
				
			<%-- <%
				if(orderPage.getDatas().size()>0){
			%>
			<div>
				<div class="col-xs-6">
					<div class="dataTables_info" id="userTable_info">页码：
						<%=(orderPage.getPageNo()+1)%>/<%=orderPage.getTotalPages()%> 共计<%=orderPage.getTotalCount()%>条记录</div>
				</div>
				<div >
					<div class="dataTables_paginate paging_bootstrap">
						<ul class="pagination">
							<%
								if(orderPage.getPageNo()<1){
							%>
							<li class="prev disabled"><a href="javascript:">首页</a></li>
							<%
							}else{
							%>
							<li class="prev"><a href="@searchParam()page=0">首页</a></li>
							<%
								}
								for(int i=0;i<orderPage.getTotalPages()-1;i++){
									if(orderPage.getTotalPages()<8){
							%>
							<li class="<c:if test="$(i==orderPage.getPageNo())">active</c:if>"><a href="@searchParam()page=@index"><%=i+1%></a></li>
							<%
							}else {
								if(orderPage.getPageNo()<7){

									if(i<8){
							%>
							<li class="active"><a href="@searchParam()page=@index"><%=i+1%></a></li>
							<%
								}else{
									if(i==(orderPage.getTotalPages()-1)){
							%>
								<li class=""><a href="javascript:">...</a></li>
							<%
									}
									if(i==(orderPage.getTotalPages())){
							%>
								<li class="active"><a href="@searchParam()page=@index"><%=i+1%></a></li>
							<%
									}
								}

							}else{
								if(orderPage.getPageNo()<(orderPage.getTotalPages()-4)){
									if(i==0||i>(orderPage.getPageNo()-4)&&i<(orderPage.getPageNo()+5)){
										%>
							<li class="active"><a href="@searchParam()page=@index"><%=i+1%></a></li>
							<%
									}else{
										if(i==2){
											%>
											<li class=""><a href="javascript:">...</a></li>
							<%
										}
										if(i==(orderPage.getTotalPages()-1)){
										%>
										<li class=""><a href="javascript:">...</a></li>
										<%
										}
										if(i==(orderPage.getTotalPages())){
										%>
										<li class="active"><a href="@searchParam()page=@index"><%=i+1%></a></li>
										<%
										}
									}
								}else{
									if(i==0||i>(orderPage.getTotalPages()-8)){
									%>
									<li class="active"><a href="@searchParam()page=@index"><%=i+1%></a></li>
									<li class=""><a href="javascript:">...</a></li>
									<%
									}else if(i==2){
									%>
									<li class=""><a href="javascript:">...</a></li>
									<%
									}
								}

										}

									}

								}
							%>

							<%
								if(orderPage.getTotalPages()==orderPage.getPageNo()){
							%>
							<li class="next disabled"><a href="javascript:">尾页</a></li>
							<%
								}else{
							%>
							<li class="next"><a href="@searchParam()page=@{ pages - 1}">尾页</a></li>
							<%
								}
							%>

						</ul>
					</div>
				</div>
			</div>
			<%
				}
			%>
 --%>
		</div>
	</div>
</section>


<!-- 选择派件员弹出窗-开始 -->
<div  id="chooseSender_div" class="popDiv" >
	<div class="title_div">选择派件员</div>
	<div class="m20">
		<span>派件员:
			<select id="sender_select">  
				<option value ="senderId1">张三</option>  
				<option value ="senderId2">李四</option>  
				<option value="senderId3">王五</option>  
			</select>				  
		</span> 
	</div>
	<div>
		<button onclick="hideSenderDiv()">取消</button>
		<button onclick="chooseSender()">确定</button>
	</div>
<div>
<!-- 选择派件员弹出窗-结束 -->
<i
<script type="text/javascript">
$(document).ready(function() {
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
	//扫描运单号  focus事件
	$("#waybillId").focus(function(){
		if($("#senderId").val() == null || $("#senderId").val() == ""){
	  		$("#waybillId_check").text("请选择派件员！");
	  	}
	});

	$("input[type='checkbox']").iCheck({
		checkboxClass : 'icheckbox_square-blue'
	});

	$("#selectAll").on('ifUnchecked', function() {
		$("input[type='checkbox']", "#orderTable").iCheck("uncheck");
	}).on('ifChecked', function() {
		$("input[type='checkbox']", "#orderTable").iCheck("check");
	});
	//扫描运单号
	$("#waybillId").on('input',function(e){  		
	       $.ajax({
			type : "GET",  //提交方式  
            url : "/packageDispatch/checkMailNum",//路径  
            data : {  
                "mailNum" : $("#waybillId").val(),
                "senderId" : $("#sender_select").val()  
            },//数据，这里使用的是Json格式进行传输  
            success : function(data) {//返回数据根据结果进行相应的处理  
                if ( data.success ) { 
                	$("#waybillId_check").text("分派成功！");
                	//刷新列表
                	
                } else {  
                    if(data.erroFlag == 0){
                		$("#waybillId_check").text("【异常扫描】不存在此订单！");
                	} else if(data.erroFlag == 2){
                		$("#waybillId_check").text("重复扫描，此运单已经分派过啦！");
                	}   
                }  
            },
            error : function() {  
           		alert("异常！");  
      		}    
        }); 
	});  

});
	
	
	//显示选择派件员div
	function showSenderDiv(waybillId) {
		$("#chooseSender_div").show();
	}
	//隐藏选择派件员div
	function hideSenderDiv() {
		$("#chooseSender_div").hide();
	}
	//选择派件员
	function chooseSender() {
	$("#ddlregtype").find("option:selected").text(); 
		$("#senderName").text($("#sender_select").find("option:selected").text());
		$("#senderId").val($("#sender_select").val());
		$("#chooseSender_div").hide();
	}
	
</script>
</body>
</html>