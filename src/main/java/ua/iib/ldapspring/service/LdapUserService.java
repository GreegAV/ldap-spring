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
                "(&" +
                        "(objectCategory=person)" +
                        "(objectClass=user)" +
                        "(mail=*)" +
                        "(department=*)" +
                        "((userAccountControl:1.2.840.113556.1.4.803:=2))" +
                        ")",
                new SearchControls(SearchControls.SUBTREE_SCOPE, 0, 0, null, false, false),
                new UserAttributesMapper());

        return search.stream()
                .filter(userDto -> !userDto.getDistinguishedName().contains("OU=Branches"))
                .sorted(Comparator.comparing(UserDto::getName))
                .toList();

    }

    private static class UserAttributesMapper implements AttributesMapper<UserDto> {
        @Override
        public UserDto mapFromAttributes(Attributes attributes) throws NamingException {

            String name = attributes.get("cn") != null ? (String) attributes.get("cn").get() : "N/A";
            String email = attributes.get("mail") != null ? (String) attributes.get("mail").get() : "N/A";
            String login = attributes.get("sAMAccountName") != null ? String.valueOf(attributes.get("sAMAccountName").get()) : "N/A";
            String department = attributes.get("department") != null ? (String) attributes.get("department").get() : "N/A";
            String workTitle = attributes.get("description") != null ? (String) attributes.get("description").get() : "N/A";
            String distinguishedName = attributes.get("distinguishedName") != null ? (String) attributes.get("distinguishedName").get() : "N/A";
            return new UserDto(name, email, login, department, workTitle, distinguishedName);
        }
    }
}

