<script type="text/x-template" id="title-setting-template">
    <div class="title-setting-fontsize">
        <Submenu name="1" style="border-bottom: inset 1px;">
            <template slot="title" ><span class="rightFontSize">标题设置</span></template>
            <div class="blockDiv">
                <Row class="ivurow">
                    <p>显示&nbsp;&nbsp;</p>
                    <i-switch size="small" style="margin-left: 142px;" v-model="titleOption.show" @on-change="onTitleChange"/>
                </Row>

                <Row class="ivurow">
                    <p>标题文字&nbsp;&nbsp;</p>
                    <i-input class="rightFontSize iSelect" size="small" v-model="titleOption.text" @on-blur="onTitleChange"></i-input>
                </Row>
                <Row class="ivurow" v-if="typeof titleOption.textStyle_color !== 'undefined'">
                    <p>字体颜色&nbsp;&nbsp;</p>
                    <Col>
                    <i-input v-model="titleOption.textStyle_color" size="small" class="rightFontSize iSelect" @on-change="onTitleChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="titleOption.textStyle_color" :editable="false"  :transfer="true" size="small" @on-change="onTitleChange"/>
                    </span>
                    </i-input>
                    </Col>
                </Row>
                <Row class="ivurow">
                    <p>字体加粗&nbsp;&nbsp;</p>
                    <#--:model.sync="echartInfo.titleFontWeight"-->
                    <i-select size="small" class="rightFontSize iSelect" v-model="titleOption.textStyle_fontWeight" @on-change="onTitleChange">
                        <i-option value="normal" class="rightFontSize">正常</i-option>
                        <i-option value="bold" class="rightFontSize">粗体</i-option>
                        <i-option value="bolder" class="rightFontSize">特粗</i-option>
                        <i-option value="lighter" class="rightFontSize">细体</i-option>
                    </i-select>
                </Row>
                <Row class="ivurow">
                    <p>字体大小&nbsp;&nbsp;</p>
                    <i-input size="small" type="number" v-model="titleOption.textStyle_fontSize" @on-change="onTitleChange"
                             class="iSelect rightFontSize"></i-input>
                </Row>
                <Row class="ivurow">
                    <p style="margin-bottom: 10px;">标题位置&nbsp;&nbsp;</p>
                    <i-select size="small" class="iSelect rightFontSize" v-model="titleOption.left" @on-change="onTitleChange" style="width: 132%;">
                        <i-option value="left" class="rightFontSize">左对齐</i-option>
                        <i-option value="center" class="rightFontSize">居中</i-option>
                        <i-option value="right" class="rightFontSize">右对齐</i-option>
                    </i-select>
                </Row>
                <Row class="ivurow">
                    <p>顶边距&nbsp;&nbsp;</p>
                    <slider v-model="titleOption.top" @on-change="onTitleChange" :tip-format="formatTop" style="margin-top: -9px;width: 150px;margin-left: 5px;"></slider>
                </Row>
            </div>
        </Submenu>
    </div>
</script>
<script>
    Vue.component('j-title-setting', {
        template: '#title-setting-template',
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
                titleOption: {
                    show: true,
                    top: 5,
                    text: '',
                    textStyle_color: '',
                    textStyle_fontWeight: '',
                    textStyle_fontSize: '',
                    left: ''
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
            formatTop(val){
                return val + 'px';
            },
            initData: function (){
                if (this.settings){
                    this.titleOption = Object.assign(this.titleOption, this.settings)
                }
            },
            onTitleChange(){
                this.$emit('change', 'title', this.titleOption)
            }
        }
    })
</script>