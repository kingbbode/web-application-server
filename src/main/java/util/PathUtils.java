package util;

/**
 * Created by YG-MAC on 2016. 12. 18..
 */
public class PathUtils {
    public static final String PREFIX = "webapp";
    public static final String NOT_FOUND = "/404.html";
    public static final String INDEX =  "/index.html";
    public static final String LOGIN = "/login.html";

    public static String resolve(String path){
        if(path == null || "/".equals(path)){
            return INDEX;
        }
        return path;
    }
}
