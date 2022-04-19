<script type="text/x-template" id="series-setting-template">
    <Submenu  name="15" style="border-bottom: inset 1px;" class="dataSourceForm">
        <template slot="title">
            <span class="rightFontSize">数值设置</span>
        </template>
        <div class="blockDiv" style="padding-bottom: 10px">
            <Row class="ivurow">
                <p>显示&nbsp;&nbsp;</p>
                <i-switch size="small" style="margin-left: 141px;" v-model="seriesOption.show" @on-change="onSeriesLabelChange"/>
            </Row>
            <Row class="ivurow">
                <p>字体大小&nbsp;&nbsp;</p>
                <i-input class="rightFontSize iSelect" size="small" type="number" v-model="seriesOption.textStyle_fontSize" @on-blur="onSeriesLabelChange"></i-input>
            </Row>
            <Row class="ivurow" v-if="typeof seriesOption.textStyle_color !== 'undefined'">
                <p>字体颜色&nbsp;&nbsp;</p>
                <Col>
                <i-input class="rightFontSize iSelect" v-model="seriesOption.textStyle_color" size="small"  @on-change="onSeriesLabelChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="seriesOption.textStyle_color" :editable="false"  :transfer="true" size="small" @on-change="onSeriesLabelChange"/>
                    </span>
                </i-input>
                </Col>
            </Row>
            <Row class="ivurow rightFontSize">
                <p>字体粗细&nbsp;&nbsp;</p>
                <i-select size="small" class="iSelect" v-model="seriesOption.textStyle_fontWeight" @on-change="onSeriesLabelChange" style="width: 115%">
                    <i-option class="rightFontSize" value="normal">正常</i-option>
                    <i-option class="rightFontSize" value="bold">粗体</i-option>
                    <i-option class="rightFontSize" value="bolder">特粗</i-option>
                    <i-option class="rightFontSize" value="lighter">细体</i-option>
                </i-select>
            </Row>
            <Row class="ivurow rightFontSize">
                <p style="margin-bottom: 10px;">字体位置&nbsp;&nbsp;</p>
                <i-select size="small" class="iSelect" v-model="seriesOption.position" @on-change="onSeriesLabelChange">
                    <i-option class="rightFontSize" v-for="(item,index) in seriesOption.labelPositionArray" :value="item.value" :index="index">
                        {{ item.text }}
                    </i-option>
                </i-select>
            </Row>
		        <Row class="ivurow rightFontSize">
			        <p style="margin-bottom: 10px;">是否显示数值&nbsp;&nbsp;</p>
			        <i-switch size="small" style="margin-left: 6px;" v-model="izShowNumber" @on-change="onSeriesFormatterChange"/>
			        </i-select>
		        </Row>
        </div>
    </Submenu>
</script>
<script>
    Vue.component('j-series-setting', {
        template: '#series-setting-template',
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
                seriesOption: {
                    textStyle_color:'',
                    textStyle_fontSize:'',
                    textStyle_fontWeight:'',
                    position:'',
                    formatter:''
                },
                labelPositions:[],
                izShowNumber:false
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
                    this.seriesOption = Object.assign(this.seriesOption, this.settings)
                    //update-begin---author:wangshuai ---date:20220407  for：[issues/I50IKB]饼图 如何直接在图上显示各分类对应的数值------------
		                if(this.seriesOption.formatter && "{b}"!=this.seriesOption.formatter){
		                    this.izShowNumber = true;
		                }else{
                        this.izShowNumber = false;
		                }
                    //update-end---author:wangshuai ---date:20220407  for：[issues/I50IKB]饼图 如何直接在图上显示各分类对应的数值------------
                }
            },
            onSeriesLabelChange(){
                let {labelPositionArray,...otherOptions}=this.seriesOption;
                this.$emit('change', otherOptions,'label')
            },
            /**
             * 图表设置格式化
             */
            onSeriesFormatterChange(){
								if(this.izShowNumber){
                    this.seriesOption.formatter="{b}\n{c}"
								}else{
                    this.seriesOption.formatter="{b}"
								}
                let {labelPositionArray,...otherOptions}=this.seriesOption;
                this.$emit('change', otherOptions,'label')
            }
        }
    })
</script>