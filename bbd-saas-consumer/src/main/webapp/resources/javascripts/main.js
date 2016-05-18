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
        if($(this).val() == 5){
            $(".j-dn").show();
        }else{
            $(".j-dn").hide();
        }
    })
    // E 站点管理
    // S 密码


    $(".j-n-pwd").on("blur",function(){
        var pwdval=$(this).val();
        if(!pwdreg.test(pwdval)){
            $(".b-prompt").addClass("mov");
            $(".b-prompt i").html("请输入6-12位数字和字母结合的密码")
            outDiv();
        }
    })
    //确认密码
    $(".j-c-pwd").on("blur",function(){
        if($(this).val()!="" && $(this).val() != $(".j-n-pwd").val()){
            $(".b-prompt").addClass("mov");
            $(".b-prompt i").html("两次密码不一致")
            outDiv();
        }
    })
    // E 密码

    // S 站点管理新建
    // 新建

    $(".j-siteM").on("click", function (e) {
        e.stopPropagation();
        $(".j-siteM-pop").addClass("in").show();
        $("#mask").show();
        $("#mask").css({left:"16%"})
        $('.j-siteM-pop').css({top:$(parent.window).scrollTop()});
        parentD.addClass("modal-open").css({paddingRight:"17px"});
    })

    $(".j-f-close").on("click",function(){
        $(this).parent().parent().parent().parent().parent().hide();
        $("#mask").hide();
        $(".j-siteM-pop").hide().removeClass("in");
        parentD.removeClass("modal-open").css({paddingRight:"0"});
    })
    
    $(".n-re-con").css({minHeight:winhei-152});

    if(window.screen.availHeight<800){
        $(".y-scroll").css({maxHeight:"200px"})
    }else{
        $(".y-scroll").css({maxHeight:"300px"})
    }
})
var pwdreg=/^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$/;
//iframe 自适应高度
function iFrameHeight() {

    var ifm = document.getElementById("iframe1");

    var subWeb = document.frames ? document.frames["iframe1"].document : ifm.contentDocument;

    //console.log("ht===000="+ifm.height);
    if (ifm != null && subWeb != null) {
        /*if(ifm.height){
            console.log("ht===000="+ifm.height);
        }*/
        if(ifm.height < 1150){
            ifm.height = subWeb.body.scrollHeight + 150;
        }else{
            ifm.height = 1150;
        }
    }
    //console.log("ht===="+ifm.height);
}
// S 自己的alert 提示
function alert_mine(titile,content){
    $(".j-alert-pop").modal();
    $(".j-c-tie").html(titile)
    $(".j-alert-con").html(content)
}
// E 自己的alert 提示

// S 气泡 提示
function outDiv(content){
    $('.b-prompt-txt').css({top:$(parent.window).scrollTop()});
    $(".b-prompt").addClass("mov");
    $(".b-prompt i").html(content);
    var txtwid=$(".b-prompt .b-prompt-txt").outerWidth();
    $(".b-prompt-txt").css({marginLeft:-txtwid/2})
    setTimeout(function(){
        $(".b-prompt").removeClass("mov");
    },2000)
}
// E 气泡 提示


// S 图片上传
function setImagePreviews(avalue) {
    var docObj = document.getElementById("licensePic");
    var dd = document.getElementById("lcs-img");
    dd.innerHTML = "";
    var fileList = docObj.files;
    for (var i = 0; i < fileList.length; i++) {
        dd.innerHTML += "<div style='float:left' > <img id='img" + i + "' class='img0'  /> </div>";
        var imgObjPreview = document.getElementById("img" + i);
        if (docObj.files && docObj.files[i]) {
            //火狐下，直接设img属性
            imgObjPreview.style.display = 'block';
            imgObjPreview.style.width = '180px';
            //火狐7以上版本不能用上面的getAsDataURL()方式获取，需要一下方式
            imgObjPreview.src = window.URL.createObjectURL(docObj.files[i]);
        }
        else {
            //IE下，使用滤镜
            docObj.select();
            var imgSrc = document.selection.createRange().text;
            var localImagId = document.getElementById("img" + i);
            //必须设置初始大小

            localImagId.style.width = "150px";

            //图片异常的捕捉，防止用户修改后缀来伪造图片

            try {

                localImagId.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";

                localImagId.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = imgSrc;

            }

            catch (e) {

                alert("您上传的图片格式不正确，请重新选择!");

                return false;

            }

            imgObjPreview.style.display = 'none';

            document.selection.empty();

        }

    }


    return true;

}
// E 图片上传