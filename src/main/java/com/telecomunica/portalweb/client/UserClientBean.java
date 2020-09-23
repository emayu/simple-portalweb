/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telecomunica.portalweb.client;

import com.telecomunica.portalweb.model.Role;
import com.telecomunica.portalweb.model.User;
import com.telecomunica.portalweb.model.UserRole;
import com.telecomunica.portalweb.model.util.JsfUtil;
import com.telecomunica.portalweb.rest.Resource;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;



/**
 *
 * @author f32
 */
@Named
@RequestScoped
public class UserClientBean {
    
    private Client client;
    private  WebTarget target;
    private  WebTarget userRoleTarget;
    private ResourceBundle bundle;
    
    @Inject 
    HttpServletRequest httpServletRequest;
    
    @Inject
    UserBackingBean bean;
    
    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("test", "secret");
        client.register(feature);
        target = client.target("http://" + httpServletRequest.getLocalName() + ":" + httpServletRequest.getLocalPort() + httpServletRequest.getContextPath() + "/rest/user/");
        userRoleTarget = client.target("http://" + httpServletRequest.getLocalName() + ":" + httpServletRequest.getLocalPort() + httpServletRequest.getContextPath() + "/rest/userrole/");
        bundle = ResourceBundle.getBundle("Bundle");
        
    }
    
    @PreDestroy
    public void destroy(){
        client.close();
    }
    
    public User[] getUsers() {
        return target.request().get(User[].class);
    }
    
    public User getUserInfo(){
        User u = target
                .path("{userId}")
                .resolveTemplate("userId", bean.getId())
                .request()
                .get(User.class);
                
        return u;
    }
    
    public Role getRol(){
        Role[] roles = getRolesByUser();
        if(roles != null && roles.length > 0){
            return roles[0];
        }
        return null;
    }
    
    public Role getRol(int userId){
        Role[] roles = getRolesByUser(userId);
        if(roles != null && roles.length > 0){
            return roles[0];
        }
        return null;
    }
    
        
    public String validateRole(){
        Role[] roles = getRolesByUser();
        if(roles != null && roles.length > 0){
            JsfUtil.addSuccessMessage(bundle.getString("Welcome") + " " + getUserInfo().getName());
            return "welcome.xhtml";
        }else{
            JsfUtil.addErrorMessage(bundle.getString("NoRoleAssociated"));
        }
        return "";
    }
    
    private Role[] getRolesByUser(int userRole){
        Role[] roles = userRoleTarget
                .path("user/{userId}")
                .resolveTemplate("userId", userRole)
                .request()
                .get(Role[].class);
        return roles;
    }
    
    private Role[] getRolesByUser(){
        return getRolesByUser(bean.getId());
    }
    
    public void deleteRole(){
        userRoleTarget
                .path("foo")
                .matrixParam("userId", bean.getId())
                .matrixParam("roleId", bean.getOldRole())
                .request()
                .delete();
    }
    
    public void updateRole(){
        //delete record
        deleteRole();
        //insert new register
        UserRole ur = new UserRole(bean.getId(), bean.getNewRole());
        Response r = userRoleTarget
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(ur, MediaType.APPLICATION_JSON));
        if(r.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL){
            JsfUtil.addErrorMessage(bundle.getString("ErrorOnCreateRecord") + r.getStatusInfo().getReasonPhrase());
        }else{
            JsfUtil.addSuccessMessage(bundle.getString("RoleAssociated"));
        }
                
    }
    
    public void createUser(){
        User u = new User(bean.getId());
        u.setName(bean.getUserName());
        Response r = target
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(u, MediaType.APPLICATION_JSON));
        if(r.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL){
            JsfUtil.addErrorMessage(bundle.getString("ErrorOnCreateRecord") + r.getStatusInfo().getReasonPhrase());
        }else{
            JsfUtil.addSuccessMessage(bundle.getString("UserCreated"));
        }
        
    }
    
    
}
