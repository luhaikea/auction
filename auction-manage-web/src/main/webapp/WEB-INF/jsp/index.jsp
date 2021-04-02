<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>后台管理主页</title>

    <link rel="stylesheet" type="text/css" href="../../resources/index/easyui/1.7.0/themes/bootstrap/easyui.css"/>
    <link rel="stylesheet" type="text/css" href="../../resources/index/css/index.css"/>
    <link rel="stylesheet" type="text/css" href="../../resources/index/css/icon.css"/>
    <script type="text/javascript" src="../../resources/index/js/jquery-3.1.1.min.js"></script>
    <script type="text/javascript" src="../../resources/index/easyui/1.7.0/jquery.easyui.min.js"></script>
    <script type="text/javascript" src="../../resources/index/easyui/1.7.0/locale/easyui-lang-zh_CN.js"></script>

</head>
<body class="easyui-layout">
<!-- begin of header -->
<div class="header" data-options="region:'north',border:false,split:true">
    <div class="header-left">
        <h1>后台管理系统</h1>
    </div>
    <div class="header-right">
        <p>|<strong class="easyui-tooltip" >${bmsRole.name}:${findByUsername.username}</strong>，欢迎您！|<a href="logout">安全退出</a>|</p>
    </div>
</div>
<!-- end of header -->
<!-- begin of sidebar -->
<div class="sidebar" data-options="region:'west',split:true,border:true,title:'菜单'">
    <div class="easyui-accordion" data-options="border:false,fit:true">
        <c:forEach items="${topMenuList }" var="topMenu">
            <div title="${topMenu.name }" data-options="iconCls:'${topMenu.icon }'" style="padding:5px;">
                <ul class="easyui-tree side-tree" >
                    <c:forEach items="${secondMenuList }" var="secondMenu">
                        <c:if test="${secondMenu.parentId == topMenu.id }">
                            <li iconCls="${secondMenu.icon }"><a href="javascript:void(0)"
                                                                 data-icon="${secondMenu.icon }"
                                                                 data-link="${secondMenu.url}?secondMenudId=${secondMenu.id }"
                                                                 iframe="1">${secondMenu.name }</a></li>
                        </c:if>
                    </c:forEach>
                </ul>
            </div>
        </c:forEach>
    </div>
</div>
<!-- end of sidebar -->
<!-- begin of main -->
<div class="main" data-options="region:'center'">
    <div id="tabs" class="easyui-tabs" data-options="border:false,fit:true">
        <div title="欢迎页" data-options="href:'welcome',closable:false,iconCls:'icon-tip',cls:'pd3'"></div>
    </div>
</div>
<!-- end of main -->
<!-- begin of footer -->
<div class="footer" data-options="region:'south',border:true,split:true">
    &copy; 2021 【第一帅程序猿】 All Rights Reserved
</div>
<!-- end of footer -->
<script type="text/javascript">

    $(function () {
        $('.side-tree a').bind("click", function () {
            var title = $(this).text();
            var url = $(this).attr('data-link');
            var iconCls = $(this).attr('data-icon');
            var iframe = $(this).attr('iframe') == 1 ? true : false;
            addTab(title, url, iconCls, iframe);
        });
    })

    /*iframe 链接跳转方式（true为href，false为iframe）这里全为href */
    function addTab(title, href, iconCls, iframe) {
        var tabPanel = $('#tabs');
        if (!tabPanel.tabs('exists', title)) {
            //iframe标签是一个内联框架，说白了就是用来在当前 HTML 页面中嵌入另一个文档的
            var content = '<iframe scrolling="auto" frameborder="0"  src="' + href + '" style="width:100%;height:100%;"></iframe>';
            if (iframe) {
                tabPanel.tabs('add', {
                    title: title,
                    content: content,
                    iconCls: iconCls,
                    fit: true,
                    cls: 'pd3',
                    closable: true
                });
            } else {
                tabPanel.tabs('add', {
                    title: title,
                    href: href,
                    iconCls: iconCls,
                    fit: true,
                    cls: 'pd3',
                    closable: true
                });
            }
        } else {
            tabPanel.tabs('select', title);
        }
    }

    /**
     * Name 移除菜单选项
     */
    function removeTab() {
        var tabPanel = $('#tabs');
        var tab = tabPanel.tabs('getSelected');
        if (tab) {
            var index = tabPanel.tabs('getTabIndex', tab);
            tabPanel.tabs('close', index);
        }
    }
</script>
</body>
</html>