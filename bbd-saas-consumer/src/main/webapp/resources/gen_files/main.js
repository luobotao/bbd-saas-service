/*
 * created by zhuqiao zhuqiao@baidu.com
 * date: 2014-12-03
 */

/*
 * 对DOM元素的样式设置和事件处理，不涉及地图的操作
 */

$(document).ready(function(){

	$(".nav>ul>li .list-item").click(function(){
		if($(this).hasClass("current")){
			$(".nav>ul>li .current").next().removeClass("show").addClass("hidden");
			$(".nav>ul>li .list-item").removeClass("current");
		}else{
			$(".nav>ul>li .current").next().removeClass("show").addClass("hidden");
			$(".nav>ul>li .current").removeClass("current");
			$(this).addClass("current");
			$(this).next().removeClass("hidden").addClass("show");
		}
	});

	$("#add_overlay ul li").click(function(){
		$("#add_overlay ul li").removeClass("ol_present");
		$("#add_overlay p.show").removeClass("show").addClass("hidden");
		$(this).addClass("ol_present");
		var idx = $(this).attr("idx");
		$("#add_overlay p:eq(" + idx + ")").removeClass("hidden").addClass("show");
		var type = $(this).attr("id");
		setCursor(type);
	});

	$("#close").click(function(){
		$(".showCode").css("display","none");
		//删除flash原件
		if(clip)
			clip = null;
	});
});

function setCity(){
	$("#setCity div").slideToggle();
}
function exitSetCity(value){
	$("#setCity div").slideToggle("fast",function(){
		$("#input_city").val(value);
		$("#current_city").text(value);
	});
	
}

function initPanel(){
	$("#add_overlay section").removeClass("show").addClass("hidden");
	$("#edit_marker").css("display","none");
	$("#edit_polyline").css("display","none");
	$("#edit_label").css("display","none");
	$("#list_marker").css("display","none");
	$("#list_polyline").css("display","none");
	$("#list_label").css("display","none");
}

function showEditPanel(domId,index){
	$("#"+domId+" .bg_item:eq("+index+")").find(".editing").removeClass("show").addClass("hidden");
	$("#"+domId+" .bg_item:eq("+index+")").next().removeClass("hidden").addClass("show");
}

function hideEditPanel(domId,index){
	$("#"+domId+" .bg_item:eq(" + index + ")").find(".editing").removeClass("hidden").addClass("show");
	$("#"+domId+" .bg_item:eq(" + index + ")").next().removeClass("show").addClass("hidden");
}

function hideMarkerSetter(){
	$(".set_marker_icon").css("display","none");
}

function showMarkerSetter(){
	$(".set_marker_icon").css("display","block");
}

function showUnsavedLabel(domId,index){
	if($("#"+domId+" .bg_item:eq("+index+")").find("label").length == 0 )
		$("#" + domId + " .bg_item:eq(" + index+")").find(".marker_title").after("<label style='color:red'> (未保存)</label>");
	else
		$("#"+domId+" .bg_item:eq("+index+")").find("label").css("display","inline");
}

function hideUnsavedLabel(domId,index){
	if($("#"+domId+" .bg_item:eq("+index+")").find("label").length != 0 ){
		$("#"+domId+" .bg_item:eq("+index+")").find("label").css("display","none");
	}
}