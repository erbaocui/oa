<%@ page contentType="text/html;charset=UTF-8" %>

	<div class="row-fluid">
		<div class="span1">
		</div>
		<div class="span10">
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
			<div class="span1">
			</div>
		</div>
	</div>



