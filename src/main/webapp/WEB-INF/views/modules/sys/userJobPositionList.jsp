<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>设计岗位管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">

        $(document).ready(function() {
            //时间比较
            $("#inputForm").validate({
                rules: {
                    professional: {remote: "${ctx}/sys/user/jobPosition/checkProfessional?professional='" +$('#professional').val()+"'&user.id=${jobPosition.user.id}"}
                },
                messages: {
                    professional: {remote: "当前专业岗位设置已存在"}

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
			window.location.href="${ctx}/sys/user/jobPosition/form?user.id=${jobPosition.user.id}";
        }
        function save(){
            var flag=false;
            $("input[name='positions']").each(function() {
                if($(this).is(":checked"))
                {
                    flag=true;
                }
            });
            if(!flag){
                $.jBox.tip("请选择岗位！", 'error');
                return;
            }

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
	<li><a href="${ctx}/sys/user/work/list?user.id=${user.id}">工作履历</a></li>
	<li><a href="${ctx}/sys/user/home/list?user.id=${user.id}">家庭信息</a></li>
	<li><a href="${ctx}/sys/user/certificate/list?user.id=${user.id}">证书信息</a></li>
	<li><a href="${ctx}/sys/user/position/list?user.id=${user.id}">任职历史</a></li>
	<li><a href="${ctx}/sys/user/reward/list?user.id=${user.id}">奖惩信息</a></li>
	<li><a href="${ctx}/sys/user/performance/list?user.id=${user.id}">绩效信息</a></li>
	<li><a href="${ctx}/sys/train/userTrainList?id=${user.id}">培训信息</a></li>
	<li><a href="${ctx}/sys/user/contract/list?user.id=${user.id}">合同信息</a></li>
	<li class="active"><a href="${ctx}/sys/user/jobPosition/list?user.id=${user.id}">设计岗位</a></li>
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
				<th>专业</th>
				<th>专业负责人</th>
				<th>专业审定人</th>
				<th>审核</th>
				<th>校对</th>
				<th>设计</th>
				<th>绘制</th>
				<th>备注</th>
				<shiro:hasPermission name="sys:user:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="jobPosition">
			<tr>
				<td>${fns:getDictLabel(jobPosition.professional, 'professional', '无')}</td>
				<td>${fns:getDictLabel(jobPosition.responsible, 'yes_no', '')}</td>
				<td>${fns:getDictLabel(jobPosition.approval, 'yes_no', '')}</td>
				<td>${fns:getDictLabel(jobPosition.audit, 'yes_no', '')}</td>
				<td>${fns:getDictLabel(jobPosition.proof, 'yes_no', '')}</td>
				<td>${fns:getDictLabel(jobPosition.design, 'yes_no', '')}</td>
				<td>${fns:getDictLabel(jobPosition.draw, 'yes_no', '')}</td>
				<td>${jobPosition.remark}</td>

				<shiro:hasPermission name="sys:user:edit">
				<td>
					<a href="${ctx}/sys/user/jobPosition/form?id=${jobPosition.id}">编辑</a>
					<a href="${ctx}/sys/user/jobPosition/delete?id=${jobPosition.id}" onclick="return confirmx('确认要删除该设计岗位吗？', this.href)">删除</a>

				</td>
				</shiro:hasPermission>



			</tr>
		</c:forEach>
		</tbody>
	</table>
<!-- 模态框（Modal） -->
<form:form id="inputForm" modelAttribute="jobPosition" action="${ctx}/sys/user/jobPosition/save" method="post" class="form-horizontal">
<form:hidden path="id"/>
<form:hidden path="user.id" />
<div id="panel">
<div class="modal fade bs-example-modal-lg" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" >
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">设计岗位${not empty jobPosition.id?'修改':'添加'}</h4>
			</div>
			<div class="modal-body">

				   <div class="control-group">
					    <label >&nbsp;&nbsp;专&nbsp;&nbsp;&nbsp;&nbsp;业&nbsp;：</label>
					   <form:select path="professional" class="input-small ">
						   <form:options items="${fns:getDictList('professional')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					   </form:select>
					   <span class="help-inline"><font color="red">*</font> </span>
				   </div>
				   <div class="control-group">
					    <label>&nbsp;&nbsp;负责人&nbsp;：</label>
					    <c:if test="${jobPosition.responsible=='1'}">
							<input name='positions' type="checkbox" value="responsible"  checked="checked"/>
						</c:if>
					    <c:if test="${jobPosition.responsible=='0'||jobPosition.responsible==null}">
						   <input name='positions' type="checkbox" value="responsible"  />
					    </c:if>

				   </div>
				   <div class="control-group">
					    <label>&nbsp;&nbsp;审定人&nbsp;：</label>

					    <c:if test="${jobPosition.approval=='1'}">
					     <input name='positions' type="checkbox" value="approval" checked="checked" />
					    </c:if>
					    <c:if test="${jobPosition.approval=='0'||jobPosition.approval==null}">
					     <input name='positions' type="checkbox" value="approval"  />
					    </c:if>
				    </div>
				   <div class="control-group">
					    <label>&nbsp;&nbsp;审&nbsp;&nbsp;&nbsp;&nbsp;核&nbsp;：</label>
					   <c:if test="${jobPosition.audit=='1'}">
						   <input name='positions' type="checkbox" value="audit" checked="checked" />
					   </c:if>
					   <c:if test="${jobPosition.audit=='0'||jobPosition.audit==null}">
						   <input name='positions' type="checkbox" value="audit"  />
					   </c:if>

				   </div>
				   <div class="control-group">
					    <label>&nbsp;&nbsp;校&nbsp;&nbsp;&nbsp;&nbsp;对&nbsp;：</label>
					   <c:if test="${jobPosition.proof=='1'}">
						   <input name='positions' type="checkbox" value="proof" checked="checked" />
					   </c:if>
					   <c:if test="${jobPosition.proof=='0'||jobPosition.proof==null}">
						   <input name='positions' type="checkbox" value="proof"  />
					   </c:if>
				    </div>
				    <div class="control-group">
					     <label>&nbsp;&nbsp;设&nbsp;&nbsp;&nbsp;&nbsp;计&nbsp;：</label>
						<c:if test="${jobPosition.design=='1'}">
							<input name='positions' type="checkbox" value="design" checked="checked" />
						</c:if>
						<c:if test="${jobPosition.design=='0'||jobPosition.design==null}">
							<input name='positions' type="checkbox" value="design"  />
						</c:if>
				    </div>
				    <div class="control-group">
					     <label>&nbsp;&nbsp;绘&nbsp;&nbsp;&nbsp;&nbsp;制&nbsp;：</label>
						<c:if test="${jobPosition.draw=='1'}">
							<input name='positions' type="checkbox" value="draw" checked="checked" />
						</c:if>
						<c:if test="${jobPosition.draw=='0'||jobPosition.draw==null}">
							<input name='positions' type="checkbox" value="draw"  />
						</c:if>
				    </div>
				   <div class="control-group">
					    <label>&nbsp;&nbsp;备&nbsp;&nbsp;&nbsp;&nbsp;注&nbsp;：</label>
					    <form:textarea path="remark" htmlEscape="false" rows="3" maxlength="200" class="input-xlarge"/>
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