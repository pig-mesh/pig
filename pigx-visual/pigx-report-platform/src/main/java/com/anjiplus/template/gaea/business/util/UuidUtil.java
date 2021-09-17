package com.anjiplus.template.gaea.business.util;

import java.util.UUID;

/**
 * Created by raodeming on 2021/8/19.
 */
public class UuidUtil {

	public static String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
			"o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8",
			"9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
			"U", "V", "W", "X", "Y", "Z" };

	/**
	 * 8位短id
	 * @return
	 */
	public static String generateShortUuid() {
		StringBuilder builder = new StringBuilder();
		String uuid = UUID.randomUUID().toString().replace("-", "");
		for (int i = 0; i < 8; i++) {
			String str = uuid.substring(i * 4, i * 4 + 4);
			int x = Integer.parseInt(str, 16);
			builder.append(chars[x % 0x3E]);
		}
		return builder.toString();

	}

	public static String generateUuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println(generateShortUuid());
		}
	}

}
