package ua.rudniev.taxi.model.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

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

    public String rolesToString() {
        StringBuilder stringBuilder = new StringBuilder();
        roles.forEach(r -> stringBuilder.append(r.name()).append(","));
        stringBuilder.setLength(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public void addRole(Role role) {
        roles.add(role);
    }
}
