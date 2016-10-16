package ch.ethz.inf.vs.a2.sensor;

public abstract class SensorFactory {
	public static Sensor getInstance(Type type) {
		switch (type) {
		case RAW_HTTP:
			// return Sensor implementation using a raw HTTP request
			return new RawHttpSensor("vslab.inf.ethz.ch",8081,"/sunspots/Spot2/sensors/temperature");
		case TEXT:
			// return Sensor implementation using text/html representation
			return new TextSensor("http://vslab.inf.ethz.ch:8081/sunspots/Spot1/sensors/temperature");
		case JSON:
			// return Sensor implementation using application/json representation
			return new JsonSensor("http://vslab.inf.ethz.ch:8081/sunspots/Spot1/sensors/temperature");
		/*case XML:
			// return Sensor implementation using application/xml representation
			return new XmlSensor();
		case SOAP:
			// return Sensor implementation using a SOAPObject
			return new SoapSensor();*/
		default:
			return null;
		}
	}
	
	public enum Type {
		RAW_HTTP, TEXT, JSON, XML, SOAP;
	}
}