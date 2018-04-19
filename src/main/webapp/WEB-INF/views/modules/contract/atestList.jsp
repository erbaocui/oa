<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$("#btnExport").click(function(){
				top.$.jBox.confirm("确认要导出用户数据吗？","系统提示",function(v,h,f){
					if(v=="ok"){
						$("#searchForm").attr("action","${ctx}/sys/user/export");
						$("#searchForm").submit();
					}
				},{buttonsFocus:1});
				top.$('.jbox-body .jbox-icon').css('top','55px');
			});
			$("#btnImport").click(function(){
				$.jBox($("#importBox").html(), {title:"导入数据", buttons:{"关闭":true}, 
					bottomText:"导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"});
			});
		});
		function page(n,s){
			if(n) $("#pageNo").val(n);
			if(s) $("#pageSize").val(s);
			$("#searchForm").attr("action","${ctx}/contract/list");
			$("#searchForm").submit();
	    	return false;
	    }
	</script>
</head>
<body>
	<div id="importBox" class="hide">
		<form id="importForm" action="${ctx}/sys/user/import" method="post" enctype="multipart/form-data"
			class="form-search" style="padding-left:20px;text-align:center;" onsubmit="loading('正在导入，请稍等...');"><br/>
			<input id="uploadFile" name="file" type="file" style="width:330px"/><br/><br/>　　
			<input id="btnImportSubmit" class="btn btn-primary" type="submit" value="   导    入   "/>
			<a href="${ctx}/sys/user/import/template">下载模板</a>
		</form>
	</div>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/contract/list">合同列表</a></li>
		<shiro:hasPermission name="sys:user:edit"><li><a href="${ctx}/contract/form">合同添加</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="queryContract" action="${ctx}/sys/user/list" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
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
		<thead><tr><th>归属公司</th><th>归属部门</th><th class="sort-column login_name">登录名</th><th class="sort-column name">姓名</th><th>电话</th><th>手机</th><%--<th>角色</th> --%><shiro:hasPermission name="sys:user:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${page.list}" var="contract">
			<tr>
				<td>${contract.code}</td>
				<td>${contract.office.name}</td>
				<td>${contrac.name}</td>
				<td>${contract.name}</td>
				<td>${contract.project.name}</td>
				<td>${contract.value}</td><%--
				<td>${user.roleNames}</td> --%>
				<shiro:hasPermission name="sys:user:edit"><td>
    				<a href="${ctx}/contract/form?id=${contract.id}">修改</a>
					<a href="${ctx}/sys/user/delete?id=${contract.id}" onclick="return confirmx('确认要删除该用户吗？', this.href)">启动流程</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>