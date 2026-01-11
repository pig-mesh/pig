package com.pig4cloud.pigx.admin.controller;

import com.pig4cloud.pigx.admin.api.vo.DingTalkDeptExcelVO;
import com.pig4cloud.pigx.admin.api.vo.DingUserExcelVo;
import com.pig4cloud.pigx.admin.api.vo.WeChatDeptExcelVO;
import com.pig4cloud.pigx.admin.api.vo.WeChatUserExcelVO;
import com.pig4cloud.pigx.admin.service.ConnectService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.excel.annotation.RequestExcel;
import com.pig4cloud.pigx.common.security.annotation.HasPermission;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 钉钉、微信 互联
 *
 * @author lengleng
 * @date 2022/4/22
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/connect")
@Tag(description = "connect", name = "开放互联")
@SecurityRequirement(name = HttpHeaders.AUTHORIZATION)
public class ConnectController {

	private final ConnectService connectService;

	/**
	 * 导入企业微信部门
	 *
	 * @param excelVOList Excel数据列表(通过@RequestExcel自动解析)
	 * @param bindingResult 校验结果
	 * @return 导入结果
	 */
	@PostMapping("/import/wecom/dept")
	@HasPermission("sys_connect_sync")
	public R importWeComDept(@RequestExcel(headRowNumber = 9) List<WeChatDeptExcelVO> excelVOList, BindingResult bindingResult) {
		return connectService.importWeChatDept(excelVOList, bindingResult);
	}

	/**
	 * 导入企业微信用户
	 *
	 * @param excelVOList Excel数据列表(通过@RequestExcel自动解析)
	 * @param bindingResult 校验结果
	 * @return 导入结果
	 */
	@PostMapping("/import/wecom/user")
	@HasPermission("sys_connect_sync")
	public R importWeComUser(@RequestExcel(headRowNumber = 9) List<WeChatUserExcelVO> excelVOList, BindingResult bindingResult) {
		return connectService.importWeChatUser(excelVOList, bindingResult);
	}

	/**
	 * 导入钉钉部门
	 *
	 * @param excelVOList Excel数据列表(通过@RequestExcel自动解析)
	 * @param bindingResult 校验结果
	 * @return 导入结果
	 */
	@PostMapping("/import/ding/dept")
	@HasPermission("sys_connect_sync")
	public R importDingDept(@RequestExcel(headRowNumber = 3) List<DingTalkDeptExcelVO> excelVOList, BindingResult bindingResult) {
		return connectService.importDingTalkDept(excelVOList, bindingResult);
	}

	/**
	 * 导入钉钉用户
	 *
	 * @param excelVOList Excel数据列表(通过@RequestExcel自动解析)
	 * @param bindingResult 校验结果
	 * @return 导入结果
	 */
	@PostMapping("/import/ding/user")
	@HasPermission("sys_connect_sync")
	public R importDingUser(@RequestExcel(headRowNumber = 3) List<DingUserExcelVo> excelVOList, BindingResult bindingResult) {
		return connectService.importDingTalkUser(excelVOList, bindingResult);
	}

}
