package com.pig4cloud.pigx.flow.task.api.feign;

import com.pig4cloud.pigx.common.core.constant.ServiceNameConstants;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author lengleng
 * @date 2023/7/14
 */
@FeignClient(contextId = "remoteFlowTaskService", value = ServiceNameConstants.FLOW_TASK_SERVER)
public interface RemoteFlowTaskService {

	/**
	 * 节点开始事件
	 * @param nodeRecordParamDto
	 */
	@PostMapping("/remote/startNodeEvent")
	void startNodeEvent(@RequestBody ProcessNodeRecordParamDto nodeRecordParamDto);

	/**
	 * 节点结束事件
	 * @param nodeRecordParamDto
	 */
	@PostMapping("/remote/endNodeEvent")
	void endNodeEvent(@RequestBody ProcessNodeRecordParamDto nodeRecordParamDto);

	/**
	 * 流程结束事件
	 * @param processInstanceParamDto
	 */
	@PostMapping("/remote/endProcess")
	void endProcessEvent(@RequestBody ProcessInstanceParamDto processInstanceParamDto);

	/**
	 * 创建流程事件
	 * @param processInstanceRecordParamDto
	 */
	@PostMapping("/remote/createProcessEvent")
	void createProcessEvent(@RequestBody ProcessInstanceRecordParamDto processInstanceRecordParamDto);

	/**
	 * 根据角色id集合查询用户id集合
	 * @param roleIdList
	 */
	@PostMapping("/remote/queryUserIdListByRoleIdList")
	R<List<String>> queryUserIdListByRoleIdList(@RequestBody List<String> roleIdList);

	/**
	 * 根据部门id集合查询所有的用户id集合
	 * @param deptIdList
	 */
	@PostMapping("/remote/queryUserIdListByDepIdList")
	R<List<String>> queryUserIdListByDepIdList(@RequestBody List<String> deptIdList);

	/**
	 * 查询流程管理员
	 * @param flowId 流程id
	 */
	@GetMapping("/remote/queryProcessAdmin")
	R<Long> queryProcessAdmin(@RequestParam("flowId") String flowId);

	/**
	 * 查询节点数据
	 * @param flowId 流程id
	 * @param nodeId 节点id
	 * @return
	 */
	@GetMapping("/processNodeData/getNodeData")
	R<Node> queryNodeOriData(@RequestParam("flowId") String flowId, @RequestParam("nodeId") String nodeId);

	/**
	 * 节点开始指派用户了
	 * @param processNodeRecordAssignUserParamDto
	 */
	@PostMapping("/remote/startAssignUser")
	R startAssignUser(@RequestBody ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto);

	/**
	 * 任务结束事件
	 * @param processNodeRecordAssignUserParamDto
	 */
	@PostMapping("/remote/taskEndEvent")
	R taskEndEvent(@RequestBody ProcessNodeRecordAssignUserParamDto processNodeRecordAssignUserParamDto);

	/**
	 * 保存抄送数据
	 * @param processCopyDto
	 */
	@PostMapping("/remote/savecc")
	R saveCC(@RequestBody ProcessCopyDto processCopyDto);

	/**
	 * 保存节点原始数据
	 * @param processNodeDataDto
	 */
	@PostMapping("/processNodeData/saveNodeData")
	R saveNodeOriData(@RequestBody ProcessNodeDataDto processNodeDataDto);

}
