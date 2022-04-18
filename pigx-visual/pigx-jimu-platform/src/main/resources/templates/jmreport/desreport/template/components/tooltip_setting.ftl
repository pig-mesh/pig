<script type="text/x-template" id="tooltip-setting-template">
    <div>
        <Submenu  name="16" style="border-bottom: inset 1px;" class="rightFontSize">
            <template slot="title">
                <span>提示语设置</span>
            </template>
            <div class="blockDiv">
                <Row class="ivurow">
                    <p>显示&nbsp;&nbsp;</p>
                    <i-switch size="small" style="margin-left: 141px;" v-model="tooltipOption.show" @on-change="onTooltipChange"/>
                </Row>
                <Row class="ivurow">
                    <p>字体大小&nbsp;&nbsp;</p>
                    <i-input class="iSelect" size="small" type="number" v-model="tooltipOption.textStyle_fontSize" @on-blur="onTooltipChange"></i-input>
                </Row>
                <Row class="ivurow" v-if="typeof tooltipOption.textStyle_color !== 'undefined'">
                    <p style="margin-bottom: 10px;">字体颜色&nbsp;&nbsp;</p>
                    <Col>
                    <i-input class="iSelect" v-model="tooltipOption.textStyle_color" size="small" @on-change="onTooltipChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="tooltipOption.textStyle_color" :editable="false"  :transfer="true" size="small" @on-change="onTooltipChange"/>
                    </span>
                    </i-input>
                    </Col>
                </Row>
                <Row class="ivurow" v-if="selectedChartType =='pie.simple' || selectedChartType =='pie.doughnut' || selectedChartType =='pie.rose'">
                  <span>百分比&nbsp;&nbsp;</span>
                  <Checkbox size="small" v-model="tooltipOption.percentage" true-value="1" false-value="0"	 style="margin-left: 14px" @on-change="percentageChange"></Checkbox>
                </Row>
            </div>
        </Submenu>
    </div>
</script>
<script>
    Vue.component('j-tooltip-setting', {
        template: '#tooltip-setting-template',
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
                tooltipOption: {
                    textStyle_color: '',
                    percentage:0
                },
                selectedChartType:"pie.simple"
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
                    this.selectedChartType = vm.selectedChartType
                    this.tooltipOption = Object.assign(this.tooltipOption, this.settings)
                }
            },
            onTooltipChange(){
                this.$emit('change', 'tooltip', this.tooltipOption)
            },
            //是否为百分比显示
            percentageChange(val){
              let tooltip = this.tooltipOption;
              let formatter = tooltip.formatter;
              if(val == 1){
                tooltip.formatter = formatter+ ' ({d}%)';
              }else{
                tooltip.formatter = formatter.substr(0,formatter.indexOf(" ({d}%)"))
              }
              this.$emit('change', 'tooltip', tooltip)
            }
        }
    })
</script>