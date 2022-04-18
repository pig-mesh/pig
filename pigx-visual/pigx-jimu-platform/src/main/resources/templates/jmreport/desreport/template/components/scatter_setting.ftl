<script type="text/x-template" id="scatter-setting-template">
    <div >
        <Submenu  name="9" style="border-bottom: inset 1px;" class="rightFontSize">
            <template slot="title">
                <span>散点设置</span>
            </template>
            <div class="blockDiv" style="padding-bottom: 10px">
                <Row class="ivurow">
                    <p>大小&nbsp;&nbsp;</p>
                    <slider v-model="scatterOption.symbolSize" @on-change="onScatterChange" style="margin-top: -9px;width: 160px;margin-left: 5px;"></slider>
                </Row>
                <Row class="ivurow" v-if="typeof scatterOption.itemStyle_color !== 'undefined'">
                    <p>颜色&nbsp;&nbsp;</p>
                    <Col>
                    <i-input v-model="scatterOption.itemStyle_color" size="small" style="width: 165px;" @on-change="onScatterChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="scatterOption.itemStyle_color" :editable="false" alpha  :transfer="true" size="small" @on-change="onScatterChange"/>
                    </span>
                    </i-input>
                    </Col>
                </Row>
                <Row class="ivurow" style="z-index:9999">
                    <p style="margin-bottom: 10px;">透明度&nbsp;&nbsp;</p>
                    <slider max="1" step="0.1" v-model="scatterOption.itemStyle_opacity" @on-change="onScatterChange" style="margin-top: -9px;width: 148px;margin-left: 5px;" ></slider>
                </Row>
            </div>
        </Submenu>
    </div>
</script>
<script>
    Vue.component('j-scatter-setting', {
        template: '#scatter-setting-template',
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
                scatterOption: {
                    itemStyle_color:"#C23531"
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
                    this.scatterOption = Object.assign(this.scatterOption, this.settings)
                }
            },
            onScatterChange(){
                this.$emit('change',this.scatterOption)
            }
        }
    })
</script>