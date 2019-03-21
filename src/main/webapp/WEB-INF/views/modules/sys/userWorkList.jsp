<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>工作履历管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        $(document).ready(function() {
            //时间比较
            $.validator.methods.compareDate = function(value, element, param) {
                var startDate = jQuery(param).val();

                var date1 = new Date(Date.parse(startDate.replace("-", "/")));
                var date2 = new Date(Date.parse(value.replace("-", "/")));
                return date1 < date2;
            };
            //增加校验
            $("#inputForm").validate({
                focusInvalid:false,
                rules:{
                    "beginDate":{
                        required: true
                    },
                    "endDate": {
                        required: true,
                        compareDate: "#beginDate"
                    }
                },
                messages:{
                    "beginDate":{
                        required: "开始时间不能为空"
                    },
                    "endDate":{
                        required: "结束时间不能为空",
                        compareDate: "结束日期必须大于开始日期!"
                    }
                }
            });
            $('#panel').hide();
            if(${show}){
                $('#panel').show();
                $('#myModal').modal()({
                    show: true,
                    backdrop:'static'
                })
            }

        });
		function add() {
            window.location.href="${ctx}/sys/user/work/form?user.id=${work.user.id}";
        }
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
	<li class="active"><a href="${ctx}/sys/user/baseForm?id=${user.id}">用户信息<shiro:hasPermission name="sys:user:edit">${not empty user.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission></a></li>
</ul>
<br/>
<ul class="nav nav-tabs">
	<li><a href="${ctx}/sys/user/form?id=${user.id}">基本信息</a></li>
	<li><a href="${ctx}/sys/user/detail/form?user.id=${user.id}">详细信息</a></li>
	<li><a href="${ctx}/sys/user/detail/currentPosition?user.id=${user.id}">任职信息</a></li>
	<li><a href="${ctx}/sys/user/study/list?user.id=${user.id}">学习履历</a></li>
	<li  class="active"><a href="${ctx}/sys/user/work/list?user.id=${user.id}">工作履历</a></li>
	<li><a href="${ctx}/sys/user/home/list?user.id=${user.id}">家庭信息</a></li>
	<li><a href="${ctx}/sys/user/certificate/list?user.id=${user.id}">证书信息</a></li>
	<li><a href="${ctx}/sys/user/position/list?user.id=${user.id}">任职历史</a></li>
	<li><a href="${ctx}/sys/user/reward/list?user.id=${user.id}">奖惩信息</a></li>
	<li><a href="${ctx}/sys/user/performance/list?user.id=${user.id}">绩效信息</a></li>
	<li><a href="${ctx}/sys/train/userTrainList?id=${user.id}">培训信息</a></li>
	<li><a href="${ctx}/sys/user/contract/list?user.id=${user.id}">合同信息</a></li>
	<li><a href="${ctx}/sys/user/jobPosition/list?user.id=${user.id}">设计岗位</a></li>
</ul>
<sys:message content="${message}"/>
<ul class="ul-form">
	<div class="btn-toolbar ">
		<div class="btn-group">
			<input id="btnSubmit" class="btn btn-primary" type="button"  onclick="add()" value="添加" />
		</div>
	</div>
</ul>
<table id="contentTable" class="table table-striped table-bordered table-condensed">
	<thead>
			<tr>
				<th>开始时间</th>
				<th>结束时间</th>
				<th>单位</th>
				<th>职位</th>
				<th>部门</th>
				<th>证明人</th>
				<th>证明人电话</th>
				<shiro:hasPermission name="sys:user:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="work">
			<tr>
				<td>
				<fmt:formatDate value="${work.beginDate}" pattern="yyyy-MM"/>
				<td>
					<fmt:formatDate value="${work.endDate}" pattern="yyyy-MM"/>
				</td>
				<td>
					${work.company}
				</td>
				<td>
					${work.position}
				</td>
				<td>
					${work.office}
				</td>
				<td>
					${work.reference}
				</td>
				<td>
					${work.mobile}
				</td>

				<shiro:hasPermission name="sys:user:edit">
					<td>
						<a href="${ctx}/sys/user/work/form?id=${work.id}">编辑</a>
						<a href="${ctx}/sys/user/work/delete?id=${work.id}" onclick="return confirmx('确认要删除该学习履历吗？', this.href)">删除</a>

					</td>
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
<!-- 模态框（Modal） -->
<form:form id="inputForm" modelAttribute="work" action="${ctx}/sys/user/work/save" method="post" class="form-horizontal">
	<form:hidden path="id"/>
	<form:hidden path="user.id" />
<div id="panel">
	<div class="modal fade bs-example-modal-lg" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" >
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
					<h4 class="modal-title" id="myModalLabel">工作履历${not empty work.id?'修改':'添加'}</h4>
				</div>
				<div class="modal-body">
					<div class="control-group">
						<label >开始日期：</label>
						<input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
							   value="<fmt:formatDate value="${work.beginDate}" pattern="yyyy-MM"/>"
							   onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
					<div class="control-group">
						<label> 结束日期：</label>
						<input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
							   value="<fmt:formatDate value="${work.endDate}" pattern="yyyy-MM"/>"
							   onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
					<div class="control-group">
						<label>&nbsp;&nbsp;单&nbsp;&nbsp;&nbsp;&nbsp;位&nbsp;：</label>
						<form:input path="company" htmlEscape="false" maxlength="200"  class="input-xlarge"  required="true" />
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
					<div class="control-group">
						<label>&nbsp;&nbsp;职&nbsp;&nbsp;&nbsp;&nbsp;位&nbsp;：</label>
						<form:input path="position" htmlEscape="false" maxlength="255"  class="input-small" />
						<label>&nbsp;&nbsp;部&nbsp;&nbsp;&nbsp;&nbsp;门&nbsp;：</label>
						<form:input path="office" htmlEscape="false" maxlength="255"  class="input-small" />
					</div>
					<div class="control-group">
						<label>&nbsp;&nbsp;证明人&nbsp;：</label>
						<form:input path="reference" htmlEscape="false" maxlength="255"  class="input-small" />
						<label>&nbsp;&nbsp;电&nbsp;&nbsp;&nbsp;&nbsp;话&nbsp;：</label>
						<form:input path="mobile" htmlEscape="false" maxlength="255"  class="input-small" />
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" onclick="save()">提交</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal -->
	</div>
</div>
</form:form>
</body>
</html>