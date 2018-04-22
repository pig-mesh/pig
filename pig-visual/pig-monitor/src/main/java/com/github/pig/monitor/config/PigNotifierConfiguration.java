package com.github.pig.monitor.config;

import com.github.pig.monitor.filter.StatusChangeNotifier;
import de.codecentric.boot.admin.notify.RemindingNotifier;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.concurrent.TimeUnit;

/**
 * @author lengleng
 * @date 2018/1/25
 *  监控提醒配置
 */
@Configuration
@EnableScheduling
public class PigNotifierConfiguration {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private MonitorPropertiesConfig monitorPropertiesConfig;
    @Bean
    @Primary
    public RemindingNotifier remindingNotifier() {
        RemindingNotifier remindingNotifier = new RemindingNotifier(mobileNotifier());
        remindingNotifier.setReminderPeriod(TimeUnit.MINUTES.toMillis(1));
        return remindingNotifier;
    }

    @Bean
    public StatusChangeNotifier mobileNotifier(){
        return new StatusChangeNotifier(monitorPropertiesConfig,rabbitTemplate);
    }

    @Scheduled(fixedRate = 60_000L)
    public void remind() {
        remindingNotifier().sendReminders();
    }
}