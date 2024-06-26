package com.pig4cloud.pigx.common.audit.handle;

import com.pig4cloud.pigx.admin.api.entity.SysAuditLog;
import com.pig4cloud.pigx.admin.api.feign.RemoteAuditLogService;
import com.pig4cloud.pigx.common.audit.annotation.Audit;
import com.pig4cloud.pigx.common.core.constant.SecurityConstants;
import com.pig4cloud.pigx.common.core.util.KeyStrResolver;
import com.pig4cloud.pigx.common.core.util.SpringContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.javers.core.Changes;
import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 默认的审计日志存储
 *
 * @author lengleng
 * @date 2023/2/27
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultAuditLogHandle implements IAuditLogHandle {

	private final RemoteAuditLogService remoteLogService;

	private final KeyStrResolver tenantKeyStrResolver;

	@Override
	public void handle(Audit audit, Changes changes) {
		// 如果变更项为空则不进行审计
		if (changes.isEmpty()) {
			return;
		}

		// 获取当前操作人
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		// 如果获取不到授权信息、或者没有身份信息的接口 直接跳过处理
		if (Objects.isNull(authentication) || !authentication.isAuthenticated()) {
			return;
		}

		List<SysAuditLog> auditLogList = new ArrayList<>();
		for (Change change : changes) {
			ValueChange valueChange = (ValueChange) change;

			SysAuditLog auditLog = new SysAuditLog();
			auditLog.setAuditName(audit.name());
			auditLog.setAuditField(valueChange.getPropertyName()); // 修改的字段名称

			if (Objects.nonNull(valueChange.getLeft())) {
				auditLog.setBeforeVal(valueChange.getLeft().toString()); // 更改前的值
			}
			if (Objects.nonNull(valueChange.getRight())) {
				auditLog.setAfterVal(valueChange.getRight().toString()); // getRight
			}

			auditLog.setCreateBy(authentication.getName()); // 操作人
			auditLog.setCreateTime(LocalDateTime.now()); // 操作时间
			auditLog.setTenantId(Long.parseLong(tenantKeyStrResolver.key())); // 设置操作所属租户

			auditLogList.add(auditLog);
		}

		// 异步保存日志，提升性能
		IAuditLogHandle auditLogHandle = SpringContextHolder.getBean(IAuditLogHandle.class);
		auditLogHandle.asyncSend(auditLogList);
	}

	@Async
	public void asyncSend(List<SysAuditLog> auditLogList) {
		remoteLogService.saveLog(auditLogList);
	}

}
