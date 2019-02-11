package fi.nls.oskari.spring.security.ad;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;



import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;
import fi.nls.oskari.service.UserService;
import fi.nls.oskari.spring.security.OskariUserHelper;
import fi.nls.oskari.user.DatabaseUserService;
import fi.nls.oskari.util.PropertyUtil;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

class UserDetailsContextMapperImpl
        implements UserDetailsContextMapper, Serializable {

    private static final long serialVersionUID = -7984892256457231051L;
    private Logger log = LogFactory
            .getLogger(UserDetailsContextMapperImpl.class);

    private OskariUserMapper userMapper = null;
    private Map<String, String> attributeMapping = new HashMap<String, String>();

    public enum ATTRIBUTE {
        FIRSTNAME("firstname"), LASTNAME("lastname"), EMAIL("email");

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
        userMapper = getUserMapper();
    }

    private OskariUserMapper getUserMapper() {
        final String mapperClassName = PropertyUtil
                .getOptional("oskari.ad.mapper");
        if (mapperClassName == null) {
            // no mapper specified
            return null;
        }
        try {
            final Class<?> clazz = Class.forName(mapperClassName.trim());
            return (OskariUserMapper) clazz.newInstance();
        } catch (Exception e) {
            log.error(e, "Error loading AD user mapper from classname:",
                    mapperClassName);
        }
        return null;
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

        if (userMapper != null) {
            userMapper.mapUser(authority, user);
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
