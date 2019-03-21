<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>培训管理</title>
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
		<li class="active"><a href="${ctx}/sys/train/">培训列表</a></li>
		<shiro:hasPermission name="sys:train:edit"><li><a href="${ctx}/sys/train/form">培训添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="train" action="${ctx}/sys/train/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li><label>专业：</label>
				<form:select path="professional" class="input-medium">
					<form:option value="" label=""/>
					<form:options items="${fns:getDictList('professional')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>培训名称：</label>
				<form:input path="name" htmlEscape="false" maxlength="200" class="input-medium"/>
			</li>
			<!--
			<li><label>日期：</label>
				<input name="date" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate"
					value="<fmt:formatDate value="${train.date}" pattern="yyyy-MM-dd HH:mm:ss"/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			</li>
			-->
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>专业</th>
				<th>培训名称</th>
				<th>日期</th>
				<th>备注</th>
				<shiro:hasPermission name="sys:train:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="train">
			<tr>
				<td><a href="${ctx}/sys/train/form?id=${train.id}">
					${fns:getDictLabel(train.professional, 'professional', '')}
				</a></td>
				<td>
					${train.name}
				</td>
				<td>
					<fmt:formatDate value="${train.date}" pattern="yyyy-MM-dd HH:mm"/>
				</td>
				<td>
					${train.remark}
				</td>
				<shiro:hasPermission name="sys:train:edit"><td>
					<a href="${ctx}/sys/train/assign?id=${train.id}">人员</a>
    				<a href="${ctx}/sys/train/form?id=${train.id}">修改</a>
					<a href="${ctx}/sys/train/delete?id=${train.id}" onclick="return confirmx('确认要删除该培训吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>