<%@ page language="java" pageEncoding="UTF-8" %>
<%@ page import="com.bbd.saas.mongoModels.Order" %>
<%@ page import="com.bbd.saas.utils.PageModel" %>
<%@ page import="com.bbd.saas.enums.ArriveStatus" %>
<%@ page import="com.bbd.saas.enums.OrderStatus" %>
<%@ page import="com.bbd.saas.utils.Dates" %>
<%@ page import="com.bbd.saas.enums.ExpressStatus" %>
<%@ page import="com.bbd.saas.enums.OrderSetStatus" %>
<%@ page import="com.bbd.saas.vo.OrderHoldToStoreVo" %>
<%@ page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <jsp:include page="../main.jsp" flush="true"/>

</head>
<%
    String proPath = request.getContextPath();
    String path = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + proPath;
%>
<body class="fbg">
<!-- S content -->
<div class="clearfix b-branch">
    <div class="container-fluid">
        <div class="row">
            <!-- S sidebar -->
            <div class="col-xs-12 col-sm-12 bbd-md-3" style="visibility: hidden;">
                <ul class="sub-sidebar">
                </ul>
            </div>
            <!-- E sidebar -->
            <!-- S detail -->
            <div class="b-detail col-xs-12 col-sm-12 bbd-md-9">
                <!-- S 状态 -->
                <ul class="row">
                    <li class="b-board-card col-xs-12 col-sm-6 col-md-3 col-lg-3">
                        <dl class="arrive-status  c1" onclick="todaySuccessOrder();" style="cursor: pointer">
                            <dt class="b-order" id="successOrderNum">${successOrderNum}</dt>
                            <dd>今日成功接单数</dd>
                        </dl>
                    </li>
                    <li class="b-board-card col-xs-12 col-sm-6 col-md-3 col-lg-3">
                        <dl class="arrive-status c2" onclick="todayNoToStore();" style="cursor: pointer">
                            <dt class="b-order" id="todayNoToStoreNum">${todayNoToStoreNum}</dt>
                            <dd>今日未入库订单数</dd>
                        </dl>
                    </li>
                    <li class="b-board-card col-xs-12 col-sm-6 col-md-3 col-lg-3">
                        <dl class="arrive-status c3" onclick="historyNoToStore();" style="cursor: pointer">
                            <dt class="b-order" id="historyToStoreNum">${historyToStoreNum}</dt>
                            <dd>历史未入库订单数</dd>
                        </dl>
                    </li>
                    <li class="b-board-card col-xs-12 col-sm-6 col-md-3 col-lg-3">
                        <dl class="arrive-status c3" onclick="todayToStore();" style="cursor: pointer">
                            <dt class="b-order" id="todayToStoreNum">${todayToStoreNum}</dt>
                            <dd>今日已入库订单数</dd>
                        </dl>
                    </li>
                </ul>
                <!-- E 状态 -->
                <!-- S 搜索区域 -->
                <form class="form-inline form-inline-n">
                    <div class="search-area">
                        <div class="row pb20">
                            <div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4">
                                <label>状态：</label>
                                <select id="orderSetStatus" name="orderSetStatus" class="form-control form-con-new">
                                    <%=OrderSetStatus.Srcs2HTML(-1)%>
                                </select>
                            </div>
                            <div class="form-group col-xs-12 col-sm-6 col-md-5 col-lg-5">
                                <label>揽件员：</label>
                                <select id="embrace" name="embrace" class="form-control form-con-new">
                                    <option value="0">请选择</option>
                                    <c:if test="${userList!=null}">
                                        <c:forEach items="${userList}" var="user">
                                            <option value="${user.id}">${user.realName}</option>
                                        </c:forEach>
                                    </c:if>
                                </select>
                            </div>
                            <div class="form-group col-xs-12 col-sm-6 col-md-3 col-lg-3">
                                <a href="javascript:void(0)" class="ser-btn l" onclick="searchOrder()"><i
                                        class="b-icon p-query p-ser"></i>查询</a>
                            </div>
                        </div>
                        <div class="row pb20">
                            <div class="form-group col-xs-12 col-sm-6 col-md-4 col-lg-4" style="display:none;">
                                <label></label>
                                <input id="parcelCode" name="parcelCode" class="form-control" type="text"
                                       onkeypress="enterPress(event)"/>
                                <p class="help-block" id="parcelCodeP" style="display:none;"></p>
                            </div>
                            <div class="form-group col-xs-12 col-sm-6 col-md-5 col-lg-5">
                                <label>扫描运单号：</label>
                                <input id="mailNum" name="mailNum" class="form-control" type="text"
                                       onkeypress="enterPress(event)"/>
                                <p class="help-block" id="mailNumP" style="display:none;"></p>
                            </div>
                        </div>
                    </div>
                </form>
                <!-- E 搜索区域 -->
                <div class="tab-bod mt20">
                    <!-- S table -->
                    <div class="table-responsive">
                        <table id="orderTable" class="table">

                            <thead>
                            <tr>
                                <th>揽件员</th>
                                <th>手机</th>
                                <th>运单号</th>
                                <th>收货人</th>
                                <th>收货人电话</th>
                                <th width="20%">地址</th>
                                <th>状态</th>
                            </tr>
                            </thead>
                            <tbody id="dataList">
                            <%
                                PageModel<OrderHoldToStoreVo> orderHoldPageModels = (PageModel<OrderHoldToStoreVo>) request.getAttribute("orderHoldPageModel");
                                List<OrderHoldToStoreVo> datas = orderHoldPageModels.getDatas();

                                if (datas != null && datas.size() > 0) {
                                    for (OrderHoldToStoreVo orderHoldToStoreVo : orderHoldPageModels.getDatas()) {
                            %>
                            <tr>
                                <td><%=orderHoldToStoreVo.getUserName()%>
                                </td>
                                <td><%=orderHoldToStoreVo.getPhone()%>
                                </td>
                                <td><%=orderHoldToStoreVo.getMailNum()%>
                                </td>
                                <td><%=orderHoldToStoreVo.getRecieverName() %>
                                </td>
                                <td><%=orderHoldToStoreVo.getRecieverPhone()%>
                                </td>
                                <td class="tl"><%=orderHoldToStoreVo.getRecieverAddress()%>
                                </td>
                                <%
                                    if (orderHoldToStoreVo.getOrderSetStatus() == OrderSetStatus.NOEMBRACE) {
                                %>
                                <td class="orange-a"><%=OrderSetStatus.NOEMBRACE.getMessage()%>
                                </td>
                                <%
                                } else if (orderHoldToStoreVo.getOrderSetStatus() == OrderSetStatus.SCANED) {
                                %>
                                <td class="blue-d"><%=OrderSetStatus.SCANED.getMessage()%>
                                </td>
                                <%
                                } else if (orderHoldToStoreVo.getOrderSetStatus() == OrderSetStatus.WAITTOIN) {
                                %>
                                <td class="orange"><%=OrderSetStatus.WAITTOIN.getMessage()%>
                                </td>
                                <%
                                } else if (orderHoldToStoreVo.getOrderSetStatus() == OrderSetStatus.WAITSET) {
                                %>
                                <td class="green-f"><%=OrderSetStatus.WAITSET.getMessage()%>
                                </td>
                                <%
                                } else if (orderHoldToStoreVo.getOrderSetStatus() == OrderSetStatus.WAITDRIVERGETED) {
                                %>
                                <td class="purple"><%=OrderSetStatus.WAITDRIVERGETED.getMessage()%>
                                </td>
                                <%
                                } else if (orderHoldToStoreVo.getOrderSetStatus() == OrderSetStatus.DRIVERGETED) {
                                %>
                                <td class="d-red"><%=OrderSetStatus.DRIVERGETED.getMessage()%>
                                </td>
                                <%
                                } else if (orderHoldToStoreVo.getOrderSetStatus() == OrderSetStatus.ARRIVEDISPATCH) {
                                %>
                                <td class="black"><%=OrderSetStatus.ARRIVEDISPATCH.getMessage()%>
                                </td>
                                <%
                                } else if (orderHoldToStoreVo.getOrderSetStatus() == OrderSetStatus.WAITDISPATCHSET) {
                                %>
                                <td class="l-blue"><%=OrderSetStatus.WAITDISPATCHSET.getMessage()%>
                                </td>
                                <%
                                } else if (orderHoldToStoreVo.getOrderSetStatus() == OrderSetStatus.WAITDRIVERTOSEND) {
                                %>
                                <td class="d-blue"><%=OrderSetStatus.WAITDRIVERTOSEND.getMessage()%>
                                </td>
                                <%
                                } else if (orderHoldToStoreVo.getOrderSetStatus() == OrderSetStatus.DRIVERSENDING) {
                                %>
                                <td class="d-blue"><%=OrderSetStatus.DRIVERSENDING.getMessage()%>
                                </td>
                                <%
                                } else if (orderHoldToStoreVo.getOrderSetStatus() == OrderSetStatus.ARRIVED) {
                                %>
                                <td class="c-green"><%=OrderSetStatus.ARRIVED.getMessage()%>
                                </td>
                                <%
                                    }
                                %>
                            </tr>
                            <%
                                }
                            } else {
                            %>
                            <tr>
                                <td colspan="7">没有符合查询条件的数据</td>
                            </tr>
                            <%
                                }
                            %>
                            </tbody>
                            <tfoot>
                            <div class="j-pl-pop modal fade" id="toSitePrompt" tabindex="-1" role="dialog"
                                 aria-hidden="true">
                                <div class="modal_wrapper">
                                    <div class="modal-dialog b-modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header b-modal-header">
                                                <button type="button" class="close" data-dismiss="modal"><span
                                                        aria-hidden="true">&times;</span><span
                                                        class="sr-only">Close</span></button>
                                                <h4 class="modal-title  tc" id="activeUserLabel">确认</h4>
                                            </div>
                                            <div class="modal-body b-modal-body" id="popContent">
                                                确认批量到站？<br>
                                                该操作会把选中的订单设置为已到站。
                                            </div>
                                            <div class="modal-footer">
                                                <div class="row mt20">
														<span class="col-md-6">
															<button type="button" class="ser-btn g wp100"
                                                                    data-dismiss="modal" onclick="cancelToSite()">取消
                                                            </button>
														</span>
														<span class="col-md-6">
                                                            <button type="button"
                                                                    class="ser-btn wp100 l"
                                                                    onclick="toStore()">确认
                                                            </button>

														</span>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            </tfoot>
                        </table>
                    </div>
                    <!-- E table -->
                    <!-- S tableBot -->
                    <div class="clearfix pad20">
                        <div id="pagin"></div>
                        <!-- E page -->
                    </div>
                    <!-- E tableBot -->
                </div>

            </div>
            <!-- E detail -->
        </div>
    </div>
</div>
<!-- E content -->
</body>
<script type="text/javascript">
    var type="-1";
    //显示分页条
    var pageStr = paginNav(<%=orderHoldPageModels.getPageNo()%>, <%=orderHoldPageModels.getTotalPages()%>, <%=orderHoldPageModels.getTotalCount()%>);
    $("#pagin").html(pageStr);


    //加载带有查询条件的指定页的数据
    function gotoPage(pageIndex) {
        var flag = false;
        var embraceId = $("#embrace option:selected").val();
        var orderSetStatus = $('#orderSetStatus option:selected').val();
        $.ajax({
            type: "GET",  //提交方式
            url: "<c:url value="/holdToStoreController/getList" />",//路径
            data: {
                "pageIndex": pageIndex,
                "orderSetStatus": orderSetStatus,
                "type": type,
                "embraceId": embraceId
            },//数据，这里使用的是Json格式进行传输
            success: function (dataObject) {//返回数据根据结果进行相应的处理
                var tbody = $("#dataList");
                // 清空表格数据
                //tbody.html("");
                var dataList = dataObject.datas;

                if (dataList != null) {
                    var datastr = "";
                    for (var i = 0; i < dataList.length; i++) {
                        datastr += getRowHtml(dataList[i]);
                    }

                    tbody.html(datastr);
                }
                //更新分页条
                var pageStr = paginNav(pageIndex, dataObject.totalPages, dataObject.totalCount);
                $("#pagin").html(pageStr);
                //更新统计数量
                updateOrderHoldToStoreNumVO();
            },
            error: function () {
                ioutDiv("加载分页数据异常！");
            }
        });
    }


    //封装一行的数据
    function getRowHtml(data) {
        var row = "<tr>";
        if (data != null) {
            row += "<td>" + data.userName + "</td>";
            row += "<td>" + data.phone + "</td>";
            row += "<td>" + data.mailNum + "</td>";
            row += "<td>" + data.recieverName + "</td>";
            row += "<td>" + data.recieverPhone + "</td>";
            row += "<td class='tl'>" + data.recieverAddress + "</td>";
            if (data.orderSetStatus == "<%=OrderSetStatus.NOEMBRACE%>") {
                row += "<td  class='orange-a'>" + "<%=OrderSetStatus.NOEMBRACE.getMessage()%>" + "</td>";
            }
            else if (data.orderSetStatus == "<%=OrderSetStatus.SCANED%>") {
                row += "<td class='blue-d'>" + "<%=OrderSetStatus.SCANED.getMessage()%>" + "</td>";
            }
            else if (data.orderSetStatus == "<%=OrderSetStatus.WAITTOIN%>") {
                row += "<td class='orange'>" + "<%=OrderSetStatus.WAITTOIN.getMessage()%>" + "</td>";
            }
            else if (data.orderSetStatus == "<%=OrderSetStatus.WAITSET%>") {
                row += "<td class='green-f'>" + "<%=OrderSetStatus.WAITSET.getMessage()%>" + "</td>";
            }
            else if (data.orderSetStatus == "<%=OrderSetStatus.WAITDRIVERGETED%>") {
                row += "<td class='purple'>" + "<%=OrderSetStatus.WAITDRIVERGETED.getMessage()%>" + "</td>";
            }
            else if (data.orderSetStatus == "<%=OrderSetStatus.DRIVERGETED%>") {
                row += "<td class='d-red'>" + "<%=OrderSetStatus.DRIVERGETED.getMessage()%>" + "</td>";
            }
            else if (data.orderSetStatus == "<%=OrderSetStatus.ARRIVEDISPATCH%>") {
                row += "<td class='black'>" + "<%=OrderSetStatus.ARRIVEDISPATCH.getMessage()%>" + "</td>";
            }
            else if (data.orderSetStatus == "<%=OrderSetStatus.WAITDISPATCHSET%>") {
                row += "<td class='l-blue'>" + "<%=OrderSetStatus.WAITDISPATCHSET.getMessage()%>" + "</td>";
            }
            else if (data.orderSetStatus == "<%=OrderSetStatus.WAITDRIVERTOSEND%>") {
                row += "<td class='d-blue'>" + "<%=OrderSetStatus.WAITDRIVERTOSEND.getMessage()%>" + "</td>";
            }
            else if (data.orderSetStatus == "<%=OrderSetStatus.DRIVERSENDING%>") {
                row += "<td class='d-blue'>" + "<%=OrderSetStatus.DRIVERSENDING.getMessage()%>" + "</td>";
            }
            else if (data.orderSetStatus == "<%=OrderSetStatus.ARRIVED%>") {
                row += "<td class='c-green'>" + "<%=OrderSetStatus.ARRIVED.getMessage()%>" + "</td>";
            }

        } else {
            row += "<td colspan='7'>" + 没有符合查询条件的数据 + "</td>"
        }
        row += "</tr>";
        return row;
    }


    //查询按钮事件
    function searchOrder() {
        type="-1";
        var embraceId = $("#embraceId").val();
        gotoPage(0);
    }
    //历史未入库
    function historyNoToStore() {
        type = "1";
        gotoPage(0);
        $('#orderSetStatus').val("-1");

    }
    //今日成功接单数
    function todaySuccessOrder() {
        type = "0";
        gotoPage(0);
        $('#orderSetStatus').val("-1");

    }
    //今日已入库
    function todayToStore() {
        type = "2";
        gotoPage(0);
        $('#orderSetStatus').val("-1");

    }
    //今日未入库
    function todayNoToStore() {
        type = "3";
        gotoPage(0);
        $('#orderSetStatus').val("-1");

    }
    //确定按钮
    function toStore() {

        $("#mailNumP").html("");
        $("#mailNumP").attr("style", "display:none");
        $("#toSitePrompt").modal("hide");
    }
    //回车事件
    function enterPress(e) {
        if (!e) e = window.event;//火狐中是 window.event
        if ((e.keyCode || e.which) == 13) {
            if ($("#mailNum").val() != null && $("#mailNum").val() != "") {
                checkOrderByMailNum1($("#mailNum").val());
            }
        }
    }

    //检查运单号 ,并进行到站，入库操作
    function checkOrderByMailNum1(mailNum) {

        if (mailNum != "") {
            $.ajax({
                url: "<%=request.getContextPath()%>/holdToStoreController/checkHoldToStoreByMailNum?mailNum=" + mailNum,
                type: 'GET',
                cache: false,
                success: function (response) {

                    if (response.status == false) {
                        console.log(response.status + "aa");
                        $("#mailNumP").html(response.msg);
                        $("#mailNumP").attr("style", "color:red");
                    } else {
                        console.log(response.status + "bb");
                        $("#popContent").html(response.msg);
                        $("#toSitePrompt").modal("show");
                        $("#mailNumP").attr("style", "");
                    }
                    //刷新页面
                    gotoPage(0);
                },
                error: function () {
                    console.log("error======");
                    $("#mailNumP").html("【异常扫描】不存在此运单号");
                    $("#mailNumP").attr("style", "color:red");
                }
            });
        }
    }
    //取消到站操作
    function cancelToSite() {
        $("#toSitePrompt").modal("hide");
    }

    //更新数据统计的数据
    function updateOrderHoldToStoreNumVO() {
        $.ajax({
            url: '<%=request.getContextPath()%>/holdToStoreController/updateOrderHoldToStoreNumVO',
            type: 'GET',
            cache: false,
            dataType: "json",
            data: {},
            success: function (response) {
                if (response != null && response != "") {

                    $("#successOrderNum").html(response.successOrderNum);
                    $("#todayNoToStoreNum").html(response.todayNoToStoreNum);
                    $("#historyToStoreNum").html(response.historyToStoreNum);
                    $("#todayToStoreNum").html(response.todayToStoreNum);
                }
            },
            error: function () {
            }
        });
    }

</script>
</body>
</html>
