<#assign CACHE_VERSION = "v=1.0.13">
<!DOCTYPE html>
<html>
<head>
<script>
    let base = "${base}";
    let baseFull = "${base}"+"${customPrePath}";
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
<meta charset="utf-8">
<meta name="viewport" content="width=device-width">
<title>在线设计</title>
    <#include "./common/resource.ftl">
    <link rel="stylesheet" href="${base}${customPrePath}/jmreport/desreport_/corelib/cust.css?${CACHE_VERSION}">
    <link rel="shortcut icon" href="${base}${customPrePath}/jmreport/desreport_/corelib/logo.png?${CACHE_VERSION}" type="image/x-ico">
</head>
<body style="background: #ffffff">
<style>

    .ivu-page,
    .ivu-page-prev,
    .ivu-page-next,
    .ivu-select-selection,
    .ivu-select-dropdown,
    .ivu-page.mini .ivu-page-options-elevator input
    {
        background-color: #ffffff;
        color: #515a6e;
    }
    .page{
        display: flex;
        justify-content: center;
        -webkit-box-pack: center;
    }
    .ivu-page-item{
        background-color: #ffffff;
        border: 1px solid rgba(131, 125, 125, 0.5);
    }
    .ivu-page-item-active{
        background-color: white;
        border: 1px solid #409eff;
    }
    .ivu-page-item a{
        margin: 0 6px;
        text-decoration: none;
        color: #515a6e;
    }
    .ivu-page-next a, .ivu-page-prev a {
        font-size: 14px;
        color: #515a6e;
    }
    .ivu-spin-fix{
        background-color: rgba(131, 125, 125, 0.5);
    }
</style>
<style>
    .title{
        font-size: 20px;
        color: #000000;
        text-align: center;
        line-height: 60px;
        font-weight: 500;
    }
    .ivu-layout-sider {
        transition: all .2s ease-in-out;
        position: relative;
        background: #ffffff;
    }
    .ivu-layout {
        display: flex;
        -webkit-box-orient: vertical;
        -webkit-box-direction: normal;
        flex-direction: column;
        -webkit-box-flex: 1;
        flex: auto;
        background: #ffffff;
    }
    .ivu-menu-dark.ivu-menu-vertical .ivu-menu-opened .ivu-menu-submenu-title  {
        background: #ffffff;
    }
    .ivu-menu-dark.ivu-menu-vertical .ivu-menu-opened {
        background: #ffffff;
    }
    .ivu-upload-list{
        display: none;
    }
    .ivu-table-tip table tbody tr td{
        background-color: ffffff;
        color: #000000;
    }
    .ivu-select-dropdown-list .ivu-select-item-focus{
        background: none !important;
    }
</style>
<!--引入自定义组件-->
<#include "./template/list.ftl">
<div id="app" style="padding-left: 30px">
    <div class="layout" style="margin-left: -30px;margin-top: -10px;">
<#--      <div style="background-color: #1890FF;height: 47px">-->
<#--        <span class="aui-logo"></span>-->
<#--        <span class="jimu-header">欢迎进入积木报表工作台 </span>-->
<#--      </div>-->
        <Layout>
            <Sider breakpoint="md" collapsible :collapsed-width="78" v-model="isCollapsed">
                <i-menu theme="primary" width="auto" :class="menuitemClasses" active-name="datainfo" :open-names="['sub']" @on-select="onMenuSelect">
                    <Submenu name="sub">
                        <template slot="title">
                            <Icon type="ios-apps"/></Icon>
                            报表管理
                        </template>
                        <Menu-Item name="datainfo">
                            <Icon type="md-list"/></Icon>
                            <span>数据报表</span>
                        </Menu-Item>
                        <Menu-Item name="printinfo">
                            <Icon type="md-print"></Icon>
                            <span>打印设计</span>
                        </Menu-Item>
                    </Submenu>
                </i-menu>
                <div slot="trigger"></div>
            </Sider>
            <Tabs value="name1" style="width: 100%" @on-click="tabsClick">
                <tab-pane icon="md-desktop" label="报表设计" name="name1" class="jimu-tab">
                  <div style="display: flex;justify-content:space-between;margin-left:16px;margin-right: 38px;">
                    <div>
                      <i-input size="small" v-model="name" @keyup.enter.native="enterSearchClick" placeholder="回车搜索报表名称"></i-input>
                    </div>
                    <div class="page">
                        <Page :total="page.total"
                              show-total
                              show-elevator
                              :page-size="page.size"
                              show-sizer
                              @on-change="handleCurrentChange"
                              @on-page-size-change="handleSizeChange"
                              size="small">
                        </Page>
                    </div>
                    <div>
                      <i-select
                              transfer="true"
                              v-model="previewModel"
                              size="small"
                              style="width: 78px;text-align: center;">
                        <i-option  value="view" style="font-size: 10px">视图</i-option>
                        <i-option  value="list" style="font-size: 10px">列表</i-option>
                      </i-select>
                    </div>
                  </div>
                    <div style="display: flex;flex-wrap: wrap;" v-if="previewModel =='view'">
                        <div class="excel-view-item excel-list-add">
                            <a @click="createExcel">
                                <i class="ivu-icon ivu-icon-md-add" style="font-size:20px; padding-bottom: 5px;"></i>
                                <p style="letter-spacing: 2px;font-size: 14px;">新建报表</p>
                            </a>
                        </div>

                        <!-- 循环开始 &ndash;&gt;-->
                        <div
                                v-for="(item,index) in dataSource"
                                :key="index"
                                class="excel-view-item"
                                @mouseover="item.editable=true"
                                @mouseout="item.editable=false">

                            <!-- 缩略图 &ndash;&gt;-->
                            <div class="thumb">
                                <img :src="getThumbSrc(item)"/>
                                <div class="excel-edit-container" v-show="item.editable">
                                    <a :href="getExcelEditUrl(item)" target="_blank">
                                        设计
                                    </a>
                                </div>
                            </div>

                            <!-- 底部 &ndash;&gt;-->
                            <div class="item-footer">
                                <span class="item-name">{{ item.name }}</span>
                                <div>
                                    <a class="opt-show" :href="getExcelViewUrl(item)" target="_blank">
                                        <Tooltip content="预览模板" placement="top">
                                            <i class="ivu-icon ivu-icon-ios-eye-outline" style="font-size: 16px"></i>
                                        </Tooltip>
                                    </a>
                                    <a class="opt-show" v-show="userMessage" @click="setTemplate(item,1)">
                                        <Tooltip content="收藏模板" placement="top">
                                            <i class="ivu-icon ivu-icon-ios-star-outline" style="font-size: 16px"></i>
                                        </Tooltip>
                                    </a>
                                    <a class="opt-show" @click="handleDelete(item)">
                                        <Tooltip content="删除模板" placement="top">
                                            <i class="ivu-icon ivu-icon-ios-trash" style="font-size: 16px"></i>
                                        </Tooltip>
                                    </a>
                                    <a class="opt-show" @click="handleCopy(item)">
                                        <Tooltip content="复制模板" placement="top">
                                            <i class="ivu-icon ivu-icon-ios-browsers" style="font-size: 16px"></i>
                                        </Tooltip>
                                    </a>
                                    <a class="opt-show" @click="handleShare(item.id)">
                                        <Tooltip content="分享" placement="top">
                                            <i class="ivu-icon ivu-icon-ios-share-alt" style="font-size: 16px"></i>
                                        </Tooltip>
                                    </a>
                                </div>

                            </div>
                        </div>
                        <!-- 循环结束 &ndash;&gt;-->
                    </div>
                  <div v-else style="padding: 10px 10px">
                    <i-button type="primary" @click="createExcel"  size="small" style="margin-left: 6px;width:78px;font-size: 10px">
                        新建报表
                    </i-button>
                    <i-table size="small" style="margin-top: 10px"  border :columns="listColumns" :data="dataSource">
                      <template slot-scope="{ row, index }" slot="action">
                        <a class="opt-list-show" :href="getExcelViewUrl(row)" target="_blank">
                          <Tooltip transfer="true" content="预览模板" placement="top">
                            <i class="ivu-icon ivu-icon-ios-eye-outline" style="font-size: 16px"></i>
                          </Tooltip>
                        </a>
                        <a class="opt-list-show" :href="getExcelEditUrl(row)" target="_blank">
                          <Tooltip transfer="true" content="编辑" placement="top">
                            <i class="ivu-icon ivu-icon-md-create" style="font-size: 16px"></i>
                          </Tooltip>
                        </a>
                        <a class="opt-list-show" v-show="userMessage" @click="setTemplate(row,1)">
                          <Tooltip transfer="true" content="收藏模板" placement="top">
                            <i class="ivu-icon ivu-icon-ios-star-outline" style="font-size: 16px"></i>
                          </Tooltip>
                        </a>
                        <a class="opt-list-show" @click="handleDelete(row)">
                          <Tooltip transfer="true" content="删除模板" placement="top">
                            <i class="ivu-icon ivu-icon-ios-trash" style="font-size: 16px"></i>
                          </Tooltip>
                        </a>
                        <a class="opt-list-show" @click="handleCopy(row)">
                          <Tooltip transfer="true" content="复制模板" placement="top">
                            <i class="ivu-icon ivu-icon-ios-browsers" style="font-size: 16px"></i>
                          </Tooltip>
                        </a>
                        <a class="opt-list-show" @click="handleShare(row.id)">
                          <Tooltip transfer="true" content="分享" placement="top">
                            <i class="ivu-icon ivu-icon-ios-share-alt" style="font-size: 16px"></i>
                          </Tooltip>
                        </a>
                      </template>
                    </i-table>
                  </div>
                </tab-pane>
                <tab-pane icon="md-options" label="模板案例" name="name2" class="jimu-tab">
                  <div style="display: flex;justify-content:space-between;margin-left:16px;margin-right: 38px">
                    <div>
                      <i-input size="small" v-model="name" @keyup.enter.native="loadData" placeholder="回车搜索报表名称"></i-input>
                    </div>
                    <div class="page">
                        <Page :total="page.total"
                              show-total
                              show-elevator
                              :page-size="page.size"
                              show-sizer
                              @on-change="handleCurrentChange"
                              @on-page-size-change="handleSizeChange"
                              size="small">
                        </Page>
                    </div>
                    <div>
                        <i-select
                                transfer="true"
                                v-model="previewModel"
                                size="small"
                                style="width: 78px;text-align: center;">
                          <i-option  value="view" style="font-size: 10px">视图</i-option>
                          <i-option  value="list" style="font-size: 10px">列表</i-option>
                        </i-select>
                      </div>
                    </div>
                    <div style="display: flex;flex-wrap: wrap;" v-if="previewModel =='view'">

                        <!-- 循环开始 &ndash;&gt;-->
                        <div
                                v-for="(item,index) in dataSource"
                                :key="index"
                                class="excel-view-item"
                                @mouseover="item.editable=true"
                                @mouseout="item.editable=false">

                            <!-- 缩略图 &ndash;&gt;-->
                            <div class="thumb">
                                <img :src="getThumbSrc(item)"/>
                                <div class="excel-edit-container" v-show="item.editable">
                                    <a v-show="userMessage" :href="getExcelEditUrl(item)" target="_blank">
                                        设计
                                    </a>
                                </div>
                            </div>

                            <!-- 底部 &ndash;&gt;-->
                            <div class="item-footer">
                                <span class="item-name">{{ item.name }}</span>
                                <div style="margin-left: 14%;">
                                    <a class="opt-show" :href="getExcelViewUrl(item)" target="_blank">
                                        <Tooltip content="预览模板" placement="top">
                                            <i class="ivu-icon ivu-icon-ios-eye-outline" style="font-size: 16px"></i>
                                        </Tooltip>
                                    </a>
                                    <a class="opt-show" v-show="userMessage" @click="setTemplate(item,0)">
                                        <Tooltip content="取消收藏" placement="top">
                                            <i class="ivu-icon ivu-icon-ios-star" style="font-size: 16px"></i>
                                        </Tooltip>
                                    </a>
                                    <a class="opt-show" @click="handleCopy(item)">
                                        <Tooltip content="复制模板" placement="top">
                                            <i class="ivu-icon ivu-icon-ios-browsers" style="font-size: 16px"></i>
                                        </Tooltip>
                                    </a>
                                </div>
                                <div v-show="userMessage" >
                                    <Upload
                                            :headers="uploadHeader"
                                            :before-upload="handleUpload"
                                            :data="{'id':item.id}"
                                            :action="actionUrl"
                                            :format="['jpg','jpeg','png']"
                                            :on-format-error="handleFormatError"
                                            :on-exceeded-size="handleMaxSize"
                                            :on-success="handleSuccess">
                                        <Tooltip content="上传封面" placement="top-end">
                                            <i class="ivu-icon ivu-icon-md-image" style="font-size: 16px"></i>
                                        </Tooltip>
                                    </Upload>
                                </div>
                            </div>
                        </div>
                        <!-- 循环结束 &ndash;&gt;-->
                    </div>
                  <div v-else style="padding: 10px 10px">
                    <i-button type="primary" @click="createExcel" style="margin-left: 6px;width:78px;font-size: 10px" size="small">
                      新建报表
                    </i-button>
                    <i-table size="small" style="margin-top: 10px"  border :columns="listColumns" :data="dataSource">
                      <template slot-scope="{ row, index }" slot="action">
                        <a class="opt-list-show" :href="getExcelEditUrl(row)" target="_blank">
                          <Tooltip transfer="true" content="编辑" placement="top">
                            <i class="ivu-icon ivu-icon-md-create" style="font-size: 16px"></i>
                          </Tooltip>
                        </a>
                        <a class="opt-list-show" :href="getExcelViewUrl(row)" target="_blank">
                          <Tooltip transfer="true" content="预览模板" placement="top">
                            <i class="ivu-icon ivu-icon-ios-eye-outline" style="font-size: 16px"></i>
                          </Tooltip>
                        </a>
                        <a class="opt-list-show" v-show="userMessage" @click="setTemplate(row,0)">
                          <Tooltip transfer="true" content="取消收藏" placement="top">
                            <i class="ivu-icon ivu-icon-ios-star" style="font-size: 16px"></i>
                          </Tooltip>
                        </a>
                        <a class="opt-list-show" @click="handleCopy(row)">
                          <Tooltip transfer="true" content="复制模板" placement="top">
                            <i class="ivu-icon ivu-icon-ios-browsers" style="font-size: 16px"></i>
                          </Tooltip>
                        </a>
                      </template>
                    </i-table>
                  </div>
                </tab-pane>
            </Tabs>
        </Layout>
    </div>
    <#--分享弹窗-->
    <j-jurisdiction ref="jurisdiction"></j-jurisdiction>
   <#-- <i-button @click="show">Click me!</i-button>
    <Modal v-model="visible" title="Welcome">Welcome to ViewUI</Modal>-->
</div>
<script>
    var BASE_URL="${base}"+"${customPrePath}";
    var currentPage = new Vue({
        el: '#app',
        data: {
            isCollapsed: false,
            token:'',//token
            name:'',
            designerObj:{},
            loading:true,
            showEdit:false,
            dataSource:[],
            modalTitle:"",
            page: { //分页参数
                page: 1,
                size: 10,
                total: 0,
            },
            changecode : "",
            changename : "",
            menuitem : "datainfo",
            tabpan : "name1",
            userMessage: false,
            file:null,
            uploadHeader:{},
            actionUrl:"",
            previewModel:"view",//浏览方式
            listColumns: [
            <#--{-->
            <#--  title: '背景图',-->
            <#--  align: 'center',-->
            <#--  key: 'thumb',-->
            <#--  width: 150,-->
            <#--  className: 'table-background',-->
            <#--  render: (h, params) => {-->
            <#--    let _img = ""-->
            <#--    if(!params.row.thumb){-->
            <#--      _img =  "${base}"+"${customPrePath}"+"/jmreport/desreport_/corelib/jiade.jpg"-->
            <#--    }else{-->
            <#--      if(params.row.thumb.indexOf('http')==0){-->
            <#--        _img = params.row.thumb-->
            <#--      }else{-->
            <#--        _img = "${base}"+"${customPrePath}"+"/jmreport/img/"+params.row.thumb-->
            <#--      }-->
            <#--    }-->
            <#--    if(_img){-->
            <#--      return h('img', {-->
            <#--        attrs: {-->
            <#--          src: _img,-->
            <#--          style: 'width: 100px;height: 39px;vertical-align: middle;'-->
            <#--        },-->
            <#--      })-->
            <#--    }else{-->
            <#--      return h("span", '');-->
            <#--    }-->
            <#--  }-->
            <#--},-->
            {
              title: '报表名称',
              key: 'name',
              align: 'left',
              className: 'table-background'
            },
            {
              title: '操作',
              width: 240,
              align: 'center',
              slot: 'action',
              className: 'table-background',
              fixed:'right'
            }
          ],//列表的列
        },
        computed: {
            menuitemClasses: function () {
                return [
                    'menu-item',
                    this.isCollapsed ? 'collapsed-menu' : ''
                ]
            }
        },
        mounted:function(){
           this.token = token;
            console.log("list_mount--------------",this.token);
           this.uploadHeader = {"X-Access-Token":this.token};
           this.actionUrl=BASE_URL+"/jmreport/putFile";
           this.$nextTick(()=>{
                this.dataSource=[];
                this.userInfo();
            });
        },
        methods: {
            handleSizeChange(val){
                this.page.size = val;
                this.loadData();
            },
            handleCurrentChange (val) {
                this.page.page = val;
                this.loadData();
            },
            show: function () {
            },
            //查询用户信息并加载数据
            userInfo: function(){
                var that = this;
                $http.get({
                    url:api.userInfo,
                    data:{
                        token:that.token
                    },
                    success:(result)=>{
                        if (result.message != null && result.message != ""){
                            if (result.message === "admin"){
                                that.userMessage = true;
                            }
                        }
                        that.$nextTick(()=>{
                            that.loadData();
                        });
                    },
                    error:(err)=>{
                        that.handleSpinHide();
                    }
                },that)
            },
            //加载数据
            loadData: function(name){
                var that = this;
                if (name != null && name != ""){
                    that.tabpan = name;
                    that.page={page: 1,size: 10,total: 0,};
                }
                var url = "";
                that.dataSource=[];
                if (that.tabpan == "name1"){
                    url = api.excelQuery
                }else {
                    url = api.excelQueryByTemplate
                }
                $http.get({
                    url:url,
                    data:{
                        pageNo:that.page.page,
                        pageSize:that.page.size,
                        reportType:that.menuitem,
                        name:that.name,
                        token:that.token
                    },
                    success:(result)=>{
                        var ls = result.records;
                        that.page.total = result.total
                        if(ls && ls.length>0){
                            for(var i = 0;i<ls.length;i++){
                                //预览时设置报表打印宽度
                                let jsonStr = ls[i].jsonStr;
                                let width;
                                if(jsonStr){
                                    jsonStr = JSON.parse(jsonStr);
                                    width = jsonStr.printElWidth || jsonStr.dataRectWidth || 800;
                                    ls[i].printWidth = width;
                                }
                                ls[i].editable=false;
                            }
                            that.$nextTick(()=>{
                                that.dataSource =JSON.parse(JSON.stringify(ls));
                            });
                        }
                    },
                    error:(err)=>{
                        that.handleSpinHide();
                    }
                },that)
            },
            //新建报表
            createExcel: function(){
                var that = this;
                $http.post({
                    url:api.saveReport,
                    data:{},
                    contentType:'json',
                    success:(result)=>{
                        //update-begin---author:wangshuai ---date:20220215  for：[issues/I4SOSH]做完的积木报表，预览生成的访问地址，默认都加了token=null------------
                        let url = api.index+result.id+"?menuType="+this.menuitem;
                        window.open(this.splicingToken(url));
                        //update-end---author:wangshuai ---date:20220215  for：[issues/I4SOSH]做完的积木报表，预览生成的访问地址，默认都加了token=null------------
                    }
                },that)
            },
            //未使用
            handleEditConfig: function(item){
                window.location.href = api.index+item.id+"?token="+this.token;
            },
            /**
             * 为路径拼接token
             * @param url 需要拼接的路径
             * @return 拼接后的token
             */
            splicingToken(url){
              if(this.token && "null" != this.token){
                if(url.indexOf("?")!=-1){
                  url =url + "&token="+this.token;
                }else{
                  url =url + "?token="+this.token;
                }
              }
              return url;
            },
            //删除报表
            handleDelete:function(item){
                $http.confirm({
                    title:'删除报表',
                    content:'是否确认删除?',
                    url:api.deleteReport,
                    data:{
                        id:item.id,
                        token:this.token
                    },
                    success:(result)=>{
                        this.loadData();
                    }
                },this);
            },
            //复制模版
            handleCopy: function(item){
                $http.confirm({
                    title:'复制报表',
                    content:'是否确认复制?',
                    url:api.reportCopy,
                    method:'get',
                    data:{
                        id:item.id,
                        token:this.token
                    },
                    success:(result)=>{
                        this.loadData();
                    }
                },this);
            },
            handlerViewExcel: function(item){
                console.log(item)
            },
            getExcelEditUrl: function(item){
                //update-begin---author:wangshuai ---date:20220215  for：[issues/I4SOSH]做完的积木报表，预览生成的访问地址，默认都加了token=null------------
                return this.splicingToken(api.index+item.id);
                //update-end---author:wangshuai ---date:20220215  for：[issues/I4SOSH]做完的积木报表，预览生成的访问地址，默认都加了token=null------------
            },
            getExcelViewUrl: function(item){
                //update-begin---author:wangshuai ---date:20220215  for：[issues/I4SOSH]做完的积木报表，预览生成的访问地址，默认都加了token=null------------
                return this.splicingToken(api.view+item.id);
                //update-end---author:wangshuai ---date:20220215  for：[issues/I4SOSH]做完的积木报表，预览生成的访问地址，默认都加了token=null------------
            },
            getLabelText1:function (createElement) {
                return createElement('div',
                    {
                        style:{color:'#fff'}
                    },
                    [
                        createElement('Icon',{props:{type:'ios-checkmark'}}),
                        '模板库'
                    ]
                )
            },
            onMenuSelect:function(name){
                this.menuitem = name;
                this.page={page: 1,size: 10,total: 0,};
                this.dataSource=[];
                this.name=""
                this.loadData();
            },
            //报表设计和模板案例点击事件
            tabsClick(name){
               this.name=""
               this.loadData(name)
            },
            //回车搜索事件
            enterSearchClick(){
                this.loadData()
            },
            //设置取消模版
            setTemplate: function(item,arg){
                var content = (arg == 1)?'是否确认设置为模板?':'是否确认取消模板?';
                let title = (arg == 1)?'收藏报表':'取消收藏报表'
                $http.confirm({
                    title:title,
                    content:content,
                    url:api.setTemplate,
                    method:'get',
                    data:{
                        id:item.id,
                        template:arg,
                        token:this.token
                    },
                    success:(result)=>{
                        this.loadData();
                    }
                },this);
            },
            handleUpload (file) {
                this.file = file;
                return true;
            },
            handleFormatError (file) {
                this.$Notice.warning({
                    title: '文件格式不正确',
                    desc: '文件 ' + file.name + ' 格式不正确，请上传 jpg 或 png 格式的图片。'
                });
            },
            handleMaxSize (file) {
                this.$Notice.warning({
                    title: '超出文件大小限制',
                    desc: '文件 ' + file.name + ' 太大，不能超过 2M。'
                });
            },
            handleSuccess (res) {
                if (res != null){
                    this.$Message.success(res.message);
                    this.dataSource.forEach((item,index,array)=>{
                        if (item.id === res.result.id){
                            item.thumb = res.result.thumb;
                        }
                    })
                }
            },
            handleSpinHide(){
               /* setTimeout(() => {
                    this.$Spin.hide();
                }, 3000);*/
            },
            //分享按钮点击事件
            handleShare(id){
                $http.get({
                    url:api.queryJurisdiction,
                    data:{reportId:id},
                    success:(result)=>{
                      if(result){
                          if(result.status=='0'){
                              let protocol = window.location.protocol;
                              let host = window.location.host;
                              //update-begin---author:wangshuai ---date:20221118  for：[issues/1383]yml中设置了项目前缀，分享链接地址有误------------
                              let url = protocol+"//"+host+BASE_URL;
                              //update-end---author:wangshuai ---date:20221118  for：[issues/1383]yml中设置了项目前缀，分享链接地址有误------------
                              result.previewUrl = url+result.previewUrl;
                              this.$refs.jurisdiction.jurisdictionData = result;
                              this.$refs.jurisdiction.shareUrlModal = true
                          }else{
                              //update-begin---author:wangshuai ---date:20220315  for：[issues/I4WWKE]分享链接的预览密码能否忽略------------
                              //兼容老数据没有是否显示密码的情况下;
                              //previewLockStatus 密码锁状态(0不存在密码锁，1存在密码锁);原来取消分享后的默认应该是1
                              if(!result.previewLockStatus){
                                result.previewLockStatus = "1"
                              }
                              //update-end---author:wangshuai ---date:20220315  for：[issues/I4WWKE]分享链接的预览密码能否忽略------------
                              this.$refs.jurisdiction.jurisdictionData = result;
                              this.$refs.jurisdiction.shareModal = true
                          }
                      }else{
                          this.$refs.jurisdiction.jurisdictionData.reportId=id
                          this.$refs.jurisdiction.shareModal = true
                      }
                    }
                })
            },
            // 获取缩略图的预览地址
            getThumbSrc(item){
                if(!item.thumb){
                    return "${base}"+"${customPrePath}"+"/jmreport/desreport_/corelib/jiade.jpg"
                }else{
                    if(item.thumb.indexOf('http')==0){
                        return item.thumb
                    }else{
                        return "${base}"+"${customPrePath}"+JM_VIEW_IMG_URL+"/"+item.thumb
                    }
                }
            }
        }
    })
</script>
 <#include "./common/tj.ftl">
</body>
</html>
