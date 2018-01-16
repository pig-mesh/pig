package com.github.pig.common.util.sms;


import com.alibaba.fastjson.JSONObject;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 阿里大于短信发送工具类
 *
 * @author 浅梦
 * @date 2018年1月16日
 */
@Slf4j
public class AliDaYuSendUtils {
    //正式环境
    private static final String URL = "http://gw.api.taobao.com/router/rest";
    private static final String KEY = "";
    private static final String SECRET = "";
    public static final String CHANNEL = "";
    /**
     * 短信签名
     */
    public static final String SIGN_NAME_LOGIN = "登录验证";

    /**
     * 发送短信验证码
     * @param channel
     * @param sign_name
     * @param phone
     * @param code
     * @return
     */
    public static boolean sendSmsCode(String channel, String sign_name, String phone, String code){
        // 配置连接参数URL、KEY、SECRET
        TaobaoClient taobaoClient = new DefaultTaobaoClient(URL, KEY, SECRET);
        // 配置请求参数
        AlibabaAliqinFcSmsNumSendRequest request = new AlibabaAliqinFcSmsNumSendRequest();
        /**
         * 公共回传参数，在“消息返回”中会透传回该参数；举例：用户可以传入自己下级的会员ID，在消息返回时，该会员ID会包含在内，用户可以根据该会员ID识别是哪位会员使用了你的应用
         */
        request.setExtend(phone);
        /**
         * 短信接收号码。支持单个或多个手机号码，传入号码为11位手机号码，不能入加0或+86。群发短信需传多个号码，以英文逗号分隔，一次调用最多传入200个号码。示例：18600000000,13911111111,13322222222
         */
        request.setRecNum(phone);
        /**
         * 短信签名，传入的短信签名必须是在阿里大鱼“管理中心-短信签名管理”中的可用签名。如“阿里大鱼”已在短信签名管理中通过审核，则可传入”阿里大鱼“（传参时去掉引号）作为短信签名。短信效果示例：【阿里大鱼】欢迎使用阿里大鱼服务。
         */
        request.setSmsFreeSignName(sign_name);
        /**
         * 短信模板变量，传参规则{"key":"value"}，key的名字须和申请模板中的变量名一致，多个变量之间以逗号隔开。示例：针对模板“验证码${code}，您正在进行${product}身份验证，打死不要告诉别人哦！”，传参时需传入{"code":"1234","product":"alidayu"}
         */
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("product","pig_cloud");
        jsonObject.put("code",code);
        request.setSmsParamString(jsonObject.toString());
        /**
         * 短信模板ID，传入的模板必须是在阿里大鱼“管理中心-短信模板管理”中的可用模板。示例：SMS_585014
         */
        request.setSmsTemplateCode(channel);
        /**
         * 短信类型，传入值请填写normal
         */
        request.setSmsType("normal");
        try {
            // 请求接口并获取返回值
            AlibabaAliqinFcSmsNumSendResponse response = taobaoClient.execute(request);
            return response.getResult().getSuccess();
        }catch (Exception e){
            return false;
        }
    }


    /**
     * 测试一哈
     * @param args
     */
    public static void main(String[] args) {
        log.info("发送结果：" + sendSmsCode(CHANNEL, SIGN_NAME_LOGIN, "1008611", String.valueOf((int)(Math.random()*9+1)*1000)));
    }
}
