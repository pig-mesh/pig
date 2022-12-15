<script type="text/x-template" id="data-dictionary">
  <div class="dictClass">
    <Modal
            :loading="loading"
            width="100%"
            v-model="dictShow"
            :title="moduleTitle"
            fullscreen=true
            :closable="true"
            :mask-closable="false">
      <div slot="footer">
        <i-button @click="close">关闭</i-button>
      </div>
        <#--查询-->
      <div style="margin-top: 10px;display: flex;width: 100%">
        <i-form style="width: 100%"  ref="queryParam" :model="queryParam" :label-width="80">
          <row>
            <i-col span="4">
              <form-item label="字典名称" prop="dictName">
                <i-input v-model="queryParam.dictName" placeholder="请填写字典名称"></i-input>
              </form-item>
            </i-col>
            <i-col span="4">
              <form-item label="字典编号" prop="dictCode">
                <i-input v-model="queryParam.dictCode" placeholder="请填写字典编号"></i-input>
              </form-item>
            </i-col>
            <i-col span="4" style="margin-left: 20px">
              <i-button type="primary" @click="loadData(1)">查询</i-button>
              <i-button @click="resetParam">清空</i-button>
            </i-col>
          </row>
        </i-form>
      </div>
        <#--begin字典-->
      <div style="margin-top: 10px">
        <i-button type="primary" @click="dictClick">添加</i-button>
        <i-button icon="ios-loading" type="primary" @click="dictReflesh">刷新缓存</i-button>
        <i-button v-if="dictDeleteShow==true" type="error" @click="dictDeleteBatch">删除</i-button>
      </div>
      <div style="margin-top: 10px">
        <i-table @on-selection-change="dictTableSelect" border stripe :columns="dictData.columns" :data="dictData.data"
                 style="margin-top: 1%;">
          <template slot-scope="{ row, index }" slot="action">
            <i-button type="primary" size="small"  @click="editDict(row)">编辑</i-button>
            <i-button type="primary" size="small"  @click="dictConfig(row.id)">字典配置</i-button>
            <Poptip
                    confirm
                    placement="left"
                    title="确定要删除吗?"
                    :transfer="true"
                    @on-ok="dictRemove(row)">
              <i-button type="error" size="small" >删除</i-button>
            </Poptip>

          </template>
        </i-table>
        <div class="page">
          <Page
                  :total="dictData.page.total"
                  show-total
                  show-elevator
                  @on-change="handleCurrentChange"
                  @on-page-size-change="handleSizeChange">
          </Page>
        </div>
      </div>
        <#--字典编辑和新增 begin-->
      <Modal
              width="36%"
              v-model="createDictShow"
              :title="dictTitle"
              :mask-closable="false"
              :closable="false"
      >
        <div slot="footer">
          <i-button @click="cancelReset('dictModel')">取消</i-button>
          <i-button :loading="createDictLoading" type="primary" @click="createDictOk('dictModel')">确定</i-button>
        </div>
        <i-form ref="dictModel" :model="dictModel" :rules="dictModelRule" :label-width="100" class="dict">
          <form-item label="字典名称:" prop="dictName">
            <i-input v-model="dictModel.dictName" placeholder="请填写字典名称"/>
          </form-item>
          <form-item label="字典编码:" prop="dictCode">
            <i-input v-model="dictModel.dictCode" placeholder="请填写字典编码"/>
          </form-item>
          <form-item label="描述:" prop="description">
            <i-input v-model="dictModel.description"/>
          </form-item>
        </i-form>
      </Modal>
        <#--字典编辑和新增 end-->
        <#--回收站 begin-->
      <Modal
              width="50%"
              v-model="recycleBinShow"
              title="回收站"
              :mask-closable="false"
              :closable="false"
      >
        <div slot="footer">
          <i-button @click="recycleBinReset">关闭</i-button>
        </div>
        <div style="margin-top: 10px">
          <i-table border stripe :columns="recycleBin.columns" :data="recycleBin.data"
                   style="margin-top: 1%;">
            <template slot-scope="{ row, index }" slot="action">
              <i-button type="primary" size="small" style="cursor: pointer" @click="recycleBinRetrieve(row.id)">取回</i-button>
              <Poptip
                      confirm
                      placement="left"
                      title="确定要彻底删除吗"
                      content="删除之后不可取回"
                      :transfer="true"
                      @on-ok="recycleBinDelete(row.id)">
              <i-button type="error" size="small">彻底删除</i-button>
              </Poptip>
            </template>
          </i-table>
        </div>
      </Modal>
        <#--回收站 end-->
        <#--end字典-->
        <#--字典配置页面 begin-->
      <Drawer :transfer="true" class="dictDrawer" width="500" title="字典列表"  v-model="itemDrawer">
        <div style="margin-top: 10px">
          <i-button type="primary" @click="dictItemClick">添加</i-button>
        </div>
        <div style="margin-top: 10px">
          <i-table border stripe
                   :columns="dictItemList.columns"
                   :data="dictItemList.data"
                   :rowClassName="getRowClassname"
                   style="margin-top: 1%;">
            <template slot-scope="{ row, index }" slot="action">
              <i-button type="primary" size="small" @click="editItemDict(row)">编辑</i-button>
              <Poptip
                      confirm
                      placement="left"
                      title="确定要删除吗?"
                      :transfer="true"
                      @on-ok="dictItemRemove(row)">
                <i-button type="error" size="small">删除</i-button>
              </Poptip>

            </template>
          </i-table>
          <div class="page">
            <Page
                    :total="dictItemList.page.total"
                    show-total
                    show-elevator
                    @on-change="handleItemCurrentChange"
                    @on-page-size-change="handleItemSizeChange">
            </Page>
          </div>
        </div>
      </Drawer>
      <#--字典详细新增和编辑 begin-->
      <Modal
              width="50%"
              v-model="createDictItemShow"
              :title="dictItemTitle"
              class-name="dictItem"
              :closable="false"
              :mask-closable="false"
      >
        <div slot="footer">
          <i-button @click="cancelItemReset('dictItemModel')">取消</i-button>
          <i-button :loading="createDictItemLoading" type="primary" @click="createItemDictOk('dictItemModel')">确定
          </i-button>
        </div>
        <i-form ref="dictItemModel" :model="dictItemModel" :rules="dictItemModelRule" :label-width="100" class="dict">
          <form-item label="名称:" prop="itemText">
            <i-input v-model="dictItemModel.itemText" placeholder="请填写名称"/>
          </form-item>
          <form-item label="数据值:" prop="itemValue">
            <i-input v-model="dictItemModel.itemValue" placeholder="请填写数据值"/>
          </form-item>
          <form-item label="描述:" prop="description">
            <i-input v-model="dictItemModel.description"/>
          </form-item>
          <form-item label="排序值:" prop="sortOrder">
            <input-number :step="1" v-model="dictItemModel.sortOrder" :min="1"/>
          </form-item>
          <form-item label="是否启用:" prop="status">
            <i-switch v-model="dictItemModel.status" :true-value="1" :false-value="0"/>
          </form-item>
        </i-form>
      </Modal>
        <#--字典详细新增和编辑 end-->
        <#--字典配置页面 end-->
    </Modal>
  </div>
  </div>
</script>
<script>
  Vue.component('j-data-dictionary', {
    template: '#data-dictionary',
    data() {
      return {
        moduleTitle: "数据字典",
        loading: false,
        dictShow: false,
        dictData: {
          data: [],
          columns: [
            {
              type: 'selection',
              width: 60,
              align: 'center'
            },
            {
              title: '字典名称',
              key: 'dictName',
              align: 'center'
            },
            {
              title: '字典编号',
              key: 'dictCode',
              align: 'center'
            },
            {
              title: '描述',
              key: 'description',
              align: 'center'
            },
            {
              title: '操作',
              key: 'action',
              width: 200,
              align: 'center',
              slot: 'action'
            },
          ],//字典集合
          page: { //分页参数
            page: 1,
            size: 10,
            total: 0,
          },
        },
        dictItemList: {
          data: [],
          columns: [
            {
              title: '名称',
              key: 'itemText',
              align: 'center'
            },
            {
              title: '数据值',
              key: 'itemValue',
              align: 'center'
            },
            {
              title: '操作',
              key: 'action',
              width: 150,
              align: 'center',
              slot: 'action'
            },
          ],//字典集合
          page: { //分页参数
            page: 1,
            size: 10,
            total: 0,
          },
        },//字典详细集合
        recycleBin: {
          data: [],
          columns: [
            {
              type: 'selection',
              width: 60,
              align: 'center'
            },
            {
              title: '字典名称',
              key: 'dictName',
              align: 'center'
            },
            {
              title: '字典编号',
              key: 'dictCode',
              align: 'center'
            },
            {
              title: '描述',
              key: 'description',
              align: 'center'
            },
            {
              title: '操作',
              key: 'action',
              width: 150,
              align: 'center',
              slot: 'action'
            },
          ],
        },//回收站
        createDictShow: false,
        dictTitle: "添加字典",
        dictItemTitle: "新增",
        dictModel: {
          id: "",
          dictName: "",
          dictCode: "",
          description: ""
        }, //新增字典集合
        dictItemModel: {
          id: "",
          itemText: "",
          itemValue: "",
          description: "",
          sortOrder: 1,
          status: 1,
        },
        dictItemModelRule: {
          itemText: [
            {required: true, message: '请输入名称', trigger: 'blur'}
          ],
          itemValue: [
            {required: true, message: '请输入数据值', trigger: 'blur'}
          ],
        },
        dictModelRule: {
          dictName: [
            {required: true, message: '请输入字典名称', trigger: 'blur'}
          ],
          dictCode: [
            {required: true, message: '请输入字典编码', trigger: 'blur'}
          ],
        },//字典验证
        createDictLoading: false,
        //批量删除按钮是否显示
        dictDeleteShow: false,
        //批量删除选中的数据
        dictSelectData: [],
        //抽屉显示事件
        itemDrawer: false,
        dictId: "", //字典id
        createDictItemShow: false, //字典详细是否显示
        createDictItemLoading: false,
        recycleBinShow:false, //回收站显示
        queryParam:{} //查询条件
      }
    },
    created() {
      this.loadData()
    },
   watch: {
     dictShow: {
            deep: true,
            immediate: true,
            handler: function (val){
            if(!val){
              this.itemDrawer=false
              this.createDictShow=false
              this.createDictItemShow=false
            }
        }
      },
    },
    methods: {
      loadData(arg) {
        //加载数据列表
        let that = this;
        let data = {}
        //update-begin---author:wangshuai ---date:20220720  for：[VUEN-1657]积木报表的字典查询，不好使输入 sex 或者性别都查不到数据------------
        if(arg){
            data.pageNo = arg
        }else{
            data.pageNo = that.dictData.page.page
        }
        //update-end---author:wangshuai ---date:20220720  for：[VUEN-1657]积木报表的字典查询，不好使输入 sex 或者性别都查不到数据--------------
        data.pageSize = that.dictData.page.size
        if(this.queryParam.dictCode){
          data.dictCode=this.queryParam.dictCode
        }
        if(this.queryParam.dictName){
          data.dictName=this.queryParam.dictName
        }
        $http.get({
          url: api.dictList,
          data: data,
          success: (res) => {
            that.dictData.data = res.records;
            that.dictData.page.size = res.size;
            that.dictData.page.total = res.total;
          }
        });
      },
      resetParam(){
        this.queryParam={}
        //update-begin---author:wangshuai ---date:20220720  for：[VUEN-1657]积木报表的字典查询，不好使输入 sex 或者性别都查不到数据------------
        this.loadData(1)
        //update-end---author:wangshuai ---date:20220720  for：[VUEN-1657]积木报表的字典查询，不好使输入 sex 或者性别都查不到数据------------
      },
      loadItemData() {
        //加载数据列表
        let that = this;
        $http.get({
          url: api.dictItemList,
          data: {
            pageNo: that.dictItemList.page.page,
            pageSize: that.dictItemList.page.size,
            dictId: this.dictId
          },
          success: (res) => {
            that.dictItemList.data = res.records;
            that.dictItemList.page.size = res.size;
            that.dictItemList.page.total = res.total;
          }
        });
      },
      handleSizeChange(val) {
        this.dictData.page.size = val;
        this.loadData();
      },
      handleCurrentChange(val) {
        this.dictData.page.page = val;
        this.loadData();
      },
      //字典新增
      dictClick() {
        this.moduleTitle = "添加字典"
        this.createDictShow = true
      },
      //字典添加和修改确定操作
      createDictOk(name) {
        let that = this
        this.$refs[name].validate((valid) => {
          if (valid) {
            this.createDictLoading = true
            let url = ""
            if (that.dictModel.id) {
              url = api.dictEdit
            } else {
              url = api.dictAdd
            }
            $http.post({
              url: url,
              contentType: 'json',
              data: JSON.stringify(that.dictModel),
              success: (res) => {
                this.createDictShow = false
                that.dictModel = {};
                this.loadData()
              },
              finally: () => {
                this.createDictLoading = false
              }
            })

          }
        })
      },
      createItemDictOk(name) {
        let that = this
        this.$refs[name].validate((valid) => {
          if (valid) {
            this.createDictItemLoading = true
            let url = ""
            if (that.dictItemModel.id) {
              url = api.dictItemEdit
            } else {
              url = api.dictItemAdd
            }
            that.dictItemModel.dictId = this.dictId
            if (!that.dictItemModel.sortOrder) {
              that.dictItemModel.sortOrder = 1
            }
            if (that.dictItemModel.status ==undefined) {
              that.dictItemModel.status = 1
            }
            console.log(that.dictItemModel);
            $http.post({
              url: url,
              contentType: 'json',
              data: JSON.stringify(that.dictItemModel),
              success: (res) => {
                this.createDictItemShow = false
                that.dictItemModel = {};
                this.loadItemData()
              },
              finally: () => {
                this.createDictItemLoading = false
              }
            })

          }
        })
      },
      //表单验证情况清空
      cancelReset(name) {
        this.$refs[name].resetFields();
        this.createDictShow = false
        this.dictModel = {};
        this.loadData()
      },
      cancelItemReset(name) {
        this.$refs[name].resetFields();
        this.createDictItemShow = false
        this.dictItemModel = {};
        this.loadItemData()
      },
      //字典编辑
      editDict(row) {
        this.moduleTitle = "修改字典"
        this.dictModel = row
        this.createDictShow = true
      },
      //字典删除
      dictRemove(row) {
        let id = row.id;
        $http.del({
          url: api.dictDelete,
          data: {"id": id},
          success: (res) => {
            this.loadData()
          }
        })
      },
      //字典详情删除
      dictItemRemove(row) {
        let id = row.id;
        $http.del({
          url: api.dictItemDelete,
          data: {"id": id},
          success: (res) => {
            this.loadItemData()
          }
        })
      },
      //复选框选中事件
      dictTableSelect(selection) {
        if (selection.length > 0) {
          this.dictDeleteShow = true
          this.dictSelectData = selection
        } else {
          this.dictDeleteShow = false
        }
      },
      //批量删除点击事件
      dictDeleteBatch() {
        let dictSelectData = this.dictSelectData
        let ids = ""
        for (const dictSelectDatum of dictSelectData) {
          let id = dictSelectDatum.id;
          ids = ids + id + ","
        }
        if (ids.length > 1) {
          ids = ids.substr(0, ids.lastIndexOf(","))
        }
        this.$Modal.confirm({
          title: "批量删除",
          content: "确定要删除吗",
          closable: true,
          onOk: (res) => {
            $http.del({
              url: api.dictDeleteBatch,
              data: {"ids": ids},
              success: (res) => {
                this.loadData()
              }
            })
          },
        });
      },
      //字典配置选项
      dictConfig(id) {
        this.itemDrawer = true
        this.dictId = id
        this.loadItemData()
      },
      handleItemSizeChange(val) {
        this.dictItemList.page.size = val;
        this.loadItemData()
      },
      handleItemCurrentChange(val) {
        this.dictItemList.page.page = val;
        this.loadItemData()
      },
      //字典详细添加
      dictItemClick() {
        this.dictItemModel = {}
        this.dictItemTitle = "新增"
        this.dictItemModel.status = 1
        this.createDictItemShow = true
      },
      //字典详情编辑
      editItemDict(row) {
        console.log(row)
        this.dictItemTitle = "修改"
        this.dictItemModel = row
        this.createDictItemShow = true
      },
      close() {
        this.dictShow = false
        this.resetParam()
      },
      getRowClassname(row) {
        if (row.status == 0) {
          return "data-rule-invalid"
        }
      },
      //刷新缓存
      dictReflesh() {
        let that = this
        $http.get({
          url: api.refleshCache,
          success: (res) => {
            that.$Message.success(res);
          }
        })
      },
      //回收站点击按钮事件
      recycleBinClick(){
        this.recycleBinShow = true
        this.loadRecycleBin();
      },
      //回收站关闭事件
      recycleBinReset(){
        this.recycleBinShow = false
      },
      //加载回收站数据
      loadRecycleBin(){
        $http.get({
          url:api.deleteList,
          success: (res) => {
           this.recycleBin.data = res
          }
        })
      },
      //回收站字典取回
      recycleBinRetrieve(id){
        $http.post({
          url: api.back,
          contentType: 'json',
          data: {"id": id},
          success: (res) => {
            this.loadRecycleBin()
            this.loadData()
          }
        }) 
      },
      //回收站字典彻底删除
      recycleBinDelete(id){
        $http.del({
          url:api.thoroughDelete,
          data: {"id": id},
          success: (res) => {
            this.loadRecycleBin()
          }
        })
      }
    }
  })
</script>
<style>
    .dict .ivu-form-item {
        margin-bottom: 24px !important;
    }

    .ivu-drawer-mask {
        z-index: 2000 !important;
    }

    .ivu-drawer-wrap {
        z-index: 2000 !important;
    }

    .dictItem {
        z-index: 2025 !important;
    }

    .ivu-poptip-popper {
        z-index: 9999 !important;
    }

    .data-rule-invalid {
        background: #f4f4f4;
        color: #bababa;
    }

    .ivu-table-stripe .ivu-table-body tr:nth-child(2n) td {
        background-color: #ffffff;
    }

    .ivu-btn-small {
        font-size: 12px;
    }
</style>