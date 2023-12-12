/**
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * https://github.com/javaee/tutorial-examples/LICENSE.txt
 */

package com.telecomunica.portalweb.rest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.sql.DataSource;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

@Singleton
@Startup
public class DatabaseSetup {

    @Resource(lookup="java:app/DefaultDataSource")	
    private DataSource dataSource;

    @Inject
    private Pbkdf2PasswordHash passwordHash;
    
    @PostConstruct
    public void init() {
        
        Map<String, String> parameters= new HashMap<>();
        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
        passwordHash.initialize(parameters);
        
        executeUpdate(dataSource, "CREATE TABLE IF NOT EXISTS USER (ID INTEGER not null primary key, NAME VARCHAR(50) not null, PASSWORD VARCHAR(255) not null)");
        executeUpdate(dataSource, "CREATE TABLE IF NOT EXISTS ROLE (ID INTEGER not null primary key, NAME VARCHAR(50) not null)");
        executeUpdate(dataSource, "CREATE TABLE IF NOT EXISTS USER_ROLE(USER_ID INTEGER not null, ROLE_ID INTEGER not null)");
        executeUpdate(dataSource, "ALTER TABLE USER_ROLE ADD CONSTRAINT USER_ID_FK FOREIGN KEY (USER_ID) REFERENCES USER (ID)");
        executeUpdate(dataSource, "ALTER TABLE USER_ROLE ADD CONSTRAINT ROLE_ID_FK FOREIGN KEY (ROLE_ID) REFERENCES ROLE (ID)");
        executeUpdate(dataSource, "ALTER TABLE USER_ROLE ADD PRIMARY KEY (USER_ID, ROLE_ID)");
        executeUpdate(dataSource, "CREATE UNIQUE INDEX USER_NAME_IDX ON USER (NAME)");
        executeUpdate(dataSource, "CREATE UNIQUE INDEX ROLE_NAME_IDX ON ROLE (NAME)");
        
        
        executeUpdate(dataSource, "INSERT INTO USER VALUES(1, 'Joe', '" + passwordHash.generate("secret".toCharArray()) + "')");
        executeUpdate(dataSource, "INSERT INTO USER VALUES(2, 'Sam', '" + passwordHash.generate("secret".toCharArray()) + "')");
        executeUpdate(dataSource, "INSERT INTO USER VALUES(3, 'Tom', '" + passwordHash.generate("secret".toCharArray()) + "')");
        executeUpdate(dataSource, "INSERT INTO USER VALUES(4, 'Sue', '" + passwordHash.generate("secret".toCharArray()) + "')");
        executeUpdate(dataSource, "INSERT INTO USER VALUES(5, 'developer', '" + passwordHash.generate("secret".toCharArray()) + "')");
        
        executeUpdate(dataSource, "INSERT INTO ROLE VALUES(1, 'USER_ROLE')");
        executeUpdate(dataSource, "INSERT INTO ROLE VALUES(2, 'USER_ADMIN')");
        executeUpdate(dataSource, "INSERT INTO ROLE VALUES(3, 'FOO')");
        
        
        //map user_roles
        executeUpdate(dataSource, "INSERT INTO USER_ROLE VALUES(1, 1)");
        executeUpdate(dataSource, "INSERT INTO USER_ROLE VALUES(2, 2)");
        executeUpdate(dataSource, "INSERT INTO USER_ROLE VALUES(3, 3)");
        executeUpdate(dataSource, "INSERT INTO USER_ROLE VALUES(5, 2)");
        
        
    }
    
    public String generateHash(String password){
        return passwordHash.generate(password.toCharArray());
    }

    /**
     * Drops the tables before instance is removed by the container.
     */
    @PreDestroy
    public void destroy() {
    	try {
    		executeUpdate(dataSource, "DROP TABLE USER_ROLE");
    		executeUpdate(dataSource, "DROP TABLE USERS");
                executeUpdate(dataSource, "DROP TABLE ROLES");
    	} catch (Exception e) {
    		
    	}
    }

    /*
    Executes the SQL statement in this PreparedStatement object against the database it is pointing to.
     */
    private void executeUpdate(DataSource dataSource, String query) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.executeUpdate();
            }
        } catch (SQLException e) {
           throw new IllegalStateException(e);
        }
    }
    
}
