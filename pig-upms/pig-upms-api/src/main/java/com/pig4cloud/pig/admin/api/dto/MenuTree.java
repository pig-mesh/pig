/*
 *
 *  *  Copyright (c) 2019-2020, 冷冷 (wangiegie@gmail.com).
 *  *  <p>
 *  *  Licensed under the GNU Lesser General Public License 3.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  * https://www.gnu.org/licenses/lgpl.html
 *  *  <p>
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package com.pig4cloud.pig.admin.api.dto;

import com.pig4cloud.pig.admin.api.vo.MenuVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author lengleng
 * @date 2017年11月9日23:33:27
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MenuTree extends TreeNode implements Serializable {

	/**
	 * 菜单图标
	 */
	private String icon;

	/**
	 * 菜单名称
	 */
	private String name;

	private boolean spread = false;

	/**
	 * 前端路由标识路径
	 */
	private String path;

	/**
	 * 路由缓冲
	 */
	private String keepAlive;

	/**
	 * 权限编码
	 */
	private String permission;

	/**
	 * 菜单类型 （0菜单 1按钮）
	 */
	private String type;

	/**
	 * 菜单标签
	 */
	private String label;

	/**
	 * 排序值
	 */
	private Integer sort;

	/**
	 * 是否包含子节点
	 *
	 * @since 3.7
	 */
	private Boolean hasChildren;

	public MenuTree() {
	}

	public MenuTree(int id, String name, int parentId) {
		this.id = id;
		this.parentId = parentId;
		this.name = name;
		this.label = name;
	}

	public MenuTree(int id, String name, MenuTree parent) {
		this.id = id;
		this.parentId = parent.getId();
		this.name = name;
		this.label = name;
	}

	public MenuTree(MenuVO menuVo) {
		this.id = menuVo.getMenuId();
		this.parentId = menuVo.getParentId();
		this.icon = menuVo.getIcon();
		this.name = menuVo.getName();
		this.path = menuVo.getPath();
		this.type = menuVo.getType();
		this.permission = menuVo.getPermission();
		this.label = menuVo.getName();
		this.sort = menuVo.getSort();
		this.keepAlive = menuVo.getKeepAlive();
	}

}
