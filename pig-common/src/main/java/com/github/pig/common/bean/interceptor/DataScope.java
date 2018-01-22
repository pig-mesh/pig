package com.github.pig.common.bean.interceptor;

import lombok.Data;

import java.util.List;

/**
 * @author lengleng
 * @date 2018/1/19
 * 数据权限、参考guns实现
 */
@Data
public class DataScope {
    /**
     * 限制范围的字段名称
     */
    private String scopeName = "dept_id";

    /**
     * 具体的数据范围
     */
    private List<Integer> deptIds;

    /**
     * 是否只查询本部门
     */
    private Boolean isOnly = false;
}
