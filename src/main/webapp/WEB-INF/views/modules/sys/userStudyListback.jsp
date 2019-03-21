<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>学习履历管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

        $(document).ready(function() {
            //时间比较
            $.validator.methods.compareDate = function(value, element, param) {
                //var startDate = jQuery(param).val() + ":00";补全yyyy-MM-dd HH:mm:ss格式
                //value = value + ":00";

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

			if(${show}){
                $('#myModal').modal();
			}
			
		});


        function add() {
			window.location.href="${ctx}/sys/user/study/form?user.id=${study.user.id}";
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
	<li class="active"><a href="${ctx}/sys/user/study/list?user.id=${user.id}">学习履历</a></li>
	<li><a href="${ctx}/sys/user/work/list?user.id=${user.id}">工作履历</a></li>
	<li><a href="${ctx}/sys/user/home/list?user.id=${user.id}">家庭信息</a></li>
	<li><a href="${ctx}/sys/user/work/list?user.id=${user.id}">证书信息</a></li>
	<li><a href="${ctx}/sys/user/position/list?user.id=${user.id}">任职历史</a></li>
	<li><a href="${ctx}/sys/user/reward/list?user.id=${user.id}">奖惩信息</a></li>
	<li><a href="${ctx}/sys/user/performance/list?user.id=${user.id}">绩效信息</a></li>
	<li><a href="${ctx}/sys/train/userTrainList?user.id=${user.id}">培训信息</a></li>
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
				<th>学校</th>
				<th>专业</th>
				<th>毕业情况</th>
				<th>学历</th>
				<th>学位</th>
				<th>第一学历</th>
				<th>最高学历</th>
				<th>学制（年）</th>
				<shiro:hasPermission name="sys:user:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="study">
			<tr>
				<td>
					<fmt:formatDate value="${study.beginDate}" pattern="yyyy-MM"/>
				</td>
				<td>
					<fmt:formatDate value="${study.endDate}" pattern="yyyy-MM"/>
				</td>
				<td>${study.school}</td>
				<td>${study.major}</td>
				<td>${fns:getDictLabel(study.graduate, 'sys_user_graduate', '无')}</td>
				<td>${fns:getDictLabel(study.education, 'sys_user_education', '无')}</td>
				<td>${study.degree}</td>
				<td>${fns:getDictLabel(study.first, 'yes_no', '无')}</td>
				<td>${fns:getDictLabel(study.highest, 'yes_no', '无')}</td>
				<td>${study.year}</td>
				<shiro:hasPermission name="sys:user:edit">
				<td>
					<a href="${ctx}/sys/user/study/form?id=${study.id}">编辑</a>
					<a href="${ctx}/sys/user/study/delete?id=${study.id}" onclick="return confirmx('确认要删除该学习履历吗？', this.href)">删除</a>
				</td>
				</shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>

<!-- 模态框（Modal） -->
<form:form id="inputForm" modelAttribute="study" action="${ctx}/sys/user/study/save" method="post" class="form-horizontal">
<form:hidden path="id"/>
<form:hidden path="user.id" />

<div class="modal fade bs-example-modal-lg" id="myModal" tabindex="1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" >
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">学习履历${not empty study.id?'修改':'添加'}</h4>
			</div>
			<div class="modal-body">

				   <div class="control-group">
					    <label >入学日期：</label>
					    <input id="beginDate" name="beginDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
						   value="<fmt:formatDate value="${study.beginDate}" pattern="yyyy-MM"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});"/>
					     <span class="help-inline"><font color="red">*</font> </span>
				   </div>
				   <div class="control-group">
					   <label> 毕业日期：</label>
					   <input id="endDate" name="endDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
						     value="<fmt:formatDate value="${study.endDate}" pattern="yyyy-MM"/>"
						     onclick="WdatePicker({dateFmt:'yyyy-MM',isShowClear:false});"/>
					    <span class="help-inline"><font color="red">*</font> </span>
				    </div>
					<div class="control-group">
						<label>&nbsp;&nbsp;学&nbsp;&nbsp;&nbsp;&nbsp;校&nbsp;：</label>
						<form:input path="school" htmlEscape="false" maxlength="200"  class="input-xlarge"  required="true" />
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
					<div class="control-group">
						<label>&nbsp;&nbsp;专&nbsp;&nbsp;&nbsp;&nbsp;业&nbsp;：</label>
						<form:input path="major" htmlEscape="false" maxlength="255"  class="input-xlarge" required="true"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
					<div class="control-group">
						<label >毕业情况：</label>
						<form:select path="graduate" class="input-small">
								<form:options items="${fns:getDictList('sys_user_graduate')}" itemLabel="label" itemValue="value" htmlEscape="false"  required="true"/>
						</form:select>
						<label >&nbsp;&nbsp;学&nbsp;&nbsp;&nbsp;&nbsp;历&nbsp;：</label>
						<form:select path="education" class="input-small">
							<form:options items="${fns:getDictList('sys_user_education')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>
					<div class="control-group">
						<label>&nbsp;&nbsp;学&nbsp;&nbsp;&nbsp;&nbsp;位&nbsp;：</label>
						<form:input path="degree" htmlEscape="false" maxlength="255" class="input-mini" />
						&nbsp;
						<label>学制(年)：</label>
						<form:input path="year" htmlEscape="false" maxlength="11" onkeyup="onlyNum(this)"   class="input-mini" required="true"/>
						<span class="help-inline"><font color="red">*</font> </span>
					</div>
					<div class="control-group">
						<label>第一学历：</label>
						<form:select path="first" class="input-small">
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<label>最高学历：</label>
						<form:select path="highest" class="input-small">
							<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
					</div>

			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" onclick="save()">提交</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
</div>
</form:form>
</body>
</html>