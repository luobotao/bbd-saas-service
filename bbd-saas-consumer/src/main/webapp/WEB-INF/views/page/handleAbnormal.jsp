<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="keywords" content="棒棒哒-异常件处理页面" />
	<meta name="description" content="棒棒哒-异常件处理页面" />
	<title>异常件处理页面</title>
	<link href="<c:url value="/resources/frame.css" />" rel="stylesheet"  type="text/css" />		
	
</head>
<body>
<div>
	<jsp:include page="../top.jsp" flush="true" />	
</div>
<div class="content">
	<div class="content-left" id="content-left"><jsp:include page="../leftMenu.jsp" flush="true" /></div>
	<div class="content-main" id="content-main">
		<div class="m20">
			<span>状态:
				<select>  
				  <option value ="1">全部</option>  
				  <option value ="2">滞留</option>  
				  <option value="3">拒收</option>  
				</select>  
			</span> 
			<span class="pl20">到站时间：<input id="toSiteTime" name="toSiteTime" type="text" value="2016-04-05" /></span>
			<br><br>
			<button id="queryData" name="queryData">查询</button>
		</div>
		
		<div class="m20">
			<table id="data_table" border="1" cellpadding="6px" cellspacing="0px"  style="background-color: #b9d8f3;">
				<tr>
					<td>包裹号</td>
					<td>运单号</td>
					<td>订单号</td>
					<td>来源</td>
					<td>收货人</td>
					<td>收货人电话</td>
					<td>地址</td>
					<td>到站时间</td>
					<td>派送员姓名</td>
					<td>派送员手机</td>
					<td>状态</td>
					<td>操作</td>
				</tr>
				<tr>
					<td>包裹号xxx</td>
					<td>运单号xxx</td>
					<td>订单号xxx</td>
					<td>京东</td>
					<td>李四</td>
					<td>123456xxx</td>
					<td>北京市-朝阳区-xxxx</td>
					<td>2016-04-03 15:2:23</td>
					<td>周七</td>
					<td>123456xxx</td>
					<td>滞留（滞留原因）</td>
					<td>
						<button onclick="showSenderDiv()">重新分派</button>
						<button onclick="showOtherExpressDiv()">转其他快递</button>
						<button onclick="showOtherSiteDiv()">转其他站点</button>
						<button onclick="showApplyReturnDiv()">申请退货</button>
					</td>
				</tr>
				<tr>
					<td>包裹号xxx</td>
					<td>运单号xxx</td>
					<td>订单号xxx</td>
					<td>京东</td>
					<td>李四</td>
					<td>123456xxx</td>
					<td>北京市-朝阳区-xxxx</td>
					<td>2016-04-03 15:2:23</td>
					<td>周七</td>
					<td>123456xxx</td>
					<td>滞留（滞留原因）</td>
					<td><button>重新分派</button></td>
				</tr>
				<tr>
					<td>包裹号xxx</td>
					<td>运单号xxx</td>
					<td>订单号xxx</td>
					<td>京东</td>
					<td>李四</td>
					<td>123456xxx</td>
					<td>北京市-朝阳区-xxxx</td>
					<td>2016-04-03 15:2:23</td>
					<td>周七</td>
					<td>123456xxx</td>
					<td>滞留（滞留原因）</td>
					<td><button>重新分派</button></td>
				</tr>
			</table>
			<div class="fr"> 
				<a href="">上页</a>
				<a href="">1</a>
				<a href="">2</a>
				<a href="">3</a>
				<a href="">4</a>
				<a href="">下页</a>
			</div>
		</div>
	</div>
</div>

<!-- 重新分派面板-开始 -->
<div  id="chooseSender_div" class="popDiv" >
	<div class="title_div">重新分派</div>
	<div class="m20">
		<span>派件员:
			<select id="sender_select">  
				<option value ="张三">张三</option>  
				<option value ="李四">李四</option>  
				<option value="王五">王五</option>  
			</select>				  
		</span> 
	</div>
	<div class="m20">
		<button onclick="hideSenderDiv()">取消</button>
		<button onclick="chooseSender()">确定</button>
	</div>
</div>
<!-- 重新分派面板-结束 -->

<!-- 转其他快递面板-开始 -->
<div  id="chooseOtherExpress_div" class="popDiv" >
	<div class="title_div">转其他快递</div>
	<div class="m20">
		<span>快递公司:
			<select id="express_select">  
				<option value ="中通">中通</option>  
				<option value ="申通">申通</option>  
				<option value="顺风">顺风</option>  
			</select>				  
		</span> <br><br>
		<span>运单号：<input id="waybillId" name="waybillId" type="text" value="" placeholder="请输入运单号"/></span>
	</div>
	<div class="m20">
		<button onclick="hideOtherExpressDiv()">取消</button>
		<button onclick="chooseOtherExpress()">确定</button>
	</div>
</div>
<!-- 转其他快递面板-结束 -->


<!-- 转其他站点面板-开始 -->
<div  id="chooseOtherSite_div" class="popDiv" >
	<div class="title_div">转其他站点</div>
	<div class="m20">
		<span>站点:
			<select id="other_site_select">  
				<option value ="站点A">站点A</option>  
				<option value ="站点B">站点B</option>  
				<option value="站点C">站点C</option>  
			</select>				  
		</span> <br>
	</div>
	<div class="m20">
		<button onclick="hideOtherSiteDiv()">取消</button>
		<button onclick="chooseOtherSite('waybillId')">确定</button>
	</div>
</div>
<!-- 转其他站点面板-结束 -->

<!-- 申请退货-开始 -->
<div  id="apply_return_div" class="popDiv" >
	<div class="title_div">申请退货</div>
	<div class="m20">
		<span>选择退货原因:
			<select id="return_reason_select">  
				<option value ="货物破损">货物破损</option>  
				<option value ="超时配送">超时配送</option>  
				<option value="客户端要求退换">客户端要求退换</option>  
				<option value="其他">其他</option>  
			</select>				  
		</span> <br><br>
		<span>
			<textarea style="display: none;" rows="5" cols="50" id="returnReason" name="returnReason" placeholder="请输入退货原因">
				
			</textarea>
		</span>
	</div>
	<div class="m20">
		<button onclick="hideApplyReturnDiv()">取消</button>
		<button onclick="applyReturn('waybillId')">确定</button>
	</div>
</div>
<!-- 转其他站点面板-结束 -->

<script type="text/javascript" src="<c:url value="/resources/jquery/jquery-1.12.3.min.js" />"></script>

<script type="text/javascript">
$(document).ready(function() {
	$("#return_reason_select").change(function(){
		if(this.value == "其他"){
			$("#returnReason").show();
		} else {
			$("#returnReason").hide();
		}
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
	$("#sender").text($("#sender_select").val());
	$("#chooseSender_div").hide();
}

//显示转其他快递公司div
function showOtherExpressDiv(waybillId) {
	$("#chooseOtherExpress_div").show();
	
}
//隐藏转其他快递公司div
function hideOtherExpressDiv() {
	$("#chooseOtherExpress_div").hide();
}
//选择其他快递
function chooseOtherExpress() {
	$("#chooseOtherExpress_div").hide();
}

//显示转其他站点div
function showOtherSiteDiv(waybillId) {
	$("#chooseOtherSite_div").show();
}
//隐藏转其他站点div
function hideOtherSiteDiv() {
	$("#chooseOtherSite_div").hide();
}
//转其他站点
function chooseOtherSite() {
	$("#chooseOtherSite_div").hide();
}

//显示申请退货div
function showApplyReturnDiv(waybillId) {
	$("#apply_return_div").show();
}
//隐藏申请退货div
function hideApplyReturnDiv() {
	$("#apply_return_div").hide();
}
//确定退货
function applyReturn() {
	$("#apply_return_div").hide();
}

</script>
</body>
</html>