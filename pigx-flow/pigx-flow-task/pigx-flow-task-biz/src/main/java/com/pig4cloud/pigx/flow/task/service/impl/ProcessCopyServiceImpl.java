package com.pig4cloud.pigx.flow.task.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.entity.Process;
import com.pig4cloud.pigx.flow.task.entity.ProcessCopy;
import com.pig4cloud.pigx.flow.task.mapper.ProcessCopyMapper;
import com.pig4cloud.pigx.flow.task.service.IProcessCopyService;
import com.pig4cloud.pigx.flow.task.service.IProcessNodeDataService;
import com.pig4cloud.pigx.flow.task.service.IProcessService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 流程抄送数据 服务实现类
 * </p>
 *
 * @author Vincent
 * @since 2023-05-20
 */
@Service
@RequiredArgsConstructor
public class ProcessCopyServiceImpl extends ServiceImpl<ProcessCopyMapper, ProcessCopy> implements IProcessCopyService {

    private final ObjectMapper objectMapper;

    private final IProcessService processService;

    private final IProcessNodeDataService nodeDataService;

    /**
     * 查询单个抄送详细信息
     * @param id
     * @return
     */
    @SneakyThrows
    @Override
    public R querySingleDetail(long id) {
        ProcessCopy processCopy = this.getById(id);
        String flowId = processCopy.getFlowId();
        Process oaForms = processService.getByFlowId(flowId);
        if (oaForms == null) {
            return R.failed("流程不存在");
        }
        String formData = processCopy.getFormData();
        String nodeId = processCopy.getNodeId();

        Node node = nodeDataService.getNodeData(flowId, nodeId).getData();
        Map<String, String> formPerms = node.getFormPerms();

        Dict set = Dict.create()
                .set("process", oaForms.getProcess())
                .set("formItems", oaForms.getFormItems())
                .set("formData", formData)
                .set("formPerms", formPerms);
        return R.ok(set);
    }

}
