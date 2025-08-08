package com.pig4cloud.pigx.flow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.pig4cloud.pigx.flow.entity.BpmOaLeaveEntity;

public interface BpmOaLeaveService extends IService<BpmOaLeaveEntity>, IProcessInstanceStatusEventService {

	/**
	 * 保存并启动请假流程
	 * @param bpmOaLeave 请假实体对象
	 * @return 保存后的请假实体对象
	 */
	BpmOaLeaveEntity saveAndStartProcess(BpmOaLeaveEntity bpmOaLeave);

}
