package com.pig4cloud.pigx.flow.api.feign;

import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.dto.ProcessInstanceParamDto;
import com.pig4cloud.pigx.flow.vo.FormGroupVo;
import com.pig4cloud.pigx.flow.vo.ProcessVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 远程流程API服务接口
 * <p>
 * 通过Feign客户端调用流程服务的远程接口，提供流程相关的核心功能。
 * 主要包括流程查询、流程启动等操作。该接口用于微服务间的远程调用，
 * 使其他服务能够便捷地使用流程引擎的功能。
 * </p>
 * 
 * @author lengleng
 * @date 2025/07/12
 */
@FeignClient(contextId = "remoteFlowApiFlowService", value = ServiceNameConstants.FLOW_SERVER)
public interface RemoteFlowApiFlowService {

	/**
	 * 查询当前用户可发起的流程列表
	 * <p>
	 * 根据当前登录用户的权限，查询该用户有权发起的所有流程。
	 * 返回结果按分组组织，方便前端展示。
	 * </p>
	 * 
	 * @return 分组后的流程列表，包含分组信息和各分组下的流程
	 */
	@GetMapping("/combination/group/listCurrentUserStartGroup")
	R<List<FormGroupVo>> listCurrentUserStartGroup();

	/**
	 * 获取流程详细信息
	 * <p>
	 * 根据流程ID获取流程的完整配置信息，包括：
	 * - 流程基本信息（名称、图标、描述等）
	 * - 表单配置（表单项定义、验证规则等）
	 * - 流程配置（节点定义、流转规则等）
	 * </p>
	 * 
	 * @param flowId 流程定义ID
	 * @return 流程详细信息视图对象
	 */
	@GetMapping("/process/getDetail")
	R<ProcessVO> getDetail(@RequestParam("flowId") String flowId);

	/**
	 * 启动流程实例
	 * <p>
	 * 根据流程定义创建新的流程实例。启动流程时需要提供：
	 * - 流程ID
	 * - 表单数据
	 * - 需要自选的节点执行人信息
	 * </p>
	 * 
	 * @param paramDto 流程实例启动参数，包含流程ID、表单数据等
	 * @return 启动成功后的流程实例ID
	 */
	@PostMapping("/process-instance/startProcessInstance")
	R<String> startProcessInstance(@RequestBody ProcessInstanceParamDto paramDto);

}
