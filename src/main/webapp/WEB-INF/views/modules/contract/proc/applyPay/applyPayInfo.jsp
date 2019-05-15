<%@ page contentType="text/html;charset=UTF-8" %>

<form:form id="inputForm" modelAttribute="contApply" action="${ctx}/cont/applyPay/save" method="post" class="form-horizontal" enctype="multipart/form-data">
		<form:input path="id"/>
		<input type="text" id="contractId" name="contractId" value="${contract.id}" />
		<input type="text" id="readonly" name="readolny" value="${readonly}" />


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
					<form:input path="receiptPhone" htmlEscape="false" maxlength="255" class="input-xlarge required" readonly="${readonly}"/>
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
					<form:input path="receiptContent" htmlEscape="false" maxlength="255" class="input-xlarge required" readonly="${readonly}"/>
					<span class="help-inline"><font color="red">*</font> </span>

				</div>
				<div class="span5">
					<label>纳&nbsp;&nbsp;税&nbsp;&nbsp;号：</label>
					<form:input path="taxId" htmlEscape="false" maxlength="255" class="input-xlarge required" readonly="${readonly}"/>
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
					<form:input path="receiptBank" htmlEscape="false"  class="input-xlarge required" readonly="${readonly}"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
				<div class="span5">
					<label>账&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;号：</label>
					<form:input path="receiptAccount" htmlEscape="false" class="input-xlarge required" readonly="${readonly}"/>
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
						   onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});" disabled="${readonly}"/>
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
			<input id="btnSubmit" class="btn btn-primary" type="button" onclick="save();" value="保 存"/>&nbsp;
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
	</div>
	</form:form>
