/*
 * created by zhuqiao zhuqiao@baidu.com
 * date: 2014-12-03
 */

//记录marker、label、polyline的个数
var NUM_MARKER = 0,
	NUM_LABEL = 0,
	NUM_POLYLINE = 0;

// 此变量在添加标注功能时，用于记录当前的click事件的处理函数
var clickHandler;

//声明map变量
var map;

//声明和初始化地图控件
var scale_control = new BMap.ScaleControl({anchor:BMAP_ANCHOR_BOTTOM_LEFT}),
	nav_control = new BMap.NavigationControl(),
	overview_control = new BMap.OverviewMapControl({isOpen:true,anchor:BMAP_ANCHOR_BOTTOM_RIGHT});

//声明和初始化跟随鼠标移动的label
var cursorLabel = new BMap.Label();
	cursorLabel.setOffset(new BMap.Size(10,10));
	cursorLabel.hide();

$(document).ready(function(){
	initialMap();
});

/* 创建和初始化地图
 * 设置地图的状态，并为地图添加监听事件
 */
function initialMap(){
	map = new BMap.Map("map_container");
	map.centerAndZoom(new BMap.Point(116.403874,39.914889), 12);
	map.enableScrollWheelZoom();
	map.enableKeyboard();

	//添加地图控件
	map.addControl(scale_control);
	map.addControl(nav_control);
	map.addControl(overview_control);
	map.addOverlay(cursorLabel);

	//注册地图上的事件
	map.addEventListener("zoomend",resetMapZoom);
	map.addEventListener("dragend",resetMapCenter);
	map.addEventListener("moveend",resetMapCenter)

	overview_control.addEventListener("viewchanged",function(){
		if(config.overview_control.isopen){
			$("input[name='overview_style']:eq(0)").removeAttr("checked");
			$("input[name='overview_style']:eq(1)").prop("checked",'checked');
			$("input[name='overview_style']:eq(1)").attr("checked",'checked');
			config.overview_control.isopen = false;
			return;
		}else{
			$("input[name='overview_style']:eq(1)").removeAttr("checked");
			$("input[name='overview_style']:eq(0)").prop("checked",'checked');
			$("input[name='overview_style']:eq(0)").attr("checked",'checked');
			config.overview_control.isopen = true;
			return;
		}
	});
}

//根据当前城市设置地图中心点
function confirmCity(){
	var value = $("#input_city").val();
	var option = {
		onSearchComplete: function(results){
			if (local.getStatus() == BMAP_STATUS_SUCCESS){
				if(results.getCurrentNumPois()){
					// map.centerAndZoom(results.getPoi(0).point,15);
					//map.setCenter(value);
					if(results.getCurrentNumPois()){
						map.setCenter(results.getPoi(0).point,15);
					}
					resetMapCenter();
					exitSetCity(value);
				}
			}else{
				alert("没有检索到您输入的城市");
				exitSetCity();
			}
		}
	};
	var local = new BMap.LocalSearch(map,option);
	local.search(value);
}

function citynameKeyUp(){
	if(window.event.keyCode == 13)
		confirmCity();
}

//根据给定的地址设置地图的中心点
function searchLocation(){
	var site = $("#input_site").val();
	var option = {
		onSearchComplete: function(results){
			if (local.getStatus() == BMAP_STATUS_SUCCESS){
				if(results.getCurrentNumPois()){
					map.centerAndZoom(results.getPoi(0).point,15);
					$("#current_city").text(results.city);
				}
			}else{
				alert("没有检索到您输入的地址");
			}
		}
	};
	var local = new BMap.LocalSearch(map,option);
	local.search(site);
}

function siteKeyUp(){
	if(event.keyCode == 13)
		searchLocation();
}

/*
 * 根据用户输入的值，改变地图容器的长度和宽度
 * value: 用户填写的长度（宽度）值
 * para: 要设置的参数（width或者height） 
 */
function resizeMap(){
	var thsEvent =  window.event || arguments.callee.caller.arguments[0];
	if(thsEvent.keyCode == 13){
		var size = map.getSize(),
			w = size.width,
			h = size.height;

		var width = parseInt($("#input_map_width").val());
		var height =  parseInt($("#input_map_height").val());

		// 检测用户输入，并转换为整数
		if($.isNumeric(width) && $.isNumeric(height)){
			if((MIN_WIDTH <= width && width <= MAX_WIDTH) && (MIN_HEIGHT <= height && height <= MAX_HEIGHT)){
				$("#map_container").css("width",width+2);
				$("#map_container").css("height",height+2);

				config.container_height = height;
				config.container_width = width;
				map.panBy((width-w)/2,(height-h)/2);
				setTimeout(function(){resetMapCenter();},500);
			}else{
				alert("宽度需要是 100 - 700 之间的整数,高度需要是100 - 550 之间的整数");
				$("#input_map_width").val(config.container_width);
				$("#input_map_height").val(config.container_height);
			}
		}
	}
}


//在配置项中重新设定地图中心点
function resetMapCenter(){
	var point = map.getCenter();
	var x = point.lng, y = point.lat;
	$("#input_x").text(x);
	$("#input_y").text(y);
	config.center_point = point;
}

//在配置项中重新设定地图的zoom值
function resetMapZoom(){
	var zoom = map.getZoom();
	$("#input_zoom").text(zoom);
	config.zoom = zoom;
	resetMapCenter();
}

// 添加或者删除navigation控件
function addNavControl(){
	if($("#input_add_nav").is(":checked")){
		map.addControl(nav_control);
		config.nav_control.added = true;
	}else{
		map.removeControl(nav_control);
		config.nav_control.added = true;
	}
}

//添加或者删除比例尺控件
function addScaleControl(){
	if($("#input_add_scale").is(":checked")){
		map.addControl(scale_control);
		config.scale_control.added = true;
	}else{
		map.removeControl(scale_control);
		config.scale_control.added = false;
	}
}

//添加或者删除缩略图控件
function addOverviewControl(){
	if($("#input_add_overview").is(":checked")){
		map.addControl(overview_control);
		config.overview_control.added = true;
	}else{
		map.removeControl(overview_control);
		config.overview_control.added = false;
	}
}

//设定或者取消设定地图的滚轮放大事件
function resetScrollWhellZoom(){
	if($("#cb1").is(":checked")){
		map.enableScrollWheelZoom();
		config.enableScrollWheelZoom = true;
	}else{
		map.disableScrollWheelZoom();
		config.enableScrollWheelZoom = false;
	}
}
//设定或者取消设定地图的键盘移动事件
function resetKeyboard(){
	if($("#cb2").is(":checked")){
		map.enableKeyboard();
		config.enableKeyboard = true;
	}else{
		map.disableKeyboard();
		config.enableKeyboard = false;
	}
}
//设定或者取消设定地图的拖拽事件
function resetDragging(){
	if($("#cb3").is(":checked")){
		map.enableDragging();
		config.enableDragging = true;
	}else{
		map.disableDragging();
		config.enableDragging = false;
	}
}
//设定或者取消设定地图的双击放大事件
function resetDoubleClickZoom(){
	if($("#cb4").is(":checked")){
		map.enableDoubleClickZoom();
		config.enableDoubleClickZoom = true;
	}else{
		map.disableDoubleClickZoom();
		config.enableDoubleClickZoom = false;
	}
}

//设定navigation控件的样式
function resetNavType(){
	var type = $("input[name='nav_style']:checked").attr("value");
	switch(type){
		case "BMAP_NAVIGATION_CONTROL_LARGE" :  nav_control.setType(BMAP_NAVIGATION_CONTROL_LARGE);
												config.nav_control.type = BMAP_NAVIGATION_CONTROL_LARGE;
												break;
		case "BMAP_NAVIGATION_CONTROL_SMALL" :  nav_control.setType(BMAP_NAVIGATION_CONTROL_SMALL);
												config.nav_control.type = BMAP_NAVIGATION_CONTROL_SMALL;
												break;
		case "BMAP_NAVIGATION_CONTROL_PAN"   :  nav_control.setType(BMAP_NAVIGATION_CONTROL_PAN);
												config.nav_control.type = BMAP_NAVIGATION_CONTROL_PAN;
												break;
		case "BMAP_NAVIGATION_CONTROL_ZOOM"   : nav_control.setType(BMAP_NAVIGATION_CONTROL_ZOOM);
												config.nav_control.type = BMAP_NAVIGATION_CONTROL_ZOOM;
												break;
	}
}

//设定navigation控件的位置
function resetNavPos(){
	var posStr = $("input[name='nav_pos']:checked").attr("value");
	nav_control.setAnchor(controlPos[posStr]);
	config.nav_control.anchor = posStr;
}

//设定缩略图控件的位置
function resetOverviewPos(){
	var posStr = $("input[name='overview_pos']:checked").attr("value");
	overview_control.setAnchor(controlPos[posStr]);
	config.overview_control.anchor = posStr;
}

//设定比例尺控件的位置
function resetScalePos(){
	var posStr = $("input[name='scale_pos']:checked").attr("value");
	scale_control.setAnchor(controlPos[posStr]);
	config.scale_control.anchor = posStr;
}
//设定比例尺控件的样式
function resetScaleUnit(value){
	scale_control.setUnit(scaleUnit[value]);
	config.scale_control.type = value;
}

function resetView(){
	overview_control.changeView();
	//config.overview_control.isopen ? config.overview_control.isopen = false : config.overview_control.isopen = true;
}

// 鼠标提示信息的跟随处理
function cursorLabelMove(e){
	cursorLabel.setPosition(e.point);
}

//显示鼠标提示信息
function cursorLableShow(){
	cursorLabel.show();
}

//隐藏鼠标提示信息
function cursorLabelHide(){
	cursorLabel.hide();
}

//根据当前绘制的标注类型来设置鼠标的样式
function setCursor(overlayType){
	if(overlayType == "ol_marker"){
		var cursorUrl = "url('./images/" + overlayType + ".cur')";
		map.setDefaultCursor(cursorUrl+','+cursorStyle[overlayType]);
	}else{
		map.setDefaultCursor(cursorStyle[overlayType]);
	}
	cursorLabel.setContent(labelInfo[overlayType]);
	map.addEventListener("mousemove",cursorLabelMove);
	map.addEventListener("mouseout",cursorLabelHide);
	map.addEventListener("mouseover",cursorLableShow);
}


//添加polyline的具体操作
function addPolyline(){
	initPanel();
	$("#list_polyline").css("display","block");

	//如果click已经有事件处理函数，先清除掉
	if(clickHandler)
		exitDrawing(clickHandler);

	map.setDefaultCursor(cursorStyle["ol_polygen"]);
	var polyPoint = [];
	var polyline = null;

	/*
	 * click事件的监听事件
	 * 如果没有初始化polyline变量，则初始化polyline并添加到地图
	 * 如果已经初始化了polyline变量，则将click的经纬度加到polyline 的路径中，并重新设定设定polyline的path
	 */
	function addPolyClickHandler(e){
		cursorLabel.setContent(labelInfo["drawing_line"]);

		var point = e.point;
		polyPoint.push(point);
		
		if(!polyline){
			polyline = new BMap.Polygon(polyPoint, {
				strokeColor:polyDefaultStyle.strokeColor,
				strokeWeight:polyDefaultStyle.strokeWeight,
				strokeOpacity:polyDefaultStyle.strokeOpacity});
			polyline.setPath(polyPoint);
			map.addOverlay(polyline);
			polyPoint.length++;
		}else{
			polyline.setPath(polyPoint);
			
		}
		
	}
	
	/*
	 * 双击(dbclick)事件的监听事件
	 * 清除为绘制polyline添加的几个监听函数
	 * 将polyline变量保存到config变量中
	 */
	function addPolyDdclickHandler(e){
		
		exitDrawing(addPolyClickHandler);
		if(polyline){
			polyline.addEventListener("mouseover",function(e){
				polyline.enableEditing();
			});
			polyline.addEventListener("mouseout",function(e){
				polyline.disableEditing();
			});
			config.polyline_array.push(polyline);
			config.polyline_config.push({path:polyline.getPath(),
				strokeColor:polyDefaultStyle.strokeColor,
				strokeWeight:polyDefaultStyle.strokeWeight,
				strokeOpacity:polyDefaultStyle.strokeOpacity});
			endDrawingPolyline();
		}
		
		map.removeEventListener("click",addPolyClickHandler);
		map.removeEventListener("mousemove",polyMoveHandler);
		map.removeEventListener("dblclick",addPolyDdclickHandler);
		map.removeEventListener("rightclick",polyRemove);

		//如果有允许双击放大地图，则重新加上
		setTimeout(function(){
			if(config.enableDoubleClickZoom)
				map.enableDoubleClickZoom();
		},1000);
		
		console.log(e.overlay.ro)
	}

	//鼠标移动事件的监听函数
	function polyMoveHandler(e){
		if(!polyline)
			return;
		if(polyPoint.length > 0){
			polyPoint[polyPoint.length-1] = e.point;
			//map.setDefaultCursor(cursorStyle["ol_polygen"]);
			cursorLabel.show();
			cursorLabel.setPosition(e.point);
			polyline.setPath(polyPoint);
		}
	}

	//取消绘制折线
	function polyRemove(e){
		map.removeOverlay(polyline);
		exitDrawing(addPolyClickHandler);

		//如果有允许双击放大地图，则重新加上
		setTimeout(function(){
			if(config.enableDoubleClickZoom)
				map.enableDoubleClickZoom();
		},1000);
	}

	clickHandler = addPolyClickHandler;

	map.disableDoubleClickZoom();
	map.addEventListener("click",addPolyClickHandler);
	map.addEventListener("mousemove",polyMoveHandler);
	map.addEventListener("dblclick",addPolyDdclickHandler);
	map.addEventListener("rightclick",polyRemove);
}

/*
 * 鼠标双击结束绘制之后的操作
 * 在左侧添加折线编辑的panel
 */
function endDrawingPolyline(){
	$("#tip2").css("display","none");

	var target = $("#list_polyline .panel.show");
		target.prev().find(".editing").removeClass("hidden").addClass("show");
		target.removeClass("show").addClass("hidden");

	if(NUM_POLYLINE < MAX_POLYLINE_NUM){

		NUM_POLYLINE ++ ;
		var dom = 	'<span class = "editing r hidden" onclick = "deletePolyline(' + config.polyline_array.length + ')">删除</span><span class = "editing r hidden" onclick = "editPolyline('+NUM_POLYLINE+')">编辑| </span></div>'
				+	'<div class = "panel l show" style="margin-top:-6px">'
				+	'<div class= "l" style = "width:221px;border:#d6d6d6 solid 1px; ">'
				+		'<div class = "l" style = "width : 100%; margin-bottom:5px;">'
				+			'<div class= "l" style = "height: 20px;width:60px; line-height:20px; cursor:pointer;">宽 度：</div>'
				+			'<div class= "l" style = "height: 20px;width:30px;text-align:center;">'
				+				'<select name = "strokeWeight" onchange = "resetPolyStyle(this.value,this.name,' + config.polyline_array.length + ')">'
				+					'<option value = "4">中等</option>'
				+					'<option value = "3">较细</option>'
				+					'<option value = "2">最细</option>'
				+					'<option value = "5">较粗</option>'
				+					'<option value = "6">最粗</option>'
				+				'</select>'
				+			'</div>'
				+		'</div>'
				+		'<div class = "l" style = "width : 100%; margin-bottom:5px;">'
				+			'<div class= "l" style = "height: 20px;width:60px; line-height:20px; cursor:pointer;">颜 色：</div>'
				+			'<div class= "l" style = "height: 20px;width:30px;text-align:center;">'
				+				'<select name = "strokeColor" onchange = "resetPolyStyle(this.value,this.name,' + config.polyline_array.length + ')">'
				+					'<option value = "#f00">红色</option>'
				+					'<option value = "#f0f">紫色</option>'
				+					'<option value = "#0ff">青色</option>'
				+					'<option value = "#000">黑色</option>'
				+					'<option value = "#00f">蓝色</option>'
				+					'<option value = "#0f0">绿色</option>'
				+				'</select>'
				+			'</div>'
				+		'</div>'
				+		'<div class = "l" style = "width : 100%; margin-bottom:5px;">'
				+			'<div class= "l" style = "height: 20px;width:60px; line-height:20px; cursor:pointer;">透明度：</div>'
				+			'<div class= "l" style = "height: 20px;width:30px;text-align:center;">'
				+				'<select name = "strokeOpacity" onchange = "resetPolyStyle(this.value,this.name,' + config.polyline_array.length +')">'
				+					'<option value = "0.6">中等</option>'
				+					'<option value = "0.4">高透明</option>'
				+					'<option value = "0.8">低透明</option>'
				+					'<option value = "1">不透明</option>'
				+				'</select>'
				+			'</div>'
				+		'</div>'
				+		'<div class="l"><input type="checkbox" class="setDefaultPoly"/><span>保存为默认值</span></div>'
				+		'<div class = "l" style = "width : 100%; padding:15px 36px 5px 20px">'
				+			'<input class = "btn_gray" type = "button" value = "保 存" onclick = "savePlEditing(' + config.polyline_array.length + ')"/>'
				+			'<input class = "btn_gray" type = "button" value = "取 消" onclick = "exitPlEditing(' + config.polyline_array.length + ')"/>'
				+		'</div>'
				+	'</div>'
				+	'<p style = "margin-top:10px">(提示：请保存未保存的线标记，未保存的线标记在获取代码时内容不是最新的！)</p>'
				+'</div>';

		$("#list_polyline").append(dom);
		var target = $("#list_polyline .panel.show");
		target.find("select[name='strokeColor']").val(polyDefaultStyle.strokeColor);
		target.find("select[name='strokeOpacity']").val(polyDefaultStyle.strokeOpacity);
		target.find("select[name='strokeWeight']").val(polyDefaultStyle.strokeWeight);
	}else{
		$("#warning_polyline").css("display","block");
		window.setTimeout(function(){ $("#warning_polyline").css("display","none"); },2000);
	}
}

// 删除折线
function deletePolyline(index){
	config.polyline_array[index-1].hide();
	config.polyline_array[index-1] = null;
	config.polyline_config[index-1] = null;
	NUM_POLYLINE--;
	$("#list_polyline .bg_item:eq(" + (index-1) + ")").addClass("hidden");
}

//编辑折线之前，在左侧添加折线的编辑面板
function editPolyline(index){
	var target = $("#list_polyline .panel.show");
	target.prev().find(".editing").removeClass("hidden").addClass("show");
	target.removeClass("show").addClass("hidden");

	config.polyline_array[index-1].enableEditing();
	showEditPanel("list_polyline",index-1);
}

//保存对折线的操作
function savePlEditing(index){
	var target = $("#list_polyline .panel.show");
	var pl = config.polyline_array[index-1];
	hideEditPanel("list_polyline",index-1);
	hideUnsavedLabel("list_polyline",index-1);
	config.polyline_config[index-1].strokeOpacity = pl.getStrokeOpacity();
	config.polyline_config[index-1].strokeColor = pl.getStrokeColor();
	config.polyline_config[index-1].strokeWeight = pl.getStrokeWeight();
	if(target.find(".setDefaultPoly").is(":checked")){
		target.find("select").each(function(){
			polyDefaultStyle[$(this).attr("name")] = $(this).val();
		});
	}
}

//设定折线的样式（颜色、透明度、粗细，以及是否将折线的样式设置为绘制的默认样式）
function resetPolyStyle(value,type, index){
	showUnsavedLabel("list_polyline",index-1);
	var pl = config.polyline_array[index-1];
	switch(type){
		case "strokeWeight" : 	pl.setStrokeWeight(value);
								return;
		case "strokeColor"  :   pl.setStrokeColor(value);
								return;
		case "strokeOpacity": 	pl.setStrokeOpacity(value);
								return;
	}
}

function exitPlEditing(index){
	var line = config.polyline_array[index-1];
	var target = $("#list_polyline .panel.show");
	target.find("select[name='strokeColor']").val(config.polyline_config[index-1].strokeColor);
	target.find("select[name='strokeOpacity']").val(config.polyline_config[index-1].strokeOpacity);
	target.find("select[name='strokeWeight']").val(config.polyline_config[index-1].strokeWeight);

	hideEditPanel("list_polyline",index-1);
	hideUnsavedLabel("list_polyline",index-1);
	line.disableEditing();
	line.setStrokeOpacity(config.polyline_config[index-1].strokeOpacity);
	line.setStrokeWeight(config.polyline_config[index-1].strokeWeight);
	line.setStrokeColor(config.polyline_config[index-1].strokeColor);
}


//退出标注的绘制。需要清除掉map上click的处理函数。并把鼠标设置为默认的样式
function exitDrawing(handlerToRemove){
	cursorLabel.hide();
	map.setDefaultCursor(cursorStyle["default"]);
	map.removeEventListener("click",handlerToRemove);
	map.removeEventListener("mouseout",cursorLabelHide);
	map.removeEventListener("mouseover",cursorLableShow);
	$("#add_overlay ul li").removeClass("ol_present");
}

