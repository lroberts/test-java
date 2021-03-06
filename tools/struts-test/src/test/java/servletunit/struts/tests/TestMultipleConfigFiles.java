package servletunit.struts.tests;

import org.junit.*;

import servletunit.struts.MockStrutsTestCase;

public class TestMultipleConfigFiles extends MockStrutsTestCase
 {

    @Before
	public void setUp() throws Exception {
        super.setUp();
        setServletConfigFile("/WEB-INF/web.xml");
        setConfigFile("/WEB-INF/struts-config-tiles.xml,/WEB-INF/struts-config.xml");
    }

    @Test
	public void testTilesForward() {
        addRequestParameter("username","deryl");
        addRequestParameter("password","radar");
        setRequestPathInfo("/tilesForward.do");
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/layouts/pageLayout.jsp");
        verifyTilesForward("success","page.library");
        clearRequestParameters();
        addRequestParameter("username","deryl");
        addRequestParameter("password","radar");
        setRequestPathInfo("/login");
        actionPerform();
        verifyForward("success");
    }
}
