package com.github.pig.admin.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.admin.common.config.QiniuPropertiesConfig;
import com.github.pig.admin.dto.UserDto;
import com.github.pig.admin.dto.UserInfo;
import com.github.pig.admin.entity.SysUser;
import com.github.pig.admin.entity.SysUserRole;
import com.github.pig.admin.service.SysMenuService;
import com.github.pig.admin.service.SysUserRoleService;
import com.github.pig.admin.service.UserService;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.UserUtils;
import com.github.pig.common.vo.UserVo;
import com.github.pig.common.web.BaseController;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.xiaoleilu.hutool.io.FileUtil;
import com.xiaoleilu.hutool.util.RandomUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lengleng
 * @date 2017/10/28
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();
    @Autowired
    private UserService userService;
    @Autowired
    private SysUserRoleService sysUserRoleService;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private QiniuPropertiesConfig qiniuPropertiesConfig;


    /**
     * 获取当前用户的用户名
     *
     * @return 用户名
     */
    @GetMapping("/info")
    public UserInfo user() {
        SysUser condition = new SysUser();
        condition.setUsername(UserUtils.getUserName());
        SysUser sysUser = userService.selectOne(new EntityWrapper<>(condition));

        UserInfo userInfo = new UserInfo();
        userInfo.setSysUser(sysUser);
        //设置角色列表
        String[] roles = getRole().toArray(new String[getRole().size()]);
        userInfo.setRoles(roles);
        //设置权限列表（menu.permission）
        String[] permissions = sysMenuService.findPermission(roles);
        userInfo.setPermissions(permissions);
        return userInfo;
    }

    /**
     * 通过ID查询当前用户信息
     *
     * @param id ID
     * @return 用户信息
     */
    @GetMapping("/{id}")
    public SysUser user(@PathVariable Integer id) {
        return userService.selectById(id);
    }

    /**
     * 删除用户信息
     *
     * @param id ID
     * @return boolean
     */
    @DeleteMapping("/{id}")
    public Boolean userDel(@PathVariable Integer id) {
        SysUser sysUser = userService.selectById(id);
        sysUser.setDelFlag(CommonConstant.STATUS_DEL);
        return userService.updateById(sysUser);
    }

    /**
     * 添加用户
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @PostMapping
    public Boolean user(@RequestBody UserDto userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setDelFlag(CommonConstant.STATUS_NORMAL);
        sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
        userService.insert(sysUser);
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(sysUser.getUserId());
        userRole.setRoleId(userDto.getRole());
        return userRole.insert();
    }

    /**
     * 更新用户信息
     *
     * @param userDto 用户信息
     * @return boolean
     */
    @PutMapping
    public Boolean userUpdate(@RequestBody UserDto userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setUpdateTime(new Date());
        userService.updateById(sysUser);

        SysUserRole condition = new SysUserRole();
        condition.setUserId(userDto.getUserId());
        SysUserRole sysUserRole = sysUserRoleService.selectOne(new EntityWrapper<>(condition));
        sysUserRole.setRoleId(userDto.getRole());
        return sysUserRoleService.update(sysUserRole, new EntityWrapper<>(condition));
    }

    /**
     * 通过用户名查询用户及其角色信息
     *
     * @param username 用户名
     * @return UseVo 对象
     */
    @GetMapping("/findUserByUsername/{username}")
    public UserVo findUserByUsername(@PathVariable String username) {
        return userService.findUserByUsername(username);
    }

    /**
     * 分页查询用户
     *
     * @param page    页码
     * @param limit   每页数量
     * @param sysUser 检索条件
     * @return 用户集合
     */
    @RequestMapping("/userPage")
    public Page userPage(Integer page, Integer limit, SysUser sysUser) {
        return userService.selectWithRolePage(new Page<>(page, limit), sysUser);
    }

    /**
     * 上传用户头像
     * (多机部署有问题，建议使用独立的文件服务器)
     *
     * @param file    资源
     * @param request request
     * @return filename map
     */
    @PostMapping("/upload")
    public Map<String, String> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        String fileExt = FileUtil.extName(file.getOriginalFilename());
        Configuration cfg = new Configuration(Zone.zone0());
        UploadManager uploadManager = new UploadManager(cfg);
        String key = RandomUtil.randomUUID() + "." + fileExt;
        Auth auth = Auth.create(qiniuPropertiesConfig.getAccessKey(), qiniuPropertiesConfig.getSecretKey());
        String upToken = auth.uploadToken(qiniuPropertiesConfig.getBucket());
        try {
            uploadManager.put(file.getInputStream(), key, upToken, null, null);
        } catch (Exception e) {
            logger.error("文件上传异常", e);
            throw new RuntimeException(e);
        }
        Map<String, String> resultMap = new HashMap<>(1);
        resultMap.put("filename", qiniuPropertiesConfig.getQiniuHost() + key);
        return resultMap;
    }

    /**
     * 修改个人信息
     *
     * @param userDto userDto
     * @return success/false
     */
    @PutMapping("/editInfo")
    public Boolean editInfo(@RequestBody UserDto userDto) {
        String username = UserUtils.getUserName();
        UserVo userVo = userService.findUserByUsername(username);

        if (!ENCODER.matches(userDto.getPassword(), userVo.getPassword())) {
            return Boolean.FALSE;
        }

        SysUser sysUser = new SysUser();
        sysUser.setUserId(userVo.getUserId());
        sysUser.setPassword(ENCODER.encode(userDto.getNewpassword1()));
        sysUser.setAvatar(userDto.getAvatar());
        return userService.updateById(sysUser);
    }

}
