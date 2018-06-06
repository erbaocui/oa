<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>账户管理</title>
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
		<li class="active"><a href="${ctx}/income/account/">账户列表</a></li>
		<shiro:hasPermission name="income:account:edit"><li><a href="${ctx}/income/account/form">账户添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="account" action="${ctx}/income/account/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>部门：</label><sys:treeselect id="office" name="office.id" value="${account.office.id}" labelName="office.name" labelValue="${account.office.name}"
													title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li><label>名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>归属机构</th>
				<th>账户名称</th>
				<th>账户名称</th>
				<th>备注</th>
				<shiro:hasPermission name="income:account:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="account">
			<tr>
				<td>
						${account.office.name}
				</td>
				<td>
					${account.name}
				</td>
				<td>
						${account.value}
				</td>
				<td>
					${account.remarks}
				</td>
				<shiro:hasPermission name="income:account:edit"><td>
    				<a href="${ctx}/income/account/form?id=${account.id}">修改</a>
					<a href="${ctx}/income/account/delete?id=${account.id}" onclick="return confirmx('确认要删除该账户吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>