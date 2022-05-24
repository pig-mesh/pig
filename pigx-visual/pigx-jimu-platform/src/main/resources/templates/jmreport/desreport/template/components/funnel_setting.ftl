<script type="text/x-template" id="funnel-setting-template">
    <Submenu  name="6" style="border-bottom: inset 1px;" class="rightFontSize dataSourceForm">
        <template slot="title">
            <span>漏斗设置</span>
        </template>
        <div class="blockDiv" style="padding-bottom: 10px">
            <Row class="ivurow">
                <p>图形间距&nbsp;&nbsp;</p>
                <slider v-model="funnelOptions.gap" @on-change="onFunnelChange" style="margin-top: -9px;width: 139px;margin-left: 5px;"></slider>
            </Row>
            <Row class="ivurow">
                <p>数据排序&nbsp;&nbsp;</p>
                <i-select size="small" class="iSelect" v-model="funnelOptions.sort" @on-change="onFunnelChange">
                    <i-option class="rightFontSize" value="ascending">升序</i-option>
                    <i-option class="rightFontSize" value="descending">降序</i-option>
                    <i-option class="rightFontSize" value="none">无序</i-option>
                </i-select>
            </Row>

            <Row class="ivurow">
                <p>宽度(%)&nbsp;&nbsp;&nbsp;</p>
                <slider :tip-format="util.percentFormat" :value="util.getNumberFromPercent(funnelOptions.width)" @on-change="(value)=>onPercentChange(value, 'funnelOptions.width','onFunnelChange')" style="margin-top: -9px;width: 139px;margin-left: 5px;"></slider>
            </Row>
        </div>
    </Submenu>

</script>
<script>
    Vue.component('j-funnel-setting', {
        template: '#funnel-setting-template',
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
                funnelOptions: {
                    gap:2,
                    sort: "descending",
                    width: "80%"
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
                    this.funnelOptions = Object.assign(this.funnelOptions, this.settings)
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
                this.$emit('change', this.funnelOptions)
            },
            onFunnelChange(){
                this.$emit('change', this.funnelOptions)
            }
        }
    })
</script>