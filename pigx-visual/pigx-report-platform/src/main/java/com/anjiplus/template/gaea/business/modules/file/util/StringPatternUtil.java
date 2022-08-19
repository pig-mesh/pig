package com.anjiplus.template.gaea.business.modules.file.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringPatternUtil {


    /**
     *
     * <p>Discription:[查找源sourceStr中是否包含pattern，返回boolean]</p>
     * @author
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param sourceStr
     * @param pattern
     * @return
     */
    public static boolean stringMatch(String sourceStr, String pattern){
        boolean result=false;
        try{
            if(StringUtils.isBlank(sourceStr)|| StringUtils.isBlank(pattern)){
                return result;
            }
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sourceStr);
            while (m.find()) {
                //String aa=m.group(0);
                result=true;
                break ;
            }

        }catch(Exception e){
            result=false;
        }
        return result;
    }

    public static boolean stringMatchIgnoreCase(String sourceStr, String pattern){
        boolean result=false;
        try{
            if(StringUtils.isBlank(sourceStr)|| StringUtils.isBlank(pattern)){
                return result;
            }
            sourceStr=sourceStr.toLowerCase();
            pattern=pattern.toLowerCase();
            result= stringMatch(sourceStr,pattern);
        }catch(Exception e){
            result=false;
        }
        return result;
    }



    /**
     * <p>Discription:[查找源sourceStr中是否包含pattern，返回找到的字符串，未找到就返回空字符串]</p>
     * @author
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     * @param sourceStr
     * @param pattern
     * @return
     */
    public static String stringFind(String sourceStr, String pattern){
        String result="";
        try{
            if(StringUtils.isBlank(sourceStr)|| StringUtils.isBlank(pattern)){
                return result;
            }
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sourceStr);
            while (m.find()) {
                result = m.group(0);
                break;
            }
        }catch(Exception e){
            result="";
        }
        return result;
    }

    public static String replace(String sourceStr, String pattern, String replaceStr){
        String result="";
        try{
            if(StringUtils.isBlank(sourceStr)|| StringUtils.isBlank(pattern)){
                return result;
            }
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(sourceStr);
            result = m.replaceAll(replaceStr);
        }catch(Exception e){
            result="";
        }
        return result;
    }
}
