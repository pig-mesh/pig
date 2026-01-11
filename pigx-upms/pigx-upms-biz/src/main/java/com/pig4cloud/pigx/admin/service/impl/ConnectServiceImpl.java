package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.pig4cloud.pigx.admin.api.dto.UserDTO;
import com.pig4cloud.pigx.admin.api.entity.SysDept;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.service.ConnectService;
import com.pig4cloud.pigx.admin.service.SysDeptService;
import com.pig4cloud.pigx.admin.service.SysUserService;
import com.pig4cloud.pigx.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;

import com.pig4cloud.pigx.admin.api.vo.DingTalkDeptExcelVO;
import com.pig4cloud.pigx.admin.api.vo.DingUserExcelVo;
import com.pig4cloud.pigx.admin.api.vo.WeChatDeptExcelVO;
import com.pig4cloud.pigx.admin.api.vo.WeChatUserExcelVO;
import com.pig4cloud.pigx.common.excel.vo.ErrorMessage;
import org.springframework.validation.BindingResult;


/**
 * 互联平台服务实现类
 * <p>
 * 提供企业微信和钉钉组织架构导入功能,包括部门导入和用户导入
 *
 * @author lengleng
 * @date 2022/4/22
 */
@SuppressWarnings("unchecked")
@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectServiceImpl implements ConnectService {

    /**
     * 部门服务
     */
    private final SysDeptService sysDeptService;

    /**
     * 用户服务
     */
    private final SysUserService sysUserService;


    /**
     * 导入企业微信部门
     * <p>
     * 解析企业微信导出的通讯录Excel,支持两种格式:
     * 1. 单部门路径: "腾讯公司/微信事业群/广州研发部"
     * 2. 多部门路径: "部门A;部门B" (用分号分隔)
     * <p>
     * 处理逻辑:
     * - 解析部门路径,从顶级部门开始逐级创建
     * - 如果部门已存在(相同父部门下同名),则更新部门信息(覆盖模式)
     * - 自动维护部门的 parentId 关系
     *
     * @param excelVOList   Excel数据列表
     * @param bindingResult 字段级别校验结果
     * @return 导入结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R importWeChatDept(List<WeChatDeptExcelVO> excelVOList, BindingResult bindingResult) {
        List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

        // 加载现有部门缓存
        Map<String, SysDept> deptKeyMap = sysDeptService.list(Wrappers.emptyWrapper()).stream()
                .collect(Collectors.toMap(
                        dept -> buildDeptKey(dept.getParentId(), dept.getName()),
                        dept -> dept,
                        (v1, v2) -> v1
                ));

        int successCount = 0;

        for (WeChatDeptExcelVO excel : excelVOList) {
            if (StrUtil.isBlank(excel.getDeptPath())) {
                errorMessageList.add(new ErrorMessage(excel.getLineNum(), Set.of("部门路径不能为空")));
                continue;
            }

            for (String deptPath : excel.getDeptPath().split(";")) {
                deptPath = deptPath.trim();
                if (StrUtil.isNotBlank(deptPath)) {
                    successCount += createDeptHierarchy(deptPath, deptKeyMap);
                }
            }
        }

        if (CollUtil.isNotEmpty(errorMessageList)) {
            return R.failed(errorMessageList);
        }
        return R.ok(null, String.format("部门导入成功! 新增: %d 个", successCount));
    }

    /**
     * 构建部门唯一键 (parentId + name)
     */
    private String buildDeptKey(Long parentId, String deptName) {
        return parentId + StrUtil.COLON + deptName;
    }

    /**
     * 标准化手机号 (去除区号前缀)
     * 支持格式: +86-17034642119, +86 17034642119, 86-17034642119, 8617034642119
     *
     * @param phone 原始手机号
     * @return 纯手机号 (11位)
     */
    private String normalizePhone(String phone) {
        if (StrUtil.isBlank(phone)) {
            return phone;
        }
        // 移除所有非数字字符，然后取后11位
        String digits = phone.replaceAll("\\D", "");
        if (digits.length() > 11) {
            return digits.substring(digits.length() - 11);
        }
        return digits;
    }

    /**
     * 创建部门层级
     *
     * @param deptPath   部门路径 "公司/部门/子部门"
     * @param deptKeyMap 部门缓存 (会被更新)
     * @return 新创建的部门数量
     */
    private int createDeptHierarchy(String deptPath, Map<String, SysDept> deptKeyMap) {
        int created = 0;
        String[] deptNames = deptPath.split(StrUtil.SLASH);
        Long parentId = 0L;

        for (String rawDeptName : deptNames) {
            String deptName = rawDeptName.trim();
            if (StrUtil.isBlank(deptName)) {
                continue;
            }

            String deptKey = buildDeptKey(parentId, deptName);
            SysDept existingDept = deptKeyMap.get(deptKey);

            if (existingDept != null) {
                parentId = existingDept.getDeptId();
            } else {
                SysDept newDept = new SysDept();
                newDept.setName(deptName);
                newDept.setParentId(parentId);
                newDept.setSortOrder(1000);
                sysDeptService.save(newDept);

                deptKeyMap.put(deptKey, newDept);
                parentId = newDept.getDeptId();
                created++;
            }
        }
        return created;
    }

    /**
     * 导入企业微信用户
     * <p>
     * 解析企业微信导出的通讯录 Excel 中的用户数据
     * <p>
     * 处理逻辑:
     * - 通过手机号判断用户是否已存在
     * - 已存在用户: 更新用户信息(覆盖模式)
     * - 新用户: 创建用户账号
     * - 部门不存在: 分配到根部门(parentId=0)
     * - 初始密码: 手机号
     *
     * @param excelVOList   Excel 数据列表
     * @param bindingResult 字段级别校验结果
     * @return 导入结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R importWeChatUser(List<WeChatUserExcelVO> excelVOList, BindingResult bindingResult) {
        // 1. 获取字段级别校验失败的数据
        List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

        // 2. 加载现有数据
        List<SysUser> existingUsers = sysUserService.list(Wrappers.emptyWrapper());
        Map<String, SysUser> phoneUserMap = existingUsers.stream()
                .filter(user -> StrUtil.isNotBlank(user.getPhone()))
                .collect(Collectors.toMap(SysUser::getPhone, user -> user, (v1, v2) -> v1));

        // 构建部门缓存 (使用精确匹配: parentId:name)
        List<SysDept> allDepts = sysDeptService.list(Wrappers.emptyWrapper());
        Map<String, SysDept> deptKeyMap = allDepts.stream()
                .collect(Collectors.toMap(
                        dept -> buildDeptKey(dept.getParentId(), dept.getName()),
                        dept -> dept,
                        (v1, v2) -> v1
                ));
        // 同时保留按名称查找的备用映射 (兼容简单场景)
        Map<String, SysDept> deptNameMap = allDepts.stream()
                .collect(Collectors.toMap(SysDept::getName, dept -> dept, (v1, v2) -> v1));

        // 3. 统计计数器
        int successCount = 0;
        int updateCount = 0;

        // 4. 遍历Excel中的每一行
        for (WeChatUserExcelVO excel : excelVOList) {
            Set<String> errorMsg = new HashSet<>();

            // 5. 基础字段校验
            String phone = excel.getPhone();
            if (StrUtil.isBlank(phone)) {
                errorMsg.add("手机号不能为空");
            }

            if (StrUtil.isBlank(excel.getName())) {
                errorMsg.add("姓名不能为空");
            }

            if (!errorMsg.isEmpty()) {
                errorMessageList.add(new ErrorMessage(excel.getLineNum(), errorMsg));
                continue;
            }

            // 6. 解析部门信息（支持多部门，使用层级精确匹配）
            List<Long> deptIds = resolveDeptIdsWithHierarchy(excel.getDeptPath(), deptKeyMap, deptNameMap, excel.getName());
            Long primaryDeptId = deptIds.isEmpty() ? 0L : deptIds.get(0);

            // 7. 计算昵称 (优先使用别名,否则使用姓名)
            String nickname = StrUtil.isNotBlank(excel.getAlias()) ? excel.getAlias() : excel.getName();

            // 8. 判断用户是否已存在(通过手机号)
            SysUser existingUser = phoneUserMap.get(phone);

            if (existingUser != null) {
                // 用户已存在,更新用户信息(覆盖模式)
                existingUser.setName(excel.getName());
                existingUser.setNickname(nickname);
                existingUser.setEmail(excel.getEmail());
                existingUser.setDeptId(primaryDeptId);
                existingUser.setWxCpUserid(excel.getAccount());

                sysUserService.updateById(existingUser);
                updateCount++;
            } else {
                // 新用户,创建账号
                UserDTO userDTO = new UserDTO();
                userDTO.setUsername(phone);
                userDTO.setPhone(phone);
                userDTO.setName(excel.getName());
                userDTO.setNickname(nickname);
                userDTO.setEmail(excel.getEmail());
                userDTO.setDeptIds(deptIds); // 设置多部门
                userDTO.setPassword(phone);
                userDTO.setPasswordModifyTime(LocalDateTime.now());
                userDTO.setWxCpUserid(excel.getAccount());

                sysUserService.saveUser(userDTO);
                successCount++;
            }
        }

        // 9. 返回结果
        if (CollUtil.isNotEmpty(errorMessageList)) {
            return R.failed(errorMessageList);
        }

        return R.ok(null, String.format("用户导入成功! 新增: %d 个, 更新: %d 个", successCount, updateCount));
    }

    /**
     * 解析部门路径,使用层级精确匹配
     *
     * @param deptPath    部门路径 (支持多部门用";"分隔)
     * @param deptKeyMap  精确匹配缓存 (parentId:name -> SysDept)
     * @param deptNameMap 名称匹配缓存 (备用)
     * @param userName    用户名 (用于日志)
     * @return 部门ID列表,第一个为主部门
     */
    private List<Long> resolveDeptIdsWithHierarchy(String deptPath,
                                                   Map<String, SysDept> deptKeyMap,
                                                   Map<String, SysDept> deptNameMap,
                                                   String userName) {

        List<Long> deptIds = new ArrayList<>();

        if (StrUtil.isBlank(deptPath)) {
            deptIds.add(0L);
            return deptIds;
        }

        for (String path : deptPath.split(";")) {
            String trimmedPath = path.trim();
            if (StrUtil.isBlank(trimmedPath)) {
                continue;
            }

            // 使用统一的部门路径解析 (支持降级匹配)
            Long deptId = resolveDeptPath(trimmedPath, deptKeyMap, deptNameMap);

            if (deptId != null) {
                deptIds.add(deptId);
            } else {
                log.warn("部门不存在: {}, 用户: {}", trimmedPath, userName);
            }
        }

        if (deptIds.isEmpty()) {
            log.warn("用户没有有效部门,将分配到根部门, 用户: {}", userName);
            deptIds.add(0L);
        }

        return deptIds;
    }

    /**
     * 解析部门路径,返回最终部门ID
     *
     * @param deptPath    部门路径 "公司/部门A/子部门B"
     * @param deptKeyMap  精确匹配缓存 (parentId:name -> SysDept)
     * @param deptNameMap 名称匹配缓存 (可选,用于降级匹配,传null则不降级)
     * @return 部门ID,未找到返回 null
     */
    private Long resolveDeptPath(String deptPath,
                                 Map<String, SysDept> deptKeyMap,
                                 Map<String, SysDept> deptNameMap) {
        String[] deptNames = deptPath.split(StrUtil.SLASH);
        Long parentId = 0L;
        SysDept targetDept = null;

        for (String rawName : deptNames) {
            String deptName = rawName.trim();
            if (StrUtil.isBlank(deptName)) {
                continue;
            }

            String deptKey = buildDeptKey(parentId, deptName);
            targetDept = deptKeyMap.get(deptKey);

            if (targetDept == null && deptNameMap != null) {
                // 降级匹配: 仅按名称
                targetDept = deptNameMap.get(deptName);
            }

            if (targetDept == null) {
                return null;
            }
            parentId = targetDept.getDeptId();
        }

        return targetDept != null ? targetDept.getDeptId() : null;
    }

    /**
     * 导入钉钉部门
     * <p>
     * 解析钉钉导出的 Excel 中的部门数据
     * <p>
     * 处理逻辑:
     * - 解析部门路径 "公司/部门A/子部门B" (支持多部门用";"分隔)
     * - 从顶级部门开始逐级创建
     * - 使用 parentId + name 作为唯一键,支持同名部门
     * - 部门已存在则复用,不存在则创建
     * - 自动维护 parentId 层级关系
     *
     * @param excelVOList   Excel 数据列表
     * @param bindingResult 字段级别校验结果
     * @return 导入结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R importDingTalkDept(List<DingTalkDeptExcelVO> excelVOList, BindingResult bindingResult) {
        List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

        // 加载现有部门缓存
        Map<String, SysDept> deptKeyMap = sysDeptService.list(Wrappers.emptyWrapper()).stream()
                .collect(Collectors.toMap(
                        dept -> buildDeptKey(dept.getParentId(), dept.getName()),
                        dept -> dept,
                        (v1, v2) -> v1
                ));

        int successCount = 0;

        for (DingTalkDeptExcelVO excel : excelVOList) {
            if (StrUtil.isBlank(excel.getDeptPath())) {
                errorMessageList.add(new ErrorMessage(excel.getLineNum(), Set.of("部门路径不能为空")));
                continue;
            }

            for (String deptPath : excel.getDeptPath().split(";")) {
                deptPath = deptPath.trim();
                if (StrUtil.isNotBlank(deptPath)) {
                    successCount += createDeptHierarchy(deptPath, deptKeyMap);
                }
            }
        }

        if (CollUtil.isNotEmpty(errorMessageList)) {
            return R.failed(errorMessageList);
        }
        return R.ok(null, String.format("部门导入成功! 新增: %d 个", successCount));
    }

    /**
     * 导入钉钉用户
     * <p>
     * 解析钉钉导出的 Excel 中的用户数据
     * <p>
     * 处理逻辑:
     * - 通过手机号判断用户是否已存在
     * - 已存在用户: 更新用户信息(覆盖模式)
     * - 新用户: 创建用户账号,用户名=手机号
     * - 部门不存在: 分配到根部门(parentId=0)
     * - 初始密码: 手机号
     *
     * @param excelVOList   Excel 数据列表
     * @param bindingResult 字段级别校验结果
     * @return 导入结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public R importDingTalkUser(List<DingUserExcelVo> excelVOList, BindingResult bindingResult) {
        // 1. 获取字段级别校验失败的数据
        List<ErrorMessage> errorMessageList = (List<ErrorMessage>) bindingResult.getTarget();

        // 2. 加载现有数据
        List<SysUser> existingUsers = sysUserService.list(Wrappers.emptyWrapper());
        Map<String, SysUser> phoneUserMap = existingUsers.stream()
                .filter(user -> StrUtil.isNotBlank(user.getPhone()))
                .collect(Collectors.toMap(SysUser::getPhone, user -> user, (v1, v2) -> v1));

        // 构建部门缓存 (使用精确匹配: parentId:name)
        List<SysDept> allDepts = sysDeptService.list(Wrappers.emptyWrapper());
        Map<String, SysDept> deptKeyMap = allDepts.stream()
                .collect(Collectors.toMap(
                        dept -> buildDeptKey(dept.getParentId(), dept.getName()),
                        dept -> dept,
                        (v1, v2) -> v1
                ));
        // 同时保留按名称查找的备用映射 (兼容简单场景)
        Map<String, SysDept> deptNameMap = allDepts.stream()
                .collect(Collectors.toMap(SysDept::getName, dept -> dept, (v1, v2) -> v1));

        // 3. 统计计数器
        int successCount = 0;
        int updateCount = 0;

        // 4. 遍历Excel中的每一行
        for (DingUserExcelVo excel : excelVOList) {
            Set<String> errorMsg = new HashSet<>();

            // 5. 基础字段校验 (标准化手机号，去除区号前缀)
            String phone = normalizePhone(excel.getPhone());
            if (StrUtil.isBlank(phone)) {
                errorMsg.add("手机号不能为空");
            }

            if (StrUtil.isBlank(excel.getName())) {
                errorMsg.add("姓名不能为空");
            }

            if (CollUtil.isNotEmpty(errorMsg)) {
                errorMessageList.add(new ErrorMessage(excel.getLineNum(), errorMsg));
                continue;
            }

            // 6. 解析部门信息
            List<Long> deptIds = new ArrayList<>();
            if (StrUtil.isNotBlank(excel.getDeptPath())) {
                // 支持多部门: "部门路径1;部门路径2"
                String[] deptPaths = excel.getDeptPath().split(";");

                for (String deptPath : deptPaths) {
                    deptPath = deptPath.trim();
                    if (StrUtil.isBlank(deptPath)) {
                        continue;
                    }

                    // 解析部门层级路径
                    Long deptId = resolveDeptPath(deptPath, deptKeyMap, deptNameMap);
                    if (deptId != null) {
                        deptIds.add(deptId);
                    }
                }
            }

            // 如果没有找到部门,分配到根部门
            if (CollUtil.isEmpty(deptIds)) {
                deptIds.add(0L);
            }

            // 7. 计算昵称 (钉钉没有别名字段,直接使用姓名)
            String nickname = excel.getName();

            // 8. 判断用户是否存在 (通过手机号)
            SysUser existingUser = phoneUserMap.get(phone);

            if (existingUser != null) {
                // 用户已存在,更新用户信息 (参考企业微信实现,使用 UserDTO 统一处理)
                UserDTO userDTO = new UserDTO();
                userDTO.setUserId(existingUser.getUserId());
                userDTO.setName(excel.getName());
                userDTO.setUsername(phone);
                userDTO.setNickname(nickname);
                userDTO.setEmail(excel.getEmail());
                userDTO.setDeptIds(deptIds); // 设置多部门，由 updateUser 自动处理部门关联
                userDTO.setLockFlag(StrUtil.isNotBlank(excel.getLockFlag()) ? excel.getLockFlag() : "0");

                sysUserService.updateUser(userDTO);
                updateCount++;
            } else {
                // 新用户,创建账号 (参考企业微信实现)
                UserDTO userDTO = new UserDTO();
                userDTO.setUsername(phone); // 用户名=手机号
                userDTO.setPhone(phone);
                userDTO.setName(excel.getName());
                userDTO.setNickname(nickname);
                userDTO.setEmail(excel.getEmail());
                userDTO.setDeptIds(deptIds); // 设置多部门
                userDTO.setPassword(phone); // 初始密码=手机号
                userDTO.setPasswordModifyTime(LocalDateTime.now());
                userDTO.setLockFlag(StrUtil.isNotBlank(excel.getLockFlag()) ? excel.getLockFlag() : "0");

                sysUserService.saveUser(userDTO);
                successCount++;
            }
        }

        // 8. 返回结果
        if (CollUtil.isNotEmpty(errorMessageList)) {
            return R.failed(errorMessageList);
        }

        return R.ok(null, String.format("用户导入成功! 新增: %d 个, 更新: %d 个", successCount, updateCount));
    }

}
