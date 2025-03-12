package ua.iib.ldapspring.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private String name;
    private String email;
    private String login;
    private String department;
    private String workTitle;
    private String distinguishedName;

}
