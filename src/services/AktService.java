package services;


import java.io.File;
import java.io.InputStream;

import java.math.BigInteger;

import java.util.List;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.InputStreamHandle;

import database.Config;
import model.Akt;
import model.Clan;
import model.Gradjanin;

import model.Podtacka;
import model.Stav;
import model.Tacka;
import rs.ac.uns.ftn.xws.util.Authenticate;

import session.AktDaoLocal;
import util.AuthenticateGradjanin;

@Path("/akt")
public class AktService {

	
	private static Logger log = Logger.getLogger(Gradjanin.class);
	
	@EJB
	AktDaoLocal aktDao;

	@GET 
    @Produces(MediaType.APPLICATION_JSON)
	@AuthenticateGradjanin
	public List<Akt> findByAll() {
		List<Akt> retVal = null;
		try {
			retVal = aktDao.findAll();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return retVal;
    }
	public void createXML (Akt akt){
		try {
			System.out.println("JAXB unmarshalling/marshalling.\n");
			

			// Definiše se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
			JAXBContext context = JAXBContext.newInstance("model");
			

			// Marshaller je objekat zadužen za konverziju iz objektnog u XML model
			Marshaller marshaller = context.createMarshaller();
			
			// Podešavanje marshaller-a
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			// Umesto System.out-a, može se koristiti FileOutputStream
			marshaller.marshal(akt, System.out);
			
			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	
	
	@GET 
    @Produces(MediaType.TEXT_HTML)
	@Path("/add")
    public String create() {
		
		log.info("example:" );

		// create the client
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.user, Config.password, Config.authType);
		
		// acquire the content
		InputStream docStream = AktService.class.getClassLoader().getResourceAsStream(
			"./Sabloni/aktPrimer1.xml");

		
		
		// create a manager for XML documents
		XMLDocumentManager docMgr = client.newXMLDocumentManager();

		// create a handle on the content
		InputStreamHandle handle = new InputStreamHandle(docStream);

		// write the document content
		docMgr.write("./Sabloni/aktPrimer1.xml", handle);

		log.info("/Sabloni/aktPrimer1.xml content");

		// release the client
		client.release();
		
		return "<html><h1>Dodavanje akta</h1></html>";
    }
	
}
