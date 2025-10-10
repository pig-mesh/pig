package com.pig4cloud.pigx.flow.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 流程节点状态枚举
 * <p>
 * 定义流程节点的执行状态，用于追踪每个节点在流程实例中的处理进度。
 * 状态变化遵循：未开始 -> 进行中 -> 已结束的生命周期。
 * </p>
 *
 * @author pigx
 * @since 2024/01/01
 */
@Getter
@RequiredArgsConstructor
public enum NodeStatusEnum {

    /**
     * 未开始
     * <p>
     * 节点已创建但尚未开始执行。
     * 表示流程已到达该节点的前置节点，但该节点还未被激活。
     * </p>
     */
    WKS(0, "未开始"),
    
    /**
     * 进行中
     * <p>
     * 节点正在执行中，等待审批人处理或系统任务完成。
     * 对于审批节点，表示已分配给审批人但尚未完成审批。
     * </p>
     */
    JXZ(1, "进行中"),
    
    /**
     * 已结束
     * <p>
     * 节点已完成执行，流程可以继续流转到下一节点。
     * 包括正常完成、跳过、驳回等所有结束状态。
     * </p>
     */
    YJS(2, "已结束");

    /**
     * 状态编码
     * <p>
     * 节点状态的数字编码，用于数据库存储和状态比较
     * </p>
     */
    private final int code;

    /**
     * 状态名称
     * <p>
     * 节点状态的中文描述，用于界面显示
     * </p>
     */
    private final String name;

    /**
     * 根据状态编码获取枚举实例
     *
     * @param code 状态编码
     * @return 对应的枚举实例，如果找不到返回null
     */
    public static NodeStatusEnum getByCode(int code) {
        for (NodeStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }

    /**
     * 判断节点是否可以处理
     *
     * @return true表示节点可以被处理，false表示不能处理
     */
    public boolean canProcess() {
        return this == JXZ;
    }

    /**
     * 判断节点是否已完成
     *
     * @return true表示节点已完成，false表示未完成
     */
    public boolean isFinished() {
        return this == YJS;
    }

}
