<style>
    .pointerIcon .ivu-icon{
        cursor: pointer;
    }
</style>

<script type="text/x-template" id="javabean-setting-template">
<div>
    <Modal title="javabean数据集配置"
           fullscreen=true
           :loading="loading"
           :value="visible"
           width="100%"
           @on-cancel="closeJavabean"
           @on-ok="saveData">
        <div>
            <i-form ref="javabeanForm" :model="formData" :rules="formDataValidate" inline :label-width="85">
                <Row span="24">
                    <i-col span="5">
                        <form-item prop="dbCode" label="编码：">
                            <i-input v-model="formData.dbCode" :disabled ="dbCodeDisabled()" style="width: 153px" type="text" placeholder="请输入编码">
                            </i-input>
                        </form-item>
                    </i-col>
                    <i-col span="5" style="margin-left: 20px;">
                        <form-item prop="dbChName" label="名称：">
                            <i-input v-model="formData.dbChName" type="text" style="width: 200px" placeholder="请输入名称">
                            </i-input>
                        </form-item>
                    </i-col>
                    <i-col span="3">
                        <form-item prop="isList">
                            <Checkbox v-model="formData.isList" style="width: 100px;margin-left: 10%">是否集合</Checkbox>
                        </form-item>
                    </i-col>
                    <i-col span="3">
                        <form-item prop="isPage" style="display: flex">
                            <Checkbox v-if="formData.isList == true" v-model="formData.isPage" @on-change="checkChange" style="width: 100px">是否分页
                              <Tooltip :transfer="true" content="分页文档" placement="top" class="jimu-tooltip">
                                <a class="jimu-table-tip help-color" href="http://report.jeecg.com/2084139" target="_blank" style="font-size: 14px;"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
                              </Tooltip>
                            </Checkbox>
                        </form-item>
                    </i-col>
                </Row>
                <Row style="margin-top:10px;">
                    <i-col span="5">
                        <form-item prop="javaType" label="类型：">
                            <RadioGroup v-model="formData.javaType">
                                <Radio label="spring-key"></Radio>
                                <Radio label="java-class"></Radio>
                            </RadioGroup>
                        </form-item>
                    </i-col>
                    <i-col span="10" style="margin-left: 20px;">
                        <form-item prop="javaValue" label="值：">
                            <i-input v-model="formData.javaValue" type="text" style="width: 400px" placeholder="请填入对应的值"/>
                        </form-item>
                        <i-button @click="analyze" type="primary">解 析</i-button>
                        <Tooltip :transfer="true" content="javabean解析文档" placement="top">
                            <a class="jimu-table-tip help-color" href="http://report.jeecg.com/2220323" target="_blank" style="font-size: 14px;"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
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
                    <i-table ref="dynamicTable"
                             style="padding-bottom: 10%;"
                             stripe
                             @on-select="selectField"
                             @on-select-all="selectFieldAll"
                             :columns="tab1.columns"
                             :data="tab1.data"
                             :height="tableHeight">
                      
                        <template slot-scope="{ row, index }" slot="extJsonFiled">
                          <i-input size="small" v-model="row.extJson" @on-change="extJsonFiledChange(index,row.extJson)" @on-click="extJsonFiledClick(index,row.extJson)" icon="md-contract" class="pointerIcon" placeholder="请输入参数配置"/>
                        </template>
                    </i-table>
                </tab-pane>

                <tab-pane label="报表参数" name="2">
                    <i-button type="primary" @click="addParamTable">新增</i-button>
                    <i-button type="primary" @click="removeParamTable" v-if="tab2.selectParamTables.length>0">删除</i-button>
                    <i-table ref="paramTable"
                             @on-select="selectParam"
                             @on-select-all="selectParamAll"
                             stripe
                             :columns="tab2.columns"
                             :data="tab2.data"
                             :height="paramTableHeight">
                        <template slot="paramName" slot-scope="props">
                            <i-form :ref="'formDynamic'+props.idx" :model="props.row">
                                <form-item prop="paramName" :rules="{required: true, message: '请输入参数', trigger: 'change'}" style="margin-top: 10px">
                                    <i-input v-model="props.row.paramName" size="small" />
                                </form-item>
                            </i-form>
                        </template>
                        <template slot="paramTxt" slot-scope="props">
                            <i-form :ref="'formTxtDynamic'+props.idx" :model="props.row">
                                <form-item prop="paramTxt" :rules="{required: true, message: '请输入参数文本', trigger: 'change'}" style="margin-top: 10px">
                                    <i-input v-model="props.row.paramTxt" size="small" />
                                </form-item>
                            </i-form>
                        </template>
                      
                        <template slot-scope="{ row, index }" slot="extJsonParam">
                          <i-input size="small" v-model="row.extJson" @on-change="extJsonParamChange(index,row.extJson)" @on-click="extJsonParamClick(index,row.extJson)" icon="md-contract" class="pointerIcon" placeholder="请输入参数配置"/>
                        </template>
                    </i-table>
                </tab-pane>
            </Tabs>
        </div>
    </Modal>
    <Modal v-model="deleteParamModel" @on-ok="deleteParamTable" title="确认删除">
        <p><Icon type="ios-alert"  color="#f90" size="20px"></Icon>是否删除选中数据?</p>
    </Modal>
    <Modal v-model="deleteFieldModel" @on-ok="deleteFieldTable" title="确认删除">
        <p><Icon type="ios-alert"  color="#f90" size="16px"></Icon>是否删除选中配置?</p>
    </Modal>
    <#--输入框参数配置放大-->
    <Modal draggable :loading="loading" v-model="extJsonShow" title="输入值" :width="1000" @on-ok="extJsonOk"
           @on-cancel="extJsonCancel">
      <i-form label-colon :label-width="100">
        <form-item label="参数配置">
          <i-input :autosize="{minRows: 10}" type="textarea" v-model="paramConfigData" placeholder="请输入参数配置"></i-input>
        </form-item>
      </i-form>
    </Modal>
</div>
</script>

<script>
    const formDataInitValue = {
        id:'',
        jimuReportId: '',
        dbCode: '',
        dbChName: '',
        //0sql, 1api ,2 javabean
        dbType: "2",
        // spring-key java-class
        javaType: 'spring-key',
        javaValue: '',
        isPage:true,
        isList:true,
    }
    Vue.component('j-javabean-setting', {
        template: '#javabean-setting-template',
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
                visible: false,
                loading: true,
                formData:{},
                tabValue: '1',
                spinShow:false,
                tableHeight: 0,
                paramTableHeight: 0,
                deleteParamModel:false,
                deleteFieldModel:false,
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
                            /*width: '220',*/
                            render: (h, params) => {
                                return this.renderInput(h, params, 'fieldName','tab1')
                            }
                        },
                        {
                            title: '排序',
                            key: 'orderNum',
                            /*width: '80',*/
                            render: (h, params) => {
                                return this.renderInput(h, params, 'orderNum','tab1')
                            }
                        },
                        {
                            title: '字段文本',
                            /*width: '220',*/
                            key: 'fieldText',
                            render: (h, params) => {
                                return this.renderInput(h, params, 'fieldText','tab1')
                            }
                        },
                        {
                            title: '类型',
                            /*width: '140',*/
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
                            /*width: '220',*/
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
                            /*width: '140',*/
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
                        /*width: '220',*/
                        key: 'searchValue',
                        render: (h, params) => {
                          return this.renderInput(h, params, 'searchValue','tab1','请输入查询默认值')
                        }
                      },
                      {
                        title: '查询日期格式',
                        /*width: '220',*/
                        key: 'searchFormat',
                        render: (h, params) => {
                          return this.renderInput(h, params, 'searchFormat','tab1','请输入查询日期格式')
                        }},
                      {
                        title: '参数配置',
                        /*width: '220',*/
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
                            title: '参数文本',
                            key: 'paramTxt',
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
                            title: '排序',
                            key: 'orderNum',
                            render: (h, params) => {
                                return this.renderInput(h, params, 'orderNum','tab2')
                            }
                        },
                      {
                        title: '类型',
                        key: 'widgetType',
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
                        render: (h, params) => {
                          return this.renderInput(h, params, 'dictCode','tab2','请输入字典code或地址')
                        }
                      },
                      {
                        title: '查询',
                        key: 'searchFlag',
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
                        render: (h, params) => {
                          return this.renderInput(h, params, 'paramValue','tab2')
                        }
                      },
                      {
                        title: '查询日期格式',
                        key: 'searchFormat',
                        render: (h, params) => {
                          return this.renderInput(h, params, 'searchFormat','tab2','请输入查询日期格式')
                        }
                      },
                      {
                        title: '参数配置',
                        /*width: '220',*/
                        key: 'extJson',
                        slot: 'extJsonParam'
                      }
                    ]
                },

                formDataValidate:{
                    dbCode:[
                        { required: true, message: '编码不能为空', trigger: 'blur' },
                        { validator: this.validateCodeExist, trigger: 'blur' }
                    ],
                    dbChName:[
                        { required: true, message: '名称不能为空', trigger: 'blur' }
                    ],
                    javaValue:[
                        { required: true, message: '请输入spring-key或者java-class', trigger: 'blur' }
                    ]
                },
                paramConfigData:"", //参数配置弹窗数据
                extJsonShow:false, //参数配置弹窗是否显示
                tableIndex:-1, //当前table的下标
                tableType:-1 //判断当前是字段还是参数 0字段 1参数
            }
        },

        watch: {
            tabValue: {
                deep: true,
                immediate: true,
                handler: function (val){
                    if(val==='3'){
                        this.spinShow=true;
                        this.loadTableData(1,this.sqlForm.dbDynSql)
                    }
                }
            }
        },
        methods: {
            javabeanShow(){
                this.visible = true;
                this.$refs.javabeanForm.resetFields()
                this.formData = {...formDataInitValue}
                this.formData.jimuReportId = excel_config_id
                //update-begin--Author:wangshuai--Date:20211202--for:javaBean数据源新增的时候设置是否显示分页
                this.setIsPage();
                //update-end--Author:wangshuai--Date:20211202--for:javaBean数据源新增的时候设置是否显示分页
            },
            //设置分页是否显示，目前只支持一个分页显示
            setIsPage(){
              $http.get({url:api.queryIsPage(excel_config_id),data:{dbcode:this.formData.dbCode},success:(result)=> {
                  if(result){
                    this.formData.isPage = false
                  }else{
                    this.formData.isPage = true
                  }
                }
              })
            },
            closeJavabean(){
                this.visible = false;
            },
            clearData(){
                this.formData = {...formDataInitValue}
                this.tab1.data = [];
                this.tab2.data = [];
                this.tab2.selectParamTables=[];
            },
            saveData(){
                this.$refs.javabeanForm.validate((valid)=>{
                    if(valid){
                        this.childrenRules().then(res =>{
                            if(res){
                                //保存表单
                                let data = {...this.formData}
                                if (data.isList==true){
                                    data.isList = '1'
                                }else{
                                    data.isList = '0'
                                    data.isPage = '0'
                                }
                                if (data.isPage==true){
                                    data.isPage = '1'
                                    data.isList = '1'
                                } else {
                                    data.isPage = '0'
                                }
                                data.fieldList = this.tab1.data; //解析出表字段
                                data.paramList = this.tab2.data; //动态表单参数
                                // console.log(123, data)
                                $http.post({
                                    url: api.saveDb,
                                    contentType:'json',
                                    data:JSON.stringify(data),
                                    success:(res)=>{
                                        this.$emit('saveback',res.id);
                                        this.visible = false
                                        this.clearData();
                                    },
                                    finally:()=>{
                                        setTimeout(() => {
                                            this.loading = false
                                            this.$nextTick(() => {
                                                this.loading = true
                                            })
                                        }, 500)
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

            editById(dbId){
                this.tabValue="1";
                this.clearData();
                $http.get({url:api.loadDbData(dbId),
                    success:(result)=>{
                        // console.log('result=====',result);
                        if(!result){
                            return;
                        }
                        //设置数据
                        let obj = {}
                        Object.keys(this.formData).map(k=>{
                            obj[k] = result.reportDb[k]
                        })
                        if (obj.isPage=='1'){
                            obj.isPage = true;
                            obj.isList = true;
                        }else {
                            obj.isPage = false;
                        }
                        if(obj.isList=='1'){
                            obj.isList = true;
                        }else{
                            obj.isList = false;
                            obj.isPage = false;
                        }
                        this.formData = obj;
                        this.tab1.data=result.fieldList;
                        if(this.tab1.data){
                            this.tab1.data.forEach((item,index)=>{
                                item.tableIndex = index+1;
                            })
                        }
                        this.tab2.data=result.paramList;
                        if(this.tab2.data){
                            this.tab2.data.forEach((item,index)=>{
                                item.tableIndex = index+1;
                            })
                        }
                        this.visible = true;
                    }
                });
            },

            dbCodeDisabled(){
              if(!this.formData.id){
                  return false
              }else{
                  return true
              }
            },
            analyze(){
                let { javaType, javaValue, isPage } = this.formData
                let param = {}
                this.tab2.data.map(item=>{
                    if(item.paramValue){
                        param[item.paramName] = item.paramValue
                    }
                })
                let dataStr = JSON.stringify({
                    'javaType': javaType,
                    'javaValue': javaValue,
                    'isPage': isPage,
                    'param': param
                });
                $http.post({
                    url: api.queryFieldByBean,
                    contentType: 'json',
                    data: dataStr,
                    success:(result)=>{
                        this.tab1.data = result
                        this.tab1.data.forEach((item,index)=>{
                            item.tableIndex = index+1;
                            item.searchFlag = 0
                            item.extJson = ""
                        })
                    }
                })
            },
            removeField(){

            },
            checkChange(ispage){
                if (ispage){
                    $http.get({url:api.queryIsPage(excel_config_id),data:{dbcode:this.formData.dbCode},success:(result)=>{
                            if (result){
                                this.$Modal.confirm({
                                    content: '已有数据集分页,是否更改?',
                                    onOk: () => {
                                        this.formData.isPage = true;
                                        this.formData.isList = true;
                                    },
                                    onCancel: ()=> {
                                        this.formData.isPage = false;
                                    }
                                });
                            }
                        }});
                }
            },
            //校验数据集编码
            validateCodeExist(rule, value, callback){
                if(this.formData.id){
                    callback();
                }
                //update-begin-Author:wangshuai--Date:20211206--for:添加JAVABean 数据集时，编码检验出错，无法添加，和SQL数据集验证保持一致 gitee #I4KT5R
                let reg=/^(?!_)(?![0-9])[0-9a-zA-Z_]+(?<!_)$/;
                if(!reg.test(value)){
                    callback(new Error('编码支持字母和数字，且不能以数字和_开头'));
                }
                //update-end-Author:wangshuai--Date:20211206--for:添加JAVABean 数据集时，编码检验出错，无法添加，和SQL数据集验证保持一致 gitee #I4KT5R
                $jm.dataCodeExist(excel_config_id, value, (result)=> {
                    if(result === true){
                        callback('编码已存在!');
                    }else{
                        callback();
                    }
                })
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
                this.tab3Loading=true;
                $http.post({
                    url:api.loadTableData,
                    data:{
                        dbSource:dbSource,
                        sql:sql,
                        tableName:this.selectTableName,
                        pageNo:1,
                        pageSize:10
                    },
                    timeout:10000,
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
                        let params = "apiUrl,apiMethod,dbChName,dbCode,dbDynSql,dbSource,dbType,fieldList,id,isList,isPage,jimuReportId,paramList"
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
                return success;
            },
            isListChange(isList){
              //update--begin-Author:wangshuai--Date:20211129--for:选择列表的时候如果有分页不选择，如果没有分页就勾选上
              if(isList){
                this.setIsPage();
              }else{
                //update-begin--Author:wangshuai--Date:20211203--for:如果不是集合是否分页应该去掉
                this.formData.isPage = false
                //update-end--Author:wangshuai--Date:20211203--for:如果不是集合是否分页应该去掉
              }
              //update--end-Author:wangshuai--Date:20211129--for:选择列表的时候如果有分页不选择，如果没有分页就勾选上
            },
            selectFieldAll(){
                this.tab1.selectParamTables = this.tab1.data.map(item=>
                {
                    return {"tableIndex":item.tableIndex,"id":item.id}
                });
            },
            selectField(selection,row){
                this.tab1.selectParamTables=[...this.tab1.selectParamTables,{"tableIndex":row.tableIndex,"id":row.id}];
            },
            removeFieldTable(){
                this.deleteFieldModel = true;
            },
            deleteFieldTable(){
                let tableIndexArr = this.tab1.selectParamTables.map(item=>item.tableIndex);
                let arr = this.tab1.data.filter(item=>!tableIndexArr.includes(item.tableIndex));
                this.tab1.data = [...arr]
            },

            selectParamAll(){
                this.tab2.selectParamTables = this.tab2.data.map(item=>
                {
                    return {"tableIndex":item.tableIndex,"id":item.id}
                });
            },
            selectParam(selection,row){
                this.tab2.selectParamTables=[...this.tab2.selectParamTables,{"tableIndex":row.tableIndex,"id":row.id}];
            },
            removeParamTable(){
                this.deleteParamModel = true;
            },
            deleteParamTable(){
                let tableIndexArr = this.tab2.selectParamTables.map(item=>item.tableIndex);
                let arr = this.tab2.data.filter(item=>!tableIndexArr.includes(item.tableIndex));
                this.tab2.data = [...arr]
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
            // console.log(this.paramConfigData)
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