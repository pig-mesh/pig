/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */

package com.github.pig.monitor;

import de.codecentric.boot.admin.config.EnableAdminServer;
import de.codecentric.boot.admin.notify.LoggingNotifier;
import de.codecentric.boot.admin.notify.Notifier;
import de.codecentric.boot.admin.notify.RemindingNotifier;
import de.codecentric.boot.admin.notify.filter.FilteringNotifier;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.turbine.EnableTurbine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

/**
 * @author lengleng
 * @date 2017-12-26 10:15:30
 * 监控模块
 */
@EnableAdminServer
@EnableTurbine
@EnableDiscoveryClient
@SpringCloudApplication
public class PigMonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PigMonitorApplication.class, args);
    }

    @Configuration
    public static class NotifierConfig {
        @Bean
        @Primary
        public RemindingNotifier remindingNotifier() {
            RemindingNotifier notifier = new RemindingNotifier(filteringNotifier(loggerNotifier()));
            notifier.setReminderPeriod(TimeUnit.SECONDS.toMillis(10));
            return notifier;
        }

        @Scheduled(fixedRate = 1_000L)
        public void remind() {
            remindingNotifier().sendReminders();
        }

        @Bean
        public FilteringNotifier filteringNotifier(Notifier delegate) {
            return new FilteringNotifier(delegate);
        }

        @Bean
        public LoggingNotifier loggerNotifier() {
            return new LoggingNotifier();
        }
    }
}
