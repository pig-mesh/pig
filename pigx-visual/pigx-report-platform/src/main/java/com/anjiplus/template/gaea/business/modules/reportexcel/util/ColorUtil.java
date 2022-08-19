package com.anjiplus.template.gaea.business.modules.reportexcel.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color;
import org.apache.poi.xssf.usermodel.XSSFColor;


/**
 * 来自：https://github.com/mengshukeji/LuckysheetServer
 *
 * @author Administrator
 */
@Slf4j
public class ColorUtil {

    private static final String S = "0123456789ABCDEF";

    public static Short getColorByStr(String colorStr) {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFPalette palette = workbook.getCustomPalette();

        if (colorStr.toLowerCase().startsWith("rgb")) {
            colorStr = colorStr.toLowerCase().replace("rgb(", "").replace(")", "");
            String[] colors = colorStr.split(",");
            if (colors.length == 3) {
                try {
                    int red = Integer.parseInt(colors[0].trim(), 16);
                    int green = Integer.parseInt(colors[1].trim(), 16);
                    int blue = Integer.parseInt(colors[2].trim(), 16);

                    HSSFColor hssfColor = palette.findSimilarColor(red, green, blue);
                    return hssfColor.getIndex();
                } catch (Exception ex) {
                    log.error(ex.toString());
                    return null;
                }
            }
            return null;
        }

        if (colorStr.equals("#000")) {
            colorStr = "#000000";
        }
        if (colorStr != null && colorStr.length() >= 6) {
            try {
                if (colorStr.length() == 8) {
                    colorStr = colorStr.substring(2);
                }
                if (colorStr.length() == 7) {
                    colorStr = colorStr.substring(1);
                }
                String str2 = colorStr.substring(0, 2);
                String str3 = colorStr.substring(2, 4);
                String str4 = colorStr.substring(4, 6);
                int red = Integer.parseInt(str2, 16);
                int green = Integer.parseInt(str3, 16);
                int blue = Integer.parseInt(str4, 16);

                HSSFColor hssfColor = palette.findSimilarColor(red, green, blue);
                return hssfColor.getIndex();
            } catch (Exception ex) {
                log.error(ex.toString());
                return null;
            }
        }
        return null;
    }

    /**
     * RGB转换成十六进制
     *
     * @param r
     * @param g
     * @param b
     * @return
     */
    public static String convertRGBToHex(short r, short g, short b) {
        String hex = "";
        if (r >= 0 && r < 256 && g >= 0 && g < 256 && b >= 0 && b < 256) {
            int x, y, z;
            x = r % 16;
            r = (short) ((r - x) / 16);
            y = g % 16;
            g = (short) ((g - y) / 16);
            z = b % 16;
            b = (short) ((b - z) / 16);
            hex = "#" + S.charAt(r) + S.charAt(x) + S.charAt(g) + S.charAt(y) + S.charAt(b) + S.charAt(z);
        }
        return hex;
    }

    /**
     * @param cell 单元格
     * @return 转换RGB颜色值
     * @description tint转换RBG
     * @author zhouhang
     * @date 2021/4/26
     */
    public static String getFillColorHex(Cell cell) {
        String fillColorString = null;
        if (cell != null) {
            CellStyle cellStyle = cell.getCellStyle();
            Color color = cellStyle.getFillForegroundColorColor();
            if (color instanceof XSSFColor) {
                XSSFColor xssfColor = (XSSFColor) color;
                byte[] argb = xssfColor.getARGB();
                fillColorString = convertRGBToHex((short) (argb[1] & 0xFF), (short) (argb[2] & 0xFF), (short) (argb[3] & 0xFF));
                // TODO: 2021/4/26 添加透明度
//                if (xssfColor.hasTint()) {
//                    fillColorString += " * " + xssfColor.getTint();
//                    byte[] rgb = xssfColor.getRGBWithTint();
//                    fillColorString += " = [" + (argb[0] & 0xFF) + ", " + (rgb[0] & 0xFF) + ", " + (rgb[1] & 0xFF) + ", " + (rgb[2] & 0xFF) + "]";
//                }
            } else if (color instanceof HSSFColor) {
                HSSFColor hssfColor = (HSSFColor) color;
                short[] rgb = hssfColor.getTriplet();
                fillColorString = convertRGBToHex((short) (rgb[0] & 0xFF), (short) (rgb[1] & 0xFF), (short) (rgb[2] & 0xFF));
                //去除黑色背景
                if (StringUtils.equals("#000000", fillColorString)) {
                    return null;
                }
            }
        }
        return fillColorString;
    }

}
