<script type="text/x-template" id="primary-sub-report-template">
  <Modal
          :title="title"
          v-model="subReportShow"
          width="600px"
          :closable="false"
  >
    <div slot="footer">
      <i-button @click="hyperlinksCancel('linkData')">取消</i-button>
      <i-button type="primary" @click="hyperlinksOk('linkData')">确定</i-button>
    </div>
    <i-form ref="linkData" :model="linkData" :label-width="100" :rules="linkDataRule">
      <form-item label="名称" prop="linkName">
        <i-input placeholder="请填写名称" v-model="linkData.linkName"></i-input>
      </form-item>
      <form-item label="主表数据源" prop="mainReport">
        <i-select :transfer="true" clearable v-model="linkData.mainReport" @on-change="mainReportChange">
          <i-option v-for="item in mainReportData" :value="item.dbCode">{{item.dbChName}}</i-option>
        </i-select>
      </form-item>
      <form-item label="子表数据源" prop="subReport">
        <i-select :transfer="true" clearable v-model="linkData.subReport" @on-change="subReportChange">
          <i-option v-for="item in subReportData" :value="item.dbCode">{{item.dbChName}}</i-option>
        </i-select>
      </form-item>
      <span>参数传递</span>
      <div style="margin-top: 10px">
        <i-button type="primary" @click="addParamTable">新增</i-button>
        <i-button type="primary" @click="removeParamTable" v-if="selectParamTables.length>0">删除</i-button>
        <i-table style="margin-top: 10px" ref="paramTable" @on-select="selectParam" @on-select-all="selectParamAll"
                 @on-select-all-cancel="cancelParamAll" @on-select-cancel="cancelParam" stripe
                 :columns="hyperlinksData.columns" :data="hyperlinksData.data" :height="paramTableHeight"></i-table>
      </div>
    </i-form>
  </Modal>
</script>
<script>
  Vue.component('j-primary-sub-report', {
    template: '#primary-sub-report-template',
    props: {
      excel: {
        type: String,
        required: true,
        default: ""
      }
    },
    data() {
      return {
        title: "",
        subReportShow: false,
        hyperlinksData: {
          columns: [
            {
              type: 'selection',
              width: 35,
              align: 'center'
            },
            {
              title: '子表参数',
              key: 'subParam',
              align: 'center',
              render: (h, params) => {
                if (this.subReportFiled) {
                  return h('i-select', {
                    props: {
                      value: params.row.subParam,
                      size: 'small',
                      transfer: true,
                      filterable: true,
                      clearable:true
                    },
                    on: {
                      'on-change': e => {
                        // 改变下拉框赋值
                        this.hyperlinksData.data[params.index].subParam = e
                      }
                    }
                  }, this.subReportFiled.map((item) => {
                    if (item) {
                      if(!item.paramTxt){
                         item.paramTxt = item.paramName
                      }
                      return h('i-option', {
                        // 下拉框的值
                        props: {
                          value: item.paramName,
                          label: item.paramTxt
                        }
                      })
                    }
                  }))
                }
              }
            },
            {
              title: '主表字段',
              key: 'mainField',
              align: 'center',
              render: (h, params) => {
                if (this.mianReportFiled) {
                  return h('i-select', {
                    props: {
                      value: params.row.mainField, // 获取选择的下拉框的值
                      size: 'small',
                      transfer: true,
                      filterable: true,
                      clearable:true
                    },
                    on: {
                      'on-change': e => {
                        // 改变下拉框赋值
                        this.hyperlinksData.data[params.index].mainField = e
                      }
                    }
                  }, this.mianReportFiled.map((item) => {
                    return h('i-option', {
                      // 下拉框的值
                      props: {
                        value: item.title,
                        label: item.fieldText
                      }
                    })
                  }))
                }
              }
            },
          ],
          data: [],
        },
        selectParamTables: [],
        paramTableHeight: 0,
        linkData: {
          id: "",
          reportId: "",//积木设计器id
          parameter: "",//参数JSON串
          linkName: "",
          mainReport: "",
          subReport: ""
        },
        linkDataRule: {
          linkName: [
            {required: true, message: '请填写参数名称', trigger: 'change'},
          ],
          mainReport: [
            {required: true, message: '请选择主表数据源', trigger: 'change'},
          ],
          subReport: [
            {required: true, message: '请选择子表数据源', trigger: 'change'},
          ]
        },
        mainReportData: [], //主报表数据集合
        subReportData: [], //子报表数据集合
        dbFiled: [], //字段的集合
        dbParam: [], //参数的集合
        mainReportFiled: [],//主报表的字段集合
        subReportFiled: [],//子报表的字段集合
        paramData:[]//父向子传递参数
      }
    },
    watch: {
      'paramData': function (val) {
        //先清空数据
        this.hyperlinksData.data = [];
        for (const valElement of val) {
            //为参数table添加数据
            this.hyperlinksData.data.push(valElement)
        }
      }
    },
    methods: {
      addParamTable() {
        let indexArr = this.hyperlinksData.data.map(item => item.tableIndex);
        if (indexArr.length == 0) {
          indexArr = [0];
        }
        this.selectParamTables = [];
        this.hyperlinksData.data = [...this.hyperlinksData.data, {
          'mainField': "",
          'subParam': "",
          'tableIndex': Math.max(...indexArr) + 1
        }];
      },
      removeParamTable() {
        // this.deleteParamModel = true;
        let tableIndexArr = this.selectParamTables.map(item => item.tableIndex);
        this.hyperlinksData.data = this.hyperlinksData.data.filter(item => !tableIndexArr.includes(item.tableIndex));
        this.selectParamTables = [];
      },
      selectParamAll() {
        this.selectParamTables = this.hyperlinksData.data.map(item => {
          return {"tableIndex": item.tableIndex, "id": item.id}
        });
      },
      cancelParamAll() {
        this.selectParamTables = [];
      },
      selectParam(selection, row) {
        this.selectParamTables = [...this.selectParamTables, {"tableIndex": row.tableIndex, "id": row.id}];
      },
      cancelParam(selection, row) {
        this.selectParamTables = this.selectParamTables.filter(item => item.tableIndex != row.tableIndex);
      },
      getReportByUser() {
        $http.get({
          url: api.getReportByUser,
          data: {reportId: excel_config_id},
          success: (result) => {
            this.reportData = result
          }
        })
      },
      //网络报表点击事件
      hyperlinksOk(name) {
        this.$refs[name].validate((valid) => {
          if (valid) {
            let mainReport = this.linkData.mainReport
            let subReport = this.linkData.subReport
            let linkData = this.linkData
            linkData.parameter = JSON.stringify({
              "main": mainReport,
              "sub": subReport,
              "subReport": this.hyperlinksData.data
            })
            linkData.linkType = "4"
            linkData.reportId = excel_config_id
            $http.post({
              url: api.linkSaveAndEdit,
              contentType: 'json',
              data: JSON.stringify(linkData),
              success: (result) => {
                this.close(name);
              }
            });
          }
        })

      },
      hyperlinksCancel(name) {
        this.close(name);
      },
      close(name) {
        this.subReportShow = false
        this.mainReportData=[]
        this.subReportData=[]
        this.dbFiled=[]
        this.dbParam=[]
        this.mainReportFiled=[]
        this.subReportFiled=[]
        this.paramData=[]
        this.title=""
        this.selectParamTables=[]
        this.linkData={}
        this.$refs[name].resetFields();
        this.$emit('mainsubreport')
      },
      //获取数据源的数据，用于主子表选择
      getListReportDb(treeData,data) {
        $http.get({
          url: api.getListReportDb,
          data: {"reportId": excel_config_id},
          success: (res) => {
            if (res) {
              this.dbParam = res.reportDbParam
              if(data){
                //为主表数据源赋值
                this.mainReportChange(data.main)
                //为子表数据源赋值
                this.subReportChange(data.sub)
                this.linkData.mainReport = data.main
                this.linkData.subReport = data.sub
                //参数赋值
                this.paramData = data.subReport
              }
            }
          }
        })
        let map = new Map();
        //获取treeData,并为字段，主表数据源，子表数据源赋值
        for (const treeDatum of treeData) {
          let tree = treeDatum[0]["children"];
          let code = treeDatum[0]["code"];
          let title = treeDatum[0]["title"];
          if(!title){
            title = code
          }
          map.set(code,tree);
          this.dbFiled.push({"code":code,"list":tree});
          this.mainReportData.push({"dbCode":code,"dbChName":title})
          this.subReportData.push({"dbCode":code,"dbChName":title})
        }
      },
      //主报表值改变事件
      mainReportChange(val) {
        let dbFiled = this.dbFiled;
        //判断当前主表数据源的code,并为参数中主表字段赋值
        dbFiled=dbFiled.filter(item=>item.code == val)
        if(dbFiled){
          this.mianReportFiled = dbFiled[0]?dbFiled[0].list:[];
        }
      },
      //子报表值改变事件
      subReportChange(val) {
        this.subReportFiled = this.dbParam[val]
      }
    },
  })
</script>
<style>
    .ivu-form-item-error-tip {
        position: relative;
    }
</style>