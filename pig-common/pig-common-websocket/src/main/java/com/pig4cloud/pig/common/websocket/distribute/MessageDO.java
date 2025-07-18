package com.pig4cloud.pig.common.websocket.distribute;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 消息数据对象（Data Object）
 * <p>
 * 用于在消息分发过程中传递消息内容和元数据，如是否广播、目标会话等。
 * </p>
 *
 * @author Hccake 2021/1/12
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MessageDO {

	/**
	 * 是否需要广播消息。
	 * <p>
	 * 如果为 {@code true}，则消息将发送给所有在线的 WebSocket 会话。
	 * </p>
	 */
	private Boolean needBroadcast;

	/**
	 * 目标会话的唯一标识列表。
	 * <p>
	 * 当 {@code needBroadcast} 为 {@code false} 或 {@code null} 时，消息将发送给此列表中的所有会话。
	 * </p>
	 */
	private List<Object> sessionKeys;

	/**
	 * 需要发送的消息文本内容。
	 */
	private String messageText;

	/**
	 * 构建一个需要广播的消息对象。
	 * @param text 要广播的消息文本。
	 * @return 返回一个配置为广播模式的 {@link MessageDO} 实例。
	 * @author lingting 2021-03-25 17:28
	 */
	public static MessageDO broadcastMessage(String text) {
		return new MessageDO().setMessageText(text).setNeedBroadcast(true);
	}

}
