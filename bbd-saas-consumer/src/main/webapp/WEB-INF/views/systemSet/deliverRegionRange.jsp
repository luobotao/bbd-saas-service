<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="keywords" content="棒棒哒-系统设置-配送区域-配送范围" />
	<meta name="description" content="棒棒哒-系统设置-配送区域-配送范围" />
	<title>系统设置-配送区域-配送范围</title>
	<link href="<c:url value="/resources/frame.css" />" rel="stylesheet"  type="text/css" />	
		
</head>
<body>
<div>
	<jsp:include page="../top.jsp" flush="true" />	
</div>
<div class="content">
	<div class="content-left" id="content-left"><jsp:include page="../leftMenu.jsp" flush="true" /></div>
	<div class="content-main" id="content-main">
		<div  style="border-bottom:solid 1px gray;width:1200px">
			<div class="m20">
				站点名称：<span class="pl20">立水桥 ${sitename}</span><br><br>
				站点地址：<span class="pl20">北京市-朝阳区-立水桥-xxx ${sitename}</span><br><br>
				配送范围：
				<span>配送范围</span>
				<span class="pl20"><a href="<c:url value="/deliverRegion/map" />" >绘制电子围栏</a></span>
				<span class="pl20"><a href="<c:url value="/deliverRegion/key" />" >导入地址关键词</a></span>
			</div>
		</div>
		
		
		
		<div class="m20" >
			<br>
			<span class="pl20">设置配送范围后，将优先匹配站点附近的订单。</span><br><br>
			<span class="pl20">站点周围
					<select>  
					  <option value ="1">1</option>  
					  <option value ="2">2</option>  
					  <option value="3">3</option> 
					  <option value="4">4</option>
					  <option value="5">5</option>
					  <option value="6">6</option>
					  <option value="7">7</option>
					  <option value="8">8</option>
					  <option value="9">9</option>
					  <option value="10">10</option>
					  <option value ="11">11</option>  
					  <option value ="12">12</option>  
					  <option value="13">13</option> 
					  <option value="14">14</option>
					  <option value="15">15</option>
					  <option value="16">16</option>
					  <option value="17">17</option>
					  <option value="18">18</option>
					  <option value="19">19</option>
					  <option value="20">20</option>  
					</select>  
				</span> <br><br>
				<span class="pl20"><button  onclick="saveRange()">保存</button></span>
		</div>
	</div>
</div>
</body>
</html>