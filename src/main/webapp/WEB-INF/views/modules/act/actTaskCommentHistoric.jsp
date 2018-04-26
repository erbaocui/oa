<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>流程历史</title>
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
		});

		function review(state){
            $("#state").val(state);
            $("#reviewForm").submit();
		}
	</script>
</head>
<body>
	<ul class="nav nav-tabs">
		<li class="active"><a>批注历史</a></li>
	</ul>
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
	<div class="form-actions">

		<input id="btnCancel"  class="btn btn-primary" type="button" value="返 回" onclick="history.go(-1)"/>
	</div>

</body>
</html>