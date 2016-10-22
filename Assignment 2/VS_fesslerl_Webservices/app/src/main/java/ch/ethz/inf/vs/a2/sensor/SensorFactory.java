package ch.ethz.inf.vs.a2.sensor;

import static ch.ethz.inf.vs.a2.http.RemoteServerConfiguration.HOST;
import static ch.ethz.inf.vs.a2.http.RemoteServerConfiguration.REST_PORT;
import static ch.ethz.inf.vs.a2.http.RemoteServerConfiguration.SOAP_PORT;
import static ch.ethz.inf.vs.a2.http.RemoteServerConfiguration.SPOT1;
import static ch.ethz.inf.vs.a2.http.RemoteServerConfiguration.XML;
import static ch.ethz.inf.vs.a2.http.RemoteServerConfiguration.WS;

public abstract class SensorFactory {

	public static Sensor getInstance(Type type) {
		switch (type) {
		case RAW_HTTP:
			// return Sensor implementation using a raw HTTP request
			return new RawHttpSensor(HOST, REST_PORT, SPOT1 + "temperature");
		case TEXT:
			// return Sensor implementation using text/html representation
			return new TextSensor("http://" + HOST + ":" + REST_PORT + SPOT1 + "temperature");
		case JSON:
			// return Sensor implementation using application/json representation
			return new JsonSensor("http://" + HOST + ":" + REST_PORT + SPOT1 + "temperature");
		case XML:
            // return Sensor implementation using application/xml representation
            return new XmlSensor("http://" + HOST + ":" + SOAP_PORT + WS,
                    XML,
                    "temperature");
		case SOAP:
			// return Sensor implementation using a SOAPObject
			return new SoapSensor("http://" + HOST + ":" + SOAP_PORT + WS,
                    "",
                    "http://webservices.vslecture.vs.inf.ethz.ch/",
                    "getSpot",
                    "id",
                    "Spot3",
                    "temperature");
		default:
			return null;
		}
	}
	
	public enum Type {
		RAW_HTTP, TEXT, JSON, XML, SOAP;
	}
}