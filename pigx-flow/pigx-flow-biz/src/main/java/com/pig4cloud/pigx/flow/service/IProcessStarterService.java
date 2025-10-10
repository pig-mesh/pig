package com.pig4cloud.pigx.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.flow.entity.ProcessStarter;

/**
 * 流程发起人权限管理服务接口
 * <p>
 * 该服务负责管理流程的发起权限配置，控制哪些用户、角色或部门可以发起特定的流程。
 * 通过灵活的权限配置，实现流程发起的精细化控制。支持按用户、角色、部门、岗位等
 * 多维度配置发起权限，确保流程只能被授权人员发起，提高流程管理的安全性。
 * </p>
 *
 * @author Vincent
 * @since 2023-05-30
 */
public interface IProcessStarterService extends IService<ProcessStarter> {

}
