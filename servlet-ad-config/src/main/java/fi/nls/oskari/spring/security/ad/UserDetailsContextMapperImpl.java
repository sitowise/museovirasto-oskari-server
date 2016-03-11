package fi.nls.oskari.spring.security.ad;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

import fi.nls.oskari.domain.Role;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.service.UserService;
import fi.nls.oskari.spring.security.OskariUserHelper;
import fi.nls.oskari.user.DatabaseUserService;
import fi.nls.oskari.util.PropertyUtil;

class UserDetailsContextMapperImpl
        implements UserDetailsContextMapper, Serializable {

    private static final long serialVersionUID = -7984892256457231051L;
    private Logger log = LogFactory
            .getLogger(UserDetailsContextMapperImpl.class);
    private Map<String, String> attributeMapping = new HashMap<String, String>();

    public enum ATTRIBUTE {
        FIRSTNAME("firstname"),
        LASTNAME("lastname"),
        EMAIL("email");

        private String property;

        ATTRIBUTE(String prop) {
            property = prop;
        }

        public String getKey() {
            return property;
        }
    }

    public UserDetailsContextMapperImpl() {
        for (ATTRIBUTE a : ATTRIBUTE.values()) {
            final String mapping = PropertyUtil
                    .getOptional("oskari.ad.credential." + a.getKey());
            if (mapping != null) {
                attributeMapping.put(a.getKey(), mapping);
            } else {
                attributeMapping.put(a.getKey(), a.getKey());
            }
        }
    }

    @Override
    public UserDetails mapUserFromContext(DirContextOperations ctx,
            String username, Collection<? extends GrantedAuthority> authority) {
        try {
            final fi.nls.oskari.domain.User user = handleUser(ctx, username,
                    authority);
            User userDetails = new User(user.getScreenname(), "N/A", true, true,
                    true, true, OskariUserHelper.getRoles(user.getRoles()));
            return userDetails;
        } catch (Exception ex) {
            log.info(ex, "Error constructing user details");
            throw new UsernameNotFoundException(
                    "Couldn't handle user correctly");
        }
    }

    @Override
    public void mapUserToContext(UserDetails arg0, DirContextAdapter arg1) {
    }

    public fi.nls.oskari.domain.User handleUser(DirContextOperations ctx,
            String username, Collection<? extends GrantedAuthority> authority)
            throws Exception {
        // load Oskari user from UserService to determine roles
        final DatabaseUserService service = getUserService();

        final fi.nls.oskari.domain.User user = new fi.nls.oskari.domain.User();

        user.setFirstname(ctx.getStringAttribute(
                attributeMapping.get(ATTRIBUTE.FIRSTNAME.getKey())));
        user.setLastname(ctx.getStringAttribute(
                attributeMapping.get(ATTRIBUTE.LASTNAME.getKey())));
        user.setEmail(ctx.getStringAttribute(
                attributeMapping.get(ATTRIBUTE.EMAIL.getKey())));

        user.setScreenname(username);
        user.addRole(Role.getDefaultUserRole());

        // TODO: Add proper configurable role mapping
        for (GrantedAuthority granted : authority) {
            if (granted.getAuthority()
                    .equalsIgnoreCase("Sito Software Engineers")) {
                user.addRole(Role.getAdminRole());
            }
        }

        log.debug("Saving user:", user, "with roles:", user.getRoles());
        final fi.nls.oskari.domain.User savedUser = service
                .getUser(service.saveUser(user).getScreenname());
        log.debug("User saved as:", savedUser, "with roles:",
                savedUser.getRoles());
        return savedUser;
    }

    private DatabaseUserService getUserService()
            throws UsernameNotFoundException {
        try {
            return (DatabaseUserService) UserService.getInstance();
        } catch (Exception ex) {
            throw new UsernameNotFoundException(
                    "Couldn't get UserService or it's not assignable to "
                            + DatabaseUserService.class.getName());
        }
    }
}
