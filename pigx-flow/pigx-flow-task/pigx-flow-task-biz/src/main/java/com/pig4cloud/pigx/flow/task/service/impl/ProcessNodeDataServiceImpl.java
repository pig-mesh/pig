package com.pig4cloud.pigx.flow.task.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.dto.ProcessNodeDataDto;
import com.pig4cloud.pigx.flow.task.entity.ProcessNodeData;
import com.pig4cloud.pigx.flow.task.mapper.ProcessNodeDataMapper;
import com.pig4cloud.pigx.flow.task.service.IProcessNodeDataService;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <p>
 * 流程节点数据 服务实现类
 * </p>
 *
 * @author Vincent
 * @since 2023-05-07
 */
@Service
public class ProcessNodeDataServiceImpl extends ServiceImpl<ProcessNodeDataMapper, ProcessNodeData>
		implements IProcessNodeDataService {

	/**
	 * 保存流程节点数据。
	 * @param processNodeDataDto 流程节点数据DTO
	 * @return 保存结果
	 */
	@Override
	public R saveNodeData(ProcessNodeDataDto processNodeDataDto) {
		ProcessNodeData processNodeData = BeanUtil.copyProperties(processNodeDataDto, ProcessNodeData.class);
		this.save(processNodeData);
		return R.ok();
	}

	/**
	 * 获取节点数据。
	 * @param flowId 流程ID
	 * @param nodeId 节点ID
	 * @return 节点数据
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
