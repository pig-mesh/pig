<style>
    .jm-form-barcode .ivu-form-item-label{
        padding-bottom: 2px !important;
    }
    .jm-form-horizontal .ivu-form-item-content{
        margin-left: 100px;
    }
    .jm-form-horizontal .ivu-form-item-label{
        text-align: left;
        width: 100px;
        float: left;
        padding: 10px 12px 10px 0;
    }
    .jm-form-barcode .ivu-switch:after{
        height: 16px;
    }
    .jm-form-barcode .ivu-switch{
        height: 20px;
        line-height: 20px;
    }
    .little-input textarea{
        height: inherit !important;
    }

    .jm-form-barcode .ivu-color-picker .ivu-select-dropdown {
        left: -142px !important;
    }

    .jm-setting-container{
        overflow-y: auto;
        padding-right: 5px;
        overflow-x:hidden;
        padding-bottom: 50px;
    }
    .pb120{
        padding-bottom: 120px;
    }
    .jm-setting-container::-webkit-scrollbar {
        width: 6px;
    }
    .jm-setting-container::-webkit-scrollbar-thumb {
        background-color: #d9d9d9;
    }
</style>
<#--JsBarcode("#imgcode", "123", {
format: "CODE39",//选择要使用的条形码类型
width:3,//设置条之间的宽度
height:100,//高度
displayValue:true,//是否在条形码下方显示文字
text:"456",//覆盖显示的文本
fontOptions:"bold italic",//使文字加粗体或变斜体
font:"fantasy",//设置文本的字体
textAlign:"left",//设置文本的水平对齐方式
textPosition:"top",//设置文本的垂直位置
textMargin:5,//设置条形码和文本之间的间距
fontSize:15,//设置文本的大小
background:"#eee",//设置条形码的背景
lineColor:"#2196f3",//设置条和文本的颜色。
margin:15//设置条形码周围的空白边距-->
<script type="text/x-template" id="barcode-setting-template">
    <div :style="{height: settingsDivHeight+'px'}" class="jm-setting-container pb120" style="padding-top: 10px;padding-left: 7px">
        <i-form class="jm-form-barcode dataSourceForm rightFontSize"  >
            <form-item label="条形码内容">
                <i-input v-model="barcodeContent" id="barcodeContent" type="textarea" :rows="2" @on-blur="onBarcodeChange"></i-input>
            </form-item>

            <form-item label="条间距">
                <i-input v-model="width" placeholder="请输入条间距" :min="1" type="number" @on-change="onBarcodeChange"></i-input>
            </form-item>

            <form-item label="高度">
                <i-input v-model="height" placeholder="请输入高度" :min="1" type="number" @on-change="onBarcodeChange"></i-input>
            </form-item>

            <form-item label="条颜色" class="colorHeight">
                <i-input v-model="lineColor" size="small" style="width: 111px;" @on-change="onBarcodeChange">
                    <span slot="append">
                        <color-picker class="colorPicker colorHeight" v-model="lineColor" :editable="false" alpha  :transfer="true" size="small" @on-change="onBarcodeChange"/>
                    </span>
                </i-input>
            </form-item>

            <form-item label="背景色" class="colorHeight">
                <i-input v-model="background" size="small" style="width: 111px;" @on-change="onBarcodeChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="background" :editable="false" alpha  :transfer="true" size="small" @on-change="onBarcodeChange"/>
                    </span>
                </i-input>
            </form-item>

            <form-item label="是否显示文字" class="jm-form-horizontal">
                <div>
                    <i-switch v-model="displayValue" @on-change="onBarcodeChange"/>
                </div>
            </form-item>

            <template v-if="displayValue">
                <form-item label="覆盖文字">
                    <i-input v-model="text" placeholder="请输入覆盖文字" @on-blur="onBarcodeChange"></i-input>
                </form-item>

                <form-item label="文字位置">
                    <i-select v-model="textPosition" placeholder="请选择文字位置" @on-change="onBarcodeChange">
                        <i-option value="bottom">底部</i-option>
                        <i-option value="top">上方</i-option>
                    </i-select>
                </form-item>

                <form-item label="文字水平对齐">
                    <i-select v-model="textAlign" placeholder="请选择水平对齐方式" @on-change="onBarcodeChange">
                        <i-option value="center">居中</i-option>
                        <i-option value="left">左对齐</i-option>
                        <i-option value="right">右对齐</i-option>
                    </i-select>
                </form-item>

                <form-item label="文字大小">
                    <i-input v-model="fontSize" placeholder="请输入文字大小" :min="1" type="number" @on-change="onBarcodeChange"></i-input>
                </form-item>

                <form-item label="文字样式">
                    <i-select v-model="fontOptions" placeholder="请选择文字样式" @on-change="onBarcodeChange">
                        <i-option value="bold">加粗</i-option>
                        <i-option value="italic">斜体</i-option>
                        <i-option value="bold italic">加粗&斜体</i-option>
                    </i-select>
                </form-item>
            </template>
        </i-form>
    </div>
</script>
<script>
    Vue.component('j-barcode-setting', {
        template: '#barcode-setting-template',
        props: {
            settings: {
                type: Object,
                required: true,
                default:()=>{}
            }
        },
        data(){
            return {
                barcodeContent: '',
                background: '#fff',
                lineColor: '#000',
                width:'',
                height:'',
                displayValue: false,
                text: '',
                textPosition: 'bottom',
                textAlign: 'center',
                fontSize: '',
                fontOptions:'',
                settingsDivHeight: ''
            }
        },
        created(){
            this.settingsDivHeight = window.innerHeight - 100
        },
        watch:{
            settings:{
                deep: true,
                immediate: true,
                handler: function(){
                    this.resetForm()
                }
            }
        },
        methods:{
            validateBarcode:(value) => {
                if(!value){
                    return false;
                }
                if(value.indexOf('$')>=0 || value.indexOf('#')>=0){
                    return true
                }
                let reg = /^[a-zA-Z0-9]+$/
                let reg2 = /^.{4,18}$/
                // 长度为4到18个字符
                if (value !== '' && !reg.test(value)) {
                    Vue.prototype.$Message.error('条码内容只允许字母、数字')
                    return  false;
                }
                return true;
            },
            resetForm: function () {
                if(this.settings){
                    Object.keys(this.settings).map(k=>{
                        this[k] = this.settings[k]
                    })
                }
            },
            onBarcodeChange(){
                if(this.validateBarcode(this.barcodeContent)){
                    let obj = {}
                    Object.keys(this.settings).map(k=>{
                        obj[k] = this[k]
                    })
                    //清除颜色后恢复默认
                    obj.background=this.background?obj.background:'#ffffff'
                    obj.lineColor=this.lineColor?obj.lineColor:'#000000'
                    this.$emit('change', obj)
                }
            }
        }
    })
</script>