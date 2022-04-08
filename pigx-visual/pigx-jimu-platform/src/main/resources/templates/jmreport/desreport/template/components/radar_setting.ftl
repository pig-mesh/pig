<script type="text/x-template" id="radar-setting-template">
    <Submenu  name="10" style="border-bottom: inset 1px;" class="rightFontSize">
        <template slot="title">
            <span>雷达图设置</span>
        </template>
        <div class="blockDiv" >
            <Row class="ivurow" v-for="(item,index) in radarOption[0].indicator" :key="index">
                <Tooltip :content="item.name" placement="bottom">
                    <p>{{item.name.substr(0,2)}}..最大值&nbsp;&nbsp;</p>
                </Tooltip>
                <i-input size="small" type="number" v-model="item.max" @on-blur="onRadarChange" style="width: 126px;"></i-input>
            </Row>
            <Row class="ivurow">
                <p>字体大小&nbsp;&nbsp;</p>
                <i-input size="small" type="number" v-model="radarOption[0].name_textStyle_fontSize" @on-blur="onRadarChange" class="iSelect"></i-input>
            </Row>
            <Row class="ivurow" v-if="typeof radarOption[0].name_textStyle_color!== 'undefined'">
                <p>字体颜色&nbsp;&nbsp;</p>
                <i-input v-model="radarOption[0].name_textStyle_color" size="small" class="iSelect" @on-change="onRadarChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="radarOption[0].name_textStyle_color" :editable="false"  :transfer="true" size="small" @on-change="onRadarChange"/>
                    </span>
                </i-input>
            </Row>

            <Row class="ivurow" v-if="typeof radarOption[0].axisLine_lineStyle_color!== 'undefined'">
                <p>轴线颜色&nbsp;&nbsp;</p>
                <i-input v-model="radarOption[0].axisLine_lineStyle_color" size="small" class="iSelect" @on-change="onRadarChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="radarOption[0].axisLine_lineStyle_color" :editable="false"  :transfer="true" size="small" @on-change="onRadarChange"/>
                    </span>
                </i-input>
            </Row>
        </div>
    </Submenu>
</script>
<script>
    Vue.component('j-radar-setting', {
        template: '#radar-setting-template',
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
                radarOption: []
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
                    this.radarOption = Object.assign(this.radarOption, this.settings)
                }
            },
            onRadarChange(){
                this.$emit('change','radar',this.radarOption)
            }
        }
    })
</script>