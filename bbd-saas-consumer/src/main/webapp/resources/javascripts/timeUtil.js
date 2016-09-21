Date.prototype.Format = function(fmt)
{
    var o = {
        "M+" : this.getMonth()+1,                 //月份
        "d+" : this.getDate(),                    //日
        "H+" : this.getHours(),                   //小时
        "m+" : this.getMinutes(),                 //分
        "s+" : this.getSeconds(),                 //秒
        "q+" : Math.floor((this.getMonth()+3)/3), //季度
        "S"  : this.getMilliseconds()             //毫秒
    };
    if(/(y+)/.test(fmt))
        fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
    for(var k in o)
        if(new RegExp("("+ k +")").test(fmt))
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
    return fmt;
}


/**
 * 计算两个日期之间相差几个月
 */
function getMonthdiff(beginDate, endDate){             
    var Month1,Month2,iMonths; 
    
    Month1 = parseInt(beginDate.split("-")[0],10)*12 + parseInt(beginDate.split("-")[1],10);
    Month2 = parseInt(endDate.split("-")[0],10)*12 + parseInt(endDate.split("-")[1],10);
    iMonths = Month2 - Month1;    
    return iMonths;      
}
/**
 * 计算两个日期之间相差几个天数，向下取整（不足一天为0天）
 */
function getDayDiff(startStr) {
    //开始时间
    startStr = (startStr).replace(/-/g, "/");//一般得到的时间的格式都是：yyyy-MM-dd hh24:mi:ss，所以我就用了这个做例子，是/的格式，就不用replace了。
    var start_date = new Date(startStr);
    //结束时间
    var end_date = new Date();//将字符串转化为时间
    var num = (end_date - start_date) / (1000 * 3600 * 24);//求出两个时间的时间差，这个是天数
    return parseInt(num);//转化为整天（小于零的话不用转了），向下取整（不足一天为0天）
    return parseInt(Math.ceil(num));//转化为整天（小于零的话不用转了），向上取整（不足一天为1天）
}

/**
 * 时间转换时间戳
 * @param endTime
 * @returns {number}
 */
function transdate(endTime){
    var date=new Date();
    date.setFullYear(endTime.substring(0,4));
    date.setMonth(endTime.substring(5,7)-1);
    date.setDate(endTime.substring(8,10));
    date.setHours(endTime.substring(11,13));
    date.setMinutes(endTime.substring(14,16));
    date.setSeconds(endTime.substring(17,19));
    return Date.parse(date)/1000;
}
/**
 * 转换成 yyyy-MM-dd HH:mm:ss 格式
 * @param tm
 * @returns {string}
 */
function getDate1(tm){
	if(tm == null){
		return "";
	}
	var date = new Date(tm);
    return date.Format("yyyy-MM-dd HH:mm:ss");
}
/**
 * 转换成 转换成 yyyy-MM-dd 格式
 * @param tm
 * @returns {string}
 */
function getDate2(tm){
    if(tm == null){
        return "";
    }

    var date = new Date(tm);
    return date.Format("yyyy-MM-dd");
}

function getDateStr(AddDayCount) {
    var dd = new Date();
    dd.setDate(dd.getDate()+AddDayCount);//获取AddDayCount天后的日期
    return dd.Format("yyyy/MM/dd");
}