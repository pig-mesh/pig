/*
 *
 * 此类来自 https://gitee.com/geek_qi/cloud-platform/blob/master/ace-common/src/main/java/com/github/wxiaoqi/security/common/vo/TreeNode.java
 * @ Apache-2.0
 */

package com.pig4cloud.pig.admin.api.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ace
 * @author lengleng
 * @date 2017年11月9日23:33:45
 */
@Data
public class TreeNode {

	protected int id;

	protected int parentId;

	protected List<TreeNode> children = new ArrayList<TreeNode>();

	public void add(TreeNode node) {
		children.add(node);
	}

}
