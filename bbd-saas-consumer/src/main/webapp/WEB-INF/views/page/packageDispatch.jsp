<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.DispatchStatus" %>
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
						<select id="src" name="src" class="form-control">
							<%=DispatchStatus.Srcs2HTML(-1)%>
						</select>
					</div>
					<div class="col-xs-3">
						<label>到站时间：</label>
						<input id="between" name="between" type="text" class="form-control" placeholder="请选择到站时间" value="${between}"/>
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
				<button onclick="showCourierDiv()">选择派件员</button>	
				<span class="ft12 pt20">已选择：<span id="courierName"></span></span>
				<input id="courierId1" type="hidden" value="">
				<input id="courierId" type="text" value="" style="width:200px"/> 	
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


<!-- 选择派件员弹出窗-开始 -->
<div  id="chooseCourier_div" class="popDiv" >
	<div class="title_div">选择派件员</div>
	<div class="m20">
		<span>派件员:
			<select id="courier_select">  
				  
			</select>				  
		</span> 
	</div>
	<div>
		<button onclick="hideCourierDiv()">取消</button>
		<button onclick="chooseCourier()">确定</button>
	</div>
<div>
<!-- 选择派件员弹出窗-结束 -->
<!-- 分页js -->
<script src="<c:url value="/resources/javascripts/page/pageBar.js" />"> </script>

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
		if($("#courierId").val() == null || $("#courierId").val() == ""){
	  		$("#waybillId_check").text("请选择派件员！");
	  	}
	});

	//扫描运单号--把快递分派给派件员
	$("#waybillId").on('input',function(e){  		
	    $.ajax({
			type : "GET",  //提交方式  
            url : "<%=path%>/packageDispatch/dispatch",//路径  
            data : {  
                "mailNum" : $("#waybillId").val(),
                "courierId" : $("#courier_select").val()  
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
	//显示分页条
	var pageStr = paginNav(<%=currentPage%>, <%=totalPage%>, <%=count%>);
	$("#pagin").html(pageStr);
	
	//初始化派件员下拉框（快递员）
	initCourier();  

});

//加载带有查询条件的指定页的数据
function gotoPage(pageIndex) {
	//查询所有派件员
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/packageDispatch/getList",//路径  
        data : {  
            "pageIndex" : pageIndex,
            "status" : -1, 
            "courierId" : ""
            /* "status" : $("#status").val(), 
            "between" : $("#between").val(), 
            "courierId" : $("#courierId").val(), */ 
        },//数据，这里使用的是Json格式进行传输  
        success : function(dataObject) {//返回数据根据结果进行相应的处理 
            console.log("dataObject==="+dataObject);
            var tbody = $("#dataList");
            // 清空表格数据
            tbody.html("");
            
            var dataList = dataObject.datas;
			if(dataList != null){
				var datastr = "";
				for(var i = 0; i < dataList.length; i++){
					datastr += getRowHtml(dataList[i]);
				}
				tbody.html(datastr);
			} 
			//更新分页条
			var pageStr = paginNav(pageIndex, <%=totalPage%>, <%=count%>);
			console.log("pageIndex===" + pageIndex + "  totalPage===" + <%=totalPage%> + "  count===" + <%=count%>);
			$("#pagin").html(pageStr);
		},
        error : function() {  
           	alert("加载分页数据异常！");  
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
	row += "</tr>";
	return row;
}

//初始化派件员下拉框（快递员）
function initCourier() {
	//查询所有派件员
	$.ajax({
		type : "GET",  //提交方式  
        url : "<%=path%>/packageDispatch/getAllUserList",//路径  
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
       		alert("派件员列表加载异常！");  
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
// 删除  
function col_delete() {  
    var selOpt = $("#mySelect option:selected");  
    selOpt.remove();  
}  
// 清空  
function col_clear() {  
    var selOpt = $("#mySelect option");  
    selOpt.remove();  
}  
//显示选择派件员div
	function showCourierDiv(waybillId) {
	}	
	//显示选择派件员div
	function showCourierDiv(waybillId) {
		
		$("#chooseCourier_div").show();
	}
	//隐藏选择派件员div
	function hideCourierDiv() {
		$("#chooseCourier_div").hide();
	}
	//选择派件员
	function chooseCourier() {
	$("#ddlregtype").find("option:selected").text(); 
		$("#courierName").text($("#courier_select").find("option:selected").text());
		$("#courierId").val($("#courier_select").val());
		$("#chooseCourier_div").hide();
	}
	
</script>
</body>
</html>