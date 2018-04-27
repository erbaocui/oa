<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});

		function review(state){
            $("#state").val(state);
            $("#reviewForm").submit();
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a>合同信息完善</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="contract" action="${ctx}/contract/contract/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>

	<div class="container-fluid">
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span3">
					<label >合同编号:</label>
					<form:input path="code"   htmlEscape="false" maxlength="50" class=" form-control input-small required" readonly="true"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
				<div class="span7">
					<label >合同名称:</label>
					<form:input path="name" htmlEscape="false" maxlength="50" class=" form-control input-xxlarge required" readonly="true"/>
				</div>
				<div class="span1">
				</div>
			</div>
		</div>
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="controls">
				</div>
				<div class="span3">

					<label >项目经理:</label>
					<sys:treeselect id="manager" name="manager.id" value="${contract.manager.id}" labelName="manager.name" labelValue="${contract.manager.name}" title="项目经理" url="/sys/office/treeData?type=3" cssClass="input-mini" allowClear="true" notAllowSelectParent="true" disabled="disabled"/>
				</div>
				<div class="span7">
					<label >项目名称:</label>
					<sys:treeselect id="project" name="project.id" value="${contract.project.id}" labelName="office.name" labelValue="${contract.project.name}" title="项目名称" url="/sys/office/treeData?type=4" cssClass="input-xxlarge" allowClear="true" notAllowSelectParent="true" disabled="disabled"/>
				</div>
				<div class="span1">
				</div>
			</div>
		</div>


		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span2">
					<label >合同类型:</label>
					<form:select path="type" class="form-control input-small" disabled="true">
						<form:option value="" label="请选择"/>
						<form:options items="${fns:getDictList('contract_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>

				</div>
				<div class="span2">
					<label >合同种类:</label>
					<form:select path="class"  class="form-control input-small" disabled="true">
						<form:options items="${fns:getDictList('contract_class')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				<div class="span2">
					<label >是否分包:</label>
					<form:select path="isSub" class="form-control input-small" disabled="true">
						<form:option value="" label="请选择"/>
						<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>
				<div class="span2">
					<label >合同状态:</label>
					<form:select path="status" class="form-control input-small" disabled="true">

						<form:options items="${fns:getDictList('contract_status')}" itemLabel="label" itemValue="value" htmlEscape="false" />
					</form:select>
				</div>

				<div class="span1">
				</div>

			</div>
		</div>

		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span2">
					<label >开始时间:</label>
					<input name="beginTime" type="text" readonly="readonly" maxlength="20" class="form-control input-small Wdate "
						   value="<fmt:formatDate value="${contract.beginTime}" pattern="yyyy-MM-dd"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" disabled="disabled"/>
				</div>
				<div class="span2">
					<label >结束时间:</label>
					<input name="endTime" type="text" readonly="readonly" maxlength="20" class="form-control input-small Wdate "
						   value="<fmt:formatDate value="${contract.endTime}" pattern="yyyy-MM-dd"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"  disabled="disabled"/></div>
				<div class="span2">
					<label >合同金额:</label>
					<form:input path="value" htmlEscape="false" class="form-control input-small" readonly="true"/>
				</div>
				<div class="span2">
					<label >已付金额:</label>
					<form:input path="income" htmlEscape="false" class="orm-control input-small" readonly="true"/>
				</div>
				<div class="span1">
				</div>

			</div>
		</div>

		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span2">
					<label >签约部门:</label>
					<sys:treeselect id="office" name="office.id" value="${contract.office.id}" labelName="office.name" labelValue="${contract.office.name}"
									title="部门" url="/sys/office/treeData?type=2" cssClass="input-mini" allowClear="true" notAllowSelectParent="true" disabled="disabled"/>
				</div>
				<div class="span2">
					<label >签约时间:</label>
					<input name="signedTime" type="text" readonly="readonly" maxlength="20" class="iform-control input-small Wdate"
						   value="<fmt:formatDate value="${contract.signedTime}" pattern="yyyy-MM-dd"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"  disabled="disabled"/></div>
				<div class="span2">
					<label>&nbsp;&nbsp;联&nbsp;系&nbsp;人:</label>
					<form:input path="contact" htmlEscape="false" maxlength="32" class="form-control input-small" readonly="true"/>
				</div>
				<div class="span2">
					<label >联系电话:</label>
					<form:input path="contactPhone" htmlEscape="false" maxlength="32" class="input-small" readonly="true"/>
				</div>
				<div class="span1">
				</div>

			</div>
		</div>
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span2">
					<label >盖章时间:</label>
					<input name="sealTime" type="text" readonly="readonly" maxlength="20" class="input-small Wdate "
						   value="<fmt:formatDate value="${contract.sealTime}" pattern="yyyy-MM-dd"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"  disabled="disabled"/>
				</div>
				<div class="span2">
					<label >备案时间:</label>
					<input name="recordTime" type="text" readonly="readonly" maxlength="20" class="input-small Wdate "
						   value="<fmt:formatDate value="${contract.recordTime}" pattern="yyyy-MM-dd"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" disabled="disabled"/>
				</div>
				<div class="span2">
					<label >返回时间:</label>
					<input name="returnTime" type="text" readonly="readonly" maxlength="20" class="input-small Wdate "
						   value="<fmt:formatDate value="${contract.returnTime}" pattern="yyyy-MM-dd"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" disabled="disabled"/>
				</div>
				<div class="span2">
					<label >财务时间:</label>
					<input name="returnFinancialTime" type="text" readonly="readonly" maxlength="20" class="input-small Wdate "
						   value="<fmt:formatDate value="${contract.returnFinancialTime}" pattern="yyyy-MM-dd"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" disabled="disabled"/>
				</div>
				<div class="span1">
				</div>

			</div>
		</div>
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span2">
					<label >单&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;价:</label>
					<form:input path="price" htmlEscape="false" maxlength="200" class="input-small" readonly="true"/>

				</div>
				<div class="span2">
					<label >面&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;积:</label>
					<form:input path="area" htmlEscape="false" maxlength="200" class="input-small" readonly="true"/>
				</div>
				<div class="span2">
					<label >投&nbsp;&nbsp;资&nbsp;&nbsp;额:</label>
					<form:input path="investment" htmlEscape="false" maxlength="200" class="input-small" readonly="true"/>
				</div>
				<div class="span2">
					<label >进&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度:</label>
					<form:input path="progress" htmlEscape="false" maxlength="200" class="input-small" readonly="true"/>
				</div>
				<div class="span1">
				</div>

			</div>
		</div>
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span3">
					<label >图纸数量:</label>
					<form:input path="blueprintNum" htmlEscape="false" maxlength="200" class=" input-lg" readonly="true"/>

				</div>
				<div class="span7">
					<label >付款约定:</label>
					<form:input path="payment" htmlEscape="false" maxlength="200" class="input-xxlarge" readonly="true"/>
				</div>
				<div class="span1">
				</div>
			</div>
		</div>

		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label >子&nbsp;&nbsp;项&nbsp;&nbsp;目:</label>
					<form:input path="subItem" htmlEscape="false" maxlength="400" class="input-xxlarge" readonly="true"/>
				</div>

				<div class="span1">
				</div>
			</div>
		</div>

		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label >工期要求:</label>
					<form:input path="timeLimit" htmlEscape="false" maxlength="400" class="input-xxlarge" readonly="true"/>

				</div>
				<div class="span1">
				</div>
			</div>
		</div>

		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label >备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label>
					<form:textarea path="remark" htmlEscape="false" rows="3" maxlength="500" class="input-xxlarge" readonly="true"/>
				</div>
				<div class="span1">
				</div>
			</div>
		</div>

	</div>
	</form:form>
	<form:form id="reviewForm" modelAttribute="review"   action="${ctx}/contract/contract/auditorReview" method="post" class="form-horizontal">
		<form:hidden path="taskId"  value="${taskId}"/>
		<form:hidden path="state" />
	<div class="container-fluid">
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label >审核意见:</label>
					<form:textarea path="comment" htmlEscape="false" rows="3" maxlength="500" class="input-xxlarge" />
				</div>
				<div class="span1">
				</div>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="submit" value="通过"  onclick="review(1)"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="驳回" onclick="review(2)"/>
		</div>
	</div>
		<table title="批注列表" class="table table-striped table-bordered table-condensed">
			<thead>
			<tr>
				<th>批注时间</th>
				<th>批注人</th>
				<th>批注信息</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${comments}" var="item">
				<tr>
					<td><fmt:formatDate value="${item.time}" type="both"/></td>
					<td> ${item.userId}</td>
					<td> ${item.message}</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</form:form>
</body>
</html>