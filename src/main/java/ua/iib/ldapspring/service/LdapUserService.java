package ua.iib.ldapspring.service;

import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.stereotype.Service;
import ua.iib.ldapspring.dto.UserDto;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import java.util.Comparator;
import java.util.List;

@Service
public class LdapUserService {

    private final LdapTemplate ldapTemplate;

    public LdapUserService(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }


    public List<UserDto> getAllActiveUsersFromAD() {

        List<UserDto> search = ldapTemplate.search(
                "",
                "(&(objectclass=User)(sAMAccountName=*)(!(objectClass=computer))(mail=*)(lastlogon=*)(!(userAccountControl=514)))",
                new SearchControls(SearchControls.SUBTREE_SCOPE, 0, 0, null, false, false),
                new UserAttributesMapper());

        return search.stream()
                .sorted(Comparator.comparing(UserDto::getName))
                .toList();

    }

    private static class UserAttributesMapper implements AttributesMapper<UserDto> {
        @Override
        public UserDto mapFromAttributes(Attributes attributes) throws NamingException {

            String name = attributes.get("cn") != null ? (String) attributes.get("cn").get() : "N/A";
            String email = attributes.get("mail") != null ? (String) attributes.get("mail").get() : "N/A";
            String login = attributes.get("sAMAccountName") != null ? String.valueOf(attributes.get("sAMAccountName").get()) : "N/A";

            return new UserDto(name, email, login);
        }
    }
}

