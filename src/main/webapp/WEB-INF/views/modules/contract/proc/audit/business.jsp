<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同管理</title>
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
            //alert($("#comment").val()=="");
            //alert($("#comment").val()==null);
			var comment=$("#comment").val().trim();
            $("#state").val(state);
            if(state==2){
				if(comment==null||comment==""){
						$.jBox.tip("提请填写审核意见！", 'error');
						return;
					}
            }
            $("#reviewForm").submit();


        }
	</script>
</head>
<body>
<ul class="nav nav-tabs">
	<li class="active"><a>合同审核-运营</a></li>
</ul><br/>
<sys:message content="${message}"/>
<%@ include file="auditInfo.jsp" %>
<form:form id="reviewForm" modelAttribute="review"   action="${ctx}/cont/audit/proc/business/submit" method="post" class="form-horizontal">
	<form:hidden path="taskId"  value="${taskId}"/>
	<form:hidden path="state"  />
	<div class="container-fluid">
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span6">
					<label >审核意见:</label>
					<form:textarea path="comment" htmlEscape="false" rows="3" maxlength="500" class="input-xxlarge" />
				</div>
				<div class="span1">
				</div>
			</div>
		</div>
		<div class="form-actions">
			<input id="btnSubmit" class="btn btn-primary" type="button" value="通过"  onclick="review(1)"/>&nbsp;
			<input id="btnCancel" class="btn " type="button" value="驳回" onclick="review(2)"/>
		</div>
	</div>
	<br>

</form:form>
<br>
<%@ include file="/WEB-INF/views/modules/act/comment.jsp" %>
</body>


</html>