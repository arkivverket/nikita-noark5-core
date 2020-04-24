//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.04.24 at 01:19:55 PM CEST 
//


package nikita.common.rep;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Tilleggsinformasjon complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="Tilleggsinformasjon">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="systemID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="rekkefoelge" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="informasjonstype" type="{http://rep.geointegrasjon.no/Arkiv/Kjerne/xml.schema/2012.01.31}Informasjonstype"/>
 *         &lt;element name="tilgangsrestriksjon" type="{http://rep.geointegrasjon.no/Arkiv/Kjerne/xml.schema/2012.01.31}Tilgangsrestriksjon" minOccurs="0"/>
 *         &lt;element name="oppbevaresTilDato" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="informasjon" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="tilgangsgruppeNavn" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="registrertDato" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *         &lt;element name="registrertAv" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="registrertAvInit" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tilleggsinformasjon", namespace = "http://rep.geointegrasjon.no/Arkiv/Kjerne/xml.schema/2012.01.31", propOrder = {
        "systemID",
        "rekkefoelge",
        "informasjonstype",
        "tilgangsrestriksjon",
        "oppbevaresTilDato",
        "informasjon",
        "tilgangsgruppeNavn",
        "registrertDato",
        "registrertAv",
        "registrertAvInit"
})
public class Tilleggsinformasjon {

    protected String systemID;
    protected String rekkefoelge;
    @XmlElement(required = true)
    protected Informasjonstype informasjonstype;
    protected Tilgangsrestriksjon tilgangsrestriksjon;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar oppbevaresTilDato;
    @XmlElement(required = true)
    protected String informasjon;
    protected String tilgangsgruppeNavn;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar registrertDato;
    protected String registrertAv;
    protected String registrertAvInit;

    /**
     * Gets the value of the systemID property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSystemID() {
        return systemID;
    }

    /**
     * Sets the value of the systemID property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSystemID(String value) {
        this.systemID = value;
    }

    /**
     * Gets the value of the rekkefoelge property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRekkefoelge() {
        return rekkefoelge;
    }

    /**
     * Sets the value of the rekkefoelge property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRekkefoelge(String value) {
        this.rekkefoelge = value;
    }

    /**
     * Gets the value of the informasjonstype property.
     *
     * @return possible object is
     * {@link Informasjonstype }
     */
    public Informasjonstype getInformasjonstype() {
        return informasjonstype;
    }

    /**
     * Sets the value of the informasjonstype property.
     *
     * @param value allowed object is
     *              {@link Informasjonstype }
     */
    public void setInformasjonstype(Informasjonstype value) {
        this.informasjonstype = value;
    }

    /**
     * Gets the value of the tilgangsrestriksjon property.
     *
     * @return possible object is
     * {@link Tilgangsrestriksjon }
     */
    public Tilgangsrestriksjon getTilgangsrestriksjon() {
        return tilgangsrestriksjon;
    }

    /**
     * Sets the value of the tilgangsrestriksjon property.
     *
     * @param value allowed object is
     *              {@link Tilgangsrestriksjon }
     */
    public void setTilgangsrestriksjon(Tilgangsrestriksjon value) {
        this.tilgangsrestriksjon = value;
    }

    /**
     * Gets the value of the oppbevaresTilDato property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getOppbevaresTilDato() {
        return oppbevaresTilDato;
    }

    /**
     * Sets the value of the oppbevaresTilDato property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setOppbevaresTilDato(XMLGregorianCalendar value) {
        this.oppbevaresTilDato = value;
    }

    /**
     * Gets the value of the informasjon property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getInformasjon() {
        return informasjon;
    }

    /**
     * Sets the value of the informasjon property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setInformasjon(String value) {
        this.informasjon = value;
    }

    /**
     * Gets the value of the tilgangsgruppeNavn property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTilgangsgruppeNavn() {
        return tilgangsgruppeNavn;
    }

    /**
     * Sets the value of the tilgangsgruppeNavn property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTilgangsgruppeNavn(String value) {
        this.tilgangsgruppeNavn = value;
    }

    /**
     * Gets the value of the registrertDato property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getRegistrertDato() {
        return registrertDato;
    }

    /**
     * Sets the value of the registrertDato property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setRegistrertDato(XMLGregorianCalendar value) {
        this.registrertDato = value;
    }

    /**
     * Gets the value of the registrertAv property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRegistrertAv() {
        return registrertAv;
    }

    /**
     * Sets the value of the registrertAv property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRegistrertAv(String value) {
        this.registrertAv = value;
    }

    /**
     * Gets the value of the registrertAvInit property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRegistrertAvInit() {
        return registrertAvInit;
    }

    /**
     * Sets the value of the registrertAvInit property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRegistrertAvInit(String value) {
        this.registrertAvInit = value;
    }

}
