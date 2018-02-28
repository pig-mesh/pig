package com.sohu.test.dao;

import com.sohu.cache.dao.AppClientReportDataSizeDao;
import com.sohu.cache.entity.AppClientDataSizeStat;
import com.sohu.test.BaseTest;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 客户端内收集数据map的尺寸查询--测试
 * @author leifu
 * @Date 2015年7月13日
 * @Time 下午3:43:20
 */
public class AppClientReportDataSizeDaoTest extends BaseTest {

    @Resource
    private AppClientReportDataSizeDao appClientReportDataSizeDao;

    @Test
    public void testSave() {
        AppClientDataSizeStat stat = new AppClientDataSizeStat();
        stat.setClientIp("10.7.40.201");
        stat.setReportTime(new Date());
        stat.setCollectTime(20150120135000L);
        stat.setCreateTime(new Date());

        stat.setCostMapSize(100);
        stat.setExceptionMapSize(5);
        stat.setValueMapSize(86);
        stat.setCollectMapSize(50);

        appClientReportDataSizeDao.save(stat);
    }

}
