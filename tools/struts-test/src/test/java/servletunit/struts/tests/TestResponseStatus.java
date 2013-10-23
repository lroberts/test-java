package servletunit.struts.tests;

import org.junit.*;

import servletunit.struts.MockStrutsTestCase;
import servletunit.HttpServletResponseSimulator;
import org.junit.*;

/**
 * Created by IntelliJ IDEA.
 * User: deryl
 * Date: May 20, 2003
 * Time: 5:16:57 PM
 * To change this template use Options | File Templates.
 */
public class TestResponseStatus extends MockStrutsTestCase {

    @Before
	public void setUp() throws Exception {
        super.setUp();
        setServletConfigFile("/WEB-INF/web.xml");
    }

    @Test
	public void testResponseCode() {
        setRequestPathInfo("/badActionPath");
        try {
            actionPerform();
        } catch (RuntimeException afe) {
            int statusCode = ((HttpServletResponseSimulator) getResponse()).getStatus();
            // todo: backwards compatible with struts 1.1
            assertTrue("unexpected response code",statusCode == 404 || statusCode == 400);
            return;
        }
        fail("expected some error code!");

    }


}
