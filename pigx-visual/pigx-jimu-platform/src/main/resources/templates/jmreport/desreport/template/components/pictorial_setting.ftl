<script type="text/x-template" id="pictorial-setting-template">
    <Submenu  name="8" style="border-bottom: inset 1px;" class="rightFontSize">
        <template slot="title">
            <span>象形图设置</span>
        </template>
        <div class="blockDiv" style="padding-bottom: 10px">
            <Row class="ivurow" >
               <span title="建议上传至服务器本地,若上传至云服务器,可能导出/打印等功能不可用" style="display: inline-block;line-height:0px;height: 20px;width: 53px;vertical-align:middle">图标</span>
                <Upload
                        :headers = "uploadHeaders"
                        ref="upload"
                        :show-upload-list="false"
                        :default-file-list="pictorialIcon"
                        :on-exceeded-size="(e)=>handleMaxSize(e,2)"
                        :on-success="uploadSuccess"
                        :format="['jpg','jpeg','png']"
                        :max-size="2048"
                        :data="localUpload"
                        :action=" actionUrlPre + '/jmreport/upload' "
                        style="display: inline-block;width:58px;">
                    <div class="pictorial-icon-upload">
                        <img style="width: 36px;height:36px" ref="symbol" :src="getPathBySymbol()"/>
                        <div class="cover">
                            <Icon type="ios-create-outline"/>
                        </div>
                    </div>
                </Upload>
            </Row>
            <Row class="ivurow" >
                <p>图标大小&nbsp;&nbsp;</p>
                <i-input size="small" v-model="pictorialOptions.symbolSize" @on-blur="onPictorialChange" class="iSelect"></i-input>
            </Row>
            <Row class="ivurow">
                <p>图标间距&nbsp;&nbsp;</p>
                <i-input  type="number" size="small" :value="util.getNumberFromPercent(pictorialOptions.symbolMargin,'!')" @on-blur="(e)=>onPercentChange(e.target.value, 'pictorialOptions.symbolMargin','onPictorialChange','!')" class="iSelect"> <span slot="append">%</span></i-input>
            </Row>

            <Row class="ivurow" >
                <p>最大值&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</p>
                <i-input size="small" v-model="pictorialOptions.symbolBoundingData" @on-blur="onPictorialChange" class="iSelect"></i-input>
            </Row>

            <Row class="ivurow">
                <p>是否补全&nbsp;&nbsp;</p>
                <i-switch size="small" style="margin-left: 119px;" v-model="pictorialOptions.double" @on-change="onPictorialChange"/>
            </Row>
            <Row class="ivurow" v-if="pictorialOptions.double">
                <p>透明度&nbsp;&nbsp;</p>
                <slider max="1" step="0.1" v-model="pictorialOptions.secondOpacity" @on-change="onPictorialChange" style="margin-top: -9px;width: 110px;margin-left: 5px;" ></slider>
            </Row>
        </div>
    </Submenu>
</script>
<script>
    Vue.component('j-pictorial-setting', {
        template: '#pictorial-setting-template',
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
                //象形图 图标配置
                pictorialIcon:[{'name': 'pictorialIcon','url': '' }],
                actionUrlPre: baseFull,
                pictorialOptions: {

                },
                localUpload:{
                    bizType: 'local'
                },
               uploadHeaders:{}
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
                    this.pictorialOptions = Object.assign(this.pictorialOptions, this.settings)
                }
                this.uploadHeaders = vm.uploadHeaders
            },
            getPathBySymbol(){
                let symbol = this.pictorialOptions['symbol']
                if(!symbol){
                    let path =  baseFull+'/jmreport/desreport_/chartsImg/pictorialIcon/spirits.png'
                    return path;
                }else{
                    return symbol.replace('image://','')
                }
            },
            //图片上传文件大小
            handleMaxSize (file,size) {
                // console.log("file===>",file)
                // console.log("size===>",size)
                this.$Notice.warning({
                    title: '超出文件大小限制',
                    desc: '文件  ' + file.name + ' 太大，请上传'+size+'M以内图片',
                    duration: 6
                });
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
                this.onPictorialChange();
            },
            uploadSuccess(res){
                this.$emit('upload-success',res, (symbol)=>{
                    this.pictorialOptions['symbol'] = symbol
                    this.$forceUpdate(()=>{
                        this.getPathBySymbol();
                    })
                })
            },
            onPictorialChange(){
                this.$emit('change', this.pictorialOptions)
            }
        }
    })
</script>