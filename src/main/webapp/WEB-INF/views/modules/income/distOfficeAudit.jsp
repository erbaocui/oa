<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>运营管理审核</title>
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

			// 请求
           /* $.post("<%--${ctx}--%>/income/distOffice/distInit",{incomeId:"1"},
                function(result){
                    $.each(result, function(key, val){
                        $("#content").append('<table id="contentTable" class="table table-striped table-bordered table-condensed">');
                        $("#content").append("<thead><tr><th>分配部门</th><th>部门金额</th><th>规则分段</th><th>明细</th></tr></thead>");
                        $("#content").append("</table>");
                       // $("#content").listview("refresh");   //在使用'ul'标签时才使用，作用:刷新列表
						$("#contentTable").trigger("create");

                    });
                });*/
		});
		function test() {

            $("#inputForm").attr("action", "${ctx}/income/distProc/distRule");
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

        function review(state){
            $("#inputForm").attr("action", "${ctx}/income/distProc/officeAuditSubmit");
            $("#state").val(state);
            $("#inputForm").submit();
        }

	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/income/distOffice/">进款分配规则选择</a></li>

	</ul><br/>
	<c:set var="saveFlag" value="true"></c:set>

	<form:form id="inputForm" modelAttribute="distOfficeProc" action="${ctx}/income/distOffice/form" method="post" class="form-horizontal">
		<form:hidden path="id"/>
		<form:hidden path="incomeId" value="${incomeId}"/>
	    <form:hidden path="taskId"  value="${taskId}"/>
	    <form:hidden path="state"  value="${state}"/>
	<sys:message content="${message}"/>
		<div id="content" >
			<table id="contentTable" class="table table-striped table-bordered table-condensed">
				<thead><tr><th>分配部门</th><th>部门金额</th><th>规则选择</th><th>规则内费用</th><th>费用项</th><th>明细</th></tr></thead>
			<c:forEach items="${distOffices}" var="distOffice">

					<tr>
						<td rowspan="${distOffice.rowspan}">${distOffice.officeName}</td>
						<td rowspan="${distOffice.rowspan}">${distOffice.value}</td>
						<td rowspan="${distOffice.rowspan}">

							<select name="groups" onchange="test()" disabled="disabled">
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
			</c:forEach>
			</table>
		</div>

	  <c:if test="${saveFlag==true}">
		<div class="form-actions">
			<%--<c:if test="${saveFlag==true}">
				<input id="btnSubmit" class="btn btn-primary" type="button" value="保 存" onclick="save()"/>&nbsp;
				<input id="btnSubmit" class="btn btn-primary" type="button" value="保存账户" onclick="saveAccount()"/>&nbsp;
			</c:if>--%>

			<%--<form:form id="reviewForm" modelAttribute="review"   action="${ctx}/income/distProc/officeDistSubmit" method="post" class="form-horizontal">--%>

				<div class="container-fluid">

							<div class="row-fluid">
								<div class="span1">
								</div>
								<div class="span10">
									<label >审核意见:</label>
									<form:textarea path="comment" htmlEscape="false" rows="3" maxlength="500" class="input-xxlarge" />
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
								<div class="span2">
									<input id="btnSubmit" class="btn btn-primary" type="button"  onclick="review(1)" value="通过" />
								</div>
								<%--<div class="span2">
									<input id="btnReject" class="btn btn-primary" type="button"  onclick="review(2)" value="驳回" />
								</div>--%>
								<div class="span5">
								</div>
							</div>
						</div>

			<%--</form:form>--%>

		</div>
		</div>
	   </c:if>

				</form:form>
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