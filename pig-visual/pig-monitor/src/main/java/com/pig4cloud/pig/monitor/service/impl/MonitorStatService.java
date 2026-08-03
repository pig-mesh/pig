package com.pig4cloud.pig.monitor.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import com.alibaba.druid.stat.DruidStatServiceMBean;
import com.alibaba.druid.support.http.stat.WebAppStatManager;
import com.alibaba.druid.support.spring.stat.SpringStatManager;
import com.alibaba.druid.util.MapComparator;
import com.alibaba.druid.util.StringUtils;
import com.pig4cloud.pig.monitor.config.DruidMonitorConfigurer;
import com.pig4cloud.pig.monitor.model.ServiceNode;
import com.pig4cloud.pig.monitor.model.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分布式 Druid 监控数据聚合服务。
 * <p>
 * 负责从注册中心发现被监控实例，采集各实例的 Druid 数据并保持 Druid 控制台要求的 JSON 字段与数值类型。
 *
 * @author linchtech
 * @date 2020-09-16 11:12
 **/
@Slf4j
@Component
public class MonitorStatService implements DruidStatServiceMBean {

	/** Druid 请求处理成功状态码。 */
	public final static int RESULT_CODE_SUCCESS = 1;

	/** Druid 请求处理失败状态码。 */
	public final static int RESULT_CODE_ERROR = -1;

	private final static int DEFAULT_PAGE = 1;

	private final static int DEFAULT_PER_PAGE_COUNT = Integer.MAX_VALUE;

	private static final String ORDER_TYPE_DESC = "desc";

	private static final String ORDER_TYPE_ASC = "asc";

	private static final String DEFAULT_ORDER_TYPE = ORDER_TYPE_ASC;

	private static final String DEFAULT_ORDERBY = "SQL";

	/**
	 * 以注册中心服务实例 ID 为键，保存对应的微服务节点。
	 */
	public static Map<String, ServiceNode> serviceIdMap = new HashMap<>();

	@Autowired
	private DiscoveryClient discoveryClient;

	@Autowired
	private DruidMonitorConfigurer.MonitorProperties monitorProperties;

	/**
	 * 获取配置范围内的全部服务节点。
	 * @return 以服务名、地址和端口组合值为键的服务节点映射
	 */
	public Map<String, ServiceNode> getAllServiceNodeMap() {
		List<String> services = discoveryClient.getServices();
		List<ServiceNode> serviceNodes = new ArrayList<>();
		for (String service : services) {
			List<ServiceInstance> instances = discoveryClient.getInstances(service);
			for (ServiceInstance instance : instances) {
				String host = instance.getHost();
				int port = instance.getPort();
				String instanceId = String.format("%s_%s", host, port);
				String serviceId = instance.getServiceId();
				// 根据前端参数采集指定的服务
				if (monitorProperties.getApplications().contains(serviceId)) {
					ServiceNode serviceNode = new ServiceNode();
					serviceNode.setId(instanceId);
					serviceNode.setPort(port);
					serviceNode.setAddress(host);
					serviceNode.setServiceName(serviceId);
					serviceNodes.add(serviceNode);
					serviceIdMap.put(instanceId, serviceNode);
				}
			}
		}
		return serviceNodes.parallelStream()
			.collect(Collectors.toMap(i -> i.getServiceName() + "-" + i.getAddress() + "-" + i.getPort(),
					Function.identity(), (v1, v2) -> v2));
	}

	/**
	 * 获取指定服务名的全部节点。
	 * @param parameters Druid 请求参数，必须包含 {@code serviceName}
	 * @return 以服务名、地址和端口组合值为键的服务节点映射
	 */
	public Map<String, ServiceNode> getServiceAllNodeMap(Map<String, String> parameters) {
		String requestServiceName = parameters.get("serviceName");
		List<String> services = discoveryClient.getServices();
		List<ServiceNode> serviceNodes = new ArrayList<>();

		for (String service : services) {
			List<ServiceInstance> instances = discoveryClient.getInstances(service);
			for (ServiceInstance instance : instances) {
				String host = instance.getHost();
				int port = instance.getPort();
				String serviceId = instance.getServiceId();
				String instanceId = String.format("%s_%s", host, port);
				// 根据前端参数采集指定的服务
				if (serviceId.equals(requestServiceName)) {
					ServiceNode serviceNode = new ServiceNode();
					serviceNode.setId(instanceId);
					serviceNode.setPort(port);
					serviceNode.setAddress(host);
					serviceNode.setServiceName(serviceId);
					serviceNodes.add(serviceNode);
					serviceIdMap.put(instanceId, serviceNode);
				}
			}
		}
		return serviceNodes.parallelStream()
			.collect(Collectors.toMap(i -> i.getServiceName() + "-" + i.getAddress() + "-" + i.getPort(),
					Function.identity(), (v1, v2) -> v2));
	}

	/** {@inheritDoc} */
	@Override
	public String service(String url) {
		Map<String, String> parameters = getParameters(url);
		if (url.endsWith("serviceList.json")) {
			return JSONUtil.toJsonStr(monitorProperties.getApplications());
		}

		if (url.equals("/datasource.json")) {
			String serviceName = StringUtils.subString(url, "serviceName=", "&sql-");
			Integer id = StringUtils.subStringToInteger(url, "datasource-", ".");
			return getDataSourceStatData();
		}

		if (url.startsWith("/datasource-")) {
			String serviceName = StringUtils.subString(url, "serviceName=", "&sql-");
			Integer id = StringUtils.subStringToInteger(url, "datasource-", ".");
			Object result = getDataSourceStatData();
			return returnJSONResult(result == null ? RESULT_CODE_ERROR : RESULT_CODE_SUCCESS, result);
		}
		// 活跃连接数查看
		if (url.startsWith("/connectionInfo-") && url.endsWith(".json")) {
			String serviceId = StringUtils.subString(url, "&serviceId=", ".json");
			Integer id = StringUtils.subStringToInteger(url, "connectionInfo-", "&");
			return getPoolingConnectionInfoByDataSourceId(id, serviceId);
		}

		// SQL监控列表
		if (url.startsWith("/sql.json")) {
			return getSqlStatDataList(parameters);
		}

		// SQL防火墙
		if (url.startsWith("/wall.json")) {
			return getWallStatMap(parameters);
		}

		// SQL详情
		if (url.startsWith("/serviceId") && url.indexOf(".json") > 0) {
			String serviceId = StringUtils.subString(url, "serviceId=", "&");
			Integer id = StringUtils.subStringToInteger(url, "sql-", ".json");
			return getSqlStat(id, serviceId);
		}

		if (url.startsWith("/weburi.json")) {
			return getWebURIStatDataList(parameters);
		}

		if (url.startsWith("/weburi-") && url.indexOf(".json") > 0) {
			String uri = StringUtils.subString(url, "weburi-", ".json", true);
			return returnJSONResult(RESULT_CODE_SUCCESS, getWebURIStatData(uri));
		}

		if (url.startsWith("/webapp.json")) {
			return returnJSONResult(RESULT_CODE_SUCCESS, getWebAppStatDataList(parameters));
		}

		if (url.startsWith("/websession.json")) {
			return returnJSONResult(RESULT_CODE_SUCCESS, getWebSessionStatDataList(parameters));
		}

		if (url.startsWith("/websession-") && url.indexOf(".json") > 0) {
			String id = StringUtils.subString(url, "websession-", ".json");
			return returnJSONResult(RESULT_CODE_SUCCESS, getWebSessionStatData(id));
		}

		if (url.startsWith("/spring.json")) {
			return returnJSONResult(RESULT_CODE_SUCCESS, getSpringStatDataList(parameters));
		}

		if (url.startsWith("/spring-detail.json")) {
			String clazz = parameters.get("class");
			String method = parameters.get("method");
			return returnJSONResult(RESULT_CODE_SUCCESS, getSpringMethodStatData(clazz, method));
		}

		return returnJSONResult(RESULT_CODE_ERROR, "Do not support this request, please contact with administrator.");
	}

	/**
	 * 将 Druid 本地监控结果包装成标准响应。
	 * @param resultCode Druid 响应状态码
	 * @param content 响应内容
	 * @return Druid 标准 JSON 响应
	 */
	public static String returnJSONResult(int resultCode, Object content) {
		return JSONUtil.createObj(JSONConfig.create().setIgnoreNullValue(false))
			.set("ResultCode", resultCode)
			.set("Content", content)
			.toString();
	}

	/**
	 * 聚合指定服务全部节点的 SQL 防火墙统计。
	 * @param parameters Druid 请求参数
	 * @return 聚合后的 Druid JSON 响应
	 */
	public String getWallStatMap(Map<String, String> parameters) {
		Map<String, ServiceNode> allNodeMap = getServiceAllNodeMap(parameters);
		List<WallResult> countResult = new ArrayList<>();
		for (String nodeKey : allNodeMap.keySet()) {
			ServiceNode serviceNode = allNodeMap.get(nodeKey);
			String url = getRequestUrl(parameters, serviceNode, "/druid/wall.json");
			WallResult wallResult = readJson(HttpUtil.get(url), WallResult.class);
			// 节点无响应或 JSON 为空时 readJson 返回 null，跳过避免聚合 NPE
			if (wallResult == null) {
				continue;
			}
			countResult.add(wallResult);
		}
		WallResult lastCount = new WallResult();

		for (WallResult wallResult : countResult) {
			lastCount.sum(wallResult, lastCount);
		}
		return JSONUtil.toJsonStr(lastCount);
	}

	private List<Map<String, Object>> getSpringStatDataList(Map<String, String> parameters) {
		List<Map<String, Object>> array = SpringStatManager.getInstance().getMethodStatData();
		return comparatorOrderBy(array, parameters);
	}

	private String getWebURIStatDataList(Map<String, String> parameters) {
		Map<String, ServiceNode> allNodeMap = getServiceAllNodeMap(parameters);
		List<Map<String, Object>> arrayMap = new ArrayList<>();
		for (String nodeKey : allNodeMap.keySet()) {
			ServiceNode serviceNode = allNodeMap.get(nodeKey);
			String url = getRequestUrl(parameters, serviceNode, "/druid/weburi.json");
			WebResult dataSourceResult = readJson(HttpUtil.get(url), WebResult.class);
			if (dataSourceResult != null) {
				List<WebResult.ContentBean> nodeContent = dataSourceResult.getContent();
				if (nodeContent != null) {
					for (WebResult.ContentBean contentBean : nodeContent) {
						Map<String, Object> map = JSONUtil.parseObj(contentBean);
						arrayMap.add(map);
					}
				}
			}
		}
		List<Map<String, Object>> maps = comparatorOrderBy(arrayMap, parameters);
		return JSONUtil.createObj().set("ResultCode", RESULT_CODE_SUCCESS).set("Content", maps).toString();
	}

	private Map<String, Object> getWebURIStatData(String uri) {
		return WebAppStatManager.getInstance().getURIStatData(uri);
	}

	private Map<String, Object> getWebSessionStatData(String sessionId) {
		return WebAppStatManager.getInstance().getSessionStat(sessionId);
	}

	private Map<String, Object> getSpringMethodStatData(String clazz, String method) {
		return SpringStatManager.getInstance().getMethodStatData(clazz, method);
	}

	private List<Map<String, Object>> getWebSessionStatDataList(Map<String, String> parameters) {
		List<Map<String, Object>> array = WebAppStatManager.getInstance().getSessionStatData();
		return comparatorOrderBy(array, parameters);
	}

	private List<Map<String, Object>> getWebAppStatDataList(Map<String, String> parameters) {
		List<Map<String, Object>> array = WebAppStatManager.getInstance().getWebAppStatData();
		return comparatorOrderBy(array, parameters);
	}

	/**
	 * 获取指定服务节点的 SQL 详情。
	 * @param id Druid SQL 记录 ID
	 * @param serviceId 注册中心服务实例 ID
	 * @return Druid SQL 详情 JSON
	 */
	private String getSqlStat(Integer id, String serviceId) {
		log.info("serviceId:{}", serviceId);
		ServiceNode serviceNode = serviceIdMap.get(serviceId);
		String url = "http://" + serviceNode.getAddress() + ":" + serviceNode.getPort() + "/druid/sql-" + id + ".json";
		SqlDetailResult sqlDetailResult = readJson(HttpUtil.get(url), SqlDetailResult.class);
		return JSONUtil.toJsonStr(sqlDetailResult);
	}

	/**
	 * 获取指定数据源的活跃连接信息。
	 * @param id Druid 数据源 ID
	 * @param serviceId 注册中心服务实例 ID
	 * @return Druid 活跃连接信息 JSON
	 */
	public String getPoolingConnectionInfoByDataSourceId(Integer id, String serviceId) {
		getAllServiceNodeMap();
		ServiceNode serviceNode = serviceIdMap.get(serviceId);
		String url = "http://" + serviceNode.getAddress() + ":" + serviceNode.getPort() + "/druid/connectionInfo-" + id
				+ ".json";
		ConnectionResult connectionResult = readJson(HttpUtil.get(url), ConnectionResult.class);
		return JSONUtil.toJsonStr(connectionResult);
	}

	/**
	 * 聚合指定服务全部节点的 SQL 监控列表。
	 * @param parameters Druid 请求参数
	 * @return Druid SQL 监控列表 JSON
	 */
	public String getSqlStatDataList(Map<String, String> parameters) {
		Map<String, ServiceNode> serviceAllNodeMap = getServiceAllNodeMap(parameters);
		List<Map<String, Object>> arrayMap = new ArrayList<>();
		for (String nodeKey : serviceAllNodeMap.keySet()) {
			ServiceNode serviceNode = serviceAllNodeMap.get(nodeKey);
			String serviceName = serviceNode.getServiceName();

			String url = getRequestUrl(parameters, serviceNode, "/druid/sql.json");
			SqlListResult sqlListResult = readJson(HttpUtil.get(url), SqlListResult.class);
			if (sqlListResult != null) {
				List<SqlListResult.ContentBean> nodeContent = sqlListResult.getContent();
				if (nodeContent != null) {
					for (SqlListResult.ContentBean contentBean : nodeContent) {
						contentBean.setName(serviceName);
						contentBean.setAddress(serviceNode.getAddress());
						contentBean.setPort(serviceNode.getPort());
						contentBean.setServiceId(serviceNode.getId());
						Map<String, Object> map = JSONUtil.parseObj(contentBean);
						arrayMap.add(map);
					}
				}
			}
		}
		List<Map<String, Object>> maps = comparatorOrderBy(arrayMap, parameters);
		return JSONUtil.createObj().set("ResultCode", RESULT_CODE_SUCCESS).set("Content", maps).toString();
	}

	/**
	 * 聚合配置范围内全部节点的数据源监控信息。
	 * @return Druid 数据源监控 JSON
	 */
	public String getDataSourceStatData() {
		Map<String, ServiceNode> allNodeMap = getAllServiceNodeMap();

		DataSourceResult lastResult = new DataSourceResult();
		List<DataSourceResult.ContentBean> contentBeans = new ArrayList<>();
		for (String nodeKey : allNodeMap.keySet()) {
			ServiceNode serviceNode = allNodeMap.get(nodeKey);
			String serviceName = serviceNode.getServiceName();

			String url = "http://" + serviceNode.getAddress() + ":" + serviceNode.getPort() + "/druid/datasource.json";
			DataSourceResult dataSourceResult = readJson(HttpUtil.get(url), DataSourceResult.class);

			if (dataSourceResult != null) {
				List<DataSourceResult.ContentBean> nodeContent = dataSourceResult.getContent();
				if (nodeContent != null) {
					for (DataSourceResult.ContentBean contentBean : nodeContent) {
						contentBean.setName(serviceName);
						contentBean.setServiceId(serviceNode.getId());
					}
					contentBeans.addAll(nodeContent);
				}
			}
		}
		lastResult.setContent(contentBeans);
		return JSONUtil.toJsonStr(lastResult);
	}

	/**
	 * 将 Druid JSON 解析为指定类型。
	 * @param content Druid JSON 文本
	 * @param valueType 目标类型
	 * @param <T> 目标类型
	 * @return 解析结果；输入为空时返回 {@code null}
	 */
	private <T> T readJson(String content, Class<T> valueType) {
		return StrUtil.isBlank(content) ? null : JSONUtil.toBean(content, valueType);
	}

	/**
	 * 拼接远端 Druid 请求地址。
	 * @param parameters Druid 请求参数
	 * @param serviceNode 目标服务节点
	 * @param prefix Druid 接口路径
	 * @return 远端 Druid 请求地址
	 */
	private String getRequestUrl(Map<String, String> parameters, ServiceNode serviceNode, String prefix) {
		StringBuilder stringBuilder = new StringBuilder("http://");
		stringBuilder.append(serviceNode.getAddress());
		stringBuilder.append(":");
		stringBuilder.append(serviceNode.getPort());
		stringBuilder.append(prefix);
		stringBuilder.append("?orderBy=");
		stringBuilder.append(parameters.get("orderBy"));
		stringBuilder.append("&orderType=");
		stringBuilder.append(parameters.get("orderType"));
		stringBuilder.append("&page=");
		stringBuilder.append(parameters.get("page"));
		stringBuilder.append("&perPageCount=");
		stringBuilder.append(parameters.get("perPageCount"));
		stringBuilder.append("&");
		return stringBuilder.toString();
	}

	/**
	 * 解析 Druid 请求查询参数。
	 * @param url Druid 请求地址
	 * @return 查询参数映射；请求地址为空时返回空映射
	 */
	public static Map<String, String> getParameters(String url) {
		if (url == null || (url = url.trim()).length() == 0) {
			return Collections.emptyMap();
		}

		String parametersStr = StringUtils.subString(url, "?", null);
		if (parametersStr == null || parametersStr.length() == 0) {
			return Collections.<String, String>emptyMap();
		}

		String[] parametersArray = parametersStr.split("&");
		Map<String, String> parameters = new LinkedHashMap<String, String>();

		for (String parameterStr : parametersArray) {
			int index = parameterStr.indexOf("=");
			if (index <= 0) {
				continue;
			}

			String name = parameterStr.substring(0, index);
			String value = parameterStr.substring(index + 1);
			parameters.put(name, value);
		}
		return parameters;
	}

	private List<Map<String, Object>> comparatorOrderBy(List<Map<String, Object>> array,
			Map<String, String> parameters) {
		// when open the stat page before executing some sql
		if (array == null || array.isEmpty()) {
			return null;
		}

		// when parameters is null
		String orderBy, orderType = null;
		int page = DEFAULT_PAGE;
		int perPageCount = DEFAULT_PER_PAGE_COUNT;
		if (parameters == null) {
			orderBy = DEFAULT_ORDERBY;
			orderType = DEFAULT_ORDER_TYPE;
			page = DEFAULT_PAGE;
			perPageCount = DEFAULT_PER_PAGE_COUNT;
		}
		else {
			orderBy = parameters.get("orderBy");
			orderType = parameters.get("orderType");
			String pageParam = parameters.get("page");
			if (pageParam != null && pageParam.length() != 0) {
				page = Integer.parseInt(pageParam);
			}
			String pageCountParam = parameters.get("perPageCount");
			if (pageCountParam != null && pageCountParam.length() > 0) {
				perPageCount = Integer.parseInt(pageCountParam);
			}
		}

		// others,such as order
		orderBy = orderBy == null ? DEFAULT_ORDERBY : orderBy;
		orderType = orderType == null ? DEFAULT_ORDER_TYPE : orderType;

		if (!ORDER_TYPE_DESC.equals(orderType)) {
			orderType = ORDER_TYPE_ASC;
		}

		// orderby the statData array
		if (orderBy.trim().length() != 0) {
			array.sort(new MapComparator<>(orderBy, ORDER_TYPE_DESC.equals(orderType)));
		}

		// page
		int fromIndex = (page - 1) * perPageCount;
		int toIndex = page * perPageCount;
		if (toIndex > array.size()) {
			toIndex = array.size();
		}

		return array.subList(fromIndex, toIndex);
	}

}
