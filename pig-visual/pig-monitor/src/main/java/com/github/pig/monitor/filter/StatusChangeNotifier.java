package com.github.pig.monitor.filter;

import com.alibaba.fastjson.JSONObject;
import com.github.pig.common.constant.MqQueueConstant;
import com.github.pig.common.constant.enums.EnumSmsChannel;
import com.github.pig.common.util.template.DingTalkMsgTemplate;
import com.github.pig.common.util.template.MobileMsgTemplate;
import com.github.pig.monitor.config.MonitorPropertiesConfig;
import com.xiaoleilu.hutool.collection.CollectionUtil;
import com.xiaoleilu.hutool.date.DateUtil;
import de.codecentric.boot.admin.event.ClientApplicationEvent;
import de.codecentric.boot.admin.event.ClientApplicationStatusChangedEvent;
import de.codecentric.boot.admin.notify.AbstractStatusChangeNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

/**
 * @author lengleng
 * @date 2018/4/22
 * 服务下线通知
 */
@Slf4j
public class StatusChangeNotifier extends AbstractStatusChangeNotifier {
    public static final String STATUS_CHANGE = "STATUS_CHANGE";
    private RabbitTemplate rabbitTemplate;
    private MonitorPropertiesConfig monitorMobilePropertiesConfig;

    public StatusChangeNotifier(MonitorPropertiesConfig monitorMobilePropertiesConfig, RabbitTemplate rabbitTemplate) {
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

            //开启短信通知
            if (monitorMobilePropertiesConfig.getMobile().getEnabled()) {
                log.info("开始短信通知，内容：{}", text);
                rabbitTemplate.convertAndSend(MqQueueConstant.MOBILE_SERVICE_STATUS_CHANGE,
                        new MobileMsgTemplate(CollectionUtil.join(monitorMobilePropertiesConfig.getMobile().getMobiles(), ","),
                                text, EnumSmsChannel.ALIYUN.getName()));
            }

            if (monitorMobilePropertiesConfig.getDingTalk().getEnabled()) {
                log.info("开始钉钉通知，内容：{}", text);
                rabbitTemplate.convertAndSend(MqQueueConstant.DINGTALK_SERVICE_STATUS_CHANGE, text);
            }


        } else {
            log.info("Application {} ({}) {}", event.getApplication().getName(),
                    event.getApplication().getId(), event.getType());
        }
    }

}
