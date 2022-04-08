<script type="text/x-template" id="line-setting-template">
    <Submenu  name="3" style="border-bottom: inset 1px;" class="rightFontSize">
        <template slot="title">
            折线设置
        </template>
        <div class="blockDiv">
            <Row class="ivurow">
                <p>平滑曲线&nbsp;&nbsp;</p>
                <i-switch size="small" style="margin-left: 116px;" v-model="lineOptions.smooth" @on-change="onLineChange"/>
            </Row>
            <Row class="ivurow">
                <p>标记点&nbsp;&nbsp;</p>
                <i-switch size="small" style="margin-left: 128px;" v-model="lineOptions.showSymbol" @on-change="onLineChange"/>
            </Row>
            <Row class="ivurow">
                <p>点大小&nbsp;&nbsp;</p>
                <slider v-model="lineOptions.symbolSize" @on-change="onLineChange" style="margin-top: -9px;width: 144px;margin-left: 12px;"></slider>
            </Row>
            <Row class="ivurow">
                <p>阶梯线图&nbsp;&nbsp;</p>
                <i-switch size="small" style="margin-left: 116px;" v-model="lineOptions.step" @on-change="onLineChange"/>
            </Row>
            <Row class="ivurow">
                <p style="margin-bottom: 10px;">线条宽度&nbsp;&nbsp;</p>
                <slider max="5" v-model="lineOptions.lineStyle_width" @on-change="onLineChange" style="margin-top: -9px;width: 134px;margin-left: 9px;"></slider>
            </Row>
            <Row v-if=" isMultiChart === false && typeof lineOptions.itemStyle_color!== 'undefined' " class="ivurow">
                <p style="margin-bottom: 10px;">线条颜色&nbsp;&nbsp;</p>
                <Col>
                <i-input v-model="lineOptions.itemStyle_color" size="small" class="iSelect" @on-change="onLineChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="lineOptions.itemStyle_color" :editable="false" alpha  :transfer="true" size="small" @on-change="onLineChange"/>
                    </span>
                </i-input>
                </Col>
            </Row>
            <Row class="ivurow" v-if="isMultiChart === false">
                <p>面积折线&nbsp;&nbsp;</p>
                <i-switch size="small" style="margin-left: 116px;" v-model="lineOptions.isArea" @on-change="onLineChange"/>
            </Row>
            <Row v-if="lineOptions.isArea" class="ivurow">
                <p style="margin-bottom: 10px;">面积颜色&nbsp;&nbsp;</p>
                <Col>
                <i-input v-model="lineOptions.areaStyle_color" size="small" class="iSelect" @on-change="onLineChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="lineOptions.areaStyle_color" :editable="false" alpha  :transfer="true" size="small" @on-change="onLineChange"/>
                    </span>
                </i-input>
                </Col>
            </Row>
            <Row v-if="lineOptions.isArea" class="ivurow" style="z-index:9999">
                <p style="margin-bottom: 10px;">面积透明度&nbsp;&nbsp;</p>
                <slider max="1" step="0.1" v-model="lineOptions.areaStyle_opacity" @on-change="onLineChange" style="margin-top: -9px;width: 114px;margin-left: 12px;"></slider>
            </Row>
        </div>
    </Submenu>
</script>
<script>
    Vue.component('j-line-setting', {
        template: '#line-setting-template',
        props: {
            settings: {
                type: Object,
                required: true,
                default: () => {
                }
            },
            isMultiChart: {
                type: Boolean,
                default: false
            }
        },
        data(){
            return {
                lineOptions: {
                    smooth: false,
                    showSymbol: true,
                    symbolSize: 5,
                    step: false,
                    lineStyle_width: 2,
                    itemStyle_color: '#c43632',
                    isArea:false,
                    areaStyle_color:'rgba(220,38,38,0)',
                    areaStyle_opacity:1
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
                    this.lineOptions = Object.assign(this.lineOptions, this.settings)
                }
            },
            onLineChange(){
                this.$emit('change', this.lineOptions)
            }
        }
    })
</script>