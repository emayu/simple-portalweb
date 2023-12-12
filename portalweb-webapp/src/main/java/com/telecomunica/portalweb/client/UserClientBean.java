
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telecomunica.portalweb.client;

import com.telecomunica.portalweb.WebPagesConfig;
import com.telecomunica.portalweb.model.Role;
import com.telecomunica.portalweb.model.User;
import com.telecomunica.portalweb.model.UserRole;
import com.telecomunica.portalweb.model.dto.UserRoleDto;
import com.telecomunica.portalweb.util.JsfUtil;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private static final Logger LOG = Logger.getLogger(UserClientBean.class.getName());
    
    private Client client;
    private  WebTarget userTarget;
    private  WebTarget userXRoleTarget;
    private ResourceBundle bundle;
    
    @Inject 
    HttpServletRequest httpServletRequest;
    
    @Inject
    UserBackingBean bean;
    
    @Inject
    UserSessionBean session;
    
    @PostConstruct
    public void init() {
        client = ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(session.getUser(), session.getPassword());
        client.register(feature);
        userTarget = client.target(WebPagesConfig.getGlobalBaseURLClientTarget() + "/user/");
        userXRoleTarget = client.target(WebPagesConfig.getGlobalBaseURLClientTarget() + "/userrole/");
        bundle = ResourceBundle.getBundle("Bundle");
        
    }
    
    @PreDestroy
    public void destroy(){
        client.close();
    }
    
    public User[] getUsers() {
        return userTarget.request().get(User[].class);
    }
    
    public User getUserInfo(){
        User u = userTarget
                .path("{userId}")
                .resolveTemplate("userId", bean.getId())
                .request()
                .get(User.class);
                
        return u;
    }
    
    public UserRoleDto[] getUserRoleMap(){
        Response r = userXRoleTarget
               .path("allDescription")
               .request().get();
        if( r.getStatusInfo().toEnum() == Response.Status.OK){
           return r.readEntity(UserRoleDto[].class);
       }
       return null;
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
    
    public Role getRoleByUserName(String userName){
        Response r = userXRoleTarget
                .path("byUserName/{userName}")
                .resolveTemplate("userName", userName)
                .request()
                .get();
        if (r.getStatusInfo().toEnum() == Response.Status.OK) {
            Role[] roles = r.readEntity(Role[].class);
            if (roles != null && roles.length > 0) {
                return roles[0];
            }
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
        Role[] roles = userXRoleTarget
                .path("byUserId/{userId}")
                .resolveTemplate("userId", userRole)
                .request()
                .get(Role[].class);
        return roles;
    }
    
    private Role[] getRolesByUser(){
        return getRolesByUser(bean.getId());
    }
    
    public void deleteRole(){
        
        if(bean.getOldRole() != null){   
            
            WebTarget target = userXRoleTarget
                .path("foo");
            LOG.info("target " + target.getUri().toString());
            var request = target
                .matrixParam("userId", bean.getId())
                .matrixParam("roleId", bean.getOldRole())
                .request();
            var response = request.delete();
            LOG.info("response status " + response.getStatusInfo().toString());
        }
    }
    
    public void updateRole(){
        //delete record
        deleteRole();
        //insert new register
        UserRole ur = new UserRole(bean.getId(), bean.getNewRole());
        Response r = userXRoleTarget
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
        u.setPassword(bean.getPassword());
        Response r = userTarget
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(u, MediaType.APPLICATION_JSON));
        if(r.getStatusInfo().getFamily() != Response.Status.Family.SUCCESSFUL){
            JsfUtil.addErrorMessage(bundle.getString("ErrorOnCreateRecord"));
            LOG.log(Level.SEVERE, "Error code: " + r.getStatusInfo().getReasonPhrase() + ", response: "+ r.readEntity(String.class));
        }else{
            JsfUtil.addSuccessMessage(bundle.getString("UserCreated"));
        }
        
    }
    
    
}
