<style>
    /*地图相关样式 -begin*/
    .page{
        float:right;
        margin-top:20px;
    }
    .vertical-center-modal-big{
        margin: 2% 4%;
    }
    .ivu-modal-confirm-head-icon {
        font-size: 28px;
    }
    .ivu-cascader-transfer .ivu-cascader-menu-item{
        font-size: 12px !important;
    }
    /*地图相关样式 -end*/
</style>
<script type="text/x-template" id="map-setting-template">
    <div>
        <Submenu name="10" style="border-bottom: inset 1px;" class="rightFontSize dataSourceForm">
            <template slot="title">
                <span>地图设置</span>
            </template>
            <div class="blockDiv">
              <Row class="ivurow">
                <p>地图级别&nbsp;&nbsp;</p>
                <div class="iSelect">
                  <i-select clearable transfer="true" size="small" @on-change="mapLevelChange" v-model="mapOption.mapLevel">
                    <i-option class="rightFontSize" v-for="(map,index) in mapLevelData" :value="map.value" :index="index">
                      {{ map.label}}
                    </i-option>
                  </i-select>
                </div>
              </Row>
              <Row class="ivurow" v-if="mapOption.mapType=='1' || mapOption.mapType=='2' || mapOption.mapType=='3'">
                <p v-if="mapOption.mapType=='1'">选择省份&nbsp;&nbsp;</p>
	              <p v-if="mapOption.mapType=='2'">选择城市&nbsp;&nbsp;</p>
	              <p v-if="mapOption.mapType=='3'">选择区域&nbsp;&nbsp;</p>
                <div class="iSelect">
                  <Cascader v-if="mapOption.mapType=='1' " clearable transfer="true" :data="provinceData" size="small" @on-change="(value, selectedData) => provinceChange(value, selectedData)" v-model="mapOption.mapCode"></Cascader>
                  <Cascader v-if="mapOption.mapType=='2' " clearable transfer="true" :data="cityData" size="small" @on-change="(value, selectedData) => provinceChange(value, selectedData)" v-model="mapOption.mapCode"></Cascader>
                  <Cascader v-if="mapOption.mapType=='3' " clearable transfer="true" :data="areaData" size="small" @on-change="(value, selectedData) => provinceChange(value, selectedData)" v-model="mapOption.mapCode"></Cascader>
                </div>
              </Row>
                <Row class="ivurow">
                    <p>比例&nbsp;&nbsp;</p>
                    <slider v-model="mapOption.zoom" step="0.1"  max="2" @on-change="onGeoChange" style="margin-top: -9px;width: 158px;margin-left: 5px;"></slider>
                </Row>
                <Row class="ivurow">
                  <p>名称显示&nbsp;&nbsp;</p>
                  <i-switch size="small" style="margin-left: 116px;" v-model="mapOption.label_show" @on-change="onGeoChange"/>
                </Row>
                <Row class="ivurow" v-if="mapOption.label_show">
                    <p>字体大小&nbsp;&nbsp;</p>
                    <i-input size="small" type="number" v-model="mapOption.label_fontSize" @on-blur="onGeoChange" style="width: 141px"></i-input>
                </Row>
                <Row class="ivurow" v-if="typeof mapOption.label_color !== 'undefined' && mapOption.label_show">
                    <p>字体颜色&nbsp;&nbsp;</p>
                    <Col>
                    <i-input v-model="mapOption.label_color" size="small" style="width: 141px" @on-change="onGeoChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="mapOption.label_color" :editable="false" alpha  :transfer="true" size="small" @on-change="onGeoChange"/>
                    </span>
                    </i-input>
                    </Col>
                </Row>
                <Row class="ivurow" v-if="typeof mapOption.emphasis_label_color !== 'undefined'">
                    <p>字体高亮颜色&nbsp;&nbsp;</p>
                    <Col>
                    <i-input v-model="mapOption.emphasis_label_color" size="small" style="width: 117px;" @on-change="onGeoChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="mapOption.emphasis_label_color" :editable="false" alpha  :transfer="true" size="small" @on-change="onGeoChange"/>
                    </span>
                    </i-input>
                    </Col>
                </Row>
                <Row class="ivurow">
                    <p>区域线&nbsp;&nbsp;</p>
                    <slider v-model="mapOption.itemStyle_borderWidth" step="0.1" max="5" @on-change="onGeoChange" style="margin-top: -9px;width: 147px;margin-left: 5px;"></slider>
                </Row>
                <Row class="ivurow" v-if="typeof mapOption.itemStyle_areaColor !== 'undefined'">
                    <p>区域颜色&nbsp;&nbsp;</p>
                    <Col>
                    <i-input v-model="mapOption.itemStyle_areaColor" size="small" style="width: 141px" @on-change="onGeoChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="mapOption.itemStyle_areaColor" :editable="false" alpha  :transfer="true" size="small" @on-change="onGeoChange"/>
                    </span>
                    </i-input>
                    </Col>
                </Row>
                <Row class="ivurow" v-if="typeof mapOption.emphasis_itemStyle_areaColor !== 'undefined'">
                    <p>区域高亮颜色&nbsp;&nbsp;</p>
                    <Col>
                    <i-input v-model="mapOption.emphasis_itemStyle_areaColor" size="small" style="width: 117px;" @on-change="onGeoChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="mapOption.emphasis_itemStyle_areaColor" :editable="false" alpha  :transfer="true" size="small" @on-change="onGeoChange"/>
                    </span>
                    </i-input>
                    </Col>
                </Row>
                <Row class="ivurow" v-if="typeof mapOption.itemStyle_borderColor !== 'undefined'">
                    <p>边框颜色&nbsp;&nbsp;</p>
                    <Col>
                    <i-input v-model="mapOption.itemStyle_borderColor" size="small" style="width: 141px" @on-change="onGeoChange">
                    <span slot="append">
                        <color-picker class="colorPicker" v-model="mapOption.itemStyle_borderColor" :editable="false" alpha  :transfer="true" size="small" @on-change="onGeoChange"/>
                    </span>
                    </i-input>
                    </Col>
                </Row>
            </div>
        </Submenu>
        <#--地图弹窗-->
        <div>
            <Modal
                    class-name="vertical-center-modal-big"
                    fullscreen=true
                    :loading="loading"
                    v-model="mapListModal"
                    title="地图维护"
                    @on-cancel="callMapDb">
                <i-form inline :label-width="85">
                    <Row >
                        <i-col span="6">
                            <form-item label="名称:">
                                <i-input  style="width: 253px" type="text" v-model="queryParams.label" placeholder="请输入名称">
                                </i-input>
                            </form-item>
                        </i-col>
                        <i-col span="6">
                            <form-item label="编码:">
                                <i-input type="text" style="width: 300px" v-model="queryParams.name" placeholder="请输入编码">
                                </i-input>
                            </form-item>
                        </i-col>
                        <i-col span="2" style="margin-left: 60px">
                            <i-button @click="loadData(1)"  type="primary" icon="ios-search">查询</i-button>
                        </i-col>
                        <i-col span="2">
                            <i-button @click="clearQuery"  type="primary" icon="ios-refresh">重置</i-button>
                        </i-col>
                    </Row>
                </i-form>
                <Row style="margin-top:25px">
                    <i-col span="3">
                        <i-button @click="addMapData" type="primary">新增</i-button>
                    </i-col>
                </Row>
                <template>
                    <i-table border stripe :columns="mapTab.columns" :data="mapTab.data"  style="margin-top: 1%;"></i-table>
                    <div class="page">
                        <Page :total="page.total"
                              show-total
                              show-elevator
                              @on-change="handleCurrentChange"
                              @on-page-size-change="handleSizeChange">
                        </Page>
                    </div>
                </template>
                <template slot="footer">
                    <i-button type="primary" @click="callMapDb">关闭</i-button>
                </template>
            </Modal>

            <Modal :loading="loading" v-model="mapDataModal" title="数据源" :width="35" @on-cancel="clearMapDb" @on-ok="saveMapDb">
                <div style="padding-right: 30px">
                    <i-form ref="mapSource" :model="mapSource" :rules="dataFormValidate" label-colon :label-width="100" >

                        <form-item prop="label" label="地图名称" style="height:50px">
                            <i-input v-model="mapSource.label" placeholder="请输入地图名称"></i-input>
                        </form-item>

                        <form-item prop="name" label="地图编码" style="height:50px">
                            <i-input v-model="mapSource.name" placeholder="请输入地图编码"></i-input>
                        </form-item>

                        <form-item prop="data" label="地图数据">
                            <i-input type="textarea" :autosize="{minRows: 15,maxRows:15}"  v-model="mapSource.data"  placeholder="请输入地图数据"></i-input>
                            <a href="http://datav.aliyun.com/tools/atlas" target="_blank">地图数据json下载</a>
                        </form-item>

                    </i-form>
                </div>
            </Modal>
        </div>
    </div>
</script>

<script>
    Vue.component('j-map-setting', {
        template: '#map-setting-template',
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
                mapOption: {
                    map:100000,//map code编码
                    mapCode:[100000],
                    mapName:"中国人民共和国",
                    mapLevel:"",//地图等级
                    mapType:"0", //地图显示
                    zoom:0.5,
                    label_color:'#000',
                    label_fontSize:12,
                    itemStyle_borderWidth :0.5,
                    itemStyle_areaColor :'#fff',
                    itemStyle_borderColor:'#000',
                    emphasis_label_color:'#fff',
                    emphasis_itemStyle_areaColor:'red',
                    label_show:false //地图是否显示
                },
                mapListModal:false,
                mapList:[],
                allMapList:[],
                loading:true,
                mapDataModal: false,
                page: { //分页参数
                    page: 1,
                    size: 10,
                    total: 0,
                },
                queryParams:{
                    label:"",
                    name:"",
                },
                mapSource:{
                    id:"",
                    label:"",
                    name:"",
                    data:""
                },
                dataFormValidate:{
                    label:[
                        { required: true, message: '地图名称不能为空', trigger: 'blur' }
                    ],
                    name:[
                        { required: true, message: '地图编码不能为空', trigger: 'blur' }
                    ],
                    data:[
                        { required: true, message: '地图数据不能为空', trigger: 'blur' }
                    ]
                },
                mapTab:{
                    data: [],
                    columns: [
                        {
                            type: 'index',
                            title: '序号',
                            width: 80,
                            align: 'center'
                        },
                        {
                            title: '地图名称',
                            key: 'label'
                        },
                        {
                            title: '地图编码',
                            key: 'name'
                        },
                        {
                            title: '操作',
                            key: 'action',
                            width: 150,
                            align: 'center',
                            render: (h, params) => {
                                return this.renderButton(h, params);
                            }
                        }
                    ]
                },
              provinceData:[],//省的json结合
              cityData:[],//城市的json结合
              areaData:[],//地区的json结合
              mapLevelData: [{
                "label": "全国",
                "value": "0"
              }, {
                "label": "省级",
                "value": "1"
              }, {
                "label": "市级",
                "value": "2"
              }, {
                "label": "区级",
                "value": "3"
              }],//地图级别集合
              mapUrl:"/jmreport/desreport_/regionaljson/", //请求路径
              mapData:[], //地图的集合
              initShow:false
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
        mounted : function() {
            this.loadAllData()
        },
        methods: {
            initData: function (){
                if (this.settings){
                    this.mapOption = Object.assign(this.mapOption, this.settings)
                }
            },
            onGeoChange(){
                this.$emit('change','geo',this.mapOption)
            },
            //地图数据集维护Modal
            mapManage(flag){
                this.clearQuery();
                this.mapListModal=flag;
            },
            loadData(){
                //加载数据列表
                let that=this;
                $http.get({
                    url: api.mapList,
                    data:{
                        label:that.queryParams.label,
                        name:that.queryParams.name,
                        current:that.page.page,
                        size:that.page.size,
                        token:this.token
                    },
                    success:(result)=>{
                        // console.log('result',result)
                        let records=result.records;
                        that.page.total = result.total
                        that.$nextTick(()=>{
                            that.mapTab.data=JSON.parse(JSON.stringify(records));
                        });
                        that.mapList = records && records.length>0?records:mapTypeList;
                    }
                });
            },
            loadAllData(){
                this.loadMapData("province.json",1)
                this.loadMapData("province.city.json",2)
                this.loadMapData("province.city.area.json",3)
            },
            //地图级别点击事件
            mapLevelChange(val){
                this.mapOption.mapType= val
                this.mapOption.mapCode=[]
                //如果是0代表全国
                if(val == 0){
                  let value =[100000]
                  let selectedData =[{label:"中华人民共和国",value:100000}] 
                  this.provinceChange(value,selectedData)
                }
            },
            //省市区点击事件
            provinceChange(value, selectedData){
              this.mapOption.mapCode = value;
              this.mapOption.map = selectedData[selectedData.length-1].value;
              let label = selectedData[selectedData.length-1].label;
              let name = selectedData[selectedData.length-1].value;
              this.mapOption.mapName = label;
              $http.post({
                contentType:'json',
                url: api.queryMapByCode,
                data:JSON.stringify({name:name,label:label,mapType:this.mapOption.mapType}),
                success:(result)=>{
                  xs.registerMap(result.name,result.data);
                  this.$emit('change','geo',this.mapOption)
                }
              })
              
            },
            handleSizeChange(val){
                this.page.size = val;
                this.loadData();
            },
            handleCurrentChange (val) {
                this.page.page = val;
                this.loadData();
            },
            saveMapDb() {
                //保存地图数据
                this.$refs.mapSource.validate((valid)=>{
                    if(valid){
                        //保存地图表单数据
                        $http.post({
                            contentType:'json',
                            url: api.addMapData,
                            data:JSON.stringify(this.mapSource),
                            success:(res)=>{
                                this.$Notice.success({
                                    title: '保存成功'
                                });
                                this.clearMapDb()
                                this.loadData();
                            }
                        });
                        return;
                    }else{
                        setTimeout(() => {
                            this.loading = false
                            this.$nextTick(() => {
                                this.loading = true
                            })
                        }, 500)
                        return;
                    }
                });
            },
            clearMapDb(){
                this.mapSource.id=""
                this.mapSource.label=""
                this.mapSource.name=""
                this.mapSource.data=""
                this.loadData();
                this.$refs["mapSource"].resetFields();
                this.mapDataModal = false;
            },
            callMapDb() {
                this.loadAllData();
                this.mapManage(false)
            },
            addMapData() {
                //新增地图
                this.mapDataModal=true;
            },
            clearQuery(){
                //清除地图查询数据
                for(let key in this.queryParams){
                    this.queryParams[key] = "";
                }
                this.page.page=1;
                this.page.size=10;
                this.loadData();
            },
            //渲染删除编辑button
            renderButton(h, params) {
                return h('div',[
                    h('i-button', {
                        props: {
                            type: 'primary',
                            size: 'small'
                        },
                        style:{
                            'margin-right':'5px'
                        },
                        on: {
                            click: () => {
                                this.mapTab.data.forEach((item)=>{
                                    if (item.id === params.row.id){
                                        this.mapSource = item;
                                    }
                                })
                                this.mapDataModal = true;
                            }
                        }
                    },'编辑'),
                    h('i-button', {
                        props: {
                            type: 'primary',
                            size: 'small'
                        },
                        on: {
                            click: () => {
                                this.$Modal.confirm({
                                    title:"提示",
                                    content: '是否确认删除?',
                                    onOk: () => {
                                        let mapSource = {};
                                        mapSource.id = params.row.id;
                                        $http.post({
                                            contentType:'json',
                                            url: api.delMapSource,
                                            data:JSON.stringify(mapSource),
                                            success:(result)=>{
                                                this.$Notice.success({
                                                    title: '删除成功'
                                                });
                                                this.loadData();
                                            }
                                        });
                                    }
                                });
                            }
                        }
                    },'删除')
                ])
            },
          //type 1 省的json 2省市的json 3省市区的json
          loadMapData(url,type) {
            $http.get({
              //update-begin---author:wangshuai ---date:20220412  for：[issues/890]微服务下customPrePath不起作用------------
              url:"${base}"+"${customPrePath}"+this.mapUrl+url,
              //update-end---author:wangshuai ---date:20220412  for：[issues/890]微服务下customPrePath不起作用--------------
              fail:(res)=>{
                if(type == 1){
                  this.provinceData = res.data
                }
                if(type == 2){
                  this.cityData = res.data
                }
                if(type == 3){
                  this.areaData = res.data
                }
              }
            })
          },
        }
    })

    //初始化地图数据
    function loadMap(item){
        let config = JSON.parse(item.config);
        this.initShow = true
        let map = config.geo.map;
        if(map=="china"){
          map=100000;
        }
        $http.post({
            contentType:'json',
            url: api.queryMapByCode,
            data:JSON.stringify({name:map,label:config.geo.mapName}),
            success:(result)=>{
                let data=JSON.parse(result.data);
                xs.registerMap(result.name,data);
                xs.updateChart(item.layer_id ,config);
            }
        });
    }
</script>