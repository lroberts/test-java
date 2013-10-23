import org.junit.*;
import java.net.*;
import java.util.Vector;
import java.io.*;
import java.util.*;
import java.nio.*;
import java.nio.charset.*;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.PropertyConfigurator;

import java.sql.*;
import javax.sql.DataSource;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import java.io.*;
import javax.naming.Context;

import edu.ucsf.sis.admit.IDSystemConnector;
import edu.ucsf.saa.ConnectionFactory;

//import edu.ucsf.sunapsis.*;

import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import javax.xml.XMLConstants;
                                                                            
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xml.serialize.OutputFormat;
import javax.xml.bind.util.JAXBSource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationException;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.util.JAXBResult;
//import javax.xml.bind.Validator;                                                             

import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import com.sun.xml.bind.IDResolver;


import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.ValidatorHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;

import java.math.BigDecimal;

import edu.ucsf.sis.feed.*;

public class FeedTest
{
    static {
	BasicConfigurator.configure();

	Logger rootLogger = Logger.getRootLogger();
	if (!rootLogger.getAllAppenders().hasMoreElements()) {
	    rootLogger.setLevel(Level.INFO);
	}
    }

    private Logger log = Logger.getLogger(this.getClass());


    @Test
    public void checkConnection()
	throws Exception
    {
	SimpleFeed feed = new SimpleFeed();
	feed.createFeed();
    }

    class SimpleFeed extends StudentFeed
    {

	public void configureFeed()
	{
	    try{
		Properties props = new Properties();
		props.load(FeedTest.class.getResourceAsStream("/testfeed-log4j.properties"));
		PropertyConfigurator.configure(props);
		
	    }catch(Exception ex){
		System.err.println("Unable to configure logging: "+ex);
	    }
	}

	public Map createRecord(Row stu, Row tstu)
	    throws Exception
	{	    
	    // we must insert values in the correct order
	    Map rec = new LinkedHashMap();
	    String empno = (String)stu.get("empno");
	    //	    rec.put("UnivId", stu.get("userid"));
	    String cardId = RFID_BY_EMPNO.get(empno.trim());
	    if(cardId == null){
		log.error("Unable to find cardId for empno: ["+empno+"]");
	    }
	    
	    String last = (String)stu.get("lastname");
	    if(last != null) last = last.replaceAll(","," ");
	    
	    String first = (String)stu.get("firstname");
	    if(first != null) first = first.replaceAll(","," ");
	    
	    String preferred = (String)stu.get("preferredname");
	    if(preferred != null) preferred = preferred.replaceAll(","," ");
	    
	    String middle = (String)stu.get("midname");
	    if(middle != null) middle = middle.replaceAll(","," ");
	    
	    rec.put("UnivId", cardId);
	    rec.put("AltUnivID", empno);
	    //	    rec.(""+stu.get("idnum")); // or userid? or empno?
	    rec.put("LastName", last);  //
	    rec.put("FirstName", first);  //
	    rec.put("PreferredName", preferred);  //
	    rec.put("MiddleName", middle);  //			
	
	    rec.put("Gender", stu.get("sex"));  // M or F or U (we should check)
		
	    return rec;	    
	}
    }

}