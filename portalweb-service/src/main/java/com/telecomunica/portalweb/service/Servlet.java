/**
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * https://github.com/javaee/tutorial-examples/LICENSE.txt
 */

package com.telecomunica.portalweb.service;

import java.io.IOException;
import java.security.Principal;

import javax.annotation.security.DeclareRoles;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Test Servlet that prints out the name of the authenticated caller and whether
 * this caller is in any of the roles {foo, bar, kaz}
 *
 */

@WebServlet("/servlet")
@DeclareRoles({ "USER_ROLE", "USER_ADMIN", "FOO" })
@ServletSecurity(@HttpConstraint(rolesAllowed = {"USER_ROLE", "USER_ADMIN"}))
public class Servlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final Principal userPrincipal = request.getUserPrincipal();
        String webName = null;
        if (userPrincipal != null) {
            webName = userPrincipal.getName();
        }

        response.getWriter().write("web username: " + webName + "\n");

        response.getWriter().write("web user has role \"USER_ROLE\": " + request.isUserInRole("USER_ROLE") + "\n");
        response.getWriter().write("web user has role \"USER_ADMIN\": " + request.isUserInRole("USER_ADMIN") + "\n");
        response.getWriter().write("web user has role \"kaz\": " + request.isUserInRole("kaz") + "\n");
    }

}
