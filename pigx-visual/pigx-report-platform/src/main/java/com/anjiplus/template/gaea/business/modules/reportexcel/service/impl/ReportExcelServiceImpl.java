package com.anjiplus.template.gaea.business.modules.reportexcel.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.anji.plus.gaea.constant.BaseOperationEnum;
import com.anji.plus.gaea.curd.mapper.GaeaBaseMapper;
import com.anji.plus.gaea.exception.BusinessException;
import com.anji.plus.gaea.utils.GaeaAssert;
import com.anji.plus.gaea.utils.GaeaBeanUtils;
import com.anjiplus.template.gaea.business.code.ResponseCode;
import com.anjiplus.template.gaea.business.enums.ExportTypeEnum;
import com.anjiplus.template.gaea.business.modules.dataset.controller.dto.DataSetDto;
import com.anjiplus.template.gaea.business.modules.dataset.controller.dto.OriginalDataDto;
import com.anjiplus.template.gaea.business.modules.dataset.service.DataSetService;
import com.anjiplus.template.gaea.business.modules.file.dao.GaeaFileMapper;
import com.anjiplus.template.gaea.business.modules.file.entity.GaeaFile;
import com.anjiplus.template.gaea.business.modules.report.dao.ReportMapper;
import com.anjiplus.template.gaea.business.modules.report.dao.entity.Report;
import com.anjiplus.template.gaea.business.modules.reportexcel.controller.dto.ReportExcelDto;
import com.anjiplus.template.gaea.business.modules.reportexcel.dao.ReportExcelMapper;
import com.anjiplus.template.gaea.business.modules.reportexcel.dao.entity.ReportExcel;
import com.anjiplus.template.gaea.business.modules.reportexcel.service.ReportExcelService;
import com.anjiplus.template.gaea.business.modules.reportexcel.util.XlsSheetUtil;
import com.anjiplus.template.gaea.business.modules.reportexcel.util.XlsUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.*;

/**
 * TODO
 *
 * @author chenkening
 * @date 2021/4/13 15:14
 */
@Service
public class ReportExcelServiceImpl implements ReportExcelService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ReportExcelMapper reportExcelMapper;

	@Autowired
	private DataSetService dataSetService;

	@Autowired
	private ReportMapper reportMapper;

	@Value("${customer.file.dist-path:''}")
	private String dictPath;

	@Value("${customer.file.downloadPath:''}")
	private String fileDownloadPath;

	@Autowired
	private GaeaFileMapper gaeaFileMapper;

	@Override
	public GaeaBaseMapper<ReportExcel> getMapper() {
		return reportExcelMapper;
	}

	@Override
	public ReportExcelDto detailByReportCode(String reportCode) {
		QueryWrapper<ReportExcel> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("report_code", reportCode);
		ReportExcel reportExcel = reportExcelMapper.selectOne(queryWrapper);
		if (reportExcel != null) {
			ReportExcelDto dto = new ReportExcelDto();
			BeanUtils.copyProperties(reportExcel, dto);
			return dto;
		}
		return null;
	}

	/**
	 * 操作前处理
	 * @param entity 前端传递的对象
	 * @param operationEnum 操作类型
	 * @throws BusinessException 阻止程序继续执行或回滚事务
	 */
	@Override
	public void processBeforeOperation(ReportExcel entity, BaseOperationEnum operationEnum) throws BusinessException {
		if (operationEnum.equals(BaseOperationEnum.INSERT)) {
			String reportCode = entity.getReportCode();
			ReportExcel report = this.selectOne("report_code", reportCode);
			if (null != report) {
				this.deleteById(report.getId());
			}
		}
	}

	/**
	 * 报表预览
	 */
	@Override
	public ReportExcelDto preview(ReportExcelDto reportExcelDto) {
		// 根据id查询 报表详情
		ReportExcel reportExcel = selectOne("report_code", reportExcelDto.getReportCode());
		QueryWrapper<Report> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("report_code", reportExcelDto.getReportCode());
		Report report = reportMapper.selectOne(queryWrapper);
		GaeaAssert.notNull(reportExcel, ResponseCode.RULE_CONTENT_NOT_EXIST, "reportExcel");
		String setParam = reportExcelDto.getSetParam();

		GaeaBeanUtils.copyAndFormatter(reportExcel, reportExcelDto);
		if (StringUtils.isNotBlank(setParam)) {
			reportExcelDto.setSetParam(setParam);
		}
		reportExcelDto.setReportName(report.getReportName());
		// 数据集解析
		String jsonStr = analysisReportData(reportExcelDto);
		reportExcelDto.setJsonStr(jsonStr);
		// reportExcelDto.setTotal(jsonObject.getJSONObject("rows").size());
		return reportExcelDto;
	}

	@Override
	public Boolean exportExcel(ReportExcelDto reportExcelDto) {
		String reportCode = reportExcelDto.getReportCode();
		String exportType = reportExcelDto.getExportType();
		logger.error("导出...");
		if (exportType.equals(ExportTypeEnum.GAEA_TEMPLATE_EXCEL.getCodeValue())) {
			ReportExcelDto report = detailByReportCode(reportCode);
			reportExcelDto.setJsonStr(report.getJsonStr());
			String jsonStr = analysisReportData(reportExcelDto);
			List<JSONObject> lists = (List<JSONObject>) JSON.parse(jsonStr);
			OutputStream out;
			try {
				String fileId = UUID.randomUUID().toString();
				String filePath = dictPath + File.separator + fileId + ".xlsx";
				String urlPath = fileDownloadPath + java.io.File.separator + fileId;

				GaeaFile gaeaFile = new GaeaFile();
				gaeaFile.setFilePath(filePath);
				gaeaFile.setFileId(fileId);
				gaeaFile.setUrlPath(urlPath);
				gaeaFile.setFileType("xlsx");
				gaeaFile.setFileInstruction(reportCode + ".xlsx");

				out = new FileOutputStream(filePath);
				XlsUtil.exportXlsFile(out, true, lists);

				gaeaFileMapper.insert(gaeaFile);
				logger.info("导出成功：{}", gaeaFile);
			}
			catch (IOException e) {
				logger.error("导出失败", e);
			}
		}
		return true;
	}

	/**
	 * 解析报表数据，动态插入列表数据和对象数据
	 */
	private String analysisReportData(ReportExcelDto reportExcelDto) {

		String jsonStr = reportExcelDto.getJsonStr();
		String setParam = reportExcelDto.getSetParam();
		List<JSONObject> dbObjectList = (List<JSONObject>) JSON.parse(jsonStr);

		if (dbObjectList != null && dbObjectList.size() > 0) {
			for (int x = 0; x < dbObjectList.size(); x++) {
				analysisSheetCellData(dbObjectList.get(x), setParam);
			}
		}
		// fastjson $ref 循环引用
		return JSONObject.toJSONString(dbObjectList, SerializerFeature.DisableCircularReferenceDetect);
	}

	/**
	 * 解析单sheet data
	 * @param dbObject
	 */
	private void analysisSheet(JSONObject dbObject, String setParma) {
		// data是一个二维数组
		if (dbObject.containsKey("data") && null != dbObject.get("data")) {
			List<JSONArray> data = (List<JSONArray>) dbObject.get("data");

			// 行
			for (int r = 0; r < data.size(); r++) {
				JSONArray jsonArray = data.get(r);
				// 列
				for (int c = 0; c < jsonArray.size(); c++) {
					// 单元格
					JSONObject cell = jsonArray.getJSONObject(c);
					if (null != cell && cell.containsKey("v") && StringUtils.isNotBlank(cell.getString("v"))) {
						String v = cell.getString("v");
						DataSetDto dataSet = getDataSet(v, setParma);
						if (null != dataSet) {
							OriginalDataDto originalDataDto = dataSetService.getData(dataSet);
							if (null != originalDataDto.getData()) {
								if (originalDataDto.getData().size() == 1) {
									// 对象
									JSONObject jsonObject = originalDataDto.getData().get(0);
									String fieldLabel = jsonObject.getString(dataSet.getFieldLabel());

									String replace = v.replace("#{".concat(dataSet.getSetCode()).concat(".")
											.concat(dataSet.getFieldLabel()).concat("}"), fieldLabel);
									dbObject.getJSONArray("data").getJSONArray(r).getJSONObject(c).put("v", replace);
									dbObject.getJSONArray("data").getJSONArray(r).getJSONObject(c).put("m", replace);

								}
								else {
									// 集合
									JSONObject jsonObject = originalDataDto.getData().get(0);
									String fieldLabel = jsonObject.getString(dataSet.getFieldLabel());

									String replace = v.replace("#{".concat(dataSet.getSetCode()).concat(".")
											.concat(dataSet.getFieldLabel()).concat("}"), fieldLabel);
									dbObject.getJSONArray("data").getJSONArray(r).getJSONObject(c).put("v", replace);
									dbObject.getJSONArray("data").getJSONArray(r).getJSONObject(c).put("m", replace);
								}
							}

						}
					}

				}
			}

			System.out.println("aaaa");

		}

	}

	/**
	 * 解析单sheet celldata
	 * @param dbObject
	 */
	private void analysisSheetCellData(JSONObject dbObject, String setParam) {
		// 清空data值
		dbObject.remove("data");
		// celldata是一个一维数组
		if (dbObject.containsKey("celldata") && null != dbObject.get("celldata")) {
			List<JSONObject> celldata = new ArrayList<>();
			celldata.addAll((List<JSONObject>) dbObject.get("celldata"));
			// 遍历已存在的单元格，查看是否存在动态参数
			for (int i = 0; i < celldata.size(); i++) {
				// 单元格对象
				JSONObject cellObj = celldata.get(i);
				// fastjson深拷贝问题
				String cellStr = cellObj.toJSONString();

				// 行号
				Integer r = cellObj.getInteger("r");
				// 列号
				Integer c = cellObj.getInteger("c");
				JSONObject cell = cellObj.getJSONObject("v");
				if (null != cell && cell.containsKey("v") && StringUtils.isNotBlank(cell.getString("v"))) {
					String v = cell.getString("v");
					DataSetDto dataSet = getDataSet(v, setParam);
					if (null != dataSet) {
						OriginalDataDto originalDataDto = dataSetService.getData(dataSet);
						if (null != originalDataDto.getData()) {
							List<JSONObject> data = originalDataDto.getData();

							for (int j = 0; j < data.size(); j++) {
								if (j == 0) {
									// 处理当前行
									// 第一行，作为渲染参照数据
									JSONObject jsonObject = data.get(j);
									String fieldLabel = jsonObject.getString(dataSet.getFieldLabel());

									String replace = v.replace("#{".concat(dataSet.getSetCode()).concat(".")
											.concat(dataSet.getFieldLabel()).concat("}"), fieldLabel);
									dbObject.getJSONArray("celldata").getJSONObject(i).getJSONObject("v").put("v",
											replace);
									dbObject.getJSONArray("celldata").getJSONObject(i).getJSONObject("v").put("m",
											replace);
								}
								else {
									// 新增的行数据
									JSONObject addCell = data.get(j);
									// 字段
									String fieldLabel = addCell.getString(dataSet.getFieldLabel());
									String replace = v.replace("#{".concat(dataSet.getSetCode()).concat(".")
											.concat(dataSet.getFieldLabel()).concat("}"), fieldLabel);

									// 转字符串，解决深拷贝问题
									JSONObject addCellData = JSONObject.parseObject(cellStr);

									addCellData.put("r", r + j);
									addCellData.put("c", c);
									addCellData.getJSONObject("v").put("v", replace);
									addCellData.getJSONObject("v").put("m", replace);
									dbObject.getJSONArray("celldata").add(addCellData);

								}

							}

						}

					}
				}
			}

		}

	}

	/**
	 * 解析 #{xxxx.xxxxx} 数据
	 * @param v
	 * @return
	 */
	private DataSetDto getDataSet(String v, String setParam) {

		DataSetDto dto = new DataSetDto();
		if (v.contains("#{") && v.contains("}")) {
			int start = v.indexOf("#{") + 2;
			int end = v.indexOf("}");
			if (start < end) {
				String substring = v.substring(start, end);
				if (substring.contains(".")) {
					String[] split = substring.split("\\.");
					dto.setSetCode(split[0]);
					dto.setFieldLabel(split[1]);
					getContextData(setParam, dto);
					return dto;
				}
			}
		}
		return null;
	}

	/**
	 * 动态参数替换
	 * @param setParam
	 * @param dto
	 */
	private void getContextData(String setParam, DataSetDto dto) {
		if (StringUtils.isNotBlank(setParam)) {
			JSONObject setParamJson = JSONObject.parseObject(setParam);
			Map<String, Object> map = new HashMap<>();
			// 查询条件
			if (setParamJson.containsKey(dto.getSetCode())) {
				JSONObject paramCondition = setParamJson.getJSONObject(dto.getSetCode());
				paramCondition.forEach(map::put);
			}
			dto.setContextData(map);
		}
	}

}
