var cityId = 0;
$(document).ready(function() {
	// 初始化省市区下拉框
	$("#addr_control").citySelect({
		prov: "",
		city: "",
		dist: "",
		nodata: "none"
	});
	$(".j-sel-input").on("click",function(e){
		e.stopPropagation();
		$(".all-area").toggle();
	});
	$(".all-area").on("click",function(e){
		e.stopPropagation();
	});
	$(document).on("click",function(){
		$(".all-area").hide();
	});
	selectS(".all-area");
	// 省改变
	$('#addr_control .prov').change(function(){
		$('#cityLable').show();
		$('#distLable').hide();
		updateSite(this.value);
	});
	// 市改变
	$('#addr_control .city').change(function(){
		$('#distLable').show();
		updateSite(this.value);
	}) ;
	// 区改变
	$('#addr_control .dist').change(function(){
		updateSite($('#addr_control .city').val() + "市" + this.value);
	});
	//绘制电子围栏 -- 更改站点
	//扫描运单号--把快递分派给派件员--边输入边改变
	$("#siteName").on('input',function(e){
		getSiteListByAddr();
		$(".all-area").show();
	});
});
//站点列表和站点地图(显示站点和派件员)更新
function updateSite(center){
	//设置地图中心点，并调整地图视野
	capamap.centerAndZoom(center);
	//站点列表和站点地图(显示站点和派件员)更新
	getSiteListByAddr();
}
function getSiteListByAddr(){
	$("#options").html("");
	$('#siteName').removeAttr("disabled");
	$.ajax({
		type : "GET",  //提交方式
		url : siteUrl,//路径
		data : {
			"prov" : $("#addr_control .prov").val(),
			"city" :  $("#addr_control .city").val(),
			"area" :  $("#addr_control .dist").val(),
			"siteName" :  $("#siteName").val().replace("全部", "")
		},//数据，这里使用的是Json格式进行传输
		success : function(data) {//返回数据
			if(data != null){
				//更新站点下拉列表数据
				loadSiteData(data);
			}

		},
		error : function() {
			ioutDiv("服务器繁忙，请稍后再试");
		}
	});
}
//更新站点下拉列表数据
function loadSiteData(optionList){
	var ulObj = $("#optionList");
	//清空数据
	ulObj.html("");
	//为Select追加一个Option(下拉项)
	console.log(optionList);
	if(optionList != null){
		ulObj.append("<li><label class='f12 linputC'><input type='checkbox' name='eachSiteId' value=''><b>全部</b></label></li>");
		optionList.forEach(function(option){
			ulObj.append(getOneOption(option.id, option.name));
		});
		selectS(".all-area");
	}
}
//获得一个选项的html
function getOneOption(id, name){
	var listr = "<li><label class='f12 linputC'><input type='checkbox' name='eachSiteId' value='" + id + "'><b>";
	listr += name + "</b></label></li>";
	return listr;
}
//获得站点多选框的值
function getSiteIdStr(){
	var siteIds = [];
	$('input[name="eachSiteId"]:checked').each(function(){
		siteIds.push(this.value);
	});
	return siteIds.join(",");
}

function getSiteAndUserList(){
	$.ajax({
		type : "GET",  //提交方式
		url : mapDataUrl,//路径
		data : {
			"prov" : $("#addr_control .prov").val(),
			"city" :  $("#addr_control .city").val(),
			"area" :  $("#addr_control .dist").val(),
			"siteIdStr" :  getSiteIdStr(),//站点id集合
		},//数据，这里使用的是Json格式进行传输
		success : function(data) {//返回数据

			capamap.clearOverlays();
			console.log(data);
			if(data != null){
				//更新地图（站点和派件员）
				showSiteAndUsers(data.siteList, data.userList);
			}
		},
		error : function() {
			ioutDiv("服务器繁忙，请稍后再试");
		}
	});
}
//站点下拉框选则操作
function selectS(selectSp){
	var sbox=$(selectSp).find(".pv-part li input")
	sbox.on("click",function(){
		var test=$(this).siblings("b").html();
		if($(this).attr("city") == undefined){ $(this).attr("city", window.cityId++);}
		var city = $(this).attr("city");
		var curP=$(this).parents('.l-sel-p').parent().index();
		var shA=$(this).parents(selectSp).siblings(".c-sel").find(".showA");
		if( $('li[city="'+ city +'"]').size()){
			$('li[city="'+ city +'"]').remove();
		}else {
			var test=$(this).siblings("b").html();
			$('<li class="licity" city='+city+'>'+test+'</li>').appendTo(shA.eq(curP).find(".cityshow"));
		};

		if($(".cityshow li:last-child")){
			$(".cityshow li:last-child").removeClass("padR6").siblings().addClass("padR6");
		};
		// 默认提示文字
		var clen=$(".cityshow li").length;
		if(clen == 0){
			$(".j-empty").prop("placeholder","请输入站点名称");
			$(".j-empty").prop("disabled",false)
		}else{
			$(".j-empty").prop("placeholder","");
			$(".j-empty").val("");
			$(".j-empty").prop("disabled",true)
		};
	});
}

function selectAll(obj){
	$("input[name='" + obj.name + "']").prop("checked", obj.checked);
}


