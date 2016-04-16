$(function(){
	// S sidebar\
	var winhei=$(window).height();
	var dochei=$(document).height();
	function counthei(){
		$(".b-branch").css({minHeight:winhei-60});
		$(".b-detail").css({minHeight:winhei});
		$(".b-sidebar .lv1").click(function() {
			if($(this).hasClass('side-cur')){//h3有curr
				if($(this).next('ul.menu').css("display")=="block"){//menu有dn
					$(this).next('ul.menu').slideUp();
				}else{//menu没有dn
					$(this).next('ul.menu').slideDown();
				}
			}else{//h3没有curr
				$(this).siblings('.lv1').removeClass('side-cur');
				$(this).siblings('.menu').slideUp();
				$(this).addClass('side-cur').next('ul.menu').slideDown();
			}
		});
	};
	counthei();
	$(window).resize(function(){
		counthei();
	});
	// E sidebar
	// S 配送区域


	// E 配送区域


	// 注册
	shP(".j-login",".j-login-type");
	// 重新分派
	shP(".j-sel",".j-sel-pop");
	// 转其他快递
	shP(".j-turn",".j-turn-pop");
	// 退货
	shP(".j-th",".j-th-pop");
	// 注册
	shP(".j-site",".j-site-pop");
	// 新建用户
	shP(".j-user",".j-user-pop");
	// 新建角色
	shP(".j-role",".j-role-pop");
	function shP(clickW,showW){
		$(clickW).on("click",function(){
			$(showW).modal('show');
		});
	}

	// 删除
	$(".j-del").on("click",function(){
		$(this).parent().parent().remove();
	})

	//全选
	$(".j-sel-all").on("click",function(){
		if(!$("input[name=inputA]").is(':checked')) {
			$("input[name=inputC]").prop("checked", false);
		}else{
			$("input[name=inputC]").prop("checked",true);
		}
	})


	//引导页
	$(".b-guide-con .step:eq(0)").show();
	//点击保存
	mcount=0;
	$('.next-step').click(function(){
		//alert(lilen)
		$('.pre-step').show();
		mcount+=1
		// 3的倍数
		if(mcount==3){
			mcount=2;
			return;
		}else if(mcount<3){
			$(".b-guide-tab li").eq(mcount).addClass("guide-cur");
		}

		showOther(mcount)
	});
	//点击上一步
	$('.pre-step').click(function(){
		mcount-=1
		if(mcount<=0){
			mcount=0;
			$(".b-guide-tab li").eq(mcount+1).removeClass("guide-cur");
			$('.pre-step').hide();

		}else if(mcount>=0){
			//alert(mcount)
			$(".b-guide-tab li").eq(mcount+1).removeClass("guide-cur");
		}
		showOther(mcount);
	});
	//点击左右按钮执行的函数
	function showOther(mcount){
		var viewwid=$('.b-tab-all').width();
		$(".b-guide-con").css({"margin-left": -viewwid*mcount+'px'});


	};
	$(".j-guide-pop").modal();

})
//iframe 自适应高度
function iFrameHeight() {

	var ifm= document.getElementById("iframe1");

	var subWeb = document.frames ? document.frames["iframe1"].document:ifm.contentDocument;

	if(ifm != null && subWeb != null) {

		ifm.height = subWeb.body.scrollHeight+60;

	}
}

