-- 该脚本不要执行，请完善 ID 对应关系,注意层级关系 !!!!

-- 菜单SQL
insert into `sys_menu` ( `parent_id`, `component`, `permission`, `type`, `path`, `icon`, `menu_id`, `del_flag`, `create_time`, `sort`, `update_time`, `name`)
    values ( '父菜单ID', 'views/${moduleName}/${pathName}/index', '', '0', '${pathName}', 'icon-bangzhushouji', '菜单ID', '0', '2018-01-20 13:17:19', '8', '2018-07-29 13:38:19', '${comments}管理');

-- 菜单对应按钮SQL
insert into `sys_menu` ( `parent_id`, `component`, `permission`, `type`, `path`, `icon`, `menu_id`, `del_flag`, `create_time`, `sort`, `update_time`, `name`)
    values ( '菜单ID', null, '${moduleName}_${pathName}_add', '1', null, '1', '子按钮ID1', '0', '2018-05-15 21:35:18', '0', '2018-07-29 13:38:59', '${comments}新增');
insert into `sys_menu` ( `parent_id`, `component`, `permission`, `type`, `path`, `icon`, `menu_id`, `del_flag`, `create_time`, `sort`, `update_time`, `name`)
    values ( '菜单ID', null, '${moduleName}_${pathName}_edit', '1', null, '1', '子按钮ID2', '0', '2018-05-15 21:35:18', '1', '2018-07-29 13:38:59', '${comments}修改');
insert into `sys_menu` ( `parent_id`, `component`, `permission`, `type`, `path`, `icon`, `menu_id`, `del_flag`, `create_time`, `sort`, `update_time`, `name`)
    values ( '菜单ID', null, '${moduleName}_${pathName}_del', '1', null, '1', '子按钮ID3', '0', '2018-05-15 21:35:18', '2', '2018-07-29 13:38:59', '${comments}删除');
