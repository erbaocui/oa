<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收款分配</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});

			// 请求
           /* $.post("<%--${ctx}--%>/income/distOffice/distInit",{incomeId:"1"},
                function(result){
                    $.each(result, function(key, val){
                        $("#content").append('<table id="contentTable" class="table table-striped table-bordered table-condensed">');
                        $("#content").append("<thead><tr><th>分配部门</th><th>部门金额</th><th>规则分段</th><th>明细</th></tr></thead>");
                        $("#content").append("</table>");
                       // $("#content").listview("refresh");   //在使用'ul'标签时才使用，作用:刷新列表
						$("#contentTable").trigger("create");

                    });
                });*/
		});
		function test() {
            $("#inputForm").submit();
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/income/distOffice/">分配</a></li>

	</ul><br/>
	<form:form id="inputForm" modelAttribute="distOffice" action="${ctx}/income/distOffice/form" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div id="content" >
			<c:forEach items="${distOffices}" var="distOffice">
				<table id="contentTable" class="table table-striped table-bordered table-condensed">
					<thead><tr><th>分配部门</th><th>部门金额</th><th>规则选择</th><th>规则内费用</th><th>费用项</th><th>明细</th></tr></thead>
					<tr>
						<td rowspan="${distOffice.rowspan}">${distOffice.officeName}</td>
						<td rowspan="${distOffice.rowspan}">${distOffice.value}</td>
						<td rowspan="${distOffice.rowspan}">

							<select name="groups" onchange="test()">
								<c:forEach items="${distOffice.ruleGroups}" var="ruleGroup">

									<c:choose>
										<c:when test="${distOffice.ruleGroupId == ruleGroup.id}">
									     <option value="${ruleGroup.id}" selected="selected" >${ruleGroup.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${ruleGroup.id}">${ruleGroup.name}</option>
										</c:otherwise>
									</c:choose>
								<%--	<c:if test="${distOffice.ruleGroupId == ruleGroup.id}">
										<option value="${ruleGroup.id}">${ruleGroup.name}</option>
									</c:if>
									<option value="${ruleGroup.id}">${ruleGroup.name}</option>--%>
								</c:forEach>
						    </select>
						</td>
						<c:forEach items="${distOffice.rules}" var="rule">

							<td rowspan="${rule.rowspan}">${rule.value}</td>
						    <c:forEach items="${rule.roleItems}" var="roleItem" varStatus="status">
								<c:if test="${status.index != 0}">
								  <tr>
								</c:if>
								<td rowspan="${roleItem.rowspan}">${roleItem.name}:${roleItem.value}</td>
								<c:forEach items="${roleItem.distributes}" var="distribut">
									<td>${distribut.name}:${distribut.value}</td>
									</tr>
								</c:forEach>
							</c:forEach>

						</c:forEach>

					</tr>
				</table>
			</c:forEach>
		</div>
		<%--<div class="control-group">
			<label class="control-label">分配部门：</label>
			<div class="controls">
				<sys:treeselect id="office" name="office.id" value="${distOffice.office.id}" labelName="office.name" labelValue="${distOffice.office.name}"
					title="部门" url="/sys/office/treeData?type=2" cssClass="" allowClear="true" notAllowSelectParent="true" disabled="disabled"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">分配金额：</label>
			<div class="controls">
				<form:input path="value" htmlEscape="false" maxlength="255" class="input-xlarge " readonly="true"/>
			</div>
		</div>--%>
		<div class="form-actions">
			<shiro:hasPermission name="income:distOffice:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="test();"/>
		</div>
	</form:form>
</body>
</html>