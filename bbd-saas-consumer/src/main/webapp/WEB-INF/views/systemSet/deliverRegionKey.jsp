<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<div class="box box-primary">
    <div class="box-body">
        <c:url var="importSiteKeywordFileUrl" value="/site/importSiteKeywordFile?${_csrf.parameterName}=${_csrf.token}"/>
        <c:url var="querySiteUrl" value="/deliverRegion/map/3?${_csrf.parameterName}=${_csrf.token}"/>
        <form action="querySiteUrl" method="post" id="siteKeywordForm" name="siteKeywordForm">
            <div class="row">
                <div class="col-xs-3">
                    <label>导入时间：</label>
                    <input id="between" name="between" type="text" class="form-control" placeholder="请选择导入时间范围" value=""/>
                </div>
                <div class="col-xs-3">
                    <label>关键词：</label>
                    <input id="keyword" name="keyword" type="text" class="form-control" placeholder="请输入关键词" value=""/>
                </div>
                <div class="col-xs-3">
                    <button class="btn btn-primary" style="margin-top:25px ; margin-left: 15px ;" type="submit">
                        查询</button>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-1">
                    <button class="btn btn-success" style="margin-top:10px ;" type="button" >
                        导入地址关键词</button>
                </div>
                <div class="col-xs-1">
                    <button class="btn btn-primary" style="margin-top:10px ;" type="button" id="exportData">
                        导出地址关键词</button>
                </div>
            </div>
        </form>
        <form action="${importSiteKeywordFileUrl}" method="post" enctype="multipart/form-data">
            选择文件:<input type="file" name="file">
            <input type="submit" value="提交">
        </form>
    </div><!-- /.box-body -->
    <div class="col-xs-12">
        <div class="box">
            <div class="box-header">
            </div>
            <div class="box-body table-responsive">
                <table id="orderTable" class="table table-bordered table-hover">
                    <thead>
                    <th>选择</th>
                    <th>导入时间</th>
                    <th>省</th>
                    <th>市</th>
                    <th>区</th>
                    <th>地址关键词</th>
                    <th>操作</th>
                    </thead>
                    <tbody>
                    <c:forEach items="${siteKeywordPageList}" var="siteKeyword">
                        <tr>
                            <td><input type="checkbox" value="${siteKeyword.id}" name="id"/></td>
                            <td>${siteKeyword.createAt}</td>
                            <td>${siteKeyword.province}</td>
                            <td>${siteKeyword.city}</td>
                            <td>${siteKeyword.distict}</td>
                            <td>${siteKeyword.keyword}</td>
                            <td><a href="/site/deleteSitePoiKeyword/${siteKeyword.id}"><u>删除</u></a></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <tfoot>
                    <th colspan="7">
                        <input id="selectAll" name="selectAll" type="checkbox"> <label for="selectAll">
                        全选</label> &nbsp;
                        <input id="piliangDel"  class="btn btn-danger" name="delAll" type="button" value="批量删除">
                    </th>
                    </tfoot>
                </table>
            </div>
        </div>
    </div>
</div>
<script type="application/javascript">
    $("input[type='checkbox']").iCheck({
        checkboxClass : 'icheckbox_square-blue'
    });
    $("#selectAll").on('ifUnchecked', function() {
        $("input[type='checkbox']", "#orderTable").iCheck("uncheck");
    }).on('ifChecked', function() {
        $("input[type='checkbox']", "#orderTable").iCheck("check");
    });
    //批量删除
    $("#piliangDel").click(function(){
        var id_array=new Array();
        $('input[name="id"]:checked').each(function(){
            id_array.push($(this).val());//向数组中添加元素
        });
        var delIds = id_array.join(',');
        console.log(delIds);
        if(delIds==""){
            alert("请选择");
            return false;
        }
        if(confirm("确认批量删除所选站点关键词？")){
            window.location.href="/site/piliangDeleteSitePoiKeyword/"+delIds
        }
    })
    $("#exportData").click(function(){
        window.location.href="/site/exportSiteKeywordFile";
    })
</script>
