<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
</head>
<body>
<!--S 设为超区件-->
<div class="j-pl-pop modal fade" id="superAreaDiv" tabindex="-1" role="dialog" aria-hidden="true">
	<div class="modal_wrapper">
		<div class="modal-dialog b-modal-dialog">
			<div class="modal-content">
				<div class="modal-header b-modal-header">
					<button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
					<h4 class="modal-title  tc" id="superAreaConform">确认</h4>
				</div>
				<div class="modal-body b-modal-body">
					<em class="f16" id="superAreaBody">确认将该订单设置为超区件？</em>
				</div>
				<div class="modal-footer tc">
					<%--<div class="row mt20">--%>
						<span class="col-md-6">
							<button type="button" class="ser-btn g wp80" data-dismiss="modal" class="close">取消</button>
						</span>
						<span class="col-md-6">
							<button  type="button" class="ser-btn l wp80" onclick="doSuperArea()">确认</button>
						</span>
					<%--</div>--%>
				</div>
			</div>
		</div>
	</div>
</div>
<!--E 设为超区件-->

<script type="text/javascript">
	var superAreaIsBatch = false;
	//设为超区件 -- 显示超区件提示
	function showSuperAreaDiv(mailNumStr) {
		mailNum = mailNumStr;
		$("#superAreaBody").html("确认将该订单设置为超区件？");
		superAreaIsBatch = false;
	}
	//做单个设置超区===确认按钮
	function doOneSuperArea() {
		$.ajax({
			type : "POST",  //提交方式
			url : "<c:url value="/packageToSite/doSuperArea?${_csrf.parameterName}=${_csrf.token}" />",//路径
			data : {
				mailNum : mailNum
			},//数据，这里使用的是Json格式进行传输
			success : function(data) {//返回数据根据结果进行相应的处理
				if(data){//
					ioutDiv("操作成功！");
					var pageIndex = parseInt($(".pagination .active").text())-1;
					gotoPage(pageIndex);
				}else{
					ioutDiv("操作失败！");
				}
			},
			error : function() {
				ioutDiv("服务器繁忙，请稍后再试！");
			}
		});
		$("#superAreaDiv").modal("hide");
	}


	//批量超区button === 显示提示信息
	function batchSuperAreaBtn(){
		var checkids = $('input[name="id"]:checked');
		ids = [];
		if(checkids.length) {
			$.each(checkids, function(i, n){
				if(!$(n).closest('tr').find('.status .label').hasClass('label-success')) {
					ids.push($(n).val());
				}
			});
		}
		if(ids.length>0){
			var url = "<c:url value="/packageToSite/isAllSuperArea?${_csrf.parameterName}=${_csrf.token}" />";
			$.ajax({
				url: url,
				type: 'POST',
				cache: false,
				dataType: "json",
				data: {
					"ids" : JSON.stringify(ids)
				},
				success: function(data){
					if(data){//没有出现状态为异常的订单
						$("#superAreaBody").html("确认将选中的订单设置为超区件？");
						$("#superAreaDiv").modal("show");
						superAreaIsBatch = true;//批量超区
					}else{
						ioutDiv("只有订单状态为未到站、未分派、已分派的订单才可以设置为超区件,选中的订单中有不符合条件的订单。");
					}
				},
				error: function(){
					ioutDiv('服务器繁忙，请稍后再试！');
				}
			});
		}else{
			ioutDiv('请选择运单！');
		}
	}
	//批量超区button === 显示提示信息 == 运单分派（不需要检查运单状态）
	function batchSuperAreaBtnWithNoCheck(){
		var checkids = $('input[name="id"]:checked');
		ids = [];
		if(checkids.length) {
			$.each(checkids, function(i, n){
				if(!$(n).closest('tr').find('.status .label').hasClass('label-success')) {
					ids.push($(n).val());
				}
			});
		}
		if(ids.length>0){
			$("#superAreaBody").html("确认将选中的订单设置为超区件？");
			$("#superAreaDiv").modal("show");
			superAreaIsBatch = true;//批量超区
		}else{
			ioutDiv('请选择运单！');
		}
	}
	//做设置超区===确认按钮
	function doSuperArea(){
		if(superAreaIsBatch){//批量
			doBatchSuperArea();
		}else{//单个
			doOneSuperArea();
		}
	}
	//做批量设置超区===确认按钮
	function doBatchSuperArea(){
		if(ids.length>0){
			var url = "<c:url value="/packageToSite/doBatchSuperArea?${_csrf.parameterName}=${_csrf.token}" />";
			$.ajax({
				url: url,
				type: 'POST',
				cache: false,
				dataType: "json",
				data: {
					"mailNums" : JSON.stringify(ids)
				},
				success: function(json){
					location.reload();
				},
				error: function(){
					ioutDiv('服务器繁忙，请稍后再试！');
				}
			});
		}else{
			ioutDiv('请选择运单！');
		}
	}
</script>
</body>
</html>
