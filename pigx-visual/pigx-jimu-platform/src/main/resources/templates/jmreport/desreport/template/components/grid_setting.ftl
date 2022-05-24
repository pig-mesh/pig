<script type="text/x-template" id="grid-setting-template">
    <div>
        <Submenu  name="18" style="border-bottom: inset 1px;" class="rightFontSize">
            <template slot="title">
                <span>坐标轴边距</span>
            </template>
            <div class="blockDiv">
                <Row class="ivurow">
                    <p>左边距&nbsp;&nbsp;</p>
                    <slider v-model="gridOptions.left" @on-change="onGridChange" style="margin-top: -9px;width: 145px;margin-left: 5px;"></slider>
                </Row>
                <Row class="ivurow">
                    <p>顶边距&nbsp;&nbsp;</p>
                    <slider v-model="gridOptions.top" @on-change="onGridChange" style="margin-top: -9px;width: 145px;margin-left: 5px;"></slider>
                </Row>
                <Row class="ivurow">
                    <p>右边距&nbsp;&nbsp;</p>
                    <slider v-model="gridOptions.right" @on-change="onGridChange" style="margin-top: -9px;width: 145px;margin-left: 5px;"></slider>
                </Row>
                <Row class="ivurow">
                    <p style="margin-bottom: 10px;">底边距&nbsp;&nbsp;</p>
                    <slider v-model="gridOptions.bottom" @on-change="onGridChange" style="margin-top: -9px;width: 145px;margin-left: 5px;"></slider>
                </Row>
            </div>
        </Submenu>
    </div>
</script>
<script>
    Vue.component('j-grid-setting', {
        template: '#grid-setting-template',
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
                gridOptions: {
                  
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
                    this.gridOptions = Object.assign(this.gridOptions, this.settings)
                }
            },
            onGridChange (){
                this.$emit('change','grid',this.gridOptions)
            }
        }
    })
</script>