//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.21 at 12:00:31 AM CEST 
//


package model.amandman;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the model.amandman package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _SadrzajReferenca_QNAME = new QName("http://www.parlament.gov.rs/amandman", "Referenca");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: model.amandman
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Amandman }
     * 
     */
    public Amandman createAmandman() {
        return new Amandman();
    }

    /**
     * Create an instance of {@link Sadrzaj }
     * 
     */
    public Sadrzaj createSadrzaj() {
        return new Sadrzaj();
    }

    /**
     * Create an instance of {@link Referenca }
     * 
     */
    public Referenca createReferenca() {
        return new Referenca();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Referenca }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.parlament.gov.rs/amandman", name = "Referenca", scope = Sadrzaj.class)
    public JAXBElement<Referenca> createSadrzajReferenca(Referenca value) {
        return new JAXBElement<Referenca>(_SadrzajReferenca_QNAME, Referenca.class, Sadrzaj.class, value);
    }

}
