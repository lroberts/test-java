package edu.ucsf.tools.reminder;

import java.net.URL;
import java.io.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.DriverManager;
import java.sql.Driver;
import java.sql.Statement;
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
import net.fortuna.ical4j.util.*;
import static net.fortuna.ical4j.util.CompatibilityHints.*;

/**
 *  load serialized appt map
 *  get SIS connection
 *  query appointment table for appointments in the next3 days
 *  for each appt
 *    check 
 */
abstract
public class AbstractEventCheck implements Runnable
{
    static {
	try{
	    Properties props = new Properties();
	    props.load(AbstractEventCheck.class.getResourceAsStream("/log4j.properties"));
	    PropertyConfigurator.configure(props);
	}catch(Exception ex){
	    System.out.println("Unable to configure logging: "+ex);
	}
    }

    protected Logger log = Logger.getLogger(this.getClass());
    private Logger sent_log = Logger.getLogger("APPT");

    private boolean isDone = false;
    
    DateFormat df = new SimpleDateFormat("EEE, MMM d 'at' h:mm a");

    private Calendar calendar;

    private String smtpHost;
    private InternetAddress adminAddress;
    private Dur defaultLookahead;
    protected int lookahead = 2;

    protected boolean isTest = false;

    private List<String> adminEmails;
    private List<String> ccEmails;

    private File sentFile;
    private Map<String,List<Event>> sent = new HashMap();
    private String url, userName, password;

    protected String eventType;
    protected Properties dbProps;
    protected String configResource;

    static
	public String val(String[] args, String... argNames){
	 boolean found = false;
	 String val = null;
	 for(int i=0;i<args.length;i++){
	     
	     String arg = args[i];
	     for(String aName :argNames){
		 if(arg.equals(aName)){
		     return args[i+1];
		 }
	     }
	 }
	 return val;
     }

     static
     public boolean is(String[] args, String... argNames){
	 boolean found = false;
	 String val = null;
	 for(int i=0;i<args.length;i++){
	     String arg = args[i];
	     for(String aName :argNames){
		 if(arg.equals(aName)){
		     return true;
		 }
	     }
	 }
	 return false;
     }

    public void go()
	throws Exception
    {
	Thread t = new Thread(this);
	t.start();

	while(!this.done()){
	    //
	    try{                            // 1sec  
		Thread.currentThread().sleep((1000)); //*10);
	    }catch(Throwable tx){
		// ignore
	    }
	}

    }

    public boolean done()
    {
	return isDone;
    }
    
    public void run()
    {
	Connection c = null;
	String curr = null;
	int counter = 0;
	try{
	    c = getSISConnection();
	    Map<String,List<Event>> upcoming = getEvents(c);
	    
	    for(String chartid : upcoming.keySet()){
		curr = chartid;
		log.info("Checking future appts for chartid: "+chartid);
		
		// pull the list of sent eventTypes for this
		// chart id
		List<Event> done = sent.get(""+chartid);
		if(done == null){
		    // initialize a new list if one does not exist
		    done = new ArrayList();
		    sent.put(""+chartid, done);
		}
		
		// get the list of eventTypes
		List<Event> appts = upcoming.get(chartid);
		List<Event> sentOk = new ArrayList();
		for(Event appt : appts){
		    if(done.contains(appt)){
			log.info(" ==["+appt+"]==x done already");
		    }else{
			// ok send the eventType
			isTest = true;
			
			//send(appt);
			//send(adminAddress.getAddress(), appt);
			
			if(isTest){
			    for(String admin : adminEmails){
				send(admin, appt);
				counter++;
			    }
			}
			sentOk.add(appt);

		    }
		}
		done.addAll(sentOk);
			
		// TESTING: just send one message
		if(counter > 2)
		    break;
	    }
	    
	    saveSent();
	}catch(Exception e){
	    log.error("Unable to check "+eventType+"s: "+e, e);
	    sendError("Unable to send "+eventType+" reminder for chartid: "+curr+":"+e, e);

	}finally{
	    try{ 
		saveSent();
	    }catch(Throwable t){
		log.error("Unable to save sent messages: "+t, t);
	    }

	    try{ 
		if(c != null) c.close();
	    }catch(Throwable t){
		log.error("Unable to close SIS connection: "+t, t);
	    }

	    isDone = true;

	}
    }
    
    public List<Map> fetch(Connection c, String query)
	throws Exception
    {
	List<Map> items = new ArrayList();	
	
	Statement stmt = null;
	try{
	    stmt = c.createStatement();
	    ResultSet rs = stmt.executeQuery(query);
	    
	    // Get result set meta data
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int numColumns = rsmd.getColumnCount();
	    
	    // Fetch each row from the result set
	    while (rs.next()) {
		// Get the data from the row using the column index
		Map u = new HashMap();
		
		for (int colNumber=1; colNumber <= numColumns; colNumber++) {
		    String colName = rsmd.getColumnName(colNumber).toLowerCase();
		    Object o = rs.getObject(colNumber);
		    u.put(colName,o);
		}
		items.add(u);
	    }
	}finally{
	    if(stmt != null) stmt.close();
	}
	
	return items;
    }


    public AbstractEventCheck(String type)
	throws Exception
    {
	this.eventType = type;
	checkInits();
    }


    public AbstractEventCheck(String type, String configResourcePath)
	throws Exception
    {
	this.eventType = type;
	this.configResource = configResourcePath;
	checkInits();
    }

    public AbstractEventCheck(String type, InputStream calStream)
	throws Exception
    {
	this(type);
	this.updateCalendar(calStream);
    }

    abstract
    public Map<String,List<Event>> getEvents(Connection c)
	throws Exception;
    

    public List<String> csvToList(String csvList){
	List<String> list = new ArrayList();
	
	if(csvList != null){
	    if(csvList.indexOf(",") > -1){
		for(String a : csvList.split(",")){
		    if(a.trim().length() > 0){
			list.add(a.trim());
		    }
		}
	    }else{
		if(csvList.trim().length() > 0){
		    list.add(csvList.trim());
		}
	    }
	}

	return list;
    }

    public void setSentFile(File f){
	sentFile = f;
    }

    public Connection getSISConnection()
	throws Exception
    { 
        Connection connection = DriverManager.getConnection(url, userName, password);
	log.debug("have connection:"+connection);
	//	this.connection.setAutoCommit(false);
	
	return connection;
    }

    public synchronized void saveSent()
	throws Exception
    {
	FileOutputStream fos = new FileOutputStream(this.sentFile);
	ObjectOutputStream oos = new ObjectOutputStream(fos);
	oos.writeObject(this.sent);
	oos.close();
    }

    public void loadSent()
	throws Exception
    {
	FileInputStream fis = new FileInputStream(this.sentFile);
	ObjectInputStream ois = new ObjectInputStream(fis);
	this.sent = (Map<String,List<Event>>) ois.readObject();
	ois.close();
    }

    public void send(Event event)
	throws Exception
    {
	send(event.getEmailAddress(), event);
    }

    public void send(String email, Event event)
	throws Exception
    {
	System.out.println("Notifying "+email+" of "+eventType+" "+event+"");
	Properties props = new Properties();
	props.put("mail.smtp.host", this.smtpHost);
		
	Session mailsession = Session.getDefaultInstance(props,null);
	
	MimeMessage message = new MimeMessage(mailsession);
		
	message.setFrom(this.adminAddress);
	message.setRecipient(Message.RecipientType.TO,new InternetAddress(email));
	for(String cc : this.ccEmails){
	    message.setRecipient(Message.RecipientType.BCC,new InternetAddress(cc));
	}

	message.setSubject(event.getEmailSubject());

	Date sentDate = new Date();
	message.setSentDate(sentDate);
	
	if(event.isHtml()){
	    this.formatHtml(message, event);
	}else{
	    String txt = event.getEmailText();
	    message.setText(txt);
	}

	Transport.send(message);

	sent_log.info("SENT: [reminder: "+df.format(sentDate)+", event:"+df.format(event.getDate())+"] "+event);
    }
    
    public void formatHtml(MimeMessage mail, Event event)
	throws Exception
    {
	//	MimeMessage mail =  new MimeMessage(mailSession);
	//mail.setSubject(subject);

	InputStream template = this.getClass().getResourceAsStream("/shs-email.html");
	InputStream logo = this.getClass().getResourceAsStream("/shs-logo.png");

	String message = template(template, event);

	MimeBodyPart messageBodyPart = new MimeBodyPart();
	messageBodyPart.setContent(message, "text/html");

	Multipart multipart = new MimeMultipart();
	multipart.addBodyPart(messageBodyPart);

	messageBodyPart = new MimeBodyPart();
	DataSource source = new ByteArrayDataSource(logo,"image/png");
	messageBodyPart.setDataHandler(new DataHandler(source));
	messageBodyPart.setFileName("shs-logo.png"); //fileAttachment.getName());
	messageBodyPart.setDisposition(MimeBodyPart.INLINE);
	messageBodyPart.setHeader("Content-ID","<logo>");
	multipart.addBodyPart(messageBodyPart);

	mail.setContent(multipart);
    }

     protected String template(InputStream template, Event event)
	throws FileNotFoundException, IOException 
    {
        String output = "";
        // FOR EACH LINE IN THE TEMPLATE FILE
        //declared here only to make visible to finally clause
	try{
	    String txt = event.getEmailText();
	    String subject = event.getEmailSubject();
	    String date = df.format(event.getDate());
	    String loc = event.getLocation();
	    BufferedReader input = new BufferedReader( new InputStreamReader(template) );
	    String line = null; //not declared within while loop
	    while (( line = input.readLine()) != null){
		
		if((line != null) && (event != null)){


		    //log.info("about to replace line:"+line);
		    line = line.replaceAll("@TITLE@",java.util.regex.Matcher.quoteReplacement(subject));
		    line = line.replaceAll("@LOCATION@",java.util.regex.Matcher.quoteReplacement(loc));
		    line = line.replaceAll("@CONTENT@",java.util.regex.Matcher.quoteReplacement(txt));
		    line = line.replaceAll("@DATE@", java.util.regex.Matcher.quoteReplacement(date));
		    //log.info("with line:"+line);
		}
		output += line + System.getProperty("line.separator");
	    }
	    
	    try {
		//flush and close both "input" and its underlying FileReader
		if (input!= null) input.close();
	    } catch (IOException ex) {
		ex.printStackTrace();
	    }
	}catch(Exception e){
	    log.error("Unable to process template ["+template+"] : "+e, e);
	    throw new IOException("Unable to process template ["+template+"]: "+e, e);
	}

        return output;
    }


    public void fail(String info, String chartid, Event event){
	List<Event> done = this.sent.get(""+chartid);
	if(done == null){
	    // initialize a new list if one does not exist
	    log.warn("in #fail, initializing new done list for chartid "+chartid);
	    done = new ArrayList();
	    sent.put(""+chartid, done);
	}

	log.warn("in #fail, chartid ["+chartid+"] has "+done.size());
	if(!done.contains(event)){
	    log.warn(" ==> have not yet sent event: "+event);
	    done.add(event);
	    sendError(info);
	    /*
	    try{
		saveSent();
	    }catch(Throwable t){
		log.error("Unable to save sent messages after sendError: "+t);
	    }
	    */
	}else{
	    log.warn("Already sent error: "+info);
	}
    }


    public void sendError(String info){

	RuntimeException rte = new RuntimeException(info);
	// shs admins
	for(String admin : adminEmails){
	    sendError(admin, info, rte);
	}

	// the technical admin
	sendError(info, rte);
    }

    public void sendError(String info, Throwable t)

    {
	String email = this.adminAddress.getAddress();
	sendError(email,info,t);
    }

    public void sendError(String email, String info, Throwable t)
    {
	String msg = info+"\n\n"+eventType+" error: "+t.getMessage();
	Throwable cause = t.getCause();
	if(cause != null) msg += "\n\nCaused By: "+cause.getMessage();

	String subj = ""+eventType+" Reminder ERROR";
	if(t != null) subj += ": "+t.getMessage();

	try{

	    log.error("Notifying "+email+" of error on ["+new Date()+"]. Error is: "+subj);
	    Properties props = new Properties();
	    props.put("mail.smtp.host", this.smtpHost);
	    
	    Session mailsession = Session.getDefaultInstance(props,null);
	    
	    Message message = new MimeMessage(mailsession);
	    
	    message.setFrom(this.adminAddress);
	    message.setRecipient(Message.RecipientType.TO,new InternetAddress(email));

	    message.setSubject(subj); 

	    message.setText(msg);
	
	    message.setSentDate(new Date());
	
	    Transport.send(message);
	}catch(Exception ex){
	    log.error("Unable to send admin error message: "+ex, ex);
	    log.error("Original error is: "+msg);
	}

    }
    
    private void checkInits()
    {
	CompatibilityHints.setHintEnabled(KEY_RELAXED_PARSING, true);
	CompatibilityHints.setHintEnabled(KEY_RELAXED_UNFOLDING, true);
	CompatibilityHints.setHintEnabled(KEY_RELAXED_VALIDATION, true);

	try{
	    if(this.configResource == null){
		this.configResource = "/appointment.properties";
	    }

	    dbProps = new Properties();
	    dbProps.load(this.getClass().getResourceAsStream(this.configResource));
	    String driver = dbProps.getProperty("driver");
	    if(driver != null){

		Driver db2 = (Driver)Class.forName(driver).newInstance();
		
		log.trace("Have Driver: "+db2);
		DriverManager.registerDriver(db2);
		log.trace("Registered driver");
		this.url = dbProps.getProperty("url");
		log.trace("have url: "+url);
		this.userName = dbProps.getProperty("dbUser");
		log.trace("have userNAme: "+userName);
		this.password = dbProps.getProperty("dbPassword");
	    }else{
		log.debug("No database driver specified, skipping db setup");
	    }

	    if(dbProps.getProperty("event.lookahead") != null)
		this.lookahead = Integer.parseInt(dbProps.getProperty("event.lookahead"));
	    if(dbProps.getProperty("appointment.admin.email.list") != null)	    
		this.adminEmails = csvToList(dbProps.getProperty("appointment.admin.email.list"));

	    if(this.adminAddress == null){
		this.setAdminAddress(dbProps.getProperty("email.return.address"),
				     dbProps.getProperty("email.return.recipient"));
	    }

	    this.ccEmails = csvToList(dbProps.getProperty("email.cc.address.list"));
	    
	    if(this.smtpHost == null){
		this.setSmtpHost(dbProps.getProperty("email.smtp.host.address"));
	    }

	    if(dbProps.getProperty("sentFile") != null){
		this.sentFile = new File(dbProps.getProperty("sentFile"));
	    
		if(this.sentFile.exists()){
		    try{
			this.loadSent();
			
			// FIXME: we need to add the ability to purge the sent 
			// of old records. 
		    }catch(Throwable t){
			log.error("Unable to load sent "+eventType+"s file: "+t, t);
		    }
		}
	    }

	    //   Properties props = new Properties();
	    //props.load(HQ.class.getResourceAsStream("/hq.config.properties"));
	    if(this.calendar == null){
		URL url = new URL(dbProps.getProperty("calendar.url"));
		InputStream in = url.openStream();

		this.updateCalendar(in);
	    }

	    if(this.defaultLookahead == null
	       && (dbProps.getProperty("event.lookahead") != null)){
		this.setLookahead(Integer.parseInt(dbProps.getProperty("event.lookahead")));

	    }
	}catch(Throwable t){
	    throw new RuntimeException("Unable to initialize: "+t, t);
	}
    }

    public void setSmtpHost(String newHost){
	this.smtpHost = newHost;
    }

    public void setAdminAddress(String email, String name)
	throws Exception
    {
	this.adminAddress = new InternetAddress(email, name);
    }

    public void setLookahead(int days){
	this.defaultLookahead = new Dur(days, 0,0,0);
    }

    public void updateCalendar(InputStream calStream)
	throws Exception
    {
	CalendarBuilder builder = new CalendarBuilder();
	
	this.calendar = builder.build(calStream);
    }

    public Collection getEventsToday(){
	return getEvents(new Dur(1, 0, 0, 0));
    }

    public DateTime today(){
	java.util.Calendar today = java.util.Calendar.getInstance();
	today.set(java.util.Calendar.HOUR_OF_DAY, 0);
	today.clear(java.util.Calendar.MINUTE);
	today.clear(java.util.Calendar.SECOND);
	return new DateTime(today.getTime());
    }

    public Collection getEvents(Dur span, Rule... otherRules){
	
	// create a period starting now with a duration of one (1) day..
	Period period = new Period(today(), span);

	return getEvents(period, otherRules);
    }

    public Collection getEvents(Period period, Rule... otherRules){

	// OR: Create the date range which is desired.
	//DateTime from = new DateTime("20100101T070000Z");
	//DateTime to = new DateTime("20100201T070000Z");;
	//Period period = new Period(from, to);

	List rules = new ArrayList();
	if(otherRules != null){
	    rules.addAll(Arrays.asList(otherRules));
	}
	rules.add( new PeriodRule(period));
	Filter filter = new Filter((Rule [])rules.toArray(new Rule[]{}), Filter.MATCH_ALL);

	ComponentList allEvents = this.calendar.getComponents(Component.VEVENT);
	if((allEvents == null) || (allEvents.size()< 1)){
	    System.out.println("No events: "+allEvents);
	    return null;
	}else{
	    Collection eventsToday = filter.filter(allEvents);
	    return eventsToday;
	}
    }
    
    /**
     *  Sends 
     */
    public void notify(String[] emails, Property... props)
	throws Exception
    {
	Rule[] rules = new Rule[props.length];
	for(int i=0; i< props.length; i++){
	    Property prop = props[i];
	    HasPropertyRule thisType = new HasPropertyRule(prop);
	    rules[i] = thisType;
	}

	notify(emails, rules);
    }

    public void notify(String[] emails, Rule... rules)
	throws Exception
    {
	Period period = new Period(this.today(), this.defaultLookahead);
	Collection events = this.getEvents(period, rules);

	for(Object event : events){
	    Period next  = this.getNext(period, (VEvent)event).get(0);
	    for(String email : emails){
		this.notify(email, (VEvent)event, next.getStart());
	    }
	}

    }

    public void notify(String email, VEvent event, DateTime next)
	throws Exception
    {
	System.out.println("Notifying "+email+" of "+event+" of class ["+event.getClass()+"]");
	Properties props = new Properties();
	props.put("mail.smtp.host", this.smtpHost);
		
	Session mailsession = Session.getDefaultInstance(props,null);
	
	Message message = new MimeMessage(mailsession);
		
	message.setFrom(this.adminAddress);
	message.setRecipient(Message.RecipientType.TO,new InternetAddress(email));

	String loc = event.getLocation().getValue();	
	String summary = event.getSummary().getValue().trim();
	if(summary.endsWith("(P)")){
	    summary = summary.substring(0, summary.length()-3)+"@ Parnassus";
	}else 	if(summary.endsWith("(MB)")){
	    summary = summary.substring(0, summary.length()-4)+"@ Mission Bay";
	}

	String desc = event.getDescription().getValue();
	RecurrenceId recurr = event.getRecurrenceId();
	/*
	Date date = event.getStartDate().getDate();
	if(recurr != null){
	    System.out.println("IS RECURRING!");
	    date = recurr.getDate();
	}
	*/

	message.setSubject("Reminder: "+summary); //"SHCS Event Reminder");
	message.setText(//"This is a reminder for "+summary+"..."+
			//"WHAT:"+	desc+"\n"+
			"WHERE: "+ loc+"\n"
			//	"WHEN: "+df.format(next.date)+"\n"
			);
	
	message.setSentDate(new Date());
	
	Transport.send(message);

    }

    public List<Period> getNext(Period period, VEvent event){

	Component c = (Component)event;
	PeriodList list = c.calculateRecurrenceSet(period);
	List next = new ArrayList();
	for (Object po : list) {
	    System.out.println((Period)po);
	    next.add((Period)po);
	}
	
	return next;
    }
}