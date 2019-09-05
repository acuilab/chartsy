package org.chartsy.main;

import java.util.EventListener;
import org.chartsy.main.chart.Chart;
import org.chartsy.main.chart.Indicator;
import org.chartsy.main.chart.Overlay;
import org.chartsy.main.data.Stock;
import org.chartsy.main.intervals.Interval;

/**
 * 图表窗口监听器
 *
 * @author Viorel
 */
public interface ChartFrameListener extends EventListener {

    // 股票改变
    public void stockChanged(Stock newStock);

    // 间隔改变
    public void intervalChanged(Interval newInterval);

    // 图表改变
    public void chartChanged(Chart newChart);

    // 
    public void datasetKeyChanged(String datasetKey);

    public void indicatorAdded(Indicator indicator);

    public void indicatorRemoved(Indicator indicator);

    public void overlayAdded(Overlay overlay);

    public void overlayRemoved(Overlay overlay);

    // 缩小
    public double zoomIn(double barWidth);

    // 放大
    public double zoomOut(double barWidth);

}
