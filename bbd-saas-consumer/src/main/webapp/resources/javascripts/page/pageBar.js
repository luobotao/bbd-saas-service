
/********currPage, totalPage, count 传入数字类型的参数*********/
//***************分页条******开始**********/
function paginNav(currPage, totalPage, count){
	var pagestr = "";
	pagestr += "<div class='fr'>"; /* pagecon pagination pagination-centered */
	pagestr += "<div class='dataTables_info fl  c-disable' id='userTable_info'>页码：";
	pagestr += (currPage + 1) + "/" + totalPage + " &nbsp;&nbsp; 共计" + count + "条记录";
	pagestr += "</div>";
	pagestr += "<div class='dataTables_paginate paging_bootstrap ml12'>";
	pagestr += "<ul class='pagination '>";
	//首页和上一页
	if (currPage < 1){
		pagestr += "<li class='prev disabled  c-disable'><a href='#'>首页</a></li>";
		pagestr += "<li class='prev disabled  c-disable'><a href='#'><em class='glyphicon glyphicon-triangle-left'></em>上一页</a></li>";
	}else{
		pagestr += "<li class='prev'><a href='#' onclick=\"gotoPage(0);\">首页</a></li>";
		pagestr += "<li class='prev'><a  href='#' onclick=\"gotoPage(" + (currPage - 1) + ");\" title='上一页'><em class='glyphicon glyphicon-triangle-left'></em>上一页</a></li>";
	}
	//当前页之前的页数-当前页-当前页之后的页数
	if (totalPage >= 0){
		//当前页之前的页数
		if (currPage<=5){
			pagestr += setPageString(1, currPage, currPage);
		}else{
			pagestr += "<li><a href='#' onclick=\"gotoPage(0);\">1</a></li>";
			pagestr += "<li><a>...</a></li>";
			pagestr += setPageString((currPage - 2) , currPage, currPage);
		}
		//当前页之后的页数
		if (currPage>=totalPage-4 || totalPage-4<=0){
			pagestr += setPageString((currPage + 1), totalPage, currPage);
		}else{
			pagestr += setPageString((currPage + 1), (currPage + 4), currPage);
			pagestr += "<li><a>...</a></li>";
			pagestr += "<li><a href='#' onclick=\"gotoPage("+(totalPage - 1)+"\);\"'>"+totalPage+"</a></li> ";
		}
	}
	//下一页和尾页
	if (currPage >=(totalPage - 1)){
		pagestr += "<li class='next disabled c-disable'><a href='#' title='已经是最后一页了'>下一页 <em class='glyphicon glyphicon-triangle-right'></em></a></li>";
		pagestr += "<li class='next disabled'><a href='#' title='已经是最后一页了'>尾页</a></li>";
	}else{
		pagestr += "<li><a href='#' title='下一页'  onclick=\"gotoPage(" + (currPage + 1) + ");\">下一页 <em class='glyphicon glyphicon-triangle-right'></em></a></li>";
		pagestr += "<li><a href='#' onclick=\"gotoPage(" + (totalPage - 1) + ");\" title='尾页'>尾页</a></li>";
	}
	pagestr += "</ul></div></div>";
	return pagestr;
}
//***************分页条******结束**********/

//***************显示连续的页码*********开始******************/
function setPageString(start, end, currPage){
	var numstr = "";
	for(var i = start; i <= end; i ++){
		if(currPage == (i-1)){
			numstr += "<li class='active'><a href='#'>" + i + "</a></li>";
		}else{
			numstr += "<li><a href='#' onclick=\"gotoPage(" + (i-1) + ");\">" + i + "</a></li>";
		}
	}
	return numstr;
}
//***************显示连续的页码*********结束******************/
