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
 * <p>Java class for NasjonalArealplanId complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="NasjonalArealplanId">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nummer" type="{http://rep.geointegrasjon.no/Plan/Felles/xml.schema/2012.01.31}Administrativenhetsnummer"/>
 *         &lt;element name="planidentifikasjon" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NasjonalArealplanId", namespace = "http://rep.geointegrasjon.no/Plan/Felles/xml.schema/2012.01.31", propOrder = {
        "nummer",
        "planidentifikasjon"
})
public class NasjonalArealplanId {

    @XmlElement(required = true)
    protected Administrativenhetsnummer nummer;
    @XmlElement(required = true)
    protected String planidentifikasjon;

    /**
     * Gets the value of the nummer property.
     *
     * @return possible object is
     * {@link Administrativenhetsnummer }
     */
    public Administrativenhetsnummer getNummer() {
        return nummer;
    }

    /**
     * Sets the value of the nummer property.
     *
     * @param value allowed object is
     *              {@link Administrativenhetsnummer }
     */
    public void setNummer(Administrativenhetsnummer value) {
        this.nummer = value;
    }

    /**
     * Gets the value of the planidentifikasjon property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPlanidentifikasjon() {
        return planidentifikasjon;
    }

    /**
     * Sets the value of the planidentifikasjon property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPlanidentifikasjon(String value) {
        this.planidentifikasjon = value;
    }

}
