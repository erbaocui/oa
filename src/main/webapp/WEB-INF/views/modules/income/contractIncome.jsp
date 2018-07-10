<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同收款</title>
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
		<li ><a href="${ctx}/cont/applyPay/list?contractId=${contract.id}&readonly=${readonly}">请款附件</a></li>
		<li><a href="${ctx}/cont/attach/list?contractId=${contract.id}&readonly=${readonly}">合同附件</a></li>
		<li class="active"><a href="${ctx}/income/income/contractIncome?contractId=${contract.id}&readonly=${readonly}">合同支付</a></li>
		<li><a href="${ctx}/cont/contItem/list?contractId=${contract.id}&readonly=${readonly}">付费约定</a></li>
	</c:if>
</ul><br/>
<sys:message content="${message}"/>




<div class="container-fluid">

		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span10">
				<label >合同名称:</label>
				${contract.name}
			</div>
			<div class="span1">
			</div>
	</div>
	<div class="row-fluid">
		<div class="span1">
		</div>
		<div class="span10">
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
			<c:forEach items="${incomes}" var="income">
				<tr>
					<td><fmt:formatDate value="${income.createDate}" type="both"/></td>
					<td>${income.value}</td>
					<td>${fns:getDictLabel(income.status, 'income_status', '无')}</td>
					<td>${income.createBy.name}</td>

					<td>
						<c:if test="${income.status==3}">
						<a href="${ctx}/income/distOffice/form?incomeId=${income.id}&editable=false"  title="分配详情" target="_blank">查看详情</a>
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
		</div>
		<div class="span1">
		</div>
	</div>
</div>

</body>
</html>