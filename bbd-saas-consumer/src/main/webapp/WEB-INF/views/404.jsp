<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<%@ page language="java" pageEncoding="UTF-8" %>
<html>
    <head>
        <meta charset="UTF-8">
        <title>404  - 棒棒达快递</title>
        <link rel="stylesheet" href="<c:url value="/resources/bootstrap/css/bootstrap.min.css" />" /> <!--bootstrap.min.css-->
        <link rel="stylesheet" href="<c:url value="/resources/stylesheets/main.css" />" /> <!--自定义css-->

    </head>
    <body>
        <!-- S nav -->
        <nav class="navbar navbar-default b-navbar">
          <div class="container">
            <div class="navbar-header">
              <a class="navbar-brand" href="#"><img src="<c:url value="/resources/images/logo.png" />"  alt="logo" /></a>
            </div>
          </div>
        </nav>
        <!-- E nav -->
        <!-- S content -->
        <div class="container">
        	<div class="error-area clearfix">
        		<div class="fl"><img src="<c:url value="/resources/images/error_img.png" />" /></div>
        		<div class="error-txt fl">
        			<img src="<c:url value="/resources/images/error_txt.png" />" />
        			<p class="reasult-info">可能因为：</p>
        			<p class="f16 mb8"><i class="f18">网址有错误</i> &gt;请检查地址是否完整或存在多余字符</p>
        			<p class="f16"><i class="f18">网址已失效 </i>&gt;可能页面已删除，活动已下线等</p>
        			<div class="error-btn tc">
	        			<a href="<c:url value="/login" />"  class="ser-btn l">返回首页</a>
	        		</div>
        		</div>
        		
        	</div>
        </div>
        <!-- E content -->
        <!-- S footer -->
        <footer class="container tc">
            <em class="b-copy">京ICP备 465789765 号 版权所有 &copy; 2016-2020 棒棒达       北京棒棒达科技有限公司</em>
        </footer>
        <!-- E footer -->
    </body>
<script>
    if(window.top==window.self){//不存在父页面

    }else{
        window.top.location.href="<c:url value="/404" />"
    }
</script>
</html>