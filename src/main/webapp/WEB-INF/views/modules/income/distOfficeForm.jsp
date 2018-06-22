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
            $("#inputForm").attr("action", "${ctx}/income/distOffice/form");
            $("#inputForm").submit();
        }

        function save() {
            $("#inputForm").attr("action", "${ctx}/income/distOffice/saveDist");
            $("#inputForm").submit();
        }
        function saveAccount() {
            $("#inputForm").attr("action", "${ctx}/income/distOffice/saveAccount");
            $("#inputForm").submit();
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/income/distOffice/">分配</a></li>

	</ul><br/>
	<c:set var="saveFlag" value="true"></c:set>
	<form:form id="inputForm" modelAttribute="distOffice" action="${ctx}/income/distOffice/form" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="incomeId" value="${incomeId}"/>
		<sys:message content="${message}"/>
		<div id="content" >
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead><tr><th>分配部门</th><th>部门金额</th><th>规则选择</th><th>规则内费用</th><th>费用项</th><th>明细</th></tr></thead>
			<c:forEach items="${distOffices}" var="distOffice">

					<tr>
						<td rowspan="${distOffice.rowspan}">${distOffice.officeName}</td>
						<td rowspan="${distOffice.rowspan}">${distOffice.value}</td>
						<td rowspan="${distOffice.rowspan}">
							<c:if test="${editable=='true'}">
								<select name="groups" onchange="test()" >
							</c:if>
								<c:if test="${editable !='true'}">
								<select name="groups" onchange="test()" disabled="disabled" >
									</c:if>
								<c:forEach items="${distOffice.ruleGroups}" var="ruleGroup">

									<c:choose>
										<c:when test="${distOffice.ruleGroupId == ruleGroup.id}">
									     <option value="${ruleGroup.id}" selected="selected" >${ruleGroup.name}</option>
										</c:when>
										<c:otherwise>
											<option value="${ruleGroup.id}">${ruleGroup.name}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
						    </select>
						</td>
						<c:if test="${distOffice.ruleGroupId== null||distOffice.ruleGroupId==''}">
							<c:set value="false" var="saveFlag" />
						</c:if>
				       <c:if test="${distOffice.rules == null}">
						<td ></td><td ></td><td ></td>
					   </c:if>
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
			</c:forEach>
			</table>
		</div>
		<c:if test="${editable=='true'}">
			<div class="form-actions">
				<c:if test="${saveFlag==true}">
					<input id="btnSubmit" class="btn btn-primary" type="button" value="保 存" onclick="save()"/>&nbsp;
					<input id="btnSubmit" class="btn btn-primary" type="button" value="保存账户" onclick="saveAccount()"/>&nbsp;
				</c:if>

			</div>
		</c:if>
	</form:form>
</body>
</html>