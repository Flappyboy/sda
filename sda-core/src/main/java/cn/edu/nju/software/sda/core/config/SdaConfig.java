package cn.edu.nju.software.sda.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class SdaConfig {
    public static Map<String, String> propertiesMap = new HashMap<>();

    public static final String PATH = "sda.path";

    private static void processProperties(Properties props) {
        propertiesMap = new HashMap<>();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            try {
                // PropertiesLoaderUtils的默认编码是ISO-8859-1,在这里转码一下
                propertiesMap.put(keyStr, new String(props.getProperty(keyStr).getBytes("ISO-8859-1"), "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (java.lang.Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadAllProperties(String propertyFileName) {
        try {
            ClassLoader classLoaderToUse = getDefaultClassLoader();
            Enumeration<URL> urls = (classLoaderToUse != null ? classLoaderToUse.getResources(propertyFileName) :
                    ClassLoader.getSystemResources(propertyFileName));
            Properties props = new Properties();
            while (urls.hasMoreElements()) {
                URL url = urls.nextElement();
                URLConnection con = url.openConnection();
                InputStream is = con.getInputStream();
                try {
                    if (propertyFileName.endsWith(".xml")) {
                        props.loadFromXML(is);
                    }
                    else {
                        props.load(is);
                    }
                }
                finally {
                    is.close();
                }
            }
            processProperties(props);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        }
        catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = SdaConfig.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                }
                catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the sourceNode can live with null...
                }
            }
        }
        return cl;
    }

    public static String getProperty(String name) {
        return propertiesMap.get(name);
    }

    public static Map<String, String> getAllProperty() {
        return propertiesMap;
    }
}
