
<script type="text/x-template" id="sql-function-replace-template">
  <div>
    <!-- 新增数据集 弹框-begin -->
    <Modal width="800px" :loading="loading" v-model="sqlFunctionShow" :title="moduleTitle">
      <div slot="footer">
        <Button type="text" size="small" @click="clearSqlFunction">取消</Button>
        <Button type="primary" size="small" @click="saveSqlFunction">确定</Button>
      </div>
      <Table ref="paramTable" :columns="functionColumns" :data="functionData">
        <template slot="paramValue" slot-scope="props">
          <i-form :ref="'formDynamic'+props.idx" :model="props.row">
            <form-item prop="paramValue" style="margin-top: 10px">
              <i-input v-model="props.row.paramValue" size="small" />
            </form-item>
          </i-form>
        </template>
      </Table>
    </Modal>
    <!-- 新增数据集 弹框-end -->
  </div>
</script>

<script>
  Vue.component('j-sql-function-replace', {
    template: '#sql-function-replace-template',
    data(){
      return {
        moduleTitle:"函数参数替换",
        sqlFunctionShow:false,
        functionColumns: [
          {
            type: 'index',
            width: 60,
            align: 'center'
          },
          {
            title: '参数',
            key: 'paramName',
            width: '200',
            render: (h, params) => {
              return this.renderInput(h, params, 'paramName','functionData')
            }
          },
          {
            title: '默认值',
            key: 'paramValue',
            width: '200',
            render: (h, params) => {
              this.functionData[params.index] = params.row;
              return h(
                  "div",
                  this.$refs.paramTable.$scopedSlots.paramValue({
                    row: params.row,
                    idx: params.row._index
                  })
              )
            }
          }
        ],//函数列
        functionData:[], //函数数据
        loading:false,
        dbDynSql:"",
        dbKey:""
      }
    },
    methods: {
      renderInput(h, params, field,tabIndex,placeholder) {
        return h('i-input', {
          props: {
            "size":"small",
            type: 'text',
            readonly:true,
            value: this.functionData[params.index][field],
            placeholder: placeholder?placeholder:`请输入`+params.column.title
          },
          on: {
            'on-change': (event) => {
              this.functionData[params.index][field] = event.target.value;
            }
          },
        })
      },
      //初始化参数解析
      initDataSource(dbDynSql,dbKey,paramData){
        this.dbDynSql = dbDynSql
        this.dbKey = dbKey
        //update-begin---author:wangshuai   Date:20211221  for：[JMREP-2505]存储过程没有空格参数会连接到一起------------
        let reg=/\$\{[^}]*\}/g;
        //update-end---author:wangshuai   Date:20211221  for：[JMREP-2505]存储过程没有空格参数会连接到一起------------
        let dbDynSqlArr = dbDynSql.match(reg);
        let paramsArr = [];
        if(dbDynSqlArr && dbDynSqlArr.length>0){
          let maxOrderNum = 1;
          //防止参数重复
          let paramExistArray = []
          dbDynSqlArr.forEach((item,index)=>{
            let paramName = item.replace("$\{","").replace("}","").trim();
            if(paramExistArray.indexOf(item)<0 && item){
              paramExistArray.push(item)
              let paramObj = {};
              let paramFilter = paramData.filter(item=> item.paramName==paramName);
              paramObj.paramName = paramName;
              paramObj.tableIndex = index;
              if(!paramFilter[0]){
                paramObj.paramValue = ""
              }else{
                let paramValue = paramFilter[0].paramValue;
                if(paramValue){
                  paramObj.paramValue = paramValue
                }
              }
              paramsArr.push(paramObj);
            }
          })
        }
        this.functionData = paramsArr;
      },
      //修改报表SQl确定事件
      saveSqlFunction(){
        let dbDynSql = this.dbDynSql;
        let functionData = this.functionData;
        let model = true
        let that = this
        for (const data of functionData) {
            let paramName = data.paramName;
            let paramValue = data.paramValue;
            if(dbDynSql.indexOf("isNotEmpty")<0 && dbDynSql.indexOf("${'<#if'}")<0){
              if(!paramValue){
                that.sqlFunctionShow=true
                that.$Message.warning("请输入"+paramName+"对应的值")
                model = true;
                break;
              }else{
                model = false;
              }
            }else{
              model = false;
              break;
            }
        }
        if(model == false){
          this.$emit("functionok", this.functionData)
          this.clearSqlFunction()
        }else{
          this.sqlFunctionShow=true
        }
      },
      clearSqlFunction(){
        this.sqlFunctionShow=false
        this.dbDynSql=""
        this.dbKey= ""
        this.functionData=[]
      },
      //默认值加验证不为空
      async childrenRules(){
        let success = true
        for (let i = 0, len = this.functionData.length; i < len; i++) {
          this.$refs['formDynamic' + i].validate(valid => {
            if (!valid) {
              success=false
            }
          })
        }
        return success;
      },
    }
  })
</script>