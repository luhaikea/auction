<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<title></title>
<link rel="stylesheet" type="text/css" href="../../resources/index/easyui/1.7.0/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="../../resources/index/css/index.css" />
<link rel="stylesheet" type="text/css" href="../../resources/index/css/icon.css" />
<script type="text/javascript" src="../../resources/index/js/jquery-3.1.1.min.js"></script>
<script type="text/javascript" src="../../resources/index/easyui/1.7.0/jquery.easyui.min.js"></script>
<script type="text/javascript" src="../../resources/index/easyui/1.7.0/locale/easyui-lang-zh_CN.js"></script>
<body class="easyui-layout">

<!-- 修改密码窗口  这个窗口默认是打开的-->
<div id="edit-dialog" class="easyui-dialog" data-options="closed:false,iconCls:'icon-save',modal:true,title:'修改密码',buttons:[{text:'确认修改',iconCls: 'icon-ok',handler:submitEdit}]" style="width:450px; padding:10px;">
	<form id="edit-form" method="post">
        <table>
           <tr>
                <td width="60" align="right">用户名:</td>
                <td><input type="text" name="username" style="height: 25px" class="text easyui-validatebox" readonly="readonly" value="${admin.username }" /></td>
            </tr>
            <tr>
                <td width="60" align="right">原密码:</td>
                <td><input type="password" id="oldPassword" style="height: 25px" class="text easyui-validatebox" data-options="required:true, missingMessage:'请填写密码'" /></td>
            </tr>
            <tr>
                <td width="60" align="right">新密码:</td>
                <td><input type="password" id="newPassword" style="height: 25px" class="text easyui-validatebox" data-options="required:true, missingMessage:'请填写密码'" /></td>
            </tr>
            <tr>
                <td width="60" align="right">重复新密码:</td>
                <td><input type="password" id="reNewPassword" style="height: 25px" class="text easyui-validatebox" data-options="required:true, missingMessage:'请填写密码'" /></td>
            </tr>
        </table>
    </form>
</div>

</body>

<!-- End of easyui-dialog -->
<script type="text/javascript">

	function submitEdit(){
		var validate = $("#edit-form").form("validate");
		if(!validate){
			$.messager.alert("消息提醒","请检查你输入的数据!","warning");
			return;
		}
		if($("#newPassword").val() != $("#reNewPassword").val()){
			$.messager.alert("消息提醒","两次密码输入不一致!","warning");
			return;
		}
		$.ajax({
			url:'editPassword',
			type:'post',
			data:{newpassword:$("#newPassword").val(),oldpassword:$("#oldPassword").val()},
			dataType:'json',
			success:function(data){
				if(data.type == 'success'){
					$.messager.alert("消息提醒","密码修改成功!","warning");
				}else{
					$.messager.alert("消息提醒",data.msg,"warning");
				}
			}
		})
	}
	
	
</script>