<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%!
	public String pageNav(String pageUrl,int totalPage,
						  int currPage,int count,String reqType,
						  Map<String,String> par){

		reqType = reqType == null || "null".equals(reqType) ? "GET":reqType;
		StringBuffer pagestr = new StringBuffer("");
		StringBuffer parstr = new StringBuffer(setParameter(reqType, par));
		StringBuffer v = new StringBuffer("");
		v.append("<div><div class='col-xs-6'>"); /* pagecon pagination pagination-centered */
		v.append("<div class='dataTables_info' id='userTable_info'>页码：");
		v.append(currPage + "/" + totalPage + " 共计" + count + "条记录");
		v.append("</div></div>");
		v.append("<div>");
		v.append("<div class='dataTables_paginate paging_bootstrap'>");
		v.append("<ul class='pagination'>");
		
		if (currPage <=1){
			v.append("<li class='prev disabled'><a href='#'>首页</a></li>");
			v.append("<li class='prev disabled'><a href='#'>上一页</a></li>");
		}else{
			if("POST".equalsIgnoreCase(reqType)){
				v.append("<li class='prev'><a href='#' onclick=\"gotoPage('1');\">首页</a></li>");
				v.append("<li class='prev'><a  href='#' onclick=\"gotoPage('" + (currPage - 1) + "');\" title='上一页'>上一页</a></li>");
			}else{
				if (!"".equals(parstr.toString())){
					v.append("<li class='prev'><a href='" + pageUrl + "?currPage=1&" + parstr.toString() + "' title='首页'>首页</a></li>");
					v.append("<li class='prev'><a href='" + pageUrl + "?currPage=" + (currPage - 1) + "&" + parstr.toString() + "' title='上一页'>上一页</a></li>");
				}else{
					v.append("<li class='prev'><a href='" + pageUrl + "?currPage=1' title='首页'>首页</a></li>");
					v.append("<li class='prev'><a href='" + pageUrl + "?currPage=" + (currPage - 1) + "' title='上一页'>上一页</a></li>");
				}
			}
		}
		if(currPage == 1){
			v.append("<li class='active'><a  href='#'>1</a></li>");
		}else{
			if("POST".equalsIgnoreCase(reqType)){
				v.append("<li><a href='#' onclick=\"gotoPage('1');\">1</a></li> ");
			}else{
				if (!"".equals(parstr.toString())){
					v.append("<li><a href='" + pageUrl + "?currPage=1&" + parstr.toString() + "'>1</a></li> ");
				}else{
					v.append("<li><a href='" + pageUrl + "?currPage=1" + parstr.toString() + "'>1</a></li> ");
				}
			}
		}
		if (totalPage > 1){
			if (currPage<=5){
				v.append(setPageString(2, currPage, currPage, reqType, parstr.toString(), pageUrl));
			}else{
				 v.append("<li><a>...</a></li>");
				 v.append(setPageString(currPage - 3, currPage, currPage, reqType, parstr.toString(), pageUrl));
			}
			
			if (currPage>=totalPage-4 || totalPage-4<=0){
				v.append(setPageString(currPage + 1, totalPage, currPage, reqType, parstr.toString(), pageUrl));
			}else{
				  v.append(setPageString(currPage + 1, currPage+3, currPage, reqType, parstr.toString(), pageUrl));
				  v.append("<li><a>...</a></li>");
				  if ("POST".equalsIgnoreCase(reqType)){
				  	 v.append("<li><a href='#' onclick=\"gotoPage('"+totalPage+"\');\"'>"+totalPage+"</a></li> ");
				  }else{
				  	if (!"".equals(parstr.toString())){
						v.append("<li><a href='" + pageUrl + "?currPage=" + totalPage + "&" + parstr.toString() + "'>" + totalPage + "</a></li>");
					}else{
						v.append("<li><a href='" + pageUrl + "?currPage=" + totalPage + "'>" + totalPage + "</a></li>");
					}
				  }
			}
		}
		if (currPage >= totalPage){
			v.append("<li class='next disabled'><a href='#' title='已经是最后一页了'>下一页</a></li>");
			v.append("<li class='next disabled'><a href='#' title='已经是最后一页了'>尾页</a></li>");
		}else{
			if("POST".equalsIgnoreCase(reqType)){
				v.append("<li><a href='#' title='下一页'  onclick=\"gotoPage('" + (currPage + 1) + "');\">下一页</a></li>");
				v.append("<li><a href='#' onclick=\"gotoPage('" + totalPage + "');\" title='尾页'>尾页</a></li>");
			}else{
				if (!"".equals(parstr.toString())){
					v.append("<li><a title='下一页' href='" + pageUrl + "?currPage=" + (currPage + 1) + "&" + parstr.toString() + "'>下一页</a></li>");
					v.append("<li><a title='尾页' href='" + pageUrl + "?currPage=" + totalPage + "&" + parstr.toString() + "'>尾页</a></li>");
				}else{
					v.append("<li><a title='下一页' href='" + pageUrl + "?currPage=" + (currPage + 1) + "'>下一页</a></li>");
					v.append("<li><a title='尾页' href='" + pageUrl + "?currPage=" + totalPage + "'>尾页</a></li>");
				}
			}
		}
		//v.append("<li><a class='hui'>共"+count+"条 | "+currPage+"/"+totalPage+"页</a></li>");
		//v.append("</span></div>");
		if("POST".equalsIgnoreCase(reqType)){
			v.append("<form action='" + pageUrl + "' method='POST' name='pagesubForm' id='pagesubForm'>");
			v.append("<input type='hidden' id='selAllFlag' name='selAllFlag'/>");
			v.append(parstr.toString());
			v.append("</form>");
			v.append("<script>");
			v.append("function gotoPage(page){");
			v.append("document.getElementById('currPage').value = page;");
			v.append("document.getElementById('pagesubForm').submit();");
			v.append("}");
			v.append("</script>");
		}
		v.append("</ul></div></div></div>");
		
		return v.toString();
	}
	public String setParameter(String type,Map<String,String> par){
		StringBuffer parameter = new StringBuffer("");
		if (par != null && par.size() > 0){
			int parcount = par.size();
			int i = 0;
			Iterator<String> it = par.keySet().iterator();
			while(it.hasNext()){
				String key =it.next();
				String value = par.get(key);
				if ("POST".equalsIgnoreCase(type)){
					parameter.append("<input type='hidden' name='" + key + "' id='" + key + "' value='" + value + "'/>");
				}else if ("GET".equalsIgnoreCase(type)){
					if (i < (parcount -1)){
						parameter.append(key + "=" + value + "&");
					}else{
						parameter.append(key + "=" + value);
					}
				}
				i ++;
			}
		}
		return parameter.toString();
	}
	public String setPageString(int start,int end,int currPage,String reqType,String parstr,String pageUrl){
		StringBuffer v = new StringBuffer("");
		for(int i = start; i <= end; i ++){
			if(currPage == i){
				v.append("<li class='active'><a href='#'>" + i + "</a></li>");
			}else{
				if("POST".equalsIgnoreCase(reqType)){
					v.append("<li><a href='#' onclick=\"gotoPage('" + i + "');\">" + i + "</a></li>");
				}else{
					if (!"".equals(parstr.toString())){
						v.append("<li><a href='" + pageUrl + "?currPage=" + i + "&" + parstr.toString() + "'>" + i + "</a></li>");
					}else{
						v.append("<li><a href='" + pageUrl + "?currPage=" + i + "'>" + i + "</a></li>");
					}
				}
			}
		}
		return v.toString();
	}
%>