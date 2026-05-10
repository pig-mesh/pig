package com.pig4cloud.pigx.app.service;

import com.pig4cloud.pigx.app.api.entity.AppPageEntity;
import com.pig4cloud.pigx.app.api.vo.AppDecorateVO;

import java.util.Map;

/**
 * App 运行时页面服务。
 * <p>
 * 负责给移动端聚合首页、装修页、工作台和 Tabbar 配置；
 * 后台装修管理接口不直接走这里。
 *
 * @author lengleng
 * @date 2023/6/8
 */
public interface AppIndexService {

	/**
     * 获取首页基础数据。
     *
     * @return 首页页面记录和资讯列表
	 */
	Map<String, Object> index();

	/**
     * 获取运行时配置。
     *
     * @return 当前访问身份可见的 Tabbar 等配置
	 */
	Map<String, Object> config();

	/**
     * 获取指定页面类型的装修页聚合数据。
     *
     * @param id 页面类型
     * @return 装修页聚合数据
	 */
    AppDecorateVO decorate(Integer id);

	/**
     * 返回工作台页面实体对象。
     * <p>
     * 工作台按钮权限由 B 端 permission 控制，这里只返回页面装修数据。
	 *
	 * @return 工作台页面实体对象
	 */
	AppPageEntity workbench();
}
