/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telecomunica.portalweb.service;

import com.telecomunica.portalweb.model.Role;
import com.telecomunica.portalweb.model.UserRole;
import com.telecomunica.portalweb.model.dto.UserRoleDto;
import com.telecomunica.portalweb.model.UserRolePK;
import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
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
import javax.ws.rs.core.PathSegment;
import java.util.Set;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author f32
 */
@Stateless
@Path("userrole")
@RolesAllowed({"USER_ADMIN","USER_ROLE"})
public class UserRoleFacadeREST extends AbstractFacade<UserRole> {

    @PersistenceContext(unitName = "telecomunicasa-auth")
    private EntityManager em;

    private UserRolePK getPrimaryKey(PathSegment pathSegment) {
        /*
         * pathSemgent represents a URI path segment and any associated matrix parameters.
         * URI path part is supposed to be in form of 'somePath;userId=userIdValue;roleId=roleIdValue'.
         * Here 'somePath' is a result of getPath() method invocation and
         * it is ignored in the following code.
         * Matrix parameters are used as field names to build a primary key instance.
         */
        com.telecomunica.portalweb.model.UserRolePK key = new com.telecomunica.portalweb.model.UserRolePK();
        javax.ws.rs.core.MultivaluedMap<String, String> map = pathSegment.getMatrixParameters();
        java.util.List<String> userId = map.get("userId");
        if (userId != null && !userId.isEmpty()) {
            key.setUserId(new java.lang.Integer(userId.get(0)));
        }
        java.util.List<String> roleId = map.get("roleId");
        if (roleId != null && !roleId.isEmpty()) {
            key.setRoleId(Integer.parseInt(roleId.get(0)));
        }
        return key;
    }

    public UserRoleFacadeREST() {
        super(UserRole.class);
    }

    @POST
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response createUserRole(UserRole entity) {
        super.create(entity);
        return Response
                .status(Response.Status.CREATED)
                .entity(entity)
                .build();
        
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") PathSegment id, UserRole entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") PathSegment id) {
        com.telecomunica.portalweb.model.UserRolePK key = getPrimaryKey(id);
        super.remove(super.find(key));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public UserRole find(@PathParam("id") PathSegment id) {
        com.telecomunica.portalweb.model.UserRolePK key = getPrimaryKey(id);
        return super.find(key);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<UserRole> findAll() {
        return super.findAll();
    }
    
    @GET
    @Path("byUserId/{userId}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Role>  findRolesByUserId(@PathParam("userId")int userId) {
        Query q = em.createNamedQuery("UserRole.findRoleByUserId").setParameter("userId", userId);
        return q.getResultList();
    }
    
    @GET
    @Path("byUserName/{userName}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Role>  findRolesByUserName(@PathParam("userName")String name) {
        Query q = em.createNamedQuery("UserRole.findRoleByUserName").setParameter("userName", name);
        return q.getResultList();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<UserRole> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }
    
    @GET
    @Path("allDescription")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<UserRoleDto> findAllDescription() {
        Query q = em.createNamedQuery("UserRole.getAllUserWithRole", UserRoleDto.class);
        return q.getResultList();
    }
    
    /** TESTING FOR FUTURE
    private static final Set<String> VALID_COLUMNS_FOR_ORDER_BY  = 
            Set.of("idUser","userName","idRole", "roleName");
    
    @GET
    @Path("allDescription")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<UserRoleDto> findAllDescription(@DefaultValue("idUser") @QueryParam("orderby") String orderBy) {
        StringBuilder query = new StringBuilder("select u.id idUser, u.name userName, r.id idRole, r.name roleName "
                    + " FROM USER u "
                    + " LEFT JOIN USER_ROLE ur on u.id = ur.user_id"
                    + " LEFT JOIN ROLE r on r.id = ur.role_id");
        
        if(VALID_COLUMNS_FOR_ORDER_BY.contains(orderBy)){
            query.append(" ORDER BY ").append(orderBy);
        }
        Query q = em.createNativeQuery(query.toString(), UserRoleDto.class);
        q.setParameter("orderInfo", orderBy);
        return q.getResultList();
    } **/

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
