package com.github.pig.admin.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.github.pig.admin.dto.UserDto;
import com.github.pig.admin.dto.UserInfo;
import com.github.pig.admin.entity.SysUser;
import com.github.pig.admin.entity.SysUserRole;
import com.github.pig.admin.service.UserService;
import com.github.pig.common.bean.config.FdfsPropertiesConfig;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.Query;
import com.github.pig.common.util.R;
import com.github.pig.common.vo.UserVo;
import com.github.pig.common.web.BaseController;
import com.luhuiguo.fastdfs.domain.StorePath;
import com.luhuiguo.fastdfs.service.FastFileStorageClient;
import com.xiaoleilu.hutool.io.FileUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private UserService userService;
    @Autowired
    private FdfsPropertiesConfig fdfsPropertiesConfig;


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
    @ApiOperation(value = "更新用户详细信息", notes = "根据传过来的UserDto信息来更新用户详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userDto", value = "用户详细实体user", required = true, dataType = "UserDto")
    })
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
     * 通过手机号查询用户及其角色信息
     *
     * @param mobile 手机号
     * @return UseVo 对象
     */
    @GetMapping("/findUserByMobile/{mobile}")
    public UserVo findUserByMobile(@PathVariable String mobile) {
        return userService.findUserByMobile(mobile);
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
        Map<String, String> resultMap = new HashMap<>(1);
        try {
            StorePath storePath = fastFileStorageClient.uploadFile(file.getBytes(),fileExt);
            resultMap.put("filename", fdfsPropertiesConfig.getFileHost() + storePath.getFullPath());
        } catch (IOException e) {
            logger.error("文件上传异常", e);
            throw new RuntimeException(e);
        }
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
