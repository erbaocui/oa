<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>培训人员</title>
	<meta name="decorator" content="default"/>
</head>
<body>
	<ul class="nav nav-tabs">
		<li><a href="${ctx}/sys/train/">培训列表</a></li>
		<li class="active"><a href="${ctx}/sys/train/assign?id=${train.id}"><shiro:hasPermission name="sys:train:edit">培训人员</shiro:hasPermission><shiro:lacksPermission name="sys:role:edit">人员列表</shiro:lacksPermission></a></li>
	</ul>
	<div class="container-fluid breadcrumb">
		<div class="row-fluid span12">
			<span class="span12">培训名称: <b>${train.name}</b></span>

		</div>
		<div class="row-fluid span12">
			<span class="span6">培训专业: ${fns:getDictLabel(train.professional, 'professional', '')}</span>
			<span class="span6">培训时间: <b>	<fmt:formatDate value="${train.date}" pattern="yyyy-MM-dd HH:mm"/></b></span>
		</div>

	</div>
	<sys:message content="${message}"/>
	<div class="breadcrumb">
		<form id="assignTrainForm" action="${ctx}/sys/train/assignTrain" method="post" class="hide">
			<input type="hidden" name="id" value="${train.id}"/>
			<input id="idsArr" type="hidden" name="idsArr" value=""/>
		</form>
		<input id="assignButton" class="btn btn-primary" type="submit" value="人员选择"/>
		<script type="text/javascript">
			$("#assignButton").click(function(){
				top.$.jBox.open("iframe:${ctx}/sys/train/usertotrain?id=${train.id}", "培训人员选择",810,$(top.document).height()-240,{
					buttons:{"确定选择":"ok", "清除已选":"clear", "关闭":true}, bottomText:"通过选择部门，然后为列出的人员进行选择。",submit:function(v, h, f){
						var pre_ids = h.find("iframe")[0].contentWindow.pre_ids;
						var ids = h.find("iframe")[0].contentWindow.ids;
						//nodes = selectedTree.getSelectedNodes();
						if (v=="ok"){
							// 删除''的元素
							if(ids[0]==''){
								ids.shift();
								pre_ids.shift();
							}
							if(pre_ids.sort().toString() == ids.sort().toString()){
								top.$.jBox.tip("未给角色【${role.name}】选择人员！", 'info');
								return false;
							};
					    	// 执行保存
					    	loading('正在提交，请稍等...');
					    	var idsArr = "";
					    	for (var i = 0; i<ids.length; i++) {
					    		idsArr = (idsArr + ids[i]) + (((i + 1)== ids.length) ? '':',');
					    	}
					    	$('#idsArr').val(idsArr);
					    	$('#assignTrainForm').submit();
					    	return true;
						} else if (v=="clear"){
							h.find("iframe")[0].contentWindow.clearAssign();
							return false;
		                }
					}, loaded:function(h){
						$(".jbox-content", top.document).css("overflow-y","hidden");
					}
				});
			});
		</script>
	</div>
	<table id="contentTable" class="table table-striped table-bordered table-condensed">
		<thead><tr><th>归属部门</th><th>登录名</th><th>姓名</th><th>电话</th><th>手机</th><shiro:hasPermission name="sys:user:edit"><th>操作</th></shiro:hasPermission></tr></thead>
		<tbody>
		<c:forEach items="${userList}" var="user">
			<tr>
				<td>${user.office.name}</td>
				<td><a href="${ctx}/sys/user/form?id=${user.id}">${user.loginName}</a></td>
				<td>${user.name}</td>
				<td>${user.phone}</td>
				<td>${user.mobile}</td>
				<shiro:hasPermission name="sys:train:edit"><td>
					<a href="${ctx}/sys/train/outTrain?userId=${user.id}&trainId=${train.id}"
						onclick="return confirmx('确认要将用户<b>[${user.name}]</b>从<b>[${train.name}]</b>培训中移除吗？', this.href)">移除</a>
				</td></shiro:hasPermission>
			</tr>
		</c:forEach>
		</tbody>
	</table>
</body>
</html>
