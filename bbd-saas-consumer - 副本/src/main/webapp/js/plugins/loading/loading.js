(function($) {
	    $.showLoad = function(msg) {
	
	        $("<div class=\"load-mask\"></div>").css({display:"block",width: $(document).width(),height:$(document).height()}).appendTo("body");
	        $("<div class=\"load-mask-msg\"></div>").html(msg ? msg : "注册中,请稍候...").appendTo("body");
	        var top = ($(window).height() - $(".load-mask-msg").outerHeight())/2;
	        var scrollTop = $(document).scrollTop();  
	        $(".load-mask-msg").css({display:"block",left: ($(window).width() -$(".load-mask-msg").outerWidth()) / 2,top:top+scrollTop})
	
	    };
	
	    $.hideLoad = function() {
	        $("div.load-mask").remove();
	        $("div.load-mask-msg").remove();
	    }
	
	})(jQuery);