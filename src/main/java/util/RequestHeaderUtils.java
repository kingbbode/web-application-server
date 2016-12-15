package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YG-MAC on 2016. 12. 15..
 */
public class RequestHeaderUtils {

    public static Map<String, String> readHeader(InputStream in) throws IOException {
        BufferedReader din = new BufferedReader(new InputStreamReader(in));
        String line;
        Map<String, String> header = new HashMap<>();
        while(!"".equals(line = din.readLine()) && line != null){
            parseHeader(header, line);
        }

        return header;
    }

    private static void parseHeader(Map<String, String> header, String line){
        String[] tokens = line.split(" ");
        if(tokens.length < 2){
            return;
        }
        switch (tokens[0]){
            case "GET":
                header.put("url", tokens[1]);
                break;
            case "HOST:":
                header.put("Host", tokens[1]);
                break;
            case "Connection:":
                header.put("connection", tokens[1]);
                break;
            case "Accept:":
                header.put("accept", tokens[1]);
                break;
        }
    }

    private static void setUrl(){

    }
}
