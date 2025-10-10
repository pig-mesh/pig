package com.pig4cloud.pigx.flow.controller;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.dto.*;
import com.pig4cloud.pigx.flow.service.IRemoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 远程服务调用控制器
 * <p>
 * 该控制器作为流程模块的内部接口，主要供其他模块调用或流程引擎内部使用。 提供了一系列辅助功能接口，包括： 1. 用户和组织查询 - 根据角色、部门查询用户 2. 流程事件处理
 * - 节点开始/结束、任务分配等事件 3. 抄送管理 - 保存流程抄送记录 4. 权限验证 - 检查用户组织关系
 * </p>
 * <p>
 * 这些接口通常在流程执行过程中被引擎自动调用， 用于完成流程的各种业务逻辑。
 * </p>
 *
 * @author pigx
 * @since 2023
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/remote")
public class ProcessRemoteController {

	private final IRemoteService remoteService;

	/**
	 * 根据角色ID列表查询用户ID列表
	 * <p>
	 * 查询拥有指定角色的所有用户ID。该接口主要用于流程节点配置中 按角色分配任务时，获取对应角色下的所有用户。
	 * </p>
	 * @param roleIdList 角色ID列表
	 * @return R<List<Long>> 统一响应对象，包含用户ID列表
	 */
	@PostMapping("queryUserIdListByRoleIdList")
	public R<List<Long>> queryUserIdListByRoleIdList(@RequestBody List<Long> roleIdList) {
		return remoteService.queryUserIdListByRoleIdList(roleIdList);
	}

	/**
	 * 保存流程抄送记录
	 * <p>
	 * 在流程执行过程中，当节点配置了抄送人时， 调用此接口保存抄送记录。被抄送人可以在抄送列表中查看流程信息。
	 * </p>
	 * @param copyDto 抄送数据对象，包含流程实例ID、抄送人、被抄送人等信息
	 * @return R 统一响应对象
	 */
	@PostMapping("savecc")
	public R saveCC(@RequestBody ProcessCopyDto copyDto) {
		return remoteService.saveCC(copyDto);
	}

	/**
	 * 检查是否为所有父级部门
	 * <p>
	 * 验证指定部门是否包含了某些部门的所有父级部门。 用于流程中按部门层级分配任务时的权限验证。
	 * </p>
	 * @param checkParentDto 检查参数，包含需要验证的部门信息
	 * @return R<Boolean> 统一响应对象，true-是所有父级，false-不是
	 */
	@PostMapping("checkIsAllParent")
	public R<Boolean> checkIsAllParent(@RequestBody CheckParentDto checkParentDto) {
		return remoteService.checkIsAllParent(checkParentDto);
	}

	/**
	 * 根据部门ID列表查询用户ID列表
	 * <p>
	 * 查询指定部门下的所有用户ID。该接口主要用于流程节点配置中 按部门分配任务时，获取对应部门下的所有用户。
	 * </p>
	 * @param depIdList 部门ID列表
	 * @return R<List<Long>> 统一响应对象，包含用户ID列表
	 */
	@PostMapping("queryUserIdListByDepIdList")
	public R<List<Long>> queryUserIdListByDepIdList(@RequestBody List<Long> depIdList) {
		return remoteService.queryUserIdListByDepIdList(depIdList);
	}

	/**
	 * 检查是否为所有子级部门
	 * <p>
	 * 验证指定部门是否包含了某些部门的所有子级部门。 用于流程中按部门层级分配任务时的权限验证。
	 * </p>
	 * @param checkChildDto 检查参数，包含需要验证的部门信息
	 * @return R<Boolean> 统一响应对象，true-是所有子级，false-不是
	 */
	@PostMapping("checkIsAllChild")
	public R<Boolean> checkIsAllChild(@RequestBody CheckChildDto checkChildDto) {
		return remoteService.checkIsAllChild(checkChildDto);
	}

	/**
	 * 获取用户全部信息
	 * <p>
	 * 查询用户的完整信息，包括基本信息和扩展字段。 扩展字段可能包括部门、角色、上级领导等信息， 这些信息在流程执行中用于人员分配和权限判断。
	 * </p>
	 * @param userId 用户ID
	 * @return R<Map<String, Object>> 统一响应对象，包含用户的所有信息
	 */
	@GetMapping("queryUserAllInfo")
	public R<Map<String, Object>> queryUserAllInfo(long userId) {
		return remoteService.queryUserAllInfo(userId);
	}

	/**
	 * 处理节点开始事件
	 * <p>
	 * 当流程节点开始执行时触发此事件。 记录节点开始时间、状态等信息，并可执行相关业务逻辑。
	 * </p>
	 * @param recordParamDto 节点记录参数，包含节点ID、流程实例ID等
	 * @return R 统一响应对象
	 */
	@PostMapping("startNodeEvent")
	public R startNodeEvent(@RequestBody ProcessNodeRecordParamDto recordParamDto) {
		return remoteService.startNodeEvent(recordParamDto);
	}

	/**
	 * 处理流程创建事件
	 * <p>
	 * 当新的流程实例被创建时触发此事件。 记录流程实例的创建信息，包括发起人、发起时间、表单数据等。
	 * </p>
	 * @param processInstanceRecordParamDto 流程实例记录参数
	 * @return R 统一响应对象
	 */
	@PostMapping("createProcessEvent")
	public R createProcessEvent(@RequestBody ProcessInstanceRecordParamDto processInstanceRecordParamDto) {
		return remoteService.createProcessEvent(processInstanceRecordParamDto);
	}

	/**
	 * 处理节点结束事件
	 * <p>
	 * 当流程节点执行完成时触发此事件。 更新节点状态、结束时间等信息，并可执行后续业务逻辑。
	 * </p>
	 * @param recordParamDto 节点记录参数，包含节点ID、流程实例ID等
	 * @return R 统一响应对象
	 */
	@PostMapping("endNodeEvent")
	public R endNodeEvent(@RequestBody ProcessNodeRecordParamDto recordParamDto) {
		return remoteService.endNodeEvent(recordParamDto);
	}

	/**
	 * 设置任务执行人
	 * <p>
	 * 在任务创建时设置任务的执行人。 根据节点配置的人员分配规则，确定并记录任务的实际处理人。
	 * </p>
	 * @param processNodeRecordAssignUserParamDto 任务分配参数，包含任务ID、用户ID等
	 * @return R 统一响应对象
	 */
	@PostMapping("startAssignUser")
	public R startAssignUser(@RequestBody ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
		return remoteService.startAssignUser(processNodeRecordAssignUserParamDto);
	}

	/**
	 * 处理任务结束事件
	 * <p>
	 * 当任务被完成时触发此事件。 更新任务状态、处理结果、处理时间等信息， 并可执行相关的业务通知或后续处理。
	 * </p>
	 * @param processNodeRecordAssignUserParamDto 任务结束参数，包含任务ID、处理结果等
	 * @return R 统一响应对象
	 */
	@PostMapping("taskEndEvent")
	public R taskEndEvent(@RequestBody ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto) {
		return remoteService.taskEndEvent(processNodeRecordAssignUserParamDto);
	}

	/**
	 * 处理流程实例结束事件
	 * <p>
	 * 当整个流程实例执行完成时触发此事件。 更新流程实例状态为已完成，记录结束时间， 并可执行流程结束后的业务处理。
	 * </p>
	 * @param processInstanceParamDto 流程实例参数
	 * @return R 统一响应对象
	 */
	@PostMapping("endProcess")
	public R endProcess(@RequestBody ProcessInstanceParamDto processInstanceParamDto) {
		return remoteService.endProcess(processInstanceParamDto);
	}

	/**
	 * 查询流程管理员
	 * <p>
	 * 获取指定流程的管理员用户ID。 流程管理员拥有该流程的特殊权限， 如强制终止流程、调整流程路径等。
	 * </p>
	 * @param flowId 流程定义ID
	 * @return R<Long> 统一响应对象，包含管理员用户ID
	 */
	@GetMapping("queryProcessAdmin")
	public R<Long> queryProcessAdmin(String flowId) {
		return remoteService.queryProcessAdmin(flowId);
	}

}
