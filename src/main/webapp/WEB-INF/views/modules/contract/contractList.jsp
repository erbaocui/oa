<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同管理</title>
	<meta name="decorator" content="default"/>
<%--	<link href="${ctxStatic}/jqGrid/4.6/css/ui.jqgrid.css" type="text/css" rel="stylesheet" />
	<script src="${ctxStatic}/jqGrid/4.7/js/jquery.jqGrid.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqGrid/4.7/js/jquery.jqGrid.extend.js" type="text/javascript"></script>--%>
	<script type="text/javascript">
		$(document).ready(function() {
			
		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
        function startProcess(id) {

            $.post("${ctx}/cont/audit/start?",{id:id},function(data){
                var code=data.result;
                if(code=='success'){
                    $.jBox.tip("流程启动成功");
                    page();

                }else{
                    $.jBox.tip("流程启动失败", 'error');
                }
            });

        }

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cont/base/list">合同列表</a></li>
		<shiro:hasPermission name="cont:base:edit"><li><a href="${ctx}/cont/base/form">合同新建</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="queryContract" action="${ctx}/cont/base/" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<ul class="ul-form">
			<li> <label>签约年份：</label>
				<form:select path="year" class="form-control input-medium">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('year')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li>&nbsp;&nbsp;&nbsp;&nbsp;
				<label>合同类型：</label>
				<form:select path="type" class="form-control input-small">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('contract_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>合同名称：</label>&nbsp;<form:input path="name" value="${name}" htmlEscape="false" maxlength="50" class="input-small"/></li>
			<li class="clearfix"></li>
			<li><label>签订部门：</label><sys:treeselect id="office" name="office.id" value="${user.office.id}" labelName="office.name" labelValue="${user.office.name}"
													title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/>
			</li>
			<li>
				<label >合同种类：</label>
				<form:select path="type" class="form-control input-small">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('sys_user_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li>
				<label>合同金额：</label>
				<form:select path="type" class="form-control">
					<form:option value="=" label="="/>
					<form:option value="<" label="<"/>
					<form:option value=">" label=">"/>
				</form:select>
				<form:input path="value" value="${value}" htmlEscape="false" maxlength="50" class="input-medium"/>
			</li>

			<li class="btns">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>name</th>
				<th>value</th>
				<shiro:hasPermission name="cont:base:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="contract">
			<tr>
				<td><a href="${ctx}/cont/base/form?id=${contract.id}">
					${contract.name}
				</a></td>
				<td>
					${contract.value}
				</td>
				<shiro:hasPermission name="cont:base:edit"><td>
					<c:if test="${contract.status == 1}">
						<a href=" #;" onclick="startProcess('${contract.id}')">启动流程</a>
					</c:if>

    				<a href="${ctx}/cont/base/form?id=${contract.id}">修改</a>
					<a href="${ctx}/cont/base/delete?id=${contract.id}" onclick="return confirmx('确认要删除该保存合同成功吗？', this.href)">删除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>