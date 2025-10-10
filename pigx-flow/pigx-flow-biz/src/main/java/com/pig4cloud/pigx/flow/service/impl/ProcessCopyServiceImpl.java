package com.pig4cloud.pigx.flow.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.entity.Process;
import com.pig4cloud.pigx.flow.entity.ProcessCopy;
import com.pig4cloud.pigx.flow.mapper.ProcessCopyMapper;
import com.pig4cloud.pigx.flow.service.IProcessCopyService;
import com.pig4cloud.pigx.flow.service.IProcessNodeDataService;
import com.pig4cloud.pigx.flow.service.IProcessService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 流程抄送数据服务实现类
 * <p>
 * 该类负责处理流程抄送相关的业务逻辑，主要功能包括：
 * 1. 管理流程抄送记录的增删改查
 * 2. 查询抄送详情，包括流程表单、表单数据和权限信息
 * 3. 为被抄送人提供查看流程执行情况的能力
 * 
 * 抄送功能允许流程在执行过程中通知相关人员，被抄送人可以查看但不参与流程审批
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
     * <p>
     * 根据抄送记录ID查询完整的抄送详情，包括：
     * 1. 流程定义信息（process）
     * 2. 表单项定义（formItems）
     * 3. 表单数据（formData）- 流程实例的实际填写数据
     * 4. 表单权限（formPerms）- 基于当前节点的表单字段权限设置
     * 
     * 该方法主要用于抄送人查看被抄送的流程详情
     * </p>
     * 
     * @param id 抄送记录ID
     * @return R 包含流程详情的响应对象，包括process、formItems、formData、formPerms
     * @throws Exception 当解析JSON数据失败时抛出异常
     */
    @SneakyThrows
    @Override
    public R querySingleDetail(long id) {
        // 获取抄送记录
        ProcessCopy processCopy = this.getById(id);
        String flowId = processCopy.getFlowId();
        
        // 查询流程定义
        Process oaForms = processService.getByFlowId(flowId);
        if (oaForms == null) {
            return R.failed("流程不存在");
        }
        
        // 获取表单数据和节点信息
        String formData = processCopy.getFormData();
        String nodeId = processCopy.getNodeId();

        // 获取节点的表单权限配置
        Node node = nodeDataService.getNodeData(flowId, nodeId).getData();
        Map<String, String> formPerms = node.getFormPerms();

        // 构建返回数据
        Dict set = Dict.create()
                .set("process", oaForms.getProcess())      // 流程定义
                .set("formItems", oaForms.getFormItems())  // 表单项定义
                .set("formData", formData)                  // 表单数据
                .set("formPerms", formPerms);               // 表单权限
        return R.ok(set);
    }

}
