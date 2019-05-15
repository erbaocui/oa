<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同管理</title>
	<meta name="decorator" content="default"/>

	<script type="text/javascript">


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
                                window.location="${ctx}/cont/split/proc/administrator?taskId=${taskId}";
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
                                //$("#officeModal").modal('hide');
                                $.jBox.tip("合同拆分细化删除成功");
                                setTimeout(function () {
                                    window.location="${ctx}/cont/split/proc/administrator?taskId=${taskId}";
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

        function commit(state){
            $("#state").val(state);
            $("#inputForm").submit();
        }




	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li class="active"><a href="#">合同拆解-审核</a></li>
</ul><br/>
	<sys:message content="${message}"/>
<div class="form-actions">
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span3">
				<label >项目名称：</label>
				${contract.project.name}
			</div>
			<div class="span3">
				<label >合同名称：</label>
				${contract.name}
			</div>
			<div class="span3">
				<label >合同金额：</label>
				${contract.value}

			</div>
			<div class="span2">
			</div>
		</div>
	</div>
</div>

<div class="container-fluid">

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
				<th>备注</th>
				<th>操作</th>
			</tr>
			</thead>
			<tbody>
			<c:set var="sum" value="0"></c:set>
			<c:forEach items="${contSplitItems}" var="contSplitItem">
				<tr>
				    <td width="20%">${fns:getDictLabel(contSplitItem.type, 'contract_split_type', '无')}</td>
					<td width="10%"> ${contSplitItem.value}</td>
					<td width="20%"><fmt:formatDate value="${contSplitItem.createDate}" type="both"/></td>
					<td width="40%">${contSplitItem.remark}</td>
					<td width="10%">
		               <a href="#" onclick="itemDelete('${contSplitItem.id}')">删除</a>
					</td>
				</tr>
				<c:set value="${sum + contSplitItem.value}" var="sum" />
			  </c:forEach>
				<tr>
					<td width="100%" colspan="5">合计:${sum}</td>
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
					<c:if test="${contract.value>sum}">
						<input   class="btn btn-primary" type="button" value="合同拆分添加" onclick="itemAdd()"/>&nbsp;
					</c:if>

				</div>
				<div class="span1">
				</div>
			</div>
		</div>
   </div>
	<form:form id="inputForm" modelAttribute="baseReview" action="${ctx}/cont/split/proc/administrator/submit" method="post" class="form-horizontal">
	<div class="form-actions">
		<div class="container-fluid">

			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label>流程备注:</label>
					<form:textarea path="comment" htmlEscape="false" rows="3" maxlength="500" class="input-xxlarge" />

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
				<div class="span4">
					<input id="btnAgree" class="btn btn-primary" type="button"  onclick="commit()" value="提交" />
				</div>

				<div class="span5">
				</div>
			</div>
		</div>
	</div>

<input type="hidden" id="state" name="state" />
<input type="hidden" id="taskId" name="taskId" value="${taskId}"/>
	</form:form>


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
					<button type="button" class="btn btn-primary"  onclick="itemSave()">提交</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal -->
	</div>

	</form:form>
	<br>
	<%@ include file="/WEB-INF/views/modules/act/comment.jsp" %>


</body>
</html>