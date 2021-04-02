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

<!--工具栏-->
<div class="easyui-layout" data-options="fit:true">

    <div id="toolbar">
        <div class="toolbar-button">
            <c:forEach items="${thirdMenuList }" var="thirdMenu">
                <a href="#" class="easyui-linkbutton" iconCls="${thirdMenu.icon }" onclick="${thirdMenu.url}" plain="true">${thirdMenu.name }</a>
            </c:forEach>
        </div>
        <div class="toolbar-search">
            <label>角色名称:</label><input id="search-name" style="height: 25px" class="text" style="width:100px">
            <a href="#" id="search-btn" class="easyui-linkbutton" iconCls="icon-search">搜索</a>
        </div>
    </div>

    <table id="data-datagrid" class="easyui-datagrid" toolbar="#toolbar"></table>
</div>

<style>
.selected{
	background:red;
}
</style>

<!--添加窗口-->
<div id="add-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
	<form id="add-form" method="post">
        <table>
            <tr>
                <td width="70" align="right">角色名称:</td>
                <td><input type="text" id="add-name" name="name" style="height: 25px" class="text easyui-validatebox" data-options="required:true, missingMessage:'请填写角色名称'" /></td>
            </tr>
            <tr>
                <td width="70" align="right">角色备注:</td>
                <td><textarea id="add-remark" name="remark" rows="6" class="textarea" style="width:260px"></textarea></td>
            </tr>
        </table>
    </form>
</div>
<!-- 修改窗口 这个窗口起初是关闭的closed:true -->
<div id="edit-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:450px; padding:10px;">
	<form id="edit-form" method="post">
        <input type="hidden" name="id" id="edit-id">
        <table>
            <tr>
                <td width="70" align="right">角色名称:</td>
                <td><input type="text" id="edit-name" name="name" style="height: 25px" class="text easyui-validatebox" data-options="required:true, missingMessage:'请填写角色名称'" /></td>
            </tr>
            <tr>
                <td width="70" align="right">角色备注:</td>
                <td><textarea id="edit-remark" name="remark" rows="6" class="textarea" style="width:260px"></textarea></td>
            </tr>
        </table>
    </form>
</div>
<!-- 编辑权限弹窗 -->
<div id="select-authority-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:220px;height:450px; padding:10px;">
    <!--easyui树（tree）定义在 <ul> 元素中    checkbox 	boolean 	定义是否在每个节点前边显示复选框。-->
    <ul id="authority-tree" url="getAllMenu" checkbox="true"></ul>
</div>

</body>

<script type="text/javascript">

    //打开添加窗口
    function openAdd(){

        $('#add-dialog').dialog({
            //closable 	boolean 	定义是否显示关闭按钮。
            closed: false,
            //modal 	boolean 	定义窗口是不是模态（modal）窗口。模态窗口就是在该窗口关闭之前，其父窗口不可能成为活动窗口的那种窗口。
            modal:true,
            //对话框的标题文本。
            title: "添加角色信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: add
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#add-dialog').dialog('close');
                }
            }]
        });
    }

	//添加记录
	function add(){

		var name = $("#add-name").val();
        var remark = $("#add-remark").val();
        if(name.length == 0){
            $.messager.alert("消息提醒","请输入角色名称!","warning");
            return;
        }
		if(remark == ""){
            $.messager.alert("消息提醒","请输入角色备注!","warning");
            return;
        }

		//序列表单内容为字符串。serialize() 方法通过序列化表单值，创建 URL 编码文本字符串。
		var data = $("#add-form").serialize();
		$.ajax({
			url:'roleAdd',
			dataType:'json',
			type:'post',
			data:data,
			success:function(data){
				if(data.type == 'success'){
					$.messager.alert('信息提示','添加成功！','info');
					$('#add-dialog').dialog('close');
					$('#data-datagrid').datagrid('reload');
				}else{
					$.messager.alert('信息提示',data.msg,'warning');
				}
			}
		});
	}

    //打开修改窗口
    function openEdit(){

        var item = $('#data-datagrid').datagrid('getSelected');
        if(item == null || item.length == 0){
            $.messager.alert('信息提示','请选择要修改的数据！','info');
            return;
        }

        $('#edit-dialog').dialog({
            closed: false,
            modal:true,
            title: "修改信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: edit
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#edit-dialog').dialog('close');
                }
            }],
            onBeforeOpen:function(){
                $("#edit-id").val(item.id);
                $("#edit-name").val(item.name);
                $("#edit-remark").val(item.remark);
            }
        });
    }

    //修改记录
    function edit(){

        var name = $("#edit-name").val();
        var remark = $("#edit-remark").val();
        if(name.length == 0){
            $.messager.alert("消息提醒","请输入角色名称!","warning");
            return;
        }
        if(remark == ""){
            $.messager.alert("消息提醒","请输入角色备注!","warning");
            return;
        }

        var data = $("#edit-form").serialize();
        $.ajax({
            url:'roleEdit',
            dataType:'json',
            type:'post',
            data:data,
            success:function(data){
                if(data.type == 'success'){
                    $.messager.alert('信息提示','修改成功！','info');
                    $('#edit-dialog').dialog('close');
                    $('#data-datagrid').datagrid('reload');
                }else{
                    $.messager.alert('信息提示',data.msg,'warning');
                }
            }
        });
    }

    //删除记录
    function remove(){

        var item = $('#data-datagrid').datagrid('getSelected');
        if(item == null || item.length == 0){
            $.messager.alert('信息提示','请选择要删除的数据！','info');
            return;
        }

        $.messager.confirm('信息提示','确定要删除该记录？', function(result){
            if(result){
                var item = $('#data-datagrid').datagrid('getSelected');
                $.ajax({
                    url:'roleDelete',
                    dataType:'json',
                    type:'post',
                    data:{id:item.id},
                    success:function(data){
                        if(data.type == 'success'){
                            $.messager.alert('信息提示','删除成功！','info');
                            $('#data-datagrid').datagrid('reload');
                        }else{
                            $.messager.alert('信息提示',data.msg,'warning');
                        }
                    }
                });
            }
        });
    }

    //搜索按钮监听
    $("#search-btn").click(function(){
        //这个name会作为参数向roleList发起请求  在service中会根据name进行模糊查询
        $('#data-datagrid').datagrid('reload',{
            name:$("#search-name").val()
        });
    });

    //载入列表数据
    $('#data-datagrid').datagrid({
        url:'roleList',
        //设置为 true，则显示带有行号的列。
        rownumbers:true,
        //设置为 true，则只允许选中一行。
        singleSelect:true,
        //当设置了 pagination 属性时，初始化页面尺寸
        pageSize:20,
        //设置为 true，则在数据网格（datagrid）底部显示分页工具栏
        pagination:true,
        //定义是否启用多列排序
        multiSort:true,
        //设置为 true，则会自动扩大或缩小列的尺寸以适应网格的宽度并且防止水平滚动。
        fitColumns:true,
        //指示哪个字段是标识字段。
        idField:'id',
        //
        treeField:'name',
        fit:true,
        columns:[[
            { field:'chk',checkbox:true},
            //sortable 	boolean 	设置为 true，则允许该列被排序。
            { field:'name',title:'角色名称',width:100,sortable:true},
            { field:'remark',title:'角色备注',width:100,sortable:true},
            //formatter单元格的格式化函数，需要三个参数：value：字段的值。rowData：行的记录数据。rowIndex：行的索引。
            { field:'icon',title:'权限操作',width:100,formatter:function(value,row,index){
                    var test = '<a class="authority-edit" onclick="selectAuthority('+row.id+')"></a>'
                    return test;
                }},
        ]],
        //事件：当数据加载成功时触发。
        onLoadSuccess:function(data){
            $('.authority-edit').linkbutton({text:'编辑权限',plain:true,iconCls:'icon-edit'});
        }
    });

    //某个角色已经拥有的权限
    var existAuthority = null;
    //打开权限选择框
    function selectAuthority(roleId){

        $('#select-authority-dialog').dialog({
            closed: false,
            modal:true,
            title: "选择权限信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: function(){

                    //getChecked 	state 	获取选中的节点。状态可用值有：'checked'、'unchecked'、'indeterminate'。如果状态未分配，则返回 'checked' 节点。
                    var checkedNodes = $("#authority-tree").tree('getChecked');
                    var ids = '';
                    for(var i=0;i<checkedNodes.length;i++){
                        ids += checkedNodes[i].id + ',';
                    }
                    var checkedParentNodes = $("#authority-tree").tree('getChecked', 'indeterminate');
                    for(var i=0;i<checkedParentNodes.length;i++){
                        ids += checkedParentNodes[i].id + ',';
                    }
                    //console.log(ids);
                    if(ids != ''){

                        $.ajax({
                            url:'addAuthority',
                            type:"post",
                            data:{ids:ids,roleId:roleId},
                            dataType:'json',
                            success:function(data){
                                if(data.type == 'success'){
                                    $.messager.alert('信息提示','权限编辑成功！','info');
                                    $('#select-authority-dialog').dialog('close');
                                }else{
                                    $.messager.alert('信息提示',data.msg,'info');
                                }
                            }
                        });
                    }else{
                        $.messager.alert('信息提示','请至少选择一条权限！','info');
                    }
                }
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#select-authority-dialog').dialog('close');
                }
            }],
            //onBeforeOpen 	none 	面板（panel）打开前触发，返回 false 就停止打开。
            onBeforeOpen:function(){

                //首先获取该角色已经拥有的权限
                $.ajax({
                    url:'getRoleAuthority',
                    data:{roleId:roleId},
                    type:'post',
                    dataType:'json',
                    success:function(data){
                        existAuthority = data;
                        $("#authority-tree").tree({
                            //返回要显示的过滤数据。返回数据时以标准树格式返回的。该函数有下列参数：data：要加载的原始数据。parent：DOM 对象，表示父节点。
                            //这里的rows是所有的菜单
                            loadFilter: function(rows){
                                return getTreeData(rows);
                            }
                        });
                    }
                });

            }
        });
    }

    //由原始数据得到符合tree要求的数据   menus：为所有菜单
    function getTreeData(menus){

        var nodes = [];
        //获取所有一级菜单
        for(var i=0; i<menus.length; i++){
            var menu = menus[i];
            if(menu.parentId == "0"){
                //这里的id，text是由tree定义的
                nodes.push({id:menu.id, text:menu.name,children:[]});
            }
        }
        //获取每个一级菜单的二级菜单
        for(var i=0; i<nodes.length; i++){
            var node = nodes[i];
            for(var j=0; j<menus.length; j++){
                var secondNode = menus[j];
                if(secondNode.parentId == node.id){
                    var child = {id:secondNode.id, text:secondNode.name}

                    //判断该菜单的权限是否为该用户所有
                    for(var k=0;k<existAuthority.length;k++){
                        if(existAuthority[k].menuId == secondNode.id ){
                            child.checked = true;
                        }
                    }

                    node.children.push(child);
                }
            }
        }

        return nodes;
    }

    //转换原始数据至符合tree的要求
    function convert(rows){
        var nodes = [];
        //首先获取所有的父分类
        for(var i=0; i<rows.length; i++){
            var row = rows[i];
            if (!exists(rows, row.parentId)){
                nodes.push({
                    id:row.id,
                    text:row.name
                });
            }
        }

        var toDo = [];
        for(var i=0; i<nodes.length; i++){
            toDo.push(nodes[i]);
        }

        while(toDo.length){
            //shift()方法用于把数组的第一个元素从其中删除，并返回第一个元素的值。
            var node = toDo.shift();
            for(var i=0; i<rows.length; i++){
                var row = rows[i];
                if (row.parentId == node.id){
                    var child = {id:row.id,text:row.name};

                    if(isAdded(row,rows)){
                        child.checked = true;
                    }
                    if (node.children){
                        node.children.push(child);
                    } else {
                        node.children = [child];
                    }
                    //把刚才创建的孩子再添加到父分类的数组中
                    toDo.push(child);
                }
            }
        }
        return nodes;
    }

    //判断是否有父类
    function exists(rows, parentId){
        for(var i=0; i<rows.length; i++){
            if (rows[i].id == parentId)
                return true;
        }
        return false;
    }

	function isAdded(row,rows){
		for(var k=0;k<existAuthority.length;k++){
			if(existAuthority[k].menuId == row.id && haveParent(rows,row.parentId)){
				//console.log(existAuthority[k].menuId+'---'+row.parentId);
				return true;
			}
		}
		return false;
	}
	
	//判断是否有父分类
	function haveParent(rows,parentId){
		for(var i=0; i<rows.length; i++){
			if (rows[i].id == parentId){
				if(rows[i].parentId != 0)
				    return true;
			}
		}
		return false;
	}

</script>