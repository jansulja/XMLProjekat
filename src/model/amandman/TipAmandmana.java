//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.21 at 12:00:31 AM CEST 
//


package model.amandman;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Tip_amandmana.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Tip_amandmana">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="DODAJ"/>
 *     &lt;enumeration value="PROMENI"/>
 *     &lt;enumeration value="OBRISI"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Tip_amandmana")
@XmlEnum
public enum TipAmandmana {

    DODAJ,
    PROMENI,
    OBRISI;

    public String value() {
        return name();
    }

    public static TipAmandmana fromValue(String v) {
        return valueOf(v);
    }

}
