//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.06.11 at 06:39:49 PM CEST 
//


package model;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Status.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Status">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="PREDLOZEN"/>
 *     &lt;enumeration value="USVOJEN_U_NACELU"/>
 *     &lt;enumeration value="USVOJEN_U_POJEDINOSTIMA"/>
 *     &lt;enumeration value="USVOJEN_U_CELINI"/>
 *     &lt;enumeration value="ODBIJEN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Status")
@XmlEnum
public enum Status {

    PREDLOZEN,
    USVOJEN_U_NACELU,
    USVOJEN_U_POJEDINOSTIMA,
    USVOJEN_U_CELINI,
    ODBIJEN;

    public String value() {
        return name();
    }

    public static Status fromValue(String v) {
        return valueOf(v);
    }

}
