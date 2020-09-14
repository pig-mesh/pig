package com.pig4cloud.pig.job.handler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.core.util.ShardingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.xxl.job.core.biz.model.ReturnT.SUCCESS;

/**
 * 示例任务处理器
 *
 * @author lishangbu
 * @date 2020/9/14
 */
@Slf4j
@Component
public class DemoJobHandler {

	@XxlJob("demoJobHandler")
	public ReturnT<String> demoJobHandler(String s) {
		ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
		XxlJobLogger.log("This is a demo job." + shardingVO);
		return SUCCESS;
	}

}
