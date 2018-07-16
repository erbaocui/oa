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


            $("#btnOpenIncome").click(function(){
                $("#incomeValue").val("");
                $('#myModal').modal();
            });

		});

         function add(){
            if($("#incomeForm").validate().element($("#incomeValue"))){
                var sum=parseInt($("#incomeValue").val())+parseInt($("#incomeSum").val());
                if(sum>$("#receiptValue").val()){
                    $.jBox.tip("收款金额累计大于请款金额", 'error');
                    return;
                }
                //$("#incomeForm").submit();
                $.post("${ctx}/income/income/add",{applyId:"${contApply.id}",incomeValue:$("#incomeValue").val()},function(result){
                    if(result=="success"){
                        top.$.jBox.tip.mess=1;
                        $.jBox.tip("收款保存成功");
                        top.$.jBox.tip.mess=null;
                        //window.location="${ctx}/income/applyPay/income?id="+$("#id").val();
                        location.reload();


                    }
                });
            }else{
                $("#incomeValue").focus();
            }
        }

        function del(id){

            top.$.jBox.confirm("确认删除收款信息吗",'系统提示',function(v,h,f){
                if(v=='ok'){
                    $.post("${ctx}/income/income/delete",{'id':id},function(result){
                        if(result=="success"){
                           // top.$.jBox.tip.mess=1000;
                            //$.jBox.tip("收款删除成功");
                            //top.$.jBox.tip.mess=null;
                            //window.location="${ctx}/income/applyPay/income?id="+$("#id").val();
                            //sleep(2000)
							location.reload();


                        } else{
                            $.jBox.tip("收款删除失败");
                        }
					})
				}
            },{buttonsFocus:1, closed:function(){
                if (typeof closed == 'function') {
                    closed();
                }
            }});

        }


        function startProcess(id) {

            $.post("${ctx}/income/distProc/start?",{id:id},function(data){
                var code=data.result;
                if(code=='success'){

                    $.jBox.tip("流程启动成功");
                    location.reload();


                }else{
                    $.jBox.tip("流程启动失败", 'error');
                }
            });

        }

        function sleep(numberMillis) {
            var now = new Date();
            var exitTime = now.getTime() + numberMillis;
            while (true) {
                now = new Date();
                if (now.getTime() > exitTime)
                    return;
            }
        }

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/cont/applyPay/incomeList">收款列表</a></li>
		<li class="active"><a href="${ctx}/income/income/applyPay?id=${contApply.id}">收款</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="contApply" action="${ctx}/income/applyPay/save" method="post" class="form-horizontal">
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">甲方名称：</label>
			<div class="controls">
				<form:input path="receiptName" htmlEscape="false" maxlength="255" class="input-xlarge " disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">请款金额：</label>
			<div class="controls">
				<form:input path="receiptValue" htmlEscape="false" class="input-xlarge " disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">已收已分：</label>
			<div class="controls">
				<form:input path="income" htmlEscape="false" class="input-xlarge " disabled="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">备注：</label>
			<div class="controls">
				<form:textarea path="remark" htmlEscape="false" rows="4" maxlength="255" class="input-xxlarge " disabled="true"/>
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
			<c:set var="sum" value="0"></c:set>
			<c:forEach items="${incomes}" var="income">
				<tr>
					<td><fmt:formatDate value="${income.createDate}" type="both"/></td>
					<td>${income.value}</td>
					<td>${fns:getDictLabel(income.status, 'income_status', '无')}</td>
					<td>${income.createBy.name}</td>

					<td>
						<c:if test="${income.status==1}">
						<a href="#" onclick="startProcess('${income.id}')">启动分配流程</a>
						<a href="#" onclick="del('${income.id}');">删除</a>
					<%--	<a href="#" onclick="return confirmx('确认要删除该收款吗？', this.href)">删除</a>--%>
						</c:if>

					</td>
				</tr>
				<c:set value="${sum + income.value}" var="sum" />
			</c:forEach>
			   <tr>
				   <td colspan="5">合计金额：${sum}</td>
                   <input type="hidden" id="incomeSum" value="${sum}" />
			   </tr>
			</tbody>
		</table>
		<div class="pagination">${page}</div>




		<div class="form-actions">
<%--			<shiro:hasPermission name="income:applyPay:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
			<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>--%>
		     <c:if test="${sum<contApply.receiptValue}">
				<input id="btnOpenIncome" class="btn btn-primary" type="button" value="添加收款" />
		     </c:if>
	         <input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>

		</div>


	</form:form>
	<form id="incomeForm"  action="${ctx}/income/income/add" method="get" class="form-horizontal">
		<input type="hidden" id="applyId" value="${contApply.id}"/>
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
						<div class="row-fluid">
							<div class="span2">
							</div>
							<div class="span9">
								<label >收款金额：</label>
								<input type="text"  class="input-small digits required" id="incomeValue" />
							</div>
							<div class="span1">
							</div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
						<button type="button" class="btn btn-primary" id="addIncome" onclick="add()">提交</button>
					</div>
				</div><!-- /.modal-content -->
			</div><!-- /.modal -->
		</div>

	</form>
</body>
</html>