package com.anjiplus.template.gaea.business.modules.reportexcel.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.util.*;

/**
 * 来自：https://github.com/mengshukeji/LuckysheetServer
 * sheet操作
 *
 * @author Administrator
 */
@Slf4j
public class XlsSheetUtil {
    /**
     * 导出sheet
     *
     * @param wb
     * @param sheetNum
     * @param dbObject
     */
    public static void exportSheet(Workbook wb, int sheetNum, JSONObject dbObject) {
        Sheet sheet = wb.createSheet();

        //设置sheet位置，名称
        if (dbObject.containsKey("name") && dbObject.get("name") != null) {
            wb.setSheetName(sheetNum, dbObject.get("name").toString());
        } else {
            wb.setSheetName(sheetNum, "sheet" + sheetNum);
        }
        //是否隐藏
        if (dbObject.containsKey("hide") && dbObject.get("hide").toString().equals("1")) {
            wb.setSheetHidden(sheetNum, true);
        }
        //是否当前选中页
        if (dbObject.containsKey("status") && dbObject.get("status").toString().equals("1")) {
            sheet.setSelected(true);
        }


        //循环数据
        if (dbObject.containsKey("celldata") && dbObject.get("celldata") != null) {
            //取到所有单元格集合
            List<JSONObject> cells_json = (List<JSONObject>) dbObject.get("celldata");
            Map<Integer, List<JSONObject>> cellMap = cellGroup(cells_json);
            //循环每一行
            for (Integer r : cellMap.keySet()) {
                Row row = sheet.createRow(r);
                //循环每一列
                for (JSONObject col : cellMap.get(r)) {
                    createCell(wb, sheet, row, col);
                }
            }
        }

        if (dbObject.containsKey("config") && dbObject.getJSONObject("config").containsKey("borderInfo")) {
            JSONArray borderInfo = dbObject.getJSONObject("config").getJSONArray("borderInfo");
            setCellBoard(wb, borderInfo, sheet);
        }


        setColumAndRow(dbObject, sheet);

    }

    /**
     * 每一个单元格
     *
     * @param row
     * @param dbObject
     */
    private static void createCell(Workbook wb, Sheet sheet, Row row, JSONObject dbObject) {
        if (dbObject.containsKey("c")) {
            Integer c = getStrToInt(dbObject.get("c"));
            if (c != null) {
                Cell cell = row.createCell(c);
                //取单元格中的v_json
                if (dbObject.containsKey("v")) {
                    //获取v对象
                    Object obj = dbObject.get("v");
                    if (obj == null) {
                        //没有内容
                        return;
                    }
                    //如果v对象直接是字符串
                    if (obj instanceof String) {
                        if (((String) obj).length() > 0) {
                            cell.setCellValue(obj.toString());
                        }
                        return;
                    }

                    //转换v为对象(v是一个对象)
                    JSONObject v_json = (JSONObject) obj;
                    //样式
                    CellStyle style = wb.createCellStyle();
                    cell.setCellStyle(style);


                    //合并单元格
                    //参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
                    //CellRangeAddress region1 = new CellRangeAddress(rowNumber, rowNumber, (short) 0, (short) 11);

                    //mc 合并单元格
                    if (v_json.containsKey("mc")) {
                        //是合并的单元格
                        JSONObject mc = v_json.getJSONObject("mc");
                        if (mc.containsKey("rs") && mc.containsKey("cs")) {
                            //合并的第一个单元格
                            if (mc.containsKey("r") && mc.containsKey("c")) {
                                Integer _rs = getIntByDBObject(mc, "rs") - 1;
                                Integer _cs = getIntByDBObject(mc, "cs") - 1;
                                Integer _r = getIntByDBObject(mc, "r");
                                Integer _c = getIntByDBObject(mc, "c");

                                CellRangeAddress region = new CellRangeAddress(_r.shortValue(), (_r.shortValue() + _rs.shortValue()), _c.shortValue(), (_c.shortValue() + _cs.shortValue()));
                                sheet.addMergedRegion(region);
                            }
                        } else {
                            //不是合并的第一个单元格
                            return;
                        }
                    }


                    //取v值 (在数据类型中处理)
                    //ct 单元格值格式 (fa,t)
                    setFormatByCt(wb, cell, style, v_json);

                    //font设置
                    setCellStyleFont(wb, style, v_json);

                    //bg 背景颜色
                    if (v_json.containsKey("bg")) {
                        String _v = getByDBObject(v_json, "bg");
                        Short _color = ColorUtil.getColorByStr(_v);
                        if (_color != null) {
                            style.setFillForegroundColor(_color);
                            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                        }
                    }

                    //vt 垂直对齐    垂直对齐方式（0=居中，1=上，2=下）
                    if (v_json.containsKey("vt")) {
                        Integer _v = getIntByDBObject(v_json, "vt");
                        if (_v != null && _v >= 0 && _v <= 2) {
                            style.setVerticalAlignment(ConstantUtil.getVerticalType(_v));
                        }
                    } else {
                        //默认设置居中
                        style.setVerticalAlignment(ConstantUtil.getVerticalType(0));
                    }

                    //ht 水平对齐   水平对齐方式（0=居中，1=左对齐，2=右对齐）
                    if (v_json.containsKey("ht")) {
                        Integer _v = getIntByDBObject(v_json, "ht");
                        if (_v != null && _v >= 0 && _v <= 2) {
                            style.setAlignment(ConstantUtil.getHorizontaltype(_v));
                        }
                    } else {
                        //默认设置左对齐
                        style.setAlignment(ConstantUtil.getHorizontaltype(1));
                    }

                    //tr 文字旋转 文字旋转角度（0=0,1=45，2=-45，3=竖排文字，4=90，5=-90）
                    if (v_json.containsKey("tr")) {
                        Integer _v = getIntByDBObject(v_json, "tr");
                        if (_v != null) {
                            style.setRotation(ConstantUtil.getRotation(_v));
                        }
                    }

                    //tb  文本换行    0 截断、1溢出、2 自动换行
                    //   2：setTextWrapped     0和1：IsTextWrapped = true
                    if (v_json.containsKey("tb")) {
                        Integer _v = getIntByDBObject(v_json, "tb");
                        if (_v != null) {
                            if (_v >= 0 && _v <= 1) {
                                style.setWrapText(false);
                            } else {
                                style.setWrapText(true);
                            }
                        }
                    }

                    //f  公式
                    if (v_json.containsKey("f")) {
                        String _v = getByDBObject(v_json, "f");
                        if (_v.length() > 0) {
                            try {
                                if (_v.startsWith("=")) {
                                    cell.setCellFormula(_v.substring(1));
                                } else {
                                    cell.setCellFormula(_v);
                                }
                            } catch (Exception ex) {
                                log.error("公式 {};Error:{}", _v, ex.toString());
                            }
                        }
                    }


                }

            }
        }
    }

    /**
     * 设置边框
     *
     * @param borderInfo
     * @param sheet
     */
    private static void setCellBoard(Workbook wb, JSONArray borderInfo, Sheet sheet) {


        //一定要通过 cell.getCellStyle()  不然的话之前设置的样式会丢失
        //设置边框
        for (int i = 0; i < borderInfo.size(); i++) {
            JSONObject borderInfoObject = (JSONObject) borderInfo.get(i);
            if (borderInfoObject.get("rangeType").equals("cell")) {//单个单元格
                JSONObject borderValueObject = borderInfoObject.getJSONObject("value");

                JSONObject l = borderValueObject.getJSONObject("l");
                JSONObject r = borderValueObject.getJSONObject("r");
                JSONObject t = borderValueObject.getJSONObject("t");
                JSONObject b = borderValueObject.getJSONObject("b");


                int row_ = borderValueObject.getInteger("row_index");
                int col_ = borderValueObject.getInteger("col_index");

                Row row = sheet.getRow(row_);
                if (null == row) {
                    row = sheet.createRow(row_);
                }
                Cell cell = row.getCell(col_);
                CellStyle style;
                if (null == cell) {
                    style = wb.createCellStyle();
                    cell = row.createCell(col_);
                    cell.setCellStyle(style);
                } else {
                    style = cell.getCellStyle();
                }


                if (l != null) {
                    style.setBorderLeft(BorderStyle.valueOf(l.getShort("style"))); //左边框
                    Short color = ColorUtil.getColorByStr(l.getString("color"));
                    if (null != color) {
                        style.setLeftBorderColor(color);//左边框颜色
                    }

                }
                if (r != null) {
                    style.setBorderRight(BorderStyle.valueOf(r.getShort("style"))); //右边框
                    Short color = ColorUtil.getColorByStr(r.getString("color"));
                    if (null != color) {
                        style.setRightBorderColor(color);//右边框颜色
                    }

                }
                if (t != null) {
                    style.setBorderTop(BorderStyle.valueOf(t.getShort("style"))); //顶部边框
                    Short _vcolor = ColorUtil.getColorByStr(t.getString("color"));
                    if (null != _vcolor) {
                        style.setTopBorderColor(_vcolor);//顶部边框颜色
                    }

                }
                if (b != null) {
                    style.setBorderBottom(BorderStyle.valueOf(b.getShort("style"))); //底部边框
                    Short _vcolor = ColorUtil.getColorByStr(b.getString("color"));
                    if (_vcolor != null) {
                        //底部边框颜色
                        style.setBottomBorderColor(_vcolor);
                    }
                }

            } else if (borderInfoObject.get("rangeType").equals("range")) {
                //选区
                Short style_ = borderInfoObject.getShort("style");
                String borderType = borderInfoObject.getString("borderType");
                Short color = ColorUtil.getColorByStr(borderInfoObject.getString("color"));
                JSONObject rangObject = (JSONObject) ((JSONArray) (borderInfoObject.get("range"))).get(0);

                JSONArray rowList = rangObject.getJSONArray("row");
                JSONArray columnList = rangObject.getJSONArray("column");


                for (int row_ = rowList.getInteger(0); row_ < rowList.getInteger(rowList.size() - 1) + 1; row_++) {
                    for (int col_ = columnList.getInteger(0); col_ < columnList.getInteger(columnList.size() - 1) + 1; col_++) {
                        Row row = sheet.getRow(row_);
                        if (null == row) {
                            row = sheet.createRow(row_);
                        }
                        Cell cell = row.getCell(col_);
                        CellStyle style;
                        if (null == cell) {
                            style = wb.createCellStyle();
                            cell = row.createCell(col_);
                            cell.setCellStyle(style);
                        } else {
                            style = cell.getCellStyle();
                        }

                        //"border-left" | "border-right" | "border-top" | "border-bottom" | "border-all" | "border-horizontal" | "border-vertical"
                        // "border-outside" | "border-inside" |  | "border-none"
                        if ("border-left".equals(borderType) || "border-all".equals(borderType)) {
                            style.setBorderLeft(BorderStyle.valueOf(style_)); //左边框
                            style.setLeftBorderColor(color);//左边框颜色
                        }
                        if ("border-right".equals(borderType) || "border-all".equals(borderType)) {
                            style.setBorderRight(BorderStyle.valueOf(style_)); //右边框
                            style.setRightBorderColor(color);//右边框颜色
                        }

                        if ("border-top".equals(borderType) || "border-all".equals(borderType)) {
                            style.setBorderTop(BorderStyle.valueOf(style_)); //顶部边框
                            style.setTopBorderColor(color);//顶部边框颜色
                        }

                        if ("border-bottom".equals(borderType) || "border-all".equals(borderType)) {
                            style.setBorderBottom(BorderStyle.valueOf(style_)); //底部边框
                            style.setBottomBorderColor(color);//底部边框颜色 }
                        }

                        if ("border-outside".equals(borderType)) {
                            //外圈边框
                            if (row_ == rowList.getInteger(0)) {
                                style.setBorderTop(BorderStyle.valueOf(style_)); //顶部边框
                                style.setTopBorderColor(color);//顶部边框颜色
                            }
                            if (col_ == columnList.getInteger(0)) {
                                style.setBorderLeft(BorderStyle.valueOf(style_)); //左边框
                                style.setLeftBorderColor(color);//左边框颜色
                            }
                            if (row_ == rowList.getInteger(rowList.size() - 1)) {
                                style.setBorderBottom(BorderStyle.valueOf(style_)); //底部边框
                                style.setBottomBorderColor(color);//底部边框颜色 }
                            }
                            if (col_ == columnList.getInteger(columnList.size() - 1)) {
                                style.setBorderRight(BorderStyle.valueOf(style_)); //右边框
                                style.setRightBorderColor(color);//右边框颜色
                            }

                        }

                        if ("border-horizontal".equals(borderType) || "border-inside".equals(borderType)) {
                            //内部横线
                            if (row_ >= rowList.getInteger(0)
                                    && row_ < rowList.getInteger(rowList.size() - 1)
                                    && col_ >= columnList.getInteger(0)
                                    && col_ <= columnList.getInteger(columnList.size() - 1)) {
                                style.setBorderBottom(BorderStyle.valueOf(style_)); //底部边框
                                style.setBottomBorderColor(color);//底部边框颜色 }
                            }
                        }

                        if ("border-vertical".equals(borderType) || "border-inside".equals(borderType)) {
                            //内部竖线
                            if (row_ >= rowList.getInteger(0)
                                    && row_ <= rowList.getInteger(rowList.size() - 1)
                                    && col_ >= columnList.getInteger(0)
                                    && col_ < columnList.getInteger(columnList.size() - 1)) {
                                style.setBorderRight(BorderStyle.valueOf(style_)); //右边框
                                style.setRightBorderColor(color);//右边框颜色
                            }
                        }

                        if ("border-none".equals(borderType)) {
                            style.setBorderLeft(BorderStyle.NONE); //左边框
                            style.setBorderRight(BorderStyle.NONE); //左边框
                            style.setBorderTop(BorderStyle.NONE); //左边框
                            style.setBorderBottom(BorderStyle.NONE); //左边框
                        }

                    }
                }


            }
        }

    }


    /**
     * 设置单元格，宽、高
     *
     * @param dbObject
     * @param sheet
     */
    private static void setColumAndRow(JSONObject dbObject, Sheet sheet) {
        if (dbObject.containsKey("config")) {
            JSONObject config = dbObject.getJSONObject("config");

            if (config.containsKey("columnlen")) {
                JSONObject columnlen = config.getJSONObject("columnlen");
                if (columnlen != null) {
                    for (String k : columnlen.keySet()) {
                        Integer _i = getStrToInt(k);
                        Integer _v = getStrToInt(columnlen.get(k).toString());
                        if (_i != null && _v != null) {
//                            sheet.setColumnWidth(_i, MSExcelUtil.heightUnits2Pixel(_v.shortValue()));
                            // TODO 乘以32，有待商榷
                            sheet.setColumnWidth(_i, _v * 32);
                        }
                    }
                }
            }
            if (config.containsKey("rowlen")) {
                JSONObject rowlen = config.getJSONObject("rowlen");
                if (rowlen != null) {
                    for (String k : rowlen.keySet()) {
                        Integer _i = getStrToInt(k);
                        Integer _v = getStrToInt(rowlen.get(k).toString());
                        if (_i != null && _v != null) {
                            Row row = sheet.getRow(_i);
                            if (row != null) {
//                                row.setHeightInPoints(MSExcelUtil.pixel2WidthUnits(_v.shortValue()));
                                row.setHeightInPoints(_v.shortValue());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 单元格字体相关样式
     *
     * @param wb
     * @param style
     * @param dbObject
     */
    private static void setCellStyleFont(Workbook wb, CellStyle style, JSONObject dbObject) {
        Font font = wb.createFont();
        style.setFont(font);

        //ff 字体
        if (dbObject.containsKey("ff")) {
            if (dbObject.get("ff") instanceof Integer) {
                Integer _v = getIntByDBObject(dbObject, "ff");
                if (_v != null && ConstantUtil.ff_IntegerToName.containsKey(_v)) {
                    font.setFontName(ConstantUtil.ff_IntegerToName.get(_v));
                }
            } else if (dbObject.get("ff") instanceof String) {
                font.setFontName(getByDBObject(dbObject, "ff"));
            }
        }
        //fc 字体颜色
        if (dbObject.containsKey("fc")) {
            String _v = getByDBObject(dbObject, "fc");
            Short _color = ColorUtil.getColorByStr(_v);
            if (_color != null) {
                font.setColor(_color);
            }
        }
        //bl 粗体
        if (dbObject.containsKey("bl")) {
            Integer _v = getIntByDBObject(dbObject, "bl");
            if (_v != null) {
                if (_v.equals(1)) {
                    //是否粗体显示
                    font.setBold(true);
                } else {
                    font.setBold(false);
                }
            }
        }
        //it 斜体
        if (dbObject.containsKey("it")) {
            Integer _v = getIntByDBObject(dbObject, "it");
            if (_v != null) {
                if (_v.equals(1)) {
                    font.setItalic(true);
                } else {
                    font.setItalic(false);
                }
            }
        }
        //fs 字体大小
        if (dbObject.containsKey("fs")) {
            Integer _v = getStrToInt(getObjectByDBObject(dbObject, "fs"));
            if (_v != null) {
                font.setFontHeightInPoints(_v.shortValue());
            }
        }
        //cl 删除线 (导入没有)   0 常规 、 1 删除线
        if (dbObject.containsKey("cl")) {
            Integer _v = getIntByDBObject(dbObject, "cl");
            if (_v != null) {
                if (_v.equals(1)) {
                    font.setStrikeout(true);
                }
            }
        }
        //ul 下划线
        if (dbObject.containsKey("ul")) {
            Integer _v = getIntByDBObject(dbObject, "ul");
            if (_v != null) {
                if (_v.equals(1)) {
                    font.setUnderline(Font.U_SINGLE);
                } else {
                    font.setUnderline(Font.U_NONE);
                }
            }
        }

    }

    /**
     * 设置cell边框颜色样式
     *
     * @param style    样式
     * @param dbObject json对象
     * @param bs       样式
     * @param bc       样式
     */
    private static void setBorderStyle(CellStyle style, JSONObject dbObject, String bs, String bc) {
        //bs 边框样式
        if (dbObject.containsKey(bs)) {
            Integer _v = getStrToInt(getByDBObject(dbObject, bs));
            if (_v != null) {
                //边框没有，不作改变
                if (bs.equals("bs") || bs.equals("bs_t")) {
                    style.setBorderTop(BorderStyle.valueOf(_v.shortValue()));
                }
                if (bs.equals("bs") || bs.equals("bs_b")) {
                    style.setBorderBottom(BorderStyle.valueOf(_v.shortValue()));
                }
                if (bs.equals("bs") || bs.equals("bs_l")) {
                    style.setBorderLeft(BorderStyle.valueOf(_v.shortValue()));
                }
                if (bs.equals("bs") || bs.equals("bs_r")) {
                    style.setBorderRight(BorderStyle.valueOf(_v.shortValue()));
                }

                //bc 边框颜色
                String _vcolor = getByDBObject(dbObject, bc);
                if (_vcolor != null) {
                    Short _color = ColorUtil.getColorByStr(_vcolor);
                    if (_color != null) {
                        if (bc.equals("bc") || bc.equals("bc_t")) {
                            style.setTopBorderColor(_color);
                        }
                        if (bc.equals("bc") || bc.equals("bc_b")) {
                            style.setBottomBorderColor(_color);
                        }
                        if (bc.equals("bc") || bc.equals("bc_l")) {
                            style.setLeftBorderColor(_color);
                        }
                        if (bc.equals("bc") || bc.equals("bc_r")) {
                            style.setRightBorderColor(_color);
                        }
                    }
                }
            }
        }
    }


    /**
     * 设置单元格格式  ct 单元格值格式 (fa,t)
     *
     * @param cell
     * @param style
     * @param dbObject
     */
    private static void setFormatByCt(Workbook wb, Cell cell, CellStyle style, JSONObject dbObject) {

        if (!dbObject.containsKey("v") && dbObject.containsKey("ct")) {
            /* 处理以下数据结构
             {
                "celldata": [{
                    "c": 0,
                    "r": 8,
                    "v": {
                        "ct": {
                            "s": [{
                                "v": "sdsdgdf\r\ndfgdfg\r\ndsfgdfgdf\r\ndsfgdfg"
                            }],
                            "t": "inlineStr",
                            "fa": "General"
                        }
                    }
                }]
            }
             */
            JSONObject ct = dbObject.getJSONObject("ct");
            if (ct.containsKey("s")) {
                Object s = ct.get("s");
                if (s instanceof List && ((List) s).size() > 0) {
                    JSONObject _s1 = (JSONObject) ((List) s).get(0);
                    if (_s1.containsKey("v") && _s1.get("v") instanceof String) {
                        dbObject.put("v", _s1.get("v"));
                        style.setWrapText(true);
                    }
                }

            }
        }

        //String v = "";  //初始化
        if (dbObject.containsKey("v")) {
            //v = v_json.get("v").toString();
            //取到v后，存到poi单元格对象
            //设置该单元格值
            //cell.setValue(v);

            //String v=getByDBObject(v_json,"v");
            //cell.setValue(v);
            Object obj = getObjectByDBObject(dbObject, "v");
            if (obj instanceof Number) {
                cell.setCellValue(Double.valueOf(obj.toString()));
            } else if (obj instanceof Double) {
                cell.setCellValue((Double) obj);
            } else if (obj instanceof Date) {
                cell.setCellValue((Date) obj);
            } else if (obj instanceof Calendar) {
                cell.setCellValue((Calendar) obj);
            } else if (obj instanceof RichTextString) {
                cell.setCellValue((RichTextString) obj);
            } else if (obj instanceof String) {
                cell.setCellValue((String) obj);
            } else {
                cell.setCellValue(obj.toString());
            }

        }

        if (dbObject.containsKey("ct")) {
            JSONObject ct = dbObject.getJSONObject("ct");
            if (ct.containsKey("fa") && ct.containsKey("t")) {
                //t 0=bool，1=datetime，2=error，3=null，4=numeric，5=string，6=unknown
                String fa = getByDBObject(ct, "fa"); //单元格格式format定义串
                String t = getByDBObject(ct, "t"); //单元格格式type类型

                Integer _i = ConstantUtil.getNumberFormatMap(fa);
                switch (t) {
                    case "s": {
                        //字符串
                        if (_i >= 0) {
                            style.setDataFormat(_i.shortValue());
                        } else {
                            style.setDataFormat((short) 0);
                        }
                        cell.setCellType(org.apache.poi.ss.usermodel.CellType.STRING);
                        break;
                    }
                    case "d": {
                        //日期
                        Date _d = null;
                        String v = getByDBObject(dbObject, "m");
                        if (v.length() == 0) {
                            v = getByDBObject(dbObject, "v");
                        }
                        if (v.length() > 0) {
                            if (v.indexOf("-") > -1) {
                                if (v.indexOf(":") > -1) {
                                    _d = ConstantUtil.stringToDateTime(v);
                                } else {
                                    _d = ConstantUtil.stringToDate(v);
                                }
                            } else {
                                _d = ConstantUtil.toDate(v);
                            }
                        }
                        if (_d != null) {
                            //能转换为日期
                            cell.setCellValue(_d);
                            DataFormat format = wb.createDataFormat();
                            style.setDataFormat(format.getFormat(fa));

                        } else {
                            //不能转换为日期
                            if (_i >= 0) {
                                style.setDataFormat(_i.shortValue());
                            } else {
                                style.setDataFormat((short) 0);
                            }
                        }
                        break;
                    }
                    case "b": {
                        //逻辑
                        cell.setCellType(org.apache.poi.ss.usermodel.CellType.BOOLEAN);
                        if (_i >= 0) {
                            style.setDataFormat(_i.shortValue());
                        } else {
                            DataFormat format = wb.createDataFormat();
                            style.setDataFormat(format.getFormat(fa));
                        }
                        break;
                    }
                    case "n": {
                        //数值
//                        cell.setCellType(CellType.NUMERIC);
                        //数字转字符串
                        cell.setCellType(org.apache.poi.ss.usermodel.CellType.STRING);
                        if (_i >= 0) {
                            style.setDataFormat(_i.shortValue());
                        } else {
                            DataFormat format = wb.createDataFormat();
                            style.setDataFormat(format.getFormat(fa));
                        }
                        break;
                    }
                    case "u":
                    case "g": {
                        //general 自动类型
                        //cell.setCellType(CellType._NONE);
                        if (_i >= 0) {
                            style.setDataFormat(_i.shortValue());
                        } else {
                            DataFormat format = wb.createDataFormat();
                            style.setDataFormat(format.getFormat(fa));
                        }
                        break;
                    }
                    case "e": {
                        //错误
                        cell.setCellType(org.apache.poi.ss.usermodel.CellType.ERROR);
                        if (_i >= 0) {
                            style.setDataFormat(_i.shortValue());
                        } else {
                            DataFormat format = wb.createDataFormat();
                            style.setDataFormat(format.getFormat(fa));
                        }
                        break;
                    }

                }

            }

        }
    }

    /**
     * 内容按行分组
     *
     * @param cells
     * @return
     */
    private static Map<Integer, List<JSONObject>> cellGroup(List<JSONObject> cells) {
        Map<Integer, List<JSONObject>> cellMap = new HashMap<>(100);
        for (JSONObject dbObject : cells) {
            //行号
            if (dbObject.containsKey("r")) {
                Integer r = getStrToInt(dbObject.get("r"));
                if (r != null) {
                    if (cellMap.containsKey(r)) {
                        cellMap.get(r).add(dbObject);
                    } else {
                        List<JSONObject> list = new ArrayList<>(10);
                        list.add(dbObject);
                        cellMap.put(r, list);
                    }
                }
            }

        }
        return cellMap;
    }


    /**
     * 获取一个k的值
     *
     * @param b
     * @param k
     * @return
     */
    public static String getByDBObject(JSONObject b, String k) {
        if (b.containsKey(k)) {
            if (b.get(k) != null && b.get(k) instanceof String) {
                return b.getString(k);
            }
        }
        return null;
    }

    /**
     * 获取一个k的值
     *
     * @param b
     * @param k
     * @return
     */
    public static Object getObjectByDBObject(JSONObject b, String k) {
        if (b.containsKey(k)) {
            if (b.get(k) != null) {
                return b.get(k);
            }
        }
        return "";
    }

    /**
     * 没有/无法转换 返回null
     *
     * @param b
     * @param k
     * @return
     */
    public static Integer getIntByDBObject(JSONObject b, String k) {
        if (b.containsKey(k)) {
            if (b.get(k) != null) {
                try {
                    String _s = b.getString(k).replace("px", "");
                    Double _d = Double.parseDouble(_s);
                    return _d.intValue();
                } catch (Exception ex) {
                    log.error(ex.getMessage());
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 转int
     *
     * @param str
     * @return
     */
    private static Integer getStrToInt(Object str) {
        try {
            if (str != null) {
                return Integer.parseInt(str.toString());
            }
            return null;
        } catch (Exception ex) {
            log.error("String:{};Error:{}", str, ex.getMessage());
            return null;
        }
    }

    private static Short getStrToShort(Object str) {
        try {
            if (str != null) {
                return Short.parseShort(str.toString());
            }
            return null;
        } catch (Exception ex) {
            log.error("String:{};Error:{}", str, ex.getMessage());
            return null;
        }
    }
}
