package ua.rudniev.taxi.dao.user;

import ua.rudniev.taxi.model.user.User;

import java.util.Optional;

/**
 * Interface class that has methods for working with repositories
 */
public interface UserDao {
    /**
     * This method searching for user in repository with indicated login
     *
     * @param login This parameter is indicating which user will be returned
     * @return Optional of User with indicated login
     */
    Optional<User> findUser(String login);

    /**
     * This method authenticates user
     * @param login This parameter indicates user login
     * @param password This parameter indicates user password
     * @return Optional of User with indicated login and password
     */
    Optional<User> findUserByLoginAndPassword(String login, String password);

    /**
     * This method creates car in repository
     * @param user This parameter contains data about created user
     * @param password This parameter has a password of created user
     */
    void createUser(User user, String password);

    /**
     * This method updates user in repository
     * @param user This parameter contains data about created user
     * @param login This parameter contains login of user which has to be updated
     */
    void updateUser(User user, String login);

    /**
     *  This method change password of user
     * @param user This parameter indicates which user's passwords has to be changed
     * @param oldPassword This parameter has old password
     * @param newPassword This parameter has new password
     */
    void updatePassword (User user, String oldPassword, String newPassword);
}
