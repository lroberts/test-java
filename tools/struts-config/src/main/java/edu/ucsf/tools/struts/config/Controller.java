//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.10.23 at 09:24:42 AM PDT 
//


package edu.ucsf.tools.struts.config;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


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
 *         &lt;element ref="{}set-property" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="bufferSize" type="{}Integer" />
 *       &lt;attribute name="className" type="{}ClassName" />
 *       &lt;attribute name="contentType" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="forwardPattern" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="inputForward" type="{}Boolean" />
 *       &lt;attribute name="locale" type="{}Boolean" />
 *       &lt;attribute name="maxFileSize" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="memFileSize" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="multipartClass" type="{}ClassName" />
 *       &lt;attribute name="nocache" type="{}Boolean" />
 *       &lt;attribute name="pagePattern" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="processorClass" type="{}ClassName" />
 *       &lt;attribute name="tempDir" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="catalog" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="command" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "setProperty"
})
@XmlRootElement(name = "controller")
public class Controller {

    @XmlElement(name = "set-property")
    protected List<SetProperty> setProperty;
    @XmlAttribute(name = "id")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;
    @XmlAttribute(name = "bufferSize")
    protected String bufferSize;
    @XmlAttribute(name = "className")
    protected String className;
    @XmlAttribute(name = "contentType")
    @XmlSchemaType(name = "anySimpleType")
    protected String contentType;
    @XmlAttribute(name = "forwardPattern")
    @XmlSchemaType(name = "anySimpleType")
    protected String forwardPattern;
    @XmlAttribute(name = "inputForward")
    protected Boolean inputForward;
    @XmlAttribute(name = "locale")
    protected Boolean locale;
    @XmlAttribute(name = "maxFileSize")
    @XmlSchemaType(name = "anySimpleType")
    protected String maxFileSize;
    @XmlAttribute(name = "memFileSize")
    @XmlSchemaType(name = "anySimpleType")
    protected String memFileSize;
    @XmlAttribute(name = "multipartClass")
    protected String multipartClass;
    @XmlAttribute(name = "nocache")
    protected Boolean nocache;
    @XmlAttribute(name = "pagePattern")
    @XmlSchemaType(name = "anySimpleType")
    protected String pagePattern;
    @XmlAttribute(name = "processorClass")
    protected String processorClass;
    @XmlAttribute(name = "tempDir")
    @XmlSchemaType(name = "anySimpleType")
    protected String tempDir;
    @XmlAttribute(name = "catalog")
    @XmlSchemaType(name = "anySimpleType")
    protected String catalog;
    @XmlAttribute(name = "command")
    @XmlSchemaType(name = "anySimpleType")
    protected String command;

    /**
     * Gets the value of the setProperty property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the setProperty property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSetProperty().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SetProperty }
     * 
     * 
     */
    public List<SetProperty> getSetProperty() {
        if (setProperty == null) {
            setProperty = new ArrayList<SetProperty>();
        }
        return this.setProperty;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the bufferSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBufferSize() {
        return bufferSize;
    }

    /**
     * Sets the value of the bufferSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBufferSize(String value) {
        this.bufferSize = value;
    }

    /**
     * Gets the value of the className property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets the value of the className property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setClassName(String value) {
        this.className = value;
    }

    /**
     * Gets the value of the contentType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the value of the contentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContentType(String value) {
        this.contentType = value;
    }

    /**
     * Gets the value of the forwardPattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getForwardPattern() {
        return forwardPattern;
    }

    /**
     * Sets the value of the forwardPattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setForwardPattern(String value) {
        this.forwardPattern = value;
    }

    /**
     * Gets the value of the inputForward property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean getInputForward() {
        return inputForward;
    }

    /**
     * Sets the value of the inputForward property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setInputForward(Boolean value) {
        this.inputForward = value;
    }

    /**
     * Gets the value of the locale property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean getLocale() {
        return locale;
    }

    /**
     * Sets the value of the locale property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setLocale(Boolean value) {
        this.locale = value;
    }

    /**
     * Gets the value of the maxFileSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMaxFileSize() {
        return maxFileSize;
    }

    /**
     * Sets the value of the maxFileSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMaxFileSize(String value) {
        this.maxFileSize = value;
    }

    /**
     * Gets the value of the memFileSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMemFileSize() {
        return memFileSize;
    }

    /**
     * Sets the value of the memFileSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMemFileSize(String value) {
        this.memFileSize = value;
    }

    /**
     * Gets the value of the multipartClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMultipartClass() {
        return multipartClass;
    }

    /**
     * Sets the value of the multipartClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMultipartClass(String value) {
        this.multipartClass = value;
    }

    /**
     * Gets the value of the nocache property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean getNocache() {
        return nocache;
    }

    /**
     * Sets the value of the nocache property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNocache(Boolean value) {
        this.nocache = value;
    }

    /**
     * Gets the value of the pagePattern property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPagePattern() {
        return pagePattern;
    }

    /**
     * Sets the value of the pagePattern property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPagePattern(String value) {
        this.pagePattern = value;
    }

    /**
     * Gets the value of the processorClass property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProcessorClass() {
        return processorClass;
    }

    /**
     * Sets the value of the processorClass property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProcessorClass(String value) {
        this.processorClass = value;
    }

    /**
     * Gets the value of the tempDir property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTempDir() {
        return tempDir;
    }

    /**
     * Sets the value of the tempDir property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTempDir(String value) {
        this.tempDir = value;
    }

    /**
     * Gets the value of the catalog property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCatalog() {
        return catalog;
    }

    /**
     * Sets the value of the catalog property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCatalog(String value) {
        this.catalog = value;
    }

    /**
     * Gets the value of the command property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCommand() {
        return command;
    }

    /**
     * Sets the value of the command property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCommand(String value) {
        this.command = value;
    }

}
