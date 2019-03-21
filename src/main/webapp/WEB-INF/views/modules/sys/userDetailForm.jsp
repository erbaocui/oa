<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>用户详细信息</title>
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
                    "idCardNo":{
                        card:true
					}
                    // ,
                    // "workDate": {
                    //     required:true,
                    //     compareDate:"#companyDate"
                    // }

                },
                messages:{
                    "idCardNo":{
                        card:"请输入真确格式"
                    }
                    // ,
                    // "workDate":{
                    //     required:"必填字段",
                    //     compareDate: "结束日期必须大于开始日期!"
                    // }

                }
            });
            $('#nativePlaceName').val('${detail.nativePlace}');
		});
        function save(){
            if ($('#inputForm').valid()) {
                 if($('#nativePlaceName').val()==null||$('#nativePlaceName').val()==""){
                     $.jBox.tip("请选择籍贯！", 'error');
                      return;
				 }
				$('#inputForm').submit();
            }

        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/user/list">用户列表</a></li>
		<li class="active"><a href="${ctx}/sys/user/baseForm?id=${user.id}&flag=detail">用户信息<shiro:hasPermission name="sys:user:edit">${not empty user.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="sys:user:edit">查看</shiro:lacksPermission></a></li>
	</ul>
    <br/>
    <ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/user/form?id=${user.id}">基本信息</a></li>
		<li class="active" ><a href="${ctx}/sys/user/detail/form?user.id=${user.id}">详细信息</a></li>
		<li ><a href="${ctx}/sys/user/detail/currentPosition?user.id=${user.id}">任职信息</a></li>
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
	   <input  type="hidden" id="flag" name="flag" value="detail" />
		<sys:message content="${message}"/>
	<div class="container-fluid">
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span4">
					<label>身份证号：</label>
					<form:input path="idCardNo"   htmlEscape="false" maxlength="50" class="input-large"  required="true"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>


				<div class="span4">
					<label >曾&nbsp;&nbsp;用&nbsp;&nbsp;名：</label>
					<form:input path="usedName"   htmlEscape="false" maxlength="50" class="input-medium" />
				</div>
				<div class="span3">
				</div>
			</div>
		</div>
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span4">
					<label >性别：</label>
					<form:select path="gender" class="input-small " required="true">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('sex')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>

				<div class="span4">
					<label>状态：</label>
					<form:select path="status" class="input-small " required="true">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('sys_user_status')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
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
				<div class="span4">
					<label >婚姻状况：</label>
					<form:select path="marital" class="input-small " required="true">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('sys_user_marital')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>

				<div class="span4">
					<label>民族：</label>
					<form:select path="nation" class="input-small " required="true">
						<form:option value="" label=""/>
						<form:options items="${fns:getDictList('sys_user_nation')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
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
				<div class="span4">
					   <label >政治面貌：</label>
						<form:select path="political" class="input-small">
							<form:option value="" label=""/>
							<form:options items="${fns:getDictList('sys_user_political')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
				</div>

				<div class="span4">
					<label>入党时间：</label>
					<input name="partyDate" type="text" readonly="readonly" maxlength="20" class=" form-control input-medium Wdate "
							   value="<fmt:formatDate value="${detail.partyDate}" pattern="yyyy-MM-dd"/>"
							   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:true});"/>
				</div>
				<div class="span3">
				</div>
			</div>
		</div>
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span4">
					<label >籍贯：</label>
					<sys:treeselectnativeplace id="nativePlace" name="nativePlace.id" value="${nativePlace}" labelName="nativePlace" labelValue="${nativePlace}"
									title="籍贯" url="/sys/area/treeNativePlaceData?type=3" extId="${nation.id}" cssClass="input-small" allowClear="true"  notAllowSelectParent="true"/>
					<span class="help-inline"><font color="red">*</font> </span>

				</div>
				<div class="span4">
					<label>出生时间：</label>
					<input name="birthdate" type="text" readonly="readonly" maxlength="20" class=" form-control input-medium Wdate "
						   value="<fmt:formatDate value="${detail.birthdate}" pattern="yyyy-MM-dd"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" required="true"/>
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
				<div class="span4">
					<label >入司时间：</label>
					<input name="companyDate" type="text" readonly="readonly" maxlength="20" class=" form-control input-medium Wdate "  required="true"
						   value="<fmt:formatDate value="${detail.companyDate}" pattern="yyyy-MM-dd"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
					<span class="help-inline"><font color="red">*</font></span>
				</div>

				<div class="span4">
					<label>工作时间：</label>
					<input name="workDate" type="text" readonly="readonly" maxlength="20" class=" form-control input-medium Wdate " required="true"
						   value="<fmt:formatDate value="${detail.workDate}" pattern="yyyy-MM-dd"/>"
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});"/>
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
						<label >家庭地址：</label>
						<form:input path="homeAddress" htmlEscape="false" maxlength="255" class="input-xxlarge " required="true"/>
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
					<label >通讯地址：</label>
					<form:input path="mailAddress" htmlEscape="false" maxlength="255" class="input-xxlarge " required="true"/>
					<span class="help-inline"><font color="red">*</font> </span>

				</div>

				<div class="span3">
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