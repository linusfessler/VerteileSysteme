package ch.ethz.inf.vs.a2.http;

/**
 * Created by johannes on 19.10.16.
 */

public class HttpResponse {
    public static String generateHtmlResponse(String htmlBody){
        String html = "<html><body>" + htmlBody + "</body></html>";
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 200 OK").append("\r\n");
        sb.append("Content-Type: ").append("text/html").append("\r\n");
        sb.append("Content-Length: ").append(html.length()).append("\r\n");
        sb.append("\r\n");

        sb.append(html);
        return sb.toString();
    }

    public static String generateErrorResponse(String errorTxt){
        String html = "<html><body><h1>ERROR</h1><p>" + errorTxt + "</p><a href='/'>Return to root</a></body></html>";
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 400 Bad Request").append("\r\n");
        sb.append("Content-Type: ").append("text/html").append("\r\n");
        sb.append("Content-Length: ").append(html.length()).append("\r\n");
        sb.append("\r\n");

        sb.append(html);
        return sb.toString();
    }
}
