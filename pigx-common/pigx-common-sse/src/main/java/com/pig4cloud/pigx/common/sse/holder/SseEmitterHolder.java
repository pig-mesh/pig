package com.pig4cloud.pigx.common.sse.holder;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SseEmitter 持有者 主要用于保存当前所有在线的会话信息
 *
 * @author lengleng 2021/1/4
 * @version 1.0
 */
public final class SseEmitterHolder {

	private SseEmitterHolder() {
	}

	private static final Map<String, SseEmitter> USER_SSE_EMITTER_MAP = new ConcurrentHashMap<>();

	/**
	 * 添加一个 sseEmitter
	 * @param sseEmitterKey sseEmitter 唯一标识
	 * @param sseEmitter 待添加的 SseEmitter
	 */
	public static void addSseEmitter(Object sseEmitterKey, SseEmitter sseEmitter) {
		USER_SSE_EMITTER_MAP.put(sseEmitterKey.toString(), sseEmitter);
	}

	/**
	 * 删除一个 sseEmitter
	 * @param sseEmitterKey sseEmitter唯一标识
	 */
	public static void removeSseEmitter(Object sseEmitterKey) {
		USER_SSE_EMITTER_MAP.remove(sseEmitterKey.toString());
	}

	/**
	 * 获取指定标识的 sseEmitter
	 * @param sseEmitterKey sseEmitter唯一标识
	 * @return SseEmitter 该标识对应的 sseEmitter
	 */
	public static SseEmitter getSseEmitter(Object sseEmitterKey) {
		return USER_SSE_EMITTER_MAP.get(sseEmitterKey.toString());
	}

	/**
	 * 获取当前所有在线的 sseEmitter
	 * @return Collection<SseEmitter> sseEmitter集合
	 */
	public static Collection<SseEmitter> getSseEmitters() {
		return USER_SSE_EMITTER_MAP.values();
	}

	/**
	 * 获取所有在线的用户标识
	 * @return Set<Object> sseEmitter唯一标识集合
	 */
	public static Set<String> getSseEmitterKeys() {
		return USER_SSE_EMITTER_MAP.keySet();
	}

}
