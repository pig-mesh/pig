<script type="text/x-template" id="legend-setting-template">
    <div>
        <Submenu  name="legend" style="border-bottom: inset 1px;" class="rightFontSize dataSourceForm">
            <template slot="title">
                <span>图例设置</span>
            </template>
            <div class="blockDiv">
                <Row class="ivurow">
                    <p>显示&nbsp;&nbsp;</p>
                    <i-switch size="small" style="margin-left: 141px;" v-model="legendOptions.show" @on-change="onLegendChange"/>
                </Row>
                <Row class="ivurow">
                    <p>字体大小&nbsp;&nbsp;</p>
                    <i-input size="small" type="number" v-model="legendOptions.textStyle_fontSize" @on-blur="onLegendChange" class="iSelect"></i-input>
                </Row>
                <Row class="ivurow" v-if="typeof legendOptions.textStyle_color !== 'undefined'">
                    <p>字体颜色&nbsp;&nbsp;</p>
                    <Col>
                    <i-input v-model="legendOptions.textStyle_color" size="small" class="iSelect" @on-change="onLegendChange">
                           <span slot="append">
                             <color-picker class="colorPicker" v-model="legendOptions.textStyle_color" :editable="false" alpha  :transfer="true" size="small" @on-change="onLegendChange"/>
                           </span>
                    </i-input>
                    </Col>
                </Row>
                <#--<Row class="ivurow">
                    <p>图例宽度&nbsp;&nbsp;</p>
                    <i-input size="small" type="number" v-model="echartInfo.legendItemWidth" @on-blur="styleChanges" style="width: 111px;margin-bottom: 10px;"></i-input>
                </Row>-->
                <Row class="ivurow">
                    <p>纵向位置&nbsp;&nbsp;</p>
                    <i-select size="small" class="iSelect" v-model="legendOptions.top" @on-change="onLegendChange">
                        <i-option class="rightFontSize" value="top">顶部</i-option>
                        <i-option class="rightFontSize" value="bottom">底部</i-option>
                    </i-select>
                </Row>
                <Row class="ivurow">
                    <p style="margin-bottom: 10px;">横向位置&nbsp;&nbsp;</p>
                    <i-select size="small" class="iSelect" v-model="legendOptions.left" @on-change="onLegendChange" style="width: 125%">
                        <i-option class="rightFontSize" value="left">左对齐</i-option>
                        <i-option class="rightFontSize" value="center">居中</i-option>
                        <i-option class="rightFontSize" value="right">右对齐</i-option>
                    </i-select>
                </Row>
                <Row class="ivurow">
                    <p style="margin-bottom: 10px;">布局朝向&nbsp;&nbsp;</p>
                    <i-select size="small" v-model="legendOptions.orient" @on-change="onLegendChange" class="iSelect">
                        <i-option class="rightFontSize" value="horizontal">横排</i-option>
                        <i-option class="rightFontSize" value="vertical">竖排</i-option>
                    </i-select>
                </Row>

                <Row class="ivurow">
                    <p>上边距&nbsp;&nbsp;</p>
                    <slider :disabled="legendOptions.top!=='top'" v-model="paddingTop" @on-change="onLegendTopChange" :tip-format="formatTop" style="margin-top: -9px;width: 148px;margin-left: 5px;"></slider>
                </Row>
            </div>
        </Submenu>
    </div>
</script>
<script>
    Vue.component('j-legend-setting', {
        template: '#legend-setting-template',
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
                legendOptions: {
                    textStyle_color:'',
                    top:"top",
                    left:"left",
                    orient:"horizontal",
                    textStyle_fontSize:""
                },
                paddingTop:'' //边距单独提出来
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
                    this.legendOptions = Object.assign(this.legendOptions, this.settings)
                    //update-begin---author:wangshuai ---date:20220628  for：[issues/I58YJG]图表中图例设置-纵向位置如果设置为底部，在挪动上边距的话那个图例就会跑到顶部去------------
                    this.paddingTop = this.legendOptions.padding[0]
                    //update-end---author:wangshuai ---date:20220628  for：[issues/I58YJG]图表中图例设置-纵向位置如果设置为底部，在挪动上边距的话那个图例就会跑到顶部去--------------
                }
            },
            onLegendChange (){
                this.$emit('change','legend',this.legendOptions)
            },
            onLegendTopChange(){
                this.legendOptions.padding[0] = this.paddingTop;
                this.onLegendChange()
            },
            formatTop(val){
                return val
            },
        }
    })
</script>