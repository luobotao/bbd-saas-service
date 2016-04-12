<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%!
	public String pageNav(String pageUrl,int totalPage,
						  int pageIndex,int count,String reqType,
						  Map<String,String> par){

		reqType = reqType == null || "null".equals(reqType) ? "GET":reqType;
		StringBuffer pagestr = new StringBuffer("");
		StringBuffer parstr = new StringBuffer(setParameter(reqType, par));
		StringBuffer v = new StringBuffer("");
		v.append("<div><div class='col-xs-6'>"); /* pagecon pagination pagination-centered */
		v.append("<div class='dataTables_info' id='userTable_info'>页码：");
		v.append(pageIndex + "/" + totalPage + " 共计" + count + "条记录");
		v.append("</div></div>");
		v.append("<div>");
		v.append("<div class='dataTables_paginate paging_bootstrap'>");
		v.append("<ul class='pagination'>");
		
		if (pageIndex <=1){
			v.append("<li class='prev disabled'><a href='javascript:'>首页</a></li>");
			v.append("<li class='prev disabled'><a href='javascript:'>上一页</a></li>");
		}else{
			if("POST".equalsIgnoreCase(reqType)){
				v.append("<li class='prev'><a onclick=\"gotoPage('1');\">首页</a></li>");
				v.append("<li class='prev'><a  onclick=\"gotoPage('" + (pageIndex - 1) + "');\" title='上一页'>上一页</a></li>");
			}else{
				if (!"".equals(parstr.toString())){
					v.append("<li class='prev'><a href='" + pageUrl + "?pageIndex=" + (pageIndex - 1) + "&" + parstr.toString() + "' title='上一页'>上一页</a></li>");
				}else{
					v.append("<li class='prev'><a href='" + pageUrl + "?pageIndex=" + (pageIndex - 1) + "' title='上一页'>上一页</a></li>");
					//v.append("<a href='" + pageUrl + "?pageIndex=" + (pageIndex - 1) + "' title='上一页'>上一页</a>");
				}
			}
		}
		if(pageIndex == 1){
			v.append("<li class='active'><a  href='#'>1</a></li>");
		}else{
			if("POST".equalsIgnoreCase(reqType)){
				v.append("<li><a onclick=\"gotoPage('1');\">1</a></li> ");
			}else{
				if (!"".equals(parstr.toString())){
					v.append("<li><a href='" + pageUrl + "?pageIndex=1&" + parstr.toString() + "'>1</a></li> ");
				}else{
					v.append("<li><a href='" + pageUrl + "?pageIndex=1" + parstr.toString() + "'>1</a></li> ");
				}
			}
		}
		if (totalPage > 1){
			if (pageIndex<=5){
				v.append(setPageString(2, pageIndex, pageIndex, reqType, parstr.toString(), pageUrl));
			}else{
				 v.append("<li><a>...</a></li>");
				 v.append(setPageString(pageIndex - 3, pageIndex, pageIndex, reqType, parstr.toString(), pageUrl));
			}
			
			if (pageIndex>=totalPage-4 || totalPage-4<=0){
				v.append(setPageString(pageIndex + 1, totalPage, pageIndex, reqType, parstr.toString(), pageUrl));
			}else{
				  v.append(setPageString(pageIndex + 1, pageIndex+3, pageIndex, reqType, parstr.toString(), pageUrl));
				  v.append("<li><a>...</a></li>");
				  if ("POST".equalsIgnoreCase(reqType)){
				  	 v.append("<li><a onclick=\"gotoPage('"+totalPage+"\');\"'>"+totalPage+"</a></li> ");
				  }else{
				  	if (!"".equals(parstr.toString())){
						v.append("<li><a href='" + pageUrl + "?pageIndex=" + totalPage + "&" + parstr.toString() + "'>" + totalPage + "</a></li>");
					}else{
						v.append("<li><a href='" + pageUrl + "?pageIndex=" + totalPage + "'>" + totalPage + "</a></li>");
					}
				  }
			}
		}
		if (pageIndex >= totalPage){
			v.append("<li><a href='#' title='已经是最后一页了'>下一页</a></li>");
		}else{
			if("POST".equalsIgnoreCase(reqType)){
				v.append("<li><a title='下一页'  onclick=\"gotoPage('" + (pageIndex + 1) + "');\">下一页</a></li>");
			}else{
				if (!"".equals(parstr.toString())){
					v.append("<li><a title='下一页' href='" + pageUrl + "?pageIndex=" + (pageIndex + 1) + "&" + parstr.toString() + "'>下一页</a></li>");
				}else{
					v.append("<li><a title='下一页' href='" + pageUrl + "?pageIndex=" + (pageIndex + 1) + "'>下一页</a></li>");
				}
			}
		}
		//v.append("<li><a class='hui'>共"+count+"条 | "+pageIndex+"/"+totalPage+"页</a></li>");
		//v.append("</span></div>");
		if("POST".equalsIgnoreCase(reqType)){
			v.append("<form action='" + pageUrl + "' method='POST' name='pagesubForm' id='pagesubForm'>");
			v.append("<input type='hidden' id='selAllFlag' name='selAllFlag'/>");
			v.append(parstr.toString());
			v.append("</form>");
			v.append("<script>");
			v.append("function gotoPage(page){");
			v.append("document.getElementById('pageIndex').value = page;");
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
	public String setPageString(int start,int end,int pageIndex,String reqType,String parstr,String pageUrl){
		StringBuffer v = new StringBuffer("");
		for(int i = start; i <= end; i ++){
			if(pageIndex == i){
				v.append("<li><a  class='active'>" + i + "</a></li>");
			}else{
				if("POST".equalsIgnoreCase(reqType)){
					v.append("<li><a onclick=\"gotoPage('" + i + "');\">" + i + "</a></li>");
				}else{
					if (!"".equals(parstr.toString())){
						v.append("<li><a href='" + pageUrl + "?pageIndex=" + i + "&" + parstr.toString() + "'>" + i + "</a></li>");
					}else{
						v.append("<li><a href='" + pageUrl + "?pageIndex=" + i + "'>" + i + "</a></li>");
					}
				}
			}
		}
		return v.toString();
	}
%>