
package com.anjiplus.template.gaea.business.modules.dataset.controller;

import com.anji.plus.gaea.annotation.AccessKey;
import com.anji.plus.gaea.annotation.Permission;
import com.anji.plus.gaea.annotation.log.GaeaAuditLog;
import com.anji.plus.gaea.bean.ResponseBean;
import com.anji.plus.gaea.curd.controller.GaeaBaseController;
import com.anji.plus.gaea.curd.service.GaeaBaseService;
import com.anji.plus.gaea.holder.UserContentHolder;
import com.anji.plus.gaea.utils.GaeaUtils;
import com.anjiplus.template.gaea.business.modules.dataset.controller.dto.DataSetDto;
import com.anjiplus.template.gaea.business.modules.dataset.controller.param.DataSetParam;
import com.anjiplus.template.gaea.business.modules.dataset.controller.param.DataSetTestTransformParam;
import com.anjiplus.template.gaea.business.modules.dataset.dao.entity.DataSet;
import com.anjiplus.template.gaea.business.modules.dataset.service.DataSetService;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
* @desc 数据集 controller
* @website https://gitee.com/anji-plus/gaea
* @author Raod
* @date 2021-03-18 12:11:31.150755900
**/
@RestController
@Api(tags = "数据集管理")
@Permission(code = "resultsetManage", name = "数据集管理")
@RequestMapping("/dataSet")
public class DataSetController extends GaeaBaseController<DataSetParam, DataSet, DataSetDto> {

    @Autowired
    private DataSetService dataSetService;

    @Override
    public GaeaBaseService<DataSetParam, DataSet> getService() {
        return dataSetService;
    }

    @Override
    public DataSet getEntity() {
        return new DataSet();
    }

    @Override
    public DataSetDto getDTO() {
        return new DataSetDto();
    }

    @GetMapping("/detailBysetId/{id}")
    @Permission( code = "query", name = "明细" )
    public ResponseBean detailBysetId(@PathVariable("id") Long id) {
        this.logger.info("{}根据ID查询服务开始，id为：{}", this.getClass().getSimpleName(), id);
        ResponseBean responseBean = this.responseSuccessWithData(dataSetService.detailSet(id));
        this.logger.info("{}根据ID查询结束，结果：{}", this.getClass().getSimpleName(), GaeaUtils.toJSONString(responseBean));
        return responseBean;
    }

    @GetMapping({"/detailBysetCode/{setCode}"})
    @Permission( code = "query", name = "明细" )
    @AccessKey
    public ResponseBean detailBysetCode(@PathVariable("setCode") String setCode) {
        this.logger.info("{}根据setCode查询服务开始，setCode为：{}", this.getClass().getSimpleName(), setCode);
        ResponseBean responseBean = this.responseSuccessWithData(dataSetService.detailSet(setCode));
        this.logger.info("{}根据setCode查询结束，结果：{}", this.getClass().getSimpleName(), GaeaUtils.toJSONString(responseBean));
        return responseBean;
    }

    @PostMapping
    @Permission(
            code = "insert",
            name = "新增"
    )
    @GaeaAuditLog(
            pageTitle = "新增"
    )
    @Override
    public ResponseBean insert(@RequestBody DataSetDto dto) {
        this.logger.info("{}新增服务开始，参数：{}", this.getClass().getSimpleName(), GaeaUtils.toJSONString(dto));
        DataSetDto dataSetDto = dataSetService.insertSet(dto);
        this.logger.info("{}新增服务结束，结果：{}", this.getClass().getSimpleName(), GaeaUtils.toJSONString(dataSetDto));
        return ResponseBean.builder().data(dataSetDto).build();
    }

    @PutMapping
    @Permission(
            code = "update",
            name = "更新"
    )
    @GaeaAuditLog(
            pageTitle = "修改"
    )
    @Override
    public ResponseBean update(@RequestBody DataSetDto dto) {
        String username = UserContentHolder.getContext().getUsername();
        this.logger.info("{}更新服务开始,更新人：{}，参数：{}", this.getClass().getSimpleName(), username, GaeaUtils.toJSONString(dto));
        ResponseBean responseBean = this.responseSuccess();
        dataSetService.updateSet(dto);
        this.logger.info("{}更新服务结束，结果：{}", this.getClass().getSimpleName(), GaeaUtils.toJSONString(responseBean));
        return this.responseSuccess();
    }

    @DeleteMapping({"/{id}"})
    @Permission(
            code = "delete",
            name = "删除"
    )
    @GaeaAuditLog(
            pageTitle = "删除"
    )
    @Override
    public ResponseBean deleteById(@PathVariable("id") Long id) {
        this.logger.info("{}删除服务开始，参数ID：{}", this.getClass().getSimpleName(), id);
        dataSetService.deleteSet(id);
        this.logger.info("{}删除服务结束", this.getClass().getSimpleName());
        return this.responseSuccess();
    }

    /**
     * 测试 数据转换是否正确
     * @param param
     * @return
     */
    @PostMapping("/testTransform")
    @Permission( code = "query", name = "明细" )
    public ResponseBean testTransform(@Validated @RequestBody DataSetTestTransformParam param) {
        DataSetDto dto = new DataSetDto();
        BeanUtils.copyProperties(param, dto);
        return responseSuccessWithData(dataSetService.testTransform(dto));
    }

    /**
     * 获取所有数据集
     * @return
     */
    @GetMapping("/queryAllDataSet")
    public ResponseBean queryAllDataSet() {
        return responseSuccessWithData(dataSetService.queryAllDataSet());
    }


}
