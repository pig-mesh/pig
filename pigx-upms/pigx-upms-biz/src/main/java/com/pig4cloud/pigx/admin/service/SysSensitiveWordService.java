package com.pig4cloud.pigx.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.admin.api.entity.SysSensitiveWordEntity;

import java.util.List;

public interface SysSensitiveWordService extends IService<SysSensitiveWordEntity> {

    /**
     * 查询敏感词
     * @param sysSensitiveWord 敏感词
     * @return 敏感词列表
     */
    List<String> matchSensitiveWord(SysSensitiveWordEntity sysSensitiveWord);

    /**
     * 保存敏感词
     * @param sysSensitiveWord
     * @return
     */
    Boolean saveSensitive(SysSensitiveWordEntity sysSensitiveWord);
}
