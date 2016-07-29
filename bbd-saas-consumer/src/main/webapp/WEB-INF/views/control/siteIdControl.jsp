<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
</head>
<body>
<div id="addr_control" class="form-group col-xs-12 col-sm-12 col-md-12 col-lg-12">
	<div class="form-group pb20">
		<label>　省：</label>
		<select name="prov" class="prov form-control form-con-new">
		</select>
	</div>
	<div class="form-group pb20">
		<label id="cityLable" hidden>　市：</label>
		<select  class="city form-control form-con-new" disabled="disabled">
		</select>
	</div>
	<div class="form-group pb20">
		<label id="distLable" hidden>　区：</label>
		<select name="dist" class="dist form-control form-con-new"  disabled="disabled">
		</select>
	</div>
	<div class="form-group pb20">
		<label class="ml16">站点：</label>
		<div class="crt-s w400">
			<div class="c-sel j-sel-input2">
				<input id="siteName_control" type="text" class="sel-input j-empty" placeholder="请输入站点名称"  value="全部"/>
				<div class='showA'><ul class='c-show cityshow' id="options"></ul></div>
			</div>
			<div class="all-area all-area2 pm-dn">
				<!-- S 1 -->
				<div class="pv-bg clearfix">
					<div class="l-sel-p">
						<ul class="pv-part" id="optionList">
							<li>
								<label class="f12 linputC">
									<input type="checkbox" name="idOpt" value="" onclick="selectAll(this)"><b>全部</b>
								</label>
							</li>
							<c:if test="${not empty siteList}">
								<c:forEach var="option" items="${siteList}">
									<li>
										<label class="f12 linputC">
											<input type="checkbox" name="idOpt" value="${option.id}"><b>${option.name}</b>
										</label>
									</li>
								</c:forEach>
							</c:if>
						</ul>
					</div>
				</div>
				<!-- E 1 -->
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
	var isSiteId = true;
</script>
</body>
</html>
