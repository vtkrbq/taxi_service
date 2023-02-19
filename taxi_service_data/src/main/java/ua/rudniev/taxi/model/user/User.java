package ua.rudniev.taxi.model.user;

import java.util.HashSet;
import java.util.Set;

public class User {

    private String login;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private final Set<Role> roles = new HashSet<>();

    public User(String login, String firstname, String lastname, String phone, String email) {
        this.login = login;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.email = email;
    }


    public User() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public Set<Role> getRoles() {
        return roles;
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
