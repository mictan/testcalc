package model.database.dao;

import model.data.User;

import java.util.Collection;

public interface UserDAO {
    void add(User user);
    void update(User user);
    void delete(User user);
    User getByName(String userName);
    Collection<User> getAll();
}
