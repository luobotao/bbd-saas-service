<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
    <title>注册公司</title>
    <jsp:include page="../main.jsp" flush="true"/>
</head>
<body class="fbg">
<c:url var="actionUrl" value="/register/saveCompany?${_csrf.parameterName}=${_csrf.token}"/>
<form role="form" enctype="multipart/form-data" action="${actionUrl}" method="post" id="companyForm"
      class="form-inline form-inline-n">
    <input type="hidden" id="phone" name="phone" value="${phone}">
    <div class="modal-body b-modal-body y-scroll">
        <ul class="b-n-crt">

            <li class="filter">
                <i>公司名称：</i>
                <input type="text" class="form-control form-bod wp80" id="companyname" name="companyname">
                <em class="tip-info" id="companynameP" style="display:none;">请输入公司名称</em>
            </li>
            <li class="filter">
                <i class="mb12">营业执照：</i>
                <div class="clearfix"></div>
                <input id="licensePic" name="licensePic" class="file input-d" type="file">
                <em class="tip-info" id="licensePicP" style="display:none;">请上传公司营业执照</em>
            </li>
            <li class="filter" id="city_4">
                <i>所在地：</i>
                <em class="wp25">
                    <select class="form-control form-bod w150  prov" name="prov"></select>
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
                <i>负责人姓名：</i>
                <input type="text" class="form-control form-bod wp80" id="responser" name="responser">
                <em class="tip-info" id="responserP" style="display:none;">请输入负责人姓名</em>
            </li>
            <li class="filter">
                <i>联系邮箱：</i>
                <input type="text" class="form-control form-bod wp80" id="email" name="email"
                       onkeyup="value=value.replace(/[^a-zA-Z\-_@@\.0-9]/g,'')">
                <em class="tip-info" id="emailP" style="display:none;">请输入邮箱且格式正确</em>
            </li>

        </ul>
    </div>
    <div class="modal-footer b-modal-body bod0">

        <a href="javascript:history.go(-1);" class="ser-btn l">上一步</a>
        <a href="javascript:void(0);" class="ser-btn l" id="saveCompanyBtn">提交</a>
    </div>
</form>

<script>
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
    $("#licensePic").fileinput({'showUpload': false, 'previewFileType': 'any'});


    $("#saveCompanyBtn").click(function () {
        var flag = true;
        var companyname = $.trim($('input[name="companyname"]').val());
        var companynameP = $("#companynameP").val();
        if (companyname == "" || companyname == 0) {
            $("#companynameP").attr("style", "color:red");
            flag = false;
        }
        var licensePic = $("#licensePic").val();
        if(licensePic==""){
            $("#licensePicP").attr("style","color:red");
            flag = false;
        } else{
            $("#licensePicP").attr("style","display:none");
        }
        var province = $(".prov").val();
        var city = $(".city").val();
        var area = $(".dist").val();
        $("#province").val(province);
        $("#city").val(city);
        $("#area").val(area);

        var responser = $("#responser").val();
        if(responser==""){
            $("#responserP").attr("style","color:red");
            flag = false;
        } else{
            $("#responserP").attr("style","display:none");
        }

        var email = $("#email").val();
        if(email==""){
            $("#emailP").attr("style","color:red");
            flag = false;
        } else{
            var emailFlag = checkemail(email);
            if(emailFlag==false){
                $("#emailP").attr("style","color:red");
                flag = false;
            }else{
                $("#emailP").attr("style","display:none");
            }
        }
        if (flag) {
            $("#companyForm").submit();
        } else {
            return false;
        }
    })
</script>
</body>
</html>