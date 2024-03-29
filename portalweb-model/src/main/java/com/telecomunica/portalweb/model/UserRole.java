/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telecomunica.portalweb.model;

import com.telecomunica.portalweb.model.dto.UserRoleDto;
import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author f32
 */
@Entity
@Table(name = "USER_ROLE")
@NamedNativeQueries({
    @NamedNativeQuery(name = "UserRole.getAllUserWithRole",
            query = "select u.id idUser, u.name userName, r.id idRole, r.name roleName "
                    + " FROM USER u "
                    + " LEFT JOIN USER_ROLE ur on u.id = ur.user_id"
                    + " LEFT JOIN ROLE r on r.id = ur.role_id ",
            resultClass = UserRoleDto.class)
})
@NamedQueries({
    @NamedQuery(name = "UserRole.findAll", query = "SELECT u FROM UserRole u"),
    @NamedQuery(name = "UserRole.findByUserId", query = "SELECT u FROM UserRole u WHERE u.userRolePK.userId = :userId"),
    @NamedQuery(name = "UserRole.findByRoleId", query = "SELECT u FROM UserRole u WHERE u.userRolePK.roleId = :roleId"),
    @NamedQuery(name = "UserRole.findRoleByUserId",
            query = "SELECT r FROM UserRole ur INNER JOIN Role r on r.id=ur.userRolePK.roleId"
                    + " INNER JOIN User u on u.id=ur.userRolePK.userId where u.id = :userId "),
    @NamedQuery(name = "UserRole.findRoleByUserName",
            query = "SELECT r FROM UserRole ur INNER JOIN Role r on r.id=ur.userRolePK.roleId"
                    + " INNER JOIN User u on u.id=ur.userRolePK.userId where u.name = :userName ")})

public class UserRole implements Serializable {

    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected UserRolePK userRolePK;

    public UserRole() {
    }

    public UserRole(UserRolePK userRolePK) {
        this.userRolePK = userRolePK;
    }

    public UserRole(int userId, int roleId) {
        this.userRolePK = new UserRolePK(userId, roleId);
    }

    public UserRolePK getUserRolePK() {
        return userRolePK;
    }

    public void setUserRolePK(UserRolePK userRolePK) {
        this.userRolePK = userRolePK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userRolePK != null ? userRolePK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserRole)) {
            return false;
        }
        UserRole other = (UserRole) object;
        if ((this.userRolePK == null && other.userRolePK != null) || (this.userRolePK != null && !this.userRolePK.equals(other.userRolePK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.telecomunica.portalweb.model.UserRole[ userRolePK=" + userRolePK + " ]";
    }
    
}
