package ch.ethz.inf.vs.a2.sensor;

import android.app.ExpandableListActivity;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by linus on 16.10.2016.
 */

public class SoapSensor extends AbstractSensor {

    private final String LOGGING_TAG = "###SoapSensor";
    private final String urlString;
    private final String soapAction;
    private final String namespace;
    private final String methodName;
    private final String parameterName;
    private final String parameterValue;
    private final String resultName;

    public SoapSensor(String url, String soapAction, String namespace, String methodName,
                      String parameterName, String parameterValue, String resultName) {
        this.urlString = url;
        this.soapAction = soapAction;
        this.namespace = namespace;
        this.methodName = methodName;
        this.parameterName = parameterName;
        this.parameterValue = parameterValue;
        this.resultName = resultName;
    }

    @Override
    public String executeRequest() throws Exception {
        SoapObject request = new SoapObject(namespace, methodName);
        request.addProperty(parameterName, parameterValue);
        // SoapEnvelope.VER12 does not work => response 500
        SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapEnvelope.setOutputSoapObject(request);

        String response;
        try {
            HttpTransportSE transport = new HttpTransportSE(urlString);
            transport.debug = true;
            transport.call(soapAction, soapEnvelope);
            response = ((SoapObject) soapEnvelope.getResponse()).getPropertyAsString(resultName);
            Log.d(LOGGING_TAG, response);
        } catch (Exception e) {
            response = e.getMessage();
            Log.d(LOGGING_TAG, e.getMessage());
        }

        return response;
    }

    @Override
    public double parseResponse(String response) {
        try {
            return Double.parseDouble(response);
        } catch (Exception e) {
            return 0.0;
        }
    }
}
