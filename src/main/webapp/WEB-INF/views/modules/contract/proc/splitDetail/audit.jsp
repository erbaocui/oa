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
			<div class="span5">
				<label >项目名称：</label>
				${contract.project.name}
			</div>
			<div class="span5">
				<label >合同名称：</label>
				${contract.name}
			</div>
			<div class="span1">
			</div>
		</div>
	</div>
</div>




<form:form id="inputForm" modelAttribute="baseReview" action="${ctx}/cont/split/detail/proc/audit/submit" method="post" class="form-horizontal">


	<div class="row-fluid">
		<div class="span1">
		</div>
		<div class="span10">
			<table title="合同拆分细化项目" class="table table-striped table-bordered table-condensed">
				<thead>
				<tr>
					<th>业务类型</th>
					<th>业务金额</th>
					<th>部门</th>
					<th>部门金额</th>
					<th>备注</th>
				</tr>
				</thead>
				<tbody>
				<c:set var="itemSum" value="0"></c:set>
				<c:forEach items="${contSplitDetailItems}" var="contSplitDetailItem">
					<tr>
						<td width="15%" rowspan="${contSplitDetailItem.officeList.size()}">${fns:getDictLabel(contSplitDetailItem.type, 'contract_split_detail_type', '无')}</td>
					    <td width="10%" rowspan="${contSplitDetailItem.officeList.size()}">${contSplitDetailItem.value}</td>


					<c:forEach items="${contSplitDetailItem.officeList}" var="office" varStatus="status">
							<c:if test="${status.index != 0}">
								<tr>
							</c:if>
							<td width="20%"> ${office.office.name}</td>
							<td width="10%"> ${office.value}</td>
						    <td width="35%">${office.remark}</td>
							</tr>
						</c:forEach>

					<c:set value="${itemSum + contSplitDetailItem.value}" var="itemSum" />
				</c:forEach>
				<input type="hidden" id="itemSum" value="${itemSum}" />
				<tr>
					<td width="100%" colspan="5">合计:${itemSum}</td>
				</tr>
				</tbody>
			</table>
		</div>
		<div class="span1">
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