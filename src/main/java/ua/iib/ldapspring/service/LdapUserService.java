package ua.iib.ldapspring.service;

import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;
import ua.iib.ldapspring.dto.UserDto;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import java.util.List;

@Service
public class LdapUserService {

    private final LdapTemplate ldapTemplate;


    public LdapUserService(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public List<UserDto> getAllUsers() {
        LdapQuery query = LdapQueryBuilder.query()
                .where("objectClass").is("person");

        return ldapTemplate.search(query, new UserAttributesMapper());
    }

    private static class UserAttributesMapper implements AttributesMapper<UserDto> {
        @Override
        public UserDto mapFromAttributes(Attributes attributes) throws NamingException {
            String name = attributes.get("cn") != null ? (String) attributes.get("cn").get() : "N/A";
            String email = attributes.get("mail") != null ? (String) attributes.get("mail").get() : "N/A";
            return new UserDto(name, email);
        }
    }

    private UserDto mapToUserDto(Attributes attributes) {
        try {
            return new UserDto(
                    attributes.get("cn").get().toString(), // Имя
                    attributes.get("mail") != null ? attributes.get("mail").get().toString() : "N/A" // Email
            );
        } catch (NamingException e) {
            throw new RuntimeException("Ошибка при обработке пользователя AD", e);
        }
    }
}