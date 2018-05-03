<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请款管理</title>
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
        function addContract(){

            location.href="${ctx}/income/applyPay/contAdd?id=${applyPay.id}&contractId="+contractId;
		}

        function openContract() {
            $.jBox("iframe:"+"${ctx}/cont/base/briefList?notContractIds="+$("#notContractIds").val(), {
                title: "合同选择",
                width: 500,
                height: 320,
                buttons: {},  //为了不出现底部的按钮这里特别要这样填写
                closed: function(){  //关闭时发生，为了刷新父级页面
                    addContract();
                },
                loaded : function(h){ //隐藏滚动条
                    $(".jbox-content", top.document).css( "overflow-y", "hidden");
                    //h.find("iframe")[0].contentDocument.ConstructionAdd_id.value=chkConstructionId;
                }
            });
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/income/applyPay/">请款列表</a></li>
		<li class="active"><a href="${ctx}/income/applyPay/form?id=${applyPay.id}">请款<shiro:hasPermission name="income:applyPay:edit">${not empty applyPay.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="income:applyPay:edit">查看</shiro:lacksPermission></a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="applyPay" action="${ctx}/income/applyPay/save" method="post" class="form-horizontal">
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


		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
			<tr>
				<th>name</th>
				<th>value</th>
				<shiro:hasPermission name="cont:base:edit"><th>操作</th></shiro:hasPermission>
			</tr>
			</thead>
			<tbody>
			<c:set var="sum" value=""></c:set>
			<c:forEach items="${applyPay.contractList}" var="contract">
				<tr>
					<td>${contract.name}</td>
					<td>${contract.code}</td>
					<td>
						<a href="${ctx}/income/applyPay/contDelete?id=${applyPay.id}&contractId=${contract.id}" onclick="return confirmx('确认要删除该合同吗？', this.href)">删除</a>
					</td>
				</tr>
				<c:set var="sum" value="${sum},${contract.id}"></c:set>
			</c:forEach>
			<c:set var="length" value="${fn:length(sum)}"></c:set>
			<input id="notContractIds" type="text" value="${fn:substring(sum, 1,length)}">
			</tbody>
		</table>
		<div class="pagination">${page}</div>




		<div class="form-actions">
			<shiro:hasPermission name="income:applyPay:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
			<input id="btnCancel" class="btn" type="button" value="测 试" onclick="openContract();"/>
		</div>
	</form:form>
</body>
</html>