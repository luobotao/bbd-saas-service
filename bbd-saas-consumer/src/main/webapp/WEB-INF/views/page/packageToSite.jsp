<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.ArriveStatus" %>
<%@ page import="com.bbd.saas.enums.OrderStatus" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<jsp:include page="../main.jsp" flush="true" />
</head>
<body >
<section class="content">
	<div class="col-xs-12">
		<!-- 订单数显示 开始 -->
		<div class="row">
			<div class="col-xs-3"><span>${non_arrival_num}</span><br><span>今日未到站订单数</span></div>
			<div class="col-xs-3"><span>${history_non_arrival_num}</span><br><span>历史未到站订单数</span></div>
			<div class="col-xs-3"><span>${arrived_num}</span><br><span>今日已到站订单数</span></div>
		</div>
	</div>

	<div class="col-xs-12">
		<!-- 订单数显示 结束   -->
		<div class="box-body">
			<form action="?" method="get" id="searchOrderForm" name="searchOrderForm">
				<div class="row">
					<div class="col-xs-3">
						<label>状态：</label>
						<select id="arriveStatus" name="arriveStatus" class="form-control">
							<%=ArriveStatus.Srcs2HTML(-1)%>
						</select>
					</div>
					<div class="col-xs-3">
						<label>预计到站时间：</label>
						<input id="between" name="between" type="text" class="form-control" placeholder="请选择预计到站时间" value=""/>
					</div>
					<div >
						<button class="btn btn-primary" style="margin-top:10px ; margin-left: 15px ;" type="submit">查询</button>
					</div>
				</div>
			</form>
			<div class="row">
				<div class="col-xs-3">
					<label>扫描包裹号：</label>
					<input id="parcelCode" name="parcelCode" type="text" onkeypress="enterPress(event)" />
					<p class="help-block" id="parcelCodeP" style="display:none;"></p>
				</div>
				<div class="col-xs-3">
					<label>扫描运单号：</label>
					<input id="mailNum" name="mailNum" type="text" onkeypress="enterPress(event)"/>
					<p class="help-block" id="mailNumP" style="display:none;"></p>
				</div>
				<span class=""><input id="batchToSite" name="batchToSite" type="button" value="批量到站"/></span>
			</div>
		</div>
	</div>
	<div class="col-xs-12">

		<div class="box-body table-responsive">
			<table id="orderTable" class="table table-bordered table-hover">
				<thead>
				<tr>
					<td>选择</td>
					<td>包裹号</td>
					<td>运单号</td>
					<td>订单号</td>
					<td>来源</td>
					<td>收货人</td>
					<td>收货人电话</td>
					<td>地址</td>
					<td>库房打单时间</td>
					<td>预计到站时间</td>
					<td>状态</td>
				</tr>
				</thead>
				<tbody id="dataList">
				<%
					PageModel<Order> orderPage = (PageModel<Order>)request.getAttribute("orderPage");
					for(Order order : orderPage.getDatas()){
				%>
				<tr>
					<td><input type="checkbox" value="<%=order.getId()%>" name="id"></td>
					<td><%=order.getParcelCode()%></td>
					<td><%=order.getMailNum()%></td>
					<td><%=order.getOrderNo()%></td>
					<td><%=order.getSrc()%></td>
					<td><%=order.getReciever().getName()%></td>
					<td><%=order.getReciever().getPhone()%></td>
					<td><%=order.getReciever().getProvince()%> <%=order.getReciever().getCity()%> <%=order.getReciever().getArea()%> <%=order.getReciever().getAddress()%></td>
					<td><%=order.getDatePrint()%></td>
					<td>2016-04-06</td>
					<%
						if(order.getOrderStatus()==OrderStatus.NOTARR || order.getOrderStatus()==null){
					%>
					<td><%=ArriveStatus.NOTARR.getMessage()%></td>
					<%
						}else{
					%>
					<td><%=ArriveStatus.ARRIVED.getMessage()%></td>
					<%
						}
					%>
				</tr>
				<%
					}
				%>
				</tbody>
				<tfoot>
				<th colspan="13">
					<input id="selectAll" name="selectAll" type="checkbox"> <label for="selectAll">
					全选</label> &nbsp;
				</th>
				</tfoot>
			</table>

			<!--页码 start-->
			<div id="pagin"></div>
			<!--页码 end-->

		</div>
	</div>
</section>

<!-- 分页js -->
<script src="<c:url value="/resources/javascripts/page/pageBar.js" />"> </script>

<script type="text/javascript">
	var flag = true;
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
	$("input[type='checkbox']").iCheck({
		checkboxClass : 'icheckbox_square-blue'
	});

	$("#selectAll").on('ifUnchecked', function() {
		$("input[type='checkbox']", "#orderTable").iCheck("uncheck");
	}).on('ifChecked', function() {
		$("input[type='checkbox']", "#orderTable").iCheck("check");
	});

	//显示分页条
	var pageStr = paginNav(<%=orderPage.getPageNo()%>, <%=orderPage.getTotalPages()%>, <%=orderPage.getTotalCount()%>);
	$("#pagin").html(pageStr);



	//加载带有查询条件的指定页的数据
	function gotoPage(pageIndex,parcelCode,mailNum) {
		//查询所有派件员
		$.ajax({
			type : "GET",  //提交方式
			url : "/packageToSite/getOrderPage",//路径
			data : {
				"pageIndex" : pageIndex,
				"status" : -1,
				"parcelCode" : parcelCode,
				"mailNum" : mailNum
			},//数据，这里使用的是Json格式进行传输
			success : function(dataObject) {//返回数据根据结果进行相应的处理
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
				var pageStr = paginNav(pageIndex,  dataObject.totalPages, dataObject.totalCount);
				$("#pagin").html(pageStr);
				$("input[type='checkbox']").iCheck({
					checkboxClass : 'icheckbox_square-blue'
				});
			},
			error : function() {
				alert("加载分页数据异常！");
			}
		});
	}


	//封装一行的数据
	function getRowHtml(data){
		var row = "<tr>";
		row +=  "<td><input type='checkbox' value='" + data.id + "' name='id'></td>";
		row +=  "<td>" + data.parcelCode + "</td>";
		row += "<td>" + data.mailNum + "</td>";
		row += "<td>" + data.orderNo + "</td>";
		row += "<td>" + data.src + "</td>";
		row += "<td>" + data.reciever.name + "</td>";
		row += "<td>" + data.reciever.phone + "</td>";
		row += "<td>" + data.reciever.province + data.reciever.city + data.reciever.area + data.reciever.address + "</td>";
		row += "<td>" + data.src + "</td>";
		row += "<td>" + data.src + "</td>";
		if(data.orderStatus=="<%=OrderStatus.NOTARR%>"){
			row += "<td>" + <%=ArriveStatus.NOTARR.getMessage()%> + "</td>";
		}else{
			row += "<td>" + <%=ArriveStatus.ARRIVED.getMessage()%> + "</td>";
		}
		row += "</tr>";
		return row;
	}


	//回车事件
	function enterPress(e){
		if(!e) e = window.event;//火狐中是 window.event
		if((e.keyCode || e.which) == 13){
			if($("#mailNum").val()!=null && $("#mailNum").val()!=""){
				checkOrderByMailNum($("#mailNum").val());
			}
			if($("#parcelCode").val()!=null && $("#parcelCode").val()!=""){
				checkOrderParcelByParcelCode($("#parcelCode").val());
			}
			gotoPage(0,$("#parcelCode").val(),$("#mailNum").val());
		}
	}


	function checkOrderByMailNum(mailNum){
		if(mailNum!=""){
			$.ajax({
				url: '/packageToSite/checkOrderByMailNum?mailNum='+mailNum,
				type: 'GET',
				cache: false,
				dataType: "text",
				data: {},
				success: function(response){
					if(response!=null &&  response!=""){
						if(response.orderStatus!="NOTARR" && response.orderStatus!=null){
							$("#mailNumP").html("重复扫描，此运单已经扫描过啦");
							$("#mailNumP").attr("style","color:red");
							flag = false;
						}else{
							$("#mailNumP").html("");
							$("#mailNumP").attr("style","display:none");
							flag = true;
						}
					}else{
						$("#mailNumP").html("【异常扫描】不存在此运单号") ;
						$("#mailNumP").attr("style","color:red");
						flag = false;
					}
				},
				error: function(){
					alert('服务器繁忙，请稍后再试！');
					flag = false;
				}
			});
		}
	}
	function checkOrderParcelByParcelCode(parcelCode){
		if(parcelCode!=""){
			$.ajax({
				url: '/packageToSite/checkOrderParcelByParcelCode?parcelCode='+parcelCode,
				type: 'GET',
				cache: false,
				dataType: "text",
				data: {},
				success: function(response){
					if(response=="false"){
						$("#parcelCodeP").html("【异常扫描】包裹号不存在") ;
						$("#parcelCodeP").attr("style","color:red");
						flag = false;
					}else{
						$("#parcelCodeP").html("扫描成功，请扫描运单号");
						$("#parcelCodeP").attr("style","color:red");
						flag = true;

					}
				},
				error: function(){
					alert('服务器繁忙，请稍后再试！');
					flag = false;
				}
			});
		}
	}
</script>
</body>
</html>