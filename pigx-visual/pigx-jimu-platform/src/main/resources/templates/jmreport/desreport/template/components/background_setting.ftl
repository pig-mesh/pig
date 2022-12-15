<script type="text/x-template" id="background-setting-template">
    <Submenu name="background" style="border-bottom: inset 1px;" class="rightFontSize">
        <template slot="title">
            <span>背景设置</span>
            <Tooltip placement="top" content="背景图片会覆盖背景颜色" :transfer="true">
                <Icon size="16" style="margin-left: 10px;" type="ios-help-circle-outline"/>
            </Tooltip>
        </template>

        <div class="blockDiv" style="padding-bottom: 20px">
            <Row class="ivurow">
                <p>启用背景&nbsp;&nbsp;</p>
                <i-switch size="small" style="margin-left: 117px;" v-model="chartBackground.enabled" @on-change="chartBackgroundChange"/>
            </Row>
            <Row class="ivurow">
                <p>背景颜色&nbsp;&nbsp;</p>
                <Col>
                <i-input class="iSelect" v-model="chartBackground.color" size="small" @on-change="chartBackgroundChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="chartBackground.color" :editable="false" alpha  :transfer="true" size="small" @on-change="chartBackgroundChange"/>
                    </span>
                </i-input>
                </Col>
            </Row>
            <Row class="ivurow">
                <p>背景图片&nbsp;&nbsp;</p>
                <div style="height: 0">
                    <Upload
                            :headers = "uploadHeaders"  
                            :show-upload-list="false"
                            :default-file-list="chartBackgroundImg"
                            :on-success="uploadSuccess"
                            :on-exceeded-size="(e)=>handleMaxSize(e,10)"
                            :format="['jpg','jpeg','png']"
                            :max-size="10240"
                            :action=" actionUrlPre + '/jmreport/upload' "
                            style="display: inline-block;width:58px;">
                        <i-button v-if="chartBackground.image" style="margin-left:106px" type="primary" size="small">修改</i-button>
                        <i-button v-else style="margin-left:106px" type="primary" size="small">上传</i-button>
                    </Upload>
                </div>
            </Row>
            <Row class="ivurow">
                <div style="width: 196px" class="pictorial-icon-upload" v-if="chartBackground.image">
                    <img style="max-width: 196px;max-height: 50px" :src="getChartBackgroundImg()"/>
                    <div class="cover">
                        <Icon type="ios-trash-outline" @click="removeChartBackground"/>
                    </div>
                </div>
            </Row>
        </div>
    </Submenu>
</script>
<script>
    Vue.component('j-background-setting', {
        template: '#background-setting-template',
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
                chartBackgroundImg: [{'name': 'chartBackgroundImg', 'url': ''}],
                actionUrlPre:  baseFull,
                chartBackground: {
                    color:''
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
                    this.chartBackground = Object.assign(this.chartBackground, this.settings)
                }
                this.uploadHeaders = vm.uploadHeaders
            },
            //获取图片预览图
            getChartBackgroundImg(){
                let path = this.chartBackground['image']
                if(path){
                    if(path.indexOf('http')<0){
                        path =  baseFull+path
                    }
                }
                return path
            },
            //图片上传文件大小
            handleMaxSize(file, size){
                // console.log("file===>", file)
                // console.log("size===>", size)
                this.$Notice.warning({
                    title: '超出文件大小限制',
                    desc: '文件  ' + file.name + ' 太大，请上传' + size + 'M以内图片',
                    duration: 6
                });
            },
            uploadSuccess(res){
                this.$emit('upload-success',res,this.chartBackground, (image)=>{
                    this.chartBackground['image'] = image
                    this.$forceUpdate(()=>{
                        this.getChartBackgroundImg();
                    })
                })
            },
            removeChartBackground(){
                this.chartBackground['image'] = ''
                this.$forceUpdate(()=>{
                    this.getChartBackgroundImg();
                })
                this.$emit('remove',this.chartBackground)
            },
            chartBackgroundChange(){
                this.$emit('change', this.chartBackground)
            }
        }
    })
</script>