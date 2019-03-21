<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>员工证书管理</title>
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
                    "getDate":{
                        required: true
                    }

                },
                messages:{
                    "getDate":{
                        required: "颁发时间不能为空"
                    }

                }
            });
			//设置初始值
            $('#panel').hide();
            $('#zg').hide();
            $('#zc').show();
            if(${show}){
                $('#panel').show();
                $('#myModal').modal()({
                    show: true,
                    backdrop:'static'
                });
            }


			
		});


        function add() {
			window.location.href="${ctx}/sys/user/certificate/form?user.id=${certificate.user.id}";
        }
        function save(){

			if (!$('#inputForm').valid()) {
			  return
			}
			var type=$("#type").val();
            if( type==1){
                if($('#tmp1Name').val()==null||$('#tmp1Name').val()==""){
                    $.jBox.tip("请选择证书名称", 'error');
                    return
				}
                $('#keyValue').val($('#tmp1Id').val());
                $('#name').val( $('#tmp1Name').val());
            }else{
                $('#name').val($('#keyValue').find('option:selected').text());
			}
			$('#inputForm').submit();


		}
		function typeChange(){
            var type=$("#type").val();
            if( type==1){
                $('#zg').hide();
                $('#zc').show();

			}else{
                $('#zc').hide();
                $('#zg').show();

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
	<li class="active"><a href="${ctx}/sys/user/certificate/list?user.id=${user.id}">证书信息</a></li>
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
				<th>证书类型</th>
				<th>证书名称</th>
				<th>取得日期</th>
				<th>失效日期</th>
				<th>备注</th>
				<shiro:hasPermission name="sys:user:edit"><th>操作</th></shiro:hasPermission>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${list}" var="certificate">
			<tr>
				<td>
						${fns:getDictLabel(certificate.type, 'sys_user_certificate_type', '')}
				</td>
				<td>
						${certificate.name}
				</td>
				<td>
					   <fmt:formatDate value="${certificate.getDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
					<fmt:formatDate value="${certificate.expiryDate}" pattern="yyyy-MM-dd"/>
				</td>
				<td>
						${certificate.remark}
				</td>
				<shiro:hasPermission name="sys:user:edit">
				<td>
					<a href="${ctx}/sys/user/certificate/form?id=${certificate.id}">编辑</a>
					<a href="${ctx}/sys/user/certificate/delete?id=${certificate.id}" onclick="return confirmx('确认要删除该员工证书吗？', this.href)">删除</a>

				</td>
				</shiro:hasPermission>



			</tr>
		</c:forEach>
		</tbody>
	</table>

<!-- 模态框（Modal） -->
<form:form id="inputForm" modelAttribute="certificate" action="${ctx}/sys/user/certificate/save" method="post" class="form-horizontal">
<form:hidden path="id"/>
<form:hidden path="user.id" />
<form:hidden path="name"  />

<div id="panel">
<div class="modal fade bs-example-modal-lg" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" >
	<div class="modal-dialog modal-lg">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
				<h4 class="modal-title" id="myModalLabel">员工证书${not empty certificate.id?'修改':'添加'}</h4>
			</div>
			<div class="modal-body">
				  <div class="control-group">
					  <label>证书类型：</label>
					  <form:select path="type" class="input-small " onchange="typeChange()">
						  <form:options items="${fns:getDictList('sys_user_certificate_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					  </form:select>
					  <span class="help-inline"><font color="red">*</font> </span>
				   </div>
				   <div class="control-group">
					   <label>证书名称：</label>
					   <span id="zc">
						   <sys:treeselect id="tmp1" name="tmp1id" value="${certificate.keyValue}" labelName="parent.name" labelValue="${certificate.name}"
										   title="证书" url="/sys/dict/treeProfessionalData" extId="${professional.id}" cssClass="input-small" allowClear="true"/>

					   </span>
					   <span id="zg">
						   <form:select path="keyValue" class="input-small ">
							   <form:options items="${fns:getDictList('sys_user_certificate_qualification')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						   </form:select>
					   </span>
					   <span class="help-inline"><font color="red">*</font> </span>
				   </div>
				   <div class="control-group">
					    <label >颁发日期：</label>
					    <input id="getDate" name="getDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
						   value="<fmt:formatDate value="${certificate.getDate}" pattern="yyyy-MM-dd"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
					   <span class="help-inline"><font color="red">*</font> </span>
				   </div>
				   <div class="control-group">
					   <label> 有效日期：</label>
					   <input id="expiryDate" name="expiryDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
						     value="<fmt:formatDate value="${certificate.expiryDate}" pattern="yyyy-MM-dd"/>"
						     onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
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