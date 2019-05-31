<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同管理</title>
	<meta name="decorator" content="default"/>
<%--	<link href="${ctxStatic}/jqGrid/4.6/css/ui.jqgrid.css" type="text/css" rel="stylesheet" />
	<script src="${ctxStatic}/jqGrid/4.7/js/jquery.jqGrid.js" type="text/javascript"></script>
	<script src="${ctxStatic}/jqGrid/4.7/js/jquery.jqGrid.extend.js" type="text/javascript"></script>--%>
	<script type="text/javascript">
		$(document).ready(function() {
            var provinceId=window.document.getElementById("province.id").value;
            if((provinceId!=null)&&(provinceId!="")){
                findCity(provinceId);
			}

		});
		function page(n,s){
			$("#pageNo").val(n);
			$("#pageSize").val(s);
			$("#searchForm").submit();
        	return false;
        }
        function startProcess(id) {
            $.post("${ctx}/cont/base/checkStartProcess",{id:id},function(data){
                var code=data.result;
                if(code=='success'){

                     $.post("${ctx}/cont/audit/proc/start?",{id:id},function(data){
                      var code=data.result;
						 if(code=='success'){
						  top.$.jBox.tip("流程启动成功","success",{persistent:true,opacity:0});
					      page();

					       }else{
					        top.$.jBox.tip("流程启动失败","error",{persistent:true,opacity:0});
					       }
					   });


                }else{
                    top.$.jBox.tip(data.msg,'error',{persistent:true,opacity:0});
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
                    $("#city").empty();
                    //$("#template").select2();
                    if (msg.result=="success") {
                        var $option = $("<option>").attr({"value" : ""}).text("请选择");
                        $("#city").append($option);
                        $.each(msg.citys,function(i, item){
                            if("${contract.city.id}"==item.id){
                                $option = $("<option  selected='selected'>").attr({"value" : item.id}).text(item.name);
                            }else{
                                $option = $("<option>").attr({"value" : item.id}).text(item.name);
							}

                            $("#city").append($option);
                        });
                        //$("#city option:first").prop("selected", 'selected');
                        $("#city").change();

                    }
                },
                error : function(json) {
                    $.jBox.alert("网络异常！");
                }
            });
        }
        function cityChange(v) {
		    window.document.getElementById("city.id").value=v;
        }

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/cont/base/list">合同列表</a></li>
		<shiro:hasPermission name="cont:creator:create"><li><a href="${ctx}/cont/base/form">合同新建</a></li></shiro:hasPermission>
	</ul>
	<form:form id="searchForm" modelAttribute="queryContract" action="${ctx}/cont/base/list" method="post" class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
		<input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

		<ul class="ul-form">
				<li><label>所属地区：</label>

				<form:select path="province.id" class="input-mini" onchange="findCity(this.options[this.options.selectedIndex].value);" >
					<form:option value="" label="请选择"/>
					<form:options items="${provinceList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
				</form:select>

				</li>
				<li><label>所属地区：</label>
					<form:hidden path="city.id"/>
					<form:select path="city" class="input-mini" onchange="cityChange(this.options[this.options.selectedIndex].value)" >
						<form:option value="" label="请选择"/>
					</form:select>
				</li>
				<li> <label>签约年份：</label>
				<form:select path="year" class="input-mini">
					<form:option value="" label="请选择"/>
					<form:options items="${fns:getDictList('contract_year')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
				</form:select>
			</li>
			<li><label>签订部门：</label><sys:treeselect id="office" name="office.id" value="${queryContract.office.id}" labelName="office.name" labelValue="${queryContract.office.name}" isAll="true"
													title="部门" url="/sys/office/treeData?type=2" cssClass="input-medium" allowClear="true" notAllowSelectParent="true"/>
			</li>

			<li class="clearfix"></li>
			<li>
				<label>合同金额：</label>
				<form:input path="min" value="${min}"  onkeyup="onlyNum(this)"  htmlEscape="false" maxlength="50" class="input-mini"/> -
				<form:input path="max" value="${max}"  onkeyup="onlyNum(this)" htmlEscape="false" maxlength="50"  class="input-mini"/>万元
			</li>
			<li><label>合同名称：</label>&nbsp;<form:input path="name" value="${name}" htmlEscape="false" maxlength="50" class="input-xlarge"/></li>
			<li><label>甲方名称：</label>&nbsp;<form:input path="firstParty" value="${firstParty}" htmlEscape="false" maxlength="50" class="input-xlarge"/></li>

			<li class="clearfix"></li>
			<li><label>合同类型：</label>
				<form:checkboxes path="typeIds" items="${fns:getDictList('contract_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
			</li>

				<li class="btns">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input id="btnSubmit" class="btn btn-primary" type="submit" value="查询" onclick="return page();"/>
			</li>
			<li class="clearfix"></li>
		</ul>
	</form:form>
	<sys:message content="${message}"/>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th style="text-align:center">合同名称</th>
				<th style="text-align:center">甲方名称</th>
				<th style="text-align:center">部门</th>
				<th style="text-align:center">合同额</th>
				<th style="text-align:center">进款额</th>
				<th style="text-align:center">收款进度</th>
				<th style="text-align:center">合同状态</th>
				<th style="text-align:center">操作</th>
			</tr>
		</thead>
		<tbody>
		<c:forEach items="${page.list}" var="contract">
			<tr>
				<td width="25%" title="${contract.name}">
					<c:if test="${fn:length(contract.name)<=22}">
						<a href="${ctx}/cont/base/form?id=${contract.id}&readonly=true&single=multi">${contract.name}</a>

					</c:if>
					<c:if test="${fn:length(contract.name)>22}">
						<a href="${ctx}/cont/base/form?id=${contract.id}&readonly=true&single=multi">${fn:substring(contract.name,0,21)}...</a>

					</c:if>
				</td>
				<td width="20%"  title="${contract.firstParty}">
					<c:if test="${fn:length(contract.firstParty)<=17}">
						${contract.firstParty}
					</c:if>
					<c:if test="${fn:length(contract.firstParty)>17}">
						${fn:substring(contract.firstParty,0,16)}...
					</c:if>
				</td>
				<td width="16%">
					${contract.office.name}
				</td>
				<td width="7%" style="text-align:right">
					${contract.value}
				</td>
				<td width="7%" style="text-align:right">
					${contract.income}
				</td>
				<td width="5%" style="text-align:right">
					<c:if test="${contract.value >0}">
					 <fmt:formatNumber type="number" value="${(contract.income/contract.value)*100}" maxFractionDigits="2"/>%
					</c:if>
					<c:if test="${contract.value <=0}">
					  0%
					</c:if>


				</td>
				<td width="5%">
						${fns:getDictLabel(contract.status, 'contract_status', '无')}
				</td>
				<td width="15%">

					<shiro:hasPermission name="cont:creator:create" >
						<c:if test="${contract.status == 1}">
							<a href=" #;" onclick="startProcess('${contract.id}')">启动流程</a>
						</c:if>
						<c:if test="${(contract.status != 1)&&(contract.status != 2)}">
							<a href="${ctx}/cont/applyPay/list?contractId=${contract.id}&readonly=false&single=single">请款管理</a></a>
						</c:if>

					</shiro:hasPermission>
					<shiro:hasPermission name="cont:manager:edit" >
						<c:if test="${contract.status != 1}">
						<a href="${ctx}/cont/base/form?id=${contract.id}&readonly=false">修改</a>
						<a href="${ctx}/cont/base/delete?id=${contract.id}" onclick="return confirmx('确认要删除该保存合同成功吗？', this.href)">删除</a>
						</c:if>
					</shiro:hasPermission>
					<shiro:hasPermission name="cont:creator:edit" >
						<c:if test="${contract.status == 1}">
						<a href="${ctx}/cont/base/form?id=${contract.id}&readonly=false">修改</a>
						<a href="${ctx}/cont/base/delete?id=${contract.id}" onclick="return confirmx('确认要删除该保存合同成功吗？', this.href)">删除</a>
						</c:if>
					</shiro:hasPermission>
				</td>
			</tr>
		</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>