package xml.signature;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.transforms.TransformationException;
import org.apache.xml.security.transforms.Transforms;
import org.apache.xml.security.utils.Constants;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SignDocument {

	private static final String KEY_STORE_PASSWORD = "sgns";

	private String document;
	private String alias;
	private String password;


	public SignDocument(String document, String alias, String password) {
		super();
		this.document = document;
		this.alias = alias;
		this.password = password;

	}

	public String sign() throws Exception {
		//ucitava se dokument
		Document doc = loadXMLFromString(this.document);
		//ucitava privatni kljuc
		PrivateKey pk = readPrivateKey();
		//ucitava sertifikat
		Certificate cert = readCertificate();
		//potpisuje
		System.out.println("Signing....");
		doc = signDocument(doc, pk, cert);
		//snima se dokument
		String signedDoc = saveDocument(doc);
		System.out.println("Signing of document done");
		return signedDoc;

	}


	public Document loadXMLFromString(String xml) throws Exception
	{
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	    factory.setNamespaceAware(true);
	    DocumentBuilder builder = factory.newDocumentBuilder();

	    return builder.parse(new ByteArrayInputStream(xml.getBytes()));
	}

	/**
	 * Ucitava sertifikat is KS fajla
	 * alias primer
	 */
	public Certificate readCertificate() {
		try {
			//kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			//ucitavamo podatke

			//this.getClass().getClassLoader().getResourceAsStream("/sgns.jks");
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("resource/sgns.jks");

			BufferedInputStream in = new BufferedInputStream(is);
			ks.load(in, KEY_STORE_PASSWORD.toCharArray());

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
	 * Ucitava privatni kljuc is KS fajla
	 * alias primer
	 */
	private PrivateKey readPrivateKey() {
		try {
			//kreiramo instancu KeyStore
			KeyStore ks = KeyStore.getInstance("JKS", "SUN");
			//ucitavamo podatke
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("resource/sgns.jks");

			BufferedInputStream in = new BufferedInputStream(is);
			ks.load(in, KEY_STORE_PASSWORD.toCharArray());

			if(ks.isKeyEntry(alias)) {
				PrivateKey pk = (PrivateKey) ks.getKey(alias, password.toCharArray());
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

}
