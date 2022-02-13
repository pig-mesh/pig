package com.anjiplus.template.gaea.business.modules.file.controller;

import com.anji.plus.gaea.annotation.Permission;
import com.anji.plus.gaea.bean.ResponseBean;
import com.anji.plus.gaea.curd.service.GaeaBaseService;
import com.anjiplus.template.gaea.business.base.BaseController;
import com.anjiplus.template.gaea.business.modules.file.controller.dto.GaeaFileDTO;
import com.anjiplus.template.gaea.business.modules.file.controller.param.GaeaFileParam;
import com.anjiplus.template.gaea.business.modules.file.entity.GaeaFile;
import com.anjiplus.template.gaea.business.modules.file.service.GaeaFileService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * (GaeaFile)实体类
 *
 * @author peiyanni
 * @since 2021-02-18 14:48:33
 */
@RestController
@RequestMapping("/file")
@Api(value = "/file", tags = "")
public class GaeaFileController extends BaseController<GaeaFileParam, GaeaFile, GaeaFileDTO> {
    @Autowired
    private GaeaFileService gaeaFileService;

    @PostMapping("/upload")
    @Permission(code = "upload", name = "文件上传")
    public ResponseBean upload(@RequestParam("file") MultipartFile file) {
        return ResponseBean.builder().message("success").data((gaeaFileService.upload(file))).build();
    }

    @GetMapping(value = "/download/{fileId}")
    public ResponseEntity<byte[]> download(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileId") String fileId) {
        return gaeaFileService.download(request, response, fileId);
    }

    /**
     * 获取实际服务类
     *
     * @return
     */
    @Override
    public GaeaBaseService<GaeaFileParam, GaeaFile> getService() {
        return gaeaFileService;
    }

    /**
     * 获取当前Controller数据库实体Entity
     *
     * @return
     */
    @Override
    public GaeaFile getEntity() {
        return new GaeaFile();
    }

    /**
     * 获取当前Controller数据传输DTO
     *
     * @return
     */
    @Override
    public GaeaFileDTO getDTO() {
        return new GaeaFileDTO();
    }
}
