package cn.edu.nju.software.sda.app.utils;

public class SqlUtil {
    public static final String MULTI_CHAR = "%";

    public static String like(String str){
        if (str == null){
            return null;
        }
        return MULTI_CHAR + str + MULTI_CHAR;
    }

    public static String startLike(String str){
        if (str == null){
            return null;
        }
        return str + MULTI_CHAR;
    }
    public static String endLike(String str){
        if (str == null){
            return null;
        }
        return MULTI_CHAR + str;
    }
}
