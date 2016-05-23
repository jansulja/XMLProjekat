//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.05.22 at 01:59:57 PM CEST 
//

package model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

/**
 * <p>
 * Java class for Uvodni_deo complex type.
 * 
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 * 
 * <pre>
 * &lt;complexType name="Uvodni_deo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Predmet_uredjivanja" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Definicija" maxOccurs="unbounded" minOccurs="0">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;minLength value="10"/>
 *               &lt;maxLength value="50"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *       &lt;attribute name="naziv">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;enumeration value="Uvodne odredbe"/>
 *             &lt;enumeration value="Osnovne odredbe"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Uvodni_deo", propOrder = { "predmetUredjivanja", "definicija" })
@Entity
@Table(name = "uvodni_deo")
public class UvodniDeo {

	@Id
	@Column(name = "uvodni_deo_id")
	@GeneratedValue
	private Integer id;

	@XmlElement(name = "Predmet_uredjivanja", required = true)
	protected String predmetUredjivanja;
	@XmlElement(name = "Definicija")
	@ElementCollection
	@CollectionTable(name = "Definicija", joinColumns = @JoinColumn(name = "akt_id"))
	@Column(name = "definicija")
	protected List<String> definicija;
	@XmlAttribute(name = "naziv")
	protected String naziv;

	

	public UvodniDeo() {
		super();
	}

	

	
	

	public Integer getId() {
		return id;
	}






	public void setId(Integer id) {
		this.id = id;
	}






	public UvodniDeo(Integer id, String predmetUredjivanja, List<String> definicija, String naziv) {
		super();
		this.id = id;
		this.predmetUredjivanja = predmetUredjivanja;
		this.definicija = definicija;
		this.naziv = naziv;
	}



	public void setDefinicija(List<String> definicija) {
		this.definicija = definicija;
	}

	/**
	 * Gets the value of the predmetUredjivanja property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getPredmetUredjivanja() {
		return predmetUredjivanja;
	}

	/**
	 * Sets the value of the predmetUredjivanja property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setPredmetUredjivanja(String value) {
		this.predmetUredjivanja = value;
	}

	/**
	 * Gets the value of the definicija property.
	 * 
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the definicija property.
	 * 
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getDefinicija().add(newItem);
	 * </pre>
	 * 
	 * 
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link String }
	 * 
	 * 
	 */
	public List<String> getDefinicija() {
		if (definicija == null) {
			definicija = new ArrayList<String>();
		}
		return this.definicija;
	}

	/**
	 * Gets the value of the naziv property.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getNaziv() {
		return naziv;
	}

	/**
	 * Sets the value of the naziv property.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setNaziv(String value) {
		this.naziv = value;
	}

}
