package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ControllerUtils;
import util.HttpRequestUtils;
import util.RequestHeaderUtils;
import util.UrlPatternUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class RequestHandler extends Thread {
    private static final int PATH = 0;
    private static final int PARAMETER = 1;

    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
    private static final String root = "webapp";
    private Socket connection;
    private Map<String, Set<String>> controller;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
        this.controller = new HashMap<>();
        Set<String> parameter = new HashSet<>();
        parameter.add("userId");
        parameter.add("password");
        parameter.add("name");
        parameter.add("email");
        this.controller.put("/create", parameter);
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            Map<String, String> header = RequestHeaderUtils.readHeader(in);

            String[] urls = UrlPatternUtils.parse(header.get("url"));
            if(controller.containsKey(urls[PATH])){
                Map<String, String> params = HttpRequestUtils.parseQueryString(urls[PARAMETER]);
                if(ControllerUtils.isValidParameters(params, controller.get(urls[PATH]))){
                    new User(params);
                }
            }
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = Files.readAllBytes(new File(root + UrlPatternUtils.resolve(header.get("url"))).toPath());
            response200Header(dos, body.length, header.get("accept"));
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: " + contentType +";charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
