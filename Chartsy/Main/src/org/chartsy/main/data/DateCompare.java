package org.chartsy.main.data;

import java.util.Comparator;

/**
 * 日期比较(按照数据项的日期)
 * @author viorel.gheba
 */
public class DateCompare implements Comparator<DataItem> {

    @Override
    public int compare(DataItem o1, DataItem o2) {
        return o1.getDate().compareTo(o2.getDate());
    }

}
