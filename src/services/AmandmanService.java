package services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.FileHandle;
import com.marklogic.client.io.JacksonHandle;
import com.marklogic.client.semantics.GraphManager;
import com.marklogic.client.semantics.RDFMimeTypes;
import com.marklogic.client.semantics.SPARQLMimeTypes;
import com.marklogic.client.semantics.SPARQLQueryDefinition;
import com.marklogic.client.semantics.SPARQLQueryManager;

import database.Config;
import model.Akt;
import model.Gradjanin;
import model.Odbornik;
import model.amandman.Amandman;
import model.amandman.AmandmanDodavanje;
import model.amandman.StatusAmandmana;
import model.metadata.AktMetaData;
import model.metadata.AmandmanMetaData;
import rs.ac.uns.ftn.xws.util.Authenticate;
import util.AuthenticateOdbornik;
import xml.rdf.MetadataExtractor;
import xml.rdf.RDFDataGenerator;
import xml.rdf.RDFDataType;
import xml.signature.SignDocument;

@Path("/amandman")
public class AmandmanService {

	private static Logger log = Logger.getLogger(AmandmanService.class);

	@Context
	private ServletContext context;

	@Context
	private HttpServletRequest request;

	@POST
	@Path("/new")
	@Consumes(MediaType.APPLICATION_JSON)
//	@Authenticate
//	@AuthenticateOdbornik
	public String predloziAmandman(String amandman){


		Random rand = new Random();
		int amandmanID = rand.nextInt(10000);

		AmandmanDodavanje am = new AmandmanDodavanje(amandman);

		Odbornik gr = (Odbornik) request.getSession().getAttribute("user");

		@SuppressWarnings("unused")
		Amandman a = xmlStringToObject(am.getAmandman());
		a.setAktId(am.getAktId());
		a.setOdbornik(gr.getEmail());
		a.setStatusAmandmana(StatusAmandmana.PREDLOZEN);
		a.setId(String.valueOf(amandmanID));
		a.setDatumPredlaganja(AktService.getXMLGregorianCalendarNow());

		@SuppressWarnings("unused")
		String amand = createXMLString(a);






		String signedAmandman = signXmlDocument(amand);

		InputStream stream = new ByteArrayInputStream(signedAmandman.getBytes(StandardCharsets.UTF_8));

		AktService.insertDocument("/amandmani/"+ a.getId() + ".xml", stream);

		String amandmanWithRDFMeta = addRDFDataToXML(signedAmandman,a.getId());


		insertMetadata(amandmanWithRDFMeta, a.getId());


		return "ok";


	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/list")
	public String izlistajAmandname() {

		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.user, Config.password,
				Config.authType);


		// Create a SPARQL query manager to query RDF datasets
		SPARQLQueryManager sparqlQueryManager = client.newSPARQLQueryManager();

				// Initialize DOM results handle
		DOMHandle domResultsHandle = new DOMHandle();


		// Initialize Jackson results handle
		JacksonHandle resultsHandle = new JacksonHandle();
		resultsHandle.setMimetype(SPARQLMimeTypes.SPARQL_JSON);

		// Initialize SPARQL query definition - retrieves all triples from RDF dataset
		SPARQLQueryDefinition query = sparqlQueryManager.newQueryDefinition("SELECT *  WHERE { ?s ?p ?o FILTER(REGEX(STR(?s),\"amandman\"))}");

		resultsHandle = sparqlQueryManager.executeSelect(query, resultsHandle);
		String json = handleResultsJSON(resultsHandle);

		return json;

	}


	@GET
	@Path("/list/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String pronadjiAmandmane(@PathParam("id") String id) {

		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.user, Config.password,
				Config.authType);


		// Create a SPARQL query manager to query RDF datasets
		SPARQLQueryManager sparqlQueryManager = client.newSPARQLQueryManager();

				// Initialize DOM results handle
		DOMHandle domResultsHandle = new DOMHandle();


		// Initialize Jackson results handle
		JacksonHandle resultsHandle = new JacksonHandle();
		resultsHandle.setMimetype(SPARQLMimeTypes.SPARQL_JSON);

		// Initialize SPARQL query definition - retrieves all triples from RDF dataset
		//SPARQLQueryDefinition query = sparqlQueryManager.newQueryDefinition("SELECT *  WHERE { ?s ?p ?o  FILTER(REGEX(STR(?p),\"akt_id\"))  FILTER(REGEX(STR(?o),\""+id+"\")) }");

		SPARQLQueryDefinition query = sparqlQueryManager.newQueryDefinition("SELECT ?s  WHERE { ?s ?p ?o  FILTER(REGEX(STR(?p),\"akt_id\"))  FILTER(REGEX(STR(?o),\""+id+"\")) }");

		

		resultsHandle = sparqlQueryManager.executeSelect(query, resultsHandle);
		//String json = handleResultsJSON(resultsHandle);

		ArrayList<String> amandmani = new ArrayList<String>();

		JsonNode tuples = resultsHandle.get().path("results").path("bindings");
		for ( JsonNode row : tuples ) {

			String subject = row.path("s").path("value").asText();
			amandmani.add(subject.split("amandman/")[1]);
		}

		String json = "[]";

		if(!amandmani.isEmpty()){


			String filter="FILTER(";
			for(int i=0;i<amandmani.size(); i++){

				filter += "REGEX(STR(?s),\""+ amandmani.get(i) +"\") ";
				if(i < amandmani.size()-1){

					filter += "|| ";

				}else{

					filter += ") ";


				}

			}


	//|| \"2016-06-16-3857\"
			resultsHandle = new JacksonHandle();
			query = sparqlQueryManager.newQueryDefinition("SELECT *  WHERE { ?s ?p ?o " + filter + "  }");
			resultsHandle = sparqlQueryManager.executeSelect(query, resultsHandle);


			json = handleResultsJSON(resultsHandle);


		}




		return json;




	}


	private String handleResultsJSON(JacksonHandle resultsHandle) {
		List<AmandmanMetaData> amandmani = new ArrayList<AmandmanMetaData>();

		JsonNode tuples = resultsHandle.get().path("results").path("bindings");
		for ( JsonNode row : tuples ) {
			String subject = row.path("s").path("value").asText();
			String predicate = row.path("p").path("value").asText();
			String object = row.path("o").path("value").asText();


			String[] sPath = subject.split("amandman/");
			String amandmanID = sPath[1];

			String[] pPath = predicate.split("predicate/");
			String pred = pPath[1];


			AmandmanMetaData amandman = findAmandman(amandmani,amandmanID);

			if(amandman == null){

				amandman = new AmandmanMetaData();
				amandman.setId(amandmanID);

				setPredicate(amandman,pred,object);
				amandmani.add(amandman);

			}else{

				setPredicate(amandman, pred,object);

			}



			if (!subject.equals("")) System.out.println(subject);
			System.out.println("\t" + predicate + " \n\t" + object + "\n");
		}


		return convertToJson(amandmani);



	}


	private String convertToJson(List<AmandmanMetaData> akti) {

		String json = "[ ";

		for(AmandmanMetaData a : akti){

			json += a.toString();
			json += ",";
		}

		StringBuilder sb = new StringBuilder(json);
		sb.setCharAt(json.length()-1, ' ');

		json = sb.toString();

		json += "]";

		return json;

	}

	private void setPredicate(AmandmanMetaData akt, String pred, String object) {

		switch (pred) {
		case "naziv":
			akt.setNaziv(object);
			break;
		case "status":
			akt.setStatus(object);
			break;
		case "odbornik":
			akt.setOdbornik(object);
			break;
		case "datumPredlaganja":
			akt.setDatumPredlaganja(object);
			break;
		case "akt_id":
			akt.setAktId(object);
			break;


		}


	}


	private static AmandmanMetaData findAmandman(List<AmandmanMetaData> akti, String aktID) {

		AmandmanMetaData akt = null;

		for(AmandmanMetaData a : akti){

			if(a.getId().equals(aktID)){

				return a;


			}

		}

		return akt;

	}

	private String addRDFDataToXML(String amandman, String id) {
		RDFDataGenerator rdfGen = new RDFDataGenerator(amandman);
		rdfGen.setPredicate("Naziv", RDFDataType.STRING, "naziv");
		rdfGen.setPredicate("ID", RDFDataType.STRING, "id");
		rdfGen.setPredicate("Akt_id", RDFDataType.STRING, "akt_id");
		rdfGen.setPredicate("Status_amandmana", RDFDataType.STRING, "status");
		rdfGen.setPredicate("Datum_predlaganja", RDFDataType.DATE, "datumPredlaganja");
		rdfGen.setPredicate("Odbornik", RDFDataType.STRING, "odbornik");
		rdfGen.setAmandmanSubject(id);
		return rdfGen.getDocumentAsString();
	}


	private void insertMetadata(String xmlRDFData, String amandmanID) {
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.user, Config.password,
				Config.authType);

		// Create a document manager to work with XML files.
		GraphManager graphManager = client.newGraphManager();

		// Set the default media type (RDF/XML)
		graphManager.setDefaultMimetype(RDFMimeTypes.RDFXML);

		// Referencing XML file with RDF data in attributes

		File tempFile = null;
		try {
			tempFile = File.createTempFile("amandman", ".rdf");
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		// Automatic extraction of RDF triples from XML file
		MetadataExtractor metadataExtractor = null;
		try {
			metadataExtractor = new MetadataExtractor();
			metadataExtractor.extractMetadata(new ByteArrayInputStream(xmlRDFData.getBytes(StandardCharsets.UTF_8)),
					new FileOutputStream(tempFile));
		} catch (SAXException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// A handle to hold the RDF content.
		FileHandle rdfFileHandle = new FileHandle(tempFile).withMimetype(RDFMimeTypes.RDFXML);

		// Writing the named graph
		System.out.println("[INFO] Overwriting triples to a named graph \"" + "amandman/" + amandmanID + "/metadata" + "\".");
		graphManager.write("amandman/" + amandmanID + "/metadata", rdfFileHandle);


		// Release the client
		client.release();

		System.out.println("[INFO] End.");

	}

	private String signXmlDocument(String document) {

		String signedDocument = null;
		Gradjanin currentUser = (Gradjanin) request.getSession().getAttribute("user");
		SignDocument sd = new SignDocument(document,currentUser.getEmail(),currentUser.getEmail());

		try {
			signedDocument = sd.sign();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return signedDocument;
	}


	public Amandman xmlStringToObject(String xml) {

		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance("model.amandman");
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Unmarshaller je objekat zadužen za konverziju iz XML-a u objektni
		// model
		Unmarshaller unmarshaller = null;
		try {
			unmarshaller = context.createUnmarshaller();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Akt akt = null;
		InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));

		Amandman amandman = null;
		try {
			amandman = (Amandman) unmarshaller.unmarshal(is);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return amandman;

	}


	public String createXMLString(Amandman amandman) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			System.out.println("JAXB unmarshalling/marshalling.\n");
			// Definiše se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
			JAXBContext context = JAXBContext.newInstance("model.amandman");
			// Marshaller je objekat zadužen za konverziju iz objektnog u XML
			// model
			Marshaller marshaller = context.createMarshaller();

			// Podešavanje marshaller-a
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Umesto System.out-a, može se koristiti FileOutputStream
			marshaller.marshal(amandman, out);

		} catch (JAXBException e) {
			e.printStackTrace();

		}

		// InputStream in = new ByteArrayInputStream(out.toByteArray());
		String aString = null;
		try {
			aString = new String(out.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}



		return aString;
	}

	@Produces(MediaType.TEXT_HTML)
	@Path("/test")
	@GET
	public void test(){



	}


}
