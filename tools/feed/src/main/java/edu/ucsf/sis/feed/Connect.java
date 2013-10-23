package edu.ucsf.sis.feed;
//import COM.ibm.db2.jdbc.DB2DataSource;

import java.util.*;
import java.sql.*;
import static java.sql.Types.*;
import javax.sql.DataSource;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import java.io.*;
import javax.naming.Context;

import edu.ucsf.sis.admit.IDSystemConnector;
import edu.ucsf.saa.ConnectionFactory;

import org.apache.log4j.Logger;

/**
 */
public class Connect {

    private Logger log = Logger.getLogger(this.getClass());
 
    private Connection connection;

    private Map<String, PreparedStatement> prepared = new HashMap();

    public Connect(String url, String user, String pass)
	throws Exception
    {
	this(url,user,pass,"com.ibm.db2.jcc.DB2Driver");
    }

    public Connect(String url, String user, String pass, String driver)
	throws Exception
    {
	Driver db2 = (Driver)Class.forName(driver).newInstance();
	log.info("Have Driver: "+db2);
	DriverManager.registerDriver(db2);

	drivers();

	this.connection = DriverManager.getConnection(url, user, pass);

	log.info("have connection! "+this.connection);
    }

    public void close(){
	try{
	    if(this.connection != null)
		this.connection.close();
	}catch(Exception ex){
	    log.error("Unable to close connection: "+ex);
	}
    }

    public Row getUser(String username, List info)
	throws SQLException
    {
	String baseQuery = "SELECT * from ucsfreg.user u WHERE u.USERID='"+username+"' ";
	List<Row> users = fetch(baseQuery, info);

	if(users.size() >1)
	    throw new RuntimeException("Found more than one record with USERID='"+username+"'");

	for(Row user : users){
	    return user;
	}
	
	return null;
    }

    public static String getWebappType(int type, ResultSet rs, int colNumber)
    {
	String s = "string";

	    switch (type) {
	    case VARBINARY:
	    case STRUCT:
	    case OTHER:
	    case REF:
	    case ARRAY:
	    case BINARY:
	    case BLOB:
	    case BIGINT:
	    case CLOB:
	    case DISTINCT:
	    case LONGVARBINARY:
		throw new RuntimeException("Type ["+type+"] is not supported");

	    case BIT:
		return "boolean";
		
	    case DATE:
		return "date";
		
	    case DECIMAL:
	    case DOUBLE:
	    case FLOAT:
	    case REAL:
	    case NUMERIC:
		return "double";
		
	    case INTEGER:
	    case SMALLINT:
	    case TINYINT:
		return "int";
		
	    case VARCHAR:
	    case CHAR:
	    case JAVA_OBJECT:
	    case LONGVARCHAR:
		return s;
		
	    case NULL:
		return null;
		
	    case TIMESTAMP:
	    case TIME:
		return "timestamp";
		

	}
	return null;
    }


    public List<Row> pfetch(String query, Object... params)
	throws SQLException
    {
	List<Row> items = new ArrayList();	
	log.debug("Exec: "+query);
	ResultSet rs = null;
	PreparedStatement stmt = this.prepared.get(query);
	try{
	    if(stmt == null){
		stmt = connection.prepareStatement(query);
	    }

	    ParameterMetaData pMeta = stmt.getParameterMetaData();
	    int pCount = 0;
	    for(Object param : params){
		pCount++;

		int pType = pMeta.getParameterType(pCount);
		int pScale = pMeta.getScale(pCount);

		stmt.setObject(pCount, param, pType, pScale);
	    }

	    if(query.trim().toLowerCase().startsWith("select")){
		log.debug("Executing: "+query);
		rs = stmt.executeQuery(query);
		log.debug("have results: "+rs);
		
	    }else{
		//log.debug("Executing 'update': "+query);
		int rowCount = stmt.executeUpdate(query);
		log.debug("=> affected rows: "+rowCount);
	    }

	    // Get result set meta data
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int numColumns = rsmd.getColumnCount();
	    
	    Map<String,Integer> cols = new HashMap();
	    
	    String fields = "";
	    String fieldTypes = "";
	    // Get the column names; column indices start from 1
	    for (int i=1; i<numColumns+1; i++) {
		String columnName = rsmd.getColumnName(i);
		int colType = rsmd.getColumnType(i);
		cols.put(columnName,new Integer(colType));
		// Get the name of the column's table name
		String tableName = rsmd.getTableName(i);
	    }
	    
	    //log.debug("model."+moredata[0]+".fields="+fields);
	    //log.debug(fieldTypes);

	    // Fetch each row from the result set
	    while (rs.next()) {
		// Get the data from the row using the column index
		Row u = new Row();
		
		for (int colNumber=1; colNumber <= numColumns; colNumber++)
		    {
			String colName = rsmd.getColumnName(colNumber);
			Object o = getObject(rsmd.getColumnType(colNumber),
					     rs, colNumber);
			u.put(colName.toLowerCase(),o);
		    }
		
		items.add(u);
	    }
	}finally{
	    // this also closes the resultset
	    if(stmt != null) stmt.close();
	}

	return items;
    }

    public List<Row> fetch(String query)
	throws SQLException
    {
	return fetch(query, null);
    }

    public List<Row> fetch(String query, List wantedCols, String... moredata)
	throws SQLException
    {
	List<Row> items = new ArrayList();	
	log.debug("Exec: "+query);
	ResultSet rs = null;
	Statement stmt = null;
	try{
	    stmt = connection.createStatement();

	    if(query.trim().toLowerCase().startsWith("select")){
		log.debug("Executing: "+query);
		rs = stmt.executeQuery(query);
		log.debug("have results: "+rs);
		
	    }else{
		//log.debug("Executing 'update': "+query);
		int rowCount = stmt.executeUpdate(query);
		log.debug("=> affected rows: "+rowCount);
	    }

	    // Get result set meta data
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int numColumns = rsmd.getColumnCount();
	    
	    Map<String,Integer> cols = new HashMap();
	    
	    String fields = "";
	    String fieldTypes = "";
	    // Get the column names; column indices start from 1
	    for (int i=1; i<numColumns+1; i++) {
		String columnName = rsmd.getColumnName(i);
		int colType = rsmd.getColumnType(i);
		cols.put(columnName,new Integer(colType));
		// Get the name of the column's table name
		String tableName = rsmd.getTableName(i);
		//	    log.debug("have column: "+columnName+" of type: "+colType);
		
		//if(i>1) fields += ",";
		//fields += columnName.toLowerCase();    
		//String ft = getWebappType(colType, rs, i );
		//if(!ft.equalsIgnoreCase("string")){
		//fieldTypes += "model."+moredata[0]+".fields."+columnName.toLowerCase()+".type="+ft+"\n";
		//}
	    }
	    
	    //log.debug("model."+moredata[0]+".fields="+fields);
	    //log.debug(fieldTypes);

	    // Fetch each row from the result set
	    while (rs.next()) {
		// Get the data from the row using the column index
		Row u = new Row();
		
		for (int colNumber=1; colNumber <= numColumns; colNumber++)
		    {
			String colName = rsmd.getColumnName(colNumber);
			//  log.debug("have col: "+colName);
			if((wantedCols == null) || wantedCols.contains(colName)){
			    Object o = getObject(rsmd.getColumnType(colNumber),	 rs, colNumber);
			    //u.put(colName,o);
			    u.put(colName.toLowerCase(),o);
			}
		    }
		
		items.add(u);
	    }
	}finally{
	    // this also closes the resultset
	    if(stmt != null) stmt.close();
	}

	return items;

    }


    /*
    public void getHolds()
	throws SQLException
    {
	execute("select a.ssn,a.fullname from ucsfreg.tshshold a where not exists (select ssn from reginfo.tstudent b where a.ssn=b.ssn)");
    }
    */
    /**
     *  @see http://publib.boulder.ibm.com/infocenter/wasinfo/v4r0/index.jsp?topic=/com.ibm.support.was40.doc/html/Java_2_Connectivity__J2C_/swg21066402.html
     *
    public ResultSet execute(String query)
	throws SQLException
    {
	// Create a result set containing all data from my_table
	Statement stmt = connection.createStatement();
	if(query.trim().toLowerCase().startsWith("select")){
	    	    log.debug("Executing: "+query);
	    ResultSet rs = stmt.executeQuery(query);
	    	    log.debug("have results: "+rs);
	    return rs;
	}else{
	    //log.debug("Executing 'update': "+query);
	    int rowCount = stmt.executeUpdate(query);
	    log.debug("=> affected rows: "+rowCount);
	    return null;
	}
    }


    public void createSchema(String name, String auth)
	throws SQLException
    {
	execute("CREATE SCHEMA "+name+" AUTHORIZATION "+auth);
    }

    public void createTable(String schema, String tableName, String... contents )
	throws SQLException
    {
	String sql = "CREATE TABLE "+schema+"."+tableName+" (";
	for(int i=0; i<contents.length; i++){
	    if(i>0) sql += ",";

	    sql += contents[i];
	}
	sql += ")";


	execute(sql);
    }
    */
    /**
     *   Use this method to return the Object from a specified column.
     *	 This returns all the possible java.sql.Types except
     *	  Distinct, Struts, and Java Object.
     *	   
     *	   If your query is returning a datatype of Distinct, Strut, or JavaObject
     *	    you should already know your dataType.
     *	     
     *	     If any of these are passed into this method it will return an
     *	      instance of java.lang.object.
     *	       
     *	       Returns a null object if nothing matched.
     *
     *	       @param type, data, rs
     *	       @return Object
     */
    public static Object getObject(int type, ResultSet rs, int colNumber)
    {
	
	try {
	    switch (type) {
	    case ARRAY:
		return rs.getArray(colNumber);
		
	    case BIGINT:
		long bigint = rs.getLong(colNumber);
		return new Long(bigint);
		
	    case BINARY:
		return rs.getBytes(colNumber);
		
	    case BIT:
		boolean bit = rs.getBoolean(colNumber);
		return new Boolean(bit);
		
	    case BLOB:
		return rs.getBlob(colNumber);

	    case CHAR:
		return rs.getString(colNumber);
		
	    case CLOB:
		return rs.getClob(colNumber);
		
	    case DATE:
		return rs.getDate(colNumber);
		
	    case DECIMAL:
		return rs.getBigDecimal(colNumber);
		
	    case DISTINCT:
		return rs.getObject(colNumber);
		
	    case DOUBLE:
		double d = rs.getDouble(colNumber);
		return new Double(d);
		
	    case FLOAT:
		float f =  rs.getFloat(colNumber);
		return new Float(f);
		
	    case INTEGER:
		int inte = rs.getInt(colNumber);
		return new Integer(inte);
		
	    case JAVA_OBJECT:
		return rs.getObject(colNumber);
		
	    case LONGVARBINARY:
		return rs.getBytes(colNumber);
		
	    case LONGVARCHAR:
		return rs.getString(colNumber);
		
	    case NULL:
		return null;
		
	    case NUMERIC:
		return rs.getBigDecimal(colNumber);
		
	    case OTHER:
		return rs.getObject(colNumber);
		
	    case REAL:
		float real = rs.getFloat(colNumber);
		return new Float(real);
		
	    case REF:
		return rs.getRef(colNumber);
		
	    case SMALLINT:
		short sh = rs.getShort(colNumber);
		return new Short(sh);
		
	    case STRUCT:
		return rs.getObject(colNumber);
		
	    case TIME:
		return rs.getTime(colNumber);
		
	    case TIMESTAMP:
		return rs.getTimestamp(colNumber);

	    case TINYINT:
		byte b = rs.getByte(colNumber);
		return new Byte(b);
		
	    case VARBINARY:
		return rs.getBytes(colNumber);
		
	    case VARCHAR:
		return rs.getString(colNumber);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public void drivers()
	throws Exception
    {
	log.debug("Checking drivers...");
	for(Enumeration e = DriverManager.getDrivers() ; e.hasMoreElements() ;) {
	    log.debug(e.nextElement());
	}
    }
}