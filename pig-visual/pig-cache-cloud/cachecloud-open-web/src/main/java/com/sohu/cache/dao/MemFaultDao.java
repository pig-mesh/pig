package com.sohu.cache.dao;

import com.sohu.cache.entity.InstanceFault;

import java.util.List;

/**
 * 故障管理
 * @author leifu
 * @Date 2015年3月2日
 * @Time 下午2:02:07
 */
public interface MemFaultDao {
    
	/**
	 * 返回所有故障
	 * @return
	 */
    List<InstanceFault> getMemFaultList();
}
