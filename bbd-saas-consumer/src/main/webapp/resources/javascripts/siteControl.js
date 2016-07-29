var cityId = 0;
//var isSiteId = false;
var controlName = "codeOpt";
if(isSiteId){
	controlName = "idOpt";
}
if(inputName == null){
	inputName = "areaCode";
}
var optionArayy = [];
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
		if(this.value == null || this.value == ""){
			$('#cityLable').hide();
		}else{
			$('#cityLable').show();
		}
		$('#distLable').hide();
		$("#addr_control .city").val("");//清空
		$("#addr_control .dist").val("");
		$("#"+inputName).val("");
		console.log($("#addr_control .city").val());
		//更新站点下拉框
		getSiteListByAddr();
	});
	// 市改变
	$('#addr_control .city').change(function(){
		if(this.value == null || this.value == ""){
			$('#distLable').hide();
		}else{
			$('#distLable').show();
		}
		$("#addr_control .dist").val("");//清空
		$("#"+inputName).val("");
		//更新站点下拉框
		getSiteListByAddr();
	}) ;
	// 区改变
	$('#addr_control .dist').change(function(){
		$("#"+inputName).val("");//清空
		//更新站点下拉框
		getSiteListByAddr();
	});
	//绘制电子围栏 -- 更改站点
	//扫描运单号--把快递分派给派件员--边输入边改变
	$("#"+inputName).on('input',function(e){
		getSiteListByAddr();
		$(".all-area2").show();
	});
});
function getSiteListByAddr(){
	$("#"+inputName).removeAttr("disabled");
	$("#options").html("");
	$.ajax({
		type : "GET",  //提交方式
		url : siteUrl,//路径
		data : {
			"prov" : $("#addr_control .prov").val(),
			"city" :  $("#addr_control .city").val(),
			"area" :  $("#addr_control .dist").val(),
			"siteName" :  $("#"+inputName).val()
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
		if($("#"+inputName).val() == null || $("#"+inputName).val() == ""){//未手动搜索，需要显示全部
			ulObj.append(getOneOption("", "全部", 1));
		}
		//ulObj.append("<li><label class='f12 linputC'><input type='checkbox' name='" + controlName + "' value=''><b>全部</b></label></li>");
		if(isSiteId){//取得站点id
			optionList.forEach(function(option){
				ulObj.append(getOneOption(option.id, option.name, 0));
			});
		}else{
			optionList.forEach(function(option){
				ulObj.append(getOneOption(option.code, option.name, 0));
			});
		}
		selectS(".all-area2");
	}

}

function getOneOption(id, name, isAll){
	var listr = "<li><label class='f12 linputC'><input type='checkbox' name='" + controlName + "' value='" + id + "' isAll='" + isAll + "'><b>";
	listr += name + "</b></label></li>";
	return listr;
}
// 创建省略号
$(".c-sel").append("<sub class='c-dot'>&hellip;</sub>")
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
// S 多选框
var txtMaxLen=18;
function selectS(selectSp){
	var sbox=$(selectSp).find(".pv-part li input");
	sbox.on("click",function(){
		var test=$(this).siblings("b").html();
		var arr="";
		//增加属性
		if($(this).attr("city") == undefined){ $(this).attr("city", window.cityId++);}
		//获取上边显示框的一些值 == 用于添加和移除元素
		var city = $(this).attr("city");
		var curP=$(this).parents('.l-sel-p').parent().index();
		var shA=$(this).parents(selectSp).siblings(".c-sel").find(".showA");

		//全选 -- 取消全选
		var isAll = $(this).attr("isAll");
		console.log("isAll===="+isAll);
		var showNameUlObj = shA.eq(curP).find(".cityshow");//$("#options");
		if(isAll == "1"){//全选
			//optionArayy.push(this.value.replace("全部", ""));
			//其他checkbox联动
			$("input[name='" + this.name + "']").prop("checked", this.checked);
			//上边框中显示的值
			if(this.checked == true){//选中
				if($("#"+inputName).val() == null || $("#"+inputName).val() == ""){//未手动搜索，需要显示全部
					showNameUlObj.html("<li city='1' class='licity'>全部</li>");
				}
			}else{//取消选中
				showNameUlObj.html("");
			}
		}else{//某个具体项
			var checkedCount = $("input[name='" + this.name + "'][isAll='0']:checked").length;//选中的checkbox个数
			var itemCount = $(selectSp).find(".pv-part li").length -1 ;//所有li个数（减去全部）
			console.log("checkedCount==11="+checkedCount+"  itemCount==="+itemCount);
			//上边框中显示的值
			if(this.checked == true){//选中
				if($("#"+inputName).val() == null || $("#"+inputName).val() == ""){//未手动搜索，需要显示全部
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
				}else{//手动搜索，不显示全部
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
		// S 增加省略号
		showNameUlObj.find("li").each(function(){
			arr+=$(this).html();
		});
		var len = getLength(arr);
		var iNum=Math.ceil(len/2);
		if(iNum>txtMaxLen){
			$(selectSp).siblings(".c-sel").find(".c-dot").show();
		}else{
			$(selectSp).siblings(".c-sel").find(".c-dot").hide();
		}

		// E 增加省略号
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
// E 多选框
function getAreaCodeStr(name){
	if(name == null){
		name = controlName;
	}
	if($("input[name='" + name + "'][isAll='1']:checked").length == 1){//全选
		return "";
	}
	areaCodes = [];
	$("input[name='" + name + "']:checked").each(function(){
		areaCodes.push(this.value);
	});
	return areaCodes.join(",");
}
function getWordNum(name){
	if(name == null){
		name = controlName;
	}
	var citys = [];
	var num = 0;
	$("input[name='" + this.name + "']:checked").each(function(){
		test=$(this).siblings("b").html();
		num += test.length;
		showNameUlObj.append('<li class="licity" city='+city+'>'+test+'</li>');
	});
	$("input[name='" + name + "']:checked").siblings("b").each(function(){
		num += $(this).html().length;
	});
	return num;
}
function selectAll(obj){
	$("input[name='" + obj.name + "']").prop("checked", obj.checked);
}

//获得字符串实际长度，中文2，英文1
function getLength(str){
	wordNum = 0;
	var realLength = 0, len = str.length, charCode = -1;
	for (var i = 0; i < len; i++) {
		if(realLength < txtMaxLen*2){
			wordNum ++;
		}
		charCode = str.charCodeAt(i);
		if (charCode >= 0 && charCode <= 128){//英文或者数字
			realLength += 1;
		} else{//中文
			realLength += 2;
		}
	}
	return realLength;
}