<#assign CACHE_VERSION = "v=1.0.13">
<#assign config_id = "${id!''}">
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width">
    <title>积木报表—免费可视化Web报表工具</title>

    <script>
        let base = "${base}";
        let baseFull = "${base}"+"${customPrePath}";
        reportConfigString = '${reportConfig}';
        /**
         * 获取url参数
         */
        function getRequestUrl() {
            var url = location.search;
            var theRequest = new Object();
            if (url.indexOf("?") != -1) {
                var str = url.substr(1);
                strs = str.split("&");
                for(var i = 0; i < strs.length; i++) {
                    theRequest[strs[i].split("=")[0]]=decodeURI(strs[i].split("=")[1]);
                }
            }
            return theRequest;
        }

        let token = getRequestUrl().token;
        if (token == "" || token == null){
            token = window.localStorage.getItem('JmReport-Access-Token');
        }
        window.localStorage.setItem('JmReport-Access-Token',token);

        //update-begin---author:wangshuai ---date:20220708  for：[JMREP-2661]多租户权限集成------------
        let tenantId = getRequestUrl().tenantId;
        if("" == tenantId || null == tenantId){
            tenantId = window.localStorage.getItem('JmReport-Tenant-Id');
        }
        window.localStorage.setItem('JmReport-Tenant-Id',tenantId);
        //update-end---author:wangshuai ---date:20220708  for：[JMREP-2661]多租户权限集成------------
    </script>
    <!--引入公共资源-->
    <#include "./common/resource.ftl">
    <link rel="stylesheet" href="${base}${customPrePath}/jmreport/desreport_/corelib/report.css?${CACHE_VERSION}">
    <link rel="stylesheet" href="${base}${customPrePath}/jmreport/desreport_/corelib/jmsheet.css?${CACHE_VERSION}">
    <link rel="shortcut icon" href="${base}${customPrePath}/jmreport/desreport_/corelib/logo.png?${CACHE_VERSION}" type="image/x-ico">
    <script src="${base}${customPrePath}/jmreport/desreport_/corelib/jmsheet.js?${CACHE_VERSION}"></script>
    <script src="${base}${customPrePath}/jmreport/desreport_/corelib/locale/zh-cn.js?${CACHE_VERSION}"></script>
    <script src="${base}${customPrePath}/jmreport/desreport_/jquery/jquery-3.4.1.min.js"></script>
    <script src="${base}${customPrePath}/jmreport/desreport_/cdn/html2canvas/html2canvas.min.js"></script>
    <script src="${base}${customPrePath}/jmreport/desreport_/cdn/html2canvas/canvas2image.js"></script>
    <script src="${base}${customPrePath}/jmreport/desreport_/js/config/chart_type_list.js?${CACHE_VERSION}"></script>
    <script src="${base}${customPrePath}/jmreport/desreport_/js/config/map_type_list.js?${CACHE_VERSION}"></script>
    <!--引入自定义组件-->
    <#include "./template/index.ftl">
    <style>
        .ivu-form-item {
            margin-bottom: 10px;
            vertical-align: top;
            zoom: 1;
        }
        .ivu-menu-vertical.ivu-menu-light:after {
            content: '';
            display: block;
            width: 1px;
            height: 100%;
            background: #ffffff;
            position: absolute;
            top: 0;
            bottom: 0;
            right: 0;
            z-index: 1;
        }
        .vertical-center-modal{
            width: 100%;
            height: 100%;
        }
        .ivu-select-dropdown.ivu-transfer-no-max-height {
            max-height: none;
            margin-left: 142px;
        }

        .ivurow{
            position: relative;
            margin-left: 0;
            margin-right: 0;
            margin-bottom: 5px;
            height: auto;
            zoom: 1;
            display: -webkit-inline-box;
        }
        .ivu-col > div.chart-active{
            border-color: blue;
            box-shadow: 0px 0px 8px blue;
        }
        .ivu-col > div.chart-selected{
            border-color: blue !important;
            box-shadow: 0px 0px 8px blue;
        }

        .chart-modal-content .ivu-tabs-tabpane{
            padding: 0 0 0 8px;
        }
        #dataTree{
            margin-left: 25px;
        }
        .no-allowed{
            cursor: not-allowed;
           /* pointer-events: none;*/
        }

        .no-allowed:after {
            position: absolute;
            width: 200px;
            height: 150px;
            top: 0;
            left: 0;
            content: "";
            background: #fff;
            opacity: 0.65;
            z-index: 5;
            filter: alpha(opacity=40);
        }
        .colorPicker{
            width: 200%;

        }
        .colorPicker .ivu-color-picker-input{
            width: 20px;
            height: 20px;
        }
        .colorPicker .ivu-color-picker-color{
            margin-left: -5px;
            margin-top: -2px;
        }
        .iSelect{
            width: 145px !important;
        }
        .datastyle{
            margin-bottom: 10px;
        }
        .blockDiv{
            margin-left: 31px;margin-top: -10px;font-size: 12px;color: #000;
        }

        .jm-rp-left-container{
            position: absolute;
            top: 0;
            left: 0;
        }
        .jm-rp-right-container{
            position: absolute;
            top: 0;
            right: 0;
        }
        .jm-rp-designer{
            position: absolute;
            left: 220px;
            width: calc(100% - 470px)
        }
        .jm-rp-designer.left{
            left: 20px;
            width: calc(100% - 260px)
        }
        .jm-rp-designer.right{
            left: 220px;
            width: calc(100% - 240px)
        }
        .jm-rp-designer.all{
            left: 20px;
            width: calc(100% - 40px)
        }
        [v-cloak] {
            display: none;
        }
        .ivu-poptip-popper {
            min-width: 100px;
        }
        .ivu-poptip-body-content-word-wrap {
            text-align: center;
        }

        /*加载效果*/
        .zindex-top{
            z-index: 999;
        }
        .zindex-top .ivu-icon-ios-loading{
            animation: cycle-spin 1s linear infinite;
        }
        @keyframes cycle-spin {
            from { transform: rotate(0deg);}
            50%  { transform: rotate(180deg);}
            to   { transform: rotate(360deg);}
        }
        .ivurow>p{
            padding-top: 3px;
        }
        .dataSource .ivu-table:before{
           background-color:  #ffffff !important;
        }
        .match_setting .ivu-table:before{
           background-color:  #ffffff !important; 
        }
        .pictorial-icon-upload{
            width: 36px;
            height:36px;
            line-height: 36px;
            position: relative;
        }
        .pictorial-icon-upload>.cover{
            display: none;
            position: absolute;
            top: 0;
            bottom: 0;
            width: 67px;
            height: 50px;
            left: 0;
            right: 0;
            border-radius:3px;
            text-align: center;
            background: rgba(0,0,0,.6);
        }
        .pictorial-icon-upload:hover .cover{
            padding-top: 10px;
            display: block;
        }
        .cover i{
            color: #fff;
            font-size: 20px;
            cursor: pointer;
        }
        .ivu-tooltip-inner {
            font-size: 10px;
            padding: 2px 8px;
            min-height: 24px;
        }
        .ivu-menu-opened>.ivu-menu-submenu-title{
            color:#2d8cf0;
        }
        .ivu-tree-title-selected{
            background: none;
        }
        #dataTree .ivu-tree-title{
            padding:0;
        }

        /*样式调整 输入框小一点 -begin*/
        .little-input .ivu-input{
            padding: 3px 7px;
            height: 28px;
            border-radius: 3px;
        }
        .little-input .ivu-select-selection,
        .little-input .ivu-select-placeholder,
        .little-input .ivu-select-selected-value{
            height: 28px !important;
            line-height: 28px !important;
        }

        .little-input .ivu-input-prefix i,
        .little-input .ivu-input-suffix i{
            line-height: 28px !important;
        }
        /*样式调整 输入框小一点 -begin*/

        .excel-backgroud-st>.ivu-upload{
            display: block !important;
        }

        /*删除弹窗样式 -begin*/
        .modal-body-del {
            padding: 15px 0 0 15px;
            font-size: 13px;
            font-weight:400;
            border-top: 1px solid #e8e8e8;
            /*border-bottom: 1px solid #e8e8e8;*/
        }
        /*删除弹窗样式-end*/
        
        /*自定义表达式样式 -begin*/
        .expression .fontColor{
            color:#888;
            font-size: 14px;
        }
        .expression .ivu-modal-body{
            background: rgb(248,248,248);
        }  
        .expression .expressionInput textarea{
            height: 100px;
        }  
        .expression .expressionHeight textarea{
            height: calc(45vh);
        }
        .expression .functionDiv{
            height: 200px;
            border: 1px solid #dcdee2;
            background: #ffffff;
        }
        .expression .leftFunction{
            cursor: pointer;
        }
        .expression .leftFunctionSelect{
            cursor: pointer;
            background: #dcdee2;
        }
        .expression .functionDiv span{
            margin-left: 10px;
        }     
        .expression .childrenDiv{
            height: 200px;
            border: 1px solid #dcdee2;
            background: #ffffff;
            overflow-y: auto;
            margin-left: 10px;
        }
        .expression .childrenDiv span{
            margin-left: 10px;
        }
        .expression .activeItem{
            cursor: pointer;
        }
        .expression .rightFunctionSelect{
            cursor: pointer;
            background: #dcdee2;
        }
        .ivu-btn-primary button{
            color: white !important;
        }
        .expression .ivu-input:focus{
            border-color: #dcdee2 !important;
            -webkit-box-shadow:none !important;
            box-shadow: none !important;
        }
        .expression .ivu-input:hover{
            border-color: #dcdee2 !important;
        }
        .interpretation{
            margin-left: 10px;
        }
        .interpretation p{
            font-size: 12px;
            color:#888;
            word-wrap: break-word;
            overflow-wrap: break-word;
        }
        /*自定义表达式样式 -end*/

        /*去掉 excel div块的间距 使其贴住浏览器边线*/
        #tableDiv .ivu-card-body{padding: 0}
        #tableDiv .layout-content{margin: 0}
        .jm-noScroll{ overflow: hidden; }
        #propsDiv .ivu-card-body{padding: 16px 0 16px 8px !important;}
        /*update-begin--Author:wangshuai  Date:20210226 for：修改设计页面左右两侧的样式-------------------*/
        .ivu-select-dropdown{
            z-index: 999;
        }
        .dataSourceForm .ivu-form-item-label{
            font-size: 12px;
        }
        .dataSourceForm .ivu-select-single .ivu-select-selection .ivu-select-selected-value{
            font-size: 12px;
        }
        .rightFontSize{
            font-size: 12px !important; 
        }
        .rightFontSize .ivu-input{
            font-size: 12px;
        } 
        .rightFontSize .ivu-table-cell{
            font-size: 12px;
        }
        /*右侧有两个文字的时候input的宽度*/
        .twoInputWidth{
            margin-left: 4px;
            width: 165px !important;
        }
        .fourInputWidth{
            margin-left: 4px;
            width: 70% !important;
        }
        /*向上边距*/
        .basicSettingTop{
            margin-top: 15px;
        }
        #dataTree .ivu-tree ul{
           font-size: 13px; 
        }
        #dataTree .ivu-btn-icon-only.ivu-btn-small{
            position: relative;
            left: 14px;
        }
        #dataTree .ivu-tree-title{
           width: 100%;
        }     
        .reportIfo .ivu-menu-submenu-title{
           width: 224px;
        }
        .rightPadding .ivu-card-body {
            padding: 8px;
        }   
        .rightPadding .ivu-form-item-content {
            display: flex;
        }
        .rightPadding  .ivu-menu-vertical .ivu-menu-submenu-title{
            padding:14px 0;
        } 
        .stylePadding  .ivu-menu-vertical .ivu-menu-submenu-title{
            padding:14px 30px;
        }
        .colorHeight .ivu-input-hide-icon .ivu-input-icon-normal+.ivu-input{
            height: 24px !important;
        }
        .basic .ivu-menu-submenu-title{
            width: 230px;
        }
        /*update-end--Author:wangshuai  Date:20210226 for：修改设计页面左右两侧的样式-------------------*/
        .ivu-table-header table{
            width: 100% !important;
        }
        .icnoAlignment{
            position:relative;
            left: 169px;
        }
        .icnoLeft{
            position:relative;
            left: 0;
        }
        .icnoRight{
            position:relative;
            left: 169px;    
        }
        .title-setting-fontsize .ivu-select-selected-value{
            font-size: 10px !important;
        }
        .twoInputWidth .ivu-icon{
            cursor: pointer;
        }
        .basicSettingTop .ivu-input{
            padding-right: 32px;
        }
       #treeDiv .ivu-card-body{
          padding: 16px 0 16px 16px !important;
       }
       .help-color{
           color:#000000;
           font-size: 10px;
       }
       .basic .layout-content{
           margin: 10px 0 10px 10px;
       }
       .help-margin{
           margin-right: 13px;
       }
       /*增强弹框样式*/
       .jmreport-enhance .ivu-collapse-content{
            padding: 0 5px;
       }
       .jmreport-enhance .ivu-collapse-content-box{
          padding-top: 5px;
          padding-bottom: 5px;
       }
       .jmreport-enhance textarea{
           resize: none;
       }
    </style>
<body onload="load()" class="jm-noScroll">
<div id="app" v-cloak>
    <Spin size="large" fix v-if="createLoading" class="zindex-top">
        <Icon type="ios-loading" size=24></Icon>
        <div>Loading</div>
    </Spin>
<#include "./modal.ftl">
    <div class="layout">
        <div class="jm-rp-left-container">
            <div id="treeDiv">
                <span slot="title" @click="toggleLeft" style="float: top;display: flex" class="icnoAlignment">
                   <span style="color: #000000;font-size: 12px;position: relative;top: 4px;" v-if="dataShow">收起</span> <Icon type="md-arrow-dropleft" size="24"/>
                </span>
                <!-- 数据源设置 -->
                <j-data-source-setting  ref="dataSource" @saveback="saveDbBack" @cancelback="cancelback"></j-data-source-setting>
                <!-- javabean设置 -->
                <j-javabean-setting  ref="javabean" @saveback="saveDbBack" @cancelback="cancelback"></j-javabean-setting>
                <card style="width: 221px;" v-if="dataShow" :style="{'overflowY':'auto', 'height':windowHeight+'px'}">
                    <template>
                        <div id="dataDiv">
                            <template>
                                <i-menu theme="light" style="margin-left: -25px;z-index: auto;width: auto !important;" :class="menuitemClasses">
                                        <div style="width:95%;height: 30px;border: none;margin-left: 10px; z-index:999;cursor: pointer;margin-left:27px">
                                            <span class="rightFontSize">数据集管理
                                                <Tooltip :transfer="true" content="数据源管理文档" placement="left" class="jimu-tooltip">
                                                    <a class=" help-color" href="http://report.jeecg.com/1835711" target="_blank" style="margin-right: 15px"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 4px;"/></a>
                                                </Tooltip>
                                            </span>
                                            <Dropdown @on-click="onMenuSelect" placement="bottom-start" :transfer="true">
                                                <a href="javascript:void(0)">
                                                    <Icon type="md-add" style="position:relative;left:89px"/>
                                                </a>
                                                <Dropdown-menu slot="list">
                                                    <Dropdown-item name="sqlInfo" class="rightFontSize">SQL数据集</Dropdown-item>
                                                    <Dropdown-item name="apiInfo" class="rightFontSize">API数据集</Dropdown-item>
                                                    <Dropdown-item name="javabean" class="rightFontSize">JavaBean数据集</Dropdown-item>
                                                    <Dropdown-item name="jsonInfo" class="rightFontSize">JSON数据集</Dropdown-item>
                                                </Dropdown-menu>
                                            </Dropdown>
                                        </div>
                                    <div id="dataTree">
                                        <template v-for="(item,index) in treeData">
                                            <Tree :data="item" @on-toggle-expand="onTreeToggleExpand" @on-select-change="changeTree"></Tree>
                                        </template>
                                    </div>

                                    <Submenu name="reportIfo" class="reportIfo">
                                        <template slot="title">
                                            <span class="rightFontSize">报表信息</span>
                                        </template>
                                        <div style="height: 86px;line-height: 8px;margin-left: 25px;">
                                            <div>
                                                <i-form :model="designerObj" label-colon :label-width="90"  class="dataSourceForm">
                                                    <div label="" class="rightFontSize">
                                                        <span>名称</span>
                                                        <i-input v-model="designerObj.name" placeholder="请输入名称" @on-blur="excelQueryName" @on-change="changeName" class="rightFontSize" style="margin-left: 4px; width: 132px;"></i-input>
                                                    </div>

                                                    <div class="dataSourceForm rightFontSize" style="margin-top: 10px">
                                                        <span>类型</span>
                                                        <i-select  :transfer="true" :model.sync="designerObj.type" v-model="designerObj.type" style="margin-left: 4px;  width: 132px;" @on-change="selectmenuList">
                                                            <i-option style="z-index: 9999" class="rightFontSize" v-for="item in menuList" :value="item.value">{{ item.label }}</i-option>
                                                        </i-select>
                                                    </div>
                                                </i-form>
                                            </div>
                                        </div>
                                    </Submenu>
                                    <j-data-dictionary ref="dataDictionary"></j-data-dictionary>
                                    <div style="width:100%;height: 45px;border: none;margin-left: 25px;cursor: pointer;margin-top: 10px">
                                      <span @click="createDictClick" class="rightFontSize">数据字典<i style="position: relative;left:100px;font-size: 14px;" class="ivu-icon ivu-icon-md-create"></i></span>
                                      <Tooltip :transfer="true" content="数据字典文档" placement="left" class="jimu-tooltip">
                                          <a class="jimu-table-tip help-color" href="http://report.jeecg.com/2083759" target="_blank"  style="margin-right: 26px;"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 4px;"/></a>
                                      </Tooltip>
                                    </div>
                                </i-menu>
                            </template>
                        </div>
                    </template>
                </card>
            </div>
        </div>

        <div :class="centerDivClass">
            <div id="tableDiv">
                <Card>
                    <div class="layout-content" style="overflow: auto">
                        <div id="jm-sheet-wrapper" style="overflow:auto"></div>
                    </div>
                </Card>
            </div>
        </div>
        <div class="jm-rp-right-container">
            <div id="propsDiv">
                <span slot="title" @click="toggleRight">
                    <span style="color: #000000;font-size: 12px;position: relative;top: 4px;float: right;right:200px" v-if="propsContentShow">收起</span><Icon type="md-arrow-dropright" size="24"/>
                </span>
                <card style="width: 250px;height: 977px" v-if="propsContentShow" class="rightPadding">
                    <Tabs size="small" v-model="rightTabName" >

                        <!-- 基本设置  -->
                        <tab-pane label="基本" name="name1" :class="'little-input'"  class="basic">
                            <div id="propsContentDiv" class="layout-content jm-setting-container" :style="{height: settingsHeight+'px'}">
                                <div class="dataSourceForm">
                                  <div class="rightFontSize">
                                    <span >坐标</span>
                                    <i-input class="twoInputWidth" disabled v-model="excel.coordinate"></i-input>
                                  </div>
                                  <div class="basicSettingTop">
                                    <span class="rightFontSize">类型</span>
<#--                                    <p @click="customExpression">自定义表单式</p>-->
                                    <i-select class="twoInputWidth" v-model="excel.type" @on-change="onChangeCellDisplay">
                                        <i-option value="normal" key="1" class="rightFontSize">文本</i-option>
                                        <i-option value="number" key="0" class="rightFontSize">数值</i-option>
                                        <i-option value="img" key="2" class="rightFontSize">图片</i-option>
                                        <i-option value="barcode" key="3" class="rightFontSize">条形码</i-option>
                                        <i-option value="qrcode" key="4" class="rightFontSize">二维码</i-option>
                                        <#--<i-option value="chart" key="5">图表</i-option>-->
                                    </i-select>
                                  </div>
                                  <div class="basicSettingTop rightFontSize">
                                    <span>值</span>
                                    <i-input @on-click="enlargeInputClick" icon="md-contract" style="margin-left: 16px;" v-model="excel.excelValue" @keyup.enter.native="submitValue" @on-blur="submitValue" class="twoInputWidth"></i-input>
                                  </div>

                                  <div class="basicSettingTop rightFontSize">
                                    <span >宽度</span>
                                    <i-input title="输入完值回车生效!" placeholder="输入完值回车生效!" v-model="excel.width" @keyup.enter.native="handleChangeCellWidth" @on-blur="handleChangeCellWidth"  class="twoInputWidth" :disabled="excel.isMergeCell"></i-input>
                                  </div>

                                  <div class="rightFontSize basicSettingTop">
                                    <span >高度</span>
                                    <i-input title="输入完值回车生效!" placeholder="输入完值回车生效!" v-model="excel.height" @on-blur="handleChangeCellHeight" @keyup.enter.native="handleChangeCellHeight" class="twoInputWidth" :disabled="excel.isMergeCell"></i-input>
                                  </div>

                                  <i-menu theme="light" width="100%" :class="menuitemClasses">

                                      <Submenu name="blankRowSetting">
                                          <template slot="title">
                                              <span class="rightFontSize">补充空白行
                                                <Tooltip :transfer="true" content="补充空白行文档" placement="left" class="jimu-tooltip">
                                                    <a class="jimu-table-tip help-color help-margin" href="http://report.jeecg.com/2361240" target="_blank"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
                                                </Tooltip>
                                              </span>
                                          </template>
                                          <div>
                                              <div class="rightFontSize" style="padding-left:2px">
                                                  <Checkbox v-model="excel.completeBlankStatus" @on-change="onChangeCompleteBlankStatus">启用</Checkbox>
                                              </div>
                                              <div class="rightFontSize" style="margin-top:12px">
                                                  <span>数据行倍数：</span>
                                                  <i-input type="number" v-model="excel.completeBlankRow" @on-change="onChangeCompleteBlankRow" style="width:80px" class="rightFontSize"></i-input>
                                              </div>
                                          </div>
                                      </Submenu>

                                    <div v-if="excel.hasGroup">


                                      <Submenu name="groupSetting">
                                        <template slot="title">
                                          <span class="rightFontSize">分组设置
                                             <Tooltip :transfer="true" content="分组设置文档" placement="left" class="jimu-tooltip">
                                                <a class="jimu-table-tip help-color help-margin" href="http://report.jeecg.com/2032023" target="_blank"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
                                            </Tooltip>      
                                          </span>
                                        </template>
                                        <div>
                                            <div class="rightFontSize">
                                              <span>聚合方式</span>
                                              <i-select ref="excelPolyWay" :transfer="true"  :model.sync="excel.polyWay" v-model="excel.polyWay" @on-change="selectPolyList" class="fourInputWidth" :disabled="wayDisabled">
                                                <i-option class="rightFontSize" v-for="item in polyWayList" :value="item.value">{{ item.label }}</i-option>
                                              </i-select>
                                            </div>
                                            <div class="basicSettingTop rightFontSize">
                                              <span>扩展方向</span>
                                              <i-select :model.sync="excel.direction" :transfer="true"  v-model="excel.direction" @on-change="selectDirectionList" class="fourInputWidth">
                                                <i-option class="rightFontSize" v-for="item in directionList" :value="item.value">{{ item.label }}</i-option>
                                              </i-select>
                                            </div>
                                            <div class="basicSettingTop rightFontSize">
                                                <span>排序方式</span>
                                                <i-select :transfer="true" :model.sync="excel.sort" :transfer="true"  v-model="excel.sort" @on-change="selectSortList" class="fourInputWidth">
                                                    <i-option class="rightFontSize" v-for="item in sortType" :value="item.value">{{ item.label }}</i-option>
                                                </i-select>
                                            </div>
                                            <div class="basicSettingTop rightFontSize">
                                              <span>高级配置</span>
                                              <i-select :transfer="true" :model.sync="excel.advanced" :transfer="true"  v-model="excel.advanced" @on-change="selectAdvancedList" class="fourInputWidth">
                                                <i-option class="rightFontSize" v-for="item in advancedList" :value="item.value">{{ item.label }}</i-option>
                                              </i-select>
                                            </div>
                                        </div>    
                                      </Submenu>
                                         <!--分组小计设置-->
                                        <Submenu name="subtotalSetting">
                                            <template slot="title">
                                                <span class="rightFontSize">小计设置
                                                    <Tooltip :transfer="true" content="分组小计文档" placement="left" class="jimu-tooltip">
                                                        <a class="jimu-table-tip help-color help-margin" href="http://report.jeecg.com/2333594" target="_blank"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
                                                    </Tooltip>  
                                                </span>
                                            </template>

                                            <div class="rightFontSize"  v-if="excel.funcname=='-1'">
                                                <span>分组依据</span>
                                                <i-select ref="excelSubtotal" :transfer="true"  v-model="excel.subtotal" @on-change="selectSubtotal" class="fourInputWidth">
                                                    <i-option class="rightFontSize" v-for="item in subtotalList" :value="item.value">{{ item.label }}</i-option>
                                                </i-select>
                                            </div>

                                            <div  :class="['rightFontSize',{'basicSettingTop':excel.subtotal=='-1'&&excel.funcname=='-1'}]" v-if="excel.subtotal=='-1'" >
                                                    <span>聚合方式</span>
                                                    <i-select ref="excelAggregate" :transfer="true"  v-model="excel.funcname"   @on-change="selectAggregate" class="fourInputWidth">
                                                        <i-option class="rightFontSize" v-for="item in aggregateList" :value="item.value">{{ item.label }}</i-option>
                                                    </i-select>
                                            </div>
                                        </Submenu>
                                    </div>


                                  <Submenu name="hyperlinksSetting">
                                    <template slot="title">
                                        <span class="rightFontSize">超链接设置
                                            <Tooltip :transfer="true" content="超链接文档" placement="left" class="jimu-tooltip">
                                                <a class="jimu-table-tip help-color help-margin" href="http://report.jeecg.com/2232719" target="_blank"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
                                            </Tooltip>
                                        </span>
                                    </template>

                                         <span class="rightFontSize">添加链接</span>
                                         <Dropdown @on-click="onHyperlinksClick" placement="bottom-start" :transfer="true">
                                             <a href="javascript:void(0)">
                                                 <Icon type="md-add" style="margin-left: 20px"/>
                                             </a>
                                             <Dropdown-menu slot="list">
                                                 <Dropdown-item name="2" class="rightFontSize">图表联动</Dropdown-item>
                                                 <Dropdown-item name="0" class="rightFontSize">报表钻取</Dropdown-item>
                                                 <Dropdown-item name="1" class="rightFontSize">网络报表</Dropdown-item>
                                             </Dropdown-menu>
                                         </Dropdown>

                                    <i-table style="margin-top: 10px" class="rightFontSize" :columns="hyperlinks.columns" :data="hyperlinks.data"></i-table>
                                    <j-hyperlinks-setting ref="hyperlinksModel"  :excel="linkExcel" @lingcallback="lingCallBack"></j-hyperlinks-setting>

                                      <j-chart-linkage ref="chartLinkage" @ok="chartLinkageConfigSuccess"></j-chart-linkage>

                                      <j-cell-linkage ref="cellLinkage" @ok="cellLinkageConfigSuccess"></j-cell-linkage>

                                  </Submenu>
                                  <Submenu name="otherSetting">
                                      <template slot="title">
                                          <span class="rightFontSize">其他设置
                                              <Tooltip :transfer="true" content="数据格式化文档" placement="left" class="jimu-tooltip">
                                                  <a class="jimu-table-tip help-color help-margin" href="http://report.jeecg.com/2084138" target="_blank"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
                                              </Tooltip>  
                                          </span>
                                      </template>
                                    <div class="rightFontSize">
                                        <span>小数位数</span>
                                        <i-input class="rightFontSize fourInputWidth" v-model="excel.decimalPlaces" @on-change="onChangeDecimalPlaces"></i-input>
                                    </div>
                                    <div class="rightFontSize" style="margin-top: 5px;padding:0 5px">
                                        <i-button @click="showEnhanceConfig" icon="md-code-working" size="small" long type="primary" ghost>增强配置</i-button>
                                    </div>
                                  </Submenu>
                                  <!--参数设置-->  
                                  <Submenu name="primarySubreportSetting">
                                      <template slot="title">
                                          <span class="rightFontSize">主子报表参数设置
                                            <Tooltip :transfer="true" content="主子报表文档" placement="left" class="jimu-tooltip">
                                                <a class="jimu-table-tip help-color help-margin" href="http://report.jeecg.com/2296481" target="_blank"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
                                            </Tooltip>
                                          </span>
                                      </template>
                                    <i-button class="rightFontSize" @click="primarySubreportClick">新增</i-button>
                                    <i-table style="margin-top: 10px" class="rightFontSize" :columns="primarySubreport.columns" :data="primarySubreport.data"></i-table>
                                    <j-primary-sub-report ref="primarySubreportModal" @mainsubreport="mainsubreport"></j-primary-sub-report>
                                  </Submenu>

                                  <Submenu name="layerOffset" v-if="layerOffsetEnable">
                                      <template slot="title">
                                          <span class="rightFontSize">图片偏移量设置</span>
                                      </template>
                                      <div class="rightFontSize">
                                          <span>横向偏移(px)：</span>
                                          <i-input type="number" style="margin-left: 10px;width:90px !important;" v-model="layerOffsetX" @on-change="handleChangeLayerOffset" class="twoInputWidth"></i-input>
                                      </div>

                                      <div class="rightFontSize" style="margin-top:12px">
                                          <span>纵向偏移(px)：</span>
                                          <i-input type="number" style="margin-left: 10px;width:90px !important;" v-model="layerOffsetY" @on-change="handleChangeLayerOffset" class="twoInputWidth"></i-input>
                                      </div>
                                  </Submenu>

                                </i-menu>
                              </div>
                            </div>
                        </tab-pane>

                        <!-- 图表样式设置  -->
                        <tab-pane v-if="chartsflag && !backgroundSettingShow" label="样式" name="name2" :disabled="selectedChartType==='apiUrlType'" class="stylePadding">
                            <i-menu theme="light" width="auto" :style="{height: settingsHeight+'px', marginLeft: '-20px'}" :class="menuitemClasses" accordion>

                                <!-- 标题设置 -->
                                <j-title-setting v-if="titleSettings" @change="onSettingsChange" :settings="titleSettings"></j-title-setting>
                                <!-- 柱体设置 -->
                                <j-bar-setting v-if="barSettings" @change="onSeriesChange" :settings="barSettings" :is-multi-chart="isMultiChart"></j-bar-setting>
                                <!-- 线体设置 -->
                                <j-line-setting v-if="lineSettings" @change="onSeriesChange" :settings="lineSettings" :is-multi-chart="isMultiChart"></j-line-setting>
                                <!-- 饼图设置-->
                                <j-pie-setting v-if="pieSettings" @change="onSeriesChange" :settings="pieSettings"></j-pie-setting>
                                <!-- 边距设置-->
                                <j-margin-setting v-if="marginSettings" @change="onSeriesChange" :settings="marginSettings"></j-margin-setting>
                                <!-- 中心点设置-->
                                <j-central-point-setting v-if="centralPointSettings" @change="onSeriesChange" :settings="centralPointSettings"></j-central-point-setting>
                                <!-- 漏斗设置-->
                                <j-funnel-setting v-if="funnelSettings" @change="onSeriesChange" :settings="funnelSettings"></j-funnel-setting>
                                <!-- 象形图设置 -->
                                <j-pictorial-setting v-if="pictorialSettings" @change="onPictorialChange" @upload-success="pictorialIconUploadSuccess" :settings="pictorialSettings"></j-pictorial-setting>
                                <!-- 地图设置 -->
                                <j-map-setting  ref="mapModal" v-if="mapGeoSettings" @change="onSettingsChange" :settings="mapGeoSettings"></j-map-setting>
                                <!-- 散点设置-->
                                <j-scatter-setting v-if="scatterSettings" @change="onSeriesChange" :settings="scatterSettings"></j-scatter-setting>
                                <!-- 雷达设置-->
                                <j-radar-setting v-if="radarSettings" @change="onSettingsChange" :settings="radarSettings"></j-radar-setting>
                                <!-- 仪表盘设置-->
                                <j-gauge-setting v-if="gaugeSettings" @change="onSeriesChange" :settings="gaugeSettings"></j-gauge-setting>
                                <!-- x轴设置-->
                                <j-xaxis-setting v-if="xAxisSettings" @change="onSettingsChange" :settings="xAxisSettings"></j-xaxis-setting>
                                <!-- y轴设置(settings支持数组)-->
                                <j-yaxis-setting v-if="yAxisSettings" @change="onSettingsChange" :settings="yAxisSettings"></j-yaxis-setting>
                                <!-- 数值设置-->
                                <j-series-setting v-if="seriesLabelSettings" @change="onSeriesChange" :settings="seriesLabelSettings"></j-series-setting>
                                <!-- 提示语设置-->
                                <j-tooltip-setting v-if="tooltipSettings" @change="onSettingsChange" :settings="tooltipSettings"></j-tooltip-setting>
                                <!-- 坐标轴边距设置-->
                                <j-grid-setting v-if="gridSettings" @change="onSettingsChange" :settings="gridSettings"></j-grid-setting>
                                <!-- 图例设置-->
                                <j-legend-setting v-if="legendSettings" @change="onSettingsChange" :settings="legendSettings"></j-legend-setting>
                                <!-- 自定义配色-->
                                <j-match-setting style="border-bottom: inset 1px;" v-if="graphSettings ||gaugeSettings ||funnelSettings || pieSettings || isMultiChart || selectedChartType.indexOf('multi')!=-1 || selectedChartType == 'radar.basic'" :chart-options="chartOptions"  :data-settings="dataSettings" ></j-match-setting>
                                <!-- 背景设置-->
                                <j-background-setting  @change="chartBackgroundChange" @upload-success="chartBackgroundUploadSuccess" @remove="removeChartBackground" :settings="chartBackground"></j-background-setting>
                                <!-- 柱形图系列类型设计-->
                                <#--<j-bar-series-setting  style="border-bottom: inset 1px;" :chart-options="chartOptions" :data-settings="dataSettings" v-if="selectedChartType == 'bar.multi' || selectedChartType == 'line.multi' "></j-bar-series-setting>-->
                            </i-menu>
                        </tab-pane>

                        <!-- 图表数据设置  -->
                        <tab-pane style="padding:10px 8px;" v-if="chartsflag && selectedChartType !== 'map.simple'" :label="dataLabel" name="name3" :class="'little-input'">
                            <div class="datastyle rightFontSize dataSourceForm">
                                <span>数据类型</span>
                                <i-select class="fourInputWidth" v-model="dataSettings.dataType" :disabled="selectedChartType==='apiUrlType'" @on-change="dataTypeChange">
                                    <i-option class="rightFontSize" value="sql">SQL数据集</i-option>
                                    <i-option class="rightFontSize" value="api">Api数据集</i-option>
                                    <i-option class="rightFontSize" value="json">JSON数据集</i-option>
                                    <i-option class="rightFontSize" value="javabean">JavaBean数据集</i-option>
                                </i-select>
                            </div>

                            <!-- api数据集 -->
                            <div class="datastyle rightFontSize dataSourceForm" v-if="dataSettings.dataType == 'api'">
                                <div class="datastyle">
                                    <span style="margin-left: 7px">Api类型</span>
                                    <i-select class="fourInputWidth" v-model="dataSettings.apiStatus" :disabled="selectedChartType==='apiUrlType'" @on-change="seriesOnChange">
                                        <i-option class="rightFontSize" value="0">静态数据</i-option>
                                        <i-option class="rightFontSize" value="1">动态数据</i-option>
                                        <i-option class="rightFontSize" value="2">接口请求</i-option>
                                    </i-select>
                                </div>
                                <div class="datastyle" v-if="dataSettings.apiStatus == '0'">
                                    <span style="display: inline-block;text-align: left;width: calc(100% - 50px);">请自定义数据值:</span>
                                    <i-button style="width: 44px;" size="small" type="primary" @click="addEchartInfoData">编辑</i-button>
                                </div>
                                <div class="datastyle" v-if="dataSettings.apiStatus == '2'">
                                    <p>接口url:&nbsp;&nbsp;</p>
                                    <i-input v-model="dataSettings.apiUrl" :autosize="true" type="textarea" placeholder="请输入接口地址..."></i-input>
                                </div>
                                <div class="datastyle rightFontSize dataSourceForm" v-if="dataSettings.apiStatus == '1'">
                                    <div>
                                        <span>绑定数据集:</span>
                                        <i-select style="width: 136px" v-model="dataSettings.dataId" @on-change="onSelectApiData">
                                            <i-option class="rightFontSize" v-for="item in apiDataList" :value="item.dbId">{{ item.title }}</i-option>
                                        </i-select>
                                    </div>
                                </div>
                            </div>

                            <!-- sql数据集 -->
                            <div class="datastyle rightFontSize dataSourceForm" v-if="dataSettings.dataType == 'sql'">
                                <div class="datastyle">
                                    <span>绑定数据集:</span>
                                    <i-select style="width: 136px" v-model="dataSettings.dataId" @on-change="onSelectSqlData">
                                        <i-option class="rightFontSize" v-for="item in sqlDataList" :value="item.dbId">{{ item.title }}</i-option>
                                    </i-select>
                                </div>
                                <div class="datastyle">
                                    <span>分类属性:</span>
                                    <i-select class="fourInputWidth" v-model="dataSettings.axisX" @on-change="onAxisXConfigChange">
                                        <i-option class="rightFontSize" v-for="item in sqlDataFieldList" :value="item.title">{{ item.fieldText }}</i-option>
                                    </i-select>
                                </div>
                                <div class="datastyle">
                                    <span style="margin-left: 12px">值属性:</span>
                                    <i-select class="fourInputWidth" :model.sync="dataSettings.axisY" v-model="dataSettings.axisY" @on-change="onAxisYConfigChange">
                                        <i-option class="rightFontSize" v-for="item in sqlDataFieldList" :value="item.title">{{ item.fieldText }}</i-option>
                                    </i-select>
                                </div>
                                <template v-if="isMultiChart || selectedChartType.indexOf('radar') !==-1 || selectedChartType == 'graph.simple'">
                                    <div class="datastyle">
                                        <span>系列属性:</span>
                                        <i-select class="fourInputWidth" v-model="dataSettings.series">
                                            <i-option v-for="item in sqlDataFieldList" :value="item.title">{{ item.title }}</i-option>
                                        </i-select>
                                    </div>
                                </template>
                                <!--分割线-->
                                <template v-if="selectedChartType == 'graph.simple'">
                                    <Divider  style="margin: 20px 0 20px 0"></Divider>
                                    <div class="datastyle">
                                        <span>绑定节点关系数据集:</span>
                                        <i-select style="width: 48%" v-model="dataSettings.dataId1" @on-change="onSelectSqlData2">
                                            <i-option v-for="item in sqlDataList" :value="item.dbId">{{ item.title }}</i-option>
                                        </i-select>
                                    </div>
                                    <div class="datastyle">
                                        <span>来源属性:</span>
                                        <i-select class="fourInputWidth" v-model="dataSettings.source">
                                            <i-option v-for="item in sqlDataFieldList2" :value="item.title">{{ item.title }}</i-option>
                                        </i-select>
                                    </div>
                                    <div class="datastyle">
                                        <span>目标属性:</span>
                                        <i-select class="fourInputWidth" :model.sync="dataSettings.target" v-model="dataSettings.target">
                                            <i-option v-for="item in sqlDataFieldList2" :value="item.title">{{ item.title }}</i-option>
                                        </i-select>
                                    </div>
                                </template>
                            </div>

                            <!-- json数据集 -->
                            <div class="datastyle rightFontSize dataSourceForm" v-if="dataSettings.dataType == 'json'">
                              <div>
                                <span>绑定数据集:</span>
                                <i-select style="width: 136px" v-model="dataSettings.dataId" @on-change="onSelectJsonData">
                                  <i-option class="rightFontSize" v-for="item in jsonDataList" :value="item.dbId">{{ item.title }}</i-option>
                                </i-select>
                              </div>
                            </div>   
                            
                            <!-- javaBean数据集 -->
                            <div class="datastyle rightFontSize dataSourceForm" v-if="dataSettings.dataType == 'javabean'">
                              <div>
                                <span>绑定数据集:</span>
                                <i-select style="width: 136px" v-model="dataSettings.dataId" @on-change="onSelectJavaBeanData">
                                  <i-option class="rightFontSize" v-for="item in javaBeanDataList" :value="item.dbId">{{ item.title }}</i-option>
                                </i-select>
                              </div>
                            </div>
                            <!-- 多维度处理  -->
                            <div class="datastyle" v-if="(dataSettings.dataType == 'sql' || dataSettings.apiStatus == '1' || dataSettings.apiStatus == '0') && (selectedChartType == 'mixed.linebar'||selectedChartType == 'bar.stack')|| selectedChartId=='bar.negative'">
                                <p>系列类型:</p>
                                <div style="margin-top: 5px;">
                                    <Row class="ivurow" style="margin-top: 5px;">
                                        <i-button type="primary" size="small" @click="seriesModal=true">新增</i-button>
                                    </Row>
                                    <i-table stripe :columns="seriesColumns" :data="seriesTypeData"></i-table>
                                </div>
                            </div>
                        <!-- 刷新配置 -->
                            <template class="rightFontSize" v-if="dataSettings.apiStatus==='1' || dataSettings.dataType == 'sql'">
                                <div class="datastyle">
                                    <span style="display: inline-block;text-align: left;width: calc(100% - 50px);" class="rightFontSize">定时刷新:</span>
                                    <i-switch size="small" v-model="dataSettings.isTiming" @on-change="timerChange"/>
                                </div>
                                <div class="datastyle" style="display: flex;align-items: center;" v-if="dataSettings.isTiming">
                                    <span style="display: inline-block;text-align: left;width: calc(100% - 100px);">刷新间隔:</span>
                                    <i-input size="small" type="number" v-model="dataSettings.intervalTime" style="width:100px" @on-blur="timerChange"><span slot="append">秒</span></i-input>
                                </div>
                            </template>
                            <i-button @click="runChart" type="primary" style="width: 100%;height: 36px;margin-top: 10%;">运行</i-button>
                        </tab-pane>

                        <!-- 背景图设置  -->
                        <tab-pane v-if="backgroundSettingShow" style="visibility: visible" label="背景图设置" name="name4" :class="'little-input'" class="rightFontSize dataSourceForm">
                           <div :class="backgroundSettings.path?'excel-backgroud-st':''" style="height: 500px;overflow-y: auto;padding:14px 7px;">
                               <span style="display:inline-block;margin: 5px 0">图片：</span>
                               <Upload
                                   ref="upload"
                                   :headers = "uploadHeaders"
                                   :show-upload-list="false"
                                   :default-file-list="backgroundImg"
                                   :on-success="backgroundImgUploadSuccess"
                                   :on-exceeded-size="(e)=>handleMaxSize(e,10)"
                                   :format="['jpg','jpeg','png']"
                                   :max-size="10240"
                                   :action=" actionUrlPre + '/jmreport/upload' "
                                   style="display: inline-block;width:58px;">
                                   <div style="display: block" class="pictorial-icon-upload" v-if="backgroundSettings.path">
                                       <img style="width: 196px;max-height: 100px" :src="getBackgroundImg()"/>
                                       <div class="cover" style="width: 196px">
                                           <Icon type="ios-create-outline"/>
                                       </div>
                                   </div>
                                   <i-button v-else style="margin-left:25px" type="primary" size="small">上传</i-button>
                               </Upload>

                               <div style="width:100%">
                                  <div class="basicSettingTop">
                                      <span style="padding: 6px 0">图片宽度:</span>
                                      <i-input class="fourInputWidth" v-model="backgroundSettings.width" @on-blur="backgroundChange"></i-input>
                                  </div>
                                   <div class="basicSettingTop">
                                       <span style="padding: 6px 0">图片高度:</span>
                                       <i-input class="fourInputWidth" v-model="backgroundSettings.height" @on-blur="backgroundChange"></i-input>
                                   </div>
                               </div>

                               <div style="width:100%;" class="basicSettingTop">
                                   <span style="padding: 6px 0">重复设置:</span>
                                   <i-select class="fourInputWidth" v-model="backgroundSettings.repeat" style="width:99%" @on-change="backgroundChange">
                                       <i-option class="rightFontSize" value="no-repeat">无重复</i-option>
                                       <i-option class="rightFontSize" value="repeat-x">水平重复</i-option>
                                       <i-option class="rightFontSize" value="repeat-y">垂直重复</i-option>
                                       <i-option class="rightFontSize" value="repeat">双向重复</i-option>
                                   </i-select>
                               </div>

                               <i-button v-if="backgroundSettings.path" style="width: 99%;margin:10px 0" type="primary" @click="removeBackground">取消背景图</i-button>

                               <div style="width:100%;" class="basicSettingTop">
                                   <span style="padding: 6px 0">表格边框:</span>
                                   <i-select class="fourInputWidth" v-model="gridLine" style="width:99%" @on-change="gridLineChange">
                                       <i-option class="rightFontSize" value="true">显示</i-option>
                                       <i-option class="rightFontSize" value="false">隐藏</i-option>
                                   </i-select>
                               </div>

                           </div>
                        </tab-pane>

                        <!-- 条形码设置  -->
                        <tab-pane v-if="barcodeSettings" style="visibility: visible" :label="qrCodeLabel" name="name5" :class="'little-input'">
                            <j-barcode-setting @change="onBarcodeChange" :settings="barcodeSettings"></j-barcode-setting>
                        </tab-pane>

                        <!-- 二维码设置  -->
                        <tab-pane v-if="qrcodeSettings" style="visibility: visible" :label="codeLabel" name="name6" :class="'little-input'">
                            <j-qrcode-setting @change="onBarcodeChange" :settings="qrcodeSettings"></j-qrcode-setting>
                        </tab-pane>
                    </Tabs>

                </card>
            </div>
        </div>
    </div>
    <!-- 打印配置弹框 -->
    <j-print-setting :show="printSettingShow" :settings="printSettings" :config="configString" @change="onPrintSettingChange"></j-print-setting>

    <j-view-setting :show="viewSettingShow" :settings="viewSettings" @change="onViewSettingChange"></j-view-setting>
</div>

<script>

    var excel_config_id = "${config_id}";
    var excel_req_token = '';
    var xs = null;
    var vm = null;
    let autoSaveFun;

    /**
     * 获取后台配置的报表配置
     */
    function getReportConfigJson() {
        let str = '${reportConfig}';
        return JSON.parse(str)
    }

    function load() {
        let token = window.localStorage.getItem('JmReport-Access-Token');
        if (token == "" || token == null){
            token = getRequestUrl().token;
        }
        excel_req_token = token
        console.log("index_load--------------",token);
        let reportConfig = getReportConfigJson();
        let colLength = 50, viewPageSize = [10,20,30], printPaper = []
        if(reportConfig['pageSize']){
            viewPageSize = reportConfig['pageSize']
        }
        if(reportConfig['col']){
            colLength = reportConfig['col']
        }
        if(reportConfig['printPaper']){
            printPaper = reportConfig['printPaper']
        }
        let showGridLine = true;
        if(reportConfig['line']==false){
            showGridLine = false;
        }
        const options = {
            "domain": 'http://localhost:8080/jeecg-boot',
            "viewLocalImage":JM_VIEW_IMG_URL,//预览本地图片方法
            "uploadUrl":"/jmreport/upload", //统一上传地址
            "uploadExcelUrl":"/jmreport/importExcel?token="+token,//上传excel方法
            pageSize: viewPageSize, //分页条数
            printPaper: printPaper,
            domain:window.location.origin+baseFull,
            showToolbar: true,     //头部操作按钮
            showGrid: showGridLine,        //excel表格
            showContextmenu: true, //右键操作按钮
            view: {
                height: () => document.documentElement.clientHeight,
                width: () => document.documentElement.clientWidth,
            },
            row: {
                len: 100,
                height: 25,
                minRowResizerHeight:1 //拖拽行最小高度
            },
            col: {
                len: colLength,
                width: 100,
                minWidth: 60,
                height: 0,
                minColResizerHeight:1//拖拽列最小高度
            },
            style: {
                bgcolor: '#ffffff',
                align: 'left',
                valign: 'middle',
                textwrap: false,
                strike: false,
                underline: false,
                color: '#0a0a0a',
                font: {
                    name: 'Microsoft YaHei',
                    size: 10,
                    bold: false,
                    italic: false,
                },
            },
        };

        x.spreadsheet.locale('zh-cn');
        xs = x.spreadsheet('#jm-sheet-wrapper', options)
                .onSave(function (data) {
                    //设置报表打印宽度
                    const dataRect = xs.data.getDataRect();
                    let dataRectWidth = 0;
                    if(dataRect){
                        dataRectWidth = dataRect.w;
                    }
                    //直接读取文本框的值
                 //   const printElWidth = xs.sheet.toolbar.toolPrintInputEl.input.el.value
                    data['dataRectWidth'] = dataRectWidth;
                    data['excel_config_id'] = excel_config_id;
                  //  data['printElWidth'] = Number(printElWidth) || dataRectWidth;
                  //  data['printElHeight'] = Number(xs.sheet.toolbar.toolPrintHeightInputEl.input.el.value)
                    data['toolPrintSizeObj'] = xs.data.toolPrintSizeObj;

                    $jm.excelSave(data, token,function (res) {
                        xs.tip("保存成功!");
                        let refresh = res.isRefresh;
                        if(refresh && refresh==true){
                          window.location.reload()
                        }
                    });
                })
                .onAddChart(function(a){
                    vm.addChartModule();
                })
                .onSelectChart(function(data){
                    vm.clearRightTabpane();
                    setTimeout(()=>{
                        vm.selectChart(data);
                    },200)
                })
                .onChartDelete(function(){
                    vm.chartsflag=false;
                    vm.rightTabName='name1';
                })
                .onSettingEvent(function (e, param) {
                    if(e==='background'){
                        vm.handleBackground(param)
                    }else if(e==='clickcell'){
                        vm.onClickCell(param)
                        //是否在弹窗情况下
                        //TODO 先注释掉获取单元格的方法
                        // if(vm.commonFunction){
                        //   let ci = param.ci;
                        //   let ri = param.ri+1;
                        //   //对列转换成英文字母
                        //   let excelStr = vm.excelColIndexToStr(ci);
                        //   //是否选中函数
                        //   if(vm.interpretation){
                        //       vm.functionText = excelStr+ri
                        //       vm.expression = "="+vm.interpretation+"("+excelStr+ri+")"
                        //   }
                        // }
                        if( vm.excel.direction=='right'){
                          vm.wayDisabled = true;
                        }else{
                          vm.wayDisabled = false;
                        }
                    }else if(e==='print-setting'){
                        vm.onPrintSetting(param)
                    }else if(e==='view-setting'){
                        vm.onViewSetting(param)
                    }else if(e==='export-config'){
                        vm.exportReportConfig()
                    }
                })
                // 自定义校验
                .onValidate(function (type, cell) {
                    if(type === 'editor'){
                        //此事件 cell对象只回传了这三个属性值
                        let { flag, text } = vm.validateDbExpression(cell.text)
                        xs.updateEditor({error: !flag, text: text})
                    }
                })
                .onUploadExcel(function (res) {
                   if(!res.success) return;
                   const xsData ={...xs.getData()};
                    let a = res.result
                    Object.keys(a).map(k=>{
                        xsData[k] = a[k]
                    })
                   xs.loadData(xsData);
               })
                .onCellExpress(function (res) {
                     vm.customExpressionShow=true
                     //记录坐标
                     if(res){
                       vm.expression=res 
                       //判断是哪个函数，循环list
                       let functionList = vm.functionList;
                       let left = 0;
                       let right = 0;
                       let text ="";
                       for (let i = 0; i <functionList.length; i++) {
                         let functionElement = functionList[i]['name'].split(",");
                         for (let j = 0; j <functionElement.length; j++) {
                            if(text && text.indexOf(functionElement[j])>0){
                              text = functionElement[j];
                              left = i;
                              right = j;
                            }else{
                              if(vm.expression.indexOf(functionElement[j])>0){
                                text = functionElement[j];
                                left = i;
                                right = j;
                              } 
                            }
                         }
                       }
                       vm.commonFunction=true
                       vm.newFunctionList= vm.functionList[left].name.split(",")
                       vm.leftFunctionIndex=left
                       vm.rightFunctionIndex=right
                       vm.interpretation= text;
                       return;
                     }
                    vm.commonFunction=true
                    vm.newFunctionList= vm.functionList[0].name.split(",")
                    vm.leftFunctionIndex=0
                    vm.rightFunctionIndex=0
                    vm.interpretation= "sum"
                });


        // issues/I4V3YW 获取屏幕的可见宽度
        xs.watchScreenInnerWidth(()=>{
            //获取表格实际能见宽度
            let width = document.documentElement.clientWidth - 100;
            if(vm.propsContentShow){
                width-=250;
            }
            if(vm.dataShow){
                width-=220;
            }
            return width;
        });
        $jm.excelGet(excel_config_id,(res)=> {
            //加入预览地址
            //update-begin---author:wangshuai ---date:20220215  for：[issues/I4SOSH]做完的积木报表，预览生成的访问地址，默认都加了token=null------------
            xs.data.settings.viewUrl = window.location.origin+api.view+excel_config_id
            if(excel_req_token && excel_req_token!="null"){
              xs.data.settings.viewUrl = xs.data.settings.viewUrl +'?token='+excel_req_token
            }
            //update-end---author:wangshuai ---date:20220215  for：[issues/I4SOSH]做完的积木报表，预览生成的访问地址，默认都加了token=null------------
            var str = res.jsonStr;
            if(!str) return;
            //页面加载时设置报表宽度
            const jsonStr = JSON.parse(str);
            console.log('jsonstr', jsonStr)
            // 设置增强
            vm.setEnhanceConfig(res.cssStr, res.jsStr)
            if(jsonStr.chartList)
            {
                jsonStr.chartList.forEach(function(item){
                    let config = JSON.parse(item.config);
                    if (config.geo){
                        if (loadMap){
                            loadMap && loadMap(item)
                        }
                    }
                })
            }
            xs.data.settings.printElWidth = jsonStr.printElWidth || 0;
            xs.data.printElHeight = jsonStr.printElHeight ||  1047; //默认a4纸大小
          //  xs.sheet.toolbar.toolPrintHeightInputEl.input.el.value =  xs.data.printElHeight;
            xs.loadData(jsonStr);
            if(jsonStr.settings){
                if(jsonStr.settings.showGrid == false){
                    vm.gridLine = "false"
                }
            }
            setTimeout(function(){
                if (xs.data.chartList && xs.data.chartList.length > 0){
                    //vm.tabPaneShow();
                    vm.refreshAllChart(xs.data.chartList);
                }
            },300)
            //启动自动保存
            startAutoSave(token, excel_config_id);
        },(res)=>{
            xs.tip(res.message);
        });
        /*xs.sheet.toolbar.toolPrintInputEl.input.el.onchange=(e=>{
            var clientWidth = document.documentElement.clientWidth;
            var remainingWidth = clientWidth - e.target.value - 330;
            if (remainingWidth<300){
                xs.sheet.horizontalScrollbar.el.el.style.overflowX="scroll";
            }else {
                xs.sheet.horizontalScrollbar.el.el.style.overflowX="hidden";
            }
        })*/
    }

</script>
<script type="text/javascript" src="${base}${customPrePath}/jmreport/desreport_/js/util.js?${CACHE_VERSION}"></script>
<script type="text/javascript" src="${base}${customPrePath}/jmreport/desreport_/js/biz/design.js?${CACHE_VERSION}"></script>
<script type="text/javascript" src="${base}${customPrePath}/jmreport/desreport_/cdn/vue/md5.min.js?${CACHE_VERSION}"></script>
<script type="text/javascript" src="${base}${customPrePath}/jmreport/desreport_/js/biz/SignMd5Util.js?${CACHE_VERSION}"></script>
<script>
     window.onbeforeunload = function(event){
	   return '您可能有数据没有保存';
     };
</script>
<#include "./common/tj.ftl">
</body>
</html>
