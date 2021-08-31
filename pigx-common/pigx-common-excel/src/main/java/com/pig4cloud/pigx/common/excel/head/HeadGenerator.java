package com.pig4cloud.pigx.common.excel.head;

/**
 * Excel头生成器，用于自定义生成头部信息
 *
 * @author Hccake 2020/10/27
 * @version 1.0
 */
public interface HeadGenerator {

	/**
	 * <p>
	 * 自定义头部信息
	 * </p>
	 * 实现类根据数据的class信息，定制Excel头<br/>
	 * 具体方法使用参考：https://www.yuque.com/easyexcel/doc/write#b4b9de00
	 * @param clazz 当前sheet的数据类型
	 * @return List<List<String>> Head头信息
	 */
	HeadMeta head(Class<?> clazz);

}
