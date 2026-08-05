package com.pig4cloud.pig.codegen.controller;

import com.pig4cloud.pig.codegen.service.GenTableColumnService;
import com.pig4cloud.pig.codegen.service.GenTableService;
import com.pig4cloud.pig.common.core.util.R;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class GenTableControllerTests {

	private final GenTableColumnService tableColumnService = mock(GenTableColumnService.class);

	private final GenTableService tableService = mock(GenTableService.class);

	private final GenTableController controller = new GenTableController(tableColumnService, tableService);

	@Test
	void rejectsIssue1273PayloadBeforeCallingService() throws Exception {
		String tableName = "fcrawler_demo_item WHERE 1=EXTRACTVALUE(1,CONCAT(0x7e,user(),0x7e))#";

		R result = controller.getColumn("master", tableName);

		assertThat(result.isOk()).isFalse();
		assertThat(result.getMsg()).isEqualTo("非法内容");
		verifyNoInteractions(tableService);
	}

	@Test
	void acceptsRegularTableName() throws Exception {
		when(tableService.queryTableColumn("master", "sys_user")).thenReturn(List.of());

		R result = controller.getColumn("master", "sys_user");

		assertThat(result.isOk()).isTrue();
	}

}
