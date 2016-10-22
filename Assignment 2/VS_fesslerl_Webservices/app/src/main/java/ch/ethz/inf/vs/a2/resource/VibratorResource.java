package ch.ethz.inf.vs.a2.resource;

import android.os.Vibrator;

import java.net.URI;

import ch.ethz.inf.vs.a2.http.HttpResponse;
import ch.ethz.inf.vs.a2.http.ParsedRequest;

/**
 * Created by linus on 22.10.2016.
 */

public class VibratorResource extends Resource {

    private Vibrator vibrator;
    private String html;

    public VibratorResource(URI uri, Vibrator vibrator) {
        super(uri);
        this.vibrator = vibrator;

        StringBuilder sb = new StringBuilder();
        sb.append("<h3>").append("Vibrator").append("</h3>");
        sb.append("<p>").append("Enter the vibration pattern in milliseconds, ")
                .append("e.g.: <b>0 75 150 75 75 75 75 75 225 75 450 75 150 75</b>")
                .append(" corresponds to ")
                .append("<b>wait 0s, vibrate 0.075s, wait 0.15s, vibrate 0.075s, etc.</b>.")
                .append("</p>");
        sb.append("<form method='post'>");
        sb.append("<input type='text' name='pattern' value='0' pattern='[0-9 ]+' size='100'").append("</input>");
        sb.append("<input type='submit' value='Submit'>").append("</input>");
        sb.append("</form>");
        sb.append("<p>").append("<a href='/'>").append("Return to root").append("</a>").append("</p>");
        html = sb.toString();
    }

    @Override
    protected String get(ParsedRequest request) {
        return HttpResponse.generateResponse("200 OK", html);
    }

    @Override
    protected String post(ParsedRequest request) {
        if (!request.header.containsKey("Content-Length") || Integer.parseInt(request.header.get("Content-Length")) == 0)
            return HttpResponse.generateErrorResponse("406 Not Acceptable", "No content in POST request.");

        if (!request.content.containsKey("pattern"))
            return HttpResponse.generateErrorResponse("400 Bad Request", "No vibration pattern in POST request.");

        String patternString = request.content.get("pattern");
        String[] patternStrings = patternString.split("\\+");
        long[] pattern = new long[patternStrings.length];
        for (int i = 0; i < patternStrings.length; i++)
            pattern[i] = Long.parseLong(patternStrings[i]);

        if (vibrator == null)
            return HttpResponse.generateResponse("404 Not Found", html + "<p>Device has no vibrator.</p>");

        vibrator.vibrate(pattern, -1);

        return HttpResponse.generateResponse("200 OK", html + "<p>Device is now vibrating according to your pattern.</p>");
    }
}
