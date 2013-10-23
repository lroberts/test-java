package edu.ucsf.sis.feed;

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


import au.com.bytecode.opencsv.*;
import au.com.bytecode.opencsv.bean.*;

abstract
public class StudentFeed
{
    /**
     *  This must be configured in a subclass #configureFeed()
     */
    private Logger STUDENT = Logger.getLogger("STUDENT");

    static
    protected Logger log = Logger.getLogger(StudentFeed.class);

    static protected Map<String, Row> TERMS = new HashMap();
    
    static protected Map<String, Row> CURRICS = new HashMap();
    
    /**
     *  Map of UCSFREG.TLEVEL keyed by the 'level' field which corresponds
     *  to Sunapsis 'codeAcademicLevel' or 'Academic Level'
     */
    static protected Map<String, Row> LEVELS = new HashMap(); 
    
    
    /**
     *  Map of distinct CATNAMEL from REGINFO.TCURRIC keyed by the 'catnamel' field which corresponds
     *  to Sunapsis 'codeAcademicCareer' or 'Academic Career'
     */
    static protected Map<String, Row> CAREERS = new HashMap(); 
    
    
    /**
     *  Map of REGSTAT from REGINFO.TREGSTAT keyed by the 'regstat' 
     *  field which corresponds to Sunapsis 'mapAcademicProgramStatus',
     *  'codeAcademicProgramStatus' or 'Program Status'
     *
     *  tsttercur records have a regstat
     */
    static protected Map<String, Row> PROGRAM_STATUS = new HashMap(); 
    
    static protected Map<String, Row> CIPMAPPING_BY_MAJOR_CODE = new HashMap();
    
    static protected Map<String, String> CIP_CODE_BY_CURRIC = new HashMap();
    
    static protected Map<String, Row> CITIZEN_COUNTRIES = new HashMap();
    
    static protected Map<String, String> DEPARTMENTS_BY_DEPT_CODE = new HashMap();
    
    static protected Map<String, String> RFID_BY_EMPNO = new HashMap();

    static protected Map<Integer, String> SHCS_CURRIC_SCHOOL_MAPPING;

    protected String currTerm = "FA10";

    protected Connect conn;

    private boolean isDelimited = true;

    static
    {
	Connect c = null;
	try{
	    c = defaultConnection();
	    
	    List<Row> rows = c.fetch("select * from UCSFREG.ID_BADGE_MAPPING");
	    for(Row row : rows){
		RFID_BY_EMPNO.put((String)row.get("ucsf_id"), (String)row.get("card_id"));
	    }
	    
	    rows = c.fetch("select * from REGINFO.TCALENDAR");
	    for(Row row : rows){
		TERMS.put((String)row.get("termname4"), row);
	    }
	    /*	    
	    // which table does the studylist.sl_7799'dept' field refer to? CATALOG.SUBJECT
	    rows = c.fetch("select dept,sname from studylst.sl_7799 where term = '"+this.currTerm+"' and  dept <> 'De' and dept in (select code from CATALOG.SUBJECT)");
	    for(Row row : rows){
	    	DEPARTMENTS_BY_DEPT_CODE.put((String)row.get("dept"), (String)row.get("sname"));
	    }
*/	    
	    rows = c.fetch("select * from REGINFO.TCITCNTRY");
	    for(Row row : rows){
		CITIZEN_COUNTRIES.put((String)row.get("citcntry"), row);
	    }
	    
	    rows = c.fetch("select * from REGINFO.TCIPMAPPING");
	    for(Row row : rows){
		CIPMAPPING_BY_MAJOR_CODE.put((String)row.get("major_code"), row);
	    }
	    
	    rows = c.fetch("select * from REGINFO.TCURRIC");
	    for(Row row : rows){
		String cur = (String)row.get("curric");
		CURRICS.put(cur, row);

		String ucopcur = (String)row.get("ucopcurr");
		//log.debug("Have ucop code: "+ucopcur+", for curric: "+cur);
		Row crow = CIPMAPPING_BY_MAJOR_CODE.get(ucopcur);
		//log.debug(" ==> maps to cip row: "+crow);

		if(crow != null){
		    String cip = (String)crow.get("cip_code");
		    CIP_CODE_BY_CURRIC.put(cur, cip);
		}else{
		    log.error("Curric ["+cur+"] with ucop/major code: "+ucopcur+" does not "+
			      "have a CIP mapping");
		}
	    }

	    rows = c.fetch("select * from UCSFREG.TLEVEL");
	    for(Row row : rows){
		LEVELS.put((String)row.get("level"), row);
	    }


	    rows = c.fetch("select distinct CATNAMES from REGINFO.TCURRIC");
	    for(Row row : rows){
		CAREERS.put((String)row.get("catnames"), row);
	    }

	    rows = c.fetch("select * from REGINFO.TREGSTAT");
	    for(Row row : rows){
		PROGRAM_STATUS.put((String)row.get("regstat"), row);
	    }

	    CSVReader reader = new CSVReader(new InputStreamReader(StudentFeed.class.getResourceAsStream("/shcs-curric-school-mapping.csv")));
	    List<String[]> shcs = reader.readAll();
	    SHCS_CURRIC_SCHOOL_MAPPING = new HashMap();
	    for(String[] row : shcs){
		String curr = row[0];
		if(curr.trim().length() <1) continue;

		Integer CURRIC_ID = new Integer(curr);
		SHCS_CURRIC_SCHOOL_MAPPING.put(CURRIC_ID, row[1]);
	    }
	}catch(Throwable t){
	    throw new RuntimeException("Unable to initialize SIS resources: "+t, t);
	}finally{
	    if(c != null) c.close();
	}

    }

    public static Connect defaultConnection()
    //	throws Exception
    {
	try{
	    InputStream is = StudentFeed.class.getResourceAsStream("/config/sis-resource.properties");
	    Properties sisProps = new Properties();
	    sisProps.load(is);
	    String testUrl = sisProps.getProperty("sis.db.ucsfsis.prod.url");
	    String testUser = sisProps.getProperty("sis.db.ucsfsis.prod.user");
	    String testPass = sisProps.getProperty("sis.db.ucsfsis.prod.pass");
	    return new Connect(testUrl, testUser, testPass);
	}catch(Exception ex){
	    log.error("Unable to create default connection: "+ex, ex);
	    throw new RuntimeException("Unable to create default connection: "+ex, ex);
	}
    }

    public StudentFeed(){
	this(defaultConnection());
    }

    public StudentFeed(Connect c){
	this.conn = c;
	this.configureFeed();
    }

    abstract
    public void configureFeed();


    public String lookupRegstat(Object idnum, Object curric, Object term)
	throws Exception
    {
	return lookupRegstat(""+idnum, (String) curric, (String) term);
    }
    
    public String lookupRegstat(String idnum, String curric, String term)
	throws Exception
    {

	String q2 = "select idnum,term,curric,degreestat,level,regstat,progrm,primecurr,school from reginfo.tsttercur where idnum="+idnum+" and curric='"+curric+"' and term='"+term+"'";
	
	List<Row> regstat = this.conn.fetch(q2);
	if(regstat.size() != 1){
	    
	    throw new RuntimeException("Unable to lookup regstat from tsttercur "+
				       "from application:  idnum="+idnum+" and curric='"+curric+"' and term='"+term+"'\n\nresults: "+
				       regstat.size());
	}
	
	return (String)regstat.get(0).get("regstat");
    }
    
    
    /**
     *  Extracts the career from this row and checks it's valid.
     */
    public String career(Row row){
	String curricName = (String) row.get("curric");
	Row thisCurric = CURRICS.get(curricName);		
	if(thisCurric == null){
	    throw new IllegalArgumentException("Unknown curric '"+curricName+"'");
	}
	
	// find the 'career' ('Undergraduate Professional','Resident' etc)
	//  - this corresponds to the CAT and CATNAMES field of the CURRIC
	String career = (String)thisCurric.get("catnames");
	return career;
    }
    
    /**
     *  Extracts the curric from this row and checks it's valid.
     */
    public String curric(Row row){
	String curricName = (String) row.get("curric");
	Row thisCurric = CURRICS.get(curricName);		
	if(thisCurric == null){
	    throw new IllegalArgumentException("Unknown curric '"+curricName+"'");
	}
	return curricName;
    }
    
    /**
     *  Extracts the term from this row and checks it's valid.
     */
    public String term(Row row){
	String termName =  (String) row.get("term");
	Row thisTerm = TERMS.get(termName);		
	if(thisTerm == null){
	    throw new IllegalArgumentException("Unknown term '"+termName+"'");
	}
	
	return termName;
    }
    
    
    /**
     *  Extracts the level from this row and checks it's valid.
     */
    public String level(Row row){
	String levelName = (String) row.get("level");
	Row thisLevel = LEVELS.get(levelName);		
	if(thisLevel == null){
	    throw new IllegalArgumentException("Unknown level '"+levelName+"'");
	}
	return levelName;
    }
    
    public String getStudentQuery(){
	return "select * from UCSFSIS.VOAAISSTUDENTFEED";
	//+" fetch first 100 rows only"
    }

    public void setIsDelimited(Boolean b){
	this.isDelimited = b;
    }
     
    public void createFeed()
	throws Exception
    {
	List<Row> students = this.conn.fetch(this.getStudentQuery());
	int counter = 0;
	List<Row> errors = new ArrayList();
	boolean printedHeader = false;
	for(Row student : students){

	    if((student.get("idnum") == null) || (student.get("idnum").toString().trim().length()<1)){
		log.error("INVALID IDNUM: "+student.get("idnum")+"");
		errors.add(student);
		continue;
	    }

	    List<Row> studentRec = this.conn.fetch("select * from REGINFO.tstudent where idnum="+
						   student.get("idnum"));

	    
	    if(studentRec.size() > 1)
		throw new RuntimeException("More than one student in TSTUDENT with idnum="+
					   student.get("idnum"));
				
	    try{	    
		if(this.isDelimited ){
		    
		    Map data = this.createRecord(student, studentRec.get(0));
		    
		    if(!printedHeader){
			
			List keys = new ArrayList();
			keys.addAll(data.keySet());
			
			Object[] headers = keys.toArray();
			//		    String header = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s", headers );
			String header = String.format(fmt(headers), headers );
			STUDENT.info(header);
			
			printedHeader = true;
		    }
		    
		    List justValues = new ArrayList();
		    justValues.addAll(data.values());
		    //	String formatted = String.format("\"%s\",  \"%s\",  \"%s\",  \"%s\",  \"%s\",  \"%s\",  \"%s\",  \"%s\",  \"%s\",  \"%s\",  \"%s\",  \"%s\",  \"%s\",  \"%s\",  \"%s\",  \"%s\",  \"%s\",  \"%s\",  \"%s\"", justValues.toArray() );
		    Object[] vals = justValues.toArray();
		    //		String formatted = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s",  );
		    String formatted = String.format(fmt(vals), vals );

		    STUDENT.info(formatted);
		}else{
		    process(student, studentRec.get(0));
		}
		 
		counter++;
	    }catch(Throwable t){
		log.error("Unable to process student: "+student+": "+t, t);
		errors.add(student);

		throw new RuntimeException("Unable to process student: "+student+": "+t, t);
	    }

	    //   if(counter > 0) break;
	}

	if(!this.isDelimited){
	    postProcess();
	}


	//log.info(dump(factory.createData(data)));
	//	saveFile(factory.createData(data), "test-feed");
	//return data;
    }

    public String delimeter = ",";
    public boolean isQuoted = false;

    public String fmt(Object... objects){
	//"%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s"
	String val = "";
	for(Object o : objects){
	    if(val.length() > 0) val += delimeter;

	    if(o == null){
		val += "%s";
		continue;
	    }

	    if(String.class.isInstance(o))
		val += "%s";
	    else{
		log.warn("Creating format for unknown class ["+o.getClass()+"] for object ["+o+"]");
		val += "%s";
	    }
	    
	}

	return val;
    }

    abstract
    public Map createRecord(Row stu, Row tstu)
	throws Exception;


    public void process(Row stu, Row tstu)
	throws Exception
    {

    }

    public void postProcess()
	throws Exception
    {

    }
   
}

	/*
	public TermListType createTerms(Row stu)
	    throws Exception
	{
	    TermListType terms = factory.createTermListType();

	    String q = "select idnum,term,curric,degreestat,level,regstat,progrm,primecurr,school,reducedfee from reginfo.tsttercur where idnum="+stu.get("idnum")+" and primecurr='P'";

	    List<Row> rows = this.conn.fetch(q);
	    for(Row row : rows){
		TermType term = factory.createTermType();

		String termName = (String) row.get("term");
		Row thisTerm = TERMS.get(termName);		
		if(thisTerm == null){
		    throw new IllegalArgumentException("Unknown term '"+termName+"'");
		}

		String levelName = level(row);
		String curricName = curric(row);
		String career = career(row);

		term.setInstCd((String) row.get("school") );  // 
		term.setAcadTermCd(termName);  // 
		term.setAcadTermBegDt(gDate(thisTerm.get("termstrt"))); 
		term.setAcadTermEndDt(gDate(thisTerm.get("termend"))); 
		// see email from angela 9/16/2010
		term.setStuDrvEnrlStatInd((String) row.get("regstat"));  // PROGRAM_STATUS

		// ISSUE: we have selected for primecurr only and we are assuming
		// that primecurr == 'Major'
		term.setCipCode((String)CIP_CODE_BY_CURRIC.get(curricName) ); // (Nillable) 
		term.setAcadPrmPlan1Cd((String)curricName );  //    (Nillable) 

		String qTotalCredits = "select idnum, SUM(minunit) as total from studylst.sl_7799 where term='"+termName+"' and booked='A' AND idnum="+stu.get("idnum")+" group by idnum";
		List<Row> credits = this.conn.fetch(qTotalCredits);
		if(credits.size() == 1)
		    term.setStuDrvdTotTermUntNbr((BigDecimal) credits.get(0).get("total"));

		if(credits.size() > 1)
		    throw new RuntimeException("More than one total credits for idnum: "+stu.get("idnum"));

		if(credits.size() == 0){
		    log.warn("No total credits for idnum: "+stu.get("idnum"));
		    term.setStuDrvdTotTermUntNbr(new BigDecimal("0"));
		}


		// find out full time
		// from doug, nov2:
		//   All are full time unless they have a reduced fee code of 'P'.
		String feecode = (String) row.get("reducedfee");
		Boolean fullTime = ((feecode != null) && (feecode.equals("P")))
		    ? new Boolean(true) : new Boolean(false);
		term.setFtFlag( fullTime );  //  (Nillable) 

		term.setAcadCareerCd((String) career);  // 
		term.setAcadlvlBegTermCd((String) levelName);  // 
		terms.getTerm().add(term);
	    }
	    
	    return terms;
	}

	public AdmissionListType createAdmissions(Row stu)
	    throws Exception
	{
	    AdmissionListType admissions = factory.createAdmissionListType();

	    // ok, now lookup the start date
	    String q = "SELECT "+
		" a.\"IDNUM\", "+
		"u.\"IDNUM\" as USERIDNUM, "+
		"u.\"USERID\", "+
		"tc.school as school, "+
		"c.\"TERMNAME4\" as term, "+
		"a.LEVEL as level, "+
		"a.curric as curric, "+
		"c.\"TERMSTRT\", "+
		"c.termend, "+
		"a.\"QRTR_APPLIED_FOR\", "+
		"S.\"ADMGROUP\", "+
		"s.\"ADSTATNAMEL\", "+
		"a.\"ADMISSION_STATUS\", "+
		"a.\"LAST_CHANGED\" "+
		" FROM \"ADMIT\".\"ADMIT_APPLICATION\" a, \"ADMIT\".\"T_ADMISSION_STATUS\" s, ucsfreg.user u, reginfo.tstudent t, reginfo.tcalendar c, reginfo.tcurric as tc "+
		"WHERE t.idnum=u.idnum "+
		"AND u.ssn=a.idnum "+
		"AND s.admission_status=a.admission_status "+
		"AND a.qrtr_applied_for=c.termname4 "+
		"AND s.admgroup='Admitted' "+
		"AND a.curric=tc.curric "+
		"AND u.userid='"+stu.get("userid")+"' "; //stu.get("idnum"); ISSUE: userid or idnum?

	    //	    String q = "select idnum,admission,curric,degreestat,level,regstat,progrm,primecurr,school from reginfo.tsttercur where idnum="+stu.get("idnum");
	    List<Row> rows = this.conn.fetch(q);
	    for(Row row : rows){
		AdmissionType admission = factory.createAdmissionType();

		String levelName = level(row);
		String curricName = curric(row);
		String career = career(row);

		admission.setInstCd((String) row.get("school") );  // 
		admission.setStuAdmtTermCd((String) row.get("term"));  // termname4
		admission.setAcadCareerCd((String) career);  // 
		admission.setApplAcadLvlCd((String)levelName );  // 

		// need to look this up from tsttercur, see email from angela 9/16/2010
		String regstat = lookupRegstat(stu.get("idnum"), row.get("curric"), row.get("term"));
		admission.setApplPgmStatCd((String) regstat);  // 

		// ISSUE: we have equated application curric with 'Major' and CIP code
		admission.setAcadPlanCd((String) curricName);  //    (Nillable) 
		admission.setCipCode((String)CIP_CODE_BY_CURRIC.get(curricName)); // (Nillable) 

		//admission.setApplCntrCd((String) );  //    (Nillable) 
		//admission.setAcadPgmCd((String) );  //    (Nillable) 
		//admission.setAcadGrpCd((String) );  //    (Nillable) 
		admission.setStuAdmtTypCd((String)row.get("admission_status")); // (Nillable) 

		admission.setStuAdmtTermBegDt(gDate(row.get("termstrt")));
		admission.setStuAdmtTermEndDt(factory.createAdmissionTypeStuAdmtTermEndDt(gDate(row.get("termend")))); // (Nillable) 

		Object last = row.get("last_changed");
		if(last != null)
		    admission.setApplPgmActnDt(factory.createAdmissionTypeApplPgmActnDt(gDate(last))); // (Nillable) 

		// FIXME: not populated
		//admission.setApplPgmActnCd((String) );  // program coding action (Nillable) 
		//admission.setExtOrgName((String) );  // previous school (Nillable) 
		//admission.setSchoolFunds => Integer  // school funding amount  (Nillable) 
		//admission.setSchoolFundsDesc((String) ); // school funding desc.(Nillable) 

		// sequence number: used to identify different admissions so the system 
		// can handle dual-degrees
		//admission.setApplPlanSeqNbr => Integer    (Nillable) 

		admissions.getAdmission().add(admission);
	    }
	    
	    return admissions;
	}

	public ProgramListType createPrograms(Row stu)
	    throws Exception
	{
	    ProgramListType programs = factory.createProgramListType();

	    String q = "select idnum,term,curric,degreestat,level,regstat,progrm,primecurr,school,last_updt_date from reginfo.tsttercur where idnum="+stu.get("idnum");

	    List<Row> rows = this.conn.fetch(q);
	    for(Row row : rows){
		ProgramType program = factory.createProgramType();

		String termName = term(row);
		String levelName = level(row);
		String curricName = curric(row);
		String career = career(row);

		program.setInstCd((String) row.get("school"));  // 
		program.setStuAdmtTermCd((String) termName);  // 
		program.setAcadCareerCd((String) career);  // 
		program.setAcadLvlCd((String) levelName);  // 
		program.setStuPgmStatCd((String)  row.get("regstat"));  // 

		program.setAcadPgmActnDt(gDate(row.get("last_updt_date")));

		String qGPA = "Select * from REPORTS.VGPARANK where term = '"+termName+"' "+
		    "and curric='"+row.get("curric")+"' and idnum="+stu.get("idnum")+"";

		//log.info(""+qGPA);		
		List<Row> gpa = this.conn.fetch(qGPA);
		if(gpa.size() > 1)
		    throw new RuntimeException("More than one GPA for term = '"+termName+"' "+
		    "and curric='"+row.get("curric")+"' and idnum="+stu.get("idnum")+"");

		if(gpa.size() == 0){
		    log.warn("No GPA for term = '"+termName+"' "+
		    "and curric='"+row.get("curric")+"' and idnum="+stu.get("idnum")+"");
		}else{
		    BigDecimal g = (BigDecimal)gpa.get(0).get("curr_gpa");
		    program.setStuCumGpaNbr(g); //    (Nillable) 
		}

		String stdeg = "SELECT * FROM REGINFO.tstdegree where curric='"+row.get("curric")+"' and idnum="+stu.get("idnum");
		List<Row> degree = this.conn.fetch(stdeg);

		program.setStuExpctGradTermCd((String)degree.get(0).get("degexptm") );  //    (Nillable) 

		programs.getProgram().add(program);
	    }
	    
	    return programs;
	}


	public CourseListType createCourses(Row stu)
	    throws Exception
	{
	    // FIXME: courses should just be for the current term
	    CourseListType courses = factory.createCourseListType();

	    // see kevin's email 9/16/2010
	    String q = "select * from studylst.sl_7799 where term = '"+this.currTerm+"' and booked <> 'I'"+
		" AND idnum="+stu.get("idnum")+" and booked='A'";

	    // booked: A=active, I=dropped course, blank='header row' ignore
	    //
	    // which table does the 'dept' field refer to? CATALOG.SUBJECT
	    //    select dept,sname from studylst.sl_7799 where term = 'FA10' and 
	    //     dept <> 'De' and dept in (select code from CATALOG.SUBJECT)
	    //
	    //    the 'De' is also a 'header' row we ignore
	    List<Row> rows = this.conn.fetch(q);
	    for(Row row : rows){
		CourseType course = factory.createCourseType();
OB
		String termName = term(row);
		String curricName = curric(row);
		String career = career(row);
		String org = ((String)row.get("school") != null)? (String)row.get("school")
		    : "";
		
		if(org == ""){
		    log.warn("No value for PplsftAcadOrgLvl2Cd ");
		}
		course.setPplsftAcadOrgLvl2Cd(org );  // 
		course.setAcadTermCd((String) termName);  // 
		course.setCrsTitle((String)row.get("name"));
		course.setCrsSubjDeptCd((String)DEPARTMENTS_BY_DEPT_CODE.get((String)row.get("dept")) );  // 

		course.setCrsCatlgNbr((String)row.get("ucode") );  // 
		course.setCrnNbr((String)row.get("course") );  // 

		course.setStuEnrlAddDt(factory.createCourseTypeStuEnrlAddDt(gDate(row.get("bookdate")))); //    (Nillable) 

		String qDrop = "select * from studylst.sl_7799 where term = '"+this.currTerm+"' and booked <> 'I'"+
		" AND idnum="+stu.get("idnum")+" and booked='I'";
		List<Row> droprows = this.conn.fetch(qDrop);
		if(droprows.size() >0){
		    course.setStuEnrlDrpDt(factory.createCourseTypeStuEnrlDrpDt(gDate(row.get("commentda")))); //    (Nillable) 
		}

		// course credits
		String units = (String)row.get("minunit");
		log.error("have units: "+units);
		if(units != null) units = units.trim();
		course.setAcadUntTknNbr(new BigDecimal(units));

		// enrollment status is regstat
		String regstat = lookupRegstat(stu.get("idnum"), row.get("curric"), row.get("term"));
		course.setStuEnrlStatCd(regstat );  // 

		courses.getCourse().add(course);
	    }
	    
	    return courses;
	}
	*/
