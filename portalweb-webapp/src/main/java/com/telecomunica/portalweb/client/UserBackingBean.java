/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.telecomunica.portalweb.client;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author f32
 */
@Named
@SessionScoped
public class UserBackingBean implements Serializable{
    
    private int id;
    private Integer oldRole;
    private int newRole;
    private String userName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getOldRole() {
        return oldRole;
    }

    public void setOldRole(Integer oldRole) {
        this.oldRole = oldRole;
    }

    public int getNewRole() {
        return newRole;
    }

    public void setNewRole(int newRole) {
        this.newRole = newRole;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
}
