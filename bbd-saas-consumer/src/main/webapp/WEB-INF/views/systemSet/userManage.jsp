<%@ page import="com.bbd.saas.mongoModels.User" %>
<%@ page import="com.bbd.saas.enums.UserRole" %>
<%@ page import="com.bbd.saas.enums.UserStatus" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="../main.jsp"%>
<html>
<head>
	<title>系统设置-用户管理页面</title>
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
			<div class="col-xs-12 col-sm-12 bbd-md-3" style="visibility: hidden;">
				<ul class="sub-sidebar">
				</ul>
			</div>
			<!-- E sidebar -->
			<!-- S detail -->
			<div class="b-detail col-xs-12 col-sm-12 bbd-md-9">
				<!-- S 搜索区域 -->
				<form class="form-inline form-inline-n">
					<div class="search-area">
						<div class="row pb20">
							<jsp:include page="../siteControl.jsp" flush="true" />
						</div>
						<div class="row pb20">
							<%--<c:if test="${userNow.role==UserRole.COMPANY}">
								<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
									<label>　站点：</label>
									<select id="sites" name="sites" class="form-control form-con-new">
										<option value ="-1" selected ="selected">全部</option>
										<c:forEach var="site" items="${siteList}">
											<option value="${site.id}">${site.name}</option>
										</c:forEach>
									</select>
								</div>
							</c:if>--%>
							<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-3">
								<label>　角色：</label>
								<select id="saasrole" name="saasrole" class="form-control form-con-new" readonly="readOnly">
									<c:if test="${userNow.role==UserRole.COMPANY}">
										<option value ="-1" selected ="selected">全部</option>
										<option value ="<%=UserRole.SITEMASTER%>"><%=UserRole.SITEMASTER.getMessage()%></option>
										<option value ="<%=UserRole.SENDMEM%>"><%=UserRole.SENDMEM.getMessage()%></option>
									</c:if>
									<c:if test="${userNow.role==UserRole.SITEMASTER}">
										<option value ="<%=UserRole.SENDMEM%>" selected ="selected"><%=UserRole.SENDMEM.getMessage()%></option>
									</c:if>
								</select>
							</div>
							<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-3">
								<label>状态：</label>
								<select id="status" name="status" class="form-control form-con-new">
									<%=UserStatus.Srcs2HTML(-1)%>
								</select>
							</div>
								<div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-3">
									<label>关键字：</label>
									<input type="text" id="keyword" name="keyword" placeholder="真实姓名/手机号" class="form-control"  />
								</div>
								<div class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-3">
									<a href="javascript:void(0)" onclick="gotoPage(0);" class="ser-btn l"><i class="b-icon p-query p-ser"></i>查询</a>
									<c:if test="${userNow.role==UserRole.SITEMASTER}">
										<a href="javascript:void(0)" onclick="showAddUserDiv();" class="ser-btn d ml6 j-user"><i class="num-add mr10">＋</i>新建</a>
									</c:if>
								</div>
						</div>
						<%--<div class="row pb20">
						</div>--%>
					</div>
				</form>
				<!-- E 搜索区域 -->
				<div class="tab-bod mt20">
					<!-- S table -->
					<div class="table-responsive">
						<table class="table">
							<thead>
							<tr>
								<c:if test="${userNow.role==UserRole.COMPANY}"><th>站点</th></c:if>
								<th>角色</th>
								<th>真实姓名</th>
								<th>手机号</th>
								<th>状态</th>
								<th>操作</th>
							</tr>
							</thead>
							<tbody id="dataList">

							<%
								for(User user : userPage.getDatas()){
							%>
							<tr>
								<c:if test="${userNow.role==UserRole.COMPANY}"><td><%=user.getSite()==null?"":user.getSite().getName()%></td></c:if>
								<td><%=user.getRole()==null?"":user.getRole().getMessage()%></td>
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
									<a href="javascript:void(0)" data-toggle='modal' data-target='#changeModal' onclick="changeStatus(3,'','<%=user.getLoginName() %>')" class="orange ml6">停用</a>
									<%
									}else{
									%>
									<a href="javascript:void(0)" data-toggle='modal' data-target='#changeModal' onclick="changeStatus(1,'','<%=user.getLoginName() %>')" class="orange ml6">启用</a>
									<%
										}
									%>
									<%--只有派件员才会显示 开通到站权限 --%>
									<%
										if(user.getRole() == UserRole.SENDMEM){
											if(user.getDispatchPermsn() == 1){
									%>
												<a href="javascript:void(0)" data-toggle='modal' data-target='#dispatchPermsn' onclick="showDispatchPermsnDiv(0, '<%=user.getLoginName() %>')" class="orange ml6">关闭到站权限</a>
									<%
											}else{
									%>
												<a href="javascript:void(0)" data-toggle='modal' data-target='#dispatchPermsn' onclick="showDispatchPermsnDiv(1, '<%=user.getLoginName() %>')" class="orange ml6">开通到站权限</a>
									<%
											}
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
				<button type="button" class="close" onclick="closeUpdDiv()" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title userclass tc"></h4>
			</div>

			<div class="modal-body b-modal-body">
				<form role="form" action="" method="post" id="userForm" class="form-inline-n">
					<ul class="b-n-crt">
						<li>
							<select id="roleId" name="roleId" class="form-control form-bod" onchange="showPass();">
								<c:if test="${userNow.role==UserRole.COMPANY}">
									<option value ="-1" selected ="selected">全部</option>
									<option value ="<%=UserRole.SITEMASTER%>"><%=UserRole.SITEMASTER.getMessage()%></option>
									<option value ="<%=UserRole.SENDMEM%>"><%=UserRole.SENDMEM.getMessage()%></option>
								</c:if>
								<c:if test="${userNow.role==UserRole.SITEMASTER}">
									<option value ="<%=UserRole.SENDMEM%>" selected ="selected"><%=UserRole.SENDMEM.getMessage()%></option>
								</c:if>
							</select>
							<p class="help-block" id="roleIdP" style="display:none;">请选中一个角色</p>
						</li>
						<li>
							<input type="text" id="realName" name="realName" class="form-control form-bod" placeholder="真实姓名" />
							<p class="help-block" id="realNameP" style="display:none;">请输入姓名</p>
						</li>
						<li>
							<input type="text" id="loginName" name="loginName"  class="form-control form-bod" placeholder="手机号" />
						</li>
						<%--<c:if test="${userNow.role==UserRole.COMPANY}">--%>
							<li id="passLi" >
								<input type="password" id="loginPass" name="loginPass" class="form-control form-bod j-nf-pwd" placeholder="密码" />
							</li>
							<li id="passCLi" >
								<input type="password" id="passwordC" name="passwordC" class="form-control form-bod j-cf-pwd" placeholder="确认密码" />
							</li>
						<%--</c:if>--%>
					</ul>
					<div class="row mt20">
						<span class="col-md-12"><a href="javascript:void(0)" id="saveuserid" onclick="saveUserBtn()" class="sbtn sbtn2 l">保存</a></span>
					</div>

					<input type="hidden" class="form-control" id="userId" name="userId" value="">
					<input type="hidden" class="form-control" id="oldLoginName" name="oldLoginName">

				</form>
			</div>

		</div>
	</div>
</div>
<!--E 新建-->
<!--S 修改用户状态-->
<div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="changeModalLabel" id="changeModal"
	 aria-hidden="true">
	<div class="modal-dialog b-modal-dialog middleS" role="document">
		<div class="modal-content">
			<div class="modal-header b-modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close" id="closeButton"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title tc">确认</h4>
			</div>
			<div class="modal-body b-modal-body">
				<input type="hidden" id="loginNameForChange" name="loginNameForChange"/>
				<input type="hidden" id="statusForChange" name="statusForChange"/>
				<%--<input type="hidden" id="idForChange" name="idForChange"/>--%>
				<em class="f16" id="messageForConfirm">确认删除？删除站点将会将该站点下的所有用户删除？</em>
				<div class="clearfix mt20">
					<a href="javascript:void(0);" id="conFirmForChangeBtn" class="sbtn sbtn2 l col-md-12">确认</a>
				</div>
			</div>
		</div>
	</div>
</div>
<!--E 修改用户状态-->
<!--S 到站权限-->
<div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="dispatchPermsnLabel" id="dispatchPermsn"
	 aria-hidden="true">
	<div class="modal-dialog b-modal-dialog middleS" role="document">
		<div class="modal-content">
			<div class="modal-header b-modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
				<h4 class="modal-title tc">确认</h4>
			</div>
			<div class="modal-body b-modal-body">
				<em class="f16" id="dispatchMsg">开通后派件员可在App中使⽤“包裹到站、
					运单分派”功能，确认开通吗？</em>
				<div class="clearfix mt20">
					<a href="javascript:void(0);" onclick="updateDispatchPermsn()" class="sbtn sbtn2 l col-md-12">确认</a>
				</div>
			</div>
		</div>
	</div>
</div>
<!--E 到站权限-->
<!-- E pop -->

<!-- S 省市区站点选择控件 -->
<script type="text/javascript">
	var  siteUrl = "<c:url value="/site/getSiteList"/>";
</script>
<script src="<c:url value="/resources/javascripts/siteControl.js" />"> </script>
<!-- E 省市区站点选择控件  -->

<script type="text/javascript">
	//是否需要校验手机号，默认需要；但是当失去焦点点击关闭按钮时，不需要校验手机号
	var isCheckPhone = true;

	//显示分页条
	var pageStr = paginNav(<%=userPage.getPageNo()%>, <%=userPage.getTotalPages()%>, <%=userPage.getTotalCount()%>);
	$("#pagin").html(pageStr);


	//公司用户角色不可以修改
	<c:if test="${userNow.role==UserRole.COMPANY}">
	$("#roleId").attr("disabled","disabled");
	</c:if>

	//加载带有查询条件的指定页的数据
	function gotoPage(pageIndex) {
		var roleId = $("#saasrole").val();
		var status = $("#status").val();
		var keyword = $("#keyword").val();
		var sites = $("#sites").val();
		var url = "<c:url value="/userManage/getUserPageFenYe" />";
		$.ajax({
			type : "GET",  //提交方式
			url : url,//路径
			data : {
				"pageIndex" : pageIndex,
				"roleId" : roleId,
				"status" : status,
				"siteId" : sites,
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
				ioutDiv("加载分页数据异常！");
			}
		});
	}


	//封装一行的数据
	function getRowHtml(data){
		var row = "<tr>";
		var temp = data.idStr;
		<c:if test="${userNow.role==UserRole.COMPANY}">
		if(data.site!=null){

			row +=  "<td>" + data.site.name + "</td>";
		}else{
			row +=  "<td></td>";
		}
		</c:if>
		row +=  "<td>" + data.roleMessage + "</td>";
		row += "<td>" + data.realName + "</td>";
		row += "<td>" + data.loginName + "</td>";
		if(data.userStatus != null){
			row += "<td>" + data.statusMessage + "</td>";
		}else{
			row += "<td>无</td>";
		}
		row += "<td><button id='editUser' class='orange' name='editUser' data-toggle='modal' data-target='#myModal' href='javascript:void(0)' onclick=\"searchUser('"+temp+"','"+data.loginName+"')\">修改</button>";
		if(data.userStatus=="<%=UserStatus.VALID%>"){
			row += "<a href='javascript:void(0)' data-toggle='modal' data-target='#changeModal' onclick=\"changeStatus(3,'"+temp+"','"+data.loginName+"')\" class=\"orange ml6\">停用</a>";
		}else{
			row += "<a href='javascript:void(0)' data-toggle='modal' data-target='#changeModal' onclick=\"changeStatus(1,'"+temp+"','"+data.loginName+"')\" class=\"orange ml6\">启用</a>";
		}
		//到站权限
		if(data.role == "<%=UserRole.SENDMEM%>"){//派件员才会显示
			if(data.dispatchPermsn == 1){
				row += "<a href='javascript:void(0)' data-toggle='modal' data-target='#dispatchPermsn' onclick=\"showDispatchPermsnDiv(0, '"+data.loginName+"')\" class=\"orange ml6\">关闭到站权限</a>";
			}else{
				row += "<a href='javascript:void(0)' data-toggle='modal' data-target='#dispatchPermsn' onclick=\"showDispatchPermsnDiv(1, '"+data.loginName+"')\" class=\"orange ml6\">开通到站权限</a>";
			}
		}
		row += "</tr>";
		return row;
	}

	function checkLoginName(loginName) {
		setTimeout(function(){
			if(isCheckPhone){
				loginName=loginName.replace(/\ +/g,"");
				var oldLoginName = document.getElementById("oldLoginName").value;
				var url = "<c:url value="/userManage/checkLognName" />";
				var userId = $("#userId").val();
				if(loginName!=''){
					$.ajax({
						url: url+"?loginName=" + loginName + "&userId=" + userId,
						type: 'GET',
						cache: false,
						dataType: "text",
						async: false,
						data: {},
						success: function(response){
							if(response=="true"){
								ioutDiv("手机号已存在!")
							}
						},
						error: function(){
							ioutDiv('服务器繁忙，请稍后再试！');
						}
					});
				}
				isCheckPhone = true;
			}
		},500);
	}

	function closeUpdDiv(){
		isCheckPhone = false;
	}

	function changeStatus(status,id,loginName){
//		$("#idForChange").val(id);
		$("#statusForChange").val(status);
		$("#loginNameForChange").val(loginName);
		if(status==3){
			//表示要停用
			$("#messageForConfirm").html('停用后将无法使用棒棒达客户端，确认停用吗？');
		}else if(status==1){
			//表示要启用
			$("#messageForConfirm").html('启用后将可以使用棒棒达客户端，确认启用吗？');
		}

	}
	$("#conFirmForChangeBtn").click(function(){
		$("#closeButton").click();
//		var id = $("#idForChange").val();
		var status = $("#statusForChange").val();
		var loginName=$("#loginNameForChange").val();
		$.ajax({
			type : "GET",
			url : '<c:url value="/userManage/changestatus" />',
			data : {
				//"id" : id,
				"status" : status,
				"loginName" : loginName
			},
			success : function(data) {
				if(data == 'true'){
					gotoPage(0);
				} else{
					alert_mine("错误","站点状态无效,请核实");
				}
			},
			error : function() {
				alert_mine("错误","请重新登录");
			}
		});
	});

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
						ioutDiv("删除成功");
						gotoPage(0);
					}
				},
				error : function() {
					ioutDiv("异常！");
				}
			});
		}
	}

	function saveUserBtn(){
		var realName = $("#realName").val();
		realName=realName.replace(/\ +/g,"");
		if(realName==""){
			ioutDiv("请输入姓名");
			return false;
		}
		var loginName = $("#loginName").val();
		loginName=loginName.replace(/\ +/g,"");
		if(loginName==""){
			ioutDiv("请输入手机号");
			return false;
		}
		if(checkMobile(loginName)==false){
			ioutDiv("请输入正确的手机号");
			return false;
		}
		//校验密码 == 公司账号
		<c:if test="${userNow.role==UserRole.COMPANY}">
			var roleId = $("#roleId").val();
			if(roleId=="<%=UserRole.SITEMASTER%>"){
				var b = checkPwd();
				if(!b){
					return false;
				}
			}
		</c:if>
		//校验密码 == 站长账号
		if(haveDispatchPermsn){//有到站权限的需要校验密码
			var b = checkPwd();
			if(!b){
				return false;
			}
		}
		var url = "";
		var userId = $("#userId").val();
		if(userId == ""){//新建
			url = '<c:url value="/userManage/saveUser?${_csrf.parameterName}=${_csrf.token}" />';
		}else{//修改
			url = '<c:url value="/userManage/editUser?${_csrf.parameterName}=${_csrf.token}" />';

		}
		checkAndSave(url, loginName, userId);
	}
	function checkPwd(){
		var password = $.trim($('input[name="loginPass"]').val());
		var passwordC = $.trim($('input[name="passwordC"]').val());
		if(password==""){
			ioutDiv("请输入密码");
			return false;
		}else if(!pwdreg.test(password)){
			ioutDiv("请输入6-12位数字和字母结合的密码");
			return false;
		}else if(passwordC==""){
			ioutDiv("请确认密码");
			return false;
		}else if(passwordC!=password){
			ioutDiv("两次密码不一致");
			return false;
		}else{
			return true;
		}
	}
	//检查手机号是否被注册，未被注册，则可以添加或者修改。
	function checkAndSave(url, loginName, userId){
		$.ajax({
			url: "<c:url value="/userManage/checkLognName" />"+"?loginName=" + loginName + "&userId=" + userId,
			type: 'GET',
			cache: false,
			dataType: "text",
			async: false,
			data: {},
			success: function(response){
				if(response=="true"){
					ioutDiv("手机号已存在!")
				}else{//保存用户
					saveOrUpdateUser(url);
				}
			},
			error: function(){
				ioutDiv('服务器繁忙，请稍后再试！');
			}
		});
	}


	//保存或者修改用户，url区别是保存还是修改
	function saveOrUpdateUser(url){
		//公司用户移除角色不可以修改属性
		<c:if test="${userNow.role==UserRole.COMPANY}">
		$("#roleId").removeAttr("disabled");
		</c:if>
		$("#userForm").ajaxSubmit({
			type: 'post',
			url: url ,
			success: function(data){
				if(data.success){
					$(".j-user-pop").modal("hide");
					gotoPage(0);
				}else{
					ioutDiv(data.msg);
				}
			},
			error: function(){
				ioutDiv( "服务器异常!");
			}
		});
	}

	function showPass() {
		<c:if test="${userNow.role==UserRole.COMPANY}">
		if($("#roleId").val()=="<%=UserRole.SITEMASTER%>"){
			$("#passLi").attr("style","");
			$("#passCLi").attr("style","");
		}else{
			$("#passLi").attr("style","display:none;");
			$("#passCLi").attr("style","display:none;");
		}
		</c:if>
	}
	var haveDispatchPermsn = false;
	function searchUser(id,loginName){
		$('.userclass').html('修改');
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
					$("#userId").val(id);
					$("#realName").val(data.realName);
					$("#loginName").val(data.loginName);
					$("#roleId").val(data.role);
					if(data.role == "<%=UserRole.SITEMASTER%>"
						||(data.role == "<%=UserRole.SENDMEM%>" && data.dispatchPermsn == 1)){
						$("#passLi").attr("style","");
						$("#passCLi").attr("style","");
						$("#loginPass").val(data.passWord);
						$("#passwordC").val(data.passWord);
						haveDispatchPermsn = true;
					}else{
						$("#passLi").attr("style","display:none;");
						$("#passCLi").attr("style","display:none;");
						haveDispatchPermsn = false;
					}
					document.getElementById("oldLoginName").value=data.loginName;
				}else{
					haveDispatchPermsn = false;
				}
			},
			error : function() {
				ioutDiv("异常！");
				haveDispatchPermsn = false;
			}
		});
	}

	function showAddUserDiv(){
		$('.userclass').html('新建');
		haveDispatchPermsn=false;
		document.getElementById("userForm").reset();
		$("#oldLoginName").val("");
		$("#userId").val("");
		<c:if test="${userNow.role == UserRole.SITEMASTER}">
			$("#passLi").attr("style","display:none;");
			$("#passCLi").attr("style","display:none;");
		</c:if>
	}

	/************************************ S 到站权限 *******************************/
	//修改到站权限弹出确认框的提示信息 0: 关闭；1：开通
	var loginName = null;
	var dispatchPermsn = null;
	function showDispatchPermsnDiv(dispthPms, lgnName){
		if(dispthPms == 0){//表示要停用
			$("#dispatchMsg").html("关闭后派件员在App中将无法使用\"包裹到站\"功能，确认关闭吗？");
		}else if(dispthPms==1){//表示要启用
			$("#dispatchMsg").html("开通后派件员可在App中使用\"包裹到站\"功能，确认开通吗？");
		}
		loginName = lgnName;
		dispatchPermsn = dispthPms;
	}
	//修改到站权限弹出确认框的提示信息 0: 关闭；1：开通
	function updateDispatchPermsn(){
		$("#dispatchPermsn").modal("hide");
		$.ajax({
			type : "POST",
			url : '<c:url value="/userManage/updateDispatchPermsn?${_csrf.parameterName}=${_csrf.token}" />',
			data : {
				"loginName" : loginName,
				"dispatchPermsn" : dispatchPermsn
			},
			success : function(data) {
				if(data.success){
					gotoPage(0);
				} else{
					alert_mine("提示", data.msg);
				}
			},
			error : function() {
				alert_mine("错误","请重新登录");
			}
		});
	}
	/************************************ E 到站权限 *******************************/

</script>
</body>
</html>