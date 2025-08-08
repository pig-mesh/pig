package com.pig4cloud.pigx.flow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.ProcessNodeDataDto;
import com.pig4cloud.pigx.flow.entity.ProcessNodeData;
import com.pig4cloud.pigx.flow.mapper.ProcessNodeDataMapper;
import com.pig4cloud.pigx.flow.service.IProcessNodeDataService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 流程节点数据服务实现类
 * <p>
 * 该类负责管理流程节点的详细数据，主要功能包括： 1. 保存流程节点的配置数据（审批人、表单权限、条件等） 2. 查询指定节点的详细配置信息 3.
 * 为流程执行提供节点配置数据支持
 * 
 * 节点数据以JSON格式存储，包含节点类型、审批人配置、表单权限等信息
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@Service
public class ProcessNodeDataServiceImpl extends ServiceImpl<ProcessNodeDataMapper, ProcessNodeData>
		implements IProcessNodeDataService {

	/**
	 * 保存流程节点数据
	 * <p>
	 * 将流程节点的配置信息保存到数据库。节点数据包括： - 节点类型（审批、抄送、条件等） - 审批人配置 - 表单权限设置 - 条件表达式等
	 * </p>
	 * @param processNodeDataDto 流程节点数据DTO，包含flowId、nodeId和data（JSON格式的节点配置）
	 * @return R 保存结果
	 */
	@Override
	public R saveNodeData(ProcessNodeDataDto processNodeDataDto) {
		ProcessNodeData processNodeData = BeanUtil.copyProperties(processNodeDataDto, ProcessNodeData.class);
		baseMapper.delete(Wrappers.<ProcessNodeData>lambdaQuery()
			.eq(ProcessNodeData::getNodeId, processNodeData.getNodeId())
			.eq(ProcessNodeData::getFlowId, processNodeData.getFlowId()));
		baseMapper.insert(processNodeData);
		return R.ok();
	}

	/**
	 * 获取节点数据
	 * <p>
	 * 根据流程ID和节点ID查询节点的详细配置信息。 节点数据存储为JSON格式，查询后会自动转换为Node对象
	 * </p>
	 * @param flowId 流程ID
	 * @param nodeId 节点ID
	 * @return R<Node> 包含节点配置的Node对象，如果节点不存在则返回null
	 */
	@Override
	public R<Node> getNodeData(String flowId, String nodeId) {
		Optional<ProcessNodeData> processNodeDataOptional = this.lambdaQuery()
			.eq(ProcessNodeData::getFlowId, flowId)
			.eq(ProcessNodeData::getNodeId, nodeId)
			.oneOpt();

		Node node = processNodeDataOptional
			.map(processNodeData -> JSONUtil.toBean(processNodeData.getData(), Node.class))
			.orElse(null);

		return R.ok(node);
	}

}
