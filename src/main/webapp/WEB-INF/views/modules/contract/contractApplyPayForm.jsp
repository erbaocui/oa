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


		function save() {


            if($("#inputForm").valid()){
                $("#inputForm").submit();


            }

        }


	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cont/applyPay/list?contractId=${contractId}&readonly=${readonly}&single=${single}">合同请款</a></li>
		<li class="active"><a href="#">合同请款${not empty contApply.id?'详情':'添加'}</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="contApply" action="${ctx}/cont/applyPay/save" method="post" class="form-horizontal" enctype="multipart/form-data">
		<form:hidden path="id"/>
		<input type="hidden" id="contractId" name="contractId" value="${contractId}" />
		<input type="hidden" id="readonly" name="readonly" value="${readonly}" />
		<input type="hidden" id="single" name="single" value="${single}" />
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
					<form:input path="receiptName" htmlEscape="false" maxlength="255" class="input-xlarge required" readonly="${readonly}"/>
					<span class="help-inline"><font color="red">*</font> </span>

				</div>
				<div class="span5">
					<label>开票金额：</label>
					<form:input path="receiptValue" htmlEscape="false" maxlength="255" class="input-xlarge required"  onkeyup="onlyNum(this)" readonly="${readonly}"/>
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
					<form:input path="receiptAddress" htmlEscape="false" maxlength="255" class="input-xlarge required" readonly="${readonly}"/>
					<span class="help-inline"><font color="red">*</font> </span>

				</div>
				<div class="span5">
					<label>联系电话：</label>
					<form:input path="receiptPhone" htmlEscape="false" maxlength="255" class="input-xlarge required"  readonly="${readonly}"/>
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
					<form:input path="receiptContent" htmlEscape="false" maxlength="255" class="input-xlarge required"  readonly="${readonly}"/>
					<span class="help-inline"><font color="red">*</font> </span>

				</div>
				<div class="span5">
					<label>纳&nbsp;&nbsp;税&nbsp;&nbsp;号：</label>
					<form:input path="taxId" htmlEscape="false" maxlength="255" class="input-xlarge required"  readonly="${readonly}"/>
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
					<form:input path="receiptBank" htmlEscape="false"  class="input-xlarge required"  readonly="${readonly}"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
				<div class="span5">
					<label>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</label>
					<form:input path="receiptAccount" htmlEscape="false" class="input-xlarge required"  readonly="${readonly}"/>
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

					<c:if test="${readonly}">
						<input name="receiptDate" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate required "
							   value="<fmt:formatDate value="${contApply.receiptDate}" pattern="yyyy-MM-dd"/>"
							   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" disabled="disabled"/>
					</c:if>
					<c:if test="${not readonly}">
						<input name="receiptDate" type="text" readonly="readonly" maxlength="20" class="iform-control input-small Wdate"
							   value="<fmt:formatDate value="${contract.receiptDate}" pattern="yyyy-MM-dd"/>"
							   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
					</c:if>

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
		<c:if test="${not readonly}">
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label >附件文件：</label>
					<input type="file" id="file" name="file"   />
					<span class="help-inline">支持文件格式：pdf、doc、docx</span>
				</div>
				<div class="span1">
				</div>
			</div>
		</div>
		</c:if>
		<div class="form-actions">
			<c:if test="${not readonly}">
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="save();" value="保 存"/>&nbsp;
			</c:if>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</div>
	</form:form>
	<c:if test="${not empty contApply.id}">
		<c:if test="${(contApply.status!=1) && (contApply.status!=2)&&(contApply.status!=null) }">
			<br>
			<%@ include file="/WEB-INF/views/modules/act/comment.jsp"%>
		</c:if>
	</c:if>
</body>
</html>