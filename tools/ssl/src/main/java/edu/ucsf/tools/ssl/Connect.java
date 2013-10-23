package edu.ucsf.sis.chartfield;

//import COM.ibm.db2.jdbc.DB2DataSource;

import java.util.*;
import java.sql.*;
import javax.sql.DataSource;
import javax.naming.NamingException;
import javax.naming.InitialContext;
import java.io.*;
import javax.naming.Context;

import org.apache.log4j.Logger;

/**
 */
public class Connect {

    private Logger log = Logger.getLogger(this.getClass());
 
    private Connection connection;

    public Connect(String url, String user, String pass)
	throws Exception
    {
	Driver db2 = (Driver)Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
	log.info("Have Driver: "+db2);
	DriverManager.registerDriver(db2);

	drivers();

	this.connection = DriverManager.getConnection(url, user, pass);

	log.info("have connection! "+this.connection);
	/*
        List<Row> users = null;

        String baseQuery = "SELECT * from ucsfreg.user u, reginfo.tstudent t WHERE u.USERID='"+username
	    +"' AND t.idnum=u.idnum ";
        if(password == null)
            users = fetch(baseQuery, info, form);

	log.info("have connection:"+this.connection);
	this.connection.setAutoCommit(false);
	*/
    }

    public List<String> getAllTables(String schemaName)
	throws Exception
    {

	List<String> tables = new ArrayList();
	// Gets the database metadata
	DatabaseMetaData dbmd = this.connection.getMetaData();
    
	// Specify the type of object; in this case we want tables
	String[] types = {"TABLE"};
	ResultSet resultSet = dbmd.getTables(null, null, "%", types);

	String output = "";
	// Get the table names
	while (resultSet.next()) {
	    // Get the table name
	    String tableName = resultSet.getString(3);
	    
	    // Get the table's catalog and schema names (if any)
	    String tableCatalog = resultSet.getString(1);
	    String tableSchema = resultSet.getString(2);

	    //	    if(schemaName.equalsIgnoreCase(tableSchema)){
	    System.setOut(new PrintStream(new FileOutputStream(tableSchema+".model.properties", 
							       true)));

		tables.add(tableName);

		//System.out.printf("%30s | %30s | %30s \n", tableName, tableCatalog, tableSchema);
		log.debug("# "+tableName+" fields");
		try{
		    List<Row> cols = this.fetch("select * from "+tableSchema+"."+tableName+" "+
						    "FETCH FIRST 2 ROWS ONLY", null, 
new String[]{tableName});

		}catch(Exception ex){ log.debug("ERROR: schema "+tableSchema+"."+tableName+": "+ex);}
		
		//}
	}
	log.debug("models.list="+tables);
	return  tables;
    }


	/*
	List<Row> users = c.fetch("SELECT * from ucsfreg.user", info);
	for(Row user : users){
	    if(curr>limit) break;

	   Integer id = (Integer)user.get("IDNUM");

	   if(id.intValue() > 0){
	       c.dump(user);
	       curr++;
	    }
	}
	*/
    public String random(int size){
	//note a single Random object is reused here
	Random randomGenerator = new Random();
	int randomInt = randomGenerator.nextInt(size);
	return ""+randomInt;
    }

    public void testSchema(String schema)
	throws SQLException
    {

	createSchema(schema, "lroberts");
	createTable(schema, "testtable", "FullName varchar(45)","Curric varchar(45)","SSN varchar(11) NOT NULL","HoldStat char(11)","primary key (ssn)");
	/*
	List<String> tables = getAllTables(schema);
	if(tables.size() != 1){
	    throw new RuntimeException("Schema ["+schema+"] has incorrect amount of tables: "+tables);
	}
	*/
	execute("DROP TABLE "+schema+".testtable");
	
	//	c.execute("Select * from ucsfreg.tsfshold");
	//	c.getHolds();
    }

    public void dump(Row user){
	Integer id = (Integer)user.get("IDNUM");

	Object pwd = user.get("PWD");
	Object empno = user.get("EMPNO");
	Object ssn = user.get("SSN");
	Object uid = user.get("USERID");
	
	//log.debug("ssn is class: "+ssn.getClass());
	log.debug("IDNUM ["+id+"] PWD ["+pwd+"], empno:"+empno+", ssn: "+ssn+", uid:"+uid);

    }
    
    public void addUser(String userName, String ssn, List info)
	throws SQLException
    {
	Row user = getUser(userName, info);
	if(user == null){
	    execute("INSERT INTO ucsfreg.user (USERID, SSN) VALUES ('"+userName+"', '"+ssn+"')");
	}else{
	    throw new IllegalArgumentException("User '"+userName+"' already exists.");
	}

    }


    public void addUser2(String userName, String pwd, List info)
	throws SQLException
    {
	Row user = getUser(userName, info);
	if(user == null){
	    execute("INSERT INTO ucsfreg.user (USERID, PWD) VALUES ('"+userName+"', '"+pwd+"')");
	}else{
	    throw new IllegalArgumentException("User '"+userName+"' already exists.");
	}

    }


    public void deleteUser(String userName)
	throws SQLException
    {
	execute("DELETE FROM ucsfreg.user WHERE USERID='"+userName+"'");
    }

    public Row getUser(String username, List info)
	throws SQLException
    {
	String baseQuery = "SELECT * from ucsfreg.user u WHERE u.USERID='"+username+"' ";
	//String baseQuery = "SELECT * from ucsfreg.user u, reginfo.tstudent t WHERE u.USERID='"+username+"' AND t.idnum=u.idnum ";
	List<Row> users = fetch(baseQuery, info);

				    //"SELECT * from ucsfreg.user WHERE USERID='"+username+"'", info);
	if(users.size() >1)
	    throw new RuntimeException("Found more than one record with USERID='"+username+"'");

	//if(users.size() <1)
	//   throw new IllegalArgumentException("Found no records with USERID='"+username+"'");

	for(Row user : users){
	    dump(user);
	    return user;
	}
	
	return null;
    }

    public void getHolds()
	throws SQLException
    {
	execute("select a.ssn,a.fullname from ucsfreg.tshshold a where not exists (select ssn from reginfo.tstudent b where a.ssn=b.ssn)");
    }

    public static String getWebappType(int type, ResultSet rs, int colNumber)
    {
	String s = "string";

	    switch (type) {
	    case java.sql.Types.VARBINARY:
	    case java.sql.Types.STRUCT:
	    case java.sql.Types.OTHER:
	    case java.sql.Types.REF:
	    case java.sql.Types.ARRAY:
	    case java.sql.Types.BINARY:
	    case java.sql.Types.BLOB:
	    case java.sql.Types.BIGINT:
	    case java.sql.Types.CLOB:
	    case java.sql.Types.DISTINCT:
	    case java.sql.Types.LONGVARBINARY:
		throw new RuntimeException("Type ["+type+"] is not supported");

	    case java.sql.Types.BIT:
		return "boolean";
		
	    case java.sql.Types.DATE:
		return "date";
		
	    case java.sql.Types.DECIMAL:
	    case java.sql.Types.DOUBLE:
	    case java.sql.Types.FLOAT:
	    case java.sql.Types.REAL:
	    case java.sql.Types.NUMERIC:
		return "double";
		
	    case java.sql.Types.INTEGER:
	    case java.sql.Types.SMALLINT:
	    case java.sql.Types.TINYINT:
		return "int";
		
	    case java.sql.Types.VARCHAR:
	    case java.sql.Types.CHAR:
	    case java.sql.Types.JAVA_OBJECT:
	    case java.sql.Types.LONGVARCHAR:
		return s;
		
	    case java.sql.Types.NULL:
		return null;
		
	    case java.sql.Types.TIMESTAMP:
	    case java.sql.Types.TIME:
		return "timestamp";
		

	}
	return null;
    }


    public List<Row> fetch(String query)
	throws SQLException
    {
	return fetch(query, null);
    }

    public List<Row> fetch(String query, List wantedCols, String... moredata)
	throws SQLException
    {
	log.debug("Exec: "+query);
	ResultSet rs = execute(query);
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

	List<Row> items = new ArrayList();	
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

	return items;

    }

    /**
     *  @see http://publib.boulder.ibm.com/infocenter/wasinfo/v4r0/index.jsp?topic=/com.ibm.support.was40.doc/html/Java_2_Connectivity__J2C_/swg21066402.html
     */
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

    public void createHoldTable()
	throws SQLException
    {
	execute("create table ucsfreg.tshshold ( FullName varchar(45), Curric varchar(45), SSN varchar(11) NOT NULL, HoldStat char(11), primary key (ssn))");
    }


    /**
        Use this method to return the Object from a specified column.
	 This returns all the possible java.sql.Types except
	  Distinct, Struts, and Java Object.
	   
	   If your query is returning a datatype of Distinct, Strut, or JavaObject
	    you should already know your dataType.
	     
	     If any of these are passed into this method it will return an
	      instance of java.lang.object.
	       
	       Returns a null object if nothing matched.

	       @param type, data, rs
	       @return Object
    */

    public static Object getObject(int type, ResultSet rs, int colNumber)
    {
	
	try {
	    switch (type) {
	    case java.sql.Types.ARRAY:
		return rs.getArray(colNumber);
		
	    case java.sql.Types.BIGINT:
		long bigint = rs.getLong(colNumber);
		return new Long(bigint);
		
	    case java.sql.Types.BINARY:
		return rs.getBytes(colNumber);
		
	    case java.sql.Types.BIT:
		boolean bit = rs.getBoolean(colNumber);
		return new Boolean(bit);
		
	    case java.sql.Types.BLOB:
		return rs.getBlob(colNumber);

	    case java.sql.Types.CHAR:
		return rs.getString(colNumber);
		
	    case java.sql.Types.CLOB:
		return rs.getClob(colNumber);
		
	    case java.sql.Types.DATE:
		return rs.getDate(colNumber);
		
	    case java.sql.Types.DECIMAL:
		return rs.getBigDecimal(colNumber);
		
	    case java.sql.Types.DISTINCT:
		return rs.getObject(colNumber);
		
	    case java.sql.Types.DOUBLE:
		double d = rs.getDouble(colNumber);
		return new Double(d);
		
	    case java.sql.Types.FLOAT:
		float f =  rs.getFloat(colNumber);
		return new Float(f);
		
	    case java.sql.Types.INTEGER:
		int inte = rs.getInt(colNumber);
		return new Integer(inte);
		
	    case java.sql.Types.JAVA_OBJECT:
		return rs.getObject(colNumber);
		
	    case java.sql.Types.LONGVARBINARY:
		return rs.getBytes(colNumber);
		
	    case java.sql.Types.LONGVARCHAR:
		return rs.getString(colNumber);
		
	    case java.sql.Types.NULL:
		return null;
		
	    case java.sql.Types.NUMERIC:
		return rs.getBigDecimal(colNumber);
		
	    case java.sql.Types.OTHER:
		return rs.getObject(colNumber);
		
	    case java.sql.Types.REAL:
		float real = rs.getFloat(colNumber);
		return new Float(real);
		
	    case java.sql.Types.REF:
		return rs.getRef(colNumber);
		
	    case java.sql.Types.SMALLINT:
		short sh = rs.getShort(colNumber);
		return new Short(sh);
		
	    case java.sql.Types.STRUCT:
		return rs.getObject(colNumber);
		
	    case java.sql.Types.TIME:
		return rs.getTime(colNumber);
		
	    case java.sql.Types.TIMESTAMP:
		return rs.getTimestamp(colNumber);

	    case java.sql.Types.TINYINT:
		byte b = rs.getByte(colNumber);
		return new Byte(b);
		
	    case java.sql.Types.VARBINARY:
		return rs.getBytes(colNumber);
		
	    case java.sql.Types.VARCHAR:
		return rs.getString(colNumber);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
delete from ucsfreg.tshshold
GO

DB2IMPORT INTO ucsfreg.tshshold ImportFile 
GO

insert into reginfo.ttranhold
    select b.idnum,'SH',current date,'SHIRLOAD','',
    cast(null as date),cast(null as date),cast(null as
					       time),'SHIRLOAD','',''
    from reginfo.tstudent b, ucsfreg.tshshold a
 where a.ssn=b.ssn
    and not exists (
         select c.idnum
         from reginfo.ttranhold c
         where b.idnum=c.idnum
             and c.holdcode='SH'
	 and c.released_by='')
GO

update reginfo.ttranhold c
    set (released_by,released_date)=('SHIRLOAD',current date) 
where c.holdcode='SH'
   and c.released_by='' 
    and not exists (
   select b.idnum
   from reginfo.tstudent b, ucsfreg.tshshold a
   where a.ssn=b.ssn
   and b.idnum=c.idnum)

    */

    public void drivers()
	throws Exception
    {
	log.debug("Checking drivers...");

	for (	Enumeration e = DriverManager.getDrivers() ; e.hasMoreElements() ;) {
	    log.debug(e.nextElement());
	}

    }

}
/*

    public static Connection retrieveConnection(String jndiName) throws NamingException, SQLException
    {
        InitialContext ctx = new InitialContext();
        Context envCtx = (Context) ctx.lookup("java:comp/env");
        DataSource ds = (DataSource)envCtx.lookup(jndiName);
        Connection con = ds.getConnection();
        con.setAutoCommit(false);
        return con;
    }
    
    public static IDSystemConnector getRemedyConnector(ArrayList messages)
    {
        try {
            try {
		//                Connection con = retrieveConnection("jdbc/oracle");
		Connection con = ConnectionFactory.getRemedyConnection();
                con.setAutoCommit(false);
                IDSystemConnector idsc = new IDSystemConnector(con);
                return idsc;
            }
            catch(Exception e)
		{
		    messages.add("There was an error creating a connection to the Oracle system.  The error was: " + e.getMessage());
		}
        }
        catch(Exception e) {
            e.printStackTrace();
            messages.add("The Oracle connection could not be created.  The error was: " + e.getMessage());
        }
        return null;
    }

*/
