package com.pig4cloud.pigx.admin.service;

import com.pig4cloud.pigx.admin.api.vo.DingTalkDeptExcelVO;
import com.pig4cloud.pigx.admin.api.vo.DingUserExcelVo;
import com.pig4cloud.pigx.admin.api.vo.WeChatDeptExcelVO;
import com.pig4cloud.pigx.admin.api.vo.WeChatUserExcelVO;
import com.pig4cloud.pigx.common.core.util.R;
import org.springframework.validation.BindingResult;

import java.util.List;

/**
 * @author lengleng
 * @date 2022/4/22
 * <p>
 * 互联平台
 */
public interface ConnectService {

	/**
	 * 导入企业微信部门
	 *
	 * @param excelVOList Excel数据列表
	 * @param bindingResult 校验结果
	 * @return 导入结果
	 */
	R importWeChatDept(List<WeChatDeptExcelVO> excelVOList, BindingResult bindingResult);

	/**
	 * 导入企业微信用户
	 *
	 * @param excelVOList Excel数据列表
	 * @param bindingResult 校验结果
	 * @return 导入结果
	 */
	R importWeChatUser(List<WeChatUserExcelVO> excelVOList, BindingResult bindingResult);

	/**
	 * 导入钉钉部门
	 *
	 * @param excelVOList   Excel数据列表
	 * @param bindingResult 校验结果
	 * @return 导入结果
	 */
	R importDingTalkDept(List<DingTalkDeptExcelVO> excelVOList, BindingResult bindingResult);

	/**
	 * 导入钉钉用户
	 *
	 * @param excelVOList   Excel数据列表
	 * @param bindingResult 校验结果
	 * @return 导入结果
	 */
	R importDingTalkUser(List<DingUserExcelVo> excelVOList, BindingResult bindingResult);

}
