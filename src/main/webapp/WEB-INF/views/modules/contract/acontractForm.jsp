<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#no").focus();
			$("#inputForm").validate({
				rules: {
					loginName: {remote: "${ctx}/sys/user/checkLoginName?oldLoginName=" + encodeURIComponent('${user.loginName}')}
				},
				messages: {
					loginName: {remote: "用户登录名已存在"},
					confirmNewPassword: {equalTo: "输入与上面相同的密码"}
				},
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
            console.log('oninput event ');
            $("#type").change(function () {
                console.log('run ');
				alert("aaaa");
            });
		});
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/contract/list">合同列表</a></li>
		<li class="active"><a href="${ctx}/contract/form?id=${user.id}">合同<shiro:hasPermission name="sys:user:edit">${not empty contract.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>


	<form:form id="inputForm" modelAttribute="contract" action="${ctx}/sys/user/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>

<div class="container-fluid">
	<div class="control-group">
		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span3">
				<label >合同编号:</label>
				<form:input path="code"  value="contract.code" htmlEscape="false" maxlength="50" class=" form-control input-small required"/>
				<span class="help-inline"><font color="red">*</font> </span>
			</div>
			<div class="span7">
				<label >合同名称:</label>
				<form:input path="name" value="contract.name" htmlEscape="false" maxlength="50" class=" form-control input-xxlarge required"/>
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
				<label >项目经理:</label>
				<sys:treeselect id="manager" name="manager.id" value="${manager.rid}" labelName="user.name" labelValue="${user.name}" title="项目经理" url="/sys/office/treeData?type=3" cssClass="input-mini" allowClear="true" notAllowSelectParent="true"/>
			</div>
			<div class="span7">
				<label >项目名称:</label>
				<sys:treeselect id="project" name="project.id" value="${project.id}" labelName="office.name" labelValue="${user.office.name}" title="项目名称" url="/sys/office/treeData?type=2" cssClass="input-xxlarge" allowClear="true" notAllowSelectParent="true" />
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
					<form:select path="type" class="form-control input-small">
							<form:option value="" label="请选择"/>
							<form:options items="${fns:getDictList('contract_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>

				</div>
				<div class="span2">
					<label >合同种类:</label>
					<form:select path="class" value="contract.code" class="form-control input-small">
						<form:option value="" label="请选择"/>
						<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				<div class="span2">
					<label >是否分包:</label>
					<form:select path="code" class="form-control input-small">
						<form:option value="" label="请选择"/>
						<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				<div class="span2">
					<label >合同状态:</label>
					<form:select path="code" class="form-control input-small">
						<form:option value="" label="请选择"/>
						<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
				<input type="text"  value=""  readonly="readonly"    onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" class="form-control input-small Wdate" />
			</div>
			<div class="span2">
				<label >结束时间:</label>
				<input type="text" value=""  readonly="readonly"    onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" class="form-control input-small Wdate" />
			</div>
			<div class="span2">
				<label >合同金额:</label>
				<input type="text"  class="form-control input-small" />
			</div>
			<div class="span2">
				<label >已付金额:</label>
				<input type="text"  class="form-control input-small" />
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
				<sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}" title="部门" url="/sys/office/treeData?type=2" cssClass="input-mini" allowClear="true" notAllowSelectParent="true"/>
			</div>
			<div class="span2">
				<label >签约时间:</label>
				<input type="text" value=""  readonly="readonly"    onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" class="form-control input-small Wdate" />
			</div>
			<div class="span2">
				<label>&nbsp;&nbsp;联&nbsp;系&nbsp;人:</label>
				<input type="text"  class="form-control input-small" />
			</div>
			<div class="span2">
				<label >联系电话:</label>
				<input type="text"  class="form-control input-small" />
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
				<input type="text"   readonly="readonly"    onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" class="form-control input-small Wdate" />
			</div>
			<div class="span2">
				<label >备案时间:</label>
				<input type="text" readonly="readonly"    onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" class="form-control input-small Wdate" />
			</div>
			<div class="span2">
				<label >返回时间:</label>
				<input type="text"    value=""  readonly="readonly"    onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" class="form-control input-small Wdate" />
			</div>
			<div class="span2">
				<label >财务时间:</label>
				<input type="text"   value=""  readonly="readonly"    onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" class="form-control input-small Wdate" />
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
				<input type="text"   class="form-control input-small" />
			</div>
			<div class="span2">
				<label >面&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;积:</label>
				<input type="text" class="form-control input-small" />
			</div>
			<div class="span2">
				<label >投&nbsp;&nbsp;资&nbsp;&nbsp;额:</label>
				<input type="text"  class="form-control input-small" />
			</div>
			<div class="span2">
				<label >进&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;度:</label>
				<input type="text"  class="form-control input-small" />
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
				<input type="text" class="form-control input-lg"  />
			</div>
			<div class="span7">
				<label >付款约定:</label>
				<input type="text" class="form-control input-xxlarge" />
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
				<input type="text" class="input-xxlarge"  />
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
				<form:input path="code" htmlEscape="false" maxlength="400" class="input-xxlarge" />
					<%--	<input type="text" class="form-control input-lg" />--%>
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
				<form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="500" class="input-xxlarge"/>
			</div>
			<div class="span1">
			</div>
		</div>
	</div>
	<div class="form-actions">
		<ul class="breadcrumb form-search ul-form ">
			<li class="clearfix center-block"></li>
			<li class="btns ">
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="保存" onclick="return page();"/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input id="btnExport" class="btn btn-primary" type="button" value="取消"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</div>
</div>

	</form:form>

</body>
</html>