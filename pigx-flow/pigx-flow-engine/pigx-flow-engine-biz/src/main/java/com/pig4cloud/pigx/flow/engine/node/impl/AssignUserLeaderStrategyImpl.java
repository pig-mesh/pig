package com.pig4cloud.pigx.flow.engine.node.impl;

import cn.hutool.core.collection.CollUtil;
import com.pig4cloud.pigx.admin.api.feign.RemoteDeptService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.security.util.SecurityUtils;
import com.pig4cloud.pigx.flow.engine.node.AssignUserStrategy;
import com.pig4cloud.pigx.flow.task.constant.ProcessInstanceConstant;
import com.pig4cloud.pigx.flow.task.dto.Node;
import com.pig4cloud.pigx.flow.task.dto.NodeUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 指定主管
 *
 * @author Huijun Zhao
 * @description
 * @date 2023-07-07 13:42
 */
@RequiredArgsConstructor
@Component(ProcessInstanceConstant.AssignedTypeClass.LEADER + "AssignUserStrategy")
public class AssignUserLeaderStrategyImpl implements AssignUserStrategy {

    private final RemoteDeptService deptService;

    @Override
    public List<Long> handle(Node node, NodeUser rootUser, Map<String, Object> variables) {
        // 如果节点没有设置部门主管，则默认为当前用户的部门主管
        if (CollUtil.isEmpty(node.getNodeUserList())) {
            List<NodeUser> nodeUserList = new ArrayList<>();
            nodeUserList.add(NodeUser.builder().id(SecurityUtils.getUser().getDeptId()).build());
            node.setNodeUserList(nodeUserList);
        }

        return node.getNodeUserList()
                .stream()
                .map(nodeUser -> deptService.getAllDeptLeader(nodeUser.getId()))
                .flatMap((Function<R<List<Long>>, Stream<Long>>) listR -> listR.getData() == null ? null
                        : listR.getData().stream())
                .collect(Collectors.toList());
    }

}
