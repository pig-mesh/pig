<script type="text/x-template" id="bar-series-template">
  <Submenu name="6" style="border-bottom: inset 1px;" class="rightFontSize dataSourceForm">
    <template slot="title">
      <span>类型设置</span>
    </template>
    <div class="blockDiv" style="padding-bottom: 10px">
      <Row class="ivurow" v-for="(item,index) in seriesData">
        <Tooltip :content="item.name" placement="top">
          <p class="text-width">
            {{item.name}}&nbsp;&nbsp;
          </p>
        </Tooltip>
        <i-select size="small" class="iSelect" v-model="item.type" @on-change="onSeriesChange">
          <i-option class="rightFontSize" value="bar">bar</i-option>
          <i-option class="rightFontSize" value="line">line</i-option>
        </i-select>
      </Row>
    </div>
  </Submenu>

</script>
<script>
    Vue.component('j-bar-series-setting', {
        template: '#bar-series-template',
        props: {
            chartOptions: {
                type: Object,
                default: () => {
                }
            },
            dataSettings: {
                type: [Object, String],
                default: () => {
                }
            }
        },
        data() {
            return {
                seriesData: {}
            }
        },
        watch: {
            chartOptions: {
                deep: true,
                immediate: true,
                handler: function () {
                    this.initData()
                }
            }
        },
        methods: {
            initData: function () {
                if (this.chartOptions && this.chartOptions.series) {
                    this.seriesData = this.chartOptions.series;
                }
            },
            onSeriesChange(value) {
                this.chartOptions.series = this.seriesData
                let id = this.dataSettings.id;
                xs.updateChart(id, this.chartOptions);
            }
        }
    })
</script>

<style scoped>
    .text-width {
        width: 50px;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        position: relative;
        top: 6px;
    }
</style>