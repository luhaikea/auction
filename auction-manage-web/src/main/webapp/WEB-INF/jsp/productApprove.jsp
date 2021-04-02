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
    <!--类目选择-->
    <div id="toolbar">
        <h3 style="color: #1F1F1F">类目选择</h3>
        <div class="toolbar-search">
            <label>一级分类:</label><select id="catalog1" class="text" style="width:100px"></select>
            <label>二级分类:</label><select id="catalog2" class="text" style="width:100px"></select>
            <label>三级分类:</label><select id="catalog3" class="text" style="width:100px"></select>
        </div>
    </div>
    <!--物品表格-->
    <table id="data-datagrid" class="easyui-datagrid" toolbar="#toolbar"></table>
    <!--物品详情-->
    <div id="details-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:900px; padding:10px;">
        <form id="details-form" method="post">
            <input type="hidden" id="details-PmsProductInfoId">
            <table id="details-attrValueTable" style="text-align: left">
                <tr>
                    <td width="80" align="right">物品名称:</td>
                    <td>
                        <input readonly="true" type="text" id="details-productName" style="height: 25px" class="text easyui-validatebox" />
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">保证金:</td>
                    <td>
                        <input readonly="true" type="text" id="details-earnest" style="height: 25px" class="text easyui-validatebox" />
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">起拍价:</td>
                    <td>
                        <input readonly="true" type="text" id="details-startingPrice" style="height: 25px" class="text easyui-validatebox" />
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">物品描述:</td>
                    <td>
                        <input readonly="true" type="text" id="details-productDesc" style="height: 25px" class="text easyui-validatebox" />
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">开始时间:</td>
                    <td>
                        <input readonly="true" class="text easyui-datetimebox" id="details-startTime" data-options="required:true,showSeconds:false" value="3/4/2010 2:3" style="width:150px">
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">结束时间:</td>
                    <td>
                        <input readonly="true" class="text easyui-datetimebox" id="details-endTime" data-options="required:true,showSeconds:false" value="3/4/2010 2:3" style="width:150px">
                    </td>
                </tr>
                <tr id="details-attradd">
                    <td></td><td><a href="#" style="width: 120px" id="details-addattr-btn" class="easyui-linkbutton">平台属性</a></td>
                </tr>
                <tr id="details-saleattradd">
                    <td></td><td><a href="#" style="width: 120px" id="details-addsaleattr-btn" class="easyui-linkbutton">销售属性</a></td>
                    <td></td><td><a href="#" style="width: 120px" id="details-deletesaleattr-btn" class="easyui-linkbutton" >销售属性</a></td>
                </tr>
                <tr>
                    <td></td><td><a href="#" style="width: 120px" class="easyui-linkbutton">物品图片</a></td>
                </tr>

            </table>
            <div id="details-images" style="margin-left: 95px">
            </div>
        </form>
    </div>
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
            $('#data-datagrid').datagrid('reload', {catalog3Id:record.id});
        }
    });
    //初始化表格  这个表格应该只能是post请求
    $('#data-datagrid').datagrid({
        //这里得到的应该是未审批的物品
        url:'productListNoApprove',
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
            //formatter单元格的格式化函数，需要三个参数：value：字段的值。rowData：行的记录数据。rowIndex：行的索引。
            { field:'catalog3Id',title:'操作',width:100,formatter:function(value,row,index){
                    var test = '<a class="view-details" onclick="viewDetails('+row.id+')"></a>'
                    return test;}
            },
            { field:'buyer',title:'审批',width:100,formatter:function(value,row,index){
                    var test = '<a class="approve" onclick="approve('+row.id+')"></a>'
                    return test;}
            },
        ]],
        //事件：当数据加载成功时触发。
        onLoadSuccess:function(data){
            $('.view-details').linkbutton({text:'查看详情',plain:true,iconCls:'icon-edit'});
            $('.approve').linkbutton({text:'同意上架',plain:true,iconCls:'icon-edit'});
        }
    })

    function viewDetails(pmsProductId){

        var pmsAttrInfoListAll = [];
        //先通过三级分类获得所有平台属性信息
        var catalog3Id = $('#catalog3').combobox('getValue');
        var catalog3IdJson = JSON.stringify({'catalog3Id':catalog3Id});
        $.ajax({
            url:'attrInfoListAll',
            dataType:'json',
            contentType:'application/json',
            type:'post',
            data:catalog3IdJson,
            success:function(pmsAttrInfoList){
                pmsAttrInfoListAll = pmsAttrInfoList;
            }
        });

        var pmsProduct = JSON.stringify({'id':pmsProductId});
        //得到要修改的物品信息
        $.ajax({
            url:'getPmsProductInfoById',
            dataType:'json',
            contentType:'application/json',
            type:'post',
            data:pmsProduct,
            success:function(data){
                //填充基本信息
                $("#details-PmsProductInfoId").val(data.id);
                $("#details-productName").val(data.productName);
                $("#details-earnest").val(data.earnest);
                $("#details-startingPrice").val(data.startingPrice);
                $("#details-productDesc").val(data.productDesc);
                $('#details-startTime').datetimebox('setValue', data.startTime);
                $('#details-endTime').datetimebox('setValue', data.endTime);
                //填充平台属性
                //先清空
                $("#details-attrValueTable tr[name='details-pmsAttrInfoRemove']").remove();
                var pmsProductAttrValues = data.pmsProductAttrValues;
                for(var i=0;i<pmsAttrInfoListAll.length;i++){

                    //该平台属性选中的属性值Id
                    var selectValueId;
                    for(var k=0; k<pmsProductAttrValues.length; k++){
                        if(pmsAttrInfoListAll[i].id == pmsProductAttrValues[k].attrId){
                            selectValueId = pmsProductAttrValues[k].valueId;
                        }
                    }

                    var str = '<tr name="details-pmsAttrInfoRemove"><td width="80" align="right" name="details-pmsAttrInfoName" value='+pmsAttrInfoListAll[i].id+'>'+pmsAttrInfoListAll[i].attrName+'</td><td><select disabled="disabled" name="details-pmsAttrInfoValue" class="text easyui-combobox" style="width:200px; height: 25px" >';
                    var attrValueList = pmsAttrInfoListAll[i].attrValueList;
                    for (var j=0;j<attrValueList.length;j++){
                        if(attrValueList[j].id == selectValueId){  //选中
                            str = str +'<option selected = "selected" value='+attrValueList[j].id+'>'+attrValueList[j].valueName+'</option>';
                        } else{
                            str = str +'<option value='+attrValueList[j].id+'>'+attrValueList[j].valueName+'</option>';
                        }
                    }
                    str = str + '</select></td></tr>'
                    $("#details-attradd").after(str);
                }
                //填充销售属性信息
                //先清空
                $("#details-attrValueTable tr[name='details-saleattrrow']").remove();
                var pmsProductSaleAttrs = data.pmsProductSaleAttrs;
                for(var i=0; i<pmsProductSaleAttrs.length; i++){
                    var pmsProductSaleAttr = pmsProductSaleAttrs[i];
                    $("#details-saleattradd").after('<tr name="details-saleattrrow">\n' +
                        '                    <td width="90" align="right">销售属性名称:</td>\n' +
                        '                    <td>\n' +
                        '                        <input readonly="true" type="text" style="height: 25px" name="details-saleattrname" class="text easyui-validatebox" value='+pmsProductSaleAttr.saleAttrName+' data-options="required:true" />\n' +
                        '                    </td>\n' +
                        '                    <td width="80" align="right">销售属性值:</td>\n' +
                        '                    <td>\n' +
                        '                        <input readonly="true" type="text" style="height: 25px" name="details-saleattrvalue" class="text easyui-validatebox" value='+pmsProductSaleAttr.saleAttrValue+' data-options="required:true" />\n' +
                        '                    </td>\n' +
                        '                </tr>');
                }
                //填充图片信息
                //先清空以前的图片
                $("#details-images div").remove();
                var pmsProductImages = data.pmsProductImages;
                for(var i=0;i<pmsProductImages.length;i++){
                    var pmsProductImage = pmsProductImages[i];
                    $("#details-images").append('<div style="float: left;border-style: solid; padding: 5px; border-color: #0000FF">\n' +
                        '                    <img src='+pmsProductImage.imgUrl+' height="100px" width="120"></div>');
                }

            }
        });
        //初始化修改窗口
        $('#details-dialog').dialog({
            //closable 	boolean 	定义是否显示关闭按钮。
            closed: false,
            //modal 	boolean 	定义窗口是不是模态（modal）窗口。模态窗口就是在该窗口关闭之前，其父窗口不可能成为活动窗口的那种窗口。
            modal:true,
            collapsible:true,
            minimizable:true,
            maximizable:true,
            //对话框的标题文本。
            title: "物品详情",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler:  function () {
                    $('#details-dialog').dialog('close');
                }
            }]
        });
    }
    function approve(productId){

        var pmsProduct = JSON.stringify({'id':productId});
        $.ajax({
            url:'aggreeApprove',
            dataType:'json',
            contentType:'application/json',
            type:'post',
            data:pmsProduct,
            success:function(data){
                if(data.type == 'success'){
                    //刷新物品列表
                    var catalog3Id = $('#catalog3').combobox('getValue');
                    $('#data-datagrid').datagrid('reload', {catalog3Id:catalog3Id});
                    $.messager.alert('信息提示',data.msg,'info');
                }else{
                    $.messager.alert('信息提示',data.msg,'warning');
                }
            }
        });

    }
</script>
</html>