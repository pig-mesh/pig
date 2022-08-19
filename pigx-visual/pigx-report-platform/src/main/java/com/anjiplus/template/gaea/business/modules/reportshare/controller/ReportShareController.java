
package com.anjiplus.template.gaea.business.modules.reportshare.controller;

import com.anji.plus.gaea.annotation.AccessKey;
import com.anji.plus.gaea.annotation.Permission;
import com.anji.plus.gaea.bean.ResponseBean;
import com.anji.plus.gaea.curd.controller.GaeaBaseController;
import com.anji.plus.gaea.curd.service.GaeaBaseService;
import com.anji.plus.gaea.utils.GaeaBeanUtils;
import com.anji.plus.gaea.utils.GaeaUtils;
import com.anjiplus.template.gaea.business.modules.reportshare.controller.dto.ReportShareDto;
import com.anjiplus.template.gaea.business.modules.reportshare.controller.param.ReportShareParam;
import com.anjiplus.template.gaea.business.modules.reportshare.dao.entity.ReportShare;
import com.anjiplus.template.gaea.business.modules.reportshare.service.ReportShareService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Raod
 * @desc 报表分享 controller
 * @date 2021-08-18 13:37:26.663
 **/
@RestController
@Api(tags = "报表分享管理")
@RequestMapping("/reportShare")
@Permission(code = "reportShareManage", name = "报表分享管理")
public class ReportShareController extends GaeaBaseController<ReportShareParam, ReportShare, ReportShareDto> {

    @Autowired
    private ReportShareService reportShareService;

    @Override
    public GaeaBaseService<ReportShareParam, ReportShare> getService() {
        return reportShareService;
    }

    @Override
    public ReportShare getEntity() {
        return new ReportShare();
    }

    @Override
    public ReportShareDto getDTO() {
        return new ReportShareDto();
    }


    @GetMapping({"/{id}"})
    @AccessKey
    @Override
    @Permission(code = "detail", name = "明细")
    public ResponseBean detail(@PathVariable("id") Long id) {
        this.logger.info("{}根据ID查询服务开始，id为：{}", this.getClass().getSimpleName(), id);
        ReportShare result = reportShareService.getDetail(id);
        ReportShareDto dto = this.getDTO();
        GaeaBeanUtils.copyAndFormatter(result, dto);
        ResponseBean responseBean = this.responseSuccessWithData(this.resultDtoHandle(dto));
        this.logger.info("{}根据ID查询结束，结果：{}", this.getClass().getSimpleName(), GaeaUtils.toJSONString(responseBean));
        return responseBean;
    }

    @GetMapping({"/detailByCode"})
    @Permission(code = "detail", name = "明细")
    public ResponseBean detailByCode(@RequestParam("shareCode") String shareCode) {
        return ResponseBean.builder().data(reportShareService.detailByCode(shareCode)).build();
    }

    @PostMapping({"/shareDelay"})
    @Permission(code = "shareDelay", name = "分享延期")
    public ResponseBean shareDelay(@RequestBody ReportShareDto dto) {
        reportShareService.shareDelay(dto);
        return ResponseBean.builder().build();
    }

}
