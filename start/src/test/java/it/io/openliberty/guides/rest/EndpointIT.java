package it.io.openliberty.guides.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Properties;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class EndpointIT {

	private static final String OS_NAME = "os.name";
	private static final String CONTEXT_ROOT = "context.root";
	private static final String HTTP_PORT = "http.port";
	private static Jsonb JSONB; 
	
	@BeforeAll
	public void configure() {
		JSONB = JsonbBuilder.create();
	}
	
	@Test
	public void testGetProperties() {
		
		String port = System.getProperty(HTTP_PORT);
		String context = System.getProperty(CONTEXT_ROOT);
		
		String url = String.format("http://localhost:%s/%s/", port , context );
		
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target( url + "System/properties" );
		Response response = target.request().get();
		
		assertEquals(
				Response.Status.OK.getStatusCode(), 
				response.getStatus() , 
				"Incorrect response code from " + url );
		
		String json = response.readEntity(String.class);
		Properties systemProperties = JSONB.fromJson( json , Properties.class );
		
		assertEquals( 
				System.getProperty(OS_NAME), 
				systemProperties.getProperty(OS_NAME) , 
				"The system property for the local and remote JVM should match." );
		
		response.close();
		
		
	}
	
}
