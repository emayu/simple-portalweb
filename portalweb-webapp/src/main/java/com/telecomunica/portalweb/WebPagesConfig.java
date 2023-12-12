package com.telecomunica.portalweb;

import com.telecomunica.portalweb.model.Role;
import com.telecomunica.portalweb.util.JsfUtil;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.annotation.FacesConfig;
import javax.inject.Named;
import javax.security.enterprise.authentication.mechanism.http.CustomFormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

// Database Definition for built-in DatabaseIdentityStore
//@DatabaseIdentityStoreDefinition(
//    dataSourceLookup = "java:app/DefaultDataSource",
//    callerQuery = "#{'select password from USER where name = ?'}",
//    groupsQuery = "select r.name from USER_ROLE ur inner join USER u on u.id = ur.user_id inner join ROLE r on r.id = ur.role_id where u.name = ?",
//    hashAlgorithm = Pbkdf2PasswordHash.class,
//    priorityExpression = "#{100}",
//    hashAlgorithmParameters = {
//        "Pbkdf2PasswordHash.Iterations=3072",
//        "${webPagesConfig.dyna}"
//    }
//)

//@BasicAuthenticationMechanismDefinition(
//        realmName = "file"
//)

@CustomFormAuthenticationMechanismDefinition(
        loginToContinue = @LoginToContinue(
                loginPage = "/login.xhtml",
                useForwardToLogin = true,
                errorPage = "/error.html"
            )
)
@FacesConfig
@ApplicationScoped
@Named
public class WebPagesConfig implements IdentityStore {

    private static final Logger LOG = Logger.getLogger(WebPagesConfig.class.getName());
    private final ResourceBundle bundle = ResourceBundle.getBundle("Bundle");

    public String[] getDyna() {
        return new String[]{"Pbkdf2PasswordHash.Algorithm=PBKDF2WithHmacSHA512", "Pbkdf2PasswordHash.SaltSizeBytes=64"};
    }

    public CredentialValidationResult validate(UsernamePasswordCredential credential) {
        Client client = ClientBuilder.newClient();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(credential.getCaller(), credential.getPasswordAsString());
        client.register(feature);
        WebTarget target = client.target(getGlobalBaseURLClientTarget() + "/userrole/");
        LOG.info(()-> "target URL:" + target.getUri().toString());
        Response response = target
                .path("byUserName/{userName}")
                .resolveTemplate("userName", credential.getCaller())
                .request()
                .get();
        if (response.getStatusInfo().toEnum() == Response.Status.OK) {
            LOG.info("reponse OK");
            Role[] roles = response.readEntity(Role[].class);
            if (roles != null && roles.length > 0) {
                String[] nameRolesArray = Arrays.stream(roles).map(r -> r.getName()).toArray(String[]::new);
                Set<String> nameRolesSet = Set.of(nameRolesArray);

                CredentialValidationResult c = new CredentialValidationResult(credential.getCaller(), nameRolesSet);
                return c;
            }
        }
        if(response.getStatusInfo().toEnum() == Response.Status.UNAUTHORIZED){
            
            JsfUtil.addErrorMessage(bundle.getString("Login.checkYourCredentials"));
        }else if( response.getStatusInfo().toEnum() == Response.Status.FORBIDDEN){
            JsfUtil.addErrorMessage(bundle.getString("Login.Forbidden"));
        }
        LOG.info(() -> "StatusInfo:" + response.getStatusInfo() + " mediaType:" + response.getMediaType());
        return CredentialValidationResult.INVALID_RESULT;
        
//        Set<String> nameRolesSet = Set.of("USER_ADMIN");
//
//        CredentialValidationResult c = new CredentialValidationResult(credential.getCaller(), nameRolesSet);
//        return c;
    }
    
    public static String getGlobalBaseURLClientTarget(){
        final String URL_BASE = System.getenv("URL_BASE");
        return URL_BASE != null && !URL_BASE.isBlank()? URL_BASE : "http://localhost:8080/portalweb-service/rest/v1";
    }

}
