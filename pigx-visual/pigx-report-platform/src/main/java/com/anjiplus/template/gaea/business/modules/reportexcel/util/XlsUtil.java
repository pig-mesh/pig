package com.anjiplus.template.gaea.business.modules.reportexcel.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.anjiplus.template.gaea.business.enums.ExcelCenterStyleEnum;
import com.anjiplus.template.gaea.business.modules.reportexcel.controller.dto.GridRecordDataModel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * 来自：https://github.com/mengshukeji/LuckysheetServer
 * 使用poi导出xls
 *
 * @author Administrator
 */
public class XlsUtil {

    private final static String MODEL = "{\"c\":0,\"r\":0,\"v\":{\"m\":\"模板\",\"v\":\"模板\",\"bl\":1,\"ct\":{\"t\":\"g\",\"fa\":\"General\"}}}";
    private final static String BORDER_MODEL = "{\"rangeType\":\"cell\",\"value\":{\"b\":{\"color\":\"rgb(0, 0, 0)\",\"style\":1},\"r\":{\"color\":\"rgb(0, 0, 0)\",\"style\":1},\"col_index\":5,\"t\":{\"color\":\"rgb(0, 0, 0)\",\"style\":1},\"row_index\":7,\"l\":{\"color\":\"rgb(0, 0, 0)\",\"style\":1}}}";
    /**
     * 默认行数
     */
    private static final int DEFAULT_ROW_INDEX = 84;
    /**
     * 默认列数
     */
    private static final int DEFAULT_COLUMN_INDEX = 64;

    /**
     * 输出文件流
     *
     * @param outputStream 流
     * @param isXlsx       是否是xlsx
     * @param dbObjectList 数据
     */
    public static void exportXlsFile(OutputStream outputStream, Boolean isXlsx, List<JSONObject> dbObjectList) throws IOException {
        Workbook wb = null;
        if (isXlsx) {
            wb = new XSSFWorkbook();
        } else {
            wb = new HSSFWorkbook();
        }
        if (dbObjectList != null && dbObjectList.size() > 0) {
            for (int x = 0; x < dbObjectList.size(); x++) {
                XlsSheetUtil.exportSheet(wb, x, dbObjectList.get(x));
            }
        }
        wb.write(outputStream);
    }

    /**
     * @param workbook 工作簿
     * @return Map
     * @description 读取excel
     * @author zhouhang
     * @date 2021/4/20
     */
    public static List<GridRecordDataModel> readExcel(Workbook workbook) {
        List<GridRecordDataModel> list = new ArrayList<>();
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        int sheetIndex = 0;
        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            //生成默认MODEL
            GridRecordDataModel model;
            if (Objects.equals(0, sheetIndex)) {
                model = strToModel("", (sheetIndex + 1) + "", 1, sheetIndex);
            } else {
                model = strToModel("", (sheetIndex + 1) + "", 0, sheetIndex);
            }
            sheetIndex++;
            //读取sheet页
            readSheet(sheet, model, workbook);
            //设置sheet页名称
            model.getJson_data().put("name", sheet.getSheetName());
            list.add(model);
        }
        return list;
    }

    public static GridRecordDataModel strToModel(String list_id, String index, int status, int order) {
        String strSheet = "{\"row\":84,\"name\":\"reSheetName\",\"chart\":[],\"color\":\"\",\"index\":\"reIndex\",\"order\":reOrder,\"column\":60,\"config\":{},\"status\":reStatus,\"celldata\":[],\"ch_width\":4748,\"rowsplit\":[],\"rh_height\":1790,\"scrollTop\":0,\"scrollLeft\":0,\"visibledatarow\":[],\"visibledatacolumn\":[],\"jfgird_select_save\":[],\"jfgrid_selection_range\":{}}";
        strSheet = strSheet.replace("reSheetName", "Sheet" + index).replace("reIndex", index).replace("reOrder", order + "").replace("reStatus", status + "");

        JSONObject bson = JSONObject.parseObject(strSheet);
        GridRecordDataModel model = new GridRecordDataModel();
        model.setBlock_id("fblock");
        model.setRow_col("5_5");
        model.setIndex(index);
        model.setIs_delete(0);
        model.setJson_data(bson);
        model.setStatus(status);
        model.setOrder(order);
        model.setList_id(list_id);
        return model;
    }

    /**
     * @param sheet    sheet页
     * @param model    数据存储
     * @param workbook excel
     * @description 读取单个sheet页
     * @author zhouhang
     * @date 2021/4/20
     */
    private static void readSheet(Sheet sheet, GridRecordDataModel model, Workbook workbook) {
        //excel数据集合
        List<JSONObject> dataList = new ArrayList<>();
        model.setDataList(dataList);
        //获取行迭代器
        Iterator<Row> rowIterator = sheet.rowIterator();
        //获取合并单元格信息
        Map<String, String> rangeMap = getRangeMap(sheet);
        //记录最大列
        int maxCellNumber = 0;
        int maxRowNumber = 0;
        //列宽
        JSONObject columnLenObj = new JSONObject();
        //行高
        JSONObject rowLenObj = new JSONObject();
        //读取文档
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            int rowLen = ((int) row.getHeight()) / 20;
            if (rowLen == 0) {
                rowLen = 30;
            }
            rowLenObj.put(row.getRowNum() + "", rowLen);
            Iterator<Cell> cellIterator = row.cellIterator();
            maxRowNumber = row.getRowNum();
            while (cellIterator.hasNext()) {
                //"{\"c\":0,\"r\":0,\"v\":{\"m\":\"模板\",\"v\":\"模板\",\"bl\":1,\"ct\":{\"t\":\"g\",\"fa\":\"General\"}}}";
                JSONObject dataModel = JSONObject.parseObject(MODEL);
                //初始化默认单元格内容
                Cell cell = cellIterator.next();
                int columnLen = sheet.getColumnWidth(cell.getColumnIndex()) / 25;
                if (columnLen == 0) {
                    columnLen = 73;
                }
                columnLenObj.put(cell.getColumnIndex() + "", columnLen);
                //修改最大列
                maxCellNumber = Math.max(cell.getColumnIndex(), maxCellNumber);
                //设置行列
                dataModel.put("c", cell.getColumnIndex());
                dataModel.put("r", row.getRowNum());
                //获取单元格内容
                switch (cell.getCellType()) {
                    case STRING:
                        dataModel.getJSONObject("v").put("m", cell.getStringCellValue());
                        dataModel.getJSONObject("v").put("v", cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        dataModel.getJSONObject("v").put("m", cell.getNumericCellValue());
                        dataModel.getJSONObject("v").put("v", cell.getNumericCellValue());
                        break;
                    case BLANK:
                        dataModel.getJSONObject("v").put("m", "");
                        dataModel.getJSONObject("v").put("v", "");
                        break;
                    case BOOLEAN:
                        dataModel.getJSONObject("v").put("m", cell.getBooleanCellValue());
                        dataModel.getJSONObject("v").put("v", cell.getBooleanCellValue());
                        break;
                    case ERROR:
                        dataModel.getJSONObject("v").put("m", cell.getErrorCellValue());
                        dataModel.getJSONObject("v").put("v", cell.getErrorCellValue());
                        break;
                    default:
                        dataModel.getJSONObject("v").put("m", "");
                        dataModel.getJSONObject("v").put("v", "");
                }
                //设置单元格合并标记
                dealWithCellMarge(rangeMap, row, cell, dataModel);
                //设置单元格样式、合并单元格信息
                dealWithExcelStyle(model, dataModel, cell, sheet, workbook);
                dataList.add(dataModel);
            }
        }
        //设置最大行、列
        model.getJson_data().put("column", Math.max(maxCellNumber, DEFAULT_COLUMN_INDEX));
        model.getJson_data().put("row", Math.max(maxRowNumber, DEFAULT_ROW_INDEX));
        //设置行高、列宽
        model.getJson_data().getJSONObject("config").put("columnlen", columnLenObj);
        model.getJson_data().getJSONObject("config").put("rowlen", rowLenObj);
    }

    /**
     * @param sheet sheet页信息
     * @return Map<String, String> 单元格合并信息
     * @description 获取合并单元格信息 所有合并单元的MAP
     * @author zhouhang
     * @date 2021/4/21
     */
    @NotNull
    private static Map<String, String> getRangeMap(Sheet sheet) {
        List<CellRangeAddress> rangeAddressList = sheet.getMergedRegions();
        Map<String, String> rangeMap = new HashMap<>(rangeAddressList.size() * 5);
        for (CellRangeAddress cellAddresses : rangeAddressList) {
            for (int i = cellAddresses.getFirstRow(); i <= cellAddresses.getLastRow(); i++) {
                for (int j = cellAddresses.getFirstColumn(); j <= cellAddresses.getLastColumn(); j++) {
                    if (i == cellAddresses.getFirstRow() && j == cellAddresses.getFirstColumn()) {
                        //单元格合并初始值特殊标记
                        rangeMap.put(i + "_" + j, cellAddresses.getFirstRow() + "_" + cellAddresses.getFirstColumn() + "_" + cellAddresses.getLastRow() + "_" + cellAddresses.getLastColumn());
                    } else {
                        rangeMap.put(i + "_" + j, cellAddresses.getFirstRow() + "_" + cellAddresses.getFirstColumn());
                    }
                }
            }
        }
        return rangeMap;
    }

    /**
     * @param rangeMap  合并信息
     * @param row       行信息
     * @param cell      单元格
     * @param dataModel 单元格数据存储信息
     * @description 设置单元格合并标记
     * @author zhouhang
     * @date 2021/4/21
     */
    private static void dealWithCellMarge(Map<String, String> rangeMap, Row row, Cell cell, JSONObject dataModel) {
        if (rangeMap.containsKey(row.getRowNum() + "_" + cell.getColumnIndex())) {
            String margeValue = rangeMap.get(row.getRowNum() + "_" + cell.getColumnIndex());
            JSONObject mcData = new JSONObject();
            String[] s = margeValue.split("_");
            mcData.put("r", Integer.parseInt(s[0]));
            mcData.put("c", Integer.parseInt(s[1]));
            if (s.length == 4) {
                mcData.put("rs", Integer.parseInt(s[2]) - Integer.parseInt(s[0]) + 1);
                mcData.put("cs", Integer.parseInt(s[3]) - Integer.parseInt(s[1]) + 1);
            }
            dataModel.getJSONObject("v").put("mc", mcData);
        }
    }

    /**
     * @param model     sheet页信息
     * @param dataModel 单元格信息
     * @param cell      单元格
     * @param sheet     sheet页数据
     * @param workbook  excel
     * @description 获取单元格样式，设置单元格样式
     * @author zhouhang
     * @date 2021/4/21
     */
    private static void dealWithExcelStyle(GridRecordDataModel model, JSONObject dataModel, Cell cell, Sheet sheet, Workbook workbook) {
        //设置单元格合并信息
        dealWithExcelMerge(model, sheet);
        //设置字体样式
        setFontStyle(dataModel, workbook, cell);
        //设置单元格样式
        dealWithBorderStyle(model, cell, workbook);
    }

    /**
     * @param model    在线表格存储单元
     * @param cell     cell
     * @param workbook workbook
     * @description 设置单元格样式
     * @author zhouhang
     * @date 2021/4/22
     */
    private static void dealWithBorderStyle(GridRecordDataModel model, Cell cell, Workbook workbook) {
        CellStyle cellStyle = cell.getCellStyle();
        //判断是否存在边框
        if (cellStyle.getBorderTop().getCode() > 0 || cellStyle.getBorderBottom().getCode() > 0 ||
                cellStyle.getBorderLeft().getCode() > 0 || cellStyle.getBorderRight().getCode() > 0) {
            JSONObject border = JSONObject.parseObject(BORDER_MODEL);
            border.getJSONObject("value").put("row_index", cell.getRowIndex());
            border.getJSONObject("value").put("col_index", cell.getColumnIndex());
            //xlsx
            if (cellStyle instanceof XSSFCellStyle) {
                XSSFCellStyle xssfCellStyle = (XSSFCellStyle) cellStyle;
                if (Objects.equals((short) 0, cellStyle.getBorderTop().getCode())) {
                    border.getJSONObject("value").remove("t");
                } else {
                    border.getJSONObject("value").getJSONObject("t").put("color", dealWithRbg(xssfCellStyle.getTopBorderXSSFColor().getRGB()));
                }
                if (Objects.equals((short) 0, cellStyle.getBorderRight().getCode())) {
                    border.getJSONObject("value").remove("r");
                } else {
                    border.getJSONObject("value").getJSONObject("r").put("color", dealWithRbg(xssfCellStyle.getRightBorderXSSFColor().getRGB()));
                }
                if (Objects.equals((short) 0, cellStyle.getBorderLeft().getCode())) {
                    border.getJSONObject("value").remove("l");
                } else {
                    border.getJSONObject("value").getJSONObject("l").put("color", dealWithRbg(xssfCellStyle.getLeftBorderXSSFColor().getRGB()));
                }
                if (Objects.equals((short) 0, cellStyle.getBorderBottom().getCode())) {
                    border.getJSONObject("value").remove("b");
                } else {
                    border.getJSONObject("value").getJSONObject("b").put("color", dealWithRbg(xssfCellStyle.getBottomBorderXSSFColor().getRGB()));
                }
            } else if (cellStyle instanceof HSSFCellStyle) {
                //xls
                HSSFWorkbook hssfWorkbook = (HSSFWorkbook) workbook;
                HSSFCellStyle hssfCellStyle = (HSSFCellStyle) cellStyle;
                if (Objects.equals((short) 0, cellStyle.getBorderTop().getCode())) {
                    border.getJSONObject("value").remove("t");
                } else {
                    HSSFColor color = hssfWorkbook.getCustomPalette().getColor(hssfCellStyle.getTopBorderColor());
                    border.getJSONObject("value").getJSONObject("t").put("color", dealWithRbgShort(color.getTriplet()));
                }
                if (Objects.equals((short) 0, cellStyle.getBorderRight().getCode())) {
                    border.getJSONObject("value").remove("r");
                } else {
                    HSSFColor color = hssfWorkbook.getCustomPalette().getColor(hssfCellStyle.getRightBorderColor());
                    border.getJSONObject("value").getJSONObject("r").put("color", dealWithRbgShort(color.getTriplet()));
                }
                if (Objects.equals((short) 0, cellStyle.getBorderLeft().getCode())) {
                    border.getJSONObject("value").remove("l");
                } else {
                    HSSFColor color = hssfWorkbook.getCustomPalette().getColor(hssfCellStyle.getLeftBorderColor());
                    border.getJSONObject("value").getJSONObject("l").put("color", dealWithRbgShort(color.getTriplet()));
                }
                if (Objects.equals((short) 0, cellStyle.getBorderBottom().getCode())) {
                    border.getJSONObject("value").remove("b");
                } else {
                    HSSFColor color = hssfWorkbook.getCustomPalette().getColor(hssfCellStyle.getBottomBorderColor());
                    border.getJSONObject("value").getJSONObject("b").put("color", dealWithRbgShort(color.getTriplet()));
                }
            }
            JSONArray borderInfo = model.getJson_data().getJSONObject("config").getJSONArray("borderInfo");
            if (Objects.isNull(borderInfo)) {
                borderInfo = new JSONArray();
                model.getJson_data().getJSONObject("config").put("borderInfo", borderInfo);
            }
            borderInfo.add(border);
        }
    }

    /**
     * @param rgb RBG short
     * @return rbg(0, 0, 0)
     * @description 转换RBG rbg(0,0,0)
     * @author zhouhang
     * @date 2021/4/26
     */
    private static String dealWithRbgShort(short[] rgb) {
        return getRbg(Objects.nonNull(rgb), rgb[0], rgb[1], rgb[2]);
    }

    @NotNull
    private static String getRbg(boolean b2, short r, short b, short g) {
        if (b2) {
            return "rgb(" + (r & 0xFF) + ", " + (b & 0xFF) + ", " + (g & 0xFF) + ")";
        } else {
            return "rgb(0, 0, 0)";
        }
    }

    /**
     * @param rgb RBG byte
     * @return rbg(0, 0, 0)
     * @description 转换RBG rbg(0,0,0)
     * @author zhouhang
     * @date 2021/4/26
     */
    private static String dealWithRbg(byte[] rgb) {
        if (Objects.isNull(rgb)) {
            return "rgb(0, 0, 0)";
        }
        short[] shorts = new short[]{rgb[0], rgb[1], rgb[2]};
        return getRbg(true, shorts[0], shorts[1], shorts[2]);
    }

    /**
     * @param dataModel 单元格内容
     * @param workbook  workbook
     * @param cell      cell
     * @description s设置字体样式
     * @author zhouhang
     * @date 2021/4/21
     */
    private static void setFontStyle(JSONObject dataModel, Workbook workbook, Cell cell) {
        CellStyle cellStyle = cell.getCellStyle();
        Font font = workbook.getFontAt(cellStyle.getFontIndexAsInt());
        JSONObject v = dataModel.getJSONObject("v");
        //ht 水平对齐   水平对齐方式（0=居中，1=左对齐，2=右对齐）   excel:左：1  中：2  右：3 未设置：0
        v.put("ht", ExcelCenterStyleEnum.getExcelCenterStyleByExcelCenterCode(cellStyle.getAlignment().getCode()).getOnlineExcelCode());
        //bl 字体加粗设置
        v.put("bl", font.getBold() ? 1 : 0);
        //lt 斜体
        v.put("it", font.getItalic() ? 1 : 0);
        //ff 字体
        v.put("ff", font.getFontName());
        //fc 字体颜色
        if (font instanceof HSSFFont) {
            HSSFFont hssfFont = (HSSFFont) font;
            HSSFColor hssfColor = hssfFont.getHSSFColor((HSSFWorkbook) workbook);
            if (Objects.nonNull(hssfColor)) {
                v.put("fc", ColorUtil.convertRGBToHex(hssfColor.getTriplet()[0], hssfColor.getTriplet()[1], hssfColor.getTriplet()[2]));
            }
        } else {
            XSSFFont xssfFont = (XSSFFont) font;
            XSSFColor xssfColor = xssfFont.getXSSFColor();
            if (Objects.nonNull(xssfColor)) {
                v.put("fc", "#" + xssfColor.getARGBHex().substring(2));
            }
        }
        //fs 字体大小
        v.put("fs", font.getFontHeightInPoints());
        //cl 删除线
        v.put("cl", font.getStrikeout() ? 1 : 0);
        //ul 下划线
        v.put("un", font.getUnderline());
        //背景色
        String fillColorHex = ColorUtil.getFillColorHex(cell);
        if (Objects.nonNull(fillColorHex)) {
            v.put("bg", fillColorHex);
        }
    }

    /**
     * @param model sheet页信息
     * @param sheet sheet页
     * @description 设置单元格合并信息
     * @author zhouhang
     * @date 2021/4/21
     */
    private static void dealWithExcelMerge(GridRecordDataModel model, Sheet sheet) {
        if (CollectionUtils.isNotEmpty(sheet.getMergedRegions())) {
            //{"color":"","list_id":"","column":60,"index":"1","jfgird_select_save":[],"rh_height":1790,"visibledatacolumn":[],"scrollTop":0,"block_id":"fblock","rowsplit":[],"visibledatarow":[],"jfgrid_selection_range":{},"name":"Sheet1","celldata":[],"ch_width":4748,"row":84,"scrollLeft":0,"id":364598,"chart":[],"config":{},"order":0,"status":1}
            JSONObject jsonObject = model.getJson_data();
            JSONObject config = jsonObject.getJSONObject("config");
            JSONObject merge = new JSONObject();
            for (CellRangeAddress mergedRegion : sheet.getMergedRegions()) {
                JSONObject mergeBase = new JSONObject();
                mergeBase.put("r", mergedRegion.getFirstRow());
                mergeBase.put("c", mergedRegion.getFirstColumn());
                mergeBase.put("rs", mergedRegion.getLastRow() - mergedRegion.getFirstRow() + 1);
                mergeBase.put("cs", mergedRegion.getLastColumn() - mergedRegion.getFirstColumn() + 1);
                merge.put(mergedRegion.getFirstRow() + "_" + mergedRegion.getFirstColumn(), mergeBase);
            }
            config.put("merge", merge);
        }
    }
}
