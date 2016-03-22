package fi.nls.oskari.spring.security.ad;

import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.spring.security.OskariUserHelper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Hooks in OskariUserHelper.onAuthenticationSuccess(). Extends different Spring class than the similar class
 * in SAML package.
 */
public class AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private Logger log = LogFactory.getLogger(AuthenticationSuccessHandler.class);
    private OskariUserHelper helper = new OskariUserHelper();

    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, javax.servlet.ServletException {
        log.debug("AD based login successful:", authentication.getPrincipal());
        super.onAuthenticationSuccess(request, response, authentication);
        helper.onAuthenticationSuccess(request, response, ((User)authentication.getPrincipal()).getUsername());
    }
}
