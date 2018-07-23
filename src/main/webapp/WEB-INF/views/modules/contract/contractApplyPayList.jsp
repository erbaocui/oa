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
		<li class="active"><a href="${ctx}/cont/applyPay/incomeList">收款列表</a></li>

	</ul>
	<form:form id="searchForm" modelAttribute="contApply" action="${ctx}/contract/contApply/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>甲方名称</th>
				<th>请款金额</th>
				<th>收款金额</th>
				<th>状态</th>
				<th>备注</th>
                 <th>操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="contApply">
			<tr>
				<td>
					${contApply.receiptName}
				</td>
				<td>
					${contApply.receiptValue}
				</td>
				<td>
						${contApply.income}
				</td>
				<td>
						${fns:getDictLabel(contApply.status, 'contract_applyPay_status', '无')}
				</td>
				<td>
						${contApply.remark}
				</td>
				<td>
					<c:if test="${contApply.status ==3}">
						<a href="${ctx}/income/income/applyPay?id=${contApply.id}">收款</a>
					</c:if>
				</td>

			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>