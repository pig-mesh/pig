<script type="text/x-template" id="chart-linkage-template">
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
                        @on-selection-change="handleClParamSelected"
                        stripe
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

    Vue.component('j-chart-linkage', {
        template: '#chart-linkage-template',
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
                        render: (h, params) => {
                            let paramSelectOptions = this.getChartParamSelectOptions()
                            return h('i-select', {
                                    props: {
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
                this.requirement = ''
                this.linkChartId = ''
                // 当前图表
                this.currentChart = ''
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
            addChartLinkage(existChartList, currentChartId){
                this.clearChartLinkage()
                this.reportId = excel_config_id
                this.linkType = '2'
                this.initChartList(existChartList, currentChartId)
                this.chartLinkageShow = true
            },
            editChartLinkage(existChartList, currentChartId, row){
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
                this.linkId = row.id
                this.linkChartId = row.linkChartId
                this.linkName = row.linkName
                this.requirement = row.requirement
                this.linkType = row.linkType
                this.reportId = row.reportId
                if(row.parameter){
                    let arr = JSON.parse(row.parameter)
                    this.clParamData = [...arr]
                    this.oldConfig = {
                        linkChartId: row.linkChartId,
                        params: [...arr]
                    }
                }
                this.initChartList(existChartList, currentChartId)
                this.chartLinkageShow = true
            },
            initChartList(existChartList, currentChartId){
                if(existChartList && existChartList.length>0){
                    let arr1 = existChartList.filter(item=>{
                        return item.id!=currentChartId
                    });
                    let arr2 = existChartList.filter(item=>{
                        return item.id==currentChartId
                    });
                    this.existChartList = [...arr1]
                    this.currentChart = arr2[0]
                }
            },
            // 确定事件
            chartLinkageOk(){
                let obj = {
                    reportId: this.reportId,
                    linkName: this.linkName,
                    requirement: this.requirement,
                    linkChartId: this.linkChartId,
                    linkType: '2',
                    parameter: JSON.stringify(this.clParamData)
                }
                if(this.linkId){
                    obj['id'] = this.linkId
                }
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
                let data = this.currentChart
                if(!data){
                    console.log("未选择联动图表!")
                    return []
                }
                if(!data.x || !data.y){
                    console.log("图表未正确配置数据源!")
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
                console.log('selections', selections)
                let arr = []
                selections.map(k=>{
                    arr.push(k.index)
                })
                this.selectedClParams = arr
            }

        }
    })
</script>