//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.05.22 at 01:59:57 PM CEST 
//


package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Glavni_deo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Glavni_deo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Prava_i_obaveze" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Ovlascenje" type="{}Ovlascenje" maxOccurs="unbounded"/>
 *         &lt;element name="Kaznena_odredba" type="{}Kaznena_odredba" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Glavni_deo", propOrder = {
    "pravaIObaveze",
    "ovlascenje",
    "kaznenaOdredba"
})
public class GlavniDeo {

	@Id
	@Column(name = "glavni_deo_id")
	@GeneratedValue
	private Integer id;
	
    @XmlElement(name = "Prava_i_obaveze", required = true)
    protected String pravaIObaveze;

    @XmlElement(name = "Ovlascenje", required = true)
    @OneToMany(cascade = {CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "glavniDeo")
    protected List<Ovlascenje> ovlascenje;
    @XmlElement(name = "Kaznena_odredba")
    protected List<KaznenaOdredba> kaznenaOdredba;

    /**
     * Gets the value of the pravaIObaveze property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPravaIObaveze() {
        return pravaIObaveze;
    }

    /**
     * Sets the value of the pravaIObaveze property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPravaIObaveze(String value) {
        this.pravaIObaveze = value;
    }

    /**
     * Gets the value of the ovlascenje property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ovlascenje property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getOvlascenje().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Ovlascenje }
     * 
     * 
     */
    public List<Ovlascenje> getOvlascenje() {
        if (ovlascenje == null) {
            ovlascenje = new ArrayList<Ovlascenje>();
        }
        return this.ovlascenje;
    }

    /**
     * Gets the value of the kaznenaOdredba property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the kaznenaOdredba property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getKaznenaOdredba().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link KaznenaOdredba }
     * 
     * 
     */
    public List<KaznenaOdredba> getKaznenaOdredba() {
        if (kaznenaOdredba == null) {
            kaznenaOdredba = new ArrayList<KaznenaOdredba>();
        }
        return this.kaznenaOdredba;
    }

}
