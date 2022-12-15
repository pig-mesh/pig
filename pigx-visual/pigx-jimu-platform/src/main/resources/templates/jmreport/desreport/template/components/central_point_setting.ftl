<script type="text/x-template" id="central-point-setting-template">
    <div >
        <Submenu  name="5" style="border-bottom: inset 1px; " class="rightFontSize">
            <template slot="title">
                <span>中心点设置</span>
            </template>
            <div class="blockDiv" style="padding-bottom: 10px">
                <Row class="ivurow">
                    <p>x轴&nbsp;&nbsp;</p>
                    <i-input size="small" type="number" v-model="centralPoint.center[0]" @on-change="onMarginChange" style="margin-left: 5px;width: 164px" :value="centralPoint.center[0]"></i-input>
                </Row>
                <Row class="ivurow">
                    <p style="margin-bottom: 10px;">y轴&nbsp;&nbsp;</p>
                    <i-input size="small" type="number" v-model="centralPoint.center[1]" @on-change="onMarginChange" style="margin-left: 5px;width: 164px" :value="centralPoint.center[1]"></i-input>
                </Row>
            </div>
        </Submenu>
    </div>
</script>
<script>
    Vue.component('j-central-point-setting', {
        template: '#central-point-setting-template',
        props: {
            settings: {
                type: Object,
                required: true,
                default: () => {
                }
            },
        },
        data(){
            return {
                centralPoint: {

                }
            }
        },
        watch: {
            settings: {
                deep: true,
                immediate: true,
                handler: function (){
                    // console.log("我进来了")
                    this.initData()
                }
            }
        },
        methods: {
            initData: function (){
                // console.log(this.centralPoint,"我进来了")
                if (this.settings){
                    this.centralPoint = Object.assign(this.centralPoint, this.settings)
                }
            },
            onMarginChange (){
                this.$emit('change',this.centralPoint)
            }
        }
    })
</script>