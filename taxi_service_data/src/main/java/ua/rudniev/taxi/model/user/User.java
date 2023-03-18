package ua.rudniev.taxi.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * This class has fields and methods that describes User object
 */
@Data
@NoArgsConstructor
public class User {

    private String login;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private final Set<Role> roles = new HashSet<>();
    private int discount;

    public User(String login, String firstname, String lastname, String phone, String email, int discount) {
        this.login = login;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
        this.discount = discount;
    }


    /**
     * This method builds a string with user's roles
     * @return String with roles list
     */
    public String rolesToString() {
        StringBuilder stringBuilder = new StringBuilder();
        roles.forEach(r -> stringBuilder.append(r.name()).append(","));
        stringBuilder.setLength(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    /**
     * This method add role to user's roles list
     * @param role This parameter indicates role that will be added to the list
     */
    public void addRole(Role role) {
        roles.add(role);
    }
}
