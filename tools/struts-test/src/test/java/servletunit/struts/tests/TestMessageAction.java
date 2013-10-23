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
//  You may view the full text here: http://www.apache.org/LICENSE.txt

package servletunit.struts.tests;

import org.junit.*;

import servletunit.struts.MockStrutsTestCase;
import org.junit.*;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

public class TestMessageAction extends MockStrutsTestCase {

    @Before
	public void setUp() throws Exception {
        super.setUp();
        setServletConfigFile("/WEB-INF/web.xml");
    }

    @Test
	public void testNoMessages() {
        addRequestParameter("username","deryl");
        addRequestParameter("password","radar");
        setRequestPathInfo("/login");
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/main/success.jsp");
        assertEquals("deryl",getSession().getAttribute("authentication"));
        verifyNoActionMessages();
    }

    @Test
	public void testMessageExists() {
        setRequestPathInfo("test","/testActionMessages");
        actionPerform();
        verifyForward("success");
        verifyActionMessages(new String[] {"test.message"});
    }

     @Test
	public void testMessageExistsExpectedNone() {
        setRequestPathInfo("test","/testActionMessages");
        actionPerform();
        verifyForward("success");
        try {
            verifyNoActionMessages();
        } catch (RuntimeException afe) {
            return;
        }
        fail("Expected an RuntimeException!");
    }

    @Test
	public void testMessageMismatch() {
        setRequestPathInfo("test","/testActionMessages");
        actionPerform();
        verifyForward("success");
        try {
            verifyActionMessages(new String[] {"error.password.mismatch"});
        } catch (RuntimeException afe) {
            return;
        }
        fail("Expected an RuntimeException!");
    }

    @Test
	public void testExpectedMessagesNoneExist() {
        addRequestParameter("username","deryl");
        addRequestParameter("password","radar");
        setRequestPathInfo("/login");
        actionPerform();
        verifyForward("success");
        verifyForwardPath("/main/success.jsp");
        assertEquals("deryl",getSession().getAttribute("authentication"));
        try {
        verifyActionMessages(new String[] {"test.message"});
        } catch (RuntimeException afe) {
            return;
        }
        fail("Expected RuntimeException!");
    }

    @Test
	public void testVerifiesComplexErrorMessageScenario() {
        ActionErrors errors = new ActionErrors();
        errors.add("error1",new ActionMessage("error1"));
        errors.add("error2",new ActionMessage("error2"));
        errors.add("error1",new ActionMessage("error1"));
        getRequest().setAttribute(Globals.ERROR_KEY,errors);
        try {
        verifyActionErrors(new String[] {"error1","error2","error2"});
        } catch (RuntimeException ex) {
            return;
        }
        fail("should not have passed!");
    }

    public static void main(String[] args) {
        // junit.textui.TestRunner.run(TestMessageAction.class);
    }


}