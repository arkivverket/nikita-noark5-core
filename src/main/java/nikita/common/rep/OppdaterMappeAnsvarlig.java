//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.04.24 at 01:19:55 PM CEST 
//


package nikita.common.rep;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="nyAdministrativEnhetKode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="nySaksbehandlerInit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="saksnokkel" type="{http://rep.geointegrasjon.no/Arkiv/Felles/xml.schema/2012.01.31}Saksnoekkel" minOccurs="0"/>
 *         &lt;element name="kontekst" type="{http://rep.geointegrasjon.no/Felles/Teknisk/xml.schema/2012.01.31}ArkivKontekst" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "nyAdministrativEnhetKode",
        "nySaksbehandlerInit",
        "saksnokkel",
        "kontekst"
})
@XmlRootElement(name = "OppdaterMappeAnsvarlig")
public class OppdaterMappeAnsvarlig {

    protected String nyAdministrativEnhetKode;
    protected String nySaksbehandlerInit;
    protected Saksnoekkel saksnokkel;
    protected ArkivKontekst kontekst;

    /**
     * Gets the value of the nyAdministrativEnhetKode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNyAdministrativEnhetKode() {
        return nyAdministrativEnhetKode;
    }

    /**
     * Sets the value of the nyAdministrativEnhetKode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNyAdministrativEnhetKode(String value) {
        this.nyAdministrativEnhetKode = value;
    }

    /**
     * Gets the value of the nySaksbehandlerInit property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getNySaksbehandlerInit() {
        return nySaksbehandlerInit;
    }

    /**
     * Sets the value of the nySaksbehandlerInit property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setNySaksbehandlerInit(String value) {
        this.nySaksbehandlerInit = value;
    }

    /**
     * Gets the value of the saksnokkel property.
     *
     * @return possible object is
     * {@link Saksnoekkel }
     */
    public Saksnoekkel getSaksnokkel() {
        return saksnokkel;
    }

    /**
     * Sets the value of the saksnokkel property.
     *
     * @param value allowed object is
     *              {@link Saksnoekkel }
     */
    public void setSaksnokkel(Saksnoekkel value) {
        this.saksnokkel = value;
    }

    /**
     * Gets the value of the kontekst property.
     *
     * @return possible object is
     * {@link ArkivKontekst }
     */
    public ArkivKontekst getKontekst() {
        return kontekst;
    }

    /**
     * Sets the value of the kontekst property.
     *
     * @param value allowed object is
     *              {@link ArkivKontekst }
     */
    public void setKontekst(ArkivKontekst value) {
        this.kontekst = value;
    }

}
