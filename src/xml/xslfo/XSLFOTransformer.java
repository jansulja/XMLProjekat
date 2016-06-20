package xml.xslfo;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.openjpa.jdbc.sql.FoxProDictionary;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import com.marklogic.client.DatabaseClient;
import com.marklogic.client.DatabaseClientFactory;
import com.marklogic.client.document.XMLDocumentManager;
import com.marklogic.client.io.DOMHandle;

import database.Config;
import net.sf.saxon.TransformerFactoryImpl;



public class XSLFOTransformer {

	public XSLFOTransformer() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void aktToHTML(Document document,String name) {
		try {
	        TransformerFactory factory = TransformerFactory.newInstance();


	        InputStream is = this.getClass().getClassLoader().getResourceAsStream("/resource/xsl/akt.xsl");


	        Source xslt = new StreamSource(is);
	        Transformer transformer = factory.newTransformer(xslt);
	        Source text = new DOMSource(document);
	        transformer.transform(text, new StreamResult(new File(System.getProperty("user.home") + "/Desktop/" +name+".html" )));
	        System.out.println("Uspesno zavrsena transformacija "+name+ " u html, na putanji: WebContent/gen/"+name+".html");
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	public static void amandmanToHTML(String xmlName) {
		try {
	        TransformerFactory factory = TransformerFactory.newInstance();
	        Source xslt = new StreamSource(new File("data/xslt/amandman.xsl"));
	        Transformer transformer = factory.newTransformer(xslt);
	        Source text = new StreamSource(new File("data/xml/"+xmlName+".xml"));
	        transformer.transform(text, new StreamResult(new File("WebContent/gen/html/"+xmlName+".html")));
	        System.out.println("Uspesno zavrsena transformacija "+xmlName+ " u html, na putanji: WebContent/gen/"+xmlName+".html");
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
	}

	public static String dokumentToHTMLStream(InputStream xmlName, String tip) throws UnsupportedEncodingException {
		try {
	        TransformerFactory factory = TransformerFactory.newInstance();
	        Source xslt = new StreamSource(new File("../standalone/deployments/XML_Projekat.war/data/xslt/"+tip+".xsl"));
	        Transformer transformer = factory.newTransformer(xslt);
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        Source text = new StreamSource(xmlName);
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        transformer.transform(text, new StreamResult(baos));
	        System.out.println("Uspesno zavrsena transformacija u html");
	        return baos.toString("UTF-8");
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			return "error";
		} catch (TransformerException e) {
			e.printStackTrace();
			return "error";
		}
	}

	public static String amandmanToHTMLStream(InputStream xmlName) throws UnsupportedEncodingException {
		try {
	        TransformerFactory factory = TransformerFactory.newInstance();
	        Source xslt = new StreamSource(new File("../standalone/deployments/XML_Projekat.war/data/xslt/amandman.xsl"));
	        Transformer transformer = factory.newTransformer(xslt);
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        Source text = new StreamSource(xmlName);
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        transformer.transform(text, new StreamResult(baos));
	        System.out.println("Uspesno zavrsena transformacija u html");
	        return baos.toString("UTF-8");
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
			return "error";
		} catch (TransformerException e) {
			e.printStackTrace();
			return "error";
		}
	}

	public void aktToPDF(Document document, String xmlName) {
		FopFactory fopFactory;
		TransformerFactory transformerFactory;

		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream("/fop.xconf");
			// Initialize FOP factory object
			File f = File.createTempFile("fop", "xconf");

		    f.deleteOnExit();
		    FileOutputStream fout = new FileOutputStream(f);
		    IOUtils.copy(is, fout);




			fopFactory = FopFactory.newInstance(f);

			// Setup the XSLT transformer factory
			transformerFactory = new TransformerFactoryImpl();
			// Point to the XSL-FO file

			is = this.getClass().getClassLoader().getResourceAsStream("/resource/xsl/akt-fo.xsl");
			File xsltFile = File.createTempFile("akt-fo", "xsl");
			xsltFile.deleteOnExit();
		    fout = new FileOutputStream(xsltFile);
		    IOUtils.copy(is, fout);


			// Create transformation source
			StreamSource transformSource = new StreamSource(xsltFile);
			// Initialize the transformation subject
			Source source = new DOMSource(document);
			// Initialize user agent needed for the transformation
			FOUserAgent userAgent = fopFactory.newFOUserAgent();
			// Create the output stream to store the results
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			// Initialize the XSL-FO transformer object
			Transformer xslFoTransformer = transformerFactory.newTransformer(transformSource);
			// Construct FOP instance with desired output format
			Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, outStream);
			// Resulting SAX events
			Result res = new SAXResult(fop.getDefaultHandler());
			// Start XSLT transformation and FOP processing
			xslFoTransformer.transform(source, res);
			// Generate PDF file
			//File pdfFile = new File(System.getProperty( "catalina.base" ) + "/webapps/xws/gen/pdf/"+xmlName+".pdf");
			File pdfFile = new File(System.getProperty("user.home") + "/Desktop/" +xmlName+".pdf" );
			OutputStream out = new BufferedOutputStream(new FileOutputStream(pdfFile));
			out.write(outStream.toByteArray());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		} catch (FOPException e) {
			e.printStackTrace();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}

		System.out.println("Uspesno zavrsena transformacija "+xmlName+ " u pdf, na putanji: WebContent/gen/"+xmlName+".pdf");
	}
	public static void main(String[] args) {

		DatabaseClient client = DatabaseClientFactory.newClient(Config.host, Config.port, Config.user, Config.password,
				Config.authType);

		XMLDocumentManager docMgr = client.newXMLDocumentManager();
		DOMHandle handle = new DOMHandle();
		docMgr.read("/akti/2016-06-14-2422.xml", handle);
		Document document = handle.get();
		client.release();

		//XSLFOTransformer.aktToPDF(document, "2016-06-14-5612");
		//XSLFOTransformer.aktToHTML(document, "2016-06-14-2422");



	}


}
