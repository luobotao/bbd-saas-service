
if(inputName == null){
	inputName = "statusOpt";
}
$(document).ready(function() {
	inputS(".j-sel-input",".all-area1");
	selectS(".all-area1");
});
//获得站点多选框的值
function getMultValStr(ulId){
	if(ulId == null){
		ulId = "options";
	}
	codes = [];
	//size = $('#options li[site="'+ option.id +'"]').size();
	$("#" + ulId +" li").each(function(){
		codes.push($(this).attr("site"));
	});
	return codes.join(",");
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
		//if($(this).attr("city") == undefined){ $(this).attr("city", window.cityId++);}
		//获取上边显示框的一些值 == 用于添加和移除元素
		var site = this.value;
		var curP=$(this).parents('.l-sel-p').parent().index();
		var shA=$(this).parents(selectSp).siblings(".c-sel").find(".showA");
		//全选 -- 取消全选
		var isAll = $(this).attr("isAll");
		var showNameUlObj = shA.eq(curP).find(".cityshow");//$("#options");
		if(isAll == "1"){//全选
			//其他checkbox联动
			$("input[name='" + this.name + "']").prop("checked", this.checked);
			//上边框中显示的值
			if(this.checked == true){//选中
				if($("#"+inputName).val() == null || $("#"+inputName).val() == ""){//未手动搜索，需要显示全部
					showNameUlObj.html("<li site='' class='lisite'>全部</li>");
				}
			}else{//取消选中
				showNameUlObj.html("");
			}
		}else{//某个具体项
			var checkedCount = $("input[name='" + this.name + "'][isAll='0']:checked").length;//选中的checkbox个数
			var itemCount = $(selectSp).find(".pv-part li").length -1 ;//所有li个数（减去全部）
			//console.log("checkedCount==11="+checkedCount+"  itemCount==="+itemCount);
			//上边框中显示的值
			if(this.checked == true){//选中
				if($("#"+inputName).val() == null || $("#"+inputName).val() == ""){//未手动搜索，需要显示全部
					if(checkedCount == itemCount ){//全部选中
						//全选checkbox选中
						$("input[name='" + this.name + "'][isAll='1']").prop("checked", this.checked);
						//上边框中显示的值
						showNameUlObj.html("<li site='' class='licity'>全部</li>");
					}else{//不是全部选中
						//上边框中添加此元素
						showNameUlObj.append('<li class="licity" site='+site+'>'+test+'</li>');
					}
				}else{//手动搜索，不显示全部
					//上边框中添加此元素
					showNameUlObj.append('<li class="licity" site='+site+'>'+test+'</li>');
				}
			}else{//取消选中
				//上边框中有值的话，移除
				if( $('li[site="'+ site +'"]').size()){
					$('li[site="'+ site +'"]').remove();
				}
				//若全部checkbox选中，则取消选中
				if($("input[name='" + this.name + "'][isAll='1']").prop("checked") == true){
					$("input[name='" + this.name + "'][isAll='1']").prop("checked", this.checked);
					showNameUlObj.html("");
					//把选中的值，添加到上边框中
					$("input[name='" + this.name + "']:checked").each(function(){
						test=$(this).siblings("b").html();
						//获取上边显示框的一些值 == 用于添加和移除元素
						site = this.value;
						showNameUlObj.append('<li class="licity" site='+site+'>'+test+'</li>');
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