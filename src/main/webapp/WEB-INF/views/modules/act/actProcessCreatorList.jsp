<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>发起的流程</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function(){
			top.$.jBox.tip.mess = null;
		});
		function page(n,s){
        	location = '${ctx}/act/process/creator/?pageNo='+n+'&pageSize='+s;
        }

	</script>

</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/act/task/todo/">待办任务</a></li>
		<li ><a href="${ctx}/act/task/historic/">已办任务</a></li>
		<li class="active"><a href="${ctx}/act/process/creator/">我发起的流程</a></li>
	</ul>
	<sys:message content="${message}"/>
	<table class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>流程名称</th>
				<th>发起时间</th>
				<th>结束时间</th>
				<th>操作</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="procIns">
				<tr>

					<td>${procIns.processDefinitionName}</td>
					<td><fmt:formatDate value="${procIns.startTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td><fmt:formatDate value="${procIns.endTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td>
						<%--<a target="_blank" href="${pageContext.request.contextPath}/act/diagram-viewer?processDefinitionId=${procIns.processDefinitionId}&processInstanceId=${procIns.processInstanceId}">跟踪</a>--%>
                       <c:if test="${empty procIns.endTime}">
						<a target="_blank" href="${ctx}/act/process/tracingMap?procDefId=${procIns.processDefinitionId}&proInstId=${procIns.processInstanceId}">追踪</a>
					   </c:if>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>
