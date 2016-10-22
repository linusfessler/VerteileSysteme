package ch.ethz.inf.vs.a2.http;


/**
 * Collection of constant definitions for the remote server.
 * 
 * @author Leyna Sadamori
 *
 */
public final class RemoteServerConfiguration {

	public static final String HOST = "vslab.inf.ethz.ch";
	public static final int REST_PORT = 8081;
	public static final int SOAP_PORT = 8080;
	public static final String SPOT1 = "/sunspots/Spot1/sensors/";
	public static final String XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
			"<S:Header/>" +
            "<S:Body>" +
            "<ns2:getSpot xmlns:ns2=\"http://webservices.vslecture.vs.inf.ethz.ch/\">" +
            "<id>Spot3</id>" +
            "</ns2:getSpot>" +
            "</S:Body>" +
            "</S:Envelope>";
    public static final String WS =  "/SunSPOTWebServices/SunSPOTWebservice";

    private RemoteServerConfiguration() {}
}
