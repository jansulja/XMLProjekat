package services;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;

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

import org.apache.log4j.Logger;

import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.InputStreamHandle;

import database.Config;
import model.Akt;
import model.Gradjanin;
import session.AktDaoLocal;

@Path("/akt")
public class AktService {

	
	private static Logger log = Logger.getLogger(Gradjanin.class);
	
	@EJB
	AktDaoLocal aktDao;

	
	@POST
	@Path("/new")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public String predloziAkt(String akt){
		
		InputStream stream = new ByteArrayInputStream(akt.getBytes(StandardCharsets.UTF_8));
		
		log.info("REST String: " + akt);
		
		Random rand = new Random();
		
		
		insertDocument("/akti/"+(String.valueOf(rand.nextInt(10000))) + ".xml", stream);
		
		return "ok";
		
		
	}
	
	
	@GET 
    @Produces(MediaType.APPLICATION_JSON)
	public List<Akt> findByAll() {
		List<Akt> retVal = null;
		try {
			retVal = aktDao.findAll();
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return retVal;
    }
	public InputStream createXML (Akt akt){
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		
		try {
			System.out.println("JAXB unmarshalling/marshalling.\n");
			// Definiše se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
			JAXBContext context = JAXBContext.newInstance("model");
			// Marshaller je objekat zadužen za konverziju iz objektnog u XML model
			Marshaller marshaller = context.createMarshaller();
			
			// Podešavanje marshaller-a
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			
			// Umesto System.out-a, može se koristiti FileOutputStream
			marshaller.marshal(akt, out);
			
		} catch (JAXBException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}
		
		
		InputStream in = new ByteArrayInputStream(out.toByteArray());
		return in;
	}
	
	
	@GET 
    @Produces(MediaType.TEXT_HTML)
	@Path("/add")
    public String create() {
		
//		log.info("example:" );
//
//		// create the client
//		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.user, Config.password, Config.authType);
//		
//		// acquire the content
////		InputStream docStream = AktService.class.getResourceAsStream(
////			"C:/Users/Shuky/workspaceee/xws/Sabloni/aktPrimer1.xml");
//		InputStream docStream = null;
//		try {
//			docStream = new FileInputStream("C:/Users/Shuky/workspaceee/xws/Sabloni/aktPrimer1.xml");
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		// create a manager for XML documents
//		XMLDocumentManager docMgr = client.newXMLDocumentManager();
//
//		// create a handle on the content
//		InputStreamHandle handle = new InputStreamHandle(docStream);
//
//		// write the document content
//		docMgr.write("/Sabloni/aktPrimer2.xml", handle);
//
//		log.info("/Sabloni/aktPrimer1.xml content");
//
//		// release the client
//		client.release();
		
		
		Akt a1 = Akt.getDummy();
		insertDocument("/akti/"+a1.getId().toString()+".xml", createXML(a1));
		
		
		return "<html><h1>Dodat akt "+ a1.getId()  +" </h1></html>";
    }
	
	public void insertDocument(String path, InputStream in){
		
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.user, Config.password, Config.authType);
		
		// create a manager for XML documents
		XMLDocumentManager docMgr = client.newXMLDocumentManager();

		// create a handle on the content
		InputStreamHandle handle = new InputStreamHandle(in);

		// write the document content
		docMgr.write(path, handle);

		log.info(path+" content");

		// release the client
		client.release();
	
	}
	
}
