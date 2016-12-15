package util;

/**
 * Created by YG-MAC on 2016. 12. 15..
 */
public class UrlPatternUtils {
    private static final String[] emptyUrls = new String[]{null,null};

    public static String resolve(String url){
        return ("/".equals(url) || url == null)?"/index.html":url;
    }

    public static String[] parse(String url){
        if(url == null){
            return emptyUrls;
        }
        int index = url.indexOf("?");
        if(index < 0){
            return new String[]{url,null};
        }
        String requestPath = url.substring(0, index);
        String queryString = url.substring(index+1);

        return new String[]{requestPath,queryString};
    }
}
