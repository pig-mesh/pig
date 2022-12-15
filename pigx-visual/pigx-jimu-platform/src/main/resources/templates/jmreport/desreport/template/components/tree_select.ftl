<style>
    .jmreport-comp .ivu-select-placeholder {
        display: block;
        height: 30px;
        line-height: 30px;
        color: #c5c8ce;
        font-size: 14px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        padding-left: 8px;
        padding-right: 22px;
    }

    .jmreport-comp .ivu-select-selected-value {
        display: block;
        height: 30px;
        line-height: 30px;
        font-size: 14px;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
        padding-left: 8px;
        padding-right: 24px;
    }
</style>
<script type="text/x-template" id="tree-select-template">
    <div class="ivu-select ivu-select-default jmreport-comp">
        <div tabindex="0" class="ivu-select-selection ivu-form-item-content" >
            <div @mouseover="mouseover" @mouseleave="mouseleave">
                <div @click="treeSelectContentShow" style="line-height: 23px;">
                    <div v-for="(item,index) in multipleShowVal" v-if="index<2" :key="item" class="ivu-tag ivu-tag-checked">
                        <span class="ivu-tag-text ">{{item}}</span>
                        <i class="ivu-icon ivu-icon-ios-close" @click.stop="removeVal(index)"></i>
                    </div>
                    <span v-if="multipleShowVal && multipleShowVal.length>2">
                        +{{ multipleShowVal.length - 2 }}
                    </span>

                    <span v-if="multipleShowVal.length === 0" class="ivu-select-placeholder">请选择</span>
                    <span class="" style="display: none;"></span>
                </div>
                <i :class="'ivu-icon ivu-icon-' +iconVal+ ' ivu-select-arrow'" @click="clickIcon"></i>
            </div>

            <div v-show="showTree" @mouseover="mouseover1" @mouseleave="mouseleave1" class="ivu-select-dropdown" style="max-height: 200px;overflow-y:auto;z-index:9999;min-width:100%;width: auto">
                <#--<div style="width: 95%;margin-left: 10px;" v-show="showQuery">
                    <i-input v-model="queryTreeVal" placeholder="请输入筛选条件"  @on-change="selectTreeChange" />
                </div>-->
                <div style="margin-left: 10px;">
                    <Tree
                        show-checkbox
                        check-strictly
                        :load-data="loadData"
                        :data="queryData"
                        :multiple="multiple"
                        ref="tree"
                        @on-check-change="treeCheckChange"
                        @on-select-change="selectChange">
                    </Tree>
                </div>
            </div>

        </div>
    </div>
</script>
<script>
    Vue.component('j-tree-select', {
        template: '#tree-select-template',
        props: {
            treeData: {
                type: Array
            },
            value:  {
                type: String
            },
            url: {
                type: String
            },
            loadtreeurl: { 
                type: String
		        }
        },
        data(){
            return {
                filterable: true,
                multiple: true,
                clearable: true,
                showQuery: true,
                disabled: true,
                iconVal: 'ios-arrow-down',
                showTree: false,
                multipleShowVal: [],
                multipleHideVal: [],
                showData:[],
                queryTreeVal: '',
                queryData: [],
                inputOver: false,
                treeOver: false,
                initValues:[] //初始化value值

            }
        },
        watch:{
            value:{
                immediate: true,
                handler: function(){
                    this.initTreeSelected()
                }
            }
        },
        mounted(){
            this.loadRoot();
        },
        methods:{
            combineTreeData(ls, callback, node){
                let arr = []
                if(ls && ls.length> 0){
                    for(let item of ls){
                        arr.push({
                            title: item.title,
                            id: item.id,
                            value: item.value,
                            loading: false,
                            children: []
                        })
                    }
                }
                
                //update-begin---author:wangshuai ---date:20220627  for：[issues/965]报表下钻时返回上一页下拉树参数回显有问题------------
                //如果没有配置通过value值获取树集合，那么就不走孩子选中时间
		            if(this.loadtreeurl){
                    this.checkChildTreeData(arr,this.initValues)
                }
                //update-end---author:wangshuai ---date:20220627  for：[issues/965]报表下钻时返回上一页下拉树参数回显有问题--------------
		            
                if(callback){
                    callback(arr)
                }else{
                    this.queryData = [...arr]
                }
                if(arr.length==0){
                    delete node.children
                    delete node.loading
                }
               // console.log('this.queryData ', this.queryData)
            },
            // 加载子节点
            loadData(item, callback){
                // console.log("====")
                // console.log("loadData")
                // console.log("====")
                let params = {
                    params: {pid: item.id}
                }
                $http.metaGet(this.url, params).then((res) => {
                    this.combineTreeData(res.data, callback, item)
                }).catch(function (error){
                    console.log('加载树错误', error)
                });
            },
            // 加载数据
            loadRoot(){
                // console.log("====")
                // console.log("loadRoot")
                // console.log("====")
                $http.metaGet(this.url).then((res) => {
                    this.combineTreeData(res.data)
                }).catch(function (error){
                  console.log('加载树错误', error)
                });
            },
            initTreeSelected(){
                // 如果有默认值  需要设置该方法
                // console.log('initTreeSelected', this.value)
                if(!this.value){
                    let arr = this.queryData;
                    this.clearTreeSelect(arr)
                    this.multipleShowVal = []
                    this.multipleHideVal = []
                    // 勾选状态去不掉
                    this.queryData = JSON.parse(JSON.stringify(arr))
                }else{
                    //update-begin---author:wangshuai ---date:20220627  for：[issues/965]报表下钻时返回上一页下拉树参数回显有问题------------
                    //如果没有配置通过值获取数集合，那么就不走请求接口，兼容老数据
		                if(this.loadtreeurl){
                        this.getTreeName(this.value);
                    }
                    //update-end---author:wangshuai ---date:20220627  for：[issues/965]报表下钻时返回上一页下拉树参数回显有问题------------
                }
            },
            clearTreeSelect(arr){
                for (let i = 0; i < arr.length; i++) {
                    arr[i].selected = false
                    arr[i].checked = false
                    if (arr[i].children) {
                        this.clearTreeSelect(arr[i].children)
                    }
                }
            },
            mouseover () {
                this.inputOver = true;
            },
            mouseleave () {
                this.inputOver = false;
                setTimeout(()=>{
                    if(this.treeOver===false){
                        this.showTree = false
                    }
                }, 200)
            },
            mouseover1 () {
                this.treeOver = true;
            },
            mouseleave1 () {
                this.treeOver = false;
                setTimeout(()=>{
                    if(this.inputOver===false){
                        this.showTree = false
                    }
                }, 200)
            },
            treeSelectContentShow(){
                if (this.showTree) {
                    this.showTree = false
                } else {
                    this.showTree = true
                }
                if (this.iconVal !== 'ios-close-circle') {
                    if (this.iconVal === 'ios-arrow-down') {
                        this.iconVal = 'ios-arrow-up'
                    } else if (this.iconVal === 'ios-arrow-up') {
                        this.iconVal = 'ios-arrow-down'
                    }
                }
            },
            removeVal (index) {
                let arr = this.queryData;
                let removeValue = this.multipleHideVal[index]
                for (let i = 0; i < arr.length; i++) {
                    if (arr[i].value === removeValue) {
                        arr[i].selected = false
                        arr[i].checked = false
                    } else if (arr[i].children) {
                        this.removeChildTreeData(arr[i].children, removeValue)
                    }
                }
                this.multipleShowVal.splice(index, 1)
                this.multipleHideVal.splice(index, 1)

                // update-begin-author:taoyan date:20211028 for:下拉树控件在多选的时候，如果选错了，察掉后不会及时生效，必须要重置才行。
                //https://gitee.com/jeecg/JimuReport/issues/I4FKR0
                let emitString = this.multipleHideVal.join(',')
                this.$emit('input', emitString)
                this.$emit('on-change', emitString)
                // update-end-author:taoyan date:20211028 for:下拉树控件在多选的时候，如果选错了，察掉后不会及时生效，必须要重置才行。

            },
            removeChildTreeData(children, removeValue) {
                for (let i = 0; i < children.length; i++) {
                    if (children[i].value === removeValue) {
                        children[i].selected = false
                        children[i].checked = false
                        return
                    } else if (children[i].children && children[i].children.length > 0) {
                        this.removeChildTreeData(children[i].children, removeValue)
                    }
                }
            },
            clickIcon () {
                if (this.iconVal === 'ios-close-circle') {
                    this.clearVal()
                } else {
                    this.treeSelectContentShow()
                }
            },
            clearVal () {
                // console.log('clearVal')
            /*    if (this.clearable && !this.multiple && this.iconVal === 'ios-close-circle') {
                    this.pickTree(this.hideValue)
                    this.queryVal = ''
                    this.hideValue = ''
                    if (this.showTree) {
                        this.iconVal = 'ios-arrow-up'
                    } else {
                        this.iconVal = 'ios-arrow-down'
                    }
                    this.$emit('input', '')
                }*/
            },

            selectTreeChange () {
                this.queryData = JSON.parse(JSON.stringify(this.showData))
                if (this.queryTreeVal !== '') {
                    let removeData = []
                    for (let i = 0; i < this.queryData.length; i++) {
                        let check = this.recursionTreeData(this.queryTreeVal, this.queryData[i])
                        if (!check) {
                            removeData.push(i)
                        }
                    }
                    this.queryData = this.queryData.filter((o, index) => {
                        return !removeData.includes(index)
                    })
                }
            },

            recursionTreeData (query, data) {
                let isCheck = false
                let removeData = []
                // 只验证最底层的节点是否有符合要求的值
                if (data.children !== undefined) {
                    for (let i = 0; i < data.children.length; i++) {
                        if (!isCheck) {
                            isCheck = this.recursionTreeData(query, data.children[i])
                            if (!isCheck) {
                                removeData.push(i)
                            }
                        } else {
                            if (!this.recursionTreeData(query, data.children[i])) {
                                removeData.push(i)
                            }
                        }
                    }
                } else {
                    // 验证当前节点是否有符合要求的值
                    if (data.title.indexOf(query) !== -1) {
                        return true
                    } else {
                        return false
                    }
                }
                data.children = data.children.filter((o, index) => {
                    return !removeData.includes(index)
                })
                // 如果子节点中有一个符合要求，则父节点就直接返回true，不做删除，若子节点没有一个符合要求，则再次验证当前的节点
                if (isCheck) {
                    return isCheck
                }
                // 表示当前的节点有值在里面
                if (data.title.indexOf(query) !== -1) {
                    return true
                } else {
                    return false
                }
            },
            selectChange (obj) {
                console.log('selectChange', obj)
            },
            treeCheckChange(arr){
                console.log('treeCheckChange', arr)
                this.multipleShowVal = []
                this.multipleHideVal = []
                for (let i = 0; i < arr.length; i++) {
                    this.multipleShowVal.push(arr[i].title)
                    this.multipleHideVal.push(arr[i].value)
                }
                let emitString = this.multipleHideVal.join(',')
                this.$emit('input', emitString)
                this.$emit('on-change', emitString)
            },
		        
            /**
             * 获取树名称
             * @param value
             */
            getTreeName(value){
                let params = {
                    params: {value: value}
                }
                $http.metaGet(this.loadtreeurl, params).then((res) => {
                    if(res.data && res.data.length> 0){
                        let initValues = [];
                        for (let i = 0; i <  res.data.length; i++) {
                            if(res.data[i].value){
                                if(this.multipleShowVal.indexOf(res.data[i].title) === -1){
                                    this.multipleShowVal.push(res.data[i].title) 
                                }
                                initValues.push(res.data[i].value)
                            }
                        }
                        //如果有值默认选中
		                    this.setChecked(initValues);
                        this.initValues = initValues
                    }
                }).catch(function (error){
                    console.log('获取树名称失败', error)
                });
            },
		        
            /**
             * 设置选中值
             * @param children
             * @param removeValue
             */
            setChecked(values){
                let arr = this.queryData;
                for (let i = 0; i < arr.length; i++) {
                    if (values.indexOf(arr[i].value)!==-1 && !values.checked) {
                        Vue.set(arr[i],"checked",true)
                    } else {
                        Vue.set(arr[i],"checked",false)
                    }
                }
            },
		        
            /**
             * 子级设置选中值
             * @param children
             * @param checkedValue
             */
            checkChildTreeData(children, checkedValue) {
                for (let i = 0; i < children.length; i++) {
                    if (checkedValue.indexOf(children[i].value)!==-1  && !checkedValue.checked) {
                        Vue.set(children[i],"checked",true)
                    }else{
                        Vue.set(children[i],"checked",false)
                    }
                }
            }

        }
    })
</script>