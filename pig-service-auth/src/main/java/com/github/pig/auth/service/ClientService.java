package com.github.pig.auth.service;


import java.util.List;

/**
 * @author lengleng
 */
public interface ClientService {
    /**
     * client信息获取token
     *
     * @param clientId clientId
     * @param secret   secret
     * @return token
     * @throws Exception Exception
     */
    String apply(String clientId, String secret) throws Exception;

    /**
     * 获取授权的客户端列表
     *
     * @param serviceId serviceId
     * @param secret    secret
     * @return 客户端列表
     */
    List<String> getAllowedClient(String serviceId, String secret);

    /**
     * 注册客户端
     */
    void registryClient();
}
