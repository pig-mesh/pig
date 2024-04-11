package com.pig4cloud.pigx.flow.task.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.common.sequence.sequence.Sequence;
import com.pig4cloud.pigx.flow.task.api.feign.RemoteFlowEngineService;
import com.pig4cloud.pigx.flow.task.constant.FormTypeEnum;
import com.pig4cloud.pigx.flow.task.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.dto.NodeUser;
import com.pig4cloud.pigx.flow.task.entity.Process;
import com.pig4cloud.pigx.flow.task.entity.ProcessStarter;
import com.pig4cloud.pigx.flow.task.mapper.ProcessMapper;
import com.pig4cloud.pigx.flow.task.service.IProcessService;
import com.pig4cloud.pigx.flow.task.service.IProcessStarterService;
import com.pig4cloud.pigx.flow.task.utils.NodeUtil;
import com.pig4cloud.pigx.flow.task.vo.FormItemVO;
import com.pig4cloud.pigx.flow.task.vo.ProcessVO;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author cxygzl
 * @since 2023-05-25
 */
@Service
@RequiredArgsConstructor
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements IProcessService {

    private final IProcessStarterService processStarterService;

    private final RemoteFlowEngineService flowEngineService;

    private final Sequence flowSequence;

    private final ObjectMapper objectMapper;

    /**
     * 获取详细数据
     *
     * @param flowId
     * @return
     */
    @Override
    public R<ProcessVO> getDetail(String flowId) {
        // 获取流程详情
        ProcessVO processVO = this.getProcessVO(flowId);
        return R.ok(processVO);
    }

    /**
     * 根据流程ID获取流程详情
     *
     * @param flowId 流程ID
     * @return 流程详情
     */
    @SneakyThrows
    private ProcessVO getProcessVO(String flowId) {

        Process oaForms = getByFlowId(flowId);
        String process = oaForms.getProcess();
        String formItems = oaForms.getFormItems();
        Node startNode = objectMapper.readValue(process, new TypeReference<>() {
        });

        Map<String, String> formPerms = startNode.getFormPerms();

        List<FormItemVO> formItemVOList = objectMapper.readValue(formItems, new TypeReference<>() {
        });
        for (FormItemVO formItemVO : formItemVOList) {
            String perm = MapUtil.getStr(formPerms, formItemVO.getId(), ProcessInstanceConstant.FormPermClass.EDIT);
            formItemVO.setPerm(perm);

            // 发号器赋值
            if (StrUtil.equals(formItemVO.getType(), FormTypeEnum.SEQUENCE.getType())) {
                FormItemVO.Props props = formItemVO.getProps();
                String prefix = props.getPrefix();
                String nextNo = flowSequence.nextNo();
                formItemVO.setPlaceholder(StrUtil.isNotBlank(prefix) ? prefix + nextNo : nextNo);
                props.setValue(StrUtil.isNotBlank(prefix) ? prefix + nextNo : nextNo);
                formItemVO.setProps(props);
            }

            if (StrUtil.equals(formItemVO.getType(), FormTypeEnum.LAYOUT.getType())) {
                // 明细
                Object value = formItemVO.getProps().getValue();
                List<FormItemVO> subList = Convert.toList(FormItemVO.class, value);
                for (FormItemVO itemVO : subList) {
                    String perm1 = MapUtil.getStr(formPerms, itemVO.getId(),
                            ProcessInstanceConstant.FormPermClass.EDIT);
                    itemVO.setPerm(perm1);
                }

                formItemVO.getProps().setValue(subList);
            }

        }
        oaForms.setFormItems(objectMapper.writeValueAsString(formItemVOList));

        List<String> selectUserNodeId = NodeUtil.selectUserNodeId(startNode);

        ProcessVO processVO = BeanUtil.copyProperties(oaForms, ProcessVO.class);
        processVO.setSelectUserNodeId(selectUserNodeId);

        return processVO;
    }

    /**
     * 根据流程ID获取流程
     *
     * @param flowId 流程ID
     * @return 流程
     */
    @Override
    public Process getByFlowId(String flowId) {
        return this.lambdaQuery().eq(Process::getFlowId, flowId).one();
    }

    /**
     * 更新流程
     *
     * @param process 流程
     */
    @Override
    public void updateByFlowId(Process process) {
        this.lambdaUpdate().eq(Process::getFlowId, process.getFlowId()).update(process);
    }

    /**
     * 隐藏流程
     *
     * @param flowId 流程ID
     */
    @Override
    public void hide(String flowId) {
        this.lambdaUpdate().set(Process::getHidden, true).eq(Process::getFlowId, flowId).update(new Process());
    }

    /**
     * 创建流程
     *
     * @param process 流程
     * @return 操作结果
     */
    @SneakyThrows
    @Override
    public R create(Process process) {
        Map<String, Object> map = new HashMap<>();
        map.put("process", process);
        map.put("userId", SecurityUtils.getUser().getId());
        R<String> r = flowEngineService.createFlow(map);
        if (!r.isOk()) {
            return R.failed(r.getMsg());
        }
        String flowId = r.getData();
        NodeUser nodeUser = objectMapper.readValue(process.getAdminList(), new TypeReference<List<NodeUser>>() {
        }).get(0);

        // 更新流程
        if (StrUtil.isNotBlank(process.getFlowId())) {

            Process oldProcess = this.getByFlowId(process.getFlowId());
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
        p.setFlowId(flowId);
        p.setName(process.getName());
        p.setLogo(process.getLogo());
        p.setSettings(process.getSettings());
        p.setGroupId(process.getGroupId());
        p.setFormItems(process.getFormItems());
        p.setProcess(process.getProcess());
        p.setRemark(process.getRemark());
        p.setSort(0);
        p.setHidden("0");
        p.setStop("0");
        p.setAdminId(nodeUser.getId());
        p.setUniqueId(IdUtil.fastSimpleUUID());
        p.setAdminList(process.getAdminList());
        p.setRangeShow(stringBuilder.toString());

        this.save(p);

        // 保存范围
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
     * 编辑表单
     *
     * @param flowId  流程ID
     * @param type    类型 stop using delete
     * @param groupId 分组ID
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

}
