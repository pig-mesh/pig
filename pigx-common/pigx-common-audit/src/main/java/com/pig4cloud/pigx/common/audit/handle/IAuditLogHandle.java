package com.pig4cloud.pigx.common.audit.handle;

import com.pig4cloud.pigx.admin.api.entity.SysAuditLog;
import com.pig4cloud.pigx.common.audit.annotation.Audit;
import org.javers.core.Changes;

import java.util.List;

/**
 * @author lengleng
 * @date 2023/2/26
 *
 * 审计日志处理器
 */
public interface IAuditLogHandle {

	void handle(Audit audit, Changes changes);

	void asyncSend(List<SysAuditLog> auditLogList);

}
