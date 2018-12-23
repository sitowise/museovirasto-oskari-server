package fi.nls.oskari.spring;

import fi.nls.oskari.util.EnvHelper;
import fi.nls.oskari.util.PropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: SMAKINEN
 * Date: 5.11.2014
 * Time: 12:54
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SpringEnvHelper {

    public static final String PROFILE_LOGIN_DB = "LoginDatabase";
    public static final String PROFILE_LOGIN_SAML = "LoginSAML";
    public static final String PROFILE_LOGIN_AD = "LoginAD";

    // login related properties
    private boolean handleLoginForm;
    private String loginUrlSAML = "/saml/login";
    private String loggedOutPage;
    private String param_username;
    private String param_password;
    private String logoutUrl;
    private String registerUrl;

    private String mapUrl;

    @Autowired
    private Environment springEnvironment;

    public SpringEnvHelper() {
        mapUrl = PropertyUtil.get("oskari.map.url", "/");
        // login related properties
        logoutUrl = PropertyUtil.get("auth.logout.url", "/logout");
        loggedOutPage = PropertyUtil.get("auth.loggedout.page", PropertyUtil.get("oskari.map.url", "/"));
        handleLoginForm = PropertyUtil.getOptional("oskari.request.handleLoginForm", true);
        param_username = PropertyUtil.get("auth.login.field.user", "j_username");
        param_password = PropertyUtil.get("auth.login.field.pass", "j_password");
        registerUrl = PropertyUtil.get("auth.register.url", "");
    }

    public boolean isSAMLEnabled() {
        return springEnvironment.acceptsProfiles(PROFILE_LOGIN_SAML);
    }
    public boolean isDBLoginEnabled() {
        return springEnvironment.acceptsProfiles(PROFILE_LOGIN_DB);
    }
    public boolean isADLoginEnabled() {
        return springEnvironment.acceptsProfiles(PROFILE_LOGIN_AD);
    }

    public boolean isHandleLoginForm() {
        return handleLoginForm;
    }


    public String getLoginUrlSAML() {
        return loginUrlSAML;
    }


    public String getLoggedOutPage() {
        return loggedOutPage;
    }

    public String getParam_username() {
        return param_username;
    }

    public String getParam_password() {
        return param_password;
    }

    public String getMapUrl() {
        return mapUrl;
    }

    public String getLoginUrl() {
        String url = EnvHelper.getLoginUrl();
        if(url != null && !url.isEmpty()) {
            return url;
        }
        return "/j_security_check";
    }
    public boolean isRegistrationAllowed() {
        return EnvHelper.isRegistrationAllowed();
    }
    public String getRegisterUrl() {
        return EnvHelper.getRegisterUrl();
    }
    public String getLogoutUrl() {
        return EnvHelper.getLogoutUrl();
    }
}
