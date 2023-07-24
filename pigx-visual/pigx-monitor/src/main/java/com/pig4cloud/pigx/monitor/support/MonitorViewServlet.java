package com.pig4cloud.pigx.monitor.support;

import com.alibaba.druid.support.jakarta.ResourceServlet;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import com.pig4cloud.pigx.monitor.service.impl.MonitorStatService;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;

/**
 * @author linchtech
 * @date 2020-09-16 11:10
 **/
@Slf4j
public class MonitorViewServlet extends ResourceServlet {

	private MonitorStatService monitorStatService;

	public MonitorViewServlet() {
		super("druid");
	}

	@Override
	public void init() throws ServletException {
		log.info("init MonitorViewServlet");
		super.init();
		monitorStatService = SpringContextHolder.getBean(MonitorStatService.class);
	}

	@Override
	protected String process(String url) {
		return monitorStatService.service(url);
	}

}
