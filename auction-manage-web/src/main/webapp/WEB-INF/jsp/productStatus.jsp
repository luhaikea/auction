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
</head>
<body class="easyui-layout">
<div class="easyui-layout" data-options="fit:true">
    <div id="toolbar">
        <div class="toolbar-search">
            <label>物品状态选择:</label><select id="productStatusSelect" class="text" style="width:100px"></select>
        </div>
    </div>
    <!--物品表格-->
    <table id="data-datagrid" class="easyui-datagrid" toolbar="#toolbar"></table>
</div>
<!--订单列表-->
<div id="order-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:100%;height:100%;padding:10px;">
    <table id="order-datagrid" class="easyui-datagrid"></table>
</div>

</body>
<script type="text/javascript">

    $('#productStatusSelect').combobox({
        url:'getProductStatusSelect',
        valueField:'id',
        textField:'name',
        onSelect: function(record){
            //var url = 'attrInfoList?catalog3Id='+record.id;
            $('#data-datagrid').datagrid('reload', {productStatus:record.id});
        }
    });
    //初始化表格  这个表格应该只能是post请求
    $('#data-datagrid').datagrid({
        //这里得到的应该是未审批的物品
        url:'productStatus',
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
            { field:'productName',title:'物品名称',width:100,sortable:true},
            { field:'productDesc',title:'商品描述',width:100,sortable:true},
            { field:'earnest',title:'保证金',width:100,sortable:true},
            { field:'startingPrice',title:'起拍价',width:100,sortable:true},
            { field:'startTime',title:'起拍时间',width:100,sortable:true},
            { field:'endTime',title:'结束时间',width:100,sortable:true},
            { field:'buyer',title:'获拍用户',width:100,sortable:true},
            //formatter单元格的格式化函数，需要三个参数：value：字段的值。rowData：行的记录数据。rowIndex：行的索引。
            { field:'catalog3Id',title:'操作',width:100,formatter:function(value,row,index){
                    var test = '<a class="view-order" onclick="viewOrder('+row.id+')"></a>'
                    return test;
                 }
            }
        ]],
        //事件：当数据加载成功时触发。
        onLoadSuccess:function(data){
            $('.view-order').linkbutton({text:'查看订单',plain:true,iconCls:'icon-edit'});
        }
    })

    function viewOrder(pmsProductId){
        //初始化修改窗口
        $('#order-dialog').dialog({
            closed: false,
            modal:true,
            collapsible:true,
            minimizable:true,
            maximizable:true,
            //对话框的标题文本。
            title: "订单列表",
            buttons: [{
                text: '关闭',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#order-dialog').dialog('close');
                }
            }]
        });

        $('#order-datagrid').datagrid({
            //这里得到的应该是未审批的物品
            url:'orderList?pmsProductId='+pmsProductId,
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
                { field:'orderSn',title:'订单编号',width:100,sortable:true},
                { field:'productId',title:'商品ID',width:100,sortable:true},
                { field:'memberId',title:'用户ID',width:100,sortable:true},
                { field:'createTime',title:'下单时间',width:100,sortable:true},
                { field:'bidPrice',title:'竞拍价格',width:100,sortable:true},
                { field:'earnestStatus',title:'保证金状态',width:100,sortable:true},
                { field:'finalPayStatus',title:'尾款状态',width:100,sortable:true},
                { field:'auctionStatus',title:'竞拍状态',width:100,sortable:true}
            ]],
        })

    }
</script>
</html>