/*
 *
 * 此类来自 https://gitee.com/geek_qi/cloud-platform/blob/master/ace-common/src/main/java/com/github/wxiaoqi/security/common/util/TreeUtil.java
 * @ Apache-2.0
 */

package com.pig4cloud.pig.admin.api.vo;

import com.pig4cloud.pig.admin.api.dto.MenuTree;
import com.pig4cloud.pig.admin.api.dto.TreeNode;
import com.pig4cloud.pig.admin.api.entity.SysMenu;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ace
 * @author lengleng
 * @date 2020-02-09
 */
@UtilityClass
public class TreeUtil {

	/**
	 * 两层循环实现建树
	 * @param treeNodes 传入的树节点列表
	 * @return
	 */
	public <T extends TreeNode> List<T> build(List<T> treeNodes, Object root) {

		List<T> trees = new ArrayList<>();

		for (T treeNode : treeNodes) {

			if (root.equals(treeNode.getParentId())) {
				trees.add(treeNode);
			}

			for (T it : treeNodes) {
				if (it.getParentId() == treeNode.getId()) {
					if (treeNode.getChildren() == null) {
						treeNode.setChildren(new ArrayList<>());
					}
					treeNode.add(it);
				}
			}
		}
		return trees;
	}

	/**
	 * 使用递归方法建树
	 * @param treeNodes
	 * @return
	 */
	public <T extends TreeNode> List<T> buildByRecursive(List<T> treeNodes, Object root) {
		List<T> trees = new ArrayList<T>();
		for (T treeNode : treeNodes) {
			if (root.equals(treeNode.getParentId())) {
				trees.add(findChildren(treeNode, treeNodes));
			}
		}
		return trees;
	}

	/**
	 * 递归查找子节点
	 * @param treeNodes
	 * @return
	 */
	public <T extends TreeNode> T findChildren(T treeNode, List<T> treeNodes) {
		for (T it : treeNodes) {
			if (treeNode.getId() == it.getParentId()) {
				if (treeNode.getChildren() == null) {
					treeNode.setChildren(new ArrayList<>());
				}
				treeNode.add(findChildren(it, treeNodes));
			}
		}
		return treeNode;
	}

	/**
	 * 通过sysMenu创建树形节点
	 * @param menus
	 * @param root
	 * @return
	 */
	public List<MenuTree> buildTree(List<SysMenu> menus, int root) {
		List<MenuTree> trees = new ArrayList<>();
		MenuTree node;
		for (SysMenu menu : menus) {
			node = new MenuTree();
			node.setId(menu.getMenuId());
			node.setParentId(menu.getParentId());
			node.setName(menu.getName());
			node.setPath(menu.getPath());
			node.setPermission(menu.getPermission());
			node.setLabel(menu.getName());
			node.setIcon(menu.getIcon());
			node.setType(menu.getType());
			node.setSort(menu.getSort());
			node.setHasChildren(true);
			node.setKeepAlive(menu.getKeepAlive());
			trees.add(node);
		}
		return TreeUtil.build(trees, root);
	}

}
