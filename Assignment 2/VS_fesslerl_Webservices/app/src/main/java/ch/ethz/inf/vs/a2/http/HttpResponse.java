package ch.ethz.inf.vs.a2.http;

/**
 * Created by johannes on 19.10.16.
 */

public class HttpResponse {

    public static String generateResponse(String successCode, String htmlBody){
        String html = "<html><body>" + htmlBody + "</body></html>";
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 " + successCode).append("\r\n");
        sb.append("Content-Type: ").append("text/html").append("\r\n");
        sb.append("Content-Length: ").append(html.length()).append("\r\n");
        sb.append("\r\n");

        sb.append(html);
        return sb.toString();
    }

    public static String generateErrorResponse(String errorCode, String errorTxt){
        String html = "<html><body><h1>" + errorCode + "</h1><p>" + errorTxt + "</p><a href='/'>Return to root</a></body></html>";
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 " + errorCode).append("\r\n");
        sb.append("Content-Type: ").append("text/html").append("\r\n");
        sb.append("Content-Length: ").append(html.length()).append("\r\n");
        sb.append("\r\n");

        sb.append(html);
        return sb.toString();
    }
}
