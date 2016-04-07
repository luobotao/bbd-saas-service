<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="keywords" content="棒棒哒-数据查询页面" />
	<meta name="description" content="棒棒哒-数据查询页面" />
	<title>数据查询页面</title>
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
				  <option value ="2">未到站</option>  
				  <option value="3">未分派</option> 
				  <option value="4">已分派</option>
				  <option value="5">滞留</option>
				  <option value="6">拒收</option>
				  <option value="7">已签收</option> 
				</select>  
			</span> 
			<span class="pl20">到站时间：<input id="toSiteTime" name="toSiteTime" type="text" value="2016-04-05" /></span>
			<span class="pl20">运单号：<input id="waybillId" name="waybillId" type="text" /></span>
			<br><br>
			<button id="queryData" name="queryData">查询</button>
			<button id="exportData" name="exportData" class="pl20">导出</button>
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
					<td>司机取货时间</td>
					<td>预计到站时间</td>
					<td>到站时间</td>
					<td>派送员</td>
					<td>派送员手机</td>
					<td>状态</td>
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
					<td>2016-04-05 15:2:23</td>
					<td>2016-04-05 13:2:23</td>
					<td>周七</td>
					<td>123456xxx</td>
					<td>滞留（滞留原因）</td>
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
					<td>2016-04-05 15:2:23</td>
					<td>2016-04-05 13:2:23</td>
					<td>周七</td>
					<td>123456xxx</td>
					<td>滞留（滞留原因）</td>
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
					<td>2016-04-05 15:2:23</td>
					<td>2016-04-05 13:2:23</td>
					<td>周七</td>
					<td>123456xxx</td>
					<td>滞留（滞留原因）</td>
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
</body>
</html>