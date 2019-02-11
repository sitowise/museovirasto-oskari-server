package fi.nls.oskari.spring.security.ad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.spring.SpringEnvHelper;
import fi.nls.oskari.spring.security.OskariLoginFailureHandler;
import fi.nls.oskari.util.PropertyUtil;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Profile(SpringEnvHelper.PROFILE_LOGIN_AD)
@Configuration
@EnableWebSecurity
@Order(2)
public class ADSecurityConfig extends WebSecurityConfigurerAdapter {

    private Logger log = LogFactory.getLogger(ADSecurityConfig.class);
    private String domain;
    private String url;

    @Autowired
    private SpringEnvHelper env;

    public ADSecurityConfig() {
        this.domain = PropertyUtil.get("oskari.ad.domain");
        this.url = PropertyUtil.get("oskari.ad.url");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.info("Configuring AD login. Domain:", this.domain, "Url:", this.url);

        AbstractLdapAuthenticationProvider authenticationProvider = new ActiveDirectoryLdapAuthenticationProvider(
                this.domain, this.url);

        UserDetailsContextMapper userDetailsContextMapper = new UserDetailsContextMapperImpl();
        authenticationProvider.setUserDetailsContextMapper(userDetailsContextMapper);

        http.authenticationProvider(authenticationProvider);

        http.headers().frameOptions().disable();

        // require form parameter "_csrf" OR "X-XSRF-TOKEN" header with token as value or respond with an error message
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());

        // IMPORTANT! Only antMatch for processing url, otherwise SAML security filters are passed even if both are active
        http.formLogin()
                .loginProcessingUrl(env.getLoginUrl())
                .passwordParameter(env.getParam_password())
                .usernameParameter(env.getParam_username())
                .failureHandler(new OskariLoginFailureHandler("/?loginState=failed"))
                .successHandler(new AuthenticationSuccessHandler())
                .loginPage("/");

    }
}