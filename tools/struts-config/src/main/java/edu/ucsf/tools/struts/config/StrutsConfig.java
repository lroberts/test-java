//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v@@BUILD_VERSION@@ 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.01.23 at 10:47:29 AM PST 
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
 *         &lt;element ref="{}display-name" minOccurs="0"/>
 *         &lt;element ref="{}description" minOccurs="0"/>
 *         &lt;element ref="{}form-beans" minOccurs="0"/>
 *         &lt;element ref="{}global-exceptions" minOccurs="0"/>
 *         &lt;element ref="{}global-forwards" minOccurs="0"/>
 *         &lt;element ref="{}action-mappings" minOccurs="0"/>
 *         &lt;element ref="{}controller" minOccurs="0"/>
 *         &lt;element ref="{}message-resources" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{}plug-in" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "displayName",
    "description",
    "formBeans",
    "globalExceptions",
    "globalForwards",
    "actionMappings",
    "controller",
    "messageResources",
    "plugIn"
})
@XmlRootElement(name = "struts-config")
public class StrutsConfig {

    @XmlElement(name = "display-name")
    protected DisplayName displayName;
    protected Description description;
    @XmlElement(name = "form-beans")
    protected FormBeans formBeans;
    @XmlElement(name = "global-exceptions")
    protected GlobalExceptions globalExceptions;
    @XmlElement(name = "global-forwards")
    protected GlobalForwards globalForwards;
    @XmlElement(name = "action-mappings")
    protected ActionMappings actionMappings;
    protected Controller controller;
    @XmlElement(name = "message-resources")
    protected List<MessageResources> messageResources;
    @XmlElement(name = "plug-in")
    protected List<PlugIn> plugIn;
    @XmlAttribute
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    @XmlID
    @XmlSchemaType(name = "ID")
    protected String id;

    /**
     * Gets the value of the displayName property.
     * 
     * @return
     *     possible object is
     *     {@link DisplayName }
     *     
     */
    public DisplayName getDisplayName() {
        return displayName;
    }

    /**
     * Sets the value of the displayName property.
     * 
     * @param value
     *     allowed object is
     *     {@link DisplayName }
     *     
     */
    public void setDisplayName(DisplayName value) {
        this.displayName = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link Description }
     *     
     */
    public Description getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link Description }
     *     
     */
    public void setDescription(Description value) {
        this.description = value;
    }

    /**
     * Gets the value of the formBeans property.
     * 
     * @return
     *     possible object is
     *     {@link FormBeans }
     *     
     */
    public FormBeans getFormBeans() {
        return formBeans;
    }

    /**
     * Sets the value of the formBeans property.
     * 
     * @param value
     *     allowed object is
     *     {@link FormBeans }
     *     
     */
    public void setFormBeans(FormBeans value) {
        this.formBeans = value;
    }

    /**
     * Gets the value of the globalExceptions property.
     * 
     * @return
     *     possible object is
     *     {@link GlobalExceptions }
     *     
     */
    public GlobalExceptions getGlobalExceptions() {
        return globalExceptions;
    }

    /**
     * Sets the value of the globalExceptions property.
     * 
     * @param value
     *     allowed object is
     *     {@link GlobalExceptions }
     *     
     */
    public void setGlobalExceptions(GlobalExceptions value) {
        this.globalExceptions = value;
    }

    /**
     * Gets the value of the globalForwards property.
     * 
     * @return
     *     possible object is
     *     {@link GlobalForwards }
     *     
     */
    public GlobalForwards getGlobalForwards() {
        return globalForwards;
    }

    /**
     * Sets the value of the globalForwards property.
     * 
     * @param value
     *     allowed object is
     *     {@link GlobalForwards }
     *     
     */
    public void setGlobalForwards(GlobalForwards value) {
        this.globalForwards = value;
    }

    /**
     * Gets the value of the actionMappings property.
     * 
     * @return
     *     possible object is
     *     {@link ActionMappings }
     *     
     */
    public ActionMappings getActionMappings() {
        return actionMappings;
    }

    /**
     * Sets the value of the actionMappings property.
     * 
     * @param value
     *     allowed object is
     *     {@link ActionMappings }
     *     
     */
    public void setActionMappings(ActionMappings value) {
        this.actionMappings = value;
    }

    /**
     * Gets the value of the controller property.
     * 
     * @return
     *     possible object is
     *     {@link Controller }
     *     
     */
    public Controller getController() {
        return controller;
    }

    /**
     * Sets the value of the controller property.
     * 
     * @param value
     *     allowed object is
     *     {@link Controller }
     *     
     */
    public void setController(Controller value) {
        this.controller = value;
    }

    /**
     * Gets the value of the messageResources property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the messageResources property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMessageResources().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link MessageResources }
     * 
     * 
     */
    public List<MessageResources> getMessageResources() {
        if (messageResources == null) {
            messageResources = new ArrayList<MessageResources>();
        }
        return this.messageResources;
    }

    /**
     * Gets the value of the plugIn property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the plugIn property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPlugIn().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PlugIn }
     * 
     * 
     */
    public List<PlugIn> getPlugIn() {
        if (plugIn == null) {
            plugIn = new ArrayList<PlugIn>();
        }
        return this.plugIn;
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

}
