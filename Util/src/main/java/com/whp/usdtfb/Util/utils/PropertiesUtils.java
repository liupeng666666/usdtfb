package com.whp.usdtfb.Util.utils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtils {
    /**
     * @param key
     * @return 根据key 获取配置文件内value 值
     */
    public static String KeyValue(String key) {
        Properties prop = new Properties();
        try {
            prop.load(PropertiesUtils.class.getClassLoader()
                    .getResourceAsStream("application.properties"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String value = prop.getProperty(key);
        return value;
    }
}
