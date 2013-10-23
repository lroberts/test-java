package edu.ucsf.sis.chartfield;

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

import javax.xml.bind.JAXBElement;

import java.sql.*;
import javax.sql.DataSource;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import java.io.*;
import javax.naming.Context;

import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;

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

//import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
//import com.sun.xml.bind.IDResolver;


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

import java.io.ByteArrayInputStream;
import java.net.Socket;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpClientConnection;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.params.SyncBasicHttpParams;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpRequestExecutor;
import org.apache.http.protocol.ImmutableHttpProcessor;
import org.apache.http.protocol.RequestConnControl;
import org.apache.http.protocol.RequestContent;
import org.apache.http.protocol.RequestExpectContinue;
import org.apache.http.protocol.RequestTargetHost;
import org.apache.http.protocol.RequestUserAgent;
import org.apache.http.util.EntityUtils;

import org.apache.http.impl.client.DefaultHttpClient;


import org.apache.http.client.methods.*;

// both javax.net.ssl.* and org.apache.http.conn.ssl have an SSLSocketFactory
//import javax.net.ssl.*;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.SSLSocket;
import org.apache.http.conn.scheme.*;
// we use this one
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.*;
import org.apache.http.conn.*;
import org.apache.http.impl.conn.*;
import org.apache.http.params.*;
import java.security.SecureRandom;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;

public class TLSSocketFactory extends SSLSocketFactory {
    
    private final javax.net.ssl.SSLSocketFactory socketfactory;
    
    public TLSSocketFactory(SSLContext sslContext) {
	super(sslContext);
	
	this.socketfactory = sslContext.getSocketFactory();
    }
    
    public Socket createSocket() throws IOException {
	SSLSocket socket = (SSLSocket) super.createSocket();
	
	    socket.setEnabledProtocols(new String[] {"SSLv3, TLSv1"});
	    
	    return socket;
    }
    
    public Socket createSocket(
			       final Socket socket,
			       final String host,
			       final int port,
			       final boolean autoClose
			       ) throws IOException, UnknownHostException {
	
	SSLSocket sslSocket = (SSLSocket) this.socketfactory.createSocket(
									  socket,
									  host,
									  port,
									  autoClose
									  );
	
	sslSocket.setEnabledProtocols(new String[] {"SSLv3", "TLSv1"});
	
	getHostnameVerifier().verify(host, sslSocket);
	
	return sslSocket;
    }
}
 
