package servletunit.struts.tests;

import org.junit.*;

import servletunit.struts.MockStrutsTestCase;
import org.junit.*;

/**
 * Created by IntelliJ IDEA.
 * User: deryl
 * Date: Apr 28, 2003
 * Time: 9:26:32 AM
 * To change this template use Options | File Templates.
 */
public class TestBadModuleName extends MockStrutsTestCase {


    @Before
	public void setUp() throws Exception {
        super.setUp();
        setServletConfigFile("/WEB-INF/web.xml");
        setConfigFile("tiles","/WEB-INF/struts-config-tiles.xml");
    }

    @Test
	public void testBadName() {
        try {
        addRequestParameter("username","deryl");
        addRequestParameter("password","radar");
        setRequestPathInfo("doesnotexist","/tilesForward");
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/layouts/pageLayout.jsp");
        } catch (RuntimeException afe) {
            return;
        }
        fail("Should have thrown a JUnit error!");
    }

}
