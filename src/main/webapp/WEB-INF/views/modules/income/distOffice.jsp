<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>进款部门分配</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        $(document).ready(function() {

        });



        function commit(){
            if('${income.draw}'=='1'){
                $.jBox.tip("请完成施工图分配", 'error');
                return
            }
            if('${income.plan}'=='1'){
                $.jBox.tip("请完成方案分配", 'error');
                return
            }

           $("#inputForm").attr("action", "${ctx}/income/distProc/officeDistSubmit");
            $("#inputForm").submit();

        }
        function add(){
            $('#addModal').modal({backdrop: 'static', keyboard: true});
        }
        function addSave(){
            if($("#distForm").validate().element($("#distValue"))){

                var type=$("#type").val()+"";
                var typeIds=$("#typeIds").val().split(",");
                var flag=true;
                $.each(typeIds,function(index,value){
                    if(value==type){
                        $.jBox.tip("类型已分配", 'error');
                        flag=false;
                    }
                });
                if(!flag){
                    return;
                }
                var sum=parseFloat($("#distValue").val())+parseFloat($("#distSum").val());
                if(sum>${income.value}){
                    $.jBox.tip("分配金额累计大于分配总金额", 'error');
                    return;
                }
                $.post("${ctx}/income/dist/addType",{incomeId:$("#incomeId").val(),type:$("#type").val(),value:$("#distValue").val()},
                    function(result){
                        if(result=="success"){
                            $.jBox.tip("收款保存成功");
                            //window.reload();
                            window.location="${ctx}/income/distProc/officeDist?id="+$("#id").val()+"&taskId=${taskId}";
                        }else{
                            $.jBox.tip("保存失败", 'error');
                        }
                    });

            }else{
                $("#distValue").focus();
                return;
            }
        }
        function del(){
            var ids=""
            $('input[name="idsSel"]:checked').each(function(){
                ids+=$(this).val()+',';
            });

            if(ids.length==0) {
                $.jBox.tip("请选择类型", 'error');
                return;
            }
            ids = ids.substring(0,ids.length - 1);
            top.$.jBox.confirm('是否要删除方案','系统提示',function(v,h,f){
                if(v=='ok'){
                    $.post("${ctx}/income/dist/deleteType",{"ids":ids,"incomeId":"${incomeId}"},
                        function(result){
                            if(result=="success"){
                                //$.jBox.tip("操作成功");
                                window.location="${ctx}/income/distProc/officeDist?id="+$("#id").val()+"&taskId=${taskId}";
                            }else{
                                $.jBox.tip("操作失败", 'error');
                            }
                        });
                }
                return true;
            });
        }
	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li class="active"><a href="${ctx}/income/distProc/officeDist?taskId=${taskId}">进款部门分配</a></li>
	<c:if test="${showDraw}">
		<li ><a href="${ctx}/income/distProc/officeDistRule?taskId=${taskId}&type=1">施工图</a></li>
	</c:if>
	<c:if test="${showPlan}">
		<li ><a href="${ctx}/income/distProc/officeDistRule?taskId=${taskId}&type=2">方案</a></li>
	</c:if>
</ul><br/>
<sys:message content="${message}"/>
<div class="form-actions">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span5">
				<label >本次收款额：</label>
				${income.value}
			</div>
			<div class="span5">
				<label >合同名称：</label>
				${income.contract.name}
			</div>
			<div class="span1">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span5">
				<label >合同编码：</label>
				${income.contract.code}
			</div>
			<div class="span5">
				<label >合同金额：</label>
				${income.contract.value}
			</div>
			<div class="span1">
			</div>
		</div>
		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span10">
				<label >项目名称：</label>
				${income.contract.project.name}
			</div>
			<div class="span1">
			</div>
		</div>
	</div>
</div>

<c:set var="saveFlag" value="true"></c:set>

<form:form id="inputForm" modelAttribute="distOfficeProc" action="${ctx}/income/distOffice/form" method="post" class="form-horizontal">

	<input type="hidden" id="incomeId" name="incomeId" value="${incomeId}"/>
	<input type="hidden" id="taskId" name="taskId"  value="${taskId}"/>

	<div id="content" >
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead><tr><th>操作</th><th>分配类型</th><th>分配金额</th></tr></thead>
			<c:set var="typeIds" value=""></c:set>
			<c:set var="sum" value="0"></c:set>
			<c:forEach items="${distTypes}" var="distType">
				<tr>
                  <td><input name="idsSel" type="checkbox" value="${distType.id}"/></td>
				  <td>${fns:getDictLabel(distType.type, 'income_dist_type', '无')}</td>
				  <td>${distType.value}</td>
				</tr>
				<c:set value="${sum + distType.value}" var="sum" />
				<c:set value="${distType.type},${typeIds}" var="typeIds" />
			</c:forEach>
			<tr>
				<td colspan="3">已分配合计金额：${sum}</td>
				<input type="hidden" id="distSum" value="${sum}" />
				<input type="hidden" id="typeIds" value="${typeIds}" />
		</table>
	</div>
	<ul class="ul-form">
		<div class="btn-toolbar ">
			<div class="btn-group">
				<c:if test="${sum<income.value}">
					<input id="btnAddType" class="btn btn-primary" type="button"  onclick="add()" value="增加类型" />
				</c:if>
				<c:if test="${sum!=0}">
					<input id="btnDelType" class="btn btn-primary" type="button"  onclick="del()" value="删除类型" />
				</c:if>
			</div>
		</div>
	</ul>

	<c:if test="${income.value-sum<=0 }">
		<div class="form-actions">
			<div class="container-fluid">

				<div class="row-fluid">
					<div class="span1">
					</div>
					<div class="span10">
						<label >备注:</label>
						<form:textarea path="comment" htmlEscape="false" rows="3" maxlength="500" class="input-xxlarge" />
						<div class="span1">
						</div>
					</div>
					<div class="row-fluid">
						<div class="span12">
						</div>
					</div>
					<div class="row-fluid">
						<div class="span5">
						</div>

						<div class="span2">
							<input id="btnSubmit" class="btn btn-primary" type="button"  onclick="commit()" value="提交" />
						</div>

						<div class="span5">
						</div>
					</div>
				</div>
			</div>
		</div>
	</c:if>

</form:form>
<form:form id="distForm" modelAttribute="distType" action="${ctx}/income/distType/save" method="post" class="form-horizontal">
<%--<form id="distForm"   action="${ctx}/income/distType/save" method="post" class="form-horizontal">--%>
	<div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
						&times;
					</button>
					<h4 class="modal-title" >
						添加分配类型
					</h4>
				</div>
				<div class="modal-body">
					<div class="container-fluid">
						<div class="form-group">
							<div class="row-fluid">
								<div class="span1">
								</div>
								<div class="span10">
									<label >分配类型:</label>
									<form:select path="type" class="input-small ">
										<form:options items="${fns:getDictList('income_dist_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
									</form:select>
								</div>
								<div class="span1">
								</div>
							</div>
						</div>
						<div class="form-group">
							<div class="row-fluid">
								<div class="span1">
								</div>
								<div class="span10">
									<label >分配金额:</label>
									<input id="distValue" class="input-small"  onkeyup="onlyFloatTowPoint(this)" required="true"/>

								</div>
								<div class="span1">
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" onclick="addSave()">提交</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
	</div>


</form:form>
<br>
<%@ include file="/WEB-INF/views/modules/act/comment.jsp" %>
</body>
</html>