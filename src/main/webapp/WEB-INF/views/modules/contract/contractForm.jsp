<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>合同管理</title>
	<meta name="decorator" content="default"/>
	<script type="text/javascript">


		$(document).ready(function() {
			$("#code").focus();
            $("#valueHint").html(dx(${contract.value}));
            if('${contract.hasProgrammeCost}'=='true' ){
                $("#hasProgrammeCostHint").html("如合同无明确约定，按公司规定方案费用公建项目收取30%，居住收取20%");
			}

			$("#inputForm").validate({
                onfocusout: function(element){
                   // $(element).valid();
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
            var checkedList = new Array();
            $("input[name='typeIdList']:checked").each(function() {
                checkedList.push($(this).val());
            });
            if(checkedList==null||checkedList==""){
                $.jBox.tip("请选合同类型", 'error');
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

        function changeFloat(that){
            that.value = that.value.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符
            that.value = that.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
            that.value = that.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
            that.value = that.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');//只能输入两个小数
            if(that.value.indexOf(".")< 0 && that.value !=""){//以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
                that.value= parseFloat(that.value);

            }
            $("#valueHint").html(dx(that.value));
        }
        function dx(money){
            var cnNums = new Array("零","壹","贰","叁","肆","伍","陆","柒","捌","玖"); //汉字的数字
            var cnIntRadice = new Array("","拾","佰","仟"); //基本单位
            var cnIntUnits = new Array("","万","亿","兆"); //对应整数部分扩展单位
            var cnDecUnits = new Array("角","分","毫","厘"); //对应小数部分单位
            //var cnInteger = "整"; //整数金额时后面跟的字符
            var cnIntLast = "元"; //整型完以后的单位
            var maxNum = 999999999999999.9999; //最大处理的数字

            var IntegerNum; //金额整数部分
            var DecimalNum; //金额小数部分
            var ChineseStr=""; //输出的中文金额字符串
            var parts; //分离金额后用的数组，预定义
            if( money == "" ){
                return "";
            }
            money = parseFloat(money);
            if( money >= maxNum ){
                //$.alert('超出最大处理数字');
                return "";
            }
            if( money == 0 ){
                //ChineseStr = cnNums[0]+cnIntLast+cnInteger;
                ChineseStr = cnNums[0]+cnIntLast
                //document.getElementById("show").value=ChineseStr;
                return ChineseStr;
            }
            money = money.toString(); //转换为字符串
            if( money.indexOf(".") == -1 ){
                IntegerNum = money;
                DecimalNum = '';
            }else{
                parts = money.split(".");
                IntegerNum = parts[0];
                DecimalNum = parts[1].substr(0,4);
            }
            if( parseInt(IntegerNum,10) > 0 ){//获取整型部分转换
                zeroCount = 0;
                IntLen = IntegerNum.length;
                for( i=0;i<IntLen;i++ ){
                    n = IntegerNum.substr(i,1);
                    p = IntLen - i - 1;
                    q = p / 4;
                    m = p % 4;
                    if( n == "0" ){
                        zeroCount++;
                    }else{
                        if( zeroCount > 0 ){
                            ChineseStr += cnNums[0];
                        }
                        zeroCount = 0; //归零
                        ChineseStr += cnNums[parseInt(n)]+cnIntRadice[m];
                    }
                    if( m==0 && zeroCount<4 ){
                        ChineseStr += cnIntUnits[q];
                    }
                }
                ChineseStr += cnIntLast;
                //整型部分处理完毕
            }
            if( DecimalNum!= '' ){//小数部分
                decLen = DecimalNum.length;
                for( i=0; i<decLen; i++ ){
                    n = DecimalNum.substr(i,1);
                    if( n != '0' ){
                        ChineseStr += cnNums[Number(n)]+cnDecUnits[i];
                    }
                }
            }
            if( ChineseStr == '' ){
                //ChineseStr += cnNums[0]+cnIntLast+cnInteger;
                ChineseStr += cnNums[0]+cnIntLast;
            }/* else if( DecimalNum == '' ){
		        ChineseStr += cnInteger;
		        ChineseStr += cnInteger;
		    } */
            return ChineseStr;
        }
        function change(that){
            that.value = that.value.replace(/[^\d.]/g,"");  //清除“数字”和“.”以外的字符
            that.value = that.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
            that.value = that.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
            that.value = that.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3');//只能输入两个小数
            if(that.value.indexOf(".")< 0 && that.value !=""){//以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
                that.value= parseFloat(that.value);

            }
        }
       function hasProgrammeCost(){
            //  alert($("#hasProgrammeCost1").attr('checked'));
            if( $("#hasProgrammeCost1").attr('checked')=='checked' ){
                //$("#hasProgrammeCost1").val(false);
                //$('#hasProgrammeCost1').removeAttr('checked');
                $("#hasProgrammeCostHint").html("如合同无明确约定，按公司规定方案费用公建项目收取30%，居住收入20%");

            }else{
                //$('#hasProgrammeCost1').attr('checked',true);
                // $("#hasProgrammeCost1").val(true);
                $("#hasProgrammeCostHint").html("");

            }
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
            <li ><a href="${ctx}/cont/contItem/list?contractId=${contract.id}&readonly=${readonly}">付费约定</a></li>
            <li><a href="${ctx}/cont/attach/list?contractId=${contract.id}&readonly=${readonly}">合同附件</a></li>
            <li><a href="${ctx}/cont/applyPay/list?contractId=${contract.id}&readonly=${readonly}">合同请款</a></li>
            <li><a href="${ctx}/income/income/contractIncome?contractId=${contract.id}&readonly=${readonly}">合同支付</a></li>
			<li><a href="${ctx}/cont/split/list?contractId=${contract.id}&readonly=${readonly}">合同拆解</a></li>

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
					<label >&nbsp;&nbsp;&nbsp;&nbsp;合同编号:</label>
					<input id="oldCode" name="oldCode" type="hidden" value="${contract.code}">
					<form:input path="code"   htmlEscape="false" maxlength="50" class=" form-control input-small " readonly="${readonly}" />

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
					<label >&nbsp;&nbsp;&nbsp;&nbsp;合同名称:</label>
					<form:input path="name" htmlEscape="false" maxlength="50" class="input-xxlarge " readonly="${readonly}" required="true"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
				<div class="span1">
				</div>
			</div>
	   </div>
		<shiro:hasPermission name="cont:manager:edit" >
		<c:if test="${contract.id!=null &&contract.status!=1 && contract.status!=2 }">
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>

				<div class="span10">
					<label >&nbsp;&nbsp;&nbsp;&nbsp;合同状态:</label>
					<form:select path="status" class="input-mini" disabled="${readonly}" >
						<form:options items="${statusList}" itemLabel="label" itemValue="value" htmlEscape="false"/>
					</form:select>
				</div>
				<div class="span1">
				</div>
			</div>
		</div>
		</c:if>
		</shiro:hasPermission>
		<div class="control-group">
                <div class="row-fluid">
                    <div class="span1">
                    </div>
                    <div class="span2">
						<label>&nbsp;&nbsp;&nbsp;&nbsp;所&nbsp;&nbsp;属&nbsp;&nbsp;省:</label>
						<form:select path="province.id" class="input-mini" onchange="findCity(this.options[this.options.selectedIndex].value);" disabled="${readonly}" >
							<form:options items="${provinceList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
						</form:select>
					</div>
					<div class="span2">
						<label>所属市/区:</label>
						<form:select path="city.id" class="input-mini"  disabled="${readonly}">
							<form:options items="${cityList}" itemLabel="name" itemValue="id" htmlEscape="false"/>
						</form:select>
					</div>
                    <div class="span7">
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
					<label >&nbsp;&nbsp;&nbsp;&nbsp;项目名称:</label>
					<div class="input-append" >
						<%--<form:input path="project.id" htmlEscape="false" maxlength="50" />
						<form:input path="projectName" htmlEscape="false" maxlength="50" class="input-xxlarge required"/>--%>
						<input id="project.id" name="project.id" readonly="readonly" type="hidden" value="${contract.project.id}" class="input-xlarge"/>
						<input id="project.name" name="project.name" readonly="readonly" type="text" value="${contract.project.name}"  class="input-xlarge" required="true"/>
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
				<div class="span11">
					<label >&nbsp;&nbsp;&nbsp;&nbsp;合同类型:</label>
                    <c:if test="${readonly}">
                        <form:checkboxes path="typeIdList" items="${fns:getDictList('contract_type')}" itemLabel="label" itemValue="value" htmlEscape="false" onclick="return false;" />
                    </c:if>
                    <c:if test="${not readonly}">
                        <form:checkboxes path="typeIdList" items="${fns:getDictList('contract_type')}" itemLabel="label" itemValue="value" htmlEscape="false" />
                    </c:if>
				</div>


			</div>
		</div>

		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label >&nbsp;&nbsp;&nbsp;&nbsp;甲方名称:</label>
					<form:input path="firstParty" htmlEscape="false" maxlength="50" class="input-xxlarge" readonly="${readonly}" required="true"/>
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
				<div class="span10">
					<label >&nbsp;&nbsp;&nbsp;&nbsp;有方案费用:</label>
                    <c:if test="${readonly}">
                        <form:checkbox path="hasProgrammeCost" htmlEscape="false" maxlength="50" onclick="return false;"  />
                    </c:if>
                    <c:if test="${not readonly}">
                        <form:checkbox path="hasProgrammeCost" htmlEscape="false" maxlength="50" onclick="hasProgrammeCost()"/>
                    </c:if>
					<span id="hasProgrammeCostHint"></span>
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
					<label >&nbsp;&nbsp;&nbsp;&nbsp;方案费用:</label>
					<form:input path="programmeCost" htmlEscape="false" maxlength="50" class="input-xxlarge" readonly="${readonly}" />

				</div>

				<div class="span1">
				</div>

			</div>
		</div>
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span4">
					<label>甲方联系人:</label>
					<form:input path="contact" htmlEscape="false" maxlength="32" class="form-control input-small" readonly="${readonly}" required="true"/>
					<span class="help-inline"><font color="red">*</font> </span>

				</div>
				<div class="span5">
					<label>甲方联系电话:</label>
					<form:input path="contactPhone" htmlEscape="false" maxlength="32" class="input-small" readonly="${readonly}" required="true"/>
					<span class="help-inline"><font color="red">*</font> </span>
				</div>

				<div class="span2">
				</div>

			</div>
		</div>


		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>


				<div class="span4">
					<label >&nbsp;&nbsp;&nbsp;投&nbsp;&nbsp;资&nbsp;&nbsp;额:</label>
					<form:input path="investment" htmlEscape="false" maxlength="200" class="input-small" readonly="${readonly}" onkeyup="onlyNum(this)" required="true"/>&nbsp;万元
					<span class="help-inline"><font color="red">*</font> </span>
				</div>
				<div class="span7">
					<label >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;合同金额:</label>
					<form:input path="value"  onkeyup="changeFloat(this)" htmlEscape="false" class="form-control input-small" readonly="${readonly}" required="true"/>&nbsp;元
					<span class="help-inline"><font color="red">*</font> </span>
					<span id="valueHint" ></span>
				</div>



			</div>
		</div>
		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>

				<div class="span4">
					<label >&nbsp;&nbsp;&nbsp;&nbsp;建筑面积:</label>
					<form:input path="areaValue" htmlEscape="false" maxlength="200" class="input-small" readonly="${readonly}" required="true" onkeyup="change(this)" />平米
					<span class="help-inline"><font color="red">*</font> </span>
				</div>

				<div class="span5">
					<label >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;图纸数量:</label>
					<form:input path="blueprintNum" htmlEscape="false" maxlength="200" class=" input-lg" readonly="${readonly}" />
					<%--<span class="help-inline"><font color="red">*</font> </span>--%>

				</div>
				<div class="span2">
				</div>

			</div>
		</div>

		<div class="control-group">
			<div class="row-fluid">
				<div class="span1">
				</div>
				<div class="span10">
					<label >&nbsp;&nbsp;&nbsp;&nbsp;包含子项:</label>
					<form:input path="subItem" htmlEscape="false" maxlength="400" class="input-xxlarge"  readonly="${readonly}" required="true"/>
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
				<div class="span10">
					<label >&nbsp;&nbsp;&nbsp;&nbsp;工期要求:</label>
					<form:input path="timeLimit" htmlEscape="false" maxlength="400" class="input-xxlarge" readonly="${readonly}" required="true"/>
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
					<label >&nbsp;&nbsp;&nbsp;&nbsp;注册时间:</label>

						<input name="createDate" type="text" readonly="readonly" maxlength="20" class="iform-control input-small Wdate"
							   value="<fmt:formatDate value="${contract.createDate}" pattern="yyyy-MM-dd"/>"
							   onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" disabled="disabled"/>


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
				<div class="span10">
					<label >&nbsp;&nbsp;&nbsp;&nbsp;备&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;注:</label>
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
		<%@ include file="proc/audit/auditComment.jsp"%>
	</c:if>
</body>
</html>