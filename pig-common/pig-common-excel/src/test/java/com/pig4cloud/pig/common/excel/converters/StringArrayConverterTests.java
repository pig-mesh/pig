package com.pig4cloud.pig.common.excel.converters;

import org.apache.fesod.sheet.FesodSheet;
import org.apache.fesod.sheet.annotation.ExcelProperty;
import org.apache.fesod.sheet.write.builder.ExcelWriterBuilder;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StringArrayConverterTests {

	@Test
	void exportsArrayWithoutExplicitCellType() throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		ExcelWriterBuilder writer = FesodSheet.write(output).head(ArrayRow.class);
		BuiltinConverters.registerTo(writer);
		ArrayRow row = new ArrayRow();
		row.setNames(new String[] { "研发", "运营" });
		writer.sheet().doWrite(List.of(row));
		try (var workbook = WorkbookFactory.create(new ByteArrayInputStream(output.toByteArray()))) {
			assertThat(workbook.getSheetAt(0).getRow(1).getCell(0).getStringCellValue()).isEqualTo("研发,运营");
		}
	}

	public static class ArrayRow {

		@ExcelProperty("名称")
		private String[] names;

		public String[] getNames() {
			return names;
		}

		public void setNames(String[] names) {
			this.names = names;
		}

	}

}
