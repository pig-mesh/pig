package com.pig4cloud.pigx.common.sse.config;

import cn.hutool.core.util.ReflectUtil;
import com.pig4cloud.pigx.common.sse.holder.SseEmitterHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collection;

/**
 * @author lengleng 2021/1/4
 * @version 1.0
 */
@Slf4j
public class SseEmitterMessageSender {

	public static void broadcast(String message) {
		Collection<SseEmitter> sseEmitters = SseEmitterHolder.getSseEmitters();
		for (SseEmitter sseEmitter : sseEmitters) {
			send(sseEmitter, message);
		}
	}

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
