/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telecomunica.portalweb.rest;


import javax.annotation.security.DeclareRoles;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationScoped
@ApplicationPath("/rest/v1")
@DeclareRoles({ "USER_ROLE", "USER_ADMIN" })
public class JaxRsActivator extends Application {

}