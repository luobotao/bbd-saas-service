;(function($, window){
// load css
var link = $('<link rel="stylesheet" href=\"/kkmy-merchant/js/plugins/dialog/default.css\">');
$("head link:last").after(link)

var defaults = {
								// 消息内容
	content: 	'<div class="ui-dialog-loading"><span>loading..</span></div>',
	title: 	 	'\u6d88\u606f',		// 标题. 默认'消息'
	ok: 	 	true,				// 确定按钮回调函数,传入false时不显示
	cancel:  	false,				// 取消按钮回调函数,传入false时不显示
	okVal:   	'\u786E\u5B9A',		// 确定按钮文本. 默认'确定'
	cancelVal:  '\u53D6\u6D88',		// 取消按钮文本. 默认'取消'
	width: 		'auto',				// 内容宽度
	padding: 	'25px',				// 内容与边界填充距离
	align:      'center',           // 水平对齐,默认居中对齐
	icon: 		null,				// 消息图标名称,未启用
	time: 		null,				// 自动关闭时间，未启用
	esc: 		false,				// 是否支持Esc键关闭
	lock: 		true,				// 是否锁屏
	background: '#000',				// 遮罩颜色
	opacity: 	0.7,				// 遮罩透明度
	duration: 	300,				// 遮罩透明度渐变动画速度
	fixed: 		false,				// 是否静止定位
	drag: 		true				// 是否允许用户拖动位置
};

var DOM 		= {},
	COUNT 		= 1,
	_$window    = $(window),
	_$document  = $(document),
	_elem 		= document.documentElement,
	_isIE6		= !-[1,] && !window.XMLHttpRequest,
	_zIndex 	= 198967,
	API 		= null;

var template =
'<div class="ui-dialog">'
+	'<table class="ui-dialog-inner">'
+		'<tbody>'
+			'<tr>'
+				'<td>'
+					'<div class="ui-dialog-header">'
+						'<em class="ui-dialog-close" title="\u5173\u95ED">\u5173\u95ED</em>'
+						'<span class="ui-dialog-title">[tit]</span>'
+					'</div>'
+					'<div class="ui-dialog-main">'
+						'<div class="ui-dialog-icon"></div>'
+						'<div class="ui-dialog-content"></div>'
+					'</div>'
+					'<div class="ui-dialog-buttons">'
+						'<a href="javascript:" class="ui-dialog-submit"><span>确定</span></a>'
+						'<a href="javascript:" class="ui-dialog-cancel"><span>取消</span></a>'
+					'</div>'
+				'</td>'
+			'</tr>'
+		'</tbody>'
+	'</table>'
+'</div>';


// 开启IE6 CSS背景图片缓存
_isIE6 && document.execCommand('BackgroundImageCache', false, true);

function Dialog(config) {
	if (config.id === (DOM[config.id] || 0)) {
		return false;
	} else if (!(this instanceof Dialog)) {
		return new Dialog(config);
	} else {
		this.config = config && $.extend({}, defaults, config) || defaults;	

		if (!this.config.id) {
			this.config.id = "dialog_" + new Date().getTime() + "_" + COUNT;	
		}

		DOM[this.config.id] = this.config.id;
		COUNT ++;
		return this.init();	
	}
}
Dialog.prototype = {
	init: function () {
		this.content();
		this.button();
		this.title();
		this.icon();
		this.reset();
		this.addEvent();
		this.time(this.config.time);
		API	= this;
		_zIndex ++;
		return this;
	},
	content: function () {
		var dom = {wrap: $(template)},
			body = document.body;
		body.appendChild(dom.wrap.get(0));	
		var name, i = 0,
			els = dom.wrap.get(0).getElementsByTagName('*'),
			elsLen = els.length;			
		for (; i < elsLen; i ++) {
			name = els[i].className.split('ui-dialog-')[1];
			name && (dom[name] = $(els[i]));
		};		

		if (window.DIALOGSKIN) {
			dom.wrap.addClass("ui-dialog-" + window.DIALOGSKIN);
		}

		dom.wrap.attr("id", this.config.id);
		dom.inner.attr({"width":this.config.width});	

		dom.main.css({"padding" : this.config.padding, "text-align" : this.config.align });
        if (this.config.align === "justify") {
            dom.main.css({"text-justify" : "inter-ideograph"}); // for ie
        }
		typeof this.config.content === "object" ? dom.content.append(this.config.content) : dom.content.html(this.config.content)
		this.dom = dom;
		return this;
	},
	button: function() {
		var k = 0;
		if (this.config.ok === false) {
			this.dom['submit'].remove();
			k++;
		} else {
			this.dom.submit.html("<span>" + this.config.okVal + "</span>");
		}
		if (this.config.cancel === false) {
			this.dom['cancel'].remove();
			k++
		} else {
			this.dom.cancel.html("<span>" + this.config.cancelVal + "</span>");
			if (k === 1 && this.config.cancelVal === "确定") {
				this.dom.cancel.attr("class", "ui-dialog-submit");
			}
		}
		if (k === 2) {
			this.dom['buttons'].remove();
		}
	},
	title: function () {
                //this.dom.title.html(this.config.title + (this.config.time ? "(" + this.config.time + "秒后自动关闭)":""));
		this.dom.title.html(this.config.title + (this.config.time ? "(<span id='dialogTimeout'>" + this.config.time + "</span>秒后自动关闭)":""));//为解决药房信息页面关闭时间倒计时不能递减所加，页面自己写定时器来控制
		this.dom.header.css({"cursor": (this.config.drag ? "move" : "default")});
		return this;
	},
	icon: function () {
		this.dom.icon.remove();
		return this;
		
		if (this.config.icon) {
			this.dom.icon.addClass("ui-dialog-" + this.config.icon);
			this.dom.content.css({"display" : "inline-block"});
			if (this.dom.content.width() + this.dom.icon.width() > this.dom.main.width()) {
				this.dom.content.css({"width"	: this.dom.main.width() - this.dom.icon.width() - 10})
			}
		} else {
			this.dom.icon.remove();
		};
		return this;
	},
	position: function () {
		var winWidth 	= _$window.width(),
			winHeight 	= _$window.height(),
			wrap 		= this.dom.wrap[0],
			isFixed 	= this.config.fixed,
			wrapWidth 	= wrap.offsetWidth,
			wrapHeight 	= wrap.offsetHeight,
			left 		= (winWidth - wrapWidth)/2,
			top  		= (winHeight - wrapHeight)/2,
			stop 		= document.documentElement.scrollTop || document.body.scrollTop,
			cssText = 'position:' + (isFixed && !_isIE6 ? 'fixed;' : 'absolute;');
		if (isFixed && _isIE6) {
			cssText += 'left:expression((document.documentElement).scrollLeft+' + left + ');top:expression((document.documentElement).scrollTop+' + top + ');';
		} else {
			cssText += 'left:' + left + 'px;top:' + (!isFixed ? (stop + top) : top) + 'px;';
		}
		wrap.style.cssText = cssText + 'z-index:' + _zIndex;
		return this;
	},
	reset: function () {
		this.position();
		this.config.lock && this.lock();
		this._ie6SelectFix();
		return this;
	},
	close: function () {
		if (API === this) API = null;
		this._timer && clearTimeout(this._timer);
		this.removeEvent();
		DOM.hasOwnProperty(this.config.id) && delete DOM[this.config.id];
		this.unlock();
		this.dom.wrap.attr({"style":""}).html('').remove();
		return this;
	},
	time: function (second) {
		var that 	= this,
			timer 	= that._timer;
		timer && clearTimeout(timer);
		if (second) {
			that._timer = setTimeout(function(){
				that.close();
			}, 1000 * second);
		};
		return that;
	},
	addEvent: function () {
		var that = this,
			resizeTimer;
		this.dom.wrap.on("click", ".ui-dialog-close, .ui-dialog-submit, .ui-dialog-cancel", function(event) {
			var elem = event.target;
			if (elem === that.dom.close[0] || elem.parentNode === that.dom.close[0] || elem === that.dom.cancel[0] || elem.parentNode === that.dom.cancel[0]) {
				if (typeof that.config.cancel === 'function') {
					that.config.cancel.apply(that, arguments);
				}
				that.close();

			} else if (elem === that.dom.submit[0] || elem.parentNode === that.dom.submit[0]) {
				if (typeof that.config.ok === 'function') {
					that.config.ok.apply(that, arguments);	
				} else {
					that.close();
				}
			}
		});

		this.dom.wrap.on("mousedown", function() {
			that.zIndex();
		})

		that._winResize = function () {
			resizeTimer && clearTimeout(resizeTimer);
			resizeTimer = setTimeout(function () {
				that.reset();
			}, 40);
		};
		_$window.on('resize', that._winResize);
		return this;
	},
	removeEvent: function () {
		this.dom.wrap.off("click mousedown");
		_$window.off('resize', this._winResize);
		return this;
	},
	zIndex: function () {
		this._lockMask && this._lockMask.css({'z-index': _zIndex});
		this.dom.wrap.css({"z-index":_zIndex});	
		_zIndex++;	
		API	= this;
		return this;
	},
	lock: function () {
		if (this._lock) return this;
		var that 		= this,
			index 		= _zIndex,
			wrap 		= that.dom.wrap,
			config 		= that.config,
			domTxt 		= '(document.documentElement)',
			lockMask 	= that._lockMask || wrap.before(document.createElement('div')).prev();

		var cssText 	= 'overflow:hidden;background-color:' + config.background
						+ ';filter:alpha(opacity=0);opacity:0;z-index:' 
						+ _zIndex + ';';

		if (_isIE6) {
			cssText 	+= 'position:absolute;left:expression(' + domTxt 
						+ '.scrollLeft);top:expression('
						+ domTxt + '.scrollTop);width:expression(' + domTxt
						+ '.clientWidth);height:expression(' + domTxt 
						+ '.clientHeight);';
			// 让IE6锁屏遮罩能够盖住下拉控件
			lockMask.html(
						  '<iframe src="about:blank" style="width:100%;height:100%;position:absolute;' 
						+ 'top:0;left:0;z-index:-1;filter:alpha(opacity=0)"></iframe>');
		} else {
			cssText 	+= 'position:fixed;width:100%;height:100%;left:0;top:0;';
		}
		lockMask.attr({"style" : cssText, "id" : config.id + "_mask"});
		if (config.duration === 0 || _isIE6) {
			lockMask.css({opacity: config.opacity});
		} else {
			lockMask.animate({opacity: config.opacity}, config.duration);
		};
		that._lockMask 	= lockMask;
		that._lock 		= true;
		return that;
	},
	unlock: function () {
		var that 		= this,
			lockMask 	= that._lockMask;
		
		if (!that._lock) return that;
		var un = function () {
			lockMask.attr({"style":""}).remove();
		};
		
		if (!that.config.duration || _isIE6) {
			un();
		} else {
			lockMask.animate({opacity: 0}, that.config.duration, un);
			if (lockMask.length > 0) {
				un();
			};
		};
		
		that._lock = false;
		return that;
	},
	_ie6SelectFix: function () {
		if (!_isIE6 || this.config.lock) {
			return this;
		}
		var wrap 	= this.dom.wrap[0],
			iframe 	= this.dom.iframe,
			width 	= wrap.offsetWidth,
			height 	= wrap.offsetHeight;

		if (iframe) {
			iframe.style.width 	= width + "px";
			iframe.style.height = height + "px";
		} else {
			iframe = wrap.appendChild(document.createElement('iframe'));
			this.dom.iframe;
			iframe.src = 'about:blank';
			iframe.style.cssText = 'position:absolute;z-index:-1;left:0;top:0;'
			+ 'filter:alpha(opacity=0);width:' + width + 'px;height:' + height + "px";
		};
	}

}
window.dialog = Dialog;


// ESC关闭弹层
_$document.on('keydown', function (event) {
	var target = event.target,
		nodeName = target.nodeName,
		rinput = /^INPUT|TEXTAREA$/,
		keyCode = event.keyCode;
	if (!API || !API.config.esc || rinput.test(nodeName)) return;		
	keyCode === 27 && API.dom.close.trigger('click');
});


// 拖拽模块
var _dragEvent, _use,
	_isLosecapture = 'onlosecapture' in _elem,
	_isSetCapture = 'setCapture' in _elem;
_$document.on('mousedown', function (event) {
	if (!API) return;
	var target = event.target,
		config = API.config,
		dom = API.dom;
	
	if (config.drag !== false && target === dom.header[0] || target === dom.title[0]) {
		_dragEvent = _dragEvent || new DragEvent();
		_use(event);
		return false;// 防止firefox与chrome滚屏
	};
});


// 拖拽事件
function DragEvent () {
	var that = this,
		proxy = function (name) {
			var fn = that[name];
			that[name] = function () {
				return fn.apply(that, arguments);
			};
		};
	proxy('start');
	proxy('move');
	proxy('end');
};
DragEvent.prototype = {
	// 开始拖拽
	onstart: function () {},
	start: function (event) {
		_$document
		.bind('mousemove', this.move)
		.bind('mouseup', this.end);
			
		this._sClientX = event.clientX;
		this._sClientY = event.clientY;
		this.onstart(event.clientX, event.clientY);

		return false;
	},
	
	// 正在拖拽
	onmove: function () {},
	move: function (event) {		
		this._mClientX = event.clientX;
		this._mClientY = event.clientY;
		this.onmove(
			event.clientX - this._sClientX,
			event.clientY - this._sClientY
		);
		
		return false;
	},
	
	// 结束拖拽
	onend: function () {},
	end: function (event) {
		_$document
		.unbind('mousemove', this.move)
		.unbind('mouseup', this.end);
		
		this.onend(event.clientX, event.clientY);
		return false;
	}
	
};

_use = function (event) {
	var limit, startWidth, startHeight, startLeft, startTop,
		dom = API.dom,
		wrap = dom.wrap,
		header = dom.header,
		main = dom.main;
	// 清除文本选择
	var clsSelect = 'getSelection' in window ? function () {
		window.getSelection().removeAllRanges();
	} : function () {
		try {
			document.selection.empty();
		} catch (e) {};
	};
	
	// 对话框准备拖动
	_dragEvent.onstart = function (x, y) {
		startLeft = wrap[0].offsetLeft;
		startTop = wrap[0].offsetTop;
		_$document.bind('dblclick', _dragEvent.end);
		!_isIE6 && _isLosecapture ?
			header.bind('losecapture', _dragEvent.end) :
			_$window.bind('blur', _dragEvent.end);
		_isSetCapture && header[0].setCapture();
		
		wrap.addClass('ui-dialog-state-drag');
	};
	
	// 对话框拖动进行中
	_dragEvent.onmove = function (x, y) {
		var style = wrap[0].style,
			left = Math.max(limit.minX, Math.min(limit.maxX, x + startLeft)),
			top = Math.max(limit.minY, Math.min(limit.maxY, y + startTop));

		left = left > 0 ? left - 1 : 0;
		style.left = left  + 'px';
		style.top = top + 'px';
	
		clsSelect();
	};
	
	// 对话框拖动结束
	_dragEvent.onend = function (x, y) {
		_$document.unbind('dblclick', _dragEvent.end);
		!_isIE6 && _isLosecapture ?
			header.unbind('losecapture', _dragEvent.end) :
			_$window.unbind('blur', _dragEvent.end);
		_isSetCapture && header[0].releaseCapture();
		wrap.removeClass('ui-dialog-state-drag');
	};
	
	limit = (function () {
		var maxX, maxY,
			wrap = API.dom.wrap[0],
			fixed = wrap.style.position === 'fixed',
			ow = wrap.offsetWidth,
			oh = wrap.offsetHeight,
			ww = _$window.width(),
			wh = _$window.height(),
			dl = fixed ? 0 : _$document.scrollLeft(),
			dt = fixed ? 0 : _$document.scrollTop(),
			
		// 坐标最大值限制
		maxX = ww - ow + dl;
		maxY = wh - oh + dt;
		
		return {
			minX: dl,
			minY: dt,
			maxX: maxX,
			maxY: maxY
		};
	})();
	_dragEvent.start(event);
};
}(jQuery, window));