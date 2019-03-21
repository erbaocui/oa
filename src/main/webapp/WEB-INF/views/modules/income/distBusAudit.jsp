<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>进款经营审核</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        $(document).ready(function() {

        });
        function commit(state){
            //$("#inputForm").attr("action", "${ctx}/income/distProc/officeAuditSubmit");
            $("#state").val(state);
            $("#inputForm").submit();
        }


	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li class="active"><a href="${ctx}/income/distOffice/">进款经营审核</a></li>

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



<form:form id="inputForm" modelAttribute="distOfficeProc" action="${ctx}/income/distProc/busAuditSubmit" method="post" class="form-horizontal">
	<form:hidden path="id"/>
	<form:hidden path="incomeId" value="${incomeId}"/>
	<form:hidden path="taskId"  value="${taskId}"/>
	<form:hidden path="state"  value=""/>

	<div id="content" >
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead><tr><th>分配部门</th><th>部门金额</th><th>规则选择</th><th>规则内费用</th><th>费用项</th><th>明细</th></tr></thead>
			<c:set var="officeIds" value=""></c:set>
			<c:forEach items="${distOffices}" var="distOffice">

				<tr>
				<td rowspan="${distOffice.rowspan}">${distOffice.officeName}</td>
				<td rowspan="${distOffice.rowspan}">${distOffice.value}</td>
				<td rowspan="${distOffice.rowspan}">

					<select name="groups" onchange="selRule()"  disabled="disabled">
						<c:forEach items="${distOffice.ruleGroups}" var="ruleGroup">

							<c:choose>
								<c:when test="${distOffice.ruleGroupId == ruleGroup.id}">
									<option value="${ruleGroup.id}" selected="selected" >${ruleGroup.name}</option>
								</c:when>
								<c:otherwise>
									<option value="${ruleGroup.id}">${ruleGroup.name}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
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
					<label>审批意见:</label>
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
						<input id="btnAgree" class="btn btn-primary" type="button"  onclick="commit(1)" value="通过" />
					</div>
					<div class="span2">
						<input id="btnDisagree" class="btn btn-primary" type="button"  onclick="commit(2)" value="驳回" />
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