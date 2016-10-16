package ch.ethz.inf.vs.a2.sensor;

public abstract class SensorFactory {

	public static Sensor getInstance(Type type) {
		switch (type) {
		case RAW_HTTP:
			// return Sensor implementation using a raw HTTP request
			return new RawHttpSensor("vslab.inf.ethz.ch", 8081, "/sunspots/Spot2/sensors/temperature");
		case TEXT:
			// return Sensor implementation using text/html representation
			return new TextSensor("http://vslab.inf.ethz.ch:8081/sunspots/Spot1/sensors/temperature");
		case JSON:
			// return Sensor implementation using application/json representation
			return new JsonSensor("http://vslab.inf.ethz.ch:8081/sunspots/Spot1/sensors/temperature");
		case XML:
            // build XML string
            StringBuilder sb = new StringBuilder ();
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">")
                    .append("<S:Header/>")
                    .append("<S:Body>")
                    .append("<ns2:getSpot xmlns:ns2=\"http://webservices.vslecture.vs.inf.ethz.ch/\">")
                    .append("<id>Spot3</id>")
                    .append("</ns2:getSpot>")
                    .append("</S:Body>")
                    .append("</S:Envelope>");
            String xml = sb.toString();

            // return Sensor implementation using application/xml representation
            return new XmlSensor("http://vslab.inf.ethz.ch:8080/SunSPOTWebServices/SunSPOTWebservice",
                    xml,
                    "temperature");
		case SOAP:
			// return Sensor implementation using a SOAPObject
			return new SoapSensor("http://vslab.inf.ethz.ch:8080/SunSPOTWebServices/SunSPOTWebservice",
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