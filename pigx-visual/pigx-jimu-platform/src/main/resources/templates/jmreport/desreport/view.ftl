<#assign CACHE_VERSION = "v=1.0.13">
<#assign config_id = "${id!''}">
<#assign shareView = "${shareView}">
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width">
<title></title>
<script>
/**
* 获取url参数
*/
function getLocalRequestUrl() {
    var url = location.search;
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for(var i = 0; i < strs.length; i++) {
            theRequest[strs[i].split("=")[0]]=decodeURI(strs[i].split("=")[1]);
        }
    }
    return theRequest;
}
</script>
<script>
    let configId= '${config_id}';
    var reportMode = "${mode!''}"
    let base = '${base}';
    let customPrePath = '${customPrePath}';
    let baseFull = "${base}"+"${customPrePath}";
    let shareView = "${shareView}";
    let token = getLocalRequestUrl().token;
    if(token && token!=null && token!=""){
        window.localStorage.setItem('JmReport-Access-Token',token);
    }
    if (token == "" || token == null){
        token = window.localStorage.getItem('JmReport-Access-Token');
    }
    /**
     * 获取后台配置的报表配置
     */
    function getReportConfigJson() {
        let str = '${reportConfig}';
        return JSON.parse(str)
    }
</script>
<#include "./common/resource.ftl">
<#include "./template/components/tree_select.ftl">
<#include "./template/view.ftl">
<!-- Import via CDN -->
<link rel="stylesheet" href="${base}${customPrePath}/jmreport/desreport_/corelib/jmsheet.css?${CACHE_VERSION}">
<script src="${base}${customPrePath}/jmreport/desreport_/corelib/jmsheet.js?${CACHE_VERSION}"></script>
<script src="${base}${customPrePath}/jmreport/desreport_/corelib/locale/zh-cn.js?${CACHE_VERSION}"></script>
<script src="${base}${customPrePath}/jmreport/desreport_/jquery/jquery-3.4.1.min.js"></script>
<link rel="shortcut icon" href="${base}${customPrePath}/jmreport/desreport_/corelib/logo.png?${CACHE_VERSION}" type="image/x-ico">
<script>
    // $http.get({
    //     url : api.show,
    //     data:{"id":configId},
    //     success : function(result) {
    //         document.title = result.name;
    //     }
    // });
</script>
<style>
  #jm-sheet-wrapper * {
    color: #000000;
    -webkit-tap-highlight-color: #000000!important;
   }
    body{
        overflow-y:hidden !important;
    }


    /*--查询区域的样式设置 --*/
  .ty-bar-container{
      padding-left: 12px;
  }

  .jm-query-collapse .ivu-collapse-header .ivu-icon{
      font-size: 20px;
      font-weight: 700;
      margin-right: 5px !important;
  }

  .jm-query-form .ivu-input{
      padding: 3px 7px;
      height: 28px;
      border-radius: 3px;
      width: 100% !important;
  }

  .jm-query-form .jmreport-query-datetime .ivu-input{
      width: 160px;
  }

  .jm-query-form .ivu-select-selection{
      width: 200px;
  }

  /*--多选 --*/
  .jm-query-form .ivu-select-multiple .ivu-select-selection{
      width: 200px;
  }
  
  /*多选输入框鼠标位置显示问题*/
  /*.jm-query-form .ivu-select-multiple .ivu-tag{*/
  /*    height: 20px;*/
  /*    line-height: 20px;*/
  /*    margin: 3px 4px 3px 0;*/
  /*    vertical-align: baseline;*/
  /*    max-width: 62% !important;*/
  /*}*/
  
  .jm-query-form .ivu-select-multiple .ivu-tag i{
      top: 3px;
  }

  .jm-query-form  .ivu-btn{height:30px !important;}
  .jm-query-form  .ivu-btn>.ivu-icon{
      font-size:16px;
  }


  .jm-query-form .ivu-select-selection,
  .jm-query-form .ivu-select-placeholder,
  .jm-query-form .ivu-select-selected-value{
      height: 28px !important;
      line-height: 24px !important;
  }

  .jm-query-form .ivu-input-prefix i,
  .jm-query-form .ivu-input-suffix i{
      line-height: 28px !important;
  }
  /*--查询区域的样式设置 --*/

  .jm-search-btns .ivu-form-item-content{
      margin-left: 30px !important;
  }
  .jm-query-form .ivu-select-dropdown{
      z-index: 99999;
  }
  [v-cloak]{
      display: none;
  }
  .jm-select-box{
      width: 200px
  }  
  
  .jm-select-box .ivu-select-input{
      height: 28px
  }
</style>
<body onload="view.load('${config_id}')" style="overflow: hidden">
<div id="app" style="overflow: hidden" v-cloak>
    <!-- 查询条件 -begin -->
    <div v-if="configQueryList && configQueryList.length>0">
        <Collapse class="jm-query-collapse" @on-change="onQueryAreaSwitch" v-model="queryPanel">
            <Panel name="1">
                <span style="color: #000000;" title="点击展开显示查询信息">查 询 栏</span>
                <div slot="content">
                    <i-form ref="queryForm" :model="queryInfo" inline :label-width="100" class="jm-query-form" @keydown.native.enter.prevent="doReportQuery">
                        <template v-for="(item, index) in configQueryList">
                            <form-item :label="getQueryItemLabel(item)" :index="index">

                                <!-- 日期选择器 yyyy-MM-dd HH:mm:ss -->
                                <template v-if="item.type=='date'">
                                    <Row v-if="item.mode==2" :class="'jmreport-query-'+item.realType">
                                        <i-col span="11">
                                            <date-picker :ref="item.key+'_begin'" @on-change="(str)=>handleQueryDateChange(str, item.key+'_begin')" :type="item.realType" :format="item.format" :transfer="true" v-model="queryInfo['onlyshow_'+item.key+'_begin']" class="jm-select-box" :placeholder="'请选择起始值'"></date-picker>
                                        </i-col>
                                        <i-col span="2" style="text-align: center">&nbsp;~</i-col>
                                        <i-col span="11">
                                            <date-picker :ref="item.key+'_end'" @on-change="(str)=>handleQueryDateChange(str, item.key+'_end')" :type="item.realType" :format="item.format" :transfer="true" v-model="queryInfo['onlyshow_'+item.key+'_end']" class="jm-select-box" :placeholder="'请选择结束值'"></date-picker>
                                        </i-col>
                                    </Row>
                                    <date-picker v-else :ref="item.key" :type="item.realType" class="jm-select-box" :transfer="true" :format="item.format" :class="'jmreport-query-'+item.type" v-model="queryInfo['onlyshow_'+item.key]" @on-change="(str)=>handleQueryDateChange(str, item.key)" :placeholder="'请选择'+item.title"></date-picker>
                                </template>

                                <!-- 时间选择器 HH:mm:ss -->
                                <template v-else-if="item.type=='time'">
                                    <Row v-if="item.mode==2" :class="'jmreport-query-'+item.realType">
                                        <i-col span="11">
                                            <time-picker :ref="item.key+'_begin'" @on-change="(str)=>handleQueryDateChange(str, item.key+'_begin')" :type="item.realType" :format="item.format" :transfer="true" v-model="queryInfo['onlyshow_'+item.key+'_begin']" class="jm-select-box" :placeholder="'请选择起始值'"></time-picker>
                                        </i-col>
                                        <i-col span="2" style="text-align: center">&nbsp;~</i-col>
                                        <i-col span="11">
                                            <time-picker :ref="item.key+'_end'" @on-change="(str)=>handleQueryDateChange(str, item.key+'_end')" :type="item.realType" :format="item.format" :transfer="true" v-model="queryInfo['onlyshow_'+item.key+'_end']" class="jm-select-box" :placeholder="'请选择结束值'"></time-picker>
                                        </i-col>
                                    </Row>
                                    <time-picker :ref="item.key" :time-picker-options="{disabledHours:true}" v-else :type="item.realType" class="jm-select-box" :transfer="true" :format="item.format" :class="'jmreport-query-'+item.type" v-model="queryInfo['onlyshow_'+item.key]" @on-change="(str)=>handleQueryDateChange(str, item.key)" :placeholder="'请选择'+item.title"></time-picker>
                                </template>

                                <!-- 下拉树 -->
                                <template v-else-if="item.mode==6">
                                    <j-tree-select :ref="item.key" :url="item.loadTree" :loadtreeurl="item.loadTreeByValue" v-model="queryInfo[item.key]"></j-tree-select>
                                </template>

                                <!-- 自定义下拉框 -->
                                <template v-else-if="item.mode==7">
                                    <i-select :ref="item.key" class="jm-select-box" clearable :transfer="true" v-model="queryInfo[item.key]" :placeholder="'请选择'+item.title">
                                        <i-option v-for="(dict, dIndex) in item.dictList" :key="dIndex" :index="index" :value="dict.value">{{ dict.text }}</i-option>
                                    </i-select>
                                </template>

                               <template v-else>
                                   <template v-if="item.dictList && item.dictList.length>0 && (item.mode==4 ||item.mode==3)">
                                       <!-- 多选 -->
                                       <j-select-scroll-multiple v-if="item.mode==3"  v-model="queryInfo['onlyshow_'+item.key]" :item="JSON.stringify(item)"  @dictmultipleok="handleDictMultipleOk" :index="index"></j-select-scroll-multiple>

                                       <!-- 单选 -->
                                       <j-select-scroll-radio v-if="item.mode==4"  v-model="queryInfo[item.key]" :item="JSON.stringify(item)"  @dictok="handleDictOk"></j-select-scroll-radio>
                                   </template>

                                   <!-- 数值查询 -->
                                   <template v-else-if="item.type=='number'">
                                       <Row v-if="item.mode==2">
                                           <i-col span="11">
                                               <i-input :ref="item.key+'_begin'" class="jm-select-box" v-model="queryInfo[item.key+'_begin']" type="number" :placeholder="'请输入起始值'" clearable></i-input>
                                           </i-col>
                                           <i-col span="2" style="text-align: center">&nbsp;~</i-col>
                                           <i-col span="11">
                                               <i-input :ref="item.key+'_end'" class="jm-select-box" v-model="queryInfo[item.key+'_end']" type="number" :placeholder="'请输入结束值'" clearable></i-input>
                                           </i-col>
                                       </Row>
                                       <i-input v-else :ref="item.key" class="jm-select-box" type="number" v-model="queryInfo[item.key]" :placeholder="'请输入'+item.title" clearable></i-input>
                                   </template>

                                   <!-- 默认输入框 模糊查询参数加* -->
                                   <template v-else>
                                       <i-input v-if="item.mode==5" :ref="item.key" @on-change="(e)=>handleLikeQueryChange(e, item.key)" class="jm-select-box" v-model="queryInfo['onlyshow_'+item.key]" :placeholder="'请输入'+item.title" clearable></i-input>
                                       <i-input v-else :ref="item.key" class="jm-select-box" v-model="queryInfo[item.key]" :placeholder="'请输入'+item.title" clearable></i-input>
                                   </template>
                               </template>
                            </form-item>
                        </template>
               
                        <form-item class="jm-search-btns">
                            <i-button type="primary" icon="ios-search-outline" @click="doReportQuery">查询</i-button>
                            <i-button style="margin-left: 8px" icon="ios-redo-outline" @click="resetReportQuery">重置</i-button>
                        </form-item>

                    </i-form>
                </div>
            </Panel>
        </Collapse>
    </div>
    <!-- 查询条件 -end -->
   <#-- <div>
      <a v-if="returnPreviousPage" onclick="returnPreviousPageClick()">返回上一页</a>
    </div>-->
    <div id="jm-sheet-wrapper" style="width:100%;height: 100%"></div>

    <!-- 报表参数弹框 -->
    <Modal
        :closable="false"
        :mask-closable="false"
        :loading="loading"
        v-model="visible"
        title="请填写报表参数"
        :width="500">
        <div slot="footer">
            <i-button type="primary" @click="onSave" style="color:#fff !important;">确定</i-button>
        </div>
        <div style="padding-right: 30px">
            <i-form :model="reportParamObj" label-colon :label-width="90">
                <form-item :label="item.paramTxt" v-for="(item, index) in reportParamList">
                    <i-input style="width: 90%" :key="index" v-model="reportParamObj[item.paramName]" :placeholder="'请输入'+ item.paramTxt "></i-input>
                </form-item>
            </i-form>
        </div>
    </Modal>
    <Modal
        :closable="false"
        :mask-closable="false"
        :loading="lockLoading"
        v-model="lockVisible"
        title="请填写密码"
        :width="500">
        <div slot="footer">
            <i-button type="primary" @click="lockClick('${config_id}')">确定</i-button>
        </div>
        <div style="padding-right: 30px">
            <i-form label-colon :label-width="90">
                <form-item label="密码">
                    <i-input v-model="lock" placeholder="请输入密码"></i-input>
                </form-item>
            </i-form>
        </div>
    </Modal>
</div>
<#--预览js-->
<script type="text/javascript" src="${base}${customPrePath}/jmreport/desreport_/js/util.js?${CACHE_VERSION}"></script>
<script type="text/javascript" src="${base}${customPrePath}/jmreport/desreport_/js/biz/row.express.js?${CACHE_VERSION}"></script>
<script type="text/javascript" src="${base}${customPrePath}/jmreport/desreport_/js/biz/row.cycle.js?${CACHE_VERSION}"></script>
<script type="text/javascript" src="${base}${customPrePath}/jmreport/desreport_/cdn/vue/xss-0.3.3.min.js?${CACHE_VERSION}"></script>
<script type="text/javascript" src="${base}${customPrePath}/jmreport/desreport_/js/biz/view.js?${CACHE_VERSION}"></script>
<script type="text/javascript" src="${base}${customPrePath}/jmreport/desreport_/cdn/vue/md5.min.js?${CACHE_VERSION}"></script>
<script type="text/javascript" src="${base}${customPrePath}/jmreport/desreport_/js/biz/SignMd5Util.js?${CACHE_VERSION}"></script>
<#include "./common/tj.ftl">
</body>
</html>
