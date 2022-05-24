<script type="text/x-template" id="gauge-setting-template">
<div>
    <Submenu name="20" style="border-bottom: inset 1px;" class="rightFontSize">
        <template slot="title">
            <span>仪表盘数据设置</span>
        </template>
        <div class="blockDiv" style="padding-bottom: 10px">
            <Row class="ivurow">
                <p>标题显示&nbsp;&nbsp;</p>
                <i-switch size="small" style="margin-left: 115px;" v-model="gaugeOption.title_show" @on-change="onGaugeChange"/>
            </Row>
            <Row class="ivurow">
                <p>标题字体大小&nbsp;&nbsp;</p>
                <i-input size="small" type="number" v-model="gaugeOption.title_textStyle_fontSize" @on-blur="onGaugeChange" style="width: 119px;"></i-input>
            </Row>
            <Row class="ivurow" v-if="typeof gaugeOption.title_textStyle_color !== 'undefined'">
                <p>标题颜色&nbsp;&nbsp;</p>
                <Col>
                <i-input v-model="gaugeOption.title_textStyle_color" size="small" style="width:143px;" @on-change="onGaugeChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="gaugeOption.title_textStyle_color" :editable="false" alpha  :transfer="true" size="small" @on-change="onGaugeChange"/>
                    </span>
                </i-input>
                </Col>
            </Row>
            <Row class="ivurow">
                <p>指针显示&nbsp;&nbsp;</p>
                <i-switch size="small" style="margin-left: 116px;" v-model="gaugeOption.pointer_show" @on-change="onGaugeChange"/>
            </Row>
            <Row class="ivurow" v-if="typeof gaugeOption.itemStyle_color !== 'undefined'">
                <p>指针颜色&nbsp;&nbsp;</p>
                <Col>
                <i-input v-model="gaugeOption.itemStyle_color" size="small" style="width:143px;" @on-change="onGaugeChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="gaugeOption.itemStyle_color" :editable="false" alpha  :transfer="true" size="small" @on-change="onGaugeChange"/>
                    </span>
                </i-input>
                </Col>
            </Row>
            <Row class="ivurow">
                <p>指针字体大小&nbsp;&nbsp;</p>
                <i-input size="small" type="number" v-model="gaugeOption.detail_textStyle_fontSize" @on-blur="onGaugeChange" style="width: 119px;"></i-input>
            </Row>
            <Row class="ivurow" v-if="typeof gaugeOption.detail_textStyle_color !== 'undefined'">
                <p>指针字体颜色&nbsp;&nbsp;</p>
                <Col>
                <i-input v-model="gaugeOption.detail_textStyle_color" size="small" style="width: 119px;" @on-change="onGaugeChange">
                           <span slot="append">
                             <color-picker class="colorPicker" v-model="gaugeOption.detail_textStyle_color" :editable="false" alpha  :transfer="true" size="small" @on-change="onGaugeChange"/>
                           </span>
                </i-input>
                </Col>
            </Row>
        </div>
    </Submenu>
    <Submenu name="21" style="border-bottom: inset 1px;" class="rightFontSize">
        <template slot="title">
            <span>仪表盘刻度设置</span>
        </template>
        <div class="blockDiv" style="padding-bottom: 10px">
            <Row class="ivurow">
                <p>刻度值显示&nbsp;&nbsp;</p>
                <i-switch size="small" style="margin-left: 104px;" v-model="gaugeOption.axisLabel_show" @on-change="onGaugeChange"/>
            </Row>
            <Row class="ivurow">
                <p>刻度值字体大小&nbsp;&nbsp;</p>
                <i-input size="small" type="number" v-model="gaugeOption.axisLabel_textStyle_fontSize" @on-blur="onGaugeChange" style="width: 108px"></i-input>
            </Row>
            <Row class="ivurow">
                <p>仪表盘半径(%)&nbsp;&nbsp;</p>
                <slider :tip-format="util.percentFormat" :value="util.getNumberFromPercent(gaugeOption.radius)" @on-change="(value)=>onPercentChange(value, 'gaugeOption.radius','onGaugeChange')" style="margin-top: -9px;width: 109px;margin-left: 5px;"></slider>
            </Row>
            <Row class="ivurow">
                <p>轴线宽度&nbsp;&nbsp;</p>
                <slider max="50" step="5" v-model="gaugeOption.axisLine_lineStyle_width" @on-change="onGaugeChange" style="margin-top: -9px;width: 139px;margin-left: 5px;"></slider>
            </Row>

            <Row class="ivurow">
                <p>分割线长度&nbsp;&nbsp;</p>
                <slider max="50" step="2" v-model="gaugeOption.splitLine_length" @on-change="onGaugeChange" style="margin-top: -9px;width: 128px;margin-left: 5px;"></slider>
            </Row>
            <Row class="ivurow">
                <p>刻度线长度&nbsp;&nbsp;</p>
                <slider max="50" step="2" v-model="gaugeOption.axisTick_length" @on-change="onGaugeChange" style="margin-top: -9px;width: 128px;margin-left: 5px;"></slider>
            </Row>
        </div>
    </Submenu>
</div>
</script>
<script>
    Vue.component('j-gauge-setting', {
        template: '#gauge-setting-template',
        props: {
            settings: {
                type: Object,
                required: true,
                default: () => {
                }
            }
        },
        data(){
            return {
                gaugeOption: {
                    title_textStyle_color:'',
                    detail_textStyle_color:'',
                    itemStyle_color:''
                }
            }
        },
        watch: {
            settings: {
                deep: true,
                immediate: true,
                handler: function (){
                    this.initData()
                }
            }
        },
        methods: {
            initData: function (){
                if (this.settings){
                    this.gaugeOption = Object.assign(this.gaugeOption, this.settings)
                }
            },
            //slider 百分数改变事件
            onPercentChange(value, key, eventName,suffix=''){
                let arr = key.split('.')
                if(arr.length>1){
                    let temp = this
                    for(let i=0;i<arr.length-1;i++){
                        temp = temp[arr[i]]
                    }
                    temp[arr[arr.length-1]] = value+'%'+suffix
                }else{
                    this[key] = value+'%'+suffix
                }
                //this[eventName]();
                this.$emit('change', this.gaugeOption)
            },
            onGaugeChange(){
                this.$emit('change',  this.gaugeOption)
            }
        }
    })
</script>