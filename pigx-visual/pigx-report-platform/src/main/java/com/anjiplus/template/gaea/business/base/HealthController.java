package com.anjiplus.template.gaea.business.base;

import com.anji.plus.gaea.bean.ResponseBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @desc 用户管理 controller
 * @author 木子李·De <lide1202@hotmail.com>
 * @date 2019-02-17 08:50:11.902
 **/
@RestController
public class HealthController {

	@GetMapping("health")
	public ResponseBean health() {
		return ResponseBean.builder().build();
	}

}
