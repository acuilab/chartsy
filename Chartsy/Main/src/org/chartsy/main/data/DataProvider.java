package org.chartsy.main.data;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import org.chartsy.main.exceptions.InvalidStockException;
import org.chartsy.main.exceptions.RegistrationException;
import org.chartsy.main.exceptions.StockNotFoundException;
import org.chartsy.main.intervals.DailyInterval;
import org.chartsy.main.intervals.FifteenMinuteInterval;
import org.chartsy.main.intervals.FiveMinuteInterval;
import org.chartsy.main.intervals.Interval;
import org.chartsy.main.intervals.MonthlyInterval;
import org.chartsy.main.intervals.OneMinuteInterval;
import org.chartsy.main.intervals.SixtyMinuteInterval;
import org.chartsy.main.intervals.ThirtyMinuteInterval;
import org.chartsy.main.intervals.WeeklyInterval;
import org.chartsy.main.managers.CacheManager;
import org.chartsy.main.managers.DatasetUsage;
import org.chartsy.main.utils.SerialVersion;
import org.openide.util.NbBundle;

/**
 * 数据提供者
 * @author viorel.gheba
 */
public abstract class DataProvider implements Serializable {

    private static final long serialVersionUID = SerialVersion.APPVERSION;

    public DataProvider(ResourceBundle bundle) {
        this(bundle, false, false);
    }

    public DataProvider(ResourceBundle bundle, boolean supportsIntraDay) {
        this(bundle, supportsIntraDay, false);
    }

    public DataProvider(
            ResourceBundle bundle,
            boolean supportsIntraDay,
            boolean supportsCustomIntervals) {
        this.name = bundle.getString("DataProvider_NAME");

        String[] exgs = bundle.getString("DataProvider_EXG").split(":");
        exchanges = new Exchange[exgs.length];
        for (int i = 0; i < exgs.length; i++) {
            String exchange = exgs[i];
            String resName = exgs[i].replace(" ", "") + "_PRE";
            String sufix = bundle.getString(resName);
            if (sufix.equals("null")) {
                sufix = "";
            }
            exchanges[i] = new Exchange(exchange, sufix);
        }
        this.supportsIntraDay = supportsIntraDay;
        this.supportsCustomInterval = supportsCustomIntervals;
    }

    public void initialize() {
    }

    public String getName() {
        return this.name;
    }

    public Exchange[] getExchanges() {
        return this.exchanges;
    }

    public Interval[] getIntraDayIntervals() {
        return INTRA_DAY_INTERVALS;
    }

    public Interval[] getIntervals() {
        return INTERVALS;
    }

    public Interval[] getSupportedIntervals() {
        return new Interval[]{};
    }

    public Interval getIntervalFromKey(String key) {
        for (Interval i : INTERVALS) {
            if (key.endsWith(i.getTimeParam())) {
                return i;
            }
        }
        for (Interval i : INTRA_DAY_INTERVALS) {
            if (key.endsWith(i.getTimeParam())) {
                return i;
            }
        }
        return null;
    }

    public static Interval getInterval(int intervalHash) {
        for (Interval interval : INTERVALS) {
            if (interval.hashCode() == intervalHash) {
                return interval;
            }
        }
        for (Interval interval : INTRA_DAY_INTERVALS) {
            if (interval.hashCode() == intervalHash) {
                return interval;
            }
        }
        return null;
    }

    public boolean supportsIntraday() {
        return supportsIntraDay;
    }

    public boolean supportsCustomInterval() {
        return supportsCustomInterval;
    }

    public boolean supportsAnyInterval() {
        return false;
    }

    /**
     * 根据股票代码拉取股票
     * @param symbol
     * @throws InvalidStockException
     * @throws StockNotFoundException
     * @throws RegistrationException
     * @throws IOException 
     */
    public void fetchStock(String symbol)
            throws InvalidStockException, StockNotFoundException, RegistrationException, IOException {
        String symb = "";
        String exchange = "";
        String company = "";

        symbol.trim();
        String delimiter = "."; // 股票代码分隔符

        if (symbol.contains(delimiter)) {
            // 若股票代码包含分隔符，则代码取分隔符之前的文本，交易所取分隔符之后的文本
            int index = symbol.indexOf(delimiter);
            symb = symbol.substring(0, index);
            exchange = symbol.substring(index - 1, symbol.length() - 1);
        } else {
            // 若股票代码不包含分隔符，则不设置exchange
            symb = symbol;
        }

        company = fetchCompanyName(symbol);

        Stock stock = new Stock(symb, exchange);
        stock.setCompanyName(company);
        cacheStock(stock);
    }

    /**
     * 根据股票代码拉取公司名称
     * @param symbol
     * @return
     * @throws InvalidStockException
     * @throws StockNotFoundException
     * @throws RegistrationException
     * @throws IOException 
     */
    protected abstract String fetchCompanyName(String symbol)
            throws InvalidStockException, StockNotFoundException, RegistrationException, IOException;

    // 将股票加入缓存中
    protected void cacheStock(Stock stock)
            throws IOException {
        String fileName = getStockKey(stock);
        CacheManager.getInstance().cacheStock(stock, fileName);
    }

    // 指定代码的股票是否已缓存
    public boolean stockExists(String symbol) {
        String fileName = getStockKey(symbol);
        return CacheManager.getInstance().stockCacheExists(fileName);
    }

    // 从缓存中拉取股票
    public Stock fetchStockFromCache(String symbol) {
        Stock stock = null;
        String fileName = getStockKey(symbol);
        try {
            stock = CacheManager.getInstance().fetchStockFromCache(fileName);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            stock = null;
        }

        return stock;
    }

    // 为收藏夹拉取数据集
    public void fetchDatasetForFavorites(Stock stock) throws IOException, ParseException {
        Dataset dataset = fetchDataForFavorites(stock);
        if (dataset != null) {
            String key = getDatasetKey(stock, DAILY);
            DatasetUsage.getInstance().addDataset(key, dataset);
        } else {
            throw new IOException();
        }
    }

    // 为收藏夹拉取数据
    protected abstract Dataset fetchDataForFavorites(Stock stock) throws IOException, ParseException;

    public void fetchDataset(Stock stock, Interval interval)
            throws IOException, ParseException {
        Dataset dataset = fetchData(stock, interval);
        if (dataset != null) {
            String key = getDatasetKey(stock, interval);
            DatasetUsage.getInstance().addDataset(key, dataset);
            //cacheDataset(key, dataset);
        } else {
            throw new IOException();
        }
    }

    protected abstract Dataset fetchData(Stock stock, Interval interval)
            throws IOException, ParseException;

    public DataItem getLastDataItem(Stock stock, Interval interval) {
        try {
            return fetchLastDataItem(stock, interval);
        } catch (Exception ex) {
            return null;
        }
    }

    protected abstract DataItem fetchLastDataItem(Stock stock, Interval interval)
            throws IOException, ParseException;

    public List<DataItem> getLastDataItems(Stock stock, Interval interval) {
        return new ArrayList<DataItem>();
    }

    // 更新是否支持盘中
    public boolean updateIntraDay(String key, List<DataItem> dataItems) {
        return false;
    }

    // 缓存数据集
    protected void cacheDataset(String key, Dataset dataset) throws IOException {
        CacheManager.getInstance().cacheDataset(dataset, key);
    }

    // 数据集是否存在
    public boolean datasetExists(Stock stock, Interval interval) {
        String fileName = getDatasetKey(stock, interval);
        return CacheManager.getInstance().datasetCacheExists(fileName);
    }

    // 从缓存中拉取数据集
    public void fetchDatasetFromCache(Stock stock, Interval interval)
            throws IOException {
        String fileName = getDatasetKey(stock, interval);
        CacheManager.getInstance().fetchDatasetFromCache(fileName);
    }

    public abstract StockSet fetchAutocomplete(String text) throws IOException;

    /*
     * Return the refresh interval in seconds
     * 返回刷新间隔（以秒为单位）
     */
    public abstract int getRefreshInterval();

    // 获得股票key
    public String getStockKey(Stock stock) {
        return getStockKey(stock.getKey());
    }

    // 获得股票key：数据提供者名称_股票代码
    public String getStockKey(String symbol) {
        return NbBundle.getMessage(
                DataProvider.class,
                "Stock_KEY",
                getName(),
                symbol);
    }

    // 获得数据集key
    public String getDatasetKey(Stock stock, Interval interval) {
        return getDatasetKey(stock.getKey(), interval);
    }

    // 获得数据集key：数据提供者名称_股票代码_间隔
    public String getDatasetKey(String symbol, Interval interval) {
        return NbBundle.getMessage(
                DataProvider.class,
                "Dataset_KEY",
                getName(),
                symbol,
                interval.getTimeParam());
    }

    // 是否需要注册
    public boolean needsRegistration() {
        return needsRegistration;
    }

    // 是否已注册
    public boolean isRegistred() {
        return isRegistered;
    }

    // 获得注册信息
    public String getRegistrationMessage() {
        return "";
    }

    // 获得注册url地址
    public String getRegistrationURL() {
        return "";
    }

    public static final Interval ONE_MINUTE = new OneMinuteInterval();          // 一分钟
    public static final Interval FIVE_MINUTE = new FiveMinuteInterval();        // 五分钟
    public static final Interval FIFTEEN_MINUTE = new FifteenMinuteInterval();  // 十五分钟
    public static final Interval THIRTY_MINUTE = new ThirtyMinuteInterval();    // 三十分钟
    public static final Interval SIXTY_MINUTE = new SixtyMinuteInterval();      // 六十分钟
    public static final Interval DAILY = new DailyInterval();                   // 每日
    public static final Interval WEEKLY = new WeeklyInterval();                 // 每周
    public static final Interval MONTHLY = new MonthlyInterval();               // 每月
    // 盘中间隔：一分钟、五分钟、十五分钟、三十分钟、六十分钟
    public static final Interval[] INTRA_DAY_INTERVALS = {ONE_MINUTE, FIVE_MINUTE, FIFTEEN_MINUTE, THIRTY_MINUTE, SIXTY_MINUTE};
    // 间隔：每日、每周、每月
    public static final Interval[] INTERVALS = {DAILY, WEEKLY, MONTHLY};

    protected String name;                              // 名称
    protected Exchange[] exchanges;                     // 交易所
    protected boolean supportsIntraDay = false;         // 是否支持盘中
    protected boolean supportsCustomInterval = false;   // 是否支持自定义间隔
    protected String stocksPath;                        // 股票路径
    protected String datasetsPath;                      // 数据集路径
    protected boolean initializeFlag = true;            // 是否初始化
    protected boolean needsRegistration = false;        // 是否需要注册
    protected boolean isRegistered = false;             // 是否已注册

}
