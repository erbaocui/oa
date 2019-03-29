<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>进款分配部门审核</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        $(document).ready(function() {

        });
		function commit(state){
            $("#state").val(state);
            $("#inputForm").submit();
        }


	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li class="active"><a href="${ctx}/income/distOffice/">进款部门审核</a></li>

</ul><br/>
<sys:message content="${message}"/>
<div class="form-actions">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span5">
				<label >本次收款额：</label>
				${income.value}
			</div>
			<div class="span5">
				<label >合同名称：</label>
				${income.contract.name}
			</div>
			<div class="span1">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span5">
				<label >合同编码：</label>
				${income.contract.code}
			</div>
			<div class="span5">
				<label >合同金额：</label>
				${income.contract.value}
			</div>
			<div class="span1">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span10">
				<label >项目名称：</label>
				${income.contract.project.name}
			</div>
			<div class="span1">
			</div>
		</div>
	</div>
</div>



<form:form id="inputForm" modelAttribute="distOfficeProc" action="${ctx}/income/distProc/officeAuditSubmit" method="post" class="form-horizontal">
	<form:hidden path="id"/>
	<form:hidden path="taskId"  value="${taskId}"/>
	<form:hidden path="state"  value=""/>

	<div id="content" >
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead><tr><th>类型</th><th>分配部门</th><th>部门金额</th><th>规则内费用</th><th>费用项</th><th>明细</th></tr></thead>
			<c:set var="officeIds" value=""></c:set>
			<c:forEach items="${distOffices}" var="distOffice">

				<tr>
				<td rowspan="${distOffice.rowspan}">${fns:getDictLabel(distOffice.type, 'income_dist_type', '无')}</td>
				<td rowspan="${distOffice.rowspan}">${distOffice.officeName}</td>
				<td rowspan="${distOffice.rowspan}">${distOffice.value}</td>

				<c:if test="${distOffice.ruleGroupId== null||distOffice.ruleGroupId==''}">
					<c:set value="false" var="saveFlag" />
				</c:if>
				<c:if test="${distOffice.rules == null}">
					<td ></td><td ></td><td ></td>
				</c:if>
				<c:forEach items="${distOffice.rules}" var="rule">
					<td rowspan="${rule.rowspan}">${rule.value}</td>
					<c:forEach items="${rule.roleItems}" var="roleItem" varStatus="status">
						<c:if test="${status.index != 0}">
							<tr>
						</c:if>
						<td rowspan="${roleItem.rowspan}">${roleItem.name}:${roleItem.value}</td>
						<c:forEach items="${roleItem.distributes}" var="distribut">
							<td>${distribut.name}:${distribut.value}</td>
							</tr>
						</c:forEach>
					</c:forEach>
				</c:forEach>


				</tr>


			</c:forEach>

		</table>
	</div>
	    <div class="form-actions">
			<div class="container-fluid">

				<div class="row-fluid">
					<div class="span1">
					</div>
					<div class="span10">
						<label >部门意见:</label>
						<form:textarea path="comment" htmlEscape="false" rows="3" maxlength="500" class="input-xxlarge" />
						<div class="span1">
						</div>
					</div>
					<div class="row-fluid">
						<div class="span12">
						</div>
					</div>
					<div class="row-fluid">
						<div class="span4">
						</div>
						<div class="span2">
							<input id="btnAgree" class="btn btn-primary" type="button"  onclick="commit(1)" value="同意" />
						</div>
						<div class="span2">
							<input id="btnDisagree" class="btn btn-primary" type="button"  onclick="commit(2)" value="不同意" />
						</div>

						<div class="span4">
						</div>
					</div>
				</div>
			</div>
		</div>


</form:form>

<br>
<%@ include file="/WEB-INF/views/modules/act/comment.jsp" %>
</body>
</html>