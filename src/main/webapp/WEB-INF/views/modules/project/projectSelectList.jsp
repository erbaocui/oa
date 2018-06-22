<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>项目管理</title>
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
        function check(id,name){

            parent.top.$.jBox.close();
           //window.parent.getEl
		}
	</script>
</head>
<body style="overflow:hidden" scroll="no" >
	<form:form id="searchForm" modelAttribute="project" action="${ctx}/project/project/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>项目代码：</label>
				<form:input path="code" htmlEscape="false" maxlength="255" class="input-medium"/>
			</li>
			<li><label>项目名称：</label>
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
				<th>选择</th>
				<th>名称</th>
				<th>编码</th>
				<th>归属部门</th>
				<th>甲方名称</th>
				<th>项目经理</th>
				<%--<shiro:hasPermission name="project:project:edit"><th>操作</th></shiro:hasPermission>--%>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="project">
			<tr>
				<td><input type="radio" name="prjSel" onclick="check('${project.name}','${project.name}')" /></td>
				<td><%--<a href="${ctx}/project/project/form?id=${project.id}"></a>--%>
					${project.name}
			    </td>
				<td>${project.code}</td>
				<td>${project.office.name}</td>
				<td>${project.custom}</td>
				<td>${project.manager}</td>
				<%--<shiro:hasPermission name="project:project:edit"><td>
    				<a href="${ctx}/project/project/form?id=${project.id}">修改</a>
					<a href="${ctx}/project/project/delete?id=${project.id}" onclick="return confirmx('确认要删除该项目吗？', this.href)">删除</a>
				</td></shiro:hasPermission>--%>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>