package com.telecomunica.portalweb;

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
    callerQuery = "#{'select password from user where name = ?'}",
    groupsQuery = "select r.name from user_role ur inner join user u on u.id = ur.user_id inner join role r on r.id = ur.role_id where u.name = ?",
    hashAlgorithm = Pbkdf2PasswordHash.class,
    priorityExpression = "#{100}",
    hashAlgorithmParameters = {
        "Pbkdf2PasswordHash.Iterations=3072",
        "${webPagesConfig.dyna}"
    }
)

@BasicAuthenticationMechanismDefinition(
        realmName = "file"
)

@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/login.xhtml",
                useForwardToLogin = false
            )
)
@FacesConfig
@ApplicationScoped
@Named
public class WebPagesConfig {
    public String[] getDyna() {
         return new String[]{"Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512", "Pbkdf2PasswordHash.SaltSizeBytes=64"};
     }
}

