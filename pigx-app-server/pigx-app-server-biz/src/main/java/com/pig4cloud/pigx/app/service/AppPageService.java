package com.pig4cloud.pigx.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.app.api.entity.AppPageEntity;

/**
 * App 装修页面基础服务。
 * <p>
 * 页面组件 JSON 保存在 {@link AppPageEntity#getPageData()}，
 * 底部 Tabbar 不保存在页面 JSON 中。
 */
public interface AppPageService extends IService<AppPageEntity> {

}
