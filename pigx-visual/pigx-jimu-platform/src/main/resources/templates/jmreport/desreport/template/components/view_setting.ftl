<style>
    .view-toolbar-container .ivu-icon{
        width:22px;
        height:22px;
        background: #fff;
        margin-left: 5px;
    }

    .view-toolbar-container .ivu-icon:hover{
        background: #eee;
    }
    .view-toolbar-list .ivu-col{
        border:1px solid #ddd;
        padding:2px;
    }

    .view-toolbar-list .ivu-col:active{
        background: #eee;
    }
    .view-toolbar-container .transform-icon, .view-toolbar-list .transform-icon{
        transform: rotate(180deg);
        padding-top: 1px;
    }

</style>
<script type="text/x-template" id="view-setting-template">
    <div style="">
        <Modal :loading="true" v-model="show" title="预览页工具条设置" :width="650" @on-ok="onViewSettingSave" @on-cancel="onViewSettingCancel" class="expression">
            <div style="height: 30px">
                <Checkbox v-model="showViewToolbar">显示工具栏：</Checkbox>
                <div style="height: 30px;float: right;margin-right: 5px">
                    <Button @click="resetViewToolbar" size="small">恢复默认</Button>
                </div>
            </div>
            <div style="padding: 9px;margin-top:2px;background: rgb(228 229 230);border-bottom: 1px solid #e0e2e4;height: 40px;text-align: left;">
                <div v-if="showViewToolbar" style="height: 22px;line-height: 22px;display: inline-block" class="view-toolbar-container">
                    <Icon
                      v-for="(item,index) in btnList"
                      v-if="isSelected(item.index)"
                      :key="index"
                      :type="item.icon"
                      :size="viewSettingFont"
                      :title="item.title"
                      :class="item.transform?'transform-icon':''"/>
<#--                    <Icon type="ios-skip-backward" :size="viewSettingFont"/>
                    <Icon type="ios-play" class="transform-icon" :size="viewSettingFont"/>
                    <Icon type="ios-paper" :size="viewSettingFont"/>
                    <Icon type="md-podium" :size="viewSettingFont"/>
                    <Icon type="ios-play" :size="viewSettingFont"/>
                    <Icon type="ios-skip-forward" :size="viewSettingFont"/>
                    <Icon type="ios-print" :size="viewSettingFont"/>
                    <Icon type="ios-cloud-download" :size="viewSettingFont"/>-->
                </div>
                <div style="height: 22px;line-height: 22px;float: right;margin-right: 5px">
                    <Button @click="clearViewToolbar" size="small" style="">清空</Button>
                </div>
            </div>

            <div style="cursor: pointer;background: #fff;margin-top:5px;padding: 2px" class="view-toolbar-list">
                <Row>
                    <Col span="8" v-for="(item,index) in btnList" :key="index">
                        <div style="height: 30px;line-height: 30px" @click="handleClickToolbarBtn(item)">
                            <Icon :type="item.icon" :size="viewSettingFont" :class="item.transform?'transform-icon':''"/>
                            {{ item.title }}
                            <span v-if="isSelected(item.index)" style="float: right;font-size: 20px">·</span>
                        </div>
                    </Col>
                    <#--<Col span="8">
                        <div style="height: 30px;line-height: 30px">
                           &nbsp;
                        </div>
                    </Col>-->
                </Row>
            </div>

            <div style="margin-top: 10px">
                <Row>
                    <Col span="4">
                        <span style="display:inline-block;height: 30px;line-height: 30px;">每页展示条数：</span>
                    </Col>
                    <Col span="4">
                        <i-input type="number" v-model="pageSize" @on-change="handleViewPageSizeChange"></i-input>
                    </Col>
                </Row>
            </div>
        </Modal>
    </div>
</script>
<script>

    Vue.component('j-view-setting', {
        template: '#view-setting-template',
        props: {
            show:{
                type: Boolean,
                required: false,
                default: false
            },
            settings:{
                type: Object,
                required: false,
                default: ()=>{}
            }
        },
        data(){
            return {
                enableViewSetting: true,
                viewSettingFont: 20,
                btnList:[
                    { icon: 'ios-skip-backward', title: '首页', index: 1 },
                    { icon: 'ios-play', title: '上一页', transform:true, index: 2 },
                    { icon: 'ios-paper', title: '当前页/总页数', index: 3 },
                    { icon: 'md-podium', title: '分页显示数', index: 4 },
                    { icon: 'ios-play', title: '下一页', index: 5 },
                    { icon: 'ios-skip-forward', title: '末页', index: 6 },
                    { icon: 'ios-print', title: '打印', index: 7 },
                    { icon: 'ios-cloud-download', title: '导出', index: 8, code:'' },
                    { icon: 'md-switch', title: '清晰度设置', index: 9, code:'' }
                ],
                selectList:[],
                selectedIndexList:[1, 2, 3, 4, 5, 6, 7, 8, 9],
                showViewToolbar:true,
                pageSize: ''
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
            onViewSettingSave(){
                let btnList = this.selectedIndexList
                if(btnList.length==0){
                    btnList.push(999)
                }
                let param = {
                    show: this.showViewToolbar,
                    btnList: btnList,
                    pageSize: this.pageSize
                }
                this.$emit('change', param)
            },
            resetForm(){
                if(this.settings){
                    const { show, btnList, pageSize } = this.settings;
                    if(show===false){
                        this.showViewToolbar = false;
                    }else{
                        this.showViewToolbar = true;
                    }
                    if(btnList){
                        if(btnList.length==0){
                            this.selectedIndexList = [1,2,3,4,5,6,7,8,9]
                        }else{
                            this.selectedIndexList = [...btnList];
                        }
                    }
                    if(pageSize){
                        this.pageSize = pageSize;
                    }
                }
            },
            onViewSettingCancel(){
                this.$emit('change', false)
            },
            handleClickToolbarBtn(item){
                // console.log('handleClickToolbarBtn', item)
                let index = item.index;
                let position = this.selectedIndexList.indexOf(index)
                if(position>=0){
                    this.selectedIndexList.splice(position, 1)
                }else{
                    this.selectedIndexList.push(index)
                }
            },
            isSelected(index){
                if(this.selectedIndexList.indexOf(index)>=0){
                    return true
                }else{
                    return false
                }
            },
            resetViewToolbar(){
                this.showViewToolbar = true
                this.selectedIndexList = [1,2,3,4,5,6,7,8,9]
            },
            clearViewToolbar(){
                this.selectedIndexList = []
            },
            hideViewToolbar(){
                this.showViewToolbar = false
            },
            handleViewPageSizeChange(e){
                if(!e.target.value){
                    this.pageSize = ''
                }else if(e.target.value<1){
                    this.pageSize = 10
                }
            }

        }
    })
</script>