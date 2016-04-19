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
			<div class="container-fluid">
        		<div class="row">
        			<!-- S sidebar -->
					<div class="col-xs-12 col-sm-12 bbd-md-3" style="opacity:0;">
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
					<div class="b-detail col-xs-12 col-sm-12 bbd-md-9">
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
												<a href="javascript:void(0)" onclick="delUser('<%=user.getLoginName() %>')" class="orange ml6">删除</a>
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
							<!--  
							<li>
								<input type="text" id="phone" name="phone" class="form-control form-bod" placeholder="手机号" />
								<p class="help-block" id="phoneP" style="display:none;">请正确输入11位手机号</p>
							</li>
							-->
							<li>
								<input type="text" id="loginName" name="loginName" onblur="checkLoginName(this.value)" class="form-control form-bod" placeholder="手机号" />
								<p class="help-block" id="loginNameP" style="display:none;">请正确输入11位手机号</p>
							</li>
							<li>
								<input type="text" id="staffid" name="staffid" onblur="checkStaffid(this.value)" class="form-control form-bod" placeholder="员工ID" />
								<p class="help-block" id="staffidP" style="display:none;">请输入员工ID</p>
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
					<input type="hidden" class="form-control" id="flaglogin" name="flaglogin">
					<input type="hidden" class="form-control" id="flagstaffid" name="flagstaffid">
					<input type="hidden" class="form-control" id="loginNameTemp" name="loginNameTemp">
					<input type="hidden" class="form-control" id="staffidTemp" name="staffidTemp">
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
	row += "<td>" + data.loginName + "</td>";
	//row += "<td>" + data.staffid + "</td>";
	if(data.userStatus!==null){
		row += "<td>" + data.statusMessage + "</td>";
	}else{
		row += "<td>无</td>";
	}
	
	//
	row += "<td><button id='editUser' name='editUser' data-toggle='modal' data-target='#myModal' href='javascript:void(0)' onclick=\"searchUser('"+temp+"','"+data.loginName+"')\">修改</button>";
	
	
	if(data.userStatus=="<%=UserStatus.VALID%>"){ 
		row += "<a href='javascript:void(0)' onclick=\"changeStatus(0,'"+temp+"','"+data.loginName+"')\" class=\"orange ml6\">停用</a>";
	}else{
		row += "<a href='javascript:void(0)' onclick=\"changeStatus(1,'"+temp+"','"+data.loginName+"')\" class=\"orange ml6\">启用</a>";
		row += "<a href='javascript:void(0)' onclick=\"delUser('"+data.loginName+"')\" class=\"orange ml6\">删除</a></td>";
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
	var url = '<c:url value="/userManage/checkLognName" />';
	if(loginName!=''){
		
		if(operate=='create'){
			
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
						$("#loginNameP").text("登录名已存在，请重新输入11位手机号!");
					    $("#loginNameP").attr("style","color:red");
					    document.getElementById("flaglogin").value='false';
					}else{
						document.getElementById("flaglogin").value='true';
						$("#loginNameP").attr("style","display:none");
					}
				},
				error: function(){
					alert('服务器繁忙，请稍后再试！');
				}
			});
		}
		
	}

}


function checkStaffid(staffid) {
	
	var operate = document.getElementById("operate").value;
	var oldstaffid = document.getElementById("staffidTemp").value;
	var newstaffid = staffid;
	var url = '<c:url value="/userManage/checkStaffIdBySiteByStaffid" />';
	if(staffid!==''){
		
		if(operate=='create'){
			
			$.ajax({
				url: url+'?staffid='+staffid,
				type: 'GET',
				cache: false,
				dataType: "text",
				data: {},
				success: function(response){
					console.log(response);
					if(response=="true"){
						//alert("您输入的登录名目前已存在，请重新输入");
						$("#staffidP").text("该站点下的staffid已存在，请重新输入!");
					    $("#staffidP").attr("style","color:red");
					    document.getElementById("flagstaffid").value='false';
					}else{
						document.getElementById("flagstaffid").value='true';
						$("#staffidP").attr("style","display:none");
					}
				},
				error: function(){
					alert('服务器繁忙，请稍后再试！');
				}
			});
		}else{
			if(newstaffid!==oldstaffid){
				$.ajax({
					url: url+'?staffid='+staffid,
					type: 'GET',
					cache: false,
					dataType: "text",
					data: {},
					success: function(response){
						console.log(response);
						if(response=="true"){
							//alert("您输入的登录名目前已存在，请重新输入");
							$("#staffidP").text("该站点下的staffid已存在，请重新输入!");
						    $("#staffidP").attr("style","color:red");
						    document.getElementById("flagstaffid").value='false';
						}else{
							document.getElementById("flagstaffid").value='true';
							$("#staffidP").attr("style","display:none");
						}
					},
					error: function(){
						alert('服务器繁忙，请稍后再试！');
					}
				});
			}else{
				document.getElementById("flagstaffid").value='true';
				$("#staffidP").attr("style","display:none");
			}
		}
		
	}
	
}


function changeStatus(status,id,loginName){
	$.ajax({
		type : "GET",  
        url : '<c:url value="/userManage/changestatus" />', 
        data : {  
            "id" : id,"status" : status,"loginName" : loginName  
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

function delUser(loginName){
	
	var ret = false;
	if(confirm('确定要执行此操作吗?')){ 
		ret = true; 
	} 
	    
	if(ret){
		
		$.ajax({
			type : "GET",  
	        url : '<c:url value="/userManage/delUser" />', 
	        data : {  
	            "loginName" : loginName 
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
	//operate这个隐藏域就是为了区别这个操作时修改还是新建
	var getSign = document.getElementById("operate").value;
	var flag = "";
	//页面隐藏域存放flagstaffid、flaglogin，当执行checkStaffid()和checkLognName()方法时，如何发现有重复的staffid或者loginName存在
	//就将flagstaffid或者flaglogin的值置为"false",如果不存在就重新置为"true"
	var flagstaffid = document.getElementById("flagstaffid").value;
	var flaglogin = document.getElementById("flaglogin").value;

	//如果flagstaffid、flaglogin为空表示当前操作为新建，就将flag初始化为 "true";
	if(!flagstaffid && !flaglogin){
		flag = "true";
	}
	//已验证过checkStaffid()和checkLognName(),只有flagstaffid和flaglogin都为'true'时才允许提交，false赋值为true;
	if(flagstaffid=='true' && flaglogin=='true'){
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
	var realName = $("#realName").val();
	var loginName = $("#loginName").val();
	var staffid = $("#staffid").val();
	var loginPass = $("#loginPass").val();
	var confirmPass = $("#confirmPass").val();
	var tel_reg = /^1[34578]{1}\d{9}/;
	var loginpasstemp = true;
	var confirmpasstemp = true;
	if (roleId=="-1") {
	    $("#roleIdP").attr("style","color:red");
		flag = false;
	}else{
		$("#roleIdP").attr("style","display:none");
	}
	if (!loginName) {
	    $("#loginNameP").attr("style","color:red");
		flag = false;
	}else{
		$("#loginNameP").attr("style","display:none");
	}
	if (!tel_reg.test(loginName)) {
		$("#loginNameP").attr("style","color:red");
		flag = false;
	}else{
		$("#loginNameP").attr("style","display:none");
	}
	if (!realName) {
	    $("#realNameP").attr("style","color:red");
		flag = false;
	}else{
		$("#realNameP").attr("style","display:none");
	}
	if (!staffid) {
	    $("#staffidP").attr("style","color:red");
		flag = false;
	}else{
		$("#staffidP").attr("style","display:none");
	}
	if (!loginPass) {
	    $("#loginpassP").attr("style","color:red");
		flag = false;
		loginpasstemp = false;
	}else{
		$("#loginpassP").attr("style","display:none");
	}
	if (!confirmPass) {
	    $("#confirmPassP").attr("style","color:red");
		flag = false;
		confirmpasstemp = false;
	}else{
		$("#confirmPassP").attr("style","display:none");
	}
	if(loginpasstemp && confirmpasstemp && loginPass==confirmPass){
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
	        		
	        		
	        		if(getSign=='edit'){
	        		}else{
	        			document.getElementById("userForm").reset();
	        		}
	        		
	        		
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


function searchUser(id,loginName){
	
	$.ajax({
		type : "GET",  
        url : '<c:url value="/userManage/getOneUser" />', 
        data : {  
            "id" : id,
            "loginName" : loginName
        },
        success : function(data) {
			if(data != null){
				document.getElementById("userForm").reset();
				$("#realName").val(data.realName);
				$("#loginName").val(data.loginName);
				$("#staffid").val(data.staffid);
				$("#roleId").val(data.roleStatus);
				$("#loginPass").val(data.passWord);
				$("#confirmPass").val(data.passWord);
				$("#loginName").attr("readonly",true);
				//document.getElementById("sign").value="edit";
				document.getElementById("loginNameTemp").value=data.loginName;
				document.getElementById("staffidTemp").value=data.staffid;
				document.getElementById("operate").value = "edit";
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
	$("#roleIdP").attr("style","display:none");
	$("#realNameP").attr("style","display:none");
	$("#loginNameP").text("请正确输入11位手机号");
	$("#staffidP").text("请输入员工ID");
	$("#staffidP").attr("style","display:none");
	$("#loginNameP").attr("style","display:none");
	$("#loginpassP").attr("style","display:none");
	$("#confirmPassP").attr("style","display:none");
	document.getElementById("operate").value = "create";
}
</script>
</body>
</html>