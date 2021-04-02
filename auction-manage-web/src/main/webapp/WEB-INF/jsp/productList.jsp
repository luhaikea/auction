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
            <a href="#" id="add-btn" class="easyui-linkbutton" iconCls="icon-add">添加物品</a>
        </div>
    </div>
    <!--物品表格-->
    <table id="data-datagrid" class="easyui-datagrid" toolbar="#toolbar"></table>
    <!--添加窗口-->
    <div id="add-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:900px; padding:10px;">
        <form id="add-form" method="post">
            <table id="attrValueTable" style="text-align: left">
                <tr>
                    <td width="80" align="right">物品名称:</td>
                    <td>
                        <input type="text" id="productName" style="height: 25px" class="text easyui-validatebox" data-options="required:true, missingMessage:'请填写物品名称'" />
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">保证金:</td>
                    <td>
                        <input type="text" id="earnest" style="height: 25px" class="text easyui-validatebox" data-options="required:true, missingMessage:'请填写保证金'" />
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">起拍价:</td>
                    <td>
                        <input type="text" id="startingPrice" style="height: 25px" class="text easyui-validatebox" data-options="required:true, missingMessage:'请填写起拍价'" />
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">物品描述:</td>
                    <td>
                        <input type="text" id="productDesc" style="height: 25px" class="text easyui-validatebox" data-options="required:true, missingMessage:'请填写物品描述'" />
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">开始时间:</td>
                    <td>
                        <input class="text easyui-datetimebox" id="startTime" data-options="required:true,showSeconds:false" value="3/4/2010 2:3" style="width:150px">
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">结束时间:</td>
                    <td>
                        <input class="text easyui-datetimebox" id="endTime" data-options="required:true,showSeconds:false" value="3/4/2010 2:3" style="width:150px">
                    </td>
                </tr>
                <tr id="attradd">
                    <td></td><td><a href="#" style="width: 120px" id="addattr-btn" class="easyui-linkbutton" iconCls="icon-add">添加平台属性</a></td>
                </tr>
                <tr id="saleattradd">
                    <td></td><td><a href="#" style="width: 120px" id="addsaleattr-btn" class="easyui-linkbutton" iconCls="icon-add">添加销售属性</a></td>
                    <td></td><td><a href="#" style="width: 120px" id="deletesaleattr-btn" class="easyui-linkbutton" iconCls="icon-add">删除销售属性</a></td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <input id="img" name="image" style="width:300px" >
                    </td>
                </tr>
            </table>
            <div id="images" style="margin-left: 95px">
            </div>
        </form>
    </div>
    <!--修改物品窗口-->
    <div id="edit-dialog" class="easyui-dialog" data-options="closed:true,iconCls:'icon-save'" style="width:900px; padding:10px;">
        <form id="edit-form" method="post">
            <input type="hidden" id="edit-PmsProductInfoId">
            <table id="edit-attrValueTable" style="text-align: left">
                <tr>
                    <td width="80" align="right">物品名称:</td>
                    <td>
                        <input type="text" id="edit-productName" style="height: 25px" class="text easyui-validatebox" data-options="required:true, missingMessage:'请填写物品名称'" />
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">保证金:</td>
                    <td>
                        <input type="text" id="edit-earnest" style="height: 25px" class="text easyui-validatebox" data-options="required:true, missingMessage:'请填写保证金'" />
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">起拍价:</td>
                    <td>
                        <input type="text" id="edit-startingPrice" style="height: 25px" class="text easyui-validatebox" data-options="required:true, missingMessage:'请填写起拍价'" />
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">物品描述:</td>
                    <td>
                        <input type="text" id="edit-productDesc" style="height: 25px" class="text easyui-validatebox" data-options="required:true, missingMessage:'请填写物品描述'" />
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">开始时间:</td>
                    <td>
                        <input class="text easyui-datetimebox" id="edit-startTime" data-options="required:true,showSeconds:false" value="3/4/2010 2:3" style="width:150px">
                    </td>
                </tr>
                <tr>
                    <td width="80" align="right">结束时间:</td>
                    <td>
                        <input class="text easyui-datetimebox" id="edit-endTime" data-options="required:true,showSeconds:false" value="3/4/2010 2:3" style="width:150px">
                    </td>
                </tr>
                <tr id="edit-attradd">
                    <td></td><td><a href="#" style="width: 120px" id="edit-addattr-btn" class="easyui-linkbutton" iconCls="icon-add">修改平台属性</a></td>
                </tr>
                <tr id="edit-saleattradd">
                    <td></td><td><a href="#" style="width: 120px" id="edit-addsaleattr-btn" class="easyui-linkbutton" iconCls="icon-add">添加销售属性</a></td>
                    <td></td><td><a href="#" style="width: 120px" id="edit-deletesaleattr-btn" class="easyui-linkbutton" iconCls="icon-add">删除销售属性</a></td>
                </tr>
                <tr>
                    <td></td>
                    <td>
                        <input id="edit-img" name="edit-image" style="width:300px" >
                    </td>
                </tr>
            </table>
            <div id="edit-images" style="margin-left: 95px">
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
        url:'productList',
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

                    var test = '<a class="authority-edit" onclick="editProductInfo('+row.id+')"></a>'
                    return test;
                }},
        ]],
        //事件：当数据加载成功时触发。
        onLoadSuccess:function(data){
            $('.authority-edit').linkbutton({text:'修改',plain:true,iconCls:'icon-edit'});
        }
    })

    //添加物品按钮监听 打开物品添加窗口
    $("#add-btn").click(function(){
        //判断是否选中三级分类
        var catalog3Val = $('#catalog3').combobox('getValue');
        if(!catalog3Val){
            $.messager.alert("消息提醒","请选择三级分类!","warning");
            return;
        }

        //先清空上一次添加留下来的原素
        //清空平台属性
        $("#attrValueTable tr[name='pmsAttrInfoRemove']").remove();
        sign = 0;
        //清空销售属性
        $("#attrValueTable tr[name='saleattrrow']").remove();
        //清空图片
        $("#images div").remove();

        $('#add-dialog').dialog({
            closed: false,
            modal:true,
            collapsible:true,
            minimizable:true,
            maximizable:true,
            inline:true,
            title: "添加物品",
            buttons: [{
                text: '确定',
                iconCls: 'icon-ok',
                handler: add,
            }, {
                text: '取消',
                iconCls: 'icon-cancel',
                handler: function () {
                    $('#add-dialog').dialog('close');
                }
            }]
        });
    });
    //用于判断是否已经添加平台属性
    var sign = 0;
    //监听添加平台属性按钮
    $("#addattr-btn").click(function (){
        //首先要得到此三级分类下的平台属性和值
        if(sign!= 0){
            return ;
        }
        var catalog3Id = $('#catalog3').combobox('getValue');
        var catalog3IdJson = JSON.stringify({'catalog3Id':catalog3Id});
        //初始化编辑框
        $.ajax({
            url:'attrInfoListAll',
            dataType:'json',
            contentType:'application/json',
            type:'post',
            data:catalog3IdJson,
            success:function(pmsAttrInfoList){

                for(var i=0;i<pmsAttrInfoList.length;i++){

                    var str = '<tr name="pmsAttrInfoRemove"><td width="80" align="right" name="pmsAttrInfoName" value='+pmsAttrInfoList[i].id+'>'+pmsAttrInfoList[i].attrName+'</td><td><select name="pmsAttrInfoValue" class="text easyui-combobox" style="width:200px; height: 25px" >';
                    str = str +'<option value="0">请选择</option>';
                    var attrValueList = pmsAttrInfoList[i].attrValueList;
                    for (var j=0;j<attrValueList.length;j++){
                        str = str +'<option value='+attrValueList[j].id+'>'+attrValueList[j].valueName+'</option>';
                    }
                    str = str + '</select></td></tr>'
                    $("#attradd").after(str);
                }
                sign = 1;
            }
        });
    })
    //监听添加销售属性按钮 会打开一组销售属性名称和销售属性值的输入框
    $("#addsaleattr-btn").click(function(){
        $("#saleattradd").after('<tr name="saleattrrow">\n' +
            '                    <td width="90" align="right">销售属性名称:</td>\n' +
            '                    <td>\n' +
            '                        <input type="text" style="height: 25px" name="saleattrname" class="text easyui-validatebox" data-options="required:true" />\n' +
            '                    </td>\n' +
            '                    <td width="80" align="right">销售属性值:</td>\n' +
            '                    <td>\n' +
            '                        <input type="text" style="height: 25px" name="saleattrvalue" class="text easyui-validatebox" data-options="required:true" />\n' +
            '                    </td>\n' +
            '                </tr>');
    })
    //监听删除销售属性按钮 会删除一组销售属性名称和销售属性值的输入框
    $("#deletesaleattr-btn").click(function(){
        //添加属性过滤器保证 属性名称: 行不被删除
        $("#attrValueTable tr[name='saleattrrow']:last").remove();
    })
    //初始化图片上传框
    $('#img').filebox({
        prompt:'图片',
        buttonText:'选择图片',
        multiple:true,
        accept:'image/png,image/gif,image/jpeg',
        //不换行float: left;
        onChange: function (newValue, oldValue){

            var formdata = new FormData();
            //easyui 改变了DOM 结构  ilebox_file_id_1 这个ID 是easyui 自己创建的input 便签. 这里面是真正保存文件的地方
            var imgs = document.getElementById('filebox_file_id_1').files;
            for(var i=0;i<imgs.length;i++){
                formdata.append("theFiles",imgs[i]);
            }

            $.ajax({
                url:"fileUpload",
                type:"post",
                data: formdata,
                contentType: false,
                processData: false,
                success: function(data) {
                    if (data.type == "success") {
                        var imgUrls = data.imgUrls;
                        for(var i=0;i<imgUrls.length;i++){
                            $("#images").append('<div style="float: left;border-style: solid; padding: 5px; border-color: #0000FF">\n' +
                                '                    <img src='+imgUrls[i]+' height="100px" width="120"><br>\n' +
                                '                    <a href="#" style="width: 120px" name="imgdeletebtn" class="easyui-linkbutton" iconCls="icon-cross">删除图片</a>\n' +
                                '                </div>');
                        }

                        $("a[name='imgdeletebtn']").bind("click",function (){
                            //要删除的的是包含图片和图片散出按钮的那个div[他们的父容器]
                            $(this).parent().remove();
                        });

                    } else {
                        alert(data.msg);
                    }
                },
                error:function(data) {
                    alert(data.type);
                }
            });
        }
    })
    //添加物品方法
    function add(){
        //基本信息
        var productName = $('#productName').val();
        var earnest = $('#earnest').val();
        var startingPrice = $('#startingPrice').val();
        var productDesc = $('#productDesc').val();
        var startTime = $('#startTime').val();
        var endTime = $('#endTime').val();
        var catalog3Id = $('#catalog3').combobox('getValue');

        //销售属性
        var saleattrnames = [];
        $('input[name="saleattrname"]').each(function(){
            saleattrnames.push($(this).val());
        });
        var saleattrvalues = [];
        $('input[name="saleattrvalue"]').each(function(){
            saleattrvalues.push($(this).val());
        });
        var pmsProductSaleAttrs = [];
        for(var i=0;i<saleattrnames.length;i++){
            pmsProductSaleAttrs.push({"saleAttrName":saleattrnames[i],"saleAttrValue":saleattrvalues[i]});
        }

        //图片
        var pmsProductImages = [];
        $("#images img").each(function (){
            pmsProductImages.push({"imgUrl":$(this).attr("src")});
        });

        //平台属性
        var pmsProductAttrValues = [];
        var attrIds = [];
        $('td[name="pmsAttrInfoName"]').each(function (){
            attrIds.push($(this).attr("value"));
        });
        var valueIds = [];
        $('select[name="pmsAttrInfoValue"]').each(function (){
            valueIds.push($(this).val());
        });
        for(var i=0;i<attrIds.length;i++){
            pmsProductAttrValues.push({"attrId":attrIds[i],"valueId":valueIds[i]});
        }

        //构造pmsProductInfo对象
        var pmsProductInfo = {'productName':productName,'earnest':earnest,'startingPrice':startingPrice,'productDesc':productDesc,'catalog3Id':catalog3Id,'startTime':startTime,'endTime':endTime,'pmsProductImages':pmsProductImages,'buyer':'-1','pmsProductAttrValues':pmsProductAttrValues,'pmsProductSaleAttrs':pmsProductSaleAttrs};
        var pmsProductInfoJSON = JSON.stringify(pmsProductInfo);

        $.ajax({
            url:'savePmsProductInfo',
            dataType:'json',
            contentType:'application/json',
            type:'post',
            data:pmsProductInfoJSON,
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

    //修改物品
    function editProductInfo(pmsProductId){

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
                $("#edit-PmsProductInfoId").val(data.id);
                $("#edit-productName").val(data.productName);
                $("#edit-earnest").val(data.earnest);
                $("#edit-startingPrice").val(data.startingPrice);
                $("#edit-productDesc").val(data.productDesc);
                $('#edit-startTime').datetimebox('setValue', data.startTime);
                $('#edit-endTime').datetimebox('setValue', data.endTime);
                //填充平台属性
                //先清空
                $("#edit-attrValueTable tr[name='edit-pmsAttrInfoRemove']").remove();
                var pmsProductAttrValues = data.pmsProductAttrValues;
                for(var i=0;i<pmsAttrInfoListAll.length;i++){

                    //该平台属性选中的属性值Id
                    var selectValueId;
                    for(var k=0; k<pmsProductAttrValues.length; k++){
                        if(pmsAttrInfoListAll[i].id == pmsProductAttrValues[k].attrId){
                            selectValueId = pmsProductAttrValues[k].valueId;
                        }
                    }

                    var str = '<tr name="edit-pmsAttrInfoRemove"><td width="80" align="right" name="edit-pmsAttrInfoName" value='+pmsAttrInfoListAll[i].id+'>'+pmsAttrInfoListAll[i].attrName+'</td><td><select name="edit-pmsAttrInfoValue" class="text easyui-combobox" style="width:200px; height: 25px" >';
                    var attrValueList = pmsAttrInfoListAll[i].attrValueList;
                    for (var j=0;j<attrValueList.length;j++){
                        if(attrValueList[j].id == selectValueId){  //选中
                            str = str +'<option selected = "selected" value='+attrValueList[j].id+'>'+attrValueList[j].valueName+'</option>';
                        } else{
                            str = str +'<option value='+attrValueList[j].id+'>'+attrValueList[j].valueName+'</option>';
                        }
                    }
                    str = str + '</select></td></tr>'
                    $("#edit-attradd").after(str);
                }
                //填充销售属性信息
                //先清空
                $("#edit-attrValueTable tr[name='edit-saleattrrow']").remove();
                var pmsProductSaleAttrs = data.pmsProductSaleAttrs;
                for(var i=0; i<pmsProductSaleAttrs.length; i++){
                    var pmsProductSaleAttr = pmsProductSaleAttrs[i];
                    $("#edit-saleattradd").after('<tr name="edit-saleattrrow">\n' +
                        '                    <td width="90" align="right">销售属性名称:</td>\n' +
                        '                    <td>\n' +
                        '                        <input type="text" style="height: 25px" name="edit-saleattrname" class="text easyui-validatebox" value='+pmsProductSaleAttr.saleAttrName+' data-options="required:true" />\n' +
                        '                    </td>\n' +
                        '                    <td width="80" align="right">销售属性值:</td>\n' +
                        '                    <td>\n' +
                        '                        <input type="text" style="height: 25px" name="edit-saleattrvalue" class="text easyui-validatebox" value='+pmsProductSaleAttr.saleAttrValue+' data-options="required:true" />\n' +
                        '                    </td>\n' +
                        '                </tr>');
                }
                //填充图片信息
                //先清空以前的图片
                $("#edit-images div").remove();
                var pmsProductImages = data.pmsProductImages;
                for(var i=0;i<pmsProductImages.length;i++){
                    var pmsProductImage = pmsProductImages[i];
                    $("#edit-images").append('<div style="float: left;border-style: solid; padding: 5px; border-color: #0000FF">\n' +
                        '                    <img src='+pmsProductImage.imgUrl+' height="100px" width="120"><br>\n' +
                        '                    <a href="#" style="width: 120px" name="edit-imgdeletebtn" class="easyui-linkbutton" iconCls="icon-cross">删除图片</a>\n' +
                        '                </div>');
                }

                $("a[name='edit-imgdeletebtn']").bind("click",function (){
                    //要删除的的是包含图片和图片散出按钮的那个div[他们的父容器]
                    $(this).parent().remove();
                });


            }
        });
        //初始化修改窗口
        $('#edit-dialog').dialog({
            //closable 	boolean 	定义是否显示关闭按钮。
            closed: false,
            //modal 	boolean 	定义窗口是不是模态（modal）窗口。模态窗口就是在该窗口关闭之前，其父窗口不可能成为活动窗口的那种窗口。
            modal:true,
            collapsible:true,
            minimizable:true,
            maximizable:true,
            //对话框的标题文本。
            title: "修改物品",
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
    //监听修改添加销售属性按钮 会打开一组销售属性名称和销售属性值的输入框
    $("#edit-addsaleattr-btn").click(function(){
        $("#edit-saleattradd").after('<tr name="edit-saleattrrow">\n' +
            '                    <td width="90" align="right">销售属性名称:</td>\n' +
            '                    <td>\n' +
            '                        <input type="text" style="height: 25px" name="edit-saleattrname" class="text easyui-validatebox" data-options="required:true" />\n' +
            '                    </td>\n' +
            '                    <td width="80" align="right">销售属性值:</td>\n' +
            '                    <td>\n' +
            '                        <input type="text" style="height: 25px" name="edit-saleattrvalue" class="text easyui-validatebox" data-options="required:true" />\n' +
            '                    </td>\n' +
            '                </tr>');
    })
    //监听修改删除销售属性按钮 会删除一组销售属性名称和销售属性值的输入框
    $("#edit-deletesaleattr-btn").click(function(){
        //添加属性过滤器保证 属性名称: 行不被删除
        $("#edit-attrValueTable tr[name='edit-saleattrrow']:last").remove();
    })
    //初始化修改图片上传窗口
    $('#edit-img').filebox({
        prompt:'图片',
        buttonText:'选择图片',
        multiple:true,
        accept:'image/png,image/gif,image/jpeg',
        //不换行float: left;
        onChange: function (newValue, oldValue){

            var formdata = new FormData();
            //easyui 改变了DOM 结构  ilebox_file_id_2 这个ID 是easyui 自己创建的input 便签. 这里面是真正保存文件的地方
            var imgs = document.getElementById('filebox_file_id_2').files;
            for(var i=0;i<imgs.length;i++){
                formdata.append("theFiles",imgs[i]);
            }
            $.ajax({
                url:"fileUpload",
                type:"post",
                data: formdata,
                contentType: false,
                processData: false,
                success: function(data) {
                    if (data.type == "success") {
                        var imgUrls = data.imgUrls;
                        for(var i=0;i<imgUrls.length;i++){
                            $("#edit-images").append('<div style="float: left;border-style: solid; padding: 5px; border-color: #0000FF">\n' +
                                '                    <img src='+imgUrls[i]+' height="100px" width="120"><br>\n' +
                                '                    <a href="#" style="width: 120px" name="edit-imgdeletebtn" class="easyui-linkbutton" iconCls="icon-cross">删除图片</a>\n' +
                                '                </div>');
                        }

                        $("a[name='edit-imgdeletebtn']").bind("click",function (){
                            //要删除的的是包含图片和图片散出按钮的那个div[他们的父容器]
                            $(this).parent().remove();
                        });

                    } else {
                        alert(data.msg);
                    }
                },
                error:function(data) {
                    alert(data.type);
                }
            });
        }
    })
    //保存修改物品数据的方法
    function edit(){
        //基本信息
        var edit_PmsProductInfoId =$('#edit-PmsProductInfoId').val();
        var edit_productName = $('#edit-productName').val();
        var edit_earnest = $('#edit-earnest').val();
        var edit_startingPrice = $('#edit-startingPrice').val();
        var edit_productDesc = $('#edit-productDesc').val();
        var edit_startTime = $('#edit-startTime').val();
        var edit_endTime = $('#edit-endTime').val();
        var catalog3Id = $('#catalog3').combobox('getValue');

        //销售属性
        var saleattrnames = [];
        $('input[name="edit-saleattrname"]').each(function(){
            saleattrnames.push($(this).val());
        });
        var saleattrvalues = [];
        $('input[name="edit-saleattrvalue"]').each(function(){
            saleattrvalues.push($(this).val());
        });
        var pmsProductSaleAttrs = [];
        for(var i=0;i<saleattrnames.length;i++){
            pmsProductSaleAttrs.push({"saleAttrName":saleattrnames[i],"saleAttrValue":saleattrvalues[i]});
        }

        //图片
        var pmsProductImages = [];
        $("#edit-images img").each(function (){
            pmsProductImages.push({"imgUrl":$(this).attr("src")});
        });

        //平台属性
        var pmsProductAttrValues = [];
        var attrIds = [];
        $('td[name="edit-pmsAttrInfoName"]').each(function (){
            attrIds.push($(this).attr("value"));
        });
        var valueIds = [];
        $('select[name="edit-pmsAttrInfoValue"]').each(function (){
            valueIds.push($(this).val());
        });
        for(var i=0;i<attrIds.length;i++){
            pmsProductAttrValues.push({"attrId":attrIds[i],"valueId":valueIds[i]});
        }

        //构造pmsProductInfo对象
        var pmsProductInfo = {'id':edit_PmsProductInfoId,'productName':edit_productName,'earnest':edit_earnest,'startingPrice':edit_startingPrice,'productDesc':edit_productDesc,'catalog3Id':catalog3Id,'startTime':edit_startTime,'endTime':edit_endTime,'pmsProductImages':pmsProductImages,'pmsProductAttrValues':pmsProductAttrValues,'pmsProductSaleAttrs':pmsProductSaleAttrs};
        var pmsProductInfoJSON = JSON.stringify(pmsProductInfo);

        $.ajax({
            url:'editPmsProductInfo',
            dataType:'json',
            contentType:'application/json',
            type:'post',
            data:pmsProductInfoJSON,
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


</html>