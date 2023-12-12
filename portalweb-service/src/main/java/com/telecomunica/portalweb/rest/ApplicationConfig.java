/**
 * Copyright (c) 2017 Oracle and/or its affiliates. All rights reserved.
 *
 * You may not modify, use, reproduce, or distribute this software except in
 * compliance with  the terms of the License at:
 * https://github.com/javaee/tutorial-examples/LICENSE.txt
 */
package com.telecomunica.portalweb.rest;

import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;
import javax.inject.Named;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;
import javax.security.enterprise.identitystore.Pbkdf2PasswordHash;

// Database Definition for built-in DatabaseIdentityStore
@DatabaseIdentityStoreDefinition(
    dataSourceLookup = "java:app/DefaultDataSource",
    callerQuery = "#{'select password from USER where name = ?'}",
    groupsQuery = "select r.name from USER_ROLE ur inner join USER u on u.id = ur.user_id inner join ROLE r on r.id = ur.role_id where u.name = ?",
    hashAlgorithm = Pbkdf2PasswordHash.class,
    priorityExpression = "#{100}",
    hashAlgorithmParameters = {
        "Pbkdf2PasswordHash.Iterations=3072",
        "${applicationConfig.dyna}"
    }
)

@BasicAuthenticationMechanismDefinition(
        realmName = "file"
)
@ApplicationScoped
@Named
public class ApplicationConfig {

     public String[] getDyna() {
         return new String[]{"Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512", "Pbkdf2PasswordHash.SaltSizeBytes=64"};
     }
    
}