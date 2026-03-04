package com.pig4cloud.pigx.flow.support.tasks;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.TriggerConfig;
import com.pig4cloud.pigx.flow.dto.TriggerParam;
import com.pig4cloud.pigx.flow.dto.TriggerReturnMapping;
import com.pig4cloud.pigx.flow.entity.ProcessInstanceRecord;
import com.pig4cloud.pigx.flow.service.IProcessInstanceRecordService;
import com.pig4cloud.pigx.flow.service.IProcessNodeDataService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.FlowableException;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.flowable.engine.impl.persistence.entity.ExecutionEntityImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 触发器服务任务
 * <p>
 * 在流程执行过程中调用外部 HTTP POST 接口，
 * 支持传递表单数据作为请求参数，并将返回值映射回流程变量。
 * <p>
 * HTTP 状态码 200 即视为调用成功，响应体按 JSON 对象解析后进行返回值映射。
 * 异常处理支持两种策略：THROW（抛出异常）和 TERMINATE（终止流程）。
 *
 * @author pigx
 * @date 2026-03-03
 */
@Slf4j
public class TriggerServiceTask implements JavaDelegate {

    private static final int READ_TIMEOUT = 30000;

    @SneakyThrows
    @Override
    public void execute(DelegateExecution execution) {
        ExecutionEntityImpl entity = (ExecutionEntityImpl) execution;
        String nodeId = entity.getActivityId();
        String flowId = entity.getProcessDefinitionKey();

        IProcessNodeDataService processNodeDataService = SpringUtil.getBean(IProcessNodeDataService.class);
        Node node = processNodeDataService.getNodeData(flowId, nodeId).getData();

        TriggerConfig config = node.getTriggerConfig();
        if (config == null || StrUtil.isBlank(config.getUrl())) {
            throw new FlowableException("触发器配置无效: URL为空");
        }

        // 构建请求头
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        if (CollUtil.isNotEmpty(config.getHeaders())) {
            for (TriggerParam param : config.getHeaders()) {
                String value = resolveParamValue(param, execution);
                if (StrUtil.isNotBlank(param.getKey()) && value != null) {
                    headerMap.put(param.getKey(), value);
                }
            }
        }

        // 构建请求体
        Map<String, Object> body = new HashMap<>();
        if (CollUtil.isNotEmpty(config.getBody())) {
            for (TriggerParam param : config.getBody()) {
                Object value = resolveParamObjectValue(param, execution);
                if (StrUtil.isNotBlank(param.getKey())) {
                    body.put(param.getKey(), value);
                }
            }
        }

        // 发起 POST 请求
        try {
            HttpResponse response = HttpRequest.post(config.getUrl())
                    .headerMap(headerMap, true)
                    .body(JSONUtil.toJsonStr(body))
                    .timeout(READ_TIMEOUT)
                    .execute();

            // HTTP 状态码非 2xx 视为调用失败
            if (!response.isOk()) {
                String msg = StrUtil.format("HTTP请求失败: status={}, body={}", response.getStatus(), response.body());
                log.warn("触发器返回异常: url={}, {}", config.getUrl(), msg);
                handleError(config.getErrorHandler(), execution, msg);
                return;
            }

            // 响应体按 JSON 对象解析，进行返回值映射
            String responseBody = response.body();
            if (StrUtil.isNotBlank(responseBody)) {
                ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);
                Object data = objectMapper.readValue(responseBody, Object.class);
                mapReturnValues(config.getReturnMapping(), data, execution);
            }
        } catch (Exception ex) {
            log.error("触发器调用异常: url={}, error={}", config.getUrl(), ex.getMessage());
            handleError(config.getErrorHandler(), execution, ex.getMessage());
        }
    }

    private String resolveParamValue(TriggerParam param, DelegateExecution execution) {
        Object value = resolveParamObjectValue(param, execution);
        return value != null ? value.toString() : null;
    }

    private Object resolveParamObjectValue(TriggerParam param, DelegateExecution execution) {
        if (Boolean.TRUE.equals(param.getUseForm()) && StrUtil.isNotBlank(param.getFormFieldId())) {
            return execution.getVariable(param.getFormFieldId());
        }
        return param.getValue();
    }

    @SneakyThrows
    @SuppressWarnings("unchecked")
    private void mapReturnValues(List<TriggerReturnMapping> mappings, Object data, DelegateExecution execution) {
        if (CollUtil.isEmpty(mappings) || data == null) {
            return;
        }

        Map<String, Object> dataMap;
        if (data instanceof Map) {
            dataMap = (Map<String, Object>) data;
        } else {
            ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);
            dataMap = objectMapper.convertValue(data, Map.class);
        }

        // 收集需要更新的表单字段
        Map<String, Object> formUpdates = new HashMap<>();
        for (TriggerReturnMapping mapping : mappings) {
            Object value = dataMap.get(mapping.getResponseKey());
            if (value != null && StrUtil.isNotBlank(mapping.getFormFieldId())) {
                execution.setVariable(mapping.getFormFieldId(), value);
                formUpdates.put(mapping.getFormFieldId(), value);
            }
        }

        // 同步更新 ProcessInstanceRecord.formData
        if (CollUtil.isNotEmpty(formUpdates)) {
            syncFormData(execution.getProcessInstanceId(), formUpdates);
        }
    }

    /**
     * 同步更新流程实例记录的表单数据
     */
    @SneakyThrows
    private void syncFormData(String processInstanceId, Map<String, Object> formUpdates) {
        try {
            ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);
            IProcessInstanceRecordService recordService = SpringUtil.getBean(IProcessInstanceRecordService.class);

            ProcessInstanceRecord record = recordService.lambdaQuery()
                    .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId)
                    .one();

            if (record == null) {
                return;
            }

            Map<String, Object> existingFormData = new HashMap<>();
            if (StrUtil.isNotBlank(record.getFormData())) {
                existingFormData = objectMapper.readValue(record.getFormData(), new TypeReference<Map<String, Object>>() {
                });
            }

            existingFormData.putAll(formUpdates);

            String updatedFormData = objectMapper.writeValueAsString(existingFormData);
            recordService.lambdaUpdate()
                    .set(ProcessInstanceRecord::getFormData, updatedFormData)
                    .eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId)
                    .update();

            log.debug("触发器同步更新流程实例 {} 的表单数据", processInstanceId);
        } catch (Exception e) {
            log.error("触发器同步更新表单数据失败: {}", e.getMessage(), e);
        }
    }

    private void handleError(String handler, DelegateExecution execution, String msg) {
        if ("TERMINATE".equals(handler)) {
            RuntimeService runtimeService = SpringUtil.getBean(RuntimeService.class);
            runtimeService.deleteProcessInstance(execution.getProcessInstanceId(), "触发器异常终止: " + msg);
        }
        throw new FlowableException("触发器调用失败: " + msg);
    }

}
