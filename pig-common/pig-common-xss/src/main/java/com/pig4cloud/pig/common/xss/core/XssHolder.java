/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.common.xss.core;

/**
 * 利用 ThreadLocal 缓存线程间的数据
 *
 * @author L.cm
 */
public class XssHolder {

	private static final ThreadLocal<Boolean> TL = new ThreadLocal<>();

	private static final ThreadLocal<XssCleanIgnore> TL_IGNORE = new ThreadLocal<>();

	/**
	 * 是否开启
	 * @return boolean
	 */
	public static boolean isEnabled() {
		return Boolean.TRUE.equals(TL.get());
	}

	/**
	 * 标记为开启
	 */
	static void setEnable() {
		TL.set(Boolean.TRUE);
	}

	/**
	 * 保存接口上的 XssCleanIgnore
	 * @param xssCleanIgnore XssCleanIgnore
	 */
	public static void setXssCleanIgnore(XssCleanIgnore xssCleanIgnore) {
		TL_IGNORE.set(xssCleanIgnore);
	}

	/**
	 * 获取接口上的 XssCleanIgnore
	 * @return XssCleanIgnore
	 */
	public static XssCleanIgnore getXssCleanIgnore() {
		return TL_IGNORE.get();
	}

	/**
	 * 关闭 xss 清理
	 */
	public static void remove() {
		TL.remove();
		TL_IGNORE.remove();
	}

}
