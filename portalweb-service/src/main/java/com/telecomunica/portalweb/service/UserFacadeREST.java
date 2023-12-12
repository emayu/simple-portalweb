/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telecomunica.portalweb.service;

import com.telecomunica.portalweb.model.User;
import com.telecomunica.portalweb.rest.DatabaseSetup;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author f32
 */
@Stateless
@Path("user")
@RolesAllowed({"USER_ADMIN","USER_ROLE"})
public class UserFacadeREST extends AbstractFacade<User> {

    @PersistenceContext(unitName = "telecomunicasa-auth")
    private EntityManager em;
    
    @Inject
    private DatabaseSetup setup;

    public UserFacadeREST() {
        super(User.class);
    }
    
    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createUser(User entity) {
        if(entity.getPassword() == null || entity.getPassword().isBlank()){
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("The field password is not set")
                    .build();
        }
        entity.setPassword(setup.generateHash(entity.getPassword()));
        super.create(entity);
        getEntityManager().flush();
        return Response
                .status(Response.Status.CREATED)
                .entity(entity)
                .build();
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, User entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public Response remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
        return Response.noContent().build();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response find(@PathParam("id") Integer id) {
        User u = super.find(id);
        if( u == null){
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity(null)
                    .build();
        }
        return Response.ok(u).build();
    }
    
    @GET
    @Path("byName/{name}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findByUserName(@PathParam("name") String name) {
        Query q = em.createNamedQuery("User.findByName").setParameter("name", name);
        return  q.getResultList();
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findAll() {
        List<User> userListInfo =  super.findAll();
        return userListInfo.stream().map(user -> {
            getEntityManager().detach(user);
            user.setPassword(null);
            return user;
                }).collect(Collectors.toList());
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<User> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
