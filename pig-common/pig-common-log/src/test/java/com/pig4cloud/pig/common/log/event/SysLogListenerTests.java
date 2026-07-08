package com.pig4cloud.pig.common.log.event;

import com.pig4cloud.pig.admin.api.dto.SysLogDTO;
import com.pig4cloud.pig.admin.api.feign.RemoteLogService;
import com.pig4cloud.pig.common.core.util.R;
import com.pig4cloud.pig.common.log.config.PigLogProperties;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * {@link SysLogListener} 请求参数过滤测试
 *
 * @author lengleng
 */
class SysLogListenerTests {

	/**
	 * MultipartFile 参数必须在序列化前被剔除，且日志仍正常保存
	 */
	@Test
	void filtersMultipartFileBeforeSerializingRequestParams() {
		RemoteLogService remoteLogService = mock(RemoteLogService.class);
		when(remoteLogService.saveLog(any(SysLogDTO.class))).thenReturn(R.ok(Boolean.TRUE));

		PigLogProperties logProperties = new PigLogProperties();
		logProperties.setExcludeFields(List.of());
		SysLogListener listener = new SysLogListener(remoteLogService, logProperties);
		listener.afterPropertiesSet();

		SysLogDTO sysLog = new SysLogDTO();
		sysLog.setBody(new Object[] { new ThrowingMultipartFile() });

		listener.saveSysLog(new SysLogEvent(sysLog));

		ArgumentCaptor<SysLogDTO> captor = ArgumentCaptor.forClass(SysLogDTO.class);
		verify(remoteLogService).saveLog(captor.capture());
		assertThat(captor.getValue().getParams()).isEqualTo("[]");
		assertThat(captor.getValue().getBody()).isNull();
	}

	private static class ThrowingMultipartFile implements MultipartFile {

		@Override
		public String getName() {
			throw new AssertionError("MultipartFile should be filtered before serialization");
		}

		@Override
		public String getOriginalFilename() {
			throw new AssertionError("MultipartFile should be filtered before serialization");
		}

		@Override
		public String getContentType() {
			throw new AssertionError("MultipartFile should be filtered before serialization");
		}

		@Override
		public boolean isEmpty() {
			throw new AssertionError("MultipartFile should be filtered before serialization");
		}

		@Override
		public long getSize() {
			throw new AssertionError("MultipartFile should be filtered before serialization");
		}

		@Override
		public byte[] getBytes() throws IOException {
			throw new AssertionError("MultipartFile should be filtered before serialization");
		}

		@Override
		public InputStream getInputStream() throws IOException {
			throw new AssertionError("MultipartFile should be filtered before serialization");
		}

		@Override
		public void transferTo(File dest) throws IOException, IllegalStateException {
			throw new AssertionError("MultipartFile should be filtered before serialization");
		}

	}

}
