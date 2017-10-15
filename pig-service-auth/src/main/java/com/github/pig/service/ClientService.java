package com.github.pig.service;


import java.util.List;

/**
 * Created by ace on 2017/9/10.
 */
public interface ClientService {
    public String apply(String clientId, String secret) throws Exception;

    /**
     * 获取授权的客户端列表
     * @param serviceId
     * @param secret
     * @return
     */
    public List<String> getAllowedClient(String serviceId, String secret);

    public void registryClient();
}
