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
	<jsp:include page="../main.jsp" flush="true" />
</head>
<body>

<div>
	<%--<jsp:include page="../top.jsp" flush="true" /> --%>	
	
</div>



<div class="content">
	<div class="content-left" id="content-left"></div>
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
			<button id="newUser" name="newUser" data-toggle="modal" data-target="#myModal">新建</button>
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




<!-- 模态框（Modal） -->
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
   <div class="modal-dialog">
      <div class="modal-content">
         <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                  &times;
            </button>
            <h4 class="modal-title" id="myModalLabel">
             	  添加用户
            </h4>
         </div>
         <div class="modal-body">
		<form role="form" enctype="multipart/form-data" action="<c:url value="/userManage/saveUser" />" method="post" id="userForm" >
						<div class="box-body">
						<div class="row" id="usernameDiv" style="margin-top:10px;">
								<div class="col-xs-4">
									<label for="title">角色:</label>
									<select name="roleId">
									  <option value="1">站长</option>
									  <option value="2">派件员</option>
									</select>
								</div>
								<div class="col-xs-4">
									<label for="title">真实姓名:</label>
									<input type="text" class="form-control" id="realName" name="realName" onblur="checkUser(this.value)">
									<p class="help-block" id="realNameP" style="display:none;">请输入姓名</p>
								</div>
								<div class="col-xs-4">
									<label for="title">手机号:</label>
									<input type="text" class="form-control" id="phone" name="phone">
									<p class="help-block" id="phoneP" style="display:none;">请正确输入11位手机号</p>
								</div>
								<div class="col-xs-4">
									<label for="title">登录名:</label>
									<input type="text" class="form-control" id="loginName" name="loginName" onblur="checkLognName(this.value)">
									<p class="help-block" id="loginNameP" style="display:none;">请输入姓名</p>
								</div>
								<div class="col-xs-4">
									<label for="title">登录密码:</label>
									<input type="text" class="form-control" id="loginPass" name="loginPass">
									<p class="help-block" id="loginpassP" style="display:none;">请输入密码</p>
								</div>
								<div class="col-xs-4">
									<label for="title">确认密码:</label>
									<input type="text" class="form-control" id="confirmPass" name="confirmPass">
									<p class="help-block" id="confirmPassP" style="display:none;">请再次输入密码</p>
								</div>
							</div>
							<div class="box-footer">
							&nbsp;&nbsp;<button type="button" class="btn btn-primary" id="saveUserBtn" style="margin-left: 10px;">保存</button>
						</div>
					</form>
         </div>
      </div><!-- /.modal-content -->
</div><!-- /.modal -->
</div>

<script type="text/javascript">

//保存
function saveUser1() {
	alert("ssss");
	var link = "<c:url value="/userManage/saveUser" />";
	$.ajax({ url: link,
		beforeSend: function(req) { 
			req.setRequestHeader("Accept", "application/rss+xml");
		},
		success: function(feed) {
			MvcUtil.showSuccessResponse(MvcUtil.xmlencode(feed), link);
		},
		error: function(xhr) { 
			MvcUtil.showErrorResponse(xhr.responseText, link);
		}
	});
}

function checkUser() {
	alert("ssss");
	$.ajax({
		url: '/site/checkSiteWithLoginName?loginName='+loginName,
		type: 'GET',
		cache: false,
		dataType: "text",
		data: {},
		success: function(response){
			console.log(response);
			if(response=="true"){
				alert("您输入的帐号目前已存在，请重新输入");
				//$("#usernameFlag").val(0);
			}else{
				//$("#usernameFlag").val(1);
			}
		},
		error: function(){
			alert('服务器繁忙，请稍后再试！');
		}
	});
}
$("#saveUserBtn").click(function(){alert("ssss");
	var flag = true;
	var phone = $("#phone").val();
	var realName = $("#realName").val();
	var phone = $("#phone").val();
	var loginName = $("#loginName").val();
	var loginPass = $("#loginPass").val();
	var confirmPass = $("#confirmPass").val();
	var tel_reg = /^1[34578]{1}\d{9}/;
	$("#userForm").submit();
	
})
</script>

</body>
</html>