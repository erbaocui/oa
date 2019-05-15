<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同请款管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">
		$(document).ready(function() {
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
		});
        function itemAdd(){
            $('#officeModal').hide();
            $('#type').val(0).trigger('change');
            $('#typeRemark').val(null);
            $('#typeValue').val(null);
			$('#itemModal').modal({backdrop: 'static', keyboard: true});
        }
        function itemSave(){
            if($("#itemForm").valid()){
                var flag=true;

                $.ajax({
                    type : "post",
                    url : "${ctx}/cont/split/detail/item/check",
                    async:false,
                    data : {
                        type: $('#type').val(),
                        detailId:$('#detailId').val()
                    },
                    success : function(data){
                        if(data.result=='error'){
                            $.jBox.tip(data.msg, 'error');
                            flag=false;
                        }
                    },
                    error : function(){

                        return ;
                    }
                });
                if(!flag){
                    return;
                }

				var  itemSum=Number($('#itemSum').val());
                var  typeValue=Number($('#typeValue').val());
                var  detailTotal=Number($('#detailTotal').val());
                if(itemSum+ typeValue>detailTotal){
                    $.jBox.tip('金额大于此次分配金额', 'error');
                    return;
                }

                $.post("${ctx}/cont/split/detail/item/save",
                    {
                        detailId:$('#detailId').val(),
                        type:$("#type").val(),
                        value:$("#typeValue").val(),
                        remark:$("#typeRemark").val()
                    },
                    function(result){
                        if(result=="success"){
                            $("#officeModal").modal('hide');
                            $.jBox.tip("业务类型添加成功");
                            setTimeout(function () {
                                window.location="${ctx}/cont/split/detail/list?id=${contSplitDetail.id}&contractId=${contractId}&readonly=${readonly}";
                            }, 500);
                        }else{
                            $.jBox.tip("业务类型添加失败", 'error');
                        }
                    });



            }

        }

        function itemDelete(id){
            top.$.jBox.confirm("确认要删除该业务类型吗？",'系统提示',function(v,h,f){
                if(v=='ok'){
                    resetTip(); //loading();
                    $.post("${ctx}/cont/split/detail/item/delete",
                        {
                            id:id
                        },
                        function(data){
                            if(data.result=="success"){
                                $("#officeModal").modal('hide');
                                $.jBox.tip("业务类型删除成功");
                                setTimeout(function () {
                                    window.location="${ctx}/cont/split/detail/list?id=${contSplitDetail.id}&contractId=${contractId}&readonly=${readonly}";
                                }, 500);
                            }else{
                                $.jBox.tip("业务类型删除失败", 'error');
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

        function officeAdd(itemId,itemValue){
            $('#itemModal').hide();
            $('#itemId').val(itemId);
            $('#itemValue').val(itemValue);
            $('#action').html('添加');
            $('#officeValue').val(null);
            $('#officeRemark').val(null);
            $('#officeId').val(null);
            $('#officeName').val(null);
            $('#officeModal').modal({backdrop: 'static', keyboard: true});
        }
        function officeSave(){
            if($("#officeForm").valid()){
                if($('#officeName').val()==null||$('#officeName').val()==""){
                    $.jBox.tip("请选择部门", 'error');
                    return;
				}
				//check

                var flag=true;
                $.ajax({
                    type : "post",
                    url : "${ctx}/cont/split/detail/office/check",
                    async:false,
                    data : {
                        itemId: $('#itemId').val(),
						officeId:$('#officeId').val()
                    },
                    success : function(data){
                        if(data.result=='error'){
                            $.jBox.tip(data.msg, 'error');
                            flag=false;
                        }
                    },
                    error : function(){
                        COM_TOOLS.alert(TEDU_MESSAGE.get('platform.common.msg.-1102'));
                        return ;
                    }
                });
                if(!flag){
                    return;
				}


                var id=$('#itemId').val();
                var  itemSum=Number($('#itemValue').val());
				var  officeValue=Number($('#officeValue').val());
				var  officeSum=Number($('#'+id+'sum').val());

                if(officeSum+officeValue>itemSum){
                    $.jBox.tip('分配金额大于单项值', 'error');
                    return;
				}

                $.post("${ctx}/cont/split/detail/office/save",
					{
					    itemId:$('#itemId').val(),
						officeId:$("#officeId").val(),
						value:$("#officeValue").val(),
						remark:$("#officeRemark").val()
					},
                    function(result){
                        if(result=="success"){
                            $("#officeModal").modal('hide');
                            $.jBox.tip("分公司添加成功");
                            setTimeout(function () {
                                window.location="${ctx}/cont/split/detail/list?id=${contSplitDetail.id}&contractId=${contractId}&readonly=${readonly}";
                            }, 500);
                        }else{
                            $.jBox.tip("分公司添加失败", 'error');
                        }
                    });



        }

	}

        function officeDelete(id){
            top.$.jBox.confirm("确认要删除该分公司吗？",'系统提示',function(v,h,f){
                if(v=='ok'){
					  resetTip(); //loading();
                      $.post("${ctx}/cont/split/detail/office/delete",
                        {
                            id:id
                        },
                        function(data){
                            if(data.result=="success"){
                                $("#officeModal").modal('hide');
                                $.jBox.tip("分公司删除成功");
                                setTimeout(function () {
                                    window.location="${ctx}/cont/split/detail/list?id=${contSplitDetail.id}&contractId=${contractId}&readonly=${readonly}";
                                }, 500);
                            }else{
                                $.jBox.tip("分公司添加失败", 'error');
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
        function back(){
            window.location="${ctx}/cont/split/list?contractId=${contractId}&readonly=${readonly}";
		}

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li ><a href="${ctx}/cont/split/list?contractId=${contract.id}&readonly=${readonly}">合同拆解</a></li>
		<li class="active"><a href="#">合同拆解细化设置</a></li>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="contSplitDetail" action="${ctx}/cont/applyPay/save" method="post" class="form-horizontal" enctype="multipart/form-data">
		<input type="hidden" id="contractId" name="contractId" value="${contractId}" />
		<input type="hidden" id="readonly" name="readonly" value="${readonly}" />
		<input type="hidden" id="detailTotal" name="detailTotal" value="${contSplitDetail.total}" />
		<input type="hidden" id="detailId" name="detailId" value="${contSplitDetail.id}" />
		<sys:message content="${message}"/>

		<div class="container-fluid">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span2">
					<h5>流程状态：${fns:getDictLabel(contSplitDetail.status, 'contract_split_detail_status', '无')}</h5>
				</div>
				<div class="span2">
					<h5>本次拆解金额：${contSplitDetail.total}</h5>
				</div>
				<div class="span2">
					<h5>合同金额：${contract.value}</h5>
				</div>
				<div class="span2">
					<h5>备注：${contSplitDetail.remark}</h5>
				</div>
				<div class="span1">
				</div>
			</div>

			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<table title="合同拆分细化项目" class="table table-striped table-bordered table-condensed">
						<thead>
						<tr>
							<th>类型</th>
							<th>金额</th>
							<th>备注</th>
							<th>操作</th>
						</tr>
						</thead>
						<tbody>
						<c:set var="itemSum" value="0"></c:set>
						<c:forEach items="${contSplitDetailItems}" var="contSplitDetailItem">
							<tr>
								<td width="30%" >${fns:getDictLabel(contSplitDetailItem.type, 'contract_split_detail_type', '无')}</td>
								<td width="10%"> ${contSplitDetailItem.value}</td>
								<td width="40%">${contSplitDetailItem.remark}</td>
								<td width="20%">
									<c:if test="${contSplitDetail.status==0}">
										<a href="#" onclick="itemDelete('${contSplitDetailItem.id}')">删除</a>
									</c:if>
								</td>
							</tr>
							<c:set value="${itemSum + contSplitDetailItem.value}" var="itemSum" />
						</c:forEach>
						    <input type="hidden" id="itemSum" value="${itemSum}" />
						    <tr>
								<td width="100%" colspan="4">合计:${itemSum}</td>
							</tr>
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
						<c:if test="${contSplitDetail.status==0}">
						  <c:if test="${contSplitDetail.total>itemSum}">
							<input class="btn btn-primary" type="button" value="添加业务种类" onclick="itemAdd()"/>&nbsp;

						 </c:if>
						</c:if>
						<input class="btn btn-primary" type="button" value="返回" onclick="back()" />&nbsp;

					</div>
					<div class="span1">
					</div>
				</div>
			</div>
		<c:forEach items="${contSplitDetailItems}" var="contSplitDetailItem" varStatus="idx">
			<c:if test="${idx.index!=0}">
			<h5 class="page-header"></h5>
			</c:if>
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span3">
					<h5>业务种类：${fns:getDictLabel(contSplitDetailItem.type, 'contract_split_detail_type', '无')}</h5>
				</div>
				<div class="span3">
					<h5>业务总金额：${contSplitDetailItem.value}</h5>

				</div>
				<div class="span3">
					<h5>备注：${contSplitDetailItem.remark}</h5>
				</div>
				<div class="span1">
				</div>
			</div>
		     <div class="row-fluid">
			  <div class="span1">
			</div>
			<div class="span10">

				<table title="合同拆分细化项目" class="table table-striped table-bordered table-condensed">
					<thead>
					<tr>
						<th>部门</th>
						<th>金额</th>
						<th>备注</th>
						<th>操作</th>
					</tr>
					</thead>
					<tbody>
					<c:set var="sum" value="0"></c:set>
					<c:forEach items="${contSplitDetailItem.officeList}" var="office">
						<tr>
							<td WIDTH="20%">${office.office.name}</td>
							<td WIDTH="10%"> ${office.value}</td>
							<td WIDTH="50%"> ${office.remark}</td>
							<td WIDTH="20%">
								<c:if test="${contSplitDetail.status==0}">
									<%--<!----%>
									<%--<a href="${ctx}/cont/split/detail/office/delete?id=${office.id}&detailId=${contSplitDetail.id}&contractId=${contractId}&readonly=${readonly}" onclick="return confirmx('确认要删除该合同拆分项吗？', this.href)">删除</a>--%>

								   <a href="#" onclick="officeDelete('${office.id}')">删除</a>
								</c:if>
							</td>
						</tr>
						<c:set value="${sum + office.value}" var="sum" />
					 </c:forEach>
					<input type="hidden" id="${contSplitDetailItem.id}sum" value="${sum}" />
					<tr>
						<td width="100%" colspan="4">合计:${sum}</td>
					</tr>
					</tbody>
				</table>
			</div>
			<div class="span1">
			</div>

		</div>
			<c:if test="${contSplitDetailItem.value>sum}">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<input  class="btn btn-primary" type="button" value="添加部门" onclick="officeAdd('${contSplitDetailItem.id}','${contSplitDetailItem.value}')"/>
				</div>
				<div class="span1">
				</div>
			</div>
		   </c:if>
		</c:forEach>


		</div>

	</form:form>

	<form id="officeForm"  name="officeForm" action="${ctx}/income/distOffice/save" method="post" class="form-horizontal">
		<input id="itemId"  type="hidden" />
		<input id="itemValue"  type="hidden" />
		<div class="modal fade" id="officeModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
							&times;
						</button>
						<h4 class="modal-title" >
							<span id="action"></span>分配部门
						</h4>
					</div>
					<div class="modal-body">
						<div class="container-fluid">
							<div class="form-group">
								<div class="row-fluid">
									<div class="span1">
									</div>
									<div class="span10">
										<label >分配部门:</label>
										<sys:treeselect id="office" name="office.id" value="" labelName="office.name" labelValue="" isAll="true"
														title="部门" url="/sys/office/treeData?type=2" cssClass="input-large" allowClear="true" notAllowSelectParent="true" />

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
										<input id="officeValue" name="officeValue" class="input-small"  onkeyup="onlyFloatTowPoint(this)" required="true"/>

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
										<label >备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label>
										<textarea id="officeRemark" name="officeRemark" rows="2" class="input-xlarge"   ></textarea>

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
					<button type="button" class="btn btn-primary" onclick="officeSave()">提交</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal -->
		</div>


	</form>
	<form:form id="itemForm" name="itemForm" modelAttribute="contSplitDetailItem" action="${ctx}/cont/split/item/save" method="post" class="form-horizontal">
	<%--<form id="itemForm"  name="officeForm" action="${ctx}/income/distOffice/save" method="post" class="form-horizontal">--%>
		<input id="detailId"  type="hidden" />
		<div class="modal fade" id="itemModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
							&times;
						</button>
						<h4 class="modal-title" >
							添加业务类型
						</h4>
					</div>
					<div class="modal-body">
						<div class="container-fluid">
							<div class="form-group">
								<div class="row-fluid">
									<div class="span1">
									</div>
									<div class="span10">
										<label >业务类型:</label>
										<form:select path="type" class="input-large ">
											<form:options items="${fns:getDictList('contract_split_detail_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
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
										<label >金&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;额:</label>
										<input id="typeValue" name="typeValue" class="input-small"  onkeyup="onlyFloatTowPoint(this)" required="true"/>

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
										<label >备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label>
										<textarea id="typeRemark" name="typeRemark" rows="2" class="input-xlarge"   ></textarea>

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
					<button type="button" class="btn btn-primary" onclick="itemSave()">提交</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal -->
		</div>


	</form:form>


</body>
</html>