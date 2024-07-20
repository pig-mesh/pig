package com.pig4cloud.pigx.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.pig4cloud.pigx.admin.api.constant.SystemConfigTypeEnum;
import com.pig4cloud.pigx.admin.api.entity.SysSystemConfigEntity;
import com.pig4cloud.pigx.admin.mapper.SysSystemConfigMapper;
import com.pig4cloud.pigx.admin.service.SysSystemConfigService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.common.log.util.JacksonSensitiveFieldUtil;
import lombok.SneakyThrows;
import org.dromara.oa.api.OaSender;
import org.dromara.oa.core.provider.factory.OaFactory;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * SYS 系统配置服务实现
 *
 * @author lengleng
 * @date 2024/07/18
 */
@Service
public class SysSystemConfigServiceImpl extends ServiceImpl<SysSystemConfigMapper, SysSystemConfigEntity> implements SysSystemConfigService {

    private final SysMessageServiceImpl sysMessageServiceImpl;

    public SysSystemConfigServiceImpl(SysMessageServiceImpl sysMessageServiceImpl) {
        this.sysMessageServiceImpl = sysMessageServiceImpl;
    }

    /**
     * 列出系统配置
     *
     * @param query 查询
     * @return {@link R }
     */
    @Override
    public R listSystemConfig(SysSystemConfigEntity query) {
        List<SysSystemConfigEntity> configEntityList = baseMapper.selectList(Wrappers.query(query));
        configEntityList.stream().forEach(systemConfig -> {
            if (StrUtil.isNotBlank(systemConfig.getConfigValue())) {
                systemConfig.setConfigValue(JacksonSensitiveFieldUtil.readStr(systemConfig.getConfigValue()));
            }
        });

        return R.ok(configEntityList);
    }

    /**
     * 系统配置
     *
     * @param page            页
     * @param sysSystemConfig sys 系统配置
     * @return {@link R }
     */
    @Override
    public R pageSystemConfig(Page page, SysSystemConfigEntity sysSystemConfig) {
        LambdaQueryWrapper<SysSystemConfigEntity> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(StrUtil.isNotBlank(sysSystemConfig.getConfigType()), SysSystemConfigEntity::getConfigType, sysSystemConfig.getConfigType());
        wrapper.like(StrUtil.isNotBlank(sysSystemConfig.getConfigName())
                , SysSystemConfigEntity::getConfigName, sysSystemConfig.getConfigName());
        Page<SysSystemConfigEntity> pageResult = baseMapper.selectPage(page, wrapper);
        pageResult.getRecords().forEach(systemConfig -> {
            if (StrUtil.isNotBlank(systemConfig.getConfigValue())) {
                systemConfig.setConfigValue(JacksonSensitiveFieldUtil.readStr(systemConfig.getConfigValue()));
            }
        });

        return R.ok(pageResult);
    }

    /**
     * 更新系统配置
     *
     * @param sysSystemConfig sys 系统配置
     * @return {@link R }
     */
    @Override
    @SneakyThrows
    public R updateSystemConfig(SysSystemConfigEntity sysSystemConfig) {
        // 更新configValue ，如果 accessSecret,tokenId,sign 属性为空，则不更新以上属性
        SysSystemConfigEntity configEntity = baseMapper.selectById(sysSystemConfig.getId());
        JSONObject oldValue = JSONUtil.parseObj(configEntity.getConfigValue());
        JSONObject newValue = JSONUtil.parseObj(sysSystemConfig.getConfigValue());
        BeanUtil.copyProperties(newValue, oldValue);

        sysSystemConfig.setConfigValue(JSONUtil.toJsonPrettyStr(oldValue));

        baseMapper.updateById(sysSystemConfig);
        // 更新短信配置
        if (SystemConfigTypeEnum.SMS.getValue().equals(sysSystemConfig.getConfigType())
                && Objects.nonNull(SmsFactory.getSmsBlend(sysSystemConfig.getConfigKey()))) {
            SmsFactory.unregister(sysSystemConfig.getConfigKey());
        }

        if (SystemConfigTypeEnum.WEBHOOK.getValue().equals(sysSystemConfig.getConfigType())) {
            // 更新webhook配置, 官方工具类没有提供清除方法，只能通过反射清除
            Field configsField = ReflectUtil.getField(OaFactory.class, "CONFIGS");
            configsField.setAccessible(true);
            Map<String, OaSender> configs = (Map<String, OaSender>) configsField.get(null);
            configs.clear();
        }
        return R.ok();
    }
}
