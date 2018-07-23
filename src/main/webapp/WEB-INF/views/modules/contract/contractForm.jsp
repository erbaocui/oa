<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">


		$(document).ready(function() {
			$("#code").focus();

			$("#inputForm").validate({
                onfocusout: function(element){
                    $(element).valid();
                },
                rules: {
                    code: {remote: "${ctx}/cont/base/checkCode?oldCode=" + $("#oldCode").val()}
                },
                messages: {
                    code: {remote: "编码已存在"}

                },
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

		function save() {
            var prjName=window.document.getElementById("project.name").value;

            if(prjName==null||prjName==""){
                $.jBox.tip("请选择项目", 'error');
                return;
            }
/*
            if($("#areaName").val()==null||$("#areaName").val()==""){
                $.jBox.tip("请选区域", 'error');
                return;
            }

            if($("#officeName").val()==null||$("#officeName").val()==""){
                $.jBox.tip("请选择签约部门", 'error');
                return;
            }*/
            if($("#inputForm").valid()){
                $("#inputForm").submit();
			}

        }
        function selectPrj() {

            top.$.jBox.open("iframe:${ctx}/project/project/selectList", "项目选择", 1000, 500, { buttons: { '关闭': true},
				loaded : function(h) {   //隐藏滚动条
                $(".jbox-content", top.document).css( "overflow-y", "hidden");
                }
            });
        }


        function findCity(v) {
            $.ajax({
                type : "post",
                async : false,
                url : "${ctx}/cont/base/city",
                data : {
                    'provinceId' : v
                },
                dataType : "json",
                success : function(msg) {
                    var city=document.getElementById('city.id');
                    city.value="";
                    city.options.length=0;
                    if (msg.result=="success") {
                        for (var i = 0; i < msg.citys.length; i++) {
                            if(i==0) {
                                city.options.add(new Option(msg.citys[i].name, msg.citys[i].id, true, true));
                            }else{
                                city.options.add(new Option(msg.citys[i].name, msg.citys[i].id, false, true));
							}
                        }
                        city.options[0].selected = "selected";
                        if(city.addEventListener){
						   city.addEventListener('change', function(){
                                console.log('aaaaaa');
                            });
                        }
                        else{
                            city.attachEvent('onchange', function(){
                                console.log('aaaaaa');
                            });
                        }
                        // 手动触发事件
                        if (city.fireEvent){
                            city.fireEvent('onchange');
                        }
                        else{
                            ev = document.createEvent("HTMLEvents");
                            ev.initEvent("change", false, true);
                            city.dispatchEvent(ev);
                        }

                    }
                },
                error : function(json) {
                    $.jBox.alert("网络异常！");
                }
            });
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
				<li class="active"> <a href="${ctx}/cont/base/form?id=${contract.id}&readonly=${readonly}">合同查看</a></li>
			</c:if>
			<c:if test="${not readonly}">
				<li class="active"> <a href="${ctx}/cont/base/form?id=${contract.id}&readonly=${readonly}">合同修改</a></li>
			</c:if>
			<li><a href="${ctx}/cont/applyPay/list?contractId=${contract.id}&readonly=${readonly}">合同请款</a></li>
			<li><a href="${ctx}/cont/attach/list?contractId=${contract.id}&readonly=${readonly}">合同附件</a></li>
			<li><a href="${ctx}/income/income/contractIncome?contractId=${contract.id}&readonly=${readonly}">合同支付</a></li>
			<li ><a href="${ctx}/cont/contItem/list?contractId=${contract.id}&readonly=${readonly}">付费约定</a></li>
		</c:if>
	</ul><br/>
	<form:form id="inputForm" modelAttribute="contract" action="${ctx}/cont/base/save" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<sys:message content="${message}"/>

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
					<label>所属地区:</label>
					<c:if test="${readonly}">
					<sys:treeselect id="area" name="area.id" value="${contract.area.id}" labelName="area.name" labelValue="${contract.area.name}"
									title="区域" url="/sys/area/treeData" cssClass="input-small" allowClear="true" notAllowSelectParent="true" disabled="disabled"/>
					<span class="help-inline"><font color="red">*</font> </span>
					</c:if>
					<c:if test="${not readonly}">
						<sys:treeselect id="area" name="area.id" value="${contract.area.id}" labelName="area.name" labelValue="${contract.area.name}"
										title="区域" url="/sys/area/treeData" cssClass="input-small" allowClear="true" notAllowSelectParent="true" />
						<span class="help-inline"><font color="red">*</font> </span>
					</c:if>
				</div>
				<div class="span1">
				</div>

			</div>
		</div>--%>
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
					<form:input path="investment" htmlEscape="false" maxlength="200" class="input-small" readonly="${readonly}" onkeyup="onlyNum(this)"/>&nbsp;万元
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

	</div>

	<div class="form-actions">
			<c:if test="${not readonly}">
		    <input id="btnSubmit" class="btn btn-primary" type="button" value="保 存" onclick="save();"/>&nbsp;
			</c:if>
			<input id="btnCancel" class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
		</div>
    <br>

	</form:form>
	<c:if test="${(contract.status!=1) && (contract.status!=2) }">
		<%@ include file="auditComment.jsp"%>
	</c:if>
</body>
</html>