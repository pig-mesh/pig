<style>
    .jimu-table-tip{
        font-weight: bold;
        display: inline-block;
        margin-left: 5px;
    }
</style>

<script type="text/x-template" id="hyperlinks-setting-template">
  <Modal
          :title="title"
          v-model="hyperlinksShow"
          width="600px"
          :closable = "false"
          :mask-closable="false"
       >
    <div slot="footer">
      <i-button @click="hyperlinksCancel('linkData')">取消</i-button>
      <i-button  type="primary" @click="hyperlinksOk('linkData')">确定
      </i-button>
    </div>
      <i-form ref="linkData" :model="linkData" :label-width="100" :rules="linkDataRule">
        <form-item label="链接名称" prop="linkName">
            <i-input placeholder="请填写链接名称" v-model="linkData.linkName"></i-input>
        </form-item>

        <form-item label="链接报表" prop="reportId" v-if="linkData.linkType==='0'">
          <i-select clearable v-model="linkData.reportId" filterable @on-change="networkClick">
            <i-option v-for="item in reportData" placeholder="请选择链接报表" :value="item.id" :key="item.id">{{ item.name }}</i-option>
          </i-select>
        </form-item>

        <form-item label="链接图表" prop="charId" v-if="linkData.linkType==='2'">
          <i-select v-model="linkData.charId" filterable @on-change="charsClick">
            <i-option v-for="item in chartData" placeholder="请选择链接图表" :value="item.id" :key="item.id">{{ item.name }}</i-option>
          </i-select>
        </form-item>

        <form-item label="网络连接" prop="apiUrl" v-if="linkData.linkType=='1'">
          <i-input placeholder="请填写网络连接" v-model="linkData.apiUrl"></i-input>
        </form-item>


        <form-item label="弹出方式" prop="ejectType" v-if="linkData.linkType=='0' || linkData.linkType=='1'">
          <i-select clearable :transfer="true" v-model="linkData.ejectType">
             <i-option value="0">新窗口</i-option>
             <i-option value="1">当前窗口</i-option>
          </i-select>
        </form-item>
          
        <form-item label="条件" prop="requirement">
          <i-input placeholder="请填写条件" v-model="linkData.requirement"></i-input>
        </form-item>
        
        <span>参数设置</span>


        <div style="margin-top: 10px">
          <i-button type="primary" @click="addParamTable">新增</i-button>
          <i-button type="primary" @click="removeParamTable" v-if="selectParamTables.length>0">删除</i-button>
          <i-table
            style="margin-top: 10px"
            ref="paramTable"
            @on-select="selectParam"
            @on-select-all="selectParamAll"
            @on-select-all-cancel="cancelParamAll"
            @on-select-cancel="cancelParam"
            stripe
            :columns="hyperlinksData.columns"
            :data="hyperlinksData.data"
            :height="paramTableHeight">
          </i-table>
        </div>
      </i-form>
  </Modal>
</script>
<script>
  Vue.component('j-hyperlinks-setting', {
    template: '#hyperlinks-setting-template',
    props: {
      excel: {
        type: String,
        required: true,
        default:""
      }
    },
    data(){
      return {
        title:"",
        hyperlinksShow:false,
        fieldList:[],
        hyperlinksData:{
          columns: [
            {
              type: 'selection',
              width: 35,
              align: 'center'
            },
            {
              title: '映射参数',
              key: 'paramName',
              align: 'center',
              render: (h, params) => {
                return this.renderInput(h, params, 'paramName','hyperlinksData')
              }
            },
            {
              title: '原始参数',
              key: 'paramValue',
              renderHeader: (h, params) => {
                //update-begin--Author:wangshuai  Date:20211213 for：判断是够选中图表----
                let selectChart = this.ifSelectChart();
                //update-end--Author:wangshuai  Date:20211213 for：判断是够选中图表----
                const sub = ()=>{
                  let arr = []
                  let it1 = h('span',params.column.title)
                  let it2 = h('Tooltip', {
                    props:{
                      placement: 'top',
                      transfer: true
                    }
                  }, [
                    h('span',{class:'jimu-table-tip'}, '?'),
                    h('div',{slot:'content', style:{'white-space':'normal'}}, [
                      h('p','1.支持下拉框选择数据集字段'),
                      h('p','2.支持表达式=A1表示A1单元格的值'),
                      h('p','3.支持表达式=A表示当前点击行所在的A列单元格的值')
                    ])
                  ])
                  arr.push(it1)
                  //update-begin--Author:wangshuai  Date:20211213 for：【JMREP-2470】如果当前点击的不是图表那么就显示问号提示----
                  if(!selectChart){
                    arr.push(it2)
                  }
                  //update-end--Author:wangshuai  Date:20211213 for：【JMREP-2470】如果当前点击的不是图表那么就显示问号提示----
                  return arr
                }
                return h('div', {}, sub())
              },
              render: (h, params) => {
                let paramSelectOptions = this.getChartParamSelectOptions()
                //update-begin--Author:wangshuai--Date:20211012--for:图表钻取的时候不显示自定义表达式
                let assembly = this.randerSelectInput(params,paramSelectOptions);
                let props = assembly.get("props")
                let select = assembly.get("select")
                assembly.clear()
                return h(select, {
                      props: props,
                //update-end--Author:wangshuai--Date:20211012--for:图表钻取的时候不显示自定义表达式
                      on: {
                        'on-change': (value) => {
                          this.hyperlinksData.data[params.index].paramValue = value;
                        }
                      },
                    },
                    paramSelectOptions.map(item => {
                      return h('i-option', {
                        props: {
                          value: item.value
                        }
                      }, item.title)
                    })
                );
              }
            },
          ],
          data:[],
        },
        selectParamTables:[],
        paramTableHeight: 0,
        linkData:{
          id:"",
          reportId:"",//积木设计器id
          parameter:"",//参数JSON串
          ejectType:"",//弹出方式（0 当前页面 1 新窗口）
          linkName:"",
          apiMethod:"",
          linkType:"",
          apiUrl:"",
          charId:"",
          requirement:"",
        },
        reportData:[],//积木报表下拉框的数据
        linkDataRule: {
          reportId: [
            { required: true, message: '请选择链接目标', trigger: 'change' }
          ],
          ejectType: [
            { required: true, message: '请选择弹出方式', trigger: 'change' },
          ],
          linkName: [
            { required: true, message: '请填写链接名称', trigger: 'change' },
          ],
          charId: [
            { required: true, message: '请选择链接图表', trigger: 'change' },
          ],
          apiUrl: [
            { required: true, message: '请填写网络连接', trigger: 'change' },
            { validator: this.validateHttpExist, trigger: 'change' }
          ],
        },
        chartData:[],// 图表下拉框数据
        paramData:[], //参数数据用于父页面传递参数便于监听
        currentDbCode:"",
        fieldName:"",
        currentChart:"" //存放当前点击图表的数据
      }
    },
    watch: {
      'paramData': function(val) {
        //先清空数据
        this.hyperlinksData.data=[];
        for (const valElement of val) {
          if(valElement.paramName != 'charId'){
            //为参数table添加数据
            this.hyperlinksData.data.push(valElement)
          }
        }
      }
    },
    created(){
    },
    methods:{
      addParamTable(){
        let indexArr = this.hyperlinksData.data.map(item=>item.tableIndex);
        if(indexArr.length==0){
          indexArr=[0];
        }
        this.selectParamTables = [];
        this.hyperlinksData.data=[...this.hyperlinksData.data,{
          'paramName':"",
          'paramValue':"",
          'paramCell':"",
          'tableIndex':Math.max(...indexArr)+1
        }];
      },
      removeParamTable(){
        // this.deleteParamModel = true;
        let tableIndexArr = this.selectParamTables.map(item=>item.tableIndex);
        this.hyperlinksData.data = this.hyperlinksData.data.filter(item=>!tableIndexArr.includes(item.tableIndex));
        this.selectParamTables = [];
      },
      selectParamAll(){
        this.selectParamTables = this.hyperlinksData.data.map(item=>
        {
          return {"tableIndex":item.tableIndex,"id":item.id}
        });
      },
      cancelParamAll(){
        this.selectParamTables = [];
      },
      selectParam(selection,row){
        this.selectParamTables=[...this.selectParamTables,{"tableIndex":row.tableIndex,"id":row.id}];
      },
      cancelParam(selection,row){
        this.selectParamTables = this.selectParamTables.filter(item=>item.tableIndex!=row.tableIndex);
      },
      getReportByUser(fieldList,dbCode,fieldName,existChartList,chartId){
        this.linkData.reportId = ""
        this.linkData.ejectType = ""
        this.fieldList = fieldList
        this.currentDbCode = dbCode
        this.fieldName = fieldName
        $http.get({
          url: api.getReportByUser,
          data:{reportId:excel_config_id},
          success:(result)=>{
            this.reportData = result
          }
        })
        if(vm.chartsflag){
          this.initChartList(existChartList,chartId)
        }
      },
      //初始化图表
      initChartList(existChartList, currentChartId){
        if(existChartList && existChartList.length>0){
          let arr2 = existChartList.filter(item=>{
            return item.id==currentChartId
          });
          if(arr2.length>0){
            this.currentChart = arr2[0]
            this.currentDbCode =  arr2[0].db
          }
        }
      },
      getReportChars(){
        let param={}
        param.reportId = excel_config_id;
        if(vm.chartsflag){
          param.charId = vm.dataSettings.id
        }
        $http.get({
          url: api.getReportChars,
          data:param,
          success:(result)=>{
            this.chartData=[]
            this.chartData = result
          }
        })
        let parameter = this.linkData.parameter;
        if(parameter){
        let parse = JSON.parse(parameter);
        let parameters = [];
        for (let i = 0; i < parse.length; i++) {
          let paramName = parse[i].paramName;
          if(paramName =="charId"){
            this.linkData.charId = parse[i].paramValue
          }else{
            let params={}
            params.paramName= parse[i].paramName;
            params.paramValue= parse[i].paramValue;
            params.paramCell= parse[i].paramCell;
            parameters.push(params)
          }
        }
        this.linkData.parameter = parameters;
        this.hyperlinksData.data = parameters;
       }
      },
      //网络报表点击事件
      networkClick(value){
        let id = api.checkParam(value);
        $http.get({
          url: id,
          success:(result)=>{
            let linksData = [];
            let i=0;
            for (const data of result) {
              let newData={}
              newData.paramName = data.paramName
              newData.paramValue = data.paramValue
              newData.tableIndex = i
              linksData.push(newData);
              i++;
            }
           this.hyperlinksData.data = linksData
          },
          fail:()=>{
            this.hyperlinksData.data=[];
          }
        })
      },
      hyperlinksOk(name){
        this.$refs[name].validate((valid) => {
          if (valid) {
            let data = this.linkData;
            let linksData = this.hyperlinksData.data;
            for(let item of linksData){
              item.dbCode = this.currentDbCode
              item.fieldName = this.fieldName
            }
            data.parameter=JSON.stringify(linksData);
            if(!data.reportId){
                data.reportId = excel_config_id
            }
            $http.post({
              url:api.linkSaveAndEdit,
              contentType:'json',
              data:JSON.stringify(data),
              success:(result)=>{
                if(!vm.chartsflag){
                let split = this.excel.split(",");
                let linkIds = xs.cellProp(split[0],split[1], "linkIds")
                if(linkIds){
                  result=linkIds+","+result
                }
                xs.cellProp(split[0],split[1], {linkIds: result})
                xs.cellProp(split[0],split[1], {display: "link"})
                this.$emit('lingcallback',result)
                }else{
                  let dataIds = vm.dataSettings['linkIds'];
                  let linkIds = "";
                  if(dataIds){
                    linkIds = dataIds+","+result;
                  }else{
                    linkIds = result;
                  }
                  let dataSettings = vm.dataSettings;
                  dataSettings['linkIds'] = linkIds
                  //更新ExtData
                  xs.updateChartExtData(vm.selectedChartId,dataSettings);
                  this.$emit('lingcallback',linkIds)
                }
                this.close(name);
              }
            });
          }
        })

      },
      hyperlinksCancel(name){
        if(this.linkData){
          this.$emit('lingcallback',this.linkData.id)
        }
        this.close(name);
      },
      //清除数据
      close(name){
        this.linkData.id="";
        this.linkData.reportId="";
        this.linkData.parameter="";
        this.linkData.ejectType="";
        this.linkData.linkName="";
        this.linkData.apiMethod="";
        this.linkData.linkType="";
        this.linkData.apiUrl="";
        this.linkData.charId = "";
        this.reportData=[];
        this.hyperlinksData.data=[];
        this.selectParamTables=[];
        this.chartData=[];
        this.paramData=[]
        this.fieldList=[]
        this.dbCode=""
        this.fieldName=""
        this.hyperlinksShow=false
        this.currentChart=""
        this.$refs[name].resetFields();
      },
      renderInput(h, params, field,tabIndex,placeholder) {
        let key = params.column.key;
        let paramName = params.row.paramName;
        if(this.linkData.linkType==='2' && paramName==='charId'){
          return h('i-input', {
            props: {
              "size":"small",
              type: 'text',
              readonly:true,
              value: this[tabIndex].data[params.index][field]
            }
          })
        }else{
        return h('i-input', {
          props: {
            "size":"small",
            type: 'text',
            value: this[tabIndex].data[params.index][field],
            placeholder: placeholder?placeholder:`请输入`+params.column.title
          },
          on: {
            'on-blur': (event) => {
              if(tabIndex==="hyperlinksData"){
                let tableIndexArr = this.selectParamTables.map(item=>item.tableIndex);
                this.hyperlinksData.data.forEach(item=>{
                  if(tableIndexArr.includes(item.tableIndex)){
                    item._checked = true;
                  }
                });
              }
              this[tabIndex].data[params.index][field] = event.target.value;
            }
          },
        })
        }
      },
      charsClick(val) {
        this.linkData.charId=val
        let id = api.checkParam(excel_config_id);
        let that = this
        $http.get({
          url: id,
          success: (result) => {
            that.hyperlinksData.data = []
            let i=2
            let dataStr="";
            for (const data of result) {
              let newData = {}
              if(dataStr.indexOf(data.paramName) == -1){
                newData.paramName = data.paramName
                newData.paramValue = data.paramValue
                newData.tableIndex = i
                that.hyperlinksData.data.push(newData);
                dataStr = dataStr +data.paramName+",";
                i++;
              }
            }
          }
        })
      },
      validateHttpExist(rule, value, callback){
        if(value){
          if(value.includes("http")){
            callback();
          }else{
            callback(new Error('请输入http或https'));
          }
        }
      },
      /**
       * 获取报表参数下拉选项
       * @param data
       * @returns {*[]|[{title: string, value: *}, {title: string, value: *}]}
       */
      getChartParamSelectOptions() {
        //判断是选择图表还是单元格
        if(!vm.chartsflag){
          let arr = []
          for(let item of this.fieldList){
            arr.push({
              title: item.fieldText, value: item.title
            })
          }
          return arr;
        }else{
          let data = this.currentChart
          if(!data){
            return []
          }
          if(!data.x || !data.y){
            return []
          }
          let arr = [{
            title: '图表分类属性', value: 'name',
          },{
            title: '图表值属性', value: 'value'
          }]
          if(data.z){
            arr.push({
              title: '图表系列属性', value: 'seriesName'
            })
          }
          return arr;
        }
      },
      
      //渲染select_input组件中的props
      randerSelectInput(params,paramSelectOptions) {
        //判断是选择图表还是单元格
        let assembly = new Map();
        let props = {};
        if (!vm.chartsflag) {
          props = {
            options: paramSelectOptions,
            size: 'small',
            transfer: true,
            value: this.hyperlinksData.data[params.index].paramValue,
            clearable: true
          }
          assembly.set("select","j-select-input")
        }else{
          props = {
            size:'small',
            transfer: true,
            value: this.hyperlinksData.data[params.index].paramValue,
            clearable: true,
          }
          assembly.set("select","i-select")
        }
        assembly.set("props",props)
        return assembly;
      },
      //判断是否选中图表
      ifSelectChart(){
        if(vm && vm.chartsflag){
          return true;
        }
        return false;
      }
    },
  })
</script>
<style>
  .ivu-form-item-error-tip{
      position:relative;
  }
</style>