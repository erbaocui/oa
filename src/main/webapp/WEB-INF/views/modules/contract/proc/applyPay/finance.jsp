<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同请款管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
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


        function review(state){

            var comment=$("#comment").val().trim();
            $("#state").val(state);
            if(state==2){
                if(comment==null||comment==""){
                    $.jBox.tip("提请填写审核意见！", 'error');
                    return;
                }
            }
            $("#reviewForm").submit();


        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">

		<li class="active"><a href="#">请款财务</a></li>
	</ul><br/>
<sys:message content="${message}"/>
	<form:form id="inputForm" modelAttribute="finance" action="" method="post" class="form-horizontal" enctype="multipart/form-data">
		<div class="container-fluid">
			<div class="page-header">
				<h4>开票信息</h4>
			</div>
			<div class="control-group">
				<div class="row-fluid">
					<div class="span1">
					</div>
					<div class="span10">
						<label>开票名称：</label>
						<form:input path="name" htmlEscape="false" maxlength="255" class="input-xlarge required" readonly="${readonly}"/>

					</div>
					<div class="span1">
					</div>
				</div>
			</div>
			<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span5">
					<label>纳税人识别号：</label>
					<form:input path="taxId" htmlEscape="false" maxlength="255" class="input-xlarge required" readonly="${readonly}"/>

				</div>
				<div class="span1">
				</div>
			</div>
		</div>
			<div class="control-group">
				<div class="row-fluid">
					<div class="span1">
					</div>
					<div class="span10">
						<label>地址与电话：</label>
						<form:input path="addressPhone" htmlEscape="false" maxlength="255" class="input-xlarge required" readonly="${readonly}"/>

					</div>
					<div class="span1">
					</div>
				</div>
			</div>
			<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label>银行与账户：</label>
					<form:input path="addressPhone" htmlEscape="false" maxlength="255" class="input-xlarge required" readonly="${readonly}"/>

				</div>
				<div class="span1">
				</div>
			</div>
		</div>
			<div class="control-group">
				<div class="row-fluid">
					<div class="span1">
					</div>
					<div class="span5">
						<label>内容：</label>
						<form:input path="content" htmlEscape="false" maxlength="255" class="input-xlarge required" readonly="${readonly}"/>

					</div>
					<div class="span5">
						<label>金额：</label>
						<form:input path="value" htmlEscape="false" maxlength="255" class="input-xlarge required" readonly="${readonly}"/>

					</div>
					<div class="span1">
					</div>
				</div>
			</div>




			<div class="control-group">
				<div class="row-fluid">
					<div class="span1">
					</div>
					<div class="span10">
						<label >发票备注：</label>
						<form:textarea path="receiptRemark" htmlEscape="false" rows="3" maxlength="500" class="input-xxlarge" readonly="${readonly}"/>
					</div>
					<div class="span1">
					</div>
				</div>
			</div>
			<div class="page-header">
				<h4>附加信息</h4>
			</div>
			<div class="control-group">
				<div class="row-fluid">
					<div class="span1">
					</div>
					<div class="span10">
						<label>希望开票日期：</label>
						<input name="receiptDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required "
							   value="<fmt:formatDate value="${contApply.receiptDate}" pattern="yyyy-MM-dd HH:mm:ss"/>"
							   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" disabled="${readonly}"/>
					</div>
					<div class="span1"></div>
				</div>
			</div>

			<div class="control-group">
				<div class="row-fluid">
					<div class="span1">
					</div>
					<div class="span10">
						<label >备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注：</label>
						<form:textarea path="remark" htmlEscape="false" rows="3" maxlength="500" class="input-xxlarge" readonly="${readonly}"/>
					</div>
					<div class="span1">
					</div>
				</div>
			</div>
		</div>
	</form:form>

	<form:form id="reviewForm" modelAttribute="review"   action="${ctx}/cont/apply/pay/proc/finance/submit" method="post" class="form-horizontal">
		<form:hidden path="taskId"  value="${taskId}"/>
		<form:hidden path="state"  />
		<div class="container-fluid">
			<div class="control-group">
				<div class="row-fluid">
					<div class="span1">
					</div>
					<div class="span6">
						<label >审核意见:</label>
						<form:textarea path="comment" htmlEscape="false" rows="3" maxlength="500" class="input-xxlarge" />
					</div>
					<div class="span1">
					</div>
				</div>
			</div>
			<div class="form-actions">
				<input id="btnSubmit" class="btn btn-primary" type="button" value="通过"  onclick="review(1)"/>&nbsp;
				<input id="btnCancel" class="btn " type="button" value="驳回" onclick="review(2)"/>
			</div>
		</div>
		<br>

	</form:form>
	<%@ include file="/WEB-INF/views/modules/act/comment.jsp"%>
</body>
</html>