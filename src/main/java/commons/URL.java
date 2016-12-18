package commons;

import util.HttpRequestUtils;
import util.PathUtils;

import java.util.Map;

/**
 * Created by YG-MAC on 2016. 12. 18..
 */
public class URL {
    private String path;
    private Map<String, String> parameters;

    public URL(String url) {
        int index = url.indexOf("?");
        if(index < 0){
            this. path = PathUtils.resolve(url);
            return;
        }
        this.path = url.substring(0, index);
        this.parameters = HttpRequestUtils.parseQueryString(url.substring(index+1));
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
