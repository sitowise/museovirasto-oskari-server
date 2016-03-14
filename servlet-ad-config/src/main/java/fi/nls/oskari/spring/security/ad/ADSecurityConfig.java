package fi.nls.oskari.spring.security.ad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.ldap.authentication.AbstractLdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.spring.EnvHelper;
import fi.nls.oskari.spring.security.OskariLoginFailureHandler;
import fi.nls.oskari.util.PropertyUtil;

@Profile(EnvHelper.PROFILE_LOGIN_AD)
@Configuration
@EnableWebSecurity
public class ADSecurityConfig extends WebSecurityConfigurerAdapter {

    private Logger log = LogFactory.getLogger(ADSecurityConfig.class);
    private String domain;
    private String url;

    @Autowired
    private EnvHelper env;

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

        authenticationProvider
                .setUserDetailsContextMapper(userDetailsContextMapper);

        http.authenticationProvider(authenticationProvider);

        http.csrf().disable();
        http.headers().frameOptions().disable();

        final String loginurl = env.getLoginUrl();
        http
                // IMPORTANT! Only antMatch for processing url, otherwise SAML
                // security filters are passed even if both are active
                .antMatcher(loginurl).formLogin().loginProcessingUrl(loginurl)
                .passwordParameter(env.getParam_password())
                .usernameParameter(env.getParam_username())
                .failureHandler(
                        new OskariLoginFailureHandler("/?loginState=failed"))
                .successHandler(new AuthenticationSuccessHandler())
                .loginPage("/");
    }
}