<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同管理</title>
	<meta name="decorator" content="default"/>

	<script type="text/javascript">

        $(document).ready(function() {

            $('#myModal').hide();
        });
        function itemAdd(){

            $("#action").val(0);
            $("#title").html("合同拆分添加");
            $("#typeDiv").show();
            $("#id").val(null);
            $("#remark").val(null);
            $("#type").val(0).trigger('change');
			$("#value").val(null);
            $('#myModal').modal({backdrop: 'static', keyboard: false});
        }
        function itemEdit(id,type,value,remark){
            $("#action").val(0);
            $("#title").html("合同拆分编辑");
            $("#id").val(id);
            $("#type").val(type).trigger('change');
            $("#remark").val(remark);
            $("#value").val(value);
            $('#myModal').modal({backdrop: 'static', keyboard: false});
        }
		function itemSave() {
            if($("#form").valid()){
                if(${contSplit.draw}==1&&$("#type").val()==0){
                    $.jBox.tip("已添加过施工图", 'error');
                    return;
				}
                if(${contSplit.plan}==1&&$("#type").val()==1){
                    $.jBox.tip("已添加过方案", 'error');
                    return;
                }
                var  itemSum=Number($('#itemSum').val());
                var  itemValue=Number($('#value').val());
                var  contractValue=${contract.value};

                if(itemSum+itemValue>contractValue){
                    $.jBox.tip('拆分金额大于合同金额', 'error');
                    return;
                }

                $.post("${ctx}/cont/split/item/save",
                    {
                        splitId:'${contSplit.id}',
						type:$('#type').val(),
                        value:$('#value').val(),
                        remark:$("#remark").val()
                    },
                    function(result){
                        if(result=="success"){
                            $("#detailModal").modal('hide');
                            $.jBox.tip("合同拆解保存成功");
                            setTimeout(function () {
                                window.location="${ctx}/cont/split/list?contractId=${contractId}&readonly=${readonly}";
                            }, 500);
                        }else{
                            $.jBox.tip("合同拆解保存失败", 'error');
                        }
                    });
			}
        }
        function itemDelete(id){

            top.$.jBox.confirm("确认要删除该合同拆分？",'系统提示',function(v,h,f){
                if(v=='ok'){
                    resetTip(); //loading();
                    $.post("${ctx}/cont/split/item/delete",
                        {
                            id:id
                        },
                        function(result){
                            if(result=="success"){
                                $("#officeModal").modal('hide');
                                $.jBox.tip("合同拆分细化删除成功");
                                setTimeout(function () {
                                    window.location="${ctx}/cont/split/list?contractId=${contractId}&readonly=${readonly}";
                                }, 500);
                            }else{
                                $.jBox.tip("合同拆分细化删除失败", 'error');
                            }
                        });


                }
            },{buttonsFocus:1, closed:function(){
                    if (typeof closed == 'function') {
                        closed();
                    }
                }});
            top.$('.jbox-body .jbox-icon').css('top','55px');



        }
        function startProcess(id) {
            var  itemSum=Number($('#itemSum').val());
            if(itemSum==0){
                $.jBox.tip('请先配置类型', 'error');
                return;
            }
            $.post("${ctx}/cont/split/proc/start?",{id:id},function(data){
                        var code=data.result;
                        if(code=='success'){
                            setTimeout(function () {
                                window.location="${ctx}/cont/split/list?contractId=${contractId}&readonly=${readonly}";
                            }, 500);

                        }else{
                            top.$.jBox.tip("流程启动失败","error",{persistent:true,opacity:0});
                        }
			});






        }



        function detailSet(id){
            window.location.href="${ctx}/cont/split/detail/list?id="+id+"&contractId=${contract.id}&readonly=${readonly}&single=single";
        }
        function detailAdd() {
            $("#action").val(1);
            $("#title").html("合同拆分细化添加");
            $("#typeDiv").hide();
			$("#remark").val(null);
            $("#value").val(null);
            $('#myModal').modal({backdrop: 'static', keyboard: false});

        }
        function detailSave(){
            if($("#form").valid()){

				var  detailSum=Number($('#detailSum').val());
                var  detailValue=Number($('#value').val());
                var  contractValue=${contract.value};

                if(detailSum+ detailValue>contractValue){
                    $.jBox.tip('金额大于合同金额', 'error');
                    return;
                }

                $.post("${ctx}/cont/split/detail/save",
                    {
                        contractId:'${contractId}',
                        total:$('#value').val(),
                        remark:$("#remark").val()
                    },
                    function(result){
                        if(result=="success"){
                            $("#detailModal").modal('hide');
                            $.jBox.tip("合同拆解细化保存成功");
                            setTimeout(function () {
                                window.location="${ctx}/cont/split/list?contractId=${contractId}&readonly=${readonly}";
                            }, 500);
                        }else{
                            $.jBox.tip("合同拆解细化保存失败", 'error');
                        }
                    });
            }
        }

        function detailDelete(id){
            top.$.jBox.confirm("确认要删除该合同拆分细化？",'系统提示',function(v,h,f){
                if(v=='ok'){
                    resetTip(); //loading();
                    $.post("${ctx}/cont/split/detail/delete",
                        {
                            id:id
                        },
                        function(result){
                            if(result=="success"){
                                //$("#myModal").modal('hide');
                                $.jBox.tip("合同拆分细化删除成功");
                                setTimeout(function () {
                                    window.location="${ctx}/cont/split/list?contractId=${contractId}&readonly=${readonly}";
                                }, 500);
                            }else{
                                $.jBox.tip("合同拆分细化删除失败", 'error');
                            }
                        });


                }
            },{buttonsFocus:1, closed:function(){
                    if (typeof closed == 'function') {
                        closed();
                    }
                }});
            top.$('.jbox-body .jbox-icon').css('top','55px');



        }

       function startDetailProcess(id) {
            $.post("${ctx}/cont/split/detail/check/process",{id:id},function(data){
                var code=data.result;
                if(code=='success'){
					$.post("${ctx}/cont/split/detail/proc/start?",{id:id},function(data){
                        var code=data.result;
                        if(code=='success'){
                            setTimeout(function () {
                                window.location="${ctx}/cont/split/list?contractId=${contractId}&readonly=${readonly}";
                            }, 500);

                        }else{
                            top.$.jBox.tip("流程启动失败","error",{persistent:true,opacity:0});
                        }
                    });


                }else{
                    top.$.jBox.tip(data.msg,'error',{persistent:true,opacity:0});
                }
            });



        }


        function save(){
            var action= $("#action").val();
            if(action==0){
                itemSave();
            }
            if(action==1){
                detailSave();
            }
        }



	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li><a href="${ctx}/cont/base/list">合同列表</a></li>
	<c:if test="${empty contract.id}">
		<li class="active"> <a href="${ctx}/cont/base/form?id=${contract.id}">合同添加</a></li>
	</c:if>

	<c:if test="${not empty contract.id}">
		<c:if test="${readonly}">
			<li> <a href="${ctx}/cont/base/form?id=${contract.id}&readonly=${readonly}">合同查看</a></li>
		</c:if>
		<c:if test="${not readonly}">
			<li > <a href="${ctx}/cont/base/form?id=${contract.id}&readonly=${readonly}">合同修改</a></li>
		</c:if>
		<li ><a href="${ctx}/cont/contItem/list?contractId=${contract.id}&readonly=${readonly}">付费约定</a></li>
		<li ><a href="${ctx}/cont/attach/list?contractId=${contract.id}&readonly=${readonly}">合同附件</a></li>
		<li><a href="${ctx}/cont/applyPay/list?contractId=${contract.id}&readonly=${readonly}">合同请款</a></li>
		<li><a href="${ctx}/income/income/contractIncome?contractId=${contract.id}&readonly=${readonly}">合同支付</a></li>
		<li class="active"><a href="${ctx}/cont/split/list?contractId=${contract.id}&readonly=${readonly}">合同拆解</a></li>
		</c:if>
</ul><br/>
	<sys:message content="${message}"/>
<div class="container-fluid">
	<div class="container-fluid">
		  <div class="row-fluid">
			  <div class="span1">
			  </div>
			  <div class="span10">
				  <h5>合同金额：${contract.value}</h5>
			  </div>
			  <div class="span1">
			  </div>
		  </div>
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<h5>合同拆分列表</h5>
				</div>
				<div class="span1">
				</div>
			</div>

		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span10">
	<table title="合同拆分列表" class="table table-striped table-bordered table-condensed">
			<thead>
			<tr>
				<th>类型</th>
				<th>金额</th>
				<th>创建时间</th>
				<th>状态</th>
				<th>备注</th>
				<th>操作</th>
			</tr>
			</thead>
			<tbody>
			<c:set var="sum" value="0"></c:set>
			<c:forEach items="${contSplitItems}" var="contSplitItem">
				<tr>
				    <td width="10%">${fns:getDictLabel(contSplitItem.type, 'contract_split_type', '无')}</td>
					<td width="10%"> ${contSplitItem.value}</td>
					<td width="20%"><fmt:formatDate value="${contSplitItem.createDate}" type="both"/></td>
					<td width="10%">${fns:getDictLabel(contSplit.status, 'contract_split_status', '无')}</td>
					<td width="40%">${contSplitItem.remark}</td>
					<td width="10%">
						<shiro:hasPermission name="cont:manager:edit" >
						<c:if test="${contSplit.status==0}">
						<%--<a href="#" onclick="itemEdit('${contSplitItem.id}','${contSplitItem.type}','${contSplitItem.value}','${contSplitItem.remark}')">编辑</a>--%>
							<a href="#" onclick="itemDelete('${contSplitItem.id}')">删除</a>
						</c:if>
						</shiro:hasPermission>

					</td>
				</tr>
				<c:set value="${sum + contSplitItem.value}" var="sum" />
			</c:forEach>
				<tr>
					<td width="100%" colspan="6">合计:${sum}</td>
				</tr>
			</tbody>
		   <input type="hidden" id="itemSum" value="${sum}" />
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
					<shiro:hasPermission name="cont:creator:create" >
					<c:if test="${contSplit.status==0}">
						<c:if test="${contract.value>sum}">
					<input id="btnAdd" class="btn btn-primary" type="button" value="合同拆分添加" onclick="itemAdd()"/>&nbsp;
					<input id="btnSubmit" class="btn btn-primary" type="button" value="发起流程" onclick="startProcess('${contSplit.id}')"/>&nbsp;
						</c:if>
					</c:if>
					</shiro:hasPermission>

				</div>
				<div class="span1">
				</div>
			</div>
		</div>
   </div>

    <div class="container-fluid">
		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span10">
				<h5>合同细化列表</h5>
			</div>
			<div class="span1">
			</div>
		</div>
    <div class="row-fluid">
	<div class="span1">
	</div>
	<div class="span10">
		<table title="合同拆分细化列表" class="table table-striped table-bordered table-condensed">
			<thead>
			<tr>
				<th>备注</th>
				<th>金额</th>
				<th>创建时间</th>
				<th>状态</th>
				<th>操作</th>
			</tr>
			</thead>
			<tbody>
			<c:set var="sum" value="0"></c:set>
			<c:forEach items="${contSplitDetails}" var="contSplitDetail">
				<tr>
					<td width="45%">${contSplitDetail.remark}</td>
					<td width="10%"> ${contSplitDetail.total}</td>
					<td width="15%"><fmt:formatDate value="${contSplitDetail.createDate}" type="both"/></td>
					<td width="10%">${fns:getDictLabel(contSplitDetail.status, 'contract_split_status', '无')}</td>

					<td width="20%">
						<c:if test="${contSplitDetail.status==0}">
							<a href="#" onclick="detailSet('${contSplitDetail.id}')">配置</a>
							<a href="#" onclick="detailDelete('${contSplitDetail.id}')">删除</a>
							<a href="#" onclick="startDetailProcess('${contSplitDetail.id}')">启动流程</a>
						</c:if>
						<c:if test="${contSplitDetail.status!=0}">
							<a href="#" onclick="detailSet('${contSplitDetail.id}')">详情</a>
						</c:if>
					</td>
				</tr>
			 <c:set value="${sum + contSplitDetail.total}" var="sum" />
			</c:forEach>
			<tr>
				<td width="100%" colspan="5">合计:${sum}</td>
			</tr>
			<input type="hidden" id="detailSum" value="${sum}" />
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
			<shiro:hasPermission name="cont:creator:create" >
			<c:if test="${sum<contract.value}">
				<c:if test="${contSplit.status==0}">
					<input id="btnAdd" class="btn btn-primary" type="button" value="合同细化添加" onclick="detailAdd()"/>&nbsp;
				</c:if>
			</c:if>
			</shiro:hasPermission>
		</div>
		<div class="span1">
		</div>
	</div>
</div>


	<form:form name="from" id="form" modelAttribute="contSplitItem" action="${ctx}/cont/split/item/save" method="post" class="form-horizontal">
	<input type="hidden" id="contractId" name="contractId" value="${contract.id}"/>
	<input type="hidden" id="readonly" name="readonly" value="${readonly}"/>
	<input type="hidden" id="id" name="id" value=""/>
	<input type="hidden" id="action" name="action" />
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
						&times;
					</button>
					<h4 class="modal-title" >
						<span id="title"></span>
					</h4>
				</div>
				<div class="modal-body">
					<div class="form-group">
						<div class="row-fluid" id="typeDiv">
							<div class="span1">
							</div>
							<div class="span10">
								<label >分配类型:</label>
								<form:select path="type" class="input-small ">
									<form:options items="${fns:getDictList('contract_split_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
								<input id="value" name="value" class="form-group input-small"  onkeyup="onlyFloatTowPoint(this)" required="true"/>

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
								<label >备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label>
								<form:textarea path="remark" htmlEscape="false" rows="3" maxlength="500" class="input-xlarge" />
							</div>
							<div class="span1">
							</div>
						</div>
					</div>


				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary"  onclick="save()">提交</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal -->
	</div>

	</form:form>
		<%--<form:form id="detailForm"  name="detailFormm" action="${ctx}/income/distOffice/save" method="post" class="form-horizontal">--%>

		<%--<div class="modal fade" id="detailModal" tabindex="-2" role="dialog" aria-labelledby="detailModalLabel" aria-hidden="true">--%>
			<%--<div class="modal-dialog">--%>
				<%--<div class="modal-content">--%>
					<%--<div class="modal-header">--%>
						<%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">--%>
							<%--&times;--%>
						<%--</button>--%>
						<%--<h4 class="modal-title" >--%>
							<%--合同拆分细化添加--%>
						<%--</h4>--%>
					<%--</div>--%>
					<%--<div class="modal-body">--%>
						<%--<div class="form-group">--%>
							<%--<div class="row-fluid">--%>
								<%--<div class="span1">--%>
								<%--</div>--%>
								<%--<div class="span10">--%>
									<%--<label >分配金额:</label>--%>
									<%--<input id="detailValue" name="detailValue" class="form-group input-small"  onkeyup="onlyFloatTowPoint(this)" required="true"/>--%>
								<%--</div>--%>
								<%--<div class="span1">--%>
								<%--</div>--%>
							<%--</div>--%>
						<%--</div>--%>
						<%--<div class="form-group">--%>
							<%--<div class="row-fluid">--%>
								<%--<div class="span1">--%>
								<%--</div>--%>
								<%--<div class="span10">--%>
									<%--<label >备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label>--%>
									<%--<textarea id="detailRemark" rows="3"  class="input-xlarge" />--%>
								<%--</div>--%>
								<%--<div class="span1">--%>
								<%--</div>--%>
							<%--</div>--%>
						<%--</div>--%>


					<%--</div>--%>
					<%--<div class="modal-footer">--%>
						<%--<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>--%>
						<%--<button type="button" class="btn btn-primary"  onclick="detailSave()">提交</button>--%>
					<%--</div>--%>
				<%--</div><!-- /.modal-content -->--%>
			<%--</div><!-- /.modal -->--%>
		<%--</div>--%>

		<%--</form:form>--%>


	<%--<form id="officeForm"  name="officeForm" action="${ctx}/income/distOffice/save" method="post" class="form-horizontal">--%>
		<%--<input id="itemId"  type="hidden" />--%>
		<%--<input id="itemValue"  type="hidden" />--%>
		<%--<div class="modal fade" id="officeModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">--%>
			<%--<div class="modal-dialog">--%>
				<%--<div class="modal-content">--%>
					<%--<div class="modal-header">--%>
						<%--<button type="button" class="close" data-dismiss="modal" aria-hidden="true">--%>
							<%--&times;--%>
						<%--</button>--%>
						<%--<h4 class="modal-title" >--%>
							<%--<span id="action"></span>分配部门--%>
						<%--</h4>--%>
					<%--</div>--%>
					<%--<div class="modal-body">--%>
						<%--<div class="container-fluid">--%>
							<%--<div class="form-group">--%>
								<%--<div class="row-fluid">--%>
									<%--<div class="span1">--%>
									<%--</div>--%>
									<%--<div class="span10">--%>
										<%--<label >分配部门:</label>--%>
										<%--<sys:treeselect id="office" name="office.id" value="" labelName="office.name" labelValue="" isAll="true"--%>
														<%--title="部门" url="/sys/office/treeData?type=2" cssClass="input-large" allowClear="true" notAllowSelectParent="true" />--%>

									<%--</div>--%>
									<%--<div class="span2">--%>
									<%--</div>--%>
								<%--</div>--%>
							<%--</div>--%>
							<%--<div class="form-group">--%>
								<%--<div class="row-fluid">--%>
									<%--<div class="span1">--%>
									<%--</div>--%>
									<%--<div class="span10">--%>
										<%--<label >分配金额:</label>--%>
										<%--<input id="officeValue" name="officeValue" class="input-small"  onkeyup="onlyFloatTowPoint(this)" required="true"/>--%>

									<%--</div>--%>
									<%--<div class="span2">--%>
									<%--</div>--%>
								<%--</div>--%>
							<%--</div>--%>
							<%--<div class="form-group">--%>
								<%--<div class="row-fluid">--%>
									<%--<div class="span1">--%>
									<%--</div>--%>
									<%--<div class="span10">--%>
										<%--<label >备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label>--%>
										<%--<textarea id="officeRemark" name="officeRemark" rows="2" class="input-xlarge"   ></textarea>--%>

									<%--</div>--%>
									<%--<div class="span1">--%>
									<%--</div>--%>
								<%--</div>--%>
							<%--</div>--%>
						<%--</div>--%>
					<%--</div>--%>
				<%--</div>--%>
				<%--<div class="modal-footer">--%>
					<%--<button type="button" id="111" class="btn btn-default" data-dismiss="modal">关闭</button>--%>
					<%--<button type="button" id="222" class="btn btn-primary" >提交</button>--%>
				<%--</div>--%>
			<%--</div><!-- /.modal-content -->--%>
		<%--</div><!-- /.modal -->--%>



<%--</form>--%>






</body>
</html>