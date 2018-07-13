<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>收款部门分配</title>
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

			$("#btnDistributeOffice").click(function(){
                $('#addModal').modal({backdrop: 'static', keyboard: true});
			});

            $("#btnSubmit").click(function(){
                $("#reviewForm").submit();
            });

            $("#addDistOffice").click(function(){

				if($("#distForm").validate().element($("#distValue"))){
                    if($("#officeId").val()==null||$("#officeId").val()==""){
                        $.jBox.tip("请选择部门", 'error');
                        return;
                    }
                    var officeId=$("#officeId").val()+"";
                    var officeIds=$("#officeIds").val().split(",");
                    var flag=true;
                    $.each(officeIds,function(index,value){
                          if(value==officeId){
                              $.jBox.tip("部门已分配", 'error');
                              flag=false;
						  }
                    });
                    if(!flag){
                        return;
					}
                    var sum=parseFloat($("#distValue").val())+parseFloat($("#distSum").val());
                    if(sum>$("#value").val()){
                        $.jBox.tip("部门分配金额累计大于分配总金额", 'error');
                        return;
                    }
                    $.post("${ctx}/income/distOffice/add",{incomeId:$("#id").val(),officeId:$("#officeId").val(),value:$("#distValue").val()},
						function(result){
							if(result=="success"){
								$.jBox.tip("收款保存成功");
								window.location="${ctx}/income/distProc/officeDist?id="+$("#id").val();
							}else{
                                $.jBox.tip("保存失败", 'error');
							}
                    });

               }else{
                    $("#distValue").focus();
                    return;
                }
            });

		});

 function delDistOffice(id){
     top.$.jBox.confirm('是否要删除部门分配','系统提示',function(v,h,f){
         if(v=='ok'){
             $.post("${ctx}/income/distOffice/delete",{"id":id},
                 function(result){
                     if(result=="success"){
                         $.jBox.tip("操作成功");
                         window.location="${ctx}/income/distProc/officeDist?id="+$("#id").val();
                     }else{
                         $.jBox.tip("操作失败", 'error');
                     }
                 });
         }
         return true;
     });

   /*
	 $.post("<%--${ctx}--%>/income/distOffice/delete",{"id":id},
                function(result){
                    if(result=="success"){
                        $.jBox.tip("操作成功");
                        window.location="${ctx}/income/income/form?id="+$("#id").val();
                    }else{
                        $.jBox.tip("操作失败", 'error');
                    }
                });*/

 }
 	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="">收款部门分配</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="income" action="${ctx}/income/income/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>
		<div class="control-group">
			<label class="control-label">本次收款额：</label>
			<div class="controls">
				<form:input path="value" htmlEscape="false" class="input-xlarge "  readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同名称：</label>
			<div class="controls">
				<form:input path="contract.name" htmlEscape="false" maxlength="255" class="input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同编码：</label>
			<div class="controls">
				<form:input path="contract.code" htmlEscape="false" maxlength="255" class="input-xlarge " readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">合同金额：</label>
			<div class="controls">
				<form:input path="contract.value" htmlEscape="false" maxlength="255" class="input-xlarge "  readonly="true"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">已收金额：</label>
			<div class="controls">
				<form:input path="contract.income" htmlEscape="false" maxlength="255" class="input-xlarge "  readonly="true"/>
			</div>
		</div>

		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead>
			<tr>
				<th>分配部门</th>
				<th>分配金额</th>
				<th>操作时间</th>
				<th>操作人</th>
				<th>操作</th>
			</tr>
			</thead>
			<tbody>
			<c:set var="officeIds" value=""></c:set>
			<c:set var="sum" value="0"></c:set>
			<c:forEach items="${income.distOfficeList}" var="distOffice">
				<tr>
					<td>${distOffice.office.name}</td>
					<td>${distOffice.value}</td>
					<td><fmt:formatDate value="${distOffice.createDate}" type="both"/></td>
					<td>${distOffice.createBy.name}</td>

					<td>
						<c:if test="${1==1}">
							<a href="#" onclick="delDistOffice('${distOffice.id}')">删除</a>
						</c:if>
					</td>
				</tr>
				<c:set value="${sum + distOffice.value}" var="sum" />
				<c:set value="${officeIds},${distOffice.office.id}" var="officeIds" />
			</c:forEach>
			<tr>
				<td colspan="5">已分配合计金额：${sum}</td>
				<input type="hidden" id="distSum" value="${sum}" />
				<input type="hidden" id="officeIds" value="${officeIds}" />
			</tr>
			</tbody>
		</table>
	   </form:form>
		<form:form id="reviewForm" modelAttribute="review"   action="${ctx}/income/distProc/officeDistSubmit" method="post" class="form-horizontal">
			${taskId}
			<form:hidden path="taskId"  value="${taskId}"/>

		<div class="form-actions">
			<c:if test="${income.value-sum>0}">
			<input id="btnDistributeOffice" class="btn btn-primary" type="button" value="增加分配部门" />
			</c:if>
			<c:if test="${income.value-sum<=0}">
			<div class="container-fluid">

					<div class="row-fluid">
						<div class="span1">
						</div>
						<div class="span10">
							<label >备注:</label>
							<form:textarea path="comment" htmlEscape="false" rows="3" maxlength="500" class="input-xxlarge" />
<%--							<textarea id="comment"  rows="3" maxlength="500" class="input-xxlarge" ></textarea>--%>
						</div>
						<div class="span1">
						</div>
					</div>
				   <div class="row-fluid">
					   <div class="span12">
					   </div>
				   </div>
				   <div class="row-fluid">
						<div class="span3">
						</div>
						<div class="span8">
							<input id="btnSubmit" class="btn btn-primary" type="button"  value="提交" />
						</div>
						<div class="span1">
						</div>
					</div>
			  </div>

			</c:if>
		</div>
	</form:form>
		<form id="distForm"   action="${ctx}/income/distOffice/save" method="post" class="form-horizontal">
			<div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
				<div class="modal-dialog">
					<div class="modal-content">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
								&times;
							</button>
							<h4 class="modal-title" >
								添加分配部门
							</h4>
						</div>
						<div class="modal-body">
							<div class="container-fluid">
								<div class="form-group">
									<div class="row-fluid">
										<div class="span1">
										</div>
										<div class="span10">
											<label >签约部门:</label>
											<sys:treeselect id="office" name="office.id" value="" labelName="office.name" labelValue=""
															title="部门" url="/sys/office/treeData?type=2" cssClass="input-mini" allowClear="true" notAllowSelectParent="true"/>
										</div>
										<div class="span2">
										</div>
									</div>
								</div>
								<div class="form-group">
									<div class="row-fluid">
										<div class="span1">
										</div>
										<div class="span10">
											<label >分配金额:</label>
											<input id="distValue" class="input-small digits required"  />

										</div>
								        <div class="span2">
										</div>
									 </div>
									</div>
								</div>
							</div>
						</div>
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
							<button type="button" class="btn btn-primary" id="addDistOffice">提交</button>
						</div>
					</div><!-- /.modal-content -->
				</div><!-- /.modal -->
			</div>


		</form>
	<table title="批注列表" class="table table-striped table-bordered table-condensed">
		<thead>
		<tr>
			<th>批注时间</th>
			<th>批注人</th>
			<th>批注信息</th>
		</tr>
		</thead>
		<tbody>
		<c:forEach items="${comments}" var="item">
			<tr>
				<td><fmt:formatDate value="${item.time}" type="both"/></td>
				<td> ${item.userId}</td>
				<td> ${item.message}</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>

</body>
</html>