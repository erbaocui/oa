<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户附加管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {



		});
        function save(){
            if ($('#inputForm').valid()) {
				$('#inputForm').submit();
            }

        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/user/list">用户列表</a></li>
		<li class="active"><a href="${ctx}/sys/user/baseForm?id=${user.id}&flag=position">用户信息<shiro:hasPermission name="sys:user:edit">${not empty user.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission></a></li>
	</ul>
    <br/>
    <ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/user/form?id=${user.id}">基本信息</a></li>
		<li ><a href="${ctx}/sys/user/detail/form?user.id=${user.id}">详细信息</a></li>
		<li  class="active"><a href="${ctx}/sys/user/detail/currentPosition?user.id=${user.id}">任职信息</a></li>
		<li><a href="${ctx}/sys/user/study/list?user.id=${user.id}">学习履历</a></li>
		<li><a href="${ctx}/sys/user/work/list?user.id=${user.id}">工作履历</a></li>
		<li><a href="${ctx}/sys/user/home/list?user.id=${user.id}">家庭信息</a></li>
		<li><a href="${ctx}/sys/user/certificate/list?user.id=${user.id}">证书信息</a></li>
		<li><a href="${ctx}/sys/user/position/list?user.id=${user.id}">任职历史</a></li>
		<li><a href="${ctx}/sys/user/reward/list?user.id=${user.id}">奖惩信息</a></li>
		<li><a href="${ctx}/sys/user/performance/list?user.id=${user.id}">绩效信息</a></li>
		<li><a href="${ctx}/sys/train/userTrainList?id=${user.id}">培训信息</a></li>
		<li><a href="${ctx}/sys/user/contract/list?user.id=${user.id}">合同信息</a></li>
		<li><a href="${ctx}/sys/user/jobPosition/list?user.id=${user.id}">设计岗位</a></li>
	</ul>
<br/>
	<form:form id="inputForm" modelAttribute="detail" action="${ctx}/sys/user/detail/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
	    <form:hidden path="user.id"/>
		<input type="hidden" id="flag" name="flag" value="position" />
		<sys:message content="${message}"/>
	<div class="container-fluid">

		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span8">
					<label>任职职位：</label>
					<form:input path="curPosition" htmlEscape="false" maxlength="255" class="input-xxlarge " required="true"/>
					<span class="help-inline"><font color="red">*</font> </span>

				</div>


				<div class="span3">
				</div>
			</div>
		</div>

		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>


				<div class="span8">
					<label>任职时间：</label>
					<input name="positionDate" type="text" readonly="readonly" maxlength="20" class=" form-control input-medium Wdate "
							   value="<fmt:formatDate value="${detail.positionDate}" pattern="yyyy-MM-dd"/>"
							   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
				<div class="span3">
				</div>
			</div>
		</div>


	</div>

		<div class="form-actions">
			<shiro:hasPermission name="sys:user:edit"><input id="btnSubmit" class="btn btn-primary" type="button" onclick="save()" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</form:form>
</body>
</html>