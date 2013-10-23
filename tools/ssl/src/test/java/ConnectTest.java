import org.junit.*;
import java.net.*;
import java.io.*;
import java.util.*;

import org.apache.log4j.Logger;
import org.apache.log4j.Level;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.LogManager;

import edu.ucsf.tools.ssl.*;

public class ConnectTest
{
    private Logger log = Logger.getLogger(this.getClass());


    @Test
    public void testBuildKeyStoreAndConnect()
	throws Exception
    {
	// PKCS#8 is required for java keystore pks
	InputStream pk = 
	    ConnectTest.class.getResourceAsStream("testfiles/client-kevin.pkcs8");

	// this cert is PEM encoded X509
	InputStream cert = 
	    ConnectTest.class.getResourceAsStream("testfiles/client-kevin.cer");

	SecureConnection req = new SecureConnection(pk, cert);
	req.trust("saa-09.ucsf.edu", 443);
	req.get("Test message", 
		 "https://saa-09.ucsf.edu/sisdataexchange/rest/vstudent?readonly=false&term=WI12");

    }

    //@Test
    public void testPreBuiltKeyStore()
	throws Exception
    {
	File ks = new File("/home/CAMPUS/lroberts/Development/checkouts/clienttest/src/test/resources/testfiles/specialks");
	SecureConnection req = new SecureConnection(ks, "changeit");
	req.get("Test message", "https://saa-09.ucsf.edu/sisdataexchange/rest/vstudent?readonly=false&term=WI12");
    }

}