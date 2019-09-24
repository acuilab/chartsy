package org.chartsy.xueqiu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import org.apache.commons.httpclient.Cookie;
import org.chartsy.main.data.DataItem;
import org.chartsy.main.data.DataProvider;
import org.chartsy.main.data.Dataset;
import org.chartsy.main.data.Stock;
import org.chartsy.main.data.StockSet;
import org.chartsy.main.exceptions.InvalidStockException;
import org.chartsy.main.exceptions.RegistrationException;
import org.chartsy.main.exceptions.StockNotFoundException;
import org.chartsy.main.intervals.Interval;
import org.chartsy.main.managers.ProxyManager;
import org.chartsy.main.utils.SerialVersion;
import org.openide.util.NbBundle;

/**
 *
 * @author viorel.gheba
 */
public class XueQiu extends DataProvider {

    private static final long serialVersionUID = SerialVersion.APPVERSION;

    public XueQiu() {
        super(NbBundle.getBundle(XueQiu.class));
    }

    @Override
    public int getRefreshInterval() {
        return 5;
    }

    @Override
    protected String fetchCompanyName(String symbol)
            throws InvalidStockException, StockNotFoundException, RegistrationException, IOException {
        System.out.println("fetchCompanyName=" + symbol);
        String uri = "https://xueqiu.com/stock/f10/compinfo.json?symbol=" + symbol;
        String res = ProxyManager.getDefault().inputStringGET("https://xueqiu.com", "");
//        System.out.println("res=" + res);
        
        // 获得登陆后的 Cookie
        Cookie[] cookies = ProxyManager.getDefault().httpClient().getState().getCookies();
        StringBuffer tmpcookies = new StringBuffer();
        for (Cookie c : cookies) {
            tmpcookies.append(c.toString() + ";");
            System.out.println("cookies = "+c.toString());
        }
        
//        System.out.println("uri=" + uri);
//        String response = ProxyManager.getDefault().inputStringGET(uri, tmpcookies.toString());
//        System.out.println("response=" + response);
//        TQCompInfo compInfo = JSON.parseObject(response, TQCompInfo.class);
//        System.out.println("fetchCompanyName=" + compInfo.getCompname());
//        return compInfo.getCompname();

        return "xxx";
    }

    @Override
    protected Dataset fetchDataForFavorites(Stock stock) throws IOException, ParseException {
        System.out.println("fetchDataForFavorites");
        synchronized ((stock.toString() + "-" + DAILY.getTimeParam()).intern()) {

            return new Dataset();
        }
    }

    @Override
    protected Dataset fetchData(Stock stock, Interval interval)
            throws IOException, ParseException {
        System.out.println("fetchData");
        synchronized ((stock.toString() + "-" + interval.getTimeParam()).intern()) {
            Dataset result = null;
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

            String uri = "https://xueqiu.com/stock/forchartk/stocklist.json?symbol=" + stock.getSymbol() + "&period=1day";
            String response = ProxyManager.getDefault().inputStringGET(uri, "");

            JSONObject root = JSON.parseObject(response);
            JSONArray arr = root.getJSONArray("chartlist");
            List<DataItem> items = arr.toJavaList(DataItem.class);
            Collections.sort(items);
            result = new Dataset(items);
            System.out.println("fetchData");
            return result;
        }
        
    }

    @Override
    protected DataItem fetchLastDataItem(Stock stock, Interval interval)
            throws IOException, ParseException {
        System.out.println("fetchLastDataItem");
        synchronized ((stock.toString() + "-" + interval.getTimeParam()).intern()) {
            //long lastTime = dataset.getLastTime();
            return new DataItem(System.currentTimeMillis(), 15d);
        }
    }

    @Override
    public StockSet fetchAutocomplete(String text)
            throws IOException {
        System.out.println("fetchAutocomplete");
        return new StockSet();
    }
}
