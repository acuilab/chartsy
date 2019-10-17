package org.chartsy.main.managers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import org.chartsy.main.data.DataProvider;
import org.openide.util.Lookup;

/**
 * 数据提供者管理器
 * @author viorel.gheba
 */
public class DataProviderManager {

    // 单例
    private static DataProviderManager instance;
    // 被忽略的数据提供者，在下面的静态代码块中初始化
    private static List<String> ignored;
    private LinkedHashMap<String, DataProvider> dataProviders;
    private boolean updated = false;

    public static DataProviderManager getDefault() {
        if (instance == null) {
            instance = new DataProviderManager();
        }
        return instance;
    }

    private DataProviderManager() {
        // 加载数据提供者并排序
        dataProviders = new LinkedHashMap<String, DataProvider>();
        Collection<? extends DataProvider> list = Lookup.getDefault().lookupAll(DataProvider.class);
        for (DataProvider dp : list) {
            if (!ignored.contains(dp.getName())) {
                dp.initialize();
                dataProviders.put(dp.getName(), dp);
            }
        }

        sort();
    }

    // 对数据提供者按拼音排序
    private void sort() {
        List<String> mapKeys = new ArrayList<String>(dataProviders.keySet());
        Collections.sort(mapKeys);

        LinkedHashMap<String, DataProvider> someMap = new LinkedHashMap<String, DataProvider>();
        for (int i = 0; i < mapKeys.size(); i++) {
            someMap.put(mapKeys.get(i), dataProviders.get(mapKeys.get(i)));
        }
        dataProviders = someMap;
    }

    public DataProvider getDataProvider(String key) {
        return dataProviders.get(key);
    }

    public List<String> getDataProviders() {
        List<String> list = new ArrayList<String>(dataProviders.keySet());
        Collections.sort(list);
        return list;
    }

    static {
        ignored = new ArrayList<String>();
        ignored.add("Yahoo");
        ignored.add("ForexFeed");
        ignored.add("MrSwing");
    }
}
