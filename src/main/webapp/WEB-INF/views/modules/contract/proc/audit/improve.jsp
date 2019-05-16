<%@ page contentType="text/html;charset=UTF-8" %>

<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同管理</title>
	<meta name="decorator" content="default"/>

	<script type="text/javascript">
        function review(state){
           // var comment=$("#comment").val().trim();
            $("#state").val(state);
            /*if(comment==null||comment==""){
                $.jBox.tip("提请填写审核意见！", 'error');
                return;
            }*/
            $("#reviewForm").submit();
        }
	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li class="active"><a>合同审核-补充</a></li>
</ul><br/>
<sys:message content="${message}"/>
<%@ include file="auditInfo.jsp" %>
<br>
<form:form id="reviewForm" modelAttribute="review"   action="${ctx}/cont/audit/proc/improve/submit" method="post" class="form-horizontal">
    <form:hidden path="taskId"  value="${taskId}"/>
    <form:hidden path="state"  />
<div class="container-fluid">
		<div class="row-fluid">
			<div class="span1">
			</div>
			<div class="span10">
				<label >补充意见:</label>
				<form:textarea path="comment" htmlEscape="false" rows="3" maxlength="500" class="input-xxlarge" />
			</div>
			<div class="span1">
			</div>
	</div>
	<div class="form-actions">
		<input id="btnSubmit" class="btn btn-primary" type="button" value="提交"  onclick="review(1)"/>&nbsp;

	</div>
</div>
<br>
</form:form>
<br>
<%@ include file="/WEB-INF/views/modules/act/comment.jsp" %>

</body>


</html>