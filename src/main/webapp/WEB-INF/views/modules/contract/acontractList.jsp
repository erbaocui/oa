<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同管理</title>
	<meta name="decorator" content="default"/>

	<link href="${ctxStatic}/jqGrid/4.6/css/ui.jqgrid.css" type="text/css" rel="stylesheet" />
	<script src="${ctxStatic}/jqGrid/4.7/js/jquery.jqGrid.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqGrid/4.7/js/jquery.jqGrid.extend.js" type="text/javascript"></script>
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
			$("#searchForm").attr("action","${ctx}/contact/list");
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
	<form:form id="searchForm" modelAttribute="user"   action="${ctx}/sys/user/listData" method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}" callback="page();"/>
		<ul class="ul-form">
			<li>
				<label>年份：</label>
                <form:select path="loginName" class="form-control input-small">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('year')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>

			</li>
			<li><label>登录名：</label><form:input path="loginName" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li class="clearfix"></li>
			<li><label>归属部门：</label><sys:treeselect id="office" name="office.id" value="" labelName="office.name" labelValue="${user.office.name}"
				title="部门" url="/sys/office/treeData?type=2" cssClass="input-small" allowClear="true" notAllowSelectParent="true"/></li>
			<li><label>姓&nbsp;&nbsp;&nbsp;名：</label><form:input path="name" htmlEscape="false" maxlength="50" class="input-medium"/></li>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
				<input id="btnExport" class="btn btn-primary" type="button" value="导出"/>
				<input id="btnImport" class="btn btn-primary" type="button" value="导入"/></li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="dataGrid"></table>
    <div class="pagination" id="dataGridPage"></div>

	<script type="text/javascript">
		// 初始化DataGrid对象
		$('#dataGrid').dataGrid({
			
			// 设置数据表格列
			columnModel: [
				{header:'归属公司', name:'loginName', index:'login_name', width:100, frozen:true , formatter: function(val, obj, row, act){
					return '<a href="${ctx}/sys/user/form?id='+row.id+'" class="btnList" data-title="编辑用户">'+val+'</a>';
				}},
				{header:'归属公司', name:'company.name', index:'', width:160},
				{header:'组织机构', name:'office.name', index:'', width:160},
				{header:'电话', name:'phone', index:'phone', width:100, sortable:false},
				{header:'手机', name:'mobile', index:'mobile', width:100, sortable:false},
				{header:'状态', name:'delFlag', index:'a.del_flag', width:50, fixed:true, align:"center", formatter: function(val, obj, row, act){
					return getDictLabel(${fns:getDictListJson('del_flag')}, val, '未知', true);
				}},
				{header:'操作', name:'actions', width:120, fixed:true, sortable:false, fixed:true, formatter: function(val, obj, row, act){
					var actions = [];
					<shiro:hasPermission name="sys:user:edit">
					actions.push('<a href="${ctx}/sys/user/form?id='+row.id+'" class="btnList" title="编辑用户">编辑</a>&nbsp;');
					</shiro:hasPermission>
					return actions.join('');
				}}
			],
			ajaxSuccess: function(data){ // 加载成功后执行方法
				
			}
		});
	</script>
</body>
</html>