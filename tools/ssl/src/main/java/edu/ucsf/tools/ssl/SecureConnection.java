package edu.ucsf.tools.ssl;

import java.net.*;
import java.util.Vector;
import java.io.*;
import java.util.*;
import java.nio.*;
import java.nio.charset.*;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
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

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.*;

// both javax.net.ssl.* and org.apache.http.conn.ssl have an SSLSocketFactory
//import javax.net.ssl.*;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;


import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.spec.*;

import org.apache.http.conn.scheme.*;
// we use this one
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.*;
import org.apache.http.conn.*;
import org.apache.http.impl.conn.*;
import org.apache.http.params.*;

public class SecureConnection
{
    static {
	//BasicConfigurator.configure();

	try{
	    Properties props = new Properties();
	    props.load(SecureConnection.class.getResourceAsStream("/ssl-log4j.properties"));
	    PropertyConfigurator.configure(props);

	}catch(Exception ex){
	    System.err.println("Unable to configure logging: "+ex);
	}
    }

    static
    private Logger log = Logger.getLogger(SecureConnection.class);

    //    public ObjectFactory factory = new ObjectFactory();

    private File outputDir;
    private File keystoreFile;
    private String keystorePassword;

    private KeyStore keystore;
    private char[] trustPass;

    /**
     *  A constructor to take a key and a certificate file which must be
     * in <code>DER</code>-format. The key must be
     * encoded with <code>PKCS#8</code>-format. The certificate must be
     * encoded in <code>X.509</code>-format.</p>
     */
    public SecureConnection(InputStream keystream, InputStream crtStream)
	throws Exception
    {

	File ksf = File.createTempFile("ssl",".keystore");
	String keystorename = ksf.getAbsolutePath();
	String alias = "tempkey";
	String keypass = "changeit";
	char[] pass = keypass.toCharArray();

        try {

	    log.info("Writing temp keystore to "+ksf);
            // initializing and clearing keystore 
            KeyStore ks = KeyStore.getInstance("JKS", "SUN");
            ks.load( null , pass);
            System.out.println("Using keystore-file : "+keystorename);
            ks.store(new FileOutputStream(keystorename), pass);
            ks.load(new FileInputStream(keystorename), pass);

            // loading Key
            InputStream fl = this.asByteStream(keystream);
            byte[] key = new byte[fl.available()];
            KeyFactory kf = KeyFactory.getInstance("RSA");
            fl.read ( key, 0, fl.available() );
            fl.close();
	    System.out.println("Loaded key ");
	    PKCS8EncodedKeySpec keysp = new PKCS8EncodedKeySpec ( key );
	    //X509EncodedKeySpec keysp = new X509EncodedKeySpec ( key );
            PrivateKey ff = kf.generatePrivate (keysp);

            // loading CertificateChain
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            ByteArrayInputStream certstream = this.asByteStream(crtStream);
	    //	    certstream.mark(0);
            Collection c = cf.generateCertificates(certstream) ;
            Certificate[] certs = new Certificate[c.toArray().length];

            if (c.size() == 1) {
		certstream.reset();
                log.info("One certificate, no chain.");
                Certificate cert = cf.generateCertificate(certstream) ;
                certs[0] = cert;
            } else {
                log.info("Certificate chain length: "+c.size());
                certs = (Certificate[])c.toArray();
            }

            // storing keystore
            ks.setKeyEntry(alias, ff, pass, certs );
            log.info ("Key and certificate stored.");
            log.info ("Alias:"+alias+"  Password:"+keypass);
            ks.store(new FileOutputStream(keystorename), pass);
	    
        } catch (Exception ex) {
	    log.error("Unable to initialize temporary keystore: "+ex, ex);
	    throw new RuntimeException("Unable to initialize temporary keystore: "+ex);
        }

	init(ksf, keypass);
    }

    public SecureConnection(File ks, String ps)
	throws Exception
    {
	init(ks, ps);
    }

    private void init(File ks, String ps)
	throws Exception
    {
	// i'f you don't do this the default trust store will still
	// be the jdk version and so if you don't substitute our relaxed
	// trustmanager (above) when creating our sslcontext then the client
	// will refuse the connection (this happens after the 
	// sucessful/complete server hello). the apache log will likely 
	// say something like "Renegotiation rejected by client". [this
	// message, like many in the ssl handshake can appear for many reasons]

	// see the #getClient()
	System.setProperty("javax.net.ssl.keyStore", ks.getAbsolutePath());
	System.setProperty("javax.net.ssl.keyStorePassword", ps);
	System.setProperty("javax.net.ssl.trustStore", ks.getAbsolutePath());
	System.setProperty("javax.net.ssl.trustStorePassword",ps);
	// we don't need these unless you are on a pre 1.6.0_22 jdk
	//System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
	//System.setProperty("sun.security.ssl.allowLegacyHelloMessages","true");

	//	this.factory = new ObjectFactory();
	this.keystoreFile = ks;
	this.keystorePassword = ps;
	FileInputStream instream = new FileInputStream(this.keystoreFile);
	try {
	    this.keystore  = KeyStore.getInstance(KeyStore.getDefaultType());
	    
	    log.info("have instream: "+instream);
	    
	    this.trustPass = this.keystorePassword != null ? 
		this.keystorePassword.toCharArray(): null;

	    this.keystore.load(instream, trustPass);
	} finally {
	    try { instream.close(); } catch (Exception ignore) {}
	}

    }

    private ByteArrayInputStream asByteStream( String fname ) 
	throws IOException 
    {
        FileInputStream fis = new FileInputStream(fname);
	return asByteStream(fis);
    }

    private ByteArrayInputStream asByteStream( InputStream is ) 
	throws IOException 
    {
        DataInputStream dis = new DataInputStream(is);
        byte[] bytes = new byte[dis.available()];
        dis.readFully(bytes);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        return bais;
    }

    private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

    private static String asHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 3);
        for (int b : bytes) {
            b &= 0xff;
            sb.append(HEXDIGITS[b >> 4]);
            sb.append(HEXDIGITS[b & 15]);
            sb.append(' ');
        }
        return sb.toString();
    }

    public void trust(String host, int port)
	throws Exception
    {
        SSLContext context = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf =
	    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(this.keystore);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        SavingTrustManager stm = new SavingTrustManager(defaultTrustManager);
        context.init(null, new TrustManager[]{stm}, null);
        javax.net.ssl.SSLSocketFactory factory = context.getSocketFactory();

        log.info("Opening connection to " + host + ":" + port + "...");
        SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
        socket.setSoTimeout(10000);
        try {
            log.info("Starting SSL handshake...");
            socket.startHandshake();
            socket.close();
            log.info("No errors, certificate is already trusted");
        } catch (SSLException e) {
            e.printStackTrace(System.out);
        }

        X509Certificate[] chain = stm.chain;
        if (chain == null) {
            log.info("Could not obtain server certificate chain");
            return;
        }

        BufferedReader reader =
	    new BufferedReader(new InputStreamReader(System.in));

        log.info("Server sent " + chain.length + " certificate(s):");
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        for (int i = 0; i < chain.length; i++) {
            X509Certificate cert = chain[i];
            log.info
		(" " + (i + 1) + " Subject " + cert.getSubjectDN());
            log.info("   Issuer  " + cert.getIssuerDN());
            sha1.update(cert.getEncoded());
            log.info("   sha1    " + asHexString(sha1.digest()));
            md5.update(cert.getEncoded());
            log.info("   md5     " + asHexString(md5.digest()));
        }

	/*
        log.info("Enter certificate to add to trusted keystore or 'q' to quit: [1]");
        String line = reader.readLine().trim();
        int k;
        try {
            k = (line.length() == 0) ? 0 : Integer.parseInt(line) - 1;
        } catch (NumberFormatException e) {
            log.info("KeyStore not changed");
            return;
        }

        X509Certificate cert = chain[k];
        String alias = host + "-" + (k + 1);
        this.keystore.setCertificateEntry(alias, cert);
	*/
	// add all
        for (int q = 0; q < chain.length; q++) {
	    log.info(""+q);
	    X509Certificate cert = chain[q];
	    String alias = host + "-" + (q + 1);
	    this.keystore.setCertificateEntry(alias, cert);
	    //   log.info(cert);
	    log.info("Added certificate to keystore '"+this.keystoreFile+"' using alias '"
		     + alias + "'");
	}

	OutputStream out = new FileOutputStream(this.keystoreFile);
        this.keystore.store(out, this.trustPass);
        out.close();



    }

    public File getOutputDir(){
	return this.outputDir;
    }


    public void setOutputDir(File o){
	this.outputDir = o;
    }

    public void get(InputStream input, String url)
	throws Exception
    {
	HttpClient httpclient = this.getClient();
	HttpGet request = new HttpGet(url);
	HttpResponse response = httpclient.execute(request);
	HttpEntity entity = response.getEntity();
	log.info(EntityUtils.toString(entity));

	log.info("----------------------------------------");
	log.info(response.getStatusLine());
	if (entity != null) {
	    log.debug("Response content length: " + entity.getContentLength());
	}
	EntityUtils.consume(entity);

    }

    //    public void send(ZSICFVALREQUESTTypeShape doc, String url)
    public void get(String fullMsg, String url)
	throws Exception
    {
	//	JAXBElement<ZSICFVALREQUESTTypeShape> elem = this.factory.createZSICFVALREQUEST(doc);
	java.util.Date now = new java.util.Date();
	SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss_z");
	//File feed = this.saveFile(elem, "request-"+fmt.format(now));	
	//String insertText = asString(new FileInputStream(feed));
	//String mainText = asString(this.getClass().getResourceAsStream("/templates/psoft-message.txt"));
	
	//ByteArrayOutputStream baos = insert(insertText, mainText, "CDATA[");
	//String fullMsg = new String(baos.toByteArray());
	log.debug("full psoft msg is: "+fullMsg);
	InputStream testIn = new ByteArrayInputStream(fullMsg.getBytes());

	this.get(testIn, url);
    }

    public void post(InputStream input, String url)
	throws Exception
    {
	HttpClient httpclient = this.getClient();

	InputStreamEntity testEnt = new InputStreamEntity(input, -1);
	HttpPost request = new HttpPost(url);
	request.setEntity(testEnt);
	log.info(">> Request URI: " + request.getRequestLine().getUri());

	HttpParams params = new SyncBasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUserAgent(params, "HttpComponents/1.1");
        HttpProtocolParams.setUseExpectContinue(params, true);
	request.setParams(params);

	HttpResponse response = httpclient.execute(request);
	HttpEntity entity = response.getEntity();
	log.info(EntityUtils.toString(entity));

	log.info("----------------------------------------");
	log.info(response.getStatusLine());
	if (entity != null) {
	    log.debug("Response content length: " + entity.getContentLength());
	}
	EntityUtils.consume(entity);

    }
    
    public HttpClient getClient()
	throws Exception
    {
	KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(
								    KeyManagerFactory.getDefaultAlgorithm());
	kmfactory.init(this.keystore, this.trustPass);
	KeyManager[] keymanagers =kmfactory.getKeyManagers();

	SecureRandom random = new SecureRandom();
	SSLContext sctxt = SSLContext.getInstance("TLS");
	// we could sub our trust manager here if we trust the server
	// and do not wish to change the system properties for replacing
	// the default key and trust stores
	//sctxt.init(keymanagers, new X509TrustManager[]{tm}, random);
	sctxt.init(keymanagers, null, random);

	SSLSocketFactory sf = new SSLSocketFactory(
				   sctxt,
				   //SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER
				    SSLSocketFactory.STRICT_HOSTNAME_VERIFIER
				   );
	Scheme https = new Scheme("https", 443, sf);
	// set up to allow ssl
	HttpClient client = new DefaultHttpClient();
	SchemeRegistry sr = client.getConnectionManager().getSchemeRegistry();
	sr.register(https);

	return client;
    }

    static
    public String asString(InputStream textStream)
        throws IOException
    {
        BufferedReader br = new BufferedReader(new InputStreamReader(textStream));
        StringBuilder sb = new StringBuilder();
        String line = null;

        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }

        br.close();
        return sb.toString();
    }

    public File saveFile(Object o, String name)
	throws Exception
    {
	if(this.outputDir == null)
	    this.outputDir = new File("target");

	if(!this.outputDir.exists()){
	    log.warn("Creating output directory: "+this.outputDir);
	    this.outputDir.mkdirs();
	}

	File save = new File(this.outputDir, name+".xml");
	log.info("Saving file: "+save);
	FileOutputStream fos = new FileOutputStream(save);

	//saveAs(fos, o);

	//this.validate(save);
	return save;
    }
    /*
    public String dump(Object o)
	throws Exception
    {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	saveAs(baos, o);
	return baos.toString();
    }

    public void saveAs(OutputStream out, Object o)
        throws Exception
    {
        try{
	    JAXBContext context = null;
	    try{
		context = JAXBContext.newInstance( factory.getClass() );
	    }catch(Exception huh){
		log.warn("Unable to instantiate JAXBContext: "+huh);
		throw huh;
	    }
	    
	    Marshaller marshaller = context.createMarshaller(  );
	    marshaller.setProperty( "jaxb.formatted.output", new Boolean( true ) );

	    marshaller.marshal( o, out);


	
        }catch(Exception ex){
            throw new Exception("Unable to save as file: "+ex, ex);
        }
    

        }

    public Object validate(File file)
	throws Exception
    {
	SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

	List<Source> sourceList = new ArrayList();

	//	InputStream schemaStream = ObjectFactory.class.getResourceAsStream("/edu/ucsf/sis/chartfield/request/SIS_CFVAL_REQUEST.xsd");

	// Alternatively, you can get the actual text. May be useful at some point.    
	//String schemaText = StringTools.asString(schemaStream);                      
	//log.info("Have schema:"+schemaText);                                         
	//StreamSource schemaSource = new StreamSource(new StringReader(schemaText));  

	StreamSource schemaSource = new StreamSource(schemaStream);
	sourceList.add(schemaSource);

	log.info("Creating Schema with ["+sourceList.size()+"] component schemas.");
	Source[] sources = sourceList.toArray(new Source[]{});
	Schema schema = schemaFactory.newSchema(sources);
	JAXBContext context = JAXBContext.newInstance( "fixme");//ObjectFactory.class );
	
	Unmarshaller unmarshaller = context.createUnmarshaller(  );
	// ISSUE: THIS IS JUST FOR DEBUG                                                   
	//unmarshaller.setEventHandler( new EventValidator( contextType ) );

	unmarshaller.setSchema(schema);

	InputStream savedFile = new FileInputStream( file.getAbsolutePath() );
	return unmarshaller.unmarshal( savedFile );
    }
    */
    public XMLGregorianCalendar gDate(Object yourDate)
	throws Exception
    {
	return gDate((java.util.Date)yourDate);
    }

    /**
     *  IMPORTANT: the java calendar month is **0-based** but the 
     *  xmlgregoriancalendar month is 1-based
     */
    public XMLGregorianCalendar gDate(java.util.Date yourDate)
	throws Exception
    {
	//log.debug("INCOMING DATE IS: "+yourDate);
	/*
	GregorianCalendar c = new GregorianCalendar();
	c.setTime(yourDate);
	XMLGregorianCalendar date2 = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	//	log.info("created date: "+date2);
	return date2;
	*/
	XMLGregorianCalendar xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar();

	Calendar cal = Calendar.getInstance();
	cal.setTime(yourDate);
	//log.debug(" ==[after set]==> "+cal.getTime()+", month is: "+cal.get(Calendar.MONTH));
	xmlGregorianCalendar.setDay(cal.get(Calendar.DAY_OF_MONTH));
	// calendar is zero based month, but xmlgregoriancalendar is 1 based
	xmlGregorianCalendar.setMonth(cal.get(Calendar.MONTH)+1);
	xmlGregorianCalendar.setYear(cal.get(Calendar.YEAR));


	//log.debug(" ==[xml gregorian cal date]==> "+xmlGregorianCalendar);
	return xmlGregorianCalendar;
    }

    static
	public ByteArrayOutputStream insert(String insertText, String mainText, String markerText)
	throws  Exception{

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(baos);

        insert(new StringReader(mainText),
               writer,
               insertText,
               markerText);

        writer.flush();

        return baos;
    }

    static
        public boolean insert( Reader in, Writer out, String text, String markerText)
        throws Exception
    {
        return insert(in,out,text,markerText,false,false);
    }

    static
        public boolean insert( Reader in, Writer out, String text, String markertext, boolean doAppend, boolean before)
        throws Exception
    {
        //declared here only to make visible to finally clause                  
        Writer output = null;
        BufferedReader input = null;
        boolean found = false;
        try {
            // use buffering                                                    
            output = new BufferedWriter( out );
            input = new BufferedReader( in );

            String line = null;
            // this implementation reads one line at a time                     
            while (( line = input.readLine()) != null){
                if((markertext != null) && (line.indexOf(markertext) > -1)){

                    int bi = line.indexOf(markertext);
                    // default is before                                        
                    String beforeMarker = line.substring(0, bi);
                    String afterMarker = line.substring(bi);
                    if(!before){
                        int inclMarkerText = bi+markertext.length();
                        beforeMarker = line.substring(0, inclMarkerText);
                        afterMarker = line.substring(inclMarkerText);
                    }


                    output.write(   beforeMarker +
                                    text +
                                    afterMarker +
				    System.getProperty("line.separator"));
                    found = true;
                }else{
                    output.write( line + System.getProperty("line.separator"));
                }
            }

            if(!found && doAppend){
                output.write( text + System.getProperty("line.separator"));
            }

        }catch(Exception e){
            e.getCause().printStackTrace();
            throw new Exception("Append failed: "+e);
        }finally {
            try {
                //flush and close both "input" and its underlying FileReader    
                if (input!= null) input.close();

                //flush and close both "output" and its underlying FileWriter  
		if (output != null) output.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

	return found;
    }

    private static class SavingTrustManager implements X509TrustManager {

        private final X509TrustManager tm;
        private X509Certificate[] chain;

        SavingTrustManager(X509TrustManager tm) {
            this.tm = tm;
        }

        public X509Certificate[] getAcceptedIssuers() {
            throw new UnsupportedOperationException();
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType)
	    throws CertificateException {
            throw new UnsupportedOperationException();
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
	    throws CertificateException {
            this.chain = chain;
            tm.checkServerTrusted(chain, authType);
        }
    }

    private X509TrustManager tm = 
	new X509TrustManager() {

	    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
		log.info("OUR TRUSTMANAGER: checkClientTrusted =============");
	    }

	    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
		log.info("OUR TRUSTMANAGER: checkServerTrusted =============");
		log.info(" ==["+string+"]==> "+xcs);
		if(xcs != null){
		    for(X509Certificate xc : xcs){
			log.info(" ==["+string+":"+xc.getSubjectDN()+"]==> valid ["+xc.getNotBefore()+" thru "+xc.getNotAfter()+"]");
		    }
		}
	    }

	    public X509Certificate[] getAcceptedIssuers() {
		log.info("OUR TRUSTMANAGER: getAcceptedIssuers =============");
		return null;
	    }
	};

}