<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page import="com.bbd.saas.mongoModels.Site" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.SiteStatus" %>
<%@ page import="com.bbd.saas.enums.SiteTurnDownReasson" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ include file="../main.jsp"%>
<html>
<head>
    <title>系统设置-站点管理</title>
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
                <div class="search-area form-inline form-inline-n">
                    <div class="row">
                        <jsp:include page="../control/siteIdControl.jsp" flush="true" />
                    </div>
                    <div class="row">
                        <div class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
                            <div class="form-group pb20">
                                <label>站点状态：</label>
                                <select id="status" name="status" class="form-control form-con-new">
                                    <%=SiteStatus.Stas2HTML(-1)%>
                                </select>
                            </div>
                            <div class="form-group pb20">
                                <label>　配送区域状态：</label>
                                <select id="areaFlag" name="areaFlag" class="form-control form-con-new">
                                    <option value="-1">全部</option>
                                    <option value="1">有效</option>
                                    <option value="0">无效</option>
                                </select>
                            </div>
                            <div class="form-group pb20">
                                <label>　关键字：</label>
                                <input type="text" id="keyword" name="keyword" placeholder="站点名称/站长姓名/手机"
                                       class="form-control"/>
                            </div>
                            <div class="form-group ml6 pb20">
                                <a href="javascript:void(0)" onclick=" gotoPage(0);" class="ser-btn l"><i
                                        class="b-icon p-query p-ser"></i>查询</a>
                                <a href="javascript:void(0)" class="ser-btn d ml6 " data-toggle='modal' data-target='#siteModal' onclick="createSite();"><i class="num-add mr10">＋</i><em>新建</em></a>

                            </div>

                        </div>

                    </div>
                    <%--<div class="row pb20">
                    </div>--%>
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
                                <th>站长姓名</th>
                                <th>手机号</th>
                                <th>邮箱</th>
                                <th>站点状态</th>
                                <th>配送区域</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody id="dataList">

                            <%
                                for (Site site : sitePage.getDatas()) {
                            %>
                            <tr>
                                <td><%=site.getName()%></td>
                                <td><%=site.getAreaCode()==null?"":site.getAreaCode()%></td>
                                <td><%=site.getProvince()%>-<%=site.getCity()%>-<%=site.getArea()%> <%=site.getAddress()%></td>
                                <td><%=site.getResponser()%></td>
                                <td><%=site.getUsername()%></td>
                                <td><%=site.getEmail()%></td>
                                <td><%=site.getStatus().getMessage()%></td>
                                <td>
                                    <%
                                        if (site.getAreaFlag() == 1) {
                                    %>
                                        有效
                                    <%
                                        }else{
                                    %>
                                        无效
                                    <%
                                        }
                                    %>
                                </td>
                                <td>
                                    <%
                                        if (SiteStatus.WAIT == site.getStatus()) {
                                    %>
                                        <span onclick="setPhone('<%=site.getUsername() %>','<%=site.getName() %>')" class="orange cp" data-toggle='modal' data-target='#validModal'>通过</span>
                                        <span onclick="setPhone('<%=site.getUsername() %>','<%=site.getName() %>')" class="orange ml6 j-reject cp">驳回</span>
                                    <%
                                        }else if (SiteStatus.TURNDOWN == site.getStatus()) {
                                    %>
                                        <span  onclick="getTurnDownMessage('<%=site.getTurnDownReasson() %>','<%=site.getTurnDownReasson().getMessage() %>','<%=site.getOtherMessage() %>')" class="orange ml6 cp" data-toggle='modal' data-target='#messageModal'>查看驳回原因</span>
                                    <%
                                        }else if (SiteStatus.APPROVE == site.getStatus()) {
                                    %>
                                        <span onclick="getSiteByAreaCode('<%=site.getAreaCode() %>')" data-toggle='modal' data-target='#siteModal'
                                           class="orange j-siteM cp">修改</span>
                                        <span class="orange cp" onclick="showConfirmDiv('<%=site.getAreaCode() %>', 'disableSite', '确认停用站点吗？')" data-toggle='modal' data-target='#confirmModal'>停用站点</span>
                                        <%
                                            if (site.getAreaFlag() == 1) {
                                        %>
                                            <span  class="orange cp" onclick="showConfirmDiv('<%=site.getAreaCode() %>', 'disableArea', '确认停用配送区域吗？')" data-toggle='modal' data-target='#confirmModal'>停用配送区域</span>
                                        <%
                                            }else{
                                        %>
                                            <span  class="orange cp" onclick="showConfirmDiv('<%=site.getAreaCode() %>', 'enableArea', '确认启用配送区域吗？')" data-toggle='modal' data-target='#confirmModal'>启用配送区域</span>
                                        <%
                                            }
                                        %>
                                    <%
                                        }else if (SiteStatus.INVALID == site.getStatus()) {
                                    %>
                                        <span onclick="getSiteByAreaCode('<%=site.getAreaCode() %>')" data-toggle='modal' data-target='#siteModal'
                                           class="orange j-siteM cp">修改</span>
                                        <span class="orange cp" onclick="showConfirmDiv('<%=site.getAreaCode() %>', 'enableSite', '确认启用站点吗？')" data-toggle='modal' data-target='#confirmModal'>启用站点</span>
                                        <%--<span class="orange cp" onclick="showConfirmDiv('<%=site.getAreaCode() %>', 'delSite', '确认删除？删除站点将会将该站点下的所有用户删除?')" data-toggle='modal' data-target='#confirmModal'>删除</span>--%>
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
<div id="mask"></div>
<!--S 新建/修改-->
<div class="j-siteM-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" id="siteModal">
    <div class="modal-dialog b-modal-dialog b-guide-dialog" role="document">
        <div class="modal-content">
        <c:url var="actionUrl" value="/siteManage/saveSite?${_csrf.parameterName}=${_csrf.token}"/>
        <form role="form" action="${actionUrl}" method="post" id="siteForm" enctype="multipart/form-data" class="form-inline form-inline-n">
            <input type="hidden" id="areaCode" name="areaCode" value=""/>
            <div class="modal-header b-modal-header">
                <button type="button" onclick="closeEditDiv()" class="close j-f-close" data-dismiss="modal" aria-label="Close" id="closeButton"><span aria-hidden="true">×</span></button>
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
                        <input type="password" class="form-control form-bod wp80 j-nf-pwd" id="password" name="password"/>
                    </li>
                    <li class="filter">
                        <i>确认密码：</i>
                        <input type="password" class="form-control form-bod wp80 j-cf-pwd" id="passwordConfirm" name="passwordConfirm"/>
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
                <em class="f16" id="validSiteName">确认将站点"AAA"审核通过吗？</em>
                <div class="clearfix mt20">
                    <a href="javascript:void(0);" id="conFirmForValidBtn" class="sbtn sbtn2 l col-md-12">确认</a>
                </div>

            </div>

        </div>
    </div>
</div>
<!--E 通过-->
<!--S 驳回-->
<div class="j-reject-pop modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" id="turnDownModal">
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

<!--S 操作确认-->
<div class="j-pl-pop modal fade" id="confirmModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal_wrapper">
        <div class="modal-dialog b-modal-dialog">
            <div class="modal-content">
                <div class="modal-header b-modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title  tc" id="confirmTitle">确认</h4>
                </div>
                <div class="modal-body b-modal-body">
                    <em class="f16" id="confirmBody">确认启用配送区域吗？</em>
                </div>
                <div class="modal-footer tc">
                    <%--<div class="row mt20">--%>
						<span class="col-md-6">
							<button type="button" class="ser-btn g wp80" data-dismiss="modal" class="close">取消</button>
						</span>
						<span class="col-md-6">
							<button  type="button" class="ser-btn l wp80" onclick="doOperation()">确认</button>
						</span>
                    <%--</div>--%>
                </div>
            </div>
        </div>
    </div>
</div>
<!--E 操作确认-->
<!-- S 省市区站点选择控件 -->
<script type="text/javascript">
    var  siteUrl = "<c:url value="/site/getSiteList"/>";
    var  inputName = "siteName_control";
    var isSiteId = true;
</script>
<script src="<c:url value="/resources/javascripts/siteControl.js" />"> </script>
<!-- E 省市区站点选择控件  -->
<!-- E pop -->
<script type="text/javascript">
    //是否需要校验手机号，默认需要；但是当失去焦点点击关闭按钮时，不需要校验手机号
    var isCheckPhone = true;
    var operFlag = ""; //操作类型（enableArea：启用配送区域；disableArea：停用配送区域；）
    var areaCode = "";//站点编码
    //显示分页条
    var pageStr = paginNav(<%=sitePage.getPageNo()%>, <%=sitePage.getTotalPages()%>, <%=sitePage.getTotalCount()%>);
    $("#pagin").html(pageStr);


    //加载带有查询条件的指定页的数据
    function gotoPage(pageIndex) {
        var status = $("#status").val();
        var areaFlag = $("#areaFlag").val();
        var keyword = $("#keyword").val();
        var url = "<c:url value="/siteManage/getSitePage" />";
        $.ajax({
            type: "GET",  //提交方式
            url: url,//路径
            dataType: "json",
            data: {
                "prov" : $("#addr_control .prov").val(),
                "city" :  $("#addr_control .city").val(),
                "area" :  $("#addr_control .dist").val(),
                "pageIndex": pageIndex,
                "siteIdStr" : getAreaCodeStr(),//站点编号集合
                "status": status,
                "areaFlag": areaFlag,
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
        var areaCode = data.areaCode;
        if(areaCode==null||areaCode=="null"){
            areaCode = "";
        }
        row += "<td>" + areaCode + "</td>";
        row += "<td>" + data.province +"-"+ data.city +"-"+ data.area +""+ data.address + "</td>";
        row += "<td>" + data.responser + "</td>";
        row += "<td>" + data.username + "</td>";
        row += "<td>" + data.email + "</td>";
        row += "<td>" + data.statusMessage + "</td>";
        if(data.areaFlag == 1 ){
            row += "<td>有效</td>";
        }else{
            row += "<td>无效</td>";
        }
        row += "<td>";
        if(data.status=="<%=SiteStatus.WAIT%>" ){
            row += "<span  onclick=\"setPhone('"+data.username+"','"+data.name+"')\" class='orange cp' data-toggle='modal' data-target='#validModal'>通过</span> ";
            row += "<span  onclick=\"setPhone('"+data.username+"','"+data.name+"')\" class='orange cp' data-toggle='modal' data-target='#turnDownModal'>驳回</span>";
        }
        if(data.status=="<%=SiteStatus.TURNDOWN%>" ){
            row += "<span  onclick=\"getTurnDownMessage('"+data.turnDownReasson+"','"+data.turnDownMessage+"','"+data.otherMessage+"')\" class='orange ml6 cp' data-toggle='modal' data-target='#messageModal'>查看驳回原因</span>";
        }
        if(data.status=="<%=SiteStatus.APPROVE%>" ){
            row += "<span  onclick=\"getSiteByAreaCode('"+data.areaCode+"')\" class='orange j-siteM cp' data-toggle='modal' data-target='#siteModal'>修改</span> ";
            row += "<span  data-toggle='modal' data-target='#confirmModal' onclick=\"showConfirmDiv('"+data.areaCode+"', 'disableSite', '确认停用站点吗？')\" class='orange cp'>停用站点</span>";
            if(data.areaFlag == 1 ){
                row += "<span  onclick=\"showConfirmDiv('"+data.areaCode+"', 'disableArea', '确认停用配送区域吗？')\" class='orange cp ml16' data-toggle='modal' data-target='#confirmModal'>停用配送区域</span> ";
            }else{
                row += "<span  onclick=\"showConfirmDiv('"+data.areaCode+"', 'enableArea', '确认启用配送区域吗？')\" class='orange cp ml16' data-toggle='modal' data-target='#confirmModal'>启用配送区域</span> ";
            }
        }
        if(data.status=="<%=SiteStatus.INVALID%>" ){
            row += "<span  onclick=\"getSiteByAreaCode('"+data.areaCode+"')\" class='orange j-siteM cp'  data-toggle='modal' data-target='#siteModal'>修改</span> ";
            row += "<span  data-toggle='modal' data-target='#confirmModal' onclick=\"showConfirmDiv('"+data.areaCode+"', 'enableSite', '确认启用站点吗？')\" class='orange cp'>启用站点</span> ";
//            row += "<span data-toggle='modal' data-target='#confirmModal' onclick=\"showConfirmDiv('"+data.areaCode+"', 'delSite', '确认删除？删除站点将会将该站点下的所有用户删除？')\" class='orange cp'>删除</span>";
        }
        row += "</td></tr>";
        return row;
    }

    function checkSiteWithUsername(loginName){
        setTimeout(function(){
            if(isCheckPhone){
                var areaCode = $("#areaCode").val();
                if(loginName!=""){
                    var linkUrl = "<c:url value="/siteManage/checkSiteWithLoginName?loginName=" />" + loginName + "&areaCode=" + areaCode;
                    $.ajax({
                        url: linkUrl,
                        type: 'GET',
                        cache: false,
                        dataType: "text",
                        data: {},
                        success: function(response){
                            if(response=="false"){
                                $("#phoneFlag").val(0);
                                ioutDiv("手机号已存在");
                            }else{
                                $("#phoneFlag").val(1);
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

    function closeEditDiv(){
        isCheckPhone = false;
    }
    //保存站点（新建）
    $("#saveSiteBtn").click(function () {

        var name = $.trim($("#name").val());
        if (name == "" ) {
            ioutDiv("请输入站点名称");
            return false;
        }
        var province = $("#city_4 .prov").val();
        var city = $("#city_4 .city").val();
        var area = $("#city_4 .dist").val();
        $("#province").val(province);
        $("#city").val(city);
        $("#area").val(area);
        var address = $("#address").val();
        if(address==""){
            ioutDiv("请输入详细地址");
            return false;
        }
        var responser = $("#address").val();
        if(responser==""){
            ioutDiv("请输站长姓名");
            return false;
        }

        var phone = $.trim($('input[name="phone"]').val());
        var phoneFlag = $("#phoneFlag").val();
        if(phone==""){
            ioutDiv("请输入手机号");
            return false;
        } else{
            if(checkMobile(phone)==false){
                ioutDiv("请输入正确的手机");
                return false;
            }else{
                var areaCode = $("#areaCode").val();
                if(phone!=""){
                    var linkUrl = "<c:url value="/siteManage/checkSiteWithLoginName?loginName=" />" + phone + "&areaCode=" + areaCode;
                    $.ajax({
                        url: linkUrl,
                        type: 'GET',
                        cache: false,
                        dataType: "text",
                        data: {},
                        success: function(response){
                            if(response=="false"){
                                $("#phoneFlag").val(0);
                                ioutDiv("手机号已存在");
                            }else{
                                $("#phoneFlag").val(1);
                                var password = $("#password").val();
                                if(password==""){
                                    ioutDiv("请输入新密码");
                                    return false;
                                }

                                if(!pwdreg.test(password)){
                                    ioutDiv("请输入6-12位数字和字母结合的密码");
                                    return false;
                                }

                                var passwordConfirm = $("#passwordConfirm").val();
                                if(passwordConfirm==""){
                                    ioutDiv("请输入确认密码");
                                    return false;
                                }
                                if(passwordConfirm!=password){
                                    ioutDiv("两次密码不一致");
                                    return false;
                                }

                                var email = $("#email").val();
                                if(email==""){
                                    ioutDiv("请输入邮箱");
                                    return false;
                                } else{
                                    var emailFlag = checkemail(email);
                                    if(emailFlag==false){
                                        ioutDiv("邮箱格式不正确");
                                        return false;
                                    }
                                }

                                $("#siteForm").ajaxSubmit({
                                    success: function(data){
                                        if(data==true){
                                            $(".j-siteM-pop").modal("hide");
                                            $("#closeButton").click();
                                            gotoPage(0);
                                        }else{
                                            ioutDiv( "保存站点失败");
                                        }

                                    },
                                    error: function(JsonHttpRequest, textStatus, errorThrown){
                                        $("#closeButton").click();
                                        gotoPage(0);
                                    }
                                });
                            }
                        },
                        error: function(){
                            ioutDiv('服务器繁忙，请稍后再试！');
                        }
                    });
                }

            }
        }


    })

    $("#conFirmForValidBtn").click(function(){
        $.ajax({
            type: "GET",
            url: '<c:url value="/siteManage/validSite" />',
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
                ioutDiv("异常！");
            }
        });
    });
    $("#conFirmForTurnDownBtn").click(function(){
        $.ajax({
            type: "GET",
            url: '<c:url value="/siteManage/turnDownSite" />',
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
                ioutDiv("异常！");
            }
        });
    });
    function disableSiteFct(){
        console.log("停用站点");
        $.ajax({
            type: "GET",
            url: '<c:url value="/siteManage/stopSite" />',
            dataType: "text",
            data: {
                "areaCode": areaCode
            },
            success: function (data) {
                if (data == 'true') {
                    location.reload();
                }
            },
            error: function () {
                ioutDiv("异常！");
            }
        });
    }

    function enableSiteFct(){
        $.ajax({
            type: "GET",
            url: '<c:url value="/siteManage/startSite" />',
            dataType: "text",
            data: {
                "areaCode": areaCode
            },
            success: function (data) {
                if (data == 'true') {
                    location.reload();
                }
            },
            error: function () {
                ioutDiv("异常！");
            }
        });
    }

    function delSiteFct(){
        $.ajax({
            type: "GET",
            url: '<c:url value="/siteManage/delSite" />',
            dataType: "text",
            data: {
                "areaCode": areaCode
            },
            success: function (data) {
                if (data == 'true') {
                    location.reload();
                }
            },
            error: function () {
                ioutDiv("异常！");
            }
        });
    }

    //赋值
    function setPhone(phone,siteName){
        $('#phoneForModal').val(phone);
        $('#validSiteName').html('确认将站点"'+siteName+'"审核通过吗？');
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
    $('#areaCode').val('');
    $('#areaCodeForModal').val('');
    var defprov = "北京";
    var defcity = "北京";
    var defdist = "朝阳区";

    $("#city_4").citySelect({
        prov: defprov,
        city: defcity,
        dist: defdist,
        nodata: "none"
    });
}
    function getSiteByAreaCode(areaCode) {
        $('#titleName').html("修改");
        $('#areaCodeForModal').val(areaCode);
        $.ajax({
            type: "GET",
            url: '<c:url value="/siteManage/getSiteByAreaCode" />',
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
                    $("#password").val(data.password);
                    $("#passwordConfirm").val(data.password);
                    $("#email").val(data.email);
                    $("#province").val(data.province);
                    $("#city").val(data.city);
                    $("#area").val(data.area);
                    $("#address").val(data.address);
                    $("#phone").val(data.username);
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


/******************************************************** 配送区域操作 *****************************************************************/
    //显示操作提示框
    function showConfirmDiv(siteIdStr, operType, info, title) {
        areaCode = siteIdStr;
        operFlag = operType;
        $("#confirmBody").html(info);
        if(title == null){
            $("#confirmBody .tc").html("确认");
        }else{
            $("#confirmBody .tc").html(title);
        }

    }
    //操作确认按钮
    function doOperation() {
        if(operFlag == "enableArea"){//启用配送区域
            updateAreaFct(1);
        }else if(operFlag == "disableArea"){//停用配送区域
            updateAreaFct(0);
        }else if(operFlag == "enableSite"){//启用配送区域
            enableSiteFct();
        }else if(operFlag == "disableSite"){//停用配送区域
            disableSiteFct();
        }else if(operFlag == "delSite"){//停用配送区域
            delSiteFct();
        }

    }
    //配送区域操作
    function updateAreaFct(areaFlag) {
        $.ajax({
            type : "POST",  //提交方式
            url : "<c:url value="/siteManage/updateArea?${_csrf.parameterName}=${_csrf.token}" />",//路径
            data : {
                areaCode : areaCode,
                areaFlag : areaFlag
            },//数据，这里使用的是Json格式进行传输
            success : function(data) {//返回数据根据结果进行相应的处理
                if(data == 1){//
                    ioutDiv("操作成功！");
                    var pageIndex = parseInt($(".pagination .active").text())-1;
                    gotoPage(pageIndex);
                }else if(data == -1){
                    ioutDiv("此站点无司机线路，请设置！");
                }else{
                    ioutDiv("操作失败！");
                }
            },
            error : function() {
                ioutDiv("服务器繁忙，请稍后再试！");
            }
        });
        $("#confirmModal").modal("hide");
    }
/********************************************************** 配送区域操作 **************************************************************/

</script>
</body>
</html>