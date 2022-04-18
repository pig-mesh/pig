<script type="text/x-template" id="margin-setting-template">
    <div >
        <Submenu  name="5" style="border-bottom: inset 1px;" class="rightFontSize">
            <template slot="title">
                <span>边距设置</span>
            </template>
            <div class="blockDiv" style="padding-bottom: 10px">
                <Row class="ivurow">
                    <p>左边距(%)</p>
                    <slider :tip-format="percentFormat" :value="getNumberFromPercent(marginOptions.left)" @on-change="(value)=>onPercentChange(value, 'marginOptions.left','onPieChange')" style="margin-top: -9px;width: 135px;margin-left: 5px;"></slider>
                </Row>

                <Row class="ivurow" v-if="marginOptions.right">
                    <p>右边距(%)</p>
                    <slider :tip-format="percentFormat" :value="getNumberFromPercent(marginOptions.right)" @on-change="(value)=>onPercentChange(value, 'marginOptions.right','onPieChange')" style="margin-top: -9px;width: 135px;margin-left: 5px;"></slider>
                </Row>
                <Row class="ivurow">
                    <p>顶边距&nbsp;&nbsp;</p>
                    <slider v-model="marginOptions.top" @on-change="onMarginChange" style="margin-top: -9px;width: 148px;margin-left: 5px;"></slider>
                </Row>
                <Row class="ivurow">
                    <p style="margin-bottom: 10px;">底边距&nbsp;&nbsp;</p>
                    <slider v-model="marginOptions.bottom" @on-change="onMarginChange" style="margin-top: -9px;width: 148px;margin-left: 5px;"></slider>
                </Row>
            </div>
        </Submenu>
    </div>
</script>
<script>
    Vue.component('j-margin-setting', {
        template: '#margin-setting-template',
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
                marginOptions: {

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
            percentFormat (val) {
                return val + '%';
            },
            //通过百分数转化 获取实际数字
            getNumberFromPercent(p,suffix=''){
                if(!p){
                    return 0;
                }
                return Number((p+'').replace('%','').replace(suffix,''))
            },
            initData: function (){
                if (this.settings){
                    this.marginOptions = Object.assign(this.marginOptions, this.settings)
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
                this.$emit('change',this.marginOptions)
            },
            onMarginChange (){
                this.$emit('change',this.marginOptions)
            }
        }
    })
</script>