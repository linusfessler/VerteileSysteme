package ch.ethz.inf.vs.a2.http;

import ch.ethz.inf.vs.a2.http.HttpRawRequest;

/**
 * Created by johannes on 15.10.16.
 */

public class HttpRawRequestImpl implements HttpRawRequest {
    @Override
    public String generateRequest(String host, int port, String path) {
        StringBuilder sb = new StringBuilder();
        sb.append("GET ").append(path).append(" HTTP/1.1").append("\r\n");
        sb.append("Host: ").append(host).append(":").append(port).append("\r\n");
        sb.append("Accept: ").append("text/html").append("\r\n");
        sb.append("Connection: close").append("\r\n");
        sb.append("\r\n");
        return sb.toString();
    }
}
