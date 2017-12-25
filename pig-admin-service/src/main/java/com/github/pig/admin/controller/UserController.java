package com.github.pig.admin.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.admin.dto.UserDto;
import com.github.pig.admin.dto.UserInfo;
import com.github.pig.admin.entity.SysUser;
import com.github.pig.admin.entity.SysUserRole;
import com.github.pig.admin.service.UserService;
import com.github.pig.common.bean.config.QiniuPropertiesConfig;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.Query;
import com.github.pig.common.util.R;
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
    private QiniuPropertiesConfig qiniuPropertiesConfig;


    /**
     * 获取当前用户信息（角色、权限）
     *
     * @param userVo 当前用户信息
     * @return 用户名
     */
    @GetMapping("/info")
    public R<UserInfo> user(UserVo userVo) {
        return new R<>(userService.findUserInfo(userVo));
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
     * @return R
     */
    @DeleteMapping("/{id}")
    public R<Boolean> userDel(@PathVariable Integer id) {
        SysUser sysUser = userService.selectById(id);
        return new R<>(userService.deleteUserById(sysUser));
    }

    /**
     * 添加用户
     *
     * @param userDto 用户信息
     * @return success/false
     */
    @PostMapping
    public R<Boolean> user(@RequestBody UserDto userDto) {
        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(userDto, sysUser);
        sysUser.setDelFlag(CommonConstant.STATUS_NORMAL);
        sysUser.setPassword(ENCODER.encode(userDto.getPassword()));
        userService.insert(sysUser);
        SysUserRole userRole = new SysUserRole();
        userRole.setUserId(sysUser.getUserId());
        userRole.setRoleId(userDto.getRole());
        return new R<>(userRole.insert());
    }

    /**
     * 更新用户信息
     *
     * @param userDto 用户信息
     * @return R
     */
    @PutMapping
    public R<Boolean> userUpdate(@RequestBody UserDto userDto) {
        SysUser user = userService.selectById(userDto.getUserId());
        return new R<>(userService.updateUser(userDto, user.getUsername()));
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
     * @param params 参数集
     * @return 用户集合
     */
    @RequestMapping("/userPage")
    public Page userPage(@RequestParam Map<String, Object> params) {
        return userService.selectWithRolePage(new Query(params));
    }

    /**
     * 上传用户头像
     * (多机部署有问题，建议使用独立的文件服务器)
     *
     * @param file 资源
     * @return filename map
     */
    @PostMapping("/upload")
    public Map<String, String> upload(@RequestParam("file") MultipartFile file) {
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
     * @param userVo  登录用户信息
     * @return success/false
     */
    @PutMapping("/editInfo")
    public R<Boolean> editInfo(@RequestBody UserDto userDto, UserVo userVo) {
        return new R<>(userService.updateUserInfo(userDto, userVo.getUsername()));
    }

}
