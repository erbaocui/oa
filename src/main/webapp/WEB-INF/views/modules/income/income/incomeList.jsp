<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收款管理</title>
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
		<li class="active"><a href="${ctx}/income/income/">收款列表</a></li>
		<shiro:hasPermission name="income:income:edit"><li><a href="${ctx}/income/income/form">收款添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="income" action="${ctx}/income/income/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>甲方名称：</label>&nbsp;<form:input path="contract.firstParty" value="${contract.firstParty}" htmlEscape="false" maxlength="50" class="input-xlarge"/></li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<shiro:hasPermission name="income:income:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="income">
			<tr>
				<shiro:hasPermission name="income:income:edit"><td>
    				<a href="${ctx}/income/income/form?id=${income.id}">修改</a>
					<a href="${ctx}/income/income/delete?id=${income.id}" onclick="return confirmx('确认要删除该收款吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>