<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page import="com.bbd.saas.enums.UserStatus" %>
<%@ page import="com.bbd.saas.mongoModels.Site" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.SiteStatus" %>
<%@ page import="com.bbd.saas.enums.SiteTurnDownReasson" %>
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
                            <input type="text" id="keyword" name="keyword" placeholder="站点名称/站长姓名/手机"
                                   class="form-control"/>
                        </div>

                    </div>
                    <div class="row pb20">
                        <div class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <a href="javascript:void(0)" onclick="toSearch();" class="ser-btn l"><i
                                    class="b-icon p-query p-ser"></i>查询</a>
                            <a href="javascript:void(0)" class="ser-btn d ml6" data-toggle='modal' data-target='#siteModal' onclick="createSite();"><i class="num-add mr10">＋</i><em>新建</em></a>

                        </div>
                    </div>
                </div>
                <!-- E 搜索区域 -->
                <div class="tab-bod mt20">
                    <!-- S table -->
                    <div class="table-responsive">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>站点名称</th>
                                <th>区域码</th>
                                <th width="20%">站点地址</th>
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
                                <td><%=site.getProvince()%>-<%=site.getCity()%>-<%=site.getArea()%> <%=site.getAddress()%>
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
                                    <a href="javascript:void(0);"  onclick="setPhone('<%=site.getUsername() %>')" class="orange" data-toggle='modal' data-target='#validModal'>通过</a>
                                    <a href="javascript:void(0);"  onclick="setPhone('<%=site.getUsername() %>')" class="orange ml6 j-reject">驳回</a>
                                    <%
                                        }
                                        if (SiteStatus.TURNDOWN == site.getStatus()) {
                                    %>
                                    <a href="javascript:void(0);" onclick="getTurnDownMessage('<%=site.getTurnDownReasson() %>','<%=site.getTurnDownReasson().getMessage() %>','<%=site.getOtherMessage() %>')" class="orange ml6" data-toggle='modal' data-target='#messageModal'>查看驳回原因</a>
                                    <%
                                        }
                                        if (SiteStatus.APPROVE == site.getStatus()) {
                                    %>
                                    <a href="javascript:void(0);" onclick="getSiteByAreaCode('<%=site.getAreaCode() %>')" data-toggle='modal' data-target='#siteModal'
                                       class="orange">修改</a>
                                    <a href="javascript:void(0);" data-toggle='modal' data-target='#stopModal'
                                       onclick="setAreaCode('<%=site.getAreaCode() %>')" class="orange">停用</a>
                                    <%
                                        }
                                        if (SiteStatus.INVALID == site.getStatus()) {
                                    %>
                                    <a href="javascript:void(0);" onclick="getSiteByAreaCode('<%=site.getAreaCode() %>')" data-toggle='modal' data-target='#siteModal'
                                       class="orange">修改</a>
                                    <a href="javascript:void(0);" data-toggle='modal' data-target='#startModal'
                                       onclick="setAreaCode('<%=site.getAreaCode() %>')" class="orange">启用</a>
                                    <%--<a href="javascript:void(0);" data-toggle='modal' data-target='#delModal'--%>
                                       <%--onclick="setAreaCode('<%=site.getAreaCode() %>')" class="orange ml6">删除</a>--%>
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
<!--S 新建/修改-->
<div class="j-siteM-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" id="siteModal">
    <div class="modal-dialog b-modal-dialog b-guide-dialog" role="document">
        <div class="modal-content">
        <c:url var="actionUrl" value="/system/siteManage/saveSite?${_csrf.parameterName}=${_csrf.token}"/>
        <form role="form" action="${actionUrl}" method="post" id="siteForm" enctype="multipart/form-data"
              class="form-inline form-inline-n">
            <input type="hidden" id="areaCode" name="areaCode"/>
            <div class="modal-header b-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                <h4 class="modal-title tc j-cg-txt" id="titleName">新建</h4>
            </div>
            <div class="modal-body b-modal-body">
                <ul class="b-n-crt b-n-crt-new form-inline-n  y-scroll">
                    <li class="filter clearfix">
                        <i>站点名称：</i>
                        <input type="text" class="form-control form-bod wp80" id="name" name="name"/>
                    </li>
                    <li class="filter" id="city_4">
                        <i>站点地址：</i>
                        <em class="wp25">
                             <select class="form-control form-bod w150 prov" name="prov"></select>
                        </em>
                        <em class="wp25">
                            <select class="form-control form-bod w150 city" disabled="disabled"></select>
                        </em>
                        <em class="wp25">
                            <select class="form-control form-bod w150 dist" name="dist" disabled="disabled"></select>
                        </em>
                        <input id="province" name="province" type="hidden" class="form-control"/>
                        <input id="city" name="city" type="hidden" class="form-control"/>
                        <input id="area" name="area" type="hidden" class="form-control"/>
                        <input type="text" class="form-control form-bod wp80 input-d" id="address" name="address" placeholder="请输入详细地址"/>
                        <em class="tip-info" id="addressP" style="display:none;">请输入详细地址</em>
                    </li>
                    <li class="filter">
                        <i>站长姓名：</i>
                        <input type="text" class="form-control form-bod wp80" id="responser" name="responser">
                    </li>
                    <li class="filter">
                        <i>站长手机号：</i>
                        <input type="text" class="form-control form-bod wp80" id="phone" name="phone" onkeyup="this.value=this.value.replace(/[^\d]/g,'')" onblur="checkSiteWithUsername(this.value)">
                        <input type="text" class="form-control" id="phoneFlag" name="phoneFlag" value="1" style="display:none;">
                        <em class="tip-info-g">保存成功后站长可使用手机号登录系统</em>
                    </li>
                    <li class="filter">
                        <i>邮 箱：</i>
                        <input type="text" class="form-control form-bod wp80" id="email" name="email"
                               onkeyup="value=value.replace(/[^a-zA-Z\-_@@\.0-9]/g,'')">
                    </li>
                    <li class="filter">
                        <i>登录密码：</i>
                        <input type="password" class="form-control form-bod wp80" id="password" />
                    </li>
                    <li class="filter">
                        <i>确认密码：</i>
                        <input type="password" class="form-control form-bod wp80" id="passwordConfirm"/>
                    </li>
                </ul>
                <div class="clearfix mt20">
                    <a href="javascript:void(0);" class="ser-btn l fl input-d" id="saveSiteBtn">保存</a>
                </div>

            </div>
        </form>
        </div>

    </div>
</div>
<!--E 新建-->

<!--全局的siteId-->
<input type="hidden" id="areaCodeForModal">
<input type="hidden" id="phoneForModal">



<!--S 通过-->
<div class="j-pass-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" id="validModal">
    <div class="modal-dialog b-modal-dialog middleS" role="document">
        <div class="modal-content">

            <div class="modal-header b-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                <h4 class="modal-title tc">通过</h4>
            </div>
            <div class="modal-body b-modal-body">
                <em class="f16">确认通过吗？</em>
                <div class="clearfix mt20">
                    <a href="javascript:void(0);" id="conFirmForValidBtn" class="sbtn sbtn2 l col-md-12">确认</a>
                </div>

            </div>

        </div>
    </div>
</div>
<!--E 通过-->
<!--S 驳回-->
<div class="j-reject-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog b-modal-dialog middleS" role="document">
        <div class="modal-content">

            <div class="modal-header b-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                <h4 class="modal-title tc">驳回</h4>
            </div>
            <div class="modal-body b-modal-body form-inline form-inline-n">
                <div class="form-group tline">
                    <label>驳回原因：</label>
                    <select class="form-control form-con-new j-sel-val" id="turnDownReason">
                        <%=SiteTurnDownReasson.Stas2HTML(1)%>
                    </select>
                    <em class="j-dn"><input type="text" class="form-control mt10 mlP " id="otherMessage"/></em>
                </div>
                <div class="row mt20">
                    <div class="col-md-6">
                        <a href="javascript:void(0);" data-dismiss="modal" class="sbtn sbtn2 l">取消</a>
                    </div>
                    <div class="col-md-6">
                        <a href="javascript:void(0);" id="conFirmForTurnDownBtn" class="sbtn sbtn2 l">确认</a>
                    </div>

                </div>

            </div>

        </div>
    </div>
</div>
<!--E 驳回-->
<!--S 查看驳回原因-->
<div class="j-rejectR-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" id="messageModal">
    <div class="modal-dialog b-modal-dialog middleS" role="document">
        <div class="modal-content">

            <div class="modal-header b-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                <h4 class="modal-title tc">驳回原因</h4>
            </div>
            <div class="modal-body b-modal-body">
                <em class="f16" id="messageEM"></em>
                <div class="clearfix mt20">
                    <a href="javascript:void(0);" class="sbtn sbtn2 l col-md-12" data-dismiss="modal">确认</a>
                </div>

            </div>

        </div>
    </div>
</div>
<!--E 查看驳回原因-->
<!--S 停用-->
<div class="j-user-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="stopModalLabel" id="stopModal"
     aria-hidden="true">
    <div class="modal-dialog b-modal-dialog middleS" role="document">
        <div class="modal-content">
            <div class="modal-header b-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                <h4 class="modal-title tc">确认停用</h4>
            </div>
            <div class="modal-body b-modal-body">
                <em class="f16">确认停用吗？</em>
                <div class="clearfix mt20">
                    <a href="javascript:void(0);" id="conFirmForStopBtn" class="sbtn sbtn2 l col-md-12">确认</a>
                </div>
            </div>
        </div>
    </div>
</div>
<!--E 停用-->
<!--S 启用-->
<div class="j-user-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="startModalLabel" id="startModal"
     aria-hidden="true">
    <div class="modal-dialog b-modal-dialog middleS" role="document">
        <div class="modal-content">
            <div class="modal-header b-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                <h4 class="modal-title tc">确认停用</h4>
            </div>
            <div class="modal-body b-modal-body">
                <em class="f16">确认启用吗？</em>
                <div class="clearfix mt20">
                    <a href="javascript:void(0);" id="conFirmForSartBtn" class="sbtn sbtn2 l col-md-12">确认</a>
                </div>
            </div>
        </div>
    </div>
</div>
<!--E 启用-->
<!--S 删除-->
<div class="j-user-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="delModalLabel" id="delModal"
     aria-hidden="true">
    <div class="modal-dialog b-modal-dialog middleS" role="document">
        <div class="modal-content">
            <div class="modal-header b-modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">×</span></button>
                <h4 class="modal-title tc">确认删除</h4>
            </div>
            <div class="modal-body b-modal-body">
                <em class="f16">确认删除？删除站点将会将该站点下的所有用户删除？</em>
                <div class="clearfix mt20">
                    <a href="javascript:void(0);" id="conFirmForDelBtn" class="sbtn sbtn2 l col-md-12">确认</a>
                </div>
            </div>
        </div>
    </div>
</div>
<!--E 删除-->
<!-- E pop -->
<script type="text/javascript">
    var defprov = "北京";
    var defcity = "北京";
    var defdist = "朝阳区";
    if ($("#province").val() != "") {
        defprov = $("#province").val();
        defcity = $("#city").val();
        defdist = $("#area").val();
    }
    $("#city_4").citySelect({
        prov: defprov,
        city: defcity,
        dist: defdist,
        nodata: "none"
    });
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
        row += "<td>" + data.province +"-"+ data.city +"-"+ data.area +""+ data.address + "</td>";
        row += "<td>" + data.responser + "</td>";
        row += "<td>" + data.username + "</td>";
        row += "<td>" + data.email + "</td>";
        row += "<td>" + data.statusMessage + "</td>";
        row += "<td>";
        if(data.status=="<%=SiteStatus.WAIT%>" ){
            row += "<a href='javascript:void(0);' onclick=\"setPhone('"+data.username+"')\" class='orange' data-toggle='modal' data-target='#validModal'>通过</a> ";
            row += "<a href='javascript:void(0);' onclick=\"setPhone('"+data.username+"')\" class='orange'>驳回</a>";
        }
        if(data.status=="<%=SiteStatus.TURNDOWN%>" ){
            row += "<a href='javascript:void(0);' onclick=\"getTurnDownMessage('"+data.turnDownReasson+"','"+data.turnDownMessage+"','"+data.otherMessage+"')\" class='orange ml6' data-toggle='modal' data-target='#messageModal'>查看驳回原因</a>";
        }
        if(data.status=="<%=SiteStatus.APPROVE%>" ){
            row += "<a href='javascript:void(0);' onclick=\"getSiteByAreaCode('"+data.areaCode+"')\" class='orange' data-toggle='modal' data-target='#siteModal'>修改</a> ";
            row += "<a href='javascript:void(0);' data-toggle='modal' data-target='#stopModal' onclick=\"setAreaCode('"+data.areaCode+"')\" class='orange'>停用</a>";
        }
        if(data.status=="<%=SiteStatus.INVALID%>" ){
            row += "<a href='javascript:void(0);' onclick=\"getSiteByAreaCode('"+data.areaCode+"')\" class='orange'  data-toggle='modal' data-target='#siteModal'>修改</a> ";
            row += "<a href='javascript:void(0);' data-toggle='modal' data-target='#startModal' onclick=\"setAreaCode('"+data.areaCode+"')\" class='orange'>启用</a> ";
//            row += "<a href='javascript:void(0);' data-toggle='modal' data-target='#delModal' onclick=\"setAreaCode('"+data.areaCode+"')\" class='orange'>删除</a>";
        }
        row += "</td></tr>";
        return row;
    }

    function toSearch() {
        var status = $("#status").val();
        var keyword = $("#keyword").val();
        gotoPage(0, status, keyword);
    }
    function checkSiteWithUsername(loginName){
        var readonly = $("input[name='phone']").attr("readonly");
        if(readonly=="readonly"){
            $("#phoneFlag").val(1);
            return true;
        }
        if(loginName!=""){
            var linkUrl = "<c:url value="/system/siteManage/checkSiteWithLoginName?loginName=" />"+loginName
            $.ajax({
                url: linkUrl,
                type: 'GET',
                cache: false,
                dataType: "text",
                data: {},
                success: function(response){
                    console.log(response);
                    if(response=="false"){
                        $("#phoneFlag").val(0);
                        outDiv("手机号已存在");
                    }else{
                        $("#phoneFlag").val(1);
                    }
                },
                error: function(){
                    alert('服务器繁忙，请稍后再试！');
                }
            });
        }
    }
    $("#saveSiteBtn").click(function () {
        var flag = true;
        var name = $.trim($("#name").val());
        if (name == "" ) {
            outDiv("请输入站点名称");
            return false;
        }
        var province = $(".prov").val();
        var city = $(".city").val();
        var area = $(".dist").val();
        $("#province").val(province);
        $("#city").val(city);
        $("#area").val(area);

        var responser = $("#responser").val();
        if(responser==""){
            outDiv("请输站长姓名");
            return false;
        }

        var phone = $.trim($('input[name="phone"]').val());
        var phoneFlag = $("#phoneFlag").val();
        if(phone==""){
            outDiv("请输入手机号");
            return false;
        } else{
            if(checkMobile(phone)==false){
                outDiv("请输入正确的手机");
                return false;
            }else{
                if(phoneFlag==0){
                    outDiv("手机号已存在");
                    return false;
                }
            }
        }
        var password = $("#password").val();
        if(password==""){
            outDiv("请输入登录密码");
            return false;
        }
        var passwordConfirm = $("#passwordConfirm").val();
        if(passwordConfirm==""){
            outDiv("请确认登录密码");
            return false;
        }

        if(!pwdreg.test(password)){
            outDiv("请输入6-12位数字和字母结合的密码");
            return false;
        }
        if(passwordConfirm!=password){
            outDiv("两次密码不一致");
            return false;
        }

        var email = $("#email").val();
        if(email==""){
            outDiv("请输入邮箱");
            return false;
        } else{
            var emailFlag = checkemail(email);
            if(emailFlag==false){
                outDiv("邮箱格式不正确");
                return false;
            }
        }
        $("#siteForm").ajaxSubmit({
            success: function(data){
                if(data==true){
                    $(".j-siteM-pop").modal("hide");
                    gotoPage(0);
                }else{
                    alert( "保存站点失败");
                }

            },
            error: function(JsonHttpRequest, textStatus, errorThrown){
                alert( "服务器异常!");
            }
        });

    })

    $("#conFirmForValidBtn").click(function(){
        $.ajax({
            type: "GET",
            url: '<c:url value="/system/siteManage/validSite" />',
            dataType: "text",
            data: {
                "phone": $("#phoneForModal").val()
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
                "phone": $("#phoneForModal").val(),
                "turnDownReason": $("#turnDownReason").val(),
                "otherMessage": $("#otherMessage").val()
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
                "areaCode": $("#areaCodeForModal").val()
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
                "areaCode": $("#areaCodeForModal").val()
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
                "areaCode": $("#areaCodeForModal").val()
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

    //赋值
    function setAreaCode(areaCode){
        $('#areaCodeForModal').val(areaCode);
    }
    //赋值
    function setPhone(phone){
        $('#phoneForModal').val(phone);
    }
    //展示驳回原因
    function getTurnDownMessage(turnDownResson, turnDownRessonMessage, message) {
        if (turnDownResson == '<%=SiteTurnDownReasson.OTHER%>') {
            $("#messageEM").html(message);
        } else {
            $("#messageEM").html(turnDownRessonMessage);
        }
    }

function createSite(){
    $('#titleName').html("新建");
    document.getElementById("siteForm").reset();
    $("input[name='phone']").removeAttr("readonly");
}
    function getSiteByAreaCode(areaCode) {
        $('#titleName').html("修改");
        $('#areaCodeForModal').val(areaCode);
        $.ajax({
            type: "GET",
            url: '<c:url value="/system/siteManage/getSiteByAreaCode" />',
            data: {
                "areaCode": areaCode
            },
            dataType: "json",
            success: function (data) {
                if (data != null) {
                    document.getElementById("siteForm").reset();
                    $("#areaCode").val(data.areaCode);
                    $("#name").val(data.name);
                    $("#responser").val(data.responser);
                    $("#email").val(data.email);
                    $("#province").val(data.province);
                    $("#city").val(data.city);
                    $("#area").val(data.area);
                    $("#address").val(data.address);
                    $("#phone").val(data.username);
                    $("input[name='phone']").attr("readonly","readonly")
                    defprov = $("#province").val();
                    defcity = $("#city").val();
                    defdist = $("#area").val();
                    $("#city_4").citySelect({
                        prov:defprov,
                        city:defcity,
                        dist:defdist,
                        nodata: "none"
                    });

                }
            },
            error: function () {

            }
        });


    }

</script>
</body>
</html>