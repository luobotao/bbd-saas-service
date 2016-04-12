<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<meta name="keywords" content="棒棒哒-系统设置-用户管理页面" />
	<meta name="description" content="棒棒哒-系统设置-用户管理页面" />
	<title>系统设置-用户管理页面</title>
	<link href="<c:url value="/resources/frame.css" />" rel="stylesheet"  type="text/css" />	
	
	<link href="http://libs.baidu.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet">
   <script src="http://libs.baidu.com/jquery/2.0.0/jquery.min.js"></script>
   <script src="http://libs.baidu.com/bootstrap/3.0.3/js/bootstrap.min.js"></script>
	
</head>
<body>
<div>
	<%--<jsp:include page="../top.jsp" flush="true" /> --%>	
	<jsp:include page="../main.jsp" flush="true" />
</div>


<div class="modal hide fade" id="myModal" tabindex="-1" role="dialog">
<div class="modal-header"><button class="close" type="button" data-dismiss="modal">×</button>
<h3 id="myModalLabel">Modal header</h3>
</div>
<div class="modal-body"></div>
</div>






<!-- 按钮触发模态框 -->
<button class="btn btn-primary btn-lg" data-toggle="modal" 
   data-target="#myModal">
   开始演示模态框
</button>

<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" 
   aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" 
               data-dismiss="modal" aria-hidden="true">
                  &times;
            </button>
            <h4 class="modal-title" id="myModalLabel">
               模态框（Modal）标题
            </h4>
         </div>
         <div class="modal-body">
            在这里添加一些文本
         </div>
         <div class="modal-footer">
            <button type="button" class="btn btn-default" 
               data-dismiss="modal">关闭
            </button>
            <button type="button" class="btn btn-primary">
               提交更改
            </button>
         </div>
      </div><!-- /.modal-content -->
</div><!-- /.modal -->
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
			<button id="newUser" name="newUser"  onClick="addUserDiv()" data-toggle="modal" data-target="#myModal">新建</button>
			<input id="btntext" type="button" value="添加文本组件" data-toggle="modal" data-target="#myModal"  href="../SysManage/ZuJianManage.aspx"/>
		</div>
		
		<div class="m20">
			<table id="data_table" border="1" cellpadding="6px" cellspacing="0px"  style="background-color: #b9d8f3;">
				<tr>
					<td>角色</td>
					<td>真实姓名</td>
					<td>手机号</td>
					<td>登录名</td>
					<td>状态</td>
					<td>操作</td>
				</tr>
				<tr>
					<td>角色xxx</td>
					<td>张三</td>
					<td>12345xxx</td>
					<td>zhangsan</td>
					<td>有效</td>
					<td>
						<button onClick="showUserDiv('userId')">修改</button>
						<button onClick="disableUser('userId')">停用</button>
					</td>
				</tr>
				<tr>
					<td>角色xxx</td>
					<td>张三</td>
					<td>12345xxx</td>
					<td>zhangsan</td>
					<td>有效</td>
					<td>
						<button onClick="showUserDiv('userId')">修改</button>
						<button onClick="activeUser('userId')">激活</button>
					</td>
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
<!-- 新建用户面板-开始 -->
<div  id="user_div" class="popDiv" >
	<div class="title_div">新建</div>
	<div class="m20">
		<span>角色:
			<select id="role">  
				<option value ="初级">初级</option>  
				<option value ="普通">普通</option>  
				<option value="高级">高级</option>  
			</select>				  
		</span> <br><br>
		<span>
			真实姓名:<input id="realName" name="realName" type="text" />	
		</span> <br><br>
		<span>
			手机号:<input id="phone" name="phone" type="text" />	
		</span> <br><br>
		<span>
			登录名:<input id="userName" name="userName" type="text" />	
		</span> <br><br>
		<span>
			登录密码:<input id="passWord" name="passWord" type="text" />	
		</span> <br><br>
		<span>
			确认密码:<input id="passWord2" name="passWord2" type="text" />	
		</span> <br><br>
	</div>
	<div>
		<button onclick="saveUser()">保存</button>
	</div>
<div>
<!-- 新建用户面板-结束 -->


<script type="text/javascript">

//显示用户编辑面板
function showUserDiv(userId) {
	$("#user_div").show();
}
//添加用户编辑面板
function addUserDiv(userId) {$('#myModal').modal('show');
	//$("#user_div").show();
	//var html = $('#add_user').html();
	//MPHdialog.show(html);
}
//停用
function disableUser(userId) {
	
}
//激活
function activeUser(userId) {
	
}
//保存
function saveUser() {
	$("#user_div").hide();
}

</script>


</body>
</html>