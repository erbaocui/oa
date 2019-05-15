<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同拆分细化-合同复核</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        function commit(state){
            $("#state").val(state);
            $("#inputForm").submit();
        }


	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li class="active"><a href="#">合同拆分细化-合同复核</a></li>

</ul><br/>
<sys:message content="${message}"/>
<div class="form-actions">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span3">
				<label >项目名称：</label>
				${contract.project.name}
			</div>
			<div class="span3">
				<label >合同名称：</label>
				${contract.name}
			</div>
			<div class="span3">
				<label >合同金额：</label>
				${contract.value}

			</div>
			<div class="span2">
			</div>
		</div>
	</div>
</div>




<form:form id="inputForm" modelAttribute="baseReview" action="${ctx}/cont/split/proc/audit/submit" method="post" class="form-horizontal">


	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span10">
				<table title="合同拆分列表" class="table table-striped table-bordered table-condensed">
					<thead>
					<tr>
						<th>类型</th>
						<th>金额</th>
						<th>创建时间</th>
						<th>备注</th>
					</tr>
					</thead>
					<tbody>
					<c:set var="sum" value="0"></c:set>
					<c:forEach items="${contSplitItems}" var="contSplitItem">
						<tr>
							<td  width="20%">${fns:getDictLabel(contSplitItem.type, 'contract_split_type', '无')}</td>
							<td  width="20%"> ${contSplitItem.value}</td>
							<td  width="20%"><fmt:formatDate value="${contSplitItem.createDate}" type="both"/></td>
							<td  width="40%">${contSplitItem.remark}</td>

						</tr>
						<c:set value="${sum + contSplitItem.value}" var="sum" />
					</c:forEach>
					<tr>
						<td width="100%" colspan="6">合计:${sum}</td>
					</tr>
					</tbody>
					<input type="hidden" id="itemSum" value="${sum}" />
				</table>
			</div>
			<div class="span1">
			</div>
		</div>
	</div>
	<div class="form-actions">
		<div class="container-fluid">

			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label>审批意见:</label>
					<form:textarea path="comment" htmlEscape="false" rows="3" maxlength="500" class="input-xxlarge" />

				</div>
				<div class="span1">
				</div>
			</div>
				<div class="row-fluid">
					<div class="span12">
					</div>
				</div>
				<div class="row-fluid">
					<div class="span3">
					</div>
					<div class="span2">
						<input id="btnAgree" class="btn btn-primary" type="button"  onclick="commit(1)" value="通过" />
					</div>
					<div class="span2">
						<input id="btnDisagree" class="btn btn-primary" type="button"  onclick="commit(2)" value="驳回" />
					</div>

					<div class="span5">
					</div>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" id="state" name="state" />
	<input type="hidden" id="taskId" name="taskId" value="${taskId}"/>



</form:form>
<br>
<%@ include file="/WEB-INF/views/modules/act/comment.jsp" %>


</body>
</html>