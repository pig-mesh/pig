package com.anjiplus.template.gaea.business.modules.reportexcel.util;


import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 来自：https://github.com/mengshukeji/LuckysheetServer
 *
 * @author cr
 */
public class ConstantUtil {
    /**
     * 导出。字体转换
     */
    public static Map<Integer, String> ff_IntegerToName = new HashMap<Integer, String>();
    /**
     * 导入。字体转换
     */
    public static Map<String, Integer> ff_NameToInteger = new HashMap<String, Integer>();

    /**
     * 导入 36种数字格式。注意官方文档的编号不是连续的，22后面直接是37，所以数组中间补14个空值
     */
    public static String[] number_type = null;
    /**
     * 导入 36种格式的定义字符串
     */
    public static String[] number_format = null;
    /**
     * 数据类型
     */
    public static Map<String, Integer> number_format_map = new HashMap<String, Integer>();

    static {
        //格式
        nf();
        //字体
        ff();
    }

    private static void nf() {
        number_type = new String[]{
                "General", "Decimal", "Decimal", "Decimal", "Decimal", "Currency", "Currency", "Currency", "Currency",
                "Percentage", "Percentage", "Scientific", "Fraction", "Fraction", "Date", "Date", "Date", "Date",
                "Time", "Time", "Time", "Time", "Time",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "Accounting", "Accounting", "Accounting", "Accounting", "Accounting",
                "Currency", "Accounting", "Currency", "Time", "Time", "Time", "Scientific", "Text"
        };

        number_format = new String[]{
                "General", "0", "0.00", "#,##0", "#,##0.00", "$#,##0;($#,##0)", "$#,##0;[Red]($#,##0)", "$#,##0.00;($#,##0.00)", "$#,##0.00;[Red]($#,##0.00)",
                "0%", "0.00%", "0.00E+00", "# ?/?", "# ??/??", "m/d/yyyy", "d-mmm-yy", "d-mmm", "mmm-yy",
                "h:mm AM/PM", "h:mm:ss AM/PM", "h:mm", "h:mm:ss", "m/d/yyyy h:mm",
                "", "", "", "", "", "", "", "", "", "", "", "", "", "",
                "#,##0;(#,##0)", "#,##0;[Red](#,##0)", "#,##0.00;(#,##0.00)", "#,##0.00;[Red](#,##0.00)", "_ * #,##0_ ;_ * (#,##0)_ ;_ * \"-\"_ ;_ @_",
                "_ $* #,##0_ ;_ $* (#,##0)_ ;_ $* \"-\"_ ;_ @_", "_ * #,##0.00_ ;_ * (#,##0.00)_ ;_ * \"-\"??_ ;_ @_", "_ $* #,##0.00_ ;_ $* (#,##0.00)_ ;_ $* \"-\"??_ ;_ @_", "mm:ss", "[h]:mm:ss", "mm:ss.0", "##0.0E+00", "@"
        };
        for (int x = 0; x < number_format.length; x++) {
            if (number_format[x].length() > 0) {
                number_format_map.put(number_format[x].toLowerCase(), x);
            }
        }
    }

    private static void ff() {
        //0 微软雅黑、1 宋体（Song）、2 黑体（ST Heiti）、3 楷体（ST Kaiti）、 4仿宋（ST FangSong）、 5 新宋体（ST Song）、
        // 6 华文新魏、 7华文行楷、 8 华文隶书、 9 Arial、 10 Times New Roman 、11 Tahoma 、12 Verdana
        ff_IntegerToName.put(0, "微软雅黑");
        ff_IntegerToName.put(1, "宋体");
        ff_IntegerToName.put(2, "黑体");
        ff_IntegerToName.put(3, "楷体");
        ff_IntegerToName.put(4, "仿宋");
        ff_IntegerToName.put(5, "新宋体");
        ff_IntegerToName.put(6, "华文新魏");
        ff_IntegerToName.put(7, "华文行楷");
        ff_IntegerToName.put(8, "华文隶书");
        ff_IntegerToName.put(9, "Arial");
        ff_IntegerToName.put(10, "Times New Roman");
        ff_IntegerToName.put(11, "Tahoma");
        ff_IntegerToName.put(12, "Verdana");

        //0 微软雅黑、1 宋体（Song）、2 黑体（ST Heiti）、3 楷体（ST Kaiti）、 4仿宋（ST FangSong）、 5 新宋体（ST Song）、
        // 6 华文新魏、 7华文行楷、 8 华文隶书、 9 Arial、 10 Times New Roman 、11 Tahoma 、12 Verdana
        ff_NameToInteger.put("微软雅黑", 0);
        ff_NameToInteger.put("宋体", 1);
        ff_NameToInteger.put("Song", 1);
        ff_NameToInteger.put("黑体", 2);
        ff_NameToInteger.put("ST Heiti", 2);
        ff_NameToInteger.put("楷体", 3);
        ff_NameToInteger.put("ST Kaiti", 3);
        ff_NameToInteger.put("仿宋", 4);
        ff_NameToInteger.put("ST FangSong", 4);
        ff_NameToInteger.put("新宋体", 5);
        ff_NameToInteger.put("ST Song", 5);
        ff_NameToInteger.put("华文新魏", 6);
        ff_NameToInteger.put("华文行楷", 7);
        ff_NameToInteger.put("华文隶书", 8);
        ff_NameToInteger.put("Arial", 9);
        ff_NameToInteger.put("Times New Roman", 10);
        ff_NameToInteger.put("Tahoma", 11);
        ff_NameToInteger.put("Verdana", 12);
    }

    private static void borderType() {
        //"border-left" | "border-right" | "border-top" | "border-bottom" | "border-all"
        // | "border-outside" | "border-inside" | "border-horizontal" | "border-vertical" | "border-none"
        ff_IntegerToName.put(0, "微软雅黑");
        ff_IntegerToName.put(1, "宋体");
        ff_IntegerToName.put(2, "黑体");
        ff_IntegerToName.put(3, "楷体");
        ff_IntegerToName.put(4, "仿宋");
        ff_IntegerToName.put(5, "新宋体");
        ff_IntegerToName.put(6, "华文新魏");
        ff_IntegerToName.put(7, "华文行楷");
        ff_IntegerToName.put(8, "华文隶书");
        ff_IntegerToName.put(9, "Arial");
        ff_IntegerToName.put(10, "Times New Roman");
        ff_IntegerToName.put(11, "Tahoma");
        ff_IntegerToName.put(12, "Verdana");

        //0 微软雅黑、1 宋体（Song）、2 黑体（ST Heiti）、3 楷体（ST Kaiti）、 4仿宋（ST FangSong）、 5 新宋体（ST Song）、
        // 6 华文新魏、 7华文行楷、 8 华文隶书、 9 Arial、 10 Times New Roman 、11 Tahoma 、12 Verdana
        ff_NameToInteger.put("微软雅黑", 0);
        ff_NameToInteger.put("宋体", 1);
        ff_NameToInteger.put("Song", 1);
        ff_NameToInteger.put("黑体", 2);
        ff_NameToInteger.put("ST Heiti", 2);
        ff_NameToInteger.put("楷体", 3);
        ff_NameToInteger.put("ST Kaiti", 3);
        ff_NameToInteger.put("仿宋", 4);
        ff_NameToInteger.put("ST FangSong", 4);
        ff_NameToInteger.put("新宋体", 5);
        ff_NameToInteger.put("ST Song", 5);
        ff_NameToInteger.put("华文新魏", 6);
        ff_NameToInteger.put("华文行楷", 7);
        ff_NameToInteger.put("华文隶书", 8);
        ff_NameToInteger.put("Arial", 9);
        ff_NameToInteger.put("Times New Roman", 10);
        ff_NameToInteger.put("Tahoma", 11);
        ff_NameToInteger.put("Verdana", 12);
    }


    /**
     * 按自定义格式
     *
     * @param fa
     * @return
     */
    public static Integer getNumberFormatMap(String fa) {
        if (number_format_map.containsKey(fa.toLowerCase())) {
            return number_format_map.get(fa.toLowerCase());
        }
        return -1;
    }

    /**
     * 获取poi表格垂直对齐  0 中间、1 上、2下
     *
     * @param i
     * @return
     */
    public static VerticalAlignment getVerticalType(int i) {
        if (0 == i) {
            return VerticalAlignment.CENTER;
        } else if (1 == i) {
            return VerticalAlignment.TOP;
        } else if (2 == i) {
            return VerticalAlignment.BOTTOM;
        }
        //默认居中
        return VerticalAlignment.CENTER;
    }

    /**
     * 获取poi表格水平对齐 0 居中、1 左、2右
     *
     * @param i
     * @return
     */
    public static HorizontalAlignment getHorizontaltype(int i) {
        if (2 == i) {
            return HorizontalAlignment.RIGHT;
        } else if (1 == i) {
            return HorizontalAlignment.LEFT;
        } else if (0 == i) {
            return HorizontalAlignment.CENTER;
        }
        //默认右
        return HorizontalAlignment.RIGHT;
    }

    /**
     * 文字旋转
     * 文字旋转角度（0=0,1=45，2=-45，3=竖排文字，4=90，5=-90）
     *
     * @param i
     * @return
     */
    public static short getRotation(int i) {
        short t = 0;
        switch (i) {
            case 1:
                t = 45;
                break;
            case 2:
                t = -45;
                break;
            case 3:
                t = 255;
                break;
            case 4:
                t = 90;
                break;
            case 5:
                t = -90;
                break;

            default:
                t = 0;
        }
        return t;
    }


    private static SimpleDateFormat df_DateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date stringToDateTime(String date) {
        if (date == null || date.length() == 0) {
            return null;
        }
        try {
            return df_DateTime.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private static SimpleDateFormat df_Date = new SimpleDateFormat("yyyy-MM-dd");

    public static Date stringToDate(String date) {
        if (date == null || date.length() == 0) {
            return null;
        }
        try {
            return df_Date.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date toDate(String numberString) {
        try {
            Double _d = Double.parseDouble(numberString);
            String _s = toDate(_d, "yyyy-MM-dd HH:mm:ss");
            if (numberString.indexOf(".") > -1) {
                return stringToDate(_s);
            } else {
                return stringToDateTime(_s);
            }

        } catch (Exception ex) {
            System.out.println(ex.toString() + " " + numberString);
        }
        return null;
    }

    private static final int SECONDS_PER_MINUTE = 60;
    private static final int MINUTES_PER_HOUR = 60;
    private static final int HOURS_PER_DAY = 24;
    private static final int SECONDS_PER_DAY = (HOURS_PER_DAY * MINUTES_PER_HOUR * SECONDS_PER_MINUTE);
    /**
     * 一天的毫秒数
     **/
    private static final long DAY_MILLISECONDS = SECONDS_PER_DAY * 1000L;

    /**
     * 转换方法
     *
     * @parma numberString 要转换的浮点数
     * @parma format 要获得的格式 例如"hh:mm:ss"
     **/
    public static String toDate(double numberString, String format) {
        SimpleDateFormat sdFormat = new SimpleDateFormat(format);
        int wholeDays = (int) Math.floor(numberString);
        int millisecondsInday = (int) ((numberString - wholeDays) * DAY_MILLISECONDS + 0.5);
        Calendar calendar = new GregorianCalendar();
        setCalendar(calendar, wholeDays, millisecondsInday, false);
        return sdFormat.format(calendar.getTime());
    }

    private static void setCalendar(Calendar calendar, int wholeDays,
                                    int millisecondsInDay, boolean use1904windowing) {
        int startYear = 1900;
        int dayAdjust = -1; // Excel thinks 2/29/1900 is a valid date, which it isn't
        if (use1904windowing) {
            startYear = 1904;
            dayAdjust = 1; // 1904 date windowing uses 1/2/1904 as the first day
        } else if (wholeDays < 61) {
            // Date is prior to 3/1/1900, so adjust because Excel thinks 2/29/1900 exists
            // If Excel date == 2/29/1900, will become 3/1/1900 in Java representation
            dayAdjust = 0;
        }
        calendar.set(startYear, 0, wholeDays + dayAdjust, 0, 0, 0);
        calendar.set(GregorianCalendar.MILLISECOND, millisecondsInDay);
    }
}
