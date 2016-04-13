//***************分页条******开始**********/

function paginNav(currPage, totalPage, count){
	var pagestr = "";
	pagestr += "<div class='col-xs-6'>"; /* pagecon pagination pagination-centered */
	pagestr += "<div class='dataTables_info' id='userTable_info'>页码：";
	pagestr += currPage + "/" + totalPage + " &nbsp;&nbsp; 共计" + count + "条记录";
	pagestr += "</div></div>";
	pagestr += "<div>";
	pagestr += "<div class='dataTables_paginate paging_bootstrap'>";
	pagestr += "<ul class='pagination'>";
	
	if (currPage <= 1){
		pagestr += "<li class='prev disabled'><a href='#'>首页</a></li>";
		pagestr += "<li class='prev disabled'><a href='#'>上一页</a></li>";
	}else{
		pagestr += "<li class='prev'><a href='#' onclick=\"gotoPage('1');\">首页</a></li>";
		pagestr += "<li class='prev'><a  href='#' onclick=\"gotoPage('" + (parseInt(currPage) + 1) + "');\" title='上一页'>上一页</a></li>";
	}
	if(currPage == 1){
		pagestr += "<li class='active'><a  href='#'>1</a></li>";
	}else{
		pagestr += "<li><a href='#' onclick=\"gotoPage('1');\">1</a></li> ";
	}
	
	if (totalPage > 1){
		if (currPage<=5){
			pagestr += setPageString(2, currPage, currPage);
		}else{
			 pagestr += "<li><a>...</a></li>";
			 pagestr += setPageString((parseInt(currPage) - 3) , currPage, currPage);
		}
		
		if (currPage>=totalPage-4 || totalPage-4<=0){
			pagestr += setPageString((parseInt(currPage) + 1), totalPage, currPage);
		}else{
			  pagestr += setPageString((parseInt(currPage) + 1), (parseInt(currPage) + 3), currPage);
			  pagestr += "<li><a>...</a></li>";
			  pagestr += "<li><a href='#' onclick=\"gotoPage('"+totalPage+"\');\"'>"+totalPage+"</a></li> ";
		}
	}
	if (currPage >= totalPage){
		pagestr += "<li class='next disabled'><a href='#' title='已经是最后一页了'>下一页</a></li>";
		pagestr += "<li class='next disabled'><a href='#' title='已经是最后一页了'>尾页</a></li>";
	}else{
		pagestr += "<li><a href='#' title='下一页'  onclick=\"gotoPage('" + (parseInt(currPage) + 1) + "');\">下一页</a></li>";
		pagestr += "<li><a href='#' onclick=\"gotoPage('" + totalPage + "');\" title='尾页'>尾页</a></li>";
	}
	pagestr += "</ul></div></div>";
	return pagestr;
}
//***************分页条******结束**********/

//***************显示连续的页码*********开始******************/
function setPageString(start, end, currPage){
	var numstr = "";
	for(var i = start; i <= end; i ++){
		if(currPage == i){
			numstr += "<li class='active'><a href='#'>" + i + "</a></li>";
		}else{
			numstr += "<li><a href='#' onclick=\"gotoPage('" + i + "');\">" + i + "</a></li>";
		}
	}
	return numstr;
}
//***************显示连续的页码*********结束******************/
