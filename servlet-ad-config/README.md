# AD support for oskari-server/servlet-map

Adds AD-security module for the servlet.


Additional properties for configuration for AD login (no additional configuration is needed for DB login):
-------------------------------------------------------------------
    ###################################
    # Login profiles/configurations
    ###################################
    # Comma-separated list of spring profiles to use
    # Basic auth profile is 'LoginDatabase' which uses database tables to authenticate.
    # To disable login option remove it from 'oskari.profiles'
    oskari.profiles=LoginAD

    oskari.ad.domain=sitois.local
    oskari.ad.url=ldap://10.50.50.3 ldap://10.50.50.4

    # mapping from AD attributes
    oskari.ad.credential.firstname = givenName
    oskari.ad.credential.lastname = sn
    oskari.ad.credential.email = mail
-------------------------------------------------------------------