package org.chartsy.main.chart;

import java.awt.Graphics2D;
import java.io.Serializable;
import org.chartsy.main.ChartFrame;
import org.chartsy.main.utils.SerialVersion;

/**
 * 图表
 *
 * @author viorel.gheba
 */
public abstract class Chart implements Serializable {

    private static final long serialVersionUID = SerialVersion.APPVERSION;

    public Chart() {
    }

    // 获得图表名称
    public abstract String getName();

    // 绘制图表
    public abstract void paint(Graphics2D g, ChartFrame cf);

}
