<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title></title>
    <link rel="stylesheet" type="text/css" href="../../resources/index/easyui/1.7.0/themes/bootstrap/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="../../resources/index/css/index.css"/>
    <link rel="stylesheet" type="text/css" href="../../resources/index/css/icon.css"/>
    <script type="text/javascript" src="../../resources/index/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../../resources/index/easyui/1.7.0/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../resources/index/easyui/1.7.0/locale/easyui-lang-zh_CN.js"></script>
</head>
<body class="easyui-layout">
<div class="easyui-layout" data-options="fit:true">
    <!--类目选择-->
    <div id="toolbar">
        <div class="toolbar-search">
            <label>反馈状态:</label>
            <select id="replayS" class="text easyui-combobox" style="width:100px">
            </select>
        </div>
    </div>
    <!--物品表格-->
    <table id="data-datagrid" class="easyui-datagrid" toolbar="#toolbar"></table>

    <div id="feedback-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:400px; padding:10px;">
      <textarea id="feedback-text" rows="5" style="width: 350px"></textarea>
    </div>

    <div id="msg-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:400px; padding:10px;">
        <label>消息标题:</label><input id="replayTitle" style="width: 350px;height: 30px"><br>
        <label>消息内容:</label><textarea id="replayMsg-text" rows="5" style="width: 350px"></textarea>
    </div>
</div>

</body>
<script type="text/javascript">

    //初始化一级分类
    $('#replayS').combobox({
        url:'getReplayStatus',
        valueField:'id',
        textField:'name',
        //onSelect 	record 	当用户选择一个列表项时触发。
        onSelect: function (record) {
            $('#data-datagrid').datagrid('reload', {replayStatus: record.id});
        }
    });

    //初始化表格  这个表格应该只能是post请求
    $('#data-datagrid').datagrid({
        //这里得到的应该是未审批的物品
        url: 'getAllUmsSurveyInfo',
        //设置为 true，则显示带有行号的列。
        rownumbers: true,
        //设置为 true，则只允许选中一行。
        singleSelect: true,
        //当设置了 pagination 属性时，初始化页面尺寸
        pageSize: 20,
        //设置为 true，则在数据网格（datagrid）底部显示分页工具栏
        pagination: true,
        //定义是否启用多列排序
        multiSort: true,
        //设置为 true，则会自动扩大或缩小列的尺寸以适应网格的宽度并且防止水平滚动。
        fitColumns: true,
        //指示哪个字段是标识字段。
        idField: 'id',
        fit: true,
        columns: [[
            {field: 'chk', checkbox: true},
            //sortable 	boolean 	设置为 true，则允许该列被排序。
            {field: 'memberId', title: '用户ID', width: 100, sortable: true},
            {field: 'username', title: '用户姓名', width: 100, sortable: true},
            {field: 'phone', title: '用户电话', width: 100, sortable: true},
            {field: 'satisfaction', title: '满意度', width: 100, sortable: true},
            {field: 'createTime', title: '反馈时间', width: 100, sortable: true},
            {field: 'replayStatus', title: '回复状态', width: 100, sortable: true},
            //formatter单元格的格式化函数，需要三个参数：value：字段的值。rowData：行的记录数据。rowIndex：行的索引。
            {
                field: 'userFeedback', title: '操作', width: 100, formatter: function (value, row, index) {
                    var test = '<a class="user-feedback" onclick="lookUserFeedback('+row.id+')"></a>'
                    return test;
                }
            },
            {
                field: 'id', title: '操作', width: 100, formatter: function (value, row, index) {
                    var test = '<a class="replayUser" onclick="replayUser('+row.id+','+row.replayStatus+','+row.memberId+')"></a>'
                    return test;
                }
            },
        ]],
        //事件：当数据加载成功时触发。
        onLoadSuccess: function (data) {
            $('.user-feedback').linkbutton({text: '查看用户反馈', plain: true, iconCls: 'icon-edit'});
            $('.replayUser').linkbutton({text: '回复', plain: true, iconCls: 'icon-edit'});
        }
    })

    function lookUserFeedback(id){

        var umsSurveyInfo = JSON.stringify({'id':id});
        $.ajax({
            url:'getUmsSurveyInfoById',
            dataType:'json',
            contentType:'application/json',
            type:'post',
            data:umsSurveyInfo,
            success:function(umsSurveyInfo){
                $('#feedback-text').val(umsSurveyInfo.userFeedback);
            }
        });
        //初始化修改窗口
        $('#feedback-dialog').dialog({
            //closable 	boolean 	定义是否显示关闭按钮。
            closed: false,
            //modal 	boolean 	定义窗口是不是模态（modal）窗口。模态窗口就是在该窗口关闭之前，其父窗口不可能成为活动窗口的那种窗口。
            modal:true,
            collapsible:true,
            minimizable:true,
            maximizable:true,
            //对话框的标题文本。
            title: "用户反馈",
            buttons: [{
                text: '关闭',
                iconCls: 'icon-ok',
                handler: function () {
                    $('#feedback-dialog').dialog('close');
                }
            }]
        });
    }
    function replayUser(surveyId, replayStatus, memberId) {
        if(replayStatus == "1") {
            $.messager.alert('信息提示', "该条反馈已回复", 'info');
            return;
        }
        $('#replayMsg-text').val("");
        $('#replayTitle').val("");
        $('#msg-dialog').dialog({
            //closable 	boolean 	定义是否显示关闭按钮。
            closed: false,
            //modal 	boolean 	定义窗口是不是模态（modal）窗口。模态窗口就是在该窗口关闭之前，其父窗口不可能成为活动窗口的那种窗口。
            modal:true,
            collapsible:true,
            minimizable:true,
            maximizable:true,
            //对话框的标题文本。
            title: "回复信息",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: function (){
                    var replayMsg = $('#replayMsg-text').val();
                    var replayTitle = $('#replayTitle').val();
                    //构造pmsProductInfo对象
                    var umsReplayInfo = {'surveyId':surveyId,'memberId':memberId,'replayTitle':replayTitle,'replayMsg':replayMsg};
                    var umsReplayInfoJSON = JSON.stringify(umsReplayInfo);

                    $.ajax({
                        url:'sendUmsReplayInfo',
                        dataType:'json',
                        contentType:'application/json',
                        type:'post',
                        data:umsReplayInfoJSON,
                        success:function(data){
                            if(data.type == 'success'){
                                $.messager.alert('信息提示',data.msg,'info');
                                $('#msg-dialog').dialog('close');
                            }else{
                                $.messager.alert('信息提示',data.msg,'warning');
                            }
                        }
                    });
                }
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#msg-dialog').dialog('close');
                }
            }]
        });
    }
</script>
</html>