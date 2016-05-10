$(function () {
	
    // S sidebar\
    var winhei = $(window).height();
    var dochei = $(document).height();

    function counthei() {
        $(".b-branch").css({minHeight: winhei - 60});
        $(".b-detail").css({minHeight: winhei});

    };
    counthei();
    $(".b-sidebar .lv1").click(function () {
        if ($(this).hasClass('side-cur')) {//h3有curr
            if ($(this).next('ul.menu').css("display") == "block") {//menu有dn
                $(this).next('ul.menu').slideUp();
            } else {//menu没有dn
                $(this).next('ul.menu').slideDown();
            }
        } else {//h3没有curr
            $(this).siblings('.lv1').removeClass('side-cur');
            $(this).siblings('.menu').slideUp();
            $(this).addClass('side-cur').next('ul.menu').slideDown();
        }
    });
    $(window).resize(function () {
        counthei();
    });
    // E sidebar

// S 站点管理

    // 新建
    shP(".j-siteM",".j-siteM-pop");
    // 修改
    shP(".j-siteM-rvs",".j-siteM-pop");
    // 通过
    shP(".j-pass",".j-pass-pop");
    // 驳回
    shP(".j-reject",".j-reject-pop");
    // 查看驳回原因
    shP(".j-rejectR",".j-rejectR-pop");
    // 修改密码
    shP(".j-pwd",".j-pwd-pop");
    // E 站点管理
    // 重新分派
    shP(".j-sel", ".j-sel-pop");
    // 转其他快递
    shP(".j-turn", ".j-turn-pop");
    // 退货
    shP(".j-th", ".j-th-pop");
    // 注册
    shP(".j-site", ".j-site-pop");
    // 新建用户
    shP(".j-user", ".j-user-pop");
    // 新建角色
    shP(".j-role", ".j-role-pop");
    function shP(clickW, showW) {
        $(clickW).on("click", function () {
            $(showW).modal('show');
        });
    }

// S 注册站点
    $("#mask").css({height: dochei + 120});
    // 点击注册
    $(".j-login").on("click", function (e) {
        e.stopPropagation();
        $(".j-login-type").addClass("in").show();
        $("#mask").show();
        $(document.body).addClass("modal-open").css({paddingRight:"17px"});
    });
    // 注册站点
    $(".j-site-re").on("click", function () {
        $(this).parents(".modal").hide();
        $("#mask").show();
        $(".j-site-re-pop").addClass("in").show();
        $(document.body).addClass("modal-open").css({paddingRight:"17px"});
    });
    $(".j-scroll-dislog").on("click", function (e) {
        e.stopPropagation();
    })
//    $(document).on("click", function () {
//        $(".j-login-type,#mask").hide()
//    })
    $(".j-close").on("click",function(){
        $(this).parent().parent().parent().parent().hide();
        $("#mask").hide();
        $(".j-guide-pop").hide().removeClass("in");
        $(document.body).removeClass("modal-open").css({paddingRight:"0"});
    })
    // E 注册站点

    //全选
    $(".j-sel-all").on("click", function () {
        if (!$("input[name=inputA]").is(':checked')) {
            $("input[name=inputC]").prop("checked", false);
        } else {
            $("input[name=inputC]").prop("checked", true);
        }
    })
    // S 浮层固定定位
    $(parent.window).scroll(function(){
	  $('.j-pl-pop').css({top:$(parent.window).scrollTop()});
	})
	// E 浮层固定定位
	
	// S iframe嵌进去的页面的左导航
	var parentD=$('#psrE',window.parent.document);  
	var ulhtml=parentD.find(".b-sidebar").html();
	$(".sub-sidebar").html(ulhtml);
	// E iframe嵌进去的页面的左导航

    //S new 注册
    //点击保存
    nstep=0;
    var innerwid=$(".n-progress-inner").width();
    $('.j-next').click(function(){
        nstep+=1
        // 3的倍数
        if(nstep==3){
            nstep=2;
            return;
        }else if(nstep<3){
            $(".n-progress-inner").css({width:innerwid+432*nstep})
            $(".n-progress-txt span").eq(nstep).addClass("n-fcur");
        }

        showStep(nstep)
    });
    //点击上一步
    $('.j-prev').click(function(){
        nstep-=1
        if(nstep<=0){
            nstep=0;
            $(".n-progress-txt span").eq(nstep+1).removeClass("n-fcur");
        }else if(nstep>=0){
            $(".n-progress-txt span").eq(nstep+1).removeClass("n-fcur");
            $(".n-progress-inner").css({width:innerwid+432*nstep})
        }
        showStep(nstep);
    });


    //点击按钮执行的函数
    function showStep(nstep){
        var viewwid=$('.n-form-infoA').width();
        $(".n-form-info").css({"margin-left": -viewwid*nstep+'px'});
    };

    // 站点
    $(".j-next-site").click(function(){
        var viewwid=$('.n-form-infoA').width();
        $(".n-form-info").css({"margin-left": -viewwid*3+'px'});
        $(".n-progress-inner").css({width:innerwid+432*3});
        $(".n-progress-txt span").eq(2).addClass("n-fcur");
    });

    $(".j-prev-site").click(function(){
        var viewwid=$('.n-form-infoA').width();
        $(".n-form-info").css({"margin-left": -viewwid*1+'px'});
        $(".n-progress-inner").css({width:innerwid+432*1})
        $(".n-progress-txt span").eq(2).removeClass("n-fcur");
    });

    // 配送公司 提交
    $(".j-company").click(function(){
        $(".n-form-infoA,.n-progress-area").hide();
        $(".j-company-result").show();
    });
    $(".j-site-s").click(function(){
        $(".n-form-infoA,.n-progress-area").hide();
        $(".j-site-result").show();
    });

    //E new 注册

    // S 站点管理
    $(".j-siteM").click(function(){
        var rvstxt=$(this).find("em").html();
        $(".j-cg-txt").html(rvstxt);
    });
    $(".j-sel-val").click(function(){
        if($(this).val() == 4){
            $(".j-dn").show();
        }else{
            $(".j-dn").hide();
        }
    })
    // E 站点管理
    
    
})
//iframe 自适应高度
function iFrameHeight() {

    var ifm = document.getElementById("iframe1");

    var subWeb = document.frames ? document.frames["iframe1"].document : ifm.contentDocument;

    if (ifm != null && subWeb != null) {

        ifm.height = subWeb.body.scrollHeight + 60;

    }
}