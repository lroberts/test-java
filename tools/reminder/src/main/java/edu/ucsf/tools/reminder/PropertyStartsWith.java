package edu.ucsf.tools.reminder;

import java.net.URL;
import java.io.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.DriverManager;
import java.sql.Driver;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;
import java.text.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.*;
import javax.activation.*;
import javax.mail.util.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.*;

import org.apache.log4j.*;

import net.fortuna.ical4j.data.CalendarBuilder;
import net.fortuna.ical4j.model.*;
import net.fortuna.ical4j.model.property.RecurrenceId;
import net.fortuna.ical4j.model.property.Summary;
import net.fortuna.ical4j.model.component.*;
import net.fortuna.ical4j.filter.*;

    public class PropertyStartsWith extends ComponentRule {

	private Property property;

	private boolean matchEquals;

	/**
	 * Constructs a new instance with the specified property. Ignores any parameters matching only on the value of the
	 * property.
	 * @param property
	 */
	public PropertyStartsWith(final Property property) {
	    this(property, false);
	}

	/**
	 * Constructs a new instance with the specified property.
	 * @param property the property to match
	 * @param matchEquals if true, matches must contain an identical property (as indicated by
	 * <code>Property.equals()</code>
	 */
	public PropertyStartsWith(final Property property, final boolean matchEquals) {
	    this.property = property;
	    this.matchEquals = matchEquals;
	}

	/*
	 * (non-Javadoc)
	 * @see net.fortuna.ical4j.filter.ComponentRule#match(net.fortuna.ical4j.model.Component)
	 */
	public final boolean match(final Component component) {
	    PropertyList properties = component.getProperties(property.getName());
	    for (Iterator i = properties.iterator(); i.hasNext();) {
		Property p = (Property) i.next();
		//if (matchEquals && property.equals(p)) {
		//  return true;
		//}
		//else 
		    if ((p.getValue() != null) && p.getValue().toString().startsWith(property.getValue())) {
		    return true;
		}
	    }
	    return false;
	}
    }
