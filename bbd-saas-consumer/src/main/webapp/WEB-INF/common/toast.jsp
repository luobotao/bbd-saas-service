<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>弹出框</title>

<script type="text/javascript">

//根据点击的位置在旁边弹出层
function toast(obj,text,width,left,top){
	showDiv(obj,"info_box",text,width,left,top);
  	setTimeout("hideDiv()",3000);//3秒消失
}

function showDiv(obj,divClass,text,width,left,top){
	if(width==null){
		width=170;
	}
	if(left==null){
		left=0;
	}
	if(top==null){
		top=0;
	}
	
	var offset = $(obj).offset();
	$("#text").html(text);
  	$("."+divClass).css({
  		width:width,
      	left:offset.left+left,
      	top:offset.top-top
  	});
  	$("."+divClass).show();
}

function hideDiv(){
	$(".info_box").hide();
}
</script>

<style type="text/css">
.info_box {
    width: 170px;
}
.ts2 {
    position: relative;
}
.ts1 p, .ts2 p {
    background: #f2e6a7 none repeat scroll 0 0;
    border: 1px solid #d4c15d;
    color: #de4218;
    height: 22px;
    line-height: 22px;
    padding: 0 8px;
}
.ts2 em {
    left: -10px;
    position: absolute;
    top: 0;
    z-index: 3;
}
em {
    font-style: normal;
}
img {
    border: 0 none;
}	
</style>

</head>
<body>
	<!-- 提示框 3 start -->
	 	<div class="info_box posa" style="z-index:50;display:none;position: absolute;">
			<div class="ts2">
				<p id="text">最多只能对比查看6个数据项</p> 
				<em><img src="/resources/images/prompt/ts2.png" width="11"
					height="24" /></em>
			</div>
		</div> 
	<!-- 提示框 3 end -->
</body>
</html>
