package com.pig4cloud.pigx.flow.controller;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.entity.Process;
import com.pig4cloud.pigx.flow.service.IProcessService;
import com.pig4cloud.pigx.flow.vo.ProcessVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 流程定义控制器
 * <p>
 * 提供流程定义相关的REST API接口，包括流程的创建、查询、更新等操作。
 * 所有接口都需要用户认证，并根据用户权限进行访问控制。
 * </p>
 * 
 * @author pigx
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/process")
public class ProcessController {

	private final IProcessService processService;

	/**
	 * 获取流程详细信息
	 * <p>
	 * 根据流程ID获取流程的完整配置信息，包括：
	 * - 基本信息（名称、图标、描述等）
	 * - 表单配置（表单项定义、验证规则等）
	 * - 流程配置（节点定义、流转规则等）
	 * - 权限配置（管理员、发起人范围等）
	 * </p>
	 * 
	 * @param flowId 流程定义ID
	 * @return 流程详细信息
	 */
	@GetMapping("getDetail")
	public R<ProcessVO> getDetail(String flowId) {
		return processService.getDetail(flowId);
	}

	/**
	 * 创建新流程定义
	 * <p>
	 * 创建新的流程定义，需要提供流程基本信息。
	 * 系统会自动生成流程的唯一标识（flowId）和默认配置。
	 * 创建者自动成为该流程的管理员。
	 * </p>
	 * 
	 * @param process 流程定义实体（包含名称、分组、图标等信息）
	 * @return 创建结果，成功时返回生成的flowId
	 */
	@PostMapping("create")
	public R create(@RequestBody Process process) {
		return processService.create(process);
	}

	/**
	 * 更新流程状态或分组
	 * <p>
	 * 支持以下操作：
	 * - stop: 停用流程（停用后不能创建新实例，但已有实例可继续执行）
	 * - using: 启用流程（恢复流程的正常使用）
	 * - delete: 删除流程（逻辑删除，可恢复）
	 * - 修改分组：传入groupId参数可将流程移动到其他分组
	 * </p>
	 *
	 * @param flowId 流程定义ID
	 * @param type 操作类型（stop/using/delete）
	 * @param groupId 流程分组ID（可选，用于修改流程所属分组）
	 * @return 操作结果
	 */
	@PutMapping("update/{flowId}")
	public R update(@PathVariable String flowId, @RequestParam String type,
			@RequestParam(required = false) Long groupId) {
		return processService.update(flowId, type, groupId);
	}

	/**
	 * 验证流程ID是否在所有IProcessInstanceStatusEventService实现类中存在
	 * <p>
	 * 用于前端表单验证，检查用户输入的流程ID是否已在系统中注册。
	 * 流程ID必须在某个IProcessInstanceStatusEventService实现类的getFlowId()方法中返回，
	 * 才能被认为是有效的流程ID。
	 * </p>
	 *
	 * @param flowId 流程定义ID
	 * @return 验证结果：true表示流程ID存在，false表示不存在
	 */
	@GetMapping("validateFlowId")
	public R<Boolean> validateFlowId(@RequestParam String flowId) {
		return processService.validateFlowId(flowId);
	}

    /**
     * 获取流程打印模板
     *
     * @param flowId 流程定义ID
     * @return 打印模板HTML内容
     */
    @GetMapping("printTemplate/{flowId}")
    public R<String> getPrintTemplate(@PathVariable String flowId) {
        Process process = processService.getByFlowId(flowId);
        return R.ok(process != null ? process.getPrintTemplate() : null);
    }

    /**
     * 保存流程打印模板
     *
     * @param process 包含flowId和printTemplate的实体
     * @return 操作结果
     */
    @PutMapping("printTemplate")
    public R<Void> savePrintTemplate(@RequestBody Process process) {
        processService.updateByFlowId(new Process().setFlowId(process.getFlowId()).setPrintTemplate(process.getPrintTemplate()));
        return R.ok();
    }

}
