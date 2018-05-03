<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请款管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/income/applyPay/">请款列表</a></li>
		<shiro:hasPermission name="income:applyPay:edit"><li><a href="${ctx}/income/applyPay/form">请款添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="applyPay" action="${ctx}/income/applyPay/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>请款名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>甲方名称：</label>
				<form:input path="firstParty" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>请款名称</th>
				<th>备注</th>
				<shiro:hasPermission name="income:applyPay:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="applyPay">
			<tr>
				<td><a href="${ctx}/income/applyPay/form?id=${applyPay.id}">
					${applyPay.name}
				</a></td>
				<td>
					${applyPay.remarks}
				</td>
				<shiro:hasPermission name="income:applyPay:edit"><td>

					<a href="${ctx}/income/applyPay/contDelete?id=${applyPay.id}" onclick="return confirmx('确认要删除该成功吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>