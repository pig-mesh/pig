package com.pig4cloud.pigx.flow.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.constant.enums.YesNoEnum;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.data.tenant.TenantContextHolder;
import com.pig4cloud.pigx.common.sequence.sequence.Sequence;
import com.pig4cloud.pigx.flow.constant.FormTypeEnum;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.NodeUser;
import com.pig4cloud.pigx.flow.entity.Process;
import com.pig4cloud.pigx.flow.entity.ProcessStarter;
import com.pig4cloud.pigx.flow.event.IProcessInstanceStatusEventService;
import com.pig4cloud.pigx.flow.mapper.ProcessMapper;
import com.pig4cloud.pigx.flow.service.IProcessService;
import com.pig4cloud.pigx.flow.service.IProcessStarterService;
import com.pig4cloud.pigx.flow.support.utils.ModelUtil;
import com.pig4cloud.pigx.flow.support.utils.NodeUtil;
import com.pig4cloud.pigx.flow.vo.FormItemVO;
import com.pig4cloud.pigx.flow.vo.ProcessVO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.Deployment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * 流程定义服务实现类
 * <p>
 * 实现流程定义的核心业务逻辑，包括流程的创建、编辑、查询、部署等功能。 该类是流程管理的核心实现，负责： - 流程定义的CRUD操作 - 流程表单的动态配置管理 -
 * 流程节点的解析和验证 - 与Flowable引擎的交互部署 - 流程权限的管理和控制
 * </p>
 *
 * @author cxygzl
 * @date 2026-02-10
 * @since 2023-05-25
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements IProcessService {

	private final IProcessStarterService processStarterService;

	private final RepositoryService repositoryService;

	private final ObjectMapper objectMapper;

	private final Sequence flowSequence;

	private final Optional<List<IProcessInstanceStatusEventService>> processInstanceStatusEventServicesOptional;

	/**
	 * 获取流程详细信息
	 * <p>
	 * 根据流程ID获取完整的流程配置信息，包括： - 流程基本信息 - 表单配置（自动生成序列号字段的值） - 需要发起人选择执行人的节点列表 - 表单权限配置
	 * </p>
	 * @param flowId 流程定义ID
	 * @return 包含流程详细信息的响应对象
	 */
	@Override
	public R<ProcessVO> getDetail(String flowId) {
		// 获取流程详情
		ProcessVO processVO = this.getProcessVO(flowId);
		return R.ok(processVO);
	}

	/**
	 * 获取流程视图对象
	 * <p>
	 * 内部方法，用于构建流程的视图对象。主要处理： - 解析流程节点结构 - 查找需要发起人选择的节点 - 处理序列号类型的表单字段，自动生成下一个序号 -
	 * 提取起始节点的表单权限配置
	 * </p>
	 * @param flowId 流程定义ID
	 * @return 流程视图对象，包含所有展示所需的信息
	 */
	@SneakyThrows
	private ProcessVO getProcessVO(String flowId) {

		Process oaForms = getByFlowId(flowId);
		String process = oaForms.getProcess();
		String formItems = oaForms.getFormItems();
		Node startNode = objectMapper.readValue(process, new TypeReference<>() {
		});

		List<String> selectUserNodeId = NodeUtil.selectUserNodeId(startNode);

		ProcessVO processVO = BeanUtil.copyProperties(oaForms, ProcessVO.class);
		processVO.setSelectUserNodeId(selectUserNodeId);

		JSONArray formItemJsonArray = JSONUtil.parseArray(formItems);
		// 特殊字段复制
		for (Object object : formItemJsonArray) {
			if (object instanceof JSONObject) {
				JSONObject formVO = (JSONObject) object;
				JSONObject props = formVO.getJSONObject(FormItemVO.Fields.props);
				if (Objects.nonNull(props)
						&& FormTypeEnum.SEQUENCE.getType().equals(props.getStr(FormItemVO.Fields.type))) {
					String nextNo = flowSequence.nextNo();
					props.set(FormItemVO.Props.Fields.value, nextNo);
					formVO.set(FormItemVO.Props.Fields.value, nextNo);
				}
			}
		}

		processVO.setFormItems(formItemJsonArray.toStringPretty());
		processVO.setFormPerms(startNode.getFormPerms());
		return processVO;
	}

	/**
	 * 根据流程ID查询流程定义
	 * <p>
	 * 通过流程的唯一标识符查询流程定义实体。 注意：一个流程可能有多个版本，此方法返回当前有效版本。
	 * </p>
	 * @param flowId 流程定义ID
	 * @return 流程定义实体，如果不存在返回null
	 */
	@Override
	public Process getByFlowId(String flowId) {
		return this.lambdaQuery().eq(Process::getFlowId, flowId).one();
	}

	/**
	 * 根据流程ID更新流程定义
	 * <p>
	 * 更新流程的配置信息，包括表单、节点、权限等。 更新不会影响已经运行的流程实例。
	 * </p>
	 * @param process 包含更新信息的流程实体
	 */
	@Override
	public void updateByFlowId(Process process) {
		this.lambdaUpdate().eq(Process::getFlowId, process.getFlowId()).update(process);
	}

	/**
	 * 隐藏流程定义
	 * <p>
	 * 将流程标记为隐藏状态，隐藏的流程： - 不在流程列表中显示 - 不能创建新的流程实例 - 已有的流程实例可以继续执行
	 * </p>
	 * @param flowId 要隐藏的流程ID
	 */
	@Override
	public void hide(String flowId) {
		this.lambdaUpdate()
			.set(Process::getHidden, YesNoEnum.YES.getCode())
			.eq(Process::getFlowId, flowId)
			.update(new Process());
	}

	/**
	 * 创建新的流程定义
	 * <p>
	 * 创建流程的完整流程： 1. 调用流程引擎创建BPMN模型 2. 如果是更新操作，隐藏旧版本 3. 保存流程定义到数据库 4. 设置流程发起人权限 5.
	 * 生成流程使用范围描述
	 * </p>
	 * @param process 流程定义实体，包含流程的所有配置信息
	 * @return 创建结果，成功返回流程ID，失败返回错误信息
	 */
	@SneakyThrows
	@Override
	public R create(Process process) {
		String flowId;
		if (StrUtil.isNotBlank(process.getFlowId())) {
			flowId = process.getFlowId();
		}
		else {
			flowId = "P" + DateUtil.format(new Date(), DatePattern.PURE_DATE_PATTERN)
					+ RandomUtil.randomString(5).toUpperCase();
		}

		BpmnModel bpmnModel = ModelUtil.buildBpmnModel(objectMapper.readValue(process.getProcess(), Node.class),
				process.getName(), flowId);
		{
			byte[] bpmnBytess = new BpmnXMLConverter().convertToXML(bpmnModel);
			String filename = "/tmp/flowable-deployment/" + flowId + ".bpmn20.xml";
			log.debug("部署时的模型文件：{}", filename);
			FileUtil.writeBytes(bpmnBytess, filename);
		}

		Process oldProcess = this.getByFlowId(flowId);

		Deployment deployment = repositoryService.createDeployment()
			.addBpmnModel(StrUtil.format("{}.bpmn20.xml", "pig"), bpmnModel)
			.tenantId(TenantContextHolder.getTenantId().toString())
			.deploy();

		NodeUser nodeUser = objectMapper.readValue(process.getAdminList(), new TypeReference<List<NodeUser>>() {
		}).get(0);

		// 更新流程
		if (StrUtil.isNotBlank(process.getFlowId()) && Objects.nonNull(oldProcess)) {
			this.hide(process.getFlowId());
			// 修改所有的管理员
			this.lambdaUpdate()
				.set(Process::getAdminId, nodeUser.getId())
				.eq(Process::getUniqueId, oldProcess.getUniqueId())
				.update(new Process());

		}

		Node startNode = objectMapper.readValue(process.getProcess(), Node.class);

		List<NodeUser> nodeUserList = startNode.getNodeUserList();

		StringBuilder stringBuilder = new StringBuilder();
		if (CollUtil.isNotEmpty(nodeUserList)) {
			int index = 0;

			for (NodeUser user : nodeUserList) {
				if (index > 0) {
					stringBuilder.append(",");
				}
				stringBuilder.append(user.getName());
				index++;
				if (index > 5) {
					break;
				}

			}
		}
		Process p = new Process();

		if (Objects.nonNull(oldProcess)) {
			p = oldProcess;
		}

		p.setFlowId(flowId);
		p.setName(process.getName());
		p.setLogo(process.getLogo());
		p.setSettings(process.getSettings());
		p.setGroupId(process.getGroupId());
		p.setFormConfig(process.getFormConfig());
		p.setFormItems(process.getFormItems());
		p.setProcess(process.getProcess());
		p.setRemark(process.getRemark());
		p.setSort(0);
		p.setHidden("0");
		p.setStop("0");
		p.setAdminId(nodeUser.getId());
		p.setUniqueId(deployment.getId());
		p.setAdminList(process.getAdminList());
		p.setRangeShow(stringBuilder.toString());
		this.saveOrUpdate(p);

		// 删除原有发起范围
		processStarterService
			.remove(Wrappers.<ProcessStarter>lambdaQuery().eq(ProcessStarter::getProcessId, p.getId()));
		for (NodeUser nodeUserDto : nodeUserList) {
			ProcessStarter processStarter = new ProcessStarter();
			processStarter.setProcessId(p.getId());
			processStarter.setTypeId(nodeUserDto.getId());
			processStarter.setType(nodeUserDto.getType());
			processStarterService.save(processStarter);
		}

		return R.ok();
	}

	/**
	 * 更新流程状态或分组
	 * <p>
	 * 支持三种操作类型： - stop：停用流程（设置stop=1） - delete：删除流程（逻辑删除，设置hidden=1） - 其他：启用流程（设置stop=0）
	 * 同时支持修改流程所属分组
	 * </p>
	 * @param flowId 流程定义ID
	 * @param type 操作类型：stop（停用）、delete（删除）、其他（启用）
	 * @param groupId 流程分组ID，可选参数
	 * @return 操作结果
	 */
	@Override
	public R update(String flowId, String type, Long groupId) {
		Process process = new Process();
		process.setFlowId(flowId);
		process.setStop("stop".equals(type) ? "1" : "0");
		process.setHidden("delete".equals(type) ? "1" : "0");
		process.setGroupId(groupId);
		this.updateByFlowId(process);
		return R.ok();
	}

	/**
	 * 验证流程ID是否在所有IProcessInstanceStatusEventService实现类中存在
	 * <p>
	 * 遍历所有实现了IProcessInstanceStatusEventService接口的服务类，检查输入的flowId
	 * 是否与任意一个服务的getFlowId()返回值匹配。如果匹配则返回true，否则返回false。
	 * </p>
	 *
	 * @param flowId 流程定义ID
	 * @return 验证结果：true表示存在，false表示不存在
	 */
	@Override
	public R<Boolean> validateFlowId(String flowId) {
		// 如果flowId为空，直接返回false
		if (StrUtil.isBlank(flowId)) {
			return R.ok(false);
		}

		// 如果没有IProcessInstanceStatusEventService实现类，返回false
		if (processInstanceStatusEventServicesOptional.isEmpty()) {
			log.warn("未找到任何IProcessInstanceStatusEventService实现类");
			return R.ok(false);
		}

		// 遍历所有实现类，检查flowId是否存在
		List<IProcessInstanceStatusEventService> serviceList = processInstanceStatusEventServicesOptional.get();
		boolean exists = serviceList.stream()
			.anyMatch(service -> flowId.equals(service.getFlowId()));

		return R.ok(exists);
	}

}
