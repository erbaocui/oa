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
			 window.location.href="${ctx}/cont/base/attachDownload?id="+id;

        }
        function preview(id) {
            window.open("${ctx}/cont/base/attachPreview?id="+id);

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
		<li><a href="${ctx}/cont/base/form?id=${contract.id}">合同<shiro:hasPermission name="cont:base:edit">${not empty contract.id?'修改':'添加'}</shiro:hasPermission><shiro:lacksPermission name="cont:base:edit">查看</shiro:lacksPermission></a></li>
		<c:if test="${not empty contract.id}">
		    <li ><a href="${ctx}/cont/base/applyPay?id=${contract.id}">请款附件</a></li>
			<li class="active"><a href="${ctx}/cont/base/attach?id=${contract.id}">合同附件</a></li>
		</c:if>
	</ul>
	<sys:message content="${message}"/>
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
			<c:forEach items="${contAttachs}" var="contAttach">
				<tr>
					<td><fmt:formatDate value="${contAttach.createDate}" type="both"/></td>
					<td> ${contAttach.createBy.name}</td>
					<td> ${contAttach.remark}</td>
					<td>
						<a href="#" onclick="downloadFile('${contAttach.id}')">下载</a>
						<a href="#" onclick="preview('${contAttach.id}')">预览</a>
						<a href="${ctx}/cont/base/attachDelete?id=${contAttach.id}&contractId=${contAttach.contractId}" onclick="return confirmx('确认要删除该合同传附件吗？', this.href)">删除</a>
					</td>
				</tr>
			</c:forEach>
			</tbody>
		</table>







		<div class="form-actions">
			<shiro:hasPermission name="cont:base:edit"><input id="btnAddFile" class="btn btn-primary" type="button" value="添加附件" onclick="complex01();"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>


	<form id="uploadFileForm"  action="${ctx}/cont/base/attachUpload" method="post" enctype="multipart/form-data" class="form-horizontal">
		<input type="hidden" id="contractId" name="contractId" value="${contract.id}"/>
		<%--type="hidden" id="contractId" value="${contract.id}"/>--%>
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
							&times;
						</button>
						<h4 class="modal-title" >
							添加合同附件
						</h4>
					</div>
					<div class="modal-body">
							<div class="row-fluid">
								<div class="span1">
								</div>
								<div class="span10">
									<label >备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label>
									<textarea id="remark"  name="remark" rows="2" maxlength="200" class="input-xlarge"></textarea>
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
						<button type="button" class="btn btn-primary" id="addAttachPayFile" onclick="save();">提交</button>
					</div>
				</div><!-- /.modal-content -->
			</div><!-- /.modal -->
		</div>

	</form>
</body>
</html>