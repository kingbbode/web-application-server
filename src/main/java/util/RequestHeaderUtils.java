package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by YG-MAC on 2016. 12. 15..
 */
public class RequestHeaderUtils {

    public static Map<String, String> readHeader(BufferedReader in) throws IOException {
        String line;
        Map<String, String> header = new HashMap<>();
        while(!"".equals(line = in.readLine()) && line != null){
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
                header.put("type", "GET");
                header.put("url", tokens[1]);
                break;
            case "POST":
                header.put("type", "POST");
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
            case "Content-Length:":
                header.put("length", tokens[1]);
                break;
        }
    }
}
