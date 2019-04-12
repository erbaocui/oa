<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同管理</title>
	<meta name="decorator" content="default"/>
<%--	<link href="${ctxStatic}/jqGrid/4.6/css/ui.jqgrid.css" type="text/css" rel="stylesheet" />
	<script src="${ctxStatic}/jqGrid/4.7/js/jquery.jqGrid.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqGrid/4.7/js/jquery.jqGrid.extend.js" type="text/javascript"></script>--%>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
        function select(id) {
		    //alert(id);
            window.parent.window.contractId=id;
            parent.$.jBox.close();
        }


	</script>
</head>
<body>
	<%--<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cont/base/list">合同列表</a></li>
	</ul>--%>
	<form:form id="searchForm" modelAttribute="queryContract" action="${ctx}/cont/base/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">


			<li><label>合同名称：</label>&nbsp;<form:input path="name" value="${name}" htmlEscape="false" maxlength="50" class="input-small"/></li>
			<li class="btns">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>name</th>
				<th>value</th>
				<shiro:hasPermission name="cont:base:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>

		<c:forEach items="${page.list}" var="contract">
			<tr>
				<td><a href="${ctx}/cont/base/form?id=${contract.id}">
					${contract.name}
				</a></td>
				<td>
					${contract.value}
				</td>
				<shiro:hasPermission name="cont:base:edit"><td>
					<a href="#" onclick="select('${contract.id}');">添加</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>

	</table>
	<div class="pagination">${page}</div>
</body>
</html>