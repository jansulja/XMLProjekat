//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2016.06.15 at 05:26:32 PM CEST
//


package model.amandman;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for anonymous complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Naziv" type="{http://www.parlament.gov.rs/amandman}NazivAmandmana"/>
 *         &lt;element name="Sadrzaj" type="{http://www.parlament.gov.rs/amandman}Sadrzaj"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
	"id",
	"datumPredlaganja",
	"aktId",
	"statusAmandmana",
	"odbornik",
    "naziv",
    "sadrzaj"
})
@XmlRootElement(name = "Amandman")
public class Amandman {

    @XmlElement(name = "Naziv", required = true)
    @XmlSchemaType(name = "string")
    protected NazivAmandmana naziv;
    @XmlElement(name = "Sadrzaj", required = true)
    protected Sadrzaj sadrzaj;
    @XmlElement(name = "Akt_id", required = true)
    protected String aktId;
    @XmlElement(name = "Status_amandmana", required = true)
    protected StatusAmandmana statusAmandmana;
    @XmlElement(name = "Odbornik")
    protected String odbornik;
    @XmlElement(name = "ID")
    protected String id;
    @XmlElement(name = "Datum_predlaganja", required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar datumPredlaganja;




    public XMLGregorianCalendar getDatumPredlaganja() {
		return datumPredlaganja;
	}

	public void setDatumPredlaganja(XMLGregorianCalendar datumPredlaganja) {
		this.datumPredlaganja = datumPredlaganja;
	}

	public String getId() {
		return id;
	}

    public void setId(String id) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();

		this.id = dateFormat.format(date) + "-" + id;
	}

	public String getAktId() {
		return aktId;
	}

	public void setAktId(String aktId) {
		this.aktId = aktId;
	}

	public StatusAmandmana getStatusAmandmana() {
		return statusAmandmana;
	}

	public void setStatusAmandmana(StatusAmandmana statusAmandmana) {
		this.statusAmandmana = statusAmandmana;
	}

	public String getOdbornik() {
		return odbornik;
	}

	public void setOdbornik(String odbornik) {
		this.odbornik = odbornik;
	}

	/**
     * Gets the value of the naziv property.
     *
     * @return
     *     possible object is
     *     {@link NazivAmandmana }
     *
     */
    public NazivAmandmana getNaziv() {
        return naziv;
    }

    /**
     * Sets the value of the naziv property.
     *
     * @param value
     *     allowed object is
     *     {@link NazivAmandmana }
     *
     */
    public void setNaziv(NazivAmandmana value) {
        this.naziv = value;
    }

    /**
     * Gets the value of the sadrzaj property.
     *
     * @return
     *     possible object is
     *     {@link Sadrzaj }
     *
     */
    public Sadrzaj getSadrzaj() {
        return sadrzaj;
    }

    /**
     * Sets the value of the sadrzaj property.
     *
     * @param value
     *     allowed object is
     *     {@link Sadrzaj }
     *
     */
    public void setSadrzaj(Sadrzaj value) {
        this.sadrzaj = value;
    }

}
