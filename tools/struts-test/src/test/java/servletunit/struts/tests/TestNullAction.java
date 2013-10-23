package servletunit.struts.tests;

import org.junit.*;

import servletunit.struts.MockStrutsTestCase;

/**
 * Created by IntelliJ IDEA.
 * User: deryl
 * Date: Apr 13, 2003
 * Time: 12:07:32 PM
 * To change this template use Options | File Templates.
 */
public class TestNullAction extends MockStrutsTestCase {

    @Before
	public void setUp() throws Exception {
        super.setUp();
        setServletConfigFile("/WEB-INF/web.xml");
    }

    @Test
	public void testNullAction() {
        setRequestPathInfo("test","/testNullAction.do");
        actionPerform();
        verifyForward(null);
        verifyForwardPath(null);
    }
}
