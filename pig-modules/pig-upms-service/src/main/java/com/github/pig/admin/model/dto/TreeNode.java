package com.github.pig.admin.model.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lengleng
 * @date 2017年11月9日23:33:45
 */
public class TreeNode {
    protected int id;
    protected int parentId;
    protected List<TreeNode> children = new ArrayList<TreeNode>();

    public void add(TreeNode node) {
        children.add(node);
    }
}
