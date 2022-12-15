<style>
    .nameCla{
        color:#333;
    }
    nameCla:hover{
        color:#2d8cf0;
    }
    .on{
        color: #2d8cf0;
    }
    .demo-spin-icon-load{
        animation: ani-demo-spin 1s linear infinite;
    }
    @keyframes ani-demo-spin {
        from { transform: rotate(0deg);}
        50%  { transform: rotate(180deg);}
        to   { transform: rotate(360deg);}
    }
    .ivu-table-body table{
        width: 100% !important;
    }
    .param-style .ivu-table-cell-with-selection{
        text-overflow: unset;
    }
    .pointerIcon .ivu-icon{
        cursor: pointer;
    }
    .ivu-table-cell{
        text-overflow: unset;
    }
    .ivu-message{
        width:50%;
        left:27%;
    }
</style>
<script type="text/x-template" id="data-source-setting-template">
    <div>
        <!-- 新增数据集 弹框-begin -->
        <Modal
                fullscreen=true
                :loading="loading"
                width="100%"
                v-model="sqlModal"
                :title="moduleTitle"
                @on-cancel="clearDb"
                @on-ok="saveDbTip"
                class="dataSource"
                >
            <Row>
                <i-col span="4" v-if="sqlForm.dbType == 0">
                  <Icon size="25" v-if="forward=='0'" type="ios-arrow-back" style="float: left;margin-top: -12px;cursor: pointer;" @click="forwardClick('1')"/>
                  <Icon size="25" v-if="forward=='1'" type="ios-arrow-forward" style="float: left;margin-top: -12px;cursor: pointer;border-right: 1px solid #dcdee2;" @click="forwardClick('0')"/>
                </i-col>
            </Row>
            <Row>
                <i-col span="4" v-if="sqlForm.dbType == 0 && forward=='0'">
                    <Card style="height: auto;">
                        <div style="display: flex">
                            <i-select size="small" filterable :model.sync="sqlForm.dbSource" v-model="sqlForm.dbSource" @on-change="selectdbSource" clearable style="width:170px" :placeholder="daSourceDesc" @on-clear="handleClear">
                                <i-option v-for="item in maintainData" :value="item.id">{{ item.name }}</i-option>
                            </i-select>
                            <i-button size="small"  @click="sourceManage" v-if="sqlForm.dbType == 0" type="primary">维护</i-button>
                            <Tooltip :transfer="true" content="数据源维护文档" placement="top" class="jimu-tooltip">
                              <a class="jimu-table-tip help-color" href="http://report.jeecg.com/2084137" target="_blank"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
                            </Tooltip>
                        </div>
                        <i-input size="small" style="margin-top: 10px" search enter-button v-model="tableName" placeholder="表名查询" @on-search="queryTableName" />
                        <div style="margin-top: 10px" v-if="tableList&&tableList.length>0">
                          <Page
                                :total="tableTotal"
                                @on-change="handleCurrentChange"
                                :page-size="tablePageSize"
                                simple
                                style="text-align: center;">
                          </Page>
                           <#--表名列表-->
                               <List size="small" border :loading="tableLoading"   style="overflow-y: auto;height: 720px;margin-top: 10px" ref="taList">
                                   <ListItem v-for="(item,index) in tableList" :id="index">
                                       <Tooltip :content="item.name" transfer>
                                           <a href="Javascript:void(0)" @click="tableNameClick(item,index)" :class="{on:currentIndex === index,'nameCla':index===index}">
                                               {{item.name}}
                                           </a>
                                       </Tooltip>
                                   </ListItem>
                               </List>
                        </div>
                    </Card>
                </i-col>
                    <div v-if="sqlForm.dbType == 0 && forward=='1'" style="height: 880px;box-sizing:border-box;width: 26px;float: left;border-right: 1px solid #dcdee2;">
                    </div>
                <i-col :span="secondSpan">
                    <div style="margin-left: 20px">
                        <i-form ref="sqlForm"
                                :model="sqlForm"
                                :rules="sqlFormValidate"
                                inline :label-width="85">
                            <Row span="24">
                                    <form-item prop="dbCode" label="编码:">
                                        <i-input :disabled ="sqlForm.id!='' && sqlForm.id!=undefined" style="width: 153px" type="text" v-model="sqlForm.dbCode" placeholder="请输入编码">
                                        </i-input>
                                    </form-item>
                                    <form-item prop="dbChName" label="名称">
                                        <i-input type="text" style="width: 200px" v-model="sqlForm.dbChName" placeholder="请输入名称">
                                        </i-input>
                                    </form-item>
                                    <form-item>
                                        <Checkbox :checked.sync="sqlForm.isList" v-model="sqlForm.isList" @on-change="isListChange" style="width: 100px">是否集合</Checkbox>
                                    </form-item>
                                    <form-item>
                                        <#--<Checkbox :checked.sync="sqlForm.isPage" v-if="addIsPage == true" disabled v-model="sqlForm.isPage">是否分页</Checkbox>-->
                                        <Checkbox :checked.sync="sqlForm.isPage" v-if="sqlForm.isList == true && sqlForm.dbType != 3" v-model="sqlForm.isPage" @on-change="checkChange" style="width: 100px;margin-left: -86px">是否分页
                                          <Tooltip :transfer="true" content="分页文档" placement="top" class="jimu-tooltip">
                                            <a class="jimu-table-tip help-color" href="http://report.jeecg.com/2084139" target="_blank" style="font-size: 14px;"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
                                          </Tooltip>
                                        </Checkbox>
                                    </form-item>
                                    <form-item prop="apiMethod" label="请求方式:" v-if="sqlForm.dbType == 1">
                                        <i-select  style="width: 153px" v-model="sqlForm.apiMethod" placeholder="请输入请求方式">
                                            <i-option value="0">get</i-option>
                                            <i-option value="1">post</i-option>
                                        </i-select>
                                    </form-item>
                            </Row>
                            <Row span="24" v-if="sqlForm.dbType == 1">
                              <form-item prop="apiConvert" label="类转换器:">
                                <i-input style="width:530px" v-model="sqlForm.apiConvert" placeholder="类转换器"></i-input>
                              </form-item>
                            </Row>
                            <Row style="margin-top: 2%;">
                                <i-col span="24">
                                    <form-item prop="dbDynSql" label="报表SQL:" v-if="sqlForm.dbType == 0">
                                        <i-input v-model="sqlForm.dbDynSql"  @on-blur="dbDynSqlBlur"  type="textarea" :rows="4"  placeholder="请输入查询SQL" style="min-height: 100px;max-height: 620px;width:950px">
                                        </i-input>
                                    <div style="font-size: 10px;">
                                      <p style="margin-left: 14px">
                                        <ul>
                                          <li>如果id字段为字符串类型则需要加单引号:select * from table where id=${"'$"}${"{id}'"}。</li>
                                          <li>您可以编写${"'$"}${"{id}'"}做为一个参数,这里id是参数的名称。例如:select * from table where id=${"'$"}${"{id}'"}。</li>
                                          <li>您可以编写${'#'}${'{sysUserCode}'} 做为一个系统变量,这里sysUserCode是当前登录人。例如:select * from table where create_by=${"'#"}${"{sysUserCode}'"}。</li>
                                          <li>您可以编写存储过程CALL proc_sys_role(${'$'}${'{'}${'pageNo'}${"}"}, ${'$'}${'{'}${'pageSize'}${"}"}),CALL为开启存储过程。
                                            <Tooltip content="数据源用法文档" placement="top">
                                              <a class="jimu-table-tip help-color" href="http://report.jeecg.com/2384069" target="_blank" style="font-size: 14px"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
                                            </Tooltip>
                                          </li>  
                                      </ul>
                                      </p>
                                    </div>
                                    </form-item>
                                    <form-item prop="apiUrl" label="Api地址:" v-else-if="sqlForm.dbType == 1">
                                        <i-input v-model="sqlForm.apiUrl" @on-blur="dbApiBlur" type="textarea" :rows="4"  placeholder="请输入Api地址" style="min-height: 100px;max-height: 620px;width:950px">
                                        </i-input>
                                      <div style="font-size: 10px;">
                                        <p style="margin-left: 14px">
                                          <ul>
                                            <li>如果id字段为字符串类型则需要加单引号:http://127.0.0.1:8080/jeecg-boot/jimureport/test?id=${"$"}${"{id}"}。</li>
                                            <li>您可以编写${"#"}${"{sysDateTime}"}做为一个系统变量,这里sysDateTime是当前系统时间。例如:http://127.0.0.1:8080/jeecg-boot/jimureport/test?riqi=${"#"}${"{sysDateTime}"}。</li>
                                            <li>您可以简写访问路径，如：{ { domainURL } }/jimureport/test。
                                              <Tooltip content="数据源用法文档" placement="top">
                                                <a class="jimu-table-tip help-color" href="http://report.jeecg.com/2384069" target="_blank" style="font-size: 14px"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
                                              </Tooltip>
                                            </li>
                                          </ul>
                                        </p>
                                      </div>
                                    </form-item>
                                     <form-item prop="jsonData" label="json数据:" v-else="sqlForm.dbType == 3">
                                      <i-input v-model="sqlForm.jsonData" type="textarea" :rows="4"  placeholder="请输入JSON数据" style="min-height: 120px;max-height: 620px;width:950px">
                                      </i-input>
                                     </form-item>
                                    <i-button @click="handleSQLAnalyze" v-if="sqlForm.dbType == 0" type="primary">SQL解析</i-button>
                                    <Tooltip v-if="sqlForm.dbType == 0"  :transfer="true" content="SQL解析文档" placement="top">
                                        <a class="jimu-table-tip help-color" href="http://report.jeecg.com/2020087" target="_blank" style="font-size: 14px"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
                                    </Tooltip>
                                    <i-button @click="handleApiAnalyze" v-if="sqlForm.dbType == 1" type="primary">Api解析</i-button>
                                    <Tooltip v-if="sqlForm.dbType == 1"  :transfer="true" content="Api解析文档" placement="top">
                                        <a class="jimu-table-tip help-color" href="http://report.jeecg.com/2020177" target="_blank" style="font-size: 14px"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
                                    </Tooltip>
                                    <i-button @click="handleJsonAnalyze" v-if="sqlForm.dbType == 3" type="primary">JSON解析</i-button>
                                    <Tooltip v-if="sqlForm.dbType == 3"  :transfer="true" content="JSON解析文档" placement="top">
                                        <a class="jimu-table-tip help-color" href="http://report.jeecg.com/2290859" target="_blank" style="font-size: 14px;"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
                                    </Tooltip>
                                </i-col>
                            </Row>
                        </i-form>
                        <Spin fix v-if="spinShow">
                            <Icon type="ios-loading" size=18 class="demo-spin-icon-load"></Icon>
                            <div>Loading</div>
                        </Spin>
                        <Tabs v-model="tabValue" style="margin-top: 15px">
                            <tab-pane label="报表字段明细" name="1">
                                <i-button type="primary" @click="removeFieldTable" v-if="tab1.selectParamTables.length>0">删除</i-button>
                                <i-table style="padding-bottom: 10%;" ref="dynamicTable" @on-select="selectField" @on-select-all="selectFieldAll" @on-select-all-cancel="cancelFieldAll" @on-select-cancel="cancelField" stripe :columns="tab1.columns" :data="tab1.data" :height="tableHeight">
                                  <template slot-scope="{ row, index }" slot="extJsonFiled">
                                    <i-input size="small" v-model="row.extJson" @on-change="extJsonFiledChange(index,row.extJson)" @on-click="extJsonFiledClick(index,row.extJson)" icon="md-contract" class="pointerIcon" placeholder="请输入参数配置"/>
                                  </template>
                                </i-table>
                            </tab-pane>
                            <tab-pane label="报表参数" name="2" v-if="sqlForm.dbType != 3">
                                <i-button type="primary" @click="addParamTable">新增</i-button>
                                <i-button type="primary" @click="removeParamTable" v-if="tab2.selectParamTables.length>0">删除</i-button>

                                <i-table class="param-style" ref="paramTable" @on-select="selectParam" @on-select-all="selectParamAll" @on-select-all-cancel="cancelParamAll" @on-select-cancel="cancelParam" stripe :columns="tab2.columns" :data="tab2.data" :height="paramTableHeight">
                                    <template slot="paramName" slot-scope="props">
                                        <i-form :ref="'formDynamic'+props.idx" :model="props.row">
                                                <form-item prop="paramName" :rules="{required: true, message: '请输入参数', trigger: 'change'}" style="margin-top: 10px">
                                                    <i-input v-model="props.row.paramName" size="small" />
                                                </form-item>
                                        </i-form>
                                    </template>
                                    <template slot="paramTxt" slot-scope="props">
                                        <i-form :ref="'formTxtDynamic'+props.idx" :model="props.row">
                                            <form-item prop="paramTxt" style="margin-top: 10px">
                                                <i-input v-model="props.row.paramTxt" size="small" />
                                            </form-item>
                                        </i-form>
                                    </template>
                                    <template slot-scope="{ row, index }" slot="extJsonParam">
                                      <i-input size="small" v-model="row.extJson" @on-change="extJsonParamChange(index,row.extJson)" @on-click="extJsonParamClick(index,row.extJson)" icon="md-contract" class="pointerIcon" placeholder="请输入参数配置"/>
                                    </template>
                                </i-table>

                            </tab-pane>
                            <tab-pane label="数据预览" name="3" v-if="sqlForm.dbType == 0">
                                <span style="color: red">提示：仅显示10条数据</span>
                                <i-table ref="viewParamTable"  stripe :columns="tab3.columns" :data="tab3.data" :loading="tab3Loading"></i-table>
<#--                                <div style="float:right;margin-top:20px;">-->
<#--                                    <Page :total="tab3.page.total" @on-change="handleCurrentChange"></Page>-->
<#--                                </div>-->
                            </tab-pane>
                        </Tabs>
                    </div>
                </i-col>
            </Row>
        </Modal>
        <!-- 新增数据集 弹框-end -->

       <#--数据源维护 弹窗-begin-->
        <Modal
                class-name="vertical-center-modal"
                :fullscreen="dataSourceFull"
                :loading="loading"
                v-model="sourceModal"
                width="800"
                title="数据源维护"
                :closable="true"
                :mask-closable="false">
            <p slot="header">
              <span>数据源维护
                 <Tooltip style="cursor: pointer;float: right;margin-right: 30px;" v-if="sourceDraggable" :transfer="true" content="缩小" placement="top">
                     <Icon type="ios-contract" @click="dataSourceClick"/>
                 </Tooltip>
                 <Tooltip style="cursor: pointer;float: right;margin-right: 30px;" v-else :transfer="true" content="放大" placement="top">
                       <Icon type="ios-expand" @click="dataSourceClick"/>
                 </Tooltip>
              </span>
            </p>
            <div slot="footer">
              <i-button @click="saveSourceDb">关闭</i-button>
            </div>
            <Row>
                <i-col span="3">
                    <i-button @click="addDataSource" type="primary">新增</i-button>
                </i-col>
            </Row>
            <template>
                <i-table border :columns="sourceTab.columns" :data="sourceTab.data"  style="margin-top: 1%;"></i-table>
                <Page
                      style="margin-top: 10px;text-align: center"  
                      :total="sourceTabPage.total"
                      show-total
                      show-elevator
                      :page-size="sourceTabPage.size"
                      show-sizer
                      @on-change="sourceTabCurrentChange"
                      @on-page-size-change="sourceTabSizeChange"
                      size="small">
                </Page>
            </template>
        </Modal>

        <Modal :loading="loading" v-model="visibleData" title="数据源" :width="35" @on-cancel="clearDbSou" @on-ok="saveDataSource">
            <div style="padding-right: 30px">
                <i-form ref="dataSource" :model="dataSource" :rules="dataFormValidate" label-colon :label-width="100" >

                    <form-item prop="name" label="数据源名称" style="height:50px">
                        <i-input v-model="dataSource.name" placeholder="请输入数据源名称"></i-input>
                    </form-item>

                    <form-item prop="dbType" label="数据源类型" style="height:50px">
                        <i-select :model.sync="dataSource.dbType" v-model="dataSource.dbType" @on-change="selectdbType">
                            <i-option v-for="item in dataSourceTypeList" :value="item.value">{{ item.label }}</i-option>
                        </i-select>
                    </form-item>

                    <form-item prop="dbDriver" label="驱动类" style="height:50px" v-if="isNoSql">
                        <i-input v-model="dataSource.dbDriver" placeholder="请输入驱动类"></i-input>
                    </form-item>

                    <form-item prop="dbUrl" label="数据源地址" style="height:50px">
                        <i-input v-model="dataSource.dbUrl" placeholder="请输入数据源地址"></i-input>
                    </form-item>

                    <form-item prop="dbUsername" label="用户名" style="height:50px" v-if="isRedis">
                        <i-input v-model="dataSource.dbUsername" placeholder="请输入用户名"></i-input>
                    </form-item>

                    <form-item prop="dbPassword" label="密码" style="height:50px;width: 100%;">
                        <i-input style="width: calc(100% - 60px)" type="password" password v-model="dataSource.dbPassword" placeholder="请输入密码"></i-input>
                        <i-button size="small" style="width: 50px" @click="dataSourceTest" type="primary">测试</i-button>
                    </form-item>
                </i-form>
            </div>
        </Modal>
    <#--数据源维护 弹窗-end-->

    <#--删除确认弹窗-begin-->
        <Modal
                v-model="deleteParamModel"
                @on-ok="deleteParamTable"
                title="确认删除">
            <p><Icon type="ios-alert"  color="#f90" size="20px"></Icon>是否删除选中数据?</p>
        </Modal>
        <Modal
                v-model="deleteFieldModel"
                @on-ok="deleteFieldTable"
                title="确认删除">
            <p><Icon type="ios-alert"  color="#f90" size="16px"></Icon>是否删除选中配置?</p>
        </Modal>
        <Modal
              v-model="reportSql"
              title="报表SQL已修改"
              @on-ok="reportOk"
              @on-cancel="reportCancel">
            <p>报表SQL已修改，是否保存</p>
       </Modal>
    <#--删除确认弹窗-end-->
    <#--输入框参数配置放大-->
    <Modal draggable :loading="loading" v-model="extJsonShow" title="输入值" :width="1000" @on-ok="extJsonOk"
           @on-cancel="extJsonCancel">
      <i-form label-colon :label-width="100">
        <form-item label="参数配置">
          <i-input :autosize="{minRows: 10}" type="textarea" v-model="paramConfigData" placeholder="请输入参数配置"></i-input>
        </form-item>
      </i-form>
    </Modal>
    <j-sql-function-replace ref="sqlFunctionReplace" @functionok="functionOk"></j-sql-function-replace>  
    </div>
</script>

<script>
    const addUrlParam = function(base, key, value){
        if(base.includes("?")){
            base = base+"&"+key+"="+value
        }else{
            base = base+"?"+key+"="+value
        }
        return base;
    }
    Vue.component('j-data-source-setting', {
        template: '#data-source-setting-template',
        props: {
            settings: {
                type: Object,
                required: false,
                default: () => {
                }
            }
        },
        data(){
            return {
                forward:"1", //图片切换
                reportSql:false,//控制Modal当SQL报表改变的时候
                loading:true,
                tab3Loading:false,
                spinShow:false,
                menuitem : "printinfo",
                selectTableName : "",
                currentIndex:-1,
                moduleTitle: "",
                tableHeight: 0,
                paramTableHeight: 0,
                tabValue:"1",
                deleteParamModel:false,
                deleteFieldModel:false,
                dataSourceTypeList:[
                    {value: 'MYSQL5.5',label: 'MySQL5.5'},
                    {value: 'MYSQL5.7',label: 'MySQL5.7+'},
                    {value: 'ORACLE',label: 'Oracle'},
                    {value: 'SQLSERVER',label: 'SQLServer'},
                    {value: 'POSTGRESQL',label: 'PostgreSQL'},
                    {value: 'MARIADB',label: 'MariaDB'},
                    {value: 'dm',label: '达梦'},
                    {value: 'kingbase8',label: '人大金仓'},
                    {value: 'oscar',label: '神通'},
                    {value: 'DB2',label: 'DB2'},
                    {value: 'Hsqldb',label: 'Hsqldb'},
                    {value: 'Derby',label: 'Derby'},
                    {value: 'H2',label: 'H2'},
                    {value: 'other',label: '其他数据库'},
                    {value: 'redis',label: 'Redis'},
                    {value: 'mongodb',label: 'MongoDb'},
                    ],
                designerObj:{
                    id:"",
                    name:"",
                    type:"printinfo"
                },
                tab1: {
                    selectParamTables:[],
                    data: [],
                    columns: [
                        {
                            type: 'selection',
                            width: 35,
                            align: 'center'
                        },
                        {
                            type: 'index',
                            width: 60,
                            align: 'center'
                        },
                        {
                            title: '字段名',
                            key: 'fieldName',
                            width: '120',
                            render: (h, params) => {
                                return this.renderInput(h, params, 'fieldName','tab1')
                            }
                        },
                        {
                            title: '排序',
                            key: 'orderNum',
                            width: '80',
                            render: (h, params) => {
                                return this.renderInput(h, params, 'orderNum','tab1')
                            }
                        },
                        {
                            title: '字段文本',
                            width: '120',
                            key: 'fieldText',
                            render: (h, params) => {
                                return this.renderInput(h, params, 'fieldText','tab1')
                            }
                        },
                        {
                            title: '类型',
                            width: '120',
                            key: 'widgetType',
                            render: (h, params) => {
                                let options = [
                                    // 下拉选项
                                    {title: '数值类型', value: 'number'},
                                    {title: '字符类型', value: 'string'},
                                    {title: '日期类型', value: 'date'}
                                ];

                                return h('i-select', {
                                            props: {
                                                size:'small',
                                                clearable:true,
                                                value: this.tab1.data[params.index].widgetType,
                                            },
                                            on: {
                                                'on-change': (value) => {
                                                    this.tab1.data[params.index].widgetType = value;
                                                }
                                            },
                                        },
                                        options.map(item => {
                                            return h('i-option', {
                                                props: {
                                                    value: item.value
                                                }
                                            }, item.title)
                                        })
                                );
                            }
                        },
                        {
                            title: '字典code',
                            width: '120',
                            key: 'dictCode',
                            render: (h, params) => {
                                return this.renderInput(h, params, 'dictCode','tab1','请输入字典code或地址')
                            }
                        },
                        {
                            title: '查询',
                            width: '80',
                            key: 'searchFlag',
                            render:(h, params)=> {
                                return h('Checkbox',{
                                    props: {
                                        size:'small',
                                        value: this.tab1.data[params.index].searchFlag,
                                        trueValue: 1,
                                        falseValue: 0
                                    },
                                    on: {
                                        'on-change': (value) => {
                                            this.tab1.data[params.index].searchFlag = value;
                                            if(value==0){
                                                // _this.tab1.data[params.index].searchMode = null;
                                            }
                                        }
                                    }
                                });
                            }
                        },
                        {
                            title: '查询模式',
                            width: '120',
                            key: 'searchMode',
                            render: (h, params) => {
                                let options = [ // 下拉选项
                                    {title: '输入框', value: 1},
                                    {title: '下拉单选', value: 4},
                                    {title: '下拉多选', value: 3, tip:'须设置字典code'},
                                    {title: '范围查询', value: 2},
                                    {title: '模糊查询', value: 5},
                                    {title: '下拉树', value: 6, tip:'须在code栏设置接口地址'},
                                    {title: '自定义下拉框', value: 7}
                                ];

                                return h('i-select', {
                                            props: {
                                                size:'small',
                                                clearable: true,
                                                transfer: true,
                                                value: this.tab1.data[params.index].searchMode
                                            },
                                            on: {
                                                'on-change': (value) => {
                                                    // console.log(this.tab1.data)
                                                    this.tab1.data[params.index].searchMode = value;
                                                    // this.tab1.data
                                                }
                                            },
                                        },
                                        options.map(item => {
                                            let optionObject = {
                                                props: {
                                                    value: item.value
                                                }
                                            }
                                            if(item.tip){
                                                optionObject['attrs'] = {title: item.tip}
                                            }
                                            return h('i-option', optionObject, item.title)
                                        })
                                );
                            }
                        },
                        {
                            title: '查询默认值',
                            width: '120',
                            key: 'searchValue',
                            render: (h, params) => {
                                return this.renderInput(h, params, 'searchValue','tab1','请输入查询默认值')
                            }
                        },
                        {
                            title: '查询日期格式',
                            width: '120',
                            key: 'searchFormat',
                            render: (h, params) => {
                                return this.renderInput(h, params, 'searchFormat','tab1','请输入查询日期格式')
                            }},
                            {
                            title: '参数配置',
                            width: '120',
                            key: 'extJson',
                            slot: 'extJsonFiled'
                        }
                    ]
                },
                tab2:{
                    selectParamTables:[],
                    data: [],
                    columns: [
                        {
                            type: 'selection',
                            width: 35,
                            align: 'center'
                        },
                        {
                            type: 'index',
                            width: 60,
                            align: 'center'
                        },
                        {
                            title: '参数',
                            key: 'paramName',
                            width: '120',
                            render: (h, params) => {
                                this.tab2.data[params.index] = params.row;
                                return h(
                                "div", 
                                this.$refs.paramTable.$scopedSlots.paramName({
                                    row: params.row,
                                    idx: params.row._index
                                })
                                )
                            }
                        },
                        {
                            title: '排序',
                            key: 'orderNum',
                            width: '80',
                            render: (h, params) => {
                                return this.renderInput(h, params, 'orderNum','tab2')
                            }
                        },
                        {
                            title: '参数文本',
                            key: 'paramTxt',
                            width: '120',
                            render: (h, params) => {
                                this.tab2.data[params.index] = params.row;
                                return h(
                                        "div",
                                        this.$refs.paramTable.$scopedSlots.paramTxt({
                                            row: params.row,
                                            idx: params.row._index
                                        })
                                )
                            }
                        },
                        {
                            title: '类型',
                            key: 'widgetType',
                            width: '120',
                            render: (h, params) => {
                                let options = [
                                    {title: '数值类型', value: 'number'},
                                    {title: '字符类型', value: 'string'},
                                    {title: '日期类型', value: 'date'}
                                ];
                                return h('i-select', {
                                        props: {
                                            size:'small',
                                            clearable:true,
                                            transfer: true,
                                            value: this.tab2.data[params.index].widgetType,
                                        },
                                        on: {
                                            'on-change': (value) => {
                                                this.tab2.data[params.index].widgetType = value;
                                            }
                                        },
                                    },
                                    options.map(item => {
                                        return h('i-option', {
                                            props: {
                                                value: item.value
                                            }
                                        }, item.title)
                                    })
                                );
                            }
                        },
                        {
                            title: '字典code',
                            key: 'dictCode',
                            width: '120',
                            render: (h, params) => {
                                return this.renderInput(h, params, 'dictCode','tab2','请输入字典code或地址')
                            }
                        },
                        {
                            title: '查询',
                            key: 'searchFlag',
                            width: '80',
                            render:(h, params)=> {
                                return h('Checkbox',{
                                    props: {
                                        size:'small',
                                        value: this.tab2.data[params.index].searchFlag,
                                        trueValue: 1,
                                        falseValue: 0
                                    },
                                    on: {
                                        'on-change': (value) => {
                                            this.tab2.data[params.index].searchFlag = value;
                                            if(value==0){
                                                // _this.tab1.data[params.index].searchMode = null;
                                            }
                                        }
                                    }
                                });
                            }
                        },
                        {
                            title: '查询模式',
                            key: 'searchMode',
                            width: '120',
                            render: (h, params) => {
                                let options = [
                                    {title: '输入框', value: 1},
                                    {title: '下拉单选', value: 4},
                                    {title: '下拉多选', value: 3, tip:'须设置字典code'},
                                    {title: '下拉树', value: 6, tip:'须在code栏设置接口地址'},
                                    {title: '自定义下拉框', value: 7}
                                ];

                                return h('i-select', {
                                        props: {
                                            size:'small',
                                            clearable: true,
                                            transfer: true,
                                            value: this.tab2.data[params.index].searchMode
                                        },
                                        on: {
                                            'on-change': (value) => {
                                                this.tab2.data[params.index].searchMode = value;
                                            }
                                        },
                                    },
                                    options.map(item => {
                                        let optionObject = {
                                            props: {
                                                value: item.value
                                            }
                                        }
                                        if(item.tip){
                                            optionObject['attrs'] = {title: item.tip}
                                        }
                                        return h('i-option', optionObject, item.title)
                                    })
                                );
                            }
                        },
                        {
                            title: '默认值',
                            key: 'paramValue',
                            width: '120',
                            render: (h, params) => {
                                return this.renderInput(h, params, 'paramValue','tab2')
                            }
                        },
                        {
                            title: '查询日期格式',
                            key: 'searchFormat',
                            width: '120',
                            render: (h, params) => {
                                return this.renderInput(h, params, 'searchFormat','tab2','请输入查询日期格式')
                            }
                        },
                        {
                          title: '参数配置',
                          width: '120',
                          key: 'extJson',
                          slot: 'extJsonParam'
                        }
                    ]
                },
                tab3:{
                    columns:[],
                    page: { //分页参数
                        page: 1,
                        size: 10,
                        total: 0,
                    },
                    data:[]
                },
                sqlForm: {
                    dbCode: "",
                    dbChName: "",
                    dbDynSql: "",
                    dbType: "",
                    apiUrl: "",
                    apiMethod: "0",
                    isPage:true,
                    isList:true,
                    dbSource:"",
                    jsonData:"",
                    apiConvert:""
                },
                sqlModal: false,
                sqlFormValidate:{
                    dbCode:[
                        { required: true, message: '编码不能为空', trigger: 'blur' },
                        { validator: this.validateCodeExist, trigger: 'blur' }
                    ],
                    dbChName:[
                        { required: true, message: '名称不能为空', trigger: 'blur' }
                    ],
                    dbDynSql:[
                        { required: true, message: '报表SQL不能为空', trigger: 'blur' }
                    ],
                    apiUrl:[
                        { required: true, message: '请求地址不能为空', trigger: 'blur' }
                    ],
                    jsonData:[
                        { required: true, message: 'JSON数据不能为空', trigger: 'blur' }
                    ]
                },
                sourceModal: false,
                sourceTab:{
                    //selectParamTables:[],
                    data: [],
                    columns: [
                        {
                            type: 'index',
                            width: 60,
                            align: 'center'
                        },
                        {
                            title: '数据源名称',
                            key: 'name'
                        },
                        {
                            title: '数据库类型',
                            key: 'dbType',
                            render: (h, params) => {
                                switch (params.row.dbType) {
                                    case "MYSQL5.5":
                                        return h('span', 'MySQL5.5');
                                        break;
                                    case "MYSQL5.7":
                                        return h('span', 'MySQL5.7');
                                        break;
                                    case "ORACLE":
                                        return h('span', 'Oracle');
                                        break;
                                    case "SQLSERVER":
                                        return h('span', 'SQLServer');
                                        break;
                                    case "POSTGRESQL":
                                        return h('span', 'PostgreSQL');
                                    case "MARIADB":
                                        return h('span', 'MariaDb');
                                    case "kingbase8":
                                        return h('span', '人大进仓');
                                    case "dm":
                                        return h('span', '达梦');
                                    case "oscar":
                                        return h('span', '神通');
                                    case "SQLite":
                                        return h('span', 'SQLite');
                                    case "DB2":
                                        return h('span', 'DB2');
                                    case "Hsqldb":
                                        return h('span', 'Hsqldb');
                                    case "Derby":
                                        return h('span', 'Derby');
                                    case "H2":
                                        return h('span', 'H2');
                                    case "other":
                                        return h('span', '其他数据库');
                                    case "redis":
                                        return h('span', 'Redis');
                                    case "mongodb":
                                        return h('span', 'MongoDb');
                                }
                            }
                        },
                        {
                            title: '用户名',
                            key: 'dbUsername'
                        },
                        {
                            title: '操作',
                            key: 'action',
                            width: 150,
                            align: 'center',
                            render: (h, params) => {
                                return this.renderButton(h, params);
                            }
                        }
                    ]
                },
                visibleData: false,
                dataSource: {
                    id: "",
                    code: "",
                    reportId:"",
                    name: "",
                    dbType: "",
                    dbDriver: "",
                    dbUrl: "",
                    dbUsername:"",
                    dbPassword:""
                },
                dataFormValidate:{
                    name:[
                        { required: true, message: '数据源名称不能为空', trigger: 'blur' }
                    ],
                    dbType:[
                        { required: true, message: '数据源类型不能为空', trigger: 'blur' }
                    ],
                    dbDriver:[
                        { required: true, message: '驱动类不能为空', trigger: 'blur' }
                    ],
                    dbUrl:[
                        { required: true, message: '数据源地址不能为空', trigger: 'blur' }
                    ],
                    // dbUsername:[
                    //     { required: true, message: '用户名不能为空', trigger: 'blur' }
                    // ],
                    dbPassword:[
                        { required: false, message: '密码不能为空', trigger: 'blur' }
                    ]
                },
                tableList:[],
                oldDbDynSql:"", //旧的数据集
                tableName:"",
                tableTotal:0,//table数量
                tablePage:1, //table当前页
                tablePageSize:50,//当前页数
                tableLoading:false,//table加载
                dataSourceFull:false, //数据源维护弹窗放大缩小操作
                sourceDraggable:false, //数据源维护弹窗放大缩小操作按钮
                sourceTabPage:{
                   page:1,//当前页数
                   total:10,//总共条数
                   size:10 //每页显示条数
                },
                maintainData:[], //维护下拉数据
                paramConfigData:"", //参数配置弹窗数据
                extJsonShow:false, //参数配置弹窗是否显示
                tableIndex:-1, //当前table的下标
                tableType:-1, //判断当前是字段还是参数 0字段 1参数
                daSourceDesc:"选择数据源" //数据源维护描述的内容
            }
        },
        mounted(){
            //多数据源
            this.initDataSource();
            this.maintainDataSource();
        },
        computed: {
            secondSpan: function () {
                if(this.forward=='1' && this.sqlForm.dbType == "0"){
                    return 23;
                }
                return this.sqlForm.dbType == "0" ? 20 : 24;
            },
            isNoSql:function(){
                if(this.dataSource.dbType == 'redis' || this.dataSource.dbType == 'mongodb'){
                    return false;
                }
                return true;
            },
            isRedis:function(){
                return this.dataSource.dbType == 'redis'?false:true;
            }
        },
        watch: {
            tab1: {
                deep: true,
                immediate: true,
                handler: function (){
                    this.changeTab3Head()
                }
            },
            tabValue: {
                deep: true,
                immediate: true,
                handler: function (val){
                    if(val==='3'){
                        this.spinShow=true;
                        this.loadTableData(1,this.sqlForm.dbDynSql)
                    }
                }
            },
            tab2:{
                deep: true,
                immediate: true,
                handler: function (){
                }
            }
        },
        methods: {
            handleClear(){
              this.daSourceDesc="选择数据源"
            },
            changeTab3Head(){
                //获取table3表头
                let tab3Head=[];
                this.tab1.data.forEach((item,index)=>{
                    let temp={title: item.fieldText,key:item.fieldName,tooltip:true,width:150};
                    if(this.tab1.data.length>15){
                        if(index<3){
                            temp.fixed='left';
                        }
                    }
                    tab3Head.push(temp)
                });
                this.tab3.columns=tab3Head;
            },
            //校验数据集编码
            validateCodeExist(rule, value, callback){
                if(this.sqlForm.id){
                    callback();
                }
                let reg=/^(?!_)(?![0-9])[0-9a-zA-Z_]+(?<!_)$/;
                if(!reg.test(value)){
                    callback(new Error('编码支持字母和数字，且不能以数字和_开头'));
                }
                $jm.dataCodeExist(excel_config_id, value, (result)=> {
                    if(result === true){
                        callback('编码已存在!');
                    }else{
                        callback();
                    }
                })
            },
            onMenuSelect(name){
                this.menuitem = name;
                if (name === "sqlInfo") {
                    //sql
                    this.moduleTitle = "SQL数据集";
                    this.sqlForm.dbType = "0";
                }else if(name==="apiInfo"){
                    //api
                    this.moduleTitle = "Api数据集";
                    this.sqlForm.dbType = "1";
                }else if(name==="jsonInfo"){
                    //json
                    this.moduleTitle = "JSON数据集";
                    this.sqlForm.dbType = "3";
                }
                this.sqlForm.isList = true
                this.sqlForm.dbCode = "";
                this.sqlForm.dbChName = "";
                this.sqlForm.dbDynSql = "";
                this.sqlForm.apiUrl = "";
                this.sqlForm.dbSource = "";
                this.tab1.data = [];
                this.tab2.data = [];
                //update-begin--Author:wangshuai--Date:20211202--for:数据源新增的时候设置是否显示分页
                this.setIsPage();
                //update-end--Author:wangshuai--Date:20211202--for:数据源新增的时候设置是否显示分页
                this.sqlModal = true;
            },
            editById(dbId){
                this.tabValue="1";
                $http.get({url:api.loadDbData(dbId),success:(result)=>{
                        // console.log('result=====',result);
                        let reportResult = result;
                        if(!reportResult){
                            return;
                        }
                        //设置数据
                        this.sqlForm = reportResult.reportDb;
                        //update-begin--Author:wangshuai--Date:20211109--for:数据源key存在下拉框提示显示数据源id
                        if(this.sqlForm.dbSource){
                          this.daSourceDesc=this.sqlForm.dbSource
                        }
                        //update-end--Author:wangshuai--Date:20211109--for:数据源key存在下拉框提示显示数据源id
                        let bol = reportResult.reportDb.isPage;
                        let isList = reportResult.reportDb.isList;
                        this.tab1.data=reportResult.fieldList;
                        if(this.tab1.data){
                            this.tab1.data.forEach((item,index)=>{
                                item.tableIndex = index+1;
                            })
                        }
                        this.tab2.data=reportResult.paramList;
                        if(this.tab2.data){
                            this.tab2.data.forEach((item,index)=>{
                                item.tableIndex = index+1;
                            })
                        }
                        if (this.sqlForm.dbType === "0"){
                            this.moduleTitle = "SQL数据集";
                            this.oldDbDynSql = this.sqlForm.dbDynSql
                        }
                        if(this.sqlForm.dbType === "1") {
                            this.moduleTitle = "Api数据集";
                        }
                        if(this.sqlForm.dbType === "3"){
                            this.moduleTitle = "JSON数据集";
                        }
                        if (bol=='1'){
                            this.sqlForm.isPage = true;
                            this.sqlForm.isList = true;
                        }else {
                            this.sqlForm.isPage = false;
                        }
                        if(isList=='1'){
                            this.sqlForm.isList = true;
                        }else{
                            this.sqlForm.isList = false;
                            this.sqlForm.isPage = false;
                        }

                        this.handleDbSourceTable()
                        this.sqlModal = true;
                    }});
            },
            //设置分页是否显示，目前只支持一个分页显示
            setIsPage(){
              $http.get({url:api.queryIsPage(excel_config_id),data:{dbcode:this.sqlForm.dbCode},success:(result)=> {
                if(result){
                    this.sqlForm.isPage = false
                  }else{
                    this.sqlForm.isPage = true
                  } 
                }
              })
            },
            clearDb(){
                this.getReport();
                for(let key in this.sqlForm){
                    this.sqlForm[key] = "";
                }
                //update--begin-Author:wangshuai--Date:20211126--for:默认选中分页
                this.sqlForm.isPage=true;
                //update--end-Author:wangshuai--Date:20211126--for:默认选中分页
                this.sqlForm.isList=true;
                this.sqlForm.apiMethod="0";
                this.tab1.data = [];
                this.tab2.data = [];
                this.tab2.selectParamTables=[];
                //清除验证
                this.$refs['sqlForm'].resetFields();
                this.daSourceDesc="选择数据源";
            },
            getReport(){
                $http.get({url:api.getReport(excel_config_id),success:(result)=>{
                    // console.log("result====>",result)
                    if (result){
                        this.$emit('cancelback',result)
                        this.designerObj = result;
                    }
                }});
            },
            handleCurrentChange (val) {
                this.loadTableData(val,this.sqlForm.dbDynSql);
            },
            loadTableData(page,sql){
                if(page){
                    this.tab3.page.page = page;
                }
                let dbSource = this.sqlForm.dbSource;
                if(!this.selectTableName){
                    let dbDynSql=this.sqlForm.dbDynSql;
                    if(dbDynSql){
                        let form_number = dbDynSql.toLowerCase().indexOf("from ");
                        let where_number = dbDynSql.toLowerCase().indexOf(" where");
                        let tableName = (where_number>-1?dbDynSql.substring(form_number+4,where_number):dbDynSql.substring(form_number+4)).trim();
                        this.selectTableName=tableName;
                    }
                }
                if(!this.selectTableName){
                   this.spinShow=false; 
                   return;
                }
                if(this.tabValue==='3' && !this.sqlForm.dbDynSql){
                    this.spinShow=false;
                    return;
                }
                //update-begin--Author:wangshuai--Date:20211014--for:参数为空不传递
                let paramArray = [];
                for (const paramElement of this.tab2.data) {
                  let paramValue = paramElement.paramValue;
                  if(paramValue){
                    paramArray.push(paramElement)
                  }
                }
                //update-begin---author:wangshuai ---date:20220221  for：[issues/I4OXTC]SQL数据集里数据预览报错
                let data={dbSource:dbSource,sql:sql,tableName:this.selectTableName, pageNo:1,pageSize:10};
                if(paramArray.length>0){
                  data.paramArray=JSON.stringify(paramArray)
                }
                //update-end---author:wangshuai ---date:20220221  for：[issues/I4OXTC]SQL数据集里数据预览报错
                //update-end--Author:wangshuai--Date:20211014--for:参数为空不传递
                this.tab3Loading=true;
                //update-begin-author:taoyan date:20220324 for: JMREP-2518【安全】动态执行sql的接口，加上签名check，参考jeecgboot
                let header = SignMd5Util.getHeader(api.loadTableData, data)
                $http.post({
                    url:api.loadTableData,
                    data:data,
                    timeout:10000,
                    contentType: 'json',
                    header: header,
                //update-end-author:taoyan date:20220324 for: JMREP-2518【安全】动态执行sql的接口，加上签名check，参考jeecgboot
                    success:(result)=>{
                        this.tab3Loading=false;
                        this.spinShow=false;
                        // console.log("loadTableData====>result",result)
                        this.tab3.data=result.records;
                        this.tab3.page.total=result.total;
                    },
                    fail:(res)=>{
                        this.tab3Loading=false;
                        this.spinShow=false;
                    },
                    error:()=>{
                        this.tab3Loading=false;
                        this.spinShow=false;
                    }
                })
            },
            //保存之前先判断报表SQl是否已改变
            saveDbTip(){
                //当为数据源SQL并且报表SQl已改变则提示
                if(this.sqlForm.dbType=='0' && this.oldDbDynSql && this.oldDbDynSql!=this.sqlForm.dbDynSql){
                    this.reportSql=true
                }else{
                    this.saveDb();  
                }
            },
            saveDb(){
                this.$refs.sqlForm.validate((valid)=>{
                    if(valid){
                      this.childrenRules().then(res =>{
                      if(res){
                        //保存表单
                        let reportDb = {};
                        reportDb.id = this.sqlForm.id;
                        reportDb.jimuReportId = excel_config_id;
                        reportDb.dbCode = this.sqlForm.dbCode;
                        reportDb.dbChName = this.sqlForm.dbChName;
                        reportDb.dbType = this.sqlForm.dbType;
                        reportDb.dbSource = this.sqlForm.dbSource;
                        reportDb.jsonData = this.sqlForm.jsonData;
                        reportDb.apiConvert = this.sqlForm.apiConvert
                        if (this.sqlForm.isList==true){
                            reportDb.isList = '1'
                        }else{
                            reportDb.isList = '0'
                            reportDb.isPage = '0'
                        }
                        if (this.sqlForm.isPage==true){
                            /*if (this.addIsPage){
                                reportDb.isPage = '0'
                            } else {
                                reportDb.isPage = '1'
                            }*/
                            reportDb.isPage = '1'
                            reportDb.isList = '1'
                        } else {
                            reportDb.isPage = '0'
                        }
                        if (this.sqlForm.dbType == "0"){
                            reportDb.dbDynSql = this.sqlForm.dbDynSql;
                        } else {
                            reportDb.apiUrl = this.sqlForm.apiUrl;
                            reportDb.apiMethod = this.sqlForm.apiMethod;
                        }
                        reportDb.fieldList = this.tab1.data; //解析出表字段
                        reportDb.paramList = this.tab2.data; //动态表单参数
                        //saveDb这个请求后台实体中没用的参数不传
                        let params = "apiUrl,apiMethod,dbChName,dbCode,dbDynSql,dbSource,dbType,fieldList,id,isList,isPage,jimuReportId,paramList,jsonData,apiConvert"
                        let reportNewDb = {};
                        for (const key in reportDb) {
                            if(params.includes(key)){
                                reportNewDb[key]=reportDb[key]
                            }
                        }
                        $http.post({
                            url: api.saveDb,
                            contentType:'json',
                            data:JSON.stringify(reportNewDb),
                            success:(res)=>{
                                this.$emit('saveback',res.id);
                                for(let key in this.sqlForm){
                                    this.sqlForm[key] = "";
                                }
                                //update--begin-Author:wangshuai--Date:20211126--for:默认选中分页
                                this.sqlForm.isPage=true;
                                //update--end-Author:wangshuai--Date:20211126--for:默认选中分页
                                this.sqlForm.apiMethod="0";
                                this.tab1.data = [];
                                this.tab2.data = [];
                                this.sqlModal = false;
                                this.oldDbDynSql=""
                                this.tab2.selectParamTables=[];
                                //清除验证
                                this.$refs['sqlForm'].resetFields();
                            },
                            finally:()=>{
                                setTimeout(() => {
                                    this.loading = false
                                    this.$nextTick(() => {
                                        this.loading = true
                                    })
                                }, 500)
                                this.daSourceDesc="选择数据源";
                            }, 
                        });
                        return;
                    }else{
                        setTimeout(() => {
                            this.loading = false
                            this.$nextTick(() => {
                                this.loading = true
                            })
                        }, 500)
                        return;
                    }
                });
                }else{
                    setTimeout(() => {
                        this.loading = false
                        this.$nextTick(() => {
                            this.loading = true
                        })
                    }, 500)
                    return;
                }
              });
            },
            //参数加验证不为空
            async childrenRules(){
                let success = true
                //为空直接length可能出现问题
                if(this.tab2.data && this.tab2.data.length>0){
                  for (let i = 0, len = this.tab2.data.length; i < len; i++) {
                    this.$refs['formDynamic' + i].validate(valid => {
                      if (!valid) {
                        success=false
                      }
                    })
                    this.$refs['formTxtDynamic' + i].validate(valid => {
                      if (!valid) {
                        success=false
                      }
                    })
                  }
                }
                return success;
            },
            isListChange(isList){
                //update--begin-Author:wangshuai--Date:20211129--for:选择列表的时候如果有分页不选择，如果没有分页就勾选上
                if(isList){ 
                  this.setIsPage()
                }else{
                  //update-begin--Author:wangshuai--Date:20211203--for:如果不是集合是否分页应该去掉
                  this.sqlForm.isPage = false
                  //update-end--Author:wangshuai--Date:20211203--for:如果不是集合是否分页应该去掉
                }
                //update--end-Author:wangshuai--Date:20211129--for:选择列表的时候如果有分页不选择，如果没有分页就勾选上
            },
            checkChange(ispage){
                if (ispage){
                    $http.get({url:api.queryIsPage(excel_config_id),data:{dbcode:this.sqlForm.dbCode},success:(result)=>{
                            if (result){
                                this.$Modal.confirm({
                                    content: '已有数据集分页,是否更改?',
                                    onOk: () => {
                                        this.sqlForm.isPage = true;
                                        this.sqlForm.isList = true;
                                    },
                                    onCancel: ()=> {
                                        this.sqlForm.isPage = false;
                                    }
                                });
                            }
                        }});
                }
            },
            selectdbSource(val){
                //this.clearSqlForm()
                this.sqlForm.dbSource = val;
                this.tableName = ""
                /*加载数据源的表信息*/
                this.handleDbSourceTable(api.loadTable,1)
            },
            //根据表明查询所有
            queryTableName(){
                let dbSource = this.sqlForm.dbSource;
                if(dbSource){
                    this.tablePage = 1
                    this.handleDbSourceTable(api.queryTableName,2)
                }else{
                    this.$Message.warning("请先选择数据源！")
                }
            },
            clearSqlForm(){
                this.sqlForm.dbDynSql="";
                this.tab1.data = [];
                this.tab2.data = [];
            },
            //type 1全部 2查询
            handleDbSourceTable(api,type){
                let dbSource = this.sqlForm.dbSource;
                if(!dbSource){
                    this.tableList=[];
                    this.tablePage=1
                    return;
                }
                let param={}
                param.dbSource=dbSource
                param.pageNo=this.tablePage
                param.pageSize=50
                if(type == 2){
                  param.tableName=this.tableName
                }
                this.tableLoading = true
                $http.post({
                    url:api,
                    data:param,
                    success:(result)=>{
                        //设置分页
                        if(result){
                          this.tableList=result.pageList
                          this.tableTotal = result.total
                        }else{
                          this.tableList=[]
                          this.tableTotal = 0
                        }
                        //存在dbDynSql，就是编辑的状态
                        let dbDynSql=this.sqlForm.dbDynSql;
                        if(dbDynSql){
                            let form_number = dbDynSql.toLowerCase().indexOf("from ");
                            let where_number = dbDynSql.toLowerCase().indexOf(" where");
                            let tableName = (where_number>-1?dbDynSql.substring(form_number+4,where_number):dbDynSql.substring(form_number+4)).trim();
                            this.selectTableName=tableName;
                            this.currentIndex=this.tableList.findIndex((item) => item.Name === tableName);
                            /*this.$nextTick(() => {
                                document.getElementById(this.currentIndex+'').scrollIntoView()
                            })*/
                        }
                       this.tableLoading = false
                    }
                })
            },
            tableNameClick(item,index){
               this.currentIndex=index;
               this.tabValue="1";
               let sql = "select *  from "+ item.name;
               this.sqlForm.dbDynSql=sql;
               this.selectTableName=item.name
            },
            sourceManage(){
                this.sourceModal = true;
            },
            //type 0不需要提示 1需要提示
            handleSQLAnalyze(type) {
                let dbDynSql = this.sqlForm.dbDynSql;
                let dbSource = this.sqlForm.dbSource;
                if(!dbDynSql){
                    return;
                }
                let reqData = {'sql': dbDynSql, 'dbSource': dbSource,'type':type=='1'?'1':'0'}
                //update-begin--Author:taoyan  Date:202108130  for：第一次点击sql解析 明明设置有默认值 但是没传 非要弹框后再传？
                //update-begin--Author:wangshuai--Date:20211014--for:参数为空不传递
                let paramArray = [];
                for (const paramElement of this.tab2.data) {
                  let paramValue = paramElement.paramValue;
                  if(paramValue){
                    paramArray.push(paramElement)
                  }
                }
                if(paramArray && paramArray.length>0) {
                  reqData['paramArray'] = JSON.stringify(paramArray);
                }
                //update-end--Author:wangshuai--Date:20211014--for:参数为空不传递
                //update-end--Author:taoyan  Date:202108130  for：第一次点击sql解析 明明设置有默认值 但是没传 非要弹框后再传？
                //update-begin--Author:liusq  Date:20210812  for：mongodb数据源传递报表参数--------------------
                if(dbDynSql.indexOf("db.getCollection")== 0){
                    reqData['paramArray'] = JSON.stringify(this.tab2.data);
                }
                //update-end--Author:liusq  Date:20210812  for：mongodb数据源传递报表参数--------------------
                //update-begin-author:taoyan date:20220324 for: JMREP-2518【安全】动态执行sql的接口，加上签名check，参考jeecgboot
                let header = SignMd5Util.getHeader(api.queryFieldBySql, reqData)
                let dataStr = JSON.stringify(reqData);
                $http.post({
                    url: api.queryFieldBySql,
                    contentType: 'json',
                    data: dataStr,
                    header: header,
                //update-end-author:taoyan date:20220324 for: JMREP-2518【安全】动态执行sql的接口，加上签名check，参考jeecgboot
                    success:(result)=>{
                      let message = result['message'];
                        if(message){
                            //Vue.prototype.$Message.error(message);
                            //弹窗sql解析报错字段
                            if(dbDynSql.indexOf("$")!=-1){
                              this.$refs.sqlFunctionReplace.sqlFunctionShow = true
                              this.$refs.sqlFunctionReplace.initDataSource(this.sqlForm.dbDynSql, this.sqlForm.dbSource, this.tab2.data)
                            }else{
                              Vue.prototype.$Message.error(message);
                            }
                        }else{
                      let resultElement = result['fieldList'];
                      if(this.tab1.data && resultElement){
                          let data = this.tab1.data;
                          //update-begin--Author:wangshuai  Date:20210430  for：sql动态报表配置明细如果字段存在就不替换，否则替换，要不然会引起数据清空--------------------
                          data = data.concat(resultElement)  
                          //先去除没有的数据
                          let newJson = [];
                          //找到不重复的数据
                          for (const datum of data) {
                              //建立标记，判断数据是否重复，true为不重复
                              let flag = true;
                              for (const results of newJson) {
                                  //循环数据删除this.tab1.data已经存在的数据
                                  if(datum['fieldName'] == results['fieldName']){
                                    flag = false;
                                  }
                              }
                            //判断是否重复
                            if(flag == true){
                              //不重复的放入新数组。  新数组的内容会继续进行上边的循环。
                              newJson.push(datum);
                            } 
                        }
                          //去除没有的数据
                          newJson = newJson.filter(item => resultElement.some(value => value.fieldName == item.fieldName))
                          //循环给当前剔除的数组加上index和排序及查询框
                          let newData=[];
                          let i =0;
                          for (const results of newJson) {
                              results.tableIndex = i
                              if(!results){
                                 results.searchFlag = 0
                              }
                              results.orderNum = i
                              results.extJson=""
                              newData.push(results);
                              i++;
                          }
                          this.tab1.data = newData
                          //update-end--Author:wangshuai  Date:20210430  for：sql动态报表配置明细如果字段存在就不替换，否则替换，要不然会引起数据清空--------------------
                      }else{
                        this.tab1.data = resultElement;
                        this.tab1.data.forEach((item,index)=>{
                            item.tableIndex = index+1;
                            item.searchFlag = 0
                        })
                      } 
                      }
                    },
                    fail:(res)=>{
                      //1001注入异常
                      if(res.code!=1001 && type!=1){
                        //弹窗sql解析报错字段
                        this.$refs.sqlFunctionReplace.sqlFunctionShow = true
                        this.$refs.sqlFunctionReplace.initDataSource(this.sqlForm.dbDynSql,this.sqlForm.dbSource,this.tab2.data)
                      }
                    }
                })
            },
            handleApiAnalyze(){
                let dbDynApi = this.sqlForm.apiUrl.trim();
                if(!dbDynApi){
                    return;
                }

                /**
                 * api地址解析规则说明
                 * 1. 解析地址获取参数map
                 * 2. 参数判断是否是表达式
                 * 3、如果是表达式 根据 this.tab2.data的默认值替换表达式
                 * 4.如果是表达式且没有默认值 就传空
                 */
                //update-begin-author:taoyan date:20210506 for:报表参数问题修复
                let urlParam =  {}
                let pIndex = dbDynApi.indexOf("?")
                if(pIndex!=-1){
                    let str = dbDynApi.substr(pIndex+1);
                    let arr = str.split("&");
                    for(let i = 0; i < arr.length; i++) {
                        let subArr = arr[i].split("=")
                        urlParam[subArr[0]]=subArr[1]
                    }
                    dbDynApi = dbDynApi.substr(0,dbDynApi.indexOf("?"));
                }
                if(urlParam){
                    let paramList = this.tab2.data || [];
                    Object.keys(urlParam).map(k=>{
                        let expressValue = urlParam[k]
                        if(expressValue && expressValue.indexOf('$\{')>=0){
                            //说明是表达式  表达式看是否有默认值 有的话替掉
                            let realParamName = expressValue.replace(/'/g, '').replace("$\{","").replace("}","").trim();
                            let temp = paramList.filter(item=>{
                                return item.paramName == realParamName
                            })
                            if(temp && temp.length>0){
                                dbDynApi = addUrlParam(dbDynApi, k, temp[0].paramValue)
                            }else{
                                dbDynApi = addUrlParam(dbDynApi, k, '')
                            }
                        }else{
                            dbDynApi = addUrlParam(dbDynApi, k, expressValue)
                        }
                    })
                }
                //update-end-author:taoyan date:20210506 for:报表参数问题修复

                let apiMethod = this.sqlForm.apiMethod;
                $http.post({
                    url:api.executeSelectApi,
                    data:{
                        api:dbDynApi,
                        method:apiMethod,
                        apiConvert:this.sqlForm.apiConvert,
                        paramArray:JSON.stringify(this.tab2.data)
                    },
                    success:(result)=>{
                      //update-begin--Author:wangshuai--Date:20211109--for:api解析为空提示
                      if(result && result.length>0){
                        Vue.prototype.$Message.success("解析成功！");
                      }else{
                        Vue.prototype.$Message.warning("数据为空，报表字段明细会被清空！");
                      }
                      //update-end--Author:wangshuai--Date:20211109--for:api解析为空提示
                      //update-begin--Author:wangshuai  Date:20210430  for：api动态报表配置明细如果字段存在就不替换，否则替换，要不然会引起数据清空--------------------
                      let data = this.tab1.data;
                      data = data.concat(result)
                      //先去除没有的数据
                      let newJson = [];
                      //找到不重复的数据
                      for (const datum of data) {
                        //建立标记，判断数据是否重复，true为不重复
                        let flag = true;
                        for (const results of newJson) {
                          //循环数据删除this.tab1.data已经存在的数据
                          if(datum['fieldName'] == results['fieldName']){
                            flag = false;
                          }
                        }
                        //判断是否重复
                        if(flag == true){
                          //不重复的放入新数组。  新数组的内容会继续进行上边的循环。
                          newJson.push(datum);
                        }
                      }
                      //去除没有的数据
                      newJson = newJson.filter(item => result.some(value => value.fieldName == item.fieldName))
                      //循环给当前剔除的数组加上index和排序及查询框
                      let newData=[];
                      let i =0;
                      for (const results of newJson) {
                        results.tableIndex = i
                        if(!results){
                          results.searchFlag = 0
                        }
                        results.orderNum = i
                        results.extJson = ""
                        newData.push(results);
                        i++;
                      }
                      this.tab1.data = newData;
                      //update-end--Author:wangshuai  Date:20210430  for：api动态报表配置明细如果字段存在就不替换，否则替换，要不然会引起数据清空--------------------
                    }
                })
            },
            handleJsonAnalyze(){
              let jsonData = this.sqlForm.jsonData
              let  parseData = this.isJSON(jsonData)
              if(parseData){
                parseData = parseData.data;
                let parseDatum = parseData[0];
                let newJsonData = []
                let orderNum = 0;
                for (const parseDatumKey in parseDatum) {
                  newJsonData.push({
                    'fieldName':parseDatumKey,
                    'fieldText':parseDatumKey,
                    'widgetType':'String',
                    'isShow':true,
                    'orderNum':orderNum++
                   })
                }
                this.tab1.data = newJsonData;
              }
            },
            //判断是否为json
            isJSON(str){
              if (typeof str == 'string') {
                try {
                  let obj=JSON.parse(str);
                  if(typeof obj == 'object' && obj ){
                    return obj;
                  }else{
                    this.$Message.warning("请输入正确的JSON格式")
                  }
                } catch(e) {
                  this.$Message.warning("请输入正确的JSON格式")
                }
              }
            },
            removeFieldTable(){
                this.deleteFieldModel = true;
            },
            selectFieldAll(){
                this.tab1.selectParamTables = this.tab1.data.map(item=>
                {
                    return {"tableIndex":item.tableIndex,"id":item.id}
                });
            },
            cancelFieldAll(){
                this.tab1.selectParamTables = [];
            },
            selectField(selection,row){
                this.tab1.selectParamTables=[...this.tab1.selectParamTables,{"tableIndex":row.tableIndex,"id":row.id}];
            },
            cancelField(selection,row){
                this.tab1.selectParamTables = this.tab1.selectParamTables.filter(item=>item.tableIndex!=row.tableIndex);
            },
            dbDynSqlBlur(){
                //update-begin--Author:liusq  Date:20210812  for：mongodb数据源不进行$参数解析--------------------
                if(this.sqlForm.dbDynSql.indexOf("db.getCollection")== 0){
                    return false
                }
                //update-end--Author:liusq  Date:20210812  for：mongodb数据源不进行$参数解析--------------------
                //获得原数据Map
                let dataMap={}
                if(this.tab2.data && this.tab2.data.length>0){
                    this.tab2.data.forEach(item=>{
                        dataMap[item.paramName] = item;
                    })
                }

                let dbDynSql = this.sqlForm.dbDynSql;

                //update-begin---author:wangshuai   Date:20211221  for：[JMREP-2505]存储过程没有空格参数会连接到一起------------
                let reg=/\$\{[^}]*\}/g;
                //update-end---author:wangshuai   Date:20211221  for：[JMREP-2505]存储过程没有空格参数会连接到一起------------
                if(!reg.test(dbDynSql)){
                    return;
                }

                let dbDynSqlArr = dbDynSql.match(reg);
                let paramsArr = [];
                if(dbDynSqlArr && dbDynSqlArr.length>0){
                    //update-begin-author:wangshuai date:20210621 for: sql数据源会将下方的新增的参数冲刷掉-Infinity tb JMREP-2110
                    let indexArr = this.tab2.data.map(item=>item.tableIndex);
                    if(indexArr && indexArr.length==0){
                      indexArr=[0]
                    }
                    //update-begin-author:wangshuai date:20210621 for: sql数据源会将下方的新增的参数冲刷掉-Infinity tb JMREP-2110
                    //防止参数重复
                    let paramExistArray = []
                    let number = Math.max(...indexArr);
                    dbDynSqlArr.forEach((item,index)=>{
                        item = item.replace("$\{","").replace("}","").trim();
                        if(paramExistArray.indexOf(item)<0 && item){
                            paramExistArray.push(item)
                            let paramObj = {};
                            paramObj.paramName = item;
                            paramObj.paramTxt = item;
                            paramObj.orderNum = number+1;
                            paramObj.tableIndex = paramObj.orderNum;
                            const oldItem =  dataMap[item];
                            paramObj.id = (oldItem && oldItem.id) || "";
                            paramObj.paramValue = (oldItem && oldItem.paramValue) || "";
                            paramObj.extJson =""
                            paramsArr.push(paramObj);
                            number++
                        }
                    })
                }
                //update-begin-author:wangshuai date:20210621 for: sql数据源会将下方的新增的参数冲刷掉-Infinity tb JMREP-2110
                let arr = [...paramsArr]
                for (const arrElement of arr) {
                  //循环判断是否有重复的数据，没有重复的数据则添加到参数栏中
                  let paramName = this.tab2.data.some(value => value.paramName == arrElement.paramName);
                  if(!paramName){
                    this.tab2.data.push(arrElement)
                  }
                }
                //update-end-author:wangshuai date:20210621 for: sql数据源会将下方的新增的参数冲刷掉-Infinity tb JMREP-2110
            },
            //API解析
            dbApiBlur(){
                //获得原数据Map
                let dataMap={}
                if(this.tab2.data && this.tab2.data.length>0){
                    this.tab2.data.forEach(item=>{
                        dataMap[item.paramName] = item;
                    })
                }
                let apiUrl = this.sqlForm.apiUrl;
                apiUrl=apiUrl.trim();
                //判断是否包含问号
                if(apiUrl.indexOf("?")==-1){
                    return ;
                }
                let apiUrlArr = apiUrl.substr(apiUrl.indexOf("?"),apiUrl.length-1);
                //去除?和&
                let urlArr=apiUrlArr.split(/[?&]/)
                let paramsArr = [];
                let indexArr = this.tab2.data.map(item=>item.tableIndex);
                if(indexArr && indexArr.length==0){
                  indexArr=[0]
                }
                let number = Math.max(...indexArr);
                if(urlArr && urlArr.length>0){
                    urlArr.forEach((item,index)=>{
                        if(item.indexOf("=")!=-1){
                           let strings = item.split("=");
                           if(strings.length>1){
                            let value =strings[1]
                            if(value.indexOf("$")!=-1){
                            value = value.replace(/'/g, '').replace("$\{","").replace("}","").trim();
                            let b = this.tab2.data.some(item => item.paramName == value);
                            if(!b){
                            number++
                            let paramObj = {};
                            paramObj.paramName = value;
                            paramObj.orderNum = number;
                            paramObj.tableIndex =  number;
                            const oldItem =  dataMap[item];
                            paramObj.id = (oldItem && oldItem.id) || "";
                            paramObj.paramValue = (oldItem && oldItem.paramValue) || "";
                            paramObj.extJson = "";
                            paramsArr.push(paramObj);
                            }
                            }
                           }
                        }
                    })
                    //update-begin--Author:wangshuai  Date:20210430  for：api参数如果存在就不替换，否则替换--------------------
                    let tabData = this.tab2.data;
                    let paramData = [...paramsArr];
                    tabData = tabData.concat(paramData);
                    let newJson=[];
                    //找到不重复的数据
                    for (const datum of tabData) {
                      //建立标记，判断数据是否重复，true为不重复
                      let flag = true;
                      for (const results of newJson) {
                        //循环数据删除this.tab1.data已经存在的数据
                        if(datum['paramName'] == results['paramName']){
                          flag = false;
                        }
                      }
                      //判断是否重复
                      if(flag == true){
                        //不重复的放入新数组。  新数组的内容会继续进行上边的循环。
                        newJson.push(datum);
                      }
                    }
                    this.tab2.data = newJson;
                    //update-end--Author:wangshuai  Date:20210430  for：api参数如果存在就不替换，否则替换--------------------
                    this.sqlForm.apiUrl = apiUrl
                }
            },
            addParamTable(){
                let indexArr = this.tab2.data.map(item=>item.tableIndex);
                let orderNumArr = this.tab2.data.map(item=>item.orderNum);
                if(indexArr.length==0){
                    indexArr=[0];
                }
                if(orderNumArr.length==0){
                    orderNumArr=[0];
                }
                this.tab2.selectParamTables = [];
                this.tab2.data=[...this.tab2.data,{
                    'paramName':"",
                    'paramTxt':"",
                    'paramValue':"",
                    'orderNum':Math.max(...orderNumArr)+1,
                    'tableIndex':Math.max(...indexArr)+1,
                    'extJson':""
                }];
            },
            removeParamTable(){
                this.deleteParamModel = true;
            },
            deleteParamTable(){
              if(this.sqlForm.dbType == 0){
                let tableIndexArr = this.tab2.selectParamTables.map(item=>item.tableIndex);
                this.tab2.data = this.tab2.data.filter(item=>!tableIndexArr.includes(item.tableIndex));
                let selectTableObj = this.tab2.selectParamTables.filter(item=>item.id);
                let selectIds = selectTableObj.map(item=>item.id);
                this.tab2.selectParamTables = [];
                let dbDynSql  = this.sqlForm.dbDynSql;
                //如果没有where就不截取了·
                //update-begin--Author:wangshuai -- Date:20210324 ----for：删除多个的时候会出现bug，如果这样子写无论是什么上面的sql语句（如：or）都会拼接and------  
                // if(dbDynSql.indexOf("where")!=-1){
                //     dbDynSql = dbDynSql.substring(0,dbDynSql.toLowerCase().indexOf("where"));    
                // }
                // let paramArr = []
                // if(this.tab2.data.length>0){
                //     // dbDynSql = dbDynSql+" where ";
                //     this.tab2.data.forEach(item=>{
                //         if(item.paramName && dbDynSql.contains(item.paramName)){
                //             //TODO 
                //             const paramName = `$\{item.paramName}`;
                //             console.log("paramName:",paramName)
                //             paramArr.push(`$\{paramName\}='`+"$\{"+paramName+"}'")  
                //         }
                //     })
                // }
                // dbDynSql = dbDynSql+paramArr.join(" and ");
                // this.sqlForm.dbDynSql=dbDynSql.trim();
                //update-end--Author:wangshuai -- Date:20210324 ----for：删除多个的时候会出现bug，如果这样子写无论是什么上面的sql语句（如：or）都会拼接and------  
                const deleParams={selectIds,id:this.sqlForm.id,dbDynSql}
                //后台删除,保存时删除
                if(selectIds.length>0){
                    $http.post({
                        url:api.deleteParamByIds,
                        contentType:'json',
                        data:JSON.stringify(deleParams),
                        success:(result)=>{
                        }
                    });
                }
              }else{
                  let apiUrl  = this.sqlForm.apiUrl;
                  let newApiUrl=[]
                  if(apiUrl.indexOf("?")!=-1){
                      newApiUrl= apiUrl.substr(apiUrl.indexOf("?"),apiUrl.length-1).split(/[?&]/)
                  }
                  let tableIndexArr = this.tab2.selectParamTables.map(item=>item.tableIndex);
                  let data = this.tab2.data;
                  let newData=[];
                  for(let item of data){
                      if(!tableIndexArr.includes(item.tableIndex)){
                          newData.push(item) 
                      }else{
                          let pageName = item.paramName;
                          if(pageName && newApiUrl.length>1){
                              for (let i=0;i<newApiUrl.length;i++) {
                                  if(newApiUrl[i].includes(pageName)){
                                      newApiUrl.splice(i,1)
                                  }
                              }   
                          }
                      }
                  }
                  if(newApiUrl.length>1){
                      let apiUrlArr = apiUrl.substr(0,apiUrl.indexOf("?")+1);
                      for (const api of newApiUrl) {
                          if(api){
                              apiUrlArr=apiUrlArr+api+"&"
                          }
                      }
                      apiUrlArr=apiUrlArr.substr(0,apiUrlArr.length-1)
                      this.sqlForm.apiUrl=apiUrlArr
                  }else{
                    if(apiUrl.indexOf("?")!=-1){
                      this.sqlForm.apiUrl= apiUrl.substr(0,apiUrl.indexOf("?"))
                    }
                  }
                  this.tab2.data =newData
              }
              this.tab2.selectParamTables=[]
            },
            removeFieldTable(){
                this.deleteFieldModel = true;
            },
            deleteFieldTable(){
                let tableIndexArr = this.tab1.selectParamTables.map(item=>item.tableIndex);
                this.tab1.data = this.tab1.data.filter(item=>!tableIndexArr.includes(item.tableIndex));
                let selectTableObj = this.tab1.selectParamTables.filter(item=>item.id);
                let selectIds = selectTableObj.map(item=>item.id);
                this.tab1.selectParamTables = [];
                if(selectIds.length>0){
                    let deleParams="";
                    for (let str of selectIds){
                        deleParams=deleParams+str+",";
                    }
                    deleParams=deleParams.substr(0,deleParams.lastIndexOf(","))
                    $http.del({
                        contentType:'json',
                        url:api.deleteFieldByIds,
                        data:deleParams,
                        success:(result)=>{
                        }
                    });
                }
            },
            selectParamAll(){
                this.tab2.selectParamTables = this.tab2.data.map(item=>
                {
                    return {"tableIndex":item.tableIndex,"id":item.id}
                });
            },
            cancelParamAll(){
                this.tab2.selectParamTables = [];
            },
            selectParam(selection,row){
                this.tab2.selectParamTables=[...this.tab2.selectParamTables,{"tableIndex":row.tableIndex,"id":row.id}];
            },
            cancelParam(selection,row){
                this.tab2.selectParamTables = this.tab2.selectParamTables.filter(item=>item.tableIndex!=row.tableIndex);
            },
            saveSourceDb(){
                this.sourceModal = false;
                this.dataSourceFull = false;
                this.sourceDraggable = false
                this.sourceTabPage.page = 1
                this.sourceTabPage.total = 10
                this.sourceTabPage.size = 10
                this.maintainDataSource();
            },
            addDataSource(){
                Object.keys(this.dataSource).map(k=>{
                    this.dataSource[k] = ''
                })
                this.visibleData = true;
            },
            selectdbType(name){
                if (name === "MYSQL5.7"){
                    this.dataSource.dbDriver = "com.mysql.cj.jdbc.Driver";
                    //update-begin---Author:wangshuai---Date:20210909---for:解决mysql数据类型为tyint页面显示true和false---
                    this.dataSource.dbUrl = "jdbc:mysql://127.0.0.1:3306/jimureport?useUnicode=true&characterEncoding=UTF-8&serverTimezone=GMT%2B8&tinyInt1isBit=false";
                    //update-end---Author:wangshuai---Date:20210909---for:解决mysql数据类型为tyint页面显示true和false---
                }else if (name === "MYSQL5.5"){
                    this.dataSource.dbDriver = "com.mysql.jdbc.Driver";
                    //update-begin---Author:wangshuai---Date:20210909---for:解决mysql数据类型为tyint页面显示true和false---
                    this.dataSource.dbUrl = "jdbc:mysql://127.0.0.1:3306/jimureport?characterEncoding=UTF-8&useUnicode=true&useSSL=false&serverTimezone=GMT%2B8&tinyInt1isBit=false";
                   //update-end---Author:wangshuai---Date:20210909---for:解决mysql数据类型为tyint页面显示true和false---
                }else if (name === "ORACLE"){
                    this.dataSource.dbDriver = "oracle.jdbc.OracleDriver";
                    this.dataSource.dbUrl = "jdbc:oracle:thin:@127.0.0.1:1521:ORCL";
                }else if (name === "SQLSERVER"){
                    this.dataSource.dbDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
                    this.dataSource.dbUrl = "jdbc:sqlserver://127.0.0.1:1433;SelectMethod=cursor;DatabaseName=jimureport";
                }else if(name === 'POSTGRESQL'){
                    this.dataSource.dbDriver = "org.postgresql.Driver";
                    this.dataSource.dbUrl = "jdbc:postgresql://127.0.0.1:5432/jimureport";
                }else if(name === 'MARIADB'){
                    this.dataSource.dbDriver = "org.mariadb.jdbc.Driver";
                    this.dataSource.dbUrl = "jdbc:mariadb://127.0.0.1:3306/jimureport?characterEncoding=UTF-8&useSSL=false";
                }else if(name === 'dm'){
                    this.dataSource.dbDriver = "dm.jdbc.driver.DmDriver";
                    this.dataSource.dbUrl = "jdbc:dm://127.0.0.1:5236/?jimureport&zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf-8";
                }else if(name === 'kingbase8'){
                    this.dataSource.dbDriver = "com.kingbase8.Driver";
                    this.dataSource.dbUrl = "jdbc:kingbase8://127.0.0.1:54321/jimureport";
                }else if(name === 'oscar'){
                    this.dataSource.dbDriver = "com.oscar.Driver";
                    this.dataSource.dbUrl = "jdbc:oscar://127.0.0.1:2003/jimureport";
                }else if(name === 'SQLite'){
                    this.dataSource.dbDriver = "org.sqlite.JDBC";
                    this.dataSource.dbUrl = "jdbc:sqlite://opt/jimureport.db";
                }else if(name === 'DB2'){
                    this.dataSource.dbDriver = "com.ibm.db2.jcc.DB2Driver";
                    this.dataSource.dbUrl = "jdbc:db2://127.0.0.1:50000/jimureport";
                }else if(name === 'Hsqldb'){
                    this.dataSource.dbDriver = "org.hsqldb.jdbc.JDBCDriver";
                    this.dataSource.dbUrl = "jdbc:hsqldb:hsql://127.0.0.1/jimureport";
                }else if(name === 'Derby'){
                    this.dataSource.dbDriver = "org.apache.derby.jdbc.ClientDriver";
                    this.dataSource.dbUrl = "jdbc:derby://127.0.0.1:1527/jimureport";
                }else if(name === 'H2'){
                    this.dataSource.dbDriver = "org.h2.Driver";
                    this.dataSource.dbUrl = "jdbc:h2:tcp://127.0.0.1:8082/~/jimureport";
                }else if(name === 'other'){
                    this.dataSource.dbDriver = "";
                    this.dataSource.dbUrl = "";
                }else if(name === 'redis'){
                    this.dataSource.dbUrl = "127.0.0.1:6379";
                }else if(name === 'mongodb'){
                    this.dataSource.dbUrl = "127.0.0.1:27017/test";
                }
            },
            clearDbSou(){
                this.$refs.dataSource.resetFields();
                this.maintainDataSource();
            },
            saveDataSource(){
                this.$refs.dataSource.validate((valid)=>{
                    if(valid){
                        //保存表单
                        let dbSource = {};
                        dbSource.id = this.dataSource.id;
                        dbSource.reportId = excel_config_id;
                        dbSource.code = this.dataSource.code;
                        dbSource.name = this.dataSource.name;
                        dbSource.dbType = this.dataSource.dbType;
                        dbSource.dbDriver = this.dataSource.dbDriver;
                        dbSource.dbUrl = this.dataSource.dbUrl;
                        dbSource.dbUsername = this.dataSource.dbUsername;
                        dbSource.dbPassword = this.dataSource.dbPassword;
                        $http.post({
                            contentType:'json',
                            url: api.addDataSource,
                            data:JSON.stringify(dbSource),
                            success:(result)=>{
                                this.initDataSource();
                                this.maintainDataSource()
                                this.dataSource = {};
                                this.visibleData = false;
                            },
                            fail:(res)=>{
                              this.dateSourceLoading();
                            }
                        });
                    }else{
                        this.dateSourceLoading();
                        return;
                    }
                })
            },
            //关闭弹窗loading
            dateSourceLoading(){
              setTimeout(() => {
                this.loading = false
                this.$nextTick(() => {
                  this.loading = true
                })
              }, 500)
            },
            initDataSource(){
                $http.get({url:api.initDataSource,success:(result)=>{
                        let reportResult = result;
                        if(!reportResult){
                            return;
                        }
                        this.maintainData = reportResult
                    }});
            },
            maintainDataSource(){
                let params = {"pageNo":this.sourceTabPage.page,"pageSize":this.sourceTabPage.size}
                $http.get({url:api.getDataSourceByPage,data:params,success:(result)=>{
                    let reportResult = result.records;
                    if(!reportResult){
                      return;
                    }
                    this.sourceTab.data = reportResult;
                    this.sourceTab.data.forEach((item,index)=>{
                      item.tableIndex = index+1;
                    })
                    this.sourceTabPage.total = result.total
                  }}); 
            },
            renderButton(h, params) {
                return h('div',[
                    h('i-button', {
                        props: {
                            type: 'primary',
                            size: 'small'
                        },
                        style:{
                            'margin-right':'5px'
                        },
                        on: {
                            click: () => {
                                this.sourceTab.data.forEach((item)=>{
                                    if (item.id === params.row.id){
                                        this.dataSource = item;
                                    }
                                })
                                this.visibleData = true;
                            }
                        }
                    },'编辑'),
                    h('i-button', {
                        props: {
                            type: 'primary',
                            size: 'small'
                        },
                        on: {
                            click: () => {
                                this.$Modal.confirm({
                                    title:"提示",
                                    content: '是否确认删除?',
                                    onOk: () => {
                                        let dbSource = {};
                                        dbSource.id = params.row.id;
                                        $http.post({
                                            contentType:'json',
                                            url: api.delDataSource,
                                            data:JSON.stringify(dbSource),
                                            success:(result)=>{
                                                this.$Notice.success({
                                                    title: '删除成功'
                                                });
                                                this.initDataSource();
                                                this.maintainDataSource()
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    },'删除')
                ])
            },
            renderInput(h, params, field,tabIndex,placeholder) {
                return h('i-input', {
                    props: {
                        "size":"small",
                        type: 'text',
                        value: this[tabIndex].data[params.index][field],
                        placeholder: placeholder?placeholder:`请输入`+params.column.title
                    },
                    on: {
                        'on-change': (event) => {
                            if(tabIndex==="tab2"){
                                let tableIndexArr = this.tab2.selectParamTables.map(item=>item.tableIndex);
                                this.tab2.data.forEach(item=>{
                                    if(tableIndexArr.includes(item.tableIndex)){
                                        item._checked = true;
                                    }
                                });
                            }
                            this[tabIndex].data[params.index][field] = event.target.value;
                        }
                    },
                })
            },
            dataSourceTest(){
                let dbSource = {};
                dbSource.dbType = this.dataSource.dbType;
                dbSource.dbDriver = this.dataSource.dbDriver;
                dbSource.dbUrl = this.dataSource.dbUrl;
                dbSource.dbName = this.dataSource.dbName;
                dbSource.dbUsername = this.dataSource.dbUsername;
                dbSource.dbPassword = this.dataSource.dbPassword;
                $http.post({
                    contentType:'json',
                    url: api.testConnection,
                    data:JSON.stringify(dbSource)
                });
            },
            forwardClick(val){
              this.forward = val
            },
            //修改报表SQl确定事件
            reportOk(){
                this.reportSql = false
                this.saveDb();
            },
            reportCancel(){
                this.reportSql = false
                this.sqlModal = true
                setTimeout(() => {
                    this.loading = false
                    this.$nextTick(() => {
                        this.loading = true
                    })
                }, 500)
            },
            //新增报表参数点击下面上面可以追加
            createParams(){
                let tabData = this.tab2.data;
                let apiUrl = this.sqlForm.apiUrl;
                if(apiUrl.includes("?")){
                    apiUrl = apiUrl.substr(0,apiUrl.indexOf("?"))
                }
                for (let argument of tabData) {
                    let paramName = argument.paramName;
                    if(paramName){
                        let s = paramName+"=$"+"{"+paramName+ "}";
                        if(s && !apiUrl.includes(s)){
                            if(apiUrl.includes("?")){
                                apiUrl=apiUrl+"&"+paramName+"="+"'$"+"{"+paramName+"}'"
                            }else{
                                apiUrl=apiUrl+"?"+paramName+"="+"'$"+"{"+paramName+"}'"
                            }
                        } 
                    }
               }
               this.sqlForm.apiUrl = apiUrl;
            },
            //参数回调替换事件
            functionOk(resultParam){
              let tabData = this.tab2.data;
              // 循环tabData进行判断
              for (const tab of tabData) {
                let paramName = tab['paramName'];
                let paramValue = tab['paramValue'];
                //循环返回结果集
                let ts = resultParam.filter(item => item.paramName == paramName);
                //弹窗返回的默认值是否存在
                let tElement = ""
                if(ts.length>0){
                   tElement = ts[0]['paramValue'];
                }
                //默认值为空，或者默认值不相等，则替换
                if(!paramValue || paramValue !=tElement){
                  tab['paramValue'] = tElement
                }
              }
              //update-begin-author:wangshuai date:20210621 for: sql解析排序会导致如果那没有数据会为-Infinity tb JMREP-2081
              //update-end-author:wangshuai date:20210621 for: sql解析排序会导致如果那没有数据会为-Infinity tb JMREP-2081
              //循环resultParam 把没有的参数push进去
              //获取排序
              let orderNumArr = this.tab2.data.map(item=>item.orderNum);
              if(orderNumArr.length==0){
                orderNumArr = [0]
              }
              let number = Math.max(...orderNumArr);
              for (const param of resultParam) {
                let paramName = param['paramName'];
                let data = tabData.filter(item =>item.paramName == paramName);
                if(!data || data.length == 0){
                  number++;
                  tabData.push({
                    'paramName':paramName,
                    'paramTxt':"",
                    'paramValue':param['paramValue'],
                    'orderNum':number,
                    'tableIndex':number
                  })
                }
              }
              console.log("tabData:",tabData)
              this.tab2.data = tabData
              //1需要报错提示
              this.handleSQLAnalyze("1");
            },
            handleCurrentChange (val) {
              this.tablePage = val;
              this.handleDbSourceTable(api.queryTableName,2);
            },
            //数据源维护弹窗放大缩小操作
             dataSourceClick(){
              this.dataSourceFull = this.dataSourceFull==false?true:false
              this.sourceDraggable = this.sourceDraggable==false?true:false
            },
            //点击分页
            sourceTabCurrentChange(val){
              this.sourceTabPage.page = val;
              this.maintainDataSource();
            },
            //点击每页显示条数
            sourceTabSizeChange(val){
              this.sourceTabPage.page = 1;
              this.sourceTabPage.size = val;
              this.maintainDataSource();
            },
            //字段放大按钮点击事件
            extJsonFiledClick(index,extJson){
                this.paramConfigData = extJson
                this.tableIndex = index
                //0字段 1参数
                this.tableType = 0
                this.extJsonShow = true
            },
            //字段值改变事件
            extJsonFiledChange(index,extJson){
                this.tab1.data[index].extJson = extJson
            },  
            //参数放大按钮点击事件
            extJsonParamChange(index,extJson){
                this.tab2.data[index].extJson = extJson
            },   
            //参数值改变事件
            extJsonParamClick(index,extJson){
                this.paramConfigData = extJson
                this.tableIndex = index
                //0字段 1参数
                this.tableType = 1
                this.extJsonShow = true
            },
            //放大取消事件
            extJsonCancel(){
                this.tableIndex = -1
                this.tableType = -1
                this.extJsonShow=false
                this.paramConfigData = ""
            },
            //放大确定事件
            extJsonOk(){
              console.log(this.paramConfigData)
               if(this.tableType == 0){
                 this.tab1.data[this.tableIndex].extJson = this.paramConfigData
               }else if(this.tableType == 1){
                 this.tab2.data[this.tableIndex].extJson = this.paramConfigData
               }
              this.extJsonCancel(); 
            }
        }
    })
</script>