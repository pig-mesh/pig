<script type="text/x-template" id="yAxis-setting-template">
    <div>
        <Submenu :name="'yAxis'+index" style="border-bottom: inset 1px;" v-for="(item,index) in yAxisOptions">
            <template slot="title">
                <span class="rightFontSize">{{getTitle(index)}}Y轴设置</span>
            </template>
            <div class="blockDiv" style="padding-bottom: 10px">
                <Row class="ivurow">
                    <p>显示&nbsp;&nbsp;</p>
                    <i-switch size="small" style="margin-left: 141px;" v-model="item.show" @on-change="onYAxisChange"/>
                </Row>
                <Row class="ivurow">
                    <p>Y轴名称&nbsp;&nbsp;</p>
                    <i-input class="rightFontSize iSelect" size="small" v-model="item.name" @on-blur="onYAxisChange" style="margin-left: 4px"></i-input>
                </Row>
                <Row class="ivurow" v-if="isCharHasYMin">
                    <p>最小值&nbsp;&nbsp;</p>
                    <i-input size="small" class="rightFontSize iSelect" style="margin-left: 12px;" v-model="item.min" @on-blur="onYAxisChange"/>
                </Row>
                <Row class="ivurow">
                    <p>分隔线&nbsp;&nbsp;</p>
                    <i-switch size="small" style="margin-left: 128px;" v-model="item.splitLine_show" @on-change="onYAxisChange"/>
                </Row>
                <Row class="ivurow" v-if="typeof item.splitLine_show !== 'undefined' && item.splitLine_show == true">
                    <p>颜色设置&nbsp;&nbsp;</p>
                    <Col>
                    <i-input class="rightFontSize iSelect" v-model="item.splitLine_lineStyle_color" size="small" @on-change="onYAxisChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="item.splitLine_lineStyle_color" :editable="false"  :transfer="true" size="small" @on-change="onYAxisChange"/>
                    </span>
                    </i-input>
                    </Col>
                </Row>
                <Row class="ivurow">
                    <p style="margin-bottom: 10px;">字体大小&nbsp;&nbsp;</p>
                    <i-input class="rightFontSize iSelect" size="small" type="number" v-model="item.axisLabel_textStyle_fontSize" @on-blur="onYAxisChange"></i-input>
                </Row>
                <Row class="ivurow" v-if="typeof item.axisLabel_textStyle_color !== 'undefined'">
                    <p>字体颜色&nbsp;&nbsp;</p>
                    <Col>
                    <i-input class="rightFontSize iSelect" v-model="item.axisLabel_textStyle_color" size="small" @on-change="onYAxisChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="item.axisLabel_textStyle_color" :editable="false"  :transfer="true" size="small" @on-change="onYAxisChange"/>
                    </span>
                    </i-input>
                    </Col>
                </Row>
                <Row class="ivurow" v-if="typeof item.axisLine_lineStyle_color !== 'undefined'">
                    <p>轴线颜色&nbsp;&nbsp;</p>
                    <Col>
                    <i-input class="rightFontSize iSelect" v-model="item.axisLine_lineStyle_color" size="small" @on-change="onYAxisChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="item.axisLine_lineStyle_color" :editable="false"  :transfer="true" size="small" @on-change="onYAxisChange"/>
                    </span>
                    </i-input>

                    </Col>
                </Row>
            </div>
        </Submenu>
    </div>
</script>
<script>
    Vue.component('j-yaxis-setting', {
        template: '#yAxis-setting-template',
        props: {
            settings: {
                type: [Object, Array],
                required: true,
                default: null
            },
            dataIndex: 0
        },
        data(){
            return {
                yAxisOption: {
                  splitLine_show:false
                },
                yAxisOptions: [],
                isCharHasYMin: true //是否显示y轴最小值
            }
        },
        watch: {
            settings: {
                deep: true,
                immediate: true,
                handler: function (){
                    this.initData()
                }
            },
            //update-begin--Author:wangshuai  Date:20211215 for：监听图表选中类型----
            'vm.selectedChartType':{
              immediate: true,
              handler() {
                //判断选中的图表是否为象形图，象形图不用设置最小值
                if(vm.selectedChartType == 'pictorial.spirits'){
                  this.isCharHasYMin=false;
                }else{
                  this.isCharHasYMin=true;
                }
              }
            }
            //update-end--Author:wangshuai  Date:20211215 for：监听图表选中类型----
        },
        methods: {
            getTitle: function(index) {
                if (this.settings instanceof Array){
                    if (index == 0){
                        return '左'
                    }
                    if (index == 1){
                        return '右'
                    }
                }else{
                    return ''
                }
            },
            initData: function (){
                if (this.settings){
                    if (this.settings instanceof Array){
                        this.yAxisOptions = this.settings;
                        this.yAxisOption = Object.assign(this.yAxisOption, this.settings[this.dataIndex])
                    } else {
                        this.yAxisOptions=[];
                        this.yAxisOptions.push(this.settings);
                        this.yAxisOption = Object.assign(this.yAxisOption, this.settings)
                    }

                }
            },
            onYAxisChange(){
                if (this.settings instanceof Array){
                    //update-begin--Author:wangshuai  Date:20211213 for：[issues/I4LZ63]折线图Y设置最小值;当存在最小值并且为空的时候那么就清空，保证数据初始化----
                    this.initYAxisOptionsMin(this.yAxisOptions,true)
                    //update-end--Author:wangshuai  Date:20211213 for：[issues/I4LZ63]折线图Y设置最小值;当存在最小值并且为空的时候那么就清空，保证数据初始化----
                    this.$emit('change', 'yAxis', this.yAxisOptions)
                } else {
                    //update-begin--Author:wangshuai  Date:20211213 for：[issues/I4LZ63]折线图Y设置最小值;当存在最小值并且为空的时候那么就清空，保证数据初始化----
                    this.initYAxisOptionsMin(this.yAxisOptions[0],false)
                    //update-end--Author:wangshuai  Date:20211213 for：[issues/I4LZ63]折线图Y设置最小值;当存在最小值并且为空的时候那么就清空，保证数据初始化----
                    this.$emit('change', 'yAxis', this.yAxisOptions[0])
                }
            },
           /**
            * 初始化最小值
            * @param yAxisOptions y轴数组对象
            * @param isArray 是否为数组 true是 false不是
            */
            initYAxisOptionsMin(yAxisOptions,isArray){
              if(!yAxisOptions.min){
                delete yAxisOptions.min
                if(isArray){
                  this.yAxisOptions = yAxisOptions
                }else{
                  this.yAxisOptions[0] = yAxisOptions 
                }
              }
            }
        }
    })
</script>