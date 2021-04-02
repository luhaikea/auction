<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <link rel="stylesheet" type="text/css" href="../../resources/index/easyui/1.7.0/themes/bootstrap/easyui.css" />
    <link rel="stylesheet" type="text/css" href="../../resources/index/css/index.css" />
    <link rel="stylesheet" type="text/css" href="../../resources/index/css/icon.css" />
    <script type="text/javascript" src="../../resources/index/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../../resources/index/easyui/1.7.0/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../resources/index/easyui/1.7.0/locale/easyui-lang-zh_CN.js"></script>
<body class="easyui-layout">
    <div class="easyui-layout" data-options="fit:true">

        <div id="toolbar">
            <h3 style="color: #1F1F1F">类目选择</h3>
            <div class="toolbar-search">
                <label>一级分类:</label><select id="catalog1" class="text" style="width:100px"></select>
                <label>二级分类:</label><select id="catalog2" class="text" style="width:100px"></select>
                <label>三级分类:</label><select id="catalog3" class="text" style="width:100px"></select>
                <a href="#" id="add-btn" class="easyui-linkbutton" iconCls="icon-add">添加平台属性</a>
                <a href="#" id="remove-btn" class="easyui-linkbutton" iconCls="icon-cross">删除平台属性</a>
            </div>
        </div>

        <table id="data-datagrid" class="easyui-datagrid" toolbar="#toolbar"></table>
    </div>

    <!--添加窗口-->
    <div id="add-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:600px; padding:10px;">
        <form id="add-form" method="post">
            <table id="attrValueTable">

                <tr>
                    <a href="#" id="addvalue-btn" class="easyui-linkbutton" iconCls="icon-add">添加属性值</a>
                </tr>
                <tr>
                    <a href="#" id="deletevalue-btn" class="easyui-linkbutton" iconCls="icon-cross">删除最后一行属性值</a>
                </tr>
                <tr>
                    <td width="60" align="right">属性名称:</td>
                    <td>
                        <input type="text" id="attrName" style="height: 25px" class="text easyui-validatebox" data-options="required:true, missingMessage:'请填写属性名称'" />
                    </td>
                </tr>

            </table>
        </form>
    </div>

    <!--修改窗口-->
    <div id="edit-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:600px; padding:10px;">
        <form id="edit-form" method="post">
            <table id="editAttrValueTable">

                <tr>
                    <a href="#" id="editaddvalue-btn" class="easyui-linkbutton" iconCls="icon-add">添加属性值</a>
                </tr>
                <tr>
                    <a href="#" id="editdeletevalue-btn" class="easyui-linkbutton" iconCls="icon-cross">删除最后一行属性值</a>
                </tr>
                <tr>
                    <td width="60" align="right">属性名称:</td>
                    <td>
                        <input type="text" id="editattrName" style="height: 25px" class="text easyui-validatebox" data-options="required:true" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="hidden" id="editattrId">
                    </td>
                </tr>

            </table>
        </form>
    </div>

</body>

<script type="text/javascript">

    //初始化一级分类
    $('#catalog1').combobox({
        url:'getCatalog1',
        valueField:'id',
        textField:'name',
        //onSelect 	record 	当用户选择一个列表项时触发。
        onSelect: function(record){
            var url = 'getCatalog2?catalog1Id='+record.id;
            $('#catalog2').combobox('reload', url);
        }
    });

    //初始化二级分类
    $('#catalog2').combobox({
        valueField:'id',
        textField:'name',
        //onSelect 	record 	当用户选择一个列表项时触发。
        onSelect: function(record){
            var url = 'getCatalog3?catalog2Id='+record.id;
            $('#catalog3').combobox('reload', url);
        }
    });

    //初始化三级分类
    $('#catalog3').combobox({
        valueField:'id',
        textField:'name',
        onSelect: function(record){
            //var url = 'attrInfoList?catalog3Id='+record.id;
            //加载并显示第一页的行，如果指定 'param' 参数，它将替换 queryParams 属性
            $('#data-datagrid').datagrid('reload', {catalog3Id:record.id});
        }
    });

    //这个表格应该只能是post请求
    $('#data-datagrid').datagrid({
        url:'attrInfoList',
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
        fit:true,
        columns:[[
            { field:'chk',checkbox:true},
            //sortable 	boolean 	设置为 true，则允许该列被排序。
            { field:'attrName',title:'属性名称',width:100,sortable:true},
            //formatter单元格的格式化函数，需要三个参数：value：字段的值。rowData：行的记录数据。rowIndex：行的索引。
            { field:'catalog3Id',title:'修改属性',width:100,formatter:function(value,row,index){

                    var test = '<a class="authority-edit" onclick="editAttrInfo('+row.id+')"></a>'
                    return test;
                }},
        ]],
        //事件：当数据加载成功时触发。
        onLoadSuccess:function(data){
            $('.authority-edit').linkbutton({text:'修改',plain:true,iconCls:'icon-edit'});
        }
    });

    //添加平台属性按钮监听
    $("#add-btn").click(function(){

        //先清空attrValueTable
        var sizeTr = $("#attrValueTable tr").length;
        $("#attrValueTable tr").slice(4,sizeTr).remove();

        //判断是否选中三级分类
        var catalog3Val = $('#catalog3').combobox('getValue');
        if(!catalog3Val){
            $.messager.alert("消息提醒","请选择三级分类!","warning");
            return;
        }
        $('#add-dialog').dialog({
            //closable 	boolean 	定义是否显示关闭按钮。
            closed: false,
            //modal 	boolean 	定义窗口是不是模态（modal）窗口。模态窗口就是在该窗口关闭之前，其父窗口不可能成为活动窗口的那种窗口。
            modal:true,
            collapsible:true,
            minimizable:true,
            maximizable:true,
            //对话框的标题文本。
            title: "添加平台属性",
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
    });

    //在添加窗口中增加一行属性值输入框
    $("#addvalue-btn").click(function(){

        $("#attrValueTable").append('<tr name="vN">\n' +
            '                    <td width="60" align="right">属性值:</td>\n' +
            '                    <td><input type="text" style="height: 25px" name="valueName" class="text easyui-validatebox pAV" data-options="required:true, missingMessage:\'请填写属性值\'" /></td>\n' +
            '                </tr>');

    })

    //在添加窗口中删除一行属性值输入框
    $("#deletevalue-btn").click(function(){
        //添加属性过滤器保证 属性名称: 行不被删除
        $("#attrValueTable tr[name='vN']:last").remove();
    })

    //增加属性方法
    function add(){
        //validate的值是true或false
        var validate = $("#add-form").form("validate");
        if(!validate){
            $.messager.alert("消息提醒","请检查你输入的数据!","warning");
            return;
        }

        var catalog3Id = $('#catalog3').combobox('getValue');
        var attrName = $("#attrName").val();

        var attrValueList = [];
        $("input[name='valueName']").each(function(){
            attrValueList.push({'valueName':$(this).val()});
        })
        var data = {'attrName':attrName,'catalog3Id':catalog3Id,'attrValueList':attrValueList};

        var d = JSON.stringify(data);

        $.ajax({
            url:'saveAttrInfo',
            dataType:'json',
            contentType:'application/json',
            type:'post',
            data:d,
            success:function(data){
                if(data.type == 'success'){
                    $.messager.alert('信息提示',data.msg,'info');
                    $('#add-dialog').dialog('close');
                    //刷新表格   回调
                    $('#data-datagrid').datagrid('reload');
                }else{
                    $.messager.alert('信息提示',data.msg,'warning');
                }
            }
        });
    }

    //删除平台属性按钮监听
    $("#remove-btn").click(function (){

        var item = $('#data-datagrid').datagrid('getSelected');
        if(item == null || item.length == 0){
            $.messager.alert('信息提示','请选择要删除的数据！','info');
            return;
        }

        $.messager.confirm('信息提示','确定要删除该记录？', function(result){
            if(result){
                var item = $('#data-datagrid').datagrid('getSelected');
                $.ajax({
                    url:'attrDelete',
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

    })

    //修改平台属性值
    function editAttrInfo(attrInfoId){

        //先清空editAttrValueTable
        var sizeTr = $("#editAttrValueTable tr").length;
        $("#editAttrValueTable tr").slice(4,sizeTr).remove();

        var id = JSON.stringify({'id':attrInfoId});
        //初始化编辑框
        $.ajax({
            url:'getAttrInfo',
            dataType:'json',
            contentType:'application/json',
            type:'post',
            data:id,
            success:function(data){

               $("#editattrName").val(data.attrName);
               $("#editattrId").val(data.id);
               var attrValueList = data.attrValueList;

               for(var i=0;i<attrValueList.length;i++){
                   $("#editAttrValueTable").append('<tr name="vN">\n' +
                       '                    <td width="60" align="right">属性值:</td>\n' +
                       '                    <td><input type="text" style="height: 25px" name="editvalueName" value='+attrValueList[i].valueName+' class="text easyui-validatebox pAV" data-options="required:true, missingMessage:\'请填写属性值\'" /></td>\n' +
                       '                </tr>');
               }
            }
        });

        $('#edit-dialog').dialog({
            //closable 	boolean 	定义是否显示关闭按钮。
            closed: false,
            //modal 	boolean 	定义窗口是不是模态（modal）窗口。模态窗口就是在该窗口关闭之前，其父窗口不可能成为活动窗口的那种窗口。
            modal:true,
            collapsible:true,
            minimizable:true,
            maximizable:true,
            //对话框的标题文本。
            title: "修改平台属性",
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
            }]
        });
    }

    //在修改窗口中删除一行属性值输入框
    $("#editdeletevalue-btn").click(function(){
        //添加属性过滤器保证 属性名称: 行不被删除
        $("#editAttrValueTable tr[name='vN']:last").remove();
    })

    //在修改窗口中增加一行属性值输入框
    $("#editaddvalue-btn").click(function(){

        $("#editAttrValueTable").append('<tr name="vN">\n' +
            '                    <td width="60" align="right">属性值:</td>\n' +
            '                    <td><input type="text" name="editvalueName" style="height: 25px" class="text easyui-validatebox pAV" data-options="required:true, missingMessage:\'请填写属性值\'" /></td>\n' +
            '                </tr>');

    })

    //修改属性值方法
    function edit(){
        //validate的值是true或false
        var validate = $("#edit-form").form("validate");
        if(!validate){
            $.messager.alert("消息提醒","请检查你输入的数据!","warning");
            return;
        }

        var catalog3Id = $('#catalog3').combobox('getValue');
        var attrName = $("#editattrName").val();
        var id = $("#editattrId").val();

        var attrValueList = [];
        $("input[name='editvalueName']").each(function(){
            attrValueList.push({'valueName':$(this).val()});
        })
        var data = {'id':id,'attrName':attrName,'catalog3Id':catalog3Id,'attrValueList':attrValueList};

        var d = JSON.stringify(data);

        $.ajax({
            url:'saveAttrInfo',
            dataType:'json',
            contentType:'application/json',
            type:'post',
            data:d,
            success:function(data){
                if(data.type == 'success'){
                    $.messager.alert('信息提示',data.msg,'info');
                    $('#edit-dialog').dialog('close');
                    //刷新表格   回调
                    $('#data-datagrid').datagrid('reload');
                }else{
                    $.messager.alert('信息提示',data.msg,'warning');
                }
            }
        });
    }

</script>