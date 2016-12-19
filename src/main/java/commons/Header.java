package commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YG-MAC on 2016. 12. 18..
 */
public class Header {
    private static final Logger log = LoggerFactory.getLogger(Header.class);
    private Map<String, String> headers;
    private Map<String, String> body;

    public Header(BufferedReader in) throws IOException {
        String line;
        headers = new HashMap();
        while(!"".equals(line = in.readLine()) && line != null){
            addHeaderParameter(line);
        }
        String length = this.headers.get("Content-Length");
        if(length == null){
            return;
        }
        addBodyParameter(IOUtils.readData(in, Integer.parseInt(length)));
    }

    public Header(Header header){
        this.headers = header.headers;
        this.body = header.body;
    }

    private void addHeaderParameter(String line){
        log.debug("HeaderHeaders - {}", line);
        HttpRequestUtils.Pair pair = HttpRequestUtils.parseHeader(line);
        this.headers.put(pair.getKey().trim(), pair.getValue().trim());
    }

    private void addBodyParameter(String content) throws IOException {
        log.debug("HeaderHeaders - {}", content);
        this.body = HttpRequestUtils.parseQueryString(content);
    }


    public Map<String, String> getHeaders() {
        return headers;
    }

    protected Map<String, String> getBody() {
        return body;
    }
}
