package model.amandman;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "Status_amandmana")
@XmlEnum
public enum StatusAmandmana {

    PREDLOZEN,
    USVOJEN,
    ODBIJEN;

    public String value() {
        return name();
    }

    public static StatusAmandmana fromValue(String v) {
        return valueOf(v);
    }

}

