/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telecomunica.portalweb.client;

import com.telecomunica.portalweb.model.Role;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

/**
 *
 * @author f32
 */
@Named
@RequestScoped
public class RoleClientBean {
    Client client;
    WebTarget target;
    @Inject 
    HttpServletRequest httpServletRequest;
    
    
    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("test", "secret");
        client.register(feature);
        target = client.target("http://" + httpServletRequest.getLocalName() + ":" + httpServletRequest.getLocalPort() + httpServletRequest.getContextPath() + "/rest/role/");
        
    }
    
    public Role[] getRoles() {
        return target.request().get(Role[].class);
    }
    
    @PreDestroy
    public void destroy(){
        client.close();
    }
    
    
    
}
