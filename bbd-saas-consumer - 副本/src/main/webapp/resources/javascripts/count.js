function addcountwx(ips,url,channel,openid){
$.ajax({
            //提交数据的类型 POST GET
            type:"POST",
            //提交的网址
            url:"/user/countjs",
            //提交的数据
            data:{ips:ips,curl:url,sharetype:"",iswx:1,channel:channel,openid:openid},
            //返回数据的格式
            datatype: "json",//"xml", "html", "script", "json", "jsonp", "text"
            //在请求之前调用的函数
            beforeSend:function(){},
            //成功返回之后调用的函数            
            success:function(data){
            }   ,
            //调用执行后调用的函数
            complete: function(XMLHttpRequest, textStatus){
            },
            //调用出错执行的函数
            error: function(){
            }        
         }); 
}
