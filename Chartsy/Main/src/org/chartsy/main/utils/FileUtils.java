package org.chartsy.main.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileUtil;

/**
 * 文件工具类
 * @author viorel.gheba
 */
public final class FileUtils {

    protected FileUtils() {
    }

    // 本地文件夹：用户家目录/Chartsy
    public static String LocalFolder() {
        String result = System.getProperty("user.home") + File.separator + "Chartsy";
        createFolder(result);
        return result;
    }

    // 日志文件夹：本地文件夹/log
    public static String LogFolder() {
        String result = LocalFolder() + File.separator + "log";
        createFolder(result);
        return result;
    }

    // 日志文件：日志文件夹/log.txt
    public static String LogFile() {
        String result = LogFolder() + File.separator + "log.txt";
        createFile(result);
        return result;
    }

    // 错误文件：日志文件夹/err.txt
    public static String ErrorFile() {
        String result = LogFolder() + File.separator + "err.txt";
        createFile(result);
        return result;
    }

    // 设置文件夹：本地文件夹/settings
    public static String SettingsFolder() {
        String result = LocalFolder() + File.separator + "settings";
        createFolder(result);
        return result;
    }

    // 用户文件：设置文件夹/user.xml
    public static String UserFile() {
        String result = SettingsFolder() + File.separator + "user.xml";
        return result;
    }

    // 注册文件：设置文件夹/registred.xml
    public static String RegisterFile() {
        String result = SettingsFolder() + File.separator + "registred.xml";
        return result;
    }

    // 间隔文件夹：本地文件夹/intervals
    public static String intervalsFolder() {
        String result = LocalFolder() + File.separator + "intervals";
        createFolder(result);
        return result;
    }

    // 数据提供者间隔文件夹：间隔文件夹/具体的数据提供者
    public static String intervalFolder(String dataProvider) {
        String folder = intervalsFolder() + File.separator + dataProvider;
        return folder;
    }

    // 间隔路径：间隔文件夹/具体的数据提供者/具体间隔.properties
    public static String intervalPath(String dataProvider, String interval) {
        String folder = intervalsFolder() + File.separator + dataProvider;
        createFolder(folder);
        String path = folder + File.separator + interval + ".properties";
        return path;
    }

    // 缓存文件夹：本地文件夹/cache
    public static String cacheFolder() {
        String result = LocalFolder() + File.separator + "cache";
        createFolder(result);
        return result;
    }

    // 缓存股票文件夹：缓存文件夹/stocks
    public static String cacheStocksFolder() {
        String result = cacheFolder() + File.separator + "stocks";
        createFolder(result);
        return result;
    }

    // 缓存数据集文件夹：缓存文件夹/datasets
    public static String cacheDatasetsFolder() {
        String result = cacheFolder() + File.separator + "datasets";
        createFolder(result);
        return result;
    }

    // 缓存图表文件夹：缓存文件夹/charts
    public static String cacheChartsFolder() {
        String result = cacheFolder() + File.separator + "charts";
        createFolder(result);
        return result;
    }

    // 指定id的缓存图表文件夹：缓存图表文件夹/具体id
    public static String cacheChartFolder(String id) {
        String result = cacheChartsFolder() + File.separator + id;
        createFolder(result);
        return result;
    }

    // 缓存历史文件夹：指定id的缓存图表文件夹/history
    public static String cacheHistoryFolder(String id) {
        String result = cacheChartFolder(id) + File.separator + "history";
        createFolder(result);
        return result;
    }

    // 缓存文件：缓存文件夹/具体文件名
    public static String cacheFile(String file) throws IOException {
        String filePath = cacheFolder() + File.separator + file;
        FileObject object = FileUtil.createData(new File(filePath));
        return object.getPath();
    }
    
    // 缓存文件路径：缓存文件夹/具体文件名
    public static String cacheFilePath(String file) throws IOException {
        String filePath = cacheFolder() + File.separator + file;
        return filePath;
    }

    // 哈希的缓存文件路径
    public static String hashedCacheFilePath(String folder, String fileName) {
        String result = folder + File.separator + getStringHash(fileName) + ".properties";
        return result;
    }

    // 哈希的缓存文件
    public static File hashedCacheFile(String folder, String fileName) {
        String path = hashedCacheFilePath(folder, fileName);
        File file = new File(path);
        return file;
    }

    // 缓存文件对象
    public static FileObject cacheFileObject(String fileName) throws IOException {
        return FileUtil.createData(new File(cacheFile(fileName)));
    }

    public static String getStringHash(String fileName) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            digest.reset();
            digest.update(fileName.getBytes());
            byte messageDigest[] = digest.digest();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++) {
                builder.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            String result = builder.toString();
            return result;

        } catch (NoSuchAlgorithmException ex) {
            return fileName;
        }
    }

    public static String getHistoryFolder() {
        String result = LocalFolder() + File.separator + "history";
        createFolder(result);
        return result;
    }

    /*public static String favoritesFolder()
    {
        String result = LocalFolder() + File.separator + "favorites";
        createFolder(result);
        return result;
    }

    public static String favoritesFile()
    {
        String result = favoritesFolder() + File.separator + "favorites.xml";
        createFile(result);
        return result;
    }*/
    public static File favoritesFile() {
        String result = LocalFolder() + File.separator + "favorites.xml";
        return new File(result);
    }

    public static String stockScanFolder() {
        String result = LocalFolder() + File.separator + "StockScanPRO";
        createFolder(result);
        return result;
    }

    public static File stockScanFile(String fileName) {
        String result = stockScanFolder() + File.separator + fileName;
        return new File(result);
    }

    public static String templatesFolder() {
        String result = LocalFolder() + File.separator + "templates";
        createFolder(result);
        return result;
    }

    public static File templatesFile(String fileName) {
        String result = templatesFolder() + File.separator + fileName;
        return new File(result);
    }

    public static boolean fileExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static void removeFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                FileObject fo = FileUtil.toFileObject(file);
                fo.delete();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void createFile(String path) {
        File f = new File(path);
        try {
            FileObject file = FileUtil.createData(f);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void createFolder(String path) {
        File dir = new File(path);
        try {
            FileObject folder = FileUtil.createFolder(dir);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void copyFile(String source, String destination) throws IOException {
        File sourceFile = new File(source);
        File destinationFile = new File(destination);
        copyFile(sourceFile, destinationFile);
    }

    public static void copyFile(File source, File destination) throws IOException {
        FileChannel in = null;
        FileChannel out = null;
        try {
            in = new FileInputStream(source).getChannel();
            out = new FileOutputStream(destination).getChannel();
            in.transferTo(0, in.size(), out);
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    public static String getFileName(String folder, final String path) {
        File dir = new File(folder);
        String[] list = dir.list(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.contains(path);
            }
        });

        if (list.length == 0) {
            return folder + File.separator + path;
        } else {
            return folder + File.separator + path + "(" + list.length + ")";
        }
    }

}
