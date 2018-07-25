<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>账户流水管理</title>
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
		<li class="active"><a href="${ctx}/income/accountFlow/">账户流水列表</a></li>
		<shiro:hasPermission name="income:accountFlow:edit"><li><a href="${ctx}/income/accountFlow/form">账户流水添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="accountFlow" action="${ctx}/income/accountFlow/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			 <li>
			 <label>日期范围：&nbsp;</label>
				 <input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="input-mini Wdate"
											 value="<fmt:formatDate value="${accountFlow.beginDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
			     &nbsp;&nbsp;---&nbsp;&nbsp;
				 <input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="input-mini Wdate"
																value="<fmt:formatDate value="${accountFlow.endDate}" pattern="yyyy-MM-dd"/>" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>&nbsp;&nbsp;
			 </li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>账户</th>
				<th>合同</th>
				<th>操作</th>
				<th>时间</th>
				<th>金额</th>
				<shiro:hasPermission name="income:accountFlow:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="accountFlow">
			<tr>
				<td>
						${accountFlow.account.name}
				</td>
				<td>
						${accountFlow.contract.name}
				</td>
				<td>
						${fns:getDictLabel(accountFlow.type, 'income_account_act_type', '无')}
				</td>
				<td>
					<fmt:formatDate value="${accountFlow.createDate}" type="both"/>
				</td>
				<td>
					${accountFlow.value}
			   </td>
				<shiro:hasPermission name="income:accountFlow:edit"><td>
    				<a href="${ctx}/income/accountFlow/form?id=${accountFlow.id}">修改</a>
					<a href="${ctx}/income/accountFlow/delete?id=${accountFlow.id}" onclick="return confirmx('确认要删除该账户流水吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>