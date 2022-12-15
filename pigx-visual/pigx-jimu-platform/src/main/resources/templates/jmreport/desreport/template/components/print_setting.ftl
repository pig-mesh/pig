<script type="text/x-template" id="print-setting-template">
    <div>
        <Modal :loading="true" v-model="show" :width="500" @on-ok="onPrintSettingSave" @on-cancel="onPrintSettingCancel" class="print-setting expression">
            <p slot="header">
            <span>打印设置
               <Tooltip :transfer="true" content="打印文档" placement="top">
                  <a class="jimu-table-tip help-color" href="http://report.jeecg.com/1605005" target="_blank"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
               </Tooltip>
            </span>
            </p>
            <i-form label-colon :label-width="90">
                <Row>
                    <i-col span="6">
                        <span>打印纸张：</span>
                    </i-col>
                    <i-col span="18">
                        <i-select v-model="paper" @on-change="onPaperChange">
                            <i-option v-for="(item, index) in paperList" :index="index" :value="item.paper">
                                {{ getPaperText(item) }}
                            </i-option>
                        </i-select>
                    </i-col>
                </Row>

                <Row style="margin-top: 8px">
                    <i-col span="6">
                        <span>打印布局：</span>
                    </i-col>
                    <i-col span="18">
                        <RadioGroup v-model="layout" @on-change="onLayoutChange">
                            <Radio label="portrait" border>
                                <span>纵向</span>
                            </Radio>
                            <Radio label="landscape" border>
                                <span>横向</span>
                            </Radio>
                        </RadioGroup>
                    </i-col>
                </Row>

                <Row style="margin-top: 8px">
                    <i-col span="6">
                        <span>打印边距(mm)：</span>
                    </i-col>
                    <i-col span="18">
                        <i-input type="number" v-model="marginX" style="width: 40%" @on-change="onPaperMarginXChange"></i-input>
                        -
                        <i-input type="number" v-model="marginY" style="width: 40%" @on-change="onPaperMarginYChange"></i-input>
                    </i-col>
                </Row>


                <Row style="margin-top: 8px">
                    <i-col span="6">
                        <span>像素宽高(px)：</span>
                    </i-col>
                    <i-col span="18">
                        <i-input v-model="pxWidth" readonly style="width: 40%"></i-input>
                        -
                        <i-input v-model="pxHeight" readonly style="width: 40%"></i-input>
                    </i-col>
                </Row>

                <Row style="margin-top: 8px">
                    <i-col span="6">
                        <span>打印清晰度：</span>
                    </i-col>
                    <i-col span="18">
                        <slider :min="1" :max="10" v-model="definition"></slider>
                    </i-col>
                </Row>

                <Row style="margin-top: 8px">
                    <i-col span="6">
                        <span>回调接口：</span>
                    </i-col>
                    <i-col span="18">
                        <i-input v-model="printCallBackUrl" style="width: 100%"></i-input>
                    </i-col>
                </Row>
                
            </i-form>
        </Modal>
    </div>
</script>
<script>
    const papers = [
        {paper: 'A4', width: 210, height: 297},
        {paper: 'A3', width: 297, height: 420},
        {paper: 'Letter', width: 216, height: 279},
        {paper: 'Legal', width: 216, height: 355},
        {paper: 'Executive', width: 184, height: 266}
    ]
    Vue.component('j-print-setting', {
        template: '#print-setting-template',
        props: {
            show:{
                type: Boolean,
                required: false,
                default: false
            },
            config: {
                type: String,
                required: false,
                default:()=>{}
            },
            settings: {
                type: Object,
                required: true,
                default:()=>{}
            }
        },
        data(){
            return {
                paperList: [],
                //纸张标识
                paper: '',
                // 纸张宽高
                width: '',
                height: '',
                //清晰度
                definition: '',
                //是否套打
                isBackend: '',
                dpi:'',
                pxWidth: '',
                pxHeight: '',
                marginX: 10,
                marginY: 10,
                layout: 'portrait',
                printCallBackUrl: ''
            }
        },
        created(){
            this.initPaperList()
            this.getWindowDpi()
        },
        watch:{
            config:{
                immediate: true,
                handler: function(){
                    this.initPaperList()
                }
            },
            settings:{
                deep: true,
                immediate: true,
                handler: function(){
                    this.resetForm(this.settings)
                }
            }
        },
        methods:{
            onPrintSettingSave(){
               // console.log('this.getEmitParam()', this.getEmitParam())
                this.$emit('change', this.getEmitParam())
            },
            onPrintSettingCancel(){
                this.$emit('change', false)
            },
            // 获取回传的参数
            getEmitParam(){
              return {
                  paper: this.paper,
                  width: this.width,
                  height: this.height,
                  definition: this.definition,
                  isBackend: this.isBackend,
                  marginX: this.marginX || 0,
                  marginY: this.marginY || 0,
                  layout: this.layout,
                  printCallBackUrl: this.printCallBackUrl
              }
            },
            // 纸张大小改变事件
            onPaperChange(value){
                let arr = this.paperList.filter(item=>{
                    return item.paper === value
                })
                this.resetForm(arr[0])
            },
            onPaperMarginYChange(event){
                let temp = parseInt(event.target.value || 0)
                if(temp<0){
                    temp = 0
                }
                this.marginY = temp
                if(this.layout == 'portrait'){
                    this.pxHeight = this.getPxHeight(this.height)+'px'
                }else{
                    this.pxWidth = this.getPxHeight(this.height)+'px'
                }
            },
            onPaperMarginXChange(event){
                let temp = parseInt(event.target.value || 0)
                if(temp<0){
                    temp = 0
                }
                this.marginX = temp
                if(this.layout == 'portrait'){
                    this.pxWidth = this.getPxWidth(this.width)+'px'
                }else{
                    this.pxHeight = this.getPxWidth(this.width)+'px'
                }
            },
            getPaperText(item){
              return item.paper+'：'+item.width+'mm x '+item.height+'mm'
            },
            initPaperList(){
                let printPaper = []
                if(this.config){
                    let config2 = JSON.parse(this.config)
                    if(config2 && config2['printPaper']){
                        printPaper = config2['printPaper']
                    }
                }
                let arr = []
                for(let item of papers){
                    arr.push(item)
                }
                for(let item of printPaper){
                    arr.push({
                        paper: item.title, width: item['size'][0], height: item['size'][1]
                    })
                }
                this.paperList = [...arr]
            },
            resetForm: function (param) {
                if(param){
                    this.paper = param.paper
                    this.width = param.width
                    this.height = param.height
                    if(param.marginX || param.marginX==0){
                        this.marginX = param.marginX
                    }
                    if(param.marginY || param.marginY==0){
                        this.marginY = param.marginY
                    }
                    if(param.layout){
                        this.layout = param.layout
                    }
                    this.resetPrintPx();
                    if(param.definition){
                        this.definition = param.definition
                    }
                    if(param.isBackend===true || param.isBackend===false){
                        this.isBackend = param.isBackend
                    }
                    this.printCallBackUrl = param.printCallBackUrl || ''
                }
            },
            // 重置宽高px的值
            resetPrintPx(){
                let pxWidth = this.getPxWidth(this.width)+'px'
                let pxHeight = this.getPxHeight(this.height)+'px'
                if(this.layout == 'portrait'){
                    this.pxWidth = pxWidth
                    this.pxHeight = pxHeight
                }else{
                    this.pxWidth = pxHeight
                    this.pxHeight = pxWidth
                }
            },
            //获取窗口dpi
            getWindowDpi(){
                //25.41 1英寸=25.41mm 96是window默认dpi,mac:72
                let arrDPI = new Array();
                if ( window.screen.deviceXDPI != undefined ) {
                    arrDPI[0] = window.screen.deviceXDPI;
                    arrDPI[1] = window.screen.deviceYDPI;
                }
                else {
                    let tmpNode = document.createElement( "DIV" );
                    tmpNode.style.cssText = "width:1in;height:1in;position:absolute;left:0px;top:0px;z-index:99;visibility:hidden";
                    document.body.appendChild( tmpNode );
                    arrDPI[0] = parseInt( tmpNode.offsetWidth );
                    arrDPI[1] = parseInt( tmpNode.offsetHeight );
                    tmpNode.parentNode.removeChild( tmpNode );
                }
                this.dpi = [...arrDPI]
            },
            //获取像素宽
            getPxWidth(width){
                let margin = this.marginX
                if(this.isBackend==true){
                    margin = 0
                }
                return Math.ceil((width - margin*2)/25.41 * this.dpi[0]);
            },
            //获取像素高
            getPxHeight(height){
                let margin = this.marginY
                if(this.isBackend==true){
                    margin = 0
                }
                return Math.ceil((height-margin*2)/25.41 * this.dpi[1]);
            },
            // 布局改变
            onLayoutChange(value){
                this.layout = value
                this.resetPrintPx();
            }


        }
    })
</script>
<style>
    .print-setting .ivu-col-span-6>span{
        display: inline-block;
        height: 32px;
        line-height: 32px;
    }
</style>