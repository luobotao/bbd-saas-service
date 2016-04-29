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
})
//iframe 自适应高度
function iFrameHeight() {

    var ifm = document.getElementById("iframe1");

    var subWeb = document.frames ? document.frames["iframe1"].document : ifm.contentDocument;

    if (ifm != null && subWeb != null) {

        ifm.height = subWeb.body.scrollHeight + 60;

    }
}