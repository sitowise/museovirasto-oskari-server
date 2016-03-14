package fi.nls.oskari.spring.security.ad;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;

import fi.nls.oskari.domain.Role;
import fi.nls.oskari.domain.User;
import fi.nls.oskari.log.LogFactory;
import fi.nls.oskari.log.Logger;

/**
 * Maps Oskari user roles based on AD roles. Example
 * configuration: 
 * - oskari.ad.mapper=fi.nls.oskari.spring.security.ad.SimpleAttributeRoleMapper
 * - oskari.ad.mapper.role.User=*
 * - oskari.ad.mapper.role.Admin=Administrators, Domain Admins
 *
 * The above would map role named 'User' to all users (attribute value * means
 * any value) and role named 'Admin' to users where AD roles contains a
 * value of either 'Administrators' or 'Domain Admins'
 */
public class SimpleADRoleMapper extends SimpleRoleMapper {

    private Logger log = LogFactory.getLogger(SimpleADRoleMapper.class);

    /**
     * Initializes the role mapping based on properties
     */
    public void init() {
        super.init();
    }

    /**
     * Clears any previously assigned roles and maps new ones based on role
     * mapping
     */
    public void mapUser(Collection<? extends GrantedAuthority> authority,
            User user) throws Exception {
        super.mapUser(authority, user);
        Set<Role> roles = new HashSet<>();
        for (GrantedAuthority granted : authority) {
            final String value = granted.getAuthority();
            roles.addAll(getRolesForValue(value));
        }
        for (Role role : roles) {
            user.addRole(role);
        }

    }
}
