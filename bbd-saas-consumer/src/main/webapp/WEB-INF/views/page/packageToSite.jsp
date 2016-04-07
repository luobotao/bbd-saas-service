<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="keywords" content="棒棒哒-包裹到站页面" />
	<meta name="description" content="棒棒哒-包裹到站页面" />
	<title>包裹到站页面</title>
	<link href="<c:url value="/resources/frame.css" />" rel="stylesheet"  type="text/css" />		
	
</head>
<body >
<div>
	<jsp:include page="../top.jsp" flush="true" />	
</div>
<div class="content">
	<div class="content-left" id="content-left"><jsp:include page="../leftMenu.jsp" flush="true" /></div>
	<div class="content-main" id="content-main">
		<!-- 订单数显示 开始 -->
		<div>
			<div class="panel"><span>${non_arrival_num}</span><br><span>今日未到站订单数</span></div>
			<div class="panel"><span>${history_non_arrival_num}</span><br><span>历史未到站订单数</span></div>
			<div class="panel"><span>${arrived_num}</span><br><span>今日已到站订单数</span></div>		
		</div>
		<!-- 订单数显示 结束   -->
		<div class="m20">
			<span>状态:
				<select>  
				  <option value ="1">全部</option>  
				  <option value ="2">未到站</option>  
				  <option value="3">已扫描到站</option>  
				</select>  
			</span> 
			<span class="pl20">预计到站时间：<input id="toSiteTime" name="toSiteTime" type="text" /></span>
			<span class="pl20"><input id="queryData" name="queryData" type="button" value="查询"/></span>
		</div>
		<div class="m20">
			<span>扫描包裹号：<input id="packageId" name="packageId" type="text" /></span>
			<span class="pl20">扫描运单号：<input id="waybillId" name="waybillId" type="text" /></span>
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
					<td>库房打单时间</td>
					<td>预计到站时间</td>
					<td>状态</td>
				</tr>
				<tr>
					<td>包裹号1</td>
					<td>运单号1</td>
					<td>订单号1</td>
					<td>淘宝</td>
					<td>李四</td>
					<td>12345678912</td>
					<td>北京市-朝阳区-xxxx</td>
					<td>2016-04-03 15:2:23</td>
					<td>2016-04-06</td>
					<td>未到站</td>
				</tr>
				<tr>
					<td>包裹号1</td>
					<td>运单号1</td>
					<td>订单号1</td>
					<td>淘宝</td>
					<td>李四</td>
					<td>12345678912</td>
					<td>北京市-朝阳区-xxxx</td>
					<td>2016-04-03 15:2:23</td>
					<td>2016-04-06</td>
					<td>未到站</td>
				</tr>
				<tr>
					<td>包裹号1</td>
					<td>运单号1</td>
					<td>订单号1</td>
					<td>淘宝</td>
					<td>李四</td>
					<td>12345678912</td>
					<td>北京市-朝阳区-xxxx</td>
					<td>2016-04-03 15:2:23</td>
					<td>2016-04-06</td>
					<td>未到站</td>
				</tr>
			</table>
			<div class="fr50"> 
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