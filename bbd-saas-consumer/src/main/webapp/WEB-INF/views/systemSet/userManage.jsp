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
	<title>系统设置-用户管理页面</title>
	<jsp:include page="../main.jsp" flush="true" />
</head>
<body class="fbg">
<% 
PageModel<User> userPage = (PageModel<User>)request.getAttribute("userPage");

%>
        <!-- S content -->
        <div class="clearfix b-branch">
        	<div class="container">
        		<div class="row">
        			<!-- S sidebar -->
        			<div class="col-xs-12 col-sm-12 col-md-3 col-lg-3" style="opacity:0;">
        				<ul class="b-sidebar">
							<li class="lv1"><a href="package-arrives.html"><i class="b-icon p-package"></i>包裹到站</a></li>
							<li class="lv1"><a href="tracking-assign.html"><i class="b-icon p-aign"></i>运单分派</a></li>
							<li class="lv1"><a href="exception-processing.html"><i class="b-icon p-error"></i>异常件处理</a></li>
							<li class="lv1"><a href="data-query.html"><i class="b-icon p-query"></i>数据查询</a></li>
							<li class="lv1 side-cur"><a href="system-distribution.html"><i class="b-icon p-set"></i>系统设置</a></li>
							<ul class="menu">
				                <li><a href="system-distribution.html">配送区域</a></li>
				                <li class="curr"><a href="system-usermanage.html">用户管理</a></li>
				                <li><a href="system-role.html">角色管理</a></li>
				            </ul>
						</ul>
        			</div>
        			<!-- E sidebar -->
        			<!-- S detail -->
        			<div class="b-detail col-xs-12 col-sm-12 col-md-9 col-lg-9">
        				<!-- S 搜索区域 -->
        				<form class="form-inline form-inline-n">
        					<div class="search-area">
	        					<div class="row pb20">
	        						<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
	        							<label>角色：</label>
	        							<select id="saasrole" name="saasrole" class="form-control form-con-new">
	        								<%=UserRole.Srcs2HTML(-1)%>
	        							</select>
	        						</div>
	        						<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
	        							<label>状态：</label>
	        							<select id="status" name="status" class="form-control form-con-new">
	        								<%=UserStatus.Srcs2HTML(-1)%>
	        							</select>
	        						</div>
	        						<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
	        							<label>关键字：</label>
	        							<input type="text" id="keyword" name="keyword" placeholder="真实姓名/手机号" class="form-control"  />
	        						</div>
	        						
	        					</div>
	        					<div class="row pb20">
	        						<div class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
	        							<a href="javascript:void(0)" onclick="toSearch();" class="ser-btn l"><i class="b-icon p-query p-ser"></i>查询</a>
	        							<a href="javascript:void(0)" onclick="restUserModel();" class="ser-btn d ml6 j-user"><i class="glyphicon glyphicon-plus mr10"></i>新建</a>
	        						</div>
	        					</div>
	        				</div>
        				</form>
        				<!-- E 搜索区域 -->
        				<div class="tab-bod mt20">
        					<!-- S table -->
        					<div class="table-responsive">
	        					<table class="table">
	        						<thead>
	        							<tr>
		        							<th>角色</th>
		        							<th>真实姓名</th>
		        							<th>手机号</th>
		        							<th>登录名</th>
		        							<th>状态</th>
		        							<th>操作</th>
	        							</tr>
	        						</thead>
	        						<tbody id="dataList">
	        							
	        							<%
											for(User user : userPage.getDatas()){
										%>
										<tr>
											
											<td><%=user.getRole().getMessage()%></td>
											<td><%=user.getRealName()%></td>
											<td><%=user.getPhone()%></td>
											<td><%=user.getLoginName()%></td>
											
											<% 
											
											if(user.getUserStatus()!=null){
												
												%>
												<td><%=user.getUserStatus().getMessage()%></td>
												<% 
											}else{
												
												%>
												<td>无</td>
											<% 
											}
											%>
											
											
											<td><a href="javascript:void(0);" onclick="searchUser('<%=user.getId() %>','')" class="orange j-user">修改</a>
											
											<% 
											
											if(user.getUserStatus()!=null && user.getUserStatus().getStatus()==1){
												
												%>
												<a href="javascript:void(0)" onclick="changeStatus(0,'<%=user.getId() %>','')" class="orange ml6">停用</a>
											
												<% 
											}else{
												
												%>
												
												<a href="javascript:void(0)" onclick="changeStatus(1,'<%=user.getId() %>','')" class="orange ml6">启用</a>
												<a href="javascript:void(0)" onclick="delUser('<%=user.getRealName() %>')" class="orange ml6">删除</a>
												<% 
											}
											
											
											%>
											
											</td>
											
										</tr>
										<%
											}
										%>
	        							
	        						</tbody>
	        					</table>
	        				</div>
	        				<!-- E table -->
	        				<!-- S tableBot -->
							<div class="clearfix pad20">
							    <!--页码 start-->
								<div id="pagin"></div>
								<!--页码 end-->
							</div>
							<!-- E tableBot -->
        				</div>	
        			</div>
        			<!-- E detail -->
        		</div>
        	</div>
        </div>
        <!-- E content -->
        <!-- S footer -->
        <footer class="pos-footer tc">
            <em class="b-copy">京ICP备 465789765 号 版权所有 &copy; 2016-2020 棒棒达       北京棒棒达科技有限公司</em>
        </footer>
        <!-- E footer -->
        <!-- S pop -->
        <!--S 新建-->
		<div class="j-user-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" id="myModal" aria-hidden="true">
			<div class="modal-dialog b-modal-dialog">
				<div class="modal-content">
					<div class="modal-header b-modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
						<h4 class="modal-title tc">新建</h4>
					</div>
					<form role="form" action="" method="post" id="userForm" >
					<div class="modal-body b-modal-body">
						<ul class="b-n-crt">
							<li>
								<select id="roleId" name="roleId" class="form-control form-bod">
									<%=UserRole.Srcs2HTML(-1)%>
								</select>
								<p class="help-block" id="roleIdP" style="display:none;">请选中一个角色</p>
							</li>
							<li>
								<input type="text" id="realName" name="realName" class="form-control form-bod" placeholder="真实姓名" />
								<p class="help-block" id="realNameP" style="display:none;">请输入姓名</p>
							</li>
							<li>
								<input type="text" id="phone" name="phone" class="form-control form-bod" placeholder="手机号" />
								<p class="help-block" id="phoneP" style="display:none;">请正确输入11位手机号</p>
							</li>
							<li>
								<input type="text" id="loginName" name="loginName" onblur="checkLoginName(this.value)" class="form-control form-bod" placeholder="登录名" />
								<p class="help-block" id="loginNameP" style="display:none;">请输入登录名</p>
							</li>
							<li>
								<input type="password" id="loginPass" name="loginPass" class="form-control form-bod" placeholder="登录密码" />
								<p class="help-block" id="loginpassP" style="display:none;">请输入密码</p>
							</li>
							<li>
								<input type="password" id="confirmPass" name="confirmPass" class="form-control form-bod" placeholder="确认密码" />
								<p class="help-block" id="confirmPassP" style="display:none;">请再次输入密码</p>
							</li>
						</ul>
							
						
						<div class="row mt20">
							<span class="col-md-12"><a href="javascript:void(0)" onclick="saveUserBtn()" class="sbtn sbtn2 l">保存</a></span>
						</div>
					</div>
					<input type="hidden" class="form-control" id="sign" name="sign">
					<input type="hidden" class="form-control" id="flag" name="flag" value="true">
					<input type="hidden" class="form-control" id="realNameTemp" name="realNameTemp">
					<input type="hidden" class="form-control" id="loginNameTemp" name="loginNameTemp">
					<input type="hidden" class="form-control" id="operate" name="operate">
					</form>
				</div>
			</div>
		</div>
		<!--E 新建-->
        <!-- E pop -->
<script type="text/javascript">
//显示分页条
var pageStr = paginNav(<%=userPage.getPageNo()%>, <%=userPage.getTotalPages()%>, <%=userPage.getTotalCount()%>);
$("#pagin").html(pageStr);


//加载带有查询条件的指定页的数据
function gotoPage(pageIndex,roleId,status,keyword) {
	var url = "<c:url value="/userManage/getUserPage" />";
	$.ajax({
		type : "GET",  //提交方式
		url : url,//路径
		data : {
			"pageIndex" : pageIndex,
			"roleId" : roleId,
			"status" : status,
			"keyword" : keyword
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
		},
		error : function() {
			alert("加载分页数据异常！");
		}
	});
}


//封装一行的数据
function getRowHtml(data){
	var row = "<tr>";
	var temp = "";
	row +=  "<td>" + data.roleMessage + "</td>";
	row += "<td>" + data.realName + "</td>";
	row += "<td>" + data.phone + "</td>";
	row += "<td>" + data.loginName + "</td>";
	
	if(data.userStatus!==null){
		row += "<td>" + data.statusMessage + "</td>";
	}else{
		row += "<td>无</td>";
	}
	
	//
	row += "<td><button id='editUser' name='editUser' data-toggle='modal' data-target='#myModal' href='javascript:void(0)' onclick=\"searchUser('"+temp+"','"+data.realName+"')\">修改</button>";
	
	
	if(data.userStatus=="<%=UserStatus.VALID%>"){ 
		row += "<a href='javascript:void(0)' onclick=\"changeStatus(0,'"+temp+"','"+data.realName+"')\" class=\"orange ml6\">停用</a>";
	}else{
		row += "<a href='javascript:void(0)' onclick=\"changeStatus(1,'"+temp+"','"+data.realName+"')\" class=\"orange ml6\">启用</a>";
		row += "<a href='javascript:void(0)' onclick=\"delUser('"+data.realName+"')\" class=\"orange ml6\">删除</a></td>";
	}

	row += "</tr>";
	return row;
}

function toSearch(){

	var roleId = $("#saasrole").val();
	var status = $("#status").val();
	var keyword = $("#keyword").val();
	gotoPage(0,roleId,status,keyword);
}

function checkLoginName(loginName) {
	var operate = document.getElementById("operate").value;
	var oldloginName = document.getElementById("loginNameTemp").value;
	var newloginName = loginName;
	if(operate=='create'){
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
					//alert("您输入的登录名目前已存在，请重新输入");
					$("#loginNameP").text("登录名目前已存在，请重新输入!");
				    $("#loginNameP").attr("style","color:red");
				    document.getElementById("flag").value='false';
				}else{
					document.getElementById("flag").value='true';
					$("#loginNameP").attr("style","display:none");
				}
			},
			error: function(){
				alert('服务器繁忙，请稍后再试！');
			}
		});
	}else{
		if(newloginName!==oldloginName){
			$.ajax({
				url: url+'?loginName='+loginName,
				type: 'GET',
				cache: false,
				dataType: "text",
				data: {},
				success: function(response){
					console.log(response);
					if(response=="true"){
						//alert("您输入的登录名目前已存在，请重新输入");
						$("#loginNameP").text("登录名目前已存在，请重新输入!");
					    $("#loginNameP").attr("style","color:red");
					    document.getElementById("flag").value='false';
					}else{
						document.getElementById("flag").value='true';
					}
				},
				error: function(){
					alert('服务器繁忙，请稍后再试！');
				}
			});
		}
	}
	
	
}

function checkUser(realname) {
	
	var operate = document.getElementById("operate").value;
	
	var newrealname = realname;
	
	var oldrealName = document.getElementById("realNameTemp").value;

	
	var url = "<c:url value="/userManage/checkUser" />";
	if(newrealname!==oldrealName){
		$.ajax({
			url: url+'?realname='+realname,
			type: 'GET',
			cache: false,
			dataType: "text",
			data: {},
			success: function(response){
				console.log(response);
				if(response=="true"){
					//alert("您输入的用户名目前已存在，请重新输入");
					$("#realNameP").text("用户名目前已存在，请重新输入!");
				    $("#realNameP").attr("style","color:red");
				    document.getElementById("flag").value='false';
				}else{
					document.getElementById("flag").value='true';
				}

			},
			error: function(){
				alert('服务器繁忙，请稍后再试！');
			}
		});
	}
}

function changeStatus(status,id,realName){
	$.ajax({
		type : "GET",  
        url : "<c:url value="/userManage/changestatus" />", 
        data : {  
            "id" : id,"status" : status,"realName" : realName  
        },
        success : function(data) {
			if(data == 'true'){
				alert("更新成功");
				gotoPage(0);
			} 
        },
        error : function() {  
       		alert("异常！");  
  		}    
    });
}

function delUser(id){
	
	var ret = false;
	if(confirm('确定要执行此操作吗?')){ 
		ret = true; 
	} 
	    
	if(ret){
		
		$.ajax({
			type : "GET",  
	        url : "<c:url value="/userManage/delUser" />", 
	        data : {  
	            "id" : id 
	        },
	        success : function(data) {
				if(data == 'true'){
					alert("删除成功");
					gotoPage(0);
				} 
	        },
	        error : function() {  
	       		alert("异常！");  
	  		}    
	    });
	}
	
	
}

function saveUserBtn(){
	
	var url = "";
	var getSign = document.getElementById("sign").value;
	
	var flag = document.getElementById("flag").value;

	if(flag=='true'){
		flag = true;
	}else{
		flag = false;
	}

	if(getSign=='edit'){
		url = '<c:url value="/userManage/editUser?${_csrf.parameterName}=${_csrf.token}" />';
	}else{
		url = '<c:url value="/userManage/saveUser?${_csrf.parameterName}=${_csrf.token}" />';
	}
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
	if(loginPass==confirmPass){
		$("#confirmPassP").attr("style","display:none");
	}else{
		$("#confirmPassP").attr("style","color:red");
		flag = false;
	}
	if(flag){
		console.log("succeful , submit");
		$("#userForm").ajaxSubmit({  
	        type: 'post',  
	        url: url ,  
	        success: function(data){  
	        	if(data=="true"){
	        		alert( "保存用户成功");  
	        		document.getElementById("userForm").reset();
	        		gotoPage(0);
	        	}else{
	        		alert( "保存用户失败");  
	        	}

	        },  
	        error: function(JsonHttpRequest, textStatus, errorThrown){  
	            alert( "error");  
	        }  
	    });
	}else{
		//alert("有非法内容，请检查内容合法性！");
		return false;
	}
	
}


function searchUser(id,realName){
	
	$.ajax({
		type : "GET",  
        url : "<c:url value="/userManage/getOneUser" />", 
        data : {  
            "id" : id,
            "realName" : realName
        },
        success : function(data) {
			if(data != null){
				
				$("#realName").val(data.realName);
				$("#phone").val(data.phone);
				$("#loginName").val(data.loginName);
				
				$("#roleId").val(data.roleStatus);
				$("#loginPass").val(data.passWord);
				$("#confirmPass").val(data.passWord);
				$("#loginName").attr("readonly",true);
				document.getElementById("sign").value="edit";
				document.getElementById("realNameTemp").value=data.realName;
				document.getElementById("loginNameTemp").value=data.loginName;
			}    
        },
        error : function() {  
       		alert("异常！");  
  		}    
    });
	
	
}


function restUserModel(){
	document.getElementById("userForm").reset();
	$("#loginName").attr("readonly",false);
	$("#loginNameP").attr("style","display:none");
	document.getElementById("operate").value = "create";
}
</script>
</body>
</html>