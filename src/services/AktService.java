package services;


import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import com.marklogic.client.io.InputStreamHandle;
import com.marklogic.client.io.SearchHandle;
import com.marklogic.client.query.MatchDocumentSummary;
import com.marklogic.client.query.QueryManager;
import com.marklogic.client.query.StringQueryDefinition;

import database.Config;
import model.Akt;
import model.Gradjanin;
import model.Odbornik;
import session.AktDaoLocal;
import xml.encryption.DecryptKEK;
import xml.encryption.EncryptKEK;

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
	private HttpServletRequest request ;
	
	@GET 
    @Produces(MediaType.APPLICATION_XML)
	@Path("/list")
	public String izlistajAkte(){
		// create the client
		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.user, Config.password, Config.authType);
		
//Ovce pocinje trazenje svih akata iz baze-------------------------------------------
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
			//	System.out.println(result.getUri());
			}
			
//		for(String s:listaUrija){
//			System.out.println(s);
//		}
			
//ovde pocinje citanje svakog dokumenta posebno uz pomoc urij-a
		// create a manager for XML documents
		XMLDocumentManager docMgr = client.newXMLDocumentManager();
			
		// create a handle to receive the document content
		DOMHandle handle = new DOMHandle();
		
		// read the document content
		String redniBroj;
		
		for(String s:listaUrija){

			docMgr.read(s, handle);
			Document document = handle.get();
			redniBroj = document.getDocumentElement().getAttribute("redni_broj");
			listaRednihBrojeva.add(redniBroj);	
		}
		
		// access the document content
		for(String s:listaRednihBrojeva){
			System.out.println(s);
		}

		
			



		  JSONArray ar=new JSONArray(listaRednihBrojeva);
		  String json= ar.toString();

	//	log.info("Read /example/flipper.xml content with the<"+fileName+"/> root element");
		
		client.release();
		return json;
		
	}
	
	
	

	@POST
	@Path("/new")
	@Produces(MediaType.APPLICATION_XML)
	@Consumes(MediaType.APPLICATION_XML)
	public String predloziAkt(String akt){

		//InputStream stream = new ByteArrayInputStream(akt.getBytes(StandardCharsets.UTF_8));

		log.info("REST String: " + akt);
		System.out.println("dakaka");
		Random rand = new Random();

		Odbornik gr = (Odbornik)request.getSession().getAttribute("user");
		//System.out.println(gr.getEmail());


		String signedXml = null;
		try {
			signedXml = runIt(gr.getEmail(), akt);
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		EncryptKEK encrypt = new EncryptKEK();
		Document doc =  encrypt.testIt(signedXml);

		DecryptKEK decrypt = new DecryptKEK();
		decrypt.testIt(doc);


		InputStream stream = new ByteArrayInputStream(signedXml.getBytes(StandardCharsets.UTF_8));

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


		//InputStream in = new ByteArrayInputStream(out.toByteArray());
		String aString = null;
		try {
			aString = new String(out.toByteArray(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		System.out.println(aString);
		Certificate cert = readCertificate("o1@o1.com", "sgns") ;
		System.out.println(cert);
		PrivateKey privateKey = readPrivateKey("o1@o1.com", "sgns", "o1@o1.com");
		System.out.println(privateKey);


		Document doc = loadDocument (aString);
		System.out.println(doc);
		Document singDoc = signDocument(doc, privateKey, cert);
		String singString = saveDocument(singDoc);
		System.out.println(singString);

		InputStream in = new ByteArrayInputStream(singString.getBytes(StandardCharsets.UTF_8));

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

		Odbornik gr = (Odbornik)request.getSession().getAttribute("user");
		System.out.println(gr.getEmail());


		Akt a1 = Akt.getDummy();
		insertDocument("/akti/"+a1.getId().toString()+".xml", createXML(a1));


		return "<html><h1>Dodat akt "+ a1.getId()  +" </h1></html>";
    }

	public static void insertDocument(String path, InputStream in){

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
	/**
	 * Ucitava sertifikat is KS fajla
	 * alias primer
	 */
	private Certificate readCertificate(String alias, String kspassword) {
		try {
			//kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			//ucitavamo podatke
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(KEY_STORE_FILE));
			ks.load(in, kspassword.toCharArray());

			if(ks.isKeyEntry(alias)) {
				Certificate cert = ks.getCertificate(alias);
				return cert;

			}
			else
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
		String xmlFile = null ;
		try {
			StringWriter writer = new StringWriter();
		    StreamResult result = new StreamResult(writer);



			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer();

			DOMSource source = new DOMSource(doc);
			//StreamResult result = new StreamResult(xmlFile);

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

			//kreira se signature objekat
			XMLSignature sig = new XMLSignature(doc, null, XMLSignature.ALGO_ID_SIGNATURE_RSA_SHA1);
			//kreiraju se transformacije nad dokumentom
			Transforms transforms = new Transforms(doc);

			//iz potpisa uklanja Signature element
			//Ovo je potrebno za enveloped tip po specifikaciji
			transforms.addTransform(Transforms.TRANSFORM_ENVELOPED_SIGNATURE);
			//normalizacija
			transforms.addTransform(Transforms.TRANSFORM_C14N_WITH_COMMENTS);

			//potpisuje se citav dokument (URI "")
			sig.addDocument("", transforms, Constants.ALGO_ID_DIGEST_SHA1);

			//U KeyInfo se postavalja Javni kljuc samostalno i citav sertifikat
			sig.addKeyInfo(cert.getPublicKey());
			sig.addKeyInfo((X509Certificate) cert);

			//poptis je child root elementa
			rootEl.appendChild(sig.getElement());

			//potpisivanje
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
	 * Ucitava privatni kljuc is KS fajla
	 * alias primer
	 */
	private PrivateKey readPrivateKey(String alias, String kspassword, String apassword) {
		try {
			//kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			//ucitavamo podatke
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(KEY_STORE_FILE));
			ks.load(in, kspassword.toCharArray());

			if(ks.isKeyEntry(alias)) {
				PrivateKey pk = (PrivateKey) ks.getKey(alias, apassword.toCharArray());
				return pk;
			}
			else
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
	 * @throws JAXBException
	 */
	private void testIt() throws JAXBException {
		//Document doc = loadDocument(AKT);
		JAXBContext context = null;
		try {
			context = JAXBContext.newInstance("model");
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Unmarshaller je objekat zadužen za konverziju iz XML-a u objektni model
		Unmarshaller unmarshaller = null;
		try {
			unmarshaller = context.createUnmarshaller();
		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//Akt akt = null;
		Akt akt = (Akt) unmarshaller.unmarshal(new File(AKT));
		InputStream is = createXML(akt);


	}
	private String runIt(String userPass, String akt) throws JAXBException {
		//Document doc = loadDocument(AKT);
		System.out.println(userPass);
		Certificate cert = readCertificate(userPass, "sgns") ;
		System.out.println(cert);
		PrivateKey privateKey = readPrivateKey(userPass, "sgns", userPass);
		System.out.println(privateKey);


		Document doc = loadDocument (akt);
		System.out.println(doc);
		Document singDoc = signDocument(doc, privateKey, cert);
		String singString = saveDocument(singDoc);
		System.out.println(singString);

		return singString;

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
