<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>请款信息补充</title>
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


		function save() {


            if($("#inputForm").valid()){
                $("#inputForm").submit();
            }

        }

        function review(state){
			var comment=$("#comment").val().trim();
            $("#state").val(state);
			if(comment==null||comment==""){
                    $.jBox.tip("提请填写审核意见！", 'error');
                    return;
			}
            $("#reviewForm").submit();
        }
	</script>
</head>
<body>
	<ul class="nav nav-tabs">

		<li class="active"><a href="#">开票信息补充</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="contApply" action="${ctx}/cont/apply/pay/proc/save" method="post" class="form-horizontal" >
		<form:hidden path="id"/>
		<input type="hidden" id="taskIdApplyPay" name="taskIdApplyPay" value="${taskId}" />
		<input type="hidden" id="contractId" name="contractId" value="${contractId}" />
		<input type="hidden" id="contract.id" name="contract.id" value="${contractId}" />
		<input type="hidden" id="readonly" name="readolny" value="${readonly}" />
		<sys:message content="${message}"/>

	<div class="container-fluid">
		<div class="page-header">
			<h4>开票信息</h4>
		</div>
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span5">
					<label>开票名称：</label>
					<form:input path="receiptName" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>

				</div>
				<div class="span5">
					<label>开票金额：</label>
					<form:input path="receiptValue" htmlEscape="false" maxlength="255" class="input-xlarge required"  onkeyup="onlyNum(this)"/>
					<span class="help-inline"><font color="red">*</font> </span>

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
					<label >联系地址：</label>
					<form:input path="receiptAddress" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>

				</div>
				<div class="span5">
					<label>联系电话：</label>
					<form:input path="receiptPhone" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>

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
					<label >开票内容：</label>
					<form:input path="receiptContent" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>

				</div>
				<div class="span5">
					<label>纳&nbsp;&nbsp;税&nbsp;&nbsp;号：</label>
					<form:input path="taxId" htmlEscape="false" maxlength="255" class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>

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
					<label >开户银行：</label>
					<form:input path="receiptBank" htmlEscape="false"  class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
				<div class="span5">
					<label>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</label>
					<form:input path="receiptAccount" htmlEscape="false" class="input-xlarge required"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
				<div class="span1"></div>
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
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
					<span class="help-inline"><font color="red">*</font> </span>
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
		<div class="form-actions">
			<input id="btnSave" class="btn btn-primary" type="button" onclick="save();" value="保 存"/>&nbsp;

		</div>
	</div>
	</form:form>
	<form:form id="reviewForm" modelAttribute="review"   action="${ctx}/cont/apply/pay/proc/improve/submit" method="post" class="form-horizontal">
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
				<input id="btnSubmit" class="btn btn-primary" type="button" value="提交"  onclick="review(1)"/>&nbsp;
			</div>
		</div>
		<br>

	</form:form>
	<%@ include file="/WEB-INF/views/modules/act/comment.jsp"%>
</body>
</html>