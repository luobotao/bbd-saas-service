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
	<script type="text/javascript" src="/js/plugins/dialog/dialog.js"></script>	
</head>
<body>
<div>
	<%--<jsp:include page="../top.jsp" flush="true" /> --%>	
	<jsp:include page="../main.jsp" flush="true" />
</div>
<!-- 添加员工 -->
	<div id="add_user">
	    <div class="pop_style add_staff">
	        <div class="title">添加用户</div>
	        <div class="close"></div>
	        <div class="form">
	        	<form id="addForm">
	            <div class="item">
	                <label>账号：</label>
	                <input type="hidden" id="merchantId" name="merchantId" value="${merchantId}">
	                <input class="ipt" type="text" name="account" id="addStaffAccout" />
	                <span id="account"></span>
	            </div>
	            <div class="item">
	                <label>密码：</label>
	                <input class="ipt" type="password" name="password" placeholder="默认为123456">
	            </div>
	            <div class="item">
	                <label>姓名：</label>
	                <input class="ipt" type="text" name="name">
	            </div>
	            <div class="item">
	                <label>角色：</label>
	                <p style="width: 380px; font: 12px/30px &quot;Microsoft Yahei&quot;" id="aRoleItem"></p>
	            </div>
	            <div class="btn">
	                <button class="submit submit_save" type="button" data-value="save" onclick="add()">保 存</button>
	            </div>
	            </form>
	        </div>
	        <div class="msg">
	            <h3>温馨提示</h3>
	            <p>1、店长：全面负责药房的管理，拥有接收订单、应答咨询、配送、验证优惠券、客户沟通等全部权限。</p>
	            <p>2、营业员：拥有接单、验证优惠券、客户沟通的权限。</p>
	            <p>3、药剂师：负责应答咨询，在店员端APP上回复顾客的问题。同时拥有营业员全部权限</p>
	            <p>4、配送员：负责接收订单、配送药品。</p>
	        </div>
	    </div>
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
			<button id="newUser" name="newUser"  onClick="addUserDiv()">新建</button>
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

<script type="text/javascript" src="<c:url value="/resources/jquery/jquery-1.12.3.min.js" />"></script>

<script type="text/javascript">
var XSQdialog;
$(function() {

	XSQdialog = {
    mask: $(".mask"),
    main: $(".ui_dialog"),
    colse:$(".colse"),
    cntbox: $("#wrapBox"),
    show: function(html) {
        this.cntbox.html(html);
        this.cntbox.find('.pop_style').show();
        this.mask.fadeIn(300);
        this.main.css({'margin-top': - (this.main.outerHeight(true)) / 2 + 'px', 'margin-left': - (this.main.outerWidth(true)) / 2 + 'px'}).fadeIn(300);
    },
    hide: function() {
        this.cntbox.html('');
        this.mask.fadeOut(200);
        this.main.fadeOut(200).removeAttr('style');
    }
    
	};
	$('#wrapBox').on("click", '.close', function(){
	    MPHdialog.hide();
	});
	$('#wrapBox').on("click", 'button.cancel', function(){
	    MPHdialog.hide();
	});
});
//显示用户编辑面板
function showUserDiv(userId) {
	$("#user_div").show();
}
//添加用户编辑面板
function addUserDiv(userId) {
	//$("#user_div").show();
	var html = $('#add_user').html();
	MPHdialog.show(html);
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