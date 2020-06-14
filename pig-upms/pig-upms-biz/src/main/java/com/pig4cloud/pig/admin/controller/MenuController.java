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

package com.pig4cloud.pig.admin.controller;

import com.pig4cloud.pig.admin.api.entity.SysMenu;
import com.pig4cloud.pig.admin.api.vo.MenuVO;
import com.pig4cloud.pig.admin.service.SysMenuService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.annotation.SysLog;
import com.pig4cloud.pig.common.security.util.SecurityUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lengleng
 * @date 2017/10/31
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/menu")
@Api(value = "menu", tags = "菜单管理模块")
public class MenuController {

	private final SysMenuService sysMenuService;

	/**
	 * 返回当前用户的树形菜单集合
	 * @param parentId 父节点ID
	 * @return 当前用户的树形菜单
	 */
	@GetMapping
	public R getUserMenu(Integer parentId) {

		// 获取符合条件的菜单
		Set<MenuVO> all = new HashSet<>();
		SecurityUtils.getRoles().forEach(roleId -> all.addAll(sysMenuService.findMenuByRoleId(roleId)));
		return R.ok(sysMenuService.filterMenu(all, parentId));
	}

	/**
	 * 返回树形菜单集合
	 * @param lazy 是否是懒加载
	 * @param parentId 父节点ID
	 * @return 树形菜单
	 */
	@GetMapping(value = "/tree")
	public R getTree(boolean lazy, Integer parentId) {
		return R.ok(sysMenuService.treeMenu(lazy, parentId));
	}

	/**
	 * 返回角色的菜单集合
	 * @param roleId 角色ID
	 * @return 属性集合
	 */
	@GetMapping("/tree/{roleId}")
	public R getRoleTree(@PathVariable Integer roleId) {
		return R.ok(
				sysMenuService.findMenuByRoleId(roleId).stream().map(MenuVO::getMenuId).collect(Collectors.toList()));
	}

	/**
	 * 通过ID查询菜单的详细信息
	 * @param id 菜单ID
	 * @return 菜单详细信息
	 */
	@GetMapping("/{id}")
	public R getById(@PathVariable Integer id) {
		return R.ok(sysMenuService.getById(id));
	}

	/**
	 * 新增菜单
	 * @param sysMenu 菜单信息
	 * @return 含ID 菜单信息
	 */
	@SysLog("新增菜单")
	@PostMapping
	@PreAuthorize("@pms.hasPermission('sys_menu_add')")
	public R save(@Valid @RequestBody SysMenu sysMenu) {
		sysMenuService.save(sysMenu);
		return R.ok(sysMenu);
	}

	/**
	 * 删除菜单
	 * @param id 菜单ID
	 * @return success/false
	 */
	@SysLog("删除菜单")
	@DeleteMapping("/{id}")
	@PreAuthorize("@pms.hasPermission('sys_menu_del')")
	public R removeById(@PathVariable Integer id) {
		return sysMenuService.removeMenuById(id);
	}

	/**
	 * 更新菜单
	 * @param sysMenu
	 * @return
	 */
	@SysLog("更新菜单")
	@PutMapping
	@PreAuthorize("@pms.hasPermission('sys_menu_edit')")
	public R update(@Valid @RequestBody SysMenu sysMenu) {
		return R.ok(sysMenuService.updateMenuById(sysMenu));
	}

}
