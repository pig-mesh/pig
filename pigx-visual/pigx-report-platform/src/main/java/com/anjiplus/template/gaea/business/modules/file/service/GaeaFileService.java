package com.anjiplus.template.gaea.business.modules.file.service;

import com.anji.plus.gaea.curd.service.GaeaBaseService;
import com.anjiplus.template.gaea.business.modules.file.entity.GaeaFile;
import com.anjiplus.template.gaea.business.modules.file.controller.param.GaeaFileParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * (GaeaFile)Service
 *
 * @author peiyanni
 * @since 2021-02-18 14:48:25
 */
public interface GaeaFileService extends GaeaBaseService<GaeaFileParam, GaeaFile> {

    /**
     * 文件上传
     *
     * @param multipartFile  文件
     * @param file 文件
     * @param customFileName 自定义文件名，默认给null
     * @return
     */
    GaeaFile upload(MultipartFile multipartFile, File file, String customFileName);

    /**
     * 文件上传
     *
     * @param multipartFile  文件
     * @return
     */
    GaeaFile upload(MultipartFile multipartFile);


    /**
     * 文件上传
     *
     * @param file 二选一
     * @param customFileName 自定义文件名
     * @return
     */
    GaeaFile upload(File file, String customFileName);
    /**
     * 根据fileId显示图片或者下载文件
     *
     * @param request
     * @param response
     * @param fileId
     * @return
     */
    ResponseEntity<byte[]> download(HttpServletRequest request, HttpServletResponse response, String fileId);
}
