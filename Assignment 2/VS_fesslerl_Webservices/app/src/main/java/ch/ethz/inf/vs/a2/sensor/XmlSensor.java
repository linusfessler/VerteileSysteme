package ch.ethz.inf.vs.a2.sensor;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Created by linus on 16.10.2016.
 */

public class XmlSensor extends AbstractSensor {

    private final String LOGGING_TAG = "###XmlSensor";
    private final String urlString;
    private final String xml;
    private final String resultName;

    public XmlSensor(String url, String xml, String resultName) {
        this.urlString = url;
        this.xml = xml;
        this.resultName = resultName;
    }

    @Override
    public String executeRequest() throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // TODO: Content-Type: application/xml does not work => response 415
        connection.setRequestProperty("Content-Type", "text/xml");
        connection.setRequestProperty("Connection", "close");
        connection.setDoInput(true);
        connection.setDoOutput(true);

        // Send request
        DataOutputStream wr = new DataOutputStream (connection.getOutputStream());
        wr.writeBytes(xml);
        wr.flush();
        wr.close();

        // Get response
        InputStream is;
        Log.d(LOGGING_TAG, "response  = " + connection.getResponseCode());
        if (connection.getResponseCode() <= 400){
            is = connection.getInputStream();
        } else {
              // Error from server
            is = connection.getErrorStream();
        }

        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuilder response = new StringBuilder();
        while((line = in.readLine()) != null)
            response.append(line).append("\n\r");

        return response.toString();
    }

    @Override
    public double parseResponse(String response) {
        double res = 0.0;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            InputStream is = new ByteArrayInputStream(response.getBytes(StandardCharsets.UTF_8));
            xpp.setInput(is, null);

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (resultName.equals(xpp.getName())) {
                    res = Double.parseDouble(xpp.nextText());
                    break;
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            Log.d(LOGGING_TAG, "Error parsing XML-Response: " + e.getMessage());
        }

        return res;
    }
}
