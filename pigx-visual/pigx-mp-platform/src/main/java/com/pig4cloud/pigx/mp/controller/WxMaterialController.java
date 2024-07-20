package com.pig4cloud.pigx.mp.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import com.pig4cloud.pigx.mp.config.WxMpInitConfigRunner;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpDraftService;
import me.chanjar.weixin.mp.api.WxMpMaterialService;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.draft.WxMpAddDraft;
import me.chanjar.weixin.mp.bean.draft.WxMpDraftArticles;
import me.chanjar.weixin.mp.bean.draft.WxMpUpdateDraft;
import me.chanjar.weixin.mp.bean.material.WxMediaImgUploadResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterial;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialFileBatchGetResult;
import me.chanjar.weixin.mp.bean.material.WxMpMaterialUploadResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 微信素材
 *
 * @author JL
 * @date 2019-03-23 21:26:35
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/wx-material")
public class WxMaterialController {

	/**
	 * 上传非图文微信素材
	 * @param mulFile
	 * @param mediaType
	 * @return
	 */
	@PostMapping("/materialFileUpload")
	@HasPermission("mp_wxmaterial_add")
	public R materialFileUpload(@RequestPart("file") MultipartFile mulFile, @RequestParam String appId,
			@RequestParam("title") String title, @RequestParam("introduction") String introduction,
			@RequestParam("mediaType") String mediaType) {
		try {
			String originalFilename = new String(
					Objects.requireNonNull(mulFile.getOriginalFilename()).getBytes(StandardCharsets.ISO_8859_1),
					StandardCharsets.UTF_8);
			WxMpMaterial material = new WxMpMaterial();
			material.setName(originalFilename);
			if (WxConsts.MediaFileType.VIDEO.equals(mediaType)) {
				material.setVideoTitle(title);
				material.setVideoIntroduction(introduction);
			}

			File file = FileUtil.newFile(FileUtil.getTmpDirPath() + originalFilename);
			mulFile.transferTo(file);
			material.setFile(file);

			WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(appId);
			WxMpMaterialService wxMpMaterialService = wxMpService.getMaterialService();
			WxMpMaterialUploadResult wxMpMaterialUploadResult = wxMpMaterialService.materialFileUpload(mediaType,
					material);
			WxMpMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem wxMpMaterialFileBatchGetResult = new WxMpMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem();
			wxMpMaterialFileBatchGetResult.setName(file.getName());
			wxMpMaterialFileBatchGetResult.setMediaId(wxMpMaterialUploadResult.getMediaId());
			wxMpMaterialFileBatchGetResult.setUrl(wxMpMaterialUploadResult.getUrl());
			FileUtil.del(file);
			return R.ok(wxMpMaterialFileBatchGetResult);
		}
		catch (WxErrorException e) {
			log.warn("上传非图文微信素材失败: {}", e.getLocalizedMessage());
			return R.failed(e.getLocalizedMessage());
		}
		catch (Exception e) {
			log.error("上传失败", e);
			return R.failed(e.getLocalizedMessage());
		}
	}

	/**
	 * 新增图文消息
	 * @param data
	 * @return
	 */
	@PostMapping("/materialNews")
	@HasPermission("mp_wxmaterial_add")
	public R materialNewsUpload(@RequestBody JSONObject data) {
		try {
			JSONArray jSONArray = data.getJSONArray("articles");
			List<WxMpDraftArticles> articles = jSONArray.toList(WxMpDraftArticles.class);

			WxMpAddDraft draft = new WxMpAddDraft();
			draft.setArticles(articles);
			String appId = data.getStr("appId");
			WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(appId);
			WxMpDraftService draftService = wxMpService.getDraftService();
			return R.ok(draftService.addDraft(draft));
		}
		catch (WxErrorException e) {
			log.warn("新增图文失败: {}", e.getMessage());
			return R.failed(e.getMessage());
		}
		catch (Exception e) {
			log.error("新增图文失败", e);
			return R.failed(e.getLocalizedMessage());
		}
	}

	/**
	 * 修改图文消息
	 * @param data
	 * @return
	 */
	@PutMapping("/materialNews")
	@HasPermission("mp_wxmaterial_add")
	public R materialNewsUpdate(@RequestBody JSONObject data) {
		try {
			String mediaId = data.getStr("mediaId");
			JSONArray jSONArray = data.getJSONArray("articles");
			List<WxMpDraftArticles> articles = jSONArray.toList(WxMpDraftArticles.class);
			String appId = data.getStr("appId");
			WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(appId);
			WxMpDraftService draftService = wxMpService.getDraftService();

			WxMpUpdateDraft mpUpdateDraft = new WxMpUpdateDraft();
			mpUpdateDraft.setMediaId(mediaId);
			int index = 0;
			for (WxMpDraftArticles article : articles) {
				mpUpdateDraft.setIndex(index);
				mpUpdateDraft.setArticles(article);
				draftService.updateDraft(mpUpdateDraft);
				index++;
			}
			return R.ok();
		}
		catch (WxErrorException e) {
			log.warn("修改图文失败:{}", e.getLocalizedMessage());
			return R.failed(e.getLocalizedMessage());
		}
		catch (Exception e) {
			log.error("修改图文失败", e);
			return R.failed(e.getLocalizedMessage());
		}
	}

	/**
	 * 上传图文消息内的图片获取URL
	 * @param mulFile
	 * @return
	 */
	@PostMapping("/newsImgUpload")
	@HasPermission("mp_wxmaterial_add")
	public String newsImgUpload(@RequestParam("file") MultipartFile mulFile, @RequestParam String appId)
			throws Exception {
		File file = FileUtil.createTempFile(FileUtil.getTmpDir());
		mulFile.transferTo(file);
		WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(appId);
		WxMpMaterialService wxMpMaterialService = wxMpService.getMaterialService();
		WxMediaImgUploadResult wxMediaImgUploadResult = wxMpMaterialService.mediaImgUpload(file);
		Map<Object, Object> responseData = new HashMap<>(2);
		responseData.put("link", wxMediaImgUploadResult.getUrl());
		FileUtil.del(file);
		return JSONUtil.toJsonStr(responseData);
	}

	/**
	 * 通过id删除微信素材
	 * @param
	 * @return R
	 */
	@DeleteMapping
	@HasPermission("mp_wxmaterial_del")
	public R materialDel(String appId, String id) {
		WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(appId);
		WxMpMaterialService wxMpMaterialService = wxMpService.getMaterialService();
		try {
			return R.ok(wxMpMaterialService.materialDelete(id));
		}
		catch (WxErrorException e) {
			log.error("删除微信素材失败", e);
			return R.failed(e.getMessage());
		}
	}

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param type
	 * @return
	 */
	@GetMapping("/page")
	public R getWxMaterialPage(Page page, String type, String appId) {
		try {
			WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(appId);
			WxMpMaterialService wxMpMaterialService = wxMpService.getMaterialService();

			int count = (int) page.getSize();
			int offset = (int) page.getCurrent() * count - count;
			if (WxConsts.MaterialType.NEWS.equals(type)) {
				WxMpDraftService draftService = wxMpService.getDraftService();
				return R.ok(draftService.listDraft(offset, count));
			}
			else {
				return R.ok(wxMpMaterialService.materialFileBatchGet(type, offset, count));
			}
		}
		catch (WxErrorException e) {
			log.error("查询素材失败", e);
			return R.failed(e.getLocalizedMessage());
		}
	}

	/**
	 * 获取微信视频素材
	 * @param
	 * @return R
	 */
	@GetMapping("/materialVideo")
	public R getMaterialVideo(String mediaId, String appId) {
		WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(appId);
		WxMpMaterialService wxMpMaterialService = wxMpService.getMaterialService();
		try {
			return R.ok(wxMpMaterialService.materialVideoInfo(mediaId));
		}
		catch (WxErrorException e) {
			log.error("获取微信视频素材失败", e);
			return R.failed(e.getMessage());
		}
	}

	/**
	 * 获取微信素材直接文件
	 * @param
	 * @return R
	 */
	@GetMapping("/materialOther")
	public ResponseEntity<byte[]> getMaterialOther(String appId, String mediaId, String fileName) throws Exception {
		try {
			WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(appId);
			WxMpMaterialService wxMpMaterialService = wxMpService.getMaterialService();
			// 获取文件
			InputStream is = wxMpMaterialService.materialImageOrVoiceDownload(mediaId);
			byte[] body = new byte[is.available()];
			is.read(body);
			HttpHeaders headers = new HttpHeaders();
			// 设置文件类型
			headers.add("Content-Disposition", "attchement;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			headers.add("Content-Type", "application/octet-stream");
			HttpStatus statusCode = HttpStatus.OK;
			// 返回数据
			ResponseEntity<byte[]> entity = new ResponseEntity<>(body, headers, statusCode);
			return entity;
		}
		catch (WxErrorException e) {
			e.printStackTrace();
			log.error("获取微信素材直接文件失败", e);
			return null;
		}
	}

	/**
	 * 获取微信临时素材直接文件
	 * @param
	 * @return R
	 */
	@GetMapping("/tempMaterialOther")
	public ResponseEntity<byte[]> getTempMaterialOther(String appId, String mediaId, String fileName) throws Exception {
		try {
			WxMpService wxMpService = WxMpInitConfigRunner.getMpServices().get(appId);
			WxMpMaterialService wxMpMaterialService = wxMpService.getMaterialService();
			// 获取文件
			InputStream is = new FileInputStream(wxMpMaterialService.mediaDownload(mediaId));
			byte[] body = new byte[is.available()];
			is.read(body);
			HttpHeaders headers = new HttpHeaders();
			// 设置文件类型
			headers.add("Content-Disposition", "attchement;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			headers.add("Content-Type", "application/octet-stream");
			HttpStatus statusCode = HttpStatus.OK;
			// 返回数据
			ResponseEntity<byte[]> entity = new ResponseEntity<>(body, headers, statusCode);
			return entity;
		}
		catch (WxErrorException e) {
			e.printStackTrace();
			log.error("获取微信素材直接文件失败", e);
			return null;
		}
	}

}
