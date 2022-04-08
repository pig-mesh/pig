<template>
    <Modal
            v-model="deleteParamModel"
            @on-ok="deleteParamTable"
            title="确认删除">
        <p><Icon type="ios-alert"  color="#f90" size="20px"></Icon>是否删除选中数据?</p>
    </Modal>
    <Modal
            v-model="deleteFieldModel"
            @on-ok="deleteFieldTable"
            title="确认删除">
        <p><Icon type="ios-alert"  color="#f90" size="16px"></Icon>是否删除选中配置?</p>
    </Modal>

    <Modal :loading="false" v-model="visible" title="报表信息" :width="500">
        <div style="padding-right: 30px">
            <i-form ref="designerObj" :model="designerObj" :label-width="90" :rules="designerObjRules">
                <#--<form-item label="编码">
                    <i-input v-model="designerObj.code" disabled></i-input>
                </form-item>-->

                <form-item label="名称" prop="name">
                    <i-input v-model="designerObj.name" placeholder="请输入名称" @on-change="changeName"></i-input>
                </form-item>

                <form-item label="类型" prop="type">
                    <i-select :model.sync="designerObj.type" v-model="designerObj.type" style="width:100%" @on-change="selectmenuList">
                        <i-option v-for="item in menuList" :value="item.value">{{ item.label }}</i-option>
                    </i-select>
                </form-item>

            </i-form>
        </div>
        <div slot="footer">
            <i-button @click="closePopup('designerObj')">取消</i-button>
            <i-button :loading="saveReportLoading" type="primary" @click="saveReport('designerObj')">确定</i-button>
        </div>
    </Modal>

    <Modal :loading="loading" v-model="addEchart" title="数据编辑" :width="50" @on-ok="addEchartData">
        <div>
            <i-form>
                <form-item>
                    <i-input type="textarea" :autosize="{minRows: 15,maxRows: 15}" v-model="apiStaticDataList" ></i-input>
                </form-item>
            </i-form>
        </div>
    </Modal>

    <Modal :loading="loading" v-model="chartModule" :width="1000" @on-ok="okAddChart" @on-cancel="selectedChartType=''">
        <p slot="header">
            <span>添加图表
               <Tooltip :transfer="true" content="图表文档" placement="top">
                  <a class="jimu-table-tip help-color" href="http://report.jeecg.com/1605007" target="_blank"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
               </Tooltip>  
            </span>
        </p>
        <Tabs value="bar" class="chart-modal-content" >
            <tab-pane :label="obj.label" :name="obj.name" v-for="(obj,index) of chartTypeList" :index="index+1">
                <Row justify="center">
                    <i-col span="5" offset="1" v-for="(item,index) of obj.typeList" :class="item.allowed ? '':'no-allowed'" style="margin-top: 20px">
                        <div style="border: solid 1px #dcdee2;width: 180px;height: 130px;" :class="selectedChartId == item.id ? 'chart-selected':''" @click="setSelectCharType(item)" >
                            <img :src="'${base}'+'${customPrePath}'+item.imgUrl" style="width:95%;height:95%;margin: 0 5px;">
                            <span style="float: left;width:180px;margin-top: 8px;text-align:center;;font-size: 12px">{{item.name}}</span>
                        </div>
                    </i-col>
                </Row>
            </tab-pane>
        </Tabs>
    </Modal>

    <Modal :loading="loading" v-model="seriesModal" title="系列类型信息" :width="30" @on-ok="addSeriesType" @on-cancel="seriesObj={}">
        <div style="padding-right: 50px">
            <i-form :model="seriesObj" label-colon :label-width="90">
                <form-item label="系列">
                    <i-select v-model="seriesObj.name" style="width:100%" @on-change="selectmenuList" placeholder="请选择">
                        <i-option v-for="(item, index) in customColorNameList" :index="index" :value="item">{{ item }}</i-option>
                    </i-select>
                </form-item>
                <form-item  label="图表类型" v-if="selectedChartType !== 'bar.stack' && selectedChartId!='bar.negative'">
                    <i-select v-model="seriesObj.type" placeholder="请选择图表类型">
                        <i-option value="bar">柱形图</i-option>
                        <i-option value="line">折线图</i-option>
                    </i-select>
                </form-item>
                <form-item  label="系列堆叠值" v-if="selectedChartType === 'bar.stack'|| selectedChartId==='bar.negative'">
                    <i-input v-model="seriesObj.stack" placeholder="请自定义系列堆叠值，相同值的会堆叠在一起"></i-input>
                </form-item>
            </i-form>
        </div>
    </Modal>
    <#--自定义表达式弹窗-->
    <Modal :draggable="funDraggable" :loading="loading" :fullscreen="functionScreen" v-model="customExpressionShow" :width="1000" :autosize="true" @on-ok="expressionSave" @on-cancel="expressionCancel" class="expression">
        <p slot="header">
            <span>添加表达式
               <Tooltip :transfer="true" content="表达式文档" placement="top">
                   <a class="jimu-table-tip help-color" href="http://report.jeecg.com/2332213" target="_blank"><Icon size="14" type="ios-help-circle-outline" style="margin-top: 2px"/></a>
               </Tooltip>
               <Tooltip style="cursor: pointer;float: right;margin-right: 30px;" v-if="functionScreen" :transfer="true" content="缩小" placement="top">
                   <Icon type="ios-contract" @click="functionScreenClick"/>
               </Tooltip>
               <Tooltip style="cursor: pointer;float: right;margin-right: 30px;" v-else :transfer="true" content="放大" placement="top">
                     <Icon type="ios-expand" @click="functionScreenClick"/>
               </Tooltip>
            </span>
        </p>
        <i-form label-colon :label-width="90">
            <Row justify="left">
                <i-col>
                    <span class="fontColor">请在下面的文本框中输入公式，不需要输入开头的等号:</span>
                </i-col>    
            </Row>
            <Row justify="center" style="margin-top: 10px">
                <i-col>
                  <i-input type="textarea" v-model="expression" placeholder="请输入表达式" :class="functionScreen==false?'expressionInput':'expressionHeight'"></i-input> 
                </i-col>
            </Row>
            <Row justify="center" style="margin-top: 10px">
                <i-col span="6" class="functionDiv">
                    <div :class="leftFunctionIndex == item.id?'leftFunctionSelect':'leftFunction'" v-for="(item,index) in functionType" @click="leftFunctionClick(item.id)">
                        <span class="fontColor">{{item.name}}</span>
                    </div>
                </i-col>
                <i-col span="1">
                </i-col>
                <i-col span="13">
                    <div class="childrenDiv">
                        <div v-if="commonFunction" v-for="(item,index) in newFunctionList" @click="rightFunctionClick(item,index)" :class="rightFunctionIndex == index?'rightFunctionSelect':'activeItem'">
                            <span class="fontColor">{{item}}</span> 
                        </div>
                    </div>
                </i-col>
                <i-col span="5">
                   <j-function-interpretation :text="interpretation"></j-function-interpretation>
                </i-col>
            </Row>
        </i-form>
    </Modal>

    <#-- 增强弹框 -->
    <Modal draggable :loading="loading" v-model="enhanceModalVisible" title="增强配置" :width="1000" :class="'jmreport-enhance'"
           @on-ok="saveEnhanceConfig" @on-cancel="cancleEnhanceModal">
        <Collapse value="1">
            <Panel name="1">
                CSS
                <div slot="content">
                    <i-input :rows="10" type="textarea" v-model="enhanceCssStr" placeholder="请输入值CSS片段" ></i-input>
                </div>
            </Panel>

            <Panel name="2">
                JS
                <div slot="content">
                    <i-input :rows="10" type="textarea" v-model="enhanceJsStr" placeholder="请输入值JS片段" ></i-input>
                </div>
            </Panel>
        </Collapse>
    </Modal>

  <#--输入框值放大-->
  <Modal draggable :loading="loading" v-model="enlargeInputModal" title="输入值" :width="1000" @on-ok="enlargeInputOk"
         @on-cancel="enlargeInputCancel" class="expression">
    <i-form label-colon :label-width="90">
      <form-item prop="valueCoordinate" label="单元格">
        <i-input readonly v-model="valueCoordinate"></i-input>
      </form-item>
      <form-item prop="valueExpression" label="值">
        <i-input type="textarea" v-model="valueExpression" placeholder="请输入值" class="expressionInput"></i-input>
      </form-item>
    </i-form>
  </Modal>
</template>
