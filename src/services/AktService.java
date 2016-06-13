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
import java.util.ArrayList;
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
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
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

//import com.gint.examples.xml.signature.SignEnveloped;
import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DOMHandle;
import com.marklogic.client.io.FileHandle;
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;
import com.marklogic.client.semantics.GraphManager;
import com.marklogic.client.semantics.RDFMimeTypes;

import com.sun.xml.internal.ws.util.DOMUtil;

import database.Config;
import model.Akt;
import model.Gradjanin;
import model.Odbornik;
import session.AktDaoLocal;
import xml.rdf.MetadataExtractor;
import xml.rdf.RDFDataGenerator;
import xml.rdf.RDFDataType;
import xml.signature.SignDocument;

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

		// for(String s:listaUrija){
		// System.out.println(s);
		// }

		// ovde pocinje citanje svakog dokumenta posebno uz pomoc urij-a
		// create a manager for XML documents
		XMLDocumentManager docMgr = client.newXMLDocumentManager();

		// create a handle to receive the document content
		DOMHandle handle = new DOMHandle();

		// read the document content
		String redniBroj;

		ArrayList<Akt> akti = new ArrayList<Akt>();

		for (String s : listaUrija) {

			docMgr.read(s, handle);
			Document document = handle.get();

			String xmlString = document.toString();

			redniBroj = document.getDocumentElement().getAttribute("redni_broj");
			listaRednihBrojeva.add(redniBroj);
		}

		// access the document content
		for (String s : listaRednihBrojeva) {
			System.out.println(s);
		}

		JSONArray ar = new JSONArray(listaRednihBrojeva);
		String json = ar.toString();

		client.release();
		return json;

	}

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

		// access the document content
		for (String s : listaRednihBrojeva) {
			System.out.println(s);
		}

		JSONArray ar = new JSONArray(listaRednihBrojeva);
		String json = ar.toString();

		// log.info("Read /example/flipper.xml content with the<"+fileName+"/>
		// root element");

		client.release();
		return json;

	}

	@POST
	@Path("/new")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public String predloziAkt(String akt) {

		// InputStream stream = new
		// ByteArrayInputStream(akt.getBytes(StandardCharsets.UTF_8));
		Akt akt1 = xmlStringToObject(akt);
		akt1.setId(132);
		log.info(createXMLString(akt1));

		log.info("REST String: " + akt);

		Random rand = new Random();

		Odbornik gr = (Odbornik) request.getSession().getAttribute("user");

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

		int aktID = rand.nextInt(10000);

		// insertDocument("/akti/"+(String.valueOf(aktID)) + ".xml", stream);

		String xmlRDFData = addRDFDataToXML(signedXml);

		insertMetadata(xmlRDFData, String.valueOf(aktID));

		return "ok";

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

		// Read the triples from the named graph
		System.out.println();
		System.out.println("[INFO] Retrieving triples from RDF store.");
		System.out.println("[INFO] Using \"" + "akt/" + aktID + "/metadata" + "\" named graph.");

		// Define a DOM handle instance to hold the results
		DOMHandle domHandle = new DOMHandle();

		// Retrieve RDF triplets in format (RDF/XML) other than default
		graphManager.read("akt/" + aktID + "/metadata", domHandle).withMimetype(RDFMimeTypes.RDFXML);

		// Serialize document to the standard output stream
		System.out.println("[INFO] Rendering triples as \"application/rdf+xml\".");
		// DOMUtil.transform(domHandle.get(), System.out);

		// Release the client
		client.release();

		System.out.println("[INFO] End.");

	}

	private String addRDFDataToXML(String akt) {

		RDFDataGenerator rdfGen = new RDFDataGenerator(akt);
		rdfGen.setPredicate("Naziv", RDFDataType.STRING, "naziv_akta");
		rdfGen.setSubject("0000001");
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

		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.user, Config.password,
				Config.authType);

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
	}

}
