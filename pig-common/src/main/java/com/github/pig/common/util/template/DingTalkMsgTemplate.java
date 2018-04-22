package com.github.pig.common.util.template;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author lengleng
 * @date 2018/1/15
 * 钉钉消息模板
 * msgtype : text
 * text : {"content":"服务: pig-upms-service 状态：UP"}
 */
@Data
@ToString
public class DingTalkMsgTemplate implements Serializable {
    private String msgtype;
    private TextBean text;

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public TextBean getText() {
        return text;
    }

    public void setText(TextBean text) {
        this.text = text;
    }

    public static class TextBean {
        /**
         * content : 服务: pig-upms-service 状态：UP
         */

        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
