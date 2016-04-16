/*
 * created by zhuqiao zhuqiao@baidu.com
 * date: 2014-12-11
 */

var clip = null; //flash原件

// 获取代码
function getCode(){

	var addMapOverlay = addMapControl = setMapEvent = '';

	setMapEvent = '		function setMapEvent(){\n';
	if(config.enableScrollWheelZoom)
		setMapEvent += '			map.enableScrollWheelZoom();\n';
	if(config.enableKeyboard)
		setMapEvent += '			map.enableKeyboard();\n';
	if(config.enableDragging)
		setMapEvent += '			map.enableDragging();\n';
	if(config.enableDoubleClickZoom)
		setMapEvent += '			map.enableDoubleClickZoom()\n';
		setMapEvent += '		}\n';

	addMapControl ='		//向地图添加控件\n		function addMapControl(){\n';
	if(config.scale_control.added){
		addMapControl += '			var scaleControl = new BMap.ScaleControl({anchor:' + config.scale_control.anchor +'});\n\
			scaleControl.setUnit(' + config.scale_control.type + ');\n\
			map.addControl(scaleControl);\n';
	}
	if(config.nav_control.added){
		addMapControl += '			var navControl = new BMap.NavigationControl({anchor:' + config.nav_control.anchor + ',type:' + config.nav_control.type + '});\n\
			map.addControl(navControl);\n';
	}
	if(config.overview_control.added){
		addMapControl += '			var overviewControl = new BMap.OverviewMapControl({anchor:' + config.overview_control.anchor + ',isOpen:' + config.overview_control.isopen + '});\n\
			map.addControl(overviewControl);\n';
	}
	addMapControl += '		}\n';

	addMapOverlay = '		function addClickHandler(target,window){\n\
			target.addEventListener("click",function(){\n\
				target.openInfoWindow(window);\n\
			});\n\
		}\n';
	addMapOverlay +='		function addMapOverlay(){\n';
	if(config.marker_config.length){
		addMapOverlay +='			var markers = [\n';
		for(var len = 0 ; len < (config.marker_config.length-1); len ++ ){
			if( config.marker_config[len] != null){
				var single = config.marker_config[len];
				addMapOverlay += '				{content:"' + single.content + '",title:"' + single.title 
				+ '",imageOffset: {width:' + single.imageOffset.width + ',height:' + single.imageOffset.height +'}' 
				+ ',position:{lat:' + single.position.lat + ',lng:' + single.position.lng + '}},\n';
			}
		}
		if( config.marker_config[config.marker_config.length-1] != null){
			var single = config.marker_config[len];
			addMapOverlay += '				{content:"' + single.content + '",title:"' + single.title 
			+ '",imageOffset: {width:' + single.imageOffset.width + ',height:' + single.imageOffset.height +'}' 
			+ ',position:{lat:' + single.position.lat + ',lng:' + single.position.lng + '}}\n';
		}

		addMapOverlay += '			];\n\
			for(var index = 0; index < markers.length; index++ ){\n\
				var point = new BMap.Point(markers[index].position.lng,markers[index].position.lat);\n\
				var marker = new BMap.Marker(point,{icon:new BMap.Icon("http://api.map.baidu.com/lbsapi/createmap/images/icon.png",new BMap.Size(20,25),{\n\
					imageOffset: new BMap.Size(markers[index].imageOffset.width,markers[index].imageOffset.height)\n\
				})});\n\
				var label = new BMap.Label(markers[index].title,{offset: new BMap.Size(25,5)});\n\
				var opts = {\n\
					width: 200,\n\
					title: markers[index].title,\n\
					enableMessage: false\n\
				};\n\
				var infoWindow = new BMap.InfoWindow(markers[index].content,opts);\n\
				marker.setLabel(label);\n\
				addClickHandler(marker,infoWindow);\n\
				map.addOverlay(marker);\n\
			};\n'
	}

	if(config.label_array.length){
		addMapOverlay +='			var labels = [\n';
		for(var len = 0; (len < config.label_array.length-1); len ++){
			var label = config.label_config[len];
			if(label != null)
				addMapOverlay += '				{position:{lng:' + label.position.lng + ',lat:' + label.position.lat + '},content:"' + label.content + '"},\n';
		}
		var label = config.label_config[config.label_array.length-1];
		if(label != null){
			addMapOverlay += '				{position:{lng:' + label.position.lng + ',lat:' + label.position.lat + '},content:"' + label.content + '"}\n';
		}
		addMapOverlay += '			];\n\
			for(var index = 0; index < labels.length; index++){\n\
				var opt = { position: new BMap.Point(labels[index].position.lng,labels[index].position.lat )};\n\
				var label = new BMap.Label(labels[index].content,opt);\n\
				map.addOverlay(label);\n\
			};\n';
	}

	if(config.polyline_array.length){
		addMapOverlay +='			var plOpts = [\n';
		for(var len = 0; (len < config.polyline_array.length-1); len++ ){
			var pl = config.polyline_config[len];
			if(pl != null){
				addMapOverlay += '				{strokeColor:"' + pl.strokeColor + '",strokeWeight:"' + pl.strokeWeight
				+ '",strokeOpacity:"' + pl.strokeOpacity + '"},\n';
			}
		}
		var pl = config.polyline_config[config.polyline_array.length-1];
		if(pl != null){
			addMapOverlay += '				{strokeColor:"' + pl.strokeColor + '",strokeWeight:"' + pl.strokeWeight
				+ '",strokeOpacity:"' + pl.strokeOpacity + '"}\n';
		}
		addMapOverlay += '			];\n';
		addMapOverlay += '			var plPath = [\n';
		for(var len = 0; len < config.polyline_array.length; len++){
			var pl = config.polyline_array[len];
			if(pl != null){
				var points = pl.getPath();
				addMapOverlay += '				[\n';
				for(var p_num = 0; p_num < (points.length-1); p_num ++){
					addMapOverlay += '					new BMap.Point(' + points[p_num].lng + ',' + points[p_num].lat + '),\n';			
				}
				addMapOverlay += '					new BMap.Point(' + points[p_num].lng + ',' + points[p_num].lat + ')\n';
				addMapOverlay += '				],\n';
			}
		}

		addMapOverlay += '			];\n\
			for(var index = 0; index < plOpts.length; index++){\n\
				var polyline = new BMap.Polyline(plPath[index],plOpts[index]);\n\
				map.addOverlay(polyline);\n\
			}\n';		
	}
	addMapOverlay +='		}\n';
	
	var html = '<!DOCTYPE html>\n\
<html xmlns="http://www.w3.org/1999/xhtml">\n\
	<head>\n\
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />\n\
		<meta name="keywords" content="百度地图,百度地图API，百度地图自定义工具，百度地图所见即所得工具" />\n\
		<meta name="description" content="百度地图API自定义地图，帮助用户在可视化操作下生成百度地图" />\n\
		<title>百度地图API自定义地图</title>\n\
		<!--引用百度地图API-->\n\
		<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=E4805d16520de693a3fe707cdc962045"></script>\n\
	</head>\n\
	\n\
	<body>\n\
		<!--百度地图容器-->\n\
		<div style="width:'+config.container_width+'px;height:'+config.container_height+'px;border:#ccc solid 1px;font-size:12px" id="map"></div>\n\
		<p style="color:red;font-weight:600">地图生成工具基于百度地图JS api v2.0版本开发，使用请申请密匙。\n\
			<a href="http://developer.baidu.com/map/index.php?title=jspopular/guide/introduction" style="color:#2f83c7" target="_blank">了解如何申请密匙</a>\n\
			<a href="http://lbsyun.baidu.com/apiconsole/key?application=key" style="color:#2f83c7" target="_blank">申请密匙</a>\n\
		</p>\n\
	</body>\n\
	<script type="text/javascript">\n\
		//创建和初始化地图函数：\n\
		function initMap(){\n\
			createMap();//创建地图\n\
			setMapEvent();//设置地图事件\n\
			addMapControl();//向地图添加控件\n\
			addMapOverlay();//向地图添加覆盖物\n\
		}\n\
		function createMap(){ \n\
			map = new BMap.Map("map"); \n\
			map.centerAndZoom(new BMap.Point('+config.center_point.lng+','+config.center_point.lat+"),"+config.zoom + ');\n\
		}\n'+ setMapEvent + addMapOverlay + addMapControl +
'		var map;\n\			initMap();\n\
	</script>\n\
</html>';

	return html;
}

function sel(s) {
	var t = document.getElementById('code'), v = t.value, start = v.indexOf(s), end;
	if (start == -1) return;//找不到内容则推出
	end = start + s.length;
	if (typeof t.createTextRange != 'undefined') { //IE
		var browser=navigator.appName 
		var b_version=navigator.appVersion 
		var version=b_version.split(";"); 
		var trim_Version=version[1].replace(/[ ]/g,""); 
		var r = t.createTextRange();
		//先将光标重合
		r.moveStart('character', 0);
		r.moveEnd('character', 0);
		r.collapse(true);
		if(browser=="Microsoft Internet Explorer" && (trim_Version=="MSIE7.0" || trim_Version=="MSIE8.0" )){
			r.moveEnd('character', end-8);
			r.moveStart('character', start-8);
			r.select();
		}else{
			r.moveEnd('character', end);
			r.moveStart('character', start);
			r.select();
		}
	}
	else if (typeof t.selectionStart!='undefined') { //firefox,chrome
		t.selectionStart = start;
		t.selectionEnd=end;
	}
}

//在新窗口预览地图
function previewPage(){
	var html = getCode();
	var newWindow = window.open('', "_blank", '');
	newWindow.document.open('text/html', 'replace');
	newWindow.document.write(html);
	if(document.all) newWindow.document.execCommand('Refresh');
	newWindow.document.close();
	sel("您的密匙");
}

function showCode(){
	var html = getCode();
	$(".showCode").css("display","block");
	html = html.replace(/\t/g,"  ");
	html = html.replace("E4805d16520de693a3fe707cdc962045","您的密匙");
	$("#code").val(html);
	
	sel("您的密匙");

	/** 复制功能 **/
	function init() {
		// debugger;
		clip = new ZeroClipboard.Client();
		clip.setHandCursor( true );         
		clip.addEventListener('load', function (client) {
			debugstr("Flash movie loaded and ready.");
		});
		clip.addEventListener('mousedown', function (client) {
			clip.setText(html);
			sel("您的密匙");
			setTimeout("alert('复制成功')",500);
		}); 
		clip.addEventListener('complete', function (client, text) {
			debugstr("Copied text to clipboard: " + text );
		});         
		clip.glue('d_clip_button');
	}
	init();
}

