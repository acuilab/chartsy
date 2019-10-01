package org.chartsy.xueqiu;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    private static final String TOKEN = "37180e7ecc92162a3a30146fcad1de3101363ee2c9716dd9d88eda07";

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

        Map paramsMap = new HashMap();
        paramsMap.put("ts_code", "600848.SH");
//        paramsMap.put("start_date", "20180701");
//        paramsMap.put("end_date", "20180718");

        Map jsonMap = new HashMap();
        jsonMap.put("api_name", "namechange");
        jsonMap.put("token", TOKEN);
        jsonMap.put("params", paramsMap);
        jsonMap.put("fields", "");
        String json = JSON.toJSONString(jsonMap);
        System.out.println("json=" + json);
        String response = ProxyManager.getDefault().inputStringPOST("http://api.waditu.com", json);
        System.out.println("response=" + UicodeBackslashU.unicodeToCn(response));

        JSONObject root = JSON.parseObject(response);
        JSONObject data = root.getJSONObject("data");
        JSONArray items = data.getJSONArray("items");
        JSONArray item0 = items.getJSONArray(0);
        return (String)item0.get(1);
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
            

        Map paramsMap = new HashMap();
        paramsMap.put("ts_code", "600848.SH");
        paramsMap.put("start_date", "20180701");
//        paramsMap.put("end_date", "20190718");

        Map jsonMap = new HashMap();
        jsonMap.put("api_name", "daily");
        jsonMap.put("token", TOKEN);
        jsonMap.put("params", paramsMap);
        jsonMap.put("fields", "");
        String json = JSON.toJSONString(jsonMap);
        System.out.println("json=" + json);
        String response = ProxyManager.getDefault().inputStringPOST("http://api.waditu.com", json);
        System.out.println("response=" + UicodeBackslashU.unicodeToCn(response));

        JSONObject root = JSON.parseObject(response);
        JSONObject data = root.getJSONObject("data");
        JSONArray items = data.getJSONArray("items");
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        List<DataItem> list = new ArrayList<>();
        
        for(int i=0;i<items.size();i++){
            JSONArray item = items.getJSONArray(i);
            
            Date date = sdf.parse(item.getString(1));
            long time = date.getTime();
            double open = item.getDoubleValue(2);
            double high = item.getDoubleValue(3);
            double low = item.getDoubleValue(4);
            double close = item.getDoubleValue(5);
            double volume = item.getDoubleValue(9);
            
            list.add(new DataItem(time, open, high, low, close, volume));
        }
        
//        JSONArray item0 = items.getJSONArray(0);
//        return (String)item0.get(1);
          
          Collections.sort(list);
        return  new Dataset(list);
            
            
//            Dataset result = null;
//            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//
//            String uri = "https://xueqiu.com/stock/forchartk/stocklist.json?symbol=" + stock.getSymbol() + "&period=1day";
//            String response = ProxyManager.getDefault().inputStringGET(uri, "");
//
//            JSONObject root = JSON.parseObject(response);
//            JSONArray arr = root.getJSONArray("chartlist");
//            List<DataItem> items = arr.toJavaList(DataItem.class);
//            Collections.sort(items);
//            result = new Dataset(items);
//            System.out.println("fetchData");
//            return result;
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
