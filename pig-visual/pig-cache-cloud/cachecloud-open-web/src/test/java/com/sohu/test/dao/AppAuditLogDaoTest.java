package com.sohu.test.dao;

import com.sohu.cache.constant.AppAuditLogTypeEnum;
import com.sohu.cache.dao.AppAuditLogDao;
import com.sohu.cache.entity.AppAuditLog;
import com.sohu.test.BaseTest;
import org.junit.Test;

import javax.annotation.Resource;

/**
 * @author leifu
 * @Date 2014年12月23日
 * @Time 上午9:41:16
 */
public class AppAuditLogDaoTest extends BaseTest {
    @Resource(name = "appAuditLogDao")
    private AppAuditLogDao appAuditLogDao;

    @Test
    public void getAuditByType() {
        Long appAuditId = 75L;
        AppAuditLog appAuditLog = appAuditLogDao.getAuditByType(appAuditId, AppAuditLogTypeEnum.APP_CHECK.value());
        logger.info("{}", appAuditLog);
    }

}
