var cityId = 0;
//var isSiteId = false;
var controlName = "codeOpt";
if(isSiteId){
	controlName = "idOpt";
}
$(document).ready(function() {
	// 初始化省市区下拉框
	$("#addr_control").citySelect({
		prov: "",
		city: "",
		dist: "",
		nodata: "none"
	});
	inputS(".j-sel-input",".all-area1");
	inputS(".j-sel-input2",".all-area2");
	selectS(".all-area1");
	selectS(".all-area2");
	// 省改变
	$('#addr_control .prov').change(function(){
		$('#cityLable').show();
		$('#distLable').hide();

		//更新站点下拉框
		getSiteListByAddr();
	});
	// 市改变
	$('#addr_control .city').change(function(){
		$('#distLable').show();
		//更新站点下拉框
		getSiteListByAddr();
	}) ;
	// 区改变
	$('#addr_control .dist').change(function(){
		//更新站点下拉框
		getSiteListByAddr();
	});
	//绘制电子围栏 -- 更改站点
	//扫描运单号--把快递分派给派件员--边输入边改变
	$("#areaCode").on('input',function(e){
		getSiteListByAddr();
		$(".all-area2").show();
	});
});
function getSiteListByAddr(){
	$('#areaCode').removeAttr("disabled");
	$("#options").html("");
	$.ajax({
		type : "GET",  //提交方式
		url : siteUrl,//路径
		data : {
			"prov" : $("#addr_control .prov").val(),
			"city" :  $("#addr_control .city").val(),
			"area" :  $("#addr_control .dist").val(),
			"siteName" :  $("#areaCode").val().replace("全部", "")
		},//数据，这里使用的是Json格式进行传输
		success : function(data) {//返回数据
			if (data != null ||data.length > 0){//全部
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
	if(optionList != null){
		ulObj.append("<li><label class='f12 linputC'><input type='checkbox' name='" + controlName + "' value=''><b>全部</b></label></li>");
		if(isSiteId){//取得站点id
			optionList.forEach(function(option){
				ulObj.append(getOneOption(option.id, option.name));
			});
		}else{
			optionList.forEach(function(option){
				ulObj.append(getOneOption(option.code, option.name));
			});
		}

		selectS(".all-area2");
	}

}

function getOneOption(id, name){
	var listr = "<li><label class='f12 linputC'><input type='checkbox' name='" + controlName + "' value='" + id + "'><b>";
	listr += name + "</b></label></li>";
	return listr;
}

// S 点击input
function inputS(clickWho,showWho){
	$(clickWho).on("click",function(e){
		e.stopPropagation();
		$(showWho).toggle();
		$(clickWho).toggleClass("bc");
		$(clickWho).parents(".row").siblings().find(".all-area").hide();
		$(clickWho).parents(".row").siblings().find("。c-sel").removeClass("bc");
	});
	$(showWho).on("click",function(e){
		e.stopPropagation();
	});
	$(document).on("click",function(){
		$(showWho).hide();
		$(clickWho).removeClass("bc");
	});
}
// E 点击input
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
		var csel=$(selectSp).siblings(".c-sel");
		var clen=csel.find(".cityshow li").length;
		if(clen == 0){
			csel.find(".j-empty").html("请选择");
		}else{
			csel.find(".j-empty").html("　");
		};
	});
}
function getAreaCodeStr(name){
	if(name == null){
		name = controlName;
	}
	areaCodes = [];
	$("input[name='" + name + "']:checked").each(function(){
		areaCodes.push(this.value.replace("全部", ""));
	});
	return areaCodes.join(",");
}
function selectAll(obj){
	$("input[name='" + obj.name + "']").prop("checked", obj.checked);
}