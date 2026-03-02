
package com.pig4cloud.pigx.flow.service;

import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.dto.*;

import java.util.List;
import java.util.Map;

/**
 * 远程服务调用接口
 * <p>
 * 该接口定义了流程模块与其他业务模块之间的远程调用契约。主要包括用户组织架构查询、
 * 流程事件通知、权限校验等功能。这些方法通过Feign客户端调用其他微服务，实现了
 * 流程引擎与业务系统的解耦。所有远程调用都进行了统一的异常处理和降级处理。
 * </p>
 *
 * @author pig code generator
 * @date 2023-07-11
 */
public interface IRemoteService {

	/**
	 * 根据角色ID列表查询用户ID列表
	 * <p>
	 * 调用用户管理服务，查询指定角色下的所有用户。主要用于流程节点配置了
	 * 角色审批时，动态获取该角色下的所有用户作为候选审批人。支持多个角色
	 * 的联合查询，返回去重后的用户ID列表。
	 * </p>
	 *
	 * @param roleIdList 角色ID列表，支持批量查询多个角色
	 * @return R<List<Long>> 响应结果，包含角色下所有用户的ID列表（已去重）
	 */
	R<List<Long>> queryUserIdListByRoleIdList(List<Long> roleIdList);

	/**
	 * 保存流程抄送记录
	 * <p>
	 * 当流程执行到抄送节点时，调用此接口创建抄送记录。抄送记录会保存到
	 * 独立的抄送表中，被抄送人可以在抄送列表中查看但不参与审批。该接口
	 * 会同时发送抄送通知给相关人员。
	 * </p>
	 *
	 * @param copyDto 抄送信息对象，包含：
	 *                - processInstanceId: 流程实例ID
	 *                - nodeId: 抄送节点ID
	 *                - userId: 被抄送人ID
	 *                - formData: 表单数据
	 *                - copyTime: 抄送时间
	 * @return R 响应结果，成功返回抄送记录ID
	 */
	R saveCC(ProcessCopyDto copyDto);

	/**
	 * 检查用户是否为指定用户的所有上级
	 * <p>
	 * 用于流程节点配置了"部门负责人"或"上级领导"审批时，验证候选人
	 * 是否符合组织架构关系。通过递归查询组织树，判断是否存在上下级关系。
	 * </p>
	 *
	 * @param checkParentDto 检查参数，包含：
	 *                       - userId: 要检查的用户ID
	 *                       - targetUserId: 目标用户ID
	 *                       - checkType: 检查类型（直接上级/所有上级）
	 * @return R<Boolean> 响应结果，true表示是上级关系，false表示不是
	 */
	R<Boolean> checkIsAllParent(CheckParentDto checkParentDto);

	/**
	 * 根据部门ID列表查询用户ID列表
	 * <p>
	 * 调用组织架构服务，查询指定部门下的所有用户。用于流程节点配置了
	 * 部门审批时，获取该部门的所有成员作为候选审批人。支持多部门查询
	 * 和递归查询子部门。
	 * </p>
	 *
	 * @param depIdList 部门ID列表，支持批量查询多个部门
	 * @return R<List<Long>> 响应结果，包含部门下所有用户的ID列表（已去重）
	 */
	R<List<Long>> queryUserIdListByDepIdList(List<Long> depIdList);

    /**
     * 根据岗位ID列表查询用户ID列表
     *
     * @param postIdList 岗位ID列表，支持批量查询多个岗位
     * @return R<List<Long>> 响应结果，包含岗位下所有用户的ID列表（已去重）
     */
    R<List<Long>> queryUserIdListByPostIdList(List<Long> postIdList);

	/**
	 * 检查用户是否为指定用户的所有下级
	 * <p>
	 * 与checkIsAllParent相反，用于验证下级关系。主要用于某些特殊场景，
	 * 如向下委托、逐级审批等。通过递归查询组织树判断层级关系。
	 * </p>
	 *
	 * @param checkChildDto 检查参数，包含：
	 *                      - userId: 要检查的用户ID
	 *                      - targetUserId: 目标用户ID
	 *                      - checkLevel: 检查层级（直接下级/所有下级）
	 * @return R<Boolean> 响应结果，true表示是下级关系，false表示不是
	 */
	R<Boolean> checkIsAllChild(CheckChildDto checkChildDto);

	/**
	 * 获取用户的完整信息（包括扩展字段）
	 * <p>
	 * 查询用户的详细信息，除基本信息外，还包括部门、角色、岗位等扩展信息。
	 * 这些信息用于流程执行时的条件判断、权限验证等。返回的Map结构便于
	 * 在表达式中直接使用。
	 * </p>
	 *
	 * @param userId 用户ID
	 * @return R<Map<String, Object>> 响应结果，包含用户的所有信息：
	 *                                - 基本信息（姓名、工号、邮箱等）
	 *                                - 组织信息（部门、岗位、上级等）
	 *                                - 权限信息（角色、权限标识等）
	 *                                - 扩展字段（自定义属性）
	 */
	R<Map<String, Object>> queryUserAllInfo(long userId);

	/**
	 * 流程节点开始执行事件通知
	 * <p>
	 * 当流程节点开始执行时触发此事件，用于通知相关系统和人员。可以在此
	 * 扩展自定义的业务逻辑，如发送消息、记录日志、触发其他流程等。
	 * </p>
	 *
	 * @param recordParamDto 节点记录参数，包含节点执行的上下文信息
	 * @return R 响应结果，事件处理状态
	 */
	R startNodeEvent(ProcessNodeRecordParamDto recordParamDto);

	/**
	 * 流程实例创建事件通知
	 * <p>
	 * 当新的流程实例被创建时触发此事件。用于初始化流程相关的业务数据，
	 * 发送流程启动通知，记录审计日志等。是流程生命周期的起始事件。
	 * </p>
	 *
	 * @param processInstanceRecordParamDto 流程实例参数，包含：
	 *                                      - processInstanceId: 流程实例ID
	 *                                      - processDefinitionId: 流程定义ID
	 *                                      - businessKey: 业务关联键
	 *                                      - startUserId: 发起人ID
	 *                                      - variables: 流程变量
	 * @return R 响应结果，事件处理状态
	 */
	R createProcessEvent(ProcessInstanceRecordParamDto processInstanceRecordParamDto);

	/**
	 * 流程节点结束事件通知
	 * <p>
	 * 当流程节点执行完成时触发此事件。可以在此处理节点完成后的业务逻辑，
	 * 如更新业务状态、发送完成通知、触发下游系统等。
	 * </p>
	 *
	 * @param recordParamDto 节点记录参数，包含节点执行结果等信息
	 * @return R 响应结果，事件处理状态
	 */
	R endNodeEvent(ProcessNodeRecordParamDto recordParamDto);

	/**
	 * 开始分配任务执行人事件
	 * <p>
	 * 当系统开始为任务分配执行人时触发。用于发送任务通知、记录分配日志等。
	 * 在多人审批场景下，会为每个执行人触发一次该事件。
	 * </p>
	 *
	 * @param processNodeRecordAssignUserParamDto 执行人分配参数，包含：
	 *                                            - userId: 被分配的用户ID
	 *                                            - taskId: 任务ID
	 *                                            - assignTime: 分配时间
	 * @return R 响应结果，事件处理状态
	 */
	R startAssignUser(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto);

	/**
	 * 任务完成事件通知
	 * <p>
	 * 当用户完成分配给自己的任务时触发。用于更新业务状态、发送完成通知、
	 * 记录操作日志等。对于会签场景，每个人完成都会触发一次。
	 * </p>
	 *
	 * @param processNodeRecordAssignUserParamDto 任务完成参数，包含：
	 *                                            - userId: 完成任务的用户ID
	 *                                            - taskId: 任务ID
	 *                                            - result: 处理结果
	 *                                            - comment: 处理意见
	 * @return R 响应结果，事件处理状态
	 */
	R taskEndEvent(ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto);

	/**
	 * 流程实例结束事件通知
	 * <p>
	 * 当整个流程实例结束时触发，包括正常结束和异常终止。用于清理流程数据、
	 * 更新最终业务状态、发送结束通知、归档流程数据等。
	 * </p>
	 *
	 * @param processInstanceParamDto 流程实例参数，包含：
	 *                                - processInstanceId: 流程实例ID
	 *                                - endReason: 结束原因
	 *                                - endType: 结束类型（正常/异常）
	 * @return R 响应结果，事件处理状态
	 */
	R endProcess(ProcessInstanceParamDto processInstanceParamDto);

	/**
	 * 查询流程的管理员
	 * <p>
	 * 获取指定流程的管理员用户ID。流程管理员拥有该流程的最高权限，
	 * 可以进行流程的编辑、删除、强制结束等操作。通常在权限校验时使用。
	 * </p>
	 *
	 * @param flowId 流程定义ID
	 * @return R<Long> 响应结果，包含流程管理员的用户ID，如果未设置则返回null
	 */
	R<Long> queryProcessAdmin(String flowId);

}
