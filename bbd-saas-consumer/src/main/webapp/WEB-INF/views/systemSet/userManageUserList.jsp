<%@ page import="com.bbd.saas.mongoModels.User" %>
<%@ page import="com.bbd.saas.enums.UserRole" %>
<%@ page import="com.bbd.saas.enums.UserStatus" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.ArriveStatus" %>
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
		<c:url var="userListAction" value="/userManage/userListpost?${_csrf.parameterName}=${_csrf.token}"/>
		<form id="userListForm" action="${userListAction}" method="post">     
		<div class="m20">
			<span>
				<label>角色:</label>
						<select id="saasrole" name="saasrole" class="form-control">
							<%=UserRole.Srcs2HTML(-1)%>
						</select>  
			</span> 
			<span>
			<label>状态：</label>
						<select id="state" name="state" class="form-control">
							<%=UserStatus.Srcs2HTML(-1)%>
						</select>
			</span>
			<span class="pl20">
				关键词：<input id="keyword" name="keyword" type="text" value="" placeholder="真实姓名/手机号" />
			</span>
			<br><br>
			<button id="queryData" name="queryData" onclick="search()">查询</button>
			
		</div>
		<input type="hidden" name="page" id="page" value="<s:property value="#request.page" />" />
		</form>
		<button id="newUser" name="newUser" data-toggle="modal" data-target="#myModal">新建</button>
		<div class="m20">
			<table id="data_table" border="1" cellpadding="6px" cellspacing="0px"  style="background-color: #b9d8f3;">
				<thead>
				<tr>
					<td>角色</td>
					<td>真实姓名</td>
					<td>手机号</td>
					<td>登录名</td>
					<td>状态</td>
					<td>操作</td>
				</tr>
				</thead>
				
				<tbody>
			
				<%
					PageModel<User> userPage = (PageModel<User>)request.getAttribute("userPage");
					for(User user : userPage.getDatas()){
				%>
				<tr>
					
					<td><%=user.getRole()%></td>
					<td><%=user.getRealName()%></td>
					<td><%=user.getPhone()%></td>
					<td><%=user.getLoginName()%></td>
					<td><%=user.getState()%></td>
					
					<td>修改|
					<c:set var="state" value="<%=user.getUserStatus().getStatus()%>"/>
					<c:if test="${state=='0'}">
				　　
					<a href="javascript:void(0);" staffId="<%=user.getId()%>"  onclick="changeStatus(this,'<%=user.getId()%>',0)" >启用</a>
				　　</c:if>
					<c:if test="${state=='1'}">
				　　停用
				　　</c:if>
					</td>
					
				</tr>
				<%
					}
				%>
				</tbody>
				
				
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
         <c:url var="actionUrl" value="/userManage/saveUser?${_csrf.parameterName}=${_csrf.token}"/>
		 <form role="form" action="${actionUrl}" method="post" id="userForm" >
						<div class="box-body">
						<div class="row" id="usernameDiv" style="margin-top:10px;">
								<div class="col-xs-4">
									<label for="title">角色:</label>
									<select id="roleId" name="roleId" class="form-control">
										<%=UserRole.Srcs2HTML(-1)%>
									</select> 
									<p class="help-block" id="roleIdP" style="display:none;">请选中一个角色</p>
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
									<input type="text" class="form-control" id="loginName" name="loginName" onblur="checkLoginName(this.value)">
									<p class="help-block" id="loginNameP" style="display:none;">请输入登录名</p>
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

function checkLoginName(loginName) {

	var url = "<c:url value="/userManage/checkLognName" />";
	$.ajax({
		url: url+'?loginName='+loginName,
		type: 'GET',
		cache: false,
		dataType: "text",
		data: {},
		success: function(response){
			console.log(response);
			if(response=="true"){
				alert("您输入的登录名目前已存在，请重新输入");
				document.getElementById("userForm").reset();
			}
			
			//$("#usernameFlag").val(0);
			
		},
		error: function(){
			alert('服务器繁忙，请稍后再试！');
		}
	});
}

function checkUser(realname) {

	var url = "<c:url value="/userManage/checkUser" />";
	$.ajax({
		url: url+'?realname='+realname,
		type: 'GET',
		cache: false,
		dataType: "text",
		data: {},
		success: function(response){
			console.log(response);
			if(response=="true"){
				alert("您输入的用户名目前已存在，请重新输入");
				document.getElementById("userForm").reset();
			}
			
			//$("#usernameFlag").val(0);
			
		},
		error: function(){
			alert('服务器繁忙，请稍后再试！');
		}
	});
}
$("#saveUserBtn1").click(function(){alert("ssss");
	var flag = true;
	var phone = $("#phone").val();
	var realName = $("#realName").val();
	var loginName = $("#loginName").val();
	var loginPass = $("#loginPass").val();
	var confirmPass = $("#confirmPass").val();
	var tel_reg = /^1[34578]{1}\d{9}/;
	$("#userForm").submit();
	
})

$("#saveUserBtn").click(function(){
	var url = "${actionUrl}";
	var flag = true;
	var roleId = $("#roleId").val();
	var phone = $("#phone").val();
	var realName = $("#realName").val();
	var loginName = $("#loginName").val();
	var loginPass = $("#loginPass").val();
	var confirmPass = $("#confirmPass").val();
	var tel_reg = /^1[34578]{1}\d{9}/;
	if (roleId=="-1") {
	    $("#roleIdP").attr("style","color:red");
		flag = false;
	}else{
		$("#roleIdP").attr("style","display:none");
	}
	if (!phone) {
	    $("#phoneP").attr("style","color:red");
		flag = false;
	}else{
		$("#phoneP").attr("style","display:none");
	}
	if (!tel_reg.test(phone)) {
		$("#phoneP").attr("style","color:red");
		flag = false;
	}else{
		$("#phoneP").attr("style","display:none");
	}
	if (!realName) {
	    $("#realNameP").attr("style","color:red");
		flag = false;
	}else{
		$("#realNameP").attr("style","display:none");
	}
	if (!loginName) {
	    $("#loginNameP").attr("style","color:red");
		flag = false;
	}else{
		$("#loginNameP").attr("style","display:none");
	}
	if (!loginPass) {
	    $("#loginpassP").attr("style","color:red");
		flag = false;
	}else{
		$("#loginpassP").attr("style","display:none");
	}
	if (!confirmPass) {
	    $("#confirmPassP").attr("style","color:red");
		flag = false;
	}else{
		$("#confirmPassP").attr("style","display:none");
	}
	if(flag){
		console.log("succeful , submit");
		$("#userForm").ajaxSubmit({  
	        type: 'post',  
	        url: url ,  
	        success: function(data){  
	        	if(data=="true"){
	        		alert( "添加用户成功");  
	        		document.getElementById("userForm").reset();
	        	}else{
	        		alert( "添加用户失败");  
	        	}
	            
	            //$( "#wfAuditForm").resetForm();  
	        },  
	        error: function(JsonHttpRequest, textStatus, errorThrown){  
	            alert( "error");  
	        }  
	    });  
	}else{
		alert("有非法内容，请检查内容合法性！");
		return false;
	}
	
})

function changeStatus(a,id,status){
			alert(id);
			alert(status);
		//var url = "<c:url value="/userManage/changestatus" />";
		if(status==0){
			//表示当前为停用，要启用
			$.ajax({
				type : "GET",  
	            url : "<c:url value="/userManage/changestatus" />", 
	            data : {  
	                "id" : id,"status" : status 
	            },
	            success : function(data) {
					if(data != null){
					} 
	            },
	            error : function() {  
	           		alert("异常！");  
	      		}    
	        });
		}
		if(status==1){
			//表示当前为启用，要停用
		}
}

function search(){alert("ss");
	//document.getElementById("page").value= page;
	document.getElementById('userListForm').submit();
}



</script>

</body>
</html>