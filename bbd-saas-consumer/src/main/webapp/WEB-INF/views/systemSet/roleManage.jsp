<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="keywords" content="棒棒哒-系统设置-角色管理页面" />
	<meta name="description" content="棒棒哒-系统设置-角色管理页面" />
	<title>系统设置-角色管理页面</title>
</head>
<body>
<div>
</div>
<div class="content">
	<div class="content-left" id="content-left"><jsp:include page="../leftMenu.jsp" flush="true" /></div>
	<div class="content-main" id="content-main">
		<div class="m20">
			<span>角色:
				<select>  
				  <option value ="1">全部</option>  
				  <option value ="2">初级</option>  
				  <option value="3">普通</option> 
				  <option value="4">高级</option>
				</select>  
			</span> 
			<span class="pl20">状态:
				<select>  
				  <option value ="1">全部</option>  
				  <option value ="2">有效</option>  
				  <option value="3">无效</option> 
				</select>  
			</span> 
			<span class="pl20">
				关键词：<input id="keyword" name="keyword" type="text" value="" placeholder="真实姓名/手机号" />
			</span>
			<br><br>
			<button id="queryData" name="queryData">查询</button>
			<button id="newRole" name="newRole"  onClick="showRoleDiv()">新建</button>
		</div>
		
		<div class="m20">
			<table id="data_table" border="1" cellpadding="6px" cellspacing="0px"  style="background-color: #b9d8f3;">
				<tr>
					<td>角色</td>
					<td>操作</td>
				</tr>
				<tr>
					<td>角色xxx</td>
					<td>
						<button onClick="showRoleDiv('roleId')">修改</button>
						<button onClick="showPermissionDiv('roleId')">设置权限</button>
					</td>
				</tr>
				<tr>
					<td>角色xxx</td>
					<td>
						<button onClick="showRoleDiv('roleId')">修改</button>
						<button onClick="showPermissionDiv('roleId')">设置权限</button>
					</td>
				</tr>
				
			</table>
			<div class="fr300"> 
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


<!-- 新建角色面板-开始 -->
 <div  id="role_div" class="popDiv" >
	<div class="title_div">新建</div>
	<div class="m20">
		
		<span>
			角色:<input id="realName" name="realName" type="text" />	
		</span> <br><br>
		<div>
			<button onclick="saveRole()">保存</button>
		</div>
	</div>	
</div> 
<!-- 新建角色面板-结束 -->


<!-- 设置权限面板-开始 -->
<div  id="permission_div" class="popDiv_big" >
	<div class="title_div">设置权限</div>
	<div class="m20">
		一级菜单名
		<ul>
			<li><a href="<c:url value="/" />"  target="content-main">二级菜单</a></li>
			<span>
				<input type="checkbox" id="" name=""/>具体功能1
				<input type="checkbox" id="" name=""/>具体功能2
				<input type="checkbox" id="" name=""/>具体功能2
			</span>
			<li><a href="<c:url value="/" />"  target="content-main">二级菜单</a></li>
			<span>
				<input type="checkbox" id="" name=""/>具体功能1
				<input type="checkbox" id="" name=""/>具体功能2
				<input type="checkbox" id="" name=""/>具体功能2
			</span>
		</ul>
		<br>
		一级菜单名
		<ul>
			<li><a href="<c:url value="/" />"  target="content-main">二级菜单</a></li>
			<span>
				<input type="checkbox" id="" name=""/>具体功能1
				<input type="checkbox" id="" name=""/>具体功能2
				<input type="checkbox" id="" name=""/>具体功能2
			</span>
		</ul>
		<div>
			<button onclick="saveRolePermission()">保存</button>
		</div>
	</div>
</div>
<!-- 设置权限面板-结束 -->

<script type="text/javascript" src="<c:url value="/resources/jquery/jquery-1.12.3.min.js" />"></script>

<script type="text/javascript">
$(document).ready(function() {
	

});
//显示面板
function showDiv(divId) {
	$("#"+divId).show();
}
//隐藏面板
function hideDiv(divId) {
	$("#"+divId).hide();
}

//显示用户编辑面板
function showRoleDiv(roleId) {
	$("#role_div").show();
}
//保存角色
function saveRole() {
	$("#role_div").hide();
}
//显示设置权限面板
function showPermissionDiv(roleId) {
	$("#permission_div").show();
}
//保存权限
function saveRolePermission(roleId) {
	$("#permission_div").hide();
}
</script>


</body>
</html>