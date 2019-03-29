<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>进款部门分配</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
        $(document).ready(function() {

        });

        function selRule() {
            $("#inputForm").attr("action", "${ctx}/income/distProc/officeDistRule");
            $("#inputForm").submit();
        }

        function save() {
            $("#inputForm").attr("action", "${ctx}/income/distOffice/saveDist");
            $("#inputForm").submit();
        }
        function saveAccount() {
            $("#inputForm").attr("action", "${ctx}/income/distOffice/saveAccount");
            $("#inputForm").submit();
        }

        function add(){
            $('#addModal').modal({backdrop: 'static', keyboard: true});
        }
        function ruleSave(){
            $("#inputForm").attr("action", "${ctx}/income/distProc/officeDistSave");
            $("#inputForm").submit();
        }
        function addSave(){
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
                if(sum>${distType.value}){
                    $.jBox.tip("部门分配金额累计大于分配总金额", 'error');
                    return;
                }
                console.log(sum);
                console.log($("#value").val());
                $.post("${ctx}/income/distOffice/add",
					{
					    incomeId:$("#incomeId").val(),
                    typeId:$("#typeId").val(),
					officeId:$("#officeId").val(),
						value:$("#distValue").val()},
                    function(result){
                        if(result=="success"){
                            $.jBox.tip("收款保存成功");
                            //window.reload();
                            window.location="${ctx}/income/distProc/officeDistRule?type=${type}&taskId=${taskId}";
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
            top.$.jBox.confirm('是否要删除部门分配','系统提示',function(v,h,f){
                if(v=='ok'){
                    $.post("${ctx}/income/dist/deleteOffice",{"ids":ids,'type':'${type}','incomeId':'${incomeId}'},
                        function(result){
                            if(result=="success"){
                                //$.jBox.tip("操作成功");
                                window.location="${ctx}/income/distProc/officeDistRule?type=${type}&taskId=${taskId}";
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
	<li ><a href="${ctx}/income/distProc/officeDist?taskId=${taskId}">进款部门分配</a></li>
	<c:if test="${showDraw}">
		<li ${type=='1'?'class="active"':''} ><a href="${ctx}/income/distProc/officeDistRule?taskId=${taskId}&type=1">施工图</a></li>
	</c:if>
	<c:if test="${showPlan}">
		<li ${type=='2'?'class="active"':''}><a href="${ctx}/income/distProc/officeDistRule?taskId=${taskId}&type=2">方案</a></li>
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
			<div class="span5">
				<label >项目名称：</label>
				${income.contract.project.name}
			</div>
			<div class="span5">
				<label >${type=='1'?'施工图':''}${type=='2'?'方案':''}费用：</label>
				${distType.value}
			</div>
			<div class="span1"></div>
		</div>
	</div>
</div>

<c:set var="saveFlag" value="true"></c:set>

<form:form id="inputForm" modelAttribute="distOfficeProc" action="${ctx}/income/distOffice/form" method="post" class="form-horizontal">
	<form:hidden path="id"/>
	<form:hidden path="incomeId" value="${incomeId}"/>
	<form:hidden path="typeId" value="${typeId}"/>
	<form:hidden path="taskId"  value="${taskId}"/>
	<input type="hidden" name="type" value="${type}"/>
	<div id="content" >
		<table id="contentTable" class="table table-striped table-bordered table-condensed">
			<thead><tr><th>操作</th><th>分配部门</th><th>部门金额</th><th>规则选择</th><th>规则内费用</th><th>费用项</th><th>明细</th></tr></thead>
			<c:set var="officeIds" value=""></c:set>
			<c:set var="sum" value="0"></c:set>
			<c:forEach items="${distOffices}" var="distOffice">

				<tr>
				<td rowspan="${distOffice.rowspan}"><input name="idsSel" type="checkbox" value="${distOffice.id}"/></td>
				<td rowspan="${distOffice.rowspan}">${distOffice.officeName}</td>
				<td rowspan="${distOffice.rowspan}">${distOffice.value}</td>
				<td rowspan="${distOffice.rowspan}">

					<select name="groups" onchange="selRule()" >
						<c:forEach items="${distOffice.ruleGroups}" var="ruleGroup">

							<c:choose>
								<c:when test="${distOffice.ruleGroupId == ruleGroup.id}">
									<option value="${ruleGroup.id}" selected="selected" >${ruleGroup.name}</option>
								</c:when>
								<c:otherwise>
									<option value="${ruleGroup.id}">${ruleGroup.name}</option>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td>
				<c:if test="${distOffice.ruleGroupId== null||distOffice.ruleGroupId==''}">
					<c:set value="false" var="saveFlag" />
				</c:if>
				<c:if test="${distOffice.rules == null}">
					<td ></td><td ></td><td ></td>
				</c:if>
				<c:forEach items="${distOffice.rules}" var="rule">
					<td rowspan="${rule.rowspan}">${rule.value}</td>
					<c:forEach items="${rule.roleItems}" var="roleItem" varStatus="status">
						<c:if test="${status.index != 0}">
							<tr>
						</c:if>
						<td rowspan="${roleItem.rowspan}">${roleItem.name}:${roleItem.value}</td>
						<c:forEach items="${roleItem.distributes}" var="distribut">
							<td>${distribut.name}:${distribut.value}</td>
							</tr>
						</c:forEach>
					</c:forEach>
				</c:forEach>


				</tr>

				<c:set value="${sum + distOffice.value}" var="sum" />
				<c:set value="${officeIds},${distOffice.officeId}" var="officeIds" />

			</c:forEach>
			<tr>
				<td colspan="7">已分配合计金额：${sum}</td>
				<input type="hidden" id="distSum" value="${sum}" />
				<input type="hidden" id="officeIds" value="${officeIds}" />
			</tr>
		</table>
	</div>
	<ul class="ul-form">
		<div class="btn-toolbar ">
			<div class="btn-group">
				<c:if test="${sum<distType.value}">
					<input id="btnAddOffice" class="btn btn-primary" type="button"  onclick="add()" value="增加部门" />
				</c:if>
				<c:if test="${sum!=0}">
					<input id="btnDelOffice" class="btn btn-primary" type="button"  onclick="del()" value="删除部门" />
				</c:if>
			</div>
		</div>
	</ul>
	<c:if test="${distType.value-sum<=0.00 && saveFlag==true}">
		<div class="form-actions">
			<div class="container-fluid">

				<div class="row-fluid">

					<div class="row-fluid">
						<div class="span12">
						</div>
					</div>
					<div class="row-fluid">
						<div class="span5">
						</div>

						<div class="span2">
							<input id="btnSubmit" class="btn btn-primary" type="button"  onclick="ruleSave()" value="保存" />
						</div>

						<div class="span5">
						</div>
					</div>
					<div class="row-fluid">
						<div class="span12">
						</div>
					</div>
				</div>
			</div>
		</div>
	</c:if>

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
									<sys:treeselect id="office" name="office.id" value="" labelName="office.name" labelValue="" isAll="true"
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
									<input id="distValue" class="input-small"  onkeyup="onlyFloatTowPoint(this)" required="true"/>

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
				<button type="button" class="btn btn-primary" onclick="addSave()">提交</button>
			</div>
		</div><!-- /.modal-content -->
	</div><!-- /.modal -->
	</div>


</form>
</body>
</html>