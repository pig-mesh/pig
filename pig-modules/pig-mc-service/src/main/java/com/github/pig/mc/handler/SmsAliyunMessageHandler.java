package com.github.pig.mc.handler;

import com.alibaba.fastjson.JSONObject;
import com.github.pig.common.constant.CommonConstant;
import com.github.pig.common.util.Assert;
import com.github.pig.common.util.template.MobileMsgTemplate;
import com.github.pig.mc.config.SmsAliyunPropertiesConfig;
import com.github.pig.mc.utils.constant.SmsChannelTemplateConstant;
import com.github.pig.mc.utils.sms.EnumSmsChannelTemplate;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author 浅梦
 * @date 2018/1/16
 * 阿里大鱼短息服务处理
 */
@Slf4j
@Component(CommonConstant.ALIYUN_SMS)
public class SmsAliyunMessageHandler extends AbstractMessageHandler {
    @Autowired
    private SmsAliyunPropertiesConfig smsAliyunPropertiesConfig;
    private static final String URL = "http://gw.api.taobao.com/router/rest";

    /**
     * 数据校验
     *
     * @param mobileMsgTemplate 消息
     */
    @Override
    public void check(MobileMsgTemplate mobileMsgTemplate) {
        Assert.isBlank(mobileMsgTemplate.getMobile(), "手机号不能为空");
        Assert.isBlank(mobileMsgTemplate.getText(), "验证码不能为空");
    }

    /**
     * 业务处理
     *
     * @param mobileMsgTemplate 消息
     */
    @Override
    public boolean process(MobileMsgTemplate mobileMsgTemplate) {
        // 配置连接参数URL、KEY、SECRET
        TaobaoClient taobaoClient = new DefaultTaobaoClient(URL, smsAliyunPropertiesConfig.getAccessKey(), smsAliyunPropertiesConfig.getSecretKey());
        // 配置请求参数
        AlibabaAliqinFcSmsNumSendRequest request = new AlibabaAliqinFcSmsNumSendRequest();
        /**
         * 公共回传参数，在“消息返回”中会透传回该参数；举例：用户可以传入自己下级的会员ID，在消息返回时，该会员ID会包含在内，用户可以根据该会员ID识别是哪位会员使用了你的应用
         */
        request.setExtend(mobileMsgTemplate.getMobile());
        /**
         * 短信接收号码。支持单个或多个手机号码，传入号码为11位手机号码，不能入加0或+86。群发短信需传多个号码，以英文逗号分隔，一次调用最多传入200个号码。示例：18600000000,13911111111,13322222222
         */
        request.setRecNum(mobileMsgTemplate.getMobile());
        /**
         * 短信签名，传入的短信签名必须是在阿里大鱼“管理中心-短信签名管理”中的可用签名。如“阿里大鱼”已在短信签名管理中通过审核，则可传入”阿里大鱼“（传参时去掉引号）作为短信签名。短信效果示例：【阿里大鱼】欢迎使用阿里大鱼服务。
         */
        request.setSmsFreeSignName(EnumSmsChannelTemplate.LOGIN_NAME_LOGIN.getDescription());
        /**
         * 短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开。示例：针对模板“验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！”，传参时需传入{"code":"1234","product":"alidayu"}
         */
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("product","pig_cloud");
        jsonObject.put("code",mobileMsgTemplate.getText());
        request.setSmsParamString(jsonObject.toString());
        /**
         * 短信模板ID，传入的模板必须是在阿里大鱼“管理中心-短信模板管理”中的可用模板。示例：SMS_585014
         */
        request.setSmsTemplateCode(smsAliyunPropertiesConfig.getChannels().get(SmsChannelTemplateConstant.LOGIN_NAME_LOGIN));
        /**
         * 短信类型，传入值请填写normal
         */
        request.setSmsType("normal");
        try {
            AlibabaAliqinFcSmsNumSendResponse response = taobaoClient.execute(request);
            return response.getResult().getSuccess();
        }catch (Exception e){
            return false;
        }
    }

    /**
     * 失败处理
     *
     * @param mobileMsgTemplate 消息
     */
    @Override
    public void fail(MobileMsgTemplate mobileMsgTemplate) {
        log.error("短信发送失败 -> 网关：{} -> 手机号：{}", mobileMsgTemplate.getType(), mobileMsgTemplate.getMobile());
    }
}
