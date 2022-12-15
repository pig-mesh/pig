<#--单选组件，用于远程搜索查询
item:传递的参数，包含字典code，查询key
value:view组件传递的值 v-model-->
<script type="text/x-template" id="select-scroll-radio-template">
    <div>
	    <div style="display: flex">
		    <input ref="clickInput" class="ivu-input ivu-input-default" v-model="dictText"
		           :placeholder="'请选择'+dataList.title" @blur="clickBlur"
		           @click="handleClick" style="cursor: pointer"/>
		    <i class="ivu-icon ivu-icon-md-close ivu-select-arrow" @click="handleIconClick" v-if="dictText"></i>
	    </div>
        <div class="scoll-class" v-if="scollShow">
            <Scroll :class="noData == true?'no-loading':''" :height="dictHeight" :on-reach-bottom="handleReachBottom"
                    :distance-to-edge="1">
                <ul v-for="(item,index) in dictList" :key="index" @mousedown="scollListClick(item)"
                    class="scoll-list-class">
                    <li><span>{{ item.text }}</span></li>
                </ul>
                <ul class="no-more-data" v-if="noData">
                    <li style="font-size: 12px;text-align: center;margin-top:5px">暂无更多数据</li>
                </ul>
            </Scroll>
        </div>
    </div>
</script>
<script>
  Vue.component('j-select-scroll-radio', {
    template: '#select-scroll-radio-template',
    props: {
      item: {
        type: Object,
        default: () => []
      },
      value: {
        type: String,
        default: () => ""
      }
    },
    data() {
      return {
        // 表格高度
        dictHeight: 200,
        // 数据总数
        tableCount: 0,
        dataList: {},
        dictList: [],
        //字典文本
        dictText: "",
        // 是否加载中
        tableLoading: false,
        //滚动组件是否显示
        scollShow: false,
        userpage: 1,
        noData: false,
        //初始字典text
        initText: "",
        //是否初始值
        initValue: true,
        //复制一份字典集合
        copyDictList: []
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
        deep: true,
        immediate: true,
        handler: function () {
          this.setDictText();
        }
      },
      dictText:{
        immediate: true,
        handler: function () {
            this.handleOnChange()
        }
      }
    },
    methods: {
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
            if (this.dataList.dictCode && this.dataList.dictCode.indexOf("select") == -1 || this.dataList.dictList.length < 7) {
              this.noData = true
            }
          }

        }
      },
      /**
       *为字典值赋值
       */
      setDictText() {
        if (!this.value) {
          this.dictText = "";
          this.searchLocalOrApi();
        } else {
          //赋值默认值
          let dict = this.dictList.filter(item => item.value == this.value)
          if (dict && dict.length > 0) {
            this.dictText = dict[0].text
            this.initText = dict[0].text
          }
          //update-begin---author:wangshuai ---date:20220315  for：下拉单选样式出现混乱--------
          this.dictHeight = this.dictList.length > 6 ? 200 : this.dictList.length * 32 + 50
          //update-end---author:wangshuai ---date:20220315  for：下拉单选样式出现混乱--------
        }
      },
      //无限加载中的事件
      scollListClick(val) {
        this.scollShow = false;
        this.dictText = val.text;
        this.value = val.value
        val.key = this.dataList.key;
        this.$emit('dictok', val)
        this.searchLocalOrApi();
      },
      handleClick() {
        this.scollShow = !this.scollShow;
      },
      clickBlur() {
        this.scollShow = false;
      },
      //滚动条无限加载
      handleReachBottom() {
        if (!this.noData) {
          return new Promise(resolve => {
            if (this.noData) {
              resolve();
            }
            this.userpage = this.userpage + 1;
            setTimeout(() => {
              this.getDictData(0);
              resolve();
            }, 500);
          });
        }
      },
      handleOnChange() {
        let val = {}
        val.key = this.dataList.key
        //update-begin---author:wangshuai ---date:20220315  for：[JMREP-2552]下拉单选重置就出不来了--------
        if(!this.dictText){
            val.value = ""
        }else{
            val.value = this.value
        }
        //update-end---author:wangshuai ---date:20220315  for：[JMREP-2552]下拉单选重置就出不来了--------
        this.$emit('dictok', val)
        this.searchLocalOrApi();
      },
      searchLocalOrApi() {
        this.userpage = 1;
        //字典为空或者包含select语句才会后台搜索，否则直接在前台搜索
        if (!this.dataList.dictCode || this.dataList.dictCode.indexOf("select") != -1) {
          //后台进行搜索
          this.getDictData(1);
        } else {
          //前台进行搜索
          if (this.dictText) {
            this.searchDictByText();
          } else {
            this.dictList = this.copyDictList
            //update-begin---author:wangshuai ---date:20220315  for：下拉单选样式出现混乱--------
            this.dictHeight = this.dictList.length > 6 ? 200 : this.dictList.length * 32 + 50
            //update-end---author:wangshuai ---date:20220315  for：下拉单选样式出现混乱--------
          }
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
          "text": this.dictText,
          "key": dbKey,
          "paramSearch": paramSearch,
          params: params,
          pageNo: this.userpage
        }
        $http.get({
          url: api.dictCodeSearch,
          data: dataStr,
          success: (res) => {
            if (res && res.length > 0) {
              if (type == 0) {
                for (let i = 0; i < res.length; i++) {
                  this.dictList.push(res[i])
                }
              } else {
                this.dictList = res
              }
              if (res.length < 7) {
                this.noData = true
              } else {
                this.noData = false
              }
            } else {
              //如果是输入框搜索数据为空的情况下直接清空
              if (type == 1) {
                this.dictList = []
              }
              this.noData = true
            }
            this.dictHeight = this.dictList.length > 6 ? 200 : this.dictList.length * 32 + 50
          }
        })
      },
      searchDictByText() {
        let dictList = this.dictList
        let dictText = this.dictText
        let newDictList = [];
        for (let i = 0; i < dictList.length; i++) {
          let value = dictList[i].value;
          let text = dictList[i].text;
          if (value.indexOf(dictText) >= 0 || value === dictText) {
            newDictList.push(dictList[i])
          }else if(text.indexOf(dictText) >= 0 || text === dictText){
            newDictList.push(dictList[i])
          }
        }
        this.dictList = newDictList;
        //update-begin---author:wangshuai ---date:20220315  for：下拉单选样式出现混乱--------
        this.dictHeight = this.dictList.length > 6 ? 200 : this.dictList.length * 32 + 50
        //update-end---author:wangshuai ---date:20220315  for：下拉单选样式出现混乱--------
      },
      /**
       * 点击叉数据清空
       */
      handleIconClick(){
        this.dictText=""
	      this.handleOnChange()
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

    .scoll-class {
        position: absolute;
        background: white;
        z-index: 9999;
        width: 100%;
        overflow: hidden;
        border: 1px solid #dddee1;
        border-radius: 4px;
        top: 35px;
        -webkit-box-shadow: 0 1px 6px rgba(0, 0, 0, 0.2);
        box-shadow: 0 1px 6px rgba(0, 0, 0, 0.2);
    }

    .scoll-list-class {
        cursor: pointer;
        z-index: 9999;
    }

    .scoll-list-class li span {
        margin-left: 15px;
        font-size: 14px;
    }

    .scoll-list-class :hover {
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
</style>