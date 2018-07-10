<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同管理</title>
	<meta name="decorator" content="default"/>

	<script type="text/javascript">
		$(document).ready(function() {

			$("#inputForm").validate({
                onfocusout: function(element){
                    $(element).valid();
                },
                rules: {
                    code: {remote: "${ctx}/cont/base/checkCode?oldCode=" + $("#oldCode").val()}
                },
                messages: {
                    code: {remote: "编码已存在"}

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

            $("#btnAddFile").click(function(){
                $("#remaks").val("");
                $('#myModal').modal({backdrop: 'static', keyboard: false});
            });

        });
        function downloadFile(id) {
			 window.location.href="${ctx}/cont/applyPay/download?id="+id;

        }
        function preview(id) {
            window.open("${ctx}/cont/applyPay/preview?id="+id);

        }


        function save() {

            if($("#uploadFileForm").valid()){
                $("#uploadFileForm").submit();
			}

        }


	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cont/base/list">合同列表</a></li>
		<c:if test="${empty contract.id}">
			<li> <a href="${ctx}/cont/base/form?id=${contract.id}">合同添加</a></li>
		</c:if>

		<c:if test="${not empty contract.id}">
			<c:if test="${readonly}">
				<li> <a href="${ctx}/cont/base/form?id=${contract.id}&readonly=${readonly}">合同查看</a></li>
			</c:if>
			<c:if test="${not readonly}">
				<li > <a href="${ctx}/cont/base/form?id=${contract.id}&readonly=${readonly}">合同修改</a></li>
			</c:if>
			<li class="active"><a href="${ctx}/cont/applyPay/list?contractId=${contract.id}&readonly=${readonly}">请款附件</a></li>
			<li><a href="${ctx}/cont/attach/list?contractId=${contract.id}&readonly=${readonly}">合同附件</a></li>
			<li><a href="${ctx}/income/income/contractIncome?contractId=${contract.id}&readonly=${readonly}">合同支付</a></li>
			<li><a href="${ctx}/cont/contItem/list?contractId=${contract.id}&readonly=${readonly}">付费约定</a></li>
		</c:if>
	</ul><br/>
	<sys:message content="${message}"/>
	<div class="container-fluid">

		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span10">
	<table title="请款附件列表" class="table table-striped table-bordered table-condensed">
			<thead>
			<tr>
				<th>上传时间</th>
				<th>操作人</th>
				<th>备注</th>
				<th>操作</th>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${contApplys}" var="contApply">
				<tr>
					<td><fmt:formatDate value="${contApply.createDate}" type="both"/></td>
					<td> ${contApply.createBy.name}</td>
					<td> ${contApply.remark}</td>
					<td>
						<a href="#" onclick="downloadFile('${contApply.id}')">下载</a>
						<a href="#" onclick="preview('${contApply.id}')">预览</a>
						<c:if test="${not readonly}">
						<a href="${ctx}/cont/applyPay/delete?id=${contApply.id}&contractId=${contApply.contractId}&readonly=${readonly}" onclick="return confirmx('确认要删除该请款传附件吗？', this.href)">删除</a>
						</c:if>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>

			</div>
			<div class="span1">
			</div>
		</div>






		<div class="form-actions">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
			<c:if test="${not readonly}">
		    <input id="btnAddFile" class="btn btn-primary" type="button" value="添加附件" onclick="complex01();"/>&nbsp;
			</c:if>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
				</div>
				<div class="span1">
				</div>
			</div>
		</div>
	</div>

	<form id="uploadFileForm"  action="${ctx}/cont/applyPay/upload" method="post" enctype="multipart/form-data" class="form-horizontal">
		<input type="hidden" id="contractId" name="contractId" value="${contract.id}"/>
		<input type="hidden" id="readonly" name="readonly" value="${readonly}"/>
		<%--type="hidden" id="contractId" value="${contract.id}"/>--%>
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
							&times;
						</button>
						<h4 class="modal-title" >
							添加请款附件
						</h4>
					</div>
					<div class="modal-body">
							<div class="row-fluid">
								<div class="span1">
								</div>
								<div class="span10">
									<label >备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label>
									<textarea id="remark"  name="remark" rows="2" maxlength="200" class="input-xlarge required"></textarea>
								</div>
								<div class="span1">
								</div>
							</div>
						  <div class="row-fluid">
							  <div class="span12">
							  </div>
						  </div>
							<div class="row-fluid">
								<div class="span1">
								</div>
								<div class="span10">
									<label >附件文件：</label>
									<input type="file" id="file" name="file" class="required"/>
									<span class="help-inline">支持文件格式：pdf、doc、docx</span>
								</div>
								<div class="span1">
								</div>
							</div>

					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button type="button" class="btn btn-primary" id="addApplyPayFile" onclick="save();">提交</button>
					</div>
				</div><!-- /.modal-content -->
			</div><!-- /.modal -->
		</div>

	</form>
</body>
</html>