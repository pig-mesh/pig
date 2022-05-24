<script type="text/x-template" id="pie-setting-template">
        <Submenu  name="4" style="border-bottom: inset 1px;" class="rightFontSize">
            <template slot="title">
                <span>饼图设置</span>
            </template>
            <div class="blockDiv" style="padding-bottom: 10px">
                <Row class="ivurow">
                    <p>环形饼图&nbsp;&nbsp;</p>
                    <i-switch size="small" style="margin-left: 117px;" v-model="pieOption.isRadius" @on-change="pieTypeChange"/>
                </Row>
                <Row class="ivurow">
                    <p>南丁格尔玫瑰&nbsp;&nbsp;</p>
                    <i-switch size="small" style="margin-left: 93px;" v-model="pieOption.isRose" @on-change="pieTypeChange('1')"/>
                </Row>
                <template v-if="pieOption.isRadius">
                    <Row class="ivurow" >
                        <p>饼块半径&nbsp;&nbsp;</p>
                        <i-input size="small" v-model="pieOption.radius[1]" @on-blur="onPieChange" class="iSelect"></i-input>
                    </Row>
                    <Row class="ivurow">
                        <p>内环半径&nbsp;&nbsp;</p>
                        <i-input size="small" v-model="pieOption.radius[0]" @on-blur="onPieChange" class="iSelect"></i-input>
                    </Row>
                </template>
                <Row class="ivurow" v-else>
                    <p>饼块半径&nbsp;&nbsp;</p>
                    <i-input size="small" v-model="pieOption.radius" @on-blur="onPieChange" class="iSelect"></i-input>
                </Row>

                <Row class="ivurow">
                    <span>最小角度&nbsp;&nbsp;</span>
                    <i-input size="small" type="number" v-model="pieOption.minAngle" @on-blur="onPieChange" class="iSelect"></i-input>
                </Row>
            </div>
        </Submenu>
</script>
<script>
    Vue.component('j-pie-setting', {
        template: '#pie-setting-template',
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
                pieOption: {
                    "isRadius":false,
                    "isRose":true,
                    "radius": "55%",
                    "roseType": "radius",
                    "minAngle":  0
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
                    this.pieOption = Object.assign(this.pieOption, this.settings)
                }
            },
            pieTypeChange(type){
                if(type==='1'){
                    this.pieOption.roseType=this.pieOption.isRose?'radius':'';
                }else{
                    this.pieOption.radius=this.pieOption.isRadius?typeJudge(this.pieOption.radius,'Array')?this.pieOption.radius :["45%",this.pieOption.radius]:typeJudge(this.pieOption.radius,'Array')?this.pieOption.radius[1] : this.pieOption.radius;
                }
                this.onPieChange();

            },
            onPieChange(){
                this.$emit('change',this.pieOption)
            }
        }
    })
</script>