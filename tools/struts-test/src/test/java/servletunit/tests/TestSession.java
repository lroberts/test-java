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

package servletunit.tests;

import org.junit.*;
import servletunit.HttpServletRequestSimulator;

import javax.servlet.http.HttpSession;

public class TestSession extends servletunit.struts.BaseCase {

    HttpServletRequestSimulator request;

    @Before
	public void setUp() {
        this.request = new HttpServletRequestSimulator(null);
    }

    @Test
	public void testGetSession() {
        assertNotNull(request.getSession());
    }

    @Test
	public void testGetSessionTrue() {
        assertNotNull(request.getSession(true));
    }

    @Test
	public void testGetSessionFalse() {
        assertNull(request.getSession(false));
    }

    @Test
	public void testGetSessionFalseSessionExists() {
        request.getSession();
        assertNotNull(request.getSession(false));
    }

    @Test
	public void testGetSessionInvalid() {
        request.getSession().invalidate();
        assertNotNull(request.getSession(true));
    }

    @Test
	public void testGetSessionInvalidFalse() {
        request.getSession().invalidate();
        assertNull(request.getSession(false));
    }

    @Test
	public void testSetAttributeNull() {
        HttpSession session = request.getSession();
        session.setAttribute("test","test");
        assertEquals("test",session.getAttribute("test"));
        session.setAttribute("test",null);
        assertNull(session.getAttribute("test"));
    }


}
