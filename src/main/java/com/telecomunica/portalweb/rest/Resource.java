/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telecomunica.portalweb.rest;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/resource")
@Produces(TEXT_PLAIN)
public class Resource {

    @GET
    @Path("hi")
    @RolesAllowed("a")
    public String hi() {
       return "hi!";
    }


}