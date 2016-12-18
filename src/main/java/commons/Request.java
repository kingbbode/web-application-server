package commons;

import enums.RequestType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Created by YG-MAC on 2016. 12. 18..
 */
public class Request extends Header{
    private static final Logger log = LoggerFactory.getLogger(Request.class);
    public Request(String line, BufferedReader in) throws IOException {
        super(in);
        if(line == null){
            return;
        }
        log.debug("Request Method - {}",line);
        String[] data = line.split(" ");
        this.type = RequestType.valueOf(data[0]);
        this.url = new URL(data[1]);
    }

    private URL url;
    private RequestType type;

    public URL getUrl() {
        return url;
    }

    public RequestType getType() {
        return type;
    }
}
