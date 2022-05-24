<script type="text/x-template" id="bar-setting-template">
    <div >
        <Submenu  name="2" style="border-bottom: inset 1px;">
            <template slot="title">
                    <span class="rightFontSize">柱体设置</span>
            </template>
            <div class="blockDiv">
                <Row class="ivurow">
                    <p>宽度&nbsp;&nbsp;</p>
                    <slider v-model="barOptions.barWidth" @on-change="onBarChange" style="margin-top: -9px;width: 140px;margin-left: 26px;"></slider>
                </Row>
                <Row class="ivurow">
                    <p>圆角&nbsp;&nbsp;</p>
                    <slider v-model="barOptions.itemStyle_barBorderRadius" @on-change="onBarChange" style="margin-top: -9px;width: 134px;margin-left: 31px;"></slider>
                </Row>
                <Row class="ivurow">
                    <p style="margin-bottom: 10px;">最小高度&nbsp;&nbsp;</p>
                    <slider v-model="barOptions.barMinHeight" @on-change="onBarChange" style="margin-top: -9px;width: 134px;margin-left: 8px;"></slider>
                </Row>
                <Row v-if="isMultiChart === false && typeof barOptions.itemStyle_color!== 'undefined' " class="ivurow">
                    <p>柱体颜色&nbsp;&nbsp;</p>
                    <i-input class="rightFontSize iSelect"  v-model="barOptions.itemStyle_color" size="small" @on-change="onBarChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="barOptions.itemStyle_color" :editable="false" alpha  :transfer="true" size="small" @on-change="onBarChange"/>
                    </span>
                    </i-input>
                </Row>
                <Row v-if="isMultiChart === false && typeof barOptions.backgroundStyle_color!== 'undefined' " class="ivurow">
                    <p>柱体背景&nbsp;&nbsp;</p>
                    <i-input class="rightFontSize iSelect" v-model="barOptions.backgroundStyle_color" size="small"  @on-change="onBarChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="barOptions.backgroundStyle_color" :editable="false" alpha  :transfer="true" size="small" @on-change="onBarChange"/>
                    </span>
                    </i-input>
                </Row>
            </div>
        </Submenu>
    </div>
</script>
<script>
    Vue.component('j-bar-setting', {
        template: '#bar-setting-template',
        props: {
            settings: {
                type: Object,
                required: true,
                default: () => {
                }
            },
            isMultiChart:{
                type: Boolean,
                default: false
            }
        },
        data(){
            return {
                barOptions: {
                    barWidth: 50,
                    itemStyle_barBorderRadius: 0,
                    itemStyle_color: '#c43632',
                    barMinHeight: 2,
                    textStyle_color: 'black',
                    textStyle_fontWeight: 'bolder'
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
                    this.barOptions = Object.assign(this.barOptions, this.settings)
                }
            },
            onBarChange (){
                this.$emit('change',this.barOptions)
            }
        }
    })
</script>