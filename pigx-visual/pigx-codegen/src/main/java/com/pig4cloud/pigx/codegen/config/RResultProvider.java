package com.pig4cloud.pigx.codegen.config;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.pig4cloud.pigx.common.core.util.R;
import org.ssssssss.magicapi.core.context.RequestEntity;
import org.ssssssss.magicapi.core.interceptor.ResultProvider;
import org.ssssssss.magicapi.modules.db.model.Page;

import java.util.List;
import java.util.Map;

/**
 * 返回R
 *
 * @author lengleng
 * @date 2022/7/9
 */
public class RResultProvider implements ResultProvider {

	@Override
	public Object buildResult(RequestEntity requestEntity, int code, String message, Object data) {
		return R.ok().setData(data).setMsg(message);
	}

	/**
	 * 定义分页返回结果，该项会被封装在Json结果内， 此方法可以不覆盖，默认返回PageResult
	 */
	@Override
	public Object buildPageResult(RequestEntity requestEntity, Page page, long total, List<Map<String, Object>> data) {
		IPage result = new com.baomidou.mybatisplus.extension.plugins.pagination.Page();
		result.setTotal(total);
		result.setRecords(data);
		return R.ok(result);
	}

	@Override
	public Object buildException(RequestEntity requestEntity, Throwable throwable) {
		return R.failed(throwable.getLocalizedMessage());
	}

}
