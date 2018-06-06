<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收款管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        var contractId=null;
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
			$("#addIncome").click(function(){
              if($("#incomeForm").validate().element($("#incomeValue"))){
				  var sum=parseInt($("#incomeValue").val())+parseInt($("#incomeSum").val());
				  if(sum>$("#applyValue").val()){
                      $.jBox.tip("收款金额累计大于请款金额", 'error');
                      return;
				  }
                 //$("#incomeForm").submit();
                 $.post("${ctx}/income/applyPay/addIncome",{applyId:$("#id").val(),incomeValue:$("#incomeValue").val()},function(result){
					 if(result=="success"){
                         $.jBox.tip("收款保存成功");
                         window.location="${ctx}/income/applyPay/income?id="+$("#id").val();

					 }
                 });
              }else{
                  $("#incomeValue").focus();
              }
			});

            $("#btnOpenIncome").click(function(){
                $("#incomeValue").val("");
                $('#myModal').modal();
            });

		});

        function startProcess(id) {

            $.post("${ctx}/income/distProc/start?",{id:id},function(data){
                var code=data.result;
                if(code=='success'){
                    $.jBox.tip("流程启动成功");
                    page();

                }else{
                    $.jBox.tip("流程启动失败", 'error');
                }
            });

        }

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/income/applyPay/list">请款列表</a></li>
		<li class="active"><a href="${ctx}/income/applyPay/form?id=${apply.id}">收款</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="apply" action="${ctx}/income/applyPay/save" method="post" class="form-horizontal">
		<form:hidden path="id" />
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">请款名称：</label>
			<div class="controls">
				<form:input path="name" htmlEscape="false" maxlength="255" class="input-xlarge "/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">请款金额：</label>
			<div class="controls">
				<form:input path="applyValue" htmlEscape="false" class="input-xlarge " disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">甲方名称：</label>
			<div class="controls">
				<form:input path="firstParty" htmlEscape="false" maxlength="255" class="input-xlarge " disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge " disabled="true"/>
			</div>
		</div>


		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
			<tr>
				<th>时间</th>
				<th>收款金额</th>
				<th>状态</th>
				<th>收款流程操作人</th>
				<th>操作</th>
			</tr>
			</thead>
			<tbody>
			<c:set var="sum" value=""></c:set>
			<c:forEach items="${apply.incomeList}" var="income">
				<tr>
					<td><fmt:formatDate value="${income.createDate}" type="both"/></td>
					<td>${income.value}</td>
					<td>${fns:getDictLabel(income.status, 'income_status', '无')}</td>
					<td>${income.createBy.name}</td>

					<td>
						<c:if test="${income.status==1}">
						<a href="#" onclick="startProcess('${income.id}')">启动流程</a>
						<a href="${ctx}/income/applyPay/delIncome?applyId=${apply.id}&incomeId=${income.id}" onclick="return confirmx('确认要删除该收款吗？', this.href)">删除</a>
					<%--	<a href="#" onclick="return confirmx('确认要删除该收款吗？', this.href)">删除</a>--%>
						</c:if>

					</td>
				</tr>
				<c:set value="${sum + income.value}" var="sum" />
			</c:forEach>
			   <tr>
				   <td colspan="4">合计金额：${sum}</td>
                   <input type="hidden" id="incomeSum" value="${sum}" />
			   </tr>
			</tbody>
		</table>
		<div class="pagination">${page}</div>



		<c:if test="${sum<apply.applyValue}">
		<div class="form-actions">
<%--			<shiro:hasPermission name="income:applyPay:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>--%>

				<input id="btnOpenIncome" class="btn btn-primary" type="button" value="添加收款" />
	            <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>

		</div>
		</c:if>

	</form:form>
	<form id="incomeForm"  action="${ctx}/income/applyPay/addIncome" method="get" class="form-horizontal">
		<input type="hidden" id="applyId" value="${apply.id}"/>
		<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
							&times;
						</button>
						<h4 class="modal-title" >
							添加收款
						</h4>
					</div>
					<div class="modal-body">
						<div class="form-group">
							<label >收款金额：</label>
							<input type="text"  class="input-small digits required" id="incomeValue" />
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button type="button" class="btn btn-primary" id="addIncome">提交</button>
					</div>
				</div><!-- /.modal-content -->
			</div><!-- /.modal -->
		</div>

	</form>
</body>
</html>