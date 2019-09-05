package org.chartsy.main.intervals;

import java.io.Serializable;
import java.util.Calendar;
import org.chartsy.main.utils.SerialVersion;

/**
 * 每天间隔
 * @author viorel.gheba
 */
public class DailyInterval extends Interval implements Serializable {

    private static final long serialVersionUID = SerialVersion.APPVERSION;

    public DailyInterval() {
        super("Daily");
        timeParam = "d";
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
        return 86400;
    }

}
