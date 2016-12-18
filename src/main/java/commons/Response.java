package commons;

import enums.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Map;

/**
 * Created by YG-MAC on 2016. 12. 18..
 */
public class Response extends Header {
    private static final Logger log = LoggerFactory.getLogger(Request.class);
    private DataOutputStream dos;

    public Response(Request request, OutputStream out) {
        super(request);
        this.dos = new DataOutputStream(out);
    }

    public void forword(String url) throws IOException {
        HttpStatusCode code = HttpStatusCode.OK;
        File file = new File("webapp" + url);
        if (file == null || !file.isFile()) {
            code = HttpStatusCode.NOT_FOUND;
            file = new File("webapp/404.html");
        }
        log.debug("response url : {}", file.toPath());
        byte[] body = Files.readAllBytes(file.toPath());
        getHeaders().put("Content-Length", body.length + "");
        getHeaders().put("Content-Type", getHeaders().get("Accept") + ";charset=utf-8");
        responseHeader(code);
        responseBody(body);
    }

    public void redirect(String url, boolean isLogin) {
        this.getHeaders().put("Location", url);
        if (isLogin) {
            this.getHeaders().put("Set-Cookie", "true");
        }
        responseHeader(HttpStatusCode.FOUND);
    }

    private void putHeaderData() throws IOException {
        for (Map.Entry entry : this.getHeaders().entrySet()) {
            dos.writeBytes(entry.getKey() + ": " + entry.getValue() + "\r\n");
        }
    }

    private void responseHeader(HttpStatusCode code) {
        try {
            dos.writeBytes("HTTP/1.1" + code.getMessage() + "\r\n");
            putHeaderData();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.writeBytes("\r\n");
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
