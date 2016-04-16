/*
 * created by zhuqiao zhuqiao@baidu.com
 * date: 2014-12-03
 */
var MAX_MARKER_NUM = 10,
	MAX_LABEL_NUM = 10,
	MAX_POLYLINE_NUM = 10;

//地图容器的最大（最小）宽度和长度
var MIN_WIDTH = 100,
	MIN_HEIGHT = 100,
	MAX_WIDTH = 700,
	MAX_HEIGHT = 550;

//枚举地图控件的位置
var controlPos = {
	"BMAP_ANCHOR_TOP_RIGHT" : BMAP_ANCHOR_TOP_RIGHT,
	"BMAP_ANCHOR_TOP_LEFT" : BMAP_ANCHOR_TOP_LEFT,
	"BMAP_ANCHOR_BOTTOM_RIGHT" : BMAP_ANCHOR_BOTTOM_RIGHT,
	"BMAP_ANCHOR_BOTTOM_LEFT" : BMAP_ANCHOR_BOTTOM_LEFT
};

var scaleUnit = {
	"BMAP_UNIT_METRIC" : BMAP_UNIT_METRIC,
	"BMAP_UNIT_IMPERIAL" : BMAP_UNIT_IMPERIAL
}

//添加标注时，鼠标的label信息
var labelInfo = {
	"ol_marker" : "左键标记，右键退出",
	"ol_polygen" : "左键单击开始画线，双击结束画线，右键退出",
	"ol_label" : "左键标记，右键退出",
	"drawing_line" : "双击结束画线"
};

//鼠标样式
var cursorStyle = {
	"ol_marker" : "hand",
	"ol_polygen" : "crosshair",
	"ol_label" : "text",
	"default" : "auto"
};

//marker图标id对应背景图片的x/y坐标
var markerIconMatch = {
	"11": [0,3],
	"12": [-23,3],
	"13": [-46,3],
	"14": [-69,3],
	"15": [-92,3],
	"16": [-115,3],
	"21": [0,-21],
	"22": [-23,-21],
	"23": [-46,-21],
	"24": [-69,-21],
	"25": [-92,-21],
	"26": [-115,-21],
	"31": [0,-46],
	"32": [-23,-46],
	"33": [-46,-46],
	"34": [-69,-46],
	"35": [-92,-46],
	"36": [-115,-46]
};

var polyDefaultStyle = {
	strokeColor: "#f00",
	strokeOpacity: 0.6,
	strokeWeight: 4
}

/*
 *用于存储地图各个配置项的数据结构
 *包括：地图中心点、地图的监听事件、地图的控件、地图上的覆盖物等信息
 *用于获取代码的时候绘制地图
 */
var config = {
	city: "北京",
	center_point: new BMap.Point(116.403874,39.914889),
	zoom: 12,

	container_width: 700,
	container_height: 550,

	enableScrollWheelZoom: true,
	enableKeyboard: true,
	enableDragging: true,
	enableDoubleClickZoom: true,
	scale_control: {
		added: true,
		anchor: "BMAP_ANCHOR_BOTTOM_LEFT",
		type: "BMAP_UNIT_IMPERIAL"
	},
	nav_control: {
		added: true,
		anchor: "BMAP_ANCHOR_TOP_LEFT",
		type: "BMAP_NAVIGATION_CONTROL_LARGE"
	},
	overview_control: {
		added: true,
		anchor: "BMAP_ANCHOR_BOTTOM_RIGHT",
		isopen: true
	},
	label_array: [],
	label_config: [],
	marker_array: [],
	marker_config: [],
	polyline_config: [],
	polyline_array: []
}