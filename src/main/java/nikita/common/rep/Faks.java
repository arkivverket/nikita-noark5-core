//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.04.24 at 01:19:55 PM CEST 
//


package nikita.common.rep;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Faks complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Faks">
 *   &lt;complexContent>
 *     &lt;extension base="{http://rep.geointegrasjon.no/Felles/Adresse/xml.schema/2012.01.31}ElektroniskAdresse">
 *       &lt;sequence>
 *         &lt;element name="faksnummer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Faks", namespace = "http://rep.geointegrasjon.no/Felles/Adresse/xml.schema/2012.01.31", propOrder = {
        "faksnummer"
})
public class Faks
        extends ElektroniskAdresse {

    @XmlElement(required = true)
    protected String faksnummer;

    /**
     * Gets the value of the faksnummer property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFaksnummer() {
        return faksnummer;
    }

    /**
     * Sets the value of the faksnummer property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFaksnummer(String value) {
        this.faksnummer = value;
    }

}
