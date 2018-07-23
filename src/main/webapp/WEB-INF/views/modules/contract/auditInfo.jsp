<%@ page contentType="text/html;charset=UTF-8" %>
<script type="text/javascript">

    function downloadFile(id) {
        window.location.href="${ctx}/cont/base/attachDownload?id="+id;

    }
    function preview(id) {
        window.open("${ctx}/cont/base/attachPreview?id="+id);

    }
    function save() {
        var prjName=window.document.getElementById("project.name").value;

        if(prjName==null||prjName==""){
            $.jBox.tip("请选择项目", 'error');
            return;
        }

        if($("#areaName").val()==null||$("#areaName").val()==""){
            $.jBox.tip("请选区域", 'error');
            return;
        }

       /* if($("#officeName").val()==null||$("#officeName").val()==""){
            $.jBox.tip("请选择签约部门", 'error');
            return;
        }*/
        if($("#inputForm").valid()){
            $("#inputForm").submit();
        }

    }

    function upload() {

        if($("#uploadFileForm").valid()){
            $("#uploadFileForm").submit();
        }

    }

    function openUpload(){

        $('#fileRemark').val("");
        $('#file').val("");
        $('#myModal').modal({backdrop: 'static', keyboard: false});
    }
    function selectPrj() {

        top.$.jBox.open("iframe:${ctx}/project/project/selectList", "项目选择", 800, 500, { buttons: { '关闭': true},
            loaded : function(h) {   //隐藏滚动条
                $(".jbox-content", top.document).css( "overflow-y", "hidden");
            }
        });
    }

</script>



<form:form id="inputForm" modelAttribute="contract" action="${ctx}/cont/proc/audit/save" method="post" class="form-horizontal">
	<form:hidden path="id"/>
	<input type="hidden" id="taskIdContract" name="taskIdContract" value="${taskId}"/>


	<div class="container-fluid">
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label >合同编号:</label>
					<input id="oldCode" name="oldCode" type="hidden" value="${contract.code}">

					<form:input path="code"   htmlEscape="false" maxlength="50" class=" form-control input-small " readonly="${readonly}"/>

				</div>
				<div class="span1">
				</div>
			</div>
		</div>
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>

				<div class="span10">
					<label >合同名称:</label>
					<form:input path="name" htmlEscape="false" maxlength="50" class="input-xxlarge required" readonly="${readonly}"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
				<div class="span1">
				</div>
			</div>
		</div>
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span3">
					<label>所&nbsp;&nbsp;属&nbsp;&nbsp;省:</label>
					<form:select path="province.id" class="input-medium" onchange="findCity(this.options[this.options.selectedIndex].value);" disabled="${readonly}" >
						<form:options items="${provinceList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
					</form:select>
				</div>
				<div class="span3">
					<label>所属市/区:</label>
					<form:select path="city.id" class="input-medium"  disabled="${readonly}">
						<form:options items="${cityList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
					</form:select>
				</div>
				<div class="span5">
				</div>

			</div>
		</div>
			<%--<div class="control-group">
                <div class="row-fluid">
                    <div class="span1">
                    </div>
                    <div class="span10">
                        <label >签约部门:</label>
                        <c:if test="${readonly}">
                        <sys:treeselect id="office" name="office.id" value="${contract.office.id}" labelName="office.name" labelValue="${contract.office.name}"
                                        title="部门" url="/sys/office/treeData?type=2" cssClass="input-small " allowClear="true" notAllowSelectParent="true" disabled="disabled"/>
                        </c:if>
                        <c:if test="${not readonly}">
                            <sys:treeselect id="office" name="office.id" value="${contract.office.id}" labelName="office.name" labelValue="${contract.office.name}"
                                            title="部门" url="/sys/office/treeData?type=2" cssClass="input-small " allowClear="true" notAllowSelectParent="true"/>
                        </c:if>
                        <span class="help-inline"><font color="red">*</font> </span>
                    </div>
                    <div class="span1">
                    </div>

                </div>
            </div>--%>
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label >项目名称:</label>
					<div class="input-append" >
							<%--<form:input path="project.id" htmlEscape="false" maxlength="50" />
                            <form:input path="projectName" htmlEscape="false" maxlength="50" class="input-xxlarge required"/>--%>
						<input id="project.id" name="project.id" readonly="readonly" type="hidden" value="${contract.project.id}" class="input-xlarge"/>
						<input id="project.name" name="project.name" readonly="readonly" type="text" value="${contract.project.name}"  class="input-xlarge"/>
						<c:if test="${readonly}">

							<a href="javascript:" class="btn"  disabled="true">&nbsp;<i class="icon-search" disabled="disabled"></i>&nbsp;</a>&nbsp;&nbsp;

						</c:if>
						<c:if test="${not readonly}">
							<a href="javascript:" class="btn" onclick="selectPrj();"  >&nbsp;<i class="icon-search"></i>&nbsp;</a>&nbsp;&nbsp;
						</c:if>
					</div>
					<span class="help-inline"><font color="red">*</font> </span>

				</div>
					<%--<div class="span5">
                        <label >项目经理:</label>
                        <form:input path="manager" htmlEscape="false" class="form-control input-small" readonly="true"/>
                    </div>--%>
				<div class="span1">
				</div>
			</div>
		</div>


		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span2">
					<label >合同类型:</label>
					<form:select path="type" class="form-control input-small" disabled="${readonly}">
						<form:options items="${fns:getDictList('contract_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>

				</div>
				<div class="span2">
					<label >合同种类:</label>
					<form:select path="class"  class="form-control input-small"  disabled="${readonly}">
						<form:options items="${fns:getDictList('contract_class')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				<div class="span1">
				</div>

			</div>
		</div>

		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label >甲方名称:</label>
					<form:input path="firstParty" htmlEscape="false" maxlength="50" class="input-xxlarge" readonly="${readonly}"/>
				</div>

				<div class="span1">
				</div>

			</div>
		</div>
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label >方案费用:</label>
					<form:input path="programmeCost" htmlEscape="false" maxlength="50" class="input-xxlarge" readonly="${readonly}"/>

				</div>

				<div class="span1">
				</div>

			</div>
		</div>
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span3">
					<label>&nbsp;&nbsp;联&nbsp;系&nbsp;人:</label>
					<form:input path="contact" htmlEscape="false" maxlength="32" class="form-control input-small" readonly="${readonly}"/>
				</div>
				<div class="span3">
					<label >联系电话:</label>
					<form:input path="contactPhone" htmlEscape="false" maxlength="32" class="input-small" readonly="${readonly}"/>
				</div>
				<div class="span4">
					<label >合同金额:</label>
					<form:input path="value"  onkeyup="onlyNum(this)" htmlEscape="false" class="form-control input-small" readonly="${readonly}"/>&nbsp;元
				</div>
				<div class="span1">
				</div>

			</div>
		</div>

		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span3">
					<label >签约时间:</label>
					<c:if test="${readonly}">
						<input name="signedTime" type="text" readonly="readonly" maxlength="20" class="iform-control input-small Wdate"
							   value="<fmt:formatDate value="${contract.signedTime}" pattern="yyyy-MM-dd"/>"
							   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" disabled="disabled"/>
					</c:if>
					<c:if test="${not readonly}">
						<input name="signedTime" type="text" readonly="readonly" maxlength="20" class="iform-control input-small Wdate"
							   value="<fmt:formatDate value="${contract.signedTime}" pattern="yyyy-MM-dd"/>"
							   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
					</c:if>
				</div>
				<div class="span3">
					<label >盖章时间:</label>
					<c:if test="${readonly}">
						<input name="sealTime" type="text" readonly="readonly" maxlength="20" class="input-small Wdate "
							   value="<fmt:formatDate value="${contract.sealTime}" pattern="yyyy-MM-dd"/>"
							   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" disabled="disabled"/>
					</c:if>
					<c:if test="${not readonly}">
						<input name="sealTime" type="text" readonly="readonly" maxlength="20" class="input-small Wdate "
							   value="<fmt:formatDate value="${contract.sealTime}" pattern="yyyy-MM-dd"/>"
							   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
					</c:if>
				</div>

				<div class="span4">
					<label >返回时间:</label>
					<c:if test="${readonly}">
						<input name="returnTime" type="text" readonly="readonly" maxlength="20" class="input-small Wdate "
							   value="<fmt:formatDate value="${contract.returnTime}" pattern="yyyy-MM-dd"/>"
							   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" disabled="disabled"/>
					</c:if>
					<c:if test="${not readonly}">
						<input name="returnTime" type="text" readonly="readonly" maxlength="20" class="input-small Wdate "
							   value="<fmt:formatDate value="${contract.returnTime}" pattern="yyyy-MM-dd"/>"
							   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
					</c:if>
				</div>

			</div>
		</div>
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>

				<div class="span3">
					<label >面&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;积:</label>
					<form:input path="areaValue" htmlEscape="false" maxlength="200" class="input-small" readonly="${readonly}"/>
				</div>
				<div class="span3">
					<label >投&nbsp;&nbsp;资&nbsp;&nbsp;额:</label>
					<form:input path="investment" htmlEscape="false" maxlength="200" class="input-small" readonly="${readonly}"  onkeyup="onlyNum(this)"/>&nbsp;万元
				</div>

				<div class="span4">
					<label >图纸数量:</label>
					<form:input path="blueprintNum" htmlEscape="false" maxlength="200" class=" input-lg" readonly="${readonly}"/>

				</div>
				<div class="span1">
				</div>

			</div>
		</div>

		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label >子&nbsp;&nbsp;项&nbsp;&nbsp;目:</label>
					<form:input path="subItem" htmlEscape="false" maxlength="400" class="input-xxlarge"  readonly="${readonly}"/>
				</div>

				<div class="span1">
				</div>
			</div>
		</div>

		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label >工期要求:</label>
					<form:input path="timeLimit" htmlEscape="false" maxlength="400" class="input-xxlarge" readonly="${readonly}"/>

				</div>
				<div class="span1">
				</div>
			</div>
		</div>

		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label >备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label>
					<form:textarea path="remark" htmlEscape="false" rows="3" maxlength="500" class="input-xxlarge" readonly="${readonly}"/>
				</div>
				<div class="span1">
				</div>
			</div>
		</div>
		<c:if test="${contract.specificItem=='1'}">
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label >特殊条款:</label>
					<form:select path="specificItem"  class="form-control input-small"  disabled="true">
						<form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				<div class="span1">
				</div>
			</div>
		</div>
		</c:if>
	<div class="control-group">
		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span10">
				<table title="请款附件列表" class="table table-striped table-bordered table-condensed">
					<thead>
					<tr>
						<th>上传时间</th>
						<th>操作人</th>
						<th>备注</th>
						<th>类型</th>
						<th>操作</th>
					</tr>
					</thead>
					<tbody>
					<c:forEach items="${contAttachs}" var="contAttach">
						<tr>
							<td><fmt:formatDate value="${contAttach.createDate}" type="both"/></td>
							<td> ${contAttach.createBy.name}</td>
							<td> ${contAttach.remark}</td>
							<td> ${fns:getDictLabel(contAttach.type, 'contract_attach_type', '无')}</td>
							<td>
								<a href="#" onclick="downloadFile('${contAttach.id}')">下载</a>
								<a href="#" onclick="preview('${contAttach.id}')">预览</a>

									<c:if test="${fileClass=='1'&&contAttach.type=='1'}">
									<a href="${ctx}/cont/proc/audit/delete?id=${contAttach.id}&taskId=${taskId}&fileClass=${fileClass}" onclick="return confirmx('确认要删除附件吗？', this.href)">删除</a>
									</c:if>
									<c:if test="${fileClass=='2'&&contAttach.type=='2'}">
										<a href="${ctx}/cont/proc/audit/delete?id=${contAttach.id}&taskId=${taskId}&fileClass=${fileClass}" onclick="return confirmx('确认要删除该附件吗？', this.href)">删除</a>
									</c:if>
							</td>
						</tr>
					</c:forEach>
					</tbody>
				</table>
			</div>
			<div class="span1">
			</div>
		</div>
	</div>
		<c:if test="${not empty fileClass}">
	<div class="form-actions">
		<c:if test="${fileClass=='1'}">
			<input class="btn btn-primary" type="button" value="合同保存" onclick="save();"/>&nbsp;
			<input class="btn btn-primary" type="button" value="合同附件上传" onclick="openUpload();"/>&nbsp;
		</c:if>
		<c:if test="${fileClass=='2'}">
			<input class="btn btn-primary" type="button" value="法律附件上传" onclick="openUpload();"/>&nbsp;
		</c:if>
	</div>
	</c:if>
	</div>
</form:form>

<form id="uploadFileForm"  action="${ctx}/cont/proc/audit/upload" method="post" enctype="multipart/form-data" class="form-horizontal">
	<input type="hidden" id="contractId" name="contractId" value="${contract.id}"/>
	<input type="hidden" id="taskIdUpload" name="taskIdUpload" value="${taskId}"/>
	<input type="hidden" id="fileClassUpload" name="fileClassUpload" value="${fileClass}"/>
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal" aria-hidden="true">
						&times;
					</button>
					<h4 class="modal-title" >
						附件
					</h4>
				</div>
				<div class="modal-body">
					<div class="row-fluid">
						<div class="span1">
						</div>
						<div class="span10">
							<label >备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label>
							<textarea id="fileRemark"  name="fileRemark" rows="2" maxlength="200" class="input-xlarge required"></textarea>
						</div>
						<div class="span1">
						</div>
					</div>
					<div class="row-fluid">
						<div class="span12">
						</div>
					</div>
					<div class="row-fluid">
						<div class="span1">
						</div>
						<div class="span10">
							<label >附件文件：</label>
							<input type="file" id="file" name="file" class="required"/>
							<span class="help-inline">支持文件格式：pdf、doc、docx</span>
						</div>
						<div class="span1">
						</div>
					</div>

				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" id="addAttachFile" onclick="upload();">提交</button>
				</div>
			</div><!-- /.modal-content -->
		</div><!-- /.modal -->
	</div>
</form>
