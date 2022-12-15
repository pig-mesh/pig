<script type="text/x-template" id="match-setting-template">
    <div class="match_setting rightFontSize" >
        <Submenu name="19">
            <template slot="title">
                <span>自定义配色</span>
                <Tooltip placement="top" content="【边缘颜色】选用相对更深色" :transfer="true" v-if="selectedChartType.indexOf('scatter.bubble')!==-1">
                    <Icon size="16" style="margin-left: 10px;" type="ios-help-circle-outline"/>
                </Tooltip>
            </template>
            <div style="margin-left: 25px;margin-top: -15px;">
                <Row class="ivurow" style="margin-bottom: 5px;">
                    <i-button type="primary" size="small" @click="colorMatchModalOpen">新增</i-button>
                </Row>
                <i-table class="rightFontSize" :columns="columns2" :data="colorMatchData"></i-table>
            </div>
        </Submenu>
        <Modal :loading="loading" v-model="colorMatchModal" title="自定义配色" :width="30" @on-ok="addColorMatch" @on-cancel="onCancel">
            <div style="padding-right: 50px" class="dataSourceForm">
                <i-form :model="colorMatch" label-colon :label-width="90">
                    <form-item :label="colorLabel">
                        <row>
                            <i-col span="12">
                                <i-input v-model="colorMatch.color" size="small" style="width: 111px;" >
                                    <span slot="append">
                                        <color-picker class="colorPicker" v-model="colorMatch.color" :editable="false"  :transfer="true" size="small" />
                                    </span>
                                </i-input>
                            </i-col>
                        </row>
                    </form-item>
                    <form-item label="边缘颜色" v-if="selectedChartType.indexOf('scatter.bubble') !== -1">
                        <row>
                            <i-col span="12">
                                <i-input v-model="colorMatch.edgeColor" size="small" style="width: 111px;" >
                                    <span slot="append">
                                        <color-picker class="colorPicker" v-model="colorMatch.edgeColor" :editable="false"  :transfer="true" size="small" />
                                    </span>
                                </i-input>
                            </i-col>
                        </row>
                    </form-item>
                    <form-item label="边框颜色" v-if="selectedChartType.indexOf('radar')>-1">
                        <row>
                            <i-col span="12">
                                <i-input v-model="colorMatch.lineColor" size="small" style="width: 111px;" >
                                    <span slot="append">
                                        <color-picker class="colorPicker" v-model="colorMatch.lineColor" :editable="false"  :transfer="true" size="small" />
                                    </span>
                                </i-input>
                            </i-col>
                        </row>
                    </form-item>
                </i-form>
            </div>
        </Modal>
    </div>
</script>
<script>
    Vue.component('j-match-setting', {
        template: '#match-setting-template',
        props: {
            chartOptions: {
                type: Object,
                default: () => {
                }
            },
            dataSettings: {
                type: [Object, String],
                default: () => {
                }
            }
        },
        data(){
            return {
                colorMatchData: [],
                colorMatch: {name:'',color:'',edgeColor:"",opacity:1,lineColor:''},
                seriesList: [],
                selectedChartType: '',
                colorMatchModal: false,
                loading: true,
                columns2: [
                    {
                        title: '系列颜色',
                        key: 'color',
                        align: 'left',
                        width: 110,
                        render: (h, params) => {
                            return this.renderColorButton(h, params, 'color', 1);
                        }
                    },
                    {
                        title: '操作',
                        key: 'action',
                        align: 'right',
                        width: 80,
                        render: (h, params) => {
                            return this.renderColorButton(h, params, 'action', 1);
                        }
                    }
                ],
                colorMatchModal: false

            }
        },
        watch: {
            chartOptions: {
                deep: true,
                immediate: true,
                handler: function (){
                    this.initData()
                }
            }
        },
        computed: {
            colorLabel: function (){
                return this.selectedChartType.indexOf('scatter.bubble') !== -1 ? '中心颜色' : '颜色'
            }
        },
        methods: {
            //渲染button
            renderColorButton(h, params, key, type){
                // console.log('params', params)
                if (key == 'action'){
                    return h('div', {style: {display: 'flex'}}, [
                        h('i-button', {
                            props: Object.assign({}, {
                                type: 'default',
                                size: 'small',
                                type: "text"
                            }, {
                                icon: 'md-create',
                            }),
                            on: {
                                "click": () => {
                                    if (type == 1){
                                        this.colorMatch = params.row;
                                        this.colorMatchModal = true;
                                    } else {
                                        this.seriesObj = params.row;
                                        this.seriesModal = true;
                                    }
                                }
                            }
                        }),
                        h('i-button', {
                            props: Object.assign({}, {
                                size: 'small',
                                type: "text"
                            }, {
                                icon: 'md-close',
                            }),
                            on: {
                                click: () => {
                                    this.$Modal.confirm({
                                        title: '提示',
                                        content: '是否确认删除?',
                                        onOk: () => {
                                            if (type == 1){
                                                this.colorMatchData.splice(params.index, 1);
                                                this.onColorChange()
                                            } else {
                                                this.seriesTypeData.splice(params.index, 1);
                                                this.runChart();
                                            }
                                        }
                                    });
                                }
                            }
                        })
                    ])
                } else {
                    //行数据显示渲染和编辑操作
                    return h('div', {
                            style: {
                                display: 'flex',
                                width: '100px',
                                alignItems: 'center'
                            }
                        },
                        [
                            (type === 1) ? h('div', {style: {'background': params.row.color, 'width': '15px', 'height': '15px'}}) : h('div', {style: {}}, params.row.type),
                            h('div', [
                                h('div', {style: {display: 'inherit', width: type === 1 ? '75px' : '60px', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap'}}, '\xa0' + params.row.name)
                            ])
                        ])
                }
            },
            colorMatchModalOpen(){
                this.colorMatchModal = true
            },
            initData: function (){
                if (this.chartOptions){
                    // console.info("########", JSON.stringify(this.dataSettings))
                    this.selectedChartType = this.dataSettings.chartType;
                    let colorArr = [];
                    let seriesList = []
                    let arr = this.chartOptions.series
                    arr.forEach(object => {
                        let type = object.type
                        if (type == 'bar' || type == 'line'){
                            if (object.itemStyle && object.itemStyle.color){
                                colorArr.push({name: object.name, color: object.itemStyle.color})
                            }
                            seriesList.push(object.name)
                        } else if(type=='graph'){
                            //关系图自定义的数据
                            object.categories.forEach(categoryObj=>{
                                if(categoryObj.itemStyle && categoryObj.itemStyle.color){
                                    let isExis = colorArr.filter(item=>item.name===categoryObj.name);
                                    if(isExis.length===0){
                                        colorArr.push({name:categoryObj.name,color:categoryObj.itemStyle.color})
                                    }
                                }
                            })
                            seriesList=object.categories.map(item=>{return item.name}).filter(function(item, index, self){ return  self.indexOf(item) == index; });
                        }else if (type == 'scatter'){
                            if (object.itemStyle && object.itemStyle.color && object.itemStyle.color.colorStops){
                                let colorStops = object.itemStyle.color.colorStops;
                                colorArr.push({name: object.name, color: colorStops[0].color, edgeColor: colorStops[1].color})
                            }
                            seriesList.push(object.name)
                        } else if (type == 'radar'){
                            object.data.forEach(item => {
                                if (item.areaStyle && item.areaStyle.color && item.areaStyle.color != ''){
                                    colorArr.push({name:item.name,color:item.areaStyle.color,opacity:item.areaStyle.opacity,lineColor:item.lineStyle.color})
                                }
                                seriesList.push(item.name)
                            });

                        } else if (type == 'pie' || type == 'funnel'){
                            object.data.map(item => {
                                if (item.itemStyle && item.itemStyle.color){
                                    colorArr.push({name: item.name, color: item.itemStyle.color})
                                }
                                seriesList.push(item.name)
                            })
                        } else if (type == 'gauge'){
                            let arr = object.axisLine.lineStyle.color;
                            for (let j = 0, len = arr.length; j < len; j++) {
                                if ((arr[0] && arr[0][1] == '#91c7ae') && (arr[1] && arr[1][1] == '#63869E')){
                                    break;
                                }
                                colorArr.push({color: arr[j][1], name: ""})
                            }
                        }
                    })
                    // console.info("自定义颜色", colorArr)
                    //已设置的颜色配置
                    this.colorMatchData = colorArr
                    //系列下拉框数据
                    this.seriesList = seriesList
                }
            },
            addColorMatch(){
                let obj={...this.colorMatch};
                if(obj._index>=0){
                    this.colorMatchData.splice(obj._index,1,obj);
                }else{
                    this.colorMatchData.push(obj)
                }
                //运行到图表
                this.onColorChange();
                this.colorMatchModal=false;
                this.colorMatch={name:'',color:'',edgeColor:"",opacity:1,lineColor:''};
            },
            onColorChange(){
                // console.log('this.colorMatchData==>',JSON.stringify(this.colorMatchData));
                // console.log('this.seriesTypeData==>',JSON.stringify(this.seriesTypeData));
                if(!this.colorMatchData){
                    return;
                }
                let arr = []
                if(this.selectedChartType.indexOf('pie')>=0 || this.selectedChartType.indexOf('funnel')>=0|| this.selectedChartType.indexOf('radar')>=0){
                    arr = this.chartOptions['series'][0]['data']
                }else if(this.selectedChartType.indexOf('graph')>=0){
                    //处理关系图的自定义配色
                    arr = this.chartOptions['series'][0]['categories']
                }else{
                    arr = this.chartOptions['series']
                }
                // console.log('this.arr==>',JSON.stringify(arr));
                let seriesArray = []
                let i = 0;
                for(let item of arr){
                    //删除背景色
                    delete item['areaStyle']
                    delete item['lineStyle']
                    if(this.selectedChartType.indexOf('gauge')>=0){
                        break;
                    }
                    if(this.selectedChartType.indexOf('radar')>=0) {
                        let color = ''
                        let lineColor = ''
                        let opacity = 1
                        if(i<=this.colorMatchData.length) {
                          let colorMatchDatum = this.colorMatchData[i];
                          if (colorMatchDatum) {
                            color = colorMatchDatum['color']
                            lineColor = colorMatchDatum['lineColor']
                            opacity = colorMatchDatum['opacity']
                            //设置雷达图背景色和透明度
                            if(color)
                            {
                              item['areaStyle'] = {color: color, opacity: opacity}
                            } else {
                              item['areaStyle'] = {color: "", opacity: 0}
                            }
                            //设置雷达图边框颜色
                            if(lineColor)
                            {
                              item['lineStyle'] = {color: lineColor}
                            } else {
                              item['lineStyle'] = {color: ""}
                            }
                          }
                          i++;
                        }

                    }else if(this.selectedChartType.indexOf('scatter')>=0){
                      if(i<=this.colorMatchData.length) {
                        let colorMatchDatum = this.colorMatchData[i];
                         if(colorMatchDatum){
                            let color =colorMatchDatum['color']//0%处的颜色 中心
                            let edgeColor = colorMatchDatum['edgeColor']//100%处的颜色 边缘
                            let tempColorStyle={type:'radial',r: 0.5,colorStops: [{offset: 0, color:color}, {offset: 1, color: edgeColor}]}
                            if (!item['itemStyle']) {
                                item['itemStyle'] = {color:tempColorStyle};
                            }else{
                                item['itemStyle']['color'] = tempColorStyle;
                            }
                         }
                         i++;
                        }

                    }else{
                        if (!item['itemStyle']) {
                            item['itemStyle'] = {color: ''}
                        }
                        let color = ''
                        if (i<=this.colorMatchData.length){
                          let colorMatchDatum = this.colorMatchData[i];
                          if(colorMatchDatum){
                            color = this.colorMatchData[i]['color']
                            item['itemStyle']['color'] = color
                          }else{
                            item['itemStyle']['color'] = ""
                          }
                          i++;
                        }
                    }

                    if(this.selectedChartType.indexOf('mixed')>=0){
                        seriesArray.push(JSON.parse(JSON.stringify(item)))
                    }
                }
                if(this.selectedChartType.indexOf('mixed')>=0){
                    this.chartOptions['series'] = seriesArray
                }
                //TODO 仪表盘目前只支持设置三色0.2/0.8/1，后期优化修改
                if(this.selectedChartType.indexOf('gauge')>=0){
                    let c1=this.colorMatchData[0]?this.colorMatchData[0].color:"#91c7ae";
                    let c2=this.colorMatchData[1]?this.colorMatchData[1].color:"#63869E";
                    let c3=this.colorMatchData[2]?this.colorMatchData[2].color:"#C23531";
                    let arr= [[0.2, c1], [0.8, c2], [1, c3]];
                    this.chartOptions['series'][0]['axisLine']['lineStyle'].color =arr
                }
                // console.info("this.chartOptions",JSON.stringify(this.chartOptions))
                let id = this.dataSettings.id;
                xs.updateChart(id , this.chartOptions);
            },
            onCancel(){
                this.colorMatch={color:'',edgeColor:'',lineColor:''}
            }
        }
    })
</script>