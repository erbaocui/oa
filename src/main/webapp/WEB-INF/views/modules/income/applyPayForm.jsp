<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收款管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        var contractId=null;
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



	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/income/applyPay/list">请款列表</a></li>
		<li class="active"><a href="${ctx}/income/applyPay/form?id=${apply.id}">收款</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="apply" action="${ctx}/income/applyPay/save" method="post" class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">请款名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">请款金额：</label>
			<div class="controls">
				<form:input path="applyValue" htmlEscape="false" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">甲方名称：</label>
			<div class="controls">
				<form:input path="firstParty" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge "/>
			</div>
		</div>







		<div class="form-actions">
       <shiro:hasPermission name="income:applyPay:edit">
		   <input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>

	</form:form>

</body>
</html>