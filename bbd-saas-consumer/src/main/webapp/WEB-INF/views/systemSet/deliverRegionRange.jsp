<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta name="generator" content=
			"HTML Tidy for Windows (vers 14 February 2006), see www.w3.org" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="generator" content="MediaWiki 1.16.1" />
	<title>
		创建地图-百度地图生成器
	</title>
	<meta name="keywords" content="地图生成器" />
	<link rel="search" type="application/opensearchdescription+xml" href=
			"/wiki/map/opensearch_desc.php" title="地图开放平台 (zh-hans)" />
	<link rel="alternate" type="application/atom+xml" title=
			"地图开放平台的Atom订阅" href=
				  "/wiki/map/index.php?title=%E7%89%B9%E6%AE%8A:%E6%9C%80%E8%BF%91%E6%9B%B4%E6%94%B9&amp;feed=atom" />
	<link rel="stylesheet" href=
			"http://dev.baidu.com/wiki/static/skins/common/shared.css?270" media=
				  "screen" type="text/css" />
	<link rel="stylesheet" href=
			"http://dev.baidu.com/wiki/static/skins/common/commonPrint.css?270" media=
				  "print" type="text/css" />
	<link rel="stylesheet" href=
			"css/main.css" media=
				  "screen" type="text/css" />
	<script src="http://dev.baidu.com/wiki/static/skins/common/apibox.js?270"
			type="text/javascript">
	</script>
	<script src="http://dev.baidu.com/wiki/static/skins/common/ajax.js?270"
			type="text/javascript">
	</script>
	<script src="http://dev.baidu.com/wiki/static/skins/common/bapi.js?270"
			type="text/javascript">
	</script>
	<script type="text/javascript" src=
			"http://api.map.baidu.com/api?key=16398096469aa2c92d7b87506732cd90&amp;v=1.1&amp;services=true">
	</script>
	<link rel="stylesheet" type="text/css" href="css/home.css?v=20101122" />
	<script type="text/javascript" src="script/tangram.js"></script>
	<script type="text/javascript" src="script/index.js?v=201012231"></script>
	<script type="text/javascript" src="script/mapext.js"></script>
	<script type="text/javascript" src="script/copy.js"></script>
</head>
<body>
<!--head bg-->
<div class="signBoxCon">
	<div class="body t10">
		<div class="sl_bg bold font14 font_color l">
			<span class="l10">创建电子围栏</span>
		</div>
		<div class="sign_content1 margin_sT5">
			<!--<div class="l color_h" style="width:227px;height:281px;margin:10px 0px 10px 10px;float:left;display:inline-block;">-->
			<table style="width:944px;margin:10px;" id="wxp_zdydt">
				<tr>
					<td style="width:223px;text-align:left;vertical-align:top">
						<div>
							<div class="map_open" onclick="setStates(this,event)" id=
									"mapCenter">
								<div class="map_title_s bg_h2 bold font14" style=
										"margin-top:0" navtype="setMapCenter">
									1.定位中心点
								</div>
								<div class="map_content">
									<ul class="wxp_list1">
										<li>
											<div id="CurrentCityCon">
												<span class="cor_1">当前城市：</span><span id=
																							  "cityName">北京市</span> <span class="cor_2"
																														  onclick="editCurrentCity()">切换</span>
											</div>
											<div id="CurrentCityEdit" class="wxp_lis_con2" style=
													"display:none;">
												<input type="text" class="inputT_2 inputT_4" value=
														"请输入城市名称" onfocus=
															   "input_focus(this,'请输入城市名称','')"
													   onblur=
															   "input_blur(this,'请输入城市名称','')" id=
															   "CurrentCityInput" title=
															   "请输入城市中文名称" />
												<div class="dwzxd_box2 dwzxd_box3">
                              <span onclick="updateCurrentCity()">确
                              定</span>
												</div>
												<div class="dwzxd_box2 dwzxd_box3">
													<span onclick="canelCurrentCity()">取 消</span>
												</div>
											</div>
										</li>
										<li id="cityNameTip" style=
												"display:none;text-align:center;color:#f00"></li>
										<li>
											<input type="text" class="inputT_2" id="searchMap"
												   value="输入地名、大厦快速定位" onfocus=
														   "input_focus(this,'输入地名、大厦快速定位','')"
												   onblur=
														   "input_blur(this,'输入地名、大厦快速定位','')" />
											<div class="dwzxd_box2">
												<span onclick="getlikeArea()">查找</span>
											</div>
										</li>
										<li>当前地图中心点经纬度：
										</li>
										<li class="input1">
											<b>X：</b><input type="text" id="mapCenterPointX"
															readonly="true" />
										</li>
										<li class="input1">
											<b>Y：</b><input type="text" id="mapCenterPointY"
															readonly="true" />
										</li>
										<li class="input2">当前地图级别：<input type=
																				 "text" id="mapCenterLevel" readonly="true" />
										</li>
									</ul>
								</div>
							</div>
							<div class="map_close" style="margin-top:10px;" onclick=
									"setStates(this,event)" id="mapSet">
								<div class="map_title_s bg_h2 bold font14" navtype=
										"setMapInfo">
									2.设置地图
								</div>
								<div class="map_content">
									<ul class="wxp_list2">
										<li class="h5 wxp_li_h1">地图尺寸
										</li>
										<li class="wxp_li_s1">宽度：<input type="text"
																		class="inputT_1" onchange="changeMapSize(this,'wid')"
																		id="map_width" /><span class="color_1">像素</span>
										</li>
										<li>高度：<input type="text" class="inputT_1"
													  onchange="changeMapSize(this,'hei')" id=
															  "map_height" /><span class="color_1">像素</span>
										</li>
									</ul>
									<ul class="wxp_list2">
										<li class="h5 wxp_li_h2">添加按钮
										</li>
										<li class="h6 checkbox">
											<label for="control_nav">地图缩放<input type=
																						"checkbox" id="control_nav" value="nav" /></label>
										</li>
										<li class="radio hei2">
											<span>样式：</span><label for=
																		   "nav_style_LARGE"><input type="radio" name=
												"nav_style" id="nav_style_LARGE" cl_type="nav" value=
																											"LARGE" />标准</label><label for=
																																			   "nav_style_SMALL"><input type="radio" name=
												"nav_style" id="nav_style_SMALL" cl_type="nav" value=
																																												"SMALL" />精简</label><br />
											<label for="nav_style_PAN"><input type="radio" name=
													"nav_style" id="nav_style_PAN" cl_type="nav" value=
																					  "PAN" />平移</label><label for=
																													   "nav_style_ZOOM"><input type="radio" name="nav_style"
																																			   id="nav_style_ZOOM" cl_type="nav" value=
																																					   "ZOOM" />缩放</label>
										</li>
										<li class="radio hei2">
											<span>位置：</span><label for=
																		   "nav_place_TOP_LEFT"><input type="radio" name=
												"nav_place" id="nav_place_TOP_LEFT" cl_type="nav"
																									   value="TOP_LEFT" />左上</label><label for=
																																				   "nav_place_TOP_RIGHT"><input type="radio" name=
												"nav_place" id="nav_place_TOP_RIGHT" cl_type="nav"
																																												value="TOP_RIGHT" />右上</label><br />
											<label for="nav_place_BOTTOM_LEFT"><input type=
																							  "radio" name="nav_place" id="nav_place_BOTTOM_LEFT"
																					  cl_type="nav" value=
																							  "BOTTOM_LEFT" />左下</label><label for=
																																	   "nav_place_BOTTOM_RIGHT"><input type="radio" name=
												"nav_place" id="nav_place_BOTTOM_RIGHT" cl_type="nav"
																																									   value="BOTTOM_RIGHT" />右下</label>
										</li>
										<li class="h6 checkbox">
											<label for="control_ove"><input type="checkbox" id=
													"control_ove" value="ove" />地图缩略图</label>
										</li>
										<li class="radio hei3">
											<span>状态：</span><label for=
																		   "ove_style_1"><input type="radio" name="overview" id=
												"ove_style_1" value="1" />展开</label><label for=
																								   "ove_style_0"><input type="radio" name="overview" id=
												"ove_style_0" value="0" />收起</label>
										</li>
										<li class="radio hei2">
											<span>位置：</span><label for=
																		   "ove_place_TOP_LEFT"><input type="radio" name=
												"ove_place" id="ove_place_TOP_LEFT" cl_type="ove"
																									   value="TOP_LEFT" />左上</label><label for=
																																				   "ove_place_TOP_RIGHT"><input type="radio" name=
												"ove_place" id="ove_place_TOP_RIGHT" cl_type="ove"
																																												value="TOP_RIGHT" />右上</label><br />
											<label for="ove_place_BOTTOM_LEFT"><input type=
																							  "radio" name="ove_place" id="ove_place_BOTTOM_LEFT"
																					  cl_type="ove" value=
																							  "BOTTOM_LEFT" />左下</label><label for=
																																	   "ove_place_BOTTOM_RIGHT"><input type="radio" name=
												"ove_place" id="ove_place_BOTTOM_RIGHT" cl_type="ove"
																																									   value="BOTTOM_RIGHT" />右下</label>
										</li>
										<li class="h6 checkbox">
											<label for="control_sca">地图比例尺<input type=
																						 "checkbox" id="control_sca" value="sca" /></label>
										</li>
										<li class="radio hei3">
											<span>单位：</span><label for=
																		   "sca_style_METRIC"><input type="radio" name="scale"
																									 id="sca_style_METRIC" cl_type="sca" value=
																											 "METRIC" />公制</label><label for=
																																				 "sca_style_IMPERIAL"><input type="radio" name="scale"
																																											 id="sca_style_IMPERIAL" cl_type="sca" value=
																																													 "IMPERIAL" />英制</label>
										</li>
										<li class="radio hei2">
											<span>位置：</span><label for=
																		   "sca_place_TOP_LEFT"><input type="radio" name=
												"sca_place" id="sca_place_TOP_LEFT" cl_type="sca"
																									   value="TOP_LEFT" />左上</label><label for=
																																				   "sca_place_TOP_RIGHT"><input type="radio" name=
												"sca_place" id="sca_place_TOP_RIGHT" cl_type="sca"
																																												value="TOP_RIGHT" />右上</label><br />
											<label for="sca_place_BOTTOM_LEFT"><input type=
																							  "radio" name="sca_place" id="sca_place_BOTTOM_LEFT"
																					  cl_type="sca" value=
																							  "BOTTOM_LEFT" />左下</label><label for=
																																	   "sca_place_BOTTOM_RIGHT"><input type="radio" name=
												"sca_place" id="sca_place_BOTTOM_RIGHT" cl_type="sca"
																																									   value="BOTTOM_RIGHT" />右下</label>
										</li>
									</ul>
									<ul class="wxp_list2">
										<li class="h5">地图状态
										</li>
										<li class="checkbox hei4">
											<label for="event_scr"><input type="checkbox" id=
													"event_scr" value=
																				  "ScrollWheelZoom" />鼠标滚轮缩放</label>
											<div style="clear:left;"></div><label for=
																						  "event_key"><input type="checkbox" id="event_key"
																											 value="Keyboard" />键盘方向移动</label><br />
											<label for="event_dra"><input type="checkbox" id=
													"event_dra" value=
																				  "Dragging" />鼠标拖动地图</label><br />
											<label for="event_dou"><input type="checkbox" id=
													"event_dou" value=
																				  "DoubleClickZoom" />鼠标双击放大</label>
										</li>
									</ul>
								</div>
							</div>
							<div class="map_close" style="margin-top:10px;" onclick=
									"setStates(this,event)" id="mapPoint">
								<div class="map_title_s bg_h2 bold font14" navtype="sign">
									3.添加标注
								</div>
								<div class="map_content">
									<div class="wxp_nav_1">
										<ul class="wxp_nav_bg1" id="sign_nav_bg">
											<li class="nav1" onclick=
													"changeSignNav(this,1,event)" usertagpanl="iw" title=
														"点标记">标记地点
											</li>
											<li class="nav2" onclick=
													"changeSignNav(this,2,event)" usertagpanl="pl" title=
														"线标记">手绘路线
											</li>
											<li class="nav3" onclick=
													"changeSignNav(this,3,event)" usertagpanl="lb" title=
														"文字标记">文字备注
											</li>
										</ul>
									</div>
									<div id="sign_content_1" style=
											"position:relative;overflow:hidden;">
										<p class="wxp_con_2" id="pointIconCon">
											<a href="javascript:exitPtIcon()" class=
													"null">̨ߔ回</a><br />
											<a class="on" onclick="setPtIcon(11);return false"
											   href="javascript:void(0);"><img class="sp_s sp_11"
																			   src="images/transparent.gif" /></a><a onclick=
																															 "setPtIcon(12);return false" href=
																															 "javascript:void(0);"><img class="sp_s sp_12" src=
												"images/transparent.gif" /></a><a onclick=
																						  "setPtIcon(13);return false" href=
																						  "javascript:void(0);"><img class="sp_s sp_13" src=
												"images/transparent.gif" /></a><a onclick=
																						  "setPtIcon(14);return false" href=
																						  "javascript:void(0);"><img class="sp_s sp_14" src=
												"images/transparent.gif" /></a><a onclick=
																						  "setPtIcon(15);return false" href=
																						  "javascript:void(0);"><img class="sp_s sp_15" src=
												"images/transparent.gif" /></a><a onclick=
																						  "setPtIcon(16);return false" href=
																						  "javascript:void(0);"><img class="sp_s sp_16" src=
												"images/transparent.gif" /></a><a onclick=
																						  "setPtIcon(21);return false" href=
																						  "javascript:void(0);"><img class="sp_s sp_21" src=
												"images/transparent.gif" /></a><a onclick=
																						  "setPtIcon(22);return false" href=
																						  "javascript:void(0);"><img class="sp_s sp_22" src=
												"images/transparent.gif" /></a><a onclick=
																						  "setPtIcon(23);return false" href=
																						  "javascript:void(0);"><img class="sp_s sp_23" src=
												"images/transparent.gif" /></a><a onclick=
																						  "setPtIcon(24);return false" href=
																						  "javascript:void(0);"><img class="sp_s sp_24" src=
												"images/transparent.gif" /></a><a onclick=
																						  "setPtIcon(25);return false" href=
																						  "javascript:void(0);"><img class="sp_s sp_25" src=
												"images/transparent.gif" /></a><a onclick=
																						  "setPtIcon(26);return false" href=
																						  "javascript:void(0);"><img class="sp_s sp_26" src=
												"images/transparent.gif" /></a><a onclick=
																						  "setPtIcon(31);return false" href=
																						  "javascript:void(0);"><img class="sp_s sp_31" src=
												"images/transparent.gif" /></a><a onclick=
																						  "setPtIcon(32);return false" href=
																						  "javascript:void(0);"><img class="sp_s sp_32" src=
												"images/transparent.gif" /></a><a onclick=
																						  "setPtIcon(33);return false" href=
																						  "javascript:void(0);"><img class="sp_s sp_33" src=
												"images/transparent.gif" /></a><a onclick=
																						  "setPtIcon(34);return false" href=
																						  "javascript:void(0);"><img class="sp_s sp_34" src=
												"images/transparent.gif" /></a><a onclick=
																						  "setPtIcon(35);return false" href=
																						  "javascript:void(0);"><img class="sp_s sp_35" src=
												"images/transparent.gif" /></a><a onclick=
																						  "setPtIcon(36);return false" href=
																						  "javascript:void(0);"><img class="sp_s sp_36" src=
												"images/transparent.gif" /></a>
										</p>
										<p id="signPointTip" style="display:none" class=
												"signTip">
											Tip
										</p>
										<ul class="wxp_lis_1" id="signPointList">
											<li id="signiwNoListTip">
												还没有在地图上添加点标记，点击上面的按钮开始在地图上添加点标记
											</li>
										</ul>
										<div style="color:#808080;display:none;padding:5px;"
											 id="signiwSaveTip">
											(提示：请保存未保存的点标记，未保存的点标记在获取代码时内容不是最新的！)
										</div>
									</div>
									<div id="sign_content_2" style="display:none;">
										<p id="signPolyLineTip" style="display:none" class=
												"signTip">
											Tip
										</p>
										<ul class="wxp_lis_1" id="signPolylineList">
											<li id="signplNoListTip">
												还没有在地图上添加线标记，点击上面的按钮开始在地图上添加线标记
											</li>
										</ul>
										<div style="color:#808080;display:none;padding:5px;"
											 id="signplSaveTip">
											(提示：请保存未保存的线标记，未保存的线标记在获取代码时内容不是最新的！)
										</div>
									</div>
									<div id="sign_content_3" style="display:none">
										<p id="signRemarkTip" style="display:none" class=
												"signTip">
											Tip
										</p>
										<ul class="wxp_lis_1" id="signRemarkList">
											<li id="signlbNoListTip">
												还没有在地图上添加文字标记，点击上面的按钮开始在地图上添加文字标记
											</li>
										</ul>
										<div style="color:#808080;display:none;padding:5px;"
											 id="signlbSaveTip">
											(提示：请保存未保存的文字标记，未保存的文字标记在获取代码时内容不是最新的！)
										</div>
									</div>
								</div>
							</div>
						</div>
					</td>
					<td style="vertical-align:top">
						<div id="MapContent" style="width:697px;height:550px;float:left;border:solid 1px #c8c8c8;margin-left:12px;">
						</div>
					</td>
				</tr>
			</table>
		</div>
	</div>
</div>
</body>
</html>
