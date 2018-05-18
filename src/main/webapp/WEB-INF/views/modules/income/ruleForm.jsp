<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>分配规则管理</title>
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
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/income/rule/">分配规则列表</a></li>
		<li class="active"><a href="${ctx}/income/rule/form?id=${rule.id}">分配规则<shiro:hasPermission name="income:rule:edit">${not empty rule.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="income:rule:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="rule" action="${ctx}/income/rule/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>		
		<div class="control-group">
			<label class="control-label">名字：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>

	<%--	<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
			<tr>
				<th>name</th>
				<th>value</th>
				<shiro:hasPermission name="cont:base:edit"><th>操作</th></shiro:hasPermission>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${applyPay.contractList}" var="contract">
				<tr>
					<td>${contract.name}</td>
					<td>${contract.code}</td>
					<td>
						<table  class="table table-striped table-bordered table-condensed">
							<tr>
								<td>aaa</td>
								<td>bbbb</td>
							</tr>
							<tr>
								<td>aaa</td>
								<td>bbbb</td>
							</tr>

						</table>
					</td>
					<td>
						<a href="${ctx}/income/applyPay/contDelete?id=${applyPay.id}&contractId=${contract.id}" onclick="return confirmx('确认要删除该合同吗？', this.href)">删除</a>
					</td>
				</tr>
				<c:set var="sum" value="${sum},${contract.id}"></c:set>
			</c:forEach>
			<c:set var="length" value="${fn:length(sum)}"></c:set>
			<input id="notContractIds" type="hidden" value="${fn:substring(sum, 1,length)}">
			</tbody>
		</table>--%>
		<div class="form-actions">
			<shiro:hasPermission name="income:rule:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>