package com.anjiplus.template.gaea.business.modules.report.constant;


import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ExpConstant {

    public static final String[] FUNCTION = new String[]{"=SUM(", "=AVERAGE(", "=MAX(", "=MIN(", "=IF(", "=AND(", "=OR(", "=CONCAT("};

    public static List<Integer> getExpFunction(String e) {
        List<Integer> counts = new ArrayList<>();
        for (int i = 0; i < FUNCTION.length; i++) {
            if(e.contains(FUNCTION[i])){
                counts.add(i);
            }
        }

        return counts;
    }

}
