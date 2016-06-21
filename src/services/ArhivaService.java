package services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.InputStreamHandle;

import database.Config;
import model.Gradjanin;
import rs.ac.uns.ftn.xws.util.Authenticate;
import util.AuthenticateOdbornik;
import xml.encryption.DecryptKEK;
import xml.signature.SignDocument;

@Path("/amandman")
public class ArhivaService {

	private static Logger log = Logger.getLogger(ArhivaService.class);

	@Context
	private ServletContext context;

	@Context
	private HttpServletRequest request;

	@POST
	@Path("/arhiviraj")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public void arhivirajAmadman(Document enkriptovaniDoc, String id){

		 DecryptKEK decClass = new DecryptKEK();




		 Document deDoc = decClass.decrypt(enkriptovaniDoc, decClass.readPrivateKey());
		 InputStream stream = new ByteArrayInputStream(saveDocument(deDoc).getBytes(StandardCharsets.UTF_8));
		 System.out.println("dekriptovan-------------------------------");
		 insertDocument("/arhiva/"+id + ".xml", stream);

		 System.out.println(saveDocument(deDoc));
		 System.out.println("dekriptovan-------------------------------");


	}
	public static void insertDocument(String path, InputStream in) {

		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.user, Config.password, Config.authType);

		// create a manager for XML documents
		XMLDocumentManager docMgr = client.newXMLDocumentManager();

		// create a handle on the content
		InputStreamHandle handle = new InputStreamHandle(in);

		// write the document content
		docMgr.write(path, handle);

		log.info(path + " content");

		// release the client
		client.release();

	}

	/**
	 * Snima DOM u XML fajl
	 */
	private String saveDocument(Document doc) {
		String xmlFile = null;
		try {
			StringWriter writer = new StringWriter();
			StreamResult result = new StreamResult(writer);

			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();

			DOMSource source = new DOMSource(doc);
			// StreamResult result = new StreamResult(xmlFile);

			transformer.transform(source, result);
			xmlFile = writer.toString();

		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (TransformerFactoryConfigurationError e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return xmlFile;
	}




}
