
package com.anjiplus.template.gaea.business.modules.datasetparam.controller;

import com.anji.plus.gaea.bean.ResponseBean;
import com.anji.plus.gaea.curd.controller.GaeaBaseController;
import com.anji.plus.gaea.curd.service.GaeaBaseService;
import com.anjiplus.template.gaea.business.modules.datasetparam.controller.dto.DataSetParamDto;
import com.anjiplus.template.gaea.business.modules.datasetparam.controller.param.DataSetParamParam;
import com.anjiplus.template.gaea.business.modules.datasetparam.controller.param.DataSetParamValidationParam;
import com.anjiplus.template.gaea.business.modules.datasetparam.dao.entity.DataSetParam;
import com.anjiplus.template.gaea.business.modules.datasetparam.service.DataSetParamService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
* @desc 数据集动态参数 controller
* @website https://gitee.com/anji-plus/gaea
* @author Raod
* @date 2021-03-18 12:12:33.108033200
**/
@RestController
@Api(tags = "数据集动态参数管理")
@RequestMapping("/dataSetParam")
public class DataSetParamController extends GaeaBaseController<DataSetParamParam, DataSetParam, DataSetParamDto> {

    @Autowired
    private DataSetParamService dataSetParamService;

    @Override
    public GaeaBaseService<DataSetParamParam, DataSetParam> getService() {
        return dataSetParamService;
    }

    @Override
    public DataSetParam getEntity() {
        return new DataSetParam();
    }

    @Override
    public DataSetParamDto getDTO() {
        return new DataSetParamDto();
    }

    /**
     * 测试 查询参数是否正确
     * @param param
     * @return
     */
    @PostMapping("/verification")
    public ResponseBean verification(@Validated @RequestBody DataSetParamValidationParam param) {
        DataSetParamDto dto = new DataSetParamDto();
        dto.setSampleItem(param.getSampleItem());
        dto.setValidationRules(param.getValidationRules());
        return responseSuccessWithData(dataSetParamService.verification(dto));
    }
}
