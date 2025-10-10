package com.pig4cloud.pigx.flow.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 流程表单分组视图对象
 * <p>
 * 用于在流程列表页面按分组展示流程，提供流程的分类管理和展示功能。
 * 支持将多个相关流程归类到同一分组中，便于用户查找和使用。
 * </p>
 *
 * @author : willian fu
 * @date : 2020/9/21
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormGroupVo {

    /**
     * 分组ID
     * <p>
     * 流程分组的唯一标识符
     * </p>
     */
    private Long id;

    /**
     * 分组名称
     * <p>
     * 流程分组的显示名称，如"人事管理"、"财务审批"等
     * </p>
     */
    private String name;

    /**
     * 流程列表
     * <p>
     * 该分组下包含的所有流程信息
     * </p>
     */
    private List<FlowVo> items;

    /**
     * 流程视图对象
     * <p>
     * 表示单个流程的基本信息，用于在分组中展示流程列表
     * </p>
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlowVo {

        /**
         * 流程ID
         * <p>
         * 流程的唯一标识符
         * </p>
         */
        private String flowId;

        /**
         * 发起范围描述
         * <p>
         * 描述哪些用户或角色可以发起该流程，用于前端展示
         * </p>
         */
        private String rangeShow;

        /**
         * 流程名称
         * <p>
         * 流程的显示名称，如"请假申请"、"报销申请"等
         * </p>
         */
        private String name;

        /**
         * 流程图标
         * <p>
         * 流程的图标标识，用于在列表中展示
         * </p>
         */
        private String logo;

        /**
         * 停用标识
         * <p>
         * 标识流程是否已停用，"1"表示已停用，其他表示正常
         * </p>
         */
        private String stop;

        /**
         * 流程备注
         * <p>
         * 流程的详细说明或使用说明
         * </p>
         */
        private String remark;

        /**
         * 更新时间
         * <p>
         * 流程最后更新的时间戳
         * </p>
         */
        private LocalDateTime updated;

    }

}
