package com.pig4cloud.pigx.flow.support.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.util.RetOps;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.flow.constant.NodeStatusEnum;
import com.pig4cloud.pigx.flow.constant.NodeTypeEnum;
import com.pig4cloud.pigx.flow.constant.NodeUserTypeEnum;
import com.pig4cloud.pigx.flow.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.dto.Node;
import com.pig4cloud.pigx.flow.dto.NodeUser;
import com.pig4cloud.pigx.flow.entity.ProcessInstanceRecord;
import com.pig4cloud.pigx.flow.entity.ProcessNodeRecordAssignUser;
import com.pig4cloud.pigx.flow.service.IProcessInstanceRecordService;
import com.pig4cloud.pigx.flow.service.IProcessNodeRecordAssignUserService;
import com.pig4cloud.pigx.flow.service.IRemoteService;
import com.pig4cloud.pigx.flow.vo.NodeVo;
import com.pig4cloud.pigx.flow.vo.UserVo;
import lombok.SneakyThrows;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 节点格式化显示工具类
 * <p>
 * 提供流程节点的可视化格式化功能，将流程定义节点转换为前端展示所需的数据结构。 该工具类负责处理节点状态、执行人信息、分支结构等显示逻辑。
 * <p>
 * 主要功能： 1. 格式化流程节点为可视化数据结构 2. 处理节点的执行状态（未开始、进行中、已完成） 3. 获取并展示节点的执行人信息 4. 处理条件分支和并行分支的嵌套结构
 * 5. 支持发起人自选、指定用户、表单人员等多种执行人类型
 * <p>
 * 使用场景： - 流程图可视化展示 - 流程进度跟踪 - 流程历史查看
 *
 * @author pigx
 */
public class NodeFormatUtil {

    /**
     * 格式化流程节点为可视化展示结构
     * <p>
     * 递归处理流程节点树，生成包含节点状态、执行人信息的展示数据。 支持处理各种节点类型，包括审批节点、抄送节点、网关节点等。
     *
     * @param node              当前处理的节点对象
     * @param completeNodeSet   已完成的节点ID集合
     * @param continueNodeSet   进行中的节点ID集合
     * @param processInstanceId 流程实例ID，用于查询历史数据，可为null
     * @param paramMap          流程变量Map，包含表单数据和执行人选择信息
     * @return 格式化后的节点展示对象列表
     */
    @SneakyThrows
    public static List<NodeVo> formatProcessNodeShow(Node node, Set<String> completeNodeSet, Set<String> continueNodeSet, String processInstanceId, Map<String, Object> paramMap) {
        List<NodeVo> list = new ArrayList<>();

        if (!NodeUtil.isNode(node)) {
            return list;
        }

        String name = node.getName();
        Integer type = node.getType();

        // SELF_SELECT

        NodeVo nodeVo = new NodeVo();
        nodeVo.setId(node.getId());
        nodeVo.setName(name);
        nodeVo.setType(type);
        nodeVo.setStatus(NodeStatusEnum.WKS.getCode());
        if (completeNodeSet.contains(node.getId())) {
            nodeVo.setStatus(NodeStatusEnum.YJS.getCode());

        }
        if (continueNodeSet.contains(node.getId())) {
            nodeVo.setStatus(NodeStatusEnum.JXZ.getCode());

        }

        nodeVo.setPlaceholder(node.getPlaceHolder());

        List<UserVo> userVoList = new ArrayList<>();
        if (type == NodeTypeEnum.APPROVAL.getValue().intValue()) {

            Integer assignedType = node.getAssignedType();

            boolean selfSelect = assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT;
            nodeVo.setSelectUser(selfSelect);
            if (selfSelect) {
                nodeVo.setMultiple(node.getMultiple());
            }

            // 用户列表
            if (StrUtil.isNotBlank(processInstanceId)) {
                IProcessNodeRecordAssignUserService processNodeRecordAssignUserService = SpringUtil.getBean(IProcessNodeRecordAssignUserService.class);
                List<ProcessNodeRecordAssignUser> processNodeRecordAssignUserList = processNodeRecordAssignUserService.lambdaQuery().eq(ProcessNodeRecordAssignUser::getNodeId, node.getId()).eq(ProcessNodeRecordAssignUser::getProcessInstanceId, processInstanceId).orderByAsc(ProcessNodeRecordAssignUser::getCreateTime).list();
                Map<String, List<ProcessNodeRecordAssignUser>> map = processNodeRecordAssignUserList.stream().collect(Collectors.groupingBy(ProcessNodeRecordAssignUser::getTaskId));

                for (Map.Entry<String, List<ProcessNodeRecordAssignUser>> entry : map.entrySet()) {
                    List<ProcessNodeRecordAssignUser> value = entry.getValue();
                    List<UserVo> collect = value.stream().map(w -> {
                        UserVo userVo = buildUser(w.getUserId());
                        if (userVo == null) {
                            return null;
                        }

                        userVo.setShowTime(w.getEndTime());
                        userVo.setApproveDesc(w.getApproveDesc());
                        userVo.setStatus(w.getStatus());
                        userVo.setOperType(w.getTaskType());
                        return userVo;
                    }).filter(Objects::nonNull).toList();
                    userVoList.addAll(collect);
                }

                if (processNodeRecordAssignUserList.isEmpty()) {
                    if (assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF) {
                        // 发起人自己
                        userVoList.addAll(CollUtil.newArrayList(buildRootUser(processInstanceId)));
                    }
                    if (assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF_SELECT) {
                        // 发起人自选
                        Object variable = paramMap.get(StrUtil.format("{}_assignee_select", node.getId()));

                        ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);
                        List<NodeUser> nodeUserDtos = objectMapper.readValue(objectMapper.writeValueAsString(variable), new TypeReference<>() {
                        });

                        if (CollUtil.isNotEmpty(nodeUserDtos)) {
                            List<Long> collect = nodeUserDtos.stream().map(NodeUser::getId).toList();
                            for (Long aLong : collect) {
                                UserVo userVo = buildUser(aLong);
                                userVoList.addAll(CollUtil.newArrayList(userVo));
                            }
                        }

                    }
                }

            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.USER) {
                // 指定用户

                List<NodeUser> nodeUserList = node.getNodeUserList();
                List<UserVo> tempList = buildUser(nodeUserList);
                userVoList.addAll(tempList);

            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.FORM_USER) {
                // 表单人员
                String formUser = node.getFormUserId();

                Object o = paramMap.get(formUser);
                if (o != null) {
                    ObjectMapper objectMapper = SpringUtil.getBean(ObjectMapper.class);
                    String jsonString = objectMapper.writeValueAsString(o);
                    if (StrUtil.isNotBlank(jsonString)) {
                        List<NodeUser> nodeUserDtoList = objectMapper.readValue(jsonString, new TypeReference<>() {
                        });
                        List<Long> userIdList = nodeUserDtoList.stream().map(NodeUser::getId).toList();
                        for (Long aLong : userIdList) {
                            userVoList.addAll(CollUtil.newArrayList(buildUser(aLong)));
                        }
                    }
                }

            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.SELF) {
                // 发起人自己
                userVoList.addAll(CollUtil.newArrayList(buildUser(SecurityUtils.getUser().getId())));
            } else if (assignedType == ProcessInstanceConstant.AssignedTypeClass.EXPRESSION) {
                // 表达式
                // Object result = getExpressionValue(paramMap, node.getAssignExpress());
                // List<UserVo> userVos = Convert.toList(Long.class, result)
                // .stream()
                // .map(NodeFormatUtil::buildUser)
                // .toList();
                // userVoList.addAll(userVos);
            }

        } else if (Objects.equals(node.getType(), NodeTypeEnum.ROOT.getValue())) {
            // 发起节点
            if (StrUtil.isBlank(processInstanceId)) {
                UserVo userVo = buildUser(SecurityUtils.getUser().getId());
                userVoList.addAll(CollUtil.newArrayList(userVo));
            } else {

                IProcessInstanceRecordService processInstanceRecordService = SpringUtil.getBean(IProcessInstanceRecordService.class);
                ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();

                UserVo userVo = buildRootUser(processInstanceId);
                userVo.setShowTime(processInstanceRecord.getCreateTime());
                userVo.setStatus(NodeStatusEnum.YJS.getCode());
                userVoList.addAll(CollUtil.newArrayList(userVo));

            }
        } else if (Objects.equals(node.getType(), NodeTypeEnum.CC.getValue())) {
            // 抄送节点

            List<NodeUser> nodeUserList = node.getNodeUserList();

            List<UserVo> tempList = buildUser(nodeUserList);
            userVoList.addAll(tempList);

        } else if (type == NodeTypeEnum.TRIGGER.getValue().intValue()) {
            // 触发器节点
        }

        // 根据userVo 的ID 去重userVoList
        nodeVo.setUserVoList(CollUtil.distinct(userVoList, UserVo::getId, true));

        List<NodeVo> branchList = new ArrayList<>();

        if (type == NodeTypeEnum.EXCLUSIVE_GATEWAY.getValue().intValue() || type == NodeTypeEnum.PARALLEL_GATEWAY.getValue().intValue()) {
            // 条件分支
            List<Node> branchs = node.getConditionNodes();

            for (Node branch : branchs) {
                Node children = branch.getChildren();
                List<NodeVo> processNodeShowDtos = formatProcessNodeShow(children, completeNodeSet, continueNodeSet, processInstanceId, paramMap);

                NodeVo p = new NodeVo();
                p.setChildren(processNodeShowDtos);

                p.setPlaceholder(branch.getPlaceHolder());
                branchList.add(p);
            }
        }
        nodeVo.setBranch(branchList);

        list.add(nodeVo);

        List<NodeVo> next = formatProcessNodeShow(node.getChildren(), completeNodeSet, continueNodeSet, processInstanceId, paramMap);
        list.addAll(next);

        return list;
    }

    /**
     * 根据流程实例ID构建发起人用户信息
     * <p>
     * 查询流程实例记录，获取发起人的用户ID，并构建用户展示对象。 主要用于展示流程的发起人信息。
     *
     * @param processInstanceId 流程实例ID
     * @return 发起人的用户展示对象
     */
    private static UserVo buildRootUser(String processInstanceId) {

        IProcessInstanceRecordService processInstanceRecordService = SpringUtil.getBean(IProcessInstanceRecordService.class);
        ProcessInstanceRecord processInstanceRecord = processInstanceRecordService.lambdaQuery().eq(ProcessInstanceRecord::getProcessInstanceId, processInstanceId).one();
        Long userId = processInstanceRecord.getUserId();
        return buildUser(userId);
    }

    /**
     * 根据用户ID构建用户展示信息
     * <p>
     * 通过远程服务获取用户详细信息，并构建用户展示对象。 如果用户不存在或为空执行人ID，返回特殊处理的对象。
     *
     * @param userId 用户ID
     * @return 用户展示对象，如果用户不存在则返回null
     */
    private static UserVo buildUser(long userId) {
        if (ProcessInstanceConstant.DEFAULT_EMPTY_ASSIGN == userId) {
            return UserVo.builder().id(userId).build();
        }

        RemoteUserService userService = SpringUtil.getBean(RemoteUserService.class);
        SysUser user = RetOps.of(userService.getUserById(CollUtil.newArrayList(userId))).getData().map(sysUsers -> sysUsers.isEmpty() ? null : sysUsers.get(0)).orElse(null);
        if (user == null) {
            return null;
        }

        return UserVo.builder().id(userId).name(user.getName()).avatar(user.getAvatar()).build();
    }

    /**
     * 批量构建用户展示信息
     * <p>
     * 根据节点用户配置列表，批量获取用户信息并构建展示对象。 支持处理用户类型和部门类型： - 用户类型：直接获取用户信息 - 部门类型：获取部门下的所有用户
     *
     * @param nodeUserList 节点用户配置列表，包含用户ID和部门ID
     * @return 用户展示对象列表
     */
    private static List<UserVo> buildUser(List<NodeUser> nodeUserList) {
        List<UserVo> userVoList = new ArrayList<>();
        // 用户id
        List<Long> userIdList = nodeUserList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.USER.getKey())).map(w -> Convert.toLong(w.getId())).toList();
        // 部门id
        List<Long> deptIdList = nodeUserList.stream().filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey())).map(w -> Convert.toLong(w.getId())).toList();

        if (CollUtil.isNotEmpty(deptIdList)) {

            IRemoteService iRemoteService = SpringUtil.getBean(IRemoteService.class);

            List<Long> data = iRemoteService.queryUserIdListByDepIdList(deptIdList).getData();

            if (CollUtil.isNotEmpty(data)) {
                for (long datum : data) {
                    if (!userIdList.contains(datum)) {
                        userIdList.add(datum);
                    }
                }
            }
        }
        {
            for (Long aLong : userIdList) {
                userVoList.addAll(CollUtil.newArrayList(buildUser(aLong)));
            }
        }
        return userVoList;
    }

}
