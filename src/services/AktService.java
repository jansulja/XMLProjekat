package services;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import javax.ejb.EJB;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.codehaus.jettison.json.JSONArray;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.JsonNode;
//import com.gint.examples.xml.signature.SignEnveloped;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.GenericDocumentManager;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.FileHandle;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.JacksonHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.io.StringHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;
import com.marklogic.client.semantics.GraphManager;
import com.marklogic.client.semantics.RDFMimeTypes;
import com.marklogic.client.semantics.SPARQLMimeTypes;
import com.marklogic.client.semantics.SPARQLQueryDefinition;
import com.marklogic.client.semantics.SPARQLQueryManager;

import database.Config;
import database.ConfigRemote;
import model.Akt;
import model.Gradjanin;
import model.Odbornik;


import rs.ac.uns.ftn.xws.util.ServiceException;

import model.view.Upit;

import session.AktDaoLocal;

import util.DOMUtil;



import xml.rdf.MetadataExtractor;
import xml.rdf.RDFDataGenerator;
import xml.rdf.RDFDataType;

import xml.signature.SignDocument;


import rs.ac.uns.ftn.examples.util.Util;
import rs.ac.uns.ftn.examples.util.Util.ConnectionProperties;

@Path("/akt")
public class AktService {
	ArrayList<String> listaUrija = new ArrayList<String>();
	ArrayList<String> listaRednihBrojeva = new ArrayList<String>();

	private static final String KEY_STORE_FILE = "C:/Users/Filipo/sgns.jks";

	private static final String AKT = "./Sabloni/aktPrimer1.xml";
	private static Logger log = Logger.getLogger(Gradjanin.class);
	static {

		Security.addProvider(new BouncyCastleProvider());
		org.apache.xml.security.Init.init();
	}
	@EJB
	AktDaoLocal aktDao;

	@Context
	private HttpServletRequest request;

	@GET
	@Produces(MediaType.APPLICATION_XML)
	@Path("/list")
	public String izlistajAkte() {
//<<<<<<< HEAD
//		// create the client
//
//		DatabaseClient client = DatabaseClientFactory.newClient(ConfigRemote.host, ConfigRemote.port,ConfigRemote.database, ConfigRemote.user, ConfigRemote.password, ConfigRemote.authType);
//
//
//=======

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
		SPARQLQueryDefinition query = sparqlQueryManager.newQueryDefinition("SELECT * WHERE { ?s ?p ?o }");

		resultsHandle = sparqlQueryManager.executeSelect(query, resultsHandle);
		//handleResults(resultsHandle);

		return "{}";

	}




	@POST
    @Path("/search")

    public void search(Upit upit) {




		String id;
		if (upit.getId().isEmpty()) {
			id = "" ;
		}else {
			id =  "?s "
					+ "<http://www.parlament.gov.rs/akt/predicate/id> "
					+ "?o1"
					+" ."
					+"FILTER regex (?o1, \""+upit.getId()+"\" )."
					;
		}

		String naziv;
		if (upit.getNaziv().isEmpty()) {
			naziv = "" ;
		}else {
			naziv =  "?s"
					+ "<http://www.parlament.gov.rs/akt/predicate/naziv> "
					+ "?o2"
					+"."
					+"FILTER regex (?o2, \""+upit.getNaziv()+"\" )."
					;
		}

		String predlagac;
		if (upit.getPredlagac().isEmpty()) {
			predlagac = "" ;
		}else {
			predlagac =  "?s"
					+ "<http://www.parlament.gov.rs/akt/predicate/odbornik> "
					+ "?o3"
					+"."
					+"FILTER regex (?o3, \""+upit.getPredlagac()+"\" )."
					;
		}


		String status;
		if (upit.getStatus().isEmpty()) {
			status = "" ;
		}else {
			status =  "?s"
					+ "?p4 "
					+ "?o4"
					+"."
					+"FILTER regex (?o4, \""+upit.getStatus()+"\" )."
					+"FILTER regex (?p4, \"http://www.parlament.gov.rs/akt/predicate/\" )."
					;
		}

		String datumUsvajanja;
		if (upit.getDatumUsvajanja()==null) {
			datumUsvajanja = "" ;
		}else {
			//System.out.println(upit.getDatumPredlogaDo());
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
		    String date = DATE_FORMAT.format(upit.getDatumUsvajanja());

			//System.out.println(date);
		    datumUsvajanja =  "?s"
					+ "<http://www.parlament.gov.rs/akt/predicate/datum_usvajanja>"
					+ "?o7"
					+"."
					+"FILTER (?o7 == \" "+date+ "\"^^xs:date )."
					;
		}



		String datumPredlogaDo;
		if (upit.getDatumPredlogaDo()==null) {
			datumPredlogaDo = "" ;
		}else {
			//System.out.println(upit.getDatumPredlogaDo());
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
		    String date = DATE_FORMAT.format(upit.getDatumPredlogaDo());

			//System.out.println(date);
			datumPredlogaDo =  "?s"
					+ "<http://www.parlament.gov.rs/akt/predicate/datum_predlaganja>"
					+ "?o5"
					+"."
					+"FILTER (?o5 < \" "+date+ "\"^^xs:date )."
					;
		}
		String datumPredlogaOd;
		if (upit.getDatumPredlogaOd()==null) {
			datumPredlogaOd = "" ;
		}else {
			//System.out.println(upit.getDatumPredlogaDo());
			SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
		    String date = DATE_FORMAT.format(upit.getDatumPredlogaOd());

			//System.out.println(date);
		    datumPredlogaOd =  "?s"
					+ "<http://www.parlament.gov.rs/akt/predicate/datum_predlaganja>"
					+ "?o5"
					+"."
					+"FILTER (?o5 >= \" "+date+ "\"^^xs:date )."
					;
		}

		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.user, Config.password,
				Config.authType);

		// Create a SPARQL query manager to query RDF datasets

		SPARQLQueryManager sparqlQueryManager = client.newSPARQLQueryManager();


		// Initialize DOM results handle
		DOMHandle domResultsHandle = new DOMHandle();


		System.out.println(id + "----------id");
		System.out.println(naziv + "----------naziv");


		// Initialize SPARQL query definition - retrieves all triples from RDF dataset
		SPARQLQueryDefinition query = sparqlQueryManager.newQueryDefinition("PREFIX xs:     <http://www.w3.org/2001/XMLSchema#>\n SELECT * WHERE { "
				+ id
				+ naziv
				+ predlagac
				+ status
				+ datumUsvajanja
				+ datumPredlogaOd
				+ datumPredlogaDo
				+ "}");

		//SPARQLQueryDefinition query = sparqlQueryManager.newQueryDefinition("SELECT ?s WHERE { ?s ?p ?o. FILTER regex (?o, \"o1@o1.com\" ). ?s ?p2 ?o2. FILTER regex (?o2, \"PREDLOZEN\" ).  }");
		System.out.println(id + "----------id");
		System.out.println(naziv + "----------naziv");
		log.info("aaaaaab              " + query );
		System.out.println("SELECT ?s WHERE { "
				+ id
				+ naziv

				+ predlagac
				+ status

				+ "}");

		System.out.println("[INFO] Showing all of the triples from RDF dataset in XML format\n");
		domResultsHandle = sparqlQueryManager.executeSelect(query, domResultsHandle);
		DOMUtil.transform(domResultsHandle.get(), System.out);
		if (!upit.getText().isEmpty()){
		// Initialize Jackson results handle
			JacksonHandle resultsHandle = new JacksonHandle();
			resultsHandle.setMimetype(SPARQLMimeTypes.SPARQL_JSON);

			QueryManager queryMgr = client.newQueryManager();

		// create a handle for the search results to be received as raw XML
			StringHandle resultsHandle2 = new StringHandle();
			SearchHandle resultsHandle3 = new SearchHandle();

			StringQueryDefinition query2 = queryMgr.newStringDefinition();

			query2.setCriteria(upit.getText());

		// run the search
			queryMgr.search(query2, resultsHandle3);
			MatchDocumentSummary[] results = resultsHandle3.getMatchResults();
		// dump the XML results to the console
			System.out.println(results[0].getPath());
		}
		client.release();

    }


	private static void handleResults(JacksonHandle resultsHandle) {
		JsonNode tuples = resultsHandle.get().path("results").path("bindings");
		for ( JsonNode row : tuples ) {
			String subject = row.path("s").path("value").asText();
			String predicate = row.path("p").path("value").asText();
			String object = row.path("o").path("value").asText();

			if (!subject.equals("")) System.out.println(subject);
			log.info("\t" + predicate + " \n\t" + object + "\n");
		}
	}



//	private static void handleResults(JacksonHandle resultsHandle) {
//		ArrayList<Akt> akti = new ArrayList<Akt>();
//
//		JsonNode tuples = resultsHandle.get().path("results").path("bindings");
//		for ( JsonNode row : tuples ) {
//			String subject = row.path("s").path("value").asText();
//			String predicate = row.path("p").path("value").asText();
//			String object = row.path("o").path("value").asText();
//
//
//			String[] sPath = subject.split("akti/");
//			String aktID = sPath[1];
//
//			String[] pPath = subject.split("akti/");
//			String pa = sPath[1];
//
//
//			if (!subject.equals("")) System.out.println(subject);
//			System.out.println("\t" + predicate + " \n\t" + object + "\n");
//		}
//	}

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/list-predlozeni")
	public String izlistajPredlozeneAkte() {
		// create the client
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.user, Config.password,
				Config.authType);

		// Ovce pocinje trazenje svih akata iz
		// baze-------------------------------------------
		// create a manager for searching
		QueryManager queryMgr = client.newQueryManager();

		// create a search definition
		StringQueryDefinition query = queryMgr.newStringDefinition();

		// Restrict the search to a specific directory
		query.setDirectory("/akti/");

		// empty search defaults to returning all results
		query.setCriteria("");

		// create a handle for the search results
		SearchHandle resultsHandle = new SearchHandle();

		// run the search
		queryMgr.search(query, resultsHandle);

		// Format the results
		// Get the list of matching documents in this page of results
		MatchDocumentSummary[] results = resultsHandle.getMatchResults();

		System.out.println("Listing " + results.length + " documents:");

		// List the URI of each matching document
		for (MatchDocumentSummary result : results) {
			listaUrija.add(result.getUri());
			// System.out.println(result.getUri());
		}

		// ovde pocinje citanje svakog dokumenta posebno uz pomoc urij-a
		// create a manager for XML documents
		XMLDocumentManager docMgr = client.newXMLDocumentManager();

		// create a handle to receive the document content
		DOMHandle handle = new DOMHandle();

		// read the document content
		String redniBroj;
		String status;
		for (String s : listaUrija) {

			docMgr.read(s, handle);
			Document document = handle.get();

			status = document.getDocumentElement().getAttribute("status");
			if (status.equals("PREDLOZEN")) {
				redniBroj = document.getDocumentElement().getAttribute("redni_broj");
				listaRednihBrojeva.add(redniBroj);
			}

		}
		return "ok";
	}


	@POST
	@Path("/delete")
    @Produces(MediaType.APPLICATION_XML)
	public String deleteAkt(String akt){


		return "ok";

	}


	public static void obrisi(String ID){
		// uri plus id plus extenzija to je uri za xml fajl koji trba da obrises
		// create the client
				DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.user, Config.password, Config.authType);

				// create a generic manager for documents
				GenericDocumentManager docMgr = client.newDocumentManager();

				String putanja = new String("/akti/" + ID + ".xml");

				// delete the documents
				docMgr.delete(putanja);




				// release the client
				client.release();
	}

	@POST
	@Path("/provera")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_JSON)

	public String proveri(Upit upit){
		log.info(upit.toString());

		return "ok";

	}

	@POST
	@Path("/new")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public String predloziAkt(String akt) {


		Random rand = new Random();
		int aktID = rand.nextInt(10000);

		Odbornik gr = (Odbornik) request.getSession().getAttribute("user");


		// InputStream stream = new
		// ByteArrayInputStream(akt.getBytes(StandardCharsets.UTF_8));
		Akt akt1 = xmlStringToObject(akt);
		akt1.setId(String.valueOf(aktID));
		akt1.setDatumPredlaganja(getXMLGregorianCalendarNow());
		akt1.setOdbornik(gr.getEmail());
		log.info(createXMLString(akt1));

		log.info("REST String: " + akt);

		akt = createXMLString(akt1);




		String signedXml = null;
		SignDocument sd = new SignDocument(akt, gr.getEmail(), gr.getEmail());

		try {
			signedXml = sd.sign();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// EncryptKEK encrypt = new EncryptKEK();
		// Document doc = encrypt.testIt(signedXml);
		//
		// DecryptKEK decrypt = new DecryptKEK();
		// decrypt.testIt(doc);

		InputStream stream = new ByteArrayInputStream(signedXml.getBytes(StandardCharsets.UTF_8));




		//String xmlRDFData = addRDFDataToXML(signedXml);

		//insertMetadata(xmlRDFData, String.valueOf(aktID));

	    insertDocument("/akti/"+akt1.getId() + ".xml", stream);

		@SuppressWarnings("unused")
		String xmlRDFData = addRDFDataToXML(signedXml,akt1.getId());

		insertMetadata(xmlRDFData, akt1.getId());

		return "ok";

	}




	public XMLGregorianCalendar getXMLGregorianCalendarNow()

    {

        GregorianCalendar gregorianCalendar = new GregorianCalendar();

        DatatypeFactory datatypeFactory = null;
		try {
			datatypeFactory = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        XMLGregorianCalendar now =
            datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        return now;
    }





	private void insertMetadata(String xmlRDFData, String aktID) {
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.user, Config.password,
				Config.authType);

		// Create a document manager to work with XML files.
		GraphManager graphManager = client.newGraphManager();

		// Set the default media type (RDF/XML)
		graphManager.setDefaultMimetype(RDFMimeTypes.RDFXML);

		// Referencing XML file with RDF data in attributes

		File tempFile = null;
		try {
			tempFile = File.createTempFile("akt", ".rdf");
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
		System.out.println("[INFO] Overwriting triples to a named graph \"" + "akt/" + aktID + "/metadata" + "\".");
		graphManager.write("akt/" + aktID + "/metadata", rdfFileHandle);


		// Release the client
		client.release();

		System.out.println("[INFO] End.");

	}

	private String addRDFDataToXML(String akt,String id) {

		RDFDataGenerator rdfGen = new RDFDataGenerator(akt);
		rdfGen.setPredicate("Naziv", RDFDataType.STRING, "naziv");
		rdfGen.setPredicate("ID", RDFDataType.STRING, "id");
		rdfGen.setPredicate("Status", RDFDataType.STRING, "status");
		rdfGen.setPredicate("Datum_predlaganja", RDFDataType.DATE, "datum_predlaganja");
		rdfGen.setPredicate("Odbornik", RDFDataType.STRING, "odbornik");
		rdfGen.setSubject(id);
		return rdfGen.getDocumentAsString();

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

	public InputStream createXML(Akt akt) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			System.out.println("JAXB unmarshalling/marshalling.\n");
			// Defini�e se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
			JAXBContext context = JAXBContext.newInstance("model");
			// Marshaller je objekat zadu�en za konverziju iz objektnog u XML
			// model
			Marshaller marshaller = context.createMarshaller();

			// Pode�avanje marshaller-a
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Umesto System.out-a, mo�e se koristiti FileOutputStream
			marshaller.marshal(akt, out);

		} catch (JAXBException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}

		// InputStream in = new ByteArrayInputStream(out.toByteArray());
		String aString = null;
		try {
			aString = new String(out.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(aString);
		Certificate cert = readCertificate("o1@o1.com", "sgns");
		System.out.println(cert);
		PrivateKey privateKey = readPrivateKey("o1@o1.com", "sgns", "o1@o1.com");
		System.out.println(privateKey);

		Document doc = loadDocument(aString);
		System.out.println(doc);
		Document singDoc = signDocument(doc, privateKey, cert);
		String singString = saveDocument(singDoc);
		System.out.println(singString);

		InputStream in = new ByteArrayInputStream(singString.getBytes(StandardCharsets.UTF_8));

		return in;
	}

	public String createXMLString(Akt akt) {

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			System.out.println("JAXB unmarshalling/marshalling.\n");
			// Defini�e se JAXB kontekst (putanja do paketa sa JAXB bean-ovima)
			JAXBContext context = JAXBContext.newInstance("model");
			// Marshaller je objekat zadu�en za konverziju iz objektnog u XML
			// model
			Marshaller marshaller = context.createMarshaller();

			// Pode�avanje marshaller-a
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Umesto System.out-a, mo�e se koristiti FileOutputStream
			marshaller.marshal(akt, out);

		} catch (JAXBException e) {
			e.printStackTrace();
			System.out.println(e.toString());
		}

		// InputStream in = new ByteArrayInputStream(out.toByteArray());
		String aString = null;
		try {
			aString = new String(out.toByteArray(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Document doc = loadDocument(aString);

		String singString = saveDocument(doc);


		return singString;
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
	 * Ucitava sertifikat is KS fajla alias primer
	 */
	private Certificate readCertificate(String alias, String kspassword) {
		try {
			// kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			// ucitavamo podatke
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(KEY_STORE_FILE));
			ks.load(in, kspassword.toCharArray());

			if (ks.isKeyEntry(alias)) {
				Certificate cert = ks.getCertificate(alias);
				return cert;

			} else
				return null;

		} catch (KeyStoreException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (CertificateException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Kreira DOM od XML dokumenta
	 */
	private Document loadDocument(String file) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			InputStream stream = new ByteArrayInputStream(file.getBytes(StandardCharsets.UTF_8));
			return builder.parse(stream);

		} catch (FactoryConfigurationError e) {
			e.printStackTrace();
			return null;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
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

	/**
	 * Potpisuje dom file
	 */

	private Document signDocument(Document doc, PrivateKey privateKey, Certificate cert) {

		try {
			Element rootEl = doc.getDocumentElement();

			// kreira se signature objekat
			XMLSignature sig = new XMLSignature(doc, null, XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1);
			// kreiraju se transformacije nad dokumentom
			Transforms transforms = new Transforms(doc);

			// iz potpisa uklanja Signature element
			// Ovo je potrebno za enveloped tip po specifikaciji
			transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
			// normalizacija
			transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);

			// potpisuje se citav dokument (URI "")
			sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);

			// U KeyInfo se postavalja Javni kljuc samostalno i citav sertifikat
			sig.addKeyInfo(cert.getPublicKey());
			sig.addKeyInfo((X509Certificate) cert);

			// poptis je child root elementa
			rootEl.appendChild(sig.getElement());

			// potpisivanje
			sig.sign(privateKey);

			return doc;

		} catch (TransformationException e) {
			e.printStackTrace();
			return null;
		} catch (XMLSignatureException e) {
			e.printStackTrace();
			return null;
		} catch (DOMException e) {
			e.printStackTrace();
			return null;
		} catch (XMLSecurityException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Ucitava privatni kljuc is KS fajla alias primer
	 */
	private PrivateKey readPrivateKey(String alias, String kspassword, String apassword) {
		try {
			// kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			// ucitavamo podatke
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(KEY_STORE_FILE));
			ks.load(in, kspassword.toCharArray());

			if (ks.isKeyEntry(alias)) {
				PrivateKey pk = (PrivateKey) ks.getKey(alias, apassword.toCharArray());
				return pk;
			} else
				return null;

		} catch (KeyStoreException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (CertificateException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Test
	 *
	 * @throws JAXBException
	 */
	private void testIt() throws JAXBException {
		// Document doc = loadDocument(AKT);
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance("model");
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Unmarshaller je objekat zadu�en za konverziju iz XML-a u objektni
		// model
		Unmarshaller unmarshaller = null;
		try {
			unmarshaller = context.createUnmarshaller();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Akt akt = null;
		Akt akt = (Akt) unmarshaller.unmarshal(new File(AKT));
		InputStream is = createXML(akt);

	}

	private String runIt(String userPass, String akt) throws JAXBException {
		// Document doc = loadDocument(AKT);
		System.out.println(userPass);
		Certificate cert = readCertificate(userPass, "sgns");
		System.out.println(cert);
		PrivateKey privateKey = readPrivateKey(userPass, "sgns", userPass);
		System.out.println(privateKey);

		Document doc = loadDocument(akt);
		System.out.println(doc);
		Document singDoc = signDocument(doc, privateKey, cert);
		String singString = saveDocument(singDoc);
		System.out.println(singString);

		return singString;

	}

	public Akt xmlStringToObject(String xml) {

		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance("model");
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Unmarshaller je objekat zadu�en za konverziju iz XML-a u objektni
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

		Akt akt = null;
		try {
			akt = (Akt) unmarshaller.unmarshal(is);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return akt;

	}

	public static void main(String[] args) {
		AktService sign = new AktService();
		try {
			sign.testIt();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		obrisi("3094");

	}

}
