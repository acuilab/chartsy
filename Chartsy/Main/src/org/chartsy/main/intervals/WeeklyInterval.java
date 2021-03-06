package org.chartsy.main.intervals;

import java.io.Serializable;
import java.util.Calendar;
import org.chartsy.main.utils.SerialVersion;

/**
 * 每周间隔
 * @author viorel.gheba
 */
public class WeeklyInterval extends Interval implements Serializable {

    private static final long serialVersionUID = SerialVersion.APPVERSION;

    public WeeklyInterval() {
        super("Weekly");
        timeParam = "w";
    }

    @Override
    public long startTime() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -4);
        return c.getTimeInMillis();
    }

    @Override
    public String getTimeParam() {
        return timeParam;
    }

    @Override
    public int getLengthInSeconds() {
        return 604800;
    }

}
