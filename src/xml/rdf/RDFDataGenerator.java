package xml.rdf;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import dom.util.DOMUtility;
import testing.testProjekta;

public class RDFDataGenerator {

	private String xml;
	private Document xmlDocument;
	private Element root;

	public RDFDataGenerator(String xml){
		this.xml = xml;
		createDOM();
	}


	public void createDOM(){
		DocumentBuilder db = null;
		InputSource is = new InputSource();
		try {

			DocumentBuilderFactory dbf =  DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);

			db = dbf.newDocumentBuilder();

			is.setCharacterStream(new StringReader(xml));
			xmlDocument = db.parse(is);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		root = xmlDocument.getDocumentElement();

	}


	public void printDocument(){
		try {
			DOMUtility.printDocument(xmlDocument, System.out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	public void setSubject(String id){
		//root.setAttribute("xlmns", "http://www.parlament.gov.rs/akt");
		//root.setAttribute("xlmns:pred", "http://www.parlament.gov.rs/akt/predicate/");
		root.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:pred", "http://www.parlament.gov.rs/akt/predicate/");
		//root.setAttribute("xlmns:xs", "http://www.w3.org/2001/XMLSchema#");
		//root.setAttribute("xlmns:pred", "http://www.parlament.gov.rs/akt/predicate/");
		root.setAttribute("about", "http://www.parlament.gov.rs/akt/" + id);

		printDocument();

	}

	public void setPredicate(String elementTagName, RDFDataType datatype, String predicateName){

		NodeList nodes = root.getElementsByTagName(elementTagName);
		Element element = (Element) nodes.item(0);
		element.setAttribute("property", "pred:" + predicateName);
		element.setAttribute("datatype", getDataType(datatype));


	}

	private String getDataType(RDFDataType datatype) {

		String type = null;

		switch (datatype) {
		case STRING:
			type = "xs:string";
			break;

		case DATE:
			type = "xs:date";
			break;
		}

		return type;
	}

	public String getDocumentAsString(){

		return DOMUtility.toString(xmlDocument);
	}


	public static void test(){

		InputStream inputStream = null;
		try {
			inputStream = new BufferedInputStream(new FileInputStream("C:/Users/Shuky/workspaceee/xws/Sabloni/akt_primer_krivicni.xml"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		StringWriter writer = new StringWriter();
		try {
			IOUtils.copy(inputStream, writer, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String theString = writer.toString();


		RDFDataGenerator rdfGen = new RDFDataGenerator(theString);
		//rdfGen.printDocument();
		rdfGen.setPredicate("Naziv", RDFDataType.STRING, "test");
		rdfGen.setSubject("0000001");
		rdfGen.printDocument();
	}

	public static void main(String[] args) {
		test();
	}

}
