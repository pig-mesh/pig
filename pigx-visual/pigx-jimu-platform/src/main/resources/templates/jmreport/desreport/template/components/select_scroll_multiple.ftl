<#--多选组件，用于远程搜索查询
item:传递的参数，包含字典code，查询key
value:view组件传递的值 v-model
index:下标-->
<script type="text/x-template" id="select-scroll-multiple-template">
    <div :id="'click'+index">
        <div class="ivu-select ivu-select-multiple ivu-select-small" style="width: 100%">
            <div class="ivu-select-selection" @click="handleClick">
                <span class="select-dict-text" v-if="dictText!=''">{{dictText}}</span>
                <span v-else  class="ivu-select-input" style="color: rgb(197,201,203)">{{'请选择'+dataList.title}}</span>
            </div>
          <i class="ivu-icon ivu-icon-md-close ivu-select-arrow" @click="handleIconClick"></i>
        </div>
        <div class="scroll-class" v-if="scrollShow">
            <Scroll :class="noData == true?'no-loading':''" :height="dictHeight" :on-reach-bottom="handleReachBottom"
                    :distance-to-edge="1">
              <input v-model="searchText" type="text" placeholder="请输入查询条件" class="ivu-select-input bottom-border" />
              <ul v-for="(item,index) in dictList" :key="index" @mousedown="scrollListClick(item,index)"
                    class="scroll-list-class">
                    <li :class="item.active == true?'select-item-selected':''"><span>{{ item.text }}</span></li>
                </ul>
                <ul class="no-more-data" v-if="noData">
                    <li style="font-size: 12px;text-align: center;margin-top:5px">暂无更多数据</li>
                </ul>
            </Scroll>
        </div>
    </div>
</script>
<script>
  Vue.component('j-select-scroll-multiple', {
    template: '#select-scroll-multiple-template',
    props: {
      item: {
        type: Object,
        default: () => []
      },
      value: {
        type: String,
        default: () => ""
      },
      index: {
        type: Number
      }
    },
    data() {
      return {
        // 表格高度
        dictHeight: 200,
        // 数据总数
        tableCount: 0,
        //下拉框数据
        dataList: {},
        dictList: [],
        //字典文本
        dictText: "",
        // 是否加载中
        tableLoading: false,
        //滚动组件是否显示
        scrollShow: false,
        userpage: 1,
        noData: false,
        //初始字典text
        initText:"",
        //是否初始值
        initValue:true,
        //复制一份字典集合
        copyDictList:[],
        //搜索的文本
        searchText:"",
        //字典值
        dictValue:""
      }
    },
    create() {

    },
    watch: {
      item: {
        deep: true,
        immediate: true,
        handler: function () {
          this.dictData();
        }
      },
      value: {
        immediate: true,
        handler: function () {
          this.setDictText();
        }
      },
      searchText(){
        this.handleOnChange();
      }
    },
    mounted() {
      //页面初始化监听click事件
      document.addEventListener("click", e => {
        this.closeScroll(e);
      });
    },
    beforeDestroy() {
      //页面销毁移除监听事件
      document.removeEventListener("click", this.closeScroll);
    },
    methods: {
      /**
       * 关闭滚动弹窗
       */
      closeScroll(e) {
        let self = this;
        let click = document.getElementById("click"+this.index);
        if (click && !click.contains(e.target)) {
          if (self.scrollShow == true){
             self.scrollShow = false;
          }
        }
      },
      /**
       * 获取字典数据
       */
      dictData() {
        if (this.item) {
          this.dataList = JSON.parse(this.item)
          if (this.dataList) {
            this.dictList = this.dataList.dictList
            this.copyDictList = this.dataList.dictList
            //目前只有不勾选复选框并且不是select的时候才会分页，api和普通的字典不用去后台查数据
            if(this.dataList.dictCode && this.dataList.dictCode.indexOf("select") ==-1 || this.dataList.dictList.length<7) {
              this.noData = true
            }
          }
          this.dictHeight = this.dictList.length>6?200:this.dictList.length*32+82
        }
      },
      /**
       *为字典值赋值
       */
      setDictText() {
        if (this.value.length == 0) {
          this.dictText = "";
          this.searchText = ""
          //重置选中事件
          this.restActive();
        }else{
          this.loopData(this.dictList);
        }
      },
      /**
       *赋值字典集合及是否选中
       * @param dictList 字典集合
       */
      loopData(dictList){
        //初始化的时候才赋默认值
        let dictText = this.dictText
        let values = this.value
        for (let i = 0; i <dictList.length ; i++) {
          let value = dictList[i].value;
          if(values.indexOf(value)!=-1){
            //字典文本不为空，得判断时候包含此文本
            if(dictText && dictText.indexOf(dictText) != 0){
               dictText = dictText +  dictList[i].text +","
            }
            //字典文本是空，直接相等即可
            if(!dictText){
              dictText = dictList[i].text;
            }
            dictList[i].active = true
          }
        }
        if(dictText && dictText.charAt(dictText.length-1) == ","){
          dictText = dictText.substring(0,dictText.length-1)
        }
        this.dictText = dictText
        this.dictList = dictList
      },
      /**
       * 重置选中状态
       */
      restActive(){
        let dictList = this.dictList
        for (let i = 0; i <dictList.length ; i++) {
          if(dictList[i].active){
            dictList[i].active = false
          }
        }
      },
      /**
       * 列表点击事件
       * @param val 字典对象（value,text）
       * @param index 当前循环的到第几个
       */
      scrollListClick(val,index) {
        if(this.dictText){
          let dictTextArray = this.dictText.split(",");
          let dictValueArray = this.value
          //是否是已删除的
          let remove = false
          for (let i = 0; i < dictTextArray.length; i++) {
            if(dictTextArray[i]==val.text){
              dictTextArray.splice(i,1)
              dictValueArray.splice(i,1)
              remove =true
            }
          }
          if(!remove){
            dictTextArray.push(val.text)
            dictValueArray.push(val.value)
          }
          if(dictTextArray.length>0){
            this.dictText = dictTextArray.toString()
            this.value = dictValueArray
          }else{
            this.dictText = ""
            this.value = []
          }
        }else{
          this.dictText = val.text
          let valueArray = [];
          valueArray.push(val.value)
          this.value =valueArray
        }
        this.scollShow = true;
        let active = this.dictList[index].active;
        this.dictList[index].active = !active;
        //返回值
        this.returnDictValue(val.value);
      },
      /**
       *输入框点击事件
       */
      handleClick() {
        this.scrollShow = !this.scrollShow;
      },
      /**
       *滚动条无限加载
       */
      handleReachBottom() {
        if (!this.noData) {
          return new Promise(resolve => {
            if (this.noData) {
              resolve();
            }
            this.userpage = this.userpage + 1;
            setTimeout(() => {
              if(!this.dataList.dictCode || this.dataList.dictCode.indexOf("select") !=-1) {
                 this.getDictData(0);
              }else{
                //前台进行搜索
                if(this.dictText){
                  this.searchDictByText();
                }else{
                  this.dictList = this.copyDictList
                }
              }
              resolve();
            }, 500);
          });
        }
      },
      /**
       *值发生改变请求后台
       */
      handleOnChange() {
        this.userpage = 1;
        this.scrollShow = true
        //字典为空或者包含select语句才会后台搜索，否则直接在前台搜索
        if(!this.dataList.dictCode || this.dataList.dictCode.indexOf("select") !=-1) {
          //后台进行搜索
          this.getDictData(1);
        }else{
          //前台进行搜索
          //update-begin---author:wangshuai ---date:20221117  for：[issues/1316]查询栏 下拉多选搜索无效------------
          if(this.searchText){
          //update-end---author:wangshuai ---date:20221117  for：[issues/1316]查询栏 下拉多选搜索无效--------------
            this.searchDictByText();
          }else{
            this.dictList = this.copyDictList
          }
          this.dictHeight = this.dictList.length>6?200:this.dictList.length*32+82
        }
      },
      /**
       * 查询字典数据
       * @param type 字典值需要拼接还是直接替换 0拼接 1 替换
       */
      getDictData(type) {
        let paramSearch = this.dataList.paramSearch
        let dbKey = this.dataList.dbCode + "__" + this.dataList.name
        let params = getRequestUrlParam();
        let dataStr = {
          "reportId": configId,
          "text": this.searchText,
          "key": dbKey,
          "paramSearch": paramSearch,
          "params": params,
          "pageNo": this.userpage
        }
        $http.get({
          url: api.dictCodeSearch,
          data: dataStr,
          success: (res) => {
            if (res && res.length > 0) {
              let dictList = this.dictList;
              //滚动条无限加载中直接push
              if (type == 0) {
                for (let i = 0; i < res.length; i++) {
                  dictList.push(res[i])
                }
              } else {
                //查询直接赋值给字典数组
                dictList = res
              }
              //循环数据赋是否已点击
              this.loopData(dictList);
              //判断长度，是否有数据
              if(res.length<7){
                this.noData = true
              }else{
                this.noData = false
              }
            } else {
              if (type == 1) {
                this.dictList = []
              }
              this.noData = true
            }
            //为下拉框设置高度
            this.dictHeight = this.dictList.length>6?200:this.dictList.length*32+82
          }
        })
      },
      /**
       *前台搜索
       */
      searchDictByText(){
        let dictList = this.dictList
        let searchText = this.searchText
        let newDictList = [];
        if(!this.searchText){
          this.dictList = this.copyDictList;
        }else{
          for (let i = 0; i <dictList.length; i++) {
            let value = dictList[i].value;
            let text = dictList[i].text;
            if(value.indexOf(searchText) >= 0 || value === searchText){
              newDictList.push(dictList[i])
            }else if(text.indexOf(searchText) >= 0 || text ===searchText){
              newDictList.push(dictList[i])
            }
          }
          if(newDictList.length>0){
            this.dictList = newDictList;
          }
        }
        this.dictHeight = this.dictList.length>6?150:this.dictList.length*32+82
      },
      /**
       *返回父组件的值
       */
      returnDictValue() {
        let val = {};
        val.value = this.value
        val.key = this.dataList.key;
        this.$emit('dictmultipleok', val)
      },
      /**
       * 图标点击事件
       */
      handleIconClick(){
        this.dictText = ""
        this.value = []
        let val = {};
        val.value = []
        val.key = this.dataList.key;
        this.$emit('dictmultipleok', val)
        this.scollShow = false
      }
    }
  })
</script>

<style scoped>
  ::-webkit-scrollbar {
      width: 6px;
      height: 16px;
      background-color: #F5F5F5;
  }
  /*定义滚动条轨道 内阴影+圆角*/
  ::-webkit-scrollbar-track {
      border-radius: 3px;
      background-color: #f5f7f9;
  }
  /*定义滑块 内阴影+圆角*/
  ::-webkit-scrollbar-thumb {
      border-radius: 3px;
      background-color: #ccc;
  }
  .scroll-class {
      position: absolute;
      background: white;
      z-index: 9999;
      overflow: hidden;
      border: 1px solid #dddee1;
      border-radius: 4px;
      top: 35px;
      -webkit-box-shadow: 0 1px 6px rgba(0, 0, 0, 0.2);
      box-shadow: 0 1px 6px rgba(0, 0, 0, 0.2);
  }
  .scroll-list-class {
      cursor: pointer;
      z-index: 9999;
      list-style-type: none;
  }
  .scroll-list-class li span {
      margin-left: 15px;
      font-size: 14px;
  }
  .scroll-list-class :hover {
      background: #f3f3f3;
  }
  .no-more-data {
      border-top: 1px solid #f0f0f0;
  }
  .no-loading .ivu-spin-text {
      display: none;
  }
  .no-loading .ivu-scroll-content-loading {
      opacity: 1;
  }
  .ivu-scroll-loader {
      margin-top: -5px;
  }
  .select-item-selected{
      color:rgba(45,140,240,.9);
      position:relative;
  }
  .select-item-selected :after{
      display: inline-block;
      font-family: Ionicons, serif;
      speak: none;
      font-style: normal;
      font-weight: 400;
      font-variant: normal;
      text-transform: none;
      text-rendering: optimizeLegibility;
      line-height: 1;
      -webkit-font-smoothing: antialiased;
      -moz-osx-font-smoothing: grayscale;
      vertical-align: -0.125em;
      text-align: center;
      font-size: 24px;
      content: "\F171";
      color: rgba(45,140,240,.9);
      position: absolute;
      top: 2px;
      right: 8px;
  }
  .select-dict-text{
      display:inline;
      float: left;
      overflow: hidden;
      text-overflow: ellipsis;
      -o-text-overflow: ellipsis;
      white-space:nowrap;
      margin-left: 5px;
      max-width:80%;
      position:relative;
  }
  .bottom-border{
      border-bottom:1px solid #f0f0f0;
  }
  .ivu-select-input{
      display: inline;
      height: 32px;
      line-height: 32px;
      width: 190px;
      font-size: 14px;
      outline: 0;
      -webkit-box-sizing: border-box;
      box-sizing: border-box;
      color: #515a6e;
      position: relative;
      cursor: pointer;
      padding-left: 16px;
  }
  .ivu-select-multiple .ivu-select-selection{
      padding: 0 0 0 4px;
      float: left;
  }
  .icon-position{
      position: relative;
      right: 25px;
      top: 6px;
      cursor: pointer;
  }
</style>