package servletunit.struts.tests;

import org.junit.*;

import servletunit.struts.MockStrutsTestCase;
import servletunit.HttpServletRequestSimulator;
import servletunit.HttpServletResponseSimulator;

import java.security.Principal;

/**
 * Created by IntelliJ IDEA.
 * User: deryl
 * Date: May 21, 2003
 * Time: 2:25:53 PM
 * To change this template use Options | File Templates.
 */
public class TestGetMockObjects extends MockStrutsTestCase {

    @Test
	public void testGetMockObjects() {
        HttpServletRequestSimulator request = this.getMockRequest();
        request.setUserRole("foo");
        HttpServletResponseSimulator response = this.getMockResponse();
        response.getStatus();
    }
}
