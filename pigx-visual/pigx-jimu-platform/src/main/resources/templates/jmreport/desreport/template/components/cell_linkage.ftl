<style>
    .jimu-table-tip{
        font-weight: bold;
        display: inline-block;
        margin-left: 5px;
    }
</style>
<script type="text/x-template" id="cell-linkage-template">
    <Modal
        width="600px"
        title="图表联动"
        v-model="chartLinkageShow"
        :closable = "true"
        :mask-closable="false">
        <div slot="footer">
            <i-button @click="chartLinkageCancel">取消</i-button>
            <i-button type="primary" @click="chartLinkageOk">确定</i-button>
        </div>
        <i-form :label-width="100">
            <form-item label="链接名称">
                <i-input placeholder="请填写链接名称" v-model="linkName"></i-input>
            </form-item>

            <form-item label="链接图表">
                <i-select v-model="linkChartId" filterable @on-change="chartLinkageChange">
                    <i-option v-for="(item, index) in existChartList" :key="index" placeholder="请选择链接图表" :value="item.id">{{ item.name }}</i-option>
                </i-select>
            </form-item>
          
            <form-item label="条件">
              <i-input placeholder="请填写条件" v-model="requirement"></i-input>
            </form-item>

            <span>参数设置</span>
            <div style="margin-top: 10px">
                <i-button type="primary" @click="addClParam">新增</i-button>
                <i-button @click="removeClParam" v-if="selectedClParams.length>0">删除</i-button>
                <i-table
                        style="margin-top: 10px"
                        ref="paramTable"
                        stripe
                        @on-selection-change="handleClParamSelected"
                        :columns="clParamColumns"
                        :data="clParamData">
                </i-table>
            </div>
        </i-form>
    </Modal>
</script>
<script>

    /**
     * 处理 字段
     * @param list
     */
    function handleLinkageFields(list) {
        let { x, y, z } = this.currentChart
        let arr = []
        arr.push(x)
        arr.push(y)
        arr.push(z)
        let dictInfo = {}
        for(let item of list){
            if(item.dictCode && arr.indexOf(item.fieldName)>=0){
                dictInfo[item.fieldName] = item.dictCode
            }
        }
        this.dictInfo = dictInfo
    }

    /**
     * 处理参数
     * @param list
     */
    function handleLinkageParams(list) {
        let ls = []
        let index = 0
        for(let item of list){
            ls.push({
                paramName: item.paramName,
                paramValue: '',
                index: ++index
            })
        }
        this.clParamData = ls
    }

    function handleLinkageOldParam(){
        const { selectedChart, oldConfig } = this
        if(selectedChart.id == oldConfig.linkChartId){
            this.clParamData = [...oldConfig.params]
        }
    }

    Vue.component('j-cell-linkage', {
        template: '#cell-linkage-template',
        props: {
            settings: {
                type: Object,
                required: true,
                default:()=>{}
            }
        },
        data(){
            return {
                chartLinkageShow: false,
                linkId: '',
                reportId: '',
                linkType: '2',
                linkName: '',
                linkChartId: '',
                requirement:'',
                currentChart:'',
                selectedChart: '',
                existChartList: [],

                dictInfo: '',

                clParamData:[],
                selectedClParams: [],
                clParamColumns:[
                    {
                        type: 'selection',
                        width: 35,
                        align: 'center'
                    },{
                        title: '映射参数',
                        key: 'paramName',
                        render: (h, params) => {
                            return h('i-input', {
                                props: {
                                    "size":"small",
                                    type: 'text',
                                    value: this.clParamData[params.index].paramName,
                                    placeholder: `请输入参数名`
                                },
                                on: {
                                    'on-blur': (event) => {
                                        this.clParamData[params.index].paramName = event.target.value;
                                    }
                                },
                            })
                        }
                    },{
                        title: '原始参数',
                        key: 'paramValue',
                        renderHeader: (h, params) => {
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
                                        h('p','3.支持表达式=A1表示A1单元格的值'),
                                        h('p','2.支持表达式=A表示当前点击行所在的A列单元格的值')
                                    ])
                                ])
                                arr.push(it1)
                                arr.push(it2)
                                return arr
                            }
                            return h('div', {}, sub())
                        },
                        render: (h, params) => {
                            let paramSelectOptions = this.getChartParamSelectOptions()
                            return h('j-select-input', {
                                    props: {
                                        options: paramSelectOptions,
                                        size:'small',
                                        transfer: true,
                                        value: this.clParamData[params.index].paramValue,
                                        clearable: true,
                                    },
                                    on: {
                                        'on-change': (value) => {
                                            this.clParamData[params.index].paramValue = value;
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
                    }
                ]

            }
        },
        created(){
            this.settingsDivHeight = window.innerHeight - 100
        },
        watch:{
            settings:{
                deep: true,
                immediate: true,
                handler: function(){
                  //  this.resetForm()
                }
            }
        },
        methods:{
            // 清空
            clearChartLinkage(){
                this.linkId = ''
                this.reportId = ''
                this.linkName = ''
                this.linkChartId = ''
                this.requirement = ''
                // 当前单元格数据集对应的 字段集合
                this.fieldList = []
                this.currentDbCode = ''
                this.fieldName = ''
                // 联动下拉框选中的图表
                this.selectedChart = ''
                // 字典信息
                this.dictInfo = ''
                // 联动下拉框的集合数据xs.get获取
                this.existChartList = []
                // 图表联动参数数据
                this.clParamData = []
                // 选中的参数index集合 删除用到
                this.selectedClParams = []
                // 编辑页面回显数据 参数记录
                this.oldConfig = {}
            },
            addChartLinkage(existChartList, fieldList, dbCode, fieldName){
                this.clearChartLinkage()
                this.reportId = excel_config_id
                this.linkType = '2'
                if(existChartList && existChartList.length>0){
                    this.existChartList = [...existChartList]
                }
                this.fieldList = fieldList
                this.currentDbCode = dbCode
                this.fieldName = fieldName
                this.chartLinkageShow = true
            },
            editChartLinkage(existChartList, fieldList, dbCode, fieldName, row){
                // 需要给row赋值
            /*    apiMethod: null
                apiUrl: null
                ejectType: null
                id: "552823131696795648"
                linkChartId: "b9Nqn1FauH9p5C1I"
                linkName: "121212"
                linkType: "2"
                parameter: "[{\"paramName\":\"sex\",\"paramValue\":\"sex\",\"index\":1}]"
                reportId: "552378751072604160"*/
                this.clearChartLinkage()

                if(existChartList && existChartList.length>0){
                    this.existChartList = [...existChartList]
                }
                this.fieldList = fieldList
                this.currentDbCode = dbCode
                this.fieldName = fieldName
                this.linkId = row.id
                this.linkChartId = row.linkChartId
                this.linkName = row.linkName
                this.linkType = row.linkType
                this.reportId = row.reportId
                this.requirement = row.requirement
                if(row.parameter){
                    let arr = JSON.parse(row.parameter)
                    this.clParamData = [...arr]
                    this.oldConfig = {
                        linkChartId: row.linkChartId,
                        params: [...arr]
                    }
                }

                this.chartLinkageShow = true
            },

            // 确定事件
            chartLinkageOk(){
                let arr = [...this.clParamData]
                for(let item of arr){
                    item.dbCode = this.currentDbCode
                    item.fieldName = this.fieldName
                }
                let obj = {
                    reportId: this.reportId,
                    linkName: this.linkName,
                    linkChartId: this.linkChartId,
                    linkType: '2',
                    parameter: JSON.stringify(arr)
                }
                if(this.linkId){
                    obj['id'] = this.linkId
                }
                obj['requirement'] = this.requirement
                $http.post({
                    url:api.linkSaveAndEdit,
                    contentType:'json',
                    data:JSON.stringify(obj),
                    success:(linkId)=>{
                        // 返回保存的联动表的ID
                        this.chartLinkageShow = false
                        this.$emit('ok',linkId)
                    }
                });

            },
            chartLinkageCancel(){
                this.chartLinkageShow = false
            },
            // 图表下拉框改变事件
            chartLinkageChange(value){
                for(let i=0;i<this.existChartList.length;i++){
                    if(value === this.existChartList[i]['id']){
                        this.selectedChart = this.existChartList[i]
                    }
                }
                this.getLinkageChartInfo();
            },
            // 图表下拉框改变 请求后台获取当前图标的字段 和 联动图标的参数信息
            getLinkageChartInfo(){
                let param = {
                    reportId: this.reportId,
                    linkageDbCode: this.selectedChart.db,
                    dbCode: this.currentChart.db
                }
                $http.get(
                    {
                        url: api.getLinkageChartInfo,
                        data: param,
                        success: (result) => {
                            // console.log(112,result)
                            // 设置被联动图表的参数接收
                            handleLinkageParams.call(this, result.paramList)
                            // 设置当前图表的字段字典信息
                            handleLinkageFields.call(this, result.fieldList)
                            // 编辑页面 如果切换下拉选项 再切回来 需要将初始数据显示出来
                            handleLinkageOldParam.call(this)
                        }
                    });
            },
            /**
             * 获取报表参数下拉选项
             * @param data
             * @returns {*[]|[{title: string, value: *}, {title: string, value: *}]}
             */
            getChartParamSelectOptions() {
                let arr = []
                for(let item of this.fieldList){
                    arr.push({
                        title: item.fieldText, value: item.title
                    })
                }
                /*arr.push({
                    title: '自定义', value: '-1'
                })*/
                return arr;
            },
            // 新增参数
            addClParam(){
                let arr = this.clParamData
                arr.push({
                    index: arr.length+1,
                    paramName: '',
                    paramValue: ''
                })
                this.clParamData = [...arr]
            },
            // 参数移除事件
            removeClParam(){
                let indexArray = this.selectedClParams
                // 先过滤
                let arr = this.clParamData.filter(k=>{
                    return indexArray.indexOf(k.index)<0
                })
                // 重排序
                for(let i=0;i<arr.length;i++){
                    arr[i]['index'] = i+1
                }
                // 赋值
                this.clParamData = [...arr]

            },
            // 参数选中改变事件
            handleClParamSelected(selections){
                // console.log('selections', selections)
                let arr = []
                selections.map(k=>{
                    arr.push(k.index)
                })
                this.selectedClParams = arr
            }

        }
    })
</script>