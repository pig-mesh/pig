<#--width: 150,
height: 150, // 高度
text: this.qrcode // 二维码内容
// render: 'canvas' // 设置渲染方式（有两种方式 table和canvas，默认是canvas）
// background: '#f0f'
// foreground: '#ff0'-->
<script type="text/x-template" id="qrcode-setting-template">
    <div style="height:auto;overflow: hidden;padding-bottom: 300px;padding-top: 10px;padding-left: 7px">
        <i-form class="jm-form-barcode dataSourceForm rightFontSize">
            <form-item label="二维码内容">
                <i-input v-model="text" type="textarea" :rows="2" @on-blur="onCodeChange" style="width: 99%"></i-input>
            </form-item>

            <form-item label="宽度">
                <i-input v-model="width" placeholder="请输入宽度" :min="1" type="number" @on-blur="onCodeChange" style="width: 99%"></i-input>
            </form-item>

            <form-item label="高度">
                <i-input v-model="height" placeholder="请输入高度" :min="1" type="number" @on-blur="onCodeChange" style="width: 99%"></i-input>
            </form-item>

            <form-item label="前景色" class="colorHeight">
                <i-input v-model="colorDark" size="small" style="width: 111px;" @on-change="onCodeChange">
                    <span slot="append">
                        <color-picker class="colorPicker colorHeight" v-model="colorDark" :editable="false"  :transfer="true" size="small" @on-change="onCodeChange"/>
                    </span>
                </i-input>
            </form-item>

            <form-item label="背景色" class="colorHeight">
                <i-input v-model="colorLight" size="small" style="width: 111px;" @on-change="onCodeChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="colorLight" :editable="false"  :transfer="true" size="small" @on-change="onCodeChange"/>
                    </span>
                </i-input>
            </form-item>
        </i-form>
    </div>
</script>
<script>
    Vue.component('j-qrcode-setting', {
        template: '#qrcode-setting-template',
        props: {
            settings: {
                type: Object,
                required: true,
                default:()=>{}
            }
        },
        data(){
            return {
                text: '',
                width:'',
                height:'',
                colorLight: '#fff',
                colorDark: '#000'
            }
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
            resetForm: function () {
                if(this.settings){
                    Object.keys(this.settings).map(k=>{
                        this[k] = this.settings[k]
                    })
                }
            },
            onCodeChange(){
                let obj = {}
                Object.keys(this.settings).map(k=>{
                    obj[k] = this[k]
                })
                //清除颜色后恢复默认
                obj.colorLight=this.colorLight?obj.colorLight:'#ffffff'
                obj.colorDark=this.colorDark?this.colorDark:'#000000'
                this.$emit('change', obj)
            }
        }
    })
</script>