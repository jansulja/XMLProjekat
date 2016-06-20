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
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.FileHandle;
import com.marklogic.client.io.JacksonHandle;
import com.marklogic.client.semantics.GraphManager;
import com.marklogic.client.semantics.RDFMimeTypes;
import com.marklogic.client.semantics.SPARQLMimeTypes;
import com.marklogic.client.semantics.SPARQLQueryDefinition;
import com.marklogic.client.semantics.SPARQLQueryManager;

import database.Config;
import dom.util.DOMUtility;
import model.Gradjanin;
import model.Odbornik;
import model.amandman.Amandman;
import model.amandman.AmandmanDodavanje;
import model.amandman.Referenca;
import model.amandman.StatusAmandmana;
import model.amandman.TipAmandmana;
import model.metadata.AmandmanMetaData;
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
	@POST
	@Path("/update")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateAmandman(String lista) {
		String[] id = lista.split("\"");

		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.user, Config.password,
				Config.authType);

	//	String metadataPutanja = new String("akt/" + id[3] + "/metadata");
		String xmlPutanja = new String("/amandmani/" + id[3] + ".xml");
		//id[3] je ID akta a id[7] je status
		//updateMetaData(id[3],id[7]);
		// ovaj je za xml fajl
		// GenericDocumentManager docMgr = client.newDocumentManager();

		// Create a document manager to work with XML files.
		// ovaj je za metadata
		GraphManager graphManager = client.newGraphManager();
		// Set the default media type (RDF/XML)
		graphManager.setDefaultMimetype(RDFMimeTypes.RDFXML);

		// create a handle to receive the document content

		// read the document content
		// create a manager for XML documents
		XMLDocumentManager docMgr = client.newXMLDocumentManager();

		// create a handle to receive the document content
		DOMHandle handle = new DOMHandle();

		// read the document content
		docMgr.read(xmlPutanja, handle);

		// access the document content
		Document document = handle.get();




		if(id[7].equals("USVOJEN")){

			Amandman a = xmlStringToObject(DOMUtility.toString(document));
			izmeniAkt(a);
		}


		NodeList rootName = document.getDocumentElement().getElementsByTagName("Status_amandmana");
		Element element = (Element) rootName.item(0);

		element.setTextContent("");
		element.setTextContent(id[7]);

		DOMHandle handle2 = new DOMHandle(document);
		docMgr.write(xmlPutanja, handle2);


		String amandmanWithRDFMeta = addRDFDataToXML(DOMUtility.toString(document),id[3]);
		insertMetadata(amandmanWithRDFMeta, id[3]);


		log.info("asd" + id[3] + id[7]);

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


	public String izmeniAkt (Amandman a){



		String aktId = a.getAktId();


		JAXBElement<Referenca> ref =  (JAXBElement<Referenca>) a.getSadrzaj().getContent().get(1);
		Referenca r = ref.getValue();

		String redniBrClana = String.valueOf(r.getClanRef());

		String izmenaTeksta = a.getSadrzaj().getContent().get(0).toString();
		//String vrstaIzmene = "DODAJ"; // DODAJ   PROMENI
		TipAmandmana vrstaIzmene = a.getTip();


		String pathToAkt = "/akti/"+aktId + ".xml" ;
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.user, Config.password,
				Config.authType);

		XMLDocumentManager docMgr = client.newXMLDocumentManager();

		// create a handle to receive the document content
		DOMHandle handle = new DOMHandle();

		// read the document content
		docMgr.read(pathToAkt, handle);

		// access the document content
		Document doc = handle.get();
//
//		String akt = saveDocument(doc);
//		Akt akt1 = xmlStringToObject(akt);
//		if(akt1.getDeo().isEmpty()){
//			akt1.getDeo().iterator()
//		}



		System.out.println(doc.getElementsByTagName("Clan").getLength());
		// update staff attribute
		if (vrstaIzmene.name().equals("OBRISI")){
			for (int i=0; i<doc.getElementsByTagName("Pododeljak").getLength(); i++) {

				Node pododeljak = doc.getElementsByTagName("Pododeljak").item(i);


				if (pododeljak.hasChildNodes()) {
					NodeList attr = pododeljak.getChildNodes();
					for (int j=0; j<attr.getLength(); j++) {

						Node clan = attr.item(j);

										if(clan.hasAttributes()){
											NamedNodeMap atribut = clan.getAttributes();
											Node nodeAttr = atribut.getNamedItem("redni_broj");
											if (redniBrClana.equals(nodeAttr.getTextContent())) {
												pododeljak.removeChild(clan);

											}
										}
					}
				}

			}
		} else {


			for (int i=0; i<doc.getElementsByTagName("Clan").getLength(); i++) {

				Node clan = doc.getElementsByTagName("Clan").item(i);


				if(clan.hasAttributes()){
					NamedNodeMap atribut = clan.getAttributes();
					Node nodeAttr = atribut.getNamedItem("redni_broj");
					//nodeAttr.getTextContent();


					System.out.println(nodeAttr.getTextContent());
					if (clan.hasChildNodes() && redniBrClana.equals(nodeAttr.getTextContent()) ){

						System.out.println(i);
						NodeList attr = clan.getChildNodes();
						System.out.println(attr.getLength());

						for (int ii = 0; ii<attr.getLength(); ii++) {
							Node kojiNode = attr.item(ii);
							if(kojiNode.hasChildNodes()) {

								NodeList stavTekst = kojiNode.getChildNodes();

								for (int iii = 0; iii<stavTekst.getLength(); iii++) {

									Node stavTekstNode = stavTekst.item(iii);
									//stavTekstNode.TEXT_NODE
//									System.out.println("tip:");
									if (stavTekstNode != null) {
									short tip = stavTekstNode.getNodeType() ;
									System.out.println(tip);
//									System.out.println("tip");
										if (stavTekstNode.getNodeName().equals("Tekst"))	{
											//Node child = kojiNode.getFirstChild().getFirstChild().getNextSibling();

												System.out.println(tip);
												System.out.println("Ovo  je tip");
												System.out.println(stavTekstNode.getNodeType());
												if(vrstaIzmene.name().equals("DODAJ")){
													stavTekstNode.setTextContent(stavTekstNode.getTextContent() +"\n" + izmenaTeksta);
												} else if (vrstaIzmene.name().equals("PROMENI")) {
													stavTekstNode.setTextContent(izmenaTeksta);
													System.out.println(stavTekstNode.getNodeType());

												}

											System.out.println(stavTekstNode.getTextContent());
											//System.out.println(child.getBaseURI());
											//System.out.println(stavTekstNode.getNodeValue());
										}

								}
								}
							}

						}
					}
				//
				}
			}
		}

		InputStream stream = new ByteArrayInputStream(AktService.saveDocument(doc).getBytes(StandardCharsets.UTF_8));




		//String xmlRDFData = addRDFDataToXML(signedXml);

		//insertMetadata(xmlRDFData, String.valueOf(aktID));

	    AktService.insertDocument("/akti/"+aktId + ".xml", stream);
		//Node nodeAttr = attr.getNamedItem("Naziv");
		//nodeAttr.setTextContent("2");


		client.release();

		return "ok";

	}


}
