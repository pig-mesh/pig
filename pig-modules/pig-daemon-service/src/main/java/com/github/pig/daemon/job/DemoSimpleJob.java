package com.github.pig.daemon.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.zen.elasticjob.spring.boot.annotation.ElasticJobConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lengleng
 * @date 2018/2/7
 * 测试Job
 */
@Slf4j
@ElasticJobConfig(cron = "0/2 * * * * ?", shardingTotalCount = 3,
        shardingItemParameters = "0=Beijing,1=Shanghai,2=Guangzhou",
        startedTimeoutMilliseconds = 5000L,
        completedTimeoutMilliseconds = 10000L)
public class DemoSimpleJob implements SimpleJob {
    /**
     * 业务执行逻辑
     *
     * @param shardingContext 分片信息
     */
    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("--------------");
    }
}
