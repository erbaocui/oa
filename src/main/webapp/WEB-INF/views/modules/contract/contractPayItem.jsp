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
                $("#content").val("");
                $('#myModal').modal({backdrop: 'static', keyboard: false});
            });

        });

		function save() {

            if($("#contentForm").valid()){
                $("#contentForm").submit();
			}
        }

        function del(id) {
            confirmx('确认要删除该付款约定吗？',"${ctx}/cont/contItem/delete?id="+id+"&contractId=${contractId}&readonly=${readonly}");
        }

	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li><a href="${ctx}/cont/base/list">合同列表</a></li>
	<c:if test="${empty contract.id}">
		<li> <a href="${ctx}/cont/base/form?id=${contract.id}">合同添加</a></li>
	</c:if>

	<c:if test="${not empty contractId}">
		<c:if test="${readonly}">
			<li> <a href="${ctx}/cont/base/form?id=${contract.id}&readonly=${readonly}">合同查看</a></li>
		</c:if>
		<c:if test="${not readonly}">
			<li > <a href="${ctx}/cont/base/form?id=${contract.id}&readonly=${readonly}">合同修改</a></li>
		</c:if>
		<li class="active" ><a href="${ctx}/cont/contItem/list?contractId=${contract.id}&readonly=${readonly}">付费约定</a></li>
		<li><a href="${ctx}/cont/attach/list?contractId=${contract.id}&readonly=${readonly}">合同附件</a></li>
		<li><a href="${ctx}/cont/applyPay/list?contractId=${contract.id}&readonly=${readonly}">合同请款</a></li>
		<li><a href="${ctx}/income/income/contractIncome?contractId=${contract.id}&readonly=${readonly}">合同支付</a></li>

	</c:if>
</ul><br/>
	<sys:message content="${message}"/>
   <div class="container-fluid">
	<c:forEach items="${contItems}" var="contItem" varStatus="status">

				<div class="control-group">
					<div class="row-fluid">
						<div class="span1">
						</div>
						<div class="span7">
							<label >${status.index+1}、</label>&nbsp;
							<textarea   rows="6" maxlength="500" class="" style="width:800px;font-size:14px;" readonly="readonly">${contItem.content}</textarea>

						</div>
						<div class="span3" style="padding-top:20px;">
							<c:if test="${not readonly}">
							<input  class="btn btn-primary" type="button" value="删除" onclick="del('${contItem.id}');"/>
							</c:if>
						</div>
						<div class="span1">
						</div>
					</div>
				</div>

			</c:forEach>








		<div class="form-actions">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
			<c:if test="${not readonly}">
			<input id="btnAddFile" class="btn btn-primary" type="button" value="添加" onclick="complex01();"/>&nbsp;
			</c:if>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
				</div>
				<div class="span1">
				</div>
			</div>
		</div>
   </div>

	<form id="contentForm"  action="${ctx}/cont/contItem/save" method="post" enctype="multipart/form-data" class="form-horizontal">
		<input type="hidden" id="contractId" name="contractId" value="${contractId}"/>
		<input type="hidden" id="readonly" name="readonly" value="${readonly}"/>
		<%--type="hidden" id="contractId" value="${contract.id}"/>--%>
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog" >
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
							&times;
						</button>
						<h4 class="modal-title" >
							增加付款约定
						</h4>
					</div>
					<div class="modal-body" >
							<div class="row-fluid">
								<div class="span1">
								</div>
								<div class="span9">
									<textarea id="content"  name="content" rows="3" maxlength="500" class="input-xxlarge"></textarea>
								</div>
								<div class="span2">
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