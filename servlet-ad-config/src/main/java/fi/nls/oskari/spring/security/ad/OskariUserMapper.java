package fi.nls.oskari.spring.security.ad;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import fi.nls.oskari.domain.User;

/**
 * Handler interface for mapping users from AD credentials. Implement your own
 * AD to Oskari user mapping by implementing mapUser(). Configured to be used in
 * oskari-ext.properties: 
 * - oskari.ad.mapper=[fqcn for class implementing OskariUserMapper]
 */
public interface OskariUserMapper {
    public void mapUser(Collection<? extends GrantedAuthority> authority,
            User prepopulatedUser) throws Exception;
}
