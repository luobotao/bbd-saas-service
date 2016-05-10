<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page import="com.bbd.saas.enums.UserStatus" %>
<%@ page import="com.bbd.saas.mongoModels.Site" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.SiteStatus" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
    <title>系统设置-站点管理</title>
    <jsp:include page="../main.jsp" flush="true"/>
</head>
<body class="fbg">
<%
    PageModel<Site> sitePage = (PageModel<Site>) request.getAttribute("sitePage");

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

                            <div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
                                <label>状态：</label>
                                <select id="status" name="status" class="form-control form-con-new">
                                    <%=SiteStatus.Stas2HTML(-1)%>
                                </select>
                            </div>
                            <div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
                                <label>关键字：</label>
                                <input type="text" id="keyword" name="keyword" placeholder="真实姓名/手机号"
                                       class="form-control"/>
                            </div>

                        </div>
                        <div class="row pb20">
                            <div class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
                                <a href="javascript:void(0)" onclick="toSearch();" class="ser-btn l"><i
                                        class="b-icon p-query p-ser"></i>查询</a>
                                <a href="javascript:void(0)" onclick="restUserModel();" class="ser-btn d ml6"><i
                                        class="num-add mr10">＋</i>新建</a>

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
                                <th>站点名称</th>
                                <th>区域码</th>
                                <th>站点地址</th>
                                <th>站点姓名</th>
                                <th>手机号</th>
                                <th>邮箱</th>
                                <th>状态</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody id="dataList">

                            <%
                                for (Site site : sitePage.getDatas()) {
                            %>
                            <tr>
                                <td><%=site.getName()%>
                                </td>
                                <td><%=site.getAreaCode()%>
                                </td>
                                <td><%=site.getProvince()%>-<%=site.getCity()%>
                                    -<%=site.getArea()%> <%=site.getAddress()%>
                                </td>
                                <td><%=site.getResponser()%>
                                </td>
                                <td><%=site.getUsername()%>
                                </td>
                                <td><%=site.getEmail()%>
                                </td>
                                <td><%=site.getStatus().getMessage()%>
                                </td>

                                <td>
                                    <%
                                        if (SiteStatus.WAIT == site.getStatus()) {
                                    %>
                                    <a href="javascript:void(0);" data-toggle='modal' data-target='#validModal'
                                       onclick="setSiteId('<%=site.getId() %>')" class="orange">通过</a>
                                    <a href="javascript:void(0);" data-toggle='modal' data-target='#turnDownModal'
                                       onclick="setSiteId('<%=site.getId() %>')" class="orange">驳回</a>
                                    <%
                                        }
                                        if (SiteStatus.TURNDOWN == site.getStatus()) {
                                    %>
                                    <a href="javascript:void(0);"
                                       onclick="searchUser('<%=site.getId() %>','')" class="orange ">查看驳回原因</a>
                                    <%
                                        }
                                        if (SiteStatus.APPROVE == site.getStatus()) {
                                    %>
                                    <a href="javascript:void(0);" onclick="searchUser('<%=site.getId() %>','')"
                                       class="orange">修改</a>
                                    <a href="javascript:void(0);" data-toggle='modal' data-target='#stopModal'
                                       onclick="setSiteId('<%=site.getId() %>')" class="orange">停用</a>
                                    <%
                                        }
                                        if (SiteStatus.INVALID == site.getStatus()) {
                                    %>
                                    <a href="javascript:void(0);" onclick="searchUser('<%=site.getId() %>','')"
                                       class="orange">修改</a>
                                    <a href="javascript:void(0);" data-toggle='modal' data-target='#startModal'
                                       onclick="setSiteId('<%=site.getId() %>')" class="orange">启用</a>
                                    <a href="javascript:void(0);" data-toggle='modal' data-target='#delModal'
                                       onclick="setSiteId('<%=site.getId() %>')" class="orange ml6">删除</a>
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
    <em class="b-copy">京ICP备 465789765 号 版权所有 &copy; 2016-2020 棒棒达 北京棒棒达科技有限公司</em>
</footer>
<!-- E footer -->
<!-- S pop -->
<!--S 新建-->
<div class="j-user-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" id="myModal"
     aria-hidden="true">
    <div class="modal-dialog b-modal-dialog">
        <div class="modal-content">
            <div class="modal-header b-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">×</span></button>
                <h4 class="modal-title userclass tc"></h4>
            </div>
            <form role="form" action="" method="post" id="userForm" class="form-inline-n">
                <div class="modal-body b-modal-body">
                    <ul class="b-n-crt">
                        <li>
                            <select id="roleId" name="roleId" class="form-control form-bod">
                                <option value="1" selected="selected">派件员</option>
                            </select>
                            <p class="help-block" id="roleIdP" style="display:none;">请选中一个角色</p>
                        </li>
                        <li>
                            <input type="text" id="realName" name="realName" class="form-control form-bod"
                                   placeholder="真实姓名"/>
                            <p class="help-block" id="realNameP" style="display:none;">请输入姓名</p>
                        </li>
                        <!--
                        <li>
                            <input type="text" id="phone" name="phone" class="form-control form-bod" placeholder="手机号" />
                            <p class="help-block" id="phoneP" style="display:none;">请正确输入11位手机号</p>
                        </li>
                        -->
                        <li>
                            <input type="text" id="loginName" name="loginName" onblur="checkLoginName(this.value)"
                                   class="form-control form-bod" placeholder="手机号"/>
                            <p class="help-block" id="loginNameP" style="display:none;">请正确输入11位手机号</p>
                        </li>
                    </ul>


                    <div class="row mt20">
                        <span class="col-md-12"><a href="javascript:void(0)" id="saveuserid" onclick="saveUserBtn()"
                                                   class="sbtn sbtn2 l">保存</a></span>
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

<!--全局的siteId-->
<input type="hidden" id="siteIdForModal">
<!--S 通过-->
<div class="j-user-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="validModalLabel" id="validModal"
     aria-hidden="true">
    <div class="modal-dialog b-modal-dialog">
        <div class="modal-content">
            <div class="modal-header b-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">×</span></button>
                <h4 class="modal-title userclass tc"></h4>
            </div>
            <div class="modal-body b-modal-body">
                <ul class="b-n-crt">
                    <li>
                        确认通过？
                    </li>
                </ul>
                <div class="row mt20">
                    <span class="col-md-12"><a href="javascript:void(0)" id="conFirmForValidBtn" class="sbtn sbtn2 l">保存</a></span>
                </div>
            </div>
        </div>
    </div>
</div>
<!--E 通过-->
<!--S 驳回-->
<div class="j-user-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="turnDownModalLabel" id="turnDownModal"
     aria-hidden="true">
    <div class="modal-dialog b-modal-dialog">
        <div class="modal-content">
            <div class="modal-header b-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">×</span></button>
                <h4 class="modal-title userclass tc"></h4>
            </div>
            <div class="modal-body b-modal-body">
                <ul class="b-n-crt">
                    <li>
                        确认驳回？
                    </li>
                    <input type="text" id="message" name="message" class="form-control form-bod" placeholder="驳回原因"/>
                </ul>
                <div class="row mt20">
                    <span class="col-md-12"><a href="javascript:void(0)" id="conFirmForTurnDownBtn" class="sbtn sbtn2 l">保存</a></span>
                </div>
            </div>
        </div>
    </div>
</div>
<!--E 驳回-->
<!--S 停用-->
<div class="j-user-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="stopModalLabel" id="stopModal"
     aria-hidden="true">
    <div class="modal-dialog b-modal-dialog">
        <div class="modal-content">
            <div class="modal-header b-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">×</span></button>
                <h4 class="modal-title userclass tc"></h4>
            </div>
            <div class="modal-body b-modal-body">
                <ul class="b-n-crt">
                    <li>
                        确认停用？
                    </li>
                </ul>
                <div class="row mt20">
                    <span class="col-md-12"><a href="javascript:void(0)" id="conFirmForStopBtn" class="sbtn sbtn2 l">保存</a></span>
                </div>
            </div>
        </div>
    </div>
</div>
<!--E 停用-->
<!--S 启用-->
<div class="j-user-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="startModalLabel" id="startModal"
     aria-hidden="true">
    <div class="modal-dialog b-modal-dialog">
        <div class="modal-content">
            <div class="modal-header b-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">×</span></button>
                <h4 class="modal-title userclass tc"></h4>
            </div>
            <div class="modal-body b-modal-body">
                <ul class="b-n-crt">
                    <li>
                        确认启用？
                    </li>
                </ul>
                <div class="row mt20">
                    <span class="col-md-12"><a href="javascript:void(0)" id="conFirmForSartBtn" class="sbtn sbtn2 l">保存</a></span>
                </div>
            </div>
        </div>
    </div>
</div>
<!--E 启用-->
<!--S 删除-->
<div class="j-user-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="delModalLabel" id="delModal"
     aria-hidden="true">
    <div class="modal-dialog b-modal-dialog">
        <div class="modal-content">
            <div class="modal-header b-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">×</span></button>
                <h4 class="modal-title userclass tc"></h4>
            </div>
            <div class="modal-body b-modal-body">
                <ul class="b-n-crt">
                    <li>
                        确认删除？删除站点将会将该站点下的所有用户删除？
                    </li>
                </ul>
                <div class="row mt20">
                    <span class="col-md-12"><a href="javascript:void(0)" id="conFirmForDelBtn" class="sbtn sbtn2 l">保存</a></span>
                </div>
            </div>
        </div>
    </div>
</div>
<!--E 删除-->
<!-- E pop -->
<script type="text/javascript">
    //显示分页条
    var pageStr = paginNav(<%=sitePage.getPageNo()%>, <%=sitePage.getTotalPages()%>, <%=sitePage.getTotalCount()%>);
    $("#pagin").html(pageStr);


    //加载带有查询条件的指定页的数据
    function gotoPage(pageIndex, status, keyword) {
        var url = "<c:url value="/system/siteManage/getSitePage" />";
        $.ajax({
            type: "GET",  //提交方式
            url: url,//路径
            dataType: "json",
            data: {
                "pageIndex": pageIndex,
                "status": status,
                "keyword": keyword
            },//数据，这里使用的是Json格式进行传输
            success: function (dataObject) {//返回数据根据结果进行相应的处理
                var tbody = $("#dataList");
                // 清空表格数据
                tbody.html("");

                var dataList = dataObject.datas;
                if (dataList != null) {
                    var datastr = "";
                    for (var i = 0; i < dataList.length; i++) {
                        datastr += getRowHtml(dataList[i]);
                    }
                    tbody.html(datastr);
                }
                //更新分页条
                var pageStr = paginNav(pageIndex, dataObject.totalPages, dataObject.totalCount);
                $("#pagin").html(pageStr);
            },
            error: function () {

            }
        });
    }
    //封装一行的数据
    function getRowHtml(data) {
        var row = "<tr>";
        var temp = "";
        row += "<td>" + data.name + "</td>";
        row += "<td>" + data.areaCode + "</td>";
        row += "<td>" + data.province +"-"+ data.city +"-"+ data.area +"-"+ data.address + "</td>";
        row += "<td>" + data.responser + "</td>";
        row += "<td>" + data.username + "</td>";
        row += "<td>" + data.email + "</td>";
        row += "<td>" + data.statusMessage + "</td>";
        row += "<td>";
        if(data.status=="<%=SiteStatus.WAIT%>" ){
            row += "<a href='javascript:void(0);' data-toggle='modal' data-target='#validModal' onclick=\"setSiteId('"+data.id+"')\" class='orange'>通过</a>";
            row += "<a href='javascript:void(0);' data-toggle='modal' data-target='#turnDownModal' onclick=\"setSiteId('"+data.id+"')\" class='orange'>驳回</a>";
        }
        if(data.status=="<%=SiteStatus.TURNDOWN%>" ){
            row += "<a href='javascript:void(0);' data-toggle='modal' data-target='#validModal' onclick=\"setSiteId('"+data.id+"')\" class='orange'>查看驳回原因</a>";
        }
        if(data.status=="<%=SiteStatus.APPROVE%>" ){
            row += "<a href='javascript:void(0);' data-toggle='modal' data-target='#validModal' onclick=\"setSiteId('"+data.id+"')\" class='orange'>修改</a>";
            row += "<a href='javascript:void(0);' data-toggle='modal' data-target='#stopModal' onclick=\"setSiteId('"+data.id+"')\" class='orange'>停用</a>";
        }
        if(data.status=="<%=SiteStatus.INVALID%>" ){
            row += "<a href='javascript:void(0);' data-toggle='modal' data-target='#validModal' onclick=\"setSiteId('"+data.id+"')\" class='orange'>修改</a>";
            row += "<a href='javascript:void(0);' data-toggle='modal' data-target='#startModal' onclick=\"setSiteId('"+data.id+"')\" class='orange'>启用</a>";
            row += "<a href='javascript:void(0);' data-toggle='modal' data-target='#delModal' onclick=\"setSiteId('"+data.id+"')\" class='orange'>删除</a>";
        }
        row += "</td></tr>";
        return row;
    }

    function toSearch() {
        var status = $("#status").val();
        var keyword = $("#keyword").val();
        gotoPage(0, status, keyword);
    }

    function checkLoginName(loginName) {
        loginName = loginName.replace(/\ +/g, "");
        var operate = document.getElementById("operate").value;
        var oldloginName = document.getElementById("loginNameTemp").value;
        var newloginName = loginName;
        var url = '<c:url value="/userManage/checkLognName" />';
        var ret = false;
        if (loginName != '') {

            if (operate == 'create') {

                $.ajax({
                    url: url + '?loginName=' + loginName,
                    type: 'GET',
                    cache: false,
                    dataType: "text",
                    async: false,
                    data: {},
                    success: function (response) {
                        console.log(response);
                        if (response == "true") {
                            $("#loginNameP").text("手机号已存在，请重新输入11位手机号!");
                            $("#loginNameP").attr("style", "color:red");
                            //document.getElementById("flaglogin").value='false';
                            //return true;
                            ret = true;
                        } else {
                            //document.getElementById("flaglogin").value='true';
                            $("#loginNameP").attr("style", "display:none");
                            //return false;
                            ret = false;
                        }
                    },
                    error: function () {
                        //alert('服务器繁忙，请稍后再试！');
                        //return true;
                        ret = true;
                        if (window.top == window.self) {//不存在父页面
                            window.location.href = "<c:url value="/login" />"
                        } else {
                            window.top.location.href = "<c:url value="/login" />"
                        }
                    }
                });
            }

        }
        return ret;
    }


    function checkStaffid(staffid) {
        staffid = staffid.replace(/\ +/g, "");
        var operate = document.getElementById("operate").value;
        var oldstaffid = document.getElementById("staffidTemp").value;
        var newstaffid = staffid;
        var url = '<c:url value="/userManage/checkStaffIdBySiteByStaffid" />';
        var ret = false;
        if (staffid !== '') {

            if (operate == 'create') {

                $.ajax({
                    url: url + '?staffid=' + staffid,
                    type: 'GET',
                    cache: false,
                    dataType: "text",
                    async: false,
                    data: {},
                    success: function (response) {
                        console.log(response);
                        if (response == "true") {
                            //alert("您输入的登录名目前已存在，请重新输入");
                            $("#staffidP").text("该站点下的工号已存在，请重新输入!");
                            $("#staffidP").attr("style", "color:red");
                            //document.getElementById("flagstaffid").value='false';
                            //return true;
                            ret = true;
                        } else {
                            //document.getElementById("flagstaffid").value='true';
                            $("#staffidP").attr("style", "display:none");
                            //return false;
                            ret = false;
                        }
                    },
                    error: function () {
                        //alert('服务器繁忙，请稍后再试！');
                        //return true;
                        ret = true;
                        if (window.top == window.self) {//不存在父页面
                            window.location.href = "<c:url value="/login" />"
                        } else {
                            window.top.location.href = "<c:url value="/login" />"
                        }
                    }
                });
            } else {
                if (newstaffid !== oldstaffid) {
                    $.ajax({
                        url: url + '?staffid=' + staffid,
                        type: 'GET',
                        cache: false,
                        dataType: "text",
                        async: false,
                        data: {},
                        success: function (response) {
                            console.log(response);
                            if (response == "true") {
                                //alert("您输入的登录名目前已存在，请重新输入");
                                $("#staffidP").text("该站点下的工号已存在，请重新输入!");
                                $("#staffidP").attr("style", "color:red");
                                //document.getElementById("flagstaffid").value='false';
                                //return true;
                                ret = true;
                            } else {
                                //document.getElementById("flagstaffid").value='true';
                                $("#staffidP").attr("style", "display:none");
                                //return false;
                                ret = false;
                            }
                        },
                        error: function () {
                            alert('服务器繁忙，请稍后再试！');
                            //return true;
                            ret = true;
                        }
                    });
                } else {
                    //document.getElementById("flagstaffid").value='true';
                    $("#staffidP").attr("style", "display:none");
                    //return false;
                    ret = false;
                }
            }

        }
        return ret;
    }


    function changeStatus(status, id, loginName) {

        var ret = false;
        if (status == 3) {
            //表示要停用
            if (confirm('停用后小件员将无法使用棒棒达客户端，确认停用吗？')) {
                ret = true;
            }
        } else if (status == 1) {
            //表示要启用
            if (confirm('启用后小件员可以使用棒棒达客户端，确认启用吗？')) {
                ret = true;
            }
        }

        if (ret) {
            $.ajax({
                type: "GET",
                url: '<c:url value="/userManage/changestatus" />',
                data: {
                    "id": id, "status": status, "loginName": loginName
                },
                success: function (data) {
                    if (data == 'true') {
                        gotoPage(0);
                    }
                },
                error: function () {
                    //alert("异常！");
                    if (window.top == window.self) {//不存在父页面
                        window.location.href = "<c:url value="/login" />"
                    } else {
                        window.top.location.href = "<c:url value="/login" />"
                    }
                }
            });
        }
    }

    function delSite(siteId) {
        var ret = false;
        if (confirm('确定要执行此操作吗?')) {
            ret = true;
        }
        if (ret) {

        }
    }
    $("#conFirmForValidBtn").click(function(){
        $.ajax({
            type: "GET",
            url: '<c:url value="/system/siteManage/validSite" />',
            dataType: "text",
            data: {
                "siteId": $("#siteIdForModal").val()
            },
            success: function (data) {
                if (data == 'true') {
                    location.reload();
                }
            },
            error: function () {
                alert("异常！");
            }
        });
    });
    $("#conFirmForTurnDownBtn").click(function(){
        $.ajax({
            type: "GET",
            url: '<c:url value="/system/siteManage/turnDownSite" />',
            dataType: "text",
            data: {
                "siteId": $("#siteIdForModal").val(),
                "message": $("#message").val()
            },
            success: function (data) {
                if (data == 'true') {
                    location.reload();
                }
            },
            error: function () {
                alert("异常！");
            }
        });
    });
    $("#conFirmForStopBtn").click(function(){
        $.ajax({
            type: "GET",
            url: '<c:url value="/system/siteManage/stopSite" />',
            dataType: "text",
            data: {
                "siteId": $("#siteIdForModal").val()
            },
            success: function (data) {
                if (data == 'true') {
                    location.reload();
                }
            },
            error: function () {
                alert("异常！");
            }
        });
    });
    $("#conFirmForSartBtn").click(function(){
        $.ajax({
            type: "GET",
            url: '<c:url value="/system/siteManage/startSite" />',
            dataType: "text",
            data: {
                "siteId": $("#siteIdForModal").val()
            },
            success: function (data) {
                if (data == 'true') {
                    location.reload();
                }
            },
            error: function () {
                alert("异常！");
            }
        });
    });
    $("#conFirmForDelBtn").click(function(){
        $.ajax({
            type: "GET",
            url: '<c:url value="/system/siteManage/delSite" />',
            dataType: "text",
            data: {
                "siteId": $("#siteIdForModal").val()
            },
            success: function (data) {
                if (data == 'true') {
                    alert("删除成功");
                    location.reload();
                }
            },
            error: function () {
                alert("异常！");
            }
        });
    });

    //赋值
    function setSiteId(siteId){
        $('#siteIdForModal').val(siteId);
    }


    function saveUserBtn() {

        var url = "";
        //operate这个隐藏域就是为了区别这个操作时修改还是新建
        var getSign = document.getElementById("operate").value;
        var flag = true;
        var checkSign = false;
        var loginNameSign = false;
        var ataffidSign = false;
        var returnmess = "";
        if (getSign == 'edit') {
            url = '<c:url value="/userManage/editUser?${_csrf.parameterName}=${_csrf.token}" />';
        } else {
            url = '<c:url value="/userManage/saveUser?${_csrf.parameterName}=${_csrf.token}" />';
        }
        //var roleId = $("#roleId").val();
        var realName = $("#realName").val();
        realName = realName.replace(/\ +/g, "");
        var loginName = $("#loginName").val();
        loginName = loginName.replace(/\ +/g, "");
        /* var staffid = $("#staffid").val();
         staffid=staffid.replace(/\ +/g,"");
         var loginPass = $("#loginPass").val();
         loginPass=loginPass.replace(/\ +/g,"");
         var confirmPass = $("#confirmPass").val();
         confirmPass=confirmPass.replace(/\ +/g,""); */
        var tel_reg = /^(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
        //var loginpasstemp = true;
        //var confirmpasstemp = true;
        /* if (roleId=="-1") {
         $("#roleIdP").attr("style","color:red");
         flag = false;
         }else{
         $("#roleIdP").attr("style","display:none");
         } */
        if (!loginName) {
            $("#loginNameP").attr("style", "color:red");
            flag = false;
        } else {
            $("#loginNameP").attr("style", "display:none");
        }
        if (checkLoginName(loginName)) {
            //returnmess = '该手机号已存在，请重新输入！';
            flag = false;
            checkSign = true;
        }
        /* if(checkStaffid(staffid)){
         returnmess = '该站点下的工号已存在，请重新输入！';
         flag = false;
         checkSign = true;
         staffidSign = true;
         } */
        if (!checkMobile(loginName)) {//!tel_reg.test(loginName)
            $("#loginNameP").text("请重新输入11位手机号!");
            $("#loginNameP").attr("style", "color:red");
            flag = false;
        } else {
            $("#loginNameP").attr("style", "display:none");
        }
        if (!realName) {
            $("#realNameP").attr("style", "color:red");
            flag = false;
        } else {
            $("#realNameP").attr("style", "display:none");
        }
        /*if (!staffid) {
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
         } */
        if (flag) {

            console.log("succeful , submit");
            $("#userForm").ajaxSubmit({
                type: 'post',
                url: url,
                success: function (data) {
                    if (data == "true") {

                        if (getSign == 'create') {
                            //alert("保存用户成功");
                            $(".j-user-pop").modal("hide");
                            //document.getElementById("userForm").reset();
                        } else {
                            alert("修改用户成功");
                            $(".j-user-pop").modal("hide");
                            //document.getElementById("staffidTemp").value = staffid;
                        }

                        gotoPage(0);
                    } else {
                        alert("保存用户失败");
                    }

                },
                error: function (JsonHttpRequest, textStatus, errorThrown) {
                    //alert( "服务器异常!");
                    if (window.top == window.self) {//不存在父页面
                        window.location.href = "<c:url value="/login" />"
                    } else {
                        window.top.location.href = "<c:url value="/login" />"
                    }
                }
            });
        } else if (checkSign) {
            //alert(returnmess);
            //$("#loginNameP").text("手机号已存在，请重新输入11位手机号!");
            $("#loginNameP").attr("style", "color:red");
            return false;
        } else {
            //alert("有非法内容，请检查内容合法性！");
            return false;
        }

    }


    function searchUser(id, loginName) {
        $('.userclass').html('修改');
        $.ajax({
            type: "GET",
            url: '<c:url value="/userManage/getOneUser" />',
            data: {
                "id": id,
                "loginName": loginName
            },
            success: function (data) {
                if (data != null) {
                    $("#loginNameP").attr("style", "display:none");
                    $("#staffidP").attr("style", "display:none");
                    document.getElementById("userForm").reset();
                    $("#realName").val(data.realName);
                    $("#loginName").val(data.loginName);
                    $("#staffid").val(data.staffid);
                    $("#roleId").val(data.roleStatus);
                    $("#loginPass").val(data.passWord);
                    $("#confirmPass").val(data.passWord);
                    $("#loginName").attr("readonly", true);
                    //document.getElementById("sign").value="edit";
                    document.getElementById("loginNameTemp").value = data.loginName;
                    document.getElementById("staffidTemp").value = data.staffid;
                    document.getElementById("operate").value = "edit";
                }
            },
            error: function () {
                //alert("异常！");
                //location.href = '<c:url value="/login" />';
                if (window.top == window.self) {//不存在父页面
                    window.location.href = "<c:url value="/login" />"
                } else {
                    window.top.location.href = "<c:url value="/login" />"
                }
            }
        });


    }


    function restUserModel() {
        $('.userclass').html('新建');
        document.getElementById("userForm").reset();
        $("#loginName").attr("readonly", false);
        $("#roleIdP").attr("style", "display:none");
        $("#realNameP").attr("style", "display:none");
        $("#loginNameP").text("请正确输入11位手机号");
        $("#staffidP").text("请输入员工ID");
        $("#staffidP").attr("style", "display:none");
        $("#loginNameP").attr("style", "display:none");
        $("#loginpassP").attr("style", "display:none");
        $("#confirmPassP").attr("style", "display:none");
        document.getElementById("operate").value = "create";
    }
</script>
</body>
</html>