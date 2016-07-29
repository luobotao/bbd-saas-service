var cityId = 0;
$(document).ready(function() {
	// 初始化省市区下拉框
	$("#addr_control").citySelect({
		prov: "",
		city: "",
		dist: "",
		nodata: "none"
	});
	inputS(".j-sel-input",".all-area");
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
// S 点击input
function inputS(clickWho,showWho){
	$(clickWho).on("click",function(e){
		e.stopPropagation();
		$(showWho).toggle();
		$(clickWho).toggleClass("bc");
		$(clickWho).parents(".row").siblings().find(".all-area").hide();
		$(clickWho).parents(".row").siblings().find(".c-sel").removeClass("bc");
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
		//增加属性
		if($(this).attr("city") == undefined){ $(this).attr("city", window.cityId++);}
		//获取上边显示框的一些值 == 用于添加和移除元素
		var city = $(this).attr("city");
		var curP=$(this).parents('.l-sel-p').parent().index();
		var shA=$(this).parents(selectSp).siblings(".c-sel").find(".showA");


		//全选 -- 取消全选
		var isAll = $(this).attr("isAll");

		var showNameUlObj = shA.eq(curP).find(".cityshow");//$("#options");
		if(isAll == "1"){//全选
			//optionArayy.push(this.value.replace("全部", ""));
			//其他checkbox联动
			$("input[name='" + this.name + "']").prop("checked", this.checked);
			//上边框中显示的值
			if(this.checked == true){//选中
				showNameUlObj.html("<li city='1' class='licity'>全部</li>");
			}else{//取消选中
				showNameUlObj.html("");
			}
		}else{//某个具体项
			var checkedCount = $("input[name='" + this.name + "'][isAll='0']:checked").length;//选中的checkbox个数
			var itemCount = $(selectSp).find(".pv-part li").length -1 ;//所有li个数（减去全部）
			console.log("checkedCount==11="+checkedCount+"  itemCount==="+itemCount);
			//上边框中显示的值
			if(this.checked == true){//选中
				if(checkedCount == itemCount ){//全部选中
					//全选checkbox选中
					$("input[name='" + this.name + "'][isAll='1']").prop("checked", this.checked);
					//上边框中显示的值
					showNameUlObj.html("<li city='1' class='licity'>全部</li>");
				}else{//不是全部选中
					//上边框中添加此元素
					if(checkedCount == 1){
						showNameUlObj.html('<li class="licity" city='+city+'>'+test+'</li>');
					}else{
						showNameUlObj.append('<li class="licity" city='+city+'>'+test+'</li>');
					}
				}
			}else{//取消选中
				//上边框中有值的话，移除
				console.log("size========"+$('li[city="'+ city +'"]').size());
				if( $('li[city="'+ city +'"]').size()){
					$('li[city="'+ city +'"]').remove();
				}
				//若全部checkbox选中，则取消选中
				if($("input[name='" + this.name + "'][isAll='1']").prop("checked") == true){
					$("input[name='" + this.name + "'][isAll='1']").prop("checked", this.checked);
					showNameUlObj.html("");
					//把选中的值，添加到上边框中
					$("input[name='" + this.name + "']:checked").each(function(){
						test=$(this).siblings("b").html();
						if($(this).attr("city") == undefined){ $(this).attr("city", window.cityId++);}
						//获取上边显示框的一些值 == 用于添加和移除元素
						city = $(this).attr("city");
						console.log(test + '   '+city);
						showNameUlObj.append('<li class="licity" city='+city+'>'+test+'</li>');
					});
				}
			}
		}
		//增加间距
		if($(".cityshow li:last-child")){
			$(".cityshow li:last-child").removeClass("padR6").siblings().addClass("padR6");
		}
		// 默认提示文字
		var csel=$(selectSp).siblings(".c-sel");
		var clen=csel.find(".cityshow li").length;
		if(clen == 0){
			csel.find(".j-empty").html("请选择");
		}else{
			csel.find(".j-empty").html("　");
		}
	});
}

function selectAll(obj){
	$("input[name='" + obj.name + "']").prop("checked", obj.checked);
}


