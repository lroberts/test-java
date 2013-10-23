//  StrutsTestCase - a JUnit extension for testing Struts actions
//  within the context of the ActionServlet.
//  Copyright (C) 2002 Deryl Seale
//
//  This library is free software; you can redistribute it and/or
//  modify it under the terms of the Apache Software License as
//  published by the Apache Software Foundation; either version 1.1
//  of the License, or (at your option) any later version.
//
//  This library is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  Apache Software Foundation Licens for more details.
//
//  You may view the full text here: http://www.apache.org/LICENSE.txt

package servletunit.struts.tests;

import org.junit.*;

import servletunit.struts.MockStrutsTestCase;
import org.junit.*;

public class TestTilesForward extends MockStrutsTestCase {


    @Before
	public void setUp() throws Exception {
        super.setUp();
        setConfigFile("tiles","/WEB-INF/struts-config-tiles.xml");
        setServletConfigFile("/WEB-INF/web.xml");
        setConfigFile("/WEB-INF/struts-config.xml");
    }


    @Test
	public void testTilesForward() {
        addRequestParameter("username","deryl");
        addRequestParameter("password","radar");
        setRequestPathInfo("tiles","/tilesForward.do");
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/layouts/pageLayout.jsp");
        verifyTilesForward("success","page.library");
    }

    @Test
	public void testTilesInputForward() {
        setRequestPathInfo("tiles","/tilesInputForward.do");
        actionPerform();
        verifyInputForward();
        verifyInputTilesForward("page.library");
    }

    @Test
	public void testTileForwardFail() {
        addRequestParameter("username","deryl");
        addRequestParameter("password","radar");
        setRequestPathInfo("tiles","/tilesForward.do");
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/layouts/pageLayout.jsp");
        try {
        verifyTilesForward("success","foo.fail");
        } catch (RuntimeException afe) {
            return;
        }
        fail("Should have failed.");
    }

//    todo: this test is failing because tiles verify doesn't work properly
//    @Test
//	public void testTileForwardFailDefinitionExists() {
//        addRequestParameter("username","deryl");
//        addRequestParameter("password","radar");
//        setRequestPathInfo("tiles","/tilesForward.do");
//        actionPerform();
//        verifyForward("success");
//        verifyForwardPath("/layouts/pageLayout.jsp");
//        try {
//        verifyTilesForward("failure","another.page");
//        } catch (RuntimeException afe) {
//            return;
//        }
//        fail("Should have failed.");
//    }


}
