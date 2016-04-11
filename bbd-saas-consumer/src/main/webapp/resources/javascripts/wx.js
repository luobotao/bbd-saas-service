
//判断是否微信浏览器
function checkiswx(){
	var ua = window.navigator.userAgent.toLowerCase();
	var bw=false;
	
	var iswx="0";
	if(ua.match(/MicroMessenger/i) == 'micromessenger')
		bw=true;
	return bw;
}

//微信分享
function wxshare(appid,timstr,nostr,sign,sharetitle,sharedesc,shareimg,surl,debug){
	  wx.config({
		    debug: debug,
		    appId: appid,
		    timestamp: timstr,
		    nonceStr: nostr,
		    signature: sign,
		    jsApiList: ['checkJsApi',
		                'onMenuShareTimeline',
		                'onMenuShareAppMessage'
		    ]
		  });
			wx.ready(function () {
		    // 1 判断当前版本是否支持指定 JS 接口，支持批量判断
				wx.checkJsApi({
				  jsApiList: [
					'getNetworkType',
					'previewImage'
				  ],
				  success: function (res) {
					//alert(JSON.stringify(res));
				  }
				});
				wx.onMenuShareTimeline({
					title: sharetitle, // 分享标题
				    link: surl, // 分享链接
				    imgUrl: shareimg, // 分享图标
				    success: function () { 
				        // 用户确认分享后执行的回调函数
				    },
				    cancel: function () { 
				        // 用户取消分享后执行的回调函数
				    }
				});

				wx.onMenuShareAppMessage({
					title: sharetitle,
					  desc: sharedesc,
					  link: surl,
					  imgUrl: shareimg,
					  success: function (res) {
						//alert('已分享');
					  },
					  cancel: function (res) {
						//alert('已取消');
					  }
				  });		
		  });
		  wx.error(function (res){
				//alert(res.errMsg);
			  if(debug)
				  alert(res.errMsg);
		  });
	}

	//***************禁止分享****************/
	function onBridgeReady(){
			  WeixinJSBridge.call('hideOptionMenu');
	}

	function nowxshare(){	 
		if (typeof WeixinJSBridge == "undefined"){
			if( document.addEventListener ){
			 	document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
			}else if (document.attachEvent){
			  document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
			  document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
			}
		}else{
			  onBridgeReady();
		}
	}
	/***************禁止分享****************/