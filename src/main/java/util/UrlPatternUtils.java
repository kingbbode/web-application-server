package util;

/**
 * Created by YG-MAC on 2016. 12. 15..
 */
public class UrlPatternUtils {
    public static String resolve(String url){
        return ("/".equals(url) || url == null)?"/index.html":url;
    }

    public static String getParameters(String url){
        if(url != null){
            return null;
        }
        if(!url.contains("?")){
            return null;
        }
        return url.split("?")[1];
    }
}
