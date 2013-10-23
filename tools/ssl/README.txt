
# you need at least JDK 1.6.0_22 as earlier versions had TLS re-negotiation issues: http://www.oracle.com/technetwork/java/javase/documentation/tlsreadme2-176330.html

# the SecureConnection class requires the private key and certificate to be
# in specific formats, see class javadoc for details.

# to convert the private key to pkcs#8 use a command like this:
openssl pkcs8 -topk8 -nocrypt -outform DER < src/test/resources/testfiles/client-kevin.key > client-kevin.pkcs8

# this example converts kevin's PEM (client-kevin.cer) to something a keystore can read openssl x509 -in client-kevin.cer -inform PEM -out client-kevin.der -outform DER
keytool -storepass changeit -keystore src/main/resources/specialks -importcert -alias kevin-client-cert-der -file kevin-certs/client-kevin.der -trustcacerts

# docs/unused contains some stuff we came accross while debugging this issue
# that may be useful in future.
# - the jdk deploy jar is there for the contained MSCryptoProvider class
#   (which don't seem to be shipped any more as some versions of httpd on 
#   windows throw a decryption error.
	/*
	try{
	Class providerClass = Class.forName("com.sun.deploy.security.MSCryptoProvider", true, ClassLoader.getSystemClassLoader());
	Provider provider = (Provider)providerClass.newInstance();
	//Security.insertProviderAt(provider, Security.getProviders().length + 1);
	// method not found
	//com.sun.deploy.config.Config.getInstance().loadDeployNativeLib();
	Security.addProvider(provider);
	}catch(Throwable t){
	    System.err.println("Unable to add MSCryptoProvider:"+t);
	    throw new RuntimeException("Unable to add MSCryptoProvider:"+t);
	}
	*/
