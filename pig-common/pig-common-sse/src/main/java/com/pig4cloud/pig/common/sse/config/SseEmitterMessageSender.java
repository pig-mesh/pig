package com.pig4cloud.pig.common.sse.config;

import com.pig4cloud.pig.common.sse.holder.SseEmitterHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collection;

/**
 * SSE消息发送工具类，提供广播和单播消息发送功能
 *
 * @author lengleng
 * @version 1.0
 * @date 2025/06/06
 */
@Slf4j
public class SseEmitterMessageSender {

    /**
     * 广播消息给所有SSE订阅者
     *
     * @param message 要广播的消息内容
     */
	public static void broadcast(String message) {
		Collection<SseEmitter> sseEmitters = SseEmitterHolder.getSseEmitters();
		for (SseEmitter sseEmitter : sseEmitters) {
			send(sseEmitter, message);
        }
    }

    /**
     * 发送消息到指定的SSE连接
     *
     * @param sseEmitterKey SSE连接标识键
     * @param message       要发送的消息内容
     * @return 发送是否成功
	 */
	public static boolean send(Object sseEmitterKey, String message) {
		SseEmitter sseEmitter = SseEmitterHolder.getSseEmitter(sseEmitterKey);
		if (sseEmitter == null) {
			log.info("[send] 当前 sseEmitterKey：{} 对应 sseEmitter 不在本服务中", sseEmitterKey);
			return false;
		}
		else {
			return send(sseEmitter, message);
        }
    }

    /**
     * 发送消息到SSE连接
     *
     * @param sseEmitter SSE连接对象
     * @param message    要发送的消息内容
     * @return 发送成功返回true，失败返回false
	 */
	public static boolean send(SseEmitter sseEmitter, String message) {
		if (sseEmitter == null) {
			log.error("[send] sseEmitter 为 null");
			return false;
		}


		try {
			sseEmitter.send(message);
		}
		catch (IOException e) {
			log.error("[send] sseEmitter({}) 发送消息({}) 异常", sseEmitter, message, e);
			SseEmitterHolder.removeSseEmitter(sseEmitter);
			return false;
		}
		return true;
	}

}
