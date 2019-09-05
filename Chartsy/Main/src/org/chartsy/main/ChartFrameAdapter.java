package org.chartsy.main;

import org.chartsy.main.chart.Chart;
import org.chartsy.main.chart.Indicator;
import org.chartsy.main.chart.Overlay;
import org.chartsy.main.data.Stock;
import org.chartsy.main.intervals.Interval;

/**
 *
 * @author Viorel
 */
public abstract class ChartFrameAdapter implements ChartFrameListener {

    // 股票改变
    @Override
    public void stockChanged(Stock newStock) {
    }

    @Override
    public void intervalChanged(Interval newInterval) {
    }

    @Override
    public void chartChanged(Chart newChart) {
    }

    // 数据集key改变
    @Override
    public void datasetKeyChanged(String datasetKey) {
    }

    @Override
    public void indicatorAdded(Indicator indicator) {
    }

    @Override
    public void indicatorRemoved(Indicator indicator) {
    }

    @Override
    public void overlayAdded(Overlay overlay) {
    }

    @Override
    public void overlayRemoved(Overlay overlay) {
    }

    @Override
    public double zoomIn(double barWidth) {
        return barWidth;
    }

    @Override
    public double zoomOut(double barWidth) {
        return barWidth;
    }

}
