package com.github.pig.monitor.filter;

import com.github.pig.common.constant.MqQueueConstant;
import com.github.pig.common.constant.enums.EnumSmsChannel;
import com.github.pig.common.util.template.MobileMsgTemplate;
import com.github.pig.monitor.config.MonitorMobilePropertiesConfig;
import com.xiaoleilu.hutool.date.DateUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;
import de.codecentric.boot.admin.event.ClientApplicationEvent;
import de.codecentric.boot.admin.event.ClientApplicationStatusChangedEvent;
import de.codecentric.boot.admin.notify.AbstractStatusChangeNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author lengleng
 * @date 2018/1/25
 * 服务下线手机短信通知
 */
@Slf4j
public class MobileNotifier extends AbstractStatusChangeNotifier {
    public static final String STATUS_CHANGE = "STATUS_CHANGE";
    private RabbitTemplate rabbitTemplate;
    private MonitorMobilePropertiesConfig monitorMobilePropertiesConfig;

    public MobileNotifier(MonitorMobilePropertiesConfig monitorMobilePropertiesConfig, RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.monitorMobilePropertiesConfig = monitorMobilePropertiesConfig;
    }

    /**
     * 判断是否通知
     *
     * @param event 事件
     * @return 是、否
     */
    @Override
    protected boolean shouldNotify(ClientApplicationEvent event) {
        boolean shouldNotify = false;
        if (STATUS_CHANGE.equals(event.getType())
                && event.getApplication().getStatusInfo().isOffline()
                || event.getApplication().getStatusInfo().isDown()) {
            shouldNotify = true;
        }
        return shouldNotify;
    }

    /**
     * 通知逻辑
     *
     * @param event 事件
     * @throws Exception 异常
     */
    @Override
    protected void doNotify(ClientApplicationEvent event) {
        if (event instanceof ClientApplicationStatusChangedEvent) {
            log.info("Application {} ({}) is {}", event.getApplication().getName(),
                    event.getApplication().getId(), ((ClientApplicationStatusChangedEvent) event).getTo().getStatus());
            String text = String.format("应用:%s 服务ID:%s 下线，时间：%s", event.getApplication().getName(), event.getApplication().getId(), DateUtil.date(event.getTimestamp()).toString());
            rabbitTemplate.convertAndSend(MqQueueConstant.SERVICE_STATUS_CHANGE,
                    new MobileMsgTemplate(CollectionUtil.join(monitorMobilePropertiesConfig.getUsers(), ","),
                            text, EnumSmsChannel.ALIYUN.getName()));
        } else {
            log.info("Application {} ({}) {}", event.getApplication().getName(),
                    event.getApplication().getId(), event.getType());
        }
    }

}
