
package com.anjiplus.template.gaea.business.modules.datasource.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anji.plus.gaea.constant.BaseOperationEnum;
import com.anji.plus.gaea.constant.Enabled;
import com.anji.plus.gaea.curd.mapper.GaeaBaseMapper;
import com.anji.plus.gaea.exception.BusinessException;
import com.anji.plus.gaea.exception.BusinessExceptionBuilder;
import com.anji.plus.gaea.utils.GaeaAssert;
import com.anjiplus.template.gaea.business.code.ResponseCode;
import com.anjiplus.template.gaea.business.constant.BusinessConstant;
import com.anjiplus.template.gaea.business.modules.dataset.controller.dto.DataSetDto;
import com.anjiplus.template.gaea.business.modules.datasetparam.service.DataSetParamService;
import com.anjiplus.template.gaea.business.modules.datasource.controller.dto.DataSourceDto;
import com.anjiplus.template.gaea.business.modules.datasource.controller.param.ConnectionParam;
import com.anjiplus.template.gaea.business.modules.datasource.dao.DataSourceMapper;
import com.anjiplus.template.gaea.business.modules.datasource.dao.entity.DataSource;
import com.anjiplus.template.gaea.business.modules.datasource.service.DataSourceService;
import com.anjiplus.template.gaea.business.modules.datasource.service.JdbcService;
import com.anjiplus.template.gaea.business.util.JdbcConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Raod
 * @desc DataSource 数据集服务实现
 * @date 2021-03-18 12:09:57.728203200
 **/
@Service
@Slf4j
public class DataSourceServiceImpl implements DataSourceService {

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Resource(name = "dataSourceRestTemplate")
    private RestTemplate restTemplate;

    @Autowired
    private DataSetParamService dataSetParamService;

    @Autowired
    private JdbcService jdbcService;

    @Override
    public GaeaBaseMapper<DataSource> getMapper() {
        return dataSourceMapper;
    }


    /**
     * 获取所有数据源
     * @return
     */
    @Override
    public List<DataSource> queryAllDataSource() {
        LambdaQueryWrapper<DataSource> wrapper = Wrappers.lambdaQuery();
        wrapper.select(DataSource::getSourceCode, DataSource::getSourceName)
                .eq(DataSource::getEnableFlag, Enabled.YES.getValue());
        wrapper.orderByDesc(DataSource::getUpdateTime);
        return dataSourceMapper.selectList(wrapper);
    }

    /**
     * 测试 连接
     *
     * @param connectionParam
     * @return
     */
    @Override
    public Boolean testConnection(ConnectionParam connectionParam) {
        String sourceType = connectionParam.getSourceType();
        String sourceConfig = connectionParam.getSourceConfig();
        DataSourceDto dto = new DataSourceDto();
        dto.setSourceConfig(sourceConfig);
        switch (sourceType) {
            case JdbcConstants.ELASTIC_SEARCH_SQL:
                testElasticsearchSqlConnection(dto);
                break;
            case JdbcConstants.MYSQL:
            case JdbcConstants.KUDU_IMAPLA:
            case JdbcConstants.ORACLE:
            case JdbcConstants.SQL_SERVER:
            case JdbcConstants.JDBC:
            case JdbcConstants.POSTGRESQL:
                testRelationalDb(dto);
                break;
            case JdbcConstants.HTTP:
                testHttp(dto);
                break;
            default:
                throw BusinessExceptionBuilder.build(ResponseCode.DATA_SOURCE_TYPE_DOES_NOT_MATCH_TEMPORARILY);
        }
        log.info("测试连接成功：{}", JSONObject.toJSONString(connectionParam));
        return true;

    }

    @Override
    public List<JSONObject> execute(DataSourceDto dto) {
        String sourceType = dto.getSourceType();
        switch (sourceType) {
            case JdbcConstants.ELASTIC_SEARCH_SQL:
                return executeElasticsearchSql(dto);
            case JdbcConstants.MYSQL:
            case JdbcConstants.KUDU_IMAPLA:
            case JdbcConstants.ORACLE:
            case JdbcConstants.SQL_SERVER:
            case JdbcConstants.JDBC:
            case JdbcConstants.POSTGRESQL:
                return executeRelationalDb(dto);
            case JdbcConstants.HTTP:
                return executeHttp(dto);
            default:
                throw BusinessExceptionBuilder.build(ResponseCode.DATA_SOURCE_TYPE_DOES_NOT_MATCH_TEMPORARILY);
        }
    }

    /**
     * 执行sql,统计数据total
     *
     * @param dto
     * @return
     */
    @Override
    public long total(DataSourceDto sourceDto, DataSetDto dto) {
        //区分数据类型
        String sourceType = sourceDto.getSourceType();
        switch (sourceType) {
            case JdbcConstants.ELASTIC_SEARCH_SQL:
                return 0;
            case JdbcConstants.MYSQL:
                return mysqlTotal(sourceDto, dto);
            default:
                throw BusinessExceptionBuilder.build(ResponseCode.DATA_SOURCE_TYPE_DOES_NOT_MATCH_TEMPORARILY);
        }

    }

    /**
     * 获取mysql count 和添加limit分页信息
     * @param sourceDto
     * @param dto
     * @return
     */
    public long mysqlTotal(DataSourceDto sourceDto, DataSetDto dto){
        String dynSentence = sourceDto.getDynSentence();
        String sql = "select count(1) as count from (" + dynSentence + ") as gaeaExecute";
        sourceDto.setDynSentence(sql);
        List<JSONObject> result = execute(sourceDto);

        //sql 拼接 limit 分页信息
        int pageNumber = Integer.parseInt(dto.getContextData().getOrDefault("pageNumber", "1").toString());
        int pageSize = Integer.parseInt(dto.getContextData().getOrDefault("pageSize", "10").toString());
        String sqlLimit = " limit " + (pageNumber - 1) * pageSize + "," + pageSize;
        sourceDto.setDynSentence(dynSentence.concat(sqlLimit));
        log.info("当前total：{}, 添加分页参数,sql语句：{}", JSONObject.toJSONString(result), sourceDto.getDynSentence());
        return result.get(0).getLongValue("count");
    }



    public List<JSONObject> executeElasticsearchSql(DataSourceDto dto) {
        analysisHttpConfig(dto);
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(JSONObject.parseObject(dto.getHeader(), Map.class));
        HttpEntity<String> entity = new HttpEntity<>(dto.getDynSentence(), headers);
        ResponseEntity<JSONObject> exchange;
        try {
            exchange = restTemplate.exchange(dto.getApiUrl(), HttpMethod.valueOf(dto.getMethod()), entity, JSONObject.class);
        } catch (Exception e) {
            log.error("error",e);
            throw BusinessExceptionBuilder.build(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, e.getMessage());
        }
        if (exchange.getStatusCode().isError()) {
            throw BusinessExceptionBuilder.build(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, exchange.getBody());
        }
        List<JSONObject> result;
        try {
            JSONObject body = exchange.getBody();
            //解析es sql数据
            if (null == body) {
                return null;
            }
            JSONArray columns = body.getJSONArray("columns");
            JSONArray rows = body.getJSONArray("rows");
            result = new ArrayList<>();
            for (int i = 0; i < rows.size(); i++) {
                JSONArray row = rows.getJSONArray(i);
                JSONObject jsonObject = new JSONObject();
                for (int j = 0; j < row.size(); j++) {
                    String name = columns.getJSONObject(j).getString("name");
                    String value = row.getString(j);
                    jsonObject.put(name, value);
                }
                result.add(jsonObject);
            }
        } catch (Exception e) {
            log.error("error",e);
            throw BusinessExceptionBuilder.build(ResponseCode.ANALYSIS_DATA_ERROR, e.getMessage());
        }
        return result;
    }

    public List<JSONObject> executeRelationalDb(DataSourceDto dto) {
        analysisRelationalDbConfig(dto);
        Connection pooledConnection = null;
        try {
            pooledConnection = jdbcService.getPooledConnection(dto);

            PreparedStatement statement = pooledConnection.prepareStatement(dto.getDynSentence());
            ResultSet rs = statement.executeQuery();

            int columnCount = rs.getMetaData().getColumnCount();

            List<String> columns = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = rs.getMetaData().getColumnLabel(i);
                columns.add(columnName);
            }
            List<JSONObject> list = new ArrayList<>();
            while (rs.next()) {
                JSONObject jo = new JSONObject();
                columns.forEach(t -> {
                    try {
                        Object value = rs.getObject(t);
                        //数据类型转换
                        Object result = dealResult(value);
                        jo.put(t, result);
                    } catch (SQLException throwable) {
                        log.error("error",throwable);
                        throw BusinessExceptionBuilder.build(ResponseCode.EXECUTE_SQL_ERROR, throwable.getMessage());
                    }
                });
                list.add(jo);
            }
            return list;
        } catch (Exception throwable) {
            log.error("error",throwable);
            throw BusinessExceptionBuilder.build(ResponseCode.EXECUTE_SQL_ERROR, throwable.getMessage());
        } finally {
            try {
                if (pooledConnection != null) {
                    pooledConnection.close();
                }
            } catch (SQLException throwable) {
                log.error("error",throwable);
                throw BusinessExceptionBuilder.build(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, throwable.getMessage());
            }
        }
    }

    /**
     * 解决sql返回值 类型问题
     * (through reference chain: java.util.HashMap["pageData"]->java.util.ArrayList[0]->java.util.HashMap["UPDATE_TIME"]->oracle.sql.TIMESTAMP["stream"])
     * @param result
     * @return
     * @throws SQLException
     */
    private Object dealResult(Object result) throws SQLException {
        if (null == result) {
            return result;
        }
        String type = result.getClass().getName();
        if ("oracle.sql.TIMESTAMP".equals(type)) {
            //oracle.sql.TIMESTAMP处理逻辑
            return new Date((Long) JSONObject.toJSON(result));
        }

        return result;
    }

    /**
     * http 执行获取数据
     *
     * @param dto
     */
    public List<JSONObject> executeHttp(DataSourceDto dto) {
        analysisHttpConfig(dto);
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(JSONObject.parseObject(dto.getHeader(), Map.class));
        HttpEntity<String> entity = new HttpEntity<>(dto.getDynSentence(), headers);
        ResponseEntity<Object> exchange;
        try {
            exchange = restTemplate.exchange(dto.getApiUrl(), HttpMethod.valueOf(dto.getMethod()), entity, Object.class);
        } catch (Exception e) {
            log.error("error",e);
            throw BusinessExceptionBuilder.build(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, e.getMessage());
        }
        if (exchange.getStatusCode().isError()) {
            throw BusinessExceptionBuilder.build(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, exchange.getBody());
        }
        Object body = exchange.getBody();
        String jsonStr = JSONObject.toJSONString(body);
        List<JSONObject> result = new ArrayList<>();
        if (jsonStr.trim().startsWith(BusinessConstant.LEFT_BIG_BOAST) && jsonStr.trim().endsWith(BusinessConstant.RIGTH_BIG_BOAST)) {
            //JSONObject
            result.add(JSONObject.parseObject(jsonStr));
        } else if (jsonStr.trim().startsWith(BusinessConstant.LEFT_MIDDLE_BOAST) && jsonStr.trim().endsWith(BusinessConstant.RIGHT_MIDDLE_BOAST)) {
            //List
            result = JSONArray.parseArray(jsonStr, JSONObject.class);
        } else {
            result.add(new JSONObject());
        }
        return result;
    }

    /**
     * 关系型数据库 测试连接
     *
     * @param dto
     */
    public void testRelationalDb(DataSourceDto dto) {
        analysisRelationalDbConfig(dto);
        try {
            Connection unPooledConnection = jdbcService.getUnPooledConnection(dto);
            String catalog = unPooledConnection.getCatalog();
            log.info("数据库测试连接成功：{}", catalog);
            unPooledConnection.close();
        } catch (SQLException e) {
            log.error("error",e);
            if (e.getCause() instanceof ClassNotFoundException) {
                throw BusinessExceptionBuilder.build(ResponseCode.CLASS_NOT_FOUND, e.getCause().getMessage());
            } else {
                throw BusinessExceptionBuilder.build(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, e.getMessage());
            }

        }
    }

    /**
     * http 测试连接
     *
     * @param dto
     */
    public void testHttp(DataSourceDto dto) {
        analysisHttpConfig(dto);
        String apiUrl = dto.getApiUrl();
        String method = dto.getMethod();
        String body = dto.getBody();
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(JSONObject.parseObject(dto.getHeader(), Map.class));
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Object> exchange;
        try {
            exchange = restTemplate.exchange(apiUrl, HttpMethod.valueOf(method), entity, Object.class);
            if (exchange.getStatusCode().isError()) {
                throw BusinessExceptionBuilder.build(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, exchange.getBody());
            }
        } catch (RestClientException e) {
            throw BusinessExceptionBuilder.build(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, e.getMessage());
        }
    }


    /**
     * 关系型数据库 测试连接
     *
     * @param dto
     */
    public void testElasticsearchSqlConnection(DataSourceDto dto) {
        analysisHttpConfig(dto);
        String apiUrl = dto.getApiUrl();
        String method = dto.getMethod();
        String body = dto.getBody();
        HttpHeaders headers = new HttpHeaders();
        headers.setAll(JSONObject.parseObject(dto.getHeader(), Map.class));
        HttpEntity<String> entity = new HttpEntity<>(body, headers);
        ResponseEntity<Object> exchange;
        try {
            exchange = restTemplate.exchange(apiUrl, HttpMethod.valueOf(method), entity, Object.class);
            if (exchange.getStatusCode().isError()) {
                throw BusinessExceptionBuilder.build(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, exchange.getBody());
            }
        } catch (RestClientException e) {
            throw BusinessExceptionBuilder.build(ResponseCode.DATA_SOURCE_CONNECTION_FAILED, e.getMessage());
        }

    }


    public void analysisRelationalDbConfig(DataSourceDto dto) {
        JSONObject json = JSONObject.parseObject(dto.getSourceConfig());
        GaeaAssert.isFalse(json.containsKey("jdbcUrl"), ResponseCode.PARAM_IS_NULL,"jdbcUrl not empty");
        GaeaAssert.isFalse(json.containsKey("driverName"), ResponseCode.PARAM_IS_NULL,"driverName not empty");
        String jdbcUrl = json.getString("jdbcUrl");
        String username = json.getString("username");
        String password = json.getString("password");
        String driverName = json.getString("driverName");
        dto.setJdbcUrl(jdbcUrl);
        dto.setDriverName(driverName);
        dto.setUsername(username);
        dto.setPassword(password);
    }


    /**
     * es通过api获取数据
     *
     * @param dto
     * @return
     */
    public void analysisHttpConfig(DataSourceDto dto) {
        JSONObject json = JSONObject.parseObject(dto.getSourceConfig());
        GaeaAssert.isFalse(json.containsKey("apiUrl"), ResponseCode.PARAM_IS_NULL,"apiUrl not empty");
        GaeaAssert.isFalse(json.containsKey("method"), ResponseCode.PARAM_IS_NULL,"method not empty");
        GaeaAssert.isFalse(json.containsKey("header"), ResponseCode.PARAM_IS_NULL,"header not empty");
        GaeaAssert.isFalse(json.containsKey("body"), ResponseCode.PARAM_IS_NULL,"body not empty");
        String apiUrl = json.getString("apiUrl");
        String method = json.getString("method");
        String header = json.getString("header");
        String body = json.getString("body");
        //解决url中存在的动态参数
        apiUrl = dataSetParamService.transform(dto.getContextData(), apiUrl);
        //请求头中动态参数
        header = dataSetParamService.transform(dto.getContextData(), header);
        dto.setApiUrl(apiUrl);
        dto.setMethod(method);
        dto.setHeader(header);
        dto.setBody(body);
    }

    /**
     * 操作后续处理
     *
     * @param entity
     * @param operationEnum 操作类型
     * @throws BusinessException 阻止程序继续执行或回滚事务
     */
    @Override
    public void processAfterOperation(DataSource entity, BaseOperationEnum operationEnum) throws BusinessException {
        jdbcService.removeJdbcConnectionPool(entity.getId());
    }
}
