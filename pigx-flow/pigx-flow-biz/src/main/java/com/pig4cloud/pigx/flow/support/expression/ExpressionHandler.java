package com.pig4cloud.pigx.flow.support.expression;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.EscapeUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.aviator.AviatorEvaluator;
import com.pig4cloud.pigx.admin.api.entity.SysUser;
import com.pig4cloud.pigx.admin.api.feign.RemoteUserService;
import com.pig4cloud.pigx.common.core.util.R;
import com.pig4cloud.pigx.flow.constant.NodeUserTypeEnum;
import com.pig4cloud.pigx.flow.dto.NodeUser;
import com.pig4cloud.pigx.flow.dto.SelectValue;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.flowable.engine.delegate.DelegateExecution;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 流程表达式处理器
 * <p>
 * 该类负责处理流程节点中的条件表达式，支持多种数据类型的比较和判断： 1. 日期时间类型的比较 2. 数字类型的比较和包含判断 3. 字符串的相等和包含判断 4.
 * 单选/多选的判断 5. 部门和用户的归属判断
 * <p>
 * 使用Aviator表达式引擎进行动态表达式计算，支持在流程定义中配置灵活的条件判断逻辑
 *
 * @author pigx
 * @date 2026-02-10
 */
@Slf4j
@Component("expressionHandler")
@RequiredArgsConstructor
public class ExpressionHandler {

	private final RemoteUserService remoteUserService;

	private final ObjectMapper objectMapper;

	/**
	 * 从流程变量中获取用户ID
	 * @param key 流程变量的键名
	 * @param execution 流程执行上下文
	 * @return 用户ID
	 * @throws Exception 当JSON解析失败时抛出异常
	 */
	@SneakyThrows
	public Long getUserId(String key, DelegateExecution execution) {
		Object variable = execution.getVariable(key);
		NodeUser nodeUserDto = objectMapper
			.readValue(objectMapper.writeValueAsString(variable), new TypeReference<List<NodeUser>>() {
			})
			.get(0);
		return nodeUserDto.getId();
	}

	/**
	 * 日期时间比较
	 * <p>
	 * 比较流程变量中的日期时间值与给定参数值的大小关系
	 * @param key 流程变量的键名
	 * @param symbol 比较符号（如：>, <, >=, <=, ==, !=）
	 * @param param 比较的参数值
	 * @param execution 流程执行上下文
	 * @param format 时间格式化模式（如：yyyy-MM-dd HH:mm:ss）
	 * @return 比较结果，true表示条件成立，false表示条件不成立
	 */
	public boolean dateTimeCompare(String key, String symbol, Object param, DelegateExecution execution,
			String format) {

		Object value = execution.getVariable(key);

		// 表单值为空
		if (value == null) {
			return false;
		}

		long valueTime = DateUtil.parse(value.toString(), format).getTime();
		long paramTime = DateUtil.parse(param.toString(), format).getTime();

		return compare(StrUtil.format("key{}{}", symbol, paramTime), Dict.create().set("key", valueTime));
	}

	/**
	 * 数字类型比较
	 * <p>
	 * 比较流程变量中的数字值与给定参数值的大小关系
	 * @param key 表单key，对应流程变量的键名
	 * @param symbol 比较符号（如：>, <, >=, <=, ==, !=）
	 * @param param 比较的参数值
	 * @param execution 流程执行上下文
	 * @return 比较结果，true表示条件成立，false表示条件不成立
	 */
	public boolean numberCompare(String key, String symbol, Object param, DelegateExecution execution) {

		Object value = execution.getVariable(key);

		// 表单值为空
		if (value == null) {
			return false;
		}

		return compare(StrUtil.format("key{}{}", symbol, param), Dict.create().set("key", Convert.toNumber(value)));

	}

	/**
	 * 通用比较方法
	 * <p>
	 * 使用Aviator表达式引擎执行比较表达式
	 * @param symbol 包含比较符号的表达式字符串
	 * @param value 包含变量值的字典
	 * @return 比较结果
	 */
	private Boolean compare(String symbol, Dict value) {
		Object result = AviatorEvaluator.getInstance().execute(symbol, value);
		// 渲染结果
		log.debug("验证结果:{}", result);
		return Convert.toBool(result, false);
	}

	/**
	 * 日期类型对比
	 * <p>
	 * 比较流程变量中的日期值与给定参数日期的大小关系
	 * @param key 表单key，对应流程变量的键名
	 * @param symbol 比较符号（如：>, <, >=, <=, ==, !=）
	 * @param param 比较的参数日期值
	 * @param execution 流程执行上下文
	 * @param format 日期格式化字符串（如：yyyy-MM-dd）
	 * @return 比较结果，true表示条件成立，false表示条件不成立
	 */
	public boolean dateCompare(String key, String symbol, Object param, DelegateExecution execution, String format) {

		Object value = execution.getVariable(key);

		// 表单值为空
		if (value == null) {
			return false;
		}

		// 处理表单值
		DateTime valueDateTime = DateUtil.parse(value.toString(), format);
		log.debug("表单值：{} 格式化显示：{}", valueDateTime.getTime(), DateUtil.formatDateTime(valueDateTime));
		// 处理参数值
		DateTime paramDateTime = DateUtil.parse(param.toString(), format);
		log.debug("参数值：{} 格式化显示：{}", paramDateTime.getTime(), DateUtil.formatDateTime(paramDateTime));

		// 获取模板
		return compare(StrUtil.format("key{}{}", symbol, paramDateTime.getTime()),
				Dict.create().set("key", valueDateTime.getTime()));

	}

	/**
	 * 判断数字数组包含
	 * <p>
	 * 检查流程变量中的数字值是否在给定的数组中
	 * @param key 表单key，对应流程变量的键名
	 * @param execution 流程执行上下文
	 * @param array 条件值数组
	 * @return true表示包含，false表示不包含
	 */
	public boolean numberContain(String key, DelegateExecution execution, Object... array) {

		Object value = execution.getVariable(key);

		if (value == null) {
			return false;
		}

		return numberContain(value, array);
	}

	/**
	 * 内部方法：检查数字值是否在数组中
	 * @param value 要检查的值
	 * @param array 目标数组
	 * @return true表示包含，false表示不包含
	 */
	private static boolean numberContain(Object value, Object[] array) {
		BigDecimal valueBigDecimal = Convert.toBigDecimal(value);

		for (Object aLong : array) {
			if (valueBigDecimal.compareTo(Convert.toBigDecimal(aLong)) == 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 单选处理
	 * <p>
	 * 检查单选表单的选中值是否在给定的数组中
	 * @param key 表单key，对应流程变量的键名
	 * @param execution 流程执行上下文
	 * @param array 条件值数组
	 * @return true表示选中值在数组中，false表示不在
	 */
	public boolean singleSelectHandler(String key, DelegateExecution execution, String... array) {
		Object value = execution.getVariable(key);

		if (value == null) {
			return false;
		}
		List<SelectValue> list = Convert.toList(SelectValue.class, value);
		if (CollUtil.isEmpty(list)) {
			return false;
		}
		return ArrayUtil.contains(array, list.get(0).getKey());
	}

	/**
	 * 字符串判断包含
	 * <p>
	 * 检查流程变量中的字符串值是否包含指定的子串
	 * @param key 表单key，对应流程变量的键名
	 * @param param 要查找的子串
	 * @param execution 流程执行上下文
	 * @return true表示包含，false表示不包含
	 */
	public boolean stringContain(String key, String param, DelegateExecution execution) {
		Object value = execution.getVariable(key);

		if (value == null) {
			return false;
		}
		return StrUtil.contains(value.toString(), param);
	}

	/**
	 * 字符串判断相等
	 * <p>
	 * 检查流程变量中的字符串值是否与给定参数相等
	 * @param key 表单key，对应流程变量的键名
	 * @param param 比较的参数值
	 * @param execution 流程执行上下文
	 * @return true表示相等，false表示不相等
	 */
	public boolean stringEqual(String key, String param, DelegateExecution execution) {
		Object value = execution.getVariable(key);

		if (value == null) {
			return false;
		}
		return StrUtil.equals(value.toString(), param);
	}

	/**
	 * 部门比较
	 * <p>
	 * 检查流程变量中的部门是否满足给定的条件（属于或不属于指定部门列表）
	 * @param key 表单key，对应流程变量的键名
	 * @param param 部门列表参数（JSON格式）
	 * @param symbol 比较符号（in/==：属于，notin/!=：不属于）
	 * @param execution 流程执行上下文
	 * @return 比较结果
	 * @throws Exception 当JSON解析失败时抛出异常
	 */
	@SneakyThrows
	public boolean deptCompare(String key, String param, String symbol, DelegateExecution execution) {
		param = EscapeUtil.unescape(param);

		Object value = execution.getVariable(key);

		String jsonString = objectMapper.writeValueAsString(value);
		log.debug("表单值：key={} value={} symbol={}", key, jsonString, symbol);
		log.debug("条件  参数：{}", param);
		if (value == null) {
			return false;
		}

		// 表单值
		List<NodeUser> nodeUserDtoList = objectMapper.readValue(jsonString, new TypeReference<>() {
		});
		if (CollUtil.isEmpty(nodeUserDtoList) || nodeUserDtoList.size() != 1) {
			return false;
		}
		NodeUser nodeUserDto = nodeUserDtoList.get(0);

		// 参数

		List<NodeUser> paramDeptList = objectMapper.readValue(param, new TypeReference<List<NodeUser>>() {
		});
		Long deptId = nodeUserDto.getId();
		List<Long> deptIdList = paramDeptList.stream().map(NodeUser::getId).toList();

		return inCompare(symbol, deptId, deptIdList);

	}

	/**
	 * 内部方法：判断ID是否在列表中
	 * @param symbol 比较符号（in/==：属于，notin/!=：不属于）
	 * @param deptId 要检查的ID
	 * @param deptIdList ID列表
	 * @return 比较结果
	 */
	private static boolean inCompare(String symbol, Long deptId, List<Long> deptIdList) {
		if (StrUtil.equalsAny(symbol, "in", "==")) {
			// 属于
			return deptIdList.contains(deptId);
		}
		if (StrUtil.equalsAny(symbol, "notin", "!=")) {
			// 属于
			return !deptIdList.contains(deptId);
		}

		return false;
	}

	/**
	 * 用户比较
	 * <p>
	 * 检查流程变量中的用户是否满足给定的条件（属于或不属于指定用户/部门列表） 支持直接指定用户或通过部门查找用户
	 * @param key 表单key，对应流程变量的键名
	 * @param param 用户/部门列表参数（JSON格式）
	 * @param symbol 比较符号（in/==：属于，notin/!=：不属于）
	 * @param execution 流程执行上下文
	 * @return 比较结果
	 * @throws Exception 当JSON解析失败时抛出异常
	 */
	@SneakyThrows
	public boolean userCompare(String key, String param, String symbol, DelegateExecution execution) {
		param = EscapeUtil.unescape(param);

		Object value = execution.getVariable(key);

		String jsonString = objectMapper.writeValueAsString(value);
		log.debug("表单值：key={} value={}   symbol={} ", key, jsonString, symbol);
		log.debug("条件  参数：{}", param);

		if (value == null) {
			return false;
		}

		// 表单值
		List<NodeUser> nodeUserDtoList = objectMapper.readValue(jsonString, new TypeReference<>() {
		});

		if (CollUtil.isEmpty(nodeUserDtoList) || nodeUserDtoList.size() != 1) {
			return false;
		}

		NodeUser nodeUserDto = nodeUserDtoList.get(0);

		// 参数
		List<NodeUser> paramDeptList = objectMapper.readValue(param, new TypeReference<>() {
		});

		List<Long> deptIdList = paramDeptList.stream()
			.filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.DEPT.getKey()))
			.map(NodeUser::getId)
			.toList();

		List<Long> userIdList = paramDeptList.stream()
			.filter(w -> StrUtil.equals(w.getType(), NodeUserTypeEnum.USER.getKey()))
			.map(NodeUser::getId)
			.toList();

		if (CollUtil.isNotEmpty(deptIdList)) {
			R<List<SysUser>> r = remoteUserService.getUserIdListByDeptIdList(deptIdList);
			List<Long> data = r.getData().stream().map(SysUser::getUserId).toList();
			userIdList.addAll(data);
		}

		return inCompare(symbol, Convert.toLong(nodeUserDto.getId()), userIdList);
	}

	/**
	 * 角色比较
	 * <p>
	 * 检查流程变量中的用户是否拥有指定的角色（属于或不属于指定角色列表）。
	 * 通过角色ID列表查询出拥有这些角色的所有用户ID，再判断当前用户是否在其中。
	 * @param key 表单key，对应流程变量的键名
	 * @param param 角色列表参数（JSON格式）
	 * @param symbol 比较符号（in/==：属于，notin/!=：不属于）
	 * @param execution 流程执行上下文
	 * @return 比较结果
	 */
	@SneakyThrows
	public boolean roleCompare(String key, String param, String symbol, DelegateExecution execution) {
		param = EscapeUtil.unescape(param);

		Object value = execution.getVariable(key);

		String jsonString = objectMapper.writeValueAsString(value);
		log.debug("表单值：key={} value={} symbol={}", key, jsonString, symbol);
		log.debug("条件  参数：{}", param);

		if (value == null) {
			return false;
		}

		// 表单值（发起人或表单中的用户）
		List<NodeUser> nodeUserDtoList = objectMapper.readValue(jsonString, new TypeReference<>() {
		});

		if (CollUtil.isEmpty(nodeUserDtoList) || nodeUserDtoList.size() != 1) {
			return false;
		}

		NodeUser nodeUserDto = nodeUserDtoList.get(0);

		// 参数 - 解析角色列表
		List<NodeUser> paramRoleList = objectMapper.readValue(param, new TypeReference<List<NodeUser>>() {
		});

		List<Long> roleIdList = paramRoleList.stream().map(NodeUser::getId).toList();

		// 根据角色ID列表查询拥有这些角色的所有用户ID
		List<Long> userIdList = new java.util.ArrayList<>();
		if (CollUtil.isNotEmpty(roleIdList)) {
			R<List<Long>> r = remoteUserService.getUserIdListByRoleIdList(roleIdList);
			if (r.getData() != null) {
				userIdList.addAll(r.getData());
			}
		}

		return inCompare(symbol, Convert.toLong(nodeUserDto.getId()), userIdList);
	}

	/**
	 * 岗位比较
	 * <p>
	 * 检查流程变量中的用户是否属于指定的岗位（属于或不属于指定岗位列表）。
	 * 通过岗位ID列表查询出属于这些岗位的所有用户ID，再判断当前用户是否在其中。
	 * @param key 表单key，对应流程变量的键名
	 * @param param 岗位列表参数（JSON格式）
	 * @param symbol 比较符号（in/==：属于，notin/!=：不属于）
	 * @param execution 流程执行上下文
	 * @return 比较结果
	 */
	@SneakyThrows
	public boolean postCompare(String key, String param, String symbol, DelegateExecution execution) {
		param = EscapeUtil.unescape(param);

		Object value = execution.getVariable(key);

		String jsonString = objectMapper.writeValueAsString(value);
		log.debug("表单值：key={} value={} symbol={}", key, jsonString, symbol);
		log.debug("条件  参数：{}", param);

		if (value == null) {
			return false;
		}

		// 表单值（发起人或表单中的用户）
		List<NodeUser> nodeUserDtoList = objectMapper.readValue(jsonString, new TypeReference<>() {
		});

		if (CollUtil.isEmpty(nodeUserDtoList) || nodeUserDtoList.size() != 1) {
			return false;
		}

		NodeUser nodeUserDto = nodeUserDtoList.get(0);

		// 参数 - 解析岗位列表
		List<NodeUser> paramPostList = objectMapper.readValue(param, new TypeReference<List<NodeUser>>() {
		});

		List<Long> postIdList = paramPostList.stream().map(NodeUser::getId).toList();

		// 根据岗位ID列表查询属于这些岗位的所有用户ID
		List<Long> userIdList = new java.util.ArrayList<>();
		if (CollUtil.isNotEmpty(postIdList)) {
			R<List<Long>> r = remoteUserService.getUserIdListByPostIdList(postIdList);
			if (r.getData() != null) {
				userIdList.addAll(r.getData());
			}
		}

		return inCompare(symbol, Convert.toLong(nodeUserDto.getId()), userIdList);
	}

	/**
	 * 天数条件判断
	 * @param execution 流程执行上下文
	 * @return 始终返回true
	 */
	public boolean days(DelegateExecution execution) {
		return true;
	}

}
