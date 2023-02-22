package ua.rudniev.taxi.dao.user;

import ua.rudniev.taxi.model.trip.TripOrder;
import ua.rudniev.taxi.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findUser(String login, String password);
    void createUser(User user, String password);
    void updateUser(User user, String login);
    void updatePassword (User user, String oldPassword, String newPassword);
}
